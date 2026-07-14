package lib.ds.arrays;

import java.util.*;

import lib.ds.*;

/**
 * long値を格納する配列の読み取り用インターフェースです。
 */
public interface LongArray extends LongCollection {

	/**
	 * 指定位置の値を返します。
	 */
	long get(final int i);

	default Spliterator.OfLong spliterator() {
		final int characteristic = Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED;
		return Spliterators.spliterator(iterator(), size(), characteristic);
	}
}
