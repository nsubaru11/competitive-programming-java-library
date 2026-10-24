# Dijkstra 利用ガイド

> `Dijkstra`は、非負重み付き`Graph`に対して単一始点または複数始点の最短経路を計算する静的ユーティリティクラスである。

[← README: Shortest Path](./README.md) · ソース: [Dijkstra.java](../../../src/lib/graph/Dijkstra.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

`Dijkstra`は、非負重み付き`Graph`に対して単一始点または複数始点の最短経路を計算する静的ユーティリティクラスである。

## 特徴

- `DirectedGraph`と`UndirectedGraph`の両方に対応
- 全頂点の距離と親を返す単一始点・複数始点版`solve`
- 全頂点または指定終点の距離を返す`dist`
- 頂点列として経路を返す`path`
- `BFS`、`BellmanFord`、`ZeroOneBFS`と共通の`PathResult`
- `LongIndexedPriorityQueue`により頂点ごとの暫定距離を管理

## 依存関係

- [`Graph`](../core/GraphGuide.md)
- [`LongIndexedPriorityQueue`](../../ds/priorityqueue/IndexedPriorityQueueGuide.md)

## API

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

`PathResult`からは始点配列`s`、`hasNegCycle`、距離配列`dist`、親配列`parent`も直接参照できる。詳細は[PathResultGuide.md](PathResultGuide.md)を参照すること。

## 利用例

準備中

## 制約・注意事項

- すべての辺の重みが非負であることを前提とする。
- 到達不能な距離は`Long.MAX_VALUE`である。
- `path`と`pathTo`は始点と終点を含む頂点列を返す。
- 複数始点版では、各始点の距離を0として同時に探索する。
- 計算結果は呼び出しごとに生成され、グラフ内にはキャッシュされない。

## 性能特性

- `solve`、`dist(graph, s)`、`path`: $\mathcal{O}((V + E) \log V)$
- `dist(graph, s, g)`: 最悪$\mathcal{O}((V + E) \log V)$。`g`の距離確定時に探索を終了
- 追加領域: $\mathcal{O}(V)$
- `pathTo`: $\mathcal{O}(V)$の配列を作成し、経路長に比例して復元

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                |
|:-------------------|:-----------|:----------------------------------------------------------------------------------------------------|
| **バージョン 4.0** | 2026-08-23 | 共通結果型を`PathResult`へ改名し、`solve`の戻り値型を更新（旧`ShortestPathResult`からの破壊的変更） |
| **バージョン 3.0** | 2026-08-23 | `ShortestPathResult`を共通結果型として導入し、複数始点`solve`/`dist`を追加。`Dijkstra.Result`を廃止 |
| **バージョン 2.2** | 2026-07-20 | Indexed PriorityQueueの追加APIを`add`へ移行                                                         |
| **バージョン 2.1** | 2026-07-18 | 暫定距離管理を`LongIndexedPriorityQueue`の新APIへ移行                                               |
| **バージョン 2.0** | 2026-07-17 | `Graph`を受け取る静的ユーティリティへ変更し、結果オブジェクトと経路復元APIを追加                    |
| **バージョン 1.0** | 2025-10-13 | グラフと始点キャッシュを保持するクラスとして初回実装                                                |
