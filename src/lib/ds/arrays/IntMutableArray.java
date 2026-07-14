package lib.ds.arrays;

import java.util.function.*;

/**
 * int値を格納する可変配列の共通インターフェースです。
 */
public interface IntMutableArray extends IntArray {

	/**
	 * 指定位置を更新し、更新前の値を返します。
	 */
	int set(final int i, final int v);

	/**
	 * 全要素を指定値で更新します。
	 */
	default void fill(final int v) {
		for (int i = 0, n = size(); i < n; i++) set(i, v);
	}

	/**
	 * 各論理添字に対して生成した値で全要素を更新します。
	 */
	default void setAll(final IntUnaryOperator init) {
		for (int i = 0, n = size(); i < n; i++) set(i, init.applyAsInt(i));
	}
}
