package lib.ds.arrays;

import java.util.function.*;

/**
 * long値を格納する可変配列の共通インターフェースです。
 */
public interface LongMutableArray extends LongArray {

	/**
	 * 指定位置を更新し、更新前の値を返します。
	 */
	long set(final int i, final long v);

	/**
	 * 全要素を指定値で更新します。
	 */
	default void fill(final long v) {
		for (int i = 0, n = size(); i < n; i++) set(i, v);
	}

	/**
	 * 各論理添字に対して生成した値で全要素を更新します。
	 */
	default void setAll(final IntToLongFunction init) {
		for (int i = 0, n = size(); i < n; i++) set(i, init.applyAsLong(i));
	}
}
