package unit.util;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class DiceTest {

	private static int[] faces(final Dice d) {
		return new int[]{d.u(), d.d(), d.f(), d.b(), d.l(), d.r()};
	}

	@Nested
	class Construction {

		@Test
		void defaultAndExplicitFaces() {
			assertArrayEquals(new int[]{1, 6, 3, 4, 2, 5}, faces(new Dice()));
			assertArrayEquals(new int[]{10, 20, 30, 40, 50, 60}, faces(new Dice(10, 20, 30, 40, 50, 60)));
			assertArrayEquals(new int[]{10, 20, 30, 40, 50, 60}, new Dice(10, 20, 30, 40, 50, 60).dice(), "上、下、前、後、左、右の順");
		}
	}

	@Nested
	class Transformation {

		@Test
		void rotateUpToRight() {
			final Dice d = new Dice(1, 6, 3, 4, 2, 5);
			d.rotateUR();
			assertEquals(1, d.r(), "上面が右面へ");
			assertEquals(2, d.u(), "左面が上面へ");
			assertEquals(6, d.l());
			assertEquals(5, d.d());
			assertEquals(3, d.f(), "前後は変わらない");
			assertEquals(4, d.b());
		}

		@Test
		void rotateFrontToRight() {
			final Dice d = new Dice(1, 6, 3, 4, 2, 5);
			d.rotateFR();
			assertEquals(3, d.r(), "前面が右面へ");
			assertEquals(2, d.f());
			assertEquals(4, d.l());
			assertEquals(5, d.b());
			assertEquals(1, d.u(), "上下は変わらない");
			assertEquals(6, d.d());
		}

		@Test
		void rotateUpToFront() {
			final Dice d = new Dice(1, 6, 3, 4, 2, 5);
			d.rotateUF();
			assertEquals(1, d.f(), "上面が前面へ");
			assertEquals(4, d.u());
			assertEquals(6, d.b());
			assertEquals(3, d.d());
			assertEquals(2, d.l(), "左右は変わらない");
			assertEquals(5, d.r());
		}

		@Test
		void fourRotationsAreIdentityAndOppositeFacesStayOpposite() {
			final Random random = new Random(1);
			final Dice d = new Dice();
			for (int t = 0; t < 200; t++) {
				switch (random.nextInt(3)) {
					case 0 -> d.rotateUR();
					case 1 -> d.rotateFR();
					default -> d.rotateUF();
				}
				assertEquals(7, d.u() + d.d());
				assertEquals(7, d.f() + d.b());
				assertEquals(7, d.l() + d.r());
				assertEquals(Set.of(1, 2, 3, 4, 5, 6), Set.of(d.u(), d.d(), d.f(), d.b(), d.l(), d.r()));
			}
			final Dice e = new Dice();
			for (int k = 0; k < 4; k++) e.rotateUR();
			for (int k = 0; k < 4; k++) e.rotateFR();
			for (int k = 0; k < 4; k++) e.rotateUF();
			assertArrayEquals(faces(new Dice()), faces(e));
		}
	}
}
