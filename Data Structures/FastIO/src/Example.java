import java.util.List;

public class Example {

	private static void solve(final ContestScanner sc, final ContestPrinter out) {
		// ここから処理を書きます。
		Object[] arr = {1, "21231", 3.14159265358979323846, new int[]{1, 2, 3, 4, 5}, new String[]{"a", "b", "c", "d", "e"}, null};
		out.print(arr);
		out.println();
		out.println(1, "232", new int[]{1, 2, 3, 4}, null);
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		out.print(list, i -> i * i);
	}

	public static void main(String[] args) {
		try (final ContestScanner sc = new ContestScanner();
			 final ContestPrinter out = new ContestPrinter()) {
			solve(sc, out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
