package lib.ds;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * int値を保持するコレクションの読み取り用インターフェースです。
 */
public interface IntCollection extends Iterable<Integer> {

	int size();

	PrimitiveIterator.OfInt iterator();

	default boolean isEmpty() {
		return size() == 0;
	}

	default boolean contains(final int v) {
		for (final PrimitiveIterator.OfInt it = iterator(); it.hasNext(); ) {
			if (it.nextInt() == v) return true;
		}
		return false;
	}

	default void forEachInt(final IntConsumer action) {
		iterator().forEachRemaining(action);
	}

	default Spliterator.OfInt spliterator() {
		final int characteristic = Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED;
		return Spliterators.spliterator(iterator(), size(), characteristic);
	}

	default IntStream intStream() {
		return StreamSupport.intStream(spliterator(), false);
	}

	default List<Integer> toList() {
		return intStream().boxed().toList();
	}

	default int[] toArray() {
		final int len = size();
		final int[] a = new int[len];
		final PrimitiveIterator.OfInt it = iterator();
		for (int i = 0; i < len; i++) a[i] = it.nextInt();
		return a;
	}

}
