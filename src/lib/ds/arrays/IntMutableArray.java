package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * int値を格納する可変配列の共通インターフェースである。
 * 単一要素の添字は {@code [0, size())} とし、範囲外の動作は get・set の実装に依存する。
 */
public interface IntMutableArray extends IntArray {

	/**
	 * 指定位置を更新し、更新前の値を返す。
	 *
	 * @param i 論理添字。{@code 0 <= i < size()}
	 */
	int set(int i, int v);

	/**
	 * 指定した論理位置に d を加算し、更新後の値を返す。
	 */
	default int add(final int i, final int d) {
		return affine(i, 1, d);
	}

	/**
	 * 指定した論理位置を m 倍し、更新後の値を返す。
	 */
	default int multiply(final int i, final int m) {
		return affine(i, m, 0);
	}

	/**
	 * 指定した論理位置を {@code 現在値 * m + d} に更新し、更新後の値を返す。
	 */
	default int affine(final int i, final int m, final int d) {
		final int v = get(i) * m + d;
		set(i, v);
		return v;
	}

	/**
	 * 指定した論理位置を {@code f(現在値, x)} に更新し、更新後の値を返す。
	 */
	default int apply(final int i, final int x, final IntBinaryOperator f) {
		final int v = f.applyAsInt(get(i), x);
		set(i, v);
		return v;
	}

	/**
	 * 指定した2要素を交換する。
	 */
	default void swap(final int i, final int j) {
		set(i, set(j, get(i)));
	}

	/**
	 * 全要素の順序を反転する。
	 */
	default void reverse() {
		reverse(0, size());
	}

	/**
	 * 半開区間 {@code [i, j)} の順序を反転する。
	 *
	 * @throws IndexOutOfBoundsException {@code 0 <= i <= j <= size()} を満たさない場合
	 */
	default void reverse(int i, int j) {
		Objects.checkFromToIndex(i, j, size());
		while (i < --j) swap(i++, j);
	}

	/**
	 * 全要素を指定値で更新する。要素数は変えない。
	 */
	default void fill(final int v) {
		for (int i = 0, n = size(); i < n; i++) set(i, v);
	}

	/**
	 * 各論理添字に対して生成した値で全要素を更新する。
	 * init は添字の昇順に1回ずつ呼ぶ。
	 *
	 * @param init 論理添字から新しい値を生成する関数
	 */
	default void setAll(final IntUnaryOperator init) {
		for (int i = 0, n = size(); i < n; i++) set(i, init.applyAsInt(i));
	}
}
