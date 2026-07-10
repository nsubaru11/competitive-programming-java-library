package lib.sort;

@SuppressWarnings("unused")
public class OddEvenSort {

	public static void oddEvenSort(int[] arr) {
		int n = arr.length;
		boolean flag = true;
		while (flag) {
			flag = false;
			for (int i = 0; i < n - 1; i += 2) {
				if (arr[i] > arr[i + 1]) {
					swap(arr, i, i + 1);
					flag = true;
				}
			}
			for (int i = 1; i < n - 1; i += 2) {
				if (arr[i] > arr[i + 1]) {
					swap(arr, i, i + 1);
					flag = true;
				}
			}
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}
