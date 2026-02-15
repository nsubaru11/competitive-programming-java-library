import java.util.*;

import static java.lang.Math.*;

/**
 * 競技プログラミング向け優先度キュー（ジェネリック版）
 */
@SuppressWarnings({"unchecked", "unused"})
public final class PriorityQueue<T extends Comparable<T>> implements Iterable<T> {
	// -------------- フィールド --------------
	private static final int DEFAULT_INITIAL_CAPACITY = 1024;
	private final Comparator<? super T> comparator;
	private T[] buf;
	private int size, capacity, unsortedCount;

	// -------------- コンストラクタ --------------

	/**
	 * コンストラクタ（デフォルト容量1024、最小値優先）
	 */
	public PriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, Comparator.naturalOrder(), false);
	}

	/**
	 * コンストラクタ（最小値優先）
	 *
	 * @param capacity 初期容量
	 */
	public PriorityQueue(int capacity) {
		this(capacity, Comparator.naturalOrder(), false);
	}

	/**
	 * コンストラクタ（デフォルト容量1024）
	 *
	 * @param comparator 比較関数
	 */
	public PriorityQueue(Comparator<T> comparator) {
		this(DEFAULT_INITIAL_CAPACITY, comparator, false);
	}

	/**
	 * コンストラクタ（デフォルト容量1024）
	 *
	 * @param isDescendingOrder true の場合は最大値優先（降順）、false の場合は最小値優先（昇順）
	 */
	public PriorityQueue(boolean isDescendingOrder) {
		this(DEFAULT_INITIAL_CAPACITY, Comparator.naturalOrder(), isDescendingOrder);
	}

	/**
	 * コンストラクタ
	 *
	 * @param capacity   初期容量
	 * @param comparator 比較関数
	 */
	public PriorityQueue(int capacity, Comparator<T> comparator) {
		this(capacity, comparator, false);
	}

	/**
	 * コンストラクタ（デフォルト容量1024）
	 *
	 * @param comparator        比較関数
	 * @param isDescendingOrder true の場合は最大値優先（降順）、false の場合は最小値優先（昇順）
	 */
	public PriorityQueue(Comparator<T> comparator, boolean isDescendingOrder) {
		this(DEFAULT_INITIAL_CAPACITY, comparator, isDescendingOrder);
	}

	/**
	 * コンストラクタ
	 *
	 * @param capacity          初期容量
	 * @param isDescendingOrder true の場合は最大値優先（降順）、false の場合は最小値優先（昇順）
	 */
	public PriorityQueue(int capacity, boolean isDescendingOrder) {
		this(capacity, Comparator.naturalOrder(), isDescendingOrder);
	}

	/**
	 * コンストラクタ
	 *
	 * @param capacity          初期容量
	 * @param comparator        比較関数
	 * @param isDescendingOrder true の場合は最大値優先（降順）、false の場合は最小値優先（昇順）
	 */
	public PriorityQueue(int capacity, Comparator<T> comparator, boolean isDescendingOrder) {
		this.capacity = max(capacity, DEFAULT_INITIAL_CAPACITY);
		this.comparator = isDescendingOrder ? comparator.reversed() : comparator;
		buf = (T[]) new Comparable<?>[this.capacity];
		size = 0;
		unsortedCount = 0;
	}

	// -------------- 公開メソッド --------------

	/**
	 * 要素を追加する
	 *
	 * @param v 追加する要素
	 */
	public void push(T v) {
		if (size == capacity) buf = Arrays.copyOf(buf, capacity <<= 1);
		buf[size++] = v;
		unsortedCount++;
	}

	/**
	 * 全ての要素を追加する
	 *
	 * @param elements 追加する要素の配列
	 */
	public void addAll(final T[] elements) {
		final int n = elements.length;
		if (size + n > capacity) {
			while (size + n > capacity) capacity <<= 1;
			buf = Arrays.copyOf(buf, capacity);
		}
		System.arraycopy(elements, 0, buf, size, n);
		size += n;
		unsortedCount += n;
	}

	/**
	 * 全ての要素を追加する
	 *
	 * @param elements 追加する要素のイテラブル
	 */
	public void addAll(final Iterable<T> elements) {
		if (elements instanceof final Collection<T> c) {
			final int n = c.size();
			if (size + n > capacity) {
				while (size + n > capacity) capacity <<= 1;
				buf = Arrays.copyOf(buf, capacity);
			}
			for (final T e : elements) buf[size++] = e;
			unsortedCount += n;
		} else {
			for (final T e : elements) push(e);
		}
	}

	/**
	 * ヒープの先頭要素を取得
	 *
	 * @return ヒープの先頭要素（昇順時は最小、降順時は最大）
	 * @throws NoSuchElementException ヒープが空の場合
	 */
	public T peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return buf[0];
	}

	/**
	 * ヒープの先頭要素を削除して返す
	 *
	 * @return 削除された要素（昇順時は最小、降順時は最大）
	 */
	public T poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		T res = buf[0];
		if (--size > 0) siftDown(buf[size], 0);
		return res;
	}

	/**
	 * ヒープの先頭要素を置き換える
	 *
	 * @param v 置き換える要素
	 * @return 置き換えられた要素（昇順時は最小、降順時は最大）
	 */
	public T replaceTop(final T v) {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		T res = buf[0];
		buf[0] = v;
		siftDown(buf[0], 0);
		return res;
	}

	/**
	 * 要素数を取得する
	 *
	 * @return 要素数
	 */
	public int size() {
		return size;
	}

	/**
	 * ヒープをクリアする
	 */
	public void clear() {
		size = 0;
		unsortedCount = 0;
	}

	/**
	 * ヒープが空かどうかを判定
	 *
	 * @return 空の場合はtrue
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 要素を順序付けていないイテレータを取得する
	 *
	 * @return 順序付けていないイテレータ
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[i++];
			}
		};
	}

	// -------------- ヒープ構築（遅延評価） --------------

	/**
	 * 遅延評価された未ソート要素をヒープ化し、ヒーププロパティを復元する
	 * <p>
	 * このメソッドは、未ソート要素が存在する場合に最適なアルゴリズムを自動選択して実行します
	 * <p><b>分岐点の決定：</b>
	 * 両アルゴリズムの最大比較回数を計算し、コストが小さい方を実行する
	 * (heapifyCost < incrementalCost なら heapify を選択)
	 */
	private void ensureHeapProperty() {
		final int log2N = 31 - Integer.numberOfLeadingZeros(size);
		final int heapifyCost = size * 2 - 2 * log2N;
		final int incrementalCost = unsortedCount <= 100 ? getIncrementalCostStrict() : getIncrementalCostApprox();
		if (heapifyCost < incrementalCost) {
			heapify();
		} else {
			for (int i = size - unsortedCount; i < size; i++) siftUp(buf[i], i);
		}
		unsortedCount = 0;
	}

	/**
	 * インクリメンタル構築の最大比較回数を厳密に計算する
	 *
	 * @return 最大比較回数の合計
	 */
	private int getIncrementalCostStrict() {
		int totalCost = 0;
		final int sortedSize = size - unsortedCount;
		for (int i = 1; i <= unsortedCount; i++) {
			final int currentHeapSize = sortedSize + i;
			final int depth = 31 - Integer.numberOfLeadingZeros(currentHeapSize);
			totalCost += depth;
		}
		return totalCost;
	}

	/**
	 * インクリメンタル構築の最大比較回数を高速に近似計算する
	 * <p>コスト ≈ k * floor(log₂(平均ヒープサイズ))
	 *
	 * @return 最大比較回数の近似値
	 */
	private int getIncrementalCostApprox() {
		final int sortedSize = size - unsortedCount;
		final int avgHeapSize = sortedSize + (unsortedCount >> 1);
		if (avgHeapSize == 0) return 0;
		final int depthOfAvgSize = 31 - Integer.numberOfLeadingZeros(avgHeapSize);
		return unsortedCount * depthOfAvgSize;
	}

	/**
	 * Bottom-up heapify (Floyd's algorithm)
	 */
	private void heapify() {
		for (int i = (size >> 1) - 1; i >= 0; i--) siftDown(buf[i], i);
	}

	// -------------- ヒープ操作（基本） --------------

	/**
	 * siftUp操作 - O(log N)
	 * <p>
	 * 新要素を親と比較しながら上方向に移動（親 ≤ 子）
	 *
	 * @param v 移動させる要素
	 * @param i 要素の現在位置
	 */
	private void siftUp(final T v, int i) {
		while (i > 0) {
			final int j = (i - 1) >> 1;
			if (comparator.compare(v, buf[j]) >= 0) break;
			buf[i] = buf[j];
			i = j;
		}
		buf[i] = v;
	}

	/**
	 * siftDown操作 - O(log N)
	 * <p>
	 * 末尾要素を子と比較しながら下方向に移動（親 ≤ 子）
	 * 2つの子のうち小さい方と比較
	 *
	 * @param v 移動させる要素
	 * @param i 要素の現在位置
	 */
	private void siftDown(final T v, int i) {
		final int half = size >> 1;
		while (i < half) {
			int child = (i << 1) + 1;
			child += child + 1 < size && comparator.compare(buf[child], buf[child + 1]) > 0 ? 1 : 0;
			if (comparator.compare(v, buf[child]) <= 0) break;
			buf[i] = buf[child];
			i = child;
		}
		buf[i] = v;
	}
}
