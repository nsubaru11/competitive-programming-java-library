# `lib.graph`

`src/lib/graph`にあるグラフ・木アルゴリズムの実装一覧である。各分野のREADMEから対象クラスと詳細Guideへ進める。

| 分野                                          | 主な実装                                                |
|-----------------------------------------------|---------------------------------------------------------|
| [重心分解](./centroiddecomposition/README.md) | Centroid Decomposition                                  |
| [連結性](./connectivity/README.md)            | 橋・関節点の検出                                        |
| [グラフ基盤](./core/README.md)                | 有向・無向グラフ、探索、SCC、二部判定                   |
| [最大流](./maxflow/README.md)                 | Dinic法などの最大流                                     |
| [最小費用流](./mincostflow/README.md)         | Min-Cost Flow                                           |
| [全域木](./minimumspanningtree/README.md)     | Kruskal法、Prim法                                       |
| [最短路](./shortestpath/README.md)            | BFS、0-1 BFS、Dijkstra、Bellman-Ford、DAG、全点対最短路 |
| [木アルゴリズム](./tree/README.md)            | LCA、HLD、全方位木DP、Euler Tourなど                    |
| [2-SAT](./twosat/README.md)                   | 含意グラフと強連結成分分解による2-SAT                   |

## 個別アルゴリズム

専用READMEをまだ持たないグラフアルゴリズムのソースである。以下は現在すべて未実装の雛形である。

| クラス                                 | ソース                                                      | 分野                       |
|----------------------------------------|-------------------------------------------------------------|----------------------------|
| `BipartiteMatching`                    | [Java](../../src/lib/graph/BipartiteMatching.java)          | 二部グラフの最大マッチング |
| `GeneralMatching`                      | [Java](../../src/lib/graph/GeneralMatching.java)            | 一般グラフの最大マッチング |
| `DominatorTree`                        | [Java](../../src/lib/graph/DominatorTree.java)              | 有向グラフの支配木         |
| `EulerianTrail`                        | [Java](../../src/lib/graph/EulerianTrail.java)              | オイラー路・オイラー閉路   |
| `Johnson`（未実装）                    | [Java](../../src/lib/graph/Johnson.java)                    | 全頂点対最短路             |
| `OfflineDynamicConnectivity`（未実装） | [Java](../../src/lib/graph/OfflineDynamicConnectivity.java) | オフライン動的連結性       |
| `StoerWagner`                          | [Java](../../src/lib/graph/StoerWagner.java)                | 大域最小カット             |

## 内部パッケージ

| パッケージ       | ソース                          | ドキュメント                                                                |
|------------------|---------------------------------|-----------------------------------------------------------------------------|
| `lib.graph`      | [src](../../src/lib/graph)      | グラフ本体・各アルゴリズムのソースは上の分野別READMEを参照                  |
| `lib.graph.tree` | [src](../../src/lib/graph/tree) | [木アルゴリズム](./tree/README.md)、[Euler Tour](../ds/eulertour/README.md) |
