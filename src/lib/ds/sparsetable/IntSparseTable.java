package lib.ds.sparsetable;

import java.util.function.*;

import lib.math.*;

/**
 * int 配列に対する冪等な区間演算を扱う Sparse Table。
 * <p>
 * 構築は {@code O(N log N)}、非空な半開区間クエリは {@code O(1)}。
 * 演算には結合則と冪等性が必要。
 */
@SuppressWarnings("unused")
public final class IntSparseTable {
	private final int[][] table;
	private final int[] logTable;
	private final IntBinaryOperator operator;

	/**
	 * 入力配列をコピーして Sparse Table を構築する。
	 */
	public IntSparseTable(final int[] data, final IntBinaryOperator operator) {
		final int n = data.length, k = MathUtils.floorLog2(n);
		table = new int[k + 1][];
		logTable = new int[n + 1];
		table[0] = data.clone();
		this.operator = operator;
		for (int i = 2; i <= n; i++) logTable[i] = logTable[i >> 1] + 1;
		for (int ki = 1; ki <= k; ki++) {
			final int width = 1 << ki, half = width >> 1;
			table[ki] = new int[n - width + 1];
			for (int i = 0; i + width <= n; i++) {
				table[ki][i] = operator.applyAsInt(table[ki - 1][i], table[ki - 1][i + half]);
			}
		}
	}

	/**
	 * 半開区間 {@code [l, r)} に演算を適用した結果を返す。区間は空でないこと。
	 */
	public int query(final int l, final int r) {
		final int k = logTable[r - l];
		return operator.applyAsInt(table[k][l], table[k][r - (1 << k)]);
	}
}
