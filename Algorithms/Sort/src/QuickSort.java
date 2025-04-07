@SuppressWarnings("unused")
public class QuickSort {

	public static void quickSort(int[] data, int left, int right) {
		if (right <= left) return;
		int l = left;
		int r = right;
		int mid = (left + right) / 2;
		int pivot = data[mid];
		while (l < r) {
			while (l < r && pivot > data[l]) l++;
			while (l < r && pivot <= data[r]) r--;
			if (l >= r) break;
			swap(data, l, r);
			l++;
			r--;
		}
		while (r < right && data[r] <= pivot) r++;
		if (mid > r) {
			swap(data, mid, r);
			r += 1;
		}
		quickSort(data, left, l);
		quickSort(data, r, right);
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}
