# BIT 利用ガイド

> 汎用・int・long の1次元、2次元、区間加算に対応する Fenwick Tree の仕様をまとめる。

[← README: Binary Indexed Tree](./README.md) · ソース: [BIT.java](../../../src/lib/ds/fenwick/BIT.java) · [IntBIT.java](../../../src/lib/ds/fenwick/IntBIT.java) · [LongBIT.java](../../../src/lib/ds/fenwick/LongBIT.java) · [IntBIT2D.java](../../../src/lib/ds/fenwick/IntBIT2D.java) · [LongBIT2D.java](../../../src/lib/ds/fenwick/LongBIT2D.java) · [IntRangeBIT.java](../../../src/lib/ds/fenwick/IntRangeBIT.java) · [LongRangeBIT.java](../../../src/lib/ds/fenwick/LongRangeBIT.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

点更新と区間集計を行う。数値版は要素の型と集計の型を分け、int版も和を long で返す。
1次元の長さを n、2次元の高さと幅を h、w とする。添字は0始まりとする。汎用 `BIT<T>` の `query` は半開区間、数値版の `sum` と `add(i, j, d)` は両端を含む閉区間を扱う。閉区間の引数は `i, j`、半開区間の引数は `l, r` とする。

## 特徴

- `BIT<T>` は演算・逆演算・単位元を指定する。
- `IntBIT` / `LongBIT` は点更新・区間和・累積和境界探索を提供する。
- `IntBIT2D` / `LongBIT2D` は点更新・矩形和を提供する。
- `IntRangeBIT` / `LongRangeBIT` は区間加算・区間和を提供する。
- `set` は全クラスで更新前の値を返す。数値版は全要素の和を保持する。
- 構築・`fill`・`setAll` は値を配置した後、共通の `build` で木を O(n) で構築する。Range版の `build` は配置した値から差分を求める。
- 長さ・初期値関数・配列・コレクション・コピーから構築できる。

### 型別クラス

| クラス                                                          | 対象型 | 選ぶ目安                                               |
|-----------------------------------------------------------------|--------|--------------------------------------------------------|
| [`BIT<T>`](../../../src/lib/ds/fenwick/BIT.java)                | `T`    | 加算以外の可換な演算や、逆演算を持つ任意の型を扱う場合 |
| [`IntBIT`](../../../src/lib/ds/fenwick/IntBIT.java)             | `int`  | 要素が int に収まり、和を long で求める点更新・区間和  |
| [`LongBIT`](../../../src/lib/ds/fenwick/LongBIT.java)           | `long` | 要素が long の点更新・区間和                           |
| [`IntBIT2D`](../../../src/lib/ds/fenwick/IntBIT2D.java)         | `int`  | 2次元の点更新・矩形和                                  |
| [`LongBIT2D`](../../../src/lib/ds/fenwick/LongBIT2D.java)       | `long` | 要素が long の2次元の点更新・矩形和                    |
| [`IntRangeBIT`](../../../src/lib/ds/fenwick/IntRangeBIT.java)   | `int`  | 区間加算と区間和を併用する場合                         |
| [`LongRangeBIT`](../../../src/lib/ds/fenwick/LongRangeBIT.java) | `long` | 要素が long の区間加算・区間和                         |

## 依存関係

- Java標準ライブラリ: `java.util.*`、`java.util.function.*`
- 数値版: [IntCollection](../../../src/lib/ds/IntCollection.java) / [LongCollection](../../../src/lib/ds/LongCollection.java)
- LongBIT2D: [IntBinaryToLongFunction](../../../src/lib/util/function/IntBinaryToLongFunction.java)

## API

### `BIT<T>`

[ソース](../../../src/lib/ds/fenwick/BIT.java)

公開フィールドは `final int size` と `final T identity` である。

| 種別        | シグネチャ                                                                                 | 戻り値        | 説明                                                     |
|-------------|--------------------------------------------------------------------------------------------|---------------|----------------------------------------------------------|
| Constructor | `BIT(int n, BinaryOperator<T> op, BinaryOperator<T> inv, T identity)`                      | —             | 全要素を単位元で初期化する。                             |
| Constructor | `BIT(int n, BinaryOperator<T> op, BinaryOperator<T> inv, IntFunction<T> init, T identity)` | —             | 添字順に初期値関数を評価して構築する。                   |
| Constructor | `BIT(T[] a, BinaryOperator<T> op, BinaryOperator<T> inv, T identity)`                      | —             | 配列の要素を添字順に保持して構築する。                   |
| Constructor | `BIT(BIT<T> other)`                                                                        | —             | 内部配列を複製し、演算・逆演算・単位元を共有する。       |
| Method      | `fill(T v)`                                                                                | `void`        | 全要素を指定値で再構築する。                             |
| Method      | `setAll(IntFunction<T> init)`                                                              | `void`        | 添字順に初期値関数を評価して再構築する。                 |
| Method      | `get(int i)`                                                                               | `T`           | 指定位置の値を返す。                                     |
| Method      | `set(int i, T v)`                                                                          | `T`           | 指定値へ更新し、更新前の値を返す。逆演算が必要。         |
| Method      | `apply(int i, T v)`                                                                        | `T`           | 現在値に op を作用させ、更新後の値を返す。               |
| Method      | `query(int r)`                                                                             | `T`           | [0, r) の集計結果を返す。r = 0 は単位元。                |
| Method      | `query(int l, int r)`                                                                      | `T`           | [l, r) の集計結果を返す。l >= r は単位元。逆演算が必要。 |
| Method      | `queryAll()`                                                                               | `T`           | 全要素の集計結果を返す。                                 |
| Method      | `size()`                                                                                   | `int`         | 要素数を返す。                                           |
| Method      | `isEmpty()`                                                                                | `boolean`     | 要素数が0かを返す。                                      |
| Method      | `iterator()`                                                                               | `Iterator<T>` | 添字順に走査する。                                       |
| Method      | `toString()`                                                                               | `String`      | 要素を空白区切りで返す。空の場合は空文字列。             |

`Iterable<T>` の `forEach(Consumer<? super T> action)` と `spliterator()` も継承する。

### `IntBIT`

[ソース](../../../src/lib/ds/fenwick/IntBIT.java)

公開フィールドは `final int size` である。

| 種別        | シグネチャ                                 | 戻り値                    | 説明                                                                      |
|-------------|--------------------------------------------|---------------------------|---------------------------------------------------------------------------|
| Constructor | `IntBIT(int n)`                            | —                         | 全要素を0で初期化する。                                                   |
| Constructor | `IntBIT(int n, IntUnaryOperator init)`     | —                         | 添字順に初期値関数を評価して構築する。                                    |
| Constructor | `IntBIT(int[] a)`                          | —                         | 配列の要素を添字順に保持して構築する。                                    |
| Constructor | `IntBIT(IntCollection a)`                  | —                         | iterator の順に要素を保持して構築する。                                   |
| Constructor | `IntBIT(IntBIT other)`                     | —                         | 内部配列を共有しない独立したコピーを構築する。                            |
| Method      | `fill(int v)`                              | `void`                    | 全要素を指定値で再構築する。                                              |
| Method      | `setAll(IntUnaryOperator init)`            | `void`                    | 構築時と同じ順序で初期値関数を評価して再構築する。                        |
| Method      | `get(int i)`                               | `int`                     | 指定位置の値を返す。                                                      |
| Method      | `set(int i, int v)`                        | `int`                     | 指定値へ更新し、更新前の値を返す。                                        |
| Method      | `add(int i, int d)`                        | `int`                     | 指定位置へ加算し、更新後の値を返す。                                      |
| Method      | `multiply(int i, int m)`                   | `int`                     | 指定位置を m 倍し、更新後の値を返す。                                     |
| Method      | `affine(int i, int m, int d)`              | `int`                     | 指定位置を 現在値 * m + d へ更新し、更新後の値を返す。                    |
| Method      | `apply(int i, int x, IntBinaryOperator f)` | `int`                     | 指定位置を f(現在値, x) へ更新し、更新後の値を返す。                      |
| Method      | `sum(int i)`                               | `long`                    | [0, i] の和を返す。i = -1 は0。                                           |
| Method      | `sum(int i, int j)`                        | `long`                    | [i, j] の和を返す。i > j は0。                                            |
| Method      | `sumAll()`                                 | `long`                    | 全要素の和を返す。                                                        |
| Method      | `lowerBound(long w)`                       | `int`                     | 累積和が w 以上となる最小の添字。w <= 0 は0、該当位置がなければ size。    |
| Method      | `upperBound(long w)`                       | `int`                     | 累積和が w より大きくなる最小の添字。w < 0 は0、該当位置がなければ size。 |
| Method      | `size()`                                   | `int`                     | 全要素数を返す。                                                          |
| Method      | `iterator()`                               | `PrimitiveIterator.OfInt` | 添字順に走査する。                                                        |
| Method      | `forEachInt(IntConsumer action)`           | `void`                    | iterator と同じ順序で各要素に action を適用する。                         |
| Method      | `toArray()`                                | `int[]`                   | iterator と同じ順序の独立した配列を返す。                                 |
| Method      | `toString()`                               | `String`                  | 要素を空白区切りで返す。空の場合は空文字列。                              |

### `LongBIT`

[ソース](../../../src/lib/ds/fenwick/LongBIT.java)

公開フィールドは `final int size` である。

| 種別        | シグネチャ                                   | 戻り値                     | 説明                                                                      |
|-------------|----------------------------------------------|----------------------------|---------------------------------------------------------------------------|
| Constructor | `LongBIT(int n)`                             | —                          | 全要素を0で初期化する。                                                   |
| Constructor | `LongBIT(int n, IntToLongFunction init)`     | —                          | 添字順に初期値関数を評価して構築する。                                    |
| Constructor | `LongBIT(long[] a)`                          | —                          | 配列の要素を添字順に保持して構築する。                                    |
| Constructor | `LongBIT(LongCollection a)`                  | —                          | iterator の順に要素を保持して構築する。                                   |
| Constructor | `LongBIT(LongBIT other)`                     | —                          | 内部配列を共有しない独立したコピーを構築する。                            |
| Method      | `fill(long v)`                               | `void`                     | 全要素を指定値で再構築する。                                              |
| Method      | `setAll(IntToLongFunction init)`             | `void`                     | 構築時と同じ順序で初期値関数を評価して再構築する。                        |
| Method      | `get(int i)`                                 | `long`                     | 指定位置の値を返す。                                                      |
| Method      | `set(int i, long v)`                         | `long`                     | 指定値へ更新し、更新前の値を返す。                                        |
| Method      | `add(int i, long d)`                         | `long`                     | 指定位置へ加算し、更新後の値を返す。                                      |
| Method      | `multiply(int i, long m)`                    | `long`                     | 指定位置を m 倍し、更新後の値を返す。                                     |
| Method      | `affine(int i, long m, long d)`              | `long`                     | 指定位置を 現在値 * m + d へ更新し、更新後の値を返す。                    |
| Method      | `apply(int i, long x, LongBinaryOperator f)` | `long`                     | 指定位置を f(現在値, x) へ更新し、更新後の値を返す。                      |
| Method      | `sum(int i)`                                 | `long`                     | [0, i] の和を返す。i = -1 は0。                                           |
| Method      | `sum(int i, int j)`                          | `long`                     | [i, j] の和を返す。i > j は0。                                            |
| Method      | `sumAll()`                                   | `long`                     | 全要素の和を返す。                                                        |
| Method      | `lowerBound(long w)`                         | `int`                      | 累積和が w 以上となる最小の添字。w <= 0 は0、該当位置がなければ size。    |
| Method      | `upperBound(long w)`                         | `int`                      | 累積和が w より大きくなる最小の添字。w < 0 は0、該当位置がなければ size。 |
| Method      | `size()`                                     | `int`                      | 全要素数を返す。                                                          |
| Method      | `iterator()`                                 | `PrimitiveIterator.OfLong` | 添字順に走査する。                                                        |
| Method      | `forEachLong(LongConsumer action)`           | `void`                     | iterator と同じ順序で各要素に action を適用する。                         |
| Method      | `toArray()`                                  | `long[]`                   | iterator と同じ順序の独立した配列を返す。                                 |
| Method      | `toString()`                                 | `String`                   | 要素を空白区切りで返す。空の場合は空文字列。                              |

### `IntBIT2D`

[ソース](../../../src/lib/ds/fenwick/IntBIT2D.java)

公開フィールドは `final int h, w, size` である。size は h * w を表す。

| 種別        | シグネチャ                                        | 戻り値                    | 説明                                                                  |
|-------------|---------------------------------------------------|---------------------------|-----------------------------------------------------------------------|
| Constructor | `IntBIT2D(int h, int w)`                          | —                         | 全要素を0で初期化する。                                               |
| Constructor | `IntBIT2D(int h, int w, IntBinaryOperator init)`  | —                         | 行優先順に初期値関数を評価して構築する。                              |
| Constructor | `IntBIT2D(int[][] a)`                             | —                         | 長方形の2次元配列を行優先順に保持して構築する。                       |
| Constructor | `IntBIT2D(IntBIT2D other)`                        | —                         | 内部配列を共有しない独立したコピーを構築する。                        |
| Method      | `fill(int v)`                                     | `void`                    | 全要素を指定値で再構築する。                                          |
| Method      | `setAll(IntBinaryOperator init)`                  | `void`                    | 構築時と同じ順序で初期値関数を評価して再構築する。                    |
| Method      | `get(int i, int j)`                               | `int`                     | 指定位置の値を返す。                                                  |
| Method      | `set(int i, int j, int v)`                        | `int`                     | 指定値へ更新し、更新前の値を返す。                                    |
| Method      | `add(int i, int j, int d)`                        | `int`                     | 指定位置へ加算し、更新後の値を返す。                                  |
| Method      | `multiply(int i, int j, int m)`                   | `int`                     | 指定位置を m 倍し、更新後の値を返す。                                 |
| Method      | `affine(int i, int j, int m, int d)`              | `int`                     | 指定位置を 現在値 * m + d へ更新し、更新後の値を返す。                |
| Method      | `apply(int i, int j, int x, IntBinaryOperator f)` | `int`                     | 指定位置を f(現在値, x) へ更新し、更新後の値を返す。                  |
| Method      | `sum(int i, int j)`                               | `long`                    | [0, i] × [0, j] の和を返す。いずれかの終端が -1 なら0。               |
| Method      | `sum(int i1, int j1, int i2, int j2)`             | `long`                    | [i1, i2] × [j1, j2] の和を返す。いずれかの始点が終点を超える場合は0。 |
| Method      | `sumAll()`                                        | `long`                    | 全要素の和を返す。                                                    |
| Method      | `size()`                                          | `int`                     | 全要素数を返す。                                                      |
| Method      | `iterator()`                                      | `PrimitiveIterator.OfInt` | 行優先順に走査する。                                                  |
| Method      | `forEachInt(IntConsumer action)`                  | `void`                    | iterator と同じ順序で各要素に action を適用する。                     |
| Method      | `toArray()`                                       | `int[]`                   | iterator と同じ順序の独立した配列を返す。                             |
| Method      | `toArray2D()`                                     | `int[][]`                 | 元の形状の独立した2次元配列を返す。                                   |
| Method      | `toString()`                                      | `String`                  | 各行を空白区切りとし、行間を改行で区切る。空の場合は空文字列。        |

### `LongBIT2D`

[ソース](../../../src/lib/ds/fenwick/LongBIT2D.java)

公開フィールドは `final int h, w, size` である。size は h * w を表す。

| 種別        | シグネチャ                                              | 戻り値                     | 説明                                                                  |
|-------------|---------------------------------------------------------|----------------------------|-----------------------------------------------------------------------|
| Constructor | `LongBIT2D(int h, int w)`                               | —                          | 全要素を0で初期化する。                                               |
| Constructor | `LongBIT2D(int h, int w, IntBinaryToLongFunction init)` | —                          | 行優先順に初期値関数を評価して構築する。                              |
| Constructor | `LongBIT2D(long[][] a)`                                 | —                          | 長方形の2次元配列を行優先順に保持して構築する。                       |
| Constructor | `LongBIT2D(LongBIT2D other)`                            | —                          | 内部配列を共有しない独立したコピーを構築する。                        |
| Method      | `fill(long v)`                                          | `void`                     | 全要素を指定値で再構築する。                                          |
| Method      | `setAll(IntBinaryToLongFunction init)`                  | `void`                     | 構築時と同じ順序で初期値関数を評価して再構築する。                    |
| Method      | `get(int i, int j)`                                     | `long`                     | 指定位置の値を返す。                                                  |
| Method      | `set(int i, int j, long v)`                             | `long`                     | 指定値へ更新し、更新前の値を返す。                                    |
| Method      | `add(int i, int j, long d)`                             | `long`                     | 指定位置へ加算し、更新後の値を返す。                                  |
| Method      | `multiply(int i, int j, long m)`                        | `long`                     | 指定位置を m 倍し、更新後の値を返す。                                 |
| Method      | `affine(int i, int j, long m, long d)`                  | `long`                     | 指定位置を 現在値 * m + d へ更新し、更新後の値を返す。                |
| Method      | `apply(int i, int j, long x, LongBinaryOperator f)`     | `long`                     | 指定位置を f(現在値, x) へ更新し、更新後の値を返す。                  |
| Method      | `sum(int i, int j)`                                     | `long`                     | [0, i] × [0, j] の和を返す。いずれかの終端が -1 なら0。               |
| Method      | `sum(int i1, int j1, int i2, int j2)`                   | `long`                     | [i1, i2] × [j1, j2] の和を返す。いずれかの始点が終点を超える場合は0。 |
| Method      | `sumAll()`                                              | `long`                     | 全要素の和を返す。                                                    |
| Method      | `size()`                                                | `int`                      | 全要素数を返す。                                                      |
| Method      | `iterator()`                                            | `PrimitiveIterator.OfLong` | 行優先順に走査する。                                                  |
| Method      | `forEachLong(LongConsumer action)`                      | `void`                     | iterator と同じ順序で各要素に action を適用する。                     |
| Method      | `toArray()`                                             | `long[]`                   | iterator と同じ順序の独立した配列を返す。                             |
| Method      | `toArray2D()`                                           | `long[][]`                 | 元の形状の独立した2次元配列を返す。                                   |
| Method      | `toString()`                                            | `String`                   | 各行を空白区切りとし、行間を改行で区切る。空の場合は空文字列。        |

### `IntRangeBIT`

[ソース](../../../src/lib/ds/fenwick/IntRangeBIT.java)

公開フィールドは `final int size` である。

| 種別        | シグネチャ                                  | 戻り値                    | 説明                                               |
|-------------|---------------------------------------------|---------------------------|----------------------------------------------------|
| Constructor | `IntRangeBIT(int n)`                        | —                         | 全要素を0で初期化する。                            |
| Constructor | `IntRangeBIT(int n, IntUnaryOperator init)` | —                         | 添字順に初期値関数を評価して構築する。             |
| Constructor | `IntRangeBIT(int[] a)`                      | —                         | 配列の要素を添字順に保持して構築する。             |
| Constructor | `IntRangeBIT(IntCollection a)`              | —                         | iterator の順に要素を保持して構築する。            |
| Constructor | `IntRangeBIT(IntRangeBIT other)`            | —                         | 内部配列を共有しない独立したコピーを構築する。     |
| Method      | `fill(int v)`                               | `void`                    | 全要素を指定値で再構築する。                       |
| Method      | `setAll(IntUnaryOperator init)`             | `void`                    | 構築時と同じ順序で初期値関数を評価して再構築する。 |
| Method      | `get(int i)`                                | `int`                     | 指定位置の値を返す。                               |
| Method      | `set(int i, int v)`                         | `int`                     | 指定値へ更新し、更新前の値を返す。                 |
| Method      | `add(int i, int d)`                         | `void`                    | 指定位置へ加算する。                               |
| Method      | `multiply(int i, int m)`                    | `void`                    | 指定位置を m 倍する。                              |
| Method      | `affine(int i, int m, int d)`               | `void`                    | 指定位置を 現在値 * m + d へ更新する。             |
| Method      | `apply(int i, int x, IntBinaryOperator f)`  | `void`                    | 指定位置を f(現在値, x) へ更新する。               |
| Method      | `add(int i, int j, int d)`                  | `void`                    | [i, j] へ加算する。i > j は何もしない。            |
| Method      | `sum(int i)`                                | `long`                    | [0, i] の和を返す。i = -1 は0。                    |
| Method      | `sum(int i, int j)`                         | `long`                    | [i, j] の和を返す。i > j は0。                     |
| Method      | `sumAll()`                                  | `long`                    | 全要素の和を返す。                                 |
| Method      | `size()`                                    | `int`                     | 全要素数を返す。                                   |
| Method      | `iterator()`                                | `PrimitiveIterator.OfInt` | 添字順に走査する。                                 |
| Method      | `toString()`                                | `String`                  | 要素を空白区切りで返す。空の場合は空文字列。       |

### `LongRangeBIT`

[ソース](../../../src/lib/ds/fenwick/LongRangeBIT.java)

公開フィールドは `final int size` である。

| 種別        | シグネチャ                                    | 戻り値                     | 説明                                               |
|-------------|-----------------------------------------------|----------------------------|----------------------------------------------------|
| Constructor | `LongRangeBIT(int n)`                         | —                          | 全要素を0で初期化する。                            |
| Constructor | `LongRangeBIT(int n, IntToLongFunction init)` | —                          | 添字順に初期値関数を評価して構築する。             |
| Constructor | `LongRangeBIT(long[] a)`                      | —                          | 配列の要素を添字順に保持して構築する。             |
| Constructor | `LongRangeBIT(LongCollection a)`              | —                          | iterator の順に要素を保持して構築する。            |
| Constructor | `LongRangeBIT(LongRangeBIT other)`            | —                          | 内部配列を共有しない独立したコピーを構築する。     |
| Method      | `fill(long v)`                                | `void`                     | 全要素を指定値で再構築する。                       |
| Method      | `setAll(IntToLongFunction init)`              | `void`                     | 構築時と同じ順序で初期値関数を評価して再構築する。 |
| Method      | `get(int i)`                                  | `long`                     | 指定位置の値を返す。                               |
| Method      | `set(int i, long v)`                          | `long`                     | 指定値へ更新し、更新前の値を返す。                 |
| Method      | `add(int i, long d)`                          | `void`                     | 指定位置へ加算する。                               |
| Method      | `multiply(int i, long m)`                     | `void`                     | 指定位置を m 倍する。                              |
| Method      | `affine(int i, long m, long d)`               | `void`                     | 指定位置を 現在値 * m + d へ更新する。             |
| Method      | `apply(int i, long x, LongBinaryOperator f)`  | `void`                     | 指定位置を f(現在値, x) へ更新する。               |
| Method      | `add(int i, int j, long d)`                   | `void`                     | [i, j] へ加算する。i > j は何もしない。            |
| Method      | `sum(int i)`                                  | `long`                     | [0, i] の和を返す。i = -1 は0。                    |
| Method      | `sum(int i, int j)`                           | `long`                     | [i, j] の和を返す。i > j は0。                     |
| Method      | `sumAll()`                                    | `long`                     | 全要素の和を返す。                                 |
| Method      | `size()`                                      | `int`                      | 全要素数を返す。                                   |
| Method      | `iterator()`                                  | `PrimitiveIterator.OfLong` | 添字順に走査する。                                 |
| Method      | `toString()`                                  | `String`                   | 要素を空白区切りで返す。空の場合は空文字列。       |

### 数値版の継承 API

各数値版は型に応じた `IntCollection` / `LongCollection` を実装する。
`isEmpty`、`contains`、`forEachInt` / `forEachLong`、`spliterator`、`intStream` / `longStream`、`toList`、`toArray`、`forEach` の全シグネチャと既定実装は [配列・コレクションの継承API](../arrays/ArrayGuide.md#継承元のコレクション-api) を参照する。
Range版の配列化は既定実装を使う。数値版の spliterator は既定実装であり、ORDERED は指定しない。

## 利用例

準備中

## 制約・注意事項

- 点の添字は0始まりで、1次元は `[0, size)`、2次元は `[0, h) × [0, w)` とする。配列アクセスで検出できない添字は明示的に検査し、範囲外では `IndexOutOfBoundsException` を投げる。
- 数値版の累積和の終端は1次元が `-1 <= i < size`、2次元が `-1 <= i < h` かつ `-1 <= j < w` とする。終端 -1 は空区間・空領域を表す。汎用 `BIT<T>.query(r)` の終端は `0 <= r <= size` とし、r = 0 は空区間を表す。
- 数値版の区間取得と区間加算は始点が終点を超える場合、汎用 `BIT<T>.query(l, r)` は `l >= r` の場合に空区間として扱う。それ以外は区間全体が有効範囲内にあることを検査する。更新に必要な検査は状態変更前に行う。
- 係数を指定するアフィン更新は `affine`、関数を指定する更新は `apply` とする。汎用 `BIT<T>.apply(i, v)` は op を作用させる既存の操作である。
- int版は、各操作後の各要素が int の範囲内に収まることを前提とする。要素の型を拡張するものではなく、更新差分・添字との積・集計値を long で計算する。要素値のオーバーフロー検査は行わない。
- long版は各要素と計算途中の値が long の範囲内に収まることを前提とする。int版も内部の集計値と計算途中の値は long の範囲内とする。
- 累積和の境界探索は全要素が非負の場合に使う。戻り値は添字または size であり、ビット反転した挿入位置ではない。
- `BIT<T>` の op は結合則・可換性・単位元を満たす演算を前提とする。set と区間 query には op に対応する逆演算 inv が必要である。
- 初期値関数・更新関数・走査コールバックの中で同じインスタンスを変更しない。走査中の変更検出は保証しない。
- iterator の終端後の取得は NoSuchElementException を投げる。

## 性能特性

区間の境界検査後は内部の累積和処理を呼び、同じ座標を再検査しない。

関数の評価時間は別途加算する。2次元の総要素数は n = h * w とする。

| 操作                   | 汎用・1次元の点BIT              | 2次元BIT       | RangeBIT   |
|------------------------|---------------------------------|----------------|------------|
| 構築・fill・setAll     | O(n)                            | O(hw)          | O(n)       |
| get                    | O(1)                            | O(1)           | O(log n)   |
| set・点更新            | O(log n)                        | O(log h log w) | O(log n)   |
| 区間加算               | —                               | —              | O(log n)   |
| 区間集計               | O(log n)                        | O(log h log w) | O(log n)   |
| sumAll                 | O(1)、汎用 queryAll は O(log n) | O(1)           | O(1)       |
| lowerBound・upperBound | 数値版のみ O(log n)             | —              | —          |
| size・iterator生成     | O(1)                            | O(1)           | O(1)       |
| 全要素の走査・文字列化 | O(n)                            | O(hw)          | O(n log n) |
| 数値版の配列化         | O(n)                            | O(hw)          | O(n log n) |
| 本体の使用領域         | O(n)                            | O(hw)          | O(n)       |

配列化・文字列化の追加領域は O(n)、更新・問い合わせは O(1)。
点BITは元の値を保持し、数値版の走査・配列化ではこの配列を直接使う。
1次元の数値版は構築時に保持した headBit を lowerBound / upperBound の両方で使う。
Range版は差分と添字で重み付けした差分を2本のBITに保持するため、1要素の取得にも O(log n) かかる。

- **空間計算量**: 1次元・Range版は O(n)、2次元は O(hw)

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                                                                                                                                            |
|--------------------|------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-02-17 | 初版作成                                                                                                                                                                                                                                                                                        |
| **バージョン 1.1** | 2026-08-10 | 数値型BITのsumAllをO(1)化                                                                                                                                                                                                                                                                       |
| **バージョン 2.0** | 2026-10-27 | int版の集計値と更新差分をlong化。setの戻り値を更新前の値へ統一。公開sizeと走査・配列化を整理し、LongBIT2Dの初期化関数をIntBinaryToLongFunctionへ統一。境界探索でheadBitを共用し、二重ループの書式を統一。汎用BIT.queryを半開区間へ変更                                                          |
| **バージョン 2.1** | 2026-10-27 | 必要な添字・区間検査を追加し、内部の累積和計算と更新処理を分離。係数指定のapplyをaffineへ改名。変数名・メソッド順を統一し、LongRangeBIT.setの重複処理を除去。内部の累積計算をprefixQuery / prefixSumへ改名                                                                                      |
| **バージョン 2.2** | 2026-10-27 | 全クラス: 木の構築を build へ集約し、要素数の引数名を n、閉区間の引数名を i / j に統一。IntBIT2D / LongBIT2D: toString の末尾改行を除去。IntRangeBIT / LongRangeBIT: fill を差分1点の加算へ簡略化                                                                                               |
| **バージョン 3.0** | 2026-10-27 | 全クラス: 配列とコピーからのコンストラクタを追加。BIT<T>: isEmpty を追加。1次元の数値版: IntCollection / LongCollection からのコンストラクタを追加。数値版: toString の初期容量を指定。IntRangeBIT / LongRangeBIT: build で配置した値から差分を求める形へ変更し、fill・setAll・構築の処理を統一 |
