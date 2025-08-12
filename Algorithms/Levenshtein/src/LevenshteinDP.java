import static java.lang.Math.*;

/**
 * 2つの配列間のレーベンシュタイン距離（編集距離）を計算するためのユーティリティクラスです。
 * <p>このクラスは文字列、文字配列、整数配列に対応した編集距離計算メソッドを提供します。</p>
 */
@SuppressWarnings("unused")
public final class LevenshteinDP {

	/* -------------------------- 文字列の編集距離計算メソッド -------------------------- */

	/**
	 * 2つの文字列間の編集距離を計算します。
	 *
	 * @param s 1つ目の文字列
	 * @param t 2つ目の文字列
	 * @return 2つの文字列間の編集距離
	 */
	public static int computeEditDistance(String s, String t) {
		if (s.length() > t.length()) {
			String temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length(), tLen = t.length();
		int[] dp1 = new int[sLen + 1], dp2 = new int[sLen + 1];
		for (int i = 0; i < sLen + 1; i++) dp1[i] = i;
		for (int i = 0; i < tLen; i++) {
			dp2[0] = i + 1;
			char ti = t.charAt(i);
			for (int j = 0; j < sLen; j++) {
				if (ti == s.charAt(j)) {
					dp2[j + 1] = dp1[j];
				} else {
					dp2[j + 1] = min(dp1[j], min(dp2[j], dp1[j + 1])) + 1;
				}
			}
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		return dp1[sLen];
	}

	/**
	 * 2つの文字列の編集距離がk以下となるか判定します。編集距離がk以下の場合その編集距離を、そうでない場合は-1を返します。
	 * このメソッドはbandedDPアルゴリズムを使用し、O(|S|K)の時間計算量で高速に評価します。
	 *
	 * @param s 1つ目の文字列
	 * @param t 2つ目の文字列
	 * @param k 最大許容距離。この値を超える編集距離の場合は-1を返します
	 * @return 編集距離がk以下の場合はその距離、k超過の場合は-1
	 */
	public static int computeEditDistance(String s, String t, int k) {
		if (abs(s.length() - t.length()) > k) return -1;
		if (s.length() > t.length()) {
			String temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length(), tLen = t.length();
		int band = k << 1 | 1;
		int inf = k + 1;
		int[] dp1 = new int[band], dp2 = new int[band];
		for (int i = 0; i < band; i++) dp1[i] = abs(i - k);
		for (int i = 0; i < sLen; i++) {
			char si = s.charAt(i);
			for (int l = max(k - i, 0), j = i + l - k; l < band && j < tLen; l++, j++) {
				if (si == t.charAt(j)) {
					dp2[l] = dp1[l];
				} else {
					int sub = dp1[l];
					int del = l + 1 < band ? dp1[l + 1] : inf;
					int ins = l - 1 >= 0 ? dp2[l - 1] : inf;
					dp2[l] = min(sub, min(del, ins)) + 1;
				}
			}
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		int ans = dp1[tLen - sLen + k];
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
		if (s.length > t.length) {
			char[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length, tLen = t.length;
		int[] dp1 = new int[sLen + 1], dp2 = new int[sLen + 1];
		for (int i = 0; i < sLen + 1; i++) dp1[i] = i;
		for (int i = 0; i < tLen; i++) {
			dp2[0] = i + 1;
			char ti = t[i];
			for (int j = 0; j < sLen; j++) {
				if (ti == s[j]) {
					dp2[j + 1] = dp1[j];
				} else {
					dp2[j + 1] = min(dp1[j], min(dp2[j], dp1[j + 1])) + 1;
				}
			}
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		return dp1[sLen];
	}

	/**
	 * 2つの文字配列の編集距離がk以下となるか判定します。編集距離がk以下の場合その編集距離を、そうでない場合は-1を返します。
	 * このメソッドはbandedDPアルゴリズムを使用し、O(|S|K)の時間計算量で高速に評価します。
	 *
	 * @param s 1つ目の文字配列
	 * @param t 2つ目の文字配列
	 * @param k 最大許容距離。この値を超える編集距離の場合は-1を返します
	 * @return 編集距離がk以下の場合はその距離、k超過の場合は-1
	 */
	public static int computeEditDistance(char[] s, char[] t, int k) {
		if (abs(s.length - t.length) > k) return -1;
		if (s.length > t.length) {
			char[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length, tLen = t.length;
		int band = k << 1 | 1;
		int inf = k + 1;
		int[] dp1 = new int[band], dp2 = new int[band];
		for (int i = 0; i < band; i++) dp1[i] = abs(i - k);
		for (int i = 0; i < sLen; i++) {
			char si = s[i];
			for (int l = max(k - i, 0), j = i + l - k; l < band && j < tLen; l++, j++) {
				if (si == t[j]) {
					dp2[l] = dp1[l];
				} else {
					int sub = dp1[l];
					int del = l + 1 < band ? dp1[l + 1] : inf;
					int ins = l - 1 >= 0 ? dp2[l - 1] : inf;
					dp2[l] = min(sub, min(del, ins)) + 1;
				}
			}
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		int ans = dp1[tLen - sLen + k];
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
		int sLen = s.length, tLen = t.length;
		int[] dp1 = new int[tLen + 1], dp2 = new int[tLen + 1];
		for (int i = 0; i < tLen + 1; i++) dp1[i] = i;
		for (int i = 0; i < sLen; i++) {
			dp2[0] = i + 1;
			int si = s[i];
			for (int j = 0; j < tLen; j++) {
				if (si == t[j]) {
					dp2[j + 1] = dp1[j];
				} else {
					dp2[j + 1] = min(dp1[j], min(dp2[j], dp1[j + 1])) + 1;
				}
			}
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		return dp1[tLen];
	}

	/**
	 * 2つの整数配列の編集距離がk以下となるか判定します。編集距離がk以下の場合その編集距離を、そうでない場合は-1を返します。
	 * このメソッドはbandedDPアルゴリズムを使用し、O(|S|K)の時間計算量で高速に評価します。
	 *
	 * @param s 1つ目の整数配列
	 * @param t 2つ目の整数配列
	 * @param k 最大許容距離。この値を超える編集距離の場合は-1を返します
	 * @return 編集距離がk以下の場合はその距離、k超過の場合は-1
	 */
	public static int computeEditDistance(int[] s, int[] t, int k) {
		if (abs(s.length - t.length) > k) return -1;
		if (s.length > t.length) {
			int[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length, tLen = t.length;
		int band = k << 1 | 1;
		int inf = k + 1;
		int[] dp1 = new int[band], dp2 = new int[band];
		for (int i = 0; i < band; i++) dp1[i] = abs(i - k);
		for (int i = 0; i < sLen; i++) {
			int si = s[i];
			for (int l = max(k - i, 0), j = i + l - k; l < band && j < tLen; l++, j++) {
				if (si == t[j]) {
					dp2[l] = dp1[l];
				} else {
					int sub = dp1[l];
					int del = l + 1 < band ? dp1[l + 1] : inf;
					int ins = l - 1 >= 0 ? dp2[l - 1] : inf;
					dp2[l] = min(sub, min(del, ins)) + 1;
				}
			}
			int[] temp = dp1;
			dp1 = dp2;
			dp2 = temp;
		}
		int ans = dp1[tLen - sLen + k];
		return ans > k ? -1 : ans;
	}
}
