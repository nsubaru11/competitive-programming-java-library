# SegmentTree 利用ガイド

## 概要

`SegmentTree` / `IntSegmentTree` / `LongSegmentTree` は、範囲集約クエリと点更新を高速に処理するセグメント木実装です。  
本ガイドは、ジェネリクス版とプリミティブ特化版をまとめて説明します。

## 特徴

- **3 実装を用途別に選択可能**:
	- `SegmentTree<T>`: 任意型向けの汎用版。
	- `IntSegmentTree`: `int` 向け高速版。
	- `LongSegmentTree`: `long` 向け高速版。
- **ボトムアップ非再帰実装**:
	- すべて配列ベースで動作し、更新・クエリは $O(\log N)$。
- **境界探索を標準搭載**:
	- `maxRight` / `minLeft` により、条件を満たす区間端を探索可能。

## 依存関係

- `java.util`
- `java.util.function`

## 主な機能（メソッド一覧）

### 1. コンストラクタ系メソッド

| メソッド                                                                       | 戻り値の型 | 説明                             |
|----------------------------------------------------------------------------|-------|--------------------------------|
| `SegmentTree(int n, BinaryOperator<T> operator, T identity)`               | `-`   | 要素数 `n` の汎用セグメント木を構築します。       |
| `SegmentTree(T[] data, BinaryOperator<T> operator, T identity)`            | `-`   | 初期配列付きで汎用セグメント木を構築します。         |
| `IntSegmentTree(int n, IntBinaryOperator operator, int identity)`          | `-`   | 要素数 `n` の `int` セグメント木を構築します。  |
| `IntSegmentTree(int[] data, IntBinaryOperator operator, int identity)`     | `-`   | 初期配列付きで `int` セグメント木を構築します。    |
| `LongSegmentTree(int n, LongBinaryOperator operator, long identity)`       | `-`   | 要素数 `n` の `long` セグメント木を構築します。 |
| `LongSegmentTree(long[] data, LongBinaryOperator operator, long identity)` | `-`   | 初期配列付きで `long` セグメント木を構築します。   |

### 2. 更新系メソッド

| メソッド                                                          | 戻り値の型  | 説明                                                       |
|---------------------------------------------------------------|--------|----------------------------------------------------------|
| `set(int i, T e)`                                             | `T`    | `SegmentTree` の位置 `i` を代入し、新しい値を返します。                    |
| `apply(int i, T v, BinaryOperator<T> op)`                     | `T`    | `SegmentTree` の位置 `i` に `op(current, v)` を適用し、新しい値を返します。 |
| `set(int i, int e)`                                           | `int`  | `IntSegmentTree` の位置 `i` を代入し、新しい値を返します。                 |
| `add(int i, int d)`                                           | `int`  | `IntSegmentTree` の位置 `i` に `d` を加算します。                   |
| `multiply(int i, int a)`                                      | `int`  | `IntSegmentTree` の位置 `i` を `a` 倍します。                     |
| `apply(int i, int a, int b)`                                  | `int`  | `IntSegmentTree` の位置 `i` に `x -> a*x + b` を適用します。        |
| `apply(int i, int v, IntBinaryOperator op)`                   | `int`  | `IntSegmentTree` の位置 `i` に二項演算で更新を適用します。                 |
| `set(int i, long e)`                                          | `long` | `LongSegmentTree` の位置 `i` を代入し、新しい値を返します。                |
| `add(int i, long d)`                                          | `long` | `LongSegmentTree` の位置 `i` に `d` を加算します。                  |
| `multiply(int i, long a)`                                     | `long` | `LongSegmentTree` の位置 `i` を `a` 倍します。                    |
| `apply(int i, long a, long b)`                                | `long` | `LongSegmentTree` の位置 `i` に `x -> a*x + b` を適用します。       |
| `apply(int i, long v, LongBinaryOperator op)`                 | `long` | `LongSegmentTree` の位置 `i` に二項演算で更新を適用します。                |
| `fill(T/int/long val)`                                        | `void` | すべての要素を同一値で埋めます。                                         |
| `setAll(IntFunction/IntUnaryOperator/LongUnaryOperator func)` | `void` | インデックスベースで全要素を再設定します。                                    |

### 3. 取得・探索系メソッド

| メソッド                                                           | 戻り値の型            | 説明                       |
|----------------------------------------------------------------|------------------|--------------------------|
| `get(int i)`                                                   | `T / int / long` | 位置 `i` の値を取得します。         |
| `query(int l, int r)`                                          | `T / int / long` | 半開区間 `[l, r)` の集約値を返します。 |
| `queryAll()`                                                   | `T / int / long` | 全体区間の集約値を返します。           |
| `maxRight(int l, Predicate/IntPredicate/LongPredicate tester)` | `int`            | 条件が真でいられる最大右端を返します。      |
| `minLeft(int r, Predicate/IntPredicate/LongPredicate tester)`  | `int`            | 条件が真でいられる最小左端を返します。      |

### 4. その他

| メソッド         | 戻り値の型                             | 説明                 |
|--------------|-----------------------------------|--------------------|
| `size()`     | `int`                             | 要素数を返します。          |
| `iterator()` | `Iterator<T> / PrimitiveIterator` | 要素を順に走査します。        |
| `toString()` | `String`                          | 要素列を空白区切り文字列で返します。 |

## 利用例

```java
SegmentTree<Integer> st = new SegmentTree<>(8, Integer::sum, 0);
st.setAll(i -> i + 1);
st.apply(2, 5, Integer::sum); // a[2] += 5
System.out.println(st.query(0, 4)); // [0,4) の和
```

```java
IntSegmentTree ist = new IntSegmentTree(8, Integer::sum, 0);
ist.setAll(i -> i);
ist.add(3, 10);
System.out.println(ist.maxRight(0, s -> s <= 20));
```

## 注意事項

- 区間はすべて半開区間 `[l, r)` です。
- `SegmentTree<T>` では、`identity` と `operator` が整合しないと不正な結果になります。
- `IntSegmentTree` / `LongSegmentTree` の `apply(i, a, b)` は単一要素更新（遅延なし）です。

## パフォーマンス特性

- **時間計算量**:
	- 構築: $O(N)$
	- `set` / `add` / `multiply` / `apply`（点更新）: $O(\log N)$
	- `query` / `maxRight` / `minLeft`: $O(\log N)$
	- `get`: $O(1)$
- **空間計算量**:
	- $O(N)$

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                                |
|:--------------|:-----------|:--------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-11-25 | `SegmentTree<T>` / `IntSegmentTree` / `LongSegmentTree` を初期実装（`set`/`query`/`queryAll`/`fill`）。   |
| **バージョン 2.0** | 2025-11-27 | 境界探索 API `maxRight` / `minLeft` を追加。                                                              |
| **バージョン 2.1** | 2026-04-30 | `IntSegmentTree` / `LongSegmentTree` に `add` / `multiply` / `apply(i,a,b)` / `apply(i,v,op)` を追加。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
