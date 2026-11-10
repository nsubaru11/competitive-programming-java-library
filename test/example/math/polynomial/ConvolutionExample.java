package example.math.polynomial;

import java.util.*;

import lib.math.polynomial.*;

public final class ConvolutionExample {

	public static void main(final String[] args) {
		// 係数
		int[] a = {1, 2, 3};
		int[] b = {4, 5, 6};

		// NTT
		System.out.println(Arrays.toString(Convolution.convolveNtt(a, b, 998244353)));
		System.out.println();

		// 任意の法
		System.out.println(Arrays.toString(Convolution.convolveArbitraryMod(a, b, 1_000_000_007)));
		long[] big = {1_000_000_000L, 999_999_999L};
		System.out.println(Arrays.toString(Convolution.convolveArbitraryMod(big, big, 1_000_000_007L)));
		System.out.println();

		// 添字の XOR・AND・OR
		int[] c = {1, 2, 3, 4};
		int[] d = {5, 6, 7, 8};
		System.out.println(Arrays.toString(Convolution.convolveXor(c, d)));
		System.out.println(Arrays.toString(Convolution.convolveAnd(c, d)));
		System.out.println(Arrays.toString(Convolution.convolveOr(c, d)));
		System.out.println();

		// 法付き
		System.out.println(Arrays.toString(Convolution.convolveXor(c, d, 7)));
	}
}
