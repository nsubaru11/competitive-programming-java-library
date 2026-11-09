package unit.ds.fenwick;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.ds.*;
import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntBITTest extends PointBITContract {

	Target create(final int n) {
		return new Adapter(new IntBIT(n));
	}

	Target create(final long[] a) {
		return new Adapter(new IntBIT(Arrays.stream(a).mapToInt(Math::toIntExact).toArray()));
	}

	long bound() {
		return 1000;
	}

	@Test
	void sumIsLong() {
		final IntBIT bit = new IntBIT(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE});
		assertEquals(2L * Integer.MAX_VALUE, bit.sumAll());
		assertEquals(2L * Integer.MAX_VALUE, bit.sum(1));
		assertEquals(2L * Integer.MAX_VALUE, bit.sum(0, 1));
		assertEquals(1, bit.lowerBound(Integer.MAX_VALUE + 1L));
	}

	private record Adapter(IntBIT bit) implements Target {
		public Object self() {
			return bit;
		}

		public Target copy() {
			return new Adapter(new IntBIT(bit));
		}

		public Target fromCollection() {
			return new Adapter(new IntBIT((IntCollection) bit));
		}

		public int size() {
			return bit.size();
		}

		public long get(final int i) {
			return bit.get(i);
		}

		public long set(final int i, final long v) {
			return bit.set(i, Math.toIntExact(v));
		}

		public long add(final int i, final long d) {
			return bit.add(i, Math.toIntExact(d));
		}

		public long multiply(final int i, final long m) {
			return bit.multiply(i, Math.toIntExact(m));
		}

		public long affine(final int i, final long m, final long d) {
			return bit.affine(i, Math.toIntExact(m), Math.toIntExact(d));
		}

		public long apply(final int i, final long x, final LongBinaryOperator f) {
			return bit.apply(i, Math.toIntExact(x), (a, b) -> Math.toIntExact(f.applyAsLong(a, b)));
		}

		public Object fill(final long v) {
			return bit.fill(Math.toIntExact(v));
		}

		public Object setAll(final IntToLongFunction init) {
			return bit.setAll(i -> Math.toIntExact(init.applyAsLong(i)));
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
			return Arrays.stream(bit.toArray()).asLongStream().toArray();
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
