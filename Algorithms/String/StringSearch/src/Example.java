import java.io.*;
import java.util.*;

public final class Example {

	private static void solve(final Scanner sc, final PrintWriter out) {
		String s = sc.next();
		out.println(Arrays.toString(ZAlgorithm.getZArray(s)));
	}

	public static void main(String[] args) {
		try (final Scanner sc = new Scanner(System.in);
		     final PrintWriter out = new PrintWriter(System.out)) {
			solve(sc, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
