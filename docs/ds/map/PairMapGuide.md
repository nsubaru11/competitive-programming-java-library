# Pair Map 利用ガイド

> int の組 (a, b) をキーとするハッシュマップ `IntPairIntMap`・`IntPairLongMap` の仕様をまとめる。

[← README: HashMap](./README.md) · ソース: [IntPairIntMap.java](../../../src/lib/ds/map/IntPairIntMap.java) · [IntPairLongMap.java](../../../src/lib/ds/map/IntPairLongMap.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

キー (a, b) を1つの long へパックし、[整数型マップ](./MapGuide.md) の `LongIntMap`・`LongLongMap` へ委譲するハッシュマップである。a を上位32bit、b を下位32bitとする long へ可逆にパックする。
戻り値・既定値・走査順の仕様は委譲先と同じである。要素数を n、委譲先の容量を c とする。

## 特徴

- (a, b) を引数で直接受け取り、呼び出し側でパックしない。
- 走査系メソッドと `computeIfAbsent` の関数はキーを成分へ復元して受け取る。
- `keys`・`entries` は委譲先の内部配列を直接走査し、戻り値の配列だけを確保する。
- コピーコンストラクタで内部配列を共有しない複製を作れる。

### 型別クラス

| クラス                                                          | 値型   | 委譲先        | 選ぶ目安                                |
|-----------------------------------------------------------------|--------|---------------|-----------------------------------------|
| [`IntPairIntMap`](../../../src/lib/ds/map/IntPairIntMap.java)   | `int`  | `LongIntMap`  | 座標・頂点の組に int 値を対応させる場合 |
| [`IntPairLongMap`](../../../src/lib/ds/map/IntPairLongMap.java) | `long` | `LongLongMap` | 値に long が必要な場合                  |

## 依存関係

- [LongIntMap](../../../src/lib/ds/map/LongIntMap.java)、[LongLongMap](../../../src/lib/ds/map/LongLongMap.java)
- Java標準ライブラリ: `java.util.function` の `IntBinaryOperator`・`LongBinaryOperator`・`IntConsumer`・`LongConsumer`
- `lib.util.function`: [IntBinaryConsumer](../../../src/lib/util/function/IntBinaryConsumer.java)、[IntTernaryConsumer](../../../src/lib/util/function/IntTernaryConsumer.java)、[IntBinaryLongConsumer](../../../src/lib/util/function/IntBinaryLongConsumer.java)、[IntBinaryToLongFunction](../../../src/lib/util/function/IntBinaryToLongFunction.java)

## API

### `IntPairIntMap`

[ソース](../../../src/lib/ds/map/IntPairIntMap.java)

| 種別        | シグネチャ                                                                      | 戻り値    | 説明                                                                                  |
|-------------|---------------------------------------------------------------------------------|-----------|---------------------------------------------------------------------------------------|
| Constructor | `IntPairIntMap()`                                                               | —         | 想定要素数1024、defaultValue 0 で構築する。                                           |
| Constructor | `IntPairIntMap(int expectedSize)`                                               | —         | 想定要素数 expectedSize、defaultValue 0 で構築する。                                  |
| Constructor | `IntPairIntMap(int expectedSize, int defaultValue)`                             | —         | 想定要素数 expectedSize と defaultValue を指定して構築する。                          |
| Constructor | `IntPairIntMap(IntPairIntMap other)`                                            | —         | 既定値・エントリ・容量を引き継ぐ独立したコピーを構築する。                            |
| Method      | `getDefaultValue()`                                                             | `int`     | defaultValue を返す。                                                                 |
| Method      | `setDefaultValue(int defaultValue)`                                             | `void`    | defaultValue を変更する。既存のエントリは変更しない。                                 |
| Method      | `put(int a, int b, int value)`                                                  | `int`     | value を格納し、以前の値を返す。未存在なら value を挿入して defaultValue を返す。     |
| Method      | `get(int a, int b)`                                                             | `int`     | キー (a, b) の値を返す。未存在なら defaultValue を返し、挿入しない。                  |
| Method      | `getOrDefault(int a, int b, int defaultValue)`                                  | `int`     | キー (a, b) の値を返す。未存在なら引数の defaultValue を返す。                        |
| Method      | `increment(int a, int b)`                                                       | `int`     | 1を加えた値を格納して返す。未存在なら defaultValue + 1 を挿入する。                   |
| Method      | `decrement(int a, int b)`                                                       | `int`     | 1を引いた値を格納して返す。未存在なら defaultValue - 1 を挿入する。                   |
| Method      | `add(int a, int b, int delta)`                                                  | `int`     | delta を加えた値を格納して返す。未存在なら defaultValue + delta を挿入する。          |
| Method      | `addOrDefault(int a, int b, int delta, int absentValue)`                        | `int`     | 存在すれば delta を加え、未存在なら absentValue を挿入する。格納した値を返す。        |
| Method      | `remove(int a, int b)`                                                          | `int`     | キー (a, b) を削除し、削除した値を返す。未存在なら defaultValue を返す。              |
| Method      | `containsKey(int a, int b)`                                                     | `boolean` | キー (a, b) が存在するかを返す。                                                      |
| Method      | `merge(int a, int b, int value, IntBinaryOperator op)`                          | `int`     | 存在すれば op(既存値, value)、未存在なら value を格納し、格納した値を返す。           |
| Method      | `putIfAbsent(int a, int b, int value)`                                          | `int`     | 存在すれば何もせず既存値を返す。未存在なら value を挿入して defaultValue を返す。     |
| Method      | `computeIfAbsent(int a, int b, IntBinaryOperator op)`                           | `int`     | 未存在なら op(a, b) を挿入して返す。存在すれば op を呼ばずに既存値を返す。            |
| Method      | `mergeMin(int a, int b, int value)`                                             | `int`     | 存在すれば min(既存値, value)、未存在なら value を格納し、格納した値を返す。          |
| Method      | `mergeMax(int a, int b, int value)`                                             | `int`     | 存在すれば max(既存値, value)、未存在なら value を格納し、格納した値を返す。          |
| Method      | `clear()`                                                                       | `void`    | 全エントリを O(1) で削除する。容量は変えない。                                        |
| Method      | `size()`                                                                        | `int`     | 要素数を返す。                                                                        |
| Method      | `isEmpty()`                                                                     | `boolean` | 要素数が0かを返す。                                                                   |
| Method      | `forEach(IntTernaryConsumer action)`                                            | `void`    | 全エントリを (a, b, value) で走査する。                                               |
| Method      | `forEachKey(IntBinaryConsumer action)`                                          | `void`    | 全キーを (a, b) で走査する。                                                          |
| Method      | `forEachValue(IntConsumer action)`                                              | `void`    | 全値を走査する。                                                                      |
| Method      | `reduce(long identity, EntryToLongAccumulator accumulator)`                     | `long`    | identity から accumulator(累積値, a, b, value) を走査順に適用した結果を返す。         |
| Method      | `reduceKeys(long identity, KeysToLongAccumulator accumulator)`                  | `long`    | identity から accumulator(累積値, a, b) を走査順に適用した結果を返す。                |
| Method      | `reduceValues(long identity, LongBinaryOperator accumulator)`                   | `long`    | identity から accumulator(累積値, value) を走査順に適用した結果を返す。               |
| Method      | `keys()`                                                                        | `int[][]` | `[2][size]` 形式でキーを返す。res[j][i] は i 番目のキーの第 j 成分（0始まり）である。 |
| Method      | `values()`                                                                      | `int[]`   | 値を走査順に並べた配列を返す。                                                        |
| Method      | `entries()`                                                                     | `int[][]` | `[3][size]` 形式でエントリを返す。res[0..1] がキーの各成分、res[2] が値である。       |
| Interface   | `KeysToLongAccumulator.apply(long accumulator, int key1, int key2)`             | `long`    | キーの集約関数。reduceKeys が受け取る。                                               |
| Interface   | `EntryToLongAccumulator.apply(long accumulator, int key1, int key2, int value)` | `long`    | エントリの集約関数。reduce が受け取る。                                               |

### `IntPairLongMap`

[ソース](../../../src/lib/ds/map/IntPairLongMap.java)

`IntPairIntMap` の値を `long` に、値の関数型を `LongBinaryOperator`・`IntBinaryToLongFunction`・`IntBinaryLongConsumer`・`LongConsumer` に置き換えた API を持つ。委譲先は `LongLongMap` である。

| 種別        | シグネチャ                                                                       | 戻り値     | 説明                                                                                  |
|-------------|----------------------------------------------------------------------------------|------------|---------------------------------------------------------------------------------------|
| Constructor | `IntPairLongMap()`                                                               | —          | 想定要素数1024、defaultValue 0 で構築する。                                           |
| Constructor | `IntPairLongMap(int expectedSize)`                                               | —          | 想定要素数 expectedSize、defaultValue 0 で構築する。                                  |
| Constructor | `IntPairLongMap(int expectedSize, long defaultValue)`                            | —          | 想定要素数 expectedSize と defaultValue を指定して構築する。                          |
| Constructor | `IntPairLongMap(IntPairLongMap other)`                                           | —          | 既定値・エントリ・容量を引き継ぐ独立したコピーを構築する。                            |
| Method      | `getDefaultValue()`                                                              | `long`     | defaultValue を返す。                                                                 |
| Method      | `setDefaultValue(long defaultValue)`                                             | `void`     | defaultValue を変更する。既存のエントリは変更しない。                                 |
| Method      | `put(int a, int b, long value)`                                                  | `long`     | value を格納し、以前の値を返す。未存在なら value を挿入して defaultValue を返す。     |
| Method      | `get(int a, int b)`                                                              | `long`     | キー (a, b) の値を返す。未存在なら defaultValue を返し、挿入しない。                  |
| Method      | `getOrDefault(int a, int b, long defaultValue)`                                  | `long`     | キー (a, b) の値を返す。未存在なら引数の defaultValue を返す。                        |
| Method      | `increment(int a, int b)`                                                        | `long`     | 1を加えた値を格納して返す。未存在なら defaultValue + 1 を挿入する。                   |
| Method      | `decrement(int a, int b)`                                                        | `long`     | 1を引いた値を格納して返す。未存在なら defaultValue - 1 を挿入する。                   |
| Method      | `add(int a, int b, long delta)`                                                  | `long`     | delta を加えた値を格納して返す。未存在なら defaultValue + delta を挿入する。          |
| Method      | `addOrDefault(int a, int b, long delta, long absentValue)`                       | `long`     | 存在すれば delta を加え、未存在なら absentValue を挿入する。格納した値を返す。        |
| Method      | `remove(int a, int b)`                                                           | `long`     | キー (a, b) を削除し、削除した値を返す。未存在なら defaultValue を返す。              |
| Method      | `containsKey(int a, int b)`                                                      | `boolean`  | キー (a, b) が存在するかを返す。                                                      |
| Method      | `merge(int a, int b, long value, LongBinaryOperator op)`                         | `long`     | 存在すれば op(既存値, value)、未存在なら value を格納し、格納した値を返す。           |
| Method      | `putIfAbsent(int a, int b, long value)`                                          | `long`     | 存在すれば何もせず既存値を返す。未存在なら value を挿入して defaultValue を返す。     |
| Method      | `computeIfAbsent(int a, int b, IntBinaryToLongFunction op)`                      | `long`     | 未存在なら op(a, b) を挿入して返す。存在すれば op を呼ばずに既存値を返す。            |
| Method      | `mergeMin(int a, int b, long value)`                                             | `long`     | 存在すれば min(既存値, value)、未存在なら value を格納し、格納した値を返す。          |
| Method      | `mergeMax(int a, int b, long value)`                                             | `long`     | 存在すれば max(既存値, value)、未存在なら value を格納し、格納した値を返す。          |
| Method      | `clear()`                                                                        | `void`     | 全エントリを O(1) で削除する。容量は変えない。                                        |
| Method      | `size()`                                                                         | `int`      | 要素数を返す。                                                                        |
| Method      | `isEmpty()`                                                                      | `boolean`  | 要素数が0かを返す。                                                                   |
| Method      | `forEach(IntBinaryLongConsumer action)`                                          | `void`     | 全エントリを (a, b, value) で走査する。                                               |
| Method      | `forEachKey(IntBinaryConsumer action)`                                           | `void`     | 全キーを (a, b) で走査する。                                                          |
| Method      | `forEachValue(LongConsumer action)`                                              | `void`     | 全値を走査する。                                                                      |
| Method      | `reduce(long identity, EntryToLongAccumulator accumulator)`                      | `long`     | identity から accumulator(累積値, a, b, value) を走査順に適用した結果を返す。         |
| Method      | `reduceKeys(long identity, KeysToLongAccumulator accumulator)`                   | `long`     | identity から accumulator(累積値, a, b) を走査順に適用した結果を返す。                |
| Method      | `reduceValues(long identity, LongBinaryOperator accumulator)`                    | `long`     | identity から accumulator(累積値, value) を走査順に適用した結果を返す。               |
| Method      | `keys()`                                                                         | `int[][]`  | `[2][size]` 形式でキーを返す。res[j][i] は i 番目のキーの第 j 成分（0始まり）である。 |
| Method      | `values()`                                                                       | `long[]`   | 値を走査順に並べた配列を返す。                                                        |
| Method      | `entries()`                                                                      | `long[][]` | `[3][size]` 形式でエントリを返す。res[0..1] がキーの各成分、res[2] が値である。       |
| Interface   | `KeysToLongAccumulator.apply(long accumulator, int key1, int key2)`              | `long`     | キーの集約関数。reduceKeys が受け取る。                                               |
| Interface   | `EntryToLongAccumulator.apply(long accumulator, int key1, int key2, long value)` | `long`     | エントリの集約関数。reduce が受け取る。                                               |

## 利用例

準備中

## 制約・注意事項

- キーの成分の順序は区別する。(a, b) と (b, a) は別のキーである。
- `expectedSize`・`defaultValue`・戻り値・`computeIfAbsent` の再探索・走査順などの委譲先と共通の仕様は [整数型マップの制約・注意事項](./MapGuide.md#制約注意事項) に従う。

## 性能特性

| 操作              | 時間計算量   | 補足                                                                                                               |
|-------------------|--------------|--------------------------------------------------------------------------------------------------------------------|
| 全操作            | 委譲先と同じ | パックとアンパックの定数コストを加える。委譲先の計算量は [整数型マップの性能特性](./MapGuide.md#性能特性) にある。 |
| `computeIfAbsent` | 委譲先と同じ | キー成分を捕捉するラムダを呼び出しごとに生成する。                                                                 |
| `keys`・`entries` | O(c)         | 委譲先の内部配列を直接走査し、戻り値の配列だけを確保する。                                                         |

- **空間計算量**: O(c)。委譲先と同じである。

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                 |
|--------------------|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-27 | Pair 系2クラスを初回実装                                                                                                                                             |
| **バージョン 2.0** | 2026-05-10 | `reduce`・`reduceKeys`・`reduceValues` と対応する Accumulator を追加。その他の実装を調整                                                                             |
| **バージョン 3.0** | 2026-08-02 | 委譲先を `LongIntMap`・`LongLongMap` に変更し、未存在キーの `get` が0を返す仕様に対応                                                                                |
| **バージョン 4.0** | 2026-08-03 | コンストラクタと変更可能な `defaultValue` を追加し、抽出時の中間配列生成を削除                                                                                       |
| **バージョン 5.0** | 2026-08-08 | `computeIfAbsent`、`mergeMin`、`mergeMax` を追加。走査・通常変換は汎用 function 型、reduce は用途固有の Accumulator 型へ整理                                         |
| **バージョン 6.0** | 2026-10-28 | `put`・`putIfAbsent`・`remove` が操作前の値（未存在なら `defaultValue`）を返すよう変更し、`remove` の戻り値を `boolean` から値の型へ変更。コピーコンストラクタを追加 |
