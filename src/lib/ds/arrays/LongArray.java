package lib.ds.arrays;

import java.util.*;

import lib.ds.*;

/**
 * long値を格納する配列の読み取り用インターフェースである。
 * iterator の走査順は論理添字の昇順とする。実装の不変性は保証しない。
 */
public interface LongArray extends LongCollection {

	/**
	 * 指定位置の値を返す。範囲外の添字に対する動作は実装に依存する。
	 *
	 * @param i 論理添字。{@code 0 <= i < size()}
	 */
	long get(int i);

	/**
	 * 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。
	 *
	 * @param i 論理添字
	 */
	default long getOrDefault(final int i, final long defaultValue) {
		return 0 <= i && i < size() ? get(i) : defaultValue;
	}

	/**
	 * 現在の論理順の先頭の値を返す。
	 *
	 * @throws NoSuchElementException 要素が空の場合
	 */
	default long getFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		return get(0);
	}

	/**
	 * 現在の論理順の先頭の値を返す。要素が空の場合は defaultValue をそのまま返す。
	 */
	default long getFirstOrDefault(final long defaultValue) {
		return getOrDefault(0, defaultValue);
	}

	/**
	 * 現在の論理順の末尾の値を返す。
	 *
	 * @throws NoSuchElementException 要素が空の場合
	 */
	default long getLast() {
		if (isEmpty()) throw new NoSuchElementException();
		return get(size() - 1);
	}

	/**
	 * 現在の論理順の末尾の値を返す。要素が空の場合は defaultValue をそのまま返す。
	 */
	default long getLastOrDefault(final long defaultValue) {
		return getOrDefault(size() - 1, defaultValue);
	}

	/**
	 * 指定値が最初に現れる論理添字を返す。存在しない場合は -1 を返す。
	 */
	default int indexOf(final long v) {
		for (int i = 0, n = size(); i < n; i++) {
			if (get(i) == v) return i;
		}
		return -1;
	}

	/**
	 * 指定値が最後に現れる論理添字を返す。存在しない場合は -1 を返す。
	 */
	default int lastIndexOf(final long v) {
		for (int i = size() - 1; i >= 0; i--) {
			if (get(i) == v) return i;
		}
		return -1;
	}

	/**
	 * 論理添字の昇順に走査する ORDERED・SIZED・SUBSIZED の spliterator を返す。
	 */
	default Spliterator.OfLong spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED);
	}
}
