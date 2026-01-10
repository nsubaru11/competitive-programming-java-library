import java.util.*;

public final class Main {

	public static void main(final String[] args) {
		int n = 1000000;
		int[] arr = new Random().ints(n, 0, (int) (n * 0.8)).toArray();
		long t = System.currentTimeMillis();
		long ans = QuickSelect.select(arr, n / 2);
		long elapsed = System.currentTimeMillis() - t;
		System.out.println(ans + " " + elapsed);
		long t2 = System.currentTimeMillis();
		Arrays.sort(arr);
		long ans2 = arr[n / 2];
		elapsed = System.currentTimeMillis() - t2;
		System.out.println(ans2 + " " + elapsed);
	}
}
