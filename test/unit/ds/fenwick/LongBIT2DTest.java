package unit.ds.fenwick;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongBIT2DTest extends BIT2DContract {

	Target create(final int h, final int w) {
		return new Adapter(new LongBIT2D(h, w));
	}

	Target create(final long[][] a) {
		return new Adapter(new LongBIT2D(a));
	}

	long bound() {
		return 1L << 40;
	}

	@Test
	void holdsValuesBeyondInt() {
		final long big = 1L << 40;
		final LongBIT2D bit = new LongBIT2D(new long[][]{{big, -big}, {big, big}});
		assertEquals(2 * big, bit.add(0, 0, big));
		assertEquals(3 * big, bit.sumAll());
		assertEquals(2 * big, bit.sum(0, 0, 1, 0) - bit.sum(0, 1));
	}

	private record Adapter(LongBIT2D bit) implements Target {
		public Object self() {
			return bit;
		}

		public Target copy() {
			return new Adapter(new LongBIT2D(bit));
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
			return bit.set(i, j, v);
		}

		public long add(final int i, final int j, final long d) {
			return bit.add(i, j, d);
		}

		public long multiply(final int i, final int j, final long m) {
			return bit.multiply(i, j, m);
		}

		public long affine(final int i, final int j, final long m, final long d) {
			return bit.affine(i, j, m, d);
		}

		public long apply(final int i, final int j, final long x, final LongBinaryOperator f) {
			return bit.apply(i, j, x, f);
		}

		public Object fill(final long v) {
			return bit.fill(v);
		}

		public Object setAll(final IntBinaryOperator init) {
			return bit.setAll(init::applyAsInt);
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
			return bit.toArray();
		}

		public long[][] toArray2D() {
			return bit.toArray2D();
		}

		public List<Long> forEachValue() {
			final List<Long> res = new ArrayList<>();
			bit.forEachLong(res::add);
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
