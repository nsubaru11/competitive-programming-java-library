package lib.ds.arrays;

import static java.util.Arrays.*;

import java.util.*;

import lib.search.*;

/**
 * int配列の座標圧縮結果を保持する。
 * 論理添字は {@code 0 <= i < size} とする。空配列にも対応する。
 */
public final class IntCompressedArray implements IntArray {
	public final int size, uniqueSize;
	private final int[] compressed, sorted, ranks;

	/**
	 * 指定配列をコピーして座標圧縮する。順位方式は DENSE、順位は0始まりとする。
	 */
	public IntCompressedArray(final int[] a) {
		this(a, RankType.DENSE, false);
	}

	/**
	 * 指定配列をコピーして座標圧縮する。指定した順位方式で0始まりとする。
	 */
	public IntCompressedArray(final int[] a, final RankType rankType) {
		this(a, rankType, false);
	}

	/**
	 * 指定配列をコピーして座標圧縮する。指定した順位方式と開始位置を使う。
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

	/**
	 * 指定配列をコピーして座標圧縮する。順位方式は DENSE、順位は0始まりとする。
	 */
	public IntCompressedArray(final IntArray a) {
		this(a.toArray(), RankType.DENSE, false);
	}

	/**
	 * 指定配列をコピーして座標圧縮する。指定した順位方式で0始まりとする。
	 */
	public IntCompressedArray(final IntArray a, final RankType rankType) {
		this(a.toArray(), rankType, false);
	}

	/**
	 * 指定配列をコピーして座標圧縮する。指定した順位方式と開始位置を使う。
	 */
	public IntCompressedArray(final IntArray a, final RankType rankType, final boolean oneBased) {
		this(a.toArray(), rankType, oneBased);
	}

	/**
	 * 元の値・順位・配列順を保持する独立したコピーを構築する。
	 */
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

	/**
	 * 指定した有効な論理添字の圧縮後の順位を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public int get(final int i) {
		return compressed[i];
	}

	/**
	 * 入力に存在する値 v の順位を返す。
	 */
	public int rankOfValue(final int v) {
		return ranks[binarySearch(sorted, v)];
	}

	/**
	 * 実在する順位 rank に対応する元の値を返す。
	 */
	public int valueOfRank(final int rank) {
		return sorted[binarySearch(ranks, rank)];
	}

	/**
	 * 圧縮前の値 v が入力に存在するかを返す。
	 */
	public boolean containsValue(final int v) {
		return binarySearch(sorted, v) >= 0;
	}

	/**
	 * 圧縮前の値 v の出現回数を返す。
	 */
	public int count(final int v) {
		return ArrayBinarySearch.count(sorted, v);
	}

	/**
	 * 元の値と配列順を復元した独立した配列を返す。
	 */
	public int[] restore() {
		final int[] res = new int[size];
		for (int i = 0; i < size; i++) res[i] = sorted[binarySearch(ranks, compressed[i])];
		return res;
	}

	/**
	 * 圧縮後の順位を元配列順でコピーして返す。
	 */
	public int[] toArray() {
		return compressed.clone();
	}

	/**
	 * 重複を含むソート済み値のコピーを返す。
	 */
	public int[] sorted() {
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
	 * 元配列順の ORDERED・SIZED・SUBSIZED・IMMUTABLE の spliterator を返す。
	 */
	public Spliterator.OfInt spliterator() {
		return Arrays.spliterator(compressed);
	}

	/**
	 * 論理順の要素を半角スペースで区切った文字列として返す。
	 */
	public String toString() {
		if (size == 0) return "";
		final StringBuilder sb = new StringBuilder(11 * size - 1);
		sb.append(compressed[0]);
		for (int i = 1; i < size; i++) sb.append(' ').append(compressed[i]);
		return sb.toString();
	}
}
