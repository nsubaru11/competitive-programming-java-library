# Kruskal 利用ガイド

## 概要

[`Kruskal`](../../../src/lib/graph/Kruskal.java)は、`UndirectedGraph`の辺を重み順に処理し、最小または最大全域森を求める静的ユーティリティクラスです。入力が連結なら結果は全域木になります。

## 特徴

- 最小全域森と最大全域森に対応
- `UndirectedGraph`に登録済みの辺を直接利用し、辺情報をソルバー内へ複製しない
- 無向辺の偶数側内部辺IDをソートし、外部には元の論理辺IDを返す
- `UnionFind`による閉路判定
- 採用辺を含むResult版と、採用辺配列を確保しないcost-only版を提供
- 負の辺、多重辺、非連結グラフを処理可能

## 依存関係

- [`UndirectedGraph`](../Core/UndirectedGraphGuide.md)
- [`UnionFind`](../../ds/UnionFind/UnionFindGuide.md)
- [`SpanningForestResult`](./SpanningForestResultGuide.md)

辺のソートには、クラス内のプリミティブ配列向け実装を使用します。

## 主な機能（メソッド一覧）

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

cost-only版でも辺のソートとUnion-Findは必要ですが、採用辺IDの配列と`SpanningForestResult`を確保しません。

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

## 注意事項

- 頂点番号は`0 <= u, v < graph.n`を前提とします。
- `edgeIds`は入力した`UndirectedGraph`の論理辺IDです。内部辺IDではありません。
- `edgeIds`はKruskal法が採用した順序で格納されます。同コスト辺の順序は保証しません。
- 非連結グラフでは、各連結成分の最小または最大全域木を合わせた全域森を返します。
- 非連結時も`cost`やcost-only版は`-1`ではなく、全域森の総コストです。
- cost-only版だけでは入力グラフが連結だったか判定できません。必要ならResult版の`componentCount`または`isSpanningTree()`を使用してください。
- 入力されたグラフと辺の重みは変更しません。

## パフォーマンス特性

- Result版
	- 時間計算量: `O(|E| log |E|)`
	- 追加メモリ: `O(|V| + |E|)`
	- 辺の処理順、Union-Find、採用辺IDを保持
- cost-only版
	- 時間計算量: `O(|E| log |E|)`
	- 追加メモリ: `O(|V| + |E|)`
	- 採用辺ID配列とResult生成を省略
- 入力が連結で`|V|-1`辺を採用した場合は、不要な残りの辺走査を打ち切ります。非連結の場合は全辺を確認します。

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                        |
|:-------------------|:-----------|:--------------------------------------------------------------------------------------------|
| **バージョン 3.0** | 2026-07-24 | `UndirectedGraph`を受け取る静的APIへ変更。最小・最大の全域森、共通Result、cost-only版に対応 |
| **バージョン 1.0** | 2025-10-07 | 状態を持つソルバーとして最小・最大全域木と採用辺取得を実装                                  |

### バージョン管理について

- 1桁目（メジャーバージョン）: API変更や機能拡張
- 2桁目（マイナーバージョン）: バグ修正、文言修正、マイクロ高速化
