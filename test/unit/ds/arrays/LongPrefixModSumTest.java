package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongPrefixModSumTest extends PrefixSumContract {

	private static final long[] SAMPLE = {3, 1, 4};

	@Test
	void fromCollection() {
		final LongPrefixModSum a = new LongPrefixModSum(new LongArrayDeque(SAMPLE), mod());
		assertEquals("3 1 4", a.toString());
		assertEquals(5L, a.sum(1, 2));
	}

	int dims() {
		return 1;
	}

	long mod() {
		return (1L << 61) - 1;
	}

	Object create(final int[] s, final long[] flat) {
		return new LongPrefixModSum(flat, mod());
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new LongPrefixModSum(s[0], mod(), i -> flat[i]);
	}
}
