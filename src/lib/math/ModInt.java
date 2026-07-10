package lib.math;

@SuppressWarnings("unused")
public final class ModInt {
	private final int value, mod;

	public ModInt() {
		this(0, 998244353);
	}

	public ModInt(final int value) {
		this(value, 998244353);
	}

	public ModInt(final int value, final int mod) {
		this.value = value;
		this.mod = mod;
	}

	public ModInt add(final ModInt other) {
		return new ModInt((value + other.value) % mod, mod);
	}

	public ModInt sub(final ModInt other) {
		return new ModInt((value - other.value + mod) % mod, mod);
	}

	public ModInt mul(final ModInt other) {
		return new ModInt((int) ((long) value * other.value % mod), mod);
	}

	public ModInt div(final ModInt other) {
		return new ModInt((int) ((long) value * modPow(other.value, mod - 2) % mod), mod);
	}

	public ModInt pow(final int exp) {
		return new ModInt(modPow(value, exp), mod);
	}

	private int modPow(int n, long k) {
		int res = 1;
		for (n %= mod; k > 0; k >>= 1, n = (int) ((long) n * n % mod)) {
			if ((k & 1) == 1) res = (int) ((long) res * n % mod);
		}
		return res;
	}

	public int value() {
		return value;
	}

	public int mod() {
		return mod;
	}

	public String toString() {
		return value + "";
	}
}
