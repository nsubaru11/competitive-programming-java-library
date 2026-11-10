package unit.ds.sparsetable;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.ds.sparsetable.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class SparseTableTest extends SparseTableContract {

	Target create(final long[] a, final boolean max) {
		final BinaryOperator<Long> op = max ? Math::max : Math::min;
		return Adapter.of(Target.class, new SparseTable<>(Arrays.stream(a).boxed().toArray(Long[]::new), op));
	}

	// 冪等な演算なら最小値・最大値以外でも使える
	@Test
	void gcdOperator() {
		final SparseTable<Integer> t = new SparseTable<>(new Integer[]{12, 18, 30, 7}, (x, y) -> y == 0 ? x : java.math.BigInteger.valueOf(x).gcd(java.math.BigInteger.valueOf(y)).intValue());
		assertEquals(6, t.query(0, 3));
		assertEquals(1, t.query(2, 4));
	}
}
