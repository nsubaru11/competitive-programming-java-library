package example.math.number;

import lib.math.number.*;

public final class ModIntExample {

	public static void main(final String[] args) {
		// 生成（値は [0, mod) で渡す）
		ModInt a = new ModInt(5, 7);
		ModInt b = new ModInt(3, 7);
		System.out.println(a);
		System.out.println(b);
		System.out.println();

		// 四則演算
		System.out.println(a.add(b));
		System.out.println(a.sub(b));
		System.out.println(a.mul(b));
		System.out.println(a.div(b));
		System.out.println();

		// 累乗
		System.out.println(a.pow(6));
		System.out.println(new ModInt(2, 1_000_000_007).pow(100));
		System.out.println();

		// 既定の法
		ModInt c = new ModInt(10);
		System.out.println(c.mod());
		System.out.println(c.div(new ModInt(3)).value());
	}
}
