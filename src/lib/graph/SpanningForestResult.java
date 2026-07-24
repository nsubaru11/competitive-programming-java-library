package lib.graph;

import java.util.*;

/**
 * 最小または最大全域森の計算結果。
 * <p>
 * {@link #edgeIds} は入力された {@link UndirectedGraph} の論理辺IDであり、
 * 辺を追加した順序に対応します。グラフが連結なら {@link #isSpanningTree()} は
 * {@code true} となり、採用辺数は通常 {@code |V| - 1} です。
 */
public final class SpanningForestResult {
	/** 全域森を構成する辺の総コスト。 */
	public final long cost;

	/** 全域森に採用された、入力グラフ上の論理辺ID。 */
	public final int[] edgeIds;

	/** 入力グラフの連結成分数。 */
	public final int componentCount;

	SpanningForestResult(final long cost, final int[] edgeIds, final int componentCount) {
		this.cost = cost;
		this.edgeIds = edgeIds;
		this.componentCount = componentCount;
	}

	/**
	 * 採用された辺数を返します。
	 *
	 * @return {@link #edgeIds} の長さ
	 */
	public int edgeCount() {
		return edgeIds.length;
	}

	/**
	 * 結果が全域森ではなく1本の全域木になっているかを返します。
	 *
	 * @return 入力グラフが連結なら {@code true}
	 */
	public boolean isSpanningTree() {
		return componentCount == 1;
	}

	@Override
	public String toString() {
		return "cost: " + cost + '\n' +
				"edgeIds: " + Arrays.toString(edgeIds) + '\n' +
				"componentCount: " + componentCount;
	}
}
