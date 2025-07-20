import java.io.PrintWriter;
import java.util.*;

import static java.util.Arrays.*;
import static java.lang.Math.*;
//import static java.lang.System.out;

public class Example {

	private static final Scanner sc = new Scanner(System.in);
	private static final PrintWriter out = new PrintWriter(System.out);

	private static void solve(int n) {
		out.println(Master.toInt("1234567890".toCharArray()));
		out.println(Master.toInt("1234567890"));
		out.println(Master.toCharArray(1234567890));
		out.println(Master.toCharArray(1234567890, 8));
		out.println(Master.toString(1234567890));
		out.println(Master.sort(417253219));
		out.println(Master.descendingSort(417253219));

		// --- 追加メソッドのテスト ---
		char[] c = {'1', '2', '3', '4'};
		int[] ia = Master.toIntArray(c);
		out.println(Arrays.toString(ia)); // [1, 2, 3, 4]
		char[] c2 = Master.toCharArray(ia);
		out.println(Arrays.toString(c2)); // [1, 2, 3, 4]
		int[] ia2 = Master.toIntArray("5678");
		out.println(Arrays.toString(ia2)); // [5, 6, 7, 8]
		out.println(Master.toString(ia2)); // 5678
		Master.reverse(ia2);
		out.println(Arrays.toString(ia2)); // [8, 7, 6, 5]
		Master.reverse(c2);
		out.println(Arrays.toString(c2)); // [4, 3, 2, 1]
		long[] la = {1, 2, 3, 4, 5};
		Master.reverse(la);
		out.println(Arrays.toString(la)); // [5, 4, 3, 2, 1]
	}

	public static void main(String[] args) {
		try {
			// 最初の入力
			int n;
			while ((n = sc.nextInt()) != 0) {
				solve(n); // メインロジック
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
			out.close();
		}
	}

}
