package example.ds.arrays;

import lib.ds.arrays.*;
import lib.util.*;

public final class Test {

	public static void main(final String[] args) {
		testArray();
		testDeque();
	}

	private static void testArray() {
		// IntCircularArray: 循環シフト後に ArrayUtils で集計
		IntCircularArray ia = new IntCircularArray(5);
		ia.setAll(i -> i + 1); // [1,2,3,4,5]
		ia.rotateLeft(); // 論理的に [2,3,4,5,1]
		System.out.println("IntCircularArray sum = " + ArrayUtils.sum(ia)); // 1+2+3+4+5=15

		// LongCircularArray: 部分和（任意個）
		LongCircularArray la = new LongCircularArray(4);
		la.setAll(i -> i * 2L + 1); // [1,3,5,7]
		System.out.println("LongCircularArray subset(target=8) = " + ArrayUtils.subsetMitm(la, 8)); // 1+7, 3+5

		// IntArray2D: 回転してアクセス
		IntArray2D gI = new IntArray2D(2, 3);
		gI.setAll((i, j) -> i * 10 + j); // [[0,1,2],[10,11,12]]
		gI.rotateLeft(); // 左回転
		System.out.println("IntArray2D get(0,0) after rotateLeft = " + gI.get(0, 0));

		// LongArray2D: 値の設定と取得
		LongArray2D gL = new LongArray2D(2, 2);
		gL.set(1, 1, 42L);
		System.out.println("LongArray2D get(1,1) = " + gL.get(1, 1));
	}

	private static void testDeque() {
		// 初期容量5のdequeを作成します。内部容量は8に正規化されます。
		IntArrayDeque buffer = new IntArrayDeque(5);
		System.out.println("--- IntArrayDeque Example ---");
		System.out.println("Initialized an IntArrayDeque with requested capacity 5.");
		System.out.println("Is empty? " + buffer.isEmpty());

		// 要素を末尾に追加します。
		System.out.println("\n1. Adding elements to the end (addLast)");
		buffer.addLast(10);
		buffer.addLast(20);
		buffer.addLast(30);
		System.out.println("Buffer content: " + buffer);
		System.out.println("Current size: " + buffer.size());

		// 要素を先頭に追加します。
		System.out.println("\n2. Adding an element to the front (addFirst)");
		buffer.addFirst(5);
		System.out.println("Buffer content: " + buffer);
		System.out.println("Current size: " + buffer.size());

		// 要素を取得します (削除はしません)。
		System.out.println("\n3. Getting elements");
		System.out.println("Get first element: " + buffer.getFirst());
		System.out.println("Get last element: " + buffer.getLast());
		System.out.println("Buffer content after peeking: " + buffer);

		// 要素を取得して削除します。
		System.out.println("\n4. Removing elements");
		System.out.println("Remove first element: " + buffer.removeFirst());
		System.out.println("Remove last element: " + buffer.removeLast());
		System.out.println("Buffer content after polling: " + buffer);
		System.out.println("Current size: " + buffer.size());

		// インデックスを指定して要素にアクセスします。
		System.out.println("\n7. Accessing elements by index (get)");
		System.out.println("Element at index 0: " + buffer.get(0));
		System.out.println("Element at index 1: " + buffer.get(1));
		// 末尾の添字は size() - 1 である。
		System.out.println("Element at index size() - 1 (last element): " + buffer.get(buffer.size() - 1));

		// インデックスを指定して要素を更新します。
		System.out.println("\n8. Updating an element by index (set)");
		buffer.set(0, 99);
		System.out.println("Set element at index 0 to 99.");
		System.out.println("Buffer content: " + buffer);

		// 拡張for文でバッファを走査します。
		System.out.println("\n9. Iterating through the buffer");
		System.out.print("Elements: ");
		for (int val : buffer) {
			System.out.print(val + " ");
		}
		System.out.println();

		// バッファをクリアします。
		System.out.println("\n10. Clearing the buffer");
		buffer.clear();
		System.out.println("Current size after clear: " + buffer.size());
		System.out.println("Is empty? " + buffer.isEmpty());
	}
}
