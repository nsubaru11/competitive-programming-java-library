/**
 * Z-Algorithm（線形時間文字列マッチング）の実装。
 */
@SuppressWarnings("unused")
public final class ZAlgorithm {

	/**
	 * Z配列を計算: {@code z[i] = LCP(S, S[i:])}
	 *
	 * @param s 探索対象文字列
	 * @return 各位置の最長共通接頭辞長の配列
	 */
	public static int[] getZArray(final String s) {
		final int len = s.length();
		final int[] z = new int[len];
		z[0] = len;
		for (int i = 1, l = 0, r = 0; i < len; i++) {
			if (i < r && z[i - l] < r - i) {
				z[i] = z[i - l];
				continue;
			}
			if (r < i) r = i;
			while (r < len && s.charAt(r) == s.charAt(r - i)) r++;
			z[i] = r - i;
			l = i;
		}
		return z;
	}

	/**
	 * Z配列を計算: {@code z[i] = LCP(S, S[i:])}
	 *
	 * @param s 探索対象文字配列
	 * @return 各位置の最長共通接頭辞長の配列
	 */
	public static int[] getZArray(final char[] s) {
		final int len = s.length;
		final int[] z = new int[len];
		z[0] = len;
		for (int i = 1, l = 0, r = 0; i < len; i++) {
			if (i < r && z[i - l] < r - i) {
				z[i] = z[i - l];
				continue;
			}
			if (r < i) r = i;
			while (r < len && s[r] == s[r - i]) r++;
			z[i] = r - i;
			l = i;
		}
		return z;
	}
}
