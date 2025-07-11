import java.io.PrintWriter;
import java.util.Scanner;
//import static java.lang.System.out;

public class Example {

	private static final Scanner sc = new Scanner(System.in);
	private static final PrintWriter out = new PrintWriter(System.out);

	private static void solve(int n) {
		String s = "i++";
		String t = "i += 2";
		out.println("\"" + s + "\" と \"" + t + "\" のレーベンシュタイン距離は" + Levenshtein.computeEditDistance(s, t) + "です。");
	}

	public static void main(String[] args) {
		try {
			// 最初の入力
			solve(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
			out.close();
		}
	}

}
