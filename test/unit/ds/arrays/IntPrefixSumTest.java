package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntPrefixSumTest extends PrefixSumContract {

	private static final long[] SAMPLE = {3, 1, 4};

	@Test
	void fromCollection() {
		final IntPrefixSum a = new IntPrefixSum(new IntArrayDeque((int[]) Adapter.convert(SAMPLE, int[].class)));
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
		return new IntPrefixSum((int[]) Adapter.convert(flat, int[].class));
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new IntPrefixSum(s[0], i -> (int) flat[i]);
	}
}
