@SuppressWarnings("unused")
public class ShakerSort {

	public static void shakerSort(int[] arr) {
		int left = 0, right = arr.length;// sort済みのidxを覚える
		for (int i = 0; i < arr.length; i++) {

			// 正順
			boolean flag = true; // swapの有無
			int r = 0;
			for (int j = left + 1; j < right; j++) {
				if (arr[j - 1] > arr[j]) {
					r = j;
					swap(arr, j - 1, j);
					flag = false;
				}
			}
			if (flag) break;

			// 逆順
			flag = true;
			right = r;
			int l = 0;
			for (int j = right - 1; j > left; j--) {
				if (arr[j - 1] > arr[j]) {
					l = j;
					swap(arr, j - 1, j);
					flag = false;
				}
			}
			if (flag) break;
			left = l;
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
