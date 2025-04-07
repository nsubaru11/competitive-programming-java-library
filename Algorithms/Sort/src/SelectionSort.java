@SuppressWarnings("unused")
public class SelectionSort {

	public static void selectionSort(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			int min = arr[i];
			int minIdx = i;
			for (int j = i + 1; j < arr.length; j++) {
				if (min > arr[j]) {
					min = arr[j];
					minIdx = j;
				}
			}
			swap(arr, i, minIdx);
		}
	}

	public static void doubleSelectionSort(int[] arr) {
		int left = 0;
		int right = arr.length;
		while (left < right) {
			int min = arr[left];
			int minIdx = left;
			int max = arr[left];
			int maxIdx = left;
			for (int i = left + 1; i < right; i++) {
				if (arr[i] < min) {
					min = arr[i];
					minIdx = i;
				}
				if (arr[i] > max) {
					max = arr[i];
					maxIdx = i;
				}
			}
			if (left == maxIdx && right - 1 == minIdx) {
				swap(arr, left, right - 1);
			} else if (left == maxIdx) {
				swap(arr, right - 1, maxIdx);
				swap(arr, left, minIdx);
			} else {
				swap(arr, left, minIdx);
				swap(arr, right - 1, maxIdx);
			}
			left++;
			right--;
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
