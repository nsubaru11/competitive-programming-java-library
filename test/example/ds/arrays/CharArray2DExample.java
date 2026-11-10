package example.ds.arrays;

import lib.ds.arrays.*;

public final class CharArray2DExample {

	public static void main(final String[] args) {
		// サンプルデータ
		char[][] a = {
				{'0', '1', '2', '3'},
				{'4', '5', '6', '7'},
				{'8', '9', 'a', 'b'}
		};

		// 初期化
		CharArray2D ia2d = new CharArray2D(a);
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
