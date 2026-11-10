package unit.math.number;

import lib.math.number.*;

import unit.support.*;

final class ModLongTest extends ModContract {

	Target create(final long value, final long mod) {
		return Adapter.of(Target.class, new ModLong(value, mod));
	}

	// 積が long に収まる範囲の素数
	long[] mods() {
		return new long[]{7, 998244353, 1_000_000_007, 2_147_483_647};
	}
}
