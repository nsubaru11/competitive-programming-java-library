package example.math;

import lib.math.*;

public final class FactorialTableExample {

	public static void main(final String[] args) {
		// 構築
		FactorialTable ft = new FactorialTable(100, 1_000_000_007);

		// 階乗・逆元
		System.out.println(ft.fact(10));
		System.out.println(ft.invFact(10));
		System.out.println(ft.inv(2));
		System.out.println();

		// 組み合わせ
		System.out.println(ft.nCr(10, 3));
		System.out.println(ft.nPr(10, 3));
		System.out.println(ft.nHr(3, 2));
		System.out.println();

		// 特殊な数
		System.out.println(ft.catalan(5));
		System.out.println(ft.narayana(4, 2));
		System.out.println();

		// 自動拡張
		System.out.println(ft.nCr(1000, 500));
	}
}
