package lib.ds.arrays;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * int値を格納する配列の共通インターフェースです。
 */
@SuppressWarnings("unused")
public interface IntArray extends Iterable<Integer> {

	/**
	 * 指定位置の値を返します。
	 */
	int get(final int i);

	/**
	 * 指定位置を更新し、更新前の値を返します。
	 */
	int set(final int i, final int v);

	int size();

	/**
	 * 論理順序でコピーした配列を返します。
	 */
	int[] toArray();

	default boolean contains(final int value) {
		for (final var it = iterator(); it.hasNext(); ) {
			if (it.nextInt() == value) return true;
		}
		return false;
	}

	default boolean isEmpty() {
		return size() == 0;
	}

	@Override
	PrimitiveIterator.OfInt iterator();

	default List<Integer> toList() {
		return intStream().boxed().toList();
	}

	default void forEachInt(final IntConsumer action) {
		iterator().forEachRemaining(action);
	}

	/**
	 * 論理順序のIntStreamを返します。
	 */
	default IntStream intStream() {
		int characteristics = Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED;
		return StreamSupport.intStream(Spliterators.spliterator(iterator(), size(), characteristics), false);
	}

}
