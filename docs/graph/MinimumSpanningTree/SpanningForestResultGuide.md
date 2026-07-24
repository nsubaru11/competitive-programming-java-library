# SpanningForestResult 利用ガイド

## 概要

[`SpanningForestResult`](../../../src/lib/graph/SpanningForestResult.java)は、Kruskal法またはPrim法で求めた最小・最大全域森の共通結果型です。総コスト、採用辺ID、入力グラフの連結成分数を保持します。

## 特徴

- `Kruskal.minimum` / `maximum`と`Prim.minimum` / `maximum`で共通利用
- 入力した`UndirectedGraph`上の論理辺IDを保持
- 採用辺数と、結果が1本の全域木かを定数時間で確認可能
- 非連結グラフでも各連結成分の木を合わせた全域森として表現

## 依存関係

- [`UndirectedGraph`](../Core/UndirectedGraphGuide.md)
- [`Kruskal`](./KruskalGuide.md)
- [`Prim`](./PrimGuide.md)

## 公開フィールド

| フィールド | 型 | 説明 |
|---|---|---|
| `cost` | `long` | 全域森に採用された辺の総コスト |
| `edgeIds` | `int[]` | 入力グラフ上の論理辺ID。アルゴリズムが採用した順序で格納 |
| `componentCount` | `int` | 入力グラフの連結成分数 |

## 主な機能（メソッド一覧）

| メソッド | 戻り値の型 | 説明 |
|---|---|---|
| `edgeCount()` | `int` | 採用辺数、すなわち`edgeIds.length`を返す |
| `isSpanningTree()` | `boolean` | 入力グラフが連結で、結果が1本の全域木なら`true` |
| `toString()` | `String` | cost、edgeIds、componentCountを複数行で返す |

コンストラクタはpackage-privateであり、通常はKruskal法またはPrim法の戻り値として取得します。

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

## 注意事項

- `edgeIds`は内部有向辺IDではなく、`UndirectedGraph`へ辺を追加した順序に対応する論理辺IDです。
- `edgeIds`の具体的な順序はアルゴリズムによって異なります。
- 同じ最適コストの全域森が複数存在する場合、Kruskal法とPrim法が異なる辺集合を返すことがあります。
- `edgeIds`は公開配列であり、複製せずに返されます。呼び出し側で書き換えるとResultの内容も変化します。
- 非連結グラフでは`isSpanningTree()`が`false`となりますが、`cost`と`edgeIds`は有効な全域森を表します。
- 総コストだけ必要なら、Resultを生成しない`minimumCost` / `maximumCost`を使用してください。

## パフォーマンス特性

- `edgeCount()`: `O(1)`
- `isSpanningTree()`: `O(1)`
- `toString()`: `O(edgeIds.length)`
- Result自体の追加メモリ: `O(edgeIds.length)`

## バージョン情報

| バージョン番号 | 年月日 | 詳細 |
|:---|:---|:---|
| **バージョン 1.0** | 2026-07-24 | Kruskal法・Prim法共通の全域森Resultとして追加 |

### バージョン管理について

- 1桁目（メジャーバージョン）: API変更や機能拡張
- 2桁目（マイナーバージョン）: バグ修正、文言修正、マイクロ高速化
