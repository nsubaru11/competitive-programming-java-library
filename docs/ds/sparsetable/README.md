# Sparse Table

## 概要

静的配列に対する冪等な区間演算を前計算し、非空区間への問い合わせを `O(1)` で処理する。
演算は結合則と冪等性を満たす必要がある。典型例は最小値、最大値、GCDである。

## クラス一覧

| クラス            | 用途                                                                                      | Guide                                     | ソース                                                       | Example / 検証 |
|-------------------|-------------------------------------------------------------------------------------------|-------------------------------------------|--------------------------------------------------------------|----------------|
| `SparseTable`     | Sparse Table は、値を変更しない配列に対して、冪等な区間演算を高速に行うデータ構造である。 | [SparseTableGuide](./SparseTableGuide.md) | [Java](../../../src/lib/ds/sparsetable/SparseTable.java)     | —              |
| `IntSparseTable`  | IntSparseTableの用途・状態。                                                              | —                                         | [Java](../../../src/lib/ds/sparsetable/IntSparseTable.java)  | —              |
| `LongSparseTable` | LongSparseTableの用途・状態。                                                             | —                                         | [Java](../../../src/lib/ds/sparsetable/LongSparseTable.java) | —              |

## 実装クラス

- [ジェネリクス版 `SparseTable<T>`](../../../src/lib/ds/sparsetable/SparseTable.java)
- [int 特化版 `IntSparseTable`](../../../src/lib/ds/sparsetable/IntSparseTable.java)
- [long 特化版 `LongSparseTable`](../../../src/lib/ds/sparsetable/LongSparseTable.java)

各クラスは入力配列をコピーして構築する。クエリ区間は半開区間 `[l, r)` である。

## 計算量

- 構築: `O(N log N)` 時間・空間
- クエリ: `O(1)`

詳細は [SparseTable 利用ガイド](./SparseTableGuide.md) を参照すること。
