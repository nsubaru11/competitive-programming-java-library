import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import static java.lang.Math.max;
import static java.lang.Math.min;

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
	 * 出力用内部バッファのデフォルトサイズ（バイト単位）<br>
	 * ※64バイト未満の場合、内部的に64バイトに調整されます。
	 */
	private static final int DEFAULT_BUFFER_SIZE = 65536;

	/**
	 * 00～99 の2桁の数字を連続した1次元配列として格納
	 */
	private static final byte[] TWO_DIGIT_NUMBERS = new byte[200];

	/* ------------------------ 静的イニシャライザ ------------------------ */
	/*
	 * TWO_DIGIT_NUMBERS の初期化を行います。
	 */
	static {
		byte tens = '0', ones = '0';
		for (int i = 0; i < 100; i++) {
			TWO_DIGIT_NUMBERS[i << 1] = tens;
			TWO_DIGIT_NUMBERS[(i << 1) + 1] = ones;
			if (++ones > '9') {
				ones = '0';
				tens++;
			}
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
		buffer = new byte[max(bufferSize, 64)];
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
		if (out != System.out)
			out.close();
	}

	/**
	 * 現在のバッファに保持しているすべてのデータを出力し、バッファをクリアします。
	 */
	public void flush() {
		try {
			if (pos > 0)
				out.write(buffer, 0, pos);
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
	public final void println() {
		ensureBufferSpace(1);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
	}

	/**
	 * int 値を出力します。（改行付き）
	 *
	 * @param i 出力する int 値
	 */
	public final void println(final int i) {
		ensureBufferSpace(MAX_INT_DIGITS + 1);
		fillBuffer(i);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
	}

	/**
	 * long 値を出力します。（改行付き）
	 *
	 * @param l 出力する long 値
	 */
	public final void println(final long l) {
		ensureBufferSpace(MAX_LONG_DIGITS + 1);
		fillBuffer(l);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
	}

	/**
	 * double 値を {@code Double.toString(d)} で文字列化し出力します。（改行付き）
	 *
	 * @param d 出力する double 値
	 */
	public final void println(final double d) {
		print(Double.toString(d), true);
	}

	/**
	 * char 値を出力します。（改行付き）
	 *
	 * @param c 出力する char 値
	 */
	public final void println(final char c) {
		ensureBufferSpace(2);
		buffer[pos++] = (byte) c;
		buffer[pos++] = '\n';
		if (autoFlush) flush();
	}

	/**
	 * boolean 値を出力します。（true は "Yes"、 false は "No", 改行付き）
	 *
	 * @param b 出力する boolean 値
	 */
	public final void println(final boolean b) {
		ensureBufferSpace(4);
		fillBuffer(b);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
	}

	/**
	 * String を出力します。（改行付き）
	 *
	 * @param s 出力する String 値
	 */
	public final void println(final String s) {
		print(s, true);
	}

	/**
	 * Object を {@code toString()} で文字列化し出力します。（改行付き）
	 *
	 * @param o 出力するオブジェクト
	 */
	public final void println(final Object o) {
		if (o == null) return;
		if (o instanceof String s) {
			print(s, true);
		} else if (o instanceof Long l) {
			println(l.longValue());
		} else if (o instanceof Integer i) {
			println(i.intValue());
		} else if (o instanceof Double d) {
			print(d.toString(), true);
		} else if (o instanceof Boolean b) {
			println(b.booleanValue());
		} else if (o instanceof Character c) {
			println(c.charValue());
		} else {
			print(o.toString(), true);
		}
	}

	/**
	 * BigInteger を {@code toString()} で文字列化し出力します。（改行付き）
	 *
	 * @param bi 出力するオブジェクト
	 */
	public final void println(final BigInteger bi) {
		print(bi.toString(), true);
	}

	/**
	 * BigDecimal を {@code toString()} で文字列化し出力します。（改行付き）
	 *
	 * @param bd 出力するオブジェクト
	 */
	public final void println(final BigDecimal bd) {
		print(bd.toString(), true);
	}

	/* ------------------------ print() 系メソッド ------------------------ */

	/**
	 * int 値を出力します。（改行無し）
	 *
	 * @param i 出力する int 値
	 */
	public final void print(final int i) {
		ensureBufferSpace(MAX_INT_DIGITS);
		fillBuffer(i);
		if (autoFlush) flush();
	}

	/**
	 * long 値を出力します。（改行無し）
	 *
	 * @param l 出力する long 値
	 */
	public final void print(final long l) {
		ensureBufferSpace(MAX_LONG_DIGITS);
		fillBuffer(l);
		if (autoFlush) flush();
	}

	/**
	 * double 値を {@code Double.toString(d)} で文字列化し出力します。（改行無し）
	 *
	 * @param d 出力する double 値
	 */
	public final void print(final double d) {
		print(Double.toString(d), false);
	}

	/**
	 * char 値を出力します。（改行無し）
	 *
	 * @param c 出力する char 値
	 */
	public final void print(final char c) {
		ensureBufferSpace(1);
		buffer[pos++] = (byte) c;
		if (autoFlush) flush();
	}

	/**
	 * boolean 値を出力します。（true は "Yes"、 false は "No", 改行無し）
	 *
	 * @param b 出力する boolean 値
	 */
	public final void print(final boolean b) {
		ensureBufferSpace(3);
		fillBuffer(b);
		if (autoFlush) flush();
	}

	/**
	 * String を出力します。（改行無し）
	 *
	 * @param s 出力する String 値
	 */
	public final void print(final String s) {
		print(s, false);
	}

	/**
	 * Object を {@code toString()} で文字列化し出力します。（改行無し）
	 *
	 * @param o 出力するオブジェクト
	 */
	public final void print(final Object o) {
		if (o == null) return;
		if (o instanceof String s) {
			print(s, false);
		} else if (o instanceof Long l) {
			print(l.longValue());
		} else if (o instanceof Integer i) {
			print(i.intValue());
		} else if (o instanceof Double d) {
			print(d.toString(), false);
		} else if (o instanceof Boolean b) {
			print(b.booleanValue());
		} else if (o instanceof Character c) {
			print(c.charValue());
		} else {
			print(o.toString(), false);
		}
	}

	/**
	 * BigInteger を {@code toString()} で文字列化し出力します。（改行無し）
	 *
	 * @param bi 出力するオブジェクト
	 */
	public final void print(final BigInteger bi) {
		print(bi.toString(), false);
	}

	/**
	 * BigDecimal を {@code toString()} で文字列化し出力します。（改行無し）
	 *
	 * @param bd 出力するオブジェクト
	 */
	public final void print(final BigDecimal bd) {
		print(bd.toString(), false);
	}

	/* ------------------------ printf() 系メソッド ------------------------ */

	/**
	 * 指定されたフォーマットに従い文字列を生成して出力します。（改行無し）
	 *
	 * @param format 書式文字列
	 * @param args   書式引数
	 */
	public final void printf(final String format, final Object... args) {
		print(String.format(format, args), false);
	}

	/**
	 * 指定された言語環境で整形し、フォーマットに従い文字列を生成して出力します。（改行無し）
	 *
	 * @param locale 言語環境
	 * @param format 書式文字列
	 * @param args   書式引数
	 */
	public final void printf(final Locale locale, final String format, final Object... args) {
		print(String.format(locale, format, args), false);
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
	 * 指定された文字列を出力し、オプションで改行を追加します。<br>
	 * 内部的にバッファへ書き込みを行い、autoFlush が有効な場合は自動で flush されます。
	 *
	 * @param s       出力する文字列
	 * @param newline true の場合、出力後に改行を追加
	 */
	protected final void print(final String s, final boolean newline) {
		fillBuffer(s);
		if (newline) {
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
		}
		if (autoFlush) flush();
	}

	/**
	 * 指定された文字列をバッファに格納します。
	 *
	 * @param s 出力する文字列
	 */
	protected final void fillBuffer(final String s) {
		if (s == null) return;
		final int len = s.length();
		for (int i = 0; i < len; ) {
			ensureBufferSpace(1);
			int limit = min(buffer.length - pos, len - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) s.charAt(i++);
			}
		}
	}

	/**
	 * 指定された boolean 値を文字列 ("Yes" または "No") に変換してバッファに格納します。
	 *
	 * @param b 出力する boolean 値
	 */
	protected final void fillBuffer(final boolean b) {
		if (b) {
			buffer[pos++] = 'Y';
			buffer[pos++] = 'e';
			buffer[pos++] = 's';
		} else {
			buffer[pos++] = 'N';
			buffer[pos++] = 'o';
		}
	}

	/**
	 * 指定された int 値を文字列に変換してバッファに格納します。<br>
	 * 負の値の場合は先頭に '-' を付加します。
	 *
	 * @param i 出力する int 値
	 */
	protected final void fillBuffer(int i) {
		if (i == Integer.MIN_VALUE) {
			buffer[pos++] = '-';
			buffer[pos++] = '2';
			buffer[pos++] = '1';
			buffer[pos++] = '4';
			buffer[pos++] = '7';
			buffer[pos++] = '4';
			buffer[pos++] = '8';
			buffer[pos++] = '3';
			buffer[pos++] = '6';
			buffer[pos++] = '4';
			buffer[pos++] = '8';
			return;
		}

		boolean negative = (i < 0);
		if (negative) {
			i = -i;
			buffer[pos++] = '-';
		}

		int numOfDigits = countDigits(i);
		int writePos = pos + numOfDigits;
		while (i >= 100) {
			int quotient = i / 100;
			int remainder = (i - quotient * 100) << 1;
			buffer[--writePos] = TWO_DIGIT_NUMBERS[remainder + 1];
			buffer[--writePos] = TWO_DIGIT_NUMBERS[remainder];
			i = quotient;
		}

		if (i < 10) {
			buffer[--writePos] = (byte) ('0' + i);
		} else {
			buffer[--writePos] = TWO_DIGIT_NUMBERS[(i << 1) + 1];
			buffer[--writePos] = TWO_DIGIT_NUMBERS[i << 1];
		}

		pos += numOfDigits;
	}

	/**
	 * 指定された long 値を文字列に変換してバッファに格納します。<br>
	 * 負の値の場合は先頭に '-' を付加します。
	 *
	 * @param l 出力する long 値
	 */
	protected final void fillBuffer(long l) {
		if ((int) l == l) {
			fillBuffer((int) l);
			return;
		}
		if (l == Long.MIN_VALUE) {
			buffer[pos++] = '-';
			buffer[pos++] = '9';
			buffer[pos++] = '2';
			buffer[pos++] = '2';
			buffer[pos++] = '3';
			buffer[pos++] = '3';
			buffer[pos++] = '7';
			buffer[pos++] = '2';
			buffer[pos++] = '0';
			buffer[pos++] = '3';
			buffer[pos++] = '6';
			buffer[pos++] = '8';
			buffer[pos++] = '5';
			buffer[pos++] = '4';
			buffer[pos++] = '7';
			buffer[pos++] = '7';
			buffer[pos++] = '5';
			buffer[pos++] = '8';
			buffer[pos++] = '0';
			buffer[pos++] = '8';
			return;
		}

		boolean negative = (l < 0);
		if (negative) {
			l = -l;
			buffer[pos++] = '-';
		}

		int numOfDigits = countDigits(l);
		int writePos = pos + numOfDigits;
		while (l >= 100) {
			long quotient = l / 100;
			int remainder = (int) (l - quotient * 100) << 1;
			buffer[--writePos] = TWO_DIGIT_NUMBERS[remainder + 1];
			buffer[--writePos] = TWO_DIGIT_NUMBERS[remainder];
			l = quotient;
		}

		if (l < 10) {
			buffer[--writePos] = (byte) ('0' + l);
		} else {
			buffer[--writePos] = TWO_DIGIT_NUMBERS[(int) (l << 1) + 1];
			buffer[--writePos] = TWO_DIGIT_NUMBERS[(int) l << 1];
		}

		pos += numOfDigits;
	}

	/**
	 * 指定された int 値の桁数を数えます。
	 * 与えられる数値は正の整数であることを前提とした実装です。
	 *
	 * @param i 数値
	 * @return 桁数
	 */
	private int countDigits(final int i) {
		if (i < 10) return 1;
		if (i < 100) return 2;
		if (i < 1000) return 3;
		if (i < 10000) return 4;
		if (i < 100000) return 5;
		if (i < 1000000) return 6;
		if (i < 10000000) return 7;
		if (i < 100000000) return 8;
		if (i < 1000000000) return 9;
		return 10;
	}

	/**
	 * 指定された long 値の桁数を数えます。
	 * 与えられる数値は正の整数であり、int の範囲外が保証されることを前提とした実装です。
	 *
	 * @param l 数値
	 * @return 桁数
	 */
	private int countDigits(final long l) {
		if (l < 10000000000L) return 10;
		if (l < 100000000000L) return 11;
		if (l < 1000000000000L) return 12;
		if (l < 10000000000000L) return 13;
		if (l < 100000000000000L) return 14;
		if (l < 1000000000000000L) return 15;
		if (l < 10000000000000000L) return 16;
		if (l < 100000000000000000L) return 17;
		if (l < 1000000000000000000L) return 18;
		return 19;
	}

}
