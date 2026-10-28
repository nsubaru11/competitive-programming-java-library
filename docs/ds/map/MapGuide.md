# 整数型マップ 利用ガイド

> int・long のキーと値に特化したオープンアドレス法のハッシュマップ `IntIntMap`・`IntLongMap`・`LongIntMap`・`LongLongMap` の仕様をまとめる。

[← README: HashMap](./README.md) · ソース: [IntIntMap.java](../../../src/lib/ds/map/IntIntMap.java) · [IntLongMap.java](../../../src/lib/ds/map/IntLongMap.java) · [LongIntMap.java](../../../src/lib/ds/map/LongIntMap.java) · [LongLongMap.java](../../../src/lib/ds/map/LongLongMap.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

線形探索のオープンアドレス法によるハッシュマップである。クラス名はキー型・値型の順に表す。
未存在キーの取得に使う既定値 `defaultValue` を持ち、`add`・`increment`・`decrement` は未存在キーを `defaultValue` から作成する。
要素数を n、内部配列の長さ（容量）を c とする。

## 特徴

- 削除は backward-shift deletion で行い、墓石を残さない。
- `clear` は世代番号を進めるだけで O(1) で行い、内部配列を再利用する。
- ハッシュは起動ごとに `ThreadLocalRandom` で生成した salt をキーへ XOR してから混合する。int キーは乗算2回、long キーは乗算1回のミキサーを使う。ミキサーの比較結果は [ハッシュ関数の検証](./hashFunc.md) にある。
- 容量は2の冪であり、要素数が容量の 3/4 に達した状態で新しいキーを挿入すると容量を2倍へ拡張する。
- `merge`・`mergeMin`・`mergeMax`・`putIfAbsent`・`computeIfAbsent`・`addOrDefault` は探索1回で参照と更新を行う。
- コピーコンストラクタで内部配列を共有しない複製を作れる。

### 型別クラス

| クラス                                                    | キー型 | 値型   | 選ぶ目安                                            |
|-----------------------------------------------------------|--------|--------|-----------------------------------------------------|
| [`IntIntMap`](../../../src/lib/ds/map/IntIntMap.java)     | `int`  | `int`  | int キー・int 値の管理                              |
| [`IntLongMap`](../../../src/lib/ds/map/IntLongMap.java)   | `int`  | `long` | キーは int で足り、値に long が必要な場合           |
| [`LongIntMap`](../../../src/lib/ds/map/LongIntMap.java)   | `long` | `int`  | long キーや、複合キーを自前で long へパックする場合 |
| [`LongLongMap`](../../../src/lib/ds/map/LongLongMap.java) | `long` | `long` | キー・値とも long が必要な場合                      |

## 依存関係

- Java標準ライブラリ: `java.util.concurrent.ThreadLocalRandom`、`java.util.function` の `IntBinaryOperator`・`LongBinaryOperator`・`IntUnaryOperator`・`IntToLongFunction`・`LongToIntFunction`・`LongUnaryOperator`・`IntConsumer`・`LongConsumer`
- `lib.util.function`: [IntBinaryConsumer](../../../src/lib/util/function/IntBinaryConsumer.java)、[IntLongConsumer](../../../src/lib/util/function/IntLongConsumer.java)、[LongIntConsumer](../../../src/lib/util/function/LongIntConsumer.java)、[LongBinaryConsumer](../../../src/lib/util/function/LongBinaryConsumer.java)

## API

### `IntIntMap`

[ソース](../../../src/lib/ds/map/IntIntMap.java)

| 種別        | シグネチャ                                                           | 戻り値    | 説明                                                                              |
|-------------|----------------------------------------------------------------------|-----------|-----------------------------------------------------------------------------------|
| Constructor | `IntIntMap()`                                                        | —         | 想定要素数1024、defaultValue 0 で構築する。                                       |
| Constructor | `IntIntMap(int expectedSize)`                                        | —         | 想定要素数 expectedSize、defaultValue 0 で構築する。                              |
| Constructor | `IntIntMap(int expectedSize, int defaultValue)`                      | —         | 想定要素数 expectedSize と defaultValue を指定して構築する。                      |
| Constructor | `IntIntMap(IntIntMap other)`                                         | —         | 既定値・エントリ・容量を引き継ぐ独立したコピーを構築する。                        |
| Method      | `getDefaultValue()`                                                  | `int`     | defaultValue を返す。                                                             |
| Method      | `setDefaultValue(int defaultValue)`                                  | `void`    | defaultValue を変更する。既存のエントリは変更しない。                             |
| Method      | `get(int key)`                                                       | `int`     | キーの値を返す。未存在なら defaultValue を返し、挿入しない。                      |
| Method      | `getOrDefault(int key, int defaultValue)`                            | `int`     | キーの値を返す。未存在なら引数の defaultValue を返す。                            |
| Method      | `increment(int key)`                                                 | `int`     | 1を加えた値を格納して返す。未存在なら defaultValue + 1 を挿入する。               |
| Method      | `decrement(int key)`                                                 | `int`     | 1を引いた値を格納して返す。未存在なら defaultValue - 1 を挿入する。               |
| Method      | `add(int key, int delta)`                                            | `int`     | delta を加えた値を格納して返す。未存在なら defaultValue + delta を挿入する。      |
| Method      | `addOrDefault(int key, int delta, int absentValue)`                  | `int`     | 存在すれば delta を加え、未存在なら absentValue を挿入する。格納した値を返す。    |
| Method      | `put(int key, int value)`                                            | `int`     | value を格納し、以前の値を返す。未存在なら value を挿入して defaultValue を返す。 |
| Method      | `remove(int key)`                                                    | `int`     | キーを削除し、削除した値を返す。未存在なら defaultValue を返す。                  |
| Method      | `containsKey(int key)`                                               | `boolean` | キーが存在するかを返す。                                                          |
| Method      | `merge(int key, int value, IntBinaryOperator op)`                    | `int`     | 存在すれば op(既存値, value)、未存在なら value を格納し、格納した値を返す。       |
| Method      | `putIfAbsent(int key, int value)`                                    | `int`     | 存在すれば何もせず既存値を返す。未存在なら value を挿入して defaultValue を返す。 |
| Method      | `computeIfAbsent(int key, IntUnaryOperator op)`                      | `int`     | 未存在なら op(key) を挿入して返す。存在すれば op を呼ばずに既存値を返す。         |
| Method      | `mergeMin(int key, int value)`                                       | `int`     | 存在すれば min(既存値, value)、未存在なら value を格納し、格納した値を返す。      |
| Method      | `mergeMax(int key, int value)`                                       | `int`     | 存在すれば max(既存値, value)、未存在なら value を格納し、格納した値を返す。      |
| Method      | `clear()`                                                            | `void`    | 全エントリを O(1) で削除する。容量は変えない。                                    |
| Method      | `size()`                                                             | `int`     | 要素数を返す。                                                                    |
| Method      | `isEmpty()`                                                          | `boolean` | 要素数が0かを返す。                                                               |
| Method      | `forEach(IntBinaryConsumer action)`                                  | `void`    | 全エントリを (key, value) で走査する。                                            |
| Method      | `forEachKey(IntConsumer action)`                                     | `void`    | 全キーを走査する。                                                                |
| Method      | `forEachValue(IntConsumer action)`                                   | `void`    | 全値を走査する。                                                                  |
| Method      | `reduce(long identity, EntryToLongAccumulator accumulator)`          | `long`    | identity から accumulator(累積値, key, value) を走査順に適用した結果を返す。      |
| Method      | `reduceKeys(long identity, LongBinaryOperator accumulator)`          | `long`    | identity から accumulator(累積値, key) を走査順に適用した結果を返す。             |
| Method      | `reduceValues(long identity, LongBinaryOperator accumulator)`        | `long`    | identity から accumulator(累積値, value) を走査順に適用した結果を返す。           |
| Method      | `keys()`                                                             | `int[]`   | キーを走査順に並べた配列を返す。                                                  |
| Method      | `values()`                                                           | `int[]`   | 値を走査順に並べた配列を返す。                                                    |
| Method      | `entries()`                                                          | `int[][]` | `[2][size]` 形式でエントリを返す。res[0] がキー、res[1] が値である。              |
| Interface   | `EntryToLongAccumulator.apply(long accumulator, int key, int value)` | `long`    | エントリの集約関数。reduce が受け取る。                                           |

### `IntLongMap`

[ソース](../../../src/lib/ds/map/IntLongMap.java)

`IntIntMap` の値を `long` に、値の関数型を `LongBinaryOperator`・`IntToLongFunction`・`IntLongConsumer`・`LongConsumer` に置き換えた API を持つ。`entries` はキーを `long` へ拡張する。

| 種別        | シグネチャ                                                            | 戻り値     | 説明                                                                              |
|-------------|-----------------------------------------------------------------------|------------|-----------------------------------------------------------------------------------|
| Constructor | `IntLongMap()`                                                        | —          | 想定要素数1024、defaultValue 0 で構築する。                                       |
| Constructor | `IntLongMap(int expectedSize)`                                        | —          | 想定要素数 expectedSize、defaultValue 0 で構築する。                              |
| Constructor | `IntLongMap(int expectedSize, long defaultValue)`                     | —          | 想定要素数 expectedSize と defaultValue を指定して構築する。                      |
| Constructor | `IntLongMap(IntLongMap other)`                                        | —          | 既定値・エントリ・容量を引き継ぐ独立したコピーを構築する。                        |
| Method      | `getDefaultValue()`                                                   | `long`     | defaultValue を返す。                                                             |
| Method      | `setDefaultValue(long defaultValue)`                                  | `void`     | defaultValue を変更する。既存のエントリは変更しない。                             |
| Method      | `get(int key)`                                                        | `long`     | キーの値を返す。未存在なら defaultValue を返し、挿入しない。                      |
| Method      | `getOrDefault(int key, long defaultValue)`                            | `long`     | キーの値を返す。未存在なら引数の defaultValue を返す。                            |
| Method      | `increment(int key)`                                                  | `long`     | 1を加えた値を格納して返す。未存在なら defaultValue + 1 を挿入する。               |
| Method      | `decrement(int key)`                                                  | `long`     | 1を引いた値を格納して返す。未存在なら defaultValue - 1 を挿入する。               |
| Method      | `add(int key, long delta)`                                            | `long`     | delta を加えた値を格納して返す。未存在なら defaultValue + delta を挿入する。      |
| Method      | `addOrDefault(int key, long delta, long absentValue)`                 | `long`     | 存在すれば delta を加え、未存在なら absentValue を挿入する。格納した値を返す。    |
| Method      | `put(int key, long value)`                                            | `long`     | value を格納し、以前の値を返す。未存在なら value を挿入して defaultValue を返す。 |
| Method      | `remove(int key)`                                                     | `long`     | キーを削除し、削除した値を返す。未存在なら defaultValue を返す。                  |
| Method      | `containsKey(int key)`                                                | `boolean`  | キーが存在するかを返す。                                                          |
| Method      | `merge(int key, long value, LongBinaryOperator op)`                   | `long`     | 存在すれば op(既存値, value)、未存在なら value を格納し、格納した値を返す。       |
| Method      | `putIfAbsent(int key, long value)`                                    | `long`     | 存在すれば何もせず既存値を返す。未存在なら value を挿入して defaultValue を返す。 |
| Method      | `computeIfAbsent(int key, IntToLongFunction op)`                      | `long`     | 未存在なら op(key) を挿入して返す。存在すれば op を呼ばずに既存値を返す。         |
| Method      | `mergeMin(int key, long value)`                                       | `long`     | 存在すれば min(既存値, value)、未存在なら value を格納し、格納した値を返す。      |
| Method      | `mergeMax(int key, long value)`                                       | `long`     | 存在すれば max(既存値, value)、未存在なら value を格納し、格納した値を返す。      |
| Method      | `clear()`                                                             | `void`     | 全エントリを O(1) で削除する。容量は変えない。                                    |
| Method      | `size()`                                                              | `int`      | 要素数を返す。                                                                    |
| Method      | `isEmpty()`                                                           | `boolean`  | 要素数が0かを返す。                                                               |
| Method      | `forEach(IntLongConsumer action)`                                     | `void`     | 全エントリを (key, value) で走査する。                                            |
| Method      | `forEachKey(IntConsumer action)`                                      | `void`     | 全キーを走査する。                                                                |
| Method      | `forEachValue(LongConsumer action)`                                   | `void`     | 全値を走査する。                                                                  |
| Method      | `reduce(long identity, EntryToLongAccumulator accumulator)`           | `long`     | identity から accumulator(累積値, key, value) を走査順に適用した結果を返す。      |
| Method      | `reduceKeys(long identity, LongBinaryOperator accumulator)`           | `long`     | identity から accumulator(累積値, key) を走査順に適用した結果を返す。             |
| Method      | `reduceValues(long identity, LongBinaryOperator accumulator)`         | `long`     | identity から accumulator(累積値, value) を走査順に適用した結果を返す。           |
| Method      | `keys()`                                                              | `int[]`    | キーを走査順に並べた配列を返す。                                                  |
| Method      | `values()`                                                            | `long[]`   | 値を走査順に並べた配列を返す。                                                    |
| Method      | `entries()`                                                           | `long[][]` | `[2][size]` 形式でエントリを返す。res[0] がキー、res[1] が値である。              |
| Interface   | `EntryToLongAccumulator.apply(long accumulator, int key, long value)` | `long`     | エントリの集約関数。reduce が受け取る。                                           |

### `LongIntMap`

[ソース](../../../src/lib/ds/map/LongIntMap.java)

`IntIntMap` のキーを `long` に、キーの関数型を `LongToIntFunction`・`LongIntConsumer`・`LongConsumer` に置き換えた API を持つ。`entries` は値を `long` へ拡張する。

| 種別        | シグネチャ                                                            | 戻り値     | 説明                                                                              |
|-------------|-----------------------------------------------------------------------|------------|-----------------------------------------------------------------------------------|
| Constructor | `LongIntMap()`                                                        | —          | 想定要素数1024、defaultValue 0 で構築する。                                       |
| Constructor | `LongIntMap(int expectedSize)`                                        | —          | 想定要素数 expectedSize、defaultValue 0 で構築する。                              |
| Constructor | `LongIntMap(int expectedSize, int defaultValue)`                      | —          | 想定要素数 expectedSize と defaultValue を指定して構築する。                      |
| Constructor | `LongIntMap(LongIntMap other)`                                        | —          | 既定値・エントリ・容量を引き継ぐ独立したコピーを構築する。                        |
| Method      | `getDefaultValue()`                                                   | `int`      | defaultValue を返す。                                                             |
| Method      | `setDefaultValue(int defaultValue)`                                   | `void`     | defaultValue を変更する。既存のエントリは変更しない。                             |
| Method      | `get(long key)`                                                       | `int`      | キーの値を返す。未存在なら defaultValue を返し、挿入しない。                      |
| Method      | `getOrDefault(long key, int defaultValue)`                            | `int`      | キーの値を返す。未存在なら引数の defaultValue を返す。                            |
| Method      | `increment(long key)`                                                 | `int`      | 1を加えた値を格納して返す。未存在なら defaultValue + 1 を挿入する。               |
| Method      | `decrement(long key)`                                                 | `int`      | 1を引いた値を格納して返す。未存在なら defaultValue - 1 を挿入する。               |
| Method      | `add(long key, int delta)`                                            | `int`      | delta を加えた値を格納して返す。未存在なら defaultValue + delta を挿入する。      |
| Method      | `addOrDefault(long key, int delta, int absentValue)`                  | `int`      | 存在すれば delta を加え、未存在なら absentValue を挿入する。格納した値を返す。    |
| Method      | `put(long key, int value)`                                            | `int`      | value を格納し、以前の値を返す。未存在なら value を挿入して defaultValue を返す。 |
| Method      | `remove(long key)`                                                    | `int`      | キーを削除し、削除した値を返す。未存在なら defaultValue を返す。                  |
| Method      | `containsKey(long key)`                                               | `boolean`  | キーが存在するかを返す。                                                          |
| Method      | `merge(long key, int value, IntBinaryOperator op)`                    | `int`      | 存在すれば op(既存値, value)、未存在なら value を格納し、格納した値を返す。       |
| Method      | `putIfAbsent(long key, int value)`                                    | `int`      | 存在すれば何もせず既存値を返す。未存在なら value を挿入して defaultValue を返す。 |
| Method      | `computeIfAbsent(long key, LongToIntFunction op)`                     | `int`      | 未存在なら op(key) を挿入して返す。存在すれば op を呼ばずに既存値を返す。         |
| Method      | `mergeMin(long key, int value)`                                       | `int`      | 存在すれば min(既存値, value)、未存在なら value を格納し、格納した値を返す。      |
| Method      | `mergeMax(long key, int value)`                                       | `int`      | 存在すれば max(既存値, value)、未存在なら value を格納し、格納した値を返す。      |
| Method      | `clear()`                                                             | `void`     | 全エントリを O(1) で削除する。容量は変えない。                                    |
| Method      | `size()`                                                              | `int`      | 要素数を返す。                                                                    |
| Method      | `isEmpty()`                                                           | `boolean`  | 要素数が0かを返す。                                                               |
| Method      | `forEach(LongIntConsumer action)`                                     | `void`     | 全エントリを (key, value) で走査する。                                            |
| Method      | `forEachKey(LongConsumer action)`                                     | `void`     | 全キーを走査する。                                                                |
| Method      | `forEachValue(IntConsumer action)`                                    | `void`     | 全値を走査する。                                                                  |
| Method      | `reduce(long identity, EntryToLongAccumulator accumulator)`           | `long`     | identity から accumulator(累積値, key, value) を走査順に適用した結果を返す。      |
| Method      | `reduceKeys(long identity, LongBinaryOperator accumulator)`           | `long`     | identity から accumulator(累積値, key) を走査順に適用した結果を返す。             |
| Method      | `reduceValues(long identity, LongBinaryOperator accumulator)`         | `long`     | identity から accumulator(累積値, value) を走査順に適用した結果を返す。           |
| Method      | `keys()`                                                              | `long[]`   | キーを走査順に並べた配列を返す。                                                  |
| Method      | `values()`                                                            | `int[]`    | 値を走査順に並べた配列を返す。                                                    |
| Method      | `entries()`                                                           | `long[][]` | `[2][size]` 形式でエントリを返す。res[0] がキー、res[1] が値である。              |
| Interface   | `EntryToLongAccumulator.apply(long accumulator, long key, int value)` | `long`     | エントリの集約関数。reduce が受け取る。                                           |

### `LongLongMap`

[ソース](../../../src/lib/ds/map/LongLongMap.java)

`IntIntMap` のキーと値を `long` に置き換えた API を持つ。

| 種別        | シグネチャ                                                             | 戻り値     | 説明                                                                              |
|-------------|------------------------------------------------------------------------|------------|-----------------------------------------------------------------------------------|
| Constructor | `LongLongMap()`                                                        | —          | 想定要素数1024、defaultValue 0 で構築する。                                       |
| Constructor | `LongLongMap(int expectedSize)`                                        | —          | 想定要素数 expectedSize、defaultValue 0 で構築する。                              |
| Constructor | `LongLongMap(int expectedSize, long defaultValue)`                     | —          | 想定要素数 expectedSize と defaultValue を指定して構築する。                      |
| Constructor | `LongLongMap(LongLongMap other)`                                       | —          | 既定値・エントリ・容量を引き継ぐ独立したコピーを構築する。                        |
| Method      | `getDefaultValue()`                                                    | `long`     | defaultValue を返す。                                                             |
| Method      | `setDefaultValue(long defaultValue)`                                   | `void`     | defaultValue を変更する。既存のエントリは変更しない。                             |
| Method      | `get(long key)`                                                        | `long`     | キーの値を返す。未存在なら defaultValue を返し、挿入しない。                      |
| Method      | `getOrDefault(long key, long defaultValue)`                            | `long`     | キーの値を返す。未存在なら引数の defaultValue を返す。                            |
| Method      | `increment(long key)`                                                  | `long`     | 1を加えた値を格納して返す。未存在なら defaultValue + 1 を挿入する。               |
| Method      | `decrement(long key)`                                                  | `long`     | 1を引いた値を格納して返す。未存在なら defaultValue - 1 を挿入する。               |
| Method      | `add(long key, long delta)`                                            | `long`     | delta を加えた値を格納して返す。未存在なら defaultValue + delta を挿入する。      |
| Method      | `addOrDefault(long key, long delta, long absentValue)`                 | `long`     | 存在すれば delta を加え、未存在なら absentValue を挿入する。格納した値を返す。    |
| Method      | `put(long key, long value)`                                            | `long`     | value を格納し、以前の値を返す。未存在なら value を挿入して defaultValue を返す。 |
| Method      | `remove(long key)`                                                     | `long`     | キーを削除し、削除した値を返す。未存在なら defaultValue を返す。                  |
| Method      | `containsKey(long key)`                                                | `boolean`  | キーが存在するかを返す。                                                          |
| Method      | `merge(long key, long value, LongBinaryOperator op)`                   | `long`     | 存在すれば op(既存値, value)、未存在なら value を格納し、格納した値を返す。       |
| Method      | `putIfAbsent(long key, long value)`                                    | `long`     | 存在すれば何もせず既存値を返す。未存在なら value を挿入して defaultValue を返す。 |
| Method      | `computeIfAbsent(long key, LongUnaryOperator op)`                      | `long`     | 未存在なら op(key) を挿入して返す。存在すれば op を呼ばずに既存値を返す。         |
| Method      | `mergeMin(long key, long value)`                                       | `long`     | 存在すれば min(既存値, value)、未存在なら value を格納し、格納した値を返す。      |
| Method      | `mergeMax(long key, long value)`                                       | `long`     | 存在すれば max(既存値, value)、未存在なら value を格納し、格納した値を返す。      |
| Method      | `clear()`                                                              | `void`     | 全エントリを O(1) で削除する。容量は変えない。                                    |
| Method      | `size()`                                                               | `int`      | 要素数を返す。                                                                    |
| Method      | `isEmpty()`                                                            | `boolean`  | 要素数が0かを返す。                                                               |
| Method      | `forEach(LongBinaryConsumer action)`                                   | `void`     | 全エントリを (key, value) で走査する。                                            |
| Method      | `forEachKey(LongConsumer action)`                                      | `void`     | 全キーを走査する。                                                                |
| Method      | `forEachValue(LongConsumer action)`                                    | `void`     | 全値を走査する。                                                                  |
| Method      | `reduce(long identity, EntryToLongAccumulator accumulator)`            | `long`     | identity から accumulator(累積値, key, value) を走査順に適用した結果を返す。      |
| Method      | `reduceKeys(long identity, LongBinaryOperator accumulator)`            | `long`     | identity から accumulator(累積値, key) を走査順に適用した結果を返す。             |
| Method      | `reduceValues(long identity, LongBinaryOperator accumulator)`          | `long`     | identity から accumulator(累積値, value) を走査順に適用した結果を返す。           |
| Method      | `keys()`                                                               | `long[]`   | キーを走査順に並べた配列を返す。                                                  |
| Method      | `values()`                                                             | `long[]`   | 値を走査順に並べた配列を返す。                                                    |
| Method      | `entries()`                                                            | `long[][]` | `[2][size]` 形式でエントリを返す。res[0] がキー、res[1] が値である。              |
| Interface   | `EntryToLongAccumulator.apply(long accumulator, long key, long value)` | `long`     | エントリの集約関数。reduce が受け取る。                                           |

## 利用例

準備中

## 制約・注意事項

- `expectedSize` は内部配列長ではなく、リサイズせずに保持できる要素数の目安である。容量は `ceil(4 * expectedSize / 3)` 以上の2の冪で、16未満なら16とする。`expectedSize` が0以下でも容量16で構築する。
- 未存在キーの `get` は `defaultValue` を返し、キーを挿入しない。`getOrDefault` の引数は呼び出し単位の既定値であり、設定済みの `defaultValue` を変更しない。
- `setDefaultValue` は既存エントリの値を変更せず、以後の未存在キーの `get` と `add`・`increment`・`decrement` の初期値に適用される。
- 未存在キーを `defaultValue` 以外の値から加算する場合は `addOrDefault` を使う。
- `put`・`putIfAbsent`・`remove` は `java.util.Map` と同様に操作前の値を返す。キーが存在しなかった場合は null の代わりに `defaultValue` を返すため、格納値が `defaultValue` と等しいエントリと未存在キーを戻り値では区別できない。区別には `containsKey` を使う。
- `put`・`putIfAbsent`・`remove` 以外の更新系メソッドは操作後にキーへ対応する値を返す。
- `computeIfAbsent` の `op` が同じマップへ挿入・削除・`clear` を行った場合は、`op` の適用後にキーを再探索し、キーが存在すれば既存値を返して `value` を格納しない。
- 加算はオーバーフロー時に値の型の範囲で巡回する。
- 走査（`forEach`・`forEachKey`・`forEachValue`・`reduce`・`reduceKeys`・`reduceValues`）と `keys`・`values`・`entries` は内部配列の順に行い、挿入順ではない。同じ状態のマップでは全メソッドが同じ順に列挙する。走査中にマップを変更しない。
- `remove`・`clear` は容量を縮小しない。走査と抽出は容量に比例する時間がかかる。
- `reduceKeys`・`reduceValues` は int のキー・値を long へ拡張して `accumulator` へ渡す。

## 性能特性

| 操作                                                    | 時間計算量           | 補足                                                                                   |
|---------------------------------------------------------|----------------------|----------------------------------------------------------------------------------------|
| 構築                                                    | O(c)                 | 内部配列を確保する。                                                                   |
| コピーコンストラクタ                                    | O(c)                 | 内部配列を複製する。                                                                   |
| `get`・`getOrDefault`・`containsKey`                    | 平均 O(1)、最悪 O(n) | 線形探索のクラスタ長に比例する。                                                       |
| 挿入・更新系メソッド                                    | 平均 O(1)、最悪 O(n) | 新しいキーの挿入で拡張する場合は O(c) の再配置を伴う。挿入の連続では償却 O(1) である。 |
| `remove`                                                | 平均 O(1)、最悪 O(n) | backward-shift deletion で後続のクラスタ要素を詰める。                                 |
| `clear`                                                 | O(1)                 | 世代番号を進めるだけで内部配列を再利用する。                                           |
| `size`・`isEmpty`・`getDefaultValue`・`setDefaultValue` | O(1)                 | —                                                                                      |
| 走査・`reduce` 系・`keys`・`values`・`entries`          | O(c)                 | `keys`・`values`・`entries` は戻り値の配列だけを確保する。                             |

- **空間計算量**: O(c)。新しいキーの挿入で容量を拡張した直後の c は n の約 8/3 倍である。

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                                                                                                                                                                                                  |
|--------------------|------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-27 | 整数型マップ3クラスを初回実装                                                                                                                                                                                                                                                                                                                         |
| **バージョン 2.0** | 2026-05-10 | `forEach`・`forEachKey`・`forEachValue` の引数修飾子や配列生成まわりを調整                                                                                                                                                                                                                                                                            |
| **バージョン 3.0** | 2026-08-02 | クラス名を変更し、`defaultValue`、未存在キーの `get` の既定値返却、backward-shift deletion を追加                                                                                                                                                                                                                                                     |
| **バージョン 3.1** | 2026-08-03 | Pair・Triple Map が直接抽出できるよう内部フィールドの可視性を調整                                                                                                                                                                                                                                                                                     |
| **バージョン 4.0** | 2026-08-08 | `IntLongMap`、`computeIfAbsent`、`mergeMin`、`mergeMax` を追加し、走査の callback を汎用 function 型へ統一。reduce は用途固有の Accumulator 名を維持                                                                                                                                                                                                  |
| **バージョン 5.0** | 2026-10-28 | 全クラス: `put`・`putIfAbsent`・`remove` が操作前の値（未存在なら `defaultValue`）を返すよう変更し、`remove` の戻り値を `boolean` から値の型へ変更。コピーコンストラクタを追加。既定の想定要素数を定数化し、`remove` の冗長な代入を除去。`IntIntMap`・`IntLongMap` の内部配列を private にし、Long 版だけを Pair・Triple Map 向けにパッケージ内へ公開 |
