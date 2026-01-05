@SuppressWarnings("unused")
public final class PalindromeNative {

	/**
	 * 文字列が回文かどうかの判定をします。
	 *
	 * @param s String
	 * @return boolean
	 */
	public static boolean isPalindrome(String s) {
		int n = s.length();
		for (int i = 0; i < n / 2; i++) {
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
		for (int i = 0; i < n / 2; i++) {
			if (c[i] != c[n - i - 1]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 文字列の指定された範囲が回文かどうかの判定をします。
	 *
	 * @param s     文字列
	 * @param start 開始インデックス（含む）
	 * @param end   終了インデックス（含まない）
	 * @return 指定範囲が回文であれば true、そうでなければ false
	 */
	public static boolean isPalindromeRange(String s, int start, int end) {
		if (start < 0 || end > s.length() || start >= end) {
			return false;
		}
		for (int i = 0; i < (end - start) / 2; i++) {
			if (s.charAt(start + i) != s.charAt(end - 1 - i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 文字配列の指定された範囲が回文かどうかの判定をします。
	 *
	 * @param c     文字配列
	 * @param start 開始インデックス（含む）
	 * @param end   終了インデックス（含まない）
	 * @return 指定範囲が回文であれば true、そうでなければ false
	 */
	public static boolean isPalindromeRange(char[] c, int start, int end) {
		if (start < 0 || end > c.length || start >= end) {
			return false;
		}
		for (int i = 0; i < (end - start) / 2; i++) {
			if (c[start + i] != c[end - 1 - i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 文字列の最長回文部分文字列を見つけます。
	 * 単純なO(n²)アルゴリズムを使用します。より効率的な実装にはManacherクラスを使用してください。
	 *
	 * @param s 文字列
	 * @return 最長回文部分文字列
	 */
	public static String findLongestPalindrome(String s) {
		if (s == null || s.isEmpty()) return "";
		int start = 0;
		int maxLen = 1;
		for (int i = 0; i < s.length(); i++) {
			for (int j = 1; i - j >= 0 && i + j < s.length(); j++) {
				if (s.charAt(i - j) != s.charAt(i + j)) {
					break;
				}
				int len = 2 * j + 1;
				if (len > maxLen) {
					start = i - j;
					maxLen = len;
				}
			}
		}
		for (int i = 0; i < s.length() - 1; i++) {
			if (s.charAt(i) == s.charAt(i + 1)) {
				if (2 > maxLen) {
					start = i;
					maxLen = 2;
				}
				for (int j = 1; i - j >= 0 && i + 1 + j < s.length(); j++) {
					if (s.charAt(i - j) != s.charAt(i + 1 + j)) {
						break;
					}
					int length = 2 * j + 2;
					if (length > maxLen) {
						start = i - j;
						maxLen = length;
					}
				}
			}
		}

		return s.substring(start, start + maxLen);
	}

	/**
	 * 文字列が回文になるように最小の文字を追加します。
	 *
	 * @param s 文字列
	 * @return 回文に変換された文字列
	 */
	public static String makePalindrome(String s) {
		if (s == null || s.isEmpty() || isPalindrome(s)) return s;
		int maxPreLen = 0;
		for (int i = 0; i < s.length(); i++) {
			if (isPalindromeRange(s, 0, i + 1)) {
				maxPreLen = i + 1;
			}
		}
		StringBuilder result = new StringBuilder(s);
		for (int i = s.length() - maxPreLen - 1; i >= 0; i--) {
			result.append(s.charAt(i));
		}
		return result.toString();
	}
}
