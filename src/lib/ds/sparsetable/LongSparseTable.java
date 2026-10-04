package lib.ds.sparsetable;

import java.util.function.*;

/**
 * long 配列に対する冪等な区間演算を扱う Sparse Table。
 * <p>
 * 構築は {@code O(N log N)}、非空な半開区間クエリは {@code O(1)}。
 * 演算には結合則と冪等性が必要。
 */
@SuppressWarnings("unused")
public final class LongSparseTable {
	private final long[][] table;
	private final LongBinaryOperator operator;

	/**
	 * 入力配列をコピーして Sparse Table を構築する。
	 */
	public LongSparseTable(final long[] data, final LongBinaryOperator operator) {
		final int n = data.length, k = n <= 1 ? 0 : 31 - Integer.numberOfLeadingZeros(n);
		table = new long[k + 1][];
		table[0] = data.clone();
		this.operator = operator;
		for (int ki = 1; ki <= k; ki++) {
			final int width = 1 << ki, half = width >> 1;
			table[ki] = new long[n - width + 1];
			for (int i = 0; i + width <= n; i++) {
				table[ki][i] = operator.applyAsLong(table[ki - 1][i], table[ki - 1][i + half]);
			}
		}
	}

	/**
	 * 半開区間 {@code [l, r)} に演算を適用した結果を返す。区間は空でないこと。
	 */
	public long query(final int l, final int r) {
		final int k = 31 - Integer.numberOfLeadingZeros(r - l), offset = 1 << k;
		return operator.applyAsLong(table[k][l], table[k][r - offset]);
	}
}
