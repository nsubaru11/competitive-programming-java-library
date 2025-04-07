import static java.lang.Math.pow;
import static java.lang.Math.round;

/**
 * 数値フォーマット関連のユーティリティクラス
 */
@SuppressWarnings("unused")
public class NumberFormatUtils {

	/**
	 * doubleの高速なフォーマット。小数点第n - 1位を四捨五入します。
	 *
	 * @param x フォーマットする対象
	 * @param n 少数点以下の桁数
	 * @return String
	 */
	public static String formatDouble(double x, int n) {
		StringBuilder sb = new StringBuilder();
		if (n == 0) return sb.append(round(x)).toString();
		if (x < 0) {
			sb.append("-");
			x = -x;
		}
		x += pow(10, -n) / 2;
		sb.append((long) x).append(".");
		x -= (long) x;
		while (n-- > 0) {
			x *= 10;
			sb.append((int) x);
			x -= (int) x;
		}
		return sb.toString();
	}

	/**
	 * 数値を指定された桁数で左側を0埋めした文字列に変換します。
	 *
	 * @param num    変換する数値
	 * @param digits 桁数
	 * @return 0埋めされた文字列
	 */
	public static String toPaddedString(long num, int digits) {
		String str = Long.toString(num);
		if (str.length() >= digits) return str;
		return "0".repeat(digits - str.length()) + str;
	}
} 