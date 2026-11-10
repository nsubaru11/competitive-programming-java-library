package example.math.polynomial;

import java.util.*;

import lib.math.polynomial.*;

public final class TransformExample {

	public static void main(final String[] args) {
		// NTT と逆変換
		int mod = 998244353;
		int[] f = Transform.ntt(new int[]{1, 2, 3, 4}, false, mod);
		System.out.println(Arrays.toString(f));
		System.out.println(Arrays.toString(Transform.ntt(f, true, mod)));
		System.out.println();

		// 長さの指定（2冪へ0埋め）
		System.out.println(Transform.ntt(new int[]{1, 2, 3}, 5, false, mod).length);
		System.out.println();

		// アダマール変換
		long[] h = Transform.fwht(new int[]{1, 2, 3, 4}, false);
		System.out.println(Arrays.toString(h));
		System.out.println(Arrays.toString(Transform.fwht(h, true)));
		System.out.println();

		// 部分集合のゼータ・メビウス変換
		long[] z = Transform.subsetZeta(new int[]{1, 2, 3, 4});
		System.out.println(Arrays.toString(z));
		System.out.println(Arrays.toString(Transform.subsetMobius(z)));
	}
}
