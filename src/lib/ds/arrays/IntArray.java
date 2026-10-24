package lib.ds.arrays;

import java.util.*;

import lib.ds.*;

/**
 * int値を格納する配列の読み取り用インターフェースである。
 * iterator の走査順は論理添字の昇順とする。実装の不変性は保証しない。
 */
public interface IntArray extends IntCollection {

	/**
	 * 指定位置の値を返す。範囲外の添字に対する動作は実装に依存する。
	 *
	 * @param i 論理添字。{@code 0 <= i < size()}
	 */
	int get(int i);

	/**
	 * 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。
	 *
	 * @param i 論理添字
	 */
	default int getOrDefault(final int i, final int defaultValue) {
		return 0 <= i && i < size() ? get(i) : defaultValue;
	}

	/**
	 * 現在の論理順の末尾の値を返す。
	 *
	 * @throws NoSuchElementException 要素が空の場合
	 */
	default int getLast() {
		if (isEmpty()) throw new NoSuchElementException();
		return get(size() - 1);
	}

	/**
	 * 現在の論理順の末尾の値を返す。要素が空の場合は defaultValue をそのまま返す。
	 */
	default int getLastOrDefault(final int defaultValue) {
		return getOrDefault(size() - 1, defaultValue);
	}

	/**
	 * 論理添字の昇順に走査する ORDERED・SIZED・SUBSIZED の spliterator を返す。
	 */
	default Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED);
	}
}
