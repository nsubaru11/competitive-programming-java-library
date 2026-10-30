package lib.ds.arrays;

import java.util.*;

import lib.ds.*;

/**
 * int値の配列を読み取る共通インターフェース。添字は0始まりとする。
 * 範囲外の添字に対する get の動作は実装による。getFirst・getLast は空のとき NoSuchElementException を投げ、
 * OrDefault 系は範囲外または空のとき指定した既定値を返す。
 */
public interface IntArray extends IntCollection {

	int get(int i);

	default int getOrDefault(final int i, final int defaultValue) {
		return 0 <= i && i < size() ? get(i) : defaultValue;
	}

	default int getFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		return get(0);
	}

	default int getFirstOrDefault(final int defaultValue) {
		return getOrDefault(0, defaultValue);
	}

	default int getLast() {
		if (isEmpty()) throw new NoSuchElementException();
		return get(size() - 1);
	}

	default int getLastOrDefault(final int defaultValue) {
		return getOrDefault(size() - 1, defaultValue);
	}

	default int indexOf(final int v) {
		for (int i = 0, n = size(); i < n; i++) {
			if (get(i) == v) return i;
		}
		return -1;
	}

	default int lastIndexOf(final int v) {
		for (int i = size() - 1; i >= 0; i--) {
			if (get(i) == v) return i;
		}
		return -1;
	}

	default Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED);
	}
}
