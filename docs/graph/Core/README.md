# Graph Core

## 概要

前方スター形式でグラフを保持するクラスと、基本的な探索・判定アルゴリズムを提供します。木専用の型・アルゴリズムは [Graph/Tree](../Tree/README.md) を参照してください。

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

- **用途**: BFS訪問順、二部判定、トポロジカルソート、閉路判定・復元、SCC
- **特徴**: グラフの内部配列を直接走査し、オブジェクト生成を抑制
- **詳細**: [GraphUtilsGuide.md](./GraphUtilsGuide.md)

## 選択ガイド

| 目的                         | 使用するクラス・メソッド                             |
|------------------------------|------------------------------------------------------|
| 有向グラフを構築する         | `DirectedGraph`                                      |
| 無向グラフを構築する         | `UndirectedGraph`                                    |
| 重みを無視した探索           | `GraphUtils.bfs`                                     |
| DAG判定・トポロジカル順      | `GraphUtils.hasCycle` / `GraphUtils.topologicalSort` |
| 有向・無向閉路の復元         | `GraphUtils.findCycle`                               |
| 強連結成分分解               | `GraphUtils.scc`                                     |
| 無向グラフの二部判定         | `GraphUtils.isBipartite`                             |
| 木の直径、根付き木、LCA・HLD | [Graph/Tree](../Tree/README.md)                      |

## 注意事項

- グラフ本体と内部配列へ直接アクセスするアルゴリズムのパッケージは`lib.graph`です。
- 辺配列の容量はコンストラクタで固定され、自動拡張されません。
- `Tree`と`RootedTree`は`lib.graph.tree`にあり、一般グラフとは独立した木専用の内部表現を持ちます。
- 検証例は[`test/verify/graph`](../../../test/verify/graph)を参照してください。
