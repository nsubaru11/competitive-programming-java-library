package lib.ds.arrays;

import java.util.*;

import lib.search.*;

/**
 * int配列の座標圧縮結果を保持する。
 * 論理添字は {@code 0 <= i < length} とする。空配列にも対応する。
 */
public final class IntCompressedArray implements IntArray {
	public final int length, uniqueSize;
	private final RankType rankType;
	private final boolean oneBased;
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
		this.rankType = rankType;
		this.oneBased = oneBased;
		length = a.length;
		compressed = new int[length];
		sorted = a.clone();
		Arrays.sort(sorted);
		ranks = new int[length];
		if (length == 0) {
			uniqueSize = 0;
			return;
		}
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
		for (int i = 0; i < length; i++) compressed[i] = ranks[Arrays.binarySearch(sorted, a[i])];
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
	public int get(final int i) {
		return compressed[Objects.checkIndex(i, length)];
	}

	/**
	 * 入力に存在する値 v の順位を返す。
	 */
	public int rankOfValue(final int v) {
		return ranks[Arrays.binarySearch(sorted, v)];
	}

	/**
	 * 実在する順位 rank に対応する元の値を返す。
	 */
	public int valueOfRank(final int rank) {
		return sorted[Arrays.binarySearch(ranks, rank)];
	}

	/**
	 * 圧縮前の値 v が入力に存在するかを返す。
	 */
	public boolean containsValue(final int v) {
		return Arrays.binarySearch(sorted, v) >= 0;
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
		final int[] res = new int[length];
		for (int i = 0; i < length; i++) res[i] = sorted[Arrays.binarySearch(ranks, compressed[i])];
		return res;
	}

	/**
	 * 圧縮後の順位を元配列順でコピーして返す。
	 */
	public int[] toArray() {
		return compressed.clone();
	}

	/**
	 * 圧縮後の順位を元配列順でコピーして返す。
	 */
	public int[] compressed() {
		return toArray();
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
				return i < length;
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
		if (length == 0) return "";
		final StringBuilder sb = new StringBuilder(11 * length - 1);
		sb.append(compressed[0]);
		for (int i = 1; i < length; i++) sb.append(' ').append(compressed[i]);
		return sb.toString();
	}
}
