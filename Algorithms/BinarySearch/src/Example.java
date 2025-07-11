import java.util.Arrays;

/**
 * BinarySearchとArrayBinarySearchの使用例とテストケース
 */
public class Example {

	public static void main(String[] args) {
		// 両方のBinarySearchクラスの機能をテスト
		testArrayBinarySearch();
		testBinarySearch();

	}

	/**
	 * ArrayBinarySearchの使用例とテスト
	 */
	private static void testArrayBinarySearch() {
		System.out.println("\n===== ArrayBinarySearch テスト =====");

		// テスト用の整数配列
		int[] sortedArray = {10, 20, 30, 30, 50, 60, 70, 70, 70};
		System.out.println("配列: " + Arrays.toString(sortedArray));
		System.out.println("--------------------------------");

		// 通常の二分探索（成功ケース）
		int target = 30;
		int index = ArrayBinarySearch.normalSearch(sortedArray, target);
		System.out.println("【成功】通常探索: 値 " + target + " のインデックス = " + index + " → 値: " + sortedArray[index]);

		// 存在しない値の探索（失敗ケース）
		target = 35;
		index = ArrayBinarySearch.normalSearch(sortedArray, target);
		System.out.print("【失敗】通常探索: 存在しない値 " + target + " の結果 = " + index);
		int insertPos = -index - 1; // 挿入位置の計算
		System.out.println(", 挿入位置 = " + insertPos + " (この位置に挿入すると配列はソート状態を維持する)");
		System.out.println("--------------------------------");

		// 上限探索(Upper Bound)
		target = 30;
		index = ArrayBinarySearch.upperBoundSearch(sortedArray, target);
		System.out.println("上限探索: " + target + " 以上の最小値のインデックス = " + index + " → 値: " + sortedArray[index]);

		target = 70;
		index = ArrayBinarySearch.upperBoundSearch(sortedArray, target);
		System.out.println("上限探索: " + target + " 以上の最小値のインデックス = " + index + " → 値: " + sortedArray[index]);

		target = 100;
		index = ArrayBinarySearch.upperBoundSearch(sortedArray, target);
		System.out.print("上限探索: " + target + " 以上の最小値のインデックス = " + index);
		if (index < 0) {
			insertPos = -index - 1; // 挿入位置の計算
			System.out.println(", 挿入位置 = " + insertPos + " (検索値が配列の最大値よりも大きい)");
		} else {
			System.out.println(" → 値: " + sortedArray[index]);
		}
		System.out.println("--------------------------------");

		// 下限探索(Lower Bound)
		target = 30;
		index = ArrayBinarySearch.lowerBoundSearch(sortedArray, 0, sortedArray.length, target);
		System.out.println("下限探索: " + target + " 以下の最大値のインデックス = " + index + " → 値: " + sortedArray[index]);

		target = 70;
		index = ArrayBinarySearch.lowerBoundSearch(sortedArray, 0, sortedArray.length, target);
		System.out.println("下限探索: " + target + " 以下の最大値のインデックス = " + index + " → 値: " + sortedArray[index]);

		target = 0;
		index = ArrayBinarySearch.lowerBoundSearch(sortedArray, target);
		System.out.print("下限探索: " + target + " 以下の最大値のインデックス = " + index);
		if (index < 0) {
			insertPos = -index - 1; // 挿入位置の計算
			System.out.println(", 挿入位置 = " + insertPos + " (検索値が配列の最小値よりも小さい)");
		} else {
			System.out.println(" → 値: " + sortedArray[index]);
		}
		System.out.println("--------------------------------");
		try {
			ArrayBinarySearch.normalSearch(sortedArray, -1, sortedArray.length + 1, 10);
		} catch (Exception e) {
			System.out.println("【例外テスト】範囲外: " + e.getMessage());
		}
	}

	/**
	 * BinarySearchの使用例とテスト
	 */
	private static void testBinarySearch() {
		System.out.println("\n===== BinarySearch テスト =====");

		// カスタム比較関数での通常の二分探索 (12345678 < x⁵ < 1234567890 を満たす x を探索)
		int target2 = 1234567890;
		int result = BinarySearch.normalSearch(0, 100, i -> {
			long k = i * i * i * i * i;
			return target2 / 100 < k && k < target2 ? 0 : k >= target2 ? 1 : -1;
		});
		System.out.println("12345678 < x⁵ < 1234567890 を満たす値 x = " + result);
		System.out.println("検証:" + result + "⁵ = " + (result * result * result * result * result));

		// カスタム比較関数での下限探索 (12345678 < x⁵ < 1234567890 を満たす最小の x を探索)
		result = BinarySearch.lowerBoundSearch(0, 100, i -> {
			long k = i * i * i * i * i;
			return target2 / 100 < k && k < target2 ? 0 : k >= target2 ? 1 : -1;
		});
		System.out.println("12345678 < x⁵ < 1234567890 を満たす最小の x = " + result);
		System.out.println("検証:" + result + "⁵ = " + (result * result * result * result * result));

		// カスタム比較関数での上限探索 (12345678 < x⁵ < 1234567890 を満たす最大の x を探索)
		result = BinarySearch.upperBoundSearch(0, 100, i -> {
			long k = i * i * i * i * i;
			return target2 / 100 < k && k < target2 ? 0 : k >= target2 ? 1 : -1;
		});
		System.out.println("12345678 < x⁵ < 1234567890 を満たす最大の x = " + result);
		System.out.println("検証:" + result + "⁵ = " + (result * result * result * result * result));
	}
}