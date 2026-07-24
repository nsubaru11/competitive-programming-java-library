# Minimum / Maximum Spanning Forest

## 概要

無向重み付きグラフの最小・最大全域森を求めるAPIです。入力が連結なら結果は全域木、非連結なら各連結成分の全域木を合わせた全域森になります。

入力グラフは [`UndirectedGraph`](../Core/UndirectedGraphGuide.md) で共通化され、Kruskal法とPrim法のどちらにも同じグラフを渡せます。採用辺を含む計算結果には [`SpanningForestResult`](./SpanningForestResultGuide.md) を使用します。

## 実装クラス

### [Kruskal](./KruskalGuide.md)

- 辺を重み順に処理し、`UnionFind`で閉路を除外
- `minimum` / `maximum`で採用辺ID・総コスト・連結成分数を取得
- `minimumCost` / `maximumCost`で総コストだけを軽量に計算
- 時間計算量: `O(|E| log |E|)`
- 追加メモリ: `O(|V| + |E|)`

### [Prim](./PrimGuide.md)

- `LongIndexedPriorityQueue`で各頂点を森へ接続する最良の辺を管理
- 非連結グラフでは、頂点番号が小さい未訪問頂点から次の成分を開始
- `minimum` / `maximum`で採用辺ID・総コスト・連結成分数を取得
- `minimumCost` / `maximumCost`では採用辺ID・親辺配列を確保しない
- 時間計算量: `O((|V| + |E|) log |V|)`
- 追加メモリ: `O(|V|)`

### Edmonds

有向グラフの最小全域有向木を求める予定の未実装クラスです。Kruskal法・Prim法が対象とする無向全域森とは入力と結果の意味が異なります。

## 共通API

| クラス             | メソッド                       | 戻り値                 | 説明                           |
|--------------------|--------------------------------|------------------------|--------------------------------|
| `Kruskal` / `Prim` | `minimum(UndirectedGraph)`     | `SpanningForestResult` | 最小全域森を構築               |
| `Kruskal` / `Prim` | `maximum(UndirectedGraph)`     | `SpanningForestResult` | 最大全域森を構築               |
| `Kruskal` / `Prim` | `minimumCost(UndirectedGraph)` | `long`                 | 最小全域森の総コストだけを計算 |
| `Kruskal` / `Prim` | `maximumCost(UndirectedGraph)` | `long`                 | 最大全域森の総コストだけを計算 |

## 選択ガイド

| 条件                                         | 推奨                                     |
|----------------------------------------------|------------------------------------------|
| 辺数が少ない、または辺IDを重み順に扱いやすい | Kruskal                                  |
| 前方スター形式の隣接辺をそのまま走査したい   | Prim                                     |
| 総コストだけ必要                             | 各クラスの `minimumCost` / `maximumCost` |
| 実装結果を相互検証したい                     | 同じ `UndirectedGraph`を両方へ渡して比較 |

同じコストの辺が複数ある場合、Kruskal法とPrim法で採用辺の順序や集合が異なることがあります。総コストと連結成分数が一致し、それぞれの結果が閉路を持たなければ正常です。

## 注意事項

- 辺IDは`UndirectedGraph`へ追加した順序の0-indexedです。
- 負の辺も処理できます。全域木・全域森では閉路を採用しないため、「負閉路」は問題になりません。
- 非連結グラフでも`-1`は返さず、全域森の総コストを返します。
- cost-only版は連結成分数を返さないため、連結性も必要ならResult版を使用してください。
- 入力された`UndirectedGraph`は変更されません。
