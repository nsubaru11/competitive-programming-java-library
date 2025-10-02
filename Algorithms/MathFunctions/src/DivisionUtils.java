/**
 * 除算関連のユーティリティクラス
 */
@SuppressWarnings("unused")
public final class DivisionUtils {

	/**
	 * a / b以下で最大の長整数を返します。
	 *
	 * @param a 割られる値(long)
	 * @param b 割る値(long)
	 * @return ⌊a / b⌋
	 */
	public static long floorLong(final long a, final long b) {
		return a < 0 ? (a - b + 1) / b : a / b;
	}

	/**
	 * a / b以下で最大の整数を返します。
	 *
	 * @param a 割られる値(int)
	 * @param b 割る値(int)
	 * @return ⌊a / b⌋
	 */
	public static int floorInt(final int a, final int b) {
		return a < 0 ? (a - b + 1) / b : a / b;
	}

	/**
	 * a / b以上で最小の長整数を返します。
	 *
	 * @param a 割られる値(long)
	 * @param b 割る値(long)
	 * @return ⌈a / b⌉
	 */
	public static long ceilLong(final long a, final long b) {
		return a < 0 ? a / b : (a + b - 1) / b;
	}

	/**
	 * a / b以上で最小の整数を返します。
	 *
	 * @param a 割られる値(int)
	 * @param b 割る値(int)
	 * @return ⌈a / b⌉
	 */
	public static long ceilInt(final int a, final int b) {
		return a < 0 ? a / b : (a + b - 1) / b;
	}

}
