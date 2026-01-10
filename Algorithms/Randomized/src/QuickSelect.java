import java.util.*;
import java.util.random.*;

public final class QuickSelect {

	private static final RandomGenerator rng = RandomGenerator.getDefault();

	public static int select(int[] a, int k) {
		int len = a.length;
		int[] copy = Arrays.copyOf(a, len);
		int l = 0, r = len - 1;
		while (l < r) {
			int pivotIdx = l + rng.nextInt(r - l + 1);
			int v = copy[pivotIdx];
			int lt = l, gt = r;
			int i = l;
			while (i <= gt) {
				if (copy[i] < v) swap(copy, lt++, i++);
				else if (copy[i] > v) swap(copy, i, gt--);
				else i++;
			}
			if (k < lt) r = lt - 1;
			else if (k > gt) l = gt + 1;
			else return v;
		}
		return copy[k];
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
