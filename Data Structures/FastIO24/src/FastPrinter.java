import java.io.*;
import java.lang.invoke.*;
import java.math.*;
import java.util.*;

import static java.lang.Math.*;

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
	 * byte配列に対するVarHandleです。高速なバッファアクセスに使用します。
	 */
	protected static final VarHandle BYTE_ARRAY_HANDLE = MethodHandles.arrayElementVarHandle(byte[].class);
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
	 * 改行文字（'\n'）のバイト表現
	 */
	protected static final byte LINE = '\n';

	/**
	 * 空白文字（' '）のバイト表現
	 */
	protected static final byte SPACE = ' ';

	/**
	 * ハイフン（'-'）のバイト表現
	 */
	protected static final byte HYPHEN = '-';

	/**
	 * ピリオド（'.'）のバイト表現
	 */
	protected static final byte PERIOD = '.';

	/**
	 * 数字のゼロ（'0'）のバイト表現
	 */
	protected static final byte ZERO = '0';

	/**
	 * 出力用内部バッファのデフォルトサイズ（バイト単位）<br>
	 * ※64バイト未満の場合、内部的に64バイトに調整されます。
	 */
	private static final int DEFAULT_BUFFER_SIZE = 65536;

	/**
	 * boolean値がtrueの場合の出力文字列 "Yes" のバイト配列
	 */
	private static final byte[] TRUE_BYTES = {'Y', 'e', 's'};

	/**
	 * boolean値がfalseの場合の出力文字列 "No" のバイト配列
	 */
	private static final byte[] FALSE_BYTES = {'N', 'o'};

	/**
	 * 00～99 の1の位の数字を格納した配列
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
	 * 00～99 の10の位の数字を格納した配列
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

	/* ------------------------ インスタンス変数 ------------------------ */

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
	 * 出力先の内部バッファです。書き込みはこの配列に対して行い、必要に応じて {@link #flush()} で出力します。
	 */
	protected byte[] buffer;

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
		this.buffer = new byte[max(64, roundUpToPowerOfTwo(bufferSize))];
		this.autoFlush = autoFlush;
	}

	/* ------------------------ 静的メソッド ------------------------ */

	/**
	 * 指定された整数値の桁数を数えます。<br>
	 * 与えられる数値は負の整数であることを前提とした実装です。
	 *
	 * @param i 負の整数値
	 * @return 桁数（符号を除く）
	 */
	protected static int countDigits(final int i) {
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
	 * 指定された整数値の桁数を数えます。<br>
	 * 与えられる数値は負の整数であることを前提とした実装です。
	 *
	 * @param l 負の整数値
	 * @return 桁数（符号を除く）
	 */
	protected static int countDigits(final long l) {
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

	/**
	 * 指定された値を自身以上の最小の2の冪乗に切り上げます。<br>
	 * バッファサイズの調整に使用します。
	 *
	 * @param x 切り上げる値
	 * @return x 以上の最小の2の冪乗
	 */
	private static int roundUpToPowerOfTwo(int x) {
		if (x <= 1) return 1;
		x--;
		x |= x >>> 1;
		x |= x >>> 2;
		x |= x >>> 4;
		x |= x >>> 8;
		x |= x >>> 16;
		return x + 1;
	}

	/* ------------------------ オーバーライドメソッド ------------------------ */

	/**
	 * {@code flush()}を実行し、このOutputStreamを閉じます。<br>
	 * 出力先が {@code System.out} の場合、閉じません。
	 *
	 * @throws RuntimeException 出力時のエラーが発生した場合
	 */
	@Override
	public final void close() {
		try {
			flush();
			if (out != System.out) out.close();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* ------------------------ 公開メソッド ------------------------ */

	/**
	 * 現在のバッファに保持しているすべてのデータを出力し、バッファをクリアします。
	 *
	 * @throws RuntimeException 出力時のエラーが発生した場合
	 */
	public final void flush() {
		if (pos == 0) return;
		try {
			out.write(buffer, 0, pos);
			pos = 0;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* ------------------------ println() 系メソッド ------------------------ */

	/**
	 * 改行のみ出力します。
	 *
	 * @return この {@code FastPrinter} インスタンス
	 */
	public FastPrinter println() {
		ensureCapacity(1);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, LINE);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * boolean 値を出力します。（true は "Yes"、 false は "No"、改行付き）
	 *
	 * @param b 出力する boolean 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final boolean b) {
		write(b);
		ensureCapacity(1);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, LINE);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * byte 値を出力します。（改行付き）
	 *
	 * @param b 出力する byte 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final byte b) {
		ensureCapacity(2);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, b);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, LINE);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * char 値を出力します。（改行付き）
	 *
	 * @param c 出力する char 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final char c) {
		ensureCapacity(2);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) c);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, LINE);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * int 値を出力します。（改行付き）
	 *
	 * @param i 出力する int 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final int i) {
		ensureCapacity(MAX_INT_DIGITS + 1);
		write(i);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, LINE);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * long 値を出力します。（改行付き）
	 *
	 * @param l 出力する long 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final long l) {
		ensureCapacity(MAX_LONG_DIGITS + 1);
		write(l);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, LINE);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * double 値を {@code Double.toString(d)} で文字列化し出力します。（改行付き）
	 *
	 * @param d 出力する double 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final double d) {
		return println(Double.toString(d));
	}

	/**
	 * BigInteger を {@code toString()} で文字列化し出力します。（改行付き）
	 *
	 * @param bi 出力する BigInteger
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final BigInteger bi) {
		return println(bi.toString());
	}

	/**
	 * BigDecimal を {@code toString()} で文字列化し出力します。（改行付き）
	 *
	 * @param bd 出力する BigDecimal
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final BigDecimal bd) {
		return println(bd.toString());
	}

	/**
	 * String を出力します。（改行付き）
	 *
	 * @param s 出力する String 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final String s) {
		ensureCapacity(s.length() + 1);
		write(s);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, LINE);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * StringBuilder を出力します。（改行付き）
	 *
	 * @param s 出力する StringBuilder 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter println(final StringBuilder s) {
		ensureCapacity(s.length() + 1);
		write(s.toString());
		BYTE_ARRAY_HANDLE.set(buffer, pos++, LINE);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * Object を文字列化して出力します。（改行付き）<br>
	 * {@code null} の場合は改行のみ出力します。<br>
	 * 効率化のため、内部で {@code switch} による型判定を行い、
	 * 基本的な型（String, Integer, Long など）はそれぞれに最適化されたメソッドで処理します。
	 *
	 * @param o 出力するオブジェクト
	 * @return この {@code FastPrinter} インスタンス
	 */
	public FastPrinter println(final Object o) {
		return switch (o) {
			case null -> println();
			case Boolean b -> println(b.booleanValue());
			case Byte b -> println(b.byteValue());
			case Character c -> println(c.charValue());
			case Integer i -> println(i.intValue());
			case Long l -> println(l.longValue());
			case Double d -> println(d.toString());
			case BigInteger bi -> println(bi.toString());
			case BigDecimal bd -> println(bd.toString());
			case String s -> println(s);
			default -> println(o.toString());
		};
	}

	/* ------------------------ print() 系メソッド ------------------------ */

	/**
	 * boolean 値を出力します。（true は "Yes"、 false は "No"、改行無し）
	 *
	 * @param b 出力する boolean 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final boolean b) {
		write(b);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * byte 値を出力します。（改行無し）
	 *
	 * @param b 出力する byte 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final byte b) {
		ensureCapacity(1);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, b);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * char 値を出力します。（改行無し）
	 *
	 * @param c 出力する char 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final char c) {
		ensureCapacity(1);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) c);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * int 値を出力します。（改行無し）
	 *
	 * @param i 出力する int 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final int i) {
		ensureCapacity(MAX_INT_DIGITS);
		write(i);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * long 値を出力します。（改行無し）
	 *
	 * @param l 出力する long 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final long l) {
		ensureCapacity(MAX_LONG_DIGITS);
		write(l);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * double 値を {@code Double.toString(d)} で文字列化し出力します。（改行無し）
	 *
	 * @param d 出力する double 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final double d) {
		return print(Double.toString(d));
	}

	/**
	 * BigInteger を {@code toString()} で文字列化し出力します。（改行無し）
	 *
	 * @param bi 出力する BigInteger
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final BigInteger bi) {
		return print(bi.toString());
	}

	/**
	 * BigDecimal を {@code toString()} で文字列化し出力します。（改行無し）
	 *
	 * @param bd 出力する BigDecimal
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final BigDecimal bd) {
		return print(bd.toString());
	}

	/**
	 * String を出力します。（改行無し）
	 *
	 * @param s 出力する String 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final String s) {
		ensureCapacity(s.length());
		write(s);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * StringBuilder を出力します。（改行無し）
	 *
	 * @param s 出力する StringBuilder 値
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter print(final StringBuilder s) {
		ensureCapacity(s.length());
		write(s.toString());
		if (autoFlush) flush();
		return this;
	}

	/**
	 * Object を文字列化して出力します。（改行無し）<br>
	 * {@code null} の場合は何も出力しません。<br>
	 * 効率化のため、内部で {@code switch} による型判定を行い、
	 * 基本的な型（String, Integer, Long など）はそれぞれに最適化されたメソッドで処理します。
	 *
	 * @param o 出力するオブジェクト
	 * @return この {@code FastPrinter} インスタンス
	 */
	public FastPrinter print(final Object o) {
		return switch (o) {
			case null -> this;
			case Boolean b -> print(b.booleanValue());
			case Byte b -> print(b.byteValue());
			case Character c -> print(c.charValue());
			case Integer i -> print(i.intValue());
			case Long l -> print(l.longValue());
			case Double d -> print(d.toString());
			case BigInteger bi -> print(bi.toString());
			case BigDecimal bd -> print(bd.toString());
			case String s -> print(s);
			default -> print(o.toString());
		};
	}

	/* ------------------------ printf() 系メソッド ------------------------ */

	/**
	 * 指定されたフォーマットに従い文字列を生成して出力します。（改行無し）
	 *
	 * @param format 書式文字列
	 * @param args   書式引数
	 * @return この {@code FastPrinter} インスタンス
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
	 * @return この {@code FastPrinter} インスタンス
	 */
	public final FastPrinter printf(final Locale locale, final String format, final Object... args) {
		return print(String.format(locale, format, args));
	}

	/* ------------------------ プライベートヘルパーメソッド ------------------------ */

	/**
	 * 指定されたバイト数のデータを出力するために必要な領域を保証します。<br>
	 * バッファの残り容量が不足している場合、内部バッファを2の冪に丸めて拡張します。
	 *
	 * @param additional 出力予定のデータのバイト数
	 */
	protected final void ensureCapacity(final int additional) {
		final int required = pos + additional;
		if (required <= buffer.length) return;
		if (required <= 1_000_000_000) {
			buffer = Arrays.copyOf(buffer, roundUpToPowerOfTwo(required));
		} else {
			flush();
		}
	}

	/**
	 * 指定された boolean 値を文字列 ("Yes" または "No") に変換してバッファに格納します。
	 *
	 * @param b 出力する boolean 値
	 */
	protected final void write(final boolean b) {
		final byte[] src = b ? TRUE_BYTES : FALSE_BYTES;
		final int len = src.length;
		ensureCapacity(len);
		System.arraycopy(src, 0, buffer, pos, len);
		pos += len;
	}

	/**
	 * 指定された整数値を文字列に変換してバッファに格納します。<br>
	 * 負の値の場合は先頭に '-' を付加します。<br>
	 * 100単位でまとめて処理することで高速化を図っています。
	 *
	 * @param i 出力する整数値
	 */
	protected final void write(int i) {
		final byte[] buf = buffer;
		int p = pos;
		if (i >= 0) i = -i;
		else BYTE_ARRAY_HANDLE.set(buf, p++, HYPHEN);
		final int digits = countDigits(i);
		int writePos = p + digits;
		while (i <= -100) {
			final int q = i / 100;
			final int r = (q << 6) + (q << 5) + (q << 2) - i;
			BYTE_ARRAY_HANDLE.set(buf, --writePos, DigitOnes[r]);
			BYTE_ARRAY_HANDLE.set(buf, --writePos, DigitTens[r]);
			i = q;
		}
		final int r = -i;
		BYTE_ARRAY_HANDLE.set(buf, --writePos, DigitOnes[r]);
		if (r >= 10) BYTE_ARRAY_HANDLE.set(buf, --writePos, DigitTens[r]);
		pos = p + digits;
	}

	/**
	 * 指定された整数値を文字列に変換してバッファに格納します。<br>
	 * 負の値の場合は先頭に '-' を付加します。<br>
	 * 100単位でまとめて処理することで高速化を図っています。
	 *
	 * @param l 出力する整数値
	 */
	protected final void write(long l) {
		final byte[] buf = buffer;
		int p = pos;
		if (l >= 0) l = -l;
		else BYTE_ARRAY_HANDLE.set(buf, p++, HYPHEN);
		final int digits = countDigits(l);
		int writePos = p + digits;
		while (l <= -100) {
			final long q = l / 100;
			final int r = (int) ((q << 6) + (q << 5) + (q << 2) - l);
			BYTE_ARRAY_HANDLE.set(buf, --writePos, DigitOnes[r]);
			BYTE_ARRAY_HANDLE.set(buf, --writePos, DigitTens[r]);
			l = q;
		}
		final int r = (int) -l;
		BYTE_ARRAY_HANDLE.set(buf, --writePos, DigitOnes[r]);
		if (r >= 10) BYTE_ARRAY_HANDLE.set(buf, --writePos, DigitTens[r]);
		pos = p + digits;
	}

	/**
	 * 指定された文字列をバッファに書き込みます。<br>
	 * ループ展開により、8文字ずつまとめて処理することで高速化を図っています。
	 *
	 * @param s 書き込む文字列
	 */
	protected final void write(final String s) {
		final int len = s.length();
		final byte[] buf = buffer;
		int p = pos, i = 0;
		final int limit = len & ~7;
		while (i < limit) {
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
		}
		while (i < len) BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
		pos = p;
	}

}
