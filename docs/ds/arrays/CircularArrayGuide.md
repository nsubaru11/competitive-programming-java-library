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

| シグネチャ                                       | 戻り値                    | 説明                                                                                |
|--------------------------------------------------|---------------------------|-------------------------------------------------------------------------------------|
| `IntCircularArray(int n)`                        | —                         | 長さ n の0初期化配列を構築する。                                                    |
| `IntCircularArray(int n, int initialValue)`      | —                         | 長さ n の全要素を initialValue で初期化する。                                       |
| `IntCircularArray(int n, IntUnaryOperator init)` | —                         | init を添字の昇順に1回ずつ呼び出して構築する。                                      |
| `IntCircularArray(int[] a)`                      | —                         | 指定された配列の要素を同じ順序で保持する循環配列を構築する。                        |
| `IntCircularArray(IntArray a)`                   | —                         | 指定された配列の論理順を保持する循環配列を構築する。                                |
| `IntCircularArray(IntCircularArray other)`       | —                         | 全要素・回転状態・総回転数を保持する独立したコピーを構築する。                      |
| `size()`                                         | `int`                     | 要素数を返す。                                                                      |
| `rotation()`                                     | `long`                    | 左回転を正、右回転を負とする総回転数を返す。                                        |
| `get(int i)`                                     | `int`                     | 指定した有効な論理添字の値を返す。                                                  |
| `getLast()`                                      | `int`                     | IntArray から継承。現在の論理順の末尾の値を返す。                                   |
| `getLastOrDefault(int defaultValue)`             | `int`                     | IntArray から継承。長さが正のため、現在の論理順の末尾の値を返す。                   |
| `set(int i, int v)`                              | `int`                     | 指定した有効な論理添字を更新し、更新前の値を返す。                                  |
| `fill(int v)`                                    | `void`                    | 全要素を指定値で更新し、回転状態と総回転数を0へ戻す。                               |
| `setAll(IntUnaryOperator init)`                  | `void`                    | init(i) で全要素を再構築し、回転状態と総回転数を0へ戻す。 init は添字の昇順に呼ぶ。 |
| `shiftLeft()`                                    | `void`                    | 論理配列を左へ1要素回転する。                                                       |
| `shiftLeft(int n)`                               | `void`                    | 論理配列を左へn要素回転する。                                                       |
| `shiftRight()`                                   | `void`                    | 論理配列を右へ1要素回転する。                                                       |
| `shiftRight(int n)`                              | `void`                    | 論理配列を右へn要素回転する。                                                       |
| `resetRotation()`                                | `void`                    | 回転状態を初期位置へ戻す。                                                          |
| `toArray()`                                      | `int[]`                   | 現在の論理順で独立した配列へコピーする。                                            |
| `iterator()`                                     | `PrimitiveIterator.OfInt` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。  |
| `toString()`                                     | `String`                  | 論理順の要素を半角スペースで区切った文字列として返す。                              |

### `LongCircularArray`

| シグネチャ                                         | 戻り値                     | 説明                                                                                |
|----------------------------------------------------|----------------------------|-------------------------------------------------------------------------------------|
| `LongCircularArray(int n)`                         | —                          | 長さ n の0初期化配列を構築する。                                                    |
| `LongCircularArray(int n, long initialValue)`      | —                          | 長さ n の全要素を initialValue で初期化する。                                       |
| `LongCircularArray(int n, IntToLongFunction init)` | —                          | init を添字の昇順に1回ずつ呼び出して構築する。                                      |
| `LongCircularArray(long[] a)`                      | —                          | 指定された配列の要素を同じ順序で保持する循環配列を構築する。                        |
| `LongCircularArray(LongArray a)`                   | —                          | 指定された配列の論理順を保持する循環配列を構築する。                                |
| `LongCircularArray(LongCircularArray other)`       | —                          | 全要素・回転状態・総回転数を保持する独立したコピーを構築する。                      |
| `size()`                                           | `int`                      | 要素数を返す。                                                                      |
| `rotation()`                                       | `long`                     | 左回転を正、右回転を負とする総回転数を返す。                                        |
| `get(int i)`                                       | `long`                     | 指定した有効な論理添字の値を返す。                                                  |
| `getLast()`                                        | `long`                     | LongArray から継承。現在の論理順の末尾の値を返す。                                  |
| `getLastOrDefault(long defaultValue)`              | `long`                     | LongArray から継承。長さが正のため、現在の論理順の末尾の値を返す。                  |
| `set(int i, long v)`                               | `long`                     | 指定した有効な論理添字を更新し、更新前の値を返す。                                  |
| `fill(long v)`                                     | `void`                     | 全要素を指定値で更新し、回転状態と総回転数を0へ戻す。                               |
| `setAll(IntToLongFunction init)`                   | `void`                     | init(i) で全要素を再構築し、回転状態と総回転数を0へ戻す。 init は添字の昇順に呼ぶ。 |
| `shiftLeft()`                                      | `void`                     | 論理配列を左へ1要素回転する。                                                       |
| `shiftLeft(int n)`                                 | `void`                     | 論理配列を左へn要素回転する。                                                       |
| `shiftRight()`                                     | `void`                     | 論理配列を右へ1要素回転する。                                                       |
| `shiftRight(int n)`                                | `void`                     | 論理配列を右へn要素回転する。                                                       |
| `resetRotation()`                                  | `void`                     | 回転状態を初期位置へ戻す。                                                          |
| `toArray()`                                        | `long[]`                   | 現在の論理順で独立した配列へコピーする。                                            |
| `iterator()`                                       | `PrimitiveIterator.OfLong` | 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。  |
| `toString()`                                       | `String`                   | 論理順の要素を半角スペースで区切った文字列として返す。                              |

両クラスの `public final int length` は固定の要素数を表す。その他の継承 API は [ArrayGuide](./ArrayGuide.md) を参照する。

## 利用例

準備中

## 制約・注意事項

- 更新関数の共通条件は [MutableArrayGuide](./MutableArrayGuide.md#制約注意事項) を参照する。
- iterator / spliterator / Stream と、それらを使う一括処理の実行中は、`shiftLeft` / `shiftRight` / `resetRotation` / `fill` / `setAll` を呼び出さない。各要素の取得時に現在の `offset` を参照するため、途中で回転状態が変わると走査位置がずれる。変更検出は保証しない。
- getOrDefault(int i, int defaultValue) / getOrDefault(int i, long defaultValue) は配列インターフェースの既定実装を使う。範囲外では指定値を返す。
- contains はコレクションの既定実装を使い、論理順に線形探索する。
- 同型のコピーコンストラクタは内部配列を複製し、offset と rotation も保持する。IntArray / LongArray 型の引数からの構築では論理順をコピーし、回転状態は0から開始する。
- 現在の実装は `n >= 1` の問題制約を前提とする。
- `get` / `set` は `0 <= i < length` の論理添字を受け付け、範囲外では `IndexOutOfBoundsException` を投げる。負の添字は使用できない。末尾の取得には `getLast()` を使う。
- `fill` と `setAll` は物理配列を先頭から再構築し、`offset` と総回転数を0へ戻す。
- `setAll(init)` の `init` には正規化後の論理添字が渡される。
- `rotation()` は配列長で剰余を取らず、左回転を正、右回転を負として累積する。
- `shiftLeft(n)` / `shiftRight(n)` の負の回転数は逆方向の回転を表す。
- `resetRotation()` は要素を並べ替えず、`offset` と総回転数を0へ戻す。
- 合計は保持しない。必要な場合は [ArrayUtils.sum](./ArrayUtilsGuide.md) で集計する。

## 性能特性

`n` は要素数とする。関数の評価時間は別途加算する。

- `get`, `getOrDefault`, `getLast`, `getLastOrDefault`, `set`, `size`, `rotation`, 各回転操作: $\mathcal{O}(1)$
- 各コンストラクタ: $\mathcal{O}(n)$
- `fill`, `setAll`, `contains`, `toArray`, 全要素の走査, `toString`: $\mathcal{O}(n)$
- iterator の生成・1要素取得: `O(1)`
- `toArray` は回転の折り返し位置で分け、2回の `System.arraycopy` でコピーする。
- 使用メモリ: $\mathcal{O}(n)$
- `toArray` と `toString` は結果用の追加メモリを使用

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                                            |
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
