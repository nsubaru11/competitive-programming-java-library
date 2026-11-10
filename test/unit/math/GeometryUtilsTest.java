package unit.math;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GeometryUtils の判定は、Javadoc の区分が一意に決まる配置だけを検査する。
 */
final class GeometryUtilsTest {

	@Nested
	class Search {

		@ParameterizedTest(name = "{8}")
		@CsvSource({
			"0, 0, 2, 2, 0, 2, 2, 0, 2",
			"0, 0, 2, 0, 1, -1, 2, 2, 1",
			"0, 0, 1, 1, 0, 0, 1, 1, 0",
			"0, 0, 2, 0, 1, 0, 3, 0, -1",
			"0, 0, 1, 0, 1, 0, 2, 0, -2",
			"0, 0, 2, 0, 1, 0, 1, 2, -3",
			"0, 0, 2, 0, 1, 0, 2, 1, -4",
			"0, 0, 1, 0, 0, 1, 1, 1, -5",
			"0, 0, 1, 0, 3, -1, 3, 1, -6",
			"0, 0, 1, 0, 3, 1, 4, 3, -7",
		})
		void crossLine(final double x11, final double y11, final double x12, final double y12, final double x21, final double y21, final double x22, final double y22, final int expected) {
			assertEquals(expected, GeometryUtils.crossLine(x11, y11, x12, y12, x21, y21, x22, y22));
		}

		@Test
		void skewLinesIn3D() {
			assertEquals(-6, GeometryUtils.crossLine3D(0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 2, 1), "垂直なねじれの位置");
			assertEquals(-7, GeometryUtils.crossLine3D(0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 2, 1), "非垂直なねじれの位置");
		}

		@Test
		void crossRect() {
			assertEquals(0, GeometryUtils.crossRect(0, 0, 2, 2, 0, 0, 2, 2), "完全に等しい");
			assertEquals(-1, GeometryUtils.crossRect(0, 0, 4, 4, 1, 1, 2, 2), "一方が他方を含む");
			assertEquals(-1, GeometryUtils.crossRect(1, 1, 2, 2, 0, 0, 4, 4));
			assertEquals(-2, GeometryUtils.crossRect(0, 0, 1, 1, 1, 1, 2, 2), "頂点のみで接する");
			assertEquals(-3, GeometryUtils.crossRect(0, 0, 1, 1, 1, 0, 2, 1), "辺で接する");
			assertEquals(-4, GeometryUtils.crossRect(0, 0, 1, 1, 2, 2, 3, 3), "交差しない");
			final int partial = GeometryUtils.crossRect(0, 0, 2, 2, 1, 1, 3, 3);
			assertTrue(partial == 1 || partial == 2, "一部が重なる: " + partial);
		}

		@Test
		void containsPointInBox() {
			assertEquals(1, GeometryUtils.containsPoint(1, 1, 1, 0, 0, 0, 2, 2, 2), "内部");
			assertEquals(0, GeometryUtils.containsPoint(0, 1, 1, 0, 0, 0, 2, 2, 2), "面上");
			assertEquals(-1, GeometryUtils.containsPoint(0, 0, 1, 0, 0, 0, 2, 2, 2), "辺上");
			assertEquals(-2, GeometryUtils.containsPoint(2, 2, 2, 0, 0, 0, 2, 2, 2), "頂点上");
			assertEquals(-3, GeometryUtils.containsPoint(3, 1, 1, 0, 0, 0, 2, 2, 2), "外部");
		}
	}

	@Nested
	class Aggregation {

		@Test
		void distancesMatchDefinitions() {
			final Random random = new Random(1);
			for (int t = 0; t < 200; t++) {
				final double[] p = random.doubles(5, -100, 100).toArray(), q = random.doubles(5, -100, 100).toArray();
				final double dx = p[0] - q[0], dy = p[1] - q[1], dz = p[2] - q[2];
				assertEquals(Math.hypot(dx, dy), GeometryUtils.euclidDist(p[0], p[1], q[0], q[1]), 1e-9);
				assertEquals(Math.sqrt(dx * dx + dy * dy + dz * dz), GeometryUtils.euclidDist3D(p[0], p[1], p[2], q[0], q[1], q[2]), 1e-9);
				assertEquals(Math.abs(dx) + Math.abs(dy), GeometryUtils.manhattanDist(p[0], p[1], q[0], q[1]), 1e-9);
				assertEquals(Math.abs(dx) + Math.abs(dy) + Math.abs(dz), GeometryUtils.manhattanDist3D(p[0], p[1], p[2], q[0], q[1], q[2]), 1e-9);
				assertEquals(Math.max(Math.abs(dx), Math.abs(dy)), GeometryUtils.chebyshevDist(p[0], p[1], q[0], q[1]), 1e-9);
				assertEquals(Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz))), GeometryUtils.chebyshevDist3D(p[0], p[1], p[2], q[0], q[1], q[2]), 1e-9);
				double e = 0, m = 0, c = 0;
				for (int i = 0; i < 5; i++) {
					final double d = Math.abs(p[i] - q[i]);
					e += d * d;
					m += d;
					c = Math.max(c, d);
				}
				assertEquals(Math.sqrt(e), GeometryUtils.euclidDistN(5, p, q), 1e-9);
				assertEquals(m, GeometryUtils.manhattanDistN(5, p, q), 1e-9);
				assertEquals(c, GeometryUtils.chebyshevDistN(5, p, q), 1e-9);
			}
		}
	}
}
