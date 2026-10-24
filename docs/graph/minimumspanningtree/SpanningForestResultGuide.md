# SpanningForestResult 利用ガイド

> [`SpanningForestResult`](../../../src/lib/graph/SpanningForestResult.java)は、Kruskal法またはPrim法で求めた最小・最大全域森の共通結果型である。

[← README: Minimum / Maximum Spanning Forest](./README.md) · ソース: [SpanningForestResult.java](../../../src/lib/graph/SpanningForestResult.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

総コスト、採用辺ID、入力グラフの連結成分数を保持する。

## 特徴

- `Kruskal.minimum` / `maximum`と`Prim.minimum` / `maximum`で共通利用
- 入力した`UndirectedGraph`上の論理辺IDを保持
- 採用辺数と、結果が1本の全域木かを定数時間で確認可能
- 非連結グラフでも各連結成分の木を合わせた全域森として表現

## 依存関係

- [`UndirectedGraph`](../core/UndirectedGraphGuide.md)
- [`Kruskal`](./KruskalGuide.md)
- [`Prim`](./PrimGuide.md)

## API

### 公開フィールド

| フィールド       | 型      | 説明                                                     |
|------------------|---------|----------------------------------------------------------|
| `cost`           | `long`  | 全域森に採用された辺の総コスト                           |
| `edgeIds`        | `int[]` | 入力グラフ上の論理辺ID。アルゴリズムが採用した順序で格納 |
| `componentCount` | `int`   | 入力グラフの連結成分数                                   |

### メソッド

| メソッド           | 戻り値の型 | 説明                                            |
|--------------------|------------|-------------------------------------------------|
| `edgeCount()`      | `int`      | 採用辺数、すなわち`edgeIds.length`を返す        |
| `isSpanningTree()` | `boolean`  | 入力グラフが連結で、結果が1本の全域木なら`true` |
| `toString()`       | `String`   | cost、edgeIds、componentCountを複数行で返す     |

コンストラクタはpackage-privateであり、通常はKruskal法またはPrim法の戻り値として取得する。

## 利用例

```java
UndirectedGraph graph = new UndirectedGraph(4, 5);
graph.add(0, 1, 1);
graph.add(0, 2, 4);
graph.add(1, 2, 2);
graph.add(1, 3, 5);
graph.add(2, 3, 3);

SpanningForestResult result = Kruskal.minimum(graph);

System.out.println(result.cost);             // 6
System.out.println(result.edgeCount());      // 3
System.out.println(result.componentCount);   // 1
System.out.println(result.isSpanningTree()); // true

for (int edgeId : result.edgeIds) {
	System.out.println(edgeId);
}
```


- [Example.java](../../../test/verify/graph/Example.java)



## 制約・注意事項

- `edgeIds`は内部有向辺IDではなく、`UndirectedGraph`へ辺を追加した順序に対応する論理辺IDである。
- `edgeIds`の具体的な順序はアルゴリズムによって異なる。
- 同じ最適コストの全域森が複数存在する場合、Kruskal法とPrim法が異なる辺集合を返すことがある。
- `edgeIds`は公開配列であり、複製せずに返される。呼び出し側で書き換えるとResultの内容も変化する。
- 非連結グラフでは`isSpanningTree()`が`false`となるが、`cost`と`edgeIds`は有効な全域森を表す。
- 総コストだけ必要なら、Resultを生成しない`minimumCost` / `maximumCost`を使用すること。

## 性能特性

- `edgeCount()`: $\mathcal{O}(1)$
- `isSpanningTree()`: $\mathcal{O}(1)$
- `toString()`: $\mathcal{O}(edgeIds.length)$
- Result自体の追加メモリ: $\mathcal{O}(edgeIds.length)$

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                          |
|:-------------------|:-----------|:----------------------------------------------|
| **バージョン 1.0** | 2026-07-24 | Kruskal法・Prim法共通の全域森Resultとして追加 |
