package lib.ds.arrays;

import static java.lang.Math.*;

import java.util.*;

import lib.ds.*;

/**
 * int 型に特化した可変容量の循環 deque。
 * 添字は先頭を0とし、有効範囲は {@code 0 <= i < size()} とする。空の get・remove は例外、OrDefault 系は指定値を返す。
 * equals・hashCode は Object の実装を継承し、内容による比較は行わない。
 */
public final class IntArrayDeque implements IntMutableArray {
	private int capacity;
	private int[] buf;
	private int head, size;

	/**
	 * 空の deque を構築する。初期容量は非負とし、最低1の2のべき乗へ切り上げる。確保可能な容量を指定する。
	 */
	public IntArrayDeque(final int capacity) {
		this.capacity = 1 << (32 - Integer.numberOfLeadingZeros(capacity - 1));
		buf = new int[this.capacity];
	}

	/**
	 * 配列の全要素を同じ順序でコピーして構築する。
	 */
	public IntArrayDeque(final int[] a) {
		this(a.length);
		System.arraycopy(a, 0, buf, 0, a.length);
		size = a.length;
	}

	/**
	 * コレクションの iterator 順に全要素をコピーして構築する。
	 */
	public IntArrayDeque(final IntCollection a) {
		this(a.size());
		size = a.size();
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0; i < size; i++) buf[i] = it.nextInt();
	}

	public int size() {
		return size;
	}

	/**
	 * 指定した論理位置の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public int get(final int i) {
		return buf[physicalIndex(Objects.checkIndex(i, size))];
	}

	/**
	 * 指定した論理位置を v に更新し、更新前の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public int set(final int i, final int v) {
		final int p = physicalIndex(Objects.checkIndex(i, size));
		final int old = buf[p];
		buf[p] = v;
		return old;
	}

	/**
	 * 先頭に v を追加する。満杯の場合は内部容量を2倍に拡張する。
	 */
	public void addFirst(final int v) {
		if (size == capacity) grow();
		head = physicalIndex(capacity - 1);
		buf[head] = v;
		size++;
	}

	/**
	 * 末尾に v を追加する。満杯の場合は内部容量を2倍に拡張する。
	 */
	public void addLast(final int v) {
		if (size == capacity) grow();
		buf[physicalIndex(size++)] = v;
	}

	/**
	 * 先頭の要素を削除して返す。
	 *
	 * @throws NoSuchElementException deque が空の場合
	 */
	public int removeFirst() {
		if (isEmpty()) throw new NoSuchElementException("IntArrayDeque is empty.");
		final int first = buf[head];
		size--;
		head = physicalIndex(1);
		return first;
	}

	/**
	 * 先頭の要素を削除して返す。空の場合は状態を変更せず defaultValue を返す。
	 */
	public int removeFirstOrDefault(final int defaultValue) {
		if (isEmpty()) return defaultValue;
		final int first = buf[head];
		size--;
		head = physicalIndex(1);
		return first;
	}

	/**
	 * 末尾の要素を削除して返す。
	 *
	 * @throws NoSuchElementException deque が空の場合
	 */
	public int removeLast() {
		if (isEmpty()) throw new NoSuchElementException("IntArrayDeque is empty.");
		return buf[physicalIndex(--size)];
	}

	/**
	 * 末尾の要素を削除して返す。空の場合は状態を変更せず defaultValue を返す。
	 */
	public int removeLastOrDefault(final int defaultValue) {
		if (isEmpty()) return defaultValue;
		return buf[physicalIndex(--size)];
	}

	/**
	 * 全要素を v に更新する。要素数と容量を維持し、内部の先頭位置を0へ移す。
	 */
	public void fill(final int v) {
		Arrays.fill(buf, 0, size, v);
		head = 0;
	}

	/**
	 * 全要素を削除する。内部配列の縮小や初期化は行わず、容量を再利用する。
	 */
	public void clear() {
		head = size = 0;
	}

	/**
	 * 先頭から末尾の論理順で独立した配列へコピーする。
	 */
	public int[] toArray() {
		final int[] res = new int[size];
		final int n = min(size, capacity - head);
		System.arraycopy(buf, head, res, 0, n);
		System.arraycopy(buf, 0, res, n, size - n);
		return res;
	}

	/**
	 * 先頭から末尾の論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。
	 */
	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[physicalIndex(i++)];
			}
		};
	}

	/**
	 * 先頭から末尾の要素を半角スペース区切りで返す。要素がなければ空文字列を返す。
	 */
	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		sb.append(buf[head]);
		for (int i = 1; i < size; i++) sb.append(' ').append(buf[physicalIndex(i)]);
		return sb.toString();
	}

	private int physicalIndex(final int i) {
		return (head + i) & (capacity - 1);
	}

	private void grow() {
		final int[] nb = new int[capacity << 1];
		final int n = capacity - head;
		System.arraycopy(buf, head, nb, 0, n);
		System.arraycopy(buf, 0, nb, n, head);
		buf = nb;
		head = 0;
		capacity <<= 1;
	}
}
