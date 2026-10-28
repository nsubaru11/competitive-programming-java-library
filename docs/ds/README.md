# `lib.ds`

`src/lib/ds`にあるデータ構造の実装一覧である。各分野のREADMEからクラスの用途、ソース、詳細Guideを確認できる。

| 分野                                                         | 主な実装                                      |
|--------------------------------------------------------------|-----------------------------------------------|
| [配列ユーティリティ](./arrays/README.md)                     | プリミティブ配列、累積和、deque、座標圧縮など |
| [AVL木](./avltree/README.md)                                 | 順序付きSet・Multiset、int / long特化版       |
| [二分探索木](./binarysearchtree/README.md)                   | BST、Treap、B木、赤黒木                       |
| [Fenwick Tree](./fenwick/README.md)                          | BIT、2D BIT、区間加算BIT                      |
| [HashMap](./map/README.md)                                   | プリミティブ特化Map、ペア・トリプルキーMap    |
| [Li Chao Tree](./lichaotree/README.md)                       | Li Chao Tree                                  |
| [Persistent Segment Tree](./persistentsegmenttree/README.md) | 永続セグメント木                              |
| [Persistent Union-Find](./persistentunionfind/README.md)     | 永続Union-Find                                |
| [Priority Queue](./priorityqueue/README.md)                  | 優先度キュー、indexed priority queue          |
| [Segment Tree](./segmenttree/README.md)                      | 通常・遅延・二乗和セグメント木                |
| [2D Segment Tree](./segmenttree2d/README.md)                 | 二次元セグメント木                            |
| [Skip List](./skiplist/README.md)                            | Skip List                                     |
| [Sparse Table](./sparsetable/README.md)                      | 静的区間クエリ                                |
| [Trie](./trie/README.md)                                     | Trieと文字列・整数キーの派生構造              |
| [Union-Find](./unionfind/README.md)                          | 素集合データ構造と派生実装                    |
| [Wavelet Tree](./wavelettree/README.md)                      | Wavelet Tree                                  |

## その他のクラス

独立した実装や、専用READMEをまだ持たないクラスは、ソースから確認できる。

| クラス                                    | ソース                                                                                                                                                    | 主な用途                              |
|-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------|
| `CartesianTree`（実装予定）               | [Java](../../src/lib/ds/CartesianTree.java)                                                                                                               | 配列からCartesian Treeを構築          |
| `DisjointSparseTable`（実装予定）         | [Java](../../src/lib/ds/DisjointSparseTable.java)                                                                                                         | 静的な区間演算を定数時間で問い合わせ  |
| `IntCollection` / `LongCollection`        | [IntCollection.java](../../src/lib/ds/IntCollection.java) · [LongCollection.java](../../src/lib/ds/LongCollection.java)                                   | プリミティブ整数コレクションの共通API |
| `IntervalSet`（実装予定）                 | [Java](../../src/lib/ds/IntervalSet.java)                                                                                                                 | 整数区間集合                          |
| `SlidingWindowAggregation`（実装予定）    | [Java](../../src/lib/ds/SlidingWindowAggregation.java)                                                                                                    | モノイド上のキュー集約                |
| `SegmentTreeBeats`（実装予定）            | [Java](../../src/lib/ds/segmenttree/SegmentTreeBeats.java)                                                                                                | 区間chmin/chmaxを扱うセグメント木     |
| `ImplicitTreap`（実装予定）               | [Java](../../src/lib/ds/set/ImplicitTreap.java)                                                                                                           | 列を管理する暗黙Treap                 |
| `LinkCutTree`（実装予定）                 | [Java](../../src/lib/ds/tree/LinkCutTree.java)                                                                                                            | 動的森上のパス操作                    |
| `BinaryTrie`（実装予定）                  | [Java](../../src/lib/ds/trie/BinaryTrie.java)                                                                                                             | 整数キーTrie                          |
| `RollbackUnionFind` / `WeightedUnionFind` | [RollbackUnionFind.java](../../src/lib/ds/unionfind/RollbackUnionFind.java) · [WeightedUnionFind.java](../../src/lib/ds/unionfind/WeightedUnionFind.java) | 巻き戻し・ポテンシャル管理の実装予定  |
| `WaveletMatrix`                           | [Java](../../src/lib/ds/WaveletMatrix.java)                                                                                                               | 静的数列の順位・頻度クエリの実装予定  |

## 内部パッケージ

| パッケージ             | ソース                                | 関連ドキュメント                                                                                                          |
|------------------------|---------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| `lib.ds.arrays`        | [src](../../src/lib/ds/arrays)        | [Arrays](./arrays/README.md)                                                                                              |
| `lib.ds.fenwick`       | [src](../../src/lib/ds/fenwick)       | [Fenwick Tree](./fenwick/README.md)                                                                                       |
| `lib.ds.map`           | [src](../../src/lib/ds/map)           | [HashMap](./map/README.md)                                                                                                |
| `lib.ds.priorityqueue` | [src](../../src/lib/ds/priorityqueue) | [Priority Queue](./priorityqueue/README.md)                                                                               |
| `lib.ds.segmenttree`   | [src](../../src/lib/ds/segmenttree)   | [Segment Tree](./segmenttree/README.md)、[2D](./segmenttree2d/README.md)、[Persistent](./persistentsegmenttree/README.md) |
| `lib.ds.set`           | [src](../../src/lib/ds/set)           | [AVL Tree](./avltree/README.md)、[Binary Search Tree](./binarysearchtree/README.md)、[Skip List](./skiplist/README.md)    |
| `lib.ds.sparsetable`   | [src](../../src/lib/ds/sparsetable)   | [Sparse Table](./sparsetable/README.md)                                                                                   |
| `lib.ds.tree`          | [src](../../src/lib/ds/tree)          | [`LinkCutTree`](../../src/lib/ds/tree/LinkCutTree.java)（実装予定）                                                       |
| `lib.ds.trie`          | [src](../../src/lib/ds/trie)          | [Trie](./trie/README.md)                                                                                                  |
| `lib.ds.unionfind`     | [src](../../src/lib/ds/unionfind)     | [Union-Find](./unionfind/README.md)、[Persistent](./persistentunionfind/README.md)                                        |
