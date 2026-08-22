# Shortest Path

## 概要

`Graph`を引数として、単一始点・複数始点または全頂点対の最短距離を計算する静的ユーティリティを提供します。

最短経路の計算結果は[`ShortestPathResult`](../../../src/lib/graph/ShortestPathResult.java)で共通化されています。

## 実装クラス

### [BFS](../../../src/lib/graph/BFS.java)

- **用途**: 辺をすべて重み1として扱うグラフの単一始点・複数始点最短経路
- **特徴**: 距離、終点距離、経路復元を提供
- **時間計算量**: $\mathcal{O}(V + E)$
- **追加領域**: $\mathcal{O}(V)$
- **詳細**: [BFSGuide.md](./BFSGuide.md)

### [Dijkstra](../../../src/lib/graph/Dijkstra.java)

- **用途**: 非負重み付きグラフの単一始点・複数始点最短経路
- **特徴**: 全頂点の距離、1頂点への距離、経路復元を用途別のメソッドで提供
- **時間計算量**: $\mathcal{O}((V + E) \log V)$
- **追加領域**: $\mathcal{O}(V)$
- **詳細**: [DijkstraGuide.md](./DijkstraGuide.md)

### [BellmanFord](../../../src/lib/graph/BellmanFord.java)

- **用途**: 負辺を含むグラフの単一始点・複数始点最短経路
- **特徴**: 始点から到達可能な負閉路と、その影響を受ける頂点を判定
- **時間計算量**: $\mathcal{O}(VE)$
- **追加領域**: $\mathcal{O}(V)$
- **詳細**: [BellmanFordGuide.md](./BellmanFordGuide.md)

### [ZeroOneBFS](../../../src/lib/graph/ZeroOneBFS.java)

- **用途**: 辺の重みが0または1のグラフの単一始点・複数始点最短経路
- **特徴**: `IntArrayDeque`を使った距離計算と経路復元
- **時間計算量**: $\mathcal{O}(V + E)$
- **追加領域**: 作業配列は $\mathcal{O}(V)$。dequeは保留中の頂点を保持し、必要に応じて拡張
- **詳細**: [ZeroOneBFSGuide.md](./ZeroOneBFSGuide.md)

### [WarshallFloyd](../../../src/lib/graph/WarshallFloyd.java)

- **用途**: 全頂点対間の最短経路
- **特徴**: 負閉路の影響を受ける頂点対だけを区別し、残りの距離を維持
- **時間計算量**: $\mathcal{O}(V^3)$
- **追加領域**: $\mathcal{O}(V^2)$
- **詳細**: [WarshallFloydGuide.md](./WarshallFloydGuide.md)

## アルゴリズムの選択ガイド

| アルゴリズム   | 辺の重み | 負辺 | 負閉路の影響判定       | 対象           | 時間計算量                    |
|----------------|----------|-----:|------------------------|----------------|-------------------------------|
| BFS            | 1        | 不可 | なし                   | 単一・複数始点 | $\mathcal{O}(V + E)$          |
| 0-1 BFS        | 0または1 | 不可 | なし                   | 単一・複数始点 | $\mathcal{O}(V + E)$          |
| Dijkstra       | 非負     | 不可 | なし                   | 単一・複数始点 | $\mathcal{O}((V + E) \log V)$ |
| Bellman-Ford   | 任意     |   可 | 始点から到達可能な範囲 | 単一・複数始点 | $\mathcal{O}(VE)$             |
| Warshall-Floyd | 任意     |   可 | 影響を受ける全頂点対   | 全頂点対       | $\mathcal{O}(V^3)$            |

## 距離値の共通表現

| 値               | 意味                                   |
|------------------|----------------------------------------|
| `Long.MAX_VALUE` | 到達不能                               |
| 通常の`long`値   | 有限の最短距離                         |
| `Long.MIN_VALUE` | 負閉路の影響により最短距離が定まらない |

`Long.MIN_VALUE`はBellman-FordとWarshall-Floydだけが返します。

## 注意事項

- 頂点番号は0-indexedです。
- BFSは辺の重みを1、0-1 BFSは辺の重みを0または1として扱います。
- Dijkstraはすべての辺が非負であることを前提とします。
- `UndirectedGraph`では負辺を往復できるため、負辺が1本でもその連結範囲は負閉路の影響を受けます。
- 各呼び出しは新しい結果を計算し、以前の始点に対するキャッシュは保持しません。
- パッケージは`lib.graph`で、`Graph`、`DirectedGraph`、`UndirectedGraph`に依存します。
- 検証コードは[Dijkstra](../../../test/verify/graph/dijkstra)、[Bellman-Ford](../../../test/verify/graph/bellmanford)、[Warshall-Floyd](../../../test/verify/graph/warshallfloyd)を参照してください。
