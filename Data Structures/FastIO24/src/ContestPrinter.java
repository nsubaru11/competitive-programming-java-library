import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.DoubleFunction;
import java.util.function.IntUnaryOperator;


import static java.lang.Math.round;

/**
 * {@code ContestPrinter} は、競技プログラミングにおける多様な出力要求に特化した高速出力ユーティリティです。<br>
 * {@link FastPrinter} の基本機能に加え、配列やコレクションの柔軟な出力、繰り返し出力、逆順出力など、
 * 競技プログラミングで頻出するパターンを効率的に扱うためのメソッドを提供します。<br>
 * <b>注意:</b> パフォーマンスを優先するため、ほとんどのメソッドで引数のnullチェックを行っていません。
 * nullが渡された場合、{@code NullPointerException} が発生する可能性があります。
 */
@SuppressWarnings("unused")
public final class ContestPrinter extends FastPrinter {


	/**
	 * 10のべき乗の配列です。POW10[i] は 10^i を表します。
	 */
	private static final long[] POW10 = {
			1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000,
			1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L,
			10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L,
			10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L
	};

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

	/* ------------------------ オーバーライド: Object出力 ------------------------ */

	/**
	 * Object を文字列化して出力します。（改行付き）<br>
	 * {@code null} の場合は改行のみ出力します。<br>
	 * 効率化のため、内部で {@code switch} による型判定を行い、
	 * 基本的な型（String, Integer, Long など）やその配列は、それぞれに最適化されたメソッドで処理します。
	 *
	 * @param o 出力するオブジェクト
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	@Override
	public ContestPrinter println(final Object o) {
		if (o == null) {
			println();
			return this;
		}
		switch (o) {
			case Boolean b -> println(b.booleanValue());
			case Byte b -> println(b.byteValue());
			case Character c -> println(c.charValue());
			case Integer i -> println(i.intValue());
			case Long l -> println(l.longValue());
			case Double d -> println(d.toString());
			case BigInteger bi -> println(bi.toString());
			case BigDecimal bd -> println(bd.toString());
			case String s -> println(s);
			case boolean[] arr -> println(arr);
			case char[] arr -> println(arr);
			case int[] arr -> println(arr);
			case long[] arr -> println(arr);
			case double[] arr -> println(arr);
			case String[] arr -> println(arr);
			case Object[] arr -> println(arr);
			default -> println(o.toString());
		}
		return this;
	}

	/**
	 * Object を文字列化して出力します。（改行無し）<br>
	 * {@code null} の場合は何も出力しません。<br>
	 * 効率化のため、内部で {@code switch} による型判定を行い、
	 * 基本的な型（String, Integer, Long など）やその配列は、それぞれに最適化されたメソッドで処理します。
	 *
	 * @param o 出力するオブジェクト
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	@Override
	public ContestPrinter print(final Object o) {
		if (o == null) return this;
		switch (o) {
			case Boolean b -> print(b.booleanValue());
			case Byte b -> print(b.byteValue());
			case Character c -> print(c.charValue());
			case Integer i -> print(i.intValue());
			case Long l -> print(l.longValue());
			case Double d -> print(d.toString());
			case BigInteger bi -> print(bi.toString());
			case BigDecimal bd -> print(bd.toString());
			case String s -> print(s);
			case boolean[] arr -> print(arr);
			case char[] arr -> print(arr);
			case int[] arr -> print(arr);
			case long[] arr -> print(arr);
			case double[] arr -> print(arr);
			case String[] arr -> print(arr);
			case Object[] arr -> print(arr);
			default -> print(o.toString());
		}
		return this;
	}

	/* ------------------------ オーバーライド: println系メソッド ------------------------ */

	public ContestPrinter println() {
		super.println();
		return this;
	}

	/* ------------------------ ペア出力メソッド(改行付き) ------------------------ */

	/**
	 * 2 つの整数値(int, int)を改行区切りで出力します。(改行付き)
	 *
	 * @param a 出力する int 値
	 * @param b 出力する int 値
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final int a, final int b) {
		return println(a, b, '\n');
	}

	/**
	 * 2 つの整数値(int, long)を改行区切りで出力します。(改行付き)
	 *
	 * @param a 出力する int 値
	 * @param b 出力する long 値
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final int a, final long b) {
		return println(a, b, '\n');
	}

	/**
	 * 2 つの整数値(long, int)を改行区切りで出力します。(改行付き)
	 *
	 * @param a 出力する long 値
	 * @param b 出力する int 値
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final long a, final int b) {
		return println(a, b, '\n');
	}

	/**
	 * 2 つの整数値(int または long)を改行区切りで出力します。(改行付き)
	 *
	 * @param a 出力する long 値
	 * @param b 出力する long 値
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final long a, final long b) {
		return println(a, b, '\n');
	}

	/**
	 * 2 つの整数値(int または long)を指定した区切り文字で出力します。(改行付き)
	 *
	 * @param a         出力する整数値(int または long)
	 * @param b         出力する整数値(int または long)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final long a, final long b, final char delimiter) {
		ensureCapacity((MAX_LONG_DIGITS << 1) + 2);
		write(a);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
		write(b);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, LINE);
		if (autoFlush) flush();
		return this;
	}

	/* ------------------------ ペア出力メソッド(改行無し) ------------------------ */

	/**
	 * 2 つの整数値(int, int)を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param a 出力する int 値
	 * @param b 出力する int 値
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final int a, final int b) {
		return print(a, b, ' ');
	}

	/**
	 * 2 つの整数値(int, long)を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param a 出力する int 値
	 * @param b 出力する long 値
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final int a, final long b) {
		return print(a, b, ' ');
	}

	/**
	 * 2 つの整数値(long, int)を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param a 出力する long 値
	 * @param b 出力する int 値
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final long a, final int b) {
		return print(a, b, ' ');
	}

	/**
	 * 2 つの整数値(long, long)を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param a 出力する long 値
	 * @param b 出力する long 値
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final long a, final long b) {
		return print(a, b, ' ');
	}

	/**
	 * 2 つの整数値(int または long)を指定した区切り文字で出力します。(改行無し)
	 *
	 * @param a         出力する整数値(int または long)
	 * @param b         出力する整数値(int または long)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final long a, final long b, final char delimiter) {
		ensureCapacity((MAX_LONG_DIGITS << 1) + 1);
		write(a);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
		write(b);
		if (autoFlush) flush();
		return this;
	}

	/* ------------------------ 小数系メソッド ------------------------ */

	/**
	 * double 値を指定された小数点以下桁数で出力します(四捨五入)。(改行付き)
	 *
	 * @param d 出力する double 値
	 * @param n 小数点以下の桁数
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final double d, final int n) {
		print(d, n).println();
		return this;
	}

	/**
	 * double 値を指定された小数点以下桁数で出力します(四捨五入)。(改行無し)
	 *
	 * @param d 出力する double 値
	 * @param n 小数点以下の桁数
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(double d, int n) {
		if (n <= 0) {
			print(round(d));
			return this;
		}
		if (d < 0) {
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, HYPHEN);
			d = -d;
		}
		if (n > 18) n = 18;
		final long intPart = (long) d;
		final long fracPart = (long) ((d - intPart) * POW10[n]);
		print(intPart);
		int leadingZeros = n - countDigits(-fracPart);
		ensureCapacity(leadingZeros + 1);
		BYTE_ARRAY_HANDLE.set(buffer, pos++, PERIOD);
		while (leadingZeros-- > 0) BYTE_ARRAY_HANDLE.set(buffer, pos++, ZERO);
		print(fracPart);
		return this;
	}

	/* ------------------------ 1次元配列系メソッド(改行付き) ------------------------ */

	/**
	 * boolean 配列の各要素を改行区切りで出力します。
	 *
	 * @param arr 出力する boolean 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final boolean[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	/**
	 * char 配列の各要素を改行区切りで出力します。
	 *
	 * @param arr 出力する char 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final char[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	/**
	 * int 配列の各要素を改行区切りで出力します。
	 *
	 * @param arr 出力する int 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final int[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	/**
	 * long 配列の各要素を改行区切りで出力します。
	 *
	 * @param arr 出力する long 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final long[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	/**
	 * double 配列の各要素を改行区切りで出力します。
	 *
	 * @param arr 出力する double 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final double[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	/**
	 * String 配列の各要素を改行区切りで出力します。
	 *
	 * @param arr 出力する String 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final String[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	/**
	 * 可変長の Object 配列の各要素を改行区切りで出力します。
	 *
	 * @param arr 出力する Object 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final Object... arr) {
		for (final Object o : arr) println(o);
		return this;
	}

	/**
	 * boolean 配列の各要素を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する boolean 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final boolean[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	/**
	 * char 配列の各要素を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する char 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final char[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	/**
	 * int 配列の各要素を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する int 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final int[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	/**
	 * long 配列の各要素を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する long 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final long[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	/**
	 * double 配列の各要素を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する double 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final double[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	/**
	 * String 配列の各要素を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する String 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final String[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	/* ------------------------ 1次元配列系メソッド(範囲/区切り/改行付き) ------------------------ */

	/**
	 * boolean 配列の指定範囲 [from, to) の各要素を改行区切りで出力します。
	 *
	 * @param arr  出力する boolean 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final boolean[] arr, final int from, final int to) {
		for (int i = from; i < to; i++) println(arr[i]);
		return this;
	}

	/**
	 * char 配列の指定範囲 [from, to) の各要素を改行区切りで出力します。
	 *
	 * @param arr  出力する char 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final char[] arr, final int from, final int to) {
		for (int i = from; i < to; i++) println(arr[i]);
		return this;
	}

	/**
	 * int 配列の指定範囲 [from, to) の各要素を改行区切りで出力します。
	 *
	 * @param arr  出力する int 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final int[] arr, final int from, final int to) {
		for (int i = from; i < to; i++) println(arr[i]);
		return this;
	}

	/**
	 * long 配列の指定範囲 [from, to) の各要素を改行区切りで出力します。
	 *
	 * @param arr  出力する long 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final long[] arr, final int from, final int to) {
		for (int i = from; i < to; i++) println(arr[i]);
		return this;
	}

	/**
	 * double 配列の指定範囲 [from, to) の各要素を改行区切りで出力します。
	 *
	 * @param arr  出力する double 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final double[] arr, final int from, final int to) {
		for (int i = from; i < to; i++) println(arr[i]);
		return this;
	}

	/**
	 * String 配列の指定範囲 [from, to) の各要素を改行区切りで出力します。
	 *
	 * @param arr  出力する String 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final String[] arr, final int from, final int to) {
		for (int i = from; i < to; i++) println(arr[i]);
		return this;
	}

	/**
	 * boolean 配列の指定範囲 [from, to) を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する boolean 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final boolean[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	/**
	 * char 配列の指定範囲 [from, to) を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する char 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final char[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	/**
	 * int 配列の指定範囲 [from, to) を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する int 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final int[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	/**
	 * long 配列の指定範囲 [from, to) を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する long 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final long[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	/**
	 * double 配列の指定範囲 [from, to) を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する double 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final double[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	/**
	 * String 配列の指定範囲 [from, to) を指定の区切り文字で連結し、最後に改行を出力します。
	 *
	 * @param arr       出力する String 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter println(final String[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	/* ------------------------ 1次元配列系メソッド(改行無し) ------------------------ */

	/**
	 * boolean 配列の各要素を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr 出力する boolean 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final boolean[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	/**
	 * char 配列の各要素を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr 出力する char 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final char[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	/**
	 * int 配列の各要素を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr 出力する int 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final int[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	/**
	 * long 配列の各要素を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr 出力する long 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final long[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	/**
	 * double 配列の各要素を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr 出力する double 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final double[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	/**
	 * String 配列の各要素を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr 出力する String 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final String[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	/**
	 * 可変長の Object 配列の各要素を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr 出力する Object 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final Object... arr) {
		final int len = arr.length;
		if (len > 0) print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, SPACE);
			print(arr[i]);
		}
		return this;
	}

	/**
	 * boolean 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する boolean 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final boolean[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	/**
	 * char 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する char 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final char[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	/**
	 * int 配列の各要素を指定の区切り文字で出力します。(改行無し)
	 *
	 * @param arr       出力する int 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final int[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	/**
	 * long 配列の各要素を指定の区切り文字で出力します。(改行無し)
	 *
	 * @param arr       出力する long 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final long[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	/**
	 * double 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する double 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final double[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	/**
	 * String 配列の各要素を指定の区切り文字で出力します。（改行無し）
	 *
	 * @param arr       出力する String 配列
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final String[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	/**
	 * boolean 配列の指定範囲 [from, to) を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr  出力する boolean 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final boolean[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	/**
	 * char 配列の指定範囲 [from, to) を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr  出力する char 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final char[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	/**
	 * int 配列の指定範囲 [from, to) を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr  出力する int 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final int[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	/**
	 * long 配列の指定範囲 [from, to) を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr  出力する long 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final long[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	/**
	 * double 配列の指定範囲 [from, to) を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr  出力する double 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final double[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	/**
	 * String 配列の指定範囲 [from, to) を半角スペース区切りで出力します。(改行無し)
	 *
	 * @param arr  出力する String 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final String[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	/**
	 * boolean 配列の指定範囲 [from, to) を指定の区切り文字で出力します。(改行無し)
	 *
	 * @param arr       出力する boolean 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final boolean[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		print(arr[from]);
		for (int i = from + 1; i < to; i++) {
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
			write(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * char 配列の指定範囲 [from, to) を指定の区切り文字で出力します。(改行無し)
	 *
	 * @param arr       出力する char 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final char[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		final int n = to - from;
		ensureCapacity(n * 2 - 1);
		byte[] buf = buffer;
		int p = pos;
		BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[from]);
		for (int i = from + 1; i < to; i++) {
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) delimiter);
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i]);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * int 配列の指定範囲 [from, to) を指定の区切り文字で出力します。(改行無し)
	 *
	 * @param arr       出力する int 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final int[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		final int len = to - from;
		ensureCapacity(len * (MAX_INT_DIGITS + 1));
		write(arr[from]);
		for (int i = from + 1; i < to; i++) {
			BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
			write(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * long 配列の指定範囲 [from, to) を指定の区切り文字で出力します。(改行無し)
	 *
	 * @param arr       出力する long 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final long[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		final int len = to - from;
		ensureCapacity(len * (MAX_LONG_DIGITS + 1));
		write(arr[from]);
		for (int i = from + 1; i < to; i++) {
			BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
			write(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}


	/**
	 * double 配列の指定範囲 [from, to) を指定の区切り文字で出力します。(改行無し)
	 *
	 * @param arr       出力する double 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final double[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		print(arr[from]);
		for (int i = from + 1; i < to; i++) {
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
			print(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * String 配列の指定範囲 [from, to) を指定の区切り文字で出力します。(改行無し)
	 *
	 * @param arr       出力する String 配列
	 * @param from      開始インデックス(含む)
	 * @param to        終了インデックス(除く)
	 * @param delimiter 区切り文字
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter print(final String[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		print(arr[from]);
		for (int i = from + 1; i < to; i++) {
			ensureCapacity(arr[i].length() + 1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
			write(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/* ------------------------ 1次元配列の関数変換系メソッド（改行付き） ------------------------ */

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
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, SPACE);
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
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, SPACE);
			print(function.apply(arr[i]));
		}
		return this;
	}

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
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, SPACE);
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
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, SPACE);
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
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, SPACE);
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
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, SPACE);
			print(function.apply(arr[i]));
		}
		return this;
	}

	/* ------------------------ 2次元配列系メソッド ------------------------ */

	/**
	 * 二次元の boolean 配列を、各行を半角スペース区切りで出力します。（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の boolean 配列
	 */
	public ContestPrinter println(final boolean[][] arr2d) {
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
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
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
		return printChars(arr, 0, arr.length);
	}

	/**
	 * char 配列の指定範囲 [from, to) を区切り文字無しで出力します。（改行無し）
	 *
	 * @param arr  出力する char 配列
	 * @param from 開始インデックス(含む)
	 * @param to   終了インデックス(除く)
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printChars(final char[] arr, final int from, final int to) {
		final int len = to - from;
		ensureCapacity(len);
		final byte[] buf = buffer;
		int p = pos, i = from;
		final int limit8 = from + (len & ~7);
		while (i < limit8) {
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i++]);
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i++]);
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i++]);
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i++]);
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i++]);
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i++]);
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i++]);
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i++]);
		}
		while (i < to) BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i++]);
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * char 配列の各要素を指定された関数で変換し、区切り文字無しで出力します。
	 *
	 * @param arr      出力する char 配列
	 * @param function char を変換する関数
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printChars(final char[] arr, final IntUnaryOperator function) {
		final int len = arr.length;
		ensureCapacity(len);
		final byte[] buf = buffer;
		int p = pos, i = 0;
		final int limit = len & ~7;
		while (i < limit) {
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) function.applyAsInt(arr[i++]));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) function.applyAsInt(arr[i++]));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) function.applyAsInt(arr[i++]));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) function.applyAsInt(arr[i++]));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) function.applyAsInt(arr[i++]));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) function.applyAsInt(arr[i++]));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) function.applyAsInt(arr[i++]));
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) function.applyAsInt(arr[i++]));
		}
		while (i < len) BYTE_ARRAY_HANDLE.set(buf, p++, (byte) function.applyAsInt(arr[i++]));
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * 二次元の char 配列を、各行を区切り文字無しで出力（各行末に改行）
	 *
	 * @param arr2d 出力する二次元の char 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
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
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printChars(final char[][] arr2d, final IntUnaryOperator function) {
		for (final char[] arr : arr2d) printChars(arr, function).println();
		return this;
	}

	/* ------------------------ Iterableオブジェクト用メソッド（改行付き） ------------------------ */

	/**
	 * イテラブルオブジェクトの各要素を改行区切りで出力します。
	 *
	 * @param iter 出力するイテラブルオブジェクト
	 * @param <T>  各要素の型
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public <T> ContestPrinter println(final Iterable<T> iter) {
		for (final T e : iter) println(e);
		return this;
	}

	/**
	 * イテラブルオブジェクトの各要素を区切り文字を指定して連結し、最後に改行を出力します。
	 *
	 * @param iter      出力するイテラブルオブジェクト
	 * @param delimiter 区切り文字
	 * @param <T>       各要素の型
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public <T> ContestPrinter println(final Iterable<T> iter, final char delimiter) {
		return print(iter, delimiter).println();
	}

	/**
	 * イテラブルオブジェクトの各要素を半角スペース区切りで出力（改行無し）
	 *
	 * @param iter 出力するイテラブルオブジェクト
	 * @param <T>  各要素の型
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
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
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public <T> ContestPrinter print(final Iterable<T> iter, final char delimiter) {
		final Iterator<T> it = iter.iterator();
		if (it.hasNext()) print(it.next());
		while (it.hasNext()) {
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
			print(it.next());
		}
		return this;
	}

	/* ------------------------ Iterableオブジェクト用メソッド（改行無し） ------------------------ */

	/**
	 * イテラブルオブジェクトの各要素を指定された関数で変換し、改行区切りで出力します。
	 *
	 * @param iter     出力するイテラブルオブジェクト
	 * @param function 変換する関数
	 * @param <T>      変換前の型
	 * @param <U>      変換後の型
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public <T, U> ContestPrinter println(final Iterable<T> iter, final Function<T, U> function) {
		for (final T e : iter) println(function.apply(e));
		return this;
	}

	/**
	 * イテラブルオブジェクトの各要素を指定された関数で変換し、各要素を区切り文字を指定して連結し、最後に改行を出力します。
	 *
	 * @param iter      出力するイテラブルオブジェクト
	 * @param function  変換する関数
	 * @param delimiter 区切り文字
	 * @param <T>       変換前の型
	 * @param <U>       変換後の型
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public <T, U> ContestPrinter println(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
		return print(iter, function, delimiter).println();
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
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buffer, pos++, (byte) delimiter);
			print(function.apply(it.next()));
		}
		return this;
	}

	/**
	 * 指定された文字を指定された回数繰り返し出力します。（改行無し）
	 *
	 * @param c     繰り返す文字
	 * @param times 繰り返す回数
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printRepeat(final char c, final int times) {
		if (times <= 0) return this;
		ensureCapacity(times);
		final byte[] buf = buffer;
		final byte b = (byte) c;
		int p = pos;
		BYTE_ARRAY_HANDLE.set(buf, p++, b);
		int cnt = 1;
		while ((cnt << 1) <= times) {
			System.arraycopy(buf, pos, buf, p, cnt);
			p += cnt;
			cnt <<= 1;
		}
		final int remaining = times - cnt;
		if (remaining > 0) {
			System.arraycopy(buf, pos, buf, p, remaining);
			p += remaining;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * 指定された文字列を指定された回数繰り返し出力します。（改行無し）
	 *
	 * @param s     繰り返す文字列
	 * @param times 繰り返す回数
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printRepeat(final String s, final int times) {
		if (times <= 0) return this;
		final int len = s.length();
		if (len == 0) return this;
		final int total = len * times;
		ensureCapacity(total);
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
		int cnt = 1;
		while ((cnt << 1) <= times) {
			System.arraycopy(buf, pos, buf, p, cnt * len);
			p += cnt * len;
			cnt <<= 1;
		}
		final int remaining = times - cnt;
		if (remaining > 0) {
			System.arraycopy(buf, pos, buf, p, remaining * len);
			p += remaining * len;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * 指定された文字と改行のペアを指定された回数繰り返し出力します。
	 *
	 * @param c     繰り返す文字
	 * @param times 繰り返す回数
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printlnRepeat(final char c, final int times) {
		if (times <= 0) return this;
		ensureCapacity(times << 1);
		final byte[] buf = buffer;
		final byte b = (byte) c;
		int p = pos;
		BYTE_ARRAY_HANDLE.set(buf, p++, b);
		BYTE_ARRAY_HANDLE.set(buf, p++, LINE);
		int cnt = 1;
		while ((cnt << 1) <= times) {
			System.arraycopy(buf, pos, buf, p, cnt << 1);
			p += cnt << 1;
			cnt <<= 1;
		}
		final int remaining = times - cnt;
		if (remaining > 0) {
			System.arraycopy(buf, pos, buf, p, remaining << 1);
			p += remaining << 1;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * 指定された文字列と改行のペアを指定された回数繰り返し出力します。
	 *
	 * @param s     繰り返す文字列
	 * @param times 繰り返す回数
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printlnRepeat(final String s, final int times) {
		if (times <= 0) return this;
		final int sLen = s.length();
		final int len = sLen + 1;
		final int total = len * times;
		ensureCapacity(total);
		final byte[] buf = buffer;
		int p = pos, i = 0;
		final int limit = sLen & ~7;
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
		while (i < sLen) BYTE_ARRAY_HANDLE.set(buf, p++, (byte) s.charAt(i++));
		BYTE_ARRAY_HANDLE.set(buf, p++, LINE);
		int cnt = 1;
		while ((cnt << 1) <= times) {
			System.arraycopy(buf, pos, buf, p, cnt * len);
			p += cnt * len;
			cnt <<= 1;
		}
		final int remaining = times - cnt;
		if (remaining > 0) {
			System.arraycopy(buf, pos, buf, p, remaining * len);
			p += remaining * len;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * boolean 配列の要素を逆順で、改行区切りで出力します。
	 *
	 * @param arr 出力する boolean 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printlnReverse(final boolean[] arr) {
		final int len = arr.length;
		final byte[] buf = buffer;
		for (int i = len - 1; i >= 0; i--) {
			write(arr[i]);
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buf, pos++, LINE);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * char 配列の要素を逆順で、改行区切りで出力します。
	 *
	 * @param arr 出力する char 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printlnReverse(final char[] arr) {
		final int len = arr.length;
		ensureCapacity(len << 1);
		final byte[] buf = buffer;
		int p = pos;
		for (int i = len - 1; i >= 0; i--) {
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i]);
			BYTE_ARRAY_HANDLE.set(buf, p++, LINE);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * int 配列の要素を逆順で、改行区切りで出力します。
	 *
	 * @param arr 出力する int 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printlnReverse(final int[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_INT_DIGITS + 1));
		final byte[] buf = buffer;
		for (int i = len - 1; i >= 0; i--) {
			write(arr[i]);
			BYTE_ARRAY_HANDLE.set(buf, pos++, LINE);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * long 配列の要素を逆順で、改行区切りで出力します。
	 *
	 * @param arr 出力する long 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printlnReverse(final long[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_LONG_DIGITS + 1));
		final byte[] buf = buffer;
		for (int i = len - 1; i >= 0; i--) {
			write(arr[i]);
			BYTE_ARRAY_HANDLE.set(buf, pos++, LINE);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * double 配列の要素を逆順で、改行区切りで出力します。
	 *
	 * @param arr 出力する double 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printlnReverse(final double[] arr) {
		final int len = arr.length;
		final byte[] buf = buffer;
		for (int i = len - 1; i >= 0; i--) {
			String s = Double.toString(arr[i]);
			ensureCapacity(s.length() + 1);
			write(s);
			BYTE_ARRAY_HANDLE.set(buf, pos++, LINE);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * String 配列の要素を逆順で、改行区切りで出力します。
	 *
	 * @param arr 出力する String 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printlnReverse(final String[] arr) {
		final int len = arr.length;
		final byte[] buf = buffer;
		for (int i = len - 1; i >= 0; i--) {
			ensureCapacity(arr[i].length() + 1);
			write(arr[i]);
			BYTE_ARRAY_HANDLE.set(buf, pos++, LINE);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * Object 配列の要素を逆順で、改行区切りで出力します。
	 *
	 * @param arr 出力する Object 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printlnReverse(final Object[] arr) {
		final int len = arr.length;
		for (int i = len - 1; i >= 0; i--) println(arr[i]);
		if (autoFlush) flush();
		return this;
	}

	/**
	 * boolean 配列の要素を逆順で、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する boolean 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printReverse(final boolean[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		final byte[] buf = buffer;
		write(arr[len - 1]);
		for (int i = len - 2; i >= 0; i--) {
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buf, pos++, SPACE);
			write(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * char 配列の要素を逆順で、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する char 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printReverse(final char[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity((len << 1) - 1);
		final byte[] buf = buffer;
		int p = pos;
		BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[len - 1]);
		for (int i = len - 2; i >= 0; i--) {
			BYTE_ARRAY_HANDLE.set(buf, p++, SPACE);
			BYTE_ARRAY_HANDLE.set(buf, p++, (byte) arr[i]);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	/**
	 * int 配列の要素を逆順で、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する int 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printReverse(final int[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_INT_DIGITS + 1) - 1);
		final byte[] buf = buffer;
		write(arr[len - 1]);
		for (int i = len - 2; i >= 0; i--) {
			BYTE_ARRAY_HANDLE.set(buf, pos++, SPACE);
			write(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * long 配列の要素を逆順で、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する long 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printReverse(final long[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_LONG_DIGITS + 1) - 1);
		final byte[] buf = buffer;
		write(arr[len - 1]);
		for (int i = len - 2; i >= 0; i--) {
			BYTE_ARRAY_HANDLE.set(buf, pos++, SPACE);
			write(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * double 配列の要素を逆順で、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する double 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printReverse(final double[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		final byte[] buf = buffer;
		print(arr[len - 1]);
		for (int i = len - 2; i >= 0; i--) {
			String s = Double.toString(arr[i]);
			ensureCapacity(s.length() + 1);
			BYTE_ARRAY_HANDLE.set(buf, pos++, SPACE);
			write(s);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * String 配列の要素を逆順で、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する String 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printReverse(final String[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		final byte[] buf = buffer;
		ensureCapacity(arr[len - 1].length());
		write(arr[len - 1]);
		for (int i = len - 2; i >= 0; i--) {
			ensureCapacity(arr[i].length() + 1);
			BYTE_ARRAY_HANDLE.set(buf, pos++, SPACE);
			write(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	/**
	 * Object 配列の要素を逆順で、半角スペース区切りで出力します。（改行無し）
	 *
	 * @param arr 出力する Object 配列
	 * @return このContestPrinterインスタンス(メソッドチェーン用)
	 */
	public ContestPrinter printReverse(final Object[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		final byte[] buf = buffer;
		print(arr[len - 1]);
		for (int i = len - 2; i >= 0; i--) {
			ensureCapacity(1);
			BYTE_ARRAY_HANDLE.set(buf, pos++, SPACE);
			print(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}
}
