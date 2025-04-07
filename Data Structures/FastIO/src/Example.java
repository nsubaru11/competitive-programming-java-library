public class Example {

	private static void solve(final ContestScanner sc, final ContestPrinter out) {
		// ここから処理を書きます。
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
