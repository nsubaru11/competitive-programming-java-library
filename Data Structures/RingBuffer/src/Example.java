public class Example {

	public static void main(String[] args) {
		// リングバッファのインスタンスを作成します。
		// 容量として5を指定しますが、内部で2のべき乗に正規化されるため、実際の容量は8になります。
		RingBuffer<Integer> buffer = new RingBuffer<>(5);
		System.out.println("--- RingBuffer Example ---");
		System.out.println("Initialized a RingBuffer with requested capacity 5.");
		System.out.println("Actual capacity (normalized to power of 2): " + buffer.capacity());
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
		System.out.println("\n3. Peeking elements");
		System.out.println("Peek first element: " + buffer.peekFirst());
		System.out.println("Peek last element: " + buffer.peekLast());
		System.out.println("Buffer content after peeking: " + buffer);

		// 要素を取得して削除します。
		System.out.println("\n4. Polling elements");
		System.out.println("Poll first element: " + buffer.pollFirst());
		System.out.println("Poll last element: " + buffer.pollLast());
		System.out.println("Buffer content after polling: " + buffer);
		System.out.println("Current size: " + buffer.size());

		// バッファをいっぱいにします。
		System.out.println("\n5. Filling the buffer");
		System.out.println("Remaining capacity: " + buffer.remainingCapacity());
		for (int i = 1; i <= 6; i++) {
			buffer.addLast(i * 100);
		}
		System.out.println("Buffer content: " + buffer);
		System.out.println("Current size: " + buffer.size());
		System.out.println("Is full? " + buffer.isFull());

		// 満杯の状態で要素を追加しようとすると失敗します。
		System.out.println("\n6. Attempting to add to a full buffer");
		boolean added = buffer.addLast(999);
		System.out.println("addLast(999) returned: " + added);
		System.out.println("Buffer content: " + buffer);

		// インデックスを指定して要素にアクセスします。
		System.out.println("\n7. Accessing elements by index (get)");
		System.out.println("Element at index 0: " + buffer.get(0));
		System.out.println("Element at index 3: " + buffer.get(3));
		// 負のインデックスは末尾からの位置を示します。
		System.out.println("Element at index -1 (last element): " + buffer.get(-1));

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
