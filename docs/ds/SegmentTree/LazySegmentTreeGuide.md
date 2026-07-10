# LazySegmentTree 利用ガイド

## 概要

`LazySegmentTree` / `IntLazySegmentTree` / `LongLazySegmentTree` は、遅延伝播付きセグメント木です。  
区間更新と区間クエリを両立し、用途に応じてジェネリクス版とプリミティブ特化版を選択できます。

## 特徴

- **区間更新 + 区間クエリ**:
	- `apply(l, r, v)` による遅延更新をサポートします。
- **3 実装を用意**:
	- `LazySegmentTree<T>`: 任意型向け。
	- `IntLazySegmentTree`: `int` 向け。
	- `LongLazySegmentTree`: `long` 向け。
- **合成可能な遅延関数**:
	- `mapping`（ノード反映）と `composition`（遅延値合成）を注入できます。

## 依存関係

- `java.util`
- `java.util.function`

## 主な機能（メソッド一覧）

### 1. コンストラクタ系メソッド

| メソッド                                                                                                                                       | 戻り値の型 | 説明                             |
|--------------------------------------------------------------------------------------------------------------------------------------------|-------|--------------------------------|
| `LazySegmentTree(int n, BinaryOperator<T> operator, T identity)`                                                                           | `-`   | 既定の遅延関数（代入型）で汎用遅延セグ木を構築します。    |
| `LazySegmentTree(int n, BinaryOperator<T> operator, T identity, BiFunction<T, T, T> mapping, BinaryOperator<T> composition)`               | `-`   | 遅延反映/合成関数を明示して構築します。           |
| `LazySegmentTree(T[] data, BinaryOperator<T> operator, T identity)`                                                                        | `-`   | 初期配列付きで構築します。                  |
| `LazySegmentTree(T[] data, BinaryOperator<T> operator, T identity, BiFunction<T, T, T> mapping, BinaryOperator<T> composition)`            | `-`   | 初期配列 + 遅延関数指定で構築します。           |
| `IntLazySegmentTree(int n, IntBinaryOperator operator, int identity)`                                                                      | `-`   | 既定の遅延関数で `int` 版を構築します。        |
| `IntLazySegmentTree(int n, IntBinaryOperator operator, int identity, IntBinaryOperator mapping, IntBinaryOperator composition)`            | `-`   | `int` 版の遅延関数を明示して構築します。        |
| `IntLazySegmentTree(int[] data, IntBinaryOperator operator, int identity)`                                                                 | `-`   | 初期配列付き `int` 版を構築します。          |
| `IntLazySegmentTree(int[] data, IntBinaryOperator operator, int identity, IntBinaryOperator mapping, IntBinaryOperator composition)`       | `-`   | 初期配列 + 遅延関数指定の `int` 版を構築します。  |
| `LongLazySegmentTree(int n, LongBinaryOperator operator, long identity)`                                                                   | `-`   | 既定の遅延関数で `long` 版を構築します。       |
| `LongLazySegmentTree(int n, LongBinaryOperator operator, long identity, LongBinaryOperator mapping, LongBinaryOperator composition)`       | `-`   | `long` 版の遅延関数を明示して構築します。       |
| `LongLazySegmentTree(long[] data, LongBinaryOperator operator, long identity)`                                                             | `-`   | 初期配列付き `long` 版を構築します。         |
| `LongLazySegmentTree(long[] data, LongBinaryOperator operator, long identity, LongBinaryOperator mapping, LongBinaryOperator composition)` | `-`   | 初期配列 + 遅延関数指定の `long` 版を構築します。 |

### 2. 更新系メソッド

| メソッド                                                          | 戻り値の型  | 説明                          |
|---------------------------------------------------------------|--------|-----------------------------|
| `set(int i, T/int/long v)`                                    | `void` | 位置 `i` を直接更新します。            |
| `apply(int i, T/int/long v)`                                  | `void` | 単一点 `[i, i+1)` に遅延更新を適用します。 |
| `apply(int l, int r, T/int/long v)`                           | `void` | 区間 `[l, r)` に遅延更新を適用します。    |
| `fill(T/int/long val)`                                        | `void` | 全要素を同じ値で再初期化します。            |
| `setAll(IntFunction/IntUnaryOperator/LongUnaryOperator func)` | `void` | 全要素を関数で再設定します。              |

### 3. 取得・探索系メソッド

| メソッド                                                           | 戻り値の型            | 説明                            |
|----------------------------------------------------------------|------------------|-------------------------------|
| `get(int i)`                                                   | `T / int / long` | 位置 `i` の値を取得します（必要に応じて遅延を伝播）。 |
| `query(int l, int r)`                                          | `T / int / long` | 区間 `[l, r)` の集約値を返します。        |
| `queryAll()`                                                   | `T / int / long` | 全体区間の集約値を返します。                |
| `maxRight(int l, Predicate/IntPredicate/LongPredicate tester)` | `int`            | 条件が真でいられる最大右端を探索します。          |
| `minLeft(int r, Predicate/IntPredicate/LongPredicate tester)`  | `int`            | 条件が真でいられる最小左端を探索します。          |

### 4. その他

| メソッド         | 戻り値の型                             | 説明                      |
|--------------|-----------------------------------|-------------------------|
| `size()`     | `int`                             | 要素数を返します。               |
| `iterator()` | `Iterator<T> / PrimitiveIterator` | 葉要素を順に走査します。            |
| `toString()` | `String`                          | 葉要素を空白区切りで連結した文字列を返します。 |

## 利用例

```java
// 区間代入 + 区間最大値の例（int版）
IntLazySegmentTree seg = new IntLazySegmentTree(
	8,
	Math::max,
	Integer.MIN_VALUE,
	(f, x) -> f, // 代入
	(f, g) -> f  // 新しい代入が優先
);
seg.apply(2, 6, 10);
System.out.println(seg.query(0, 8)); // 10
```

```java
// 汎用版（和）の例
LazySegmentTree<Integer> seg = new LazySegmentTree<>(
	8,
	Integer::sum,
	0,
	(f, x) -> x + f,
	Integer::sum
);
seg.apply(0, 8, 3);
System.out.println(seg.query(0, 4));
```

## 注意事項

- 区間は半開区間 `[l, r)` です。
- `mapping` は「遅延値 `f` を現在ノード値 `x` にどう反映するか」、`composition` は「新しい遅延値と既存遅延値をどう合成するか」を表します。
- 集約値が区間長に依存する場合（例: 区間和への一様加算）は、ノード型に区間長情報を持たせるなど設計が必要です。

## パフォーマンス特性

- **時間計算量**:
	- 構築: $O(N)$
	- `set` / `apply`（区間・点）: $O(\log N)$
	- `query` / `maxRight` / `minLeft`: $O(\log N)$
	- `get`: $O(\log N)$
- **空間計算量**:
	- $O(N)$

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                                           |
|:--------------|:-----------|:-------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-30 | `LazySegmentTree<T>` / `IntLazySegmentTree` / `LongLazySegmentTree` を初期実装（`apply(l,r,v)` による区間更新 + `query`）。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
