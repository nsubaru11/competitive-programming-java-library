package lib.ds.arrays;

import java.util.*;

import lib.ds.*;

/**
 * long値の配列を読み取る共通インターフェース。添字は0始まりとする。
 * 範囲外の添字に対する get の動作は実装による。getFirst・getLast は空のとき NoSuchElementException を投げ、
 * OrDefault 系は範囲外または空のとき指定した既定値を返す。
 */
public interface LongArray extends LongCollection {

	long get(int i);

	default long getOrDefault(final int i, final long defaultValue) {
		return 0 <= i && i < size() ? get(i) : defaultValue;
	}

	default long getFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		return get(0);
	}

	default long getFirstOrDefault(final long defaultValue) {
		return getOrDefault(0, defaultValue);
	}

	default long getLast() {
		if (isEmpty()) throw new NoSuchElementException();
		return get(size() - 1);
	}

	default long getLastOrDefault(final long defaultValue) {
		return getOrDefault(size() - 1, defaultValue);
	}

	default int indexOf(final long v) {
		for (int i = 0, n = size(); i < n; i++) {
			if (get(i) == v) return i;
		}
		return -1;
	}

	default int lastIndexOf(final long v) {
		for (int i = size() - 1; i >= 0; i--) {
			if (get(i) == v) return i;
		}
		return -1;
	}

	default Spliterator.OfLong spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED);
	}
}
