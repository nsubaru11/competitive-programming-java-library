/**
 * 配列を昇順にソートするバブルソートアルゴリズムを提供するクラスです。
 * ソート効率を考慮し、最適化されたバージョンも提供します。
 */
@SuppressWarnings("unused")
public class BubbleSort {

	/**
	 * 標準的なバブルソートアルゴリズムにより配列を昇順にソートします。<br>
	 * 途中でソート完了が判明した場合は早期終了します。
	 *
	 * @param arr ソート対象の整数配列
	 */
	public static void basicBubbleSort(int[] arr) {
		int len = arr.length;
		for (int i = 0; i < len; i++) {
			boolean sorted = true;
			for (int j = 1; j < len - i; j++) {
				if (arr[j - 1] > arr[j]) {
					swap(arr, j - 1, j);
					sorted = false;
				}
			}
			if (sorted) break;
		}
	}

	/**
	 * 改良版バブルソートアルゴリズムを使用して配列を昇順にソートします。<br>
	 * 最後に交換が行われた位置を記録し、その位置以降の要素は次回のループから処理を省略します。
	 * これによりソート範囲が回避され、効率性が向上します。
	 *
	 * @param arr ソート対象の整数配列
	 */
	public static void bubbleSort(int[] arr) {
		int len = arr.length;
		for (int i = 0, right = len; i < len; i++) {
			boolean sorted = true;
			int r = 0;
			for (int j = 1; j < right; j++) {
				if (arr[j - 1] > arr[j]) {
					r = j;
					swap(arr, j - 1, j);
					sorted = false;
				}
			}
			if (sorted) break;
			right = r;
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}
