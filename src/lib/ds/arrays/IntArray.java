package lib.ds.arrays;

import java.util.*;

import lib.ds.*;

/**
 * int値を格納する配列の読み取り用インターフェースです。
 */
public interface IntArray extends IntCollection {

	/**
	 * 指定位置の値を返します。
	 */
	int get(final int i);

	default Spliterator.OfInt spliterator() {
		final int characteristic = Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED;
		return Spliterators.spliterator(iterator(), size(), characteristic);
	}
}
