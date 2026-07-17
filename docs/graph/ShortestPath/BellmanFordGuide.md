# BellmanFord 利用ガイド

## 概要

`BellmanFord`は、負辺を含む`Graph`に対して単一始点最短経路を計算する静的ユーティリティクラスです。

## 特徴

- `DirectedGraph`と`UndirectedGraph`の両方に対応
- 始点から到達可能な負閉路を検出
- 負閉路から到達可能な頂点まで影響範囲を伝播
- 有限距離、到達不能、負閉路の影響を1つの距離配列で表現
- 負閉路の影響を受けない頂点では経路復元が可能

## 依存関係

- [`Graph`](../Core/GraphGuide.md)

## 主な機能（メソッド一覧）

### 1. 計算

| メソッド                        | 戻り値の型                | 説明                         |
|-----------------------------|----------------------|----------------------------|
| `solve(Graph graph, int s)` | `BellmanFord.Result` | 始点`s`から全頂点への距離、親、負閉路の影響を計算 |

### 2. Result

| メソッド               | 戻り値の型     | 説明                       |
|--------------------|-----------|--------------------------|
| `distTo(int v)`    | `long`    | 頂点`v`への距離                |
| `reachable(int v)` | `boolean` | 頂点`v`へ到達可能か判定            |
| `parent(int v)`    | `int`     | 最短経路上の親                  |
| `pathTo(int v)`    | `int[]`   | 始点から`v`への経路。復元不能なら`null` |

`Result`からは`hasNegCycle`、始点`s`、距離配列`dist`、親配列`parent`も直接参照できます。
`hasNegCycle`は、始点から到達可能な負閉路がある場合に`true`です。

## 利用例

```java
import lib.graph.BellmanFord;
import lib.graph.DirectedGraph;

DirectedGraph graph = new DirectedGraph(6, 6);
graph.add(0, 1, 2);
graph.add(1, 2, -3);
graph.add(2, 1, 1); // 負閉路
graph.add(2, 3, 4); // 負閉路の影響を受ける
graph.add(0, 4, 5);
graph.add(4, 5, 1);

BellmanFord.Result result = BellmanFord.solve(graph, 0);
long affected = result.distTo(3); // Long.MIN_VALUE
long finite = result.distTo(5);   // 6
```

## 注意事項

- 到達不能な頂点の距離は`Long.MAX_VALUE`です。
- 到達可能な負閉路上の頂点と、そこから到達可能な頂点の距離は`Long.MIN_VALUE`です。
- `reachable(v)`は距離が`Long.MIN_VALUE`でも`true`を返します。
- 到達不能または負閉路の影響下では、`parent(v)`は`-1`、`pathTo(v)`は`null`です。
- 始点から到達できない負閉路は、その始点の`hasNegCycle`と距離配列に影響しません。

## パフォーマンス特性

- 時間計算量: O(VE)
- 負閉路の影響伝播: O(V + E)。全体のO(VE)に含まれる
- 追加領域: O(V)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                             |
|:--------------|:-----------|:-----------------------------------------------|
| **バージョン 2.0** | 2026-07-17 | `Graph`を受け取る静的ユーティリティへ変更し、頂点ごとの負閉路影響判定と経路復元を追加 |
| **バージョン 1.0** | 2025-03-22 | グラフと始点キャッシュを保持するクラスとして初回実装                     |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
