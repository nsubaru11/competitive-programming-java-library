package lib.ds;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * int値を保持するコレクションの読み取り用インターフェースである。
 * 走査とそれを使う一括処理の実行中は、要素数や走査順を変更しないこと。
 */
public interface IntCollection extends Iterable<Integer> {

	int size();

	/**
	 * プリミティブ値を走査する iterator を返す。走査順は実装に依存する。
	 */
	PrimitiveIterator.OfInt iterator();

	default boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 指定値を含むかを線形探索で判定する。
	 */
	default boolean contains(final int v) {
		for (final PrimitiveIterator.OfInt it = iterator(); it.hasNext(); ) {
			if (it.nextInt() == v) return true;
		}
		return false;
	}

	/**
	 * iterator の走査順で各要素に action を適用する。
	 */
	default void forEachInt(final IntConsumer action) {
		iterator().forEachRemaining(action);
	}

	/**
	 * SIZED・SUBSIZED の spliterator を返す。既定実装は ORDERED を指定しない。
	 */
	default Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size(), 0);
	}

	/**
	 * 逐次実行のプリミティブ Stream を返す。
	 */
	default IntStream intStream() {
		return StreamSupport.intStream(spliterator(), false);
	}

	/**
	 * iterator の走査順にボックス化した、変更不可の List を返す。
	 */
	default List<Integer> toList() {
		return intStream().boxed().toList();
	}

	/**
	 * iterator の走査順で独立したプリミティブ配列へコピーする。
	 */
	default int[] toArray() {
		final int len = size();
		final int[] a = new int[len];
		final PrimitiveIterator.OfInt it = iterator();
		for (int i = 0; i < len; i++) a[i] = it.nextInt();
		return a;
	}
}
