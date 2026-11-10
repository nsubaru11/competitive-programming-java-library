package example.string.suffix;

import lib.string.suffix.*;

public final class SuffixArrayExample {

	public static void main(final String[] args) {
		// 構築
		SuffixArray sa = new SuffixArray("mississippi");

		// 部分文字列の判定
		System.out.println(sa.contains("issi"));
		System.out.println(sa.contains("ppi"));
		System.out.println();

		// 含まれないとき
		System.out.println(sa.contains("pis"));
		System.out.println(sa.contains("mississippis"));
	}
}
