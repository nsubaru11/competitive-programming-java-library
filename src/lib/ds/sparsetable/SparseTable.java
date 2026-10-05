package lib.ds.sparsetable;

import java.util.*;
import java.util.function.*;

import lib.math.*;

/**
 * 静的配列に対する冪等な区間演算を扱う Sparse Table。
 * <p>
 * 構築は {@code O(N log N)}、非空な半開区間クエリは {@code O(1)}。
 * 演算には結合則と冪等性が必要。
 */
@SuppressWarnings({"unused", "unchecked"})
public final class SparseTable<T> {
	private final T[][] table;
	private final int[] logTable;
	private final BinaryOperator<T> operator;

	/**
	 * 入力配列をコピーして Sparse Table を構築する。
	 */
	public SparseTable(final T[] data, final BinaryOperator<T> operator) {
		final int n = data.length, k = MathUtils.floorLog2(n);
		table = (T[][]) new Object[k + 1][];
		logTable = new int[n + 1];
		table[0] = data.clone();
		this.operator = operator;
		for (int i = 2; i <= n; i++) logTable[i] = logTable[i >> 1] + 1;
		for (int ki = 1; ki <= k; ki++) {
			final int width = 1 << ki, half = width >> 1;
			table[ki] = (T[]) new Object[n - width + 1];
			for (int i = 0; i + width <= n; i++) {
				table[ki][i] = operator.apply(table[ki - 1][i], table[ki - 1][i + half]);
			}
		}
	}

	/**
	 * 半開区間 {@code [l, r)} に演算を適用した結果を返す。区間は空でないこと。
	 */
	public T query(final int l, final int r) {
		final int k = logTable[r - l];
		return operator.apply(table[k][l], table[k][r - (1 << k)]);
	}
}
