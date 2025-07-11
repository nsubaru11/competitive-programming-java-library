import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.util.Arrays.setAll;

public final class Levenshtein {

	public static int computeEditDistance(String s, String t) {
		if (s.length() < t.length()) {
			String temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length();
		int tLen = t.length();
		int[][] dp = new int[2][tLen + 1];
		setAll(dp[0], i -> i);
		for (int i = 0; i < sLen; i++) {
			int prev = i & 1, cur = (i + 1) & 1;
			dp[cur][0] = i + 1;
			for (int j = 0; j < tLen; j++) {
				if (s.charAt(i) != t.charAt(j)) {
					dp[cur][j + 1] = min(dp[prev][j], min(dp[cur][j], dp[prev][j + 1])) + 1;
				} else {
					dp[cur][j + 1] = dp[prev][j];
				}
			}
		}
		return dp[sLen % 2][tLen];
	}

	public static int computeEditDistance(String s, String t, int k) {
		if (abs(s.length() - t.length()) > k) return -1;
		if (s.length() < t.length()) {
			String temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length();
		int tLen = t.length();
		int[][] dp = new int[2][tLen + 1];
		setAll(dp[0], i -> i);
		for (int i = 0; i < sLen; i++) {
			int prev = i & 1, cur = (i + 1) & 1;
			dp[cur][0] = i + 1;
			int minDist = dp[cur][0];
			for (int j = 0; j < tLen; j++) {
				if (s.charAt(i) != t.charAt(j)) {
					dp[cur][j + 1] = min(dp[prev][j], min(dp[cur][j], dp[prev][j + 1])) + 1;
				} else {
					dp[cur][j + 1] = dp[prev][j];
				}
				minDist = min(minDist, dp[cur][j + 1]);
			}
			if (minDist > k) return -1;
		}
		int ans = dp[sLen & 1][tLen];
		return ans > k ? -1 : ans;
	}

	public static int computeEditDistance(char[] s, char[] t) {
		if (s.length < t.length) {
			char[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length;
		int tLen = t.length;
		int[][] dp = new int[2][tLen + 1];
		setAll(dp[0], i -> i);
		for (int i = 0; i < sLen; i++) {
			int prev = i & 1, cur = (i + 1) & 1;
			dp[cur][0] = i + 1;
			for (int j = 0; j < tLen; j++) {
				if (s[i] != t[j]) {
					dp[cur][j + 1] = min(dp[prev][j], min(dp[cur][j], dp[prev][j + 1])) + 1;
				} else {
					dp[cur][j + 1] = dp[prev][j];
				}
			}
		}
		return dp[sLen % 2][tLen];
	}

	public static int computeEditDistance(char[] s, char[] t, int k) {
		if (abs(s.length - t.length) > k) return -1;
		if (s.length < t.length) {
			char[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length;
		int tLen = t.length;
		int[][] dp = new int[2][tLen + 1];
		setAll(dp[0], i -> i);
		for (int i = 0; i < sLen; i++) {
			int prev = i & 1, cur = (i + 1) & 1;
			dp[cur][0] = i + 1;
			int minDist = dp[cur][0];
			for (int j = 0; j < tLen; j++) {
				if (s[i] != t[j]) {
					dp[cur][j + 1] = min(dp[prev][j], min(dp[cur][j], dp[prev][j + 1])) + 1;
				} else {
					dp[cur][j + 1] = dp[prev][j];
				}
				minDist = min(minDist, dp[cur][j + 1]);
			}
			if (minDist > k) return -1;
		}
		int ans = dp[sLen & 1][tLen];
		return ans > k ? -1 : ans;
	}

	public static int computeEditDistance(int[] s, int[] t) {
		if (s.length < t.length) {
			int[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length;
		int tLen = t.length;
		int[][] dp = new int[2][tLen + 1];
		setAll(dp[0], i -> i);
		for (int i = 0; i < sLen; i++) {
			int prev = i & 1, cur = (i + 1) & 1;
			dp[cur][0] = i + 1;
			for (int j = 0; j < tLen; j++) {
				if (s[i] != t[j]) {
					dp[cur][j + 1] = min(dp[prev][j], min(dp[cur][j], dp[prev][j + 1])) + 1;
				} else {
					dp[cur][j + 1] = dp[prev][j];
				}
			}
		}
		return dp[sLen % 2][tLen];
	}

	public static int computeEditDistance(int[] s, int[] t, int k) {
		if (abs(s.length - t.length) > k) return -1;
		if (s.length < t.length) {
			int[] temp = s;
			s = t;
			t = temp;
		}
		int sLen = s.length;
		int tLen = t.length;
		int[][] dp = new int[2][tLen + 1];
		setAll(dp[0], i -> i);
		for (int i = 0; i < sLen; i++) {
			int prev = i & 1, cur = (i + 1) & 1;
			dp[cur][0] = i + 1;
			int minDist = dp[cur][0];
			for (int j = 0; j < tLen; j++) {
				if (s[i] != t[j]) {
					dp[cur][j + 1] = min(dp[prev][j], min(dp[cur][j], dp[prev][j + 1])) + 1;
				} else {
					dp[cur][j + 1] = dp[prev][j];
				}
				minDist = min(minDist, dp[cur][j + 1]);
			}
			if (minDist > k) return -1;
		}
		int ans = dp[sLen & 1][tLen];
		return ans > k ? -1 : ans;
	}
}
