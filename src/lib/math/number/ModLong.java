package lib.math.number;

/**
 * {@code long} で剰余値と法を保持するイミュータブルな数値です。
 * 二項演算では両辺の法が等しく、演算途中の積が {@code long} に収まることを前提とします。
 */
@SuppressWarnings("unused")
public final class ModLong extends Number {
	public final long value;
	public final long mod;

	/**
	 * 指定した値と法で作成します。
	 *
	 * @param value 保持する剰余値
	 * @param mod   法
	 */
	public ModLong(final long value, final long mod) {
		this.value = value;
		this.mod = mod;
	}

	/**
	 * 保持する剰余値を返します。
	 */
	public long value() {
		return value;
	}

	/**
	 * 法を返します。
	 */
	public long mod() {
		return mod;
	}

	/**
	 * 加算結果を返します。
	 */
	public ModLong add(final ModLong other) {
		return new ModLong((value + other.value) % mod, mod);
	}

	/**
	 * 減算結果を返します。
	 */
	public ModLong sub(final ModLong other) {
		return new ModLong((value - other.value + mod) % mod, mod);
	}

	/**
	 * 乗算結果を返します。
	 */
	public ModLong mul(final ModLong other) {
		return new ModLong(value * other.value % mod, mod);
	}

	/**
	 * Fermat の小定理による除算結果を返します。法は素数を前提とします。
	 */
	public ModLong div(final ModLong other) {
		return new ModLong(value * modPow(other.value, mod - 2) % mod, mod);
	}

	/**
	 * 非負整数乗を返します。
	 */
	public ModLong pow(final int exp) {
		return new ModLong(modPow(value, exp), mod);
	}

	private long modPow(long n, long k) {
		long res = 1;
		for (n %= mod; k > 0; k >>= 1, n = n * n % mod) {
			if ((k & 1) == 1) res = res * n % mod;
		}
		return res;
	}

	@Override
	public int intValue() {
		return (int) value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public boolean equals(final Object obj) {
		return this == obj || obj instanceof ModLong other && value == other.value && mod == other.mod;
	}

	@Override
	public int hashCode() {
		int result = Long.hashCode(value);
		result = 31 * result + Long.hashCode(mod);
		return result;
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}
}
