package lib.ds.arrays;

import static java.util.Arrays.*;

import java.util.*;

import lib.ds.*;
import lib.search.*;

/**
 * long値の2次元配列の全要素を共通の順位空間へ座標圧縮する。各次元の長さが正の長方形配列を前提とする。
 * 座標は0始まりの行 i・列 j とし、get は範囲外で IndexOutOfBoundsException を投げ、getOrDefault は defaultValue を返す。
 * 構築時に入力をコピーする。順位方式の既定は DENSE、順位は既定で0始まり、oneBased が true のとき1始まりとする。
 * 走査・toString は行優先順、toArray2D は元の形状で圧縮後の順位を返し、restore は元の値を復元する。sorted・ranks は重複を含むソート済み値と対応する順位のコピーを返す。
 * rankOfValue は入力に存在する値、valueOfRank は実在する順位を前提とする。containsValue・count は圧縮前の値を対象とする。
 */
public final class LongCompressedArray2D implements IntCollection {
	public final int h, w, size, uniqueSize;
	private final int[] compressed, ranks;
	private final long[] sorted;

	public LongCompressedArray2D(final long[][] a) {
		this(a, RankType.DENSE, false);
	}

	public LongCompressedArray2D(final long[][] a, final RankType rankType) {
		this(a, rankType, false);
	}

	public LongCompressedArray2D(final long[][] a, final RankType rankType, final boolean oneBased) {
		h = a.length;
		w = a[0].length;
		size = h * w;
		compressed = new int[size];
		sorted = new long[size];
		for (int i = 0; i < h; i++) System.arraycopy(a[i], 0, sorted, i * w, w);
		sort(sorted);
		ranks = new int[size];
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
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				compressed[p] = ranks[binarySearch(sorted, a[i][j])];
			}
		}
	}

	public LongCompressedArray2D(final LongCompressedArray2D other) {
		h = other.h;
		w = other.w;
		size = other.size;
		uniqueSize = other.uniqueSize;
		compressed = other.compressed.clone();
		sorted = other.sorted.clone();
		ranks = other.ranks.clone();
	}

	public int size() {
		return size;
	}

	public boolean inRange(final int i, final int j) {
		return (i | j | (h - 1 - i) | (w - 1 - j)) >= 0;
	}

	public int get(final int i, final int j) {
		return compressed[checkedIndex(i, j)];
	}

	public int getOrDefault(final int i, final int j, final int defaultValue) {
		return inRange(i, j) ? compressed[i * w + j] : defaultValue;
	}

	public int rankOfValue(final long v) {
		return ranks[binarySearch(sorted, v)];
	}

	public long valueOfRank(final int rank) {
		return sorted[binarySearch(ranks, rank)];
	}

	public boolean containsValue(final long v) {
		return binarySearch(sorted, v) >= 0;
	}

	public int count(final long v) {
		return ArrayBinarySearch.count(sorted, v);
	}

	public long[][] restore() {
		final long[][] res = new long[h][w];
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				res[i][j] = sorted[binarySearch(ranks, compressed[p])];
			}
		}
		return res;
	}

	public int[][] toArray2D() {
		final int[][] res = new int[h][w];
		for (int i = 0; i < h; i++) System.arraycopy(compressed, i * w, res[i], 0, w);
		return res;
	}

	public long[] sorted() {
		return sorted.clone();
	}

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
		for (int j = 1; j < w; j++) sb.append(' ').append(compressed[j]);
		for (int i = 1, p = w; i < h; i++) {
			sb.append('\n').append(compressed[p++]);
			for (int j = 1; j < w; j++) {
				sb.append(' ').append(compressed[p++]);
			}
		}
		return sb.toString();
	}

	private int checkedIndex(final int i, final int j) {
		Objects.checkIndex(j, w);
		return i * w + j;
	}
}
