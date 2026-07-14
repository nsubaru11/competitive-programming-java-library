package lib.ds;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * long値を保持するコレクションの読み取り用インターフェースです。
 */
public interface LongCollection extends Iterable<Long> {

	int size();

	PrimitiveIterator.OfLong iterator();

	default boolean isEmpty() {
		return size() == 0;
	}

	default boolean contains(final long v) {
		for (final PrimitiveIterator.OfLong it = iterator(); it.hasNext(); ) {
			if (it.nextLong() == v) return true;
		}
		return false;
	}

	default void forEachLong(final LongConsumer action) {
		iterator().forEachRemaining(action);
	}

	default Spliterator.OfLong spliterator() {
		final int characteristic = Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED;
		return Spliterators.spliterator(iterator(), size(), characteristic);
	}

	default LongStream longStream() {
		return StreamSupport.longStream(spliterator(), false);
	}

	default List<Long> toList() {
		return longStream().boxed().toList();
	}

	default long[] toArray() {
		final int len = size();
		final long[] a = new long[len];
		final PrimitiveIterator.OfLong it = iterator();
		for (int i = 0; i < len; i++) a[i] = it.nextLong();
		return a;
	}
}
