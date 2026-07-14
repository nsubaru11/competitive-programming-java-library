package lib.ds.arrays;

import java.util.*;

public final class LongPrefixSum implements LongArray {
	private final int length;
	private final long[] sum;

	public LongPrefixSum(int[] a) {
		length = a.length;
		sum = new long[length];
		sum[0] = a[0];
		for (int i = 1; i < length; i++) {
			sum[i] = sum[i - 1] + a[i];
		}
	}

	public LongPrefixSum(LongArray a) {
		length = a.size();
		sum = new long[length];
		sum[0] = a.get(0);
		for (int i = 1; i < length; i++) {
			sum[i] = sum[i - 1] + a.get(i);
		}
	}

	public long get(final int i) {
		return i > 0 ? sum[i] - sum[i - 1] : sum[0];
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

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public long nextLong() {
				return get(i++);
			}

			public boolean hasNext() {
				return i < length;
			}
		};
	}
}
