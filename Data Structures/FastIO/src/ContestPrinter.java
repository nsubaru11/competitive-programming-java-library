import java.io.OutputStream;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.DoubleFunction;
import java.util.function.IntUnaryOperator;


import static java.lang.Math.min;
import static java.lang.Math.round;

/**
 * {@code ContestPrinter} は、競技プログラミング向けの高速出力ユーティリティです。<br>
 * {@link FastPrinter} を拡張し、配列の改行・空白区切り出力や、関数を利用した変換出力をサポートします。<br>
 * null チェックを導入していないため、引数にnullを渡すと {@code NullPointerException} が起こる可能性があります。<br>
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
	public ContestPrinter println(final Object o) {
		if (o == null) return this;
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
		return this;
	}

	/**
	 * Object を {@code toString()} で文字列化し出力します。（改行無し）
	 *
	 * @param o 出力するオブジェクト
	 */
	@Override
	public ContestPrinter print(final Object o) {
		if (o == null) return this;
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
		return this;
	}

	/* ------------------------ ペア出力メソッド（改行付き） ------------------------ */

	/**
	 * 2 つの整数値（int, int）を改行区切りで出力します。（改行付き）
	 *
	 * @param a 出力する int 値
	 * @param b 出力する int 値
	 */
	public ContestPrinter println(final int a, final int b) {
		return println(a, b, '\n');
	}

	/**
	 * 2 つの整数値（int, long）を改行区切りで出力します。（改行付き）
	 *
	 * @param a 出力する int 値
	 * @param b 出力する long 値
	 */
	public ContestPrinter println(final int a, final long b) {
		return println(a, b, '\n');
	}

	/**
	 * 2 つの整数値（long, int）を改行区切りで出力します。（改行付き）
	 *
	 * @param a 出力する long 値
	 * @param b 出力する int 値
	 */
	public ContestPrinter println(final long a, final int b) {
		return println(a, b, '\n');
	}

	/**
	 * 2 つの整数値（int または long）を改行区切りで出力します。（改行付き）
	 *
	 * @param a 出力する long 値
	 * @param b 出力する long 値
	 */
	public ContestPrinter println(final long a, final long b) {
		return println(a, b, '\n');
	}

	/**
	 * 2 つの整数値（int または long）を指定した区切り文字で出力します。（改行付き）
	 *
	 * @param a         出力する整数値（int または long）
	 * @param b         出力する整数値（int または long）
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final long a, final long b, final char delimiter) {
		ensureBufferSpace((MAX_LONG_DIGITS << 1) + 2);
		fillBuffer(a);
		buffer[pos++] = (byte) delimiter;
		fillBuffer(b);
		buffer[pos++] = '\n';
		if (autoFlush) flush();
		return this;
	}

	/* ------------------------ ペア出力メソッド（改行無し） ------------------------ */

	/**
	 * 2 つの整数値（int, int）を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param a 出力する int 値
	 * @param b 出力する int 値
	 */
	public ContestPrinter print(final int a, final int b) {
		return print(a, b, ' ');
	}

	/**
	 * 2 つの整数値（int, long）を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param a 出力する int 値
	 * @param b 出力する long 値
	 */
	public ContestPrinter print(final int a, final long b) {
		return print(a, b, ' ');
	}

	/**
	 * 2 つの整数値（long, int）を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param a 出力する long 値
	 * @param b 出力する int 値
	 */
	public ContestPrinter print(final long a, final int b) {
		return print(a, b, ' ');
	}

	/**
	 * 2 つの整数値（long, long）を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param a 出力する long 値
	 * @param b 出力する long 値
	 */
	public ContestPrinter print(final long a, final long b) {
		return print(a, b, ' ');
	}

	/**
	 * 2 つの整数値（int または long）を指定した区切り文字で出力します。（改行無し）
	 *
	 * @param a         出力する整数値（int または long）
	 * @param b         出力する整数値（int または long）
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter print(final long a, final long b, final char delimiter) {
		ensureBufferSpace((MAX_LONG_DIGITS << 1) + 1);
		fillBuffer(a);
		buffer[pos++] = (byte) delimiter;
		fillBuffer(b);
		if (autoFlush) flush();
		return this;
	}

	/* ------------------------ 小数系メソッド ------------------------ */

	/**
	 * double 値を指定された小数点以下桁数で出力します（四捨五入）。（改行付き）
	 *
	 * @param d 出力する double 値
	 * @param n 小数点以下の桁数
	 */
	public ContestPrinter println(final double d, final int n) {
		print(d, n).println();
		return this;
	}

	/**
	 * double 値を指定された小数点以下桁数で出力します（四捨五入）。（改行無し）
	 *
	 * @param d 出力する double 値
	 * @param n 小数点以下の桁数
	 */
	public ContestPrinter print(double d, int n) {
		if (n <= 0) {
			print(round(d));
			return this;
		}
		if (d >= 0) {
			// fall through
		} else {
			ensureBufferSpace(1);
			buffer[pos++] = '-';
			d = -d;
		}
		if (n > 18) n = 18;
		long intPart = (long) d;
		long fracPart = (long) ((d - intPart) * POW10[n]);
		print(intPart);
		int digits = n - countDigits(-fracPart);
		ensureBufferSpace(digits + 1);
		buffer[pos++] = '.';
		while (digits-- > 0) buffer[pos++] = '0';
		print(fracPart);
		return this;
	}

	/* ------------------------ 1次元配列系メソッド（改行付き） ------------------------ */

	/**
	 * int 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する int 配列
	 */
	public ContestPrinter println(final int[] arr) {
		return println(arr, '\n');
	}

	/**
	 * long 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する long 配列
	 */
	public ContestPrinter println(final long[] arr) {
		return println(arr, '\n');
	}

	/**
	 * double 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する double 配列
	 */
	public ContestPrinter println(final double[] arr) {
		return println(arr, '\n');
	}

	/**
	 * char 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する char 配列
	 */
	public ContestPrinter println(final char[] arr) {
		return println(arr, '\n');
	}

	/**
	 * boolean 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する boolean 配列
	 */
	public ContestPrinter println(final boolean[] arr) {
		return println(arr, '\n');
	}

	/**
	 * String 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する String 配列
	 */
	public ContestPrinter println(final String[] arr) {
		return println(arr, '\n');
	}

	/**
	 * 可変長の Object 配列の各要素を改行区切りで出力します。（改行付き）
	 *
	 * @param arr 出力する Object 配列
	 */
	public ContestPrinter println(final Object... arr) {
		for (final Object o : arr) println(o);
		return this;
	}

	/**
	 * int 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する int 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final int[] arr, final char delimiter) {
		print(arr, delimiter).println();
		return this;
	}

	/**
	 * long 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する long 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final long[] arr, final char delimiter) {
		print(arr, delimiter).println();
		return this;
	}

	/**
	 * double 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する double 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final double[] arr, final char delimiter) {
		print(arr, delimiter).println();
		return this;
	}

	/**
	 * char 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する char 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final char[] arr, final char delimiter) {
		print(arr, delimiter).println();
		return this;
	}

	/**
	 * boolean 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する boolean 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final boolean[] arr, final char delimiter) {
		print(arr, delimiter).println();
		return this;
	}

	/**
	 * String 配列の各要素を指定の区切り文字で出力します。（改行付き）
	 *
	 * @param arr       出力する String 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final String[] arr, final char delimiter) {
		print(arr, delimiter).println();
		return this;
	}

	/* ------------------------ 1次元配列系メソッド（改行無し） ------------------------ */

	/**
	 * int 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する int 配列
	 */
	public ContestPrinter print(final int[] arr) {
		return print(arr, ' ');
	}

	/**
	 * long 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する long 配列
	 */
	public ContestPrinter print(final long[] arr) {
		return print(arr, ' ');
	}

	/**
	 * double 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する double 配列
	 */
	public ContestPrinter print(final double[] arr) {
		return print(arr, ' ');
	}

	/**
	 * char 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する char 配列
	 */
	public ContestPrinter print(final char[] arr) {
		return print(arr, ' ');
	}

	/**
	 * boolean 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する boolean 配列
	 */
	public ContestPrinter print(final boolean[] arr) {
		return print(arr, ' ');
	}

	/**
	 * String 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する String 配列
	 */
	public ContestPrinter print(final String[] arr) {
		return print(arr, ' ');
	}

	/**
	 * 可変長の Object 配列の各要素を半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する Object 配列
	 */
	public ContestPrinter print(final Object... arr) {
		final int len = arr.length;
		if (len > 0) print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(1);
			buffer[pos++] = ' ';
			print(arr[i]);
		}
		return this;
	}

	/**
	 * int 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する int 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter print(final int[] arr, final char delimiter) {
		final int len = arr.length;
		if (len > 0) print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(MAX_INT_DIGITS + 1);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * long 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する long 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter print(final long[] arr, final char delimiter) {
		final int len = arr.length;
		if (len > 0) print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(MAX_LONG_DIGITS + 1);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * double 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する double 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter print(final double[] arr, final char delimiter) {
		final int len = arr.length;
		if (len > 0) print(arr[0], 16);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(1);
			buffer[pos++] = (byte) delimiter;
			print(arr[i], 16);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * char 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する char 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter print(final char[] arr, final char delimiter) {
		final int len = arr.length;
		if (len > 0) print(arr[0]);
		int i = 1;
		while (i < len) {
			ensureBufferSpace(2);
			int limit = min((BUFFER_SIZE - pos) >> 1, len - i);
			while (limit-- > 0) {
				buffer[pos++] = (byte) delimiter;
				buffer[pos++] = (byte) arr[i++];
			}
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * boolean 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する boolean 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter print(final boolean[] arr, final char delimiter) {
		final int len = arr.length;
		if (len > 0) print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(4);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * String 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する String 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter print(final String[] arr, final char delimiter) {
		final int len = arr.length;
		if (len > 0) print(arr[0]);
		for (int i = 1; i < len; i++) print(delimiter).print(arr[i]);
		return this;
	}

	/* ------------------------ 1次元配列の関数変換系メソッド（改行付き） ------------------------ */

	/**
	 * int 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する int 配列
	 * @param function int を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final int[] arr, final IntFunction<T> function) {
		for (final int i : arr) println(function.apply(i));
		return this;
	}

	/**
	 * long 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する long 配列
	 * @param function long を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final long[] arr, final LongFunction<T> function) {
		for (final long l : arr) println(function.apply(l));
		return this;
	}

	/**
	 * double 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する double 配列
	 * @param function double を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final double[] arr, final DoubleFunction<T> function) {
		for (final double l : arr) println(function.apply(l));
		return this;
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する char 配列
	 * @param function char を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final char[] arr, final IntFunction<T> function) {
		for (final char c : arr) println(function.apply(c));
		return this;
	}

	/**
	 * boolean 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する boolean 配列
	 * @param function boolean を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final boolean[] arr, final Function<Boolean, T> function) {
		for (final boolean b : arr) println(function.apply(b));
		return this;
	}

	/**
	 * String 配列の各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param arr      出力する String 配列
	 * @param function String を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final String[] arr, final Function<String, T> function) {
		for (final String s : arr) println(function.apply(s));
		return this;
	}

	/* ------------------------ 1次元配列の関数変換系メソッド（改行無し） ------------------------ */

	/**
	 * int 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する int 配列
	 * @param function int を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter print(final int[] arr, final IntFunction<T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(1);
			buffer[pos++] = ' ';
			print(function.apply(arr[i]));
		}
		return this;
	}

	/**
	 * long 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する long 配列
	 * @param function long を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter print(final long[] arr, final LongFunction<T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(1);
			buffer[pos++] = ' ';
			print(function.apply(arr[i]));
		}
		return this;
	}

	/**
	 * double 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する double 配列
	 * @param function double を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter print(final double[] arr, final DoubleFunction<T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(1);
			buffer[pos++] = ' ';
			print(function.apply(arr[i]));
		}
		return this;
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する char 配列
	 * @param function char を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter print(final char[] arr, final IntFunction<T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(1);
			buffer[pos++] = ' ';
			print(function.apply(arr[i]));
		}
		return this;
	}

	/**
	 * boolean 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する boolean 配列
	 * @param function boolean を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter print(final boolean[] arr, final Function<Boolean, T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(1);
			buffer[pos++] = ' ';
			print(function.apply(arr[i]));
		}
		return this;
	}

	/**
	 * String 配列の各要素を指定された関数で変換し、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr      出力する String 配列
	 * @param function String を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter print(final String[] arr, final Function<String, T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			ensureBufferSpace(1);
			buffer[pos++] = ' ';
			print(function.apply(arr[i]));
		}
		return this;
	}

	/* ------------------------ 2次元配列系メソッド ------------------------ */

	/**
	 * 二次元の int 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の int 配列
	 */
	public ContestPrinter println(final int[][] arr2d) {
		return println(arr2d, ' ');
	}

	/**
	 * 二次元の long 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の long 配列
	 */
	public ContestPrinter println(final long[][] arr2d) {
		return println(arr2d, ' ');
	}

	/**
	 * 二次元の double 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の double 配列
	 */
	public ContestPrinter println(final double[][] arr2d) {
		return println(arr2d, ' ');
	}

	/**
	 * 二次元の char 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の char 配列
	 */
	public ContestPrinter println(final char[][] arr2d) {
		return println(arr2d, ' ');
	}

	/**
	 * 二次元の boolean 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の boolean 配列
	 */
	public ContestPrinter println(final boolean[][] arr2d) {
		return println(arr2d, ' ');
	}

	/**
	 * 二次元の String 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の String 配列
	 */
	public ContestPrinter println(final String[][] arr2d) {
		return println(arr2d, ' ');
	}

	/**
	 * 二次元の Object 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の Object 配列
	 */
	public ContestPrinter println(final Object[][] arr2d) {
		return println(arr2d, ' ');
	}

	/**
	 * 二次元の int 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の int 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final int[][] arr2d, final char delimiter) {
		for (final int[] arr : arr2d) println(arr, delimiter);
		return this;
	}

	/**
	 * 二次元の long 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の long 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final long[][] arr2d, final char delimiter) {
		for (final long[] arr : arr2d) println(arr, delimiter);
		return this;
	}

	/**
	 * 二次元の double 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の double 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final double[][] arr2d, final char delimiter) {
		for (final double[] arr : arr2d) println(arr, delimiter);
		return this;
	}

	/**
	 * 二次元の char 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の char 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final char[][] arr2d, final char delimiter) {
		for (final char[] arr : arr2d) println(arr, delimiter);
		return this;
	}

	/**
	 * 二次元の boolean 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の boolean 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final boolean[][] arr2d, final char delimiter) {
		for (final boolean[] arr : arr2d) println(arr, delimiter);
		return this;
	}

	/**
	 * 二次元の String 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の String 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final String[][] arr2d, final char delimiter) {
		for (final String[] arr : arr2d) println(arr, delimiter);
		return this;
	}

	/**
	 * 二次元の Object 配列を、各行を指定した区切り文字で出力します。（各行末に改行）
	 *
	 * @param arr2d     出力する二次元の Object 配列
	 * @param delimiter 区切り文字
	 */
	public ContestPrinter println(final Object[][] arr2d, final char delimiter) {
		for (final Object[] arr : arr2d) {
			int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = (byte) delimiter;
				print(arr[i]);
			}
			println();
		}
		return this;
	}

	/* ------------------------ 2次元配列関数変換系メソッド ------------------------ */

	/**
	 * 二次元の int 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の int 配列
	 * @param function int を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final int[][] arr2d, final IntFunction<T> function) {
		for (final int[] arr : arr2d) print(arr, function).println();
		return this;
	}

	/**
	 * 二次元の long 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の long 配列
	 * @param function long を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final long[][] arr2d, final LongFunction<T> function) {
		for (final long[] arr : arr2d) print(arr, function).println();
		return this;
	}

	/**
	 * 二次元の double 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の double 配列
	 * @param function double を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final double[][] arr2d, final DoubleFunction<T> function) {
		for (final double[] arr : arr2d) print(arr, function).println();
		return this;
	}

	/**
	 * 二次元の char 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の char 配列
	 * @param function char を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final char[][] arr2d, final IntFunction<T> function) {
		for (final char[] arr : arr2d) print(arr, function).println();
		return this;
	}

	/**
	 * 二次元の boolean 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の boolean 配列
	 * @param function boolean を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final boolean[][] arr2d, final Function<Boolean, T> function) {
		for (final boolean[] arr : arr2d) print(arr, function).println();
		return this;
	}

	/**
	 * 二次元の String 配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の String 配列
	 * @param function String を変換する関数
	 * @param <T>      変換後の型
	 */
	public <T> ContestPrinter println(final String[][] arr2d, final Function<String, T> function) {
		for (final String[] arr : arr2d) print(arr, function).println();
		return this;
	}

	/* ------------------------ char配列系メソッド ------------------------ */

	/**
	 * char 配列の各要素を区切り文字無しで出力します。（改行無し）
	 *
	 * @param arr 出力する char 配列
	 */
	public ContestPrinter printChars(final char[] arr) {
		int i = 0;
		final int len = arr.length;
		while (i < len) {
			ensureBufferSpace(1);
			int limit = min(BUFFER_SIZE - pos, len - i);
			while (limit-- > 0) buffer[pos++] = (byte) arr[i++];
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、区切り文字無しで出力します。
	 *
	 * @param arr      出力する char 配列
	 * @param function char を変換する関数
	 */
	public ContestPrinter printChars(final char[] arr, final IntUnaryOperator function) {
		int i = 0;
		final int len = arr.length;
		while (i < len) {
			ensureBufferSpace(1);
			int limit = min(BUFFER_SIZE - pos, len - i);
			while (limit-- > 0) buffer[pos++] = (byte) function.applyAsInt(arr[i++]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * 二次元の char 配列を、各行を区切り文字無しで出力（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の char 配列
	 */
	public ContestPrinter printChars(final char[][] arr2d) {
		for (final char[] arr : arr2d) printChars(arr).println();
		return this;
	}

	/**
	 * 二次元の char 配列の各要素を指定された関数で変換し、各行を区切り文字無しで出力（各行末に改行）
	 *
	 * @param arr2d    出力する二次元の char 配列
	 * @param function char を変換する関数
	 */
	public ContestPrinter printChars(final char[][] arr2d, final IntUnaryOperator function) {
		for (final char[] arr : arr2d) printChars(arr, function).println();
		return this;
	}

	/* ------------------------ Iterableオブジェクト用メソッド（改行付き） ------------------------ */

	/**
	 * イテラブルオブジェクトの各要素を改行区切りで出力（各行末に改行）
	 *
	 * @param iter 出力するイテラブルオブジェクト（改行付き）
	 * @param <T>  各要素の型
	 */
	public <T> ContestPrinter println(final Iterable<T> iter) {
		print(iter, '\n').println();
		return this;
	}

	/**
	 * イテラブルオブジェクトの各要素を区切り文字を指定して出力（改行付き）
	 *
	 * @param iter      出力するイテラブルオブジェクト
	 * @param delimiter 区切り文字
	 * @param <T>       各要素の型
	 */
	public <T> ContestPrinter println(final Iterable<T> iter, final char delimiter) {
		print(iter, delimiter).println();
		return this;
	}

	/**
	 * イテラブルオブジェクトの各要素を指定された関数で変換し、改行区切りで出力（改行付き）
	 *
	 * @param iter     出力するイテラブルオブジェクト
	 * @param function 変換する関数
	 * @param <T>      変換前の型
	 * @param <U>      変換後の型
	 */
	public <T, U> ContestPrinter println(final Iterable<T> iter, final Function<T, U> function) {
		print(iter, function, '\n').println();
		return this;
	}

	/* ------------------------ Iterableオブジェクト用メソッド（改行無し） ------------------------ */

	/**
	 * イテラブルオブジェクトの各要素を半角スペース区切りで出力（各行末に改行）
	 *
	 * @param iter 出力するイテラブルオブジェクト（改行無し）
	 * @param <T>  各要素の型
	 */
	public <T> ContestPrinter print(final Iterable<T> iter) {
		return print(iter, ' ');
	}

	/**
	 * イテラブルオブジェクトの各要素を区切り文字を指定して出力（改行無し）
	 *
	 * @param iter      出力するイテラブルオブジェクト
	 * @param delimiter 区切り文字
	 * @param <T>       各要素の型
	 */
	public <T> ContestPrinter print(final Iterable<T> iter, final char delimiter) {
		final Iterator<T> it = iter.iterator();
		if (it.hasNext()) print(it.next());
		while (it.hasNext()) {
			ensureBufferSpace(1);
			buffer[pos++] = (byte) delimiter;
			print(it.next());
		}
		return this;
	}

	/**
	 * イテラブルオブジェクトの各要素を指定された関数で変換し、半角スペース区切りで出力（改行無し）
	 *
	 * @param iter     出力するイテラブルオブジェクト
	 * @param function 変換する関数
	 * @param <T>      変換前の型
	 * @param <U>      変換後の型
	 */
	public <T, U> ContestPrinter print(final Iterable<T> iter, final Function<T, U> function) {
		return print(iter, function, ' ');
	}

	/**
	 * イテラブルオブジェクトの各要素を指定された関数で変換し、区切り文字を指定して出力（改行無し）
	 *
	 * @param iter      出力するイテラブルオブジェクト
	 * @param function  変換する関数
	 * @param delimiter 区切り文字
	 * @param <T>       変換前の型
	 * @param <U>       変換後の型
	 */
	public <T, U> ContestPrinter print(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
		final Iterator<T> it = iter.iterator();
		if (it.hasNext()) print(function.apply(it.next()));
		while (it.hasNext()) {
			ensureBufferSpace(1);
			buffer[pos++] = (byte) delimiter;
			print(function.apply(it.next()));
		}
		return this;
	}
}