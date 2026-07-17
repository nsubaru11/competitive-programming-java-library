# GraphUtils 利用ガイド

## 概要

`GraphUtils`は、`Graph`の内部配列を直接走査して基本的な探索・判定を行う静的ユーティリティクラスです。

## 特徴

- 単一始点・複数始点のBFSと辺数距離
- 無向グラフの二部判定
- 有向グラフのトポロジカルソートと閉路判定
- 再帰を使わないTarjan法による強連結成分分解

## 依存関係

- [`Graph`](./GraphGuide.md)
- [`DirectedGraph`](./DirectedGraphGuide.md)
- [`UndirectedGraph`](./UndirectedGraphGuide.md)

## 主な機能（メソッド一覧）

### 1. 無向グラフの判定

| メソッド                                 | 戻り値の型     | 説明                |
|--------------------------------------|-----------|-------------------|
| `isBipartite(UndirectedGraph graph)` | `boolean` | すべての連結成分が二部グラフか判定 |

### 2. 幅優先探索

| メソッド                          | 戻り値の型   | 説明                       |
|-------------------------------|---------|--------------------------|
| `bfs(Graph graph, int s)`     | `int[]` | 始点`s`からの訪問順。未到達部分は`-1`   |
| `bfs(Graph graph, int... s)`  | `int[]` | 複数始点からの訪問順。未到達部分は`-1`    |
| `dist(Graph graph, int s)`    | `int[]` | 始点`s`からの最小辺数。到達不能は`-1`   |
| `dist(Graph graph, int... s)` | `int[]` | いずれかの始点からの最小辺数。到達不能は`-1` |

### 3. 有向グラフ

| メソッド                                   | 戻り値の型     | 説明                     |
|----------------------------------------|-----------|------------------------|
| `topologicalSort(DirectedGraph graph)` | `int[]`   | トポロジカル順。閉路があれば`null`   |
| `hasCycle(DirectedGraph graph)`        | `boolean` | 有向閉路があれば`true`         |
| `scc(DirectedGraph graph)`             | `int[][]` | 強連結成分を縮約グラフのトポロジカル順で返す |

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

int[] distance = GraphUtils.dist(graph, 0);
int[][] components = GraphUtils.scc(graph);
System.out.println(Arrays.deepToString(components));
```

## 注意事項

- `bfs`と`dist`は辺の重みを無視します。
- 複数始点版では、始点が互いに異なることを前提とします。
- BFSの同距離頂点の順序は、前方スター形式の走査順に依存します。
- `scc`の成分間順序はトポロジカル順ですが、成分内の頂点順は規定しません。

## パフォーマンス特性

- すべてのメソッドの時間計算量: O(V + E)
- `isBipartite`、`bfs`、`dist`、`topologicalSort`、`hasCycle`の追加領域: O(V)
- `scc`の追加領域: O(V)。再帰スタックは使用しません

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                  |
|:--------------|:-----------|:------------------------------------|
| **バージョン 1.0** | 2026-07-17 | BFS、辺数距離、二部判定、トポロジカルソート、閉路判定、SCCを実装 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
