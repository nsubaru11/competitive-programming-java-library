# Kruskal 利用ガイド

> [`Kruskal`](../../../src/lib/graph/Kruskal.java)は、`UndirectedGraph`の辺を重み順に処理し、最小または最大全域森を求める静的ユーティリティクラスである。

[← README: Minimum / Maximum Spanning Forest](./README.md) · ソース: [Kruskal.java](../../../src/lib/graph/Kruskal.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

入力が連結なら結果は全域木になる。

## 特徴

- 最小全域森と最大全域森に対応
- `UndirectedGraph`に登録済みの辺を直接利用し、辺情報をソルバー内へ複製しない
- 無向辺の偶数側内部辺IDをソートし、外部には元の論理辺IDを返す
- `UnionFind`による閉路判定
- 採用辺を含むResult版と、採用辺配列を確保しないcost-only版を提供
- 負の辺、多重辺、非連結グラフを処理可能

## 依存関係

- [`UndirectedGraph`](../core/UndirectedGraphGuide.md)
- [`UnionFind`](../../ds/unionfind/UnionFindGuide.md)
- [`SpanningForestResult`](./SpanningForestResultGuide.md)

辺のソートにはクラス内のプリミティブ配列向け実装を使用する。

## API

### 1. Resultを返すメソッド

| メソッド                         | 戻り値の型             | 説明                                             |
|----------------------------------|------------------------|--------------------------------------------------|
| `minimum(UndirectedGraph graph)` | `SpanningForestResult` | 最小全域森の総コスト、採用辺ID、連結成分数を返す |
| `maximum(UndirectedGraph graph)` | `SpanningForestResult` | 最大全域森の総コスト、採用辺ID、連結成分数を返す |

### 2. 総コストだけを返すメソッド

| メソッド                             | 戻り値の型 | 説明                           |
|--------------------------------------|------------|--------------------------------|
| `minimumCost(UndirectedGraph graph)` | `long`     | 最小全域森の総コストだけを返す |
| `maximumCost(UndirectedGraph graph)` | `long`     | 最大全域森の総コストだけを返す |

cost-only版でも辺のソートとUnion-Findは必要であるが、採用辺IDの配列と`SpanningForestResult`を確保しない。

## 利用例

```java
import lib.graph.Kruskal;
import lib.graph.SpanningForestResult;
import lib.graph.UndirectedGraph;

UndirectedGraph graph = new UndirectedGraph(4, 5);
graph.add(0, 1, 1); // edge 0
graph.add(0, 2, 4); // edge 1
graph.add(1, 2, 2); // edge 2
graph.add(1, 3, 5); // edge 3
graph.add(2, 3, 3); // edge 4

SpanningForestResult result = Kruskal.minimum(graph);
System.out.println(result.cost);            // 6
System.out.println(result.edgeCount());     // 3
System.out.println(result.isSpanningTree()); // true
```

総コストだけが必要な場合はcost-only版を使用します。

```java
long minimum = Kruskal.minimumCost(graph); // 6
long maximum = Kruskal.maximumCost(graph); // 12
```


- [Example.java](../../../test/example/graph/Example.java)



## 制約・注意事項

- 頂点番号は`0 <= u, v < graph.n`を前提とする。
- `edgeIds`は入力した`UndirectedGraph`の論理辺IDである。内部辺IDではない。
- `edgeIds`はKruskal法が採用した順序で格納される。同コスト辺の順序は保証しない。
- 非連結グラフでは、各連結成分の最小または最大全域木を合わせた全域森を返す。
- 非連結時も`cost`やcost-only版は`-1`ではなく、全域森の総コストである。
- cost-only版だけでは入力グラフが連結だったか判定できない。必要ならResult版の`componentCount`または`isSpanningTree()`を使用すること。
- 入力されたグラフと辺の重みは変更しない。

## 性能特性

- Result版
	- 時間計算量: $\mathcal{O}(\lvert E\rvert \log \lvert E\rvert)$
	- 追加メモリ: $\mathcal{O}(\lvert V\rvert + \lvert E\rvert)$
	- 辺の処理順、Union-Find、採用辺IDを保持
- cost-only版
	- 時間計算量: $\mathcal{O}(\lvert E\rvert \log \lvert E\rvert)$
	- 追加メモリ: $\mathcal{O}(\lvert V\rvert + \lvert E\rvert)$
	- 採用辺ID配列とResult生成を省略
- 入力が連結で`|V|-1`辺を採用した場合は、不要な残りの辺走査を打ち切る。非連結の場合は全辺を確認する。

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                        |
|--------------------|------------|---------------------------------------------------------------------------------------------|
| **バージョン 3.2** | 2026-08-23 | 辺インデックスの交換処理を元の実装に変更                                                    |
| **バージョン 3.1** | 2026-08-23 | 辺インデックスの交換処理を共通ユーティリティ`ArrayUtils.swap`へ移行                         |
| **バージョン 3.0** | 2026-07-24 | `UndirectedGraph`を受け取る静的APIへ変更。最小・最大の全域森、共通Result、cost-only版に対応 |
| **バージョン 1.0** | 2025-10-07 | 状態を持つソルバーとして最小・最大全域木と採用辺取得を実装                                  |
