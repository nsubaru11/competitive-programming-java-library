@SuppressWarnings("unused")
public class ShellSort {

	public static void shellSort(int[] arr) {
		int n = arr.length;
		int h = 1;
		int k = 1;
		while (n > h) {
			h = (int) (Math.pow(4, k) + 3 * Math.pow(2, k - 1) + 1);
			k++;
		}
		k--;
		while (k >= 0) {
			if (k == 0) {
				h = 1;
			} else {
				h = (int) (Math.pow(4, k) + 3 * Math.pow(2, k - 1) + 1);
			}
			for (int i = 0; i < h; i++) {
				for (int j = i + h - 1; j < n; j += h) {
					int idx = j;
					while (idx >= h && arr[idx - h] > arr[idx]) {
						swap(arr, idx - h, idx);
						idx -= h;
					}
				}
			}
			k--;
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
