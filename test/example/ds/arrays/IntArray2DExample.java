package example.ds.arrays;

import lib.ds.arrays.*;

public final class IntArray2DExample {

	public static void main(final String[] args) {
		// サンプルデータ
		int[][] a = {
				{11, 12, 13, 14},
				{21, 22, 23, 24},
				{31, 32, 33, 34}
		};

		// 初期化
		IntArray2D ia2d = new IntArray2D(a);
		System.out.println(ia2d);
		System.out.println();

		// 左回転
		ia2d.rotateLeft();
		System.out.println(ia2d);
		System.out.println();

		// 右回転
		ia2d.rotateRight();
		System.out.println(ia2d);
		System.out.println();

		// 転置
		ia2d.transpose();
		System.out.println(ia2d);
	}
}
