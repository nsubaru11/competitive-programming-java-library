package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * long値の配列を更新する共通インターフェース。添字は0始まり、区間は半開区間 {@code [l, r)} とする。
 * set は更新前の値、add・multiply・affine・apply は更新後の値を返し、配列全体を変更するメソッドは自身を返す。
 * affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
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

	default LongMutableArray swap(final int i, final int j) {
		set(i, set(j, get(i)));
		return this;
	}

	default LongMutableArray reverse() {
		return reverse(0, size());
	}

	default LongMutableArray reverse(int l, int r) {
		Objects.checkFromToIndex(l, r, size());
		while (l < --r) swap(l++, r);
		return this;
	}
}
