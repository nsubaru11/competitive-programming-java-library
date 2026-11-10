package unit.ds.arrays;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntCircularArray・LongCircularArray に共通する振る舞いのテスト。値は long で扱う。
 */
abstract class CircularArrayContract {

	/**
	 * 左へ d だけずらす。d が負なら右へずらす。
	 */
	private static void rotate(final long[] a, final int d) {
		final int n = a.length, s = Math.floorMod(d, n);
		final long[] b = new long[n];
		for (int i = 0; i < n; i++) b[i] = a[(i + s) % n];
		System.arraycopy(b, 0, a, 0, n);
	}

	abstract Target create(int n);

	abstract Target create(int n, long v);

	abstract Target create(long[] a);

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(10);
		final long[] naive = new long[n];
		for (int i = 0; i < n; i++) naive[i] = random.nextInt(1000);
		final Target c = create(naive.clone());
		final long[] rotation = {0};
		new RandomOperations()
				.add("rotateLeft", (r, at) -> {
					final int d = r.nextInt(-25, 26);
					c.rotateLeft(d);
					rotation[0] += d;
					rotate(naive, d);
					assertEquals(rotation[0], c.rotation(), at);
				})
				.add("rotateRight", (r, at) -> {
					final int d = r.nextInt(-25, 26);
					c.rotateRight(d);
					rotation[0] -= d;
					rotate(naive, -d);
					assertEquals(rotation[0], c.rotation(), at);
				})
				.add("set", (r, at) -> {
					final int i = r.nextInt(n);
					final long v = r.nextInt(1000);
					assertEquals(naive[i], c.set(i, v), at);
					naive[i] = v;
				})
				.add("reverse", (r, at) -> {
					final int l = r.nextInt(n + 1), rr = l + r.nextInt(n - l + 1);
					c.reverse(l, rr);
					for (int i = l, j = rr - 1; i < j; i++, j--) {
						final long t = naive[i];
						naive[i] = naive[j];
						naive[j] = t;
					}
				})
				.add("get", (r, at) -> {
					final int i = r.nextInt(n);
					assertEquals(naive[i], c.get(i), at);
				})
				.add("toArray", (r, at) -> assertArrayEquals(naive, c.toArray(), at))
				.run(seed, random, 300);
	}

	interface Target {
		Object self();

		Target copy();

		Target fromCollection();

		int size();

		long rotation();

		long get(int i);

		long set(int i, long v);

		Object fill(long v);

		Object setAll(IntToLongFunction init);

		Object swap(int i, int j);

		Object reverse();

		Object reverse(int l, int r);

		Object rotateLeft();

		Object rotateLeft(int distance);

		Object rotateRight();

		Object rotateRight(int distance);

		Object resetRotation();

		long[] toArray();

		Iterator<? extends Number> iterator();
	}

	@Nested
	class Construction {

		@Test
		void fromSizeAndValue() {
			assertArrayEquals(new long[3], create(3).toArray());
			assertArrayEquals(new long[]{7, 7, 7}, create(3, 7).toArray());
			assertEquals(0L, create(3, 7).rotation());
		}

		@Test
		void fromArrayCollectionAndCopy() {
			final long[] a = {1, 2, 3, 4};
			final Target c = create(a);
			c.rotateLeft();
			assertArrayEquals(new long[]{2, 3, 4, 1}, c.fromCollection().toArray());
			final Target copy = c.copy();
			assertEquals(1L, copy.rotation());
			copy.rotateLeft();
			copy.set(0, 9);
			assertArrayEquals(new long[]{2, 3, 4, 1}, c.toArray());
			assertArrayEquals(new long[]{9, 4, 1, 2}, copy.toArray());
		}

		@Test
		void arrayIsCopied() {
			final long[] a = {1, 2, 3};
			final Target c = create(a);
			a[0] = 9;
			assertEquals(1L, c.get(0));
		}
	}

	@Nested
	class State {

		@Test
		void rotationIsTotalWithoutModulo() {
			final Target c = create(new long[]{1, 2, 3});
			c.rotateLeft(5);
			c.rotateRight();
			c.rotateLeft();
			c.rotateRight(7);
			assertEquals(-2L, c.rotation());
			assertEquals(3, c.size());
		}
	}

	@Nested
	class Access {

		@Test
		void getAndSetFollowRotation() {
			final Target c = create(new long[]{1, 2, 3, 4});
			c.rotateLeft();
			assertEquals(2L, c.get(0));
			assertEquals(1L, c.get(3));
			assertEquals(1L, c.set(3, 9), "set は更新前の値");
			assertArrayEquals(new long[]{2, 3, 4, 9}, c.toArray());
		}

		@Test
		void outOfRangeThrows() {
			final Target c = create(new long[]{1, 2});
			assertThrows(IndexOutOfBoundsException.class, () -> c.get(2));
			assertThrows(IndexOutOfBoundsException.class, () -> c.set(-1, 0));
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillAndSetAllResetRotation() {
			final Target c = create(new long[]{1, 2, 3});
			c.rotateLeft();
			assertSame(c.self(), c.fill(5));
			assertEquals(0L, c.rotation());
			assertArrayEquals(new long[]{5, 5, 5}, c.toArray());
			c.rotateRight();
			assertSame(c.self(), c.setAll(i -> 10L * i));
			assertEquals(0L, c.rotation());
			assertArrayEquals(new long[]{0, 10, 20}, c.toArray());
		}
	}

	@Nested
	class Transformation {

		@Test
		void rotateBothWays() {
			final Target c = create(new long[]{1, 2, 3, 4});
			assertSame(c.self(), c.rotateLeft());
			assertArrayEquals(new long[]{2, 3, 4, 1}, c.toArray());
			assertSame(c.self(), c.rotateRight());
			assertSame(c.self(), c.rotateRight());
			assertArrayEquals(new long[]{4, 1, 2, 3}, c.toArray());
			assertSame(c.self(), c.rotateLeft(6));
			assertArrayEquals(new long[]{2, 3, 4, 1}, c.toArray());
			assertSame(c.self(), c.rotateRight(5));
			assertArrayEquals(new long[]{1, 2, 3, 4}, c.toArray());
			// 負の回転数は逆向き
			c.rotateLeft(-1);
			assertArrayEquals(new long[]{4, 1, 2, 3}, c.toArray());
			c.rotateRight(-1);
			assertArrayEquals(new long[]{1, 2, 3, 4}, c.toArray());
		}

		@Test
		void resetRotation() {
			final Target c = create(new long[]{1, 2, 3});
			c.rotateLeft(2);
			assertSame(c.self(), c.resetRotation());
			assertEquals(0L, c.rotation());
			assertArrayEquals(new long[]{1, 2, 3}, c.toArray());
		}

		@Test
		void swapAndReverseAfterRotation() {
			final Target c = create(new long[]{1, 2, 3, 4, 5});
			c.rotateLeft(2);
			assertSame(c.self(), c.swap(0, 4));
			assertArrayEquals(new long[]{2, 4, 5, 1, 3}, c.toArray());
			assertSame(c.self(), c.reverse(1, 4));
			assertArrayEquals(new long[]{2, 1, 5, 4, 3}, c.toArray());
			assertSame(c.self(), c.reverse());
			assertArrayEquals(new long[]{3, 4, 5, 1, 2}, c.toArray());
		}
	}

	@Nested
	class Conversion {

		@Test
		void iteratorAndToStringFollowRotation() {
			final Target c = create(new long[]{1, 2, 3});
			c.rotateRight();
			final Iterator<? extends Number> it = c.iterator();
			for (final long v : new long[]{3, 1, 2}) assertEquals(v, it.next().longValue());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
			assertEquals("3 1 2", c.toString());
		}
	}
}
