@SuppressWarnings("unused")
public final class ModLong {
	private final long inv2;
	private final long value, mod;

	public ModLong(final long value, final long mod) {
		this.value = value;
		this.mod = mod;
		inv2 = modPow(2, mod - 2);
	}

	public ModLong add(final ModLong other) {
		return new ModLong((value + other.value) % mod, mod);
	}

	public ModLong sub(final ModLong other) {
		return new ModLong((value - other.value + mod) % mod, mod);
	}

	public ModLong mul(final ModLong other) {
		return new ModLong(value * other.value % mod, mod);
	}

	public ModLong div(final ModLong other) {
		if (other.value == 2) return new ModLong(value * inv2 % mod, mod);
		return new ModLong(value * modPow(other.value, mod - 2) % mod, mod);
	}

	public ModLong pow(final int exp) {
		return new ModLong(modPow(value, exp), mod);
	}

	private long modPow(long n, long k) {
		long res = 1;
		for (n %= mod, k %= mod; k > 0; k >>= 1, n = n * n % mod) {
			if ((k & 1) == 1) res = res * n % mod;
		}
		return res;
	}

	public long value() {
		return value;
	}

	public long mod() {
		return mod;
	}

	public String toString() {
		return value + "";
	}
}
