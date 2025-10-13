import java.io.*;
import java.util.*;

public final class Example {

	private static void solve(final PrintWriter out) {
		int[] array = {1, 2, 3, 4, 5};

		// 全ての順列を列挙するため、最初に昇順ソートする
		Arrays.sort(array);

		out.println("All permutations of " + Arrays.toString(array) + ":");
		do {
			// 現在の順列を出力
			out.println(Arrays.toString(array));
		} while (Permutation.next(array)); // 次の順列を生成
	}

	public static void main(String[] args) {
		try (final PrintWriter out = new PrintWriter(System.out)) {
			solve(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
