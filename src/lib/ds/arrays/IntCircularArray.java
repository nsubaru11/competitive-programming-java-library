package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * 固定長の回転可能なint配列です。
 */
@SuppressWarnings("unused")
public final class IntCircularArray implements IntMutableArray {
	public final int size;
	private final int[] a;
	private long sum;
	private int offset = 0;

	public IntCircularArray(final int n, final IntUnaryOperator init) {
		size = n;
		a = new int[n];
		int v = init.applyAsInt(0);
		a[0] = v;
		long s = v;
		for (int i = 1; i < n; i++) {
			v = init.applyAsInt(i);
			a[i] = v;
			s += v;
		}
		sum = s;
	}

	public int get(final int i) {
		int j = offset + i;
		if (j >= size) j -= size;
		return a[j];
	}

	public int set(final int i, final int v) {
		int j = offset + i;
		if (j >= size) j -= size;
		int old = a[j];
		a[j] = v;
		sum += (long) v - old;
		return old;
	}

	public void fill(final int v) {
		Arrays.fill(a, v);
		sum = (long) v * size;
	}

	public void setAll(final IntUnaryOperator init) {
		long s = 0;
		for (int i = 0; i < size; i++) {
			int v = init.applyAsInt(i);
			int j = offset + i;
			if (j >= size) j -= size;
			a[j] = v;
			s += v;
		}
		sum = s;
	}

	public int size() {
		return size;
	}

	public boolean contains(final int v) {
		for (int i = 0; i < size; i++) if (get(i) == v) return true;
		return false;
	}

	public int[] toArray() {
		int[] res = new int[size];
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

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < size;
			}

			public int nextInt() {
				return get(idx++);
			}
		};
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final PrimitiveIterator.OfInt it = iterator();
		sb.append(it.nextInt());
		while (it.hasNext()) sb.append(' ').append(it.nextInt());
		return sb.toString();
	}
}
