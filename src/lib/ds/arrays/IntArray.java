package lib.ds.arrays;

import java.util.*;

import lib.ds.*;

/**
 * int値を格納する配列の読み取り用インターフェースである。実装の不変性は保証しない。
 * 論理添字は {@code 0 <= i < size()} とし、iterator と spliterator は論理添字の昇順に走査する。
 * get の範囲外の動作は実装に依存する。getOrDefault・getFirstOrDefault・getLastOrDefault は範囲外または空の場合に defaultValue を返し、
 * getFirst・getLast は空の場合に NoSuchElementException を投げる。indexOf・lastIndexOf は該当がなければ -1 を返す。
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
