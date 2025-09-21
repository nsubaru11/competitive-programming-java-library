import sun.misc.Unsafe;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.lang.reflect.Field;
import java.util.Locale;


import static java.lang.Math.min;
import static java.lang.Math.max;

/**
 * 競技プログラミング向けの高速出力クラスです。<br>
 * ※注意: 内部バッファが満杯になると自動的に指定の OutputStream に書き出します。<br>
 * 処理途中で結果をすぐに反映させる必要がある場合は、autoFlush を true にするか、明示的に {@link #flush()} を呼び出してください。
 * ASCII範囲外の文字は取り扱えません。
 */
@SuppressWarnings("unused")
public class FastPrinter implements AutoCloseable {

	/* ------------------------ 定数 ------------------------ */

	/**
	 * int 型の値を文字列に変換した際に必要となる最大桁数（符号込み）<br>
	 * 例: Integer.MIN_VALUE は "-2147483648"（11バイト）
	 */
	protected static final int MAX_INT_DIGITS = 11;

	/**
	 * long 型の値を文字列に変換した際に必要となる最大桁数（符号込み）<br>
	 * 例: Long.MIN_VALUE は "-9223372036854775808"（20バイト）
	 */
	protected static final int MAX_LONG_DIGITS = 20;

	/**
	 * 10のべき乗の配列です。POW10[i] は 10^i を表します。
	 */
	protected static final long[] POW10 = {
			1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000,
			1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L,
			10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L,
			10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L
	};

	/**
	 * 出力用内部バッファのデフォルトサイズ（バイト単位）<br>
	 * ※64バイト未満の場合、内部的に64バイトに調整されます。
	 */
	private static final int DEFAULT_BUFFER_SIZE = 65536;

	/**
	 * 00～99 の2桁の数字の2桁目を格納
	 */
	private static final byte[] DigitTens = {
			'0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
			'1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
			'2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
			'3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
			'4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
			'5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
			'6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
			'7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
			'8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
			'9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
	};

	/**
	 * 00～99 の1桁の数字の2桁目を格納
	 */
	private static final byte[] DigitOnes = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	};

	/**
	 * true は "Yes"、 false は "No" の配列です。
	 */
	private static final byte[] TRUE_BYTES = {'Y', 'e', 's'};

	/**
	 * true は "Yes"、 false は "No" の配列です。
	 */
	private static final byte[] FALSE_BYTES = {'N', 'o'};

	/**
	 * Unsafe を用いて、バイト配列の内容を直接書き換えるためのユーティリティメソッドです。
	 */
	private static final Unsafe UNSAFE;

	/**
	 * String の value フィールドのオフセット値です。
	 */
	private static final long STRING_VALUE_OFFSET;

	/**
	 * AbstractStringBuilder の value フィールドのオフセット値です。
	 */
	private static final long ABSTRACT_STRING_BUILDER_VALUE_OFFSET;

	/* ------------------------ 初期化処理 ------------------------ */

	static {
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			UNSAFE = (Unsafe) f.get(null);
			STRING_VALUE_OFFSET = UNSAFE.objectFieldOffset(String.class.getDeclaredField("value"));
			Class<?> asbClass = Class.forName("java.lang.AbstractStringBuilder");
			ABSTRACT_STRING_BUILDER_VALUE_OFFSET = UNSAFE.objectFieldOffset(asbClass.getDeclaredField("value"));
		} catch (Exception e) {
			throw new RuntimeException("Unsafe initialization failed. Check Java version and environment.", e);
		}
	}


	/* ------------------------ インスタンス変数 ------------------------ */

	/**
	 * 出力先の内部バッファです。書き込みはこの配列に対して行い、必要に応じて {@link #flush()} で出力します。
	 */
	protected final byte[] buffer;

	/**
	 * 出力後に自動的にバッファを flush するかどうかを示すフラグです。<br>
	 * true の場合、各出力操作後に自動的に {@link #flush()} が呼ばれます。
	 */
	protected final boolean autoFlush;

	/**
	 * 内部バッファの容量です。64 バイト未満の場合は 64 バイトに調整されます。
	 */
	protected final int BUFFER_SIZE;

	/**
	 * 出力先の OutputStream です。デフォルトは {@code System.out} です。
	 */
	private final OutputStream out;

	/**
	 * 現在のバッファ内での書き込み位置
	 */
	protected int pos = 0;

	/* ------------------------ コンストラクタ ------------------------ */

	/**
	 * デフォルトの設定でFastPrinterを初期化します。<br>
	 * バッファ容量: 65536 <br>
	 * OutputStream: System.out <br>
	 * autoFlush: false
	 */
	public FastPrinter() {
		this(System.out, DEFAULT_BUFFER_SIZE, false);
	}

	/**
	 * 指定されたOutputStreamを用いてFastPrinterを初期化します。<br>
	 * バッファ容量: 65536 <br>
	 * autoFlush: false
	 *
	 * @param out 出力先の OutputStream
	 */
	public FastPrinter(final OutputStream out) {
		this(out, DEFAULT_BUFFER_SIZE, false);
	}

	/**
	 * 指定されたバッファ容量でFastPrinterを初期化します。<br>
	 * OutputStream: System.out <br>
	 * autoFlush: false
	 *
	 * @param bufferSize 内部バッファの容量（バイト単位）。
	 */
	public FastPrinter(final int bufferSize) {
		this(System.out, bufferSize, false);
	}

	/**
	 * autoFlush を指定して FastPrinter を初期化します。<br>
	 * バッファ容量: 65536 <br>
	 * OutputStream: System.out
	 *
	 * @param autoFlush true の場合、各出力操作後に自動的に {@link #flush()} が呼ばれます。
	 */
	public FastPrinter(final boolean autoFlush) {
		this(System.out, DEFAULT_BUFFER_SIZE, autoFlush);
	}

	/**
	 * 指定された OutputStream と autoFlush 設定で FastPrinter を初期化します。<br>
	 * バッファ容量: 65536
	 *
	 * @param out       出力先の OutputStream
	 * @param autoFlush true を指定すると、各出力操作後に自動的に {@link #flush()} が呼ばれ、出力結果が即座に反映されます。
	 */
	public FastPrinter(final OutputStream out, final boolean autoFlush) {
		this(out, DEFAULT_BUFFER_SIZE, autoFlush);
	}

	/**
	 * 指定されたバッファ容量と autoFlush 設定で FastPrinter を初期化します。<br>
	 * OutputStream: System.out
	 *
	 * @param bufferSize 内部バッファの初期容量（バイト単位）。64 バイト未満の場合、内部的に 64 バイトに調整されます。
	 * @param autoFlush  true を指定すると、各出力操作後に自動的に {@link #flush()} が呼ばれ、出力結果が即座に反映されます。
	 */
	public FastPrinter(final int bufferSize, final boolean autoFlush) {
		this(System.out, bufferSize, autoFlush);
	}

	/**
	 * 指定されたバッファ容量と OutputStream で FastPrinter を初期化します。<br>
	 * autoFlush: false
	 *
	 * @param out        出力先の OutputStream
	 * @param bufferSize 内部バッファの初期容量（バイト単位）。64 バイト未満の場合、内部的に 64 バイトに調整されます。
	 */
	public FastPrinter(final OutputStream out, final int bufferSize) {
		this(out, bufferSize, false);
	}

	/**
	 * 指定されたバッファ容量、OutputStream、autoFlush 設定で FastPrinter を初期化します。
	 *
	 * @param out        出力先の OutputStream
	 * @param bufferSize 内部バッファの初期容量（バイト単位）。64 バイト未満の場合、内部的に 64 バイトに調整されます。
	 * @param autoFlush  true を指定すると、各出力操作後に自動的に {@link #flush()} が呼ばれ、出力結果が即座に反映されます。
	 */
	public FastPrinter(final OutputStream out, final int bufferSize, final boolean autoFlush) {
		this.out = out;
		this.BUFFER_SIZE = max(bufferSize, 64);
		this.buffer = new byte[BUFFER_SIZE];
		this.autoFlush = autoFlush;
	}

	/* ------------------------ オーバーライドメソッド ------------------------ */

	/**
	 * {@code flush()}を実行し、このOutputStreamを閉じます。<br>
	 * 出力先が {@code System.out} の場合、閉じません。
	 *
	 * @throws IOException 出力時のエラーが発生した場合
	 */
	@Override
	public void close() throws IOException {
		flush();
		if (out != System.out) out.close();
	}

	/**
	 * 現在のバッファに保持しているすべてのデータを出力し、バッファをクリアします。
	 */
	public void flush() {
		try {
			if (pos > 0) out.write(buffer, 0, pos);
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		pos = 0;
	}

	/* ------------------------ println() 系メソッド ------------------------ */

	/**
	 * 改行のみ出力します。
	 */
	public final FastPrinter println() {
		ensureBufferSpace(1);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
		return this;
	}

	/**
	 * int 値を出力します。（改行付き）
	 *
	 * @param i 出力する int 値
	 */
	public final FastPrinter println(final int i) {
		ensureBufferSpace(MAX_INT_DIGITS + 1);
		fillBuffer(i);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
		return this;
	}

	/**
	 * long 値を出力します。（改行付き）
	 *
	 * @param l 出力する long 値
	 */
	public final FastPrinter println(final long l) {
		ensureBufferSpace(MAX_LONG_DIGITS + 1);
		fillBuffer(l);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
		return this;
	}

	/**
	 * double 値を {@code Double.toString(d)} で文字列化し出力します。（改行付き）
	 *
	 * @param d 出力する double 値
	 */
	public final FastPrinter println(final double d) {
		return println(Double.toString(d));
	}

	/**
	 * char 値を出力します。（改行付き）
	 *
	 * @param c 出力する char 値
	 */
	public final FastPrinter println(final char c) {
		ensureBufferSpace(2);
		buffer[pos++] = (byte) c;
		buffer[pos++] = '\n';
		if (autoFlush) flush();
		return this;
	}

	/**
	 * boolean 値を出力します。（true は "Yes"、 false は "No", 改行付き）
	 *
	 * @param b 出力する boolean 値
	 */
	public final FastPrinter println(final boolean b) {
		ensureBufferSpace(4);
		fillBuffer(b);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
		return this;
	}

	/**
	 * String を出力します。（改行付き）
	 *
	 * @param s 出力する String 値
	 */
	public final FastPrinter println(final String s) {
		final byte[] src = (byte[]) UNSAFE.getObject(s, STRING_VALUE_OFFSET);
		fillBuffer(src, s.length());
		ensureBufferSpace(1);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
		return this;
	}

	/**
	 * StringBuilder を出力します。（改行付き）
	 *
	 * @param s 出力する StringBuilder 値
	 */
	public FastPrinter println(final StringBuilder s) {
		final byte[] src = (byte[]) UNSAFE.getObject(s, ABSTRACT_STRING_BUILDER_VALUE_OFFSET);
		final int len = s.length();
		fillBuffer(src, len);
		ensureBufferSpace(1);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
		return this;
	}

	/**
	 * Object を {@code toString()} で文字列化し出力します。（改行付き）
	 * 要素が String, Long, Integer, Double Boolean Character 型である場合、
	 * その型に沿った出力をします。
	 *
	 * @param o 出力するオブジェクト
	 */
	public FastPrinter println(final Object o) {
		if (o == null) return this;
		if (o instanceof String s) {
			return println(s);
		} else if (o instanceof Long l) {
			return println(l.longValue());
		} else if (o instanceof Integer i) {
			return println(i.intValue());
		} else if (o instanceof Double d) {
			return println(d.toString());
		} else if (o instanceof Boolean b) {
			return println(b.booleanValue());
		} else if (o instanceof Character c) {
			return println(c.charValue());
		} else {
			return println(o.toString());
		}
	}

	/**
	 * BigInteger を {@code toString()} で文字列化し出力します。（改行付き）
	 *
	 * @param bi 出力するオブジェクト
	 */
	public final FastPrinter println(final BigInteger bi) {
		return println(bi.toString());
	}

	/**
	 * BigDecimal を {@code toString()} で文字列化し出力します。（改行付き）
	 *
	 * @param bd 出力するオブジェクト
	 */
	public final FastPrinter println(final BigDecimal bd) {
		return println(bd.toString());
	}

	/* ------------------------ print() 系メソッド ------------------------ */

	/**
	 * int 値を出力します。（改行無し）
	 *
	 * @param i 出力する int 値
	 */
	public final FastPrinter print(final int i) {
		ensureBufferSpace(MAX_INT_DIGITS);
		fillBuffer(i);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * long 値を出力します。（改行無し）
	 *
	 * @param l 出力する long 値
	 */
	public final FastPrinter print(final long l) {
		ensureBufferSpace(MAX_LONG_DIGITS);
		fillBuffer(l);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * double 値を {@code Double.toString(d)} で文字列化し出力します。（改行無し）
	 *
	 * @param d 出力する double 値
	 */
	public final FastPrinter print(final double d) {
		return print(Double.toString(d));
	}

	/**
	 * char 値を出力します。（改行無し）
	 *
	 * @param c 出力する char 値
	 */
	public final FastPrinter print(final char c) {
		ensureBufferSpace(1);
		buffer[pos++] = (byte) c;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * boolean 値を出力します。（true は "Yes"、 false は "No", 改行無し）
	 *
	 * @param b 出力する boolean 値
	 */
	public final FastPrinter print(final boolean b) {
		ensureBufferSpace(3);
		fillBuffer(b);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * String を出力します。（改行無し）
	 *
	 * @param s 出力する String 値
	 */
	public final FastPrinter print(final String s) {
		final byte[] src = (byte[]) UNSAFE.getObject(s, STRING_VALUE_OFFSET);
		fillBuffer(src, s.length());
		if (autoFlush) flush();
		return this;
	}

	/**
	 * StringBuilder を出力します。（改行無し）
	 *
	 * @param s 出力する StringBuilder 値
	 */
	public FastPrinter print(final StringBuilder s) {
		final byte[] src = (byte[]) UNSAFE.getObject(s, ABSTRACT_STRING_BUILDER_VALUE_OFFSET);
		final int len = s.length();
		fillBuffer(src, len);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * Object を {@code toString()} で文字列化し出力します。（改行無し）
	 * 要素が String, Long, Integer, Double Boolean Character 型である場合、
	 * その型に沿った出力をします。
	 *
	 * @param o 出力するオブジェクト
	 */
	public FastPrinter print(final Object o) {
		if (o == null) return this;
		if (o instanceof String s) {
			return print(s);
		} else if (o instanceof Long l) {
			return print(l.longValue());
		} else if (o instanceof Integer i) {
			return print(i.intValue());
		} else if (o instanceof Double d) {
			return print(d.toString());
		} else if (o instanceof Boolean b) {
			return print(b.booleanValue());
		} else if (o instanceof Character c) {
			return print(c.charValue());
		} else {
			return print(o.toString());
		}
	}

	/**
	 * BigInteger を {@code toString()} で文字列化し出力します。（改行無し）
	 *
	 * @param bi 出力するオブジェクト
	 */
	public final FastPrinter print(final BigInteger bi) {
		return print(bi.toString());
	}

	/**
	 * BigDecimal を {@code toString()} で文字列化し出力します。（改行無し）
	 *
	 * @param bd 出力するオブジェクト
	 */
	public final FastPrinter print(final BigDecimal bd) {
		return print(bd.toString());
	}

	/* ------------------------ printf() 系メソッド ------------------------ */

	/**
	 * 指定されたフォーマットに従い文字列を生成して出力します。（改行無し）
	 *
	 * @param format 書式文字列
	 * @param args   書式引数
	 */
	public final FastPrinter printf(final String format, final Object... args) {
		return print(String.format(format, args));
	}

	/**
	 * 指定された言語環境で整形し、フォーマットに従い文字列を生成して出力します。（改行無し）
	 *
	 * @param locale 言語環境
	 * @param format 書式文字列
	 * @param args   書式引数
	 */
	public final FastPrinter printf(final Locale locale, final String format, final Object... args) {
		return print(String.format(locale, format, args));
	}

	/* ------------------------ プライベートヘルパーメソッド ------------------------ */

	/**
	 * 指定されたバイト数のデータを出力するために必要な領域を保証します。<br>
	 * バッファの残り容量が不足している場合、flush() を呼び出してバッファをクリアします。
	 *
	 * @param size 出力予定のデータのバイト数
	 */
	protected final void ensureBufferSpace(final int size) {
		if (pos + size > buffer.length) {
			try {
				out.write(buffer, 0, pos);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			pos = 0;
		}
	}

	/**
	 * 指定された文字列をバッファに格納します。
	 *
	 * @param src 出力するbyte列
	 * @param len 出力するバイト数
	 */
	protected void fillBuffer(final byte[] src, final int len) {
		for (int i = 0; i < len; ) {
			ensureBufferSpace(1);
			int limit = min(BUFFER_SIZE - pos, len - i);
			System.arraycopy(src, i, buffer, pos, limit);
			pos += limit;
			i += limit;
		}
	}

	/**
	 * 指定された boolean 値を文字列 ("Yes" または "No") に変換してバッファに格納します。
	 *
	 * @param b 出力する boolean 値
	 */
	protected final void fillBuffer(final boolean b) {
		if (b) {
			System.arraycopy(TRUE_BYTES, 0, buffer, pos, 3);
			pos += 3;
		} else {
			System.arraycopy(FALSE_BYTES, 0, buffer, pos, 2);
			pos += 2;
		}
	}

	/**
	 * 指定された整数値を文字列に変換してバッファに格納します。<br>
	 * 負の値の場合は先頭に '-' を付加します。
	 *
	 * @param i 出力する整数値
	 */
	protected void fillBuffer(int i) {
		if (i >= 0) i = -i;
		else buffer[pos++] = '-';
		int quotient, remainder;
		final int numOfDigits = countDigits(i);
		int writePos = pos + numOfDigits;
		while (i <= -100) {
			quotient = i / 100;
			remainder = (quotient << 6) + (quotient << 5) + (quotient << 2) - i;
			buffer[--writePos] = DigitOnes[remainder];
			buffer[--writePos] = DigitTens[remainder];
			i = quotient;
		}
		quotient = i / 10;
		remainder = (quotient << 3) + (quotient << 1) - i;
		buffer[--writePos] = (byte) ('0' + remainder);
		if (quotient < 0) buffer[--writePos] = (byte) ('0' - quotient);
		pos += numOfDigits;
	}

	/**
	 * 指定された整数値を文字列に変換してバッファに格納します。<br>
	 * 負の値の場合は先頭に '-' を付加します。
	 *
	 * @param l 出力する整数値
	 */
	protected void fillBuffer(long l) {
		if (l >= 0) l = -l;
		else buffer[pos++] = '-';
		long quotient;
		int remainder;
		final int numOfDigits = countDigits(l);
		int writePos = pos + numOfDigits;
		while (l <= -100) {
			quotient = l / 100;
			remainder = (int) ((quotient << 6) + (quotient << 5) + (quotient << 2) - l);
			buffer[--writePos] = DigitOnes[remainder];
			buffer[--writePos] = DigitTens[remainder];
			l = quotient;
		}
		quotient = l / 10;
		remainder = (int) ((quotient << 3) + (quotient << 1) - l);
		buffer[--writePos] = (byte) ('0' + remainder);
		if (quotient < 0) buffer[--writePos] = (byte) ('0' - quotient);
		pos += numOfDigits;
	}

	/**
	 * 指定された整数値の桁数を数えます。
	 * 与えられる数値は負の整数であることを前提とした実装です。
	 *
	 * @param i 整数
	 * @return 桁数
	 */
	protected int countDigits(int i) {
		if (i > -100000) {
			if (i > -100) {
				return i > -10 ? 1 : 2;
			} else {
				if (i > -10000) return i > -1000 ? 3 : 4;
				else return 5;
			}
		} else {
			if (i > -10000000) {
				return i > -1000000 ? 6 : 7;
			} else {
				if (i > -1000000000) return i > -100000000 ? 8 : 9;
				else return 10;
			}
		}
	}

	/**
	 * 指定された整数値の桁数を数えます。
	 * 与えられる数値は負の整数であることを前提とした実装です。
	 *
	 * @param l 整数
	 * @return 桁数
	 */
	protected int countDigits(long l) {
		if (l > -1000000000) {
			if (l > -10000) {
				if (l > -100) {
					return l > -10 ? 1 : 2;
				} else {
					return l > -1000 ? 3 : 4;
				}
			} else {
				if (l > -1000000) {
					return l > -100000 ? 5 : 6;
				} else {
					if (l > -100000000) return l > -10000000 ? 7 : 8;
					else return 9;
				}
			}
		} else {
			if (l > -10000000000000L) {
				if (l > -100000000000L) {
					return l > -10000000000L ? 10 : 11;
				} else {
					return l > -1000000000000L ? 12 : 13;
				}
			} else {
				if (l > -10000000000000000L) {
					if (l > -1000000000000000L) return l > -100000000000000L ? 14 : 15;
					else return 16;
				} else {
					if (l > -1000000000000000000L) return l > -100000000000000000L ? 17 : 18;
					else return 19;
				}
			}
		}
	}

}
