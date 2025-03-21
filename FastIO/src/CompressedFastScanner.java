import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Arrays.setAll;
import static java.util.Arrays.sort;

public class CompressedFastScanner {

	/**
	 * 競技プログラミング向けの高速入力クラスです。
	 * 内部バッファを利用して InputStream からの入力を効率的に処理します。
	 * ※ ASCII 範囲外の文字は正しく処理できません。入力は半角スペースまたは改行で区切られていることを前提とします.
	 */
	@SuppressWarnings("unused")
	private static final class FastScanner implements AutoCloseable {

//  ------------------------ 定数 ------------------------

		/**
		 * 入力用内部バッファのデフォルトサイズ（バイト単位）。
		 */
		private static final int DEFAULT_BUFFER_SIZE = 65536;

//  --------------------- インスタンス変数 ---------------------

		/**
		 * 入力元の InputStream です。デフォルトは {@code System.in} です。
		 */
		private final InputStream in;

		/**
		 * 入力データを一時的に格納する内部バッファです。<br>
		 * 読み込み時に {@link #read()} でデータを取得し、バッファから消費します。
		 */
		private final byte[] buffer;

		/**
		 * 現在のバッファ内で次に読み込む位置
		 */
		private int pos = 0;

		/**
		 * バッファに読み込まれているバイト数
		 */
		private int bufferLength = 0;

//  ---------------------- コンストラクタ ----------------------

		/**
		 * デフォルトの設定でFastScannerを初期化します。<br>
		 * バッファ容量: 65536 <br>
		 * InputStream: System.in <br>
		 */
		public FastScanner() {
			this(System.in, DEFAULT_BUFFER_SIZE);
		}

		/**
		 * 指定されたInputStreamを用いてFastScannerを初期化します。<br>
		 * バッファ容量: 65536 <br>
		 *
		 * @param in 入力元の InputStream
		 */
		public FastScanner(InputStream in) {
			this(in, DEFAULT_BUFFER_SIZE);
		}

		/**
		 * 指定されたバッファ容量でFastScannerを初期化します。<br>
		 * InputStream: System.in <br>
		 *
		 * @param bufferSize 内部バッファの容量（文字単位）
		 */
		public FastScanner(int bufferSize) {
			this(System.in, bufferSize);
		}

		/**
		 * 指定されたバッファ容量とInputStreamでFastScannerを初期化します。<br>
		 *
		 * @param in         入力元の InputStream
		 * @param bufferSize 内部バッファの容量（文字単位）
		 */
		public FastScanner(InputStream in, int bufferSize) {
			this.in = in;
			this.buffer = new byte[bufferSize];
		}

//  -------------------- オーバーライドメソッド --------------------

		/**
		 * このInputStreamを閉じます。
		 * 入力元が {@code System.in} の場合、閉じません。
		 *
		 * @throws IOException {@code close}の際にエラーが発生した場合
		 */
		@Override
		public void close() throws IOException {
			if (in != System.in)
				in.close();
		}

		/**
		 * 内部バッファから 1 バイトを読み込みます。<br>
		 * バッファが空の場合、新たにデータを読み込みます。
		 *
		 * @return 読み込んだバイト
		 * @throws RuntimeException 入力終了または I/O エラー時
		 */
		public byte read() {
			if (pos >= bufferLength) {
				try {
					bufferLength = in.read(buffer, pos = 0, buffer.length);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (bufferLength < 0) {
					throw new RuntimeException(new IOException("End of input reached"));
				}
			}
			return buffer[pos++];
		}

//  -------------------- 基本入力メソッド --------------------

		/**
		 * 次の int 値を読み込みます。
		 *
		 * @return 読み込んだ int 値
		 */
		public int nextInt() {
			int b = read();
			while (isDelimiter(b)) b = read();
			boolean negative = b == '-';
			if (negative) b = read();
			int result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			return negative ? -result : result;
		}

		/**
		 * 次の long 値を読み込みます。
		 *
		 * @return 読み込んだ long 値
		 */
		public long nextLong() {
			int b = read();
			while (isDelimiter(b)) b = read();
			boolean negative = b == '-';
			if (negative) b = read();
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			return negative ? -result : result;
		}

		/**
		 * 次の double 値を読み込みます。
		 *
		 * @return 読み込んだ double 値
		 */
		public double nextDouble() {
			int b = read();
			while (isDelimiter(b)) b = read();
			boolean negative = b == '-';
			if (negative) b = read();
			double result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			if (b == '.') {
				b = read();
				double factor = 10;
				while ('0' <= b && b <= '9') {
					result += (b - '0') / factor;
					factor *= 10;
					b = read();
				}
			}
			return negative ? -result : result;
		}

		/**
		 * 次の char 値（非空白文字）を読み込みます。
		 *
		 * @return 読み込んだ char 値
		 */
		public char nextChar() {
			byte b = read();
			while (isDelimiter(b)) b = read();
			return (char) b;
		}

		/**
		 * 次の String（空白で区切られた文字列）を読み込みます。
		 *
		 * @return 読み込んだ String
		 */
		public String next() {
			return nextStringBuilder().toString();
		}

		/**
		 * 次の StringBuilder（空白で区切られた文字列）を読み込みます。
		 *
		 * @return 読み込んだ StringBuilder
		 */
		public StringBuilder nextStringBuilder() {
			StringBuilder sb = new StringBuilder();
			byte b = read();
			while (isDelimiter(b)) b = read();
			while (!isDelimiter(b)) {
				sb.appendCodePoint(b);
				b = read();
			}
			return sb;
		}

		/**
		 * 次の1行を読み込みます。（改行文字は読み飛ばされます）
		 *
		 * @return 読み込んだ String
		 */
		public String nextLine() {
			StringBuilder sb = new StringBuilder();
			int b = read();
			while (b != 0 && b != '\r' && b != '\n') {
				sb.appendCodePoint(b);
				b = read();
			}
			return sb.toString();
		}

		/**
		 * 次のトークンを BigInteger として読み込みます。
		 *
		 * @return 読み込んだ BigInteger
		 */
		public BigInteger nextBigInteger() {
			return new BigInteger(next());
		}

		/**
		 * 次のトークンを BigDecimal として読み込みます。
		 *
		 * @return 読み込んだ BigDecimal
		 */
		public BigDecimal nextBigDecimal() {
			return new BigDecimal(next());
		}

		// -------------------- プライベートヘルパーメソッド --------------------

		/**
		 * 指定した文字コードが空白文字（' '、'\n'、'\r'）かどうか判定します。
		 *
		 * @param c 判定対象の文字コード
		 * @return 空白文字の場合 true、それ以外の場合 false
		 */
		private boolean isDelimiter(int c) {
			return ' ' == c || '\n' == c || '\r' == c;
		}

//  --------------------- 一次元配列入力メソッド ---------------------

		/**
		 * 指定された長さの int 配列を読み込みます。
		 *
		 * @param n 配列の長さ
		 * @return 読み込んだ int 配列
		 */
		public int[] nextInt(final int n) {
			final int[] a = new int[n];
			setAll(a, i -> nextInt());
			return a;
		}

		/**
		 * 指定された長さの long 配列を読み込みます。
		 *
		 * @param n 配列の長さ
		 * @return 読み込んだ long 配列
		 */
		public long[] nextLong(final int n) {
			final long[] a = new long[n];
			setAll(a, i -> nextLong());
			return a;
		}

		/**
		 * 指定された長さの double 配列を読み込みます。
		 *
		 * @param n 配列の長さ
		 * @return 読み込んだ double 配列
		 */
		public double[] nextDouble(final int n) {
			final double[] a = new double[n];
			setAll(a, i -> nextDouble());
			return a;
		}

		/**
		 * 次の文字列を char 配列として読み込みます。
		 *
		 * @return 読み込んだ char 配列
		 */
		public char[] nextChars() {
			return next().toCharArray();
		}

		/**
		 * 指定された長さの char 配列を読み込みます。
		 *
		 * @param n 配列の長さ
		 * @return 読み込んだ char 配列
		 */
		public char[] nextChars(final int n) {
			final char[] c = new char[n];
			for (int i = 0; i < n; i++)
				c[i] = nextChar();
			return c;
		}

		/**
		 * 指定された長さの String 配列を読み込みます。
		 *
		 * @param n 配列の長さ
		 * @return 読み込んだ String 配列
		 */
		public String[] nextStrings(final int n) {
			final String[] s = new String[n];
			setAll(s, x -> next());
			return s;
		}

//  --------------------- 通常の2次元配列入力メソッド ---------------------

		/**
		 * 指定された行数・列数の int の2次元配列を読み込みます。
		 *
		 * @param h 行数
		 * @param w 列数
		 * @return 読み込んだ2次元 int 配列
		 */
		public int[][] nextIntMat(final int h, final int w) {
			final int[][] a = new int[h][w];
			for (int i = 0; i < h; i++)
				setAll(a[i], x -> nextInt());
			return a;
		}

		/**
		 * 指定された行数・列数の long の2次元配列を読み込みます。
		 *
		 * @param h 行数
		 * @param w 列数
		 * @return 読み込んだ2次元 long 配列
		 */
		public long[][] nextLongMat(final int h, final int w) {
			final long[][] a = new long[h][w];
			for (int i = 0; i < h; i++)
				setAll(a[i], x -> nextLong());
			return a;
		}

		/**
		 * 指定された行数・列数の double の2次元配列を読み込みます。
		 *
		 * @param h 行数
		 * @param w 列数
		 * @return 読み込んだ2次元double 配列
		 */
		public double[][] nextDoubleMat(final int h, final int w) {
			final double[][] a = new double[h][w];
			for (int i = 0; i < h; i++)
				setAll(a[i], x -> nextDouble());
			return a;
		}

		/**
		 * 複数の文字列を2次元の char 配列として読み込みます。
		 *
		 * @param n 行数（文字列の個数）
		 * @return 読み込んだ2次元 char 配列
		 */
		public char[][] nextCharMat(final int n) {
			final char[][] c = new char[n][];
			setAll(c, x -> nextChars());
			return c;
		}

		/**
		 * 指定された行数・列数の char 2次元文字配列を読み込みます。
		 *
		 * @param h 行数
		 * @param w 列数
		 * @return 読み込んだ2次元 char 配列
		 */
		public char[][] nextCharMat(final int h, final int w) {
			final char[][] c = new char[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					c[i][j] = nextChar();
			return c;
		}

		/**
		 * 指定された行数・列数の String の2次元配列を読み込みます。
		 *
		 * @param h 行数
		 * @param w 列数
		 * @return 読み込んだ2次元 String 配列
		 */
		public String[][] nextStringMat(final int h, final int w) {
			final String[][] s = new String[h][w];
			for (int i = 0; i < h; i++)
				setAll(s[i], x -> next());
			return s;
		}

//  --------------------- 3次元配列入力メソッド ---------------------

		/**
		 * 指定されたサイズの3次元 int 配列を読み込みます。
		 *
		 * @param x サイズX
		 * @param y サイズY
		 * @param z サイズZ
		 * @return 読み込んだ3次元 int 配列
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
		 * 指定されたサイズの3次元 long 配列を読み込みます。
		 *
		 * @param x サイズX
		 * @param y サイズY
		 * @param z サイズZ
		 * @return 読み込んだ3次元 long 配列
		 */
		public long[][][] nextLong3D(final int x, final int y, final int z) {
			final long[][][] a = new long[x][y][z];
			for (int i = 0; i < x; i++)
				for (int j = 0; j < y; j++)
					for (int k = 0; k < z; k++)
						a[i][j][k] = nextLong();
			return a;
		}

//  --------------------- ソート済み配列入力メソッド ---------------------

		/**
		 * 指定された長さの int 配列を読み込み、ソートして返します。
		 *
		 * @param n 配列の長さ
		 * @return ソートされた int 配列
		 */
		public int[] nextSortedInt(final int n) {
			final int[] a = nextInt(n);
			sort(a);
			return a;
		}

		/**
		 * 指定された長さの long 配列を読み込み、ソートして返します。
		 *
		 * @param n 配列の長さ
		 * @return ソートされた long 配列
		 */
		public long[] nextSortedLong(final int n) {
			final long[] a = nextLong(n);
			sort(a);
			return a;
		}

		/**
		 * 指定された長さの double 配列を読み込み、ソートして返します。
		 *
		 * @param n 配列の長さ
		 * @return ソートされた double 配列
		 */
		public double[] nextSortedDouble(final int n) {
			final double[] a = nextDouble(n);
			sort(a);
			return a;
		}

		/**
		 * 次の文字列を char 配列として読み込み、ソートして返します。
		 *
		 * @return ソートされた char 配列
		 */
		public char[] nextSortedChars() {
			final char[] c = nextChars();
			sort(c);
			return c;
		}

		/**
		 * 指定された長さの char 配列を読み込み、ソートして返します。
		 *
		 * @param n 配列の長さ
		 * @return ソートされた char 配列
		 */
		public char[] nextSortedChars(final int n) {
			final char[] c = nextChars(n);
			sort(c);
			return c;
		}

		/**
		 * 指定された長さの String 配列を読み込み、ソートして返します。
		 *
		 * @param n 配列の長さ
		 * @return ソートされた String 配列
		 */
		public String[] nextSortedStrings(final int n) {
			final String[] s = nextStrings(n);
			sort(s);
			return s;
		}

//  --------------------- 累積和配列入力メソッド ---------------------

		/**
		 * 整数の累積和配列を読み込みます。
		 *
		 * @param n 配列の長さ
		 * @return 累積和配列(int[])
		 */
		public int[] nextIntPrefixSum(final int n) {
			final int[] prefixSum = new int[n];
			setAll(prefixSum, i -> i > 0 ? nextInt() + prefixSum[i - 1] : nextInt());
			return prefixSum;
		}

		/**
		 * 長整数の累積和配列を読み込みます。
		 *
		 * @param n 配列の長さ
		 * @return 累積和配列(long[])
		 */
		public long[] nextLongPrefixSum(final int n) {
			final long[] prefixSum = new long[n];
			setAll(prefixSum, i -> i > 0 ? nextLong() + prefixSum[i - 1] : nextLong());
			return prefixSum;
		}

		/**
		 * 整数の2次元累積和配列を読み込みます。<br>
		 * 戻り値の配列サイズは (h+1) x (w+1) となります。
		 *
		 * @param h 行数
		 * @param w 列数
		 * @return 2次元累積和配列(int[][])
		 */
		public int[][] nextIntPrefixSum(final int h, final int w) {
			final int[][] prefixSum = new int[h + 1][w + 1];
			for (int i = 1; i <= h; i++) {
				int j = i;
				setAll(prefixSum[i], k -> k > 0 ? nextInt() + prefixSum[j - 1][k] + prefixSum[j][k - 1] - prefixSum[j - 1][k - 1] : 0);
			}
			return prefixSum;
		}

		/**
		 * 長整数の2次元累積和配列を読み込みます。<br>
		 * 戻り値の配列サイズは (rows+1) x (cols+1) となります。
		 *
		 * @param h 行数
		 * @param w 列数
		 * @return 2次元累積和配列(long[][])
		 */
		public long[][] nextLongPrefixSum(final int h, final int w) {
			final long[][] prefixSum = new long[h + 1][w + 1];
			for (int i = 1; i <= h; i++) {
				int j = i;
				setAll(prefixSum[i], k -> k > 0 ? nextLong() + prefixSum[j - 1][k] + prefixSum[j][k - 1] - prefixSum[j - 1][k - 1] : 0);
			}
			return prefixSum;
		}

		/**
		 * 整数の3次元累積和配列を読み込みます。<br>
		 * 戻り値の配列サイズは (x+1) x (y+1) x (z+1) となります。
		 *
		 * @param x サイズ X
		 * @param y サイズ Y
		 * @param z サイズ Z
		 * @return 3次元累積和配列（int[][][]）
		 */
		public int[][][] nextIntPrefixSum(final int x, final int y, final int z) {
			final int[][][] e = new int[x + 1][y + 1][z + 1];
			for (int a = 1; a <= x; a++)
				for (int b = 1; b <= y; b++) {
					int A = a, B = b;
					setAll(e[A][B], c -> c > 0 ? nextInt() + e[A - 1][B][c] + e[A][B - 1][c] + e[A][B][c - 1]
							- e[A - 1][B - 1][c] - e[A - 1][B][c - 1] - e[A][B - 1][c - 1] + e[A - 1][B - 1][c - 1] : 0);
				}
			return e;
		}

		/**
		 * 長整数の3次元累積和配列を読み込みます。<br>
		 * 戻り値の配列サイズは (x+1) x (y+1) x (z+1) となります。
		 *
		 * @param x サイズ X
		 * @param y サイズ Y
		 * @param z サイズ Z
		 * @return 3次元累積和配列（long[][][]）
		 */
		public long[][][] nextLongPrefixSum(final int x, final int y, final int z) {
			final long[][][] e = new long[x + 1][y + 1][z + 1];
			for (int a = 1; a <= x; a++)
				for (int b = 1; b <= y; b++) {
					int A = a, B = b;
					setAll(e[A][B], c -> c > 0 ? nextLong() + e[A - 1][B][c] + e[A][B - 1][c] + e[A][B][c - 1]
							- e[A - 1][B - 1][c] - e[A - 1][B][c - 1] - e[A][B - 1][c - 1] + e[A - 1][B - 1][c - 1] : 0);
				}
			return e;
		}

//  --------------------- 逆写像配列入力メソッド ---------------------

		/**
		 * 入力値が1-indexedの整数に対する逆写像を生成します。<br>
		 * 例：入力が「3 1 2」の場合、返される配列は {1, 2, 0} となります。
		 *
		 * @param n 配列の長さ
		 * @return 各入力値に対して、入力された順序（0-indexed）を格納した逆写像
		 */
		public int[] nextIntInverseMapping(final int n) {
			final int[] a = new int[n];
			for (int i = 0; i < n; i++)
				a[nextInt() - 1] = i;
			return a;
		}

//  --------------------- Collection<Integer>入力メソッド ---------------------

		/**
		 * 整数を読み込み、指定したコレクションに格納して返します。
		 *
		 * @param <T>      コレクションの型
		 * @param n        要素数
		 * @param supplier コレクションのインスタンスを生成するサプライヤ
		 * @return 読み込んだ整数のコレクション
		 */
		private <T extends Collection<Integer>> T nextIntCollection(int n, Supplier<T> supplier) {
			T c = supplier.get();
			while (n-- > 0) {
				c.add(nextInt());
			}
			return c;
		}

		/**
		 * 指定された長さの整数 ArrayList を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ ArrayList&lt;Integer&gt;
		 */
		public ArrayList<Integer> nextIntAL(int n) {
			return nextIntCollection(n, () -> new ArrayList<>(n));
		}

		/**
		 * 指定された長さの整数 HashSet を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ HashSet&lt;Integer&gt;
		 */
		public HashSet<Integer> nextIntHS(int n) {
			return nextIntCollection(n, () -> new HashSet<>(n));
		}

		/**
		 * 指定された長さの整数 TreeSet を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ TreeSet&lt;Integer&gt;
		 */
		public TreeSet<Integer> nextIntTS(int n) {
			return nextIntCollection(n, TreeSet::new);
		}

//  --------------------- Collection<Long>入力メソッド ---------------------

		/**
		 * 長整数を読み込み、指定したコレクションに格納して返します。
		 *
		 * @param <T>      コレクションの型
		 * @param n        要素数
		 * @param supplier コレクションのインスタンスを生成するサプライヤ
		 * @return 読み込んだ長整数のコレクション
		 */
		private <T extends Collection<Long>> T nextLongCollection(int n, Supplier<T> supplier) {
			T c = supplier.get();
			while (n-- > 0) {
				c.add(nextLong());
			}
			return c;
		}

		/**
		 * 指定された長さの長整数 ArrayList を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ ArrayList&lt;Long&gt;
		 */
		public ArrayList<Long> nextLongAL(int n) {
			return nextLongCollection(n, () -> new ArrayList<>(n));
		}

		/**
		 * 指定された長さの長整数 HashSet を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ HashSet&lt;Long&gt;
		 */
		public HashSet<Long> nextLongHS(int n) {
			return nextLongCollection(n, () -> new HashSet<>(n));
		}

		/**
		 * 指定された長さの長整数 TreeSet を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ TreeSet&lt;Long&gt;
		 */
		public TreeSet<Long> nextLongTS(int n) {
			return nextLongCollection(n, TreeSet::new);
		}

//  --------------------- Collection<Character>入力メソッド ---------------------

		/**
		 * 文字を読み込み、指定したコレクションに格納して返します。
		 *
		 * @param <T>      コレクションの型
		 * @param n        要素数
		 * @param supplier コレクションのインスタンスを生成するサプライヤ
		 * @return 読み込んだ文字のコレクション
		 */
		private <T extends Collection<Character>> T nextCharacterCollection(int n, Supplier<T> supplier) {
			T c = supplier.get();
			while (n-- > 0) {
				c.add(nextChar());
			}
			return c;
		}

		/**
		 * 指定された長さの文字 ArrayList を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ ArrayList&lt;Character&gt;
		 */
		public ArrayList<Character> nextCharacterAL(int n) {
			return nextCharacterCollection(n, () -> new ArrayList<>(n));
		}

		/**
		 * 指定された長さの文字 HashSet を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ HashSet&lt;Character&gt;
		 */
		public HashSet<Character> nextCharacterHS(int n) {
			return nextCharacterCollection(n, () -> new HashSet<>(n));
		}

		/**
		 * 指定された長さの文字 TreeSet を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ TreeSet&lt;Character&gt;
		 */
		public TreeSet<Character> nextCharacterTS(int n) {
			return nextCharacterCollection(n, TreeSet::new);
		}

//  --------------------- Collection<String>入力メソッド ---------------------

		/**
		 * 文字列を読み込み、指定したコレクションに格納して返します。
		 *
		 * @param <T>      コレクションの型
		 * @param n        要素数
		 * @param supplier コレクションのインスタンスを生成するサプライヤ
		 * @return 読み込んだ文字列のコレクション
		 */
		private <T extends Collection<String>> T nextStringCollection(int n, Supplier<T> supplier) {
			T c = supplier.get();
			while (n-- > 0) {
				c.add(next());
			}
			return c;
		}

		/**
		 * 指定された長さの文字列 ArrayList を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ ArrayList&lt;String&gt;
		 */
		public ArrayList<String> nextStringAL(int n) {
			return nextStringCollection(n, () -> new ArrayList<>(n));
		}

		/**
		 * 指定された長さの文字列 HashSet を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ HashSet&lt;String&gt;
		 */
		public HashSet<String> nextStringHS(int n) {
			return nextStringCollection(n, () -> new HashSet<>(n));
		}

		/**
		 * 指定された長さの文字列 TreeSet を読み込みます。
		 *
		 * @param n 要素数
		 * @return 読み込んだ TreeSet&lt;String&gt;
		 */
		public TreeSet<String> nextStringTS(int n) {
			return nextStringCollection(n, TreeSet::new);
		}

// -------------------- Multiset (Map) 入力メソッド --------------------


		/**
		 * 整数の出現回数をカウントしたマルチセットを読み込みます。<br>
		 * キーが整数、値が出現回数となるマップを返します。
		 *
		 * @param <T>      マップの型
		 * @param n        要素数
		 * @param supplier マップのインスタンスを生成するサプライヤ
		 * @return 整数のマルチセット（マップ）
		 */
		private <T extends Map<Integer, Integer>> T nextIntMultiset(int n, Supplier<T> supplier) {
			T c = supplier.get();
			while (n-- > 0) {
				int a = nextInt();
				c.put(a, c.getOrDefault(a, 0) + 1);
			}
			return c;
		}

		/**
		 * 整数のマルチセットを HashMap で読み込みます。
		 *
		 * @param n 要素数
		 * @return 整数のマルチセット（HashMap）
		 */
		public HashMap<Integer, Integer> nextIntMultisetHM(int n) {
			return nextIntMultiset(n, () -> new HashMap<>(n));
		}

		/**
		 * 整数のマルチセットを TreeMap で読み込みます。
		 *
		 * @param n 要素数
		 * @return 整数のマルチセット（TreeMap）
		 */
		public TreeMap<Integer, Integer> nextIntMultisetTM(int n) {
			return nextIntMultiset(n, TreeMap::new);
		}

		/**
		 * 長整数の出現回数をカウントしたマルチセットを読み込みます。<br>
		 * キーが長整数、値が出現回数となるマップを返します。
		 *
		 * @param <T>      マップの型
		 * @param n        要素数
		 * @param supplier マップのインスタンスを生成するサプライヤ
		 * @return 長整数のマルチセット（マップ）
		 */
		private <T extends Map<Long, Integer>> T nextLongMultiset(int n, Supplier<T> supplier) {
			T c = supplier.get();
			while (n-- > 0) {
				long a = nextLong();
				c.put(a, c.getOrDefault(a, 0) + 1);
			}
			return c;
		}

		/**
		 * 長整数のマルチセットを HashMap で読み込みます。
		 *
		 * @param n 要素数
		 * @return 長整数のマルチセット（HashMap）
		 */
		public HashMap<Long, Integer> nextLongMultisetHM(int n) {
			return nextLongMultiset(n, () -> new HashMap<>(n));
		}

		/**
		 * 長整数のマルチセットを TreeMap で読み込みます。
		 *
		 * @param n 要素数
		 * @return 長整数のマルチセット（TreeMap）
		 */
		public TreeMap<Long, Integer> nextLongMultisetTM(int n) {
			return nextLongMultiset(n, TreeMap::new);
		}

		/**
		 * 文字の出現回数をカウントしたマルチセットを読み込みます。<br>
		 * キーが文字、値が出現回数となるマップを返します。
		 *
		 * @param <T>      マップの型
		 * @param n        要素数
		 * @param supplier マップのインスタンスを生成するサプライヤ
		 * @return 文字のマルチセット（マップ）
		 */
		private <T extends Map<Character, Integer>> T nextCharMultiset(int n, Supplier<T> supplier) {
			T c = supplier.get();
			while (n-- > 0) {
				char a = nextChar();
				c.put(a, c.getOrDefault(a, 0) + 1);
			}
			return c;
		}

		/**
		 * 文字のマルチセットを HashMap で読み込みます。
		 *
		 * @param n 要素数
		 * @return 文字のマルチセット（HashMap）
		 */
		public HashMap<Character, Integer> nextCharMultisetHM(int n) {
			return nextCharMultiset(n, () -> new HashMap<>(n));
		}

		/**
		 * 文字のマルチセットを TreeMap で読み込みます。
		 *
		 * @param n 要素数
		 * @return 文字のマルチセット（TreeMap）
		 */
		public TreeMap<Character, Integer> nextCharMultisetTM(int n) {
			return nextCharMultiset(n, TreeMap::new);
		}

		/**
		 * 文字列の出現回数をカウントしたマルチセットを読み込みます。<br>
		 * キーが文字列、値が出現回数となるマップを返します。
		 *
		 * @param <T>      マップの型
		 * @param n        要素数
		 * @param supplier マップのインスタンスを生成するサプライヤ
		 * @return 文字列のマルチセット（マップ）
		 */
		private <T extends Map<String, Integer>> T nextStringMultiset(int n, Supplier<T> supplier) {
			T c = supplier.get();
			while (n-- > 0) {
				String a = next();
				c.put(a, c.getOrDefault(a, 0) + 1);
			}
			return c;
		}

		/**
		 * 文字列のマルチセットを HashMap で読み込みます。
		 *
		 * @param n 要素数
		 * @return 文字列のマルチセット（HashMap）
		 */
		public HashMap<String, Integer> nextStringMultisetHM(int n) {
			return nextStringMultiset(n, () -> new HashMap<>(n));
		}

		/**
		 * 文字列のマルチセットを TreeMap で読み込みます。
		 *
		 * @param n 要素数
		 * @return 文字列のマルチセット（TreeMap）
		 */
		public TreeMap<String, Integer> nextStringMultisetTM(int n) {
			return nextStringMultiset(n, TreeMap::new);
		}
	}
}