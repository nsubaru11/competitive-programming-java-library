package lib.ds.arrays;

import static java.util.Arrays.*;

import java.util.*;

import lib.search.*;

/**
 * long配列の座標圧縮結果をint値で保持します。
 */
public final class LongCompressedArray implements IntArray {
	public final int length, distinctSize;
	private final RankType rankType;
	private final boolean oneBased;
	private final int[] compressed, ranks;
	private final long[] sorted;

	public LongCompressedArray(final long[] a) {
		this(a, RankType.DENSE, false);
	}

	public LongCompressedArray(final long[] a, final RankType rankType) {
		this(a, rankType, false);
	}

	public LongCompressedArray(final LongArray a) {
		this(a.toArray(), RankType.DENSE, false);
	}

	public LongCompressedArray(final LongArray a, final RankType rankType) {
		this(a.toArray(), rankType, false);
	}

	public LongCompressedArray(final LongArray a, final RankType rankType, final boolean oneBased) {
		this(a.toArray(), rankType, oneBased);
	}

	public LongCompressedArray(final long[] a, final RankType rankType, final boolean oneBased) {
		this.rankType = rankType;
		this.oneBased = oneBased;
		length = a.length;
		compressed = new int[length];
		sorted = copyOf(a, length);
		sort(sorted);
		ranks = new int[length];
		int r = oneBased ? 1 : 0;
		int distinctSize = 1;
		switch (rankType) {
			case DENSE:
				ranks[0] = r;
				for (int i = 1; i < length; i++) {
					if (sorted[i] != sorted[i - 1]) {
						r++;
						distinctSize++;
					}
					ranks[i] = r;
				}
				break;
			case COMPETITION:
				ranks[0] = r++;
				for (int i = 1; i < length; i++, r++) {
					if (sorted[i] != sorted[i - 1]) {
						ranks[i] = r;
						distinctSize++;
					} else {
						ranks[i] = ranks[i - 1];
					}
				}
				break;
			case MODIFIED_COMPETITION:
				ranks[length - 1] = length - 1 + r;
				for (int i = length - 2; i >= 0; i--) {
					if (sorted[i] == sorted[i + 1]) {
						ranks[i] = ranks[i + 1];
					} else {
						ranks[i] = i + r;
						distinctSize++;
					}
				}
				break;
		}
		this.distinctSize = distinctSize;
		for (int i = 0; i < length; i++) {
			compressed[i] = ranks[binarySearch(sorted, a[i])];
		}
	}

	public int get(final int i) {
		return compressed[i];
	}

	public int rankOfValue(final long v) {
		return ranks[binarySearch(sorted, v)];
	}

	public long valueOfRank(final int rank) {
		return sorted[binarySearch(ranks, rank)];
	}

	public RankType rankType() {
		return rankType;
	}

	public boolean isOneBased() {
		return oneBased;
	}

	public int distinctSize() {
		return distinctSize;
	}

	public int size() {
		return length;
	}

	public long[] restore() {
		final long[] res = new long[length];
		for (int i = 0; i < length; i++) {
			res[i] = sorted[binarySearch(ranks, compressed[i])];
		}
		return res;
	}

	public boolean containsValue(final long v) {
		return binarySearch(sorted, v) >= 0;
	}

	public int count(final long v) {
		return ArrayBinarySearch.count(sorted, v);
	}

	/** 圧縮後の順位を元配列順でコピーして返します。 */
	public int[] toArray() {
		return copyOf(compressed, length);
	}

	/** 圧縮後の順位を元配列順でコピーして返します。 */
	public int[] compressed() {
		return toArray();
	}

	public long[] sorted() {
		return copyOf(sorted, length);
	}

	public int[] ranks() {
		return copyOf(ranks, length);
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			int i = 0;

			public int nextInt() {
				return compressed[i++];
			}

			public boolean hasNext() {
				return i < length;
			}
		};
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder(11 * length);
		sb.append(compressed[0]);
		for (int i = 1; i < length; i++) sb.append(' ').append(compressed[i]);
		return sb.toString();
	}

	public enum RankType {
		/**
		 * 1, 2, 2, 3形式。
		 */
		DENSE,
		/**
		 * 1, 2, 2, 4形式。
		 */
		COMPETITION,
		/**
		 * 1, 3, 3, 4形式。
		 */
		MODIFIED_COMPETITION
	}
}
