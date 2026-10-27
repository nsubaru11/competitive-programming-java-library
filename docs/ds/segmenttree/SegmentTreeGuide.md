# SegmentTree 利用ガイド

> `SegmentTree` / `IntSegmentTree` / `LongSegmentTree` は、範囲集約クエリと点更新を高速に処理するセグメント木実装である。

[← README: SegmentTree](./README.md) · ソース: [SegmentTree.java](../../../src/lib/ds/segmenttree/SegmentTree.java) · [IntSegmentTree.java](../../../src/lib/ds/segmenttree/IntSegmentTree.java) · [LongSegmentTree.java](../../../src/lib/ds/segmenttree/LongSegmentTree.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

本ガイドは、ジェネリクス版とプリミティブ特化版をまとめて説明する。以下、`N` は要素数を表す。

## 特徴

- **3 実装を用途別に選択可能**:
	- `SegmentTree<T>`: 任意型向けの汎用版。
	- `IntSegmentTree`: `int` 向け高速版。
	- `LongSegmentTree`: `long` 向け高速版。
- **ボトムアップ非再帰実装**:
	- すべて配列ベースで動作し、更新・クエリは $\mathcal{O}(\log N)$。
- **境界探索を標準搭載**:
	- `maxRight` / `minLeft` により、条件を満たす区間端を探索可能。

## 依存関係

- `java.util`
- `java.util.function`

## API

### 1. コンストラクタ系メソッド

| メソッド                                                                   | 戻り値の型 | 説明                                           |
|----------------------------------------------------------------------------|------------|------------------------------------------------|
| `SegmentTree(int n, BinaryOperator<T> operator, T identity)`               | `-`        | 要素数 `n` の汎用セグメント木を構築する。      |
| `SegmentTree(T[] data, BinaryOperator<T> operator, T identity)`            | `-`        | 初期配列付きで汎用セグメント木を構築する。     |
| `IntSegmentTree(int n, IntBinaryOperator operator, int identity)`          | `-`        | 要素数 `n` の `int` セグメント木を構築する。   |
| `IntSegmentTree(int[] data, IntBinaryOperator operator, int identity)`     | `-`        | 初期配列付きで `int` セグメント木を構築する。  |
| `LongSegmentTree(int n, LongBinaryOperator operator, long identity)`       | `-`        | 要素数 `n` の `long` セグメント木を構築する。  |
| `LongSegmentTree(long[] data, LongBinaryOperator operator, long identity)` | `-`        | 初期配列付きで `long` セグメント木を構築する。 |

### 2. 更新系メソッド

| メソッド                                                      | 戻り値の型 | 説明                                                                    |
|---------------------------------------------------------------|------------|-------------------------------------------------------------------------|
| `set(int i, T v)`                                             | `T`        | `SegmentTree` の位置 `i` に代入し、更新前の値を返す。                     |
| `apply(int i, T v, BinaryOperator<T> op)`                     | `T`        | `SegmentTree` の位置 `i` に `op(current, v)` を適用し、新しい値を返す。 |
| `set(int i, int v)`                                           | `int`      | `IntSegmentTree` の位置 `i` に代入し、更新前の値を返す。                  |
| `add(int i, int d)`                                           | `int`      | `IntSegmentTree` の位置 `i` に `d` を加算する。                         |
| `multiply(int i, int a)`                                      | `int`      | `IntSegmentTree` の位置 `i` を `a` 倍する。                             |
| `apply(int i, int a, int b)`                                  | `int`      | `IntSegmentTree` の位置 `i` に `x -> a*x + b` を適用する。              |
| `apply(int i, int v, IntBinaryOperator op)`                   | `int`      | `IntSegmentTree` の位置 `i` に二項演算で更新を適用する。                |
| `set(int i, long v)`                                          | `long`     | `LongSegmentTree` の位置 `i` に代入し、更新前の値を返す。                 |
| `add(int i, long d)`                                          | `long`     | `LongSegmentTree` の位置 `i` に `d` を加算する。                        |
| `multiply(int i, long a)`                                     | `long`     | `LongSegmentTree` の位置 `i` を `a` 倍する。                            |
| `apply(int i, long a, long b)`                                | `long`     | `LongSegmentTree` の位置 `i` に `x -> a*x + b` を適用する。             |
| `apply(int i, long v, LongBinaryOperator op)`                 | `long`     | `LongSegmentTree` の位置 `i` に二項演算で更新を適用する。               |
| `fill(T/int/long val)`                                        | `void`     | すべての要素を同一値で埋める。                                          |
| `setAll(IntFunction/IntUnaryOperator/IntToLongFunction func)` | `void`     | インデックスベースで全要素を再設定する。                                |

### 3. 取得・探索系メソッド

| メソッド                                                       | 戻り値の型       | 説明                               |
|----------------------------------------------------------------|------------------|------------------------------------|
| `get(int i)`                                                   | `T / int / long` | 位置 `i` の値を取得する。          |
| `query(int l, int r)`                                          | `T / int / long` | 半開区間 `[l, r)` の集約値を返す。 |
| `queryAll()`                                                   | `T / int / long` | 全体区間の集約値を返す。           |
| `maxRight(int l, Predicate/IntPredicate/LongPredicate tester)` | `int`            | 条件が真でいられる最大右端を返す。 |
| `minLeft(int r, Predicate/IntPredicate/LongPredicate tester)`  | `int`            | 条件が真でいられる最小左端を返す。 |

### 4. その他

| メソッド     | 戻り値の型                        | 説明                             |
|--------------|-----------------------------------|----------------------------------|
| `size()`     | `int`                             | 要素数を返す。                   |
| `iterator()` | `Iterator<T> / PrimitiveIterator` | 要素を順に走査する。             |
| `toString()` | `String`                          | 要素列を空白区切り文字列で返す。 |

## 利用例

準備中

## 制約・注意事項

- 区間はすべて半開区間 `[l, r)` である。
- `SegmentTree<T>` では、`identity` と `operator` が整合しないと不正な結果になる。
- `IntSegmentTree` / `LongSegmentTree` の `apply(i, a, b)` は単一要素更新（遅延なし）である。
- `add` / `multiply` / `apply` は更新後の値を返す。
- `SegmentTree<T>.set` は、旧値と新値が `equals` で等しくても指定された参照を代入する。

## 性能特性

- **時間計算量**:
	- 構築: $\mathcal{O}(N)$
	- `set` / `add` / `multiply` / `apply`（点更新）: $\mathcal{O}(\log N)$
	- `query` / `maxRight` / `minLeft`: $\mathcal{O}(\log N)$
	- `get`: $\mathcal{O}(1)$
- **空間計算量**:
	- $\mathcal{O}(N)$

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                    |
|--------------------|------------|---------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-11-25 | `SegmentTree<T>` / `IntSegmentTree` / `LongSegmentTree` を初期実装（`set`/`query`/`queryAll`/`fill`）。 |
| **バージョン 2.0** | 2025-11-27 | 境界探索 API `maxRight` / `minLeft` を追加。                                                            |
| **バージョン 2.1** | 2026-04-30 | `IntSegmentTree` / `LongSegmentTree` に `add` / `multiply` / `apply(i,a,b)` / `apply(i,v,op)` を追加。  |
| **バージョン 2.2** | 2026-10-07 | `LongSegmentTree.setAll` の初期化関数を `IntToLongFunction` に変更。                                    |
| **バージョン 2.3** | 2026-10-27 | 3クラスの `set` を更新前の値を返す仕様に統一。汎用版の同値判定による代入省略を削除し、`apply` の更新後の値を返す仕様を維持。 |
