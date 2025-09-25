import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

/**
 * long型に特化したリングバッファ。
 * <p>
 * 固定長のリングバッファ（サーキュラーバッファ）を実装します。競技プログラミングでの利用を想定し、
 * 内部容量を自動的に2のべき乗に正規化することで、剰余演算の代わりに高速なビット演算を利用します。
 */
@SuppressWarnings("unused")
public final class LongRingBuffer implements Iterable<Long>, Cloneable {
	private final int capacity; // バッファの最大サイズ
	private long[] buf; // データを格納する配列
	private int head, size; // データの先頭を表すインデックスとデータの要素数

	/**
	 * 指定された推奨容量でLongRingBufferを構築します。
	 *
	 * @param capacity 推奨されるバッファ容量。内部で2のべき乗に正規化されます。正の値を指定してください。
	 */
	public LongRingBuffer(final int capacity) {
		this.capacity = 1 << (32 - Integer.numberOfLeadingZeros(capacity - 1));
		this.buf = new long[this.capacity];
		this.head = size = 0;
	}

	/**
	 * バッファの末尾に要素を追加します。
	 *
	 * @param e 追加する要素
	 * @return 要素が追加された場合は {@code true}、バッファが満杯で追加できなかった場合は {@code false}
	 */
	public boolean addLast(final long e) {
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
	public boolean addFirst(final long e) {
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
	public long get(int index) {
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
	public long pollLast() {
		if (isEmpty()) throw new RingBufferIndexException("Buffer is empty.");
		return buf[physicalIndex(--size)];
	}

	/**
	 * 先頭の要素を取得して削除します。
	 *
	 * @return 先頭の要素
	 * @throws RingBufferIndexException バッファが空の場合
	 */
	public long pollFirst() {
		if (isEmpty()) throw new RingBufferIndexException("Buffer is empty.");
		long first = buf[head];
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
	public long peekLast() {
		if (isEmpty()) throw new RingBufferIndexException("Buffer is empty.");
		return buf[physicalIndex(size - 1)];
	}

	/**
	 * 先頭の要素を取得します。要素は削除されません。
	 *
	 * @return 先頭の要素
	 * @throws RingBufferIndexException バッファが空の場合
	 */
	public long peekFirst() {
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
	public LongRingBuffer set(int index, final long e) {
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
	public LongRingBuffer setLength(int newLen) {
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
	public boolean contains(final long e) {
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
	public LongRingBuffer setAll(final IntToLongFunction generator) {
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
	public LongRingBuffer fill(final long e) {
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
	public LongRingBuffer clear() {
		head = size = 0;
		return this;
	}

	/**
	 * このRingBufferインスタンスのクローンを作成します。 クローンされたインスタンスは元のインスタンスの独立したコピーです。
	 *
	 * @return クローンされたRingBufferインスタンス
	 */
	public LongRingBuffer clone() {
		try {
			LongRingBuffer rb = (LongRingBuffer) super.clone();
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
		if (!(o instanceof LongRingBuffer other)) return false;
		return size == other.size && IntStream.range(0, size)
				.allMatch(i -> buf[physicalIndex(i)] == other.buf[other.physicalIndex(i)]);
	}

	/**
	 * バッファ内の要素をスペースで区切った文字列として返します。
	 *
	 * @return バッファ内の要素を表す文字列。バッファが空の場合は空文字列
	 */
	public String toString() {
		return stream().mapToObj(Long::toString).collect(Collectors.joining(" "));
	}

	/**
	 * このRingBufferの論理状態に基づくハッシュコードを返します。 論理状態とは、バッファ内の現在有効な要素（headからsize分）の順序を指します。
	 *
	 * @return 論理状態に基づいたハッシュコード
	 */
	public int hashCode() {
		if (isEmpty()) return 0;
		int result = 1;
		for (long element : this) result = 31 * result + Long.hashCode(element);
		return result;
	}

	/**
	 * バッファの論理状態（headからsize個の要素）を含む新しいLongStreamを返します。
	 *
	 * @return 現在のバッファ内容を含むLongStream
	 */
	public LongStream stream() {
		int characteristics = Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED;
		return StreamSupport.longStream(Spliterators.spliterator(iterator(), size, characteristics), false);
	}

	/**
	 * バッファの論理状態（headからsize個の要素）を含む新しい並列LongStreamを返します。
	 *
	 * @return 現在のバッファ内容を含む並列LongStream
	 */
	public LongStream parallelStream() {
		return stream().parallel();
	}

	/**
	 * バッファの論理状態（headからsize個の要素）を含む新しいlong配列を返します。
	 *
	 * @return 現在のバッファ内容を含む配列
	 */
	public long[] toArray() {
		return stream().toArray();
	}

	/**
	 * 指定された配列にバッファの論理状態をコピーして返します。 渡された配列のサイズが足りない場合は、新しい配列が生成されます。
	 *
	 * @param data コピー先の配列
	 * @return バッファ内容がコピーされた配列
	 */
	public long[] toArray(final long[] data) {
		if (data.length < size) return toArray();
		for (int i = 0; i < size; i++) data[i] = buf[physicalIndex(i)];
		return data;
	}

	/**
	 * バッファの論理状態（headからsize個の要素）を含むリストを返します。
	 *
	 * @return バッファ内の要素のリスト
	 */
	public List<Long> toList() {
		return stream().boxed().toList();
	}

	/**
	 * このRingBufferの論理状態（headからsize個の要素）を走査するIteratorを返します。
	 *
	 * @return バッファ内の要素を順に返すIterator
	 */
	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int index = 0;

			public boolean hasNext() {
				return index < size;
			}

			public long nextLong() {
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
