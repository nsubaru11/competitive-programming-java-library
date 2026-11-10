package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntPrefixModSumTest extends PrefixSumContract {

	private static final long[] SAMPLE = {3, 1, 4};

	@Test
	void fromCollection() {
		final IntPrefixModSum a = new IntPrefixModSum(new IntArrayDeque((int[]) Adapter.convert(SAMPLE, int[].class)), (int) mod());
		assertEquals("3 1 4", a.toString());
		assertEquals(5L, a.sum(1, 2));
	}

	int dims() {
		return 1;
	}

	long mod() {
		return 998244353;
	}

	Object create(final int[] s, final long[] flat) {
		return new IntPrefixModSum((int[]) Adapter.convert(flat, int[].class), (int) mod());
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new IntPrefixModSum(s[0], (int) mod(), i -> (int) flat[i]);
	}
}
