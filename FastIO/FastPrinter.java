import java.util.Locale;
import java.io.IOException;
import java.io.OutputStream;

import static java.lang.Math.*;

/**
 * 標準出力を高速に処理するクラスです。
 * ※注意：このクラスは内部バッファが満杯になると自動的に出力されますが、
 * 処理の途中で結果をすぐに反映させる必要がある場合は、明示的に flush() を呼び出す必要があります。
 */
@SuppressWarnings("unused")
public final class FastPrinter {
	private static final int DEFAULT_BUFFER_SIZE = 65536;
	private static final OutputStream out = System.out;
	private final byte[] buffer;
	private int pos = 0;

	// ------------------ コンストラクタ ------------------

	/**
	 * デフォルトの設定でFastPrinterを初期化します。<p>
	 * バッファ容量: 65536 <p>
	 * autoFlush: false（バッファが満杯になるとflushされます。）
	 */
	public FastPrinter() {
		this(DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 指定されたバッファ容量でFastPrinterを初期化します。<p>
	 * autoFlush: false（バッファが満杯になるとflushされます。）
	 *
	 * @param size バッファの初期容量（文字単位）
	 */
	public FastPrinter(int size) {
		buffer = new byte[max(size, 64)];
	}

	// --------------- 出力メソッド（改行付き） ---------------

	/**
	 * 改行のみ出力
	 */
	public void println() {
		if (pos >= buffer.length)
			flushBuffer();
		buffer[pos++] = '\n';
	}

	/**
	 * Objectを出力（改行付き）
	 */
	public void println(Object o) {
		println(o.toString());
	}

	/**
	 * charを出力（改行付き）
	 */
	public void println(char c) {
		if (pos + 1 >= buffer.length)
			flushBuffer();
		buffer[pos++] = (byte) c;
		buffer[pos++] = '\n';
	}

	/**
	 * Stringを出力（改行付き）
	 */
	public void println(String s) {
		print(s);
		println();
	}

	/**
	 * boolean を出力（true→"Yes", false→"No", 改行付き）
	 */
	public void println(boolean b) {
		if (pos + 3 >= buffer.length)
			flushBuffer();
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
	 * intを出力（改行付き）
	 */
	public void println(int i) {
		if (pos + 11 >= buffer.length)
			flushBuffer();
		fillBuffer(i);
		buffer[pos++] = '\n';
	}

	/**
	 * longを出力（改行付き）
	 */
	public void println(long l) {
		if (pos + 20 >= buffer.length)
			flushBuffer();
		fillBuffer(l);
		buffer[pos++] = '\n';
	}

	/**
	 * doubleを出力（改行付き）
	 */
	public void println(double d) {
		println(Double.toString(d));
	}

	/**
	 * 指定された桁数で double を出力（改行付き）
	 * 小数点以下 n 桁に丸め（四捨五入）
	 */
	public void println(double d, int n) {
		print(d, n);
		println();
	}

	/**
	 * 2 つの整数（int, int）をそれぞれ改行して出力
	 */
	public void println(int a, int b) {
		println(a);
		println(b);
	}

	/**
	 * 2 つの整数（int, long）をそれぞれ改行して出力
	 */
	public void println(int a, long b) {
		println(a);
		println(b);
	}

	/**
	 * 2 つの整数（long, int）をそれぞれ改行して出力
	 */
	public void println(long a, int b) {
		println(a);
		println(b);
	}

	/**
	 * 2 つの整数（long, long）をそれぞれ改行して出力
	 */
	public void println(long a, long b) {
		println(a);
		println(b);
	}

	/**
	 * 可変長の Object 配列の各要素を改行区切りで出力
	 */
	public void println(Object... o) {
		for (Object x : o)
			println(x.toString());
	}

	/**
	 * String 配列の各要素を改行区切りで出力
	 */
	public void println(String[] s) {
		for (String x : s)
			println(x);
	}

	/**
	 * char 配列の各要素を改行区切りで出力
	 */
	public void println(char[] c) {
		int i = 0;
		while (i < c.length) {
			if (pos >= buffer.length)
				flushBuffer();
			int limit = min((buffer.length - pos) >> 1, c.length - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) c[i++];
				buffer[pos++] = '\n';
			}
		}
	}

	/**
	 * int 配列の各要素を改行区切りで出力
	 */
	public void println(int[] a) {
		for (int x : a) {
			println(x);
		}
	}

	/**
	 * long 配列の各要素を改行区切りで出力
	 */
	public void println(long[] a) {
		for (long x : a) {
			println(x);
		}
	}

	/**
	 * 二次元 Object 配列を、各行を半角スペース区切りで出力（行末は改行）
	 */
	public void println(Object[][] o) {
		for (Object[] x : o) {
			print(x);
			println();
		}
	}

	/**
	 * 二次元 char 配列を、各行を半角スペース区切りで出力（行末は改行）
	 */
	public void println(char[][] c) {
		for (char[] x : c) {
			print(x);
			println();
		}
	}

	/**
	 * 二次元 int 配列を、各行を半角スペース区切りで出力（行末は改行）
	 */
	public void println(int[][] a) {
		for (int[] x : a) {
			print(x);
			println();
		}
	}


	/**
	 * 二次元 long 配列を、各行を半角スペース区切りで出力（行末は改行）
	 */
	public void println(long[][] a) {
		for (long[] x : a) {
			print(x);
			println();
		}
	}

	/**
	 * 二次元 char 配列を、各行を区切り文字無しで出力（各行末に改行）
	 */
	public void printChars(char[][] c) {
		for (char[] x : c) {
			printChars(x);
			println();
		}
	}

	//---------------- 出力メソッド（改行無し） ----------------

	/**
	 * Object を出力（改行無し）
	 */
	public void print(Object o) {
		print(o.toString());
	}

	/**
	 * char を出力（改行無し）
	 */
	public void print(char c) {
		if (pos >= buffer.length)
			flushBuffer();
		buffer[pos++] = (byte) c;
	}

	/**
	 * String を出力（改行無し）
	 */
	public void print(String s) {
		int i = 0;
		while (i < s.length()) {
			if (pos >= buffer.length)
				flushBuffer();
			int limit = min(buffer.length - pos, s.length() - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) s.charAt(i++);
			}
		}
	}

	/**
	 * int を出力（改行無し）
	 */
	public void print(int i) {
		if (pos + 10 >= buffer.length)
			flushBuffer();
		fillBuffer(i);
	}

	/**
	 * long を出力（改行無し）
	 */
	public void print(long l) {
		if (pos + 19 >= buffer.length)
			flushBuffer();
		fillBuffer(l);
	}

	/**
	 * double を出力（改行無し）
	 */
	public void print(double d) {
		print(Double.toString(d));
	}

	/**
	 * boolean を出力（true→"Yes", false→"No", 改行無し）
	 */
	public void print(boolean b) {
		if (pos + 2 >= buffer.length)
			flushBuffer();
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
	 * 指定された桁数で double を出力（改行無し）
	 * 小数点以下 n 桁に丸め（四捨五入）
	 */
	public void print(double a, int n) {
		if (n == 0) {
			print(round(a));
			return;
		}
		if (a < 0) {
			print('-');
			a = -a;
		}
		a += pow(10, -n) / 2;
		print((long) a);
		if (pos + n >= buffer.length)
			flushBuffer();
		buffer[pos++] = '.';
		a -= (long) a;
		while (n-- > 0) {
			a *= 10;
			buffer[pos++] = (byte) ((int) a + '0');
			a -= (int) a;
		}
	}

	/**
	 * 2 つの整数（int, int）を半角スペース区切りで出力（改行無し）
	 */
	public void print(int a, int b) {
		print(a);
		print(' ');
		print(b);
	}

	/**
	 * 2 つの整数（int, long）を半角スペース区切りで出力（改行無し）
	 */
	public void print(int a, long b) {
		print(a);
		print(' ');
		print(b);
	}

	/**
	 * 2 つの整数（long, int）を半角スペース区切りで出力（改行無し）
	 */
	public void print(long a, int b) {
		print(a);
		print(' ');
		print(b);
	}

	/**
	 * 2 つの整数（long, long）を半角スペース区切りで出力（改行無し）
	 */
	public void print(long a, long b) {
		print(a);
		print(' ');
		print(b);
	}

	/**
	 * 可変長の Object 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(Object... o) {
		if (o.length == 0) return;
		print(o[0]);
		for (int i = 1; i < o.length; i++) {
			print(' ');
			print(o[i]);
		}
	}

	/**
	 * String 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(String[] s) {
		print(s[0]);
		for (int i = 1; i < s.length; i++) {
			print(' ');
			print(s[i]);
		}
	}

	/**
	 * char 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(char[] c) {
		print(c[0]);
		for (int i = 1; i < c.length; i++) {
			if (pos + 1 >= buffer.length)
				flushBuffer();
			buffer[pos++] = ' ';
			buffer[pos++] = (byte) c[i];
		}
	}

	/**
	 * int 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(int[] a) {
		print(a[0]);
		for (int i = 1; i < a.length; i++) {
			if (pos + 11 >= buffer.length)
				flushBuffer();
			buffer[pos++] = ' ';
			fillBuffer(a[i]);
		}
	}

	/**
	 * long 配列の各要素を半角スペース区切りで出力（改行無し）
	 */
	public void print(long[] a) {
		print(a[0]);
		for (int i = 1; i < a.length; i++) {
			if (pos + 20 >= buffer.length)
				flushBuffer();
			buffer[pos++] = ' ';
			fillBuffer(a[i]);
		}
	}

	/**
	 * char 配列の各要素を区切り文字無しで出力（改行無し）
	 */
	public void printChars(char[] c) {
		int i = 0;
		while (i < c.length) {
			if (pos >= buffer.length)
				flushBuffer();
			int limit = min(buffer.length - pos, c.length - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) c[i++];
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
	 * 現在のバッファに保持している全ての出力内容を標準出力に書き出し、バッファをクリアします。
	 */
	public void flush() {
		try {
			if (pos > 0) {
				out.write(buffer, 0, pos);
			}
			out.flush();
		} catch (IOException ignored) {
		}
		pos = 0;
	}

	/**
	 * 指定された int 値を文字列に変換し、buffer に格納します。
	 * 10進数表現により、負の値の場合は先頭に '-' を付けます。
	 *
	 * @param i int
	 */
	private void fillBuffer(int i) {
		int p = 0;
		byte[] tmp;
		if (i == Integer.MIN_VALUE) {
			buffer[pos++] = '-';
			tmp = new byte[]{'8', '4', '6', '3', '8', '4', '7', '4', '1', '2'};
			p = 10;
		} else {
			if (i < 0) {
				buffer[pos++] = '-';
				i = -i;
			}
			tmp = new byte[10];
			do {
				tmp[p++] = (byte) ((i % 10) + '0');
			} while ((i /= 10) > 0);
		}
		while (p-- > 0) {
			buffer[pos++] = tmp[p];
		}
	}

	/**
	 * 指定された long 値を文字列に変換し、buffer に格納します。
	 * 10進数表現により、負の値の場合は先頭に '-' を付けます。
	 *
	 * @param l long
	 */
	private void fillBuffer(long l) {
		int p = 0;
		byte[] tmp;
		if (l == Long.MIN_VALUE) {
			buffer[pos++] = '-';
			tmp = new byte[]{'8', '0', '8', '5', '7', '7', '4', '5', '8', '6', '3', '0', '2', '7', '3', '3', '2', '2', '9'};
			p = 19;
		} else {
			if (l < 0) {
				buffer[pos++] = '-';
				l = -l;
			}
			tmp = new byte[19];
			do {
				tmp[p++] = (byte) ((l % 10) + '0');
			} while ((l /= 10) > 0);
		}
		while (p-- > 0) {
			buffer[pos++] = tmp[p];
		}
	}

	/**
	 * 現在の buffer の内容を標準出力に書き出し、バッファをクリアします。
	 */
	private void flushBuffer() {
		try {
			out.write(buffer, 0, pos);
			pos = 0;
		} catch (IOException ignore) {
		}
	}

}
