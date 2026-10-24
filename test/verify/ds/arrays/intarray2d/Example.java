package verify.ds.arrays.intarray2d;

import lib.ds.arrays.*;

public final class Example {

	public static void main(final String[] args) {
		int[][] a = {{11, 12, 13, 14}, {21, 22, 23, 24}, {31, 32, 33, 34}};
		IntArray2D ia2d = new IntArray2D(a);
		System.out.println(ia2d);
		ia2d.rotateLeft();
		System.out.println(ia2d);
		ia2d.transpose();
		System.out.println(ia2d);
	}
}
