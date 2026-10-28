package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * long値を格納する可変配列の共通インターフェースである。単一要素の添字は {@code [0, size())} とし、範囲外の動作は get・set の実装に依存する。
 * set は更新前の値、add・multiply・affine・apply は更新後の値を返す。affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
 * {@code reverse(l, r)} は半開区間 {@code [l, r)} を反転し、{@code 0 <= l <= r <= size()} を満たさない場合は IndexOutOfBoundsException を投げる。
 * fill・setAll は要素数を変えずに自身を返す。setAll の init は添字の昇順に1回ずつ呼ぶ。
 */
public interface LongMutableArray extends LongArray {

	long set(int i, long v);

	default long add(final int i, final long d) {
		return affine(i, 1, d);
	}

	default long multiply(final int i, final long m) {
		return affine(i, m, 0);
	}

	default long affine(final int i, final long m, final long d) {
		final long v = get(i) * m + d;
		set(i, v);
		return v;
	}

	default long apply(final int i, final long x, final LongBinaryOperator f) {
		final long v = f.applyAsLong(get(i), x);
		set(i, v);
		return v;
	}

	default LongMutableArray fill(final long v) {
		for (int i = 0, n = size(); i < n; i++) set(i, v);
		return this;
	}

	default LongMutableArray setAll(final IntToLongFunction init) {
		for (int i = 0, n = size(); i < n; i++) set(i, init.applyAsLong(i));
		return this;
	}

	default void swap(final int i, final int j) {
		set(i, set(j, get(i)));
	}

	default void reverse() {
		reverse(0, size());
	}

	default void reverse(int l, int r) {
		Objects.checkFromToIndex(l, r, size());
		while (l < --r) swap(l++, r);
	}
}
