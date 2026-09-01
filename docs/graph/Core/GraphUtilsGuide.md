# GraphUtils 利用ガイド

## 概要

`GraphUtils`は、`Graph`の内部配列を直接走査して基本的な探索・判定を行う静的ユーティリティクラスです。

## 特徴

- 到達可能な頂点だけを返す単一始点・複数始点のBFS訪問順
- 無向グラフの二部判定
- 有向・無向グラフの閉路判定と閉路の復元
- 有向グラフのトポロジカルソート
- 再帰を使わないTarjan法による強連結成分分解

## 依存関係

- [`Graph`](./GraphGuide.md)
- [`DirectedGraph`](./DirectedGraphGuide.md)
- [`UndirectedGraph`](./UndirectedGraphGuide.md)

## 主な機能（メソッド一覧）

### 1. 無向グラフの判定

| メソッド                             | 戻り値の型 | 説明                               |
|--------------------------------------|------------|------------------------------------|
| `isBipartite(UndirectedGraph graph)` | `boolean`  | すべての連結成分が二部グラフか判定 |

### 2. 幅優先探索

| メソッド                      | 戻り値の型 | 説明                                                     |
|-------------------------------|------------|----------------------------------------------------------|
| `bfs(Graph graph, int s)`     | `int[]`    | 始点`s`から到達可能な頂点だけを訪問順に返す              |
| `bfs(Graph graph, int... s)`  | `int[]`    | 複数始点から到達可能な頂点だけを訪問順に返す。重複は除く |

### 3. 閉路・トポロジカルソート

| メソッド                                  | 戻り値の型 | 説明                                                              |
|-------------------------------------------|------------|-------------------------------------------------------------------|
| `topologicalSort(DirectedGraph graph)`    | `int[]`    | トポロジカル順。閉路があれば`null`                                |
| `hasCycle(DirectedGraph graph)`           | `boolean`  | 有向閉路があれば`true`                                            |
| `findCycle(DirectedGraph graph)`          | `Cycle`    | 最初に見つけた有向閉路。閉路がなければ`null`                      |
| `hasCycle(UndirectedGraph graph)`         | `boolean`  | 無向閉路があれば`true`                                            |
| `findCycle(UndirectedGraph graph)`        | `Cycle`    | 最初に見つけた無向閉路。閉路がなければ`null`                      |

### 4. 強連結成分分解

| メソッド                    | 戻り値の型 | 説明                                         |
|-----------------------------|------------|----------------------------------------------|
| `scc(DirectedGraph graph)`  | `int[][]`  | 強連結成分を縮約グラフのトポロジカル順で返す |

### 5. 閉路の表現

| メンバー           | 戻り値の型 | 説明                   |
|--------------------|------------|------------------------|
| `Cycle.vertices()` | `int[]`    | 閉路の頂点列           |
| `Cycle.edges()`    | `int[]`    | 頂点列の対応する辺ID列 |
| `Cycle.size()`     | `int`      | 閉路の頂点数および辺数 |

`Cycle`では、`vertices[i]`から`vertices[(i + 1) % cycle.size()]`へ
`edges[i]`番の辺で進みます。有向グラフでは有向辺ID、無向グラフでは無向辺IDです。
`vertices()`と`edges()`が返す配列はコピーされません。

## 利用例

```java
import java.util.Arrays;
import lib.graph.DirectedGraph;
import lib.graph.GraphUtils;

DirectedGraph graph = new DirectedGraph(5, 6);
graph.add(0, 1);
graph.add(1, 0);
graph.add(1, 2);
graph.add(2, 3);
graph.add(3, 2);
graph.add(3, 4);

int[] order = GraphUtils.bfs(graph, 0);
int[][] components = GraphUtils.scc(graph);
System.out.println(Arrays.deepToString(components));

GraphUtils.Cycle cycle = GraphUtils.findCycle(graph);
if (cycle != null) {
	System.out.println(Arrays.toString(cycle.vertices()));
	System.out.println(Arrays.toString(cycle.edges()));
}
```

## 注意事項

- `bfs`は辺の重みを無視します。重みなしグラフの最短距離が必要な場合は`BFS`を使用してください。
- `bfs`の返却配列の長さは、到達可能な頂点数です。未到達頂点を`-1`で埋めることはありません。
- 複数始点版では、重複する始点は最初の1つだけが訪問順に含まれます。
- BFSの同距離頂点の順序は、前方スター形式の走査順に依存します。
- `findCycle`が返す閉路は一意ではなく、頂点・辺の並びは辺の追加順と頂点番号順に依存します。
- `scc`の成分間順序はトポロジカル順ですが、成分内の頂点順は規定しません。

## パフォーマンス特性

- すべてのメソッドの時間計算量: $\mathcal{O}(V + E)$
- `isBipartite`、`bfs`、`topologicalSort`、`hasCycle`、`findCycle`の追加領域: $\mathcal{O}(V)$
- `scc`の追加領域: $\mathcal{O}(V)$。再帰スタックは使用しません

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                                                                                                    |
|:-------------------|:-----------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 3.0** | 2026-09-02 | 有向・無向グラフの閉路を頂点列・辺ID列として復元する`findCycle`と`Cycle`を追加。BFSは到達頂点だけを返す仕様へ変更し、無向グラフの閉路判定を親辺に基づく正しい判定へ修正 |
| **バージョン 2.2** | 2026-08-28 | 無向グラフ用の`hasCycle`を作成                                                                                                                                          |
| **バージョン 2.1** | 2026-08-27 | `scc`の反復Tarjan実装を整理し、`Math.min`を使わない比較へ変更                                                                                                           |
| **バージョン 2.0** | 2026-08-23 | 辺数距離のAPIを`BFS`へ分離し、BFS訪問順・二部判定・トポロジカルソート・閉路判定・SCCに責務を整理                                                                        |
| **バージョン 1.0** | 2026-07-17 | BFS、辺数距離、二部判定、トポロジカルソート、閉路判定、SCCを実装                                                                                                        |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
