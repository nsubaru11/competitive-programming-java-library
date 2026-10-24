package lib.ds.arrays;

import java.util.function.*;

/**
 * long値を格納する可変配列の共通インターフェースである。
 */
public interface LongMutableArray extends LongArray {

	/**
	 * 指定位置を更新し、更新前の値を返す。
	 *
	 * @param i 論理添字。{@code 0 <= i < size()}
	 */
	long set(int i, long v);

	/**
	 * 全要素を指定値で更新する。要素数は変えない。
	 */
	default void fill(final long v) {
		for (int i = 0, n = size(); i < n; i++) set(i, v);
	}

	/**
	 * 各論理添字に対して生成した値で全要素を更新する。
	 * init は添字の昇順に1回ずつ呼ぶ。
	 *
	 * @param init 論理添字から新しい値を生成する関数
	 */
	default void setAll(final IntToLongFunction init) {
		for (int i = 0, n = size(); i < n; i++) set(i, init.applyAsLong(i));
	}
}
