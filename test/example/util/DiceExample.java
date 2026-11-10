package example.util;

import lib.util.*;

public final class DiceExample {

	public static void main(final String[] args) {
		// 上・下・前・後・左・右
		Dice dice = new Dice(1, 6, 2, 5, 4, 3);
		print(dice);
		System.out.println();

		// 上面を右面へ
		dice.rotateUR();
		print(dice);
		System.out.println();

		// 前面を右面へ
		dice.rotateFR();
		print(dice);
		System.out.println();

		// 上面を前面へ
		dice.rotateUF();
		print(dice);
		System.out.println();

		// 既定の配置
		print(new Dice());
	}

	private static void print(final Dice d) {
		System.out.println(d.u() + " " + d.d() + " " + d.f() + " " + d.b() + " " + d.l() + " " + d.r());
	}
}
