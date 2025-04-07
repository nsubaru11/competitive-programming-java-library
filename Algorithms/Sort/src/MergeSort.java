import java.util.Arrays;

@SuppressWarnings("unused")
public class MergeSort {

	public static int[] mergeSort(int[] data, int n) {
		if (n > 1) {
			int l_len = n / 2;
			int r_len = n - n / 2;
			int[] left = mergeSort(Arrays.copyOfRange(data, 0, l_len), l_len);
			int[] right = mergeSort(Arrays.copyOfRange(data, l_len, n), r_len);
			for (int l = 0, r = 0; l + r < n; ) {
				while (l + r < n && r >= r_len || (l < l_len && left[l] <= right[r])) {
					data[l + r] = left[l];
					l++;
				}
				while (l + r < n && l >= l_len || (r < r_len && left[l] > right[r])) {
					data[l + r] = right[r];
					r++;
				}
			}
		}
		return data;
	}
}
