package lib.math.number;

/**
 * {@code int} で剰余値と法を保持するイミュータブルな数値です。
 * 二項演算では両辺の法が等しく、法が正であることを前提とします。
 */
@SuppressWarnings("unused")
public final class ModInt extends Number {
	public final int value;
	public final int mod;

	/**
	 * 指定した値と法で作成します。
	 *
	 * @param value 保持する剰余値
	 * @param mod   法
	 */
	public ModInt(final int value, final int mod) {
		this.value = value;
		this.mod = mod;
	}

	/**
	 * 値 0、法 998244353 で作成します。
	 */
	public ModInt() {
		this(0, 998244353);
	}

	/**
	 * 指定値、法 998244353 で作成します。
	 *
	 * @param value 保持する値
	 */
	public ModInt(final int value) {
		this(value, 998244353);
	}

	/**
	 * 保持する剰余値を返します。
	 */
	public int value() {
		return value;
	}

	/**
	 * 法を返します。
	 */
	public int mod() {
		return mod;
	}

	/**
	 * 加算結果を返します。
	 */
	public ModInt add(final ModInt other) {
		return new ModInt((value + other.value) % mod, mod);
	}

	/**
	 * 減算結果を返します。
	 */
	public ModInt sub(final ModInt other) {
		return new ModInt((value - other.value + mod) % mod, mod);
	}

	/**
	 * 乗算結果を返します。
	 */
	public ModInt mul(final ModInt other) {
		return new ModInt((int) ((long) value * other.value % mod), mod);
	}

	/**
	 * Fermat の小定理による除算結果を返します。法は素数を前提とします。
	 */
	public ModInt div(final ModInt other) {
		return new ModInt((int) ((long) value * modPow(other.value, mod - 2) % mod), mod);
	}

	/**
	 * 非負整数乗を返します。
	 */
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

	@Override
	public int intValue() {
		return value;
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
		return this == obj || obj instanceof ModInt other && value == other.value && mod == other.mod;
	}

	@Override
	public int hashCode() {
		return 31 * value + mod;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
