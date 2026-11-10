package example.math.number;

import lib.math.number.*;

public final class ModLongExample {

	public static void main(final String[] args) {
		// 生成（値は [0, mod) で渡す）
		long mod = 1_000_000_007L;
		ModLong a = new ModLong(123_456_789L, mod);
		ModLong b = new ModLong(987_654_321L, mod);
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
		System.out.println(new ModLong(2, mod).pow(100));
		System.out.println();

		// 値と法
		System.out.println(a.value());
		System.out.println(a.mod());
	}
}
