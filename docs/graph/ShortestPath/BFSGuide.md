# BFS 利用ガイド

## 概要

`BFS`は、辺の重みを1として扱う`Graph`に対して、幅優先探索で単一始点または複数始点の最短経路を計算する静的ユーティリティクラスです。

## 特徴

- `DirectedGraph`と`UndirectedGraph`の両方に対応
- 全頂点の距離と親を返す`solve`
- 全頂点または指定した終点の距離を返す`dist`
- `ShortestPathResult`による経路復元
- プリミティブ配列だけを使う`O(V + E)`の探索

## 依存関係

- [`Graph`](../Core/GraphGuide.md)
- [`ShortestPathResult`](./ShortestPathResultGuide.md)

## 主な機能（メソッド一覧）

### 1. 計算結果を返すメソッド

| メソッド                       | 戻り値の型           | 説明                                           |
|--------------------------------|----------------------|------------------------------------------------|
| `solve(Graph graph, int s)`    | `ShortestPathResult` | 始点`s`から全頂点への距離と親を計算            |
| `solve(Graph graph, int... s)` | `ShortestPathResult` | 複数始点のいずれかから全頂点への距離と親を計算 |

### 2. 距離を返すメソッド

| メソッド                          | 戻り値の型 | 説明                                       |
|-----------------------------------|------------|--------------------------------------------|
| `dist(Graph graph, int s)`        | `long[]`   | 始点`s`から全頂点への距離を返す            |
| `dist(Graph graph, int... s)`     | `long[]`   | 複数始点のいずれかから全頂点への距離を返す |
| `dist(Graph graph, int s, int g)` | `long`     | 始点`s`から終点`g`への距離を返す           |

### 3. 経路を返すメソッド

| メソッド                          | 戻り値の型 | 説明                               |
|-----------------------------------|------------|------------------------------------|
| `path(Graph graph, int s, int g)` | `int[]`    | 始点`s`から終点`g`への頂点列を返す |

## 利用例

```java
import lib.graph.BFS;
import lib.graph.DirectedGraph;
import lib.graph.ShortestPathResult;

DirectedGraph graph = new DirectedGraph(5, 5);
graph.add(0, 1);
graph.add(0, 2);
graph.add(1, 3);
graph.add(2, 3);
graph.add(3, 4);

ShortestPathResult result = BFS.solve(graph, 0);
long distance = result.distTo(4); // 3
int[] path = result.pathTo(4);    // {0, 2, 3, 4} など
```

終点までの距離だけが必要なら、`BFS.dist(graph, 0, 4)`を使用できます。複数始点の場合は`BFS.solve(graph, 0, 1)`のように呼び出します。

## 注意事項

- 辺の重みは参照せず、すべての辺を距離1として扱います。重みが0または1の場合は`ZeroOneBFS`を使用してください。
- 到達不能な頂点の距離は`Long.MAX_VALUE`です。
- 複数始点版では、各始点の距離を0として同時に探索します。
- `path`と`pathTo`は始点と終点を含む頂点列を返し、到達不能な場合は`null`を返します。
- 結果の`hasNegCycle`は常に`false`です。

## パフォーマンス特性

- `solve`、`dist(graph, s)`、`path`: $\mathcal{O}(V + E)$
- `dist(graph, s, g)`: 最悪$\mathcal{O}(V + E)$。終点を発見した時点で探索を終了
- 追加領域: $\mathcal{O}(V)$
- `pathTo`: 経路長に比例する配列を作成

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                   |
|:-------------------|:-----------|:-------------------------------------------------------|
| **バージョン 1.0** | 2026-08-23 | 単一始点・複数始点の距離計算、終点距離、経路復元を実装 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
