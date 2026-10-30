package lib.ds.arrays;

import static java.util.Arrays.*;

import java.util.*;

import lib.ds.*;
import lib.search.*;

/**
 * int配列を座標圧縮した順位の配列。get・走査・toArray は各要素の順位を返す。順位は int で表す。
 * 構築時に入力をコピーする。順位の付け方は RankType で選び（既定は DENSE）、順位は既定で0始まりとする。
 */
public final class IntCompressedArray implements IntArray {
	public final int size, uniqueSize;
	private final int[] compressed, sorted, ranks;

	public IntCompressedArray(final int[] a) {
		this(a, RankType.DENSE, false);
	}

	public IntCompressedArray(final int[] a, final RankType rankType) {
		this(a, rankType, false);
	}

	/**
	 * oneBased が true なら順位を1始まりにする。
	 */
	public IntCompressedArray(final int[] a, final RankType rankType, final boolean oneBased) {
		size = a.length;
		compressed = new int[size];
		sorted = a.clone();
		sort(sorted);
		ranks = new int[size];
		if (size == 0) {
			uniqueSize = 0;
			return;
		}
		int r = oneBased ? 1 : 0, u = 1;
		switch (rankType) {
			case DENSE -> {
				ranks[0] = r;
				for (int i = 1; i < size; i++) {
					if (sorted[i] != sorted[i - 1]) {
						r++;
						u++;
					}
					ranks[i] = r;
				}
			}
			case COMPETITION -> {
				ranks[0] = r;
				for (int i = 1; i < size; i++) {
					if (sorted[i] != sorted[i - 1]) {
						ranks[i] = i + r;
						u++;
					} else {
						ranks[i] = ranks[i - 1];
					}
				}
			}
			case MODIFIED_COMPETITION -> {
				ranks[size - 1] = size - 1 + r;
				for (int i = size - 2; i >= 0; i--) {
					if (sorted[i] != sorted[i + 1]) {
						ranks[i] = i + r;
						u++;
					} else {
						ranks[i] = ranks[i + 1];
					}
				}
			}
		}
		uniqueSize = u;
		for (int i = 0; i < size; i++) compressed[i] = ranks[binarySearch(sorted, a[i])];
	}

	public IntCompressedArray(final IntCollection a) {
		this(a.toArray(), RankType.DENSE, false);
	}

	public IntCompressedArray(final IntCollection a, final RankType rankType) {
		this(a.toArray(), rankType, false);
	}

	/**
	 * oneBased が true なら順位を1始まりにする。
	 */
	public IntCompressedArray(final IntCollection a, final RankType rankType, final boolean oneBased) {
		this(a.toArray(), rankType, oneBased);
	}

	public IntCompressedArray(final IntCompressedArray other) {
		size = other.size;
		uniqueSize = other.uniqueSize;
		compressed = other.compressed.clone();
		sorted = other.sorted.clone();
		ranks = other.ranks.clone();
	}

	public int size() {
		return size;
	}

	public int get(final int i) {
		return compressed[i];
	}

	/**
	 * 値 v の順位を返す。v は入力に含まれる値とする。
	 */
	public int rankOfValue(final int v) {
		return ranks[binarySearch(sorted, v)];
	}

	/**
	 * 順位 rank を持つ値を返す。rank は実在する順位とする。
	 */
	public int valueOfRank(final int rank) {
		return sorted[binarySearch(ranks, rank)];
	}

	public boolean containsValue(final int v) {
		return binarySearch(sorted, v) >= 0;
	}

	/**
	 * 入力に含まれる値 v の個数を返す。
	 */
	public int count(final int v) {
		return ArrayBinarySearch.count(sorted, v);
	}

	/**
	 * 圧縮前の値の配列を返す。
	 */
	public int[] restore() {
		final int[] res = new int[size];
		for (int i = 0; i < size; i++) res[i] = sorted[binarySearch(ranks, compressed[i])];
		return res;
	}

	public int[] toArray() {
		return compressed.clone();
	}

	/**
	 * 入力を昇順に並べた配列を返す。重複は除かない。
	 */
	public int[] sorted() {
		return sorted.clone();
	}

	/**
	 * sorted() の各要素の順位を返す。
	 */
	public int[] ranks() {
		return ranks.clone();
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return compressed[i++];
			}
		};
	}

	public Spliterator.OfInt spliterator() {
		return Arrays.spliterator(compressed);
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(11 * size - 1);
		sb.append(compressed[0]);
		for (int i = 1; i < size; i++) sb.append(' ').append(compressed[i]);
		return sb.toString();
	}
}
