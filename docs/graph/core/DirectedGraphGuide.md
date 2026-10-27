# DirectedGraph 利用ガイド

> `DirectedGraph`は、固定長の前方スター形式で有向グラフを保持するクラスである。

[← README: Graph Core](./README.md) · ソース: [DirectedGraph.java](../../../src/lib/graph/DirectedGraph.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

`DirectedGraph`は、固定長の前方スター形式で有向グラフを保持するクラスである。

## 特徴

- 辺の追加順に0始まりの論理辺IDを割り当て
- 入次数と出次数を追加時に更新
- 重みなしの辺を重み1として追加可能
- `GraphUtils`と最短経路ユーティリティへそのまま渡せる

## 依存関係

- [`Graph`](./GraphGuide.md)

## API

### 1. 構築・更新

| メソッド                                                          | 戻り値の型 | 説明                                    |
|-------------------------------------------------------------------|------------|-----------------------------------------|
| `DirectedGraph(int n, int m)`                                     | -          | `n`頂点、`m`辺分の領域を確保            |
| `add(int u, int v)`                                               | `void`     | 重み1の有向辺を追加                     |
| `add(int u, int v, long c)`                                       | `void`     | 重み付き有向辺を追加                    |
| `setAll(IntSupplier u, IntSupplier v)`                            | `void`     | 現在の辺を消去し、重み1の辺を`m`本入力  |
| `setAll(IntSupplier u, IntSupplier v, LongSupplier c)`            | `void`     | 現在の辺を消去し、重み付き辺を`m`本入力 |
| `addAll(int count, IntSupplier u, IntSupplier v)`                 | `void`     | 重み1の辺を`count`本追加                |
| `addAll(int count, IntSupplier u, IntSupplier v, LongSupplier c)` | `void`     | 重み付き辺を`count`本追加               |
| `clear()`                                                         | `void`     | すべての辺と次数を消去                  |

### 2. 参照

| メソッド            | 戻り値の型 | 説明                 |
|---------------------|------------|----------------------|
| `to(int e)`         | `int`      | 辺`e`の終点          |
| `cost(int e)`       | `long`     | 辺`e`の重み          |
| `edgeCount()`       | `int`      | 現在の辺数           |
| `degree(int v)`     | `int`      | 入次数と出次数の合計 |
| `inDegree(int v)`   | `int`      | 入次数               |
| `outDegree(int v)`  | `int`      | 出次数               |
| `adj(int u)`        | `int[]`    | `u`から出る辺の終点  |
| `adjEdgeIds(int u)` | `int[]`    | `u`から出る辺のID    |

## 利用例

準備中

## 制約・注意事項

- コンストラクタの`m`は一括入力辺数であり、同時に確保する辺容量である。
- 容量を超える辺追加に対する明示的な検査は行わない。
- 自己ループは`degree(v)`で2、`inDegree(v)`と`outDegree(v)`でそれぞれ1と数える。
- 隣接頂点と隣接辺IDは基本的に辺の追加と逆の順序で返る。

## 性能特性

- 辺追加、辺情報、次数の取得: $\mathcal{O}(1)$
- `adj`、`adjEdgeIds`: $\mathcal{O}(\text{出次数})$
- 保持領域: $\mathcal{O}(V + E)$

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                  |
|--------------------|------------|-------------------------------------------------------|
| **バージョン 2.0** | 2026-07-17 | 共通基底`Graph`を継承する固定長有向グラフとして再実装 |
| **バージョン 1.0** | 2026-01-08 | 有向グラフクラスとして初回実装                        |
