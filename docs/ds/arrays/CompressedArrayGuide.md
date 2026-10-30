# CompressedArray / CompressedArray2D 利用ガイド

> `IntCompressedArray` / `LongCompressedArray` とその2D版は、元配列の全要素をソート順の順位へ変換して保持する座標圧縮クラスである。

[← README: Primitive Array Utilities](./README.md) · ソース: [IntCompressedArray.java](../../../src/lib/ds/arrays/IntCompressedArray.java) · [IntCompressedArray2D.java](../../../src/lib/ds/arrays/IntCompressedArray2D.java) · [LongCompressedArray.java](../../../src/lib/ds/arrays/LongCompressedArray.java) · [LongCompressedArray2D.java](../../../src/lib/ds/arrays/LongCompressedArray2D.java) · [RankType.java](../../../src/lib/ds/arrays/RankType.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

dense ranking に加え、competition ranking と modified competition ranking に対応する。

## 特徴

- 元配列順の圧縮結果を `int[]` で保持
- 2D版も内部では一次元配列へ圧縮し、全要素を共通の順位空間へ変換
- 0-based / 1-based の順位を選択可能
- 3種類の同順位処理を `RankType` で選択可能
- 値から順位、順位から値への $\mathcal{O}(\log n)$ 変換
- 元配列の復元、出現判定、出現回数の取得に対応
- 圧縮結果・ソート済み値・順位配列をコピーとして取得可能
- 同型のコピーコンストラクタは再圧縮せず、元の値と順位の対応を保持
- `LongCompressedArray` も圧縮後の順位は `int` のため `IntArray` を実装

### 型別クラス

| クラス                                                                           | 対象型 | 選ぶ目安                                        |
|----------------------------------------------------------------------------------|--------|-------------------------------------------------|
| [`IntCompressedArray`](../../../src/lib/ds/arrays/IntCompressedArray.java)       | `int`  | int配列を座標圧縮する場合                       |
| [`LongCompressedArray`](../../../src/lib/ds/arrays/LongCompressedArray.java)     | `long` | long配列を座標圧縮する場合。順位は int で返す   |
| [`IntCompressedArray2D`](../../../src/lib/ds/arrays/IntCompressedArray2D.java)   | `int`  | int値の2次元配列を共通の順位空間へ圧縮する場合  |
| [`LongCompressedArray2D`](../../../src/lib/ds/arrays/LongCompressedArray2D.java) | `long` | long値の2次元配列を共通の順位空間へ圧縮する場合 |
| [`RankType`](../../../src/lib/ds/arrays/RankType.java)                           | —      | 同順位の割り当て方式の指定                      |

## 依存関係

- `java.util.Objects`
- `java.util.Arrays`
- `java.util.PrimitiveIterator`
- `java.util.Spliterator`
- [`lib.ds.IntCollection`](../../../src/lib/ds/IntCollection.java)
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)
- [`lib.ds.arrays.RankType`](../../../src/lib/ds/arrays/RankType.java)
- [`lib.search.ArrayBinarySearch`](../../../src/lib/search/ArrayBinarySearch.java)

## API

### `IntCompressedArray`

| 種別        | シグネチャ                                                                 | 戻り値                    | 説明                                                                                        |
|-------------|----------------------------------------------------------------------------|---------------------------|---------------------------------------------------------------------------------------------|
| Constructor | `IntCompressedArray(int[] a)`                                              | —                         | 指定配列をコピーして座標圧縮する。順位方式は DENSE、順位は0始まりとする。                   |
| Constructor | `IntCompressedArray(int[] a, RankType rankType)`                           | —                         | 指定配列をコピーして座標圧縮する。指定した順位方式で0始まりとする。                         |
| Constructor | `IntCompressedArray(int[] a, RankType rankType, boolean oneBased)`         | —                         | 指定配列をコピーして座標圧縮する。指定した順位方式と開始位置を使う。                        |
| Constructor | `IntCompressedArray(IntCollection a)`                                      | —                         | コレクションの iterator 順にコピーして座標圧縮する。順位方式は DENSE、順位は0始まりとする。 |
| Constructor | `IntCompressedArray(IntCollection a, RankType rankType)`                   | —                         | コレクションの iterator 順にコピーして座標圧縮する。指定した順位方式で0始まりとする。       |
| Constructor | `IntCompressedArray(IntCollection a, RankType rankType, boolean oneBased)` | —                         | コレクションの iterator 順にコピーして座標圧縮する。指定した順位方式と開始位置を使う。      |
| Constructor | `IntCompressedArray(IntCompressedArray other)`                             | —                         | 元の値・順位・配列順を保持する独立したコピーを構築する。                                    |
| Method      | `size()`                                                                   | `int`                     | 要素数を返す。                                                                              |
| Method      | `get(int i)`                                                               | `int`                     | 指定した有効な論理添字の圧縮後の順位を返す。                                                |
| Method      | `rankOfValue(int v)`                                                       | `int`                     | 入力に存在する値 v の順位を返す。                                                           |
| Method      | `valueOfRank(int rank)`                                                    | `int`                     | 実在する順位 rank に対応する元の値を返す。                                                  |
| Method      | `containsValue(int v)`                                                     | `boolean`                 | 圧縮前の値 v が入力に存在するかを返す。                                                     |
| Method      | `count(int v)`                                                             | `int`                     | 圧縮前の値 v の出現回数を返す。                                                             |
| Method      | `restore()`                                                                | `int[]`                   | 元の値と配列順を復元した独立した配列を返す。                                                |
| Method      | `toArray()`                                                                | `int[]`                   | 圧縮後の順位を元配列順でコピーして返す。                                                    |
| Method      | `sorted()`                                                                 | `int[]`                   | 重複を含むソート済み値のコピーを返す。                                                      |
| Method      | `ranks()`                                                                  | `int[]`                   | ソート済み値に対応する順位配列のコピーを返す。                                              |
| Method      | `iterator()`                                                               | `PrimitiveIterator.OfInt` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。          |
| Method      | `spliterator()`                                                            | `Spliterator.OfInt`       | 元配列順の ORDERED・SIZED・SUBSIZED・IMMUTABLE の spliterator を返す。                      |
| Method      | `toString()`                                                               | `String`                  | 論理順の要素を半角スペースで区切った文字列として返す。                                      |

継承するメソッド：

- [`IntArray`](./ArrayGuide.md#intarray)：`getFirst`、`getFirstOrDefault`、`getLast`、`getLastOrDefault`、`getOrDefault`、`indexOf`、`lastIndexOf`
- [`IntCollection`](./ArrayGuide.md#intcollection)：`contains`、`forEachInt`、`intStream`、`isEmpty`、`toList`
- `Iterable`：`forEach`

### `LongCompressedArray`

| 種別        | シグネチャ                                                                   | 戻り値                    | 説明                                                                                        |
|-------------|------------------------------------------------------------------------------|---------------------------|---------------------------------------------------------------------------------------------|
| Constructor | `LongCompressedArray(long[] a)`                                              | —                         | 指定配列をコピーして座標圧縮する。順位方式は DENSE、順位は0始まりとする。                   |
| Constructor | `LongCompressedArray(long[] a, RankType rankType)`                           | —                         | 指定配列をコピーして座標圧縮する。指定した順位方式で0始まりとする。                         |
| Constructor | `LongCompressedArray(long[] a, RankType rankType, boolean oneBased)`         | —                         | 指定配列をコピーして座標圧縮する。指定した順位方式と開始位置を使う。                        |
| Constructor | `LongCompressedArray(LongCollection a)`                                      | —                         | コレクションの iterator 順にコピーして座標圧縮する。順位方式は DENSE、順位は0始まりとする。 |
| Constructor | `LongCompressedArray(LongCollection a, RankType rankType)`                   | —                         | コレクションの iterator 順にコピーして座標圧縮する。指定した順位方式で0始まりとする。       |
| Constructor | `LongCompressedArray(LongCollection a, RankType rankType, boolean oneBased)` | —                         | コレクションの iterator 順にコピーして座標圧縮する。指定した順位方式と開始位置を使う。      |
| Constructor | `LongCompressedArray(LongCompressedArray other)`                             | —                         | 元の値・順位・配列順を保持する独立したコピーを構築する。                                    |
| Method      | `size()`                                                                     | `int`                     | 要素数を返す。                                                                              |
| Method      | `get(int i)`                                                                 | `int`                     | 指定した有効な論理添字の圧縮後の順位を返す。                                                |
| Method      | `rankOfValue(long v)`                                                        | `int`                     | 入力に存在する値 v の順位を返す。                                                           |
| Method      | `valueOfRank(int rank)`                                                      | `long`                    | 実在する順位 rank に対応する元の値を返す。                                                  |
| Method      | `containsValue(long v)`                                                      | `boolean`                 | 圧縮前の値 v が入力に存在するかを返す。                                                     |
| Method      | `count(long v)`                                                              | `int`                     | 圧縮前の値 v の出現回数を返す。                                                             |
| Method      | `restore()`                                                                  | `long[]`                  | 元の値と配列順を復元した独立した配列を返す。                                                |
| Method      | `toArray()`                                                                  | `int[]`                   | 圧縮後の順位を元配列順でコピーして返す。                                                    |
| Method      | `sorted()`                                                                   | `long[]`                  | 重複を含むソート済み値のコピーを返す。                                                      |
| Method      | `ranks()`                                                                    | `int[]`                   | ソート済み値に対応する順位配列のコピーを返す。                                              |
| Method      | `iterator()`                                                                 | `PrimitiveIterator.OfInt` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。          |
| Method      | `spliterator()`                                                              | `Spliterator.OfInt`       | 元配列順の ORDERED・SIZED・SUBSIZED・IMMUTABLE の spliterator を返す。                      |
| Method      | `toString()`                                                                 | `String`                  | 論理順の要素を半角スペースで区切った文字列として返す。                                      |

継承するメソッド：

- [`IntArray`](./ArrayGuide.md#intarray)：`getFirst`、`getFirstOrDefault`、`getLast`、`getLastOrDefault`、`getOrDefault`、`indexOf`、`lastIndexOf`
- [`IntCollection`](./ArrayGuide.md#intcollection)：`contains`、`forEachInt`、`intStream`、`isEmpty`、`toList`
- `Iterable`：`forEach`

### `IntCompressedArray2D`

| 種別        | シグネチャ                                                             | 戻り値                    | 説明                                                                               |
|-------------|------------------------------------------------------------------------|---------------------------|------------------------------------------------------------------------------------|
| Constructor | `IntCompressedArray2D(int[][] a)`                                      | —                         | 指定配列をコピーして座標圧縮する。順位方式は DENSE、順位は0始まりとする。          |
| Constructor | `IntCompressedArray2D(int[][] a, RankType rankType)`                   | —                         | 指定配列をコピーして座標圧縮する。指定した順位方式で0始まりとする。                |
| Constructor | `IntCompressedArray2D(int[][] a, RankType rankType, boolean oneBased)` | —                         | 指定配列をコピーして座標圧縮する。指定した順位方式と開始位置を使う。               |
| Constructor | `IntCompressedArray2D(IntCompressedArray2D other)`                     | —                         | 元の値・順位・配列順・寸法を保持する独立したコピーを構築する。                     |
| Method      | `size()`                                                               | `int`                     | 要素数を返す。                                                                     |
| Method      | `inRange(int i, int j)`                                                | `boolean`                 | 0 <= i < h かつ 0 <= j < w かを返す。                                              |
| Method      | `get(int i, int j)`                                                    | `int`                     | 指定した有効な論理添字の圧縮後の順位を返す。                                       |
| Method      | `getOrDefault(int i, int j, int defaultValue)`                         | `int`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。       |
| Method      | `rankOfValue(int v)`                                                   | `int`                     | 入力に存在する値 v の順位を返す。                                                  |
| Method      | `valueOfRank(int rank)`                                                | `int`                     | 実在する順位 rank に対応する元の値を返す。                                         |
| Method      | `containsValue(int v)`                                                 | `boolean`                 | 圧縮前の値 v が入力に存在するかを返す。                                            |
| Method      | `count(int v)`                                                         | `int`                     | 圧縮前の値 v の出現回数を返す。                                                    |
| Method      | `restore()`                                                            | `int[][]`                 | 元の値と配列順を復元した独立した配列を返す。                                       |
| Method      | `toArray2D()`                                                          | `int[][]`                 | 圧縮後の順位を元の2次元形状でコピーして返す。                                      |
| Method      | `sorted()`                                                             | `int[]`                   | 重複を含むソート済み値のコピーを返す。                                             |
| Method      | `ranks()`                                                              | `int[]`                   | ソート済み値に対応する順位配列のコピーを返す。                                     |
| Method      | `iterator()`                                                           | `PrimitiveIterator.OfInt` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。 |
| Method      | `spliterator()`                                                        | `Spliterator.OfInt`       | 行優先順の ORDERED・SIZED・SUBSIZED・IMMUTABLE の spliterator を返す。             |
| Method      | `toString()`                                                           | `String`                  | 各行を半角スペース区切り、行間を改行した文字列として返す。                         |

継承するメソッド：

- [`IntCollection`](./ArrayGuide.md#intcollection)：`contains`、`forEachInt`、`intStream`、`isEmpty`、`toArray`、`toList`
- `Iterable`：`forEach`

### `LongCompressedArray2D`

| 種別        | シグネチャ                                                               | 戻り値                    | 説明                                                                               |
|-------------|--------------------------------------------------------------------------|---------------------------|------------------------------------------------------------------------------------|
| Constructor | `LongCompressedArray2D(long[][] a)`                                      | —                         | 指定配列をコピーして座標圧縮する。順位方式は DENSE、順位は0始まりとする。          |
| Constructor | `LongCompressedArray2D(long[][] a, RankType rankType)`                   | —                         | 指定配列をコピーして座標圧縮する。指定した順位方式で0始まりとする。                |
| Constructor | `LongCompressedArray2D(long[][] a, RankType rankType, boolean oneBased)` | —                         | 指定配列をコピーして座標圧縮する。指定した順位方式と開始位置を使う。               |
| Constructor | `LongCompressedArray2D(LongCompressedArray2D other)`                     | —                         | 元の値・順位・配列順・寸法を保持する独立したコピーを構築する。                     |
| Method      | `size()`                                                                 | `int`                     | 要素数を返す。                                                                     |
| Method      | `inRange(int i, int j)`                                                  | `boolean`                 | 0 <= i < h かつ 0 <= j < w かを返す。                                              |
| Method      | `get(int i, int j)`                                                      | `int`                     | 指定した有効な論理添字の圧縮後の順位を返す。                                       |
| Method      | `getOrDefault(int i, int j, int defaultValue)`                           | `int`                     | 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。       |
| Method      | `rankOfValue(long v)`                                                    | `int`                     | 入力に存在する値 v の順位を返す。                                                  |
| Method      | `valueOfRank(int rank)`                                                  | `long`                    | 実在する順位 rank に対応する元の値を返す。                                         |
| Method      | `containsValue(long v)`                                                  | `boolean`                 | 圧縮前の値 v が入力に存在するかを返す。                                            |
| Method      | `count(long v)`                                                          | `int`                     | 圧縮前の値 v の出現回数を返す。                                                    |
| Method      | `restore()`                                                              | `long[][]`                | 元の値と配列順を復元した独立した配列を返す。                                       |
| Method      | `toArray2D()`                                                            | `int[][]`                 | 圧縮後の順位を元の2次元形状でコピーして返す。                                      |
| Method      | `sorted()`                                                               | `long[]`                  | 重複を含むソート済み値のコピーを返す。                                             |
| Method      | `ranks()`                                                                | `int[]`                   | ソート済み値に対応する順位配列のコピーを返す。                                     |
| Method      | `iterator()`                                                             | `PrimitiveIterator.OfInt` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。 |
| Method      | `spliterator()`                                                          | `Spliterator.OfInt`       | 行優先順の ORDERED・SIZED・SUBSIZED・IMMUTABLE の spliterator を返す。             |
| Method      | `toString()`                                                             | `String`                  | 各行を半角スペース区切り、行間を改行した文字列として返す。                         |

継承するメソッド：

- [`IntCollection`](./ArrayGuide.md#intcollection)：`contains`、`forEachInt`、`intStream`、`isEmpty`、`toArray`、`toList`
- `Iterable`：`forEach`

### `RankType`

ソート済み値 `[10, 20, 20, 30]` に対する順位は次の通りである。

| RankType               | 0-based        | 1-based        | 説明                                 |
|------------------------|----------------|----------------|--------------------------------------|
| `DENSE`                | `[0, 1, 1, 2]` | `[1, 2, 2, 3]` | 異なる値ごとに連番                   |
| `COMPETITION`          | `[0, 1, 1, 3]` | `[1, 2, 2, 4]` | 同順位の個数だけ次順位を飛ばす       |
| `MODIFIED_COMPETITION` | `[0, 2, 2, 3]` | `[1, 3, 3, 4]` | 同順位グループの末尾位置を順位にする |

継承するメソッド：



1D版は `public final int size, uniqueSize`、2D版は `public final int h, w, size, uniqueSize` を公開する。

`contains(int v)` は圧縮後の順位、`containsValue` は圧縮前の値を探索する。2D版の `toArray()` は行優先順の一次元配列を返す。

## 利用例

準備中

## 制約・注意事項

- 1D版は IntArray.getOrDefault(int i, int defaultValue) を継承する。2D版は座標ごとに範囲を判定する。範囲内では圧縮後の順位、範囲外では指定値を返す。
- 1D版の `getLast()` は元配列の末尾に対応する順位を返す。空の場合の動作と指定値付き取得は [ArrayGuide](./ArrayGuide.md#api) を参照する。
- 1D版は空配列にも対応する。2D版は各次元の長さが1以上の矩形配列を前提とする。
- 添字は0始まりで、各次元の範囲内とする。`get` の範囲外アクセスは `IndexOutOfBoundsException` を投げる。1D版は配列アクセスに任せ、2D版は平坦化前に列を検査し、行は配列アクセスで検出する。
- iterator の終端後の取得は `NoSuchElementException` を投げる。
- 2D版は行ごとではなく、全要素をまとめて座標圧縮する。
- `rankOfValue(v)` は `containsValue(v)` が true の値に対して呼び出す。
- `valueOfRank(rank)` は選択した `RankType` に実在する順位に対して呼び出す。
- `sorted()` と `ranks()` は重複を除かず、どちらも元配列と同じ長さである。
- 配列からの構築は入力配列を変更せず、内部へコピーしてソートする。
- 同型のコピーコンストラクタは内部の3配列を複製し、元の値と順位の対応・順位の開始位置・要素順を保持する。再ソートや順位の再割り当ては行わない。2D版は寸法も保持する。
- コレクションを受け取るコンストラクタは、入力の要素を新たに圧縮する。`IntCompressedArray` を `IntCollection` 型の変数として渡した場合、入力は元の値ではなく圧縮後の順位となる。
- 圧縮後の順位は元の数値型に関係なく `int` である。

## 性能特性

`n` は全要素数とする。2D版の `n` は `h * w` である。

| 操作                                                                           | 時間計算量   | 補足                                                                       |
|--------------------------------------------------------------------------------|--------------|----------------------------------------------------------------------------|
| 配列・コレクションからの構築                                                   | `O(n log n)` | ソートと二分探索による                                                     |
| 同型のコピーコンストラクタ                                                     | `O(n)`       | 再圧縮しない                                                               |
| `get`, `getOrDefault`, `size`, `inRange`, 1D版の `getLast`, `getLastOrDefault` | `O(1)`       | —                                                                          |
| `rankOfValue`, `valueOfRank`, `containsValue`, `count`                         | `O(log n)`   | —                                                                          |
| `restore`                                                                      | `O(n log n)` | 各要素の値を二分探索で復元する                                             |
| `toArray`, `toArray2D`, `sorted`, `ranks`, 全要素の走査, `toString`            | `O(n)`       | —                                                                          |
| iterator / spliterator の生成・iterator の1要素取得                            | `O(1)`       | `spliterator` は `Arrays.spliterator(compressed)` で内部配列を直接走査する |

- **空間計算量**: `O(n)`

## 実装履歴

| バージョン          | 日付       | 対象クラス・変更内容                                                                                                                              |
|---------------------|------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0**  | 2026-07-15 | DENSE・COMPETITION・MODIFIED_COMPETITION、0/1-based、値と順位の相互変換を備える座標圧縮として初回実装                                             |
| **バージョン 1.1**  | 2026-07-15 | `toString()` を元配列順の空白区切り形式へ改善                                                                                                     |
| **バージョン 2.0**  | 2026-07-15 | 元配列順の圧縮結果を直接コピーする `toArray()` と `compressed()` を追加                                                                           |
| **バージョン 3.0**  | 2026-07-27 | `distinctSize` を `uniqueSize` へ変更して `length` とともに公開し、`toString()` の容量を調整                                                      |
| **バージョン 4.0**  | 2026-07-27 | 2D版を追加し、4クラスで重複していた順位方式をトップレベルの `RankType` へ統合                                                                     |
| **バージョン 5.0**  | 2026-10-23 | 2D版に順序付きプリミティブ spliterator を追加。全4クラスの iterator に終端例外を追加し、1D版の空配列の文字列化に対応                              |
| **バージョン 6.0**  | 2026-10-23 | 2D版に getOrDefault を追加。1D版は共通インターフェースから継承。配列全体のコピーを clone に統一                                                   |
| **バージョン 6.1**  | 2026-10-24 | 全4クラスの get に添字検査を追加し、2D版では checkedIndex で各軸を検査                                                                            |
| **バージョン 7.0**  | 2026-10-24 | 1D版で getLast / getLastOrDefault を継承。全4クラスの spliterator を内部配列用の実装へ変更                                                        |
| **バージョン 8.0**  | 2026-10-25 | 公開の長さを size に統一し、uniqueSize()・rankType()・isOneBased()・compressed() を削除。2D版を IntCollection に揃え、inRange と toArray2D を提供 |
| **バージョン 9.0**  | 2026-10-26 | 全4クラスに同型のコピーコンストラクタを追加。再圧縮せず、元の値・順位・要素順と2D版の寸法を保持                                                   |
| **バージョン 9.1**  | 2026-10-27 | 1Dのgetと2Dの行添字で配列アクセスと重複する範囲検査を削除                                                                                         |
| **バージョン 9.2**  | 2026-10-27 | IntCompressedArray / LongCompressedArray: toString の空判定を isEmpty に統一                                                                      |
| **バージョン 10.0** | 2026-10-29 | IntCompressedArray / LongCompressedArray: 配列インターフェースを受け取るコンストラクタの引数型を IntCollection / LongCollection へ拡張            |
