# IntMutableArray / LongMutableArray 利用ガイド

> [`IntMutableArray`](../../../src/lib/ds/arrays/IntMutableArray.java) と [`LongMutableArray`](../../../src/lib/ds/arrays/LongMutableArray.java) は、`IntArray` / `LongArray` に添字更新を追加するインターフェースである。

[← README: Primitive Array Utilities](./README.md) · ソース: [IntMutableArray.java](../../../src/lib/ds/arrays/IntMutableArray.java) · [LongMutableArray.java](../../../src/lib/ds/arrays/LongMutableArray.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

具体的な格納形式に依存せず、論理添字による要素更新と一括更新を行う。要素数を変える操作は含まない。

## 特徴

- 読み取り API と更新 API を型レベルで分離
- `set` は更新前の値を返すため、別途 `get` せず差分処理が可能
- 加算・乗算・アフィン更新・二項関数による更新、要素交換・区間反転に対応
- `fill` と `setAll` を共通の既定メソッドとして提供
- `CircularArray` と `ArrayDeque` を同じ更新可能配列として扱える
- `int` / `long` のプリミティブ特化によりボクシングを回避

## 依存関係

- `java.util.Objects`
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntToLongFunction`
- `java.util.function.IntBinaryOperator` / `LongBinaryOperator`

## API

### `IntMutableArray`

| シグネチャ                                 | 戻り値 | 説明                                                                             |
|--------------------------------------------|--------|----------------------------------------------------------------------------------|
| `set(int i, int v)`                        | `int`  | 指定位置を更新し、更新前の値を返す。                                             |
| `add(int i, int d)`                        | `int`  | 指定位置に d を加算し、更新後の値を返す。                                        |
| `multiply(int i, int m)`                   | `int`  | 指定位置を m 倍し、更新後の値を返す。                                            |
| `affine(int i, int m, int d)`              | `int`  | 現在値 * m + d に更新し、更新後の値を返す。                                      |
| `apply(int i, int x, IntBinaryOperator f)` | `int`  | f(現在値, x) に更新し、更新後の値を返す。                                        |
| `swap(int i, int j)`                       | `void` | 指定した2要素を交換する。                                                        |
| `reverse()`                                | `void` | 全要素の順序を反転する。                                                         |
| `reverse(int i, int j)`                    | `void` | 半開区間 [i, j) 内の順序を反転する。                                             |
| `fill(int v)`                              | `void` | 全要素を指定値で更新する。要素数は変えない。                                     |
| `setAll(IntUnaryOperator init)`            | `void` | 各論理添字に対して生成した値で全要素を更新する。init は添字の昇順に1回ずつ呼ぶ。 |

### `LongMutableArray`

| シグネチャ                                   | 戻り値 | 説明                                                                             |
|----------------------------------------------|--------|----------------------------------------------------------------------------------|
| `set(int i, long v)`                         | `long` | 指定位置を更新し、更新前の値を返す。                                             |
| `add(int i, long d)`                         | `long` | 指定位置に d を加算し、更新後の値を返す。                                        |
| `multiply(int i, long m)`                    | `long` | 指定位置を m 倍し、更新後の値を返す。                                            |
| `affine(int i, long m, long d)`              | `long` | 現在値 * m + d に更新し、更新後の値を返す。                                      |
| `apply(int i, long x, LongBinaryOperator f)` | `long` | f(現在値, x) に更新し、更新後の値を返す。                                        |
| `swap(int i, int j)`                         | `void` | 指定した2要素を交換する。                                                        |
| `reverse()`                                  | `void` | 全要素の順序を反転する。                                                         |
| `reverse(int i, int j)`                      | `void` | 半開区間 [i, j) 内の順序を反転する。                                             |
| `fill(long v)`                               | `void` | 全要素を指定値で更新する。要素数は変えない。                                     |
| `setAll(IntToLongFunction init)`             | `void` | 各論理添字に対して生成した値で全要素を更新する。init は添字の昇順に1回ずつ呼ぶ。 |

読み取り・走査・変換の継承 API は [IntArray / LongArray 利用ガイド](./ArrayGuide.md) を参照する。

## 利用例

準備中

## 制約・注意事項

- 単一要素の更新・交換に渡す有効な論理添字は `[0, size())` とする。範囲外の動作は実装に依存する。
- `apply` / `setAll` の関数内から対象配列を変更しないこと。値の書き込みは呼び出し元のメソッドが行う。具体クラスが持つ要素追加・削除・回転・一括更新も関数内では呼び出さない。
- `reverse(i, j)` は `Objects.checkFromToIndex` で `0 <= i <= j <= size()` を更新前に検査する。範囲外または区間の逆転は `IndexOutOfBoundsException` を投げ、`i == j` の空区間は許可する。
- 既定の `affine` / `apply` は値を1回読み、計算結果を書き込んで返す。
- 既定の `fill` / `setAll` は各添字へ `set` を呼ぶ。具体クラスは必要に応じて一括更新をオーバーライドする。
- `CircularArray` の添字は現在の回転状態を反映する。
- `ArrayDeque` の添字は `[0, size())` とする。詳細は [ArrayDequeGuide](./ArrayDequeGuide.md) を参照する。
- 配列長の変更操作はこのインターフェースには含まれない。

## 性能特性

`n` は要素数とする。`get` / `set` が `O(1)` の実装を前提とし、関数の評価時間は別途加算する。

- 現在の実装クラスでは `set`, `add`, `multiply`, `affine`, `apply`, `swap` は `O(1)`$
- `fill`, `setAll`, `reverse()`: `O(n)`
- `reverse(i, j)`: `O(j - i)`。両端から異なる位置の要素だけを交換し、奇数長の中央要素には触れない
- 各操作の追加領域は `O(1)`

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                   |
|--------------------|------------|--------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-15 | `IntArray` / `LongArray` から更新操作を分離するインターフェースとして初回実装                          |
| **バージョン 2.0** | 2026-07-15 | 全要素を更新する既定メソッド `fill` / `setAll` を追加                                                  |
| **バージョン 3.0** | 2026-10-25 | 要素演算・swap・reverse を既定メソッドへ追加。affine / apply の再取得を除去し、Int / Long の処理を統一 |
| **バージョン 3.1** | 2026-10-26 | Int / Long の reverse の停止条件を変更し、奇数長での中央要素の自己交換を除去                           |
| **バージョン 3.2** | 2026-10-27 | reverseの入口で半開区間を検査し、不正な範囲の黙認と交換途中の失敗を防止 |
