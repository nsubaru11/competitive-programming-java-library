package lib.ds.arrays;

import static java.util.Arrays.*;

import java.util.*;

import lib.ds.*;
import lib.search.*;

/**
 * int値の2次元配列の全要素をまとめて座標圧縮した順位の配列。get・走査は各要素の順位を返す。順位は int で表す。
 * 座標は0始まりの (行 i, 列 j) とし、範囲外では IndexOutOfBoundsException を投げる。入力は1行以上の長方形配列とする。
 * 構築時に入力をコピーする。順位の付け方は RankType で選び（既定は DENSE）、順位は既定で0始まりとする。
 */
public final class IntCompressedArray2D implements IntCollection {
	public final int h, w, size, uniqueSize;
	private final int[] compressed, sorted, ranks;

	public IntCompressedArray2D(final int[][] a) {
		this(a, RankType.DENSE, false);
	}

	public IntCompressedArray2D(final int[][] a, final RankType rankType) {
		this(a, rankType, false);
	}

	/**
	 * oneBased が true なら順位を1始まりにする。
	 */
	public IntCompressedArray2D(final int[][] a, final RankType rankType, final boolean oneBased) {
		h = a.length;
		w = a[0].length;
		size = h * w;
		compressed = new int[size];
		sorted = new int[size];
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

	public IntCompressedArray2D(final IntCompressedArray2D other) {
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
	 * 圧縮前の値の2次元配列を返す。
	 */
	public int[][] restore() {
		final int[][] res = new int[h][w];
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				res[i][j] = sorted[binarySearch(ranks, compressed[p])];
			}
		}
		return res;
	}

	/**
	 * 各要素の順位を元の形の2次元配列で返す。
	 */
	public int[][] toArray2D() {
		final int[][] res = new int[h][w];
		for (int i = 0; i < h; i++) System.arraycopy(compressed, i * w, res[i], 0, w);
		return res;
	}

	/**
	 * 全要素を昇順に並べた配列を返す。重複は除かない。
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
