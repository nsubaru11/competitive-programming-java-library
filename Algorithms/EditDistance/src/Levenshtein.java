import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * 2つの配列間のレーベンシュタイン距離（編集距離）を計算するためのユーティリティクラスです。
 * <p>このクラスは文字列、文字配列、整数配列に対応した編集距離計算メソッドを提供します。</p>
 */
@SuppressWarnings("unused")
public final class Levenshtein {

	/* -------------------------- 文字列の編集距離計算メソッド -------------------------- */

	/**
	 * 2つの文字列間の編集距離を計算します。
	 *
	 * @param s 1つ目の文字列
	 * @param t 2つ目の文字列
	 * @return 2つの文字列間の編集距離
	 */
	public static int computeEditDistance(String s, String t) {
		if (s.length() < t.length()) {
			String temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length();
		int tLen = t.length();
		int[] dp1 = new int[tLen + 1], dp2 = new int[tLen + 1];
		for (int i = 0; i < tLen + 1; i++) dp1[i] = i;
		for (int i = 0; i < sLen; i++) {
			dp2[0] = i + 1;
			char si = s.charAt(i);
			for (int j = 0; j < tLen; j++) {
				if (si != t.charAt(j)) {
					dp2[j + 1] = min(dp1[j], min(dp2[j], dp1[j + 1])) + 1;
				} else {
					dp2[j + 1] = dp1[j];
				}
			}
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		return dp1[tLen];
	}

	/**
	 * 2つの文字列間の編集距離を計算します。最大許容距離（k）を超える場合は早期に計算を終了します。
	 * <p>文字列の長さの差が最大許容距離を超える場合、即座に-1を返します。</p>
	 *
	 * @param s 1つ目の文字列
	 * @param t 2つ目の文字列
	 * @param k 最大許容距離。この値を超える編集距離の場合は-1を返します
	 * @return 編集距離がk以下の場合はその距離、k超過の場合は-1
	 */
	public static int computeEditDistance(String s, String t, int k) {
		if (abs(s.length() - t.length()) > k) return -1;
		if (s.length() < t.length()) {
			String temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length(), tLen = t.length();
		int[] dp1 = new int[tLen + 1], dp2 = new int[tLen + 1];
		for (int i = 0; i < tLen + 1; i++) dp1[i] = i;
		for (int i = 0; i < sLen; i++) {
			int minDist = dp2[0] = i + 1;
			char si = s.charAt(i);
			for (int j = 0; j < tLen; j++) {
				if (si != t.charAt(j)) {
					dp2[j + 1] = min(dp1[j], min(dp2[j], dp1[j + 1])) + 1;
				} else {
					dp2[j + 1] = dp1[j];
				}
				minDist = min(minDist, dp2[j + 1]);
			}
			if (minDist > k) return -1;
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		int ans = dp1[tLen];
		return ans > k ? -1 : ans;
	}

	/* -------------------------- 文字配列の編集距離計算メソッド -------------------------- */

	/**
	 * 2つの文字配列間の編集距離を計算します。
	 *
	 * @param s 1つ目の文字配列
	 * @param t 2つ目の文字配列
	 * @return 2つの文字配列間の編集距離
	 */
	public static int computeEditDistance(char[] s, char[] t) {
		if (s.length < t.length) {
			char[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length;
		int tLen = t.length;
		int[] dp1 = new int[tLen + 1], dp2 = new int[tLen + 1];
		for (int i = 0; i < tLen + 1; i++) dp1[i] = i;
		for (int i = 0; i < sLen; i++) {
			dp2[0] = i + 1;
			char si = s[i];
			for (int j = 0; j < tLen; j++) {
				if (si != t[j]) {
					dp2[j + 1] = min(dp1[j], min(dp2[j], dp1[j + 1])) + 1;
				} else {
					dp2[j + 1] = dp1[j];
				}
			}
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		return dp1[tLen];
	}

	/**
	 * 2つの文字配列間の編集距離を計算します。最大許容距離（k）を超える場合は早期に計算を終了します。
	 * <p>配列の長さの差が最大許容距離を超える場合、即座に-1を返します。</p>
	 *
	 * @param s 1つ目の文字配列
	 * @param t 2つ目の文字配列
	 * @param k 最大許容距離。この値を超える編集距離の場合は-1を返します
	 * @return 編集距離がk以下の場合はその距離、k超過の場合は-1
	 */
	public static int computeEditDistance(char[] s, char[] t, int k) {
		if (abs(s.length - t.length) > k) return -1;
		if (s.length < t.length) {
			char[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length;
		int tLen = t.length;
		int[] dp1 = new int[tLen + 1], dp2 = new int[tLen + 1];
		for (int i = 0; i < tLen + 1; i++) dp1[i] = i;
		for (int i = 0; i < sLen; i++) {
			int minDist = dp2[0] = i + 1;
			char si = s[i];
			for (int j = 0; j < tLen; j++) {
				if (si != t[j]) {
					dp2[j + 1] = min(dp1[j], min(dp2[j], dp1[j + 1])) + 1;
				} else {
					dp2[j + 1] = dp1[j];
				}
				minDist = min(minDist, dp2[j + 1]);
			}
			if (minDist > k) return -1;
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		int ans = dp1[tLen];
		return ans > k ? -1 : ans;
	}

	/* -------------------------- 整数配列の編集距離計算メソッド -------------------------- */

	/**
	 * 2つの整数配列間の編集距離を計算します。
	 *
	 * @param s 1つ目の整数配列
	 * @param t 2つ目の整数配列
	 * @return 2つの整数配列間の編集距離
	 */
	public static int computeEditDistance(int[] s, int[] t) {
		if (s.length < t.length) {
			int[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length;
		int tLen = t.length;
		int[] dp1 = new int[tLen + 1], dp2 = new int[tLen + 1];
		for (int i = 0; i < tLen + 1; i++) dp1[i] = i;
		for (int i = 0; i < sLen; i++) {
			dp2[0] = i + 1;
			int si = s[i];
			for (int j = 0; j < tLen; j++) {
				if (si != t[j]) {
					dp2[j + 1] = min(dp1[j], min(dp2[j], dp1[j + 1])) + 1;
				} else {
					dp2[j + 1] = dp1[j];
				}
			}
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		return dp1[tLen];
	}

	/**
	 * 2つの整数配列間の編集距離を計算します。最大許容距離（k）を超える場合は早期に計算を終了します。
	 * <p>配列の長さの差が最大許容距離を超える場合、即座に-1を返します。</p>
	 *
	 * @param s 1つ目の整数配列
	 * @param t 2つ目の整数配列
	 * @param k 最大許容距離。この値を超える編集距離の場合は-1を返します
	 * @return 編集距離がk以下の場合はその距離、k超過の場合は-1
	 */
	public static int computeEditDistance(int[] s, int[] t, int k) {
		if (abs(s.length - t.length) > k) return -1;
		if (s.length < t.length) {
			int[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length;
		int tLen = t.length;
		int[] dp1 = new int[tLen + 1], dp2 = new int[tLen + 1];
		for (int i = 0; i < tLen + 1; i++) dp1[i] = i;
		for (int i = 0; i < sLen; i++) {
			int minDist = dp2[0] = i + 1;
			int si = s[i];
			for (int j = 0; j < tLen; j++) {
				if (si != t[j]) {
					dp2[j + 1] = min(dp1[j], min(dp2[j], dp1[j + 1])) + 1;
				} else {
					dp2[j + 1] = dp1[j];
				}
				minDist = min(minDist, dp2[j + 1]);
			}
			if (minDist > k) return -1;
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		int ans = dp1[tLen];
		return ans > k ? -1 : ans;
	}
}
