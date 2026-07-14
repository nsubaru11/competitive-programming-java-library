package lib.ds.arrays;

import java.util.*;

/**
 * long型に特化した可変容量の配列deque。
 * <p>
 * 可変容量の配列dequeを実装します。競技プログラミングでの利用を想定し、
 * 内部容量を自動的に2のべき乗に正規化することで、剰余演算の代わりに高速なビット演算を利用します。
 */
@SuppressWarnings("unused")
public final class LongArrayDeque implements LongMutableArray, Cloneable {
	private int capacity;
	private long[] buf;
	private int head, size;

	/**
	 * 指定された初期容量でLongArrayDequeを構築します。
	 *
	 * @param capacity 初期容量。内部で2のべき乗に正規化されます。
	 */
	public LongArrayDeque(final int capacity) {
		this.capacity = 1 << (32 - Integer.numberOfLeadingZeros(capacity - 1));
		this.buf = new long[this.capacity];
		this.head = size = 0;
	}

	/**
	 * バッファの末尾に要素を追加します。
	 *
	 * @param e 追加する要素
	 * @return 常に {@code true}
	 */
	public boolean addLast(final long e) {
		if (isFull()) grow();
		buf[physicalIndex(size++)] = e;
		return true;
	}

	/**
	 * バッファの先頭に要素を追加します。
	 *
	 * @param e 追加する要素
	 * @return 常に {@code true}
	 */
	public boolean addFirst(final long e) {
		if (isFull()) grow();
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
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合
	 */
	public long get(int index) {
		if (index >= size || index < -size) throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
		if (index < 0) index += size;
		return buf[physicalIndex(index)];
	}

	/**
	 * 末尾の要素を取得して削除します。
	 *
	 * @return 末尾の要素
	 * @throws NoSuchElementException dequeが空の場合
	 */
	public long pollLast() {
		if (isEmpty()) throw new NoSuchElementException("Deque is empty.");
		return buf[physicalIndex(--size)];
	}

	/**
	 * 先頭の要素を取得して削除します。
	 *
	 * @return 先頭の要素
	 * @throws NoSuchElementException dequeが空の場合
	 */
	public long pollFirst() {
		if (isEmpty()) throw new NoSuchElementException("Deque is empty.");
		long first = buf[head];
		size--;
		head = physicalIndex(1);
		return first;
	}

	/**
	 * 末尾の要素を取得します。要素は削除されません。
	 *
	 * @return 末尾の要素
	 * @throws NoSuchElementException dequeが空の場合
	 */
	public long peekLast() {
		if (isEmpty()) throw new NoSuchElementException("Deque is empty.");
		return buf[physicalIndex(size - 1)];
	}

	/**
	 * 先頭の要素を取得します。要素は削除されません。
	 *
	 * @return 先頭の要素
	 * @throws NoSuchElementException dequeが空の場合
	 */
	public long peekFirst() {
		if (isEmpty()) throw new NoSuchElementException("Deque is empty.");
		return buf[head];
	}

	/**
	 * 指定されたインデックスの要素を上書きします。負のインデックスは、末尾からの相対位置として解釈されます (例: -1は最後の要素)。
	 *
	 * @param index 上書きする要素のインデックス。{@code -size() <= index < size()} を満たす必要があります。
	 * @param e     新しい要素
	 * @return 設定前の値
	 * @throws IndexOutOfBoundsException インデックスが範囲外の場合
	 */
	public long set(int index, final long e) {
		if (index < -size || size <= index) throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
		if (index < 0) index += size;
		int i = physicalIndex(index);
		long old = buf[i];
		buf[i] = e;
		return old;
	}

	/**
	 * 指定した要素がバッファに含まれているかを調べます。
	 *
	 * @param e 検索対象の要素
	 * @return 指定した要素が含まれている場合はtrue、含まれていない場合はfalse
	 */
	public boolean contains(final long e) {
		for (int i = 0; i < size; i++) if (buf[physicalIndex(i)] == e) return true;
		return false;
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
	 * @return 現在の要素数が内部配列の容量に達している場合はtrue、それ以外の場合はfalse
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
	 * @return 現在の内部配列の容量
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
	 * バッファ内の全ての要素を削除し、空の状態にします。
	 *
	 * @return このインスタンス
	 */
	public LongArrayDeque clear() {
		head = size = 0;
		return this;
	}

	/**
	 * このdequeの独立したコピーを返します。
	 *
	 * @return 複製したdeque
	 */
	public LongArrayDeque clone() {
		try {
			LongArrayDeque rb = (LongArrayDeque) super.clone();
			rb.buf = Arrays.copyOf(this.buf, this.capacity);
			return rb;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	/**
	 * 要素と順序が一致するLongArrayDequeかを判定します。
	 *
	 * @param o 比較対象のオブジェクト
	 * @return 要素と順序が一致する場合はtrue
	 */
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LongArrayDeque other)) return false;
		if (size != other.size) return false;
		for (int i = 0; i < size; i++) {
			if (buf[physicalIndex(i)] != other.buf[other.physicalIndex(i)]) return false;
		}
		return true;
	}

	/**
	 * バッファ内の要素をスペースで区切った文字列として返します。
	 *
	 * @return バッファ内の要素を表す文字列。バッファが空の場合は空文字列
	 */
	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(20 * size);
		sb.append(buf[head]);
		for (int i = 1; i < size; i++) sb.append(' ').append(buf[physicalIndex(i)]);
		return sb.toString();
	}

	/**
	 * 要素と順序に基づくハッシュコードを返します。
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
	 * バッファの論理状態（headからsize個の要素）を含む新しいlong配列を返します。
	 *
	 * @return 現在のバッファ内容を含む配列
	 */
	public long[] toArray() {
		long[] res = new long[size];
		for (int i = 0; i < size; i++) res[i] = buf[physicalIndex(i)];
		return res;
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
	 * 先頭から順に走査するiteratorを返します。
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

	private void grow() {
		long[] newBuf = new long[capacity << 1];
		for (int i = 0; i < size; i++) newBuf[i] = buf[physicalIndex(i)];
		buf = newBuf;
		head = 0;
		capacity <<= 1;
	}

}
