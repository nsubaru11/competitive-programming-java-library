package unit.ds.fenwick;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.ds.*;
import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongBITTest extends PointBITContract {

	Target create(final int n) {
		return new Adapter(new LongBIT(n));
	}

	Target create(final long[] a) {
		return new Adapter(new LongBIT(a));
	}

	long bound() {
		return 1L << 40;
	}

	@Test
	void holdsValuesBeyondInt() {
		final long big = 1L << 40;
		final LongBIT bit = new LongBIT(new long[]{big, big});
		assertEquals(big, bit.get(1));
		assertEquals(2 * big, bit.sumAll());
		assertEquals(1, bit.lowerBound(big + 1));
	}

	private record Adapter(LongBIT bit) implements Target {
		public Object self() {
			return bit;
		}

		public Target copy() {
			return new Adapter(new LongBIT(bit));
		}

		public Target fromCollection() {
			return new Adapter(new LongBIT((LongCollection) bit));
		}

		public int size() {
			return bit.size();
		}

		public long get(final int i) {
			return bit.get(i);
		}

		public long set(final int i, final long v) {
			return bit.set(i, v);
		}

		public long add(final int i, final long d) {
			return bit.add(i, d);
		}

		public long multiply(final int i, final long m) {
			return bit.multiply(i, m);
		}

		public long affine(final int i, final long m, final long d) {
			return bit.affine(i, m, d);
		}

		public long apply(final int i, final long x, final LongBinaryOperator f) {
			return bit.apply(i, x, f);
		}

		public Object fill(final long v) {
			return bit.fill(v);
		}

		public Object setAll(final IntToLongFunction init) {
			return bit.setAll(init);
		}

		public long sum(final int i) {
			return bit.sum(i);
		}

		public long sum(final int i, final int j) {
			return bit.sum(i, j);
		}

		public long sumAll() {
			return bit.sumAll();
		}

		public int lowerBound(final long w) {
			return bit.lowerBound(w);
		}

		public int upperBound(final long w) {
			return bit.upperBound(w);
		}

		public long[] toArray() {
			return bit.toArray();
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
