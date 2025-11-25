import java.io.*;
import java.util.*;
import java.util.function.*;

import static java.util.Arrays.*;

/**
 * {@code ContestScanner} は、競技プログラミング向けの高速入力ユーティリティです。<br>
 * {@link FastScanner} を拡張し、各種配列、2次元・3次元配列、ソート済み配列、累積和配列、逆写像配列、各種コレクションの入力をサポートします。
 */
@SuppressWarnings("unused")
public final class ContestScanner extends FastScanner {

	/* ------------------------ コンストラクタ ------------------------ */

	/**
	 * デフォルトの設定で {@code ContestScanner} を初期化します。
	 * <ul>
	 *   <li>バッファ容量: 65536</li>
	 *   <li>{@code InputStream}: {@code System.in}</li>
	 * </ul>
	 */
	public ContestScanner() {
		super();
	}

	/**
	 * 指定された {@code InputStream} を使用して {@code ContestScanner} を初期化します。
	 * <ul>
	 *   <li>バッファ容量: 65536</li>
	 * </ul>
	 *
	 * @param in 入力元の {@code InputStream}
	 */
	public ContestScanner(final InputStream in) {
		super(in);
	}

	/**
	 * 指定されたバッファ容量を用いて {@code ContestScanner} を初期化します。
	 * <ul>
	 *   <li>入力元: {@code System.in}</li>
	 * </ul>
	 *
	 * @param bufferSize 内部バッファの容量（バイト単位）
	 */
	public ContestScanner(final int bufferSize) {
		super(bufferSize);
	}

	/**
	 * 指定された {@code InputStream} とバッファ容量で {@code FastScanner} を初期化します。
	 *
	 * @param in         入力元の {@code InputStream}
	 * @param bufferSize 内部バッファの容量（バイト単位）
	 */
	public ContestScanner(final InputStream in, final int bufferSize) {
		super(in, bufferSize);
	}

	/* ------------------------ 1次元配列入力メソッド ------------------------ */

	/**
	 * 指定された要素数の int 配列を読み込みます。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだ int 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[] nextInt(final int n) {
		final int[] a = new int[n];
		for (int i = 0; i < n; i++) a[i] = nextInt();
		return a;
	}

	/**
	 * 指定された要素数の long 配列を読み込みます。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだ long 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public long[] nextLong(final int n) {
		final long[] a = new long[n];
		for (int i = 0; i < n; i++) a[i] = nextLong();
		return a;
	}

	/**
	 * 指定された要素数の double 配列を読み込みます。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだ double 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public double[] nextDouble(final int n) {
		final double[] a = new double[n];
		for (int i = 0; i < n; i++) a[i] = nextDouble();
		return a;
	}

	/**
	 * 次の文字列を char 配列として読み込みます。
	 *
	 * @return 読み込んだ char 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public char[] nextChars() {
		return next().toCharArray();
	}

	/**
	 * 指定された要素数の char 配列を読み込みます。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだ char 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public char[] nextChars(final int n) {
		final char[] c = new char[n];
		for (int i = 0; i < n; i++) c[i] = nextChar();
		return c;
	}

	/**
	 * 指定された要素数の String 配列を読み込みます。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだ String 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public String[] nextStrings(final int n) {
		final String[] s = new String[n];
		for (int i = 0; i < n; i++) s[i] = next();
		return s;
	}

	/* ------------------------ 2次元配列入力メソッド ------------------------ */

	/**
	 * 指定された行数・列数の int 2次元配列を読み込みます。
	 *
	 * @param h 読み込む配列の行数
	 * @param w 読み込む配列の列数
	 * @return 読み込んだ int 2次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[][] nextIntMat(final int h, final int w) {
		final int[][] a = new int[h][w];
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				a[i][j] = nextInt();
		return a;
	}

	/**
	 * 指定された行数・列数の long 2次元配列を読み込みます。
	 *
	 * @param h 読み込む配列の行数
	 * @param w 読み込む配列の列数
	 * @return 読み込んだ long 2次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public long[][] nextLongMat(final int h, final int w) {
		final long[][] a = new long[h][w];
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				a[i][j] = nextLong();
		return a;
	}

	/**
	 * 指定された行数・列数の double 2次元配列を読み込みます。
	 *
	 * @param h 読み込む配列の行数
	 * @param w 読み込む配列の列数
	 * @return 読み込んだ double 2次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public double[][] nextDoubleMat(final int h, final int w) {
		final double[][] a = new double[h][w];
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				a[i][j] = nextDouble();
		return a;
	}

	/**
	 * 指定された行数の文字列を char 2次元配列として読み込みます。
	 *
	 * @param n 読み込む配列の行数
	 * @return 読み込んだ char 2次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public char[][] nextCharMat(final int n) {
		final char[][] c = new char[n][];
		for (int i = 0; i < n; i++) c[i] = nextChars();
		return c;
	}

	/**
	 * 指定された行数・列数の char 2次元配列を読み込みます。
	 *
	 * @param h 読み込む配列の行数
	 * @param w 読み込む配列の列数
	 * @return 読み込んだ char 2次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public char[][] nextCharMat(final int h, final int w) {
		final char[][] c = new char[h][w];
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				c[i][j] = nextChar();
		return c;
	}

	/**
	 * 指定された行数・列数の String 2次元配列を読み込みます。
	 *
	 * @param h 読み込む配列の行数
	 * @param w 読み込む配列の列数
	 * @return 読み込んだ String 2次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public String[][] nextStringMat(final int h, final int w) {
		final String[][] s = new String[h][w];
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				s[i][j] = next();
		return s;
	}

	/* ------------------------ 3次元配列入力メソッド ------------------------ */

	/**
	 * 指定されたサイズの int 3次元配列を読み込みます。
	 *
	 * @param x 読み込む配列の第1次元のサイズ
	 * @param y 読み込む配列の第2次元のサイズ
	 * @param z 読み込む配列の第3次元のサイズ
	 * @return 読み込んだ int 3次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[][][] nextInt3D(final int x, final int y, final int z) {
		final int[][][] a = new int[x][y][z];
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				for (int k = 0; k < z; k++)
					a[i][j][k] = nextInt();
		return a;
	}

	/**
	 * 指定されたサイズの long 3次元配列を読み込みます。
	 *
	 * @param x 読み込む配列の第1次元のサイズ
	 * @param y 読み込む配列の第2次元のサイズ
	 * @param z 読み込む配列の第3次元のサイズ
	 * @return 読み込んだ long 3次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public long[][][] nextLong3D(final int x, final int y, final int z) {
		final long[][][] a = new long[x][y][z];
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				for (int k = 0; k < z; k++)
					a[i][j][k] = nextLong();
		return a;
	}

	/* ------------------------ ソート済み1次元配列入力メソッド ------------------------ */

	/**
	 * 指定された要素数の int 配列を読み込み、ソートして返します。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだソート済み int 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[] nextSortedInt(final int n) {
		final int[] a = nextInt(n);
		sort(a);
		return a;
	}

	/**
	 * 指定された要素数の long 配列を読み込み、ソートして返します。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだソート済み long 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public long[] nextSortedLong(final int n) {
		final long[] a = nextLong(n);
		sort(a);
		return a;
	}

	/**
	 * 指定された要素数の double 配列を読み込み、ソートして返します。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだソート済み double 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public double[] nextSortedDouble(final int n) {
		final double[] a = nextDouble(n);
		sort(a);
		return a;
	}

	/**
	 * 次の文字列を char 配列として読み込み、ソートして返します。
	 *
	 * @return 読み込んだソート済み char 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public char[] nextSortedChars() {
		final char[] c = nextChars();
		sort(c);
		return c;
	}

	/**
	 * 指定された要素数の char 配列を読み込み、ソートして返します。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだソート済み char 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public char[] nextSortedChars(final int n) {
		final char[] c = nextChars(n);
		sort(c);
		return c;
	}

	/**
	 * 指定された要素数の String 配列を読み込み、ソートして返します。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだソート済み String 配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public String[] nextSortedStrings(final int n) {
		final String[] s = nextStrings(n);
		sort(s);
		return s;
	}

	/* ------------------------ 累積和配列入力メソッド ------------------------ */

	/**
	 * 指定された要素数の int 累積和配列を読み込みます。
	 * 戻り値の配列サイズは {@code n} となります。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだ int 累積和配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[] nextIntPrefixSum(final int n) {
		final int[] ps = new int[n];
		ps[0] = nextInt();
		for (int i = 1; i < n; i++) ps[i] = nextInt() + ps[i - 1];
		return ps;
	}

	/**
	 * 指定された要素数の long 累積和配列を読み込みます。
	 * 戻り値の配列サイズは {@code n} となります。
	 *
	 * @param n 読み込む配列の要素数
	 * @return 読み込んだ long 累積和配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public long[] nextLongPrefixSum(final int n) {
		final long[] ps = new long[n];
		ps[0] = nextLong();
		for (int i = 1; i < n; i++) ps[i] = nextLong() + ps[i - 1];
		return ps;
	}

	/**
	 * 指定された行数・列数の int 2次元累積和配列を読み込みます。
	 * 戻り値の配列サイズは {@code (h + 1) * (w + 1)} となります。
	 *
	 * @param h 読み込む配列の行数
	 * @param w 読み込む配列の列数
	 * @return 読み込んだ int 2次元累積和配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[][] nextIntPrefixSum(final int h, final int w) {
		final int[][] ps = new int[h + 1][w + 1];
		for (int i = 1; i <= h; i++)
			for (int j = 1; j <= w; j++)
				ps[i][j] = nextInt() + ps[i - 1][j] + ps[i][j - 1] - ps[i - 1][j - 1];
		return ps;
	}

	/**
	 * 指定された行数・列数の long 2次元累積和配列を読み込みます。
	 * 戻り値の配列サイズは {@code (h + 1) * (w + 1)} となります。
	 *
	 * @param h 読み込む配列の行数
	 * @param w 読み込む配列の列数
	 * @return 読み込んだ long 2次元累積和配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public long[][] nextLongPrefixSum(final int h, final int w) {
		final long[][] ps = new long[h + 1][w + 1];
		for (int i = 1; i <= h; i++)
			for (int j = 1; j <= w; j++)
				ps[i][j] = nextLong() + ps[i - 1][j] + ps[i][j - 1] - ps[i - 1][j - 1];
		return ps;
	}

	/**
	 * 指定されたサイズの int 3次元累積和配列を読み込みます。
	 * 戻り値の配列サイズは {@code (x + 1) * (y + 1) * (z + 1)} となります。
	 *
	 * @param x 読み込む配列の第1次元のサイズ
	 * @param y 読み込む配列の第2次元のサイズ
	 * @param z 読み込む配列の第3次元のサイズ
	 * @return 読み込んだ int 3次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[][][] nextIntPrefixSum(final int x, final int y, final int z) {
		final int[][][] ps = new int[x + 1][y + 1][z + 1];
		for (int a = 1; a <= x; a++)
			for (int b = 1; b <= y; b++)
				for (int c = 1; c <= z; c++)
					ps[a][b][c] = nextInt() + ps[a - 1][b][c] + ps[a][b - 1][c] + ps[a][b][c - 1] - ps[a - 1][b - 1][c]
							- ps[a - 1][b][c - 1] - ps[a][b - 1][c - 1] + ps[a - 1][b - 1][c - 1];
		return ps;
	}

	/**
	 * 指定されたサイズの long 3次元累積和配列を読み込みます。
	 * 戻り値の配列サイズは {@code (x + 1) * (y + 1) * (z + 1)} となります。
	 *
	 * @param x 読み込む配列の第1次元のサイズ
	 * @param y 読み込む配列の第2次元のサイズ
	 * @param z 読み込む配列の第3次元のサイズ
	 * @return 読み込んだ long 3次元配列
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public long[][][] nextLongPrefixSum(final int x, final int y, final int z) {
		final long[][][] ps = new long[x + 1][y + 1][z + 1];
		for (int a = 1; a <= x; a++)
			for (int b = 1; b <= y; b++)
				for (int c = 1; c <= z; c++)
					ps[a][b][c] = nextLong() + ps[a - 1][b][c] + ps[a][b - 1][c] + ps[a][b][c - 1] - ps[a - 1][b - 1][c]
							- ps[a - 1][b][c - 1] - ps[a][b - 1][c - 1] + ps[a - 1][b - 1][c - 1];
		return ps;
	}

	/* ------------------------ 逆写像配列入力メソッド ------------------------ */

	/**
	 * 1 から n までの整数の順列に対する逆写像配列を生成します。<br>
	 * （例）n=3 で入力が「3 1 2」の場合、返される配列は {1, 2, 0} となります。<br>
	 * <strong>注意:</strong> このメソッドは入力が 1 から n までの整数を一度ずつ含む順列であることを前提とします。
	 *
	 * @param n 順列の要素数
	 * @return int 逆写像配列。配列の k-1 番目の要素は、値 k が入力の何番目 (0-indexed) に現れたかを示します。
	 * @throws RuntimeException               ストリームの終端に達した場合など、読み込みに失敗した場合
	 * @throws ArrayIndexOutOfBoundsException 入力値が [1, n] の範囲外の場合
	 */
	public int[] nextIntInverseMapping(final int n) {
		final int[] inv = new int[n];
		for (int i = 0; i < n; i++) inv[nextInt() - 1] = i;
		return inv;
	}

	/* ------------------------ Collection<Integer>入力メソッド ------------------------ */

	/**
	 * 指定された要素数の {@code ArrayList<Integer>} を読み込みます。
	 *
	 * @param n 読み込む {@code ArrayList<Integer>} の要素数
	 * @return 読み込んだ  {@code ArrayList<Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public ArrayList<Integer> nextIntAL(final int n) {
		return nextCollection(n, this::nextInt, () -> new ArrayList<>(n));
	}

	/**
	 * 指定された要素数の {@code HashSet<Integer>} を読み込みます。
	 *
	 * @param n 読み込む {@code HashSet<Integer>} の要素数
	 * @return 読み込んだ  {@code HashSet<Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public HashSet<Integer> nextIntHS(final int n) {
		return nextCollection(n, this::nextInt, () -> new HashSet<>(n));
	}

	/**
	 * 指定された要素数の {@code TreeSet<Integer>} を読み込みます。
	 *
	 * @param n 読み込む {@code TreeSet<Integer>} の要素数
	 * @return 読み込んだ  {@code TreeSet<Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public TreeSet<Integer> nextIntTS(final int n) {
		return nextCollection(n, this::nextInt, TreeSet::new);
	}

	/* ------------------------ Collection<Long>入力メソッド ------------------------ */

	/**
	 * 指定された要素数の {@code ArrayList<Long>} を読み込みます。
	 *
	 * @param n 読み込む {@code ArrayList<Long>} の要素数
	 * @return 読み込んだ  {@code ArrayList<Long>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public ArrayList<Long> nextLongAL(final int n) {
		return nextCollection(n, this::nextLong, () -> new ArrayList<>(n));
	}

	/**
	 * 指定された要素数の {@code HashSet<Long>} を読み込みます。
	 *
	 * @param n 読み込む {@code HashSet<Long>} の要素数
	 * @return 読み込んだ  {@code HashSet<Long>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public HashSet<Long> nextLongHS(final int n) {
		return nextCollection(n, this::nextLong, () -> new HashSet<>(n));
	}

	/**
	 * 指定された要素数の {@code TreeSet<Long>} を読み込みます。
	 *
	 * @param n 読み込む {@code TreeSet<Long>} の要素数
	 * @return 読み込んだ  {@code TreeSet<Long>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public TreeSet<Long> nextLongTS(final int n) {
		return nextCollection(n, this::nextLong, TreeSet::new);
	}

	/* ------------------------ Collection<Character>入力メソッド ------------------------ */

	/**
	 * 指定された要素数の {@code ArrayList<Character>} を読み込みます。
	 *
	 * @param n 読み込む {@code ArrayList<Character>} の要素数
	 * @return 読み込んだ  {@code ArrayList<Character>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public ArrayList<Character> nextCharacterAL(final int n) {
		return nextCollection(n, this::nextChar, () -> new ArrayList<>(n));
	}

	/**
	 * 指定された要素数の {@code HashSet<Character>} を読み込みます。
	 *
	 * @param n 読み込む {@code HashSet<Character>} の要素数
	 * @return 読み込んだ  {@code HashSet<Character>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public HashSet<Character> nextCharacterHS(final int n) {
		return nextCollection(n, this::nextChar, () -> new HashSet<>(n));
	}

	/**
	 * 指定された要素数の {@code TreeSet<Character>} を読み込みます。
	 *
	 * @param n 読み込む {@code TreeSet<Character>} の要素数
	 * @return 読み込んだ  {@code TreeSet<Character>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public TreeSet<Character> nextCharacterTS(final int n) {
		return nextCollection(n, this::nextChar, TreeSet::new);
	}

	/* ------------------------ Collection<String>入力メソッド ------------------------ */

	/**
	 * 指定された要素数の {@code ArrayList<String>} を読み込みます。
	 *
	 * @param n 読み込む {@code ArrayList<String>} の要素数
	 * @return 読み込んだ  {@code ArrayList<String>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public ArrayList<String> nextStringAL(final int n) {
		return nextCollection(n, this::next, () -> new ArrayList<>(n));
	}

	/**
	 * 指定された要素数の {@code HashSet<String>} を読み込みます。
	 *
	 * @param n 読み込む {@code HashSet<String>} の要素数
	 * @return 読み込んだ  {@code HashSet<String>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public HashSet<String> nextStringHS(final int n) {
		return nextCollection(n, this::next, () -> new HashSet<>(n));
	}

	/**
	 * 指定された要素数の {@code TreeSet<String>} を読み込みます。
	 *
	 * @param n 読み込む {@code TreeSet<String>} の要素数
	 * @return 読み込んだ  {@code TreeSet<String>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public TreeSet<String> nextStringTS(final int n) {
		return nextCollection(n, this::next, TreeSet::new);
	}

	/* ------------------------ Collection 入力ヘルパーメソッド ------------------------ */

	/**
	 * 指定された要素数の {@code Collection<E>} を読み込みます。
	 *
	 * @param <S>        要素の型
	 * @param <T>        コレクションの型
	 * @param n          読み込む {@code Collection<E>} の要素数
	 * @param input      要素を1つ読み込むためのサプライヤ (例: this::nextInt)
	 * @param collection コレクションのインスタンスを生成するサプライヤ
	 * @return 読み込んだ要素のコレクション
	 */
	private <S, T extends Collection<S>> T nextCollection(int n, final Supplier<S> input, final Supplier<T> collection) {
		final T t = collection.get();
		while (n-- > 0) t.add(input.get());
		return t;
	}

	/* ------------------------ Multiset (Map) 入力メソッド ------------------------ */

	/**
	 * int の出現回数をカウントし、{@code HashMap<Integer, Integer>} として返します。
	 *
	 * @param n 要素数
	 * @return int の出現回数を格納した {@code HashMap<Integer, Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public HashMap<Integer, Integer> nextIntMultisetHM(final int n) {
		return nextMultiset(n, this::nextInt, () -> new HashMap<>(n));
	}

	/**
	 * int の出現回数をカウントし、{@code TreeMap<Integer, Integer>} として返します。
	 *
	 * @param n 要素数
	 * @return int の出現回数を格納した {@code TreeMap<Integer, Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public TreeMap<Integer, Integer> nextIntMultisetTM(final int n) {
		return nextMultiset(n, this::nextInt, TreeMap::new);
	}

	/**
	 * long の出現回数をカウントし、{@code HashMap<Long, Integer>} として返します。
	 *
	 * @param n 要素数
	 * @return long の出現回数を格納した {@code HashMap<Long, Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public HashMap<Long, Integer> nextLongMultisetHM(final int n) {
		return nextMultiset(n, this::nextLong, () -> new HashMap<>(n));
	}

	/**
	 * long の出現回数をカウントし、{@code TreeMap<Long, Integer>} として返します。
	 *
	 * @param n 要素数
	 * @return long の出現回数を格納した {@code TreeMap<Long, Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public TreeMap<Long, Integer> nextLongMultisetTM(final int n) {
		return nextMultiset(n, this::nextLong, TreeMap::new);
	}

	/**
	 * char の出現回数をカウントし、{@code HashMap<Character, Integer>} として返します。
	 *
	 * @param n 要素数
	 * @return char の出現回数を格納した {@code HashMap<Character, Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public HashMap<Character, Integer> nextCharMultisetHM(final int n) {
		return nextMultiset(n, this::nextChar, () -> new HashMap<>(n));
	}

	/**
	 * char の出現回数をカウントし、{@code TreeMap<Character, Integer>} として返します。
	 *
	 * @param n 要素数
	 * @return char の出現回数を格納した {@code TreeMap<Character, Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public TreeMap<Character, Integer> nextCharMultisetTM(final int n) {
		return nextMultiset(n, this::nextChar, TreeMap::new);
	}

	/**
	 * String の出現回数をカウントし、{@code HashMap<String, Integer>} として返します。
	 *
	 * @param n 要素数
	 * @return String の出現回数を格納した {@code HashMap<String, Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public HashMap<String, Integer> nextStringMultisetHM(final int n) {
		return nextMultiset(n, this::next, () -> new HashMap<>(n));
	}

	/**
	 * String の出現回数をカウントし、{@code TreeMap<String, Integer>} として返します。
	 *
	 * @param n 要素数
	 * @return String の出現回数を格納した {@code TreeMap<String, Integer>}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public TreeMap<String, Integer> nextStringMultisetTM(final int n) {
		return nextMultiset(n, this::next, TreeMap::new);
	}

	/* ------------------------ MultiSet 入力ヘルパーメソッド ------------------------ */

	/**
	 * 指定した型の要素の出現回数をカウントしたマルチセットを読み込みます。
	 *
	 * @param <S>   要素の型
	 * @param <T>   マップの型
	 * @param n     要素数
	 * @param input 要素を1つ読み込むためのサプライヤ (例: this::nextInt)
	 * @param map   マップのインスタンスを生成するサプライヤ
	 * @return 整数のマルチセット（マップ）
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	private <S, T extends Map<S, Integer>> T nextMultiset(int n, final Supplier<S> input, final Supplier<T> map) {
		final T multiSet = map.get();
		while (n-- > 0) {
			final S i = input.get();
			multiSet.put(i, multiSet.getOrDefault(i, 0) + 1);
		}
		return multiSet;
	}

	/* ------------------------ Multiset (配列) 入力メソッド ------------------------ */

	/**
	 * int の出現回数をカウントし、{@code int[]} として返します。
	 * <strong>注意:</strong> このメソッドは入力値が 1-indexed で、かつ [1, m] の範囲内であることを前提とします。
	 *
	 * @param n 要素数
	 * @param m カウント対象の最大値
	 * @return int の出現回数を格納した {@code int[]}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[] nextIntMultiset(final int n, final int m) {
		final int[] multiset = new int[m];
		for (int i = 0; i < n; i++) multiset[nextInt() - 1]++;
		return multiset;
	}

	/**
	 * 大文字 char の出現回数をカウントし、{@code int[]} として返します。
	 * <strong>注意:</strong> このメソッドは入力値が 大文字 であることを前提とします。
	 *
	 * @param n 要素数
	 * @return 大文字 char の出現回数を格納した {@code int[]}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[] nextUpperCharMultiset(final int n) {
		return nextCharMultiset(n, 'A', 'Z');
	}

	/**
	 * 小文字 char の出現回数をカウントし、{@code int[]} として返します。
	 * <strong>注意:</strong> このメソッドは入力値が 小文字 であることを前提とします。
	 *
	 * @param n 要素数
	 * @return 小文字 char の出現回数を格納した {@code int[]}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[] nextLowerCharMultiset(final int n) {
		return nextCharMultiset(n, 'a', 'z');
	}

	/**
	 * 指定した範囲の char の出現回数をカウントし、{@code int[]} として返します。
	 * <strong>注意:</strong> このメソッドは入力値が [l, r] の範囲内であることを前提とします。
	 *
	 * @param n 要素数
	 * @param l 範囲の開始文字
	 * @param r 範囲の終了文字
	 * @return 指定した範囲の char の出現回数を格納した {@code int[]}
	 * @throws RuntimeException ストリームの終端に達した場合など、読み込みに失敗した場合
	 */
	public int[] nextCharMultiset(int n, final char l, final char r) {
		final int[] multiset = new int[r - l + 1];
		while (n-- > 0) {
			final int c = nextChar() - l;
			multiset[c]++;
		}
		return multiset;
	}
}