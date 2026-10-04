# SparseTable 利用ガイド

## 概要

Sparse Table は、値を変更しない配列に対して、冪等な区間演算を高速に行うデータ構造です。
長さが `2^k` の区間演算結果を前計算し、クエリ区間を覆う重なった2区間を使って `O(1)` で答えます。

## 特徴

- 構築は `O(N log N)`、クエリは `O(1)`。
- int / long / ジェネリクス版を提供します。
- 入力配列の要素列をコピーするため、構築後に元配列の要素を別の値へ置き換えてもクエリ結果は変わりません。
- 通常の Sparse Table のクエリには、演算の結合則と冪等性が必要です。

## 依存関係

- ジェネリクス版: `java.util.function.BinaryOperator`
- int 版: `java.util.function.IntBinaryOperator`
- long 版: `java.util.function.LongBinaryOperator`

## 主な機能（メソッド一覧）

### 構築

| クラス            | コンストラクタ                                              | 説明                 |
|-------------------|-------------------------------------------------------------|----------------------|
| `SparseTable<T>`  | `SparseTable(T[] data, BinaryOperator<T> operator)`         | ジェネリクス版を構築 |
| `IntSparseTable`  | `IntSparseTable(int[] data, IntBinaryOperator operator)`    | int 特化版を構築     |
| `LongSparseTable` | `LongSparseTable(long[] data, LongBinaryOperator operator)` | long 特化版を構築    |

### 区間クエリ

| クラス            | メソッド              | 戻り値 | 説明                          |
|-------------------|-----------------------|--------|-------------------------------|
| `SparseTable<T>`  | `query(int l, int r)` | `T`    | `[l, r)` に演算を適用した結果 |
| `IntSparseTable`  | `query(int l, int r)` | `int`  | `[l, r)` に演算を適用した結果 |
| `LongSparseTable` | `query(int l, int r)` | `long` | `[l, r)` に演算を適用した結果 |

## 利用例

int 配列の半開区間 `[1, 5)` の最小値を求めます。

```java
int[] a = {7, 2, 5, 1, 6, 4};
IntSparseTable st = new IntSparseTable(a, Math::min);
int min = st.query(1, 5); // 1
```

ジェネリクス版では、比較した2値から小さい方を返す演算を渡します。

```java
Integer[] a = {7, 2, 5, 1, 6, 4};
SparseTable<Integer> st = new SparseTable<>(a, (x, y) -> Math.min(x, y));
int min = st.query(1, 5); // 1
```

## 注意事項

- `query(l, r)` は `0 <= l < r <= N` を満たす非空半開区間を指定してください。空区間の単位元は定義していません。
- 演算は結合的かつ冪等である必要があります。和や積など冪等でない演算には使えません。
- ジェネリクス版は要素オブジェクト自体を複製しません。可変オブジェクトを使う場合、演算結果に影響する内部状態を構築後に変更しないでください。
- 配列の値を更新する用途には向きません。更新が必要な場合はセグメント木を検討してください。
- 結合則のみを満たす演算に対する `O(1)` クエリには Disjoint Sparse Table が適しています。

## パフォーマンス特性

- 構築: `O(N log N)` 時間・空間
- 区間クエリ: `O(1)`
- int / long 版はプリミティブ配列を使い、ボクシングを行いません。

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                  |
|:-------------------|:-----------|:------------------------------------------------------|
| **バージョン 1.0** | 2026-10-05 | ジェネリクス版、int 版、long 版と半開区間クエリを追加 |
