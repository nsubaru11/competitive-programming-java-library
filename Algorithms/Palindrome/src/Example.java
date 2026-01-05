import java.io.PrintStream;
import java.util.Arrays;

public class Example {

	public static void main(String[] args) {
		PrintStream out = System.out;

		out.println("===== Manacherクラスのデモ =====");
		String text = "levelracecarreferdeifiednoonmalayalam";
		char[] chars = text.toCharArray();

		out.println("入力文字列: " + text);
		out.println("文字列の長さ: " + chars.length);

		Manacher mc = new Manacher(text);
		out.println("\n--- 基本情報 ---");
		out.println("回文長配列: " + Arrays.toString(mc.getPalindromeLengths()));
		out.println("最長偶数長回文の長さ: " + mc.getEvenLongestLen());
		out.println("最長奇数長回文の長さ: " + mc.getOddLongestLen());
		out.println("最長回文の長さ: " + mc.getLongestLen());

		out.println("\n--- 位置指定の回文長さ ---");
		out.println("位置2を中心とする奇数長回文の長さ: " + mc.getOddPalindromeLengthAt(2));
		out.println("位置4の直後を中心とする偶数長回文の長さ: " + mc.getEvenPalindromeLengthAt(4));

		out.println("\n--- 回文文字列の取得 ---");
		out.println("最長回文: " + mc.getLongestPalindrome());
		out.println("最長奇数長回文: " + mc.getLongestOddPalindrome());
		out.println("最長偶数長回文: " + mc.getLongestEvenPalindrome());
		out.println("位置2を中心とする奇数長回文: " + mc.getOddPalindromeAt(2));
		out.println("位置4の直後を中心とする偶数長回文: " + mc.getEvenPalindromeAt(4));

		out.println("\n--- 範囲判定 ---");
		out.println("範囲[1, 6)は回文か: " + mc.isPalindromeRange(1, 6));
		out.println("範囲[0, 8)は回文か: " + mc.isPalindromeRange(0, 8));

		// PalindromeUtilsクラスのデモ
		out.println("\n===== PalindromeUtilsクラスのデモ =====");
		String[] testStrings = {"level", "hello", "racecar", "abcdcba", "abcde"};

		out.println("\n--- 回文判定 ---");
		for (String s : testStrings) {
			out.println(s + " は回文か: " + PalindromeNative.isPalindrome(s));
		}

		out.println("\n--- 範囲指定の回文判定 ---");
		String rangeTest = "levelupracecar";
		out.println("文字列: " + rangeTest);
		out.println("範囲[0, 5)は回文か: " + PalindromeNative.isPalindromeRange(rangeTest, 0, 5)); // "level"
		out.println("範囲[7, 14)は回文か: " + PalindromeNative.isPalindromeRange(rangeTest, 7, 14)); // "racecar"

		out.println("\n--- 最長回文部分文字列 ---");
		String longText = "babad";
		out.println("文字列: " + longText);
		out.println("最長回文部分文字列: " + PalindromeNative.findLongestPalindrome(longText));

		out.println("\n--- 回文への変換 ---");
		String[] conversionTests = {"abc", "abb", "cbabc"};
		for (String s : conversionTests) {
			String palindrome = PalindromeNative.makePalindrome(s);
			out.println(s + " -> " + palindrome + " (" +
					(PalindromeNative.isPalindrome(palindrome) ? "回文" : "非回文") + ")");
		}
	}
}
