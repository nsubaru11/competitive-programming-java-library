package unit.math.polynomial;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.math.polynomial.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 仕様は docs/math/polynomial/ConvolutionArchitecture.md に従う。FFT は未実装のため検査しない。
 */
final class TransformTest {

	private static final int MOD = 998244353;

	@Nested
	class Ntt {

		@Test
		void inverseRestoresInput() {
			final Random random = new Random(1);
			for (final int mod : new int[]{998244353, 167772161, 469762049, 1224736769, 7340033}) {
				final long[] a = random.longs(5, 0, mod).toArray();
				final long[] f = Transform.ntt(a, 8, false, mod);
				assertEquals(8, f.length, "len 以上の最小の2の冪へ0埋めする");
				final long[] back = Transform.ntt(f, true, mod);
				assertArrayEquals(Arrays.copyOf(a, 8), back, "mod=" + mod);
				final int[] ia = Arrays.stream(a).mapToInt(x -> (int) x).toArray();
				assertArrayEquals(Arrays.copyOf(ia, 8), Transform.ntt(Transform.ntt(ia, 5, false, mod), true, mod), "mod=" + mod);
			}
		}

		@Test
		void evaluatesPolynomialAtRootsOfUnity() {
			// 長さ1の NTT は恒等、全要素の和は x = 1 での値
			assertArrayEquals(new long[]{7}, Transform.ntt(new long[]{7}, false, MOD));
			final long[] f = Transform.ntt(new long[]{1, 2, 3, 4}, false, MOD);
			assertTrue(Arrays.stream(f).anyMatch(x -> x == 10), "x = 1 での値 10 を含む");
		}

		@Test
		void inPlaceRejectsInvalidLength() {
			assertFalse(Transform.nttInPlace(new int[3], false, MOD));
			assertFalse(Transform.nttInPlace(new long[0], false, MOD));
			// 7 - 1 = 6 は 4 で割り切れない
			assertFalse(Transform.nttInPlace(new long[4], false, 7));
			final int[] a = {1, 2, 3, 4};
			assertTrue(Transform.nttInPlace(a, false, MOD));
			assertTrue(Transform.nttInPlace(a, true, MOD));
			assertArrayEquals(new int[]{1, 2, 3, 4}, a);
		}

		@Test
		void inputIsNotModified() {
			final long[] a = {1, 2, 3};
			Transform.ntt(a, false, MOD);
			assertArrayEquals(new long[]{1, 2, 3}, a);
		}
	}

	@Nested
	class Walsh {

		@Test
		void forwardIsSumAndDifference() {
			assertArrayEquals(new long[]{10, -2, -4, 0}, Transform.fwht(new long[]{1, 2, 3, 4}, false));
			assertArrayEquals(new long[]{1, 2, 3, 4}, Transform.fwht(Transform.fwht(new long[]{1, 2, 3, 4}, false), true));
			assertArrayEquals(new long[]{10, MOD - 2, MOD - 4, 0}, Transform.fwht(new long[]{1, 2, 3, 4}, false, MOD));
			assertArrayEquals(new int[]{10, MOD - 2, MOD - 4, 0}, Transform.fwht(new int[]{1, 2, 3, 4}, false, MOD));
			assertArrayEquals(new long[]{10, -2, -4, 0}, Transform.fwht(new int[]{1, 2, 3, 4}, false));
			assertEquals(4, Transform.fwht(new long[]{1, 2, 3}, 3, false).length);
			assertFalse(Transform.fwhtInPlace(new long[3], false));
		}
	}

	@Nested
	class SetTransforms {

		@RepeatedTest(20)
		void zetaAndMobiusMatchDefinitions(final RepetitionInfo info) {
			final Random random = new Random(info.getCurrentRepetition());
			final int n = 1 << random.nextInt(5);
			final long[] a = random.longs(n, 0, 100).toArray();
			final long[] sub = new long[n], sup = new long[n];
			for (int s = 0; s < n; s++) {
				for (int t = 0; t < n; t++) {
					if ((t & s) == t) sub[s] += a[t];
					if ((t & s) == s) sup[s] += a[t];
				}
			}
			final String at = "a=" + Arrays.toString(a);
			assertArrayEquals(sub, Transform.subsetZeta(a), at);
			assertArrayEquals(a, Transform.subsetMobius(sub), at);
			assertArrayEquals(sup, Transform.supersetZeta(a), at);
			assertArrayEquals(a, Transform.supersetMobius(sup), at);
			assertArrayEquals(Arrays.stream(sub).map(x -> x % MOD).toArray(), Transform.subsetZeta(a, n, MOD), at);
			assertArrayEquals(Arrays.stream(sup).map(x -> x % MOD).toArray(), Transform.supersetZeta(a, n, MOD), at);
			final int[] ia = Arrays.stream(a).mapToInt(x -> (int) x).toArray();
			assertArrayEquals(sub, Transform.subsetZeta(ia), at);
			assertArrayEquals(sup, Transform.supersetZeta(ia), at);
			assertArrayEquals(ia, Transform.subsetMobius(Arrays.stream(sub).mapToInt(x -> (int) x).toArray(), n, MOD), at);
		}

		@RepeatedTest(20)
		void multipleAndDivisorMatchDefinitions(final RepetitionInfo info) {
			final Random random = new Random(info.getCurrentRepetition());
			final int n = 1 + random.nextInt(30);
			final long[] a = random.longs(n, 0, 100).toArray();
			// 添字 t が整数 t + 1 を表す
			final long[] mult = new long[n], div = new long[n];
			for (int d = 1; d <= n; d++) {
				for (int i = 1; i <= n; i++) {
					if (i % d == 0) mult[d - 1] += a[i - 1];
					if (d % i == 0) div[d - 1] += a[i - 1];
				}
			}
			final String at = "a=" + Arrays.toString(a);
			assertArrayEquals(mult, Transform.multipleZeta(a), at);
			assertArrayEquals(a, Transform.multipleMobius(mult), at);
			assertArrayEquals(div, Transform.divisorZeta(a), at);
			assertArrayEquals(a, Transform.divisorMobius(div), at);
			assertArrayEquals(Arrays.stream(mult).map(x -> x % MOD).toArray(), Transform.multipleZeta(a, (long) MOD), at);
			assertArrayEquals(Arrays.stream(div).map(x -> x % MOD).toArray(), Transform.divisorZeta(a, (long) MOD), at);
			assertEquals(n + 3, Transform.multipleZeta(a, n + 3).length, "コピー版は len をそのまま返却長にする");
			final int[] ia = Arrays.stream(a).mapToInt(x -> (int) x).toArray();
			assertArrayEquals(mult, Transform.multipleZeta(ia), at);
			assertArrayEquals(div, Transform.divisorZeta(ia), at);
			assertArrayEquals(ia, Transform.divisorMobius(Arrays.stream(div).mapToInt(x -> (int) x).toArray(), n, MOD), at);
		}
	}
}
