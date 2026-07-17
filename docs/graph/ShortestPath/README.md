# Shortest Path

## 概要

`Graph`を引数として、単一始点または全頂点対の最短距離を計算する静的ユーティリティを提供します。

## 実装クラス

### [Dijkstra](../../../src/lib/graph/Dijkstra.java)

- **用途**: 非負重み付きグラフの単一始点最短経路
- **特徴**: 全頂点の距離、1頂点への距離、経路復元を用途別のメソッドで提供
- **時間計算量**: O((V + E) log V)
- **追加領域**: O(V)
- **詳細**: [DijkstraGuide.md](./DijkstraGuide.md)

### [BellmanFord](../../../src/lib/graph/BellmanFord.java)

- **用途**: 負辺を含むグラフの単一始点最短経路
- **特徴**: 始点から到達可能な負閉路と、その影響を受ける頂点を判定
- **時間計算量**: O(VE)
- **追加領域**: O(V)
- **詳細**: [BellmanFordGuide.md](./BellmanFordGuide.md)

### [WarshallFloyd](../../../src/lib/graph/WarshallFloyd.java)

- **用途**: 全頂点対間の最短経路
- **特徴**: 負閉路の影響を受ける頂点対だけを区別し、残りの距離を維持
- **時間計算量**: O(V³)
- **追加領域**: O(V²)
- **詳細**: [WarshallFloydGuide.md](./WarshallFloydGuide.md)

## アルゴリズムの選択ガイド

| アルゴリズム         | 負辺 |    負閉路の影響判定 | 対象   |            時間計算量 |
|----------------|---:|------------:|------|-----------------:|
| Dijkstra       | 不可 |          なし | 単一始点 | O((V + E) log V) |
| Bellman-Ford   |  可 | 始点から到達可能な範囲 | 単一始点 |            O(VE) |
| Warshall-Floyd |  可 |  影響を受ける全頂点対 | 全頂点対 |            O(V³) |

## 距離値の共通表現

| 値                | 意味                  |
|------------------|---------------------|
| `Long.MAX_VALUE` | 到達不能                |
| 通常の`long`値       | 有限の最短距離             |
| `Long.MIN_VALUE` | 負閉路の影響により最短距離が定まらない |

`Long.MIN_VALUE`はBellman-FordとWarshall-Floydだけが返します。

## 注意事項

- 頂点番号は0-indexedです。
- Dijkstraはすべての辺が非負であることを前提とします。
- `UndirectedGraph`では負辺を往復できるため、負辺が1本でもその連結範囲は負閉路の影響を受けます。
- 各呼び出しは新しい結果を計算し、以前の始点に対するキャッシュは保持しません。
- パッケージは`lib.graph`で、`Graph`、`DirectedGraph`、`UndirectedGraph`に依存します。
- Example・Checkは[`test/verify/graph/shortestpath`](../../../test/verify/graph/shortestpath)を参照してください。
