# Tree Algorithms

## 概要

`lib.graph.tree` は木専用の表現と、木の構造を直接利用するアルゴリズムをまとめる。一般グラフの配列表現とは独立した API を持つ。

## クラス一覧

| クラス                  | 用途                                                                                                     | Guide                                     | ソース                                                         | Example / 検証                                                         |
|-------------------------|----------------------------------------------------------------------------------------------------------|-------------------------------------------|----------------------------------------------------------------|------------------------------------------------------------------------|
| `HLD`                   | `HLD` は根付き木を heavy-first 順に線形化し、頂点パスや部分木を配列上の半開区間へ分解する。              | [HLDGuide](./HLDGuide.md)                 | [Java](../../../src/lib/graph/tree/HLD.java)                   | [Check1.java](../../../test/verify/graph/tree/hld/Check1.java)         |
| `LCA`                   | `LCA` は、根付き木における2頂点の最小共通祖先（Lowest Common Ancestor: LCA）を高速に求めるクラスである。 | [LCAGuide](./LCAGuide.md)                 | [Java](../../../src/lib/graph/tree/LCA.java)                   | [Check1.java](../../../test/verify/graph/tree/lca/Check1.java)         |
| `RerootingDP`           | `RerootingDP` は、全頂点を根とした木DPを実装するときに使う2方向の走査を提供する。                        | [RerootingDPGuide](./RerootingDPGuide.md) | [Java](../../../src/lib/graph/tree/RerootingDP.java)           | [Check1.java](../../../test/verify/graph/tree/rerootingdp/Check1.java) |
| `Tree`                  | 重み付き・重みなし木の保持、直径・直径長の計算                                                           | —                                         | [Java](../../../src/lib/graph/tree/Tree.java)                  | [Check2.java](../../../test/verify/graph/tree/core/Check2.java)        |
| `RootedTree`            | 親・親辺ID・深さ・部分木サイズ・根からの距離とpreorder区間                                               | —                                         | [Java](../../../src/lib/graph/tree/RootedTree.java)            | [Check1.java](../../../test/verify/graph/tree/core/Check1.java)        |
| `EulerTour`             | 長さ `2N` の入退場イベント列                                                                             | —                                         | [Java](../../../src/lib/graph/tree/EulerTour.java)             | —                                                                      |
| `CentroidDecomposition` | 重心分解木の構築                                                                                         | —                                         | [Java](../../../src/lib/graph/tree/CentroidDecomposition.java) | —                                                                      |
| `VirtualTree`           | 指定頂点と LCA による木の圧縮                                                                            | —                                         | [Java](../../../src/lib/graph/tree/VirtualTree.java)           | —                                                                      |
| `TreePathMo`            | 木上パスクエリのオフライン Mo 処理                                                                       | —                                         | [Java](../../../src/lib/graph/tree/TreePathMo.java)            | [Check1.java](../../../test/verify/graph/tree/treepathmo/Check1.java)  |

## 実装クラス

| クラス                                                                            | 状態     | 用途                                                       |
|-----------------------------------------------------------------------------------|----------|------------------------------------------------------------|
| [`Tree`](../../../src/lib/graph/tree/Tree.java)                                   | 実装済み | 重み付き・重みなし木の保持、直径・直径長の計算             |
| [`RootedTree`](../../../src/lib/graph/tree/RootedTree.java)                       | 実装済み | 親・親辺ID・深さ・部分木サイズ・根からの距離とpreorder区間 |
| [`HLD`](../../../src/lib/graph/tree/HLD.java)                                     | 実装済み | Heavy-Light Decomposition、LCA、パス・部分木の区間分解     |
| [`EulerTour`](../../../src/lib/graph/tree/EulerTour.java)                         | 実装済み | 長さ `2N` の入退場イベント列                               |
| [`LCA`](../../../src/lib/graph/tree/LCA.java)                                     | 実装済み | ±1 RMQ による O(1) LCA クエリ                              |
| [`RerootingDP`](../../../src/lib/graph/tree/RerootingDP.java)                     | 実装済み | 全方位木DP用の子から親・親から子への走査                   |
| [`CentroidDecomposition`](../../../src/lib/graph/tree/CentroidDecomposition.java) | TODO     | 重心分解木の構築                                           |
| [`VirtualTree`](../../../src/lib/graph/tree/VirtualTree.java)                     | TODO     | 指定頂点と LCA による木の圧縮                              |
| [`TreePathMo`](../../../src/lib/graph/tree/TreePathMo.java)                       | 実装済み | 木上パスクエリのオフライン Mo 処理                         |

TODO クラスは未実装であり、まだ利用できない。ここでは作成予定の API と対象アルゴリズムを一覧化している。

## API の選択

| 目的                                                 | クラス                  |
|------------------------------------------------------|-------------------------|
| 木を入力して直径を求める                             | `Tree`                  |
| preorder順の部分木区間、祖先判定、根からの情報を得る | `RootedTree`            |
| 親頂点と親子間の論理辺IDを得る                       | `RootedTree`            |
| LCA、距離、k-th ancestor、パスを区間分解する         | `HLD`                   |
| 静的な木上で O(1) の LCA クエリを行う                | `LCA`                   |
| 入退場列を使った処理・木上 Mo のイベント順を得る     | `EulerTour`             |
| 全頂点を根とした DP を行う                           | `RerootingDP`           |
| 少数の指定頂点だけを残した圧縮木を作る               | `VirtualTree`（未実装） |
| オフラインの木上パスクエリを処理する                 | `TreePathMo`            |

## 設計上の注意

- `RootedTree` のDFS情報は最初の情報参照時に構築される。全辺を追加した後に利用し、構築後は木を変更しないこと。
- `RootedTree.parent(u)` は親頂点、`RootedTree.parentEdge(u)` は親との無向辺IDを返す。根の `parentEdge` は `-1` である。辺IDは追加順の0始まりの論理辺IDで、辺ごとの値を使う全方位木DPなどで利用できる。
- 子 `v` の親辺に付いた値を参照するときは、`parentEdge(v)` を添字に使える。
- `RootedTree.querySubtree` と `HLD.querySubtree` の範囲コールバックは、半開区間端点 `int l, int r` を受け取り `long` を返す `IntBinaryToLongFunction` である。
- `RootedTree` は明示スタックで preorder を作り、逆 preorder で子の部分木サイズを親へ加算する。再帰 DFS は使わない。
- `RootedTree.in/out` は通常のDFS preorder上の部分木区間である。`HLD.enter/exit` はheavy-first順の部分木区間で、互いに異なる番号付けである。
- `EulerTour` は各頂点の入場・退場を記録する長さ `2N` の列である。LCA用のDFS往復列とは異なる。
- `LCA` はオイラーツアーによる ±1 RMQ を用いて前処理 $O(N)$、各クエリ $O(1)$ で最小共通祖先を求める。
- 部分木の区間クエリには `RootedTree` または `HLD` の頂点1回の列を使う。`EulerTour` は入退場イベントを必要とする処理に使う。
- `TreePathMo` は各パスを Euler Tour 上の区間へ変換し、LCA を一時的に加えることで頂点の追加・削除コールバックを呼び分ける。集計状態とクエリ結果の保存は利用側が行う。クエリはオフラインで並べ替えて処理される。

## 関連ドキュメント

- [HLD 利用ガイド](./HLDGuide.md)
- [LCA 利用ガイド](./LCAGuide.md)
- [RerootingDP 利用ガイド](./RerootingDPGuide.md)
- [EulerTour](../../ds/eulertour/README.md)
- [CentroidDecomposition](../centroiddecomposition/README.md)
- [RootedTree と一般グラフのコア](../core/README.md)
