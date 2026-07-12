package lib.ds.arrays;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * long値を格納する配列の共通インターフェースです。
 */
@SuppressWarnings("unused")
public interface LongArray extends Iterable<Long> {

	/**
	 * 指定位置の値を返します。
	 */
	long get(final int i);

	/**
	 * 指定位置を更新し、更新前の値を返します。
	 */
	long set(final int i, final long v);

	int size();

	/**
	 * 論理順序でコピーした配列を返します。
	 */
	long[] toArray();

	default boolean contains(final long value) {
		for (final var it = iterator(); it.hasNext(); ) {
			if (it.nextLong() == value) return true;
		}
		return false;
	}

	default boolean isEmpty() {
		return size() == 0;
	}

	PrimitiveIterator.OfLong iterator();

	default List<Long> toList() {
		return longStream().boxed().toList();
	}

	default void forEachLong(final LongConsumer action) {
		iterator().forEachRemaining(action);
	}

	/**
	 * 論理順序のLongStreamを返します。
	 */
	default LongStream longStream() {
		int characteristics = Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED;
		return StreamSupport.longStream(Spliterators.spliterator(iterator(), size(), characteristics), false);
	}
}
