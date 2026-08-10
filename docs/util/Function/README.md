# Primitive Functional Interfaces

## 概要

`lib.util.function` は、標準ライブラリにないプリミティブ特化の汎用関数型インターフェースを提供します。
引数の個数は `Binary`、`Ternary`、`Quaternary` で表し、型の並びだけで用途が決まる走査・通常変換に使用します。

## インターフェース

| インターフェース           | シグネチャ                      |
|----------------------------|---------------------------------|
| `IntBinaryConsumer`        | `(int, int) -> void`            |
| `IntTernaryConsumer`       | `(int, int, int) -> void`       |
| `IntQuaternaryConsumer`    | `(int, int, int, int) -> void`  |
| `IntLongConsumer`          | `(int, long) -> void`           |
| `LongIntConsumer`          | `(long, int) -> void`           |
| `LongBinaryConsumer`       | `(long, long) -> void`          |
| `IntBinaryLongConsumer`    | `(int, int, long) -> void`      |
| `IntTernaryLongConsumer`   | `(int, int, int, long) -> void` |
| `IntBinaryToLongFunction`  | `(int, int) -> long`            |
| `IntTernaryOperator`       | `(int, int, int) -> int`        |
| `IntTernaryToLongFunction` | `(int, int, int) -> long`       |
| `IntComparator`            | `(int, int) -> int`             |
| `LongComparator`           | `(long, long) -> int`           |

## 命名方針

- 座標、辺、複合キーなど複数の用途でそのまま再利用できる callback は、シグネチャを表す構造的な名前にします。
- 第1引数が現在の累積値であるなど、呼び出し規約にドメイン上の意味がある callback は共通化しません。
- Map の `reduce` / `reduceKeys` では、`EntryToLongAccumulator` / `KeysToLongAccumulator` のような用途固有名を各 Map に定義します。

この区別により、単純な走査型の重複を避けつつ、累積値・キー・値の引数順を API 上で明確にします。
