# TopologicalShortestPath 利用ガイド

## 概要

`TopologicalShortestPath`は、`DirectedGraph`がDAG（有向非巡回グラフ）であることを利用して、単一始点または複数始点からの最短経路・最長経路を計算する静的ユーティリティクラスです。
トポロジカル順に辺を緩和するため、負辺を含むDAGでも動作します。

## 特徴

- `DirectedGraph`専用
- トポロジカル順に基づく`O(V + E)`の計算
- 最短経路と最長経路の両方に対応
- 単一始点・複数始点に対応
- `PathResult`による距離参照と経路復元
- グラフに閉路がある場合は`IllegalArgumentException`を送出

## 依存関係

- [`DirectedGraph`](../Core/DirectedGraphGuide.md)
- [`GraphUtils`](../Core/GraphUtilsGuide.md)
- [`PathResult`](./PathResultGuide.md)

## 主な機能（メソッド一覧）

### 1. 最短経路

| メソッド                                  | 戻り値の型   | 説明                                               |
|-------------------------------------------|--------------|----------------------------------------------------|
| `solve(DirectedGraph graph, int s)`       | `PathResult` | 始点`s`から全頂点への最短距離と親を計算            |
| `solve(DirectedGraph graph, int... s)`    | `PathResult` | 複数始点のいずれかから全頂点への最短距離と親を計算 |
| `dist(DirectedGraph graph, int s)`        | `long[]`     | 始点`s`から全頂点への最短距離を返す                |
| `dist(DirectedGraph graph, int... s)`     | `long[]`     | 複数始点のいずれかから全頂点への最短距離を返す     |
| `path(DirectedGraph graph, int s, int g)` | `int[]`      | 始点`s`から終点`g`への最短経路を返す               |

### 2. 最長経路

| メソッド                                      | 戻り値の型   | 説明                                               |
|-----------------------------------------------|--------------|----------------------------------------------------|
| `solveLongest(DirectedGraph graph, int s)`    | `PathResult` | 始点`s`から全頂点への最長距離と親を計算            |
| `solveLongest(DirectedGraph graph, int... s)` | `PathResult` | 複数始点のいずれかから全頂点への最長距離と親を計算 |

最長経路では、`solveLongest`の戻り値に対して`distTo`や`pathTo`を呼び出します。
最長距離専用の`dist`・`path`メソッドはありません。

## 利用例

```java
import lib.graph.DirectedGraph;
import lib.graph.PathResult;
import lib.graph.TopologicalShortestPath;

DirectedGraph graph = new DirectedGraph(5, 5);
graph.add(0, 1, 2);
graph.add(0, 2, 5);
graph.add(1, 3, 3);
graph.add(2, 3, 1);
graph.add(3, 4, 4);

PathResult shortest = TopologicalShortestPath.solve(graph, 0);
PathResult longest = TopologicalShortestPath.solveLongest(graph, 0);
long min = shortest.distTo(4); // 9
long max = longest.distTo(4);  // 10
int[] path = longest.pathTo(4); // {0, 2, 3, 4}
```

## 注意事項

- 入力グラフはDAGである必要があります。閉路が1つでも存在すると`IllegalArgumentException`になります。
- 頂点番号は0-indexedです。
- 最短経路では負辺を使用できます。DAGである限り、負閉路は存在しません。
- 到達不能な頂点の距離は`Long.MAX_VALUE`、`parent`は`-1`、`pathTo`は`null`です。
- `solveLongest`でも到達不能な頂点の距離は`Long.MAX_VALUE`です。
- 複数始点版では、すべての始点の初期距離を0として計算します。
- 結果の`hasNegCycle`は常に`false`です。

## パフォーマンス特性

- トポロジカルソート: `O(V + E)`
- 最短経路・最長経路の緩和: `O(V + E)`
- 全体の時間計算量: `O(V + E)`
- 追加領域: `O(V)`
- `pathTo`: 経路長に比例する配列を作成し、最悪`O(V)`

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                              |
|:-------------------|:-----------|:--------------------------------------------------|
| **バージョン 1.0** | 2026-08-23 | DAGの単一始点・複数始点の最短経路・最長経路を追加 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
