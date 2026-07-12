package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * 固定長の回転可能なlong配列です。
 */
@SuppressWarnings("unused")
public final class LongCircularArray implements LongArray {
	public final int size;
	private final long[] arr;
	private long sum;
	private int offset = 0;

	public LongCircularArray(final int n, final IntToLongFunction init) {
		size = n;
		arr = new long[n];
		long v0 = init.applyAsLong(0);
		arr[0] = v0;
		long s = v0;
		for (int i = 1; i < n; i++) {
			long v = init.applyAsLong(i);
			arr[i] = v;
			s += v;
		}
		sum = s;
	}

	public long get(final int i) {
		int j = offset + i;
		if (j >= size) j -= size;
		return arr[j];
	}

	public long set(final int i, final long v) {
		int j = offset + i;
		if (j >= size) j -= size;
		long old = arr[j];
		arr[j] = v;
		sum += v - old;
		return old;
	}

	public void fill(final long v) {
		Arrays.fill(arr, v);
		sum = v * size;
	}

	public void setAll(final IntToLongFunction init) {
		long s = 0;
		for (int i = 0; i < size; i++) {
			long v = init.applyAsLong(i);
			int j = offset + i;
			if (j >= size) j -= size;
			arr[j] = v;
			s += v;
		}
		sum = s;
	}

	public int size() {
		return size;
	}

	public boolean contains(final long v) {
		for (int i = 0; i < size; i++) if (get(i) == v) return true;
		return false;
	}

	public long[] toArray() {
		long[] res = new long[size];
		for (int i = 0; i < size; i++) res[i] = get(i);
		return res;
	}

	public long sum() {
		return sum;
	}

	/**
	 * 論理配列を左へ1要素回転します。
	 */
	public void lShift() {
		offset++;
		if (offset == size) offset = 0;
	}

	/** 論理配列を右へ1要素回転します。 */
	public void rShift() {
		offset--;
		if (offset == -1) offset = size - 1;
	}

	/** 論理配列を左へn要素回転します。 */
	public void lShift(final int n) {
		if (n < 0) {
			rShift(-n);
			return;
		}
		offset += n % size;
		if (offset >= size) offset -= size;
	}

	/** 論理配列を右へn要素回転します。 */
	public void rShift(final int n) {
		if (n < 0) {
			lShift(-n);
			return;
		}
		offset -= n % size;
		if (offset < 0) offset += size;
	}

	/** 回転状態を初期位置へ戻します。 */
	public void resetRotation() {
		offset = 0;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < size;
			}

			public long nextLong() {
				return get(idx++);
			}
		};
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final PrimitiveIterator.OfLong it = iterator();
		sb.append(it.nextLong());
		while (it.hasNext()) sb.append(' ').append(it.nextLong());
		return sb.toString();
	}
}
