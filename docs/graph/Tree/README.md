# Tree Algorithms

## 概要

`lib.graph.tree` は木専用の表現と、木の構造を直接利用するアルゴリズムをまとめます。一般グラフの配列表現とは独立した API を持ちます。

## 実装クラス

| クラス                                                                            | 状態     | 用途                                                           |
|-----------------------------------------------------------------------------------|----------|----------------------------------------------------------------|
| [`Tree`](../../../src/lib/graph/tree/Tree.java)                                   | 実装済み | 重み付き・重みなし木の保持、直径・直径長の計算                 |
| [`RootedTree`](../../../src/lib/graph/tree/RootedTree.java)                       | 実装済み | 根付き木の親・深さ・部分木サイズ、LCA、距離、HLDによるパス分解 |
| [`EulerTour`](../../../src/lib/graph/tree/EulerTour.java)                         | TODO     | DFS順序・入退場列の構築。列形式と区間 API は設計中             |
| [`RerootingDP`](../../../src/lib/graph/tree/RerootingDP.java)                     | TODO     | 全頂点を根とした木 DP の一括計算                               |
| [`CentroidDecomposition`](../../../src/lib/graph/tree/CentroidDecomposition.java) | TODO     | 重心分解木の構築                                               |
| [`VirtualTree`](../../../src/lib/graph/tree/VirtualTree.java)                     | TODO     | 指定頂点と LCA による木の圧縮                                  |
| [`TreePathMo`](../../../src/lib/graph/tree/TreePathMo.java)                       | TODO     | 木上パスクエリのオフライン Mo 処理                             |

TODO クラスには公開メソッドがなく、まだ利用できません。ここでは作成予定の API と対象アルゴリズムを一覧化しています。

## API の選択

| 目的                                               | クラス                  |
|----------------------------------------------------|-------------------------|
| 木を入力して直径を求める                           | `Tree`                  |
| LCA、距離、k-th ancestor、HLD でパスを区間分解する | `RootedTree`            |
| 部分木や木上 Mo 用の DFS 列を得る                  | `EulerTour`（未実装）   |
| 全頂点を根とした DP を行う                         | `RerootingDP`（未実装） |
| 少数の指定頂点だけを残した圧縮木を作る             | `VirtualTree`（未実装） |
| オフラインの木上パスクエリを処理する               | `TreePathMo`（未実装）  |

## 設計上の注意

- `Tree` と `RootedTree` は固定長の隣接配列を持ち、構築後の辺追加を想定しています。
- `RootedTree` の `in` / `out` は HLD の頂点順に関する値です。一般的な DFS 入退場時刻と同じ意味だと仮定せず、`EulerTour` の走査列とは区別してください。
- Euler Tour は用途により、各頂点を一度記録する DFS 順序、入場・退場を記録する列、隣接頂点列など複数の形式を指します。`EulerTour` の API では各配列の意味を明記します。
- `TreePathMo` はクエリに対する集計状態を内包せず、頂点の有効状態を反転する処理を利用側から受け取る方針です。

## 関連ドキュメント

- [EulerTour](../EulerTour/README.md)
- [CentroidDecomposition](../CentroidDecomposition/README.md)
- [RootedTree と一般グラフのコア](../Core/README.md)
