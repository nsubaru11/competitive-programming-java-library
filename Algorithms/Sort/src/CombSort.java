/**
 * 整数配列を昇順にソートするコムソート（Comb Sort）アルゴリズムを提供するクラスです。
 * コムソートはバブルソートを改良したアルゴリズムであり、要素を比較する間隔（gap）を徐々に縮めることで効率を向上させます。
 * 主に大規模な配列のソートで効果的に使用できます。
 */
@SuppressWarnings("unused")
public class CombSort {
	private static final double SHRINK_FACTOR = 1.3;

	/**
	 * 標準的なコムソートアルゴリズムにより配列を昇順にソートします。<br>
	 * ギャップは固定比率（約1.3倍）で徐々に縮小されていき、最後のギャップが1の場合は単純なバブルソートとなります。
	 *
	 * @param arr ソート対象の整数配列
	 */
	public static void standardCombSort(int[] arr) {
		int len = arr.length;
		int gap = len;
		boolean sorted = false;
		while (gap > 1 || !sorted) {
			gap = (int) (gap / SHRINK_FACTOR);
			if (gap < 1) gap = 1;

			sorted = true;
			for (int i = 0; i + gap < len; i++) {
				if (arr[i] > arr[i + gap]) {
					swap(arr, i, i + gap);
					sorted = false;
				}
			}
		}
	}

	/**
	 * 改良型コムソートアルゴリズムを使用して配列を昇順にソートします。<br>
	 * 基本型に対して特定のギャップ数値（9,10）で性能の低下が起こることがあるため、
	 * 処理時間を短縮するためにそれらをスキップして調整しています。
	 *
	 * @param arr ソート対象の整数配列
	 */
	public static void optimizedCombSort(int[] arr) {
		int len = arr.length;
		int gap = len;
		boolean sorted = false;
		while (gap > 1 || !sorted) {
			gap = (int) (gap / SHRINK_FACTOR);
			if (gap == 9 || gap == 10) {
				gap = 11; // gapが9,10の場合は性能劣化を防ぐため11に強制調整
			}
			if (gap < 1) gap = 1;

			sorted = true;
			for (int i = 0; i + gap < len; i++) {
				if (arr[i] > arr[i + gap]) {
					swap(arr, i, i + gap);
					sorted = false;
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