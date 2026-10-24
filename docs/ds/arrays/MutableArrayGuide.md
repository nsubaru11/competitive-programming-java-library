# IntMutableArray / LongMutableArray 利用ガイド

> [`IntMutableArray`](../../../src/lib/ds/arrays/IntMutableArray.java) と [`LongMutableArray`](../../../src/lib/ds/arrays/LongMutableArray.java) は、`IntArray` / `LongArray` に添字更新を追加するインターフェースである。

[← README: Primitive Array Utilities](./README.md) · ソース: [IntMutableArray.java](../../../src/lib/ds/arrays/IntMutableArray.java) · [LongMutableArray.java](../../../src/lib/ds/arrays/LongMutableArray.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

[`IntMutableArray`](../../../src/lib/ds/arrays/IntMutableArray.java) と [`LongMutableArray`](../../../src/lib/ds/arrays/LongMutableArray.java) は、`IntArray` / `LongArray` に添字更新を追加するインターフェースである。

## 特徴

- 読み取り API と更新 API を型レベルで分離
- `set` は更新前の値を返すため、別途 `get` せず差分処理が可能
- `fill` と `setAll` を共通の既定メソッドとして提供
- `CircularArray` と `ArrayDeque` を同じ更新可能配列として扱える
- `int` / `long` のプリミティブ特化によりボクシングを回避

## 依存関係

- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntToLongFunction`

## API

### `IntMutableArray`

| シグネチャ                      | 戻り値 | 説明                                                                             |
|---------------------------------|--------|----------------------------------------------------------------------------------|
| `set(int i, int v)`             | `int`  | 指定位置を更新し、更新前の値を返す。                                             |
| `fill(int v)`                   | `void` | 全要素を指定値で更新する。要素数は変えない。                                     |
| `setAll(IntUnaryOperator init)` | `void` | 各論理添字に対して生成した値で全要素を更新する。init は添字の昇順に1回ずつ呼ぶ。 |

### `LongMutableArray`

| シグネチャ                       | 戻り値 | 説明                                                                             |
|----------------------------------|--------|----------------------------------------------------------------------------------|
| `set(int i, long v)`             | `long` | 指定位置を更新し、更新前の値を返す。                                             |
| `fill(long v)`                   | `void` | 全要素を指定値で更新する。要素数は変えない。                                     |
| `setAll(IntToLongFunction init)` | `void` | 各論理添字に対して生成した値で全要素を更新する。init は添字の昇順に1回ずつ呼ぶ。 |

読み取り・走査・変換の継承 API は [IntArray / LongArray 利用ガイド](./ArrayGuide.md) を参照する。

## 利用例

準備中

## 制約・注意事項

- `set` の有効な論理添字は `[0, size())` とする。範囲外の動作は実装に依存する。
- `setAll` の初期化関数内から対象配列を変更しないこと。値の書き込みは `setAll` が行う。具体クラスが持つ要素追加・削除・回転・一括更新も関数内では呼び出さない。
- 既定の `fill` / `setAll` は各添字へ `set` を呼ぶ。具体クラスは必要に応じて一括更新をオーバーライドする。
- `CircularArray` の添字は現在の回転状態を反映する。
- `ArrayDeque` の添字は `[0, size())` とする。詳細は [ArrayDequeGuide](./ArrayDequeGuide.md) を参照する。
- 配列長の変更操作はこのインターフェースには含まれない。

## 性能特性

`n` は要素数とする。`set` が `O(1)` の実装を前提とし、関数の評価時間は別途加算する。

- 現在の実装クラスでは `set` は $\mathcal{O} (1)$
- `fill`, `setAll`: $\mathcal{O} (n)$
- `set` 自体は追加メモリを使用しない

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                          |
|--------------------|------------|-------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-15 | `IntArray` / `LongArray` から更新操作を分離するインターフェースとして初回実装 |
| **バージョン 2.0** | 2026-07-15 | 全要素を更新する既定メソッド `fill` / `setAll` を追加                         |
