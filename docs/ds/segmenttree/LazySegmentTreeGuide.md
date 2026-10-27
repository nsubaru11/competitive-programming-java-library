# LazySegmentTree 利用ガイド

> `LazySegmentTree` / `IntLazySegmentTree` / `LongLazySegmentTree` は、遅延伝播付きセグメント木である。

[← README: SegmentTree](./README.md) · ソース: [LazySegmentTree.java](../../../src/lib/ds/segmenttree/LazySegmentTree.java) · [IntLazySegmentTree.java](../../../src/lib/ds/segmenttree/IntLazySegmentTree.java) · [LongLazySegmentTree.java](../../../src/lib/ds/segmenttree/LongLazySegmentTree.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

区間更新と区間クエリを両立し、用途に応じてジェネリクス版とプリミティブ特化版を選択できる。以下、`N` は要素数を表す。

## 特徴

- **区間更新 + 区間クエリ**:
	- `apply(l, r, v)` による遅延更新をサポートする。
- **3 実装を用意**:
	- `LazySegmentTree<T>`: 任意型向け。
	- `IntLazySegmentTree`: `int` 向け。
	- `LongLazySegmentTree`: `long` 向け。
- **合成可能な遅延関数**:
	- `mapping`（ノード反映）と `composition`（遅延値合成）を注入できる。

## 依存関係

- `java.util`
- `java.util.function`

## API

### 1. コンストラクタ系メソッド

| メソッド                                                                                                                                   | 戻り値の型 | 説明                                                 |
|--------------------------------------------------------------------------------------------------------------------------------------------|------------|------------------------------------------------------|
| `LazySegmentTree(int n, BinaryOperator<T> operator, T identity)`                                                                           | `-`        | 既定の遅延関数（代入型）で汎用遅延セグ木を構築する。 |
| `LazySegmentTree(int n, BinaryOperator<T> operator, T identity, BiFunction<T, T, T> mapping, BinaryOperator<T> composition)`               | `-`        | 遅延反映/合成関数を明示して構築する。                |
| `LazySegmentTree(T[] data, BinaryOperator<T> operator, T identity)`                                                                        | `-`        | 初期配列付きで構築する。                             |
| `LazySegmentTree(T[] data, BinaryOperator<T> operator, T identity, BiFunction<T, T, T> mapping, BinaryOperator<T> composition)`            | `-`        | 初期配列 + 遅延関数指定で構築する。                  |
| `IntLazySegmentTree(int n, IntBinaryOperator operator, int identity)`                                                                      | `-`        | 既定の遅延関数で `int` 版を構築する。                |
| `IntLazySegmentTree(int n, IntBinaryOperator operator, int identity, IntBinaryOperator mapping, IntBinaryOperator composition)`            | `-`        | `int` 版の遅延関数を明示して構築する。               |
| `IntLazySegmentTree(int[] data, IntBinaryOperator operator, int identity)`                                                                 | `-`        | 初期配列付き `int` 版を構築する。                    |
| `IntLazySegmentTree(int[] data, IntBinaryOperator operator, int identity, IntBinaryOperator mapping, IntBinaryOperator composition)`       | `-`        | 初期配列 + 遅延関数指定の `int` 版を構築する。       |
| `LongLazySegmentTree(int n, LongBinaryOperator operator, long identity)`                                                                   | `-`        | 既定の遅延関数で `long` 版を構築する。               |
| `LongLazySegmentTree(int n, LongBinaryOperator operator, long identity, LongBinaryOperator mapping, LongBinaryOperator composition)`       | `-`        | `long` 版の遅延関数を明示して構築する。              |
| `LongLazySegmentTree(long[] data, LongBinaryOperator operator, long identity)`                                                             | `-`        | 初期配列付き `long` 版を構築する。                   |
| `LongLazySegmentTree(long[] data, LongBinaryOperator operator, long identity, LongBinaryOperator mapping, LongBinaryOperator composition)` | `-`        | 初期配列 + 遅延関数指定の `long` 版を構築する。      |

### 2. 更新系メソッド

| メソッド                                                      | 戻り値の型 | 説明                                     |
|---------------------------------------------------------------|------------|------------------------------------------|
| `set(int i, T v)`                                            | `T`        | `LazySegmentTree` の位置 `i` を更新し、更新前の値を返す。 |
| `set(int i, int v)`                                          | `int`      | `IntLazySegmentTree` の位置 `i` を更新し、更新前の値を返す。 |
| `set(int i, long v)`                                         | `long`     | `LongLazySegmentTree` の位置 `i` を更新し、更新前の値を返す。 |
| `apply(int i, T/int/long v)`                                  | `void`     | 単一点 `[i, i+1)` に遅延更新を適用する。 |
| `apply(int l, int r, T/int/long v)`                           | `void`     | 区間 `[l, r)` に遅延更新を適用する。     |
| `fill(T/int/long val)`                                        | `void`     | 全要素を同じ値で再初期化する。           |
| `setAll(IntFunction/IntUnaryOperator/IntToLongFunction func)` | `void`     | 全要素を関数で再設定する。               |

### 3. 取得・探索系メソッド

| メソッド                                                       | 戻り値の型       | 説明                                                |
|----------------------------------------------------------------|------------------|-----------------------------------------------------|
| `get(int i)`                                                   | `T / int / long` | 位置 `i` の値を取得する（必要に応じて遅延を伝播）。 |
| `query(int l, int r)`                                          | `T / int / long` | 区間 `[l, r)` の集約値を返す。                      |
| `queryAll()`                                                   | `T / int / long` | 全体区間の集約値を返す。                            |
| `maxRight(int l, Predicate/IntPredicate/LongPredicate tester)` | `int`            | 条件が真でいられる最大右端を探索する。              |
| `minLeft(int r, Predicate/IntPredicate/LongPredicate tester)`  | `int`            | 条件が真でいられる最小左端を探索する。              |

### 4. その他

| メソッド     | 戻り値の型                        | 説明                                       |
|--------------|-----------------------------------|--------------------------------------------|
| `size()`     | `int`                             | 要素数を返す。                             |
| `iterator()` | `Iterator<T> / PrimitiveIterator` | 葉要素を順に走査する。                     |
| `toString()` | `String`                          | 葉要素を空白区切りで連結した文字列を返す。 |

## 利用例

準備中

## 制約・注意事項

- 区間は半開区間 `[l, r)` である。
- `mapping` は「遅延値 `f` を現在ノード値 `x` にどう反映するか」、`composition` は「新しい遅延値と既存遅延値をどう合成するか」を表す。
- 集約値が区間長に依存する場合（例: 区間和への一様加算）は、ノード型に区間長情報を持たせるなど設計が必要である。
- `set` の戻り値には、その位置に対する保留中の遅延操作も反映される。

## 性能特性

`set` は対象の葉への遅延伝播を1回行い、旧値の取得と代入を同じ処理内で行う。

- **時間計算量**:
	- 構築: $\mathcal{O}(N)$
	- `set` / `apply`（区間・点）: $\mathcal{O}(\log N)$
	- `query` / `maxRight` / `minLeft`: $\mathcal{O}(\log N)$
	- `get`: $\mathcal{O}(\log N)$
- **空間計算量**:
	- $\mathcal{O}(N)$

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                                        |
|--------------------|------------|-----------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-30 | `LazySegmentTree<T>` / `IntLazySegmentTree` / `LongLazySegmentTree` を初期実装（`apply(l,r,v)` による区間更新 + `query`）。 |
| **バージョン 1.1** | 2026-10-07 | `LongLazySegmentTree.setAll` の初期化関数を `IntToLongFunction` に変更。                                                    |
| **バージョン 2.0** | 2026-10-27 | 3クラスの単一点 `set` に更新前の値を返す機能を追加。遅延伝播後に旧値を取得。 |
