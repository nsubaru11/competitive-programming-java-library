@SuppressWarnings("unused")
public class GnomeSort {

	public static void basicGnomeSort(int[] arr) {
		int len = arr.length;
		for (int i = 0; i < len - 1; ) {
			if (arr[i] > arr[i + 1]) {
				swap(arr, i, i + 1);
				i = i > 1 ? i - 1 : 0;
			} else {
				i++;
			}
		}
	}

	public static void gnomeSort(int[] arr) {
		int len = arr.length;
		for (int i = 0, maxIdx = 0; i < len - 1; ) {
			if (arr[i] > arr[i + 1]) {
				swap(arr, i, i + 1);
				i = i > 1 ? i - 1 : 0;
			} else {
				i = ++maxIdx;
			}
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
