# IntCircularArray / LongCircularArray 利用ガイド

> [`IntCircularArray`](../../../src/lib/ds/arrays/IntCircularArray.java) と [`LongCircularArray`](../../../src/lib/ds/arrays/LongCircularArray.java) は、固定長配列を物理移動せず $\mathcal{O}(1)$ で左右へ論理回転するプリミティブ配列である。

[← README: Primitive Array Utilities](./README.md) · ソース: [IntCircularArray.java](../../../src/lib/ds/arrays/IntCircularArray.java) · [LongCircularArray.java](../../../src/lib/ds/arrays/LongCircularArray.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

現在の論理順に対する要素アクセスと更新、一括初期化に対応する。

## 特徴

- `offset` の更新だけで左右の論理回転を実現
- 現在の論理順に対する $\mathcal{O}(1)$ の `get` / `set`
- 左回転を正、右回転を負とする総回転数を保持
- `fill` と `setAll` による一括更新と回転状態の正規化
- 長さ・初期値・添字関数・配列・コピーによる構築に対応
- `IntMutableArray` / `LongMutableArray` として汎用アルゴリズムへ渡せる
- iterator、配列化、文字列化はいずれも現在の論理順を反映

### 型別クラス

| クラス                                                                   | 対象型 | 選ぶ目安                           |
|--------------------------------------------------------------------------|--------|------------------------------------|
| [`IntCircularArray`](../../../src/lib/ds/arrays/IntCircularArray.java)   | `int`  | int値の固定長配列を回転させる場合  |
| [`LongCircularArray`](../../../src/lib/ds/arrays/LongCircularArray.java) | `long` | long値の固定長配列を回転させる場合 |

## 依存関係

- `java.util.Arrays`
- `java.util.Objects`
- `java.util.PrimitiveIterator`
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntToLongFunction`
- [`lib.ds.arrays.IntMutableArray`](../../../src/lib/ds/arrays/IntMutableArray.java)
- [`lib.ds.arrays.LongMutableArray`](../../../src/lib/ds/arrays/LongMutableArray.java)

## API

### `IntCircularArray`

| 種別        | シグネチャ                                       | 戻り値                    | 説明                                                                                |
|-------------|--------------------------------------------------|---------------------------|-------------------------------------------------------------------------------------|
| Constructor | `IntCircularArray(int n)`                        | —                         | 長さ n の0初期化配列を構築する。                                                    |
| Constructor | `IntCircularArray(int n, int v)`                 | —                         | 長さ n の全要素を v で初期化する。                                                  |
| Constructor | `IntCircularArray(int n, IntUnaryOperator init)` | —                         | init を添字の昇順に1回ずつ呼び出して構築する。                                      |
| Constructor | `IntCircularArray(int[] a)`                      | —                         | 指定された配列の要素を同じ順序で保持する循環配列を構築する。                        |
| Constructor | `IntCircularArray(IntArray a)`                   | —                         | 指定された配列の論理順を保持する循環配列を構築する。                                |
| Constructor | `IntCircularArray(IntCircularArray other)`       | —                         | 全要素・回転状態・総回転数を保持する独立したコピーを構築する。                      |
| Method      | `size()`                                         | `int`                     | 要素数を返す。                                                                      |
| Method      | `rotation()`                                     | `long`                    | 左回転を正、右回転を負とする総回転数を返す。                                        |
| Method      | `get(int i)`                                     | `int`                     | 指定した有効な論理添字の値を返す。                                                  |
| Method      | `getLast()`                                      | `int`                     | IntArray から継承。現在の論理順の末尾の値を返す。                                   |
| Method      | `getLastOrDefault(int defaultValue)`             | `int`                     | IntArray から継承。長さが正のため、現在の論理順の末尾の値を返す。                   |
| Method      | `set(int i, int v)`                              | `int`                     | 指定した有効な論理添字を更新し、更新前の値を返す。                                  |
| Method      | `fill(int v)`                                    | `void`                    | 全要素を指定値で更新し、回転状態と総回転数を0へ戻す。                               |
| Method      | `setAll(IntUnaryOperator init)`                  | `void`                    | init(i) で全要素を再構築し、回転状態と総回転数を0へ戻す。 init は添字の昇順に呼ぶ。 |
| Method      | `rotateLeft()`                                   | `IntCircularArray`        | 論理配列を左へ1要素回転する。                                                       |
| Method      | `rotateLeft(int distance)`                       | `IntCircularArray`        | 論理配列を左へ distance 要素回転する。                                              |
| Method      | `rotateRight()`                                  | `IntCircularArray`        | 論理配列を右へ1要素回転する。                                                       |
| Method      | `rotateRight(int distance)`                      | `IntCircularArray`        | 論理配列を右へ distance 要素回転する。                                              |
| Method      | `resetRotation()`                                | `IntCircularArray`        | 回転状態を初期位置へ戻す。                                                          |
| Method      | `toArray()`                                      | `int[]`                   | 現在の論理順で独立した配列へコピーする。                                            |
| Method      | `iterator()`                                     | `PrimitiveIterator.OfInt` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。  |
| Method      | `toString()`                                     | `String`                  | 論理順の要素を半角スペースで区切った文字列として返す。                              |

### `LongCircularArray`

| 種別        | シグネチャ                                         | 戻り値                     | 説明                                                                                |
|-------------|----------------------------------------------------|----------------------------|-------------------------------------------------------------------------------------|
| Constructor | `LongCircularArray(int n)`                         | —                          | 長さ n の0初期化配列を構築する。                                                    |
| Constructor | `LongCircularArray(int n, long v)`                 | —                          | 長さ n の全要素を v で初期化する。                                                  |
| Constructor | `LongCircularArray(int n, IntToLongFunction init)` | —                          | init を添字の昇順に1回ずつ呼び出して構築する。                                      |
| Constructor | `LongCircularArray(long[] a)`                      | —                          | 指定された配列の要素を同じ順序で保持する循環配列を構築する。                        |
| Constructor | `LongCircularArray(LongArray a)`                   | —                          | 指定された配列の論理順を保持する循環配列を構築する。                                |
| Constructor | `LongCircularArray(LongCircularArray other)`       | —                          | 全要素・回転状態・総回転数を保持する独立したコピーを構築する。                      |
| Method      | `size()`                                           | `int`                      | 要素数を返す。                                                                      |
| Method      | `rotation()`                                       | `long`                     | 左回転を正、右回転を負とする総回転数を返す。                                        |
| Method      | `get(int i)`                                       | `long`                     | 指定した有効な論理添字の値を返す。                                                  |
| Method      | `getLast()`                                        | `long`                     | LongArray から継承。現在の論理順の末尾の値を返す。                                  |
| Method      | `getLastOrDefault(long defaultValue)`              | `long`                     | LongArray から継承。長さが正のため、現在の論理順の末尾の値を返す。                  |
| Method      | `set(int i, long v)`                               | `long`                     | 指定した有効な論理添字を更新し、更新前の値を返す。                                  |
| Method      | `fill(long v)`                                     | `void`                     | 全要素を指定値で更新し、回転状態と総回転数を0へ戻す。                               |
| Method      | `setAll(IntToLongFunction init)`                   | `void`                     | init(i) で全要素を再構築し、回転状態と総回転数を0へ戻す。 init は添字の昇順に呼ぶ。 |
| Method      | `rotateLeft()`                                     | `LongCircularArray`        | 論理配列を左へ1要素回転する。                                                       |
| Method      | `rotateLeft(int distance)`                         | `LongCircularArray`        | 論理配列を左へ distance 要素回転する。                                              |
| Method      | `rotateRight()`                                    | `LongCircularArray`        | 論理配列を右へ1要素回転する。                                                       |
| Method      | `rotateRight(int distance)`                        | `LongCircularArray`        | 論理配列を右へ distance 要素回転する。                                              |
| Method      | `resetRotation()`                                  | `LongCircularArray`        | 回転状態を初期位置へ戻す。                                                          |
| Method      | `toArray()`                                        | `long[]`                   | 現在の論理順で独立した配列へコピーする。                                            |
| Method      | `iterator()`                                       | `PrimitiveIterator.OfLong` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。  |
| Method      | `toString()`                                       | `String`                   | 論理順の要素を半角スペースで区切った文字列として返す。                              |

両クラスの `public final int size` は固定の要素数を表す。先頭・末尾取得と探索は [ArrayGuide](./ArrayGuide.md)、要素演算・交換・反転は [MutableArrayGuide](./MutableArrayGuide.md) の継承 API を参照する。

## 利用例

[Test.java](../../../test/example/ds/arrays/Test.java)

## 制約・注意事項

- 更新関数の共通条件は [MutableArrayGuide](./MutableArrayGuide.md#制約注意事項) を参照する。
- iterator / spliterator / Stream と、それらを使う一括処理の実行中は、`rotateLeft` / `rotateRight` / `resetRotation` / `fill` / `setAll` を呼び出さない。各要素の取得時に現在の `offset` を参照するため、途中で回転状態が変わると走査位置がずれる。変更検出は保証しない。
- getOrDefault(int i, int defaultValue) / getOrDefault(int i, long defaultValue) は配列インターフェースの既定実装を使う。範囲外では指定値を返す。
- contains はコレクションの既定実装を使い、論理順に線形探索する。
- 同型のコピーコンストラクタは内部配列を複製し、offset と rotation も保持する。IntArray / LongArray 型の引数からの構築では論理順をコピーし、回転状態は0から開始する。
- 現在の実装は `n >= 1` の問題制約を前提とする。
- `get` / `set` は `0 <= i < size` の論理添字を受け付け、範囲外では `IndexOutOfBoundsException` を投げる。負の添字は使用できない。末尾の取得には `getLast()` を使う。
- `fill` と `setAll` は物理配列を先頭から再構築し、`offset` と総回転数を0へ戻す。
- `setAll(init)` の `init` には正規化後の論理添字が渡される。
- `rotation()` は配列長で剰余を取らず、左回転を正、右回転を負として累積する。
- `rotateLeft(distance)` / `rotateRight(distance)` の負の回転数は逆方向の回転を表す。
- 回転・`resetRotation()` は自身を返し、連続呼び出しができる。
- `resetRotation()` は要素を並べ替えず、`offset` と総回転数を0へ戻す。
- 合計は保持しない。必要な場合は [ArrayUtils.sum](./ArrayUtilsGuide.md) で集計する。

## 性能特性

`n` は要素数とする。関数の評価時間は別途加算する。

| 操作                                                                                        | 時間計算量 | 補足                                                                        |
|---------------------------------------------------------------------------------------------|------------|-----------------------------------------------------------------------------|
| `get`, `getOrDefault`, `getLast`, `getLastOrDefault`, `set`, `size`, `rotation`, 各回転操作 | `O(1)`     | 回転は `offset` の更新だけで行う                                            |
| 各コンストラクタ                                                                            | `O(n)`     | `IntArray` / `LongArray` からの構築はコピー元の `toArray` を使う            |
| `fill`, `setAll`, `contains`, 全要素の走査                                                  | `O(n)`     | —                                                                           |
| `toArray`, `toString`                                                                       | `O(n)`     | `toArray` は回転の折り返し位置で分け、2回の `System.arraycopy` でコピーする |
| iterator の生成・1要素取得                                                                  | `O(1)`     | —                                                                           |

- **空間計算量**: 本体は `O(n)`。`toArray` と `toString` は結果用に `O(n)` を使う

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                            |
|--------------------|------------|---------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-12 | 旧一次元配列実装を、固定長の論理回転・更新・合計保持を行う `CircularArray` として再編                                           |
| **バージョン 2.0** | 2026-07-15 | `IntMutableArray` / `LongMutableArray` の実装へ変更                                                                             |
| **バージョン 2.1** | 2026-07-15 | `toString()` を現在の論理順の空白区切り形式へ改善                                                                               |
| **バージョン 3.0** | 2026-07-15 | 配列・配列インターフェース・supplierからの構築を追加                                                                            |
| **バージョン 4.0** | 2026-07-27 | 公開 `length` と `rotation()` を追加し、一括更新時の回転状態の正規化と `toString()` の容量調整を実施                            |
| **バージョン 4.1** | 2026-10-23 | toArray を2回の arraycopy に変更。iterator の終端例外とメソッドの定義順を統一                                                   |
| **バージョン 4.2** | 2026-10-23 | sum メソッドと合計保持フィールドを削除し、構築・更新時の集計処理を除去                                                          |
| **バージョン 5.0** | 2026-10-23 | 長さ・初期値指定・回転状態を保持するコピーのコンストラクタを追加。contains を既定実装へ移行し、複製方法・命名・メソッド順を統一 |
| **バージョン 6.0** | 2026-10-24 | get / set に論理添字の範囲検査を追加し、負の添字を禁止。現在の論理順の末尾を取得する getLast() を追加                           |
| **バージョン 7.0** | 2026-10-24 | getLast を配列インターフェースの既定実装へ移行し、getLastOrDefault を継承                                                       |
| **バージョン 7.1** | 2026-10-25 | 右回転名を shiftRight に統一。配列インターフェースからの構築でコピー元の toArray を利用                                         |
| **バージョン 8.0** | 2026-10-25 | 左右の回転名を rotateLeft / rotateRight に統一し、回転・resetRotation が自身を返すよう変更。公開の長さを size に統一            |
| **バージョン 8.1** | 2026-10-27 | 初期値指定コンストラクタの引数名をvに統一                                                                                       |
| **バージョン 8.2** | 2026-10-27 | IntCircularArray / LongCircularArray: 回転量の引数名を distance に統一し、初期値関数による構築を setAll へ委譲                  |
