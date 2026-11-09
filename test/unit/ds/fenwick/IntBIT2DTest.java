package unit.ds.fenwick;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntBIT2DTest extends BIT2DContract {

	Target create(final int h, final int w) {
		return new Adapter(new IntBIT2D(h, w));
	}

	Target create(final long[][] a) {
		final int[][] b = new int[a.length][];
		for (int i = 0; i < a.length; i++) b[i] = Arrays.stream(a[i]).mapToInt(Math::toIntExact).toArray();
		return new Adapter(new IntBIT2D(b));
	}

	long bound() {
		return 1000;
	}

	@Test
	void sumIsLong() {
		final IntBIT2D bit = new IntBIT2D(new int[][]{{Integer.MAX_VALUE}, {Integer.MAX_VALUE}});
		assertEquals(2L * Integer.MAX_VALUE, bit.sumAll());
		assertEquals(2L * Integer.MAX_VALUE, bit.sum(1, 0));
		assertEquals(2L * Integer.MAX_VALUE, bit.sum(0, 0, 1, 0));
	}

	private record Adapter(IntBIT2D bit) implements Target {
		public Object self() {
			return bit;
		}

		public Target copy() {
			return new Adapter(new IntBIT2D(bit));
		}

		public int h() {
			return bit.h;
		}

		public int w() {
			return bit.w;
		}

		public int size() {
			return bit.size();
		}

		public long get(final int i, final int j) {
			return bit.get(i, j);
		}

		public long set(final int i, final int j, final long v) {
			return bit.set(i, j, Math.toIntExact(v));
		}

		public long add(final int i, final int j, final long d) {
			return bit.add(i, j, Math.toIntExact(d));
		}

		public long multiply(final int i, final int j, final long m) {
			return bit.multiply(i, j, Math.toIntExact(m));
		}

		public long affine(final int i, final int j, final long m, final long d) {
			return bit.affine(i, j, Math.toIntExact(m), Math.toIntExact(d));
		}

		public long apply(final int i, final int j, final long x, final LongBinaryOperator f) {
			return bit.apply(i, j, Math.toIntExact(x), (a, b) -> Math.toIntExact(f.applyAsLong(a, b)));
		}

		public Object fill(final long v) {
			return bit.fill(Math.toIntExact(v));
		}

		public Object setAll(final IntBinaryOperator init) {
			return bit.setAll(init);
		}

		public long sum(final int i, final int j) {
			return bit.sum(i, j);
		}

		public long sum(final int i1, final int j1, final int i2, final int j2) {
			return bit.sum(i1, j1, i2, j2);
		}

		public long sumAll() {
			return bit.sumAll();
		}

		public long[] toArray() {
			return Arrays.stream(bit.toArray()).asLongStream().toArray();
		}

		public long[][] toArray2D() {
			return Arrays.stream(bit.toArray2D()).map(row -> Arrays.stream(row).asLongStream().toArray()).toArray(long[][]::new);
		}

		public List<Long> forEachValue() {
			final List<Long> res = new ArrayList<>();
			bit.forEachInt(v -> res.add((long) v));
			return res;
		}

		public Iterator<? extends Number> iterator() {
			return bit.iterator();
		}

		public String toString() {
			return bit.toString();
		}
	}
}
