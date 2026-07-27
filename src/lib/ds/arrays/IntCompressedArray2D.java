package lib.ds.arrays;

import static java.util.Arrays.*;

import java.util.*;

import lib.search.*;

/**
 * int型2次元配列の全要素を共通の順位空間へ座標圧縮します。
 */
public final class IntCompressedArray2D implements Iterable<Integer> {
	public final int h, w, length, uniqueSize;
	private final RankType rankType;
	private final boolean oneBased;
	private final int[] compressed, sorted, ranks;

	public IntCompressedArray2D(final int[][] a) {
		this(a, RankType.DENSE, false);
	}

	public IntCompressedArray2D(final int[][] a, final RankType rankType) {
		this(a, rankType, false);
	}

	public IntCompressedArray2D(final int[][] a, final RankType rankType, final boolean oneBased) {
		h = a.length;
		w = a[0].length;
		length = h * w;
		this.rankType = rankType;
		this.oneBased = oneBased;
		compressed = new int[length];
		sorted = new int[length];
		for (int i = 0; i < h; i++) System.arraycopy(a[i], 0, sorted, i * w, w);
		sort(sorted);
		ranks = new int[length];
		int r = oneBased ? 1 : 0, u = 1;
		switch (rankType) {
			case DENSE:
				ranks[0] = r;
				for (int i = 1; i < length; i++) {
					if (sorted[i] != sorted[i - 1]) {
						r++;
						u++;
					}
					ranks[i] = r;
				}
				break;
			case COMPETITION:
				ranks[0] = r++;
				for (int i = 1; i < length; i++, r++) {
					if (sorted[i] != sorted[i - 1]) {
						ranks[i] = r;
						u++;
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
						u++;
					}
				}
				break;
		}
		uniqueSize = u;
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) compressed[p] = ranks[binarySearch(sorted, a[i][j])];
		}
	}

	/**
	 * 指定位置の圧縮後の順位を返します。
	 */
	public int get(final int i, final int j) {
		return compressed[i * w + j];
	}

	public int rankOfValue(final int v) {
		return ranks[binarySearch(sorted, v)];
	}

	public int valueOfRank(final int rank) {
		return sorted[binarySearch(ranks, rank)];
	}

	public RankType rankType() {
		return rankType;
	}

	public boolean isOneBased() {
		return oneBased;
	}

	public int uniqueSize() {
		return uniqueSize;
	}

	public int size() {
		return length;
	}

	public int[][] restore() {
		final int[][] res = new int[h][w];
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) res[i][j] = sorted[binarySearch(ranks, compressed[p])];
		}
		return res;
	}

	public boolean containsValue(final int v) {
		return binarySearch(sorted, v) >= 0;
	}

	public int count(final int v) {
		return ArrayBinarySearch.count(sorted, v);
	}

	/**
	 * 圧縮後の順位を元の2次元形状でコピーして返します。
	 */
	public int[][] toArray() {
		final int[][] res = new int[h][w];
		for (int i = 0; i < h; i++) System.arraycopy(compressed, i * w, res[i], 0, w);
		return res;
	}

	/**
	 * 圧縮後の順位を元の2次元形状でコピーして返します。
	 */
	public int[][] compressed() {
		return toArray();
	}

	public int[] sorted() {
		return copyOf(sorted, length);
	}

	public int[] ranks() {
		return copyOf(ranks, length);
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public int nextInt() {
				return compressed[i++];
			}

			public boolean hasNext() {
				return i < length;
			}
		};
	}

	/**
	 * 各行を半角スペース区切り、行間を改行した文字列として返します。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(11 * length - 1);
		sb.append(compressed[0]);
		for (int j = 1; j < w; j++) sb.append(' ').append(compressed[j]);
		for (int i = 1, p = w; i < h; i++) {
			sb.append('\n').append(compressed[p++]);
			for (int j = 1; j < w; j++) sb.append(' ').append(compressed[p++]);
		}
		return sb.toString();
	}
}
