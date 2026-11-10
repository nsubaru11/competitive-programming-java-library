package example.math.number;

import lib.math.number.*;

public final class Int128Example {

	public static void main(final String[] args) {
		// 生成
		String s = "123456789012345678901234567890";
		Int128 b = Int128.of(Long.MAX_VALUE);
		System.out.println(Int128.of(s));
		System.out.println(b);
		System.out.println();

		// 四則演算（呼び出し元を更新して返す）
		System.out.println(Int128.of(s).add(b));
		System.out.println(Int128.of(s).sub(b));
		System.out.println(Int128.of(Long.MAX_VALUE).mul(b));
		System.out.println(Int128.of(s).div(b));
		System.out.println();

		// 更新の連鎖
		Int128 x = Int128.of(10);
		x.mul(Int128.of(10)).add(Int128.of(5));
		System.out.println(x);
		System.out.println();

		// 比較・判定
		System.out.println(Int128.of(s).compareTo(b));
		System.out.println(Int128.of(-3).isNegative());
		System.out.println(Int128.of(0).isZero());
		System.out.println();

		// 変換
		System.out.println(Int128.of(s).div(b).longValue());
		System.out.println(Int128.unsignedMultiplyHigh(-1L, -1L));
	}
}
