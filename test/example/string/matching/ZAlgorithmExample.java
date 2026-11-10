package example.string.matching;

import java.util.*;

import lib.string.matching.*;

public final class ZAlgorithmExample {

	public static void main(final String[] args) {
		// Z 配列
		System.out.println(Arrays.toString(ZAlgorithm.getZArray("aabcaabxaaaz")));
		System.out.println(Arrays.toString(ZAlgorithm.getZArray("abababa".toCharArray())));
		System.out.println();

		// パターン検索
		String pattern = "aba";
		String text = "abacabab";
		int[] z = ZAlgorithm.getZArray(pattern + "$" + text);
		for (int i = pattern.length() + 1; i < z.length; i++) {
			if (z[i] >= pattern.length()) {
				System.out.println(i - pattern.length() - 1);
			}
		}
	}
}
