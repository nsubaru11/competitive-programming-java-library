# Primitive Functional Interfaces

## 概要

`lib.util.function` は、標準ライブラリにないプリミティブ特化の汎用関数型インターフェースを提供する。
引数の個数は `Binary`、`Ternary`、`Quaternary` で表し、型の並びだけで用途が決まる走査・通常変換に使用する。

## クラス一覧

| クラス                     | 用途                            | Guide | ソース                                                                                   | 利用例 | テスト                                                                     |
|----------------------------|---------------------------------|-------|------------------------------------------------------------------------------------------|--------|----------------------------------------------------------------------------|
| `IntBinaryConsumer`        | `(int, int) -> void`            | —     | [IntBinaryConsumer](../../../src/lib/util/function/IntBinaryConsumer.java)               | —      | [Unit](../../../test/unit/util/function/IntBinaryConsumerTest.java)        |
| `IntTernaryConsumer`       | `(int, int, int) -> void`       | —     | [IntTernaryConsumer](../../../src/lib/util/function/IntTernaryConsumer.java)             | —      | [Unit](../../../test/unit/util/function/IntTernaryConsumerTest.java)       |
| `IntQuaternaryConsumer`    | `(int, int, int, int) -> void`  | —     | [IntQuaternaryConsumer](../../../src/lib/util/function/IntQuaternaryConsumer.java)       | —      | [Unit](../../../test/unit/util/function/IntQuaternaryConsumerTest.java)    |
| `IntLongConsumer`          | `(int, long) -> void`           | —     | [IntLongConsumer](../../../src/lib/util/function/IntLongConsumer.java)                   | —      | [Unit](../../../test/unit/util/function/IntLongConsumerTest.java)          |
| `LongIntConsumer`          | `(long, int) -> void`           | —     | [LongIntConsumer](../../../src/lib/util/function/LongIntConsumer.java)                   | —      | [Unit](../../../test/unit/util/function/LongIntConsumerTest.java)          |
| `LongBinaryConsumer`       | `(long, long) -> void`          | —     | [LongBinaryConsumer](../../../src/lib/util/function/LongBinaryConsumer.java)             | —      | [Unit](../../../test/unit/util/function/LongBinaryConsumerTest.java)       |
| `IntBinaryLongConsumer`    | `(int, int, long) -> void`      | —     | [IntBinaryLongConsumer](../../../src/lib/util/function/IntBinaryLongConsumer.java)       | —      | [Unit](../../../test/unit/util/function/IntBinaryLongConsumerTest.java)    |
| `IntTernaryLongConsumer`   | `(int, int, int, long) -> void` | —     | [IntTernaryLongConsumer](../../../src/lib/util/function/IntTernaryLongConsumer.java)     | —      | [Unit](../../../test/unit/util/function/IntTernaryLongConsumerTest.java)   |
| `IntBinaryToLongFunction`  | `(int, int) -> long`            | —     | [IntBinaryToLongFunction](../../../src/lib/util/function/IntBinaryToLongFunction.java)   | —      | [Unit](../../../test/unit/util/function/IntBinaryToLongFunctionTest.java)  |
| `IntTernaryOperator`       | `(int, int, int) -> int`        | —     | [IntTernaryOperator](../../../src/lib/util/function/IntTernaryOperator.java)             | —      | —                                                                          |
| `IntTernaryToLongFunction` | `(int, int, int) -> long`       | —     | [IntTernaryToLongFunction](../../../src/lib/util/function/IntTernaryToLongFunction.java) | —      | [Unit](../../../test/unit/util/function/IntTernaryToLongFunctionTest.java) |
| `IntComparator`            | `(int, int) -> int`             | —     | [IntComparator](../../../src/lib/util/function/IntComparator.java)                       | —      | [Unit](../../../test/unit/util/function/IntComparatorTest.java)            |
| `LongComparator`           | `(long, long) -> int`           | —     | [LongComparator](../../../src/lib/util/function/LongComparator.java)                     | —      | [Unit](../../../test/unit/util/function/LongComparatorTest.java)           |

## インターフェース

| インターフェース                                                                           | シグネチャ                      |
|--------------------------------------------------------------------------------------------|---------------------------------|
| [`IntBinaryConsumer`](../../../src/lib/util/function/IntBinaryConsumer.java)               | `(int, int) -> void`            |
| [`IntTernaryConsumer`](../../../src/lib/util/function/IntTernaryConsumer.java)             | `(int, int, int) -> void`       |
| [`IntQuaternaryConsumer`](../../../src/lib/util/function/IntQuaternaryConsumer.java)       | `(int, int, int, int) -> void`  |
| [`IntLongConsumer`](../../../src/lib/util/function/IntLongConsumer.java)                   | `(int, long) -> void`           |
| [`LongIntConsumer`](../../../src/lib/util/function/LongIntConsumer.java)                   | `(long, int) -> void`           |
| [`LongBinaryConsumer`](../../../src/lib/util/function/LongBinaryConsumer.java)             | `(long, long) -> void`          |
| [`IntBinaryLongConsumer`](../../../src/lib/util/function/IntBinaryLongConsumer.java)       | `(int, int, long) -> void`      |
| [`IntTernaryLongConsumer`](../../../src/lib/util/function/IntTernaryLongConsumer.java)     | `(int, int, int, long) -> void` |
| [`IntBinaryToLongFunction`](../../../src/lib/util/function/IntBinaryToLongFunction.java)   | `(int, int) -> long`            |
| [`IntTernaryOperator`](../../../src/lib/util/function/IntTernaryOperator.java)             | `(int, int, int) -> int`        |
| [`IntTernaryToLongFunction`](../../../src/lib/util/function/IntTernaryToLongFunction.java) | `(int, int, int) -> long`       |
| [`IntComparator`](../../../src/lib/util/function/IntComparator.java)                       | `(int, int) -> int`             |
| [`LongComparator`](../../../src/lib/util/function/LongComparator.java)                     | `(long, long) -> int`           |

## 命名方針

- 座標、辺、複合キーなど複数の用途でそのまま再利用できる callback は、シグネチャを表す構造的な名前にする。
- 第1引数が現在の累積値であるなど、呼び出し規約にドメイン上の意味がある callback は共通化しない。
- Map の `reduce` / `reduceKeys` では、`EntryToLongAccumulator` / `KeysToLongAccumulator` のような用途固有名を各 Map に定義する。

この区別により、単純な走査型の重複を避けつつ、累積値・キー・値の引数順を API 上で明確にする。
