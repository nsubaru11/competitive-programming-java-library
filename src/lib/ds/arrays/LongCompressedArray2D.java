package lib.ds.arrays;

import java.util.*;

import lib.search.*;

/**
 * long型2次元配列の全要素を共通の順位空間へ座標圧縮する。
 * 各次元の長さが正の長方形配列を前提とし、座標は0始まりの有効範囲とする。
 */
public final class LongCompressedArray2D implements Iterable<Integer> {
	public final int h, w, length, uniqueSize;
	private final RankType rankType;
	private final boolean oneBased;
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
		length = h * w;
		this.rankType = rankType;
		this.oneBased = oneBased;
		compressed = new int[length];
		sorted = new long[length];
		for (int i = 0; i < h; i++) System.arraycopy(a[i], 0, sorted, i * w, w);
		Arrays.sort(sorted);
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
			for (int j = 0; j < w; j++, p++) {
				compressed[p] = ranks[Arrays.binarySearch(sorted, a[i][j])];
			}
		}
	}

	public int size() {
		return length;
	}

	public int uniqueSize() {
		return uniqueSize;
	}

	public RankType rankType() {
		return rankType;
	}

	public boolean isOneBased() {
		return oneBased;
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
		return 0 <= i && i < h && 0 <= j && j < w ? get(i, j) : defaultValue;
	}

	/**
	 * 入力に存在する値 v の順位を返す。
	 */
	public int rankOfValue(final long v) {
		return ranks[Arrays.binarySearch(sorted, v)];
	}

	/**
	 * 実在する順位 rank に対応する元の値を返す。
	 */
	public long valueOfRank(final int rank) {
		return sorted[Arrays.binarySearch(ranks, rank)];
	}

	/**
	 * 圧縮前の値 v が入力に存在するかを返す。
	 */
	public boolean containsValue(final long v) {
		return Arrays.binarySearch(sorted, v) >= 0;
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
				res[i][j] = sorted[Arrays.binarySearch(ranks, compressed[p])];
			}
		}
		return res;
	}

	/**
	 * 圧縮後の順位を元の2次元形状でコピーして返す。
	 */
	public int[][] toArray() {
		final int[][] res = new int[h][w];
		for (int i = 0; i < h; i++) System.arraycopy(compressed, i * w, res[i], 0, w);
		return res;
	}

	/**
	 * 圧縮後の順位を元の2次元形状でコピーして返す。
	 */
	public int[][] compressed() {
		return toArray();
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
				return i < length;
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
		final StringBuilder sb = new StringBuilder(11 * length - 1);
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
		Objects.checkIndex(i, h);
		Objects.checkIndex(j, w);
		return i * w + j;
	}
}
