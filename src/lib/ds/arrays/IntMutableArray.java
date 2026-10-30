package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * int値の配列を更新する共通インターフェース。添字は0始まり、区間は半開区間 {@code [l, r)} とする。
 * set は更新前の値、add・multiply・affine・apply は更新後の値を返し、配列全体を変更するメソッドは自身を返す。
 * affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
 */
public interface IntMutableArray extends IntArray {

	int set(int i, int v);

	default int add(final int i, final int d) {
		return affine(i, 1, d);
	}

	default int multiply(final int i, final int m) {
		return affine(i, m, 0);
	}

	default int affine(final int i, final int m, final int d) {
		final int v = get(i) * m + d;
		set(i, v);
		return v;
	}

	default int apply(final int i, final int x, final IntBinaryOperator f) {
		final int v = f.applyAsInt(get(i), x);
		set(i, v);
		return v;
	}

	default IntMutableArray fill(final int v) {
		for (int i = 0, n = size(); i < n; i++) set(i, v);
		return this;
	}

	default IntMutableArray setAll(final IntUnaryOperator init) {
		for (int i = 0, n = size(); i < n; i++) set(i, init.applyAsInt(i));
		return this;
	}

	default IntMutableArray swap(final int i, final int j) {
		set(i, set(j, get(i)));
		return this;
	}

	default IntMutableArray reverse() {
		return reverse(0, size());
	}

	default IntMutableArray reverse(int l, int r) {
		Objects.checkFromToIndex(l, r, size());
		while (l < --r) swap(l++, r);
		return this;
	}
}
