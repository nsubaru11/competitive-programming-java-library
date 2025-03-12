import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import static java.lang.Math.*;

/**
 * 標準出力を高速に処理するクラスです。
 * ※注意：このクラスは内部バッファが満杯になると自動的に指定のOutputStreamに書き出しますが、
 * 処理の途中で結果をすぐに反映させる必要がある場合は、明示的に flush() を呼び出す必要があります。
 */
public final class FastPrinter implements AutoCloseable {

	// ------------------ フィールド ------------------

	/**
	 * int 型の値を文字列に変換した際に必要となる最大桁数 (符号込み)。
	 * 例：Integer.MIN_VALUE は "-2147483648" で 11 バイト。
	 */
	private static final int MAX_INT_DIGITS = 11;

	/**
	 * long 型の値を文字列に変換した際に必要となる最大桁数 (符号込み)。
	 * 例：Long.MIN_VALUE は "-9223372036854775808" で 20 バイト。
	 */
	private static final int MAX_LONG_DIGITS = 20;
	/**
	 * 出力用内部バッファの初期サイズ（バイト単位）。
	 */
	private static final int DEFAULT_BUFFER_SIZE = 65536;
	/**
	 * 00～99 の 2 桁の数字を連続した 1 次元配列として格納
	 */
	private static final byte[] TWO_DIGIT_NUMBERS = new byte[200];

	/*
	 * static initializer
	 * TWO_DIGIT_NUMBERSの初期化を行います。
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

	/**
	 * 標準出力の出力先。System.out を標準で利用します。
	 */
	private final OutputStream out;
	/**
	 * 出力先の内部バッファ。書き込みはこの配列に対して行い、必要に応じて flush() で出力します。
	 */
	private final byte[] buffer;

	/**
	 * 現在のバッファ内での書き込み位置を示します。
	 */
	private int pos = 0;

	// ------------------ コンストラクタ ------------------

	/**
	 * デフォルトの設定でFastPrinterを初期化します。<p>
	 * バッファ容量: 65536 <p>
	 * OutputStream: System.out <p>
	 * autoFlush: false（バッファが満杯になるとflushされます。）
	 */
	public FastPrinter() {
		this(System.out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 指定されたOutputStreamを用いてFastPrinterを初期化します。<p>
	 * バッファ容量: 65536 <p>
	 * autoFlush: false（バッファが満杯になるとflushされます。）
	 *
	 * @param out OutputStream
	 */
	public FastPrinter(OutputStream out) {
		this(out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 指定されたバッファ容量でFastPrinterを初期化します。<p>
	 * OutputStream: System.out <p>
	 * autoFlush: false（バッファが満杯になるとflushされます。）
	 *
	 * @param bufferSize バッファの初期容量（文字単位）
	 *                   64バイト未満を指定した場合、容量は64となります。
	 */
	public FastPrinter(int bufferSize) {
		this(System.out, bufferSize);
	}

	/**
	 * 指定されたバッファ容量とOutputStreamでFastPrinterを初期化します。<p>
	 * autoFlush: false（バッファが満杯になるとflushされます。）
	 *
	 * @param out        OutputStream
	 * @param bufferSize バッファの初期容量（文字単位）
	 *                   64バイト未満を指定した場合、容量は64となります。
	 */
	public FastPrinter(OutputStream out, int bufferSize) {
		this.out = new BufferedOutputStream(out);
		buffer = new byte[max(bufferSize, 64)];
	}

	// --------------- 出力メソッド（改行付き） ---------------

	/**
	 * 改行のみ出力
	 */
	public void println() {
		ensureBufferSpace(1);
		buffer[pos++] = '\n';
	}

	/**
	 * intを出力（改行付き）
	 */
	public void println(final int i) {
		ensureBufferSpace(MAX_INT_DIGITS + 1);
		fillBuffer(i);
		buffer[pos++] = '\n';
	}

	/**
	 * longを出力（改行付き）
	 */
	public void println(final long l) {
		ensureBufferSpace(MAX_LONG_DIGITS + 1);
		fillBuffer(l);
		buffer[pos++] = '\n';
	}

	/**
	 * doubleを出力（改行付き）
	 */
	public void println(final double d) {
		println(Double.toString(d));
	}

	/**
	 * charを出力（改行付き）
	 */
	public void println(final char c) {
		ensureBufferSpace(2);
		buffer[pos++] = (byte) c;
		buffer[pos++] = '\n';
	}

	/**
	 * booleanを出力（true→"Yes", false→"No", 改行付き）
	 */
	public void println(final boolean b) {
		ensureBufferSpace(4);
		if (b) {
			buffer[pos++] = 'Y';
			buffer[pos++] = 'e';
			buffer[pos++] = 's';
		} else {
			buffer[pos++] = 'N';
			buffer[pos++] = 'o';
		}
		buffer[pos++] = '\n';
	}

	/**
	 * Stringを出力（改行付き）
	 */
	public void println(final String s) {
		print(s);
		println();
	}

	/**
	 * Objectを出力（改行付き）
	 */
	public void println(final Object o) {
		println(o.toString());
	}

	/**
	 * 2つの整数（int, int）をそれぞれ改行して出力
	 */
	public void println(final int a, final int b) {
		ensureBufferSpace((MAX_INT_DIGITS << 1) + 2);
		fillBuffer(a);
		buffer[pos++] = '\n';
		fillBuffer(b);
		buffer[pos++] = '\n';
	}

	/**
	 * 2つの整数（int, long）をそれぞれ改行して出力
	 */
	public void println(final int a, final long b) {
		ensureBufferSpace(MAX_INT_DIGITS + MAX_LONG_DIGITS + 2);
		fillBuffer(a);
		buffer[pos++] = '\n';
		fillBuffer(b);
		buffer[pos++] = '\n';
	}

	/**
	 * 2つの整数（long, int）をそれぞれ改行して出力
	 */
	public void println(final long a, final int b) {
		ensureBufferSpace(MAX_LONG_DIGITS + MAX_INT_DIGITS + 2);
		fillBuffer(a);
		buffer[pos++] = '\n';
		fillBuffer(b);
		buffer[pos++] = '\n';
	}

	/**
	 * 2つの整数（long, long）をそれぞれ改行して出力
	 */
	public void println(final long a, final long b) {
		ensureBufferSpace(MAX_LONG_DIGITS + MAX_LONG_DIGITS + 2);
		fillBuffer(a);
		buffer[pos++] = '\n';
		fillBuffer(b);
		buffer[pos++] = '\n';
	}

	/**
	 * 指定された桁数で double を出力（改行付き）
	 * 小数点以下 n 桁に丸め（四捨五入）
	 */
	public void println(final double d, final int n) {
		print(d, n);
		println();
	}

	/**
	 * booleanを指定された関数で変換し、変換後の値を出力（改行付き）
	 */
	public <T> void println(final boolean b, final Function<Boolean, T> function) {
		println(function.apply(b));
	}

	/**
	 * int 配列の各要素を改行区切りで出力
	 */
	public void println(final int[] arr) {
		for (final int i : arr)
			println(i);
	}

	/**
	 * long 配列の各要素を改行区切りで出力
	 */
	public void println(final long[] arr) {
		for (final long l : arr)
			println(l);
	}

	/**
	 * char 配列の各要素を改行区切りで出力
	 */
	public void println(final char[] arr) {
		int i = 0;
		final int len = arr.length;
		while (i < len) {
			ensureBufferSpace(2);
			int limit = min((buffer.length - pos) >> 1, len - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) arr[i++];
				buffer[pos++] = '\n';
			}
		}
	}

	/**
	 * boolean 配列の各要素を改行区切りで出力
	 */
	public void println(final boolean[] arr) {
		for (final boolean b : arr)
			println(b);
	}

	/**
	 * String 配列の各要素を改行区切りで出力
	 */
	public void println(final String[] arr) {
		for (final String s : arr)
			println(s);
	}

	/**
	 * int 配列の各要素を指定された関数で変換し、変換後の値を改行区切りで出力
	 */
	public <T> void println(final int[] arr, final IntFunction<T> function) {
		for (final int i : arr)
			println(function.apply(i));
	}

	/**
	 * long 配列の各要素を指定された関数で変換し、変換後の値を改行区切りで出力
	 */
	public <T> void println(final long[] arr, final LongFunction<T> function) {
		for (final long l : arr)
			println(function.apply(l));
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、変換後の値を改行区切りで出力
	 */
	public <T> void println(final char[] arr, final Function<Character, T> function) {
		for (final char c : arr)
			println(function.apply(c));
	}

	/**
	 * boolean 配列の各要素を指定された関数で変換し、変換後の値を改行区切りで出力
	 */
	public <T> void println(final boolean[] arr, final Function<Boolean, T> function) {
		for (final boolean b : arr)
			println(function.apply(b));
	}

	/**
	 * String 配列の各要素を指定された関数で変換し、変換後の値を改行区切りで出力
	 */
	public <T> void println(final String[] arr, final Function<String, T> function) {
		for (final String s : arr)
			println(function.apply(s));
	}

	/**
	 * 可変長の Object 配列の各要素を改行区切りで出力
	 */
	public void println(final Object... arr) {
		for (final Object o : arr)
			println(o.toString());
	}

	/**
	 * 二次元 int 配列を、各行を半角スペース区切りで出力（各行末に改行）
	 */
	public void println(final int[][] arr2d) {
		for (final int[] arr : arr2d) {
			print(arr);
			println();
		}
	}

	/**
	 * 二次元 long 配列を、各行を半角スペース区切りで出力（各行末に改行）
	 */
	public void println(final long[][] arr2d) {
		for (final long[] arr : arr2d) {
			print(arr);
			println();
		}
	}

	/**
	 * 二次元 char 配列を、各行を半角スペース区切りで出力（各行末に改行）
	 */
	public void println(final char[][] arr2d) {
		for (final char[] arr : arr2d) {
			print(arr);
			println();
		}
	}

	/**
	 * 二次元 boolean 配列を、各行を半角スペース区切りで出力（各行末に改行）
	 */
	public void println(final boolean[][] arr2d) {
		for (final boolean[] arr : arr2d) {
			print(arr);
			println();
		}
	}

	/**
	 * 二次元 String 配列を、各行を半角スペース区切りで出力（各行末に改行）
	 */
	public void println(final String[][] arr2d) {
		for (final String[] arr : arr2d) {
			print(arr);
			println();
		}
	}

	/**
	 * 二次元 Object 配列を、各行を半角スペース区切りで出力（各行末に改行）
	 */
	public void println(final Object[][] arr2d) {
		for (final Object[] arr : arr2d) {
			print(arr);
			println();
		}
	}

	/**
	 * 二次元 int 配列の各要素を指定された関数で変換し、変換後の値を各行を半角スペース区切りで出力（各行末に改行）
	 */
	public <T> void println(final int[][] arr2d, final IntFunction<T> function) {
		for (final int[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元 long 配列の各要素を指定された関数で変換し、変換後の値を各行を半角スペース区切りで出力（各行末に改行）
	 */
	public <T> void println(final long[][] arr2d, final LongFunction<T> function) {
		for (final long[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元 char 配列の各要素を指定された関数で変換し、変換後の値を各行を半角スペース区切りで出力（各行末に改行）
	 */
	public <T> void println(final char[][] arr2d, final LongFunction<T> function) {
		for (final char[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元 boolean 配列の各要素を指定された関数で変換し、変換後の値を各行を半角スペース区切りで出力（各行末に改行）
	 */
	public <T> void println(final boolean[][] arr2d, final Function<Boolean, T> function) {
		for (final boolean[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元 String 配列の各要素を指定された関数で変換し、変換後の値を各行を半角スペース区切りで出力（各行末に改行）
	 */
	public <T> void println(final String[][] arr2d, final Function<String, T> function) {
		for (final String[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元 char 配列を、各行を区切り文字無しで出力（各行末に改行）
	 */
	public void printChars(final char[][] arr2d) {
		for (final char[] arr : arr2d) {
			printChars(arr);
			println();
		}
	}

	/**
	 * 二次元 char 配列の各要素を指定された関数で変換し、変換後の値を各行を区切り文字無しで出力（各行末に改行）
	 */
	public void printChars(final char[][] arr2d, final Function<Character, Character> function) {
		for (final char[] arr : arr2d) {
			printChars(arr, function);
			println();
		}
	}

	//---------------- 出力メソッド（改行無し） ----------------

	/**
	 * int を出力（改行無し）
	 */
	public void print(final int i) {
		ensureBufferSpace(MAX_INT_DIGITS);
		fillBuffer(i);
	}

	/**
	 * long を出力（改行無し）
	 */
	public void print(final long l) {
		ensureBufferSpace(MAX_LONG_DIGITS);
		fillBuffer(l);
	}

	/**
	 * double を出力（改行無し）
	 */
	public void print(final double d) {
		print(Double.toString(d));
	}

	/**
	 * char を出力（改行無し）
	 */
	public void print(final char c) {
		ensureBufferSpace(1);
		buffer[pos++] = (byte) c;
	}

	/**
	 * boolean を出力（true→"Yes", false→"No", 改行無し）
	 */
	public void print(final boolean b) {
		ensureBufferSpace(3);
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
	 * Stringを出力（改行無し）
	 */
	public void print(final String s) {
		int i = 0;
		final int len = s.length();
		while (i < len) {
			ensureBufferSpace(1);
			int limit = min(buffer.length - pos, len - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) s.charAt(i++);
			}
		}
	}

	/**
	 * Object を出力（改行無し）
	 */
	public void print(final Object o) {
		print(o.toString());
	}

	/**
	 * 2 つの整数（int, int）を半角スペース区切りで出力（改行無し）
	 */
	public void print(final int a, final int b) {
		ensureBufferSpace((MAX_INT_DIGITS << 1) + 1);
		fillBuffer(a);
		buffer[pos++] = ' ';
		fillBuffer(b);
	}

	/**
	 * 2 つの整数（int, long）を半角スペース区切りで出力（改行無し）
	 */
	public void print(final int a, final long b) {
		ensureBufferSpace(MAX_INT_DIGITS + MAX_LONG_DIGITS + 1);
		fillBuffer(a);
		buffer[pos++] = ' ';
		fillBuffer(b);
	}

	/**
	 * 2 つの整数（long, int）を半角スペース区切りで出力（改行無し）
	 */
	public void print(final long a, final int b) {
		ensureBufferSpace(MAX_LONG_DIGITS + MAX_INT_DIGITS + 1);
		fillBuffer(a);
		buffer[pos++] = ' ';
		fillBuffer(b);
	}

	/**
	 * 2 つの整数（long, long）を半角スペース区切りで出力（改行無し）
	 */
	public void print(final long a, final long b) {
		ensureBufferSpace(MAX_LONG_DIGITS + MAX_LONG_DIGITS + 1);
		fillBuffer(a);
		buffer[pos++] = ' ';
		fillBuffer(b);
	}

	/**
	 * 指定された桁数で double を出力（改行無し）
	 * 小数点以下 n 桁に丸め（四捨五入）
	 */
	public void print(double a, int n) {
		if (n == 0) {
			print(round(a));
			return;
		}

		if (a < 0) {
			ensureBufferSpace(1);
			buffer[pos++] = '-';
			a = -a;
		}

		a += pow(10, -n) / 2;
		print((long) a);
		ensureBufferSpace(n + 1);
		buffer[pos++] = '.';
		a -= (long) a;
		while (n-- > 0) {
			a *= 10;
			buffer[pos++] = (byte) ((int) a + '0');
			a -= (int) a;
		}
	}


	/**
	 * booleanを指定された関数で変換し、変換後の値を出力（改行無し）
	 */
	public <T> void print(final boolean b, final Function<Boolean, T> function) {
		println(function.apply(b));
	}

	/**
	 * 可変長の Object 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(final Object... arr) {
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			print(' ');
			print(arr[i]);
		}
	}

	/**
	 * int 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(final int[] arr) {
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(MAX_INT_DIGITS + 1);
			buffer[pos++] = ' ';
			fillBuffer(arr[i]);
		}
	}

	/**
	 * long 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(final long[] arr) {
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(MAX_LONG_DIGITS + 1);
			buffer[pos++] = ' ';
			fillBuffer(arr[i]);
		}
	}

	/**
	 * char 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(final char[] arr) {
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		int i = 1;
		while (i < len) {
			ensureBufferSpace(2);
			int limit = min(buffer.length - pos, len - i);
			while (limit-- > 0) {
				buffer[pos++] = ' ';
				buffer[pos++] = (byte) arr[i++];
			}
		}
	}

	/**
	 * boolean 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(final boolean[] arr) {
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			print(' ');
			print(arr[i]);
		}
	}

	/**
	 * String 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(final String[] arr) {
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			print(' ');
			print(arr[i]);
		}
	}

	/**
	 * int 配列の各要素を指定された関数で変換し、変換後の値を半角スペース区切りで出力（改行無し）
	 */
	public <T> void print(final int[] arr, final IntFunction<T> function) {
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * long 配列の各要素を指定された関数で変換し、変換後の値を半角スペース区切りで出力（改行無し）
	 */
	public <T> void print(final long[] arr, final LongFunction<T> function) {
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、変換後の値を半角スペース区切りで出力（改行無し）
	 */
	public <T> void print(final char[] arr, final Function<Character, T> function) {
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * boolean 配列の各要素を指定された関数で変換し、変換後の値を半角スペース区切りで出力（改行無し）
	 */
	public <T> void print(final boolean[] arr, final Function<Boolean, T> function) {
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * String 配列の各要素を指定された関数で変換し、変換後の値を半角スペース区切りで出力（改行無し）
	 */
	public <T> void print(final String[] arr, final Function<String, T> function) {
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * char 配列の各要素を区切り文字無しで出力（改行無し）
	 */
	public void printChars(final char[] arr) {
		int i = 0;
		final int len = arr.length;
		while (i < len) {
			ensureBufferSpace(1);
			int limit = min(buffer.length - pos, len - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) arr[i++];
			}
		}
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、変換後の値を区切り文字無しで出力（改行無し）
	 */
	public void printChars(final char[] arr, final Function<Character, Character> function) {
		int i = 0;
		final int len = arr.length;
		while (i < len) {
			ensureBufferSpace(1);
			int limit = min(buffer.length - pos, len - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) function.apply(arr[i++]).charValue();
			}
		}
	}

	/**
	 * フォーマットを指定して出力（改行無し）
	 */
	public void printf(String format, Object... args) {
		print(String.format(format, args));
	}

	/**
	 * フォーマットを指定して出力します。指定された言語環境での整形を行います。（改行無し）
	 */
	public void printf(Locale locale, String format, Object... args) {
		print(String.format(locale, format, args));
	}

	// ------------------ バッファの管理 ------------------

	/**
	 * 指定されたバイト数のデータを出力するために必要な領域を保証します。
	 * 内部バッファの残り領域が不足している場合、flush() を呼び出してバッファをクリアします。
	 *
	 * @param size 出力予定のデータのバイト数
	 */
	private void ensureBufferSpace(int size) {
		if (pos + size - 1 >= buffer.length) {
			try {
				out.write(buffer, 0, pos);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			pos = 0;
		}
	}

	/**
	 * 現在のバッファに保持している全ての出力内容をこのOutputStreamに書き出し、バッファをクリアします。
	 */
	public void flush() {
		try {
			if (pos > 0) {
				out.write(buffer, 0, pos);
			}
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		pos = 0;
	}

	/**
	 * 指定された int 値を文字列に変換し、buffer に格納します。
	 * 数値が負の場合は符号 '-' を先頭に付加します。
	 */
	private void fillBuffer(int i) {
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

		int p = pos + MAX_INT_DIGITS - 1;
		while (i >= 100) {
			int q = i / 100;
			int r = i - q * 100;
			buffer[--p] = TWO_DIGIT_NUMBERS[(r << 1) + 1];
			buffer[--p] = TWO_DIGIT_NUMBERS[r << 1];
			i = q;
		}

		if (i < 10) {
			buffer[--p] = (byte) ('0' + i);
		} else {
			buffer[--p] = TWO_DIGIT_NUMBERS[(i << 1) + 1];
			buffer[--p] = TWO_DIGIT_NUMBERS[i << 1];
		}

		int len = pos + MAX_INT_DIGITS - p - 1;
		if (p == pos) {
			pos += len;
			return;
		}
		while (len-- > 0) {
			buffer[pos++] = buffer[p++];
		}
	}

	/**
	 * 指定された long 値を文字列に変換し、buffer に格納します。
	 * 数値が負の場合は符号 '-' を先頭に付加します。
	 */
	private void fillBuffer(long value) {
		if (value == Long.MIN_VALUE) {
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

		boolean negative = (value < 0);
		if (negative) {
			value = -value;
			buffer[pos++] = '-';
		}

		int writePos = pos + MAX_LONG_DIGITS - 1;
		while (value >= 100) {
			long quotient = value / 100;
			int remainder = (int) (value - quotient * 100);
			buffer[--writePos] = TWO_DIGIT_NUMBERS[(remainder << 1) + 1];
			buffer[--writePos] = TWO_DIGIT_NUMBERS[remainder << 1];
			value = quotient;
		}

		if (value < 10) {
			buffer[--writePos] = (byte) ('0' + value);
		} else {
			buffer[--writePos] = TWO_DIGIT_NUMBERS[(int) (value << 1) + 1];
			buffer[--writePos] = TWO_DIGIT_NUMBERS[(int) value << 1];
		}

		int len = pos + MAX_LONG_DIGITS - writePos - 1;
		if (writePos == pos) {
			pos += len;
			return;
		}
		while (len-- > 0) {
			buffer[pos++] = buffer[writePos++];
		}
	}

	/**
	 * flush()を行い、このOutputStreamを閉じます。
	 * このOutputStreamがSystem.outなら閉じません。
	 */
	public void close() throws IOException {
		flush();
		if (out != System.out)
			out.close();
	}
}
