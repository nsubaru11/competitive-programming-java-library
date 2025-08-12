import java.util.Arrays;

import static java.lang.Math.*;

@SuppressWarnings("unused")
public final class Myers {

	/* -------------------------- 文字列の編集距離計算メソッド -------------------------- */

	/**
	 * 2つの文字列間の編集距離を計算します。
	 *
	 * @param s 1つ目の文字列
	 * @param t 2つ目の文字列
	 * @return 2つの文字列間の編集距離
	 */
	public static int computeEditDistance(String s, String t) {
		return 0;
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
		return 0;
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
		int delta = tLen - sLen;
		int[] v = new int[sLen + tLen + 1];
		Arrays.fill(v, -1);
		v[sLen] = 0;
		for (int d = 0; d <= tLen; d++) {
			for (int k = -d; k <= d; k++) {
				int kIdx = k + sLen;
				int x = v[kIdx]; // 編集距離d-1で到達可能な最大のx
				if (kIdx + 1 < tLen + sLen + 1 && v[kIdx + 1] > x) {
					x = v[kIdx + 1]; // 編集距離d-1で到達可能な最大のxから削除操作を行ったときのx
				}
				int y = abs(x + k);
				if (s[x] == t[y] && s[x + 1] == t[y] && v[kIdx] > v[kIdx + 1]) {
					v[kIdx + 1] = x + 1;
				}
				if (s[x] == t[y] && s[x] == t[y + 1] && v[kIdx] > v[kIdx - 1]) {
					v[kIdx - 1] = x - 1;
				}
				while (x < sLen && y < tLen && s[x] == t[y]) {
					y++;
					x++;
				}
				v[kIdx] = x - 1;
				if (k == delta && y == tLen) {
					return d;
				}
				if (x == sLen)
				if (s[x - 1] == t[y - 1] && s[x - 1] == t[y - 1]) {}
				System.out.println(d + " " + Arrays.toString(v));
			}
		}
		return sLen;
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
		return 0;
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
		return 0;
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
		return 0;
	}
}
