package unit.math.number;

import org.junit.jupiter.api.*;

import lib.math.number.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class ModIntTest extends ModContract {

	Target create(final long value, final long mod) {
		return Adapter.of(Target.class, new ModInt(Math.toIntExact(value), Math.toIntExact(mod)));
	}

	long[] mods() {
		return new long[]{7, 998244353, 1_000_000_007};
	}

	@Test
	void defaultMod() {
		assertEquals(998244353, new ModInt().mod);
		assertEquals(0, new ModInt().value);
		assertEquals(5, new ModInt(5).value());
		assertEquals(998244353, new ModInt(5).mod());
	}
}
