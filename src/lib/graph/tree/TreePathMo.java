package lib.graph.tree;

/**
 * Euler Tour 上の区間に変換した木上の静的パスクエリを Mo's Algorithm でオフライン処理する。
 * 木の頂点数を {@code N}、クエリ数を {@code Q}、区間移動1回の処理時間を {@code C} とすると、
 * ブロック幅 {@code B} に対する計算量は概ね {@code O(Q log Q + (QB + N^2 / B) C)}、
 * 空間計算量は {@code O(N + Q)}。
 */
@SuppressWarnings("unused")
public final class TreePathMo {
	// TODO: EulerTour上の区間変換、LCAの一時反転、クエリ順序とcallback APIを設計する
}
