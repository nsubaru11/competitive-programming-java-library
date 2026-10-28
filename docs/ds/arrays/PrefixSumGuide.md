# PrefixSum / PrefixModSum 利用ガイド

> `IntPrefixSum` / `LongPrefixSum` とその2D・3D版は、構築後に更新しない配列の累積和を保持する。

[← README: Primitive Array Utilities](./README.md) · ソース: [IntPrefixSum.java](../../../src/lib/ds/arrays/IntPrefixSum.java) · [IntPrefixSum2D.java](../../../src/lib/ds/arrays/IntPrefixSum2D.java) · [IntPrefixSum3D.java](../../../src/lib/ds/arrays/IntPrefixSum3D.java) · [LongPrefixSum.java](../../../src/lib/ds/arrays/LongPrefixSum.java) · [LongPrefixSum2D.java](../../../src/lib/ds/arrays/LongPrefixSum2D.java) · [LongPrefixSum3D.java](../../../src/lib/ds/arrays/LongPrefixSum3D.java) · [IntPrefixModSum.java](../../../src/lib/ds/arrays/IntPrefixModSum.java) · [IntPrefixModSum2D.java](../../../src/lib/ds/arrays/IntPrefixModSum2D.java) · [IntPrefixModSum3D.java](../../../src/lib/ds/arrays/IntPrefixModSum3D.java) · [LongPrefixModSum.java](../../../src/lib/ds/arrays/LongPrefixModSum.java) · [LongPrefixModSum2D.java](../../../src/lib/ds/arrays/LongPrefixModSum2D.java) · [LongPrefixModSum3D.java](../../../src/lib/ds/arrays/LongPrefixModSum3D.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

`IntPrefixModSum` / `LongPrefixModSum` とその2D・3D版は、同じ累積和を法 `mod` で保持する。
1Dの閉区間、2Dの閉矩形、3Dの閉直方体の和をいずれも $\mathcal{O}(1)$ で取得できる。

## 特徴

- 値を配置した後、共通の `build` で累積和を計算する
- 内部に先頭ゼロの境界を設け、1Dは `n + 1`、2Dは `(h + 1)(w + 1)`、3Dは `(d + 1)(h + 1)(w + 1)` 要素で保持
- 多次元版も一次元プリミティブ配列へ圧縮
- `IntPrefixSum` 系は全次元で累積値を `long[]` で保持し、int列の合計をlongで返す
- `IntPrefixModSum` 系は剰余値がintに収まるため `int[]` で保持
- 多次元MOD版は包含排除式を1回の走査で計算し、剰余を `[0, mod)` に正規化
- 配列と、座標を受け取る初期化関数から構築可能
- 全体和、原点からの累積和、任意区間の和、元の1要素を取得可能
- 全ての区間指定を両端を含む閉区間で統一

### 型別クラス

| クラス                                                                     | 対象型 | 選ぶ目安                           |
|----------------------------------------------------------------------------|--------|------------------------------------|
| [`IntPrefixSum`](../../../src/lib/ds/arrays/IntPrefixSum.java)             | `int`  | 1次元の累積和。和は long で返す    |
| [`IntPrefixSum2D`](../../../src/lib/ds/arrays/IntPrefixSum2D.java)         | `int`  | 2次元の累積和。和は long で返す    |
| [`IntPrefixSum3D`](../../../src/lib/ds/arrays/IntPrefixSum3D.java)         | `int`  | 3次元の累積和。和は long で返す    |
| [`LongPrefixSum`](../../../src/lib/ds/arrays/LongPrefixSum.java)           | `long` | 1次元の累積和                      |
| [`LongPrefixSum2D`](../../../src/lib/ds/arrays/LongPrefixSum2D.java)       | `long` | 2次元の累積和                      |
| [`LongPrefixSum3D`](../../../src/lib/ds/arrays/LongPrefixSum3D.java)       | `long` | 3次元の累積和                      |
| [`IntPrefixModSum`](../../../src/lib/ds/arrays/IntPrefixModSum.java)       | `int`  | 1次元の累積和を法 mod で求める場合 |
| [`IntPrefixModSum2D`](../../../src/lib/ds/arrays/IntPrefixModSum2D.java)   | `int`  | 2次元の累積和を法 mod で求める場合 |
| [`IntPrefixModSum3D`](../../../src/lib/ds/arrays/IntPrefixModSum3D.java)   | `int`  | 3次元の累積和を法 mod で求める場合 |
| [`LongPrefixModSum`](../../../src/lib/ds/arrays/LongPrefixModSum.java)     | `long` | 1次元の累積和を法 mod で求める場合 |
| [`LongPrefixModSum2D`](../../../src/lib/ds/arrays/LongPrefixModSum2D.java) | `long` | 2次元の累積和を法 mod で求める場合 |
| [`LongPrefixModSum3D`](../../../src/lib/ds/arrays/LongPrefixModSum3D.java) | `long` | 3次元の累積和を法 mod で求める場合 |

## 依存関係

- `java.util.Objects`
- `java.util.PrimitiveIterator`
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntBinaryOperator`
- `java.util.function.IntToLongFunction`
- [`lib.util.function.IntBinaryToLongFunction`](../../../src/lib/util/function/IntBinaryToLongFunction.java)
- [`lib.util.function.IntTernaryOperator`](../../../src/lib/util/function/IntTernaryOperator.java)
- [`lib.util.function.IntTernaryToLongFunction`](../../../src/lib/util/function/IntTernaryToLongFunction.java)
- [`lib.ds.IntCollection`](../../../src/lib/ds/IntCollection.java) / [`LongCollection`](../../../src/lib/ds/LongCollection.java)
- `java.util.Spliterator` / `Spliterators`
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)

## API

### `IntPrefixSum`

| 種別        | シグネチャ                                   | 戻り値                    | 説明                                                                               |
|-------------|----------------------------------------------|---------------------------|------------------------------------------------------------------------------------|
| Constructor | `IntPrefixSum(int n, IntUnaryOperator init)` | —                         | init を添字の昇順に1回ずつ呼び出して構築する。                                     |
| Constructor | `IntPrefixSum(int[] a)`                      | —                         | 指定配列の論理順で構築する。入力は変更しない。                                     |
| Constructor | `IntPrefixSum(IntArray a)`                   | —                         | 指定配列の論理順で構築する。入力は変更しない。                                     |
| Method      | `size()`                                     | `int`                     | 要素数を返す。                                                                     |
| Method      | `get(int i)`                                 | `int`                     | 指定した有効な論理添字の値を返す。                                                 |
| Method      | `sumAll()`                                   | `long`                    | 全要素の和を返す。                                                                 |
| Method      | `sum(int i)`                                 | `long`                    | 閉区間 [0, i] の和を返す。                                                         |
| Method      | `sum(int i, int j)`                          | `long`                    | 閉区間 [i, j] の和を返す。                                                         |
| Method      | `iterator()`                                 | `PrimitiveIterator.OfInt` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。 |
| Method      | `toString()`                                 | `String`                  | 元の要素を半角スペースで区切った文字列として返す。                                 |

### `LongPrefixSum`

| 種別        | シグネチャ                                     | 戻り値                     | 説明                                                                               |
|-------------|------------------------------------------------|----------------------------|------------------------------------------------------------------------------------|
| Constructor | `LongPrefixSum(int n, IntToLongFunction init)` | —                          | init を添字の昇順に1回ずつ呼び出して構築する。                                     |
| Constructor | `LongPrefixSum(long[] a)`                      | —                          | 指定配列の論理順で構築する。入力は変更しない。                                     |
| Constructor | `LongPrefixSum(LongArray a)`                   | —                          | 指定配列の論理順で構築する。入力は変更しない。                                     |
| Method      | `size()`                                       | `int`                      | 要素数を返す。                                                                     |
| Method      | `get(int i)`                                   | `long`                     | 指定した有効な論理添字の値を返す。                                                 |
| Method      | `sumAll()`                                     | `long`                     | 全要素の和を返す。                                                                 |
| Method      | `sum(int i)`                                   | `long`                     | 閉区間 [0, i] の和を返す。                                                         |
| Method      | `sum(int i, int j)`                            | `long`                     | 閉区間 [i, j] の和を返す。                                                         |
| Method      | `iterator()`                                   | `PrimitiveIterator.OfLong` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。 |
| Method      | `toString()`                                   | `String`                   | 元の要素を半角スペースで区切った文字列として返す。                                 |

### `IntPrefixSum2D`

| 種別        | シグネチャ                                             | 戻り値                    | 説明                                                                         |
|-------------|--------------------------------------------------------|---------------------------|------------------------------------------------------------------------------|
| Constructor | `IntPrefixSum2D(int h, int w, IntBinaryOperator init)` | —                         | 各座標に init(i, j) を行優先順で適用して構築する。                           |
| Constructor | `IntPrefixSum2D(int[][] a)`                            | —                         | 指定された配列から構築する。                                                 |
| Method      | `size()`                                               | `int`                     | 全要素数を返す。                                                             |
| Method      | `inRange(int i, int j)`                                | `boolean`                 | 0 <= i < h かつ 0 <= j < w かを返す。                                        |
| Method      | `get(int i, int j)`                                    | `int`                     | 指定位置の元の値を返す。                                                     |
| Method      | `getOrDefault(int i, int j, int defaultValue)`         | `int`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。 |
| Method      | `sumAll()`                                             | `long`                    | 全要素の和を返す。                                                           |
| Method      | `sum(int i, int j)`                                    | `long`                    | 閉矩形[0, i] x [0, j]の和を返す。                                            |
| Method      | `sum(int i1, int j1, int i2, int j2)`                  | `long`                    | 閉矩形[i1, i2] x [j1, j2]の和を返す。                                        |
| Method      | `iterator()`                                           | `PrimitiveIterator.OfInt` | 元の値を行優先順に走査する。                                                 |
| Method      | `spliterator()`                                        | `Spliterator.OfInt`       | iterator と同じ順の ORDERED・SIZED・SUBSIZED を持つ。                        |
| Method      | `toString()`                                           | `String`                  | 各行を半角スペース区切り、行間を改行した文字列として返す。                   |

### `LongPrefixSum2D`

| 種別        | シグネチャ                                                    | 戻り値                     | 説明                                                                         |
|-------------|---------------------------------------------------------------|----------------------------|------------------------------------------------------------------------------|
| Constructor | `LongPrefixSum2D(int h, int w, IntBinaryToLongFunction init)` | —                          | 各座標に init(i, j) を行優先順で適用して構築する。                           |
| Constructor | `LongPrefixSum2D(long[][] a)`                                 | —                          | 指定された配列から構築する。                                                 |
| Method      | `size()`                                                      | `int`                      | 全要素数を返す。                                                             |
| Method      | `inRange(int i, int j)`                                       | `boolean`                  | 0 <= i < h かつ 0 <= j < w かを返す。                                        |
| Method      | `get(int i, int j)`                                           | `long`                     | 指定位置の元の値を返す。                                                     |
| Method      | `getOrDefault(int i, int j, long defaultValue)`               | `long`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。 |
| Method      | `sumAll()`                                                    | `long`                     | 全要素の和を返す。                                                           |
| Method      | `sum(int i, int j)`                                           | `long`                     | 閉矩形[0, i] x [0, j]の和を返す。                                            |
| Method      | `sum(int i1, int j1, int i2, int j2)`                         | `long`                     | 閉矩形[i1, i2] x [j1, j2]の和を返す。                                        |
| Method      | `iterator()`                                                  | `PrimitiveIterator.OfLong` | 元の値を行優先順に走査する。                                                 |
| Method      | `spliterator()`                                               | `Spliterator.OfLong`       | iterator と同じ順の ORDERED・SIZED・SUBSIZED を持つ。                        |
| Method      | `toString()`                                                  | `String`                   | 各行を半角スペース区切り、行間を改行した文字列として返す。                   |

### `IntPrefixSum3D`

| 種別        | シグネチャ                                                     | 戻り値                    | 説明                                                                         |
|-------------|----------------------------------------------------------------|---------------------------|------------------------------------------------------------------------------|
| Constructor | `IntPrefixSum3D(int d, int h, int w, IntTernaryOperator init)` | —                         | 各座標に init(i, j, k) を (i, j, k) の辞書順で1回ずつ適用して構築する。      |
| Constructor | `IntPrefixSum3D(int[][][] a)`                                  | —                         | 指定された配列から構築する。                                                 |
| Method      | `size()`                                                       | `int`                     | 全要素数を返す。                                                             |
| Method      | `inRange(int i, int j, int k)`                                 | `boolean`                 | 0 <= i < d かつ 0 <= j < h かつ 0 <= k < w かを返す。                        |
| Method      | `get(int i, int j, int k)`                                     | `int`                     | 指定位置の元の値を返す。                                                     |
| Method      | `getOrDefault(int i, int j, int k, int defaultValue)`          | `int`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。 |
| Method      | `sumAll()`                                                     | `long`                    | 全要素の和を返す。                                                           |
| Method      | `sum(int i, int j, int k)`                                     | `long`                    | 閉直方体[0, i] x [0, j] x [0, k]の和を返す。                                 |
| Method      | `sum(int i1, int j1, int k1, int i2, int j2, int k2)`          | `long`                    | 閉直方体[i1, i2] x [j1, j2] x [k1, k2]の和を返す。                           |
| Method      | `iterator()`                                                   | `PrimitiveIterator.OfInt` | 元の値を i, j, k の辞書順に走査する。                                        |
| Method      | `spliterator()`                                                | `Spliterator.OfInt`       | iterator と同じ順の ORDERED・SIZED・SUBSIZED を持つ。                        |
| Method      | `toString()`                                                   | `String`                  | 最終軸の要素を半角スペースで区切り、各行を改行した文字列として返す。         |

### `LongPrefixSum3D`

| 種別        | シグネチャ                                                            | 戻り値                     | 説明                                                                         |
|-------------|-----------------------------------------------------------------------|----------------------------|------------------------------------------------------------------------------|
| Constructor | `LongPrefixSum3D(int d, int h, int w, IntTernaryToLongFunction init)` | —                          | 各座標に init(i, j, k) を (i, j, k) の辞書順で1回ずつ適用して構築する。      |
| Constructor | `LongPrefixSum3D(long[][][] a)`                                       | —                          | 指定された配列から構築する。                                                 |
| Method      | `size()`                                                              | `int`                      | 全要素数を返す。                                                             |
| Method      | `inRange(int i, int j, int k)`                                        | `boolean`                  | 0 <= i < d かつ 0 <= j < h かつ 0 <= k < w かを返す。                        |
| Method      | `get(int i, int j, int k)`                                            | `long`                     | 指定位置の元の値を返す。                                                     |
| Method      | `getOrDefault(int i, int j, int k, long defaultValue)`                | `long`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。 |
| Method      | `sumAll()`                                                            | `long`                     | 全要素の和を返す。                                                           |
| Method      | `sum(int i, int j, int k)`                                            | `long`                     | 閉直方体[0, i] x [0, j] x [0, k]の和を返す。                                 |
| Method      | `sum(int i1, int j1, int k1, int i2, int j2, int k2)`                 | `long`                     | 閉直方体[i1, i2] x [j1, j2] x [k1, k2]の和を返す。                           |
| Method      | `iterator()`                                                          | `PrimitiveIterator.OfLong` | 元の値を i, j, k の辞書順に走査する。                                        |
| Method      | `spliterator()`                                                       | `Spliterator.OfLong`       | iterator と同じ順の ORDERED・SIZED・SUBSIZED を持つ。                        |
| Method      | `toString()`                                                          | `String`                   | 最終軸の要素を半角スペースで区切り、各行を改行した文字列として返す。         |

### `IntPrefixModSum`

| 種別        | シグネチャ                                               | 戻り値                    | 説明                                                                               |
|-------------|----------------------------------------------------------|---------------------------|------------------------------------------------------------------------------------|
| Constructor | `IntPrefixModSum(int n, int mod, IntUnaryOperator init)` | —                         | init を添字の昇順に1回ずつ呼び出して構築する。                                     |
| Constructor | `IntPrefixModSum(int[] a, int mod)`                      | —                         | 指定配列の論理順で構築する。入力は変更しない。                                     |
| Constructor | `IntPrefixModSum(IntArray a, int mod)`                   | —                         | 指定配列の論理順で構築する。入力は変更しない。                                     |
| Method      | `size()`                                                 | `int`                     | 要素数を返す。                                                                     |
| Method      | `get(int i)`                                             | `int`                     | 指定した有効な論理添字の値を返す。                                                 |
| Method      | `sumAll()`                                               | `int`                     | 全要素の和を法modで返す。                                                          |
| Method      | `sum(int i)`                                             | `int`                     | 閉区間 [0, i] の和を法modで返す。                                                  |
| Method      | `sum(int i, int j)`                                      | `int`                     | 閉区間 [i, j] の和を法modで返す。                                                  |
| Method      | `iterator()`                                             | `PrimitiveIterator.OfInt` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。 |
| Method      | `toString()`                                             | `String`                  | 元の要素を半角スペースで区切った文字列として返す。                                 |

### `LongPrefixModSum`

| 種別        | シグネチャ                                                  | 戻り値                     | 説明                                                                               |
|-------------|-------------------------------------------------------------|----------------------------|------------------------------------------------------------------------------------|
| Constructor | `LongPrefixModSum(int n, long mod, IntToLongFunction init)` | —                          | init を添字の昇順に1回ずつ呼び出して構築する。                                     |
| Constructor | `LongPrefixModSum(long[] a, long mod)`                      | —                          | 指定配列の論理順で構築する。入力は変更しない。                                     |
| Constructor | `LongPrefixModSum(LongArray a, long mod)`                   | —                          | 指定配列の論理順で構築する。入力は変更しない。                                     |
| Method      | `size()`                                                    | `int`                      | 要素数を返す。                                                                     |
| Method      | `get(int i)`                                                | `long`                     | 指定した有効な論理添字の値を返す。                                                 |
| Method      | `sumAll()`                                                  | `long`                     | 全要素の和を法modで返す。                                                          |
| Method      | `sum(int i)`                                                | `long`                     | 閉区間 [0, i] の和を法modで返す。                                                  |
| Method      | `sum(int i, int j)`                                         | `long`                     | 閉区間 [i, j] の和を法modで返す。                                                  |
| Method      | `iterator()`                                                | `PrimitiveIterator.OfLong` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。 |
| Method      | `toString()`                                                | `String`                   | 元の要素を半角スペースで区切った文字列として返す。                                 |

### `IntPrefixModSum2D`

| 種別        | シグネチャ                                                         | 戻り値                    | 説明                                                                         |
|-------------|--------------------------------------------------------------------|---------------------------|------------------------------------------------------------------------------|
| Constructor | `IntPrefixModSum2D(int h, int w, int mod, IntBinaryOperator init)` | —                         | 各座標に init(i, j) を行優先順で適用して構築する。                           |
| Constructor | `IntPrefixModSum2D(int[][] a, int mod)`                            | —                         | 指定された配列から構築する。                                                 |
| Method      | `size()`                                                           | `int`                     | 全要素数を返す。                                                             |
| Method      | `inRange(int i, int j)`                                            | `boolean`                 | 0 <= i < h かつ 0 <= j < w かを返す。                                        |
| Method      | `get(int i, int j)`                                                | `int`                     | 指定位置の元の値を返す。                                                     |
| Method      | `getOrDefault(int i, int j, int defaultValue)`                     | `int`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。 |
| Method      | `sumAll()`                                                         | `int`                     | 全要素の和を法modで返す。                                                    |
| Method      | `sum(int i, int j)`                                                | `int`                     | 閉矩形[0, i] x [0, j]の和を法modで返す。                                     |
| Method      | `sum(int i1, int j1, int i2, int j2)`                              | `int`                     | 閉矩形[i1, i2] x [j1, j2]の和を法modで返す。                                 |
| Method      | `iterator()`                                                       | `PrimitiveIterator.OfInt` | 元の値を行優先順に走査する。                                                 |
| Method      | `spliterator()`                                                    | `Spliterator.OfInt`       | iterator と同じ順の ORDERED・SIZED・SUBSIZED を持つ。                        |
| Method      | `toString()`                                                       | `String`                  | 各行を半角スペース区切り、行間を改行した文字列として返す。                   |

### `LongPrefixModSum2D`

| 種別        | シグネチャ                                                                 | 戻り値                     | 説明                                                                         |
|-------------|----------------------------------------------------------------------------|----------------------------|------------------------------------------------------------------------------|
| Constructor | `LongPrefixModSum2D(int h, int w, long mod, IntBinaryToLongFunction init)` | —                          | 各座標に init(i, j) を行優先順で適用して構築する。                           |
| Constructor | `LongPrefixModSum2D(long[][] a, long mod)`                                 | —                          | 指定された配列から構築する。                                                 |
| Method      | `size()`                                                                   | `int`                      | 全要素数を返す。                                                             |
| Method      | `inRange(int i, int j)`                                                    | `boolean`                  | 0 <= i < h かつ 0 <= j < w かを返す。                                        |
| Method      | `get(int i, int j)`                                                        | `long`                     | 指定位置の元の値を返す。                                                     |
| Method      | `getOrDefault(int i, int j, long defaultValue)`                            | `long`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。 |
| Method      | `sumAll()`                                                                 | `long`                     | 全要素の和を法modで返す。                                                    |
| Method      | `sum(int i, int j)`                                                        | `long`                     | 閉矩形[0, i] x [0, j]の和を法modで返す。                                     |
| Method      | `sum(int i1, int j1, int i2, int j2)`                                      | `long`                     | 閉矩形[i1, i2] x [j1, j2]の和を法modで返す。                                 |
| Method      | `iterator()`                                                               | `PrimitiveIterator.OfLong` | 元の値を行優先順に走査する。                                                 |
| Method      | `spliterator()`                                                            | `Spliterator.OfLong`       | iterator と同じ順の ORDERED・SIZED・SUBSIZED を持つ。                        |
| Method      | `toString()`                                                               | `String`                   | 各行を半角スペース区切り、行間を改行した文字列として返す。                   |

### `IntPrefixModSum3D`

| 種別        | シグネチャ                                                                 | 戻り値                    | 説明                                                                         |
|-------------|----------------------------------------------------------------------------|---------------------------|------------------------------------------------------------------------------|
| Constructor | `IntPrefixModSum3D(int d, int h, int w, int mod, IntTernaryOperator init)` | —                         | 各座標に init(i, j, k) を (i, j, k) の辞書順で1回ずつ適用して構築する。      |
| Constructor | `IntPrefixModSum3D(int[][][] a, int mod)`                                  | —                         | 指定された配列から構築する。                                                 |
| Method      | `size()`                                                                   | `int`                     | 全要素数を返す。                                                             |
| Method      | `inRange(int i, int j, int k)`                                             | `boolean`                 | 0 <= i < d かつ 0 <= j < h かつ 0 <= k < w かを返す。                        |
| Method      | `get(int i, int j, int k)`                                                 | `int`                     | 指定位置の元の値を返す。                                                     |
| Method      | `getOrDefault(int i, int j, int k, int defaultValue)`                      | `int`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。 |
| Method      | `sumAll()`                                                                 | `int`                     | 全要素の和を法modで返す。                                                    |
| Method      | `sum(int i, int j, int k)`                                                 | `int`                     | 閉直方体[0, i] x [0, j] x [0, k]の和を法modで返す。                          |
| Method      | `sum(int i1, int j1, int k1, int i2, int j2, int k2)`                      | `int`                     | 閉直方体[i1, i2] x [j1, j2] x [k1, k2]の和を法modで返す。                    |
| Method      | `iterator()`                                                               | `PrimitiveIterator.OfInt` | 元の値を i, j, k の辞書順に走査する。                                        |
| Method      | `spliterator()`                                                            | `Spliterator.OfInt`       | iterator と同じ順の ORDERED・SIZED・SUBSIZED を持つ。                        |
| Method      | `toString()`                                                               | `String`                  | 最終軸の要素を半角スペースで区切り、各行を改行した文字列として返す。         |

### `LongPrefixModSum3D`

| 種別        | シグネチャ                                                                         | 戻り値                     | 説明                                                                         |
|-------------|------------------------------------------------------------------------------------|----------------------------|------------------------------------------------------------------------------|
| Constructor | `LongPrefixModSum3D(int d, int h, int w, long mod, IntTernaryToLongFunction init)` | —                          | 各座標に init(i, j, k) を (i, j, k) の辞書順で1回ずつ適用して構築する。      |
| Constructor | `LongPrefixModSum3D(long[][][] a, long mod)`                                       | —                          | 指定された配列から構築する。                                                 |
| Method      | `size()`                                                                           | `int`                      | 全要素数を返す。                                                             |
| Method      | `inRange(int i, int j, int k)`                                                     | `boolean`                  | 0 <= i < d かつ 0 <= j < h かつ 0 <= k < w かを返す。                        |
| Method      | `get(int i, int j, int k)`                                                         | `long`                     | 指定位置の元の値を返す。                                                     |
| Method      | `getOrDefault(int i, int j, int k, long defaultValue)`                             | `long`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。 |
| Method      | `sumAll()`                                                                         | `long`                     | 全要素の和を法modで返す。                                                    |
| Method      | `sum(int i, int j, int k)`                                                         | `long`                     | 閉直方体[0, i] x [0, j] x [0, k]の和を法modで返す。                          |
| Method      | `sum(int i1, int j1, int k1, int i2, int j2, int k2)`                              | `long`                     | 閉直方体[i1, i2] x [j1, j2] x [k1, k2]の和を法modで返す。                    |
| Method      | `iterator()`                                                                       | `PrimitiveIterator.OfLong` | 元の値を i, j, k の辞書順に走査する。                                        |
| Method      | `spliterator()`                                                                    | `Spliterator.OfLong`       | iterator と同じ順の ORDERED・SIZED・SUBSIZED を持つ。                        |
| Method      | `toString()`                                                                       | `String`                   | 最終軸の要素を半角スペースで区切り、各行を改行した文字列として返す。         |

1D版は `public final int size`、2D版は `public final int h, w, size`、3D版は `public final int d, h, w, size` を公開する。MOD版は型に応じた `public final int mod` / `public final long mod` も公開する。

1D版は `getLast()` と型に応じた `getLastOrDefault(int defaultValue)` / `getLastOrDefault(long defaultValue)` を継承し、末尾の元の値を返す。空の場合の動作と、検索・配列化・Stream を含む継承 API は [ArrayGuide](./ArrayGuide.md) を参照する。

2D・3D版は型に応じた `IntCollection` / `LongCollection` を実装する。`isEmpty`、`contains`、`toArray`、`toList`、プリミティブ Stream などの継承 API は [ArrayGuide](./ArrayGuide.md#継承元のコレクション-api) を参照する。配列化は全次元とも一次元配列を返す。

## 利用例

準備中

## 制約・注意事項

- 1D版は getOrDefault(int i, int defaultValue) / getOrDefault(int i, long defaultValue) を継承する。2D・3D版は座標ごとに範囲を判定する。範囲外では指定値をそのまま返し、MOD版でも指定値の正規化は行わない。
- 1D版は空配列にも対応する。2D・3D版は各次元の長さが1以上で、配列が矩形または直方体である問題制約を前提とする。
- 1Dは `0 <= i <= j < size`、2D・3Dも各軸で同様の有効な閉区間を指定する。`get` と引数付きの `sum` は、明示的な検査と配列アクセスの範囲検査により、範囲外または区間の逆転で `IndexOutOfBoundsException` を投げる。
- 空でない半開区間 `[l, r)` は閉区間 `[l, r - 1]` へ変換して使用する。空区間は受け付けない。
- 通常版は全次元で、累積値と構築・問い合わせの途中の加減算が long の範囲に収まる問題制約で使用する。
- MOD版は `mod > 0`、かつ全ての入力値が `0 <= value < mod` を満たす問題制約で使用する。
- MOD版の全ての `sum` は結果を `[0, mod)` に正規化する。この規則は `getOrDefault` / `getLastOrDefault` が返す指定値には適用しない。
- 1Dの `IntPrefixModSum` は構築・問い合わせの途中の加減算が int の範囲に収まること。その他のMOD版は途中の加減算が long の範囲に収まること。
- 全次元の iterator は終端後の取得で `NoSuchElementException` を投げる。
- Long版の2D初期化関数は `IntBinaryToLongFunction` を使い、行・列を `int` で受け取って値を `long` で返す。
- 構築後の更新には対応しない。

## 性能特性

`n` は全要素数、`d`, `h`, `w` は各次元の長さとする。関数の評価時間は別途加算する。

| 操作                                                                                             | 時間計算量 | 補足                                                                                 |
|--------------------------------------------------------------------------------------------------|------------|--------------------------------------------------------------------------------------|
| 構築                                                                                             | `O(n)`     | 全次元で値を配置した後に `build` で累積する                                          |
| `get`, `getOrDefault`, `sum`, `size`, 2D・3D版の `inRange`, 1D版の `getLast`, `getLastOrDefault` | `O(1)`     | `get` は1要素分の差分・包含排除を直接計算する。MOD版は結果を `[0, mod)` に正規化する |
| `toArray`, `toList`, `toString`                                                                  | `O(n)`     | —                                                                                    |
| iterator の生成・1要素取得                                                                       | `O(1)`     | 全要素の走査は `O(n)`                                                                |

- **空間計算量**: 1Dは `O(n)`、2Dは `O(hw)`、3Dは `O(dhw)`。多次元版も累積値を一次元配列へ圧縮して保持する

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                         |
|--------------------|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-15 | 1Dの `IntPrefixSum` / `LongPrefixSum` を追加                                                                                                                                 |
| **バージョン 2.0** | 2026-07-15 | 初期化関数コンストラクタとsupplierによる生成を追加                                                                                                                           |
| **バージョン 3.0** | 2026-07-27 | 先頭ゼロ付きの内部表現と公開 `length`、1DのMOD版、2D・3D版、次元に応じた `toString()` を追加                                                                                 |
| **バージョン 3.1** | 2026-10-23 | 1D版の iterator に終端例外を追加し、空配列の文字列化に対応。メソッドと内部補助処理の定義順を統一                                                                             |
| **バージョン 4.0** | 2026-10-23 | 2D・3D版に getOrDefault を追加。1D版は共通インターフェースから継承。単純なループの書式を統一                                                                                 |
| **バージョン 4.1** | 2026-10-24 | 全12クラスの get と引数付き sum に添字・閉区間の範囲検査を追加                                                                                                               |
| **バージョン 5.0** | 2026-10-24 | 1D版で配列インターフェースの getLast / getLastOrDefault を継承                                                                                                               |
| **バージョン 5.1** | 2026-10-25 | 3D版の区間和で局所添字名を i / j / k に統一                                                                                                                                  |
| **バージョン 6.0** | 2026-10-25 | IntPrefixSum の累積値と sum の戻り値を long 化。2D・3D版に配列コンストラクタと型別コレクション実装を追加。2D版の inRange と公開 size を統一                                  |
| **バージョン 6.1** | 2026-10-26 | Long版2Dの初期化関数を IntBinaryToLongFunction に統一。通常版・MOD版の2D・3D get を直接計算へ変更し、座標の重複検査を除去                                                    |
| **バージョン 6.2** | 2026-10-27 | getと1D区間和で配列アクセスと重複する範囲検査を削除。座標変換で必要な内側の軸と区間順序の検査は維持                                                                          |
| **バージョン 6.3** | 2026-10-27 | 全12クラス: 構築処理を build へ集約。IntPrefixSum2D / LongPrefixSum2D: toString に空判定を追加                                                                               |
| **バージョン 7.0** | 2026-10-27 | IntPrefixSum3D / LongPrefixSum3D / IntPrefixModSum3D / LongPrefixModSum3D: 初期化関数を座標を受け取る IntTernaryOperator / IntTernaryToLongFunction へ変更し、inRange を追加 |
| **バージョン 8.0** | 2026-10-29 | 全12クラス: 全要素の和を返す `sum()` を fenwick と同じ `sumAll()` へ改名                                                                                                     |
