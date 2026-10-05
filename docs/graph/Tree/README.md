# Tree Algorithms

## 概要

`lib.graph.tree` は木専用の表現と、木の構造を直接利用するアルゴリズムをまとめます。一般グラフの配列表現とは独立した API を持ちます。

## 実装クラス

| クラス                                                                            | 状態     | 用途                                                   |
|-----------------------------------------------------------------------------------|----------|--------------------------------------------------------|
| [`Tree`](../../../src/lib/graph/tree/Tree.java)                                   | 実装済み | 重み付き・重みなし木の保持、直径・直径長の計算         |
| [`RootedTree`](../../../src/lib/graph/tree/RootedTree.java)                       | 実装済み | 親・深さ・部分木サイズ・根からの距離とpreorder区間     |
| [`HLD`](../../../src/lib/graph/tree/HLD.java)                                     | 実装済み | Heavy-Light Decomposition、LCA、パス・部分木の区間分解 |
| [`EulerTour`](../../../src/lib/graph/tree/EulerTour.java)                         | 実装済み | 長さ `2N` の入退場イベント列                           |
| [`LCA`](../../../src/lib/graph/tree/LCA.java)                                     | 実装済み | ±1 RMQ による O(1) LCA クエリ                          |
| [`RerootingDP`](../../../src/lib/graph/tree/RerootingDP.java)                     | TODO     | 全頂点を根とした木 DP の一括計算                       |
| [`CentroidDecomposition`](../../../src/lib/graph/tree/CentroidDecomposition.java) | TODO     | 重心分解木の構築                                       |
| [`VirtualTree`](../../../src/lib/graph/tree/VirtualTree.java)                     | TODO     | 指定頂点と LCA による木の圧縮                          |
| [`TreePathMo`](../../../src/lib/graph/tree/TreePathMo.java)                       | TODO     | 木上パスクエリのオフライン Mo 処理                     |

TODO クラスは未実装であり、まだ利用できません。ここでは作成予定の API と対象アルゴリズムを一覧化しています。

## API の選択

| 目的                                                 | クラス                  |
|------------------------------------------------------|-------------------------|
| 木を入力して直径を求める                             | `Tree`                  |
| preorder順の部分木区間、祖先判定、根からの情報を得る | `RootedTree`            |
| LCA、距離、k-th ancestor、パスを区間分解する         | `HLD`                   |
| 静的な木上で O(1) の LCA クエリを行う                | `LCA`                   |
| 入退場列を使った処理・木上 Mo のイベント順を得る     | `EulerTour`             |
| 全頂点を根とした DP を行う                           | `RerootingDP`（未実装） |
| 少数の指定頂点だけを残した圧縮木を作る               | `VirtualTree`（未実装） |
| オフラインの木上パスクエリを処理する                 | `TreePathMo`（未実装）  |

## 設計上の注意

- `RootedTree` のDFS情報は最初の情報参照時に構築されます。全辺を追加した後に利用し、構築後は木を変更しないでください。
- `RootedTree` は明示スタックで preorder を作り、逆 preorder で子の部分木サイズを親へ加算します。再帰 DFS は使いません。
- `RootedTree.in/out` は通常のDFS preorder上の部分木区間です。`HLD.enter/exit` はheavy-first順の部分木区間で、互いに異なる番号付けです。
- `EulerTour` は各頂点の入場・退場を記録する長さ `2N` の列です。LCA用のDFS往復列とは異なります。
- `LCA` はオイラーツアーによる ±1 RMQ を用いて前処理 $O(N)$、各クエリ $O(1)$ で最小共通祖先を求めます。
- 部分木の区間クエリには `RootedTree` または `HLD` の頂点1回の列を使います。`EulerTour` は入退場イベントを必要とする処理に使います。
- `TreePathMo` はクエリに対する集計状態を内包せず、頂点の有効状態を反転する処理を利用側から受け取る方針です。

## 関連ドキュメント

- [LCA 利用ガイド](./LCAGuide.md)
- [EulerTour](../../ds/EulerTour/README.md)
- [CentroidDecomposition](../CentroidDecomposition/README.md)
- [RootedTree と一般グラフのコア](../Core/README.md)
