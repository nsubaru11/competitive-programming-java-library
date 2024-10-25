import java.util.Locale;

/**
 * 標準出力を管理するクラスです。 複数回の出力を高速に行うことに適しており、全ての出力をStringBuilderで結合して最後に出力します。
 * 一度flushしたあと再度出力を行う必要がある場合、clearメソッドを呼び出しStringBuilderを一度初期化する必要があります。
 * 逐次flushを行う必要がある場合や出力が一度のみでよい場合などは推奨されません。
 * 内部でStringBuilderを用いるためスレッドセーフではありません。
 */
final class FastPrinter {
	private StringBuilder sb;

	public FastPrinter() {
		sb = new StringBuilder();
	}

	/**
	 * 改行を一つ出力します。
	 */
	public FastPrinter println() {
		print("\n");
		return this;
	}

	/**
	 * Objectを出力し、行末は改行します。
	 *
	 * @param o Object
	 */
	public FastPrinter println(Object o) {
		print(o).println();
		return this;
	}

	/**
	 * Stringを出力し、行末は改行します。
	 *
	 * @param s String
	 */
	public FastPrinter println(String s) {
		print(s).println();
		return this;
	}

	/**
	 * booleanを出力し、行末は改行します。
	 *
	 * @param f boolean
	 */
	public FastPrinter println(boolean f) {
		print(f).println();
		return this;
	}

	/**
	 * charを出力し、行末は改行します。
	 *
	 * @param c char
	 */
	public FastPrinter println(char c) {
		print(c).println();
		return this;
	}

	/**
	 * intを出力し、行末は改行します。
	 *
	 * @param a int
	 */
	public FastPrinter println(int a) {
		print(a).println();
		return this;
	}

	/**
	 * longを出力し、行末は改行します。
	 *
	 * @param a long
	 */
	public FastPrinter println(long a) {
		print(a).println();
		return this;
	}

	/**
	 * doubleを出力し、行末は改行します。
	 *
	 * @param a double
	 */
	public FastPrinter println(double a) {
		print(a).println();
		return this;
	}

	/**
	 * doubleを桁数を指定して出力し、行末を改行します。
	 *
	 * @param a double
	 * @param n int
	 */
	public FastPrinter println(double a, int n) {
		print(a, n).println();
		return this;
	}

	/**
	 * 2つの整数を改行区切りで出力し、行末を改行します。
	 *
	 * @param a int
	 * @param b int
	 */
	public FastPrinter println(int a, int b) {
		println(a).println(b);
		return this;
	}

	/**
	 * 2つの整数を改行区切りで出力し、行末を改行します。
	 *
	 * @param a int
	 * @param b long
	 */
	public FastPrinter println(int a, long b) {
		println(a).println(b);
		return this;
	}

	/**
	 * 2つの整数を改行区切りで出力し、行末を改行します。
	 *
	 * @param a long
	 * @param b int
	 */
	public FastPrinter println(long a, int b) {
		println(a).println(b);
		return this;
	}

	/**
	 * 2つの整数を改行区切りで出力し、行末を改行します。
	 *
	 * @param a long
	 * @param b long
	 */
	public FastPrinter println(long a, long b) {
		println(a).println(b);
		return this;
	}

	/**
	 * Object配列を改行区切りで出力し、行末も改行します。
	 *
	 * @param o Object...
	 */
	public FastPrinter println(Object... o) {
		for (Object x : o)
			println(x);
		return this;
	}

	/**
	 * String配列を改行区切りで出力し、行末も改行します。
	 *
	 * @param s String[]
	 */
	public FastPrinter println(String[] s) {
		for (String x : s)
			println(x);
		return this;
	}

	/**
	 * char配列を改行区切りで出力し、行末も改行します。
	 *
	 * @param c char[]
	 */
	public FastPrinter println(char[] c) {
		for (char x : c)
			println(x);
		return this;
	}

	/**
	 * int配列を改行区切りで出力し、行末も改行します。
	 *
	 * @param a int[]
	 */
	public FastPrinter println(int[] a) {
		for (int x : a)
			println(x);
		return this;
	}

	/**
	 * long配列を改行区切りで出力し、行末も改行します。
	 *
	 * @param a long[]
	 */
	public FastPrinter println(long[] a) {
		for (long x : a)
			println(x);
		return this;
	}

	/**
	 * Objectの二次元配列を出力し、行末は改行します。 行ごとに改行し、区切り文字は半角スペース。
	 *
	 * @param o Object[][]
	 */
	public FastPrinter println(Object[][] o) {
		for (Object[] x : o)
			print(x).println();
		return this;
	}

	/**
	 * charの二次元配列を出力し、行末は改行します。 行ごとに改行し、区切り文字は半角スペース。
	 *
	 * @param c char[][]
	 */
	public FastPrinter println(char[][] c) {
		for (char[] x : c)
			print(x).println();
		return this;
	}

	/**
	 * intの二次元配列を出力し、行末は改行します。 行ごとに改行し、区切り文字は半角スペース。
	 *
	 * @param a int[][]
	 */
	public FastPrinter println(int[][] a) {
		for (int[] x : a)
			print(x).println();
		return this;
	}

	/**
	 * longの二次元配列を出力し、行末は改行します。 行ごとに改行し、区切り文字は半角スペース。
	 *
	 * @param a long[][]
	 */
	public FastPrinter println(long[][] a) {
		for (long[] x : a)
			print(x).println();
		return this;
	}

	/**
	 * charの二次元配列を区切り文字なしで出力します。行末は改行します。
	 *
	 * @param c char[][]
	 */
	public FastPrinter printChars(char[][] c) {
		for (char[] x : c)
			printChars(x).println();
		return this;
	}

	/**
	 * Objectを出力し、行末は改行しません。
	 *
	 * @param o Object
	 */
	public FastPrinter print(Object o) {
		sb.append(o);
		return this;
	}

	/**
	 * Stringを出力し、行末は改行しません。
	 *
	 * @param s String
	 */
	public FastPrinter print(String s) {
		sb.append(s);
		return this;
	}

	/**
	 * booleanを出力し、行末は改行しません。
	 *
	 * @param f boolean
	 */
	public FastPrinter print(boolean f) {
		sb.append(f);
		return this;
	}

	/**
	 * charを出力し、行末は改行しません。
	 *
	 * @param c char
	 */
	public FastPrinter print(char c) {
		sb.append(c);
		return this;
	}

	/**
	 * intを出力し、行末は改行しません。
	 *
	 * @param a int
	 */
	public FastPrinter print(int a) {
		sb.append(a);
		return this;
	}

	/**
	 * longを出力し、行末は改行しません。
	 *
	 * @param a long
	 */
	public FastPrinter print(long a) {
		sb.append(a);
		return this;
	}

	/**
	 * doubleを出力し、行末は改行しません。
	 *
	 * @param a double
	 */
	public FastPrinter print(double a) {
		sb.append(a);
		return this;
	}

	/**
	 * doubleを桁数を指定して出力し、行末は改行しません。
	 *
	 * @param a double
	 * @param n int
	 */
	public FastPrinter print(double a, int n) {
		if (n == 0) {
			print(Math.round(a));
			return this;
		}
		if (a < 0) {
			print("-");
			a = -a;
		}
		a += Math.pow(10, -n) / 2;
		print((long) a).print(".");
		a -= (long) a;
		while (n-- > 0) {
			a *= 10;
			print((int) a);
			a -= (int) a;
		}
		return this;
	}

	/**
	 * 2つの整数を半角スペース区切りで出力し、行末は改行しません。
	 *
	 * @param a int
	 * @param b int
	 */
	public FastPrinter print(int a, int b) {
		print(a).print(" ").print(b);
		return this;
	}

	/**
	 * 2つの整数を半角スペース区切りで出力し、行末は改行しません。
	 *
	 * @param a int
	 * @param b long
	 */
	public FastPrinter print(int a, long b) {
		print(a).print(" ").print(b);
		return this;
	}

	/**
	 * 2つの整数を半角スペース区切りで出力し、行末は改行しません。
	 *
	 * @param a long
	 * @param b int
	 */
	public FastPrinter print(long a, int b) {
		print(a).print(" ").print(b);
		return this;
	}

	/**
	 * 2つの整数を半角スペース区切りで出力し、行末は改行しません。
	 *
	 * @param a long
	 * @param b long
	 */
	public FastPrinter print(long a, long b) {
		print(a).print(" ").print(b);
		return this;
	}

	/**
	 * Object配列を半角スペース区切りで出力し、行末は改行しません。
	 *
	 * @param o Object...
	 */
	public FastPrinter print(Object... o) {
		print(o[0]);
		for (int i = 1; i < o.length; i++) {
			print(" ").print(o[i]);
		}
		return this;
	}

	/**
	 * String配列を半角スペース区切りで出力し、行末は改行しません。
	 *
	 * @param s String[]
	 */
	public FastPrinter print(String[] s) {
		print(s[0]);
		for (int i = 1; i < s.length; i++) {
			print(" ").print(s[i]);
		}
		return this;
	}

	/**
	 * char配列を半角スペース区切りで出力し、行末は改行しません。
	 *
	 * @param c char[]
	 */
	public FastPrinter print(char[] c) {
		print(c[0]);
		for (int i = 1; i < c.length; i++) {
			print(" ").print(c[i]);
		}
		return this;
	}

	/**
	 * int配列を半角スペース区切りで出力し、行末は改行しません。
	 *
	 * @param a int[]
	 */
	public FastPrinter print(int[] a) {
		print(a[0]);
		for (int i = 1; i < a.length; i++) {
			print(" ").print(a[i]);
		}
		return this;
	}

	/**
	 * long配列を半角スペース区切りで出力し、行末は改行しません。
	 *
	 * @param a long[]
	 */
	public FastPrinter print(long[] a) {
		print(a[0]);
		for (int i = 1; i < a.length; i++) {
			print(" ").print(a[i]);
		}
		return this;
	}

	/**
	 * charの配列を区切り文字無しで出力します。
	 * 
	 * @param c char[]
	 */
	public FastPrinter printChars(char[] c) {
		sb.append(c);
		return this;
	}

	/**
	 * フォーマットを指定して出力します。
	 *
	 * @param format String 書式文字列
	 * @param args   Object... 書式に割り当てる値
	 */
	public FastPrinter printf(String format, Object... args) {
		print(String.format(format, args));
		return this;
	}

	/**
	 * フォーマットを指定して出力します。 このとき指定した言語環境での整形を行います。
	 *
	 * @param locale Locale 言語環境
	 * @param format String 書式文字列
	 * @param args   Object... 書式に割り当てる値
	 */
	public FastPrinter printf(Locale locale, String format, Object... args) {
		print(String.format(locale, format, args));
		return this;
	}

	/**
	 * StringBuilderにためた出力内容を逆順にします。
	 */
	public FastPrinter reverse() {
		sb.reverse();
		return this;
	}

	/**
	 * StringBuilderにためた出力内容を表示します。
	 */
	public FastPrinter flush() {
		System.out.print(sb);
		return this;
	}

	/**
	 * StringBuilderの初期化を行います。
	 */
	public FastPrinter clear() {
		sb.setLength(0);
		return this;
	}
}
