import java.util.*;

public final class Example {

	// ------------------------ 定数 ------------------------
	private static final ContestScanner sc = new ContestScanner();
	private static final ContestPrinter out = new ContestPrinter();

	// ------------------------ メインロジック ------------------------
	private static void solve() {
		Object[] arr = {1, "21231", 3.14159265358979323846, new int[]{1, 2, 3, 4, 5}, new String[]{"a", "b", "c", "d", "e"}, null};
		out.print(arr);
		out.println();
		out.println(1, "232", new int[]{1, 2, 3, 4}, null);
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		out.print(list, i -> i * i);
	}

	// ------------------------ main() 関数 ------------------------
	public static void main(final String[] args) {
		try {
			solve();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
			out.close();
		}
	}

}
