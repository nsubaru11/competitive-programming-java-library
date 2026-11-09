package unit.math;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

final class FactorialTableTest {

	private static final int MOD = 998244353;

	// 初期サイズ 0 から動的に拡張して、階乗と階乗逆元を求める
	@Test
	void factorialAndInverse() {
		final FactorialTable table = new FactorialTable(0, MOD);
		long expected = 1;
		for (int i = 0; i <= 100_000; i++) {
			if (i > 0) expected = expected * i % MOD;
			assertEquals(expected, table.fact(i), "i=" + i);
			assertEquals(1, (long) table.fact(i) * table.invFact(i) % MOD, "i=" + i);
		}
	}

	@ParameterizedTest
	@CsvSource({
		"10, 3, 120, 720",
		"5, 0, 1, 1",
		"5, 5, 1, 120",
	})
	void combinationsAndPermutations(final int n, final int r, final long nCr, final long nPr) {
		final FactorialTable table = new FactorialTable(0, MOD);
		assertEquals(nCr, table.nCr(n, r));
		assertEquals(nPr, table.nPr(n, r));
	}
}
