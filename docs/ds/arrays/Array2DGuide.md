# IntArray2D / LongArray2D / CharArray2D 利用ガイド

> 座標変換で回転・転置・反転を行うプリミティブ2次元配列。

[← Primitive Array Utilities](./README.md) · ソース: [IntArray2D.java](../../../src/lib/ds/arrays/IntArray2D.java) · [LongArray2D.java](../../../src/lib/ds/arrays/LongArray2D.java) · [CharArray2D.java](../../../src/lib/ds/arrays/CharArray2D.java)

**目次** [概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

## 概要

要素を一次元配列に保持し、論理座標 `(i, j)` を `base + i * di + j * dj` で物理位置へ変換する。
変換操作はデータを移動せず、論理寸法と座標変換の状態だけを変更する。

## 特徴

- 長方形の回転・転置・左右反転・上下反転を定数時間で合成できる。
- 配列・初期値・座標関数から構築でき、コピーコンストラクタは向きも保持する。
- int / long / char の3種類を提供する。char版には数値演算用の `add` / `multiply` / `affine` は含めない。
- `set` は旧値、`add` / `multiply` / `affine` / `apply` は更新後の値を返す。
- `setAll` は現在の論理座標に従い全要素を更新する。
- iterator、配列化、Stream は現在の論理状態の行優先順に対応する。

## 依存関係

- `java.lang.Math`、`java.util` の配列操作・添字検査・例外・プリミティブ iterator / spliterator。
- `IntCollection` / `LongCollection`。
- `IntBinaryOperator` / `LongBinaryOperator`、Long 版の座標初期化には `lib.util.function.IntBinaryToLongFunction`。
- 継承 API は `java.util.function` と `java.util.stream` を利用する。

## API

3クラスとも `public final int size` を公開し、総要素数を保持する。`h()` / `w()` は回転・転置後の論理寸法を返す。

### `IntArray2D`

| シグネチャ                                         | 戻り値                    | 説明                                                                                                 |
|----------------------------------------------------|---------------------------|------------------------------------------------------------------------------------------------------|
| `IntArray2D(int h, int w)`                         | —                         | 高さ h、幅 w の0初期化配列を構築する。                                                               |
| `IntArray2D(int h, int w, int initialValue)`       | —                         | 全要素を initialValue で初期化する。                                                                 |
| `IntArray2D(int h, int w, int[] a)`                | —                         | 配列の先頭 h * w 要素を行優先順でコピーする。配列長は要素数以上とする。                              |
| `IntArray2D(int h, int w, IntBinaryOperator init)` | —                         | 各座標に init(i, j) を行優先順で適用して構築する。                                                   |
| `IntArray2D(int[][] a)`                            | —                         | 少なくとも1行を持つ長方形配列を行優先順でコピーする。各行の長さは等しいこと。                        |
| `IntArray2D(IntArray2D other)`                     | —                         | 論理寸法・向き・全要素を保持する独立したコピーを構築する。                                           |
| `size()`                                           | `int`                     | 現在の要素数を返す。                                                                                 |
| `h()`                                              | `int`                     | 現在の論理状態の高さを返す。                                                                         |
| `w()`                                              | `int`                     | 現在の論理状態の幅を返す。                                                                           |
| `inRange(int i, int j)`                            | `boolean`                 | 0 <= i < h() かつ 0 <= j < w() かを返す。                                                            |
| `get(int i, int j)`                                | `int`                     | 指定した論理位置の値を返す。                                                                         |
| `getOrDefault(int i, int j, int defaultValue)`     | `int`                     | 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。                                     |
| `set(int i, int j, int v)`                         | `int`                     | 指定した論理位置を v に更新し、更新前の値を返す。                                                    |
| `add(int i, int j, int d)`                         | `int`                     | 指定した論理位置に d を加算し、更新後の値を返す。                                                    |
| `multiply(int i, int j, int m)`                    | `int`                     | 指定した論理位置を m 倍し、更新後の値を返す。                                                        |
| `affine(int i, int j, int m, int d)`               | `int`                     | 指定した論理位置を 現在値 * m + d に更新し、更新後の値を返す。                                       |
| `apply(int i, int j, int x, IntBinaryOperator f)`  | `int`                     | 指定した論理位置を f(現在値, x) に更新し、更新後の値を返す。                                         |
| `fill(int v)`                                      | `void`                    | 全要素を v に更新する。論理寸法と向きは維持する。                                                    |
| `setAll(IntBinaryOperator init)`                   | `void`                    | 現在の論理座標に init を行優先順で適用して全要素を更新する。向きは維持する。                         |
| `rotateLeft()`                                     | `IntArray2D`              | 反時計回りに90度論理回転する。                                                                       |
| `rotateLeft(int distance)`                         | `IntArray2D`              | 反時計回りに90度単位で distance 回論理回転する。負数は逆方向を表す。                                 |
| `rotateRight()`                                    | `IntArray2D`              | 時計回りに90度論理回転する。                                                                         |
| `rotateRight(int distance)`                        | `IntArray2D`              | 時計回りに90度単位で distance 回論理回転する。負数は逆方向を表す。                                   |
| `transpose()`                                      | `IntArray2D`              | 論理状態を転置し、高さと幅を入れ替える。                                                             |
| `flipHorizontal()`                                 | `IntArray2D`              | 論理状態を左右反転する。                                                                             |
| `flipVertical()`                                   | `IntArray2D`              | 論理状態を上下反転する。                                                                             |
| `toArray2D()`                                      | `int[][]`                 | 現在の論理状態を行優先順で独立した2次元配列へコピーする。                                            |
| `iterator()`                                       | `PrimitiveIterator.OfInt` | 現在の論理状態の行優先順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。 |
| `spliterator()`                                    | `Spliterator.OfInt`       | 行優先順の ORDERED・SIZED・SUBSIZED を持つ spliterator を返す。                                      |
| `toString()`                                       | `String`                  | 論理状態の各行を半角スペース区切り、行間を改行した文字列として返す。要素がなければ空文字列を返す。   |

継承する API：

| シグネチャ                                  | 戻り値          | 説明                                            |
|---------------------------------------------|-----------------|-------------------------------------------------|
| `isEmpty()`                                 | `boolean`       | 要素数が0か判定                                 |
| `contains(int v)`                           | `boolean`       | iterator による線形探索                         |
| `forEachInt(IntConsumer action)`            | `void`          | 論理順で各要素に適用                            |
| `intStream()`                               | `IntStream`     | 論理順の逐次 Stream を生成                      |
| `toList()`                                  | `List<Integer>` | 論理順の不変リストを生成                        |
| `forEach(Consumer<? super Integer> action)` | `void`          | Iterable の既定実装。ボクシングして論理順に適用 |
| `toArray()`                                 | `int[]`         | 行優先順の独立した一次元配列を生成              |

### `LongArray2D`

| シグネチャ                                                | 戻り値                     | 説明                                                                                                 |
|-----------------------------------------------------------|----------------------------|------------------------------------------------------------------------------------------------------|
| `LongArray2D(int h, int w)`                               | —                          | 高さ h、幅 w の0初期化配列を構築する。                                                               |
| `LongArray2D(int h, int w, long initialValue)`            | —                          | 全要素を initialValue で初期化する。                                                                 |
| `LongArray2D(int h, int w, int[] a)`                      | —                          | 配列の先頭 h * w 要素を行優先順でコピーする。配列長は要素数以上とする。                              |
| `LongArray2D(int h, int w, long[] a)`                     | —                          | 配列の先頭 h * w 要素を行優先順でコピーする。配列長は要素数以上とする。                              |
| `LongArray2D(int h, int w, IntBinaryToLongFunction init)` | —                          | 各座標に init(i, j) を行優先順で適用して構築する。                                                   |
| `LongArray2D(int[][] a)`                                  | —                          | 少なくとも1行を持つ長方形配列を行優先順でコピーする。各行の長さは等しいこと。                        |
| `LongArray2D(long[][] a)`                                 | —                          | 少なくとも1行を持つ長方形配列を行優先順でコピーする。各行の長さは等しいこと。                        |
| `LongArray2D(LongArray2D other)`                          | —                          | 論理寸法・向き・全要素を保持する独立したコピーを構築する。                                           |
| `size()`                                                  | `int`                      | 現在の要素数を返す。                                                                                 |
| `h()`                                                     | `int`                      | 現在の論理状態の高さを返す。                                                                         |
| `w()`                                                     | `int`                      | 現在の論理状態の幅を返す。                                                                           |
| `inRange(int i, int j)`                                   | `boolean`                  | 0 <= i < h() かつ 0 <= j < w() かを返す。                                                            |
| `get(int i, int j)`                                       | `long`                     | 指定した論理位置の値を返す。                                                                         |
| `getOrDefault(int i, int j, long defaultValue)`           | `long`                     | 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。                                     |
| `set(int i, int j, long v)`                               | `long`                     | 指定した論理位置を v に更新し、更新前の値を返す。                                                    |
| `add(int i, int j, long d)`                               | `long`                     | 指定した論理位置に d を加算し、更新後の値を返す。                                                    |
| `multiply(int i, int j, long m)`                          | `long`                     | 指定した論理位置を m 倍し、更新後の値を返す。                                                        |
| `affine(int i, int j, long m, long d)`                    | `long`                     | 指定した論理位置を 現在値 * m + d に更新し、更新後の値を返す。                                       |
| `apply(int i, int j, long x, LongBinaryOperator f)`       | `long`                     | 指定した論理位置を f(現在値, x) に更新し、更新後の値を返す。                                         |
| `fill(long v)`                                            | `void`                     | 全要素を v に更新する。論理寸法と向きは維持する。                                                    |
| `setAll(IntBinaryToLongFunction init)`                    | `void`                     | 現在の論理座標に init を行優先順で適用して全要素を更新する。向きは維持する。                         |
| `rotateLeft()`                                            | `LongArray2D`              | 反時計回りに90度論理回転する。                                                                       |
| `rotateLeft(int distance)`                                | `LongArray2D`              | 反時計回りに90度単位で distance 回論理回転する。負数は逆方向を表す。                                 |
| `rotateRight()`                                           | `LongArray2D`              | 時計回りに90度論理回転する。                                                                         |
| `rotateRight(int distance)`                               | `LongArray2D`              | 時計回りに90度単位で distance 回論理回転する。負数は逆方向を表す。                                   |
| `transpose()`                                             | `LongArray2D`              | 論理状態を転置し、高さと幅を入れ替える。                                                             |
| `flipHorizontal()`                                        | `LongArray2D`              | 論理状態を左右反転する。                                                                             |
| `flipVertical()`                                          | `LongArray2D`              | 論理状態を上下反転する。                                                                             |
| `toArray2D()`                                             | `long[][]`                 | 現在の論理状態を行優先順で独立した2次元配列へコピーする。                                            |
| `iterator()`                                              | `PrimitiveIterator.OfLong` | 現在の論理状態の行優先順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。 |
| `spliterator()`                                           | `Spliterator.OfLong`       | 行優先順の ORDERED・SIZED・SUBSIZED を持つ spliterator を返す。                                      |
| `toString()`                                              | `String`                   | 論理状態の各行を半角スペース区切り、行間を改行した文字列として返す。要素がなければ空文字列を返す。   |

継承する API：

| シグネチャ                               | 戻り値       | 説明                                            |
|------------------------------------------|--------------|-------------------------------------------------|
| `isEmpty()`                              | `boolean`    | 要素数が0か判定                                 |
| `contains(long v)`                       | `boolean`    | iterator による線形探索                         |
| `forEachLong(LongConsumer action)`       | `void`       | 論理順で各要素に適用                            |
| `longStream()`                           | `LongStream` | 論理順の逐次 Stream を生成                      |
| `toList()`                               | `List<Long>` | 論理順の不変リストを生成                        |
| `forEach(Consumer<? super Long> action)` | `void`       | Iterable の既定実装。ボクシングして論理順に適用 |
| `toArray()`                              | `long[]`     | 行優先順の独立した一次元配列を生成              |

### `CharArray2D`

| シグネチャ                                          | 戻り値                    | 説明                                                                                                       |
|-----------------------------------------------------|---------------------------|------------------------------------------------------------------------------------------------------------|
| `CharArray2D(int h, int w)`                         | —                         | 高さ h、幅 w の0初期化配列を構築する。                                                                     |
| `CharArray2D(int h, int w, char initialValue)`      | —                         | 高さ h、幅 w の全要素を initialValue で初期化する。                                                        |
| `CharArray2D(int h, int w, char[] a)`               | —                         | 配列の先頭 h * w 文字を行優先順でコピーする。配列長は要素数以上とする。                                    |
| `CharArray2D(int h, int w, IntBinaryOperator init)` | —                         | init(i, j) を行優先順に呼び、結果を char に変換して構築する。                                              |
| `CharArray2D(char[][] a)`                           | —                         | 少なくとも1行を持つ長方形配列を行優先順でコピーする。各行の長さは等しいこと。                              |
| `CharArray2D(CharArray2D other)`                    | —                         | 論理寸法・向き・全要素を保持する独立したコピーを構築する。                                                 |
| `size()`                                            | `int`                     | 現在の要素数を返す。                                                                                       |
| `h()`                                               | `int`                     | 現在の論理状態の高さを返す。                                                                               |
| `w()`                                               | `int`                     | 現在の論理状態の幅を返す。                                                                                 |
| `inRange(int i, int j)`                             | `boolean`                 | 0 <= i < h() かつ 0 <= j < w() かを返す。                                                                  |
| `get(int i, int j)`                                 | `char`                    | 指定した論理位置の値を返す。                                                                               |
| `getOrDefault(int i, int j, char defaultValue)`     | `char`                    | 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。                                           |
| `set(int i, int j, char v)`                         | `char`                    | 指定した論理位置を v に更新し、更新前の値を返す。                                                          |
| `apply(int i, int j, char x, IntBinaryOperator f)`  | `char`                    | 指定した論理位置を (char) f(現在値, x) に更新し、更新後の値を返す。                                        |
| `fill(char v)`                                      | `void`                    | 全要素を v に更新する。論理寸法と向きは維持する。                                                          |
| `setAll(IntBinaryOperator init)`                    | `void`                    | 現在の論理座標に init を行優先順で適用して全要素を更新する。関数の結果は char に変換する。向きは維持する。 |
| `rotateLeft()`                                      | `CharArray2D`             | 反時計回りに90度論理回転する。                                                                             |
| `rotateLeft(int distance)`                          | `CharArray2D`             | 反時計回りに90度単位で distance 回論理回転する。負数は逆方向を表す。                                       |
| `rotateRight()`                                     | `CharArray2D`             | 時計回りに90度論理回転する。                                                                               |
| `rotateRight(int distance)`                         | `CharArray2D`             | 時計回りに90度単位で distance 回論理回転する。負数は逆方向を表す。                                         |
| `transpose()`                                       | `CharArray2D`             | 論理状態を転置し、高さと幅を入れ替える。                                                                   |
| `flipHorizontal()`                                  | `CharArray2D`             | 論理状態を左右反転する。                                                                                   |
| `flipVertical()`                                    | `CharArray2D`             | 論理状態を上下反転する。                                                                                   |
| `toArray2D()`                                       | `char[][]`                | 現在の論理状態を行優先順で独立した2次元配列へコピーする。                                                  |
| `iterator()`                                        | `PrimitiveIterator.OfInt` | 現在の論理状態の行優先順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。       |
| `spliterator()`                                     | `Spliterator.OfInt`       | 行優先順の ORDERED・SIZED・SUBSIZED を持つ spliterator を返す。                                            |
| `toString()`                                        | `String`                  | 各行の文字を区切りなしで連結し、行間を改行した文字列を返す。                                               |

`CharArray2D` は `IntCollection` の全 API を継承する。`toArray()` は文字を int に拡張した一次元配列、`toArray2D()` は `char[][]` を返す。継承メソッドのシグネチャは [ArrayGuide](./ArrayGuide.md) を参照する。

## 利用例

### IntArray2D

[Example.java](../../../test/verify/ds/arrays/intarray2d/Example.java)

### LongArray2D

準備中

### CharArray2D

準備中

## 制約・注意事項

- 寸法は非負で、`h * w` が int の範囲内かつ配列として確保可能であること。寸法の検査は行わない。
- 2次元配列からの構築は少なくとも1行を持つ長方形配列を前提とし、幅を先頭行から決める。一次元配列からの構築は配列長が `h * w` 以上であること。
- 座標は0始まりの `(行, 列)`。現在の論理寸法の `[0, h()) × [0, w())` を有効範囲とする。
- `get` / `set` / `add` / `multiply` / `affine` / `apply` は範囲外で `IndexOutOfBoundsException` を投げる。`getOrDefault` は指定値を返す。
- `set` は更新前の値を返す。他の要素更新演算は更新後の値を返す。算術演算は int / long の演算規則に従う。
- 回転数は90度単位で、負数は逆方向を表す。回転・転置・反転はこのインスタンスを変更して自身を返す。
- `fill` / `setAll` は論理寸法と向きを維持する。`setAll` は現在の論理座標で関数を評価する。
- `apply` / `setAll` の関数内、および iterator / spliterator / Stream とそれらを使う一括処理の実行中は、回転・転置・反転を行わない。これらの操作は座標から配列位置への対応や論理寸法を変更する。`apply` は関数評価前の配列位置へ書き込み、`setAll` と走査は処理中の論理状態を参照するため、途中で変更すると対象位置が一致しなくなる。変更検出は保証しない。
- コピーコンストラクタは内部配列を共有しない。`toArray` / `toArray2D` も独立した配列を返す。
- `CharArray2D` は UTF-16 のコード単位を扱う。`apply` / `setAll` と初期化関数の結果は char に変換する。文字列化では行内に区切りを入れない。
- `equals` / `hashCode` は `Object` の実装を継承する。内容比較は行わない。

## 性能特性

`h`, `w` は現在の論理寸法、`n = h * w` は要素数とする。

| 操作                                               | 時間計算量 | 追加領域   |
|----------------------------------------------------|------------|------------|
| 配列確保・初期値指定・一次元配列・コピーによる構築 | `O(n)`     | `O(n)`     |
| 2次元配列・座標関数による構築                      | `O(h + n)` | `O(n)`     |
| 要素の取得・更新、寸法取得、範囲判定               | `O(1)`     | `O(1)`     |
| 回転（回数指定を含む）・転置・反転                 | `O(1)`     | `O(1)`     |
| `fill`, `contains`, 全要素の走査                   | `O(n)`     | `O(1)`     |
| `setAll`                                           | `O(h + n)` | `O(1)`     |
| `toArray`, `toList`, `toString`                    | `O(n)`     | `O(n)`     |
| `toArray2D`                                        | `O(h + n)` | `O(h + n)` |
| iterator / spliterator / Stream の生成             | `O(1)`     | `O(1)`     |

関数の評価時間は別途加算する。iterator の1要素取得は `O(1)`。本体の使用領域は `O(n)`。変換後の配列化では論理順の添字計算を行う。寸法が正なら `O(h + n)` は `O(n)` である。

## 実装履歴

| バージョン         | 日付       | 詳細                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
|--------------------|------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-11-19 | 一次元圧縮格納、`get` / `set`、90度回転、正方行列の転置、iterator を備える2次元配列として初回実装                                                                                                                                                                                                                                                                                                                                                                                                                  |
| **バージョン 2.0** | 2026-06-11 | 配列ライブラリの名称・構成変更に合わせて再編                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| **バージョン 3.0** | 2026-07-10 | import可能な `lib.ds` パッケージ構成へ移行                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| **バージョン 4.0** | 2026-07-12 | `lib.ds.arrays` パッケージへ移動し、配列データ構造群として再編                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **バージョン 5.0** | 2026-07-27 | `h()` / `w()`、公開 `length`、論理状態を行単位で出力する `toString()` を追加                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| **バージョン 5.1** | 2026-10-22 | `LongArray2D` の初期値関数を `(int, int) -> long` に特化した `IntBinaryToLongFunction` へ変更                                                                                                                                                                                                                                                                                                                                                                                                                      |
| **バージョン 6.0** | 2026-10-22 | 寸法・配列・関数による構築、`fill`、論理配列へのコピー、回転数指定、長方形の転置と変換操作の合成に対応                                                                                                                                                                                                                                                                                                                                                                                                             |
| **バージョン 7.0** | 2026-10-23 | 座標変換を `base` / `di` / `dj` による添字計算へ変更。`flipHorizontal` / `flipVertical`、`inRange`、`getOrDefault`、`add`、`multiply`、`affine`、`apply` を追加。`lRotate` / `rRotate` を `rotateLeft` / `rotateRight` へ改名。`IntCollection` / `LongCollection` を実装し、`iterator()` を論理順へ変更。`toFlatArray()` を `toArray()` に置換し、`length` を `size` に変更。範囲外座標で `IndexOutOfBoundsException` を投げるよう変更。`set` の戻り値を更新後の値に変更。空の配列の `toString()` を空文字列に変更 |
| **バージョン 8.0** | 2026-10-23 | 初期値・コピーによる構築と論理座標による setAll を追加。set を旧値返却へ変更。変換操作を自身の返却に対応させ、ORDERED の spliterator を追加                                                                                                                                                                                                                                                                                                                                                                        |
| **バージョン 9.0** | 2026-10-23 | CharArray2D を追加。文字列化の容量に改行分を含め、論理座標による setAll と数値版に揃えたメソッド順を採用                                                                                                                                                                                                                                                                                                                                                                                                           |
| **バージョン 9.1** | 2026-10-23 | 物理添字と返却用配列の名前、二重ループの書式を統一                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
