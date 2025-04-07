
@SuppressWarnings("unused")
public class PalindromeUtils {

	/**
	 * 文字列が回文かどうかの判定をします。
	 *
	 * @param s String
	 * @return boolean
	 */
	public static boolean isPalindrome(String s) {
		int n = s.length();
		for (int i = 0; i <= n / 2; i++) {
			if (s.charAt(i) != s.charAt(n - i - 1)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 文字配列が回文かどうかの判定をします。
	 *
	 * @param c char[]
	 * @return boolean
	 */
	public static boolean isPalindrome(char[] c) {
		int n = c.length;
		for (int i = 0; i <= n / 2; i++) {
			if (c[i] != c[n - i - 1]) {
				return false;
			}
		}
		return true;
	}
}