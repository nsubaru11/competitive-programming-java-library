@SuppressWarnings("unused")
public class InsertionSort {

	public static void insertionSort(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			int j = i;
			while (j >= 1 && arr[j - 1] > arr[j]) {
				swap(arr, j - 1, j);
				j--;
			}
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
