import java.io.OutputStream;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import static java.lang.Math.*;

/**
 * {@code ContestPrinter} は、競技プログラミング向けの高速出力ユーティリティです。<br>
 * {@link FastPrinter} を拡張し、配列の改行・空白区切り出力や、関数を利用した変換出力をサポートします。<br>
 * null チェックを導入し、意図しない {@code NullPointerException} を防ぎます。<br>
 */
@SuppressWarnings("unused")
public final class ContestPrinter extends FastPrinter {

	/* ------------------------ コンストラクタ ------------------------ */

	/**
	 * デフォルトの設定で ContestPrinter を初期化します。<br>
	 * バッファ容量: 65536 バイト <br>
	 * OutputStream: System.out <br>
	 * autoFlush: false
	 */
	public ContestPrinter() {
		super();
	}

	/**
	 * 指定された OutputStream を用いて ContestPrinter を初期化します。<br>
	 * バッファ容量: 65536 バイト <br>
	 * autoFlush: false
	 *
	 * @param out 出力先の OutputStream
	 */
	public ContestPrinter(final OutputStream out) {
		super(out);
	}

	/**
	 * 指定されたバッファ容量で ContestPrinter を初期化します。<br>
	 * OutputStream: System.out <br>
	 * autoFlush: false
	 *
	 * @param bufferSize 内部バッファの容量（バイト単位）。64 バイト未満の場合、内部的に 64 バイトに調整されます。
	 */
	public ContestPrinter(final int bufferSize) {
		super(bufferSize);
	}

	/**
	 * autoFlush を指定して ContestPrinter を初期化します。<br>
	 * バッファ容量: 65536 バイト <br>
	 * OutputStream: System.out
	 *
	 * @param autoFlush true の場合、各出力操作後に自動的に {@link #flush()} が呼ばれます。
	 */
	public ContestPrinter(final boolean autoFlush) {
		super(autoFlush);
	}

	/**
	 * 指定された OutputStream とバッファ容量で ContestPrinter を初期化します。<br>
	 * autoFlush: false
	 *
	 * @param out        出力先の OutputStream
	 * @param bufferSize 内部バッファの容量（バイト単位）。64 バイト未満の場合、内部的に 64 バイトに調整されます。
	 */
	public ContestPrinter(final OutputStream out, final int bufferSize) {
		super(out, bufferSize);
	}

	/**
	 * 指定された OutputStream と autoFlush 設定で ContestPrinter を初期化します。<br>
	 * バッファ容量: 65536 バイト
	 *
	 * @param out       出力先の OutputStream
	 * @param autoFlush true を指定すると、各出力操作後に自動的に {@link #flush()} が呼ばれ、出力結果が即座に反映されます。
	 */
	public ContestPrinter(final OutputStream out, final boolean autoFlush) {
		super(out, autoFlush);
	}

	/**
	 * 指定されたバッファ容量と autoFlush 設定で ContestPrinter を初期化します。<br>
	 * OutputStream: System.out
	 *
	 * @param bufferSize 内部バッファの初期容量（バイト単位）。64 バイト未満の場合、内部的に 64 バイトに調整されます。
	 * @param autoFlush  true を指定すると、各出力操作後に自動的に {@link #flush()} が呼ばれ、出力結果が即座に反映されます。
	 */
	public ContestPrinter(final int bufferSize, final boolean autoFlush) {
		super(bufferSize, autoFlush);
	}

	/**
	 * 指定されたバッファ容量、OutputStream、autoFlush 設定で ContestPrinter を初期化します。
	 *
	 * @param out        出力先の OutputStream
	 * @param bufferSize 内部バッファの初期容量（バイト単位）。64 バイト未満の場合、内部的に 64 バイトに調整されます。
	 * @param autoFlush  true を指定すると、各出力操作後に自動的に {@link #flush()} が呼ばれ、出力結果が即座に反映されます。
	 */
	public ContestPrinter(final OutputStream out, final int bufferSize, final boolean autoFlush) {
		super(out, bufferSize, autoFlush);
	}

	/**
	 * Object を {@code toString()} で文字列化し出力します。（改行付き）
	 * 要素が String, Long, Integer, Double, Boolean, Character,
	 * int[], long[], double[], char[], String[], Object[] 型である場合、
	 * その型に沿った出力をします。
	 *
	 * @param o 出力するオブジェクト
	 */
	@Override
	public void println(final Object o) {
		if (o == null) return;
		if (o instanceof String s) {
			println(s);
		} else if (o instanceof Long l) {
			println(l.longValue());
		} else if (o instanceof Integer i) {
			println(i.intValue());
		} else if (o instanceof Double d) {
			println(d.toString());
		} else if (o instanceof Boolean b) {
			println(b.booleanValue());
		} else if (o instanceof Character c) {
			println(c.charValue());
		} else if (o instanceof int[] arr) {
			println(arr);
		} else if (o instanceof long[] arr) {
			println(arr);
		} else if (o instanceof double[] arr) {
			println(arr);
		} else if (o instanceof boolean[] arr) {
			println(arr);
		} else if (o instanceof char[] arr) {
			println(arr);
		} else if (o instanceof String[] arr) {
			println(arr);
		} else if (o instanceof Object[] arr) {
			println(arr);
		} else {
			println(o.toString());
		}
	}

	/**
	 * Object を {@code toString()} で文字列化し出力します。（改行無し）
	 *
	 * @param o 出力するオブジェクト
	 */
	@Override
	public void print(final Object o) {
		if (o == null) return;
		if (o instanceof String s) {
			print(s);
		} else if (o instanceof Long l) {
			print(l.longValue());
		} else if (o instanceof Integer i) {
			print(i.intValue());
		} else if (o instanceof Double d) {
			print(d.toString());
		} else if (o instanceof Boolean b) {
			print(b.booleanValue());
		} else if (o instanceof Character c) {
			print(c.charValue());
		} else if (o instanceof int[] arr) {
			print(arr);
		} else if (o instanceof long[] arr) {
			print(arr);
		} else if (o instanceof double[] arr) {
			print(arr);
		} else if (o instanceof boolean[] arr) {
			print(arr);
		} else if (o instanceof char[] arr) {
			print(arr);
		} else if (o instanceof String[] arr) {
			print(arr);
		} else if (o instanceof Object[] arr) {
			print(arr);
		} else {
			print(o.toString());
		}
	}

	/* ------------------------ ペア出力メソッド（改行付き） ------------------------ */

	/**
	 * 2 つの整数値（int, int）を改行区切りで出力します。（改行付き）
	 *
	 * @param a 出力する int 値
	 * @param b 出力する int 値
	 */
	public void println(final int a, final int b) {
		println(a, b, '\n');
	}

	/**
	 * 2 つの整数値（int, long）を改行区切りで出力します。（改行付き）
	 *
	 * @param a 出力する int 値
	 * @param b 出力する long 値
	 */
	public void println(final int a, final long b) {
		println(a, b, '\n');
	}

	/**
	 * 2 つの整数値（long, int）を改行区切りで出力します。（改行付き）
	 *
	 * @param a 出力する long 値
	 * @param b 出力する int 値
	 */
	public void println(final long a, final int b) {
		println(a, b, '\n');
	}

	/**
	 * 2 つの整数値（int または long）を改行区切りで出力します。（改行付き）
	 *
	 * @param a 出力する long 値
	 * @param b 出力する long 値
	 */
	public void println(final long a, final long b) {
		println(a, b, '\n');
	}

	/**
	 * 2 つの整数値（int または long）を指定した区切り文字で出力します。（改行付き）
	 *
	 * @param a         出力する整数値（int または long）
	 * @param b         出力する整数値（int または long）
	 * @param delimiter 区切り文字
	 */
	public void println(final long a, final long b, final char delimiter) {
		ensureBufferSpace((MAX_LONG_DIGITS << 1) + 2);
		fillBuffer(a);
		buffer[pos++] = (byte) delimiter;
		fillBuffer(b);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
	}

	/* ------------------------ ペア出力メソッド（改行無し） ------------------------ */

	/**
	 * 2 つの整数値（int, int）を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param a 出力する int 値
	 * @param b 出力する int 値
	 */
	public void print(final int a, final int b) {
		print(a, b, ' ');
	}

	/**
	 * 2 つの整数値（int, long）を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param a 出力する int 値
	 * @param b 出力する long 値
	 */
	public void print(final int a, final long b) {
		print(a, b, ' ');
	}

	/**
	 * 2 つの整数値（long, int）を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param a 出力する long 値
	 * @param b 出力する int 値
	 */
	public void print(final long a, final int b) {
		print(a, b, ' ');
	}

	/**
	 * 2 つの整数値（long, long）を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param a 出力する long 値
	 * @param b 出力する long 値
	 */
	public void print(final long a, final long b) {
		print(a, b, ' ');
	}

	/**
	 * 2 つの整数値（int または long）を指定した区切り文字で出力します。（改行無し）
	 *
	 * @param a         出力する整数値（int または long）
	 * @param b         出力する整数値（int または long）
	 * @param delimiter 区切り文字
	 */
	public void print(final long a, final long b, final char delimiter) {
		ensureBufferSpace((MAX_LONG_DIGITS << 1) + 1);
		fillBuffer(a);
		buffer[pos++] = (byte) delimiter;
		fillBuffer(b);
		if (autoFlush) flush();
	}

	/* ------------------------ 小数系メソッド ------------------------ */

	/**
	 * double 値を指定された小数点以下桁数で出力します（四捨五入）。（改行付き）
	 *
	 * @param d 出力する double 値
	 * @param n 小数点以下の桁数
	 */
	public void println(final double d, final int n) {
		print(d, n);
		println();
	}

	/**
	 * double 値を指定された小数点以下桁数で出力します（四捨五入）。（改行無し）
	 *
	 * @param d 出力する double 値
	 * @param n 小数点以下の桁数
	 */
	public void print(double d, int n) {
		if (n == 0) {
			print(round(d));
			return;
		}
		if (d < 0) {
			ensureBufferSpace(1);
			buffer[pos++] = '-';
			d = -d;
		}
		d += pow(10, -n) / 2;
		print((long) d);
		ensureBufferSpace(n + 1);
		buffer[pos++] = '.';
		d -= (long) d;
		while (n-- > 0) {
			d *= 10;
			buffer[pos++] = (byte) ((int) d + '0');
			d -= (int) d;
		}
		if (autoFlush) flush();
	}

	/* ------------------------ 1次元配列系メソッド（改行付き） ------------------------ */

	/**
	 * int 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する int 配列（null の場合何も出力を行いません）
	 */
	public void println(final int[] arr) {
		println(arr, '\n');
	}

	/**
	 * long 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する long 配列（null の場合何も出力を行いません）
	 */
	public void println(final long[] arr) {
		println(arr, '\n');
	}

	/**
	 * double 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する double 配列（null の場合何も出力を行いません）
	 */
	public void println(final double[] arr) {
		println(arr, '\n');
	}

	/**
	 * char 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する char 配列（null の場合何も出力を行いません）
	 */
	public void println(final char[] arr) {
		println(arr, '\n');
	}

	/**
	 * boolean 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する boolean 配列（null の場合何も出力を行いません）
	 */
	public void println(final boolean[] arr) {
		println(arr, '\n');
	}

	/**
	 * String 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する String 配列（null の場合何も出力を行いません）
	 */
	public void println(final String[] arr) {
		println(arr, '\n');
	}

	/**
	 * 可変長の Object 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する Object 配列（null の場合何も出力を行いません）
	 */
	public void println(final Object... arr) {
		if (arr == null) return;
		for (final Object o : arr)
			println(o);
	}

	/**
	 * int 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する int 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final int[] arr, final char delimiter) {
		print(arr, delimiter);
		println();
	}

	/**
	 * long 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する long 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final long[] arr, final char delimiter) {
		print(arr, delimiter);
		println();
	}

	/**
	 * double 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する double 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final double[] arr, final char delimiter) {
		print(arr, delimiter);
		println();
	}

	/**
	 * char 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する char 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final char[] arr, final char delimiter) {
		print(arr, delimiter);
		println();
	}

	/**
	 * boolean 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する boolean 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final boolean[] arr, final char delimiter) {
		print(arr, delimiter);
		println();
	}

	/**
	 * String 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する String 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final String[] arr, final char delimiter) {
		print(arr, delimiter);
		println();
	}

	/* ------------------------ 1次元配列系メソッド（改行無し） ------------------------ */

	/**
	 * int 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する int 配列（null の場合何も出力を行いません）
	 */
	public void print(final int[] arr) {
		print(arr, ' ');
	}

	/**
	 * long 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する long 配列（null の場合何も出力を行いません）
	 */
	public void print(final long[] arr) {
		print(arr, ' ');
	}

	/**
	 * double 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する double 配列（null の場合何も出力を行いません）
	 */
	public void print(final double[] arr) {
		print(arr, ' ');
	}

	/**
	 * char 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する char 配列（null の場合何も出力を行いません）
	 */
	public void print(final char[] arr) {
		print(arr, ' ');
	}

	/**
	 * boolean 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する boolean 配列（null の場合何も出力を行いません）
	 */
	public void print(final boolean[] arr) {
		print(arr, ' ');
	}

	/**
	 * String 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する String 配列（null の場合何も出力を行いません）
	 */
	public void print(final String[] arr) {
		print(arr, ' ');
	}

	/**
	 * 可変長の Object 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する Object 配列（null の場合何も出力を行いません）
	 */
	public void print(final Object... arr) {
		if (arr == null) return;
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			print(' ');
			print(arr[i]);
		}
	}

	/**
	 * int 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する int 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void print(final int[] arr, final char delimiter) {
		if (arr == null) return;
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(MAX_INT_DIGITS + 1);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(arr[i]);
		}
		if (autoFlush) flush();
	}

	/**
	 * long 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する long 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void print(final long[] arr, final char delimiter) {
		if (arr == null) return;
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(MAX_LONG_DIGITS + 1);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(arr[i]);
		}
		if (autoFlush) flush();
	}

	/**
	 * double 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する double 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void print(final double[] arr, final char delimiter) {
		if (arr == null) return;
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0], 16);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(1);
			buffer[pos++] = (byte) delimiter;
			print(arr[i], 16);
		}
		if (autoFlush) flush();
	}

	/**
	 * char 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する char 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void print(final char[] arr, final char delimiter) {
		if (arr == null) return;
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		int i = 1;
		while (i < len) {
			ensureBufferSpace(2);
			int limit = min(buffer.length - pos, len - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) delimiter;
				buffer[pos++] = (byte) arr[i++];
			}
		}
		if (autoFlush) flush();
	}

	/**
	 * boolean 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する boolean 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void print(final boolean[] arr, final char delimiter) {
		if (arr == null) return;
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(4);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(arr[i]);
		}
		if (autoFlush) flush();
	}

	/**
	 * String 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する String 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void print(final String[] arr, final char delimiter) {
		if (arr == null) return;
		final int len = arr.length;
		if (len == 0) return;
		print(arr[0]);
		for (int i = 1; i < len; i++) {
			print(delimiter);
			print(arr[i]);
		}
	}

	/* ------------------------ 1次元配列の関数変換系メソッド（改行付き） ------------------------ */

	/**
	 * int 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する int 配列（null の場合何も出力を行いません）
	 * @param function int を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final int[] arr, final IntFunction<T> function) {
		if (arr == null) return;
		if (function == null) {
			println(arr);
			return;
		}
		for (final int i : arr)
			println(function.apply(i));
	}

	/**
	 * long 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する long 配列（null の場合何も出力を行いません）
	 * @param function long を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final long[] arr, final LongFunction<T> function) {
		if (arr == null) return;
		if (function == null) {
			println(arr);
			return;
		}
		for (final long l : arr)
			println(function.apply(l));
	}

	/**
	 * double 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する double 配列（null の場合何も出力を行いません）
	 * @param function double を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final double[] arr, final DoubleFunction<T> function) {
		if (arr == null) return;
		if (function == null) {
			println(arr);
			return;
		}
		for (final double l : arr)
			println(function.apply(l));
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する char 配列（null の場合何も出力を行いません）
	 * @param function char を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final char[] arr, final Function<Character, T> function) {
		if (arr == null) return;
		if (function == null) {
			println(arr);
			return;
		}
		for (final char c : arr)
			println(function.apply(c));
	}

	/**
	 * boolean 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する boolean 配列（null の場合何も出力を行いません）
	 * @param function boolean を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final boolean[] arr, final Function<Boolean, T> function) {
		if (arr == null) return;
		if (function == null) {
			println(arr);
			return;
		}
		for (final boolean b : arr)
			println(function.apply(b));
	}

	/**
	 * String 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する String 配列（null の場合何も出力を行いません）
	 * @param function String を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final String[] arr, final Function<String, T> function) {
		if (arr == null) return;
		if (function == null) {
			println(arr);
			return;
		}
		for (final String s : arr)
			println(function.apply(s));
	}

	/* ------------------------ 1次元配列の関数変換系メソッド（改行無し） ------------------------ */

	/**
	 * int 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する int 配列（null の場合何も出力を行いません）
	 * @param function int を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void print(final int[] arr, final IntFunction<T> function) {
		if (arr == null) return;
		if (function == null) {
			print(arr, ' ');
			return;
		}
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * long 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する long 配列（null の場合何も出力を行いません）
	 * @param function long を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void print(final long[] arr, final LongFunction<T> function) {
		if (arr == null) return;
		if (function == null) {
			print(arr, ' ');
			return;
		}
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * double 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する double 配列（null の場合何も出力を行いません）
	 * @param function double を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void print(final double[] arr, final DoubleFunction<T> function) {
		if (arr == null) return;
		if (function == null) {
			print(arr, ' ');
			return;
		}
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する char 配列（null の場合何も出力を行いません）
	 * @param function char を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void print(final char[] arr, final Function<Character, T> function) {
		if (arr == null) return;
		if (function == null) {
			print(arr, ' ');
			return;
		}
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * boolean 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する boolean 配列（null の場合何も出力を行いません）
	 * @param function boolean を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void print(final boolean[] arr, final Function<Boolean, T> function) {
		if (arr == null) return;
		if (function == null) {
			print(arr, ' ');
			return;
		}
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/**
	 * String 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する String 配列（null の場合何も出力を行いません）
	 * @param function String を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void print(final String[] arr, final Function<String, T> function) {
		if (arr == null) return;
		if (function == null) {
			print(arr, ' ');
			return;
		}
		final int len = arr.length;
		if (len == 0) return;
		print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(' ');
			print(function.apply(arr[i]));
		}
	}

	/* ------------------------ 2次元配列系メソッド ------------------------ */

	/**
	 * 二次元の int 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の int 配列（null の場合何も出力を行いません）
	 */
	public void println(final int[][] arr2d) {
		println(arr2d, ' ');
	}

	/**
	 * 二次元の long 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の long 配列（null の場合何も出力を行いません）
	 */
	public void println(final long[][] arr2d) {
		println(arr2d, ' ');
	}

	/**
	 * 二次元の double 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の double 配列（null の場合何も出力を行いません）
	 */
	public void println(final double[][] arr2d) {
		println(arr2d, ' ');
	}

	/**
	 * 二次元の char 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の char 配列（null の場合何も出力を行いません）
	 */
	public void println(final char[][] arr2d) {
		println(arr2d, ' ');
	}

	/**
	 * 二次元の boolean 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の boolean 配列（null の場合何も出力を行いません）
	 */
	public void println(final boolean[][] arr2d) {
		println(arr2d, ' ');
	}

	/**
	 * 二次元の String 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の String 配列（null の場合何も出力を行いません）
	 */
	public void println(final String[][] arr2d) {
		println(arr2d, ' ');
	}

	/**
	 * 二次元の Object 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の Object 配列（null の場合何も出力を行いません）
	 */
	public void println(final Object[][] arr2d) {
		println(arr2d, ' ');
	}

	/**
	 * 二次元の int 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の int 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final int[][] arr2d, final char delimiter) {
		if (arr2d == null) return;
		for (final int[] arr : arr2d)
			println(arr, delimiter);
	}

	/**
	 * 二次元の long 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の long 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final long[][] arr2d, final char delimiter) {
		if (arr2d == null) return;
		for (final long[] arr : arr2d)
			println(arr, delimiter);
	}

	/**
	 * 二次元の double 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の double 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final double[][] arr2d, final char delimiter) {
		if (arr2d == null) return;
		for (final double[] arr : arr2d)
			println(arr, delimiter);
	}

	/**
	 * 二次元の char 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の char 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final char[][] arr2d, final char delimiter) {
		if (arr2d == null) return;
		for (final char[] arr : arr2d)
			println(arr, delimiter);
	}

	/**
	 * 二次元の boolean 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の boolean 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final boolean[][] arr2d, final char delimiter) {
		if (arr2d == null) return;
		for (final boolean[] arr : arr2d)
			println(arr, delimiter);
	}

	/**
	 * 二次元の String 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の String 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final String[][] arr2d, final char delimiter) {
		if (arr2d == null) return;
		for (final String[] arr : arr2d)
			println(arr, delimiter);
	}

	/**
	 * 二次元の Object 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の Object 配列（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 */
	public void println(final Object[][] arr2d, final char delimiter) {
		if (arr2d == null) return;
		for (final Object[] arr : arr2d) {
			print(arr[0]);
			for (int i = 1; i < arr.length; i++) {
				print(delimiter);
				print(arr[i]);
			}
			println();
		}
	}

	/* ------------------------ 2次元配列関数変換系メソッド ------------------------ */

	/**
	 * 二次元の int 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の int 配列（null の場合何も出力を行いません）
	 * @param function int を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final int[][] arr2d, final IntFunction<T> function) {
		if (arr2d == null) return;
		if (function == null) {
			println(arr2d);
			return;
		}
		for (final int[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元の long 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の long 配列（null の場合何も出力を行いません）
	 * @param function long を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final long[][] arr2d, final LongFunction<T> function) {
		if (arr2d == null) return;
		if (function == null) {
			println(arr2d);
			return;
		}
		for (final long[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元の double 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の double 配列（null の場合何も出力を行いません）
	 * @param function double を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final double[][] arr2d, final DoubleFunction<T> function) {
		if (arr2d == null) return;
		if (function == null) {
			println(arr2d);
			return;
		}
		for (final double[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元の char 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の char 配列（null の場合何も出力を行いません）
	 * @param function char を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final char[][] arr2d, final LongFunction<T> function) {
		if (arr2d == null) return;
		if (function == null) {
			println(arr2d);
			return;
		}
		for (final char[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元の boolean 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の boolean 配列（null の場合何も出力を行いません）
	 * @param function boolean を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final boolean[][] arr2d, final Function<Boolean, T> function) {
		if (arr2d == null) return;
		if (function == null) {
			println(arr2d);
			return;
		}
		for (final boolean[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/**
	 * 二次元の String 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の String 配列（null の場合何も出力を行いません）
	 * @param function String を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> void println(final String[][] arr2d, final Function<String, T> function) {
		if (arr2d == null) return;
		if (function == null) {
			println(arr2d);
			return;
		}
		for (final String[] arr : arr2d) {
			print(arr, function);
			println();
		}
	}

	/* ------------------------ char配列系メソッド ------------------------ */

	/**
	 * char 配列の各要素を区切り文字無しで出力します。（改行無し）
	 *
	 * @param arr 出力する char 配列（null の場合何も出力を行いません）
	 */
	public void printChars(final char[] arr) {
		if (arr == null) return;
		int i = 0;
		final int len = arr.length;
		while (i < len) {
			ensureBufferSpace(1);
			int limit = min(buffer.length - pos, len - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) arr[i++];
			}
		}
		if (autoFlush) flush();
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、区切り文字無しで出力します。
	 *
	 * @param arr      出力する char 配列（null の場合何も出力を行いません）
	 * @param function char を変換する関数
	 */
	public void printChars(final char[] arr, final Function<Character, Character> function) {
		if (arr == null) return;
		if (function == null) {
			printChars(arr);
			return;
		}
		int i = 0;
		final int len = arr.length;
		while (i < len) {
			ensureBufferSpace(1);
			int limit = min(buffer.length - pos, len - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) function.apply(arr[i++]).charValue();
			}
		}
		if (autoFlush) flush();
	}

	/**
	 * 二次元の char 配列を、各行を区切り文字無しで出力（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の char 配列（null の場合何も出力を行いません）
	 */
	public void printChars(final char[][] arr2d) {
		if (arr2d == null) return;
		for (final char[] arr : arr2d) {
			printChars(arr);
			println();
		}
	}

	/**
	 * 二次元の char 配列の各要素を指定された関数で変換し、各行を区切り文字無しで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の char 配列（null の場合何も出力を行いません）
	 * @param function char を変換する関数
	 */
	public void printChars(final char[][] arr2d, final Function<Character, Character> function) {
		if (arr2d == null) return;
		if (function == null) {
			printChars(arr2d);
			return;
		}
		for (final char[] arr : arr2d) {
			printChars(arr, function);
			println();
		}
	}

	/* ------------------------ Iterableオブジェクト用メソッド（改行付き） ------------------------ */

	/**
	 * イテラブルオブジェクトの各要素を改行区切りで出力（各行末に改行）
	 *
	 * @param iter 出力するイテラブルオブジェクト（null の場合何も出力を行いません）（改行付き）
	 * @param <T>  各要素の型
	 */
	public <T> void println(final Iterable<T> iter) {
		print(iter, '\n');
		println();
	}

	/**
	 * イテラブルオブジェクトの各要素を区切り文字を指定して出力（改行付き）
	 *
	 * @param iter      出力するイテラブルオブジェクト（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 * @param <T>       各要素の型
	 */
	public <T> void println(final Iterable<T> iter, final char delimiter) {
		print(iter, delimiter);
		println();
	}

	/**
	 * イテラブルオブジェクトの各要素を指定された関数で変換し、改行区切りで出力（改行付き）
	 *
	 * @param iter     出力するイテラブルオブジェクト（null の場合何も出力を行いません）
	 * @param function 変換する関数
	 * @param <T>      変換前の型
	 * @param <U>      変換後の型
	 */
	public <T, U> void println(final Iterable<T> iter, final Function<T, U> function) {
		print(iter, function, '\n');
		println();
	}

	/* ------------------------ Iterableオブジェクト用メソッド（改行無し） ------------------------ */

	/**
	 * イテラブルオブジェクトの各要素を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param iter 出力するイテラブルオブジェクト（null の場合何も出力を行いません）（改行無し）
	 * @param <T>  各要素の型
	 */
	public <T> void print(final Iterable<T> iter) {
		print(iter, ' ');
	}

	/**
	 * イテラブルオブジェクトの各要素を区切り文字を指定して出力（改行無し）
	 *
	 * @param iter      出力するイテラブルオブジェクト（null の場合何も出力を行いません）
	 * @param delimiter 区切り文字
	 * @param <T>       各要素の型
	 */
	public <T> void print(final Iterable<T> iter, final char delimiter) {
		if (iter == null) return;
		boolean first = true;
		for (final T t : iter) {
			if (first) {
				first = false;
			} else {
				ensureBufferSpace(1);
				buffer[pos++] = (byte) delimiter;
			}
			print(t);
		}
	}

	/**
	 * イテラブルオブジェクトの各要素を指定された関数で変換し、半角スペース区切りで出力（改行無し）
	 *
	 * @param iter     出力するイテラブルオブジェクト（null の場合何も出力を行いません）
	 * @param function 変換する関数
	 * @param <T>      変換前の型
	 * @param <U>      変換後の型
	 */
	public <T, U> void print(final Iterable<T> iter, final Function<T, U> function) {
		print(iter, function, ' ');
	}

	/**
	 * イテラブルオブジェクトの各要素を指定された関数で変換し、区切り文字を指定して出力（改行無し）
	 *
	 * @param iter      出力するイテラブルオブジェクト（null の場合何も出力を行いません）
	 * @param function  変換する関数
	 * @param delimiter 区切り文字
	 * @param <T>       変換前の型
	 * @param <U>       変換後の型
	 */
	public <T, U> void print(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
		if (iter == null) return;
		if (function == null) {
			print(iter, delimiter);
			return;
		}
		boolean first = true;
		for (final T t : iter) {
			if (first) {
				first = false;
			} else {
				ensureBufferSpace(1);
				buffer[pos++] = (byte) delimiter;
			}
			print(function.apply(t));
		}
	}
}