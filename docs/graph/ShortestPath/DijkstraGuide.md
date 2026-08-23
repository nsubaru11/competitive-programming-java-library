# Dijkstra 利用ガイド

## 概要

`Dijkstra`は、非負重み付き`Graph`に対して単一始点または複数始点の最短経路を計算する静的ユーティリティクラスです。

## 特徴

- `DirectedGraph`と`UndirectedGraph`の両方に対応
- 全頂点の距離と親を返す単一始点・複数始点版`solve`
- 全頂点または指定終点の距離を返す`dist`
- 頂点列として経路を返す`path`
- `BFS`、`BellmanFord`、`ZeroOneBFS`と共通の`PathResult`
- `LongIndexedPriorityQueue`により頂点ごとの暫定距離を管理

## 依存関係

- [`Graph`](../Core/GraphGuide.md)
- [`LongIndexedPriorityQueue`](../../ds/PriorityQueue/IndexedPriorityQueueGuide.md)

## 主な機能（メソッド一覧）

### 1. 計算

| メソッド                          | 戻り値の型   | 説明                                           |
|-----------------------------------|--------------|------------------------------------------------|
| `solve(Graph graph, int s)`       | `PathResult` | 始点`s`から全頂点への距離と親を計算            |
| `solve(Graph graph, int... s)`    | `PathResult` | 複数始点のいずれかから全頂点への距離と親を計算 |
| `dist(Graph graph, int s)`        | `long[]`     | 始点`s`から全頂点への距離を返す                |
| `dist(Graph graph, int... s)`     | `long[]`     | 複数始点のいずれかから全頂点への距離を返す     |
| `dist(Graph graph, int s, int g)` | `long`       | 始点`s`から終点`g`への距離を返す               |
| `path(Graph graph, int s, int g)` | `int[]`      | 始点`s`から終点`g`への経路を返す               |

### 2. `PathResult`

| メソッド           | 戻り値の型 | 説明                                         |
|--------------------|------------|----------------------------------------------|
| `distTo(int v)`    | `long`     | 頂点`v`への距離。到達不能は`Long.MAX_VALUE`  |
| `reachable(int v)` | `boolean`  | 頂点`v`へ到達可能か判定                      |
| `parent(int v)`    | `int`      | 最短経路木上の親。始点は自身、到達不能は`-1` |
| `pathTo(int v)`    | `int[]`    | 始点から`v`までの頂点列。到達不能は`null`    |

`PathResult`からは始点配列`s`、`hasNegCycle`、距離配列`dist`、親配列`parent`も直接参照できます。詳細は[PathResultGuide.md](PathResultGuide.md)を参照してください。

## 利用例

```java
import lib.graph.DirectedGraph;
import lib.graph.Dijkstra;
import lib.graph.PathResult;

DirectedGraph graph = new DirectedGraph(5, 6);
graph.add(0, 1, 4);
graph.add(0, 2, 1);
graph.add(2, 1, 2);
graph.add(1, 3, 1);
graph.add(2, 3, 5);
graph.add(3, 4, 3);

PathResult result = Dijkstra.solve(graph, 0);
long distance = result.distTo(4); // 7
int[] path = result.pathTo(4);    // {0, 2, 1, 3, 4}
```

終点までの距離だけが必要なら、`Dijkstra.dist(graph, 0, 4)`を使用できます。

## 注意事項

- すべての辺の重みが非負であることを前提とします。
- 到達不能な距離は`Long.MAX_VALUE`です。
- `path`と`pathTo`は始点と終点を含む頂点列を返します。
- 複数始点版では、各始点の距離を0として同時に探索します。
- 計算結果は呼び出しごとに生成され、グラフ内にはキャッシュされません。

## パフォーマンス特性

- `solve`、`dist(graph, s)`、`path`: $\mathcal{O}((V + E) \log V)$
- `dist(graph, s, g)`: 最悪$\mathcal{O}((V + E) \log V)$。`g`の距離確定時に探索を終了
- 追加領域: $\mathcal{O}(V)$
- `pathTo`: $\mathcal{O}(V)$の配列を作成し、経路長に比例して復元

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                                |
|:-------------------|:-----------|:----------------------------------------------------------------------------------------------------|
| **バージョン 4.0** | 2026-08-23 | 共通結果型を`PathResult`へ改名し、`solve`の戻り値型を更新（旧`ShortestPathResult`からの破壊的変更） |
| **バージョン 3.0** | 2026-08-23 | `ShortestPathResult`を共通結果型として導入し、複数始点`solve`/`dist`を追加。`Dijkstra.Result`を廃止 |
| **バージョン 2.2** | 2026-07-20 | Indexed PriorityQueueの追加APIを`add`へ移行                                                         |
| **バージョン 2.1** | 2026-07-18 | 暫定距離管理を`LongIndexedPriorityQueue`の新APIへ移行                                               |
| **バージョン 2.0** | 2026-07-17 | `Graph`を受け取る静的ユーティリティへ変更し、結果オブジェクトと経路復元APIを追加                    |
| **バージョン 1.0** | 2025-10-13 | グラフと始点キャッシュを保持するクラスとして初回実装                                                |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
