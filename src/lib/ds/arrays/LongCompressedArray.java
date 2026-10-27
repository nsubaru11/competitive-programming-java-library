package lib.ds.arrays;

import static java.util.Arrays.*;

import java.util.*;

import lib.search.*;

/**
 * long配列の座標圧縮結果を int の順位で保持する。空配列にも対応し、論理添字は {@code 0 <= i < size} とする。
 * 構築時に入力をコピーする。順位方式の既定は DENSE、順位は既定で0始まり、oneBased が true のとき1始まりとする。
 * get・走査・toArray は元配列順の圧縮後の順位を返し、restore は元の値を復元する。sorted・ranks は重複を含むソート済み値と対応する順位のコピーを返す。
 * rankOfValue は入力に存在する値、valueOfRank は実在する順位を前提とする。containsValue・count は圧縮前の値を対象とする。
 */
public final class LongCompressedArray implements IntArray {
	public final int size, uniqueSize;
	private final int[] compressed, ranks;
	private final long[] sorted;

	public LongCompressedArray(final long[] a) {
		this(a, RankType.DENSE, false);
	}

	public LongCompressedArray(final long[] a, final RankType rankType) {
		this(a, rankType, false);
	}

	public LongCompressedArray(final long[] a, final RankType rankType, final boolean oneBased) {
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

	public LongCompressedArray(final LongArray a) {
		this(a.toArray(), RankType.DENSE, false);
	}

	public LongCompressedArray(final LongArray a, final RankType rankType) {
		this(a.toArray(), rankType, false);
	}

	public LongCompressedArray(final LongArray a, final RankType rankType, final boolean oneBased) {
		this(a.toArray(), rankType, oneBased);
	}

	public LongCompressedArray(final LongCompressedArray other) {
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

	public long[] restore() {
		final long[] res = new long[size];
		for (int i = 0; i < size; i++) res[i] = sorted[binarySearch(ranks, compressed[i])];
		return res;
	}

	public int[] toArray() {
		return compressed.clone();
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
		for (int i = 1; i < size; i++) sb.append(' ').append(compressed[i]);
		return sb.toString();
	}
}
