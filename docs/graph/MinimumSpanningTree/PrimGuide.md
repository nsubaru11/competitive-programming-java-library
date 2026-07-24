# Prim 利用ガイド

## 概要

[`Prim`](../../../src/lib/graph/Prim.java)は、`UndirectedGraph`の前方スター形式を直接走査し、最小または最大全域森を求める静的ユーティリティクラスです。各頂点を現在の森へ接続する最良コストを`LongIndexedPriorityQueue`で管理します。

## 特徴

- 最小全域森と最大全域森に対応
- `UndirectedGraph`の隣接辺配列を直接走査し、グラフをソルバー内へ複製しない
- 最小では昇順、最大では降順の`LongIndexedPriorityQueue`を使用
- 非連結グラフでは、頂点番号が小さい未訪問頂点から次の連結成分を開始
- 採用辺を含むResult版と、採用辺・親辺配列を確保しないcost-only版を提供
- 負の辺、多重辺、非連結グラフを処理可能

## 依存関係

- [`UndirectedGraph`](../Core/UndirectedGraphGuide.md)
- [`LongIndexedPriorityQueue`](../../ds/PriorityQueue/IndexedPriorityQueueGuide.md)
- [`SpanningForestResult`](./SpanningForestResultGuide.md)

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

cost-only版は各連結成分の開始頂点をコスト0でキューへ追加し、取り出した全頂点の接続コストを加算します。採用辺IDと親辺を保持しないため、Result版より`int`配列2本分軽量です。

## 利用例

```java
import lib.graph.Prim;
import lib.graph.SpanningForestResult;
import lib.graph.UndirectedGraph;

UndirectedGraph graph = new UndirectedGraph(4, 5);
graph.add(0, 1, 1); // edge 0
graph.add(0, 2, 4); // edge 1
graph.add(1, 2, 2); // edge 2
graph.add(1, 3, 5); // edge 3
graph.add(2, 3, 3); // edge 4

SpanningForestResult result = Prim.minimum(graph);
System.out.println(result.cost);             // 6
System.out.println(result.edgeCount());      // 3
System.out.println(result.isSpanningTree()); // true
```

総コストだけが必要な場合はcost-only版を使用します。

```java
long minimum = Prim.minimumCost(graph); // 6
long maximum = Prim.maximumCost(graph); // 12
```

## 注意事項

- 頂点番号は`0 <= u, v < graph.n`を前提とします。
- `edgeIds`は入力した`UndirectedGraph`の論理辺IDです。内部辺IDではありません。
- `edgeIds`は各連結成分でPrim法が辺を採用した順序です。同コスト辺がある場合の具体的な木は保証しません。
- 非連結グラフでは、頂点番号が小さい未訪問頂点を根として各成分を順に処理します。
- 非連結時も`cost`やcost-only版は`-1`ではなく、全域森の総コストです。
- cost-only版だけでは入力グラフが連結だったか判定できません。必要ならResult版の`componentCount`または`isSpanningTree()`を使用してください。
- 入力されたグラフと辺の重みは変更しません。

## パフォーマンス特性

- Result版
	- 時間計算量: `O((|V| + |E|) log |V|)`
	- 追加メモリ: `O(|V|)`
	- 優先度キュー、親辺、採用辺IDを保持
- cost-only版
	- 時間計算量: `O((|V| + |E|) log |V|)`
	- 追加メモリ: `O(|V|)`
	- 親辺配列、採用辺ID配列、Result生成を省略
- `LongIndexedPriorityQueue`のコスト履歴を訪問済み判定にも再利用し、別の`boolean[]`を確保しません。

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                    |
|:-------------------|:-----------|:----------------------------------------------------------------------------------------|
| **バージョン 3.0** | 2026-07-24 | `UndirectedGraph`を受け取る静的APIへ変更。非連結な全域森、共通Result、cost-only版に対応 |
| **バージョン 2.1** | 2026-07-20 | Indexed PriorityQueueの追加APIを`add`へ移行                                             |
| **バージョン 2.0** | 2026-07-18 | private優先度キューを削除し、`LongIndexedPriorityQueue`を再利用                         |
| **バージョン 1.0** | 2025-10-07 | 最小・最大全域木と採用辺取得を初回実装                                                  |

### バージョン管理について

- 1桁目（メジャーバージョン）: API変更や機能拡張
- 2桁目（マイナーバージョン）: バグ修正、文言修正、マイクロ高速化
