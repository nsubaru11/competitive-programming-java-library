package lib.ds.arrays;

import java.util.*;

public final class IntPrefixSum implements IntArray {
	private final int length;
	private final long[] sum;

	public IntPrefixSum(int[] a) {
		length = a.length;
		sum = new long[length];
		sum[0] = a[0];
		for (int i = 1; i < length; i++) {
			sum[i] = sum[i - 1] + a[i];
		}
	}

	public IntPrefixSum(IntArray a) {
		length = a.size();
		sum = new long[length];
		sum[0] = a.get(0);
		for (int i = 1; i < length; i++) {
			sum[i] = sum[i - 1] + a.get(i);
		}
	}

	public int get(final int i) {
		return (int) (i > 0 ? sum[i] - sum[i - 1] : sum[0]);
	}

	public long sum() {
		return sum[length - 1];
	}

	public long sum(final int i) {
		return i > 0 ? sum[i] : sum[0];
	}

	public long sum(final int i, final int j) {
		return i > 0 ? sum[j] - sum[i - 1] : sum[j];
	}

	public int size() {
		return length;
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public int nextInt() {
				return get(i++);
			}

			public boolean hasNext() {
				return i < length;
			}
		};
	}
}
