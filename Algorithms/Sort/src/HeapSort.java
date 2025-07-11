@SuppressWarnings("unused")
public class HeapSort {

	public static void heapSort(int[] arr) {
		int len = arr.length;
		for (int i = 0; i < len; i++) {
			int child = i;
			int parent = (i - 1) >> 1;
			while (child > 0 && arr[parent] < arr[child]) {
				swap(arr, child, parent);
				child = parent;
				parent = (child - 1) >> 1;
			}
		}
		for (int i = 0; i < len; i++) {
			int j = len - i - 1;
			swap(arr, 0, j);
			int k = 0;
			while (true) {
				int l = ((k + 1) << 1) - 1;
				if (l >= j) break;
				if (l + 1 < j && arr[l] < arr[l + 1]) {
					l++;
				}
				if (arr[k] < arr[l]) {
					swap(arr, k, l);
					k = l;
				} else {
					break;
				}
			}
		}
	}

	private static void swap(int[] data, int i, int j) {
		int temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}
}
