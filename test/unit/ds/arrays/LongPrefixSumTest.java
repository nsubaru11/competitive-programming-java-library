package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongPrefixSumTest extends PrefixSumContract {

	private static final long[] SAMPLE = {3, 1, 4};

	@Test
	void fromCollection() {
		final LongPrefixSum a = new LongPrefixSum(new LongArrayDeque(SAMPLE));
		assertEquals("3 1 4", a.toString());
		assertEquals(5L, a.sum(1, 2));
	}

	int dims() {
		return 1;
	}

	long mod() {
		return 0;
	}

	Object create(final int[] s, final long[] flat) {
		return new LongPrefixSum(flat);
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new LongPrefixSum(s[0], i -> flat[i]);
	}
}
