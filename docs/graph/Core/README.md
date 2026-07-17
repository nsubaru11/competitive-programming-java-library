# Graph Core

## 概要

前方スター形式でグラフを保持するクラスと、基本的な探索・判定アルゴリズムを提供します。

## 実装クラス

### [Graph](../../../src/lib/graph/Graph.java)

- **用途**: `DirectedGraph`と`UndirectedGraph`に共通する構築・参照APIの定義
- **特徴**: 固定長のプリミティブ配列で辺を保持
- **詳細**: [GraphGuide.md](./GraphGuide.md)

### [DirectedGraph](../../../src/lib/graph/DirectedGraph.java)

- **用途**: 有向グラフの保持
- **特徴**: 辺ID、入次数、出次数を管理
- **詳細**: [DirectedGraphGuide.md](./DirectedGraphGuide.md)

### [UndirectedGraph](../../../src/lib/graph/UndirectedGraph.java)

- **用途**: 無向グラフの保持
- **特徴**: 1本の無向辺を向きの異なる2本の内部辺として保持
- **詳細**: [UndirectedGraphGuide.md](./UndirectedGraphGuide.md)

### [GraphUtils](../../../src/lib/graph/GraphUtils.java)

- **用途**: BFS、辺数距離、二部判定、トポロジカルソート、閉路判定、SCC
- **特徴**: グラフの内部配列を直接走査し、オブジェクト生成を抑制
- **詳細**: [GraphUtilsGuide.md](./GraphUtilsGuide.md)

### [Tree](../../../src/lib/graph/Tree.java)

- **用途**: 重み付き・重みなし木の保持と直径計算
- **特徴**: `n - 1`本の無向辺に特化した固定長表現

### [RootedTree](../../../src/lib/graph/RootedTree.java)

- **用途**: 根付き木の親・深さ・部分木サイズ・LCA・HLD
- **特徴**: 必要になるまで構築処理を遅延

## 選択ガイド

| 目的            | 使用するクラス・メソッド                                         |
|---------------|------------------------------------------------------|
| 有向グラフを構築する    | `DirectedGraph`                                      |
| 無向グラフを構築する    | `UndirectedGraph`                                    |
| 重みを無視した探索・距離  | `GraphUtils.bfs` / `GraphUtils.dist`                 |
| DAG判定・トポロジカル順 | `GraphUtils.hasCycle` / `GraphUtils.topologicalSort` |
| 強連結成分分解       | `GraphUtils.scc`                                     |
| 無向グラフの二部判定    | `GraphUtils.isBipartite`                             |
| 木の直径          | `Tree`                                               |
| 根付き木・LCA・HLD  | `RootedTree`                                         |

## 注意事項

- パッケージは`lib.graph`です。
- 辺配列の容量はコンストラクタで固定され、自動拡張されません。
- `Tree`と`RootedTree`は現在、`Graph`とは独立した木専用の内部表現を持ちます。
- 検証例は[`test/verify/graph/core`](../../../test/verify/graph/core)を参照してください。
