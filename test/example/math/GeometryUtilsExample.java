package example.math;

import lib.math.*;

public final class GeometryUtilsExample {

	public static void main(final String[] args) {
		// 線分の交差
		System.out.println(GeometryUtils.crossLine(0, 0, 2, 2, 0, 2, 2, 0));
		System.out.println(GeometryUtils.crossLine(0, 0, 2, 0, 0, 1, 2, 1));
		System.out.println();

		// 長方形の交差
		System.out.println(GeometryUtils.crossRect(0, 0, 2, 2, 1, 1, 3, 3));
		System.out.println(GeometryUtils.crossRect(0, 0, 1, 1, 2, 2, 3, 3));
		System.out.println();

		// 距離
		System.out.println(GeometryUtils.euclidDist(0, 0, 3, 4));
		System.out.println(GeometryUtils.manhattanDist(0, 0, 3, 4));
		System.out.println(GeometryUtils.chebyshevDist(0, 0, 3, 4));
	}
}
