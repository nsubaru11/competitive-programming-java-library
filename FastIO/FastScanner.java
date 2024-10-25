import java.util.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.function.Supplier;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.setAll;
import static java.util.Arrays.sort;

/**
 * 標準入力を高速に処理するためのクラスです。配列の入力時に総和、最大値、最小値を取得できます。
 */
final class FastScanner {
	private static final BufferedInputStream reader = new BufferedInputStream(System.in);
	private static final byte[] buf = new byte[1 << 17];
	private static int pos = 0, cnt = 0;
	private long sum, low, high;

	/**
	 * バッファから1バイトを読み込みます。
	 *
	 * @return 読み込んだバイト（byte）
	 */
	private byte read() {
		if (pos == cnt) {
			try {
				cnt = reader.read(buf, pos = 0, 1 << 17);
			} catch (IOException ignored) {
			}
		}
		if (cnt < 0)
			return 0;
		return buf[pos++];
	}

	/**
	 * 次の一文字を読み込みます。
	 * 
	 * @return 読み込んだ文字(char)
	 */
	public char nextChar() {
		byte b = read();
		while (b < '!' || '~' < b)
			b = read();
		return (char) b;
	}

	/**
	 * 次のトークンを文字列(String)として読み込みます。
	 *
	 * @return 読み込んだ文字列(String)
	 */
	public String next() {
		return nextSb().toString();
	}

	/**
	 * 次のトークンを文字列(StringBuilder)として読み込みます。
	 *
	 * @return 読み込んだ文字列(STringBuilder)
	 */
	public StringBuilder nextSb() {
		StringBuilder sb = new StringBuilder();
		int b = read();
		while (b < '!' || '~' < b)
			b = read();
		while ('!' <= b && b <= '~') {
			sb.appendCodePoint(b);
			b = read();
		}
		return sb;
	}

	/**
	 * 改行までの一行を読み込みます。
	 *
	 * @return 読み込んだ行(String)
	 */
	public String nextLine() {
		StringBuilder sb = new StringBuilder();
		int b = read();
		while (b != 0 && b != '\r' && b != '\n') {
			sb.appendCodePoint(b);
			b = read();
		}
		if (b == '\r')
			read();
		return sb.toString();
	}

	/**
	 * 次の整数を読み込みます。
	 *
	 * @return 読み込んだ整数(int)
	 */
	public int nextInt() {
		int b = nextChar();
		boolean neg = b == '-';
		if (neg)
			b = read();
		int n = 0;
		while ('0' <= b && b <= '9') {
			n = n * 10 + b - '0';
			b = read();
		}
		return neg ? -n : n;
	}

	/**
	 * 次の長整数を読み込みます。
	 *
	 * @return 読み込んだ長整数(long)
	 */
	public long nextLong() {
		int b = nextChar();
		boolean neg = b == '-';
		if (neg)
			b = read();
		long n = 0;
		while ('0' <= b && b <= '9') {
			n = n * 10 + b - '0';
			b = read();
		}
		return neg ? -n : n;
	}

	/**
	 * 基数を指定して整数を読み込みます。
	 * 
	 * @param radix 基数
	 * @return 読み込んだ整数(int)
	 */
	public int nextIntRadix(int radix) {
		return Integer.parseInt(next(), radix);
	}

	/**
	 * 基数を指定して長整数を読み込みます。
	 * 
	 * @param radix 基数
	 * @return 読み込んだ長整数(long)
	 */
	public long nextLongRadix(int radix) {
		return Long.parseLong(next(), radix);
	}

	/**
	 * 次のトークンをBigIntegerとして読み込みます。
	 * 
	 * @return 読み込んだBigInteger
	 */
	public BigInteger nextBigInteger() {
		return new BigInteger(next());
	}

	/**
	 * 基数を指定してBigIntegerを読み込みます。
	 * 
	 * @param radix 基数
	 * @return 読み込んだBigInteger
	 */
	public BigInteger nextBigInteger(int radix) {
		return new BigInteger(next(), radix);
	}

	/**
	 * 次のトークンを浮動小数点数として読み込みます。
	 *
	 * @return 読み込んだ浮動小数点数(double)
	 */
	public double nextDouble() {
		return Double.parseDouble(next());
	}

	/**
	 * 指定された長さの文字列配列を読み込みます。
	 *
	 * @param n 配列の長さ
	 * @return 読み込んだ文字列配列(String[])
	 */
	public String[] nextStrings(int n) {
		String[] s = new String[n];
		setAll(s, x -> next());
		return s;
	}

	/**
	 * 指定された長さの文字列配列を読み込み、ソートされた文字列配列として返します。
	 *
	 * @param n 配列の長さ
	 * @return ソートされた文字列配列(String[])
	 */
	public String[] nextSortedStrings(int n) {
		String[] s = nextStrings(n);
		sort(s);
		return s;
	}

	/**
	 * 指定された行数・列数の二次元文字列配列を読み込みます。
	 *
	 * @param n 行数
	 * @param m 列数
	 * @return 読み込んだ二次元文字列配列(String[][])
	 */
	public String[][] nextStringMat(int n, int m) {
		String[][] s = new String[n][m];
		setAll(s, x -> nextStrings(m));
		return s;
	}

	/**
	 * 次の文字列のi番目の文字を読み込みます。
	 * 
	 * @param i 読み込む文字のindex
	 * @return 読み込んだ文字(char)
	 */
	public char nextCharAt(int i) {
		return next().charAt(i);
	}

	/**
	 * 文字列を文字配列として読み込みます。
	 *
	 * @return 読み込んだ文字配列(char[])
	 */
	public char[] nextChars() {
		return next().toCharArray();
	}

	/**
	 * 指定された長さの文字配列を読み込みます。
	 *
	 * @param n 配列の長さ
	 * @return 読み込んだ文字配列(char[])
	 */
	public char[] nextChars(int n) {
		char[] c = new char[n];
		for (int i = 0; i < n; i++)
			c[i] = nextChar();
		return c;
	}

	/**
	 * 文字列を読み込み、ソートされた文字配列として返します。
	 *
	 * @return ソートされた文字配列(char[])
	 */
	public char[] nextSortedChars() {
		char[] c = nextChars();
		sort(c);
		return c;
	}

	/**
	 * 指定された長さの文字配列を読み込み、ソートして返します。
	 *
	 * @param n 配列の長さ
	 * @return ソートされた文字配列(char[])
	 */
	public char[] nextSortedChars(int n) {
		char[] c = nextChars(n);
		sort(c);
		return c;
	}

	/**
	 * 複数の文字列を二次元の文字配列として読み込みます。
	 *
	 * @param n 行数（文字列の個数）
	 * @return 読み込んだ二次元文字配列(char[][])
	 */
	public char[][] nextCharMat(int n) {
		char[][] c = new char[n][];
		setAll(c, x -> nextChars());
		return c;
	}

	/**
	 * 指定された行数・列数の二次元文字配列を読み込みます。
	 *
	 * @param n 行数
	 * @param m 列数
	 * @return 読み込んだ二次元文字配列(char[][])
	 */
	public char[][] nextCharMat(int n, int m) {
		char[][] c = new char[n][m];
		setAll(c, x -> nextChars(m));
		return c;
	}

	/**
	 * 指定された長さの整数(int)配列を読み込みます。
	 *
	 * @param n 配列の長さ
	 * @return 読み込んだ整数配列(int[])
	 */
	public int[] nextInt(int n) {
		int[] a = new int[n];
		resetStats();
		for (int i = 0; i < n; i++)
			updateStats(a[i] = nextInt());
		return a;
	}

	/**
	 * 指定された長さの整数(Integer)配列を読み込みます。
	 *
	 * @param n 配列の長さ
	 * @return 読み込んだ整数配列(Integer[])
	 */
	public Integer[] nextInts(int n) {
		Integer[] a = new Integer[n];
		resetStats();
		for (int i = 0; i < n; i++)
			updateStats(a[i] = nextInt());
		return a;
	}

	/**
	 * 指定された長さの整数(int)配列を読み込み、ソートして返します。
	 *
	 * @param n 配列の長さ
	 * @return ソートされた整数配列(int[])
	 */
	public int[] nextSortedInt(int n) {
		int[] a = nextInt(n);
		sort(a);
		return a;
	}

	/**
	 * 指定された長さの整数(Integer)配列を読み込み、ソートして返します。
	 *
	 * @param n 配列の長さ
	 * @return ソートされた整数配列(Integer[])
	 */
	public Integer[] nextSortedInts(int n) {
		Integer[] a = nextInts(n);
		sort(a);
		return a;
	}

	/**
	 * 整数の累積和配列を読み込みます。
	 *
	 * @param n 配列の長さ
	 * @return 累積和配列(int[])
	 */
	public int[] nextIntSum(int n) {
		int[] a = new int[n];
		resetStats();
		updateStats(a[0] = nextInt());
		for (int i = 1; i < n; i++) {
			updateStats(a[i] = nextInt());
			a[i] += a[i - 1];
		}
		return a;
	}

	/**
	 * 二次元整数配列を読み込みます。
	 *
	 * @param h 行数
	 * @param w 列数
	 * @return 読み込んだ二次元整数配列(int[][])
	 */
	public int[][] nextIntMat(int h, int w) {
		int[][] a = new int[h][w];
		setAll(a, x -> nextInt(w));
		return a;
	}

	/**
	 * 二次元整数配列を読み込み、累積和配列として返します。
	 *
	 * @param h 行数
	 * @param w 列数
	 * @return 二次元累積和配列(int[][])
	 */
	public int[][] nextIntMatSum(int h, int w) {
		int[][] a = new int[h + 1][w + 1];
		for (int i = 1; i <= h; i++)
			for (int j = 1; j <= w; j++)
				a[i][j] = nextInt() + a[i - 1][j] + a[i][j - 1] - a[i - 1][j - 1];
		return a;
	}

	/**
	 * 三次元整数配列を読み込みます。
	 *
	 * @param x サイズX
	 * @param y サイズY
	 * @param z サイズZ
	 * @return 読み込んだ三次元整数配列(int[][][])
	 */
	public int[][][] nextInt3D(int x, int y, int z) {
		int[][][] a = new int[x][y][z];
		setAll(a, b -> nextIntMat(y, z));
		return a;
	}

	/**
	 * 三次元整数配列を読み込み、累積和配列として返します。
	 *
	 * @param x サイズX
	 * @param y サイズY
	 * @param z サイズZ
	 * @return 三次元累積和配列(int[][][])
	 */
	public int[][][] nextIntSum3D(int x, int y, int z) {
		int[][][] e = new int[x + 1][y + 1][z + 1];
		for (int a = 1; a <= x; a++)
			for (int b = 1; b <= y; b++)
				for (int c = 1; c <= z; c++)
					e[a][b][c] = nextInt() + e[a - 1][b][c] + e[a][b - 1][c] + e[a][b][c - 1] - e[a - 1][b - 1][c]
							- e[a - 1][b][c - 1] - e[a][b - 1][c - 1] + e[a - 1][b - 1][c - 1];
		return e;
	}

	/**
	 * 指定された長さの長整数(long)配列を読み込みます。
	 *
	 * @param n 配列の長さ
	 * @return 読み込んだ長整数配列(long[])
	 */
	public long[] nextLong(int n) {
		long[] a = new long[n];
		resetStats();
		for (int i = 0; i < n; i++)
			updateStats(a[i] = nextLong());
		return a;
	}

	/**
	 * 指定された長さの長整数(Long)配列を読み込みます。
	 *
	 * @param n 配列の長さ
	 * @return 読み込んだ長整数配列(Long[])
	 */
	public Long[] nextLongs(int n) {
		Long[] a = new Long[n];
		resetStats();
		for (int i = 0; i < n; i++)
			updateStats(a[i] = nextLong());
		return a;
	}

	/**
	 * 指定された長さの長整数(long)配列を読み込み、ソートして返します。
	 *
	 * @param n 配列の長さ
	 * @return ソートされた長整数配列(long[])
	 */
	public long[] nextSortedLong(int n) {
		long[] a = nextLong(n);
		sort(a);
		return a;
	}

	/**
	 * 指定された長さの長整数(Long)配列を読み込み、ソートして返します。
	 *
	 * @param n 配列の長さ
	 * @return ソートされた長整数配列(Long[])
	 */
	public Long[] nextSortedLongs(int n) {
		Long[] a = nextLongs(n);
		sort(a);
		return a;
	}

	/**
	 * 長整数の累積和配列を読み込みます。
	 *
	 * @param n 配列の長さ
	 * @return 累積和配列(long[])
	 */
	public long[] nextLongSum(int n) {
		long[] a = new long[n];
		low = high = sum = a[0] = nextLong();
		for (int i = 1; i < n; i++) {
			updateStats(a[i] = nextLong());
			a[i] += a[i - 1];
		}
		return a;
	}

	/**
	 * 二次元長整数配列を読み込みます。
	 *
	 * @param h 行数
	 * @param w 列数
	 * @return 読み込んだ二次元長整数配列(long[])
	 */
	public long[][] nextLongMat(int h, int w) {
		long[][] a = new long[h][w];
		setAll(a, x -> nextLong(w));
		return a;
	}

	/**
	 * 二次元長整数配列を読み込み、累積和配列として返します。
	 *
	 * @param h 行数
	 * @param w 列数
	 * @return 二次元累積和配列(long[][])
	 */
	public long[][] nextLongMatSum(int h, int w) {
		long[][] a = new long[h + 1][w + 1];
		for (int i = 1; i <= h; i++)
			for (int j = 1; j <= w; j++)
				a[i][j] = nextLong() + a[i - 1][j] + a[i][j - 1] - a[i - 1][j - 1];
		return a;
	}

	/**
	 * 三次元長整数配列を読み込みます。
	 *
	 * @param x サイズX
	 * @param y サイズY
	 * @param z サイズZ
	 * @return 読み込んだ三次元長整数配列(long[][])
	 */
	public long[][][] nextLong3D(int x, int y, int z) {
		long[][][] a = new long[x][y][z];
		setAll(a, b -> nextLongMat(y, z));
		return a;
	}

	/**
	 * 三次元長整数配列を読み込み、累積和配列として返します。
	 *
	 * @param x サイズX
	 * @param y サイズY
	 * @param z サイズZ
	 * @return 三次元累積和配列(long[][][])
	 */
	public long[][][] nextLongSum3D(int x, int y, int z) {
		long[][][] e = new long[x + 1][y + 1][z + 1];
		for (int a = 1; a <= x; a++)
			for (int b = 1; b <= y; b++)
				for (int c = 1; c <= z; c++)
					e[a][b][c] = nextLong() + e[a - 1][b][c] + e[a][b - 1][c] + e[a][b][c - 1] - e[a - 1][b - 1][c]
							- e[a - 1][b][c - 1] - e[a][b - 1][c - 1] + e[a - 1][b - 1][c - 1];
		return e;
	}

	/**
	 * 整数を含むコレクションを読み込みます。
	 *
	 * @param <T> コレクションの型
	 * @param n   要素数
	 * @param s   コレクションのサプライヤー
	 * @return 読み込んだコレクション(Collection)
	 */
	private <T extends Collection<Integer>> T nextIntCollection(int n, Supplier<T> s) {
		T c = s.get();
		resetStats();
		while (n-- > 0) {
			int a = nextInt();
			c.add(a);
			updateStats(a);
		}
		return c;
	}

	/**
	 * 指定された長さの整数のArrayListを読み込みます。
	 *
	 * @param n 要素数
	 * @return 読み込んだArrayList
	 */
	public ArrayList<Integer> nextIntAL(int n) {
		return nextIntCollection(n, ArrayList::new);
	}

	/**
	 * 指定された長さの整数のLinkedListを読み込みます。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedList
	 */
	public LinkedList<Integer> nextIntLL(int n) {
		return nextIntCollection(n, LinkedList::new);
	}

	/**
	 * 指定された長さの整数を読み込んだHashSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだHashSet
	 */
	public HashSet<Integer> nextIntHS(int n) {
		return nextIntCollection(n, HashSet::new);
	}

	/**
	 * 指定された長さの整数を読み込んだLinkedHashSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedHashSet
	 */
	public LinkedHashSet<Integer> nextIntLHS(int n) {
		return nextIntCollection(n, LinkedHashSet::new);
	}

	/**
	 * 指定された長さの整数を読み込んだTreeSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだTreeSet
	 */
	public TreeSet<Integer> nextIntTS(int n) {
		return nextIntCollection(n, TreeSet::new);
	}

	/**
	 * 長整数を含むコレクションを読み込みます。
	 *
	 * @param <T> コレクションの型
	 * @param n   要素数
	 * @param s   コレクションのサプライヤー
	 * @return 読み込んだコレクション(Collection)
	 */
	private <T extends Collection<Long>> T nextLongCollection(int n, Supplier<T> s) {
		T c = s.get();
		resetStats();
		while (n-- > 0) {
			long a = nextLong();
			c.add(a);
			updateStats(a);
		}
		return c;
	}

	/**
	 * 指定された長さの整数のArrayListを読み込みます。
	 *
	 * @param n 要素数
	 * @return 読み込んだArrayList
	 */
	public ArrayList<Long> nextLongAL(int n) {
		return nextLongCollection(n, ArrayList::new);
	}

	/**
	 * 指定された長さの整数のLinkedListを読み込みます。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedList
	 */
	public LinkedList<Long> nextLongLL(int n) {
		return nextLongCollection(n, LinkedList::new);
	}

	/**
	 * 指定された長さの長整数を読み込んだHashSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだHashSet
	 */
	public HashSet<Long> nextLongHS(int n) {
		return nextLongCollection(n, HashSet::new);
	}

	/**
	 * 指定された長さの長整数を読み込んだLinkedHashSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedHashSet
	 */
	public LinkedHashSet<Long> nextLongLHS(int n) {
		return nextLongCollection(n, LinkedHashSet::new);
	}

	/**
	 * 指定された長さの長整数を読み込んだTreeSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだTreeSet
	 */
	public TreeSet<Long> nextLongTS(int n) {
		return nextLongCollection(n, TreeSet::new);
	}

	/**
	 * 文字列を含むコレクションを読み込みます。
	 *
	 * @param <T> コレクションの型
	 * @param n   要素数
	 * @param s   コレクションのサプライヤー
	 * @return 読み込んだコレクション(Collection)
	 */
	private <T extends Collection<String>> T nextStringCollection(int n, Supplier<T> s) {
		T c = s.get();
		while (n-- > 0) {
			c.add(next());
		}
		return c;
	}

	/**
	 * 指定された長さの文字列のArrayListを読み込みます。
	 *
	 * @param n 要素数
	 * @return 読み込んだArrayList
	 */
	public ArrayList<String> nextStringAL(int n) {
		return nextStringCollection(n, ArrayList::new);
	}

	/**
	 * 指定された長さの文字列のLinkedListを読み込みます。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedList
	 */
	public LinkedList<String> nextStringLL(int n) {
		return nextStringCollection(n, LinkedList::new);
	}

	/**
	 * 指定された長さの文字列を読み込んだHashSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだHashSet
	 */
	public HashSet<String> nextStringHS(int n) {
		return nextStringCollection(n, HashSet::new);
	}

	/**
	 * 指定された長さの文字列を読み込んだLinkedHashSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedHashSet
	 */
	public LinkedHashSet<String> nextStringLHS(int n) {
		return nextStringCollection(n, LinkedHashSet::new);
	}

	/**
	 * 指定された長さの文字列を読み込んだTreeSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだTreeSet
	 */
	public TreeSet<String> nextStringTS(int n) {
		return nextStringCollection(n, TreeSet::new);
	}

	/**
	 * 文字を含むコレクションを読み込みます。
	 *
	 * @param <T> コレクションの型
	 * @param n   要素数
	 * @param s   コレクションのサプライヤー
	 * @return 読み込んだコレクション(Collection)
	 */
	private <T extends Collection<Character>> T nextCharacterCollection(int n, Supplier<T> s) {
		T c = s.get();
		resetStats();
		while (n-- > 0) {
			char t = nextChar();
			c.add(t);
			updateStats(t);
		}
		return c;
	}

	/**
	 * 指定された長さの文字のArrayListを読み込みます。
	 *
	 * @param n 要素数
	 * @return 読み込んだArrayList
	 */
	public ArrayList<Character> nextCharacterAL(int n) {
		return nextCharacterCollection(n, ArrayList::new);
	}

	/**
	 * 指定された長さの文字のLinkedListを読み込みます。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedList
	 */
	public LinkedList<Character> nextCharacterLL(int n) {
		return nextCharacterCollection(n, LinkedList::new);
	}

	/**
	 * 指定された長さの文字を読み込んだHashSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだHashSet
	 */
	public HashSet<Character> nextCharacterHS(int n) {
		return nextCharacterCollection(n, HashSet::new);
	}

	/**
	 * 指定された長さの文字を読み込んだLinkedHashSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedHashSet
	 */
	public LinkedHashSet<Character> nextCharacterLHS(int n) {
		return nextCharacterCollection(n, LinkedHashSet::new);
	}

	/**
	 * 指定された長さの文字を読み込んだTreeSetを返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだTreeSet
	 */
	public TreeSet<Character> nextCharacterTS(int n) {
		return nextCharacterCollection(n, TreeSet::new);
	}

	/**
	 * 整数をKey、出現回数をValueとするMapを読み込みます。
	 *
	 * @param <T> Mapの型
	 * @param n   要素数
	 * @param s   Mapのサプライヤー
	 * @return 読み込んだMap
	 */
	private <T extends Map<Integer, Integer>> T nextIntMultiset(int n, Supplier<T> s) {
		T c = s.get();
		resetStats();
		while (n-- > 0) {
			int a = nextInt();
			c.put(a, c.getOrDefault(a, 0) + 1);
			updateStats(a);
		}
		return c;
	}

	/**
	 * Key(Integer)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだHashMap
	 */
	public HashMap<Integer, Integer> nextIntMultisetHM(int n) {
		return nextIntMultiset(n, HashMap::new);
	}

	/**
	 * Key(Integer)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedHashMap
	 */
	public LinkedHashMap<Integer, Integer> nextIntMultisetLHM(int n) {
		return nextIntMultiset(n, LinkedHashMap::new);
	}

	/**
	 * Key(Integer)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 読み込む配列の長さ
	 * @return 読み込んだTreeMap
	 */
	public TreeMap<Integer, Integer> nextIntMultisetTM(int n) {
		return nextIntMultiset(n, TreeMap::new);
	}

	/**
	 * 長整数をKey、出現回数をValueとするMapを読み込みます。
	 *
	 * @param <T> Mapの型
	 * @param n   要素数
	 * @param s   Mapのサプライヤー
	 * @return 読み込んだMap
	 */
	private <T extends Map<Long, Integer>> T nextLongMultiset(int n, Supplier<T> s) {
		T c = s.get();
		resetStats();
		while (n-- > 0) {
			long a = nextLong();
			c.put(a, c.getOrDefault(a, 0) + 1);
			updateStats(a);
		}
		return c;
	}

	/**
	 * Key(Long)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだHashMap
	 */
	public HashMap<Long, Integer> nextLongMultisetHM(int n) {
		return nextLongMultiset(n, HashMap::new);
	}

	/**
	 * Key(Long)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedHashMap
	 */
	public LinkedHashMap<Long, Integer> nextLongMultisetLHM(int n) {
		return nextLongMultiset(n, LinkedHashMap::new);
	}

	/**
	 * Key(Long)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 読み込む配列の長さ
	 * @return 読み込んだTreeMap
	 */
	public TreeMap<Long, Integer> nextLongMultisetTM(int n) {
		return nextLongMultiset(n, TreeMap::new);
	}

	/**
	 * 文字列をKey、出現回数をValueとするMapを読み込みます。
	 *
	 * @param <T> Mapの型
	 * @param n   要素数
	 * @param s   Mapのサプライヤー
	 * @return 読み込んだMap
	 */
	private <T extends Map<String, Integer>> T nextStringMultiset(int n, Supplier<T> s) {
		T c = s.get();
		while (n-- > 0) {
			String a = next();
			c.put(a, c.getOrDefault(a, 0) + 1);
		}
		return c;
	}

	/**
	 * Key(String)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだHashMap
	 */
	public HashMap<String, Integer> nextStringMultisetHM(int n) {
		return nextStringMultiset(n, HashMap::new);
	}

	/**
	 * Key(String)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedHashMap
	 */
	public LinkedHashMap<String, Integer> nextStringMultisetLHM(int n) {
		return nextStringMultiset(n, LinkedHashMap::new);
	}

	/**
	 * Key(String)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 読み込む配列の長さ
	 * @return 読み込んだTreeMap
	 */
	public TreeMap<String, Integer> nextStringMultisetTM(int n) {
		return nextStringMultiset(n, TreeMap::new);
	}

	/**
	 * 文字をKey、出現回数をValueとするMapを読み込みます。
	 *
	 * @param <T> Mapの型
	 * @param n   要素数
	 * @param s   Mapのサプライヤー
	 * @return 読み込んだMap
	 */
	private <T extends Map<Character, Integer>> T nextCharMultiset(int n, Supplier<T> s) {
		T c = s.get();
		resetStats();
		while (n-- > 0) {
			char a = nextChar();
			c.put(a, c.getOrDefault(a, 0) + 1);
			updateStats(a);
		}
		return c;
	}

	/**
	 * Key(Character)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだHashMap
	 */
	public HashMap<Character, Integer> nextCharMultisetHM(int n) {
		return nextCharMultiset(n, HashMap::new);
	}

	/**
	 * Key(Character)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 要素数
	 * @return 読み込んだLinkedHashMap
	 */
	public LinkedHashMap<Character, Integer> nextCharacterMultisetLHM(int n) {
		return nextCharMultiset(n, LinkedHashMap::new);
	}

	/**
	 * Key(Character)に対しValueをその出現回数とする写像を返します。
	 *
	 * @param n 読み込む配列の長さ
	 * @return 読み込んだTreeMap
	 */
	public TreeMap<Character, Integer> nextCharMultisetTM(int n) {
		return nextCharMultiset(n, TreeMap::new);
	}

	/**
	 * 読み込んだ配列の最小値を返します。
	 *
	 * @return 最小値
	 */
	public long getLowestNum() {
		return low;
	}

	/**
	 * 読み込んだ配列の最大値を返します。
	 *
	 * @return 最大値
	 */
	public long getHighestNum() {
		return high;
	}

	/**
	 * 読み込んだ配列の総和を返します。
	 *
	 * @return 総和
	 */
	public long getSum() {
		return sum;
	}

	/**
	 * 統計情報をリセットします（最小値、最大値、総和）。
	 */
	private void resetStats() {
		low = Long.MAX_VALUE;
		high = Long.MIN_VALUE;
		sum = 0;
	}

	/**
	 * 読み込んだ値で統計情報（最小値、最大値、総和）を更新します。
	 *
	 * @param a 更新する値
	 */
	private void updateStats(long a) {
		sum += a;
		low = min(low, a);
		high = max(high, a);
	}
}
