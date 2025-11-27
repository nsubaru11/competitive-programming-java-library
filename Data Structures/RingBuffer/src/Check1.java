import sun.misc.*;

import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

public final class Check1 {

	// region initialization
	// ------------------------ 定数 ------------------------
	private static final boolean DEBUG;
	private static final int MOD;
	private static final int[] dij;
	private static final FastScanner sc;
	private static final FastPrinter out;

	static {
		DEBUG = true;
		MOD = 998244353;
		// MOD = 1_000_000_007;
		dij = new int[]{-1, -1, -1, 0, 0, 1, 1, 1, -1, 0, 1, -1, 1, -1, 0, 1};
		sc = new FastScanner(System.in);
		out = new FastPrinter(System.out);
	}
	// endregion

	// ------------------------ メインロジック ------------------------
	private static void solve() {
		int q = sc.nextInt();
		IntRingBuffer a = new IntRingBuffer(500000);
		while (q-- > 0) {
			int t = sc.nextInt();
			if (t == 0) {
				a.addFirst(sc.nextInt());
			} else if (t == 1) {
				a.addLast(sc.nextInt());
			} else if (t == 2) {
				a.pollFirst();
			} else if (t == 3) {
				a.pollLast();
			} else if (t == 4) {
				System.out.println(a.get(sc.nextInt()));
			}
		}
	}

	// region main() and debug() methods
	// ------------------------ main() 関数 ------------------------
	public static void main(final String[] args) {
		try {
			solve();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
			out.close();
		}
	}

	// ------------------------ デバッグ用 ------------------------
	private static void debug(final Object... args) {
		if (DEBUG) {
			out.flush();
			System.err.println(deepToString(args));
		}
	}
	// endregion

	/**
	 * int型に特化したリングバッファ。
	 * <p>
	 * 固定長のリングバッファ（サーキュラーバッファ）を実装します。競技プログラミングでの利用を想定し、
	 * 内部容量を自動的に2のべき乗に正規化することで、剰余演算の代わりに高速なビット演算を利用します。
	 */
	@SuppressWarnings("unused")
	private static final class IntRingBuffer implements Iterable<Integer>, Cloneable {
		private final int capacity; // バッファの最大サイズ
		private int[] buf; // データを格納する配列
		private int head, size; // データの先頭を表すインデックスとデータの要素数

		/**
		 * 指定された推奨容量でIntegerRingBufferを構築します。
		 *
		 * @param capacity 推奨されるバッファ容量。内部で2のべき乗に正規化されます。正の値を指定してください。
		 */
		public IntRingBuffer(final int capacity) {
			this.capacity = 1 << (32 - Integer.numberOfLeadingZeros(capacity - 1));
			this.buf = new int[this.capacity];
			this.head = size = 0;
		}

		/**
		 * バッファの末尾に要素を追加します。
		 *
		 * @param e 追加する要素
		 * @return 要素が追加された場合は {@code true}、バッファが満杯で追加できなかった場合は {@code false}
		 */
		public boolean addLast(final int e) {
			if (isFull()) return false;
			buf[physicalIndex(size++)] = e;
			return true;
		}

		/**
		 * バッファの先頭に要素を追加します。
		 *
		 * @param e 追加する要素
		 * @return 要素が追加された場合は {@code true}、バッファが満杯で追加できなかった場合は {@code false}
		 */
		public boolean addFirst(final int e) {
			if (isFull()) return false;
			head = physicalIndex(capacity - 1);
			buf[head] = e;
			size++;
			return true;
		}

		/**
		 * 指定したインデックスの要素を取得します。負のインデックスは、末尾からの相対位置として解釈されます (例: -1は最後の要素)。
		 *
		 * @param index 取得する要素のインデックス。{@code -size() <= index < size()} を満たす必要があります。
		 * @return 指定されたインデックスの要素
		 * @throws RingBufferIndexException バッファが空の場合、またはインデックスが範囲外の場合
		 */
		public int get(int index) {
			if (isEmpty()) throw new RingBufferIndexException("Buffer is empty.");
			if (index >= size || index < -size) throw new RingBufferIndexException(index, -size, size - 1);
			if (index < 0) index += size;
			return buf[physicalIndex(index)];
		}

		/**
		 * 末尾の要素を取得して削除します。
		 *
		 * @return 末尾の要素
		 * @throws RingBufferIndexException バッファが空の場合
		 */
		public int pollLast() {
			if (isEmpty()) throw new RingBufferIndexException("Buffer is empty.");
			return buf[physicalIndex(--size)];
		}

		/**
		 * 先頭の要素を取得して削除します。
		 *
		 * @return 先頭の要素
		 * @throws RingBufferIndexException バッファが空の場合
		 */
		public int pollFirst() {
			if (isEmpty()) throw new RingBufferIndexException("Buffer is empty.");
			int first = buf[head];
			size--;
			head = physicalIndex(1);
			return first;
		}

		/**
		 * 末尾の要素を取得します。要素は削除されません。
		 *
		 * @return 末尾の要素
		 * @throws RingBufferIndexException バッファが空の場合
		 */
		public int peekLast() {
			if (isEmpty()) throw new RingBufferIndexException("Buffer is empty.");
			return buf[physicalIndex(size - 1)];
		}

		/**
		 * 先頭の要素を取得します。要素は削除されません。
		 *
		 * @return 先頭の要素
		 * @throws RingBufferIndexException バッファが空の場合
		 */
		public int peekFirst() {
			if (isEmpty()) throw new RingBufferIndexException("Buffer is empty.");
			return buf[head];
		}

		/**
		 * 指定されたインデックスの要素を上書きします。負のインデックスは、末尾からの相対位置として解釈されます (例: -1は最後の要素)。
		 *
		 * @param index 上書きする要素のインデックス。{@code -size() <= index < size()} を満たす必要があります。
		 * @param e     新しい要素
		 * @return このインスタンス自体
		 * @throws RingBufferIndexException バッファが空の場合、またはインデックスが範囲外の場合
		 */
		public IntRingBuffer set(int index, final int e) {
			if (isEmpty()) throw new RingBufferIndexException("Buffer is empty.");
			if (index < -size || size <= index) throw new RingBufferIndexException(index, -size, size - 1);
			if (index < 0) index += size;
			buf[physicalIndex(index)] = e;
			return this;
		}

		/**
		 * 現在のバッファの長さを変更します。
		 * <p>
		 * 長さが増加する場合、新しい位置には{@code 0}が挿入されます。
		 * 長さが減少する場合、末尾の要素が切り捨てられます。
		 *
		 * @param newLen 新しい長さ。{@code 0 <= newLen <= capacity()} を満たす必要があります。
		 * @return このインスタンス自体
		 * @throws RingBufferIndexException 指定された長さが {@code 0} 未満、または {@code capacity()} を超える場合
		 */
		public IntRingBuffer setLength(int newLen) {
			if (newLen > capacity || newLen < 0) throw new RingBufferIndexException(newLen, capacity);
			if (size < newLen) {
				while (size < newLen) addLast(0);
			} else {
				size = newLen;
			}
			return this;
		}

		/**
		 * 指定した要素がバッファに含まれているかを調べます。
		 *
		 * @param e 検索対象の要素
		 * @return 指定した要素が含まれている場合はtrue、含まれていない場合はfalse
		 */
		public boolean contains(final int e) {
			return stream().anyMatch(i -> i == e);
		}

		/**
		 * バッファが空かどうかを返します。
		 *
		 * @return 現在の要素数が0の場合はtrue、それ以外の場合はfalse
		 */
		public boolean isEmpty() {
			return size == 0;
		}

		/**
		 * バッファが満杯かどうかを返します。
		 *
		 * @return 現在の要素数がバッファの最大サイズに達している場合はtrue、それ以外の場合はfalse
		 */
		public boolean isFull() {
			return size == capacity;
		}

		/**
		 * 現在のバッファ内の要素数を返します。
		 *
		 * @return 現在の要素数
		 */
		public int size() {
			return size;
		}

		/**
		 * バッファの最大容量を返します。
		 *
		 * @return バッファの最大容量
		 */
		public int capacity() {
			return capacity;
		}

		/**
		 * バッファにこれ以上追加できる残りの容量を返します。
		 *
		 * @return {@code capacity() - size()}
		 */
		public int remainingCapacity() {
			return capacity - size;
		}

		/**
		 * 指定された関数を使用してバッファ内の全要素を初期化します。
		 *
		 * @param generator 要素を生成する関数。インデックス（0からsize-1まで）を引数として受け取り、初期化する値を返します。
		 * @return このインスタンス自体を返します。
		 */
		public IntRingBuffer setAll(final IntUnaryOperator generator) {
			head = 0;
			size = capacity;
			Arrays.setAll(buf, generator);
			return this;
		}

		/**
		 * 指定された値でバッファ全体を初期化します。
		 *
		 * @param e バッファの全ての要素を埋める値
		 * @return このインスタンス自体を返します。
		 */
		public IntRingBuffer fill(final int e) {
			head = 0;
			size = capacity;
			Arrays.fill(buf, e);
			return this;
		}

		/**
		 * バッファ内の全ての要素を削除し、空の状態にします。
		 *
		 * @return このインスタンス
		 */
		public IntRingBuffer clear() {
			head = size = 0;
			return this;
		}

		/**
		 * このRingBufferインスタンスのクローンを作成します。 クローンされたインスタンスは元のインスタンスの独立したコピーです。
		 *
		 * @return クローンされたRingBufferインスタンス
		 */
		public IntRingBuffer clone() {
			try {
				IntRingBuffer rb = (IntRingBuffer) super.clone();
				rb.buf = Arrays.copyOf(this.buf, this.capacity);
				return rb;
			} catch (CloneNotSupportedException e) {
				throw new AssertionError();
			}
		}

		/**
		 * 指定されたオブジェクトがこのRingBufferと等しいかを判定します。
		 *
		 * @param o 比較対象のオブジェクト
		 * @return trueの場合、指定されたオブジェクトがRingBufferのインスタンスであり、
		 * バッファ内の要素と順序が完全に一致していることを示します。
		 */
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof IntRingBuffer other)) return false;
			return size == other.size && IntStream.range(0, size)
					.allMatch(i -> buf[physicalIndex(i)] == other.buf[other.physicalIndex(i)]);
		}

		/**
		 * バッファ内の要素をスペースで区切った文字列として返します。
		 *
		 * @return バッファ内の要素を表す文字列。バッファが空の場合は空文字列
		 */
		public String toString() {
			return stream().mapToObj(Integer::toString).collect(Collectors.joining(" "));
		}

		/**
		 * このRingBufferの論理状態に基づくハッシュコードを返します。 論理状態とは、バッファ内の現在有効な要素（headからsize分）の順序を指します。
		 *
		 * @return 論理状態に基づいたハッシュコード
		 */
		public int hashCode() {
			if (isEmpty()) return 0;
			int result = 1;
			for (int element : this) result = 31 * result + element;
			return result;
		}

		/**
		 * バッファの論理状態（headからsize個の要素）を含む新しいIntStreamを返します。
		 *
		 * @return 現在のバッファ内容を含むIntStream
		 */
		public IntStream stream() {
			int characteristics = Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED;
			return StreamSupport.intStream(Spliterators.spliterator(iterator(), size, characteristics), false);
		}

		/**
		 * バッファの論理状態（headからsize個の要素）を含む新しい並列IntStreamを返します。
		 *
		 * @return 現在のバッファ内容を含む並列IntStream
		 */
		public IntStream parallelStream() {
			return stream().parallel();
		}

		/**
		 * バッファの論理状態（headからsize個の要素）を含む新しいint配列を返します。
		 *
		 * @return 現在のバッファ内容を含む配列
		 */
		public int[] toArray() {
			return stream().toArray();
		}

		/**
		 * 指定された配列にバッファの論理状態をコピーして返します。 渡された配列のサイズが足りない場合は、新しい配列が生成されます。
		 *
		 * @param data コピー先の配列
		 * @return バッファ内容がコピーされた配列
		 */
		public int[] toArray(final int[] data) {
			if (data.length < size) return toArray();
			for (int i = 0; i < size; i++) data[i] = buf[physicalIndex(i)];
			return data;
		}

		/**
		 * バッファの論理状態（headからsize個の要素）を含むリストを返します。
		 *
		 * @return バッファ内の要素のリスト
		 */
		public List<Integer> toList() {
			return stream().boxed().toList();
		}

		/**
		 * このRingBufferの論理状態（headからsize個の要素）を走査するIteratorを返します。
		 *
		 * @return バッファ内の要素を順に返すIterator
		 */
		public PrimitiveIterator.OfInt iterator() {
			return new PrimitiveIterator.OfInt() {
				private int index = 0;

				public boolean hasNext() {
					return index < size;
				}

				public int nextInt() {
					if (!hasNext()) throw new NoSuchElementException();
					return buf[physicalIndex(index++)];
				}
			};
		}

		/**
		 * 論理インデックスから実インデックスを取得します。
		 *
		 * @param logicalIndex 論理インデックス
		 * @return 実インデックス
		 */
		private int physicalIndex(int logicalIndex) {
			return (head + logicalIndex) & (capacity - 1);
		}

		/**
		 * RingBufferに関連する例外クラス。 主にインデックスや長さの不正使用時にスローされます。
		 */
		private static final class RingBufferIndexException extends RuntimeException {

			@Serial
			private static final long serialVersionUID = -7627324668888567160L;

			/**
			 * 指定されたエラーメッセージを使用して例外を初期化します。
			 *
			 * @param message エラーメッセージ
			 */
			public RingBufferIndexException(String message) {
				super(message);
			}

			/**
			 * 指定されたインデックスが有効範囲外である場合の例外を初期化します。
			 *
			 * @param index 不正なインデックス
			 * @param from  有効範囲の下限（含む）
			 * @param to    有効範囲の上限（含む）
			 */
			public RingBufferIndexException(int index, int from, int to) {
				super(String.format("Invalid index %d. Valid range is [%d, %d].", index, from, to));
			}

			/**
			 * 指定された長さが有効範囲外である場合の例外を初期化します。
			 *
			 * @param len    不正な長さ
			 * @param maxLen 許容される最大長
			 */
			public RingBufferIndexException(int len, int maxLen) {
				super(String.format("Invalid length %d. Max allowed: %d.", len, maxLen));
			}
		}
	}

	// ------------------------ 高速入出力クラス ------------------------
	@SuppressWarnings("unused")
	private static final class FastScanner implements AutoCloseable {
		private static final int DEFAULT_BUFFER_SIZE = 65536;
		private final InputStream in;
		private final byte[] buffer;
		private int pos = 0, bufferLength = 0;

		public FastScanner() {
			this(System.in, DEFAULT_BUFFER_SIZE);
		}

		public FastScanner(final InputStream in) {
			this(in, DEFAULT_BUFFER_SIZE);
		}

		public FastScanner(final int bufferSize) {
			this(System.in, bufferSize);
		}

		public FastScanner(final InputStream in, final int bufferSize) {
			this.in = in;
			this.buffer = new byte[bufferSize];
		}

		private int skipSpaces() {
			int b = read();
			while (b <= 32) b = read();
			return b;
		}

		@Override
		public void close() {
			try {
				if (in != System.in) in.close();
				pos = 0;
				bufferLength = 0;
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int read() {
			if (pos >= bufferLength) {
				try {
					bufferLength = in.read(buffer, pos = 0, buffer.length);
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
				if (bufferLength <= 0) throw new RuntimeException(new EOFException());
			}
			return buffer[pos++] & 0xFF;
		}

		public int peek() {
			try {
				int b = skipSpaces();
				pos--;
				return b;
			} catch (final RuntimeException e) {
				return 0;
			}
		}

		public boolean hasNext() {
			return peek() != 0;
		}

		public int nextInt() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = read();
			}
			int result = 0;
			do {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			} while (b >= '0' && b <= '9');
			return negative ? -result : result;
		}

		public long nextLong() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = read();
			}
			long result = 0;
			do {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			} while (b >= '0' && b <= '9');
			return negative ? -result : result;
		}

		public double nextDouble() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = read();
			}
			long intPart = 0;
			do {
				intPart = (intPart << 3) + (intPart << 1) + (b & 15);
				b = read();
			} while (b >= '0' && b <= '9');
			double result = intPart;
			if (b == '.') {
				b = read();
				double scale = 0.1;
				do {
					result += (b & 15) * scale;
					scale *= 0.1;
					b = read();
				} while (b >= '0' && b <= '9');
			}
			return negative ? -result : result;
		}

		public char nextChar() {
			int b = skipSpaces();
			return (char) b;
		}

		public String next() {
			return nextStringBuilder().toString();
		}

		public StringBuilder nextStringBuilder() {
			final StringBuilder sb = new StringBuilder();
			int b = skipSpaces();
			do {
				sb.append((char) b);
				b = read();
			} while (b > 32);
			return sb;
		}

		public String nextLine() {
			final StringBuilder sb = new StringBuilder();
			int b = read();
			while (b != 0 && b != '\n' && b != '\r') {
				sb.append((char) b);
				b = read();
			}
			if (b == '\r') {
				int c = read();
				if (c != '\n') pos--;
			}
			return sb.toString();
		}

		public BigInteger nextBigInteger() {
			return new BigInteger(next());
		}

		public BigDecimal nextBigDecimal() {
			return new BigDecimal(next());
		}

		public int[] nextInt(final int n) {
			final int[] a = new int[n];
			for (int i = 0; i < n; i++) a[i] = nextInt();
			return a;
		}

		public long[] nextLong(final int n) {
			final long[] a = new long[n];
			for (int i = 0; i < n; i++) a[i] = nextLong();
			return a;
		}

		public double[] nextDouble(final int n) {
			final double[] a = new double[n];
			for (int i = 0; i < n; i++) a[i] = nextDouble();
			return a;
		}

		public char[] nextChars() {
			return next().toCharArray();
		}

		public char[] nextChars(final int n) {
			final char[] c = new char[n];
			for (int i = 0; i < n; i++) c[i] = nextChar();
			return c;
		}

		public String[] nextStrings(final int n) {
			final String[] s = new String[n];
			for (int i = 0; i < n; i++) s[i] = next();
			return s;
		}

		public int[][] nextIntMat(final int h, final int w) {
			final int[][] a = new int[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					a[i][j] = nextInt();
			return a;
		}

		public long[][] nextLongMat(final int h, final int w) {
			final long[][] a = new long[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					a[i][j] = nextLong();
			return a;
		}

		public double[][] nextDoubleMat(final int h, final int w) {
			final double[][] a = new double[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					a[i][j] = nextDouble();
			return a;
		}

		public char[][] nextCharMat(final int n) {
			final char[][] c = new char[n][];
			for (int i = 0; i < n; i++) c[i] = nextChars();
			return c;
		}

		public char[][] nextCharMat(final int h, final int w) {
			final char[][] c = new char[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					c[i][j] = nextChar();
			return c;
		}

		public String[][] nextStringMat(final int h, final int w) {
			final String[][] s = new String[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					s[i][j] = next();
			return s;
		}

		public int[][][] nextInt3D(final int x, final int y, final int z) {
			final int[][][] a = new int[x][y][z];
			for (int i = 0; i < x; i++)
				for (int j = 0; j < y; j++)
					for (int k = 0; k < z; k++)
						a[i][j][k] = nextInt();
			return a;
		}

		public long[][][] nextLong3D(final int x, final int y, final int z) {
			final long[][][] a = new long[x][y][z];
			for (int i = 0; i < x; i++)
				for (int j = 0; j < y; j++)
					for (int k = 0; k < z; k++)
						a[i][j][k] = nextLong();
			return a;
		}

		public int[] nextSortedInt(final int n) {
			final int[] a = nextInt(n);
			sort(a);
			return a;
		}

		public long[] nextSortedLong(final int n) {
			final long[] a = nextLong(n);
			sort(a);
			return a;
		}

		public double[] nextSortedDouble(final int n) {
			final double[] a = nextDouble(n);
			sort(a);
			return a;
		}

		public char[] nextSortedChars() {
			final char[] c = nextChars();
			sort(c);
			return c;
		}

		public char[] nextSortedChars(final int n) {
			final char[] c = nextChars(n);
			sort(c);
			return c;
		}

		public String[] nextSortedStrings(final int n) {
			final String[] s = nextStrings(n);
			sort(s);
			return s;
		}

		public int[] nextIntPrefixSum(final int n) {
			final int[] ps = new int[n];
			ps[0] = nextInt();
			for (int i = 1; i < n; i++) ps[i] = nextInt() + ps[i - 1];
			return ps;
		}

		public long[] nextLongPrefixSum(final int n) {
			final long[] ps = new long[n];
			ps[0] = nextLong();
			for (int i = 1; i < n; i++) ps[i] = nextLong() + ps[i - 1];
			return ps;
		}

		public int[][] nextIntPrefixSum(final int h, final int w) {
			final int[][] ps = new int[h + 1][w + 1];
			for (int i = 1; i <= h; i++)
				for (int j = 1; j <= w; j++)
					ps[i][j] = nextInt() + ps[i - 1][j] + ps[i][j - 1] - ps[i - 1][j - 1];
			return ps;
		}

		public long[][] nextLongPrefixSum(final int h, final int w) {
			final long[][] ps = new long[h + 1][w + 1];
			for (int i = 1; i <= h; i++)
				for (int j = 1; j <= w; j++)
					ps[i][j] = nextLong() + ps[i - 1][j] + ps[i][j - 1] - ps[i - 1][j - 1];
			return ps;
		}

		public int[][][] nextIntPrefixSum(final int x, final int y, final int z) {
			final int[][][] ps = new int[x + 1][y + 1][z + 1];
			for (int a = 1; a <= x; a++)
				for (int b = 1; b <= y; b++)
					for (int c = 1; c <= z; c++)
						ps[a][b][c] = nextInt() + ps[a - 1][b][c] + ps[a][b - 1][c] + ps[a][b][c - 1] - ps[a - 1][b - 1][c]
								- ps[a - 1][b][c - 1] - ps[a][b - 1][c - 1] + ps[a - 1][b - 1][c - 1];
			return ps;
		}

		public long[][][] nextLongPrefixSum(final int x, final int y, final int z) {
			final long[][][] ps = new long[x + 1][y + 1][z + 1];
			for (int a = 1; a <= x; a++)
				for (int b = 1; b <= y; b++)
					for (int c = 1; c <= z; c++)
						ps[a][b][c] = nextLong() + ps[a - 1][b][c] + ps[a][b - 1][c] + ps[a][b][c - 1] - ps[a - 1][b - 1][c]
								- ps[a - 1][b][c - 1] - ps[a][b - 1][c - 1] + ps[a - 1][b - 1][c - 1];
			return ps;
		}

		public int[] nextIntInverseMapping(final int n) {
			final int[] inv = new int[n];
			for (int i = 0; i < n; i++) inv[nextInt() - 1] = i;
			return inv;
		}

		public ArrayList<Integer> nextIntAL(final int n) {
			return nextCollection(n, this::nextInt, () -> new ArrayList<>(n));
		}

		public HashSet<Integer> nextIntHS(final int n) {
			return nextCollection(n, this::nextInt, () -> new HashSet<>(n));
		}

		public TreeSet<Integer> nextIntTS(final int n) {
			return nextCollection(n, this::nextInt, TreeSet::new);
		}

		public ArrayList<Long> nextLongAL(final int n) {
			return nextCollection(n, this::nextLong, () -> new ArrayList<>(n));
		}

		public HashSet<Long> nextLongHS(final int n) {
			return nextCollection(n, this::nextLong, () -> new HashSet<>(n));
		}

		public TreeSet<Long> nextLongTS(final int n) {
			return nextCollection(n, this::nextLong, TreeSet::new);
		}

		public ArrayList<Character> nextCharacterAL(final int n) {
			return nextCollection(n, this::nextChar, () -> new ArrayList<>(n));
		}

		public HashSet<Character> nextCharacterHS(final int n) {
			return nextCollection(n, this::nextChar, () -> new HashSet<>(n));
		}

		public TreeSet<Character> nextCharacterTS(final int n) {
			return nextCollection(n, this::nextChar, TreeSet::new);
		}

		public ArrayList<String> nextStringAL(final int n) {
			return nextCollection(n, this::next, () -> new ArrayList<>(n));
		}

		public HashSet<String> nextStringHS(final int n) {
			return nextCollection(n, this::next, () -> new HashSet<>(n));
		}

		public TreeSet<String> nextStringTS(final int n) {
			return nextCollection(n, this::next, TreeSet::new);
		}

		private <S, T extends Collection<S>> T nextCollection(int n, final Supplier<S> input, final Supplier<T> collection) {
			final T t = collection.get();
			while (n-- > 0) t.add(input.get());
			return t;
		}

		public HashMap<Integer, Integer> nextIntMultisetHM(final int n) {
			return nextMultiset(n, this::nextInt, () -> new HashMap<>(n));
		}

		public TreeMap<Integer, Integer> nextIntMultisetTM(final int n) {
			return nextMultiset(n, this::nextInt, TreeMap::new);
		}

		public HashMap<Long, Integer> nextLongMultisetHM(final int n) {
			return nextMultiset(n, this::nextLong, () -> new HashMap<>(n));
		}

		public TreeMap<Long, Integer> nextLongMultisetTM(final int n) {
			return nextMultiset(n, this::nextLong, TreeMap::new);
		}

		public HashMap<Character, Integer> nextCharMultisetHM(final int n) {
			return nextMultiset(n, this::nextChar, () -> new HashMap<>(n));
		}

		public TreeMap<Character, Integer> nextCharMultisetTM(final int n) {
			return nextMultiset(n, this::nextChar, TreeMap::new);
		}

		public HashMap<String, Integer> nextStringMultisetHM(final int n) {
			return nextMultiset(n, this::next, () -> new HashMap<>(n));
		}

		public TreeMap<String, Integer> nextStringMultisetTM(final int n) {
			return nextMultiset(n, this::next, TreeMap::new);
		}

		private <S, T extends Map<S, Integer>> T nextMultiset(int n, final Supplier<S> input, final Supplier<T> map) {
			final T multiSet = map.get();
			while (n-- > 0) {
				final S i = input.get();
				multiSet.put(i, multiSet.getOrDefault(i, 0) + 1);
			}
			return multiSet;
		}

		public int[] nextIntMultiset(final int n, final int m) {
			final int[] multiset = new int[m];
			for (int i = 0; i < n; i++) multiset[nextInt() - 1]++;
			return multiset;
		}

		public int[] nextUpperCharMultiset(final int n) {
			return nextCharMultiset(n, 'A', 'Z');
		}

		public int[] nextLowerCharMultiset(final int n) {
			return nextCharMultiset(n, 'a', 'z');
		}

		public int[] nextCharMultiset(int n, final char l, final char r) {
			final int[] multiset = new int[r - l + 1];
			while (n-- > 0) {
				final int c = nextChar() - l;
				multiset[c]++;
			}
			return multiset;
		}
	}

	@SuppressWarnings("unused")
	private static final class FastPrinter implements AutoCloseable {
		private static final int MAX_INT_DIGITS = 11;
		private static final int MAX_LONG_DIGITS = 20;
		private static final int DEFAULT_BUFFER_SIZE = 65536;
		private static final byte LINE = '\n';
		private static final byte SPACE = ' ';
		private static final byte HYPHEN = '-';
		private static final byte PERIOD = '.';
		private static final byte ZERO = '0';
		private static final byte[] TRUE_BYTES = {'Y', 'e', 's'};
		private static final byte[] FALSE_BYTES = {'N', 'o'};
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
		private static final long[] POW10 = {
				1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000,
				1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L,
				10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L,
				10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L
		};
		private static final Unsafe UNSAFE;
		private static final long STRING_VALUE_OFFSET;
		private static final long ABSTRACT_STRING_BUILDER_VALUE_OFFSET;


		static {
			try {
				final Field f = Unsafe.class.getDeclaredField("theUnsafe");
				f.setAccessible(true);
				UNSAFE = (Unsafe) f.get(null);
				STRING_VALUE_OFFSET = UNSAFE.objectFieldOffset(String.class.getDeclaredField("value"));
				final Class<?> asbClass = Class.forName("java.lang.AbstractStringBuilder");
				ABSTRACT_STRING_BUILDER_VALUE_OFFSET = UNSAFE.objectFieldOffset(asbClass.getDeclaredField("value"));
			} catch (final Exception e) {
				throw new RuntimeException("Unsafe initialization failed. Check Java version and environment.", e);
			}
		}

		private final OutputStream out;
		private final boolean autoFlush;
		private byte[] buffer;
		private int pos = 0;

		public FastPrinter() {
			this(System.out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter(final OutputStream out) {
			this(out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter(final int bufferSize) {
			this(System.out, bufferSize, false);
		}

		public FastPrinter(final boolean autoFlush) {
			this(System.out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter(final OutputStream out, final boolean autoFlush) {
			this(out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter(final int bufferSize, final boolean autoFlush) {
			this(System.out, bufferSize, autoFlush);
		}

		public FastPrinter(final OutputStream out, final int bufferSize) {
			this(out, bufferSize, false);
		}

		public FastPrinter(final OutputStream out, final int bufferSize, final boolean autoFlush) {
			this.out = out;
			this.buffer = new byte[max(64, roundUpToPowerOfTwo(bufferSize))];
			this.autoFlush = autoFlush;
		}

		private static int countDigits(final int i) {
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

		private static int countDigits(final long l) {
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

		@Override
		public void close() {
			try {
				flush();
				if (out != System.out) out.close();
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void flush() {
			if (pos == 0) return;
			try {
				out.write(buffer, 0, pos);
				pos = 0;
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		public FastPrinter println() {
			ensureCapacity(1);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final boolean b) {
			write(b);
			ensureCapacity(1);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final byte b) {
			ensureCapacity(2);
			buffer[pos++] = b;
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final char c) {
			ensureCapacity(2);
			buffer[pos++] = (byte) c;
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final int i) {
			ensureCapacity(MAX_INT_DIGITS + 1);
			write(i);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final long l) {
			ensureCapacity(MAX_LONG_DIGITS + 1);
			write(l);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final double d) {
			return println(Double.toString(d));
		}

		public FastPrinter println(final BigInteger bi) {
			return println(bi.toString());
		}

		public FastPrinter println(final BigDecimal bd) {
			return println(bd.toString());
		}

		public FastPrinter println(final String s) {
			ensureCapacity(s.length() + 1);
			write(s);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final StringBuilder s) {
			ensureCapacity(s.length() + 1);
			write(s);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final Object o) {
			if (o == null) return println();
			else if (o instanceof Boolean b) {
				return println(b.booleanValue());
			} else if (o instanceof Byte b) {
				return println(b.byteValue());
			} else if (o instanceof Character c) {
				return println(c.charValue());
			} else if (o instanceof Integer i) {
				return println(i.intValue());
			} else if (o instanceof Long l) {
				return println(l.longValue());
			} else if (o instanceof Double d) {
				return println(d.toString());
			} else if (o instanceof BigInteger bi) {
				return println(bi.toString());
			} else if (o instanceof BigDecimal bd) {
				return println(bd.toString());
			} else if (o instanceof String s) {
				return println(s);
			} else if (o instanceof boolean[] arr) {
				return println(arr);
			} else if (o instanceof char[] arr) {
				return println(arr);
			} else if (o instanceof int[] arr) {
				return println(arr);
			} else if (o instanceof long[] arr) {
				return println(arr);
			} else if (o instanceof double[] arr) {
				return println(arr);
			} else if (o instanceof String[] arr) {
				return println(arr);
			} else if (o instanceof Object[] arr) {
				return println(arr);
			} else {
				return println(o.toString());
			}
		}

		public FastPrinter print(final boolean b) {
			write(b);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final byte b) {
			ensureCapacity(1);
			buffer[pos++] = b;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final char c) {
			ensureCapacity(1);
			buffer[pos++] = (byte) c;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final int i) {
			ensureCapacity(MAX_INT_DIGITS);
			write(i);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final long l) {
			ensureCapacity(MAX_LONG_DIGITS);
			write(l);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final double d) {
			return print(Double.toString(d));
		}

		public FastPrinter print(final BigInteger bi) {
			return print(bi.toString());
		}

		public FastPrinter print(final BigDecimal bd) {
			return print(bd.toString());
		}

		public FastPrinter print(final String s) {
			ensureCapacity(s.length());
			write(s);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final StringBuilder s) {
			ensureCapacity(s.length());
			write(s);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final Object o) {
			if (o == null) return this;
			else if (o instanceof Boolean b) {
				return print(b.booleanValue());
			} else if (o instanceof Byte b) {
				return print(b.byteValue());
			} else if (o instanceof Character c) {
				return print(c.charValue());
			} else if (o instanceof Integer i) {
				return print(i.intValue());
			} else if (o instanceof Long l) {
				return print(l.longValue());
			} else if (o instanceof Double d) {
				return print(d.toString());
			} else if (o instanceof BigInteger bi) {
				return print(bi.toString());
			} else if (o instanceof BigDecimal bd) {
				return print(bd.toString());
			} else if (o instanceof String s) {
				return print(s);
			} else if (o instanceof boolean[] arr) {
				return print(arr);
			} else if (o instanceof char[] arr) {
				return print(arr);
			} else if (o instanceof int[] arr) {
				return print(arr);
			} else if (o instanceof long[] arr) {
				return print(arr);
			} else if (o instanceof double[] arr) {
				return print(arr);
			} else if (o instanceof String[] arr) {
				return print(arr);
			} else if (o instanceof Object[] arr) {
				return print(arr);
			} else {
				return print(o.toString());
			}
		}

		public FastPrinter printf(final String format, final Object... args) {
			return print(String.format(format, args));
		}

		public FastPrinter printf(final Locale locale, final String format, final Object... args) {
			return print(String.format(locale, format, args));
		}

		private void ensureCapacity(final int additional) {
			final int required = pos + additional;
			if (required <= buffer.length) return;
			if (required <= 1_000_000_000) {
				buffer = copyOf(buffer, roundUpToPowerOfTwo(required));
			} else {
				flush();
			}
		}

		private void write(final boolean b) {
			final byte[] src = b ? TRUE_BYTES : FALSE_BYTES;
			final int len = src.length;
			ensureCapacity(len);
			System.arraycopy(src, 0, buffer, pos, len);
			pos += len;
		}

		private void write(int i) {
			final byte[] buf = buffer;
			int p = pos;
			if (i >= 0) i = -i;
			else buf[p++] = HYPHEN;
			final int digits = countDigits(i);
			int writePos = p + digits;
			while (i <= -100) {
				final int q = i / 100;
				final int r = (q << 6) + (q << 5) + (q << 2) - i;
				buf[--writePos] = DigitOnes[r];
				buf[--writePos] = DigitTens[r];
				i = q;
			}
			final int r = -i;
			buf[--writePos] = DigitOnes[r];
			if (r >= 10) buf[--writePos] = DigitTens[r];
			pos = p + digits;
		}

		private void write(long l) {
			final byte[] buf = buffer;
			int p = pos;
			if (l >= 0) l = -l;
			else buf[p++] = HYPHEN;
			final int digits = countDigits(l);
			int writePos = p + digits;
			while (l <= -100) {
				final long q = l / 100;
				final int r = (int) ((q << 6) + (q << 5) + (q << 2) - l);
				buf[--writePos] = DigitOnes[r];
				buf[--writePos] = DigitTens[r];
				l = q;
			}
			final int r = (int) -l;
			buf[--writePos] = DigitOnes[r];
			if (r >= 10) buf[--writePos] = DigitTens[r];
			pos = p + digits;
		}

		private void write(final String s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, STRING_VALUE_OFFSET);
			final int len = s.length();
			System.arraycopy(src, 0, buffer, pos, len);
			pos += len;
		}

		private void write(final StringBuilder s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, ABSTRACT_STRING_BUILDER_VALUE_OFFSET);
			final int len = s.length();
			System.arraycopy(src, 0, buffer, pos, len);
			pos += len;
		}

		public FastPrinter println(final int a, final int b) {
			return println(a, b, '\n');
		}

		public FastPrinter println(final int a, final long b) {
			return println(a, b, '\n');
		}

		public FastPrinter println(final long a, final int b) {
			return println(a, b, '\n');
		}

		public FastPrinter println(final long a, final long b) {
			return println(a, b, '\n');
		}

		public FastPrinter println(final long a, final long b, final char delimiter) {
			ensureCapacity((MAX_LONG_DIGITS << 1) + 2);
			write(a);
			buffer[pos++] = (byte) delimiter;
			write(b);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final int a, final int b) {
			return print(a, b, ' ');
		}

		public FastPrinter print(final int a, final long b) {
			return print(a, b, ' ');
		}

		public FastPrinter print(final long a, final int b) {
			return print(a, b, ' ');
		}

		public FastPrinter print(final long a, final long b) {
			return print(a, b, ' ');
		}

		public FastPrinter print(final long a, final long b, final char delimiter) {
			ensureCapacity((MAX_LONG_DIGITS << 1) + 1);
			write(a);
			buffer[pos++] = (byte) delimiter;
			write(b);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final double d, final int n) {
			return print(d, n).println();
		}

		public FastPrinter print(double d, int n) {
			if (n <= 0) return print(round(d));
			if (d < 0) {
				ensureCapacity(1);
				buffer[pos++] = HYPHEN;
				d = -d;
			}
			if (n > 18) n = 18;
			final long intPart = (long) d;
			final long fracPart = (long) ((d - intPart) * POW10[n]);
			print(intPart);
			int leadingZeros = n - countDigits(-fracPart);
			ensureCapacity(leadingZeros + 1);
			buffer[pos++] = PERIOD;
			while (leadingZeros-- > 0) buffer[pos++] = ZERO;
			print(fracPart);
			return this;
		}

		public FastPrinter println(final boolean[] arr) {
			return print(arr, 0, arr.length, '\n').println();
		}

		public FastPrinter println(final char[] arr) {
			return print(arr, 0, arr.length, '\n').println();
		}

		public FastPrinter println(final int[] arr) {
			return print(arr, 0, arr.length, '\n').println();
		}

		public FastPrinter println(final long[] arr) {
			return print(arr, 0, arr.length, '\n').println();
		}

		public FastPrinter println(final double[] arr) {
			return print(arr, 0, arr.length, '\n').println();
		}

		public FastPrinter println(final String[] arr) {
			return print(arr, 0, arr.length, '\n').println();
		}

		public FastPrinter println(final Object... arr) {
			for (final Object o : arr) println(o);
			return this;
		}

		public FastPrinter println(final boolean[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter).println();
		}

		public FastPrinter println(final char[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter).println();
		}

		public FastPrinter println(final int[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter).println();
		}

		public FastPrinter println(final long[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter).println();
		}

		public FastPrinter println(final double[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter).println();
		}

		public FastPrinter println(final String[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter).println();
		}

		public FastPrinter println(final boolean[] arr, final int from, final int to) {
			return print(arr, from, to, '\n').println();
		}

		public FastPrinter println(final char[] arr, final int from, final int to) {
			return print(arr, from, to, '\n').println();
		}

		public FastPrinter println(final int[] arr, final int from, final int to) {
			return print(arr, from, to, '\n').println();
		}

		public FastPrinter println(final long[] arr, final int from, final int to) {
			return print(arr, from, to, '\n').println();
		}

		public FastPrinter println(final double[] arr, final int from, final int to) {
			return print(arr, from, to, '\n').println();
		}

		public FastPrinter println(final String[] arr, final int from, final int to) {
			return print(arr, from, to, '\n').println();
		}

		public FastPrinter println(final boolean[] arr, final int from, final int to, final char delimiter) {
			return print(arr, from, to, delimiter).println();
		}

		public FastPrinter println(final char[] arr, final int from, final int to, final char delimiter) {
			return print(arr, from, to, delimiter).println();
		}

		public FastPrinter println(final int[] arr, final int from, final int to, final char delimiter) {
			return print(arr, from, to, delimiter).println();
		}

		public FastPrinter println(final long[] arr, final int from, final int to, final char delimiter) {
			return print(arr, from, to, delimiter).println();
		}

		public FastPrinter println(final double[] arr, final int from, final int to, final char delimiter) {
			return print(arr, from, to, delimiter).println();
		}

		public FastPrinter println(final String[] arr, final int from, final int to, final char delimiter) {
			return print(arr, from, to, delimiter).println();
		}

		public FastPrinter print(final boolean[] arr) {
			return print(arr, 0, arr.length, ' ');
		}

		public FastPrinter print(final char[] arr) {
			return print(arr, 0, arr.length, ' ');
		}

		public FastPrinter print(final int[] arr) {
			return print(arr, 0, arr.length, ' ');
		}

		public FastPrinter print(final long[] arr) {
			return print(arr, 0, arr.length, ' ');
		}

		public FastPrinter print(final double[] arr) {
			return print(arr, 0, arr.length, ' ');
		}

		public FastPrinter print(final String[] arr) {
			return print(arr, 0, arr.length, ' ');
		}

		public FastPrinter print(final Object... arr) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(arr[i]);
			}
			return this;
		}

		public FastPrinter print(final boolean[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter);
		}

		public FastPrinter print(final char[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter);
		}

		public FastPrinter print(final int[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter);
		}

		public FastPrinter print(final long[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter);
		}

		public FastPrinter print(final double[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter);
		}

		public FastPrinter print(final String[] arr, final char delimiter) {
			return print(arr, 0, arr.length, delimiter);
		}

		public FastPrinter print(final boolean[] arr, final int from, final int to) {
			return print(arr, from, to, ' ');
		}

		public FastPrinter print(final char[] arr, final int from, final int to) {
			return print(arr, from, to, ' ');
		}

		public FastPrinter print(final int[] arr, final int from, final int to) {
			return print(arr, from, to, ' ');
		}

		public FastPrinter print(final long[] arr, final int from, final int to) {
			return print(arr, from, to, ' ');
		}

		public FastPrinter print(final double[] arr, final int from, final int to) {
			return print(arr, from, to, ' ');
		}

		public FastPrinter print(final String[] arr, final int from, final int to) {
			return print(arr, from, to, ' ');
		}

		public FastPrinter print(final boolean[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			print(arr[from]);
			for (int i = from + 1; i < to; i++) {
				ensureCapacity(1);
				buffer[pos++] = (byte) delimiter;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final char[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			ensureCapacity(((to - from) << 1) - 1);
			byte[] buf = buffer;
			int p = pos;
			buf[p++] = (byte) arr[from];
			for (int i = from + 1; i < to; i++) {
				buf[p++] = (byte) delimiter;
				buf[p++] = (byte) arr[i];
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final int[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			final int len = to - from;
			ensureCapacity(len * (MAX_INT_DIGITS + 1));
			write(arr[from]);
			for (int i = from + 1; i < to; i++) {
				buffer[pos++] = (byte) delimiter;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final long[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			final int len = to - from;
			ensureCapacity(len * (MAX_LONG_DIGITS + 1));
			write(arr[from]);
			for (int i = from + 1; i < to; i++) {
				buffer[pos++] = (byte) delimiter;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final double[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			print(arr[from]);
			for (int i = from + 1; i < to; i++) {
				String s = Double.toString(arr[i]);
				ensureCapacity(s.length() + 1);
				buffer[pos++] = (byte) delimiter;
				write(s);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final String[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			print(arr[from]);
			for (int i = from + 1; i < to; i++) {
				ensureCapacity(arr[i].length() + 1);
				buffer[pos++] = (byte) delimiter;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public <T> FastPrinter println(final boolean[] arr, final Function<Boolean, T> function) {
			for (final boolean b : arr) println(function.apply(b));
			return this;
		}

		public <T> FastPrinter println(final char[] arr, final IntFunction<T> function) {
			for (final char c : arr) println(function.apply(c));
			return this;
		}

		public <T> FastPrinter println(final int[] arr, final IntFunction<T> function) {
			for (final int i : arr) println(function.apply(i));
			return this;
		}

		public <T> FastPrinter println(final long[] arr, final LongFunction<T> function) {
			for (final long l : arr) println(function.apply(l));
			return this;
		}

		public <T> FastPrinter println(final double[] arr, final DoubleFunction<T> function) {
			for (final double l : arr) println(function.apply(l));
			return this;
		}

		public <T> FastPrinter println(final String[] arr, final Function<String, T> function) {
			for (final String s : arr) println(function.apply(s));
			return this;
		}

		public <T> FastPrinter print(final boolean[] arr, final Function<Boolean, T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final char[] arr, final IntFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final int[] arr, final IntFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final long[] arr, final LongFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final double[] arr, final DoubleFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final String[] arr, final Function<String, T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public FastPrinter println(final boolean[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final char[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final int[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final long[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final double[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final String[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final Object[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final boolean[][] arr2d, final char delimiter) {
			for (final boolean[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final char[][] arr2d, final char delimiter) {
			for (final char[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final int[][] arr2d, final char delimiter) {
			for (final int[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final long[][] arr2d, final char delimiter) {
			for (final long[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final double[][] arr2d, final char delimiter) {
			for (final double[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final String[][] arr2d, final char delimiter) {
			for (final String[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final Object[][] arr2d, final char delimiter) {
			for (final Object[] arr : arr2d) {
				final int len = arr.length;
				if (len > 0) print(arr[0]);
				for (int i = 1; i < len; i++) {
					ensureCapacity(1);
					buffer[pos++] = (byte) delimiter;
					print(arr[i]);
				}
				println();
			}
			return this;
		}

		public <T> FastPrinter println(final boolean[][] arr2d, final Function<Boolean, T> function) {
			for (final boolean[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final char[][] arr2d, final IntFunction<T> function) {
			for (final char[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final int[][] arr2d, final IntFunction<T> function) {
			for (final int[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final long[][] arr2d, final LongFunction<T> function) {
			for (final long[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final double[][] arr2d, final DoubleFunction<T> function) {
			for (final double[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final String[][] arr2d, final Function<String, T> function) {
			for (final String[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public FastPrinter printChars(final char[] arr) {
			return printChars(arr, 0, arr.length);
		}

		public FastPrinter printChars(final char[] arr, final int from, final int to) {
			final int len = to - from;
			ensureCapacity(len);
			final byte[] buf = buffer;
			int p = pos, i = from;
			final int limit8 = from + (len & ~7);
			while (i < limit8) {
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
			}
			while (i < to) buf[p++] = (byte) arr[i++];
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printChars(final char[] arr, final IntUnaryOperator function) {
			final int len = arr.length;
			ensureCapacity(len);
			final byte[] buf = buffer;
			int p = pos, i = 0;
			final int limit = len & ~7;
			while (i < limit) {
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
			}
			while (i < len) buf[p++] = (byte) function.applyAsInt(arr[i++]);
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printChars(final char[][] arr2d) {
			for (final char[] arr : arr2d) printChars(arr).println();
			return this;
		}

		public FastPrinter printChars(final char[][] arr2d, final IntUnaryOperator function) {
			for (final char[] arr : arr2d) printChars(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final Iterable<T> iter) {
			return print(iter, '\n').println();
		}

		public <T> FastPrinter println(final Iterable<T> iter, final char delimiter) {
			return print(iter, delimiter).println();
		}

		public <T> FastPrinter print(final Iterable<T> iter) {
			return print(iter, ' ');
		}

		public <T> FastPrinter print(final Iterable<T> iter, final char delimiter) {
			final Iterator<T> it = iter.iterator();
			if (it.hasNext()) print(it.next());
			while (it.hasNext()) {
				ensureCapacity(1);
				buffer[pos++] = (byte) delimiter;
				print(it.next());
			}
			return this;
		}

		public <T, U> FastPrinter println(final Iterable<T> iter, final Function<T, U> function) {
			return print(iter, function, '\n').println();
		}

		public <T, U> FastPrinter println(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
			return print(iter, function, delimiter).println();
		}

		public <T, U> FastPrinter print(final Iterable<T> iter, final Function<T, U> function) {
			return print(iter, function, ' ');
		}

		public <T, U> FastPrinter print(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
			final Iterator<T> it = iter.iterator();
			if (it.hasNext()) print(function.apply(it.next()));
			while (it.hasNext()) {
				ensureCapacity(1);
				buffer[pos++] = (byte) delimiter;
				print(function.apply(it.next()));
			}
			return this;
		}

		public FastPrinter printRepeat(final char c, final int cnt) {
			if (cnt <= 0) return this;
			ensureCapacity(cnt);
			final byte[] buf = buffer;
			final byte b = (byte) c;
			int p = pos;
			buf[p++] = b;
			int copied = 1;
			while (copied << 1 <= cnt) {
				System.arraycopy(buf, pos, buf, p, copied);
				p += copied;
				copied <<= 1;
			}
			final int remain = cnt - copied;
			if (remain > 0) {
				System.arraycopy(buf, pos, buf, p, remain);
				p += remain;
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printRepeat(final String s, final int cnt) {
			if (cnt <= 0) return this;
			final int len = s.length();
			if (len == 0) return this;
			final int total = len * cnt;
			ensureCapacity(total);
			final byte[] buf = buffer;
			int p = pos, i = 0;
			final int limit8 = len & ~7;
			while (i < limit8) {
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
			}
			while (i < len) buf[p++] = (byte) s.charAt(i++);
			int copied = 1;
			while (copied << 1 <= cnt) {
				System.arraycopy(buf, pos, buf, p, copied * len);
				p += copied * len;
				copied <<= 1;
			}
			final int remain = cnt - copied;
			if (remain > 0) {
				System.arraycopy(buf, pos, buf, p, remain * len);
				p += remain * len;
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnRepeat(final char c, final int cnt) {
			if (cnt <= 0) return this;
			final int total = cnt << 1;
			ensureCapacity(total);
			final byte[] buf = buffer;
			final byte b = (byte) c;
			int p = pos;
			buf[p++] = b;
			buf[p++] = LINE;
			int copied = 2;
			while (copied << 1 <= total) {
				System.arraycopy(buf, pos, buf, p, copied);
				p += copied;
				copied <<= 1;
			}
			final int remain = total - copied;
			if (remain > 0) {
				System.arraycopy(buf, pos, buf, p, remain);
				p += remain;
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnRepeat(final String s, final int cnt) {
			if (cnt <= 0) return this;
			final int sLen = s.length();
			if (sLen == 0) return this;
			final int unit = sLen + 1;
			final int total = unit * cnt;
			ensureCapacity(total);
			final byte[] buf = buffer;
			int p = pos;
			int i = 0;
			final int limit8 = sLen & ~7;
			while (i < limit8) {
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
			}
			while (i < sLen) buf[p++] = (byte) s.charAt(i++);
			buf[p++] = LINE;
			int copied = 1;
			while (copied << 1 <= cnt) {
				System.arraycopy(buf, pos, buf, p, copied * unit);
				p += copied * unit;
				copied <<= 1;
			}
			final int remain = cnt - copied;
			if (remain > 0) {
				System.arraycopy(buf, pos, buf, p, remain * unit);
				p += remain * unit;
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final boolean[] arr) {
			final int len = arr.length;
			final byte[] buf = buffer;
			for (int i = len - 1; i >= 0; i--) {
				write(arr[i]);
				ensureCapacity(1);
				buf[pos++] = LINE;
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final char[] arr) {
			final int len = arr.length;
			ensureCapacity(len << 1);
			final byte[] buf = buffer;
			int p = pos;
			for (int i = len - 1; i >= 0; i--) {
				buf[p++] = (byte) arr[i];
				buf[p++] = LINE;
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final int[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			ensureCapacity(len * (MAX_INT_DIGITS + 1));
			final byte[] buf = buffer;
			for (int i = len - 1; i >= 0; i--) {
				write(arr[i]);
				buf[pos++] = LINE;
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final long[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			ensureCapacity(len * (MAX_LONG_DIGITS + 1));
			final byte[] buf = buffer;
			for (int i = len - 1; i >= 0; i--) {
				write(arr[i]);
				buf[pos++] = LINE;
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final double[] arr) {
			final int len = arr.length;
			final byte[] buf = buffer;
			for (int i = len - 1; i >= 0; i--) {
				String s = Double.toString(arr[i]);
				ensureCapacity(s.length() + 1);
				write(s);
				buf[pos++] = LINE;
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final String[] arr) {
			final int len = arr.length;
			final byte[] buf = buffer;
			for (int i = len - 1; i >= 0; i--) {
				String s = arr[i];
				ensureCapacity(s.length() + 1);
				write(s);
				buf[pos++] = LINE;
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final Object[] arr) {
			final int len = arr.length;
			for (int i = len - 1; i >= 0; i--) println(arr[i]);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final boolean[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			final byte[] buf = buffer;
			write(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				ensureCapacity(1);
				buf[pos++] = SPACE;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final char[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			ensureCapacity((len << 1) - 1);
			final byte[] buf = buffer;
			int p = pos;
			buf[p++] = (byte) arr[len - 1];
			for (int i = len - 2; i >= 0; i--) {
				buf[p++] = SPACE;
				buf[p++] = (byte) arr[i];
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final int[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			ensureCapacity(len * (MAX_INT_DIGITS + 1) - 1);
			final byte[] buf = buffer;
			write(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				buf[pos++] = SPACE;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final long[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			ensureCapacity(len * (MAX_LONG_DIGITS + 1) - 1);
			final byte[] buf = buffer;
			write(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				buf[pos++] = SPACE;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final double[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			final byte[] buf = buffer;
			print(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				String s = Double.toString(arr[i]);
				ensureCapacity(s.length() + 1);
				buf[pos++] = SPACE;
				write(s);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final String[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			final byte[] buf = buffer;
			ensureCapacity(arr[len - 1].length());
			write(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				ensureCapacity(arr[i].length() + 1);
				buf[pos++] = SPACE;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final Object[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			final byte[] buf = buffer;
			print(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				ensureCapacity(1);
				buf[pos++] = SPACE;
				print(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}
	}
}
