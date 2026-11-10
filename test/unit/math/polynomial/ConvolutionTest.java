package unit.math.polynomial;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.math.polynomial.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 仕様は docs/math/polynomial/ConvolutionArchitecture.md に従う。FFT は未実装のため検査しない。
 */
final class ConvolutionTest {

	private static final int MOD = 998244353, MOD7 = 1_000_000_007;

	/**
	 * 4つのオーバーロード（long・long+mod・int・int+mod）を愚直解と比べる。
	 * pow2 が true なら返却長は最大入力長以上の最小の2の冪、false なら最大入力長である。
	 */
	private static void check(final String at, final long[] a, final long[] b, final IntBinaryOperator index, final boolean pow2,
							  final BinaryOperator<long[]> plain, final LongMod withMod, final IntPlain intPlain, final IntMod intMod) {
		int len = Math.max(a.length, b.length);
		if (pow2) {
			int p = 1;
			while (p < len) p <<= 1;
			len = p;
		}
		final long[] expected = new long[len];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				final int k = index.applyAsInt(i, j);
				if (k < len) expected[k] += a[i] * b[j];
			}
		}
		final long[] expectedMod = Arrays.stream(expected).map(x -> x % MOD).toArray();
		assertArrayEquals(expected, plain.apply(a, b), at);
		assertArrayEquals(expectedMod, withMod.apply(a, b, MOD), at + ", mod");
		assertArrayEquals(expected, intPlain.apply(toInt(a), toInt(b)), at + ", int");
		assertArrayEquals(toInt(expectedMod), intMod.apply(toInt(a), toInt(b), MOD), at + ", int mod");
	}

	private static long[] naive(final long[] a, final long[] b, final long mod) {
		final long[] res = new long[a.length + b.length - 1];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < b.length; j++) res[i + j] = (res[i + j] + a[i] % mod * (b[j] % mod)) % mod;
		return res;
	}

	private static int[] toInt(final long[] a) {
		return Arrays.stream(a).mapToInt(Math::toIntExact).toArray();
	}

	private static int gcd(final int a, final int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	private static int lcm(final int a, final int b) {
		return a / gcd(a, b) * b;
	}

	interface LongMod {
		long[] apply(long[] a, long[] b, long mod);
	}

	interface IntMod {
		int[] apply(int[] a, int[] b, int mod);
	}

	interface IntPlain {
		long[] apply(int[] a, int[] b);
	}

	@Nested
	class Polynomial {

		@Test
		void smallExample() {
			assertArrayEquals(new int[]{4, 13, 22, 15}, Convolution.convolveNtt(new int[]{1, 2, 3}, new int[]{4, 5}, MOD));
			assertArrayEquals(new long[]{4, 13, 22, 15}, Convolution.convolveNtt(new long[]{1, 2, 3}, new long[]{4, 5}, MOD));
		}

		@RepeatedTest(50)
		void nttAndArbitraryModMatchNaive(final RepetitionInfo info) {
			final Random random = new Random(info.getCurrentRepetition());
			final long[] a = random.longs(1 + random.nextInt(40), 0, MOD7).toArray(), b = random.longs(1 + random.nextInt(40), 0, MOD7).toArray();
			final String at = "seed=" + info.getCurrentRepetition();
			final long[] am = Arrays.stream(a).map(x -> x % MOD).toArray(), bm = Arrays.stream(b).map(x -> x % MOD).toArray();
			assertArrayEquals(naive(am, bm, MOD), Convolution.convolveNtt(am, bm, MOD), at);
			assertArrayEquals(toInt(naive(am, bm, MOD)), Convolution.convolveNtt(toInt(am), toInt(bm), MOD), at);
			assertArrayEquals(naive(a, b, MOD7), Convolution.convolveArbitraryMod(a, b, MOD7), at);
			assertArrayEquals(toInt(naive(a, b, MOD7)), Convolution.convolveArbitraryMod(toInt(a), toInt(b), MOD7), at);
		}
	}

	@Nested
	class BitwiseAndNumberTheoretic {

		@RepeatedTest(50)
		void matchNaive(final RepetitionInfo info) {
			final Random random = new Random(info.getCurrentRepetition());
			final long[] a = random.longs(1 + random.nextInt(20), 0, 1000).toArray(), b = random.longs(1 + random.nextInt(20), 0, 1000).toArray();
			final String at = "seed=" + info.getCurrentRepetition() + ", a=" + Arrays.toString(a) + ", b=" + Arrays.toString(b);
			check(at + ", xor", a, b, (i, j) -> i ^ j, true, Convolution::convolveXor, Convolution::convolveXor, Convolution::convolveXor, Convolution::convolveXor);
			check(at + ", and", a, b, (i, j) -> i & j, true, Convolution::convolveAnd, Convolution::convolveAnd, Convolution::convolveAnd, Convolution::convolveAnd);
			check(at + ", or", a, b, (i, j) -> i | j, true, Convolution::convolveOr, Convolution::convolveOr, Convolution::convolveOr, Convolution::convolveOr);
			// GCD / LCM は添字 t が整数 t + 1 を表す
			check(at + ", gcd", a, b, (i, j) -> gcd(i + 1, j + 1) - 1, false, Convolution::convolveGcd, Convolution::convolveGcd, Convolution::convolveGcd, Convolution::convolveGcd);
			check(at + ", lcm", a, b, (i, j) -> lcm(i + 1, j + 1) - 1, false, Convolution::convolveLcm, Convolution::convolveLcm, Convolution::convolveLcm, Convolution::convolveLcm);
		}
	}
}
