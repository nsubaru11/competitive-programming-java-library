package unit.string;

import java.util.*;

/**
 * string のテストで使うランダムな文字列と愚直解。
 */
public final class Naive {

	private Naive() {
	}

	public static String randomString(final Random random, final int maxLength, final int alphabet) {
		final StringBuilder sb = new StringBuilder();
		for (int i = random.nextInt(maxLength + 1); i > 0; i--) sb.append((char) ('a' + random.nextInt(alphabet)));
		return sb.toString();
	}

	public static boolean isPalindrome(final String s, final int l, final int r) {
		for (int i = l, j = r - 1; i < j; i++, j--) if (s.charAt(i) != s.charAt(j)) return false;
		return true;
	}

	/**
	 * center を中心とする最長回文の長さ。odd なら奇数長、そうでなければ center と center + 1 の間を中心とする偶数長。
	 */
	public static int palindromeAt(final String s, final int center, final boolean odd) {
		int l = odd ? center - 1 : center, r = center + 1;
		while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
			l--;
			r++;
		}
		return r - l - 1;
	}

	public static int longestPalindrome(final String s, final boolean odd, final boolean even) {
		int best = 0;
		for (int l = 0; l < s.length(); l++) {
			for (int r = l + 1; r <= s.length(); r++) {
				final boolean isOdd = (r - l) % 2 == 1;
				if ((isOdd ? odd : even) && isPalindrome(s, l, r)) best = Math.max(best, r - l);
			}
		}
		return best;
	}

	public static int editDistance(final String s, final String t) {
		final int[][] dp = new int[s.length() + 1][t.length() + 1];
		for (int i = 0; i <= s.length(); i++) dp[i][0] = i;
		for (int j = 0; j <= t.length(); j++) dp[0][j] = j;
		for (int i = 1; i <= s.length(); i++) {
			for (int j = 1; j <= t.length(); j++) {
				dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]) + 1, dp[i - 1][j - 1] + (s.charAt(i - 1) == t.charAt(j - 1) ? 0 : 1));
			}
		}
		return dp[s.length()][t.length()];
	}
}
