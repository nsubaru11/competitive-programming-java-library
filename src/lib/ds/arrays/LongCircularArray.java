package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * 固定長の回転可能なlong配列です。
 */
@SuppressWarnings("unused")
public final class LongCircularArray implements LongMutableArray {
	public final int length;
	private final long[] arr;
	private long sum;
	private long rotation = 0;
	private int offset = 0;

	public LongCircularArray(final int n, final IntToLongFunction init) {
		length = n;
		arr = new long[n];
		long s = arr[0] = init.applyAsLong(0);
		for (int i = 1; i < n; i++) {
			s += arr[i] = init.applyAsLong(i);
		}
		sum = s;
	}

	/**
	 * 指定された配列の要素を同じ順序で保持する循環配列を構築します。
	 */
	public LongCircularArray(final long[] a) {
		length = a.length;
		arr = Arrays.copyOf(a, length);
		long s = 0;
		for (final long v : a) s += v;
		sum = s;
	}

	/**
	 * 指定された配列の論理順を保持する循環配列を構築します。
	 */
	public LongCircularArray(final LongArray a) {
		length = a.size();
		arr = new long[length];
		long s = 0;
		for (int i = 0; i < length; i++) {
			s += arr[i] = a.get(i);
		}
		sum = s;
	}

	/**
	 * supplierが生成するn要素を保持する循環配列を返します。
	 */
	public static LongCircularArray generate(final int n, final LongSupplier init) {
		return new LongCircularArray(n, _ -> init.getAsLong());
	}

	public long get(final int i) {
		int j = offset + i;
		if (j >= length) j -= length;
		return arr[j];
	}

	public long set(final int i, final long v) {
		int j = offset + i;
		if (j >= length) j -= length;
		long old = arr[j];
		arr[j] = v;
		sum += v - old;
		return old;
	}

	public void fill(final long v) {
		Arrays.fill(arr, v);
		sum = v * length;
		rotation = 0;
		offset = 0;
	}

	public void setAll(final IntToLongFunction init) {
		long s = 0;
		for (int i = 0; i < length; i++) {
			s += arr[i] = init.applyAsLong(i);
		}
		sum = s;
		rotation = 0;
		offset = 0;
	}

	public int size() {
		return length;
	}

	public boolean contains(final long v) {
		for (int i = 0; i < length; i++) if (get(i) == v) return true;
		return false;
	}

	public long[] toArray() {
		long[] res = new long[length];
		for (int i = 0; i < length; i++) res[i] = get(i);
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
		if (offset == length) offset = 0;
		rotation++;
	}

	/**
	 * 論理配列を右へ1要素回転します。
	 */
	public void rShift() {
		offset--;
		if (offset == -1) offset = length - 1;
		rotation--;
	}

	/**
	 * 論理配列を左へn要素回転します。
	 */
	public void lShift(final int n) {
		if (n < 0) {
			rShift(-n);
			return;
		}
		offset += n % length;
		if (offset >= length) offset -= length;
		rotation += n;
	}

	/**
	 * 論理配列を右へn要素回転します。
	 */
	public void rShift(final int n) {
		if (n < 0) {
			lShift(-n);
			return;
		}
		offset -= n % length;
		if (offset < 0) offset += length;
		rotation -= n;
	}

	/**
	 * 左回転を正、右回転を負とする総回転数を返します。
	 */
	public long rotation() {
		return rotation;
	}

	/**
	 * 回転状態を初期位置へ戻します。
	 */
	public void resetRotation() {
		rotation = 0;
		offset = 0;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < length;
			}

			public long nextLong() {
				return get(idx++);
			}
		};
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder(21 * length - 1);
		sb.append(arr[offset]);
		for (int i = 1; i < length; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
