package lib.ds.arrays;

import static java.util.Arrays.*;

import java.util.*;

import lib.ds.*;
import lib.search.*;

/**
 * long型2次元配列の全要素を共通の順位空間へ座標圧縮する。
 * 各次元の長さが正の長方形配列を前提とし、座標は0始まりの有効範囲とする。
 */
public final class LongCompressedArray2D implements IntCollection {
	public final int h, w, size, uniqueSize;
	private final int[] compressed, ranks;
	private final long[] sorted;

	/**
	 * 指定配列をコピーして座標圧縮する。順位方式は DENSE、順位は0始まりとする。
	 */
	public LongCompressedArray2D(final long[][] a) {
		this(a, RankType.DENSE, false);
	}

	/**
	 * 指定配列をコピーして座標圧縮する。指定した順位方式で0始まりとする。
	 */
	public LongCompressedArray2D(final long[][] a, final RankType rankType) {
		this(a, rankType, false);
	}

	/**
	 * 指定配列をコピーして座標圧縮する。指定した順位方式と開始位置を使う。
	 */
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

	/**
	 * 元の値・順位・配列順・寸法を保持する独立したコピーを構築する。
	 */
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

	/**
	 * {@code 0 <= i < h} かつ {@code 0 <= j < w} かを返す。
	 */
	public boolean inRange(final int i, final int j) {
		return (i | j | (h - 1 - i) | (w - 1 - j)) >= 0;
	}

	/**
	 * 指定した有効な論理添字の圧縮後の順位を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public int get(final int i, final int j) {
		return compressed[checkedIndex(i, j)];
	}

	/**
	 * 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。
	 */
	public int getOrDefault(final int i, final int j, final int defaultValue) {
		return inRange(i, j) ? compressed[i * w + j] : defaultValue;
	}

	/**
	 * 入力に存在する値 v の順位を返す。
	 */
	public int rankOfValue(final long v) {
		return ranks[binarySearch(sorted, v)];
	}

	/**
	 * 実在する順位 rank に対応する元の値を返す。
	 */
	public long valueOfRank(final int rank) {
		return sorted[binarySearch(ranks, rank)];
	}

	/**
	 * 圧縮前の値 v が入力に存在するかを返す。
	 */
	public boolean containsValue(final long v) {
		return binarySearch(sorted, v) >= 0;
	}

	/**
	 * 圧縮前の値 v の出現回数を返す。
	 */
	public int count(final long v) {
		return ArrayBinarySearch.count(sorted, v);
	}

	/**
	 * 元の値と配列順を復元した独立した配列を返す。
	 */
	public long[][] restore() {
		final long[][] res = new long[h][w];
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				res[i][j] = sorted[binarySearch(ranks, compressed[p])];
			}
		}
		return res;
	}

	/**
	 * 圧縮後の順位を元の2次元形状でコピーして返す。
	 */
	public int[][] toArray2D() {
		final int[][] res = new int[h][w];
		for (int i = 0; i < h; i++) System.arraycopy(compressed, i * w, res[i], 0, w);
		return res;
	}

	/**
	 * 重複を含むソート済み値のコピーを返す。
	 */
	public long[] sorted() {
		return sorted.clone();
	}

	/**
	 * ソート済み値に対応する順位配列のコピーを返す。
	 */
	public int[] ranks() {
		return ranks.clone();
	}

	/**
	 * 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。
	 */
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

	/**
	 * 行優先順の ORDERED・SIZED・SUBSIZED・IMMUTABLE の spliterator を返す。
	 */
	public Spliterator.OfInt spliterator() {
		return Arrays.spliterator(compressed);
	}

	/**
	 * 各行を半角スペース区切り、行間を改行した文字列として返す。
	 */
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
