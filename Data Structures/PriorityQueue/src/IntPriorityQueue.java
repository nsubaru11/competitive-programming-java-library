import java.util.*;

import static java.lang.Math.*;

/**
 * 競技プログラミング向け優先度キュー（int型特化版）
 */
@SuppressWarnings("unused")
public final class IntPriorityQueue implements Iterable<Integer> {
	// -------------- フィールド --------------
	private static final int DEFAULT_INITIAL_CAPACITY = 1024;
	private final boolean isDescendingOrder;
	private int[] buf;
	private int size, capacity, unsortedCount;

	// -------------- コンストラクタ --------------

	/**
	 * コンストラクタ（デフォルト容量1024、最小値優先）
	 */
	public IntPriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, false);
	}

	/**
	 * コンストラクタ（最小値優先）
	 *
	 * @param capacity 初期容量
	 */
	public IntPriorityQueue(int capacity) {
		this(capacity, false);
	}

	/**
	 * コンストラクタ（デフォルト容量1024）
	 *
	 * @param isDescendingOrder true の場合は最大値優先（降順）、false の場合は最小値優先（昇順）
	 */
	public IntPriorityQueue(boolean isDescendingOrder) {
		this(DEFAULT_INITIAL_CAPACITY, isDescendingOrder);
	}

	/**
	 * コンストラクタ
	 *
	 * @param capacity          初期容量
	 * @param isDescendingOrder true の場合は最大値優先（降順）、false の場合は最小値優先（昇順）
	 */
	public IntPriorityQueue(int capacity, boolean isDescendingOrder) {
		this.capacity = max(capacity, DEFAULT_INITIAL_CAPACITY);
		this.isDescendingOrder = isDescendingOrder;
		buf = new int[this.capacity];
		size = 0;
		unsortedCount = 0;
	}

	// -------------- 公開メソッド --------------

	/**
	 * 要素を追加する
	 *
	 * @param v 追加する要素
	 */
	public void push(int v) {
		if (size == capacity) buf = Arrays.copyOf(buf, capacity <<= 1);
		if (isDescendingOrder) v = -v;
		buf[size++] = v;
		unsortedCount++;
	}

	/**
	 * 全ての要素を追加する
	 *
	 * @param elements 追加する要素の配列
	 */
	public void addAll(final int[] elements) {
		final int n = elements.length;
		int s = size;
		if (s + n > capacity) {
			int newCap = capacity;
			while (s + n > newCap) newCap <<= 1;
			buf = Arrays.copyOf(buf, newCap);
			capacity = newCap;
		}
		final int[] b = buf;
		if (isDescendingOrder) for (int i = 0; i < n; i++) b[s + i] = -elements[i];
		else System.arraycopy(elements, 0, b, s, n);
		size = s + n;
		unsortedCount += n;
	}

	/**
	 * 全ての要素を追加する
	 *
	 * @param elements 追加する要素のイテラブル
	 */
	public void addAll(final Iterable<Integer> elements) {
		if (elements instanceof final Collection<Integer> c) {
			final int n = c.size();
			int s = size;
			if (s + n > capacity) {
				int newCap = capacity;
				while (s + n > newCap) newCap <<= 1;
				buf = Arrays.copyOf(buf, newCap);
				capacity = newCap;
			}
			final int[] b = buf;
			if (isDescendingOrder) for (final int e : c) b[s++] = -e;
			else for (final int e : c) b[s++] = e;
			size = s;
			unsortedCount += n;
		} else {
			for (final int e : elements) push(e);
		}
	}

	/**
	 * ヒープの先頭要素を取得
	 *
	 * @return ヒープの先頭要素（昇順時は最小、降順時は最大）
	 * @throws NoSuchElementException ヒープが空の場合
	 */
	public int peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return isDescendingOrder ? -buf[0] : buf[0];
	}

	/**
	 * ヒープの先頭要素を削除して返す
	 *
	 * @return 削除された要素（昇順時は最小、降順時は最大）
	 */
	public int poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		int res = isDescendingOrder ? -buf[0] : buf[0];
		if (--size > 0) siftDown(buf[size], 0);
		return res;
	}

	/**
	 * ヒープの先頭要素を置き換える
	 *
	 * @param v 置き換える要素
	 * @return 置き換えられた要素（昇順時は最小、降順時は最大）
	 */
	public int replaceTop(final int v) {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		int res = isDescendingOrder ? -buf[0] : buf[0];
		buf[0] = isDescendingOrder ? -v : v;
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
	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				int v = buf[i++];
				return isDescendingOrder ? -v : v;
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
	private void siftUp(final int v, int i) {
		final int[] b = buf;
		while (i > 0) {
			final int j = (i - 1) >> 1;
			if (v >= b[j]) break;
			b[i] = b[j];
			i = j;
		}
		b[i] = v;
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
	private void siftDown(final int v, int i) {
		final int[] b = buf;
		final int n = size;
		final int half = n >> 1;
		while (i < half) {
			int child = (i << 1) + 1;
			if (child + 1 < n && b[child] > b[child + 1]) child++;
			if (v <= b[child]) break;
			b[i] = b[child];
			i = child;
		}
		b[i] = v;
	}
}
