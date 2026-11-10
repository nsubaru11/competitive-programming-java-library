package example.ds.arrays;

import lib.ds.arrays.*;

public final class IntCircularArrayExample {

	public static void main(final String[] args) {
		// 初期化
		IntCircularArray a = new IntCircularArray(new int[]{1, 2, 3, 4, 5});
		System.out.println(a);
		System.out.println();

		// 左回転
		a.rotateLeft(2);
		System.out.println(a);
		System.out.println(a.rotation());
		System.out.println();

		// 右回転
		a.rotateRight();
		System.out.println(a);
		System.out.println(a.rotation());
		System.out.println();

		// 取得・更新
		a.set(0, 10);
		System.out.println(a.get(0));
		System.out.println(a.getLast());
		System.out.println(a);
		System.out.println();

		// 回転の解除
		a.resetRotation();
		System.out.println(a);
	}
}
