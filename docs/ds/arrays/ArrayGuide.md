# IntArray / LongArray 利用ガイド

> [`IntArray`](../../../src/lib/ds/arrays/IntArray.java) と [`LongArray`](../../../src/lib/ds/arrays/LongArray.java) は、添字アクセス可能なプリミティブ配列の読み取り用インターフェースである。

[← README: Primitive Array Utilities](./README.md) · ソース: [IntArray.java](../../../src/lib/ds/arrays/IntArray.java) · [LongArray.java](../../../src/lib/ds/arrays/LongArray.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

アルゴリズム側が具体的な格納形式に依存せず、複数の配列実装を同じ API で扱うために使用する。

## 特徴

- `int` / `long` をプリミティブのまま取得できる
- `getFirst` / `getLast` による先頭・末尾取得と、範囲外・空の場合に指定値を返す取得に対応
- `indexOf` / `lastIndexOf` で指定値の論理添字を線形探索
- `PrimitiveIterator` とプリミティブ Stream に対応
- `ORDERED`, `SIZED`, `SUBSIZED` の特性を持つ `Spliterator` を提供
- `PrefixSum`, `CircularArray`, `ArrayDeque`, `CompressedArray` の共通引数として利用可能
- 読み取り専用 API のため、アルゴリズムが不要な更新操作へ依存しない

## 依存関係

- `java.util.NoSuchElementException`
- `java.util.PrimitiveIterator`
- `java.util.Spliterator`
- `java.util.Spliterators`
- [`lib.ds.IntCollection`](../../../src/lib/ds/IntCollection.java)
- [`lib.ds.LongCollection`](../../../src/lib/ds/LongCollection.java)

## API

### `IntArray`

| シグネチャ                              | 戻り値              | 説明                                                                       |
|-----------------------------------------|---------------------|----------------------------------------------------------------------------|
| `get(int i)`                            | `int`               | 指定位置の値を返す。範囲外の添字に対する動作は実装に依存する。             |
| `getOrDefault(int i, int defaultValue)` | `int`               | 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。           |
| `getFirst()`                            | `int`               | 現在の論理順の先頭の値を返す。空の場合は NoSuchElementException を投げる。 |
| `getFirstOrDefault(int defaultValue)`   | `int`               | 先頭の値を返す。空の場合は defaultValue を返す。                           |
| `getLast()`                             | `int`               | 現在の論理順の末尾の値を返す。空の場合は NoSuchElementException を投げる。 |
| `getLastOrDefault(int defaultValue)`    | `int`               | 現在の論理順の末尾の値を返す。空の場合は defaultValue をそのまま返す。     |
| `indexOf(int v)`                        | `int`               | 指定値の最初の論理添字を返す。存在しなければ -1。                          |
| `lastIndexOf(int v)`                    | `int`               | 指定値の最後の論理添字を返す。存在しなければ -1。                          |
| `spliterator()`                         | `Spliterator.OfInt` | 論理添字の昇順に走査する ORDERED・SIZED・SUBSIZED の spliterator を返す。  |

### `LongArray`

| シグネチャ                               | 戻り値               | 説明                                                                       |
|------------------------------------------|----------------------|----------------------------------------------------------------------------|
| `get(int i)`                             | `long`               | 指定位置の値を返す。範囲外の添字に対する動作は実装に依存する。             |
| `getOrDefault(int i, long defaultValue)` | `long`               | 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。           |
| `getFirst()`                             | `long`               | 現在の論理順の先頭の値を返す。空の場合は NoSuchElementException を投げる。 |
| `getFirstOrDefault(long defaultValue)`   | `long`               | 先頭の値を返す。空の場合は defaultValue を返す。                           |
| `getLast()`                              | `long`               | 現在の論理順の末尾の値を返す。空の場合は NoSuchElementException を投げる。 |
| `getLastOrDefault(long defaultValue)`    | `long`               | 現在の論理順の末尾の値を返す。空の場合は defaultValue をそのまま返す。     |
| `indexOf(long v)`                        | `int`                | 指定値の最初の論理添字を返す。存在しなければ -1。                          |
| `lastIndexOf(long v)`                    | `int`                | 指定値の最後の論理添字を返す。存在しなければ -1。                          |
| `spliterator()`                          | `Spliterator.OfLong` | 論理添字の昇順に走査する ORDERED・SIZED・SUBSIZED の spliterator を返す。  |

### 継承元のコレクション API

`IntCollection` / `LongCollection` 自体は添字を持たず、走査順は実装に依存する。配列インターフェースでは論理添字の昇順とする。

### `IntCollection`

| シグネチャ                       | 戻り値                    | 説明                                                                     |
|----------------------------------|---------------------------|--------------------------------------------------------------------------|
| `size()`                         | `int`                     | 要素数を返す。                                                           |
| `iterator()`                     | `PrimitiveIterator.OfInt` | プリミティブ値を走査する iterator を返す。走査順は実装に依存する。       |
| `isEmpty()`                      | `boolean`                 | 要素数が0かを返す。                                                      |
| `contains(int v)`                | `boolean`                 | 指定値を含むかを線形探索で判定する。                                     |
| `forEachInt(IntConsumer action)` | `void`                    | iterator の走査順で各要素に action を適用する。                          |
| `spliterator()`                  | `Spliterator.OfInt`       | SIZED・SUBSIZED の spliterator を返す。既定実装は ORDERED を指定しない。 |
| `intStream()`                    | `IntStream`               | 逐次実行のプリミティブ Stream を返す。                                   |
| `toList()`                       | `List<Integer>`           | iterator の走査順にボックス化した、変更不可の List を返す。              |
| `toArray()`                      | `int[]`                   | iterator の走査順で独立したプリミティブ配列へコピーする。                |

### `LongCollection`

| シグネチャ                         | 戻り値                     | 説明                                                                     |
|------------------------------------|----------------------------|--------------------------------------------------------------------------|
| `size()`                           | `int`                      | 要素数を返す。                                                           |
| `iterator()`                       | `PrimitiveIterator.OfLong` | プリミティブ値を走査する iterator を返す。走査順は実装に依存する。       |
| `isEmpty()`                        | `boolean`                  | 要素数が0かを返す。                                                      |
| `contains(long v)`                 | `boolean`                  | 指定値を含むかを線形探索で判定する。                                     |
| `forEachLong(LongConsumer action)` | `void`                     | iterator の走査順で各要素に action を適用する。                          |
| `spliterator()`                    | `Spliterator.OfLong`       | SIZED・SUBSIZED の spliterator を返す。既定実装は ORDERED を指定しない。 |
| `longStream()`                     | `LongStream`               | 逐次実行のプリミティブ Stream を返す。                                   |
| `toList()`                         | `List<Long>`               | iterator の走査順にボックス化した、変更不可の List を返す。              |
| `toArray()`                        | `long[]`                   | iterator の走査順で独立したプリミティブ配列へコピーする。                |

両コレクションは `Iterable` の `forEach(Consumer<? super Integer> action)` / `forEach(Consumer<? super Long> action)` も継承する。これはボックス化を伴う。

## 利用例

準備中

## 制約・注意事項

- `get(i)` は `0 <= i < size()` の有効な論理添字で呼び出す。範囲外の動作は実装に依存する。
- `getOrDefault` は範囲外で指定値を返す。既定実装は範囲内の場合のみ `get` を呼ぶ。
- `getFirst()` は空かを確認してから `get(0)` を呼ぶ。`getFirstOrDefault(defaultValue)` は `getOrDefault(0, defaultValue)` を呼ぶ。
- `getLast()` は空かを確認してから `get(size() - 1)` を呼ぶ。`getLastOrDefault(defaultValue)` は `getOrDefault(size() - 1, defaultValue)` を呼ぶ。どちらも要素を削除しない。
- iterator / spliterator / Stream と、それらを使う一括処理の実行中は、要素数や走査順を変更しないこと。このインターフェースはスナップショットや変更検出を保証しない。具体的な変更操作は各クラスのGuideを参照する。
- 読み取り用インターフェースであり、格納形式や実装の不変性を規定しない。
- 更新が必要な共通引数には `IntMutableArray` / `LongMutableArray` を使用する。更新関数の注意事項は [MutableArrayGuide](./MutableArrayGuide.md#制約注意事項) を参照する。
- `toList()` はボックス化を伴うため、性能重視のループでは `iterator()` またはプリミティブ Stream を使用する。

## 性能特性

`n` は要素数とする。既定メソッドは `size()` と iterator の1要素取得が `O(1)` の実装を前提とする。

| 操作                                                                                                                           | 時間計算量 | 追加領域 |
|--------------------------------------------------------------------------------------------------------------------------------|------------|----------|
| `size`, `isEmpty`, 現在の配列実装の `get` / `getOrDefault` / `getFirst` / `getFirstOrDefault` / `getLast` / `getLastOrDefault` | `O(1)`     | `O(1)`   |
| `spliterator`, `intStream`, `longStream` の生成                                                                                | `O(1)`     | `O(1)`   |
| `contains`, `indexOf`, `lastIndexOf`, `forEachInt`, `forEachLong`, 全要素の走査                                                | `O(n)`     | `O(1)`   |
| `toArray`, `toList`                                                                                                            | `O(n)`     | `O(n)`   |

関数の評価時間は別途加算する。Stream は終端操作時に走査する。インターフェース自体は各実装の計算量を制約しない。

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                    |
|--------------------|------------|---------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-12 | 配列データ構造の共通インターフェースとして初回実装                                                      |
| **バージョン 2.0** | 2026-07-15 | `IntCollection` / `LongCollection` を継承する読み取り専用 API へ分離し、順序付き `spliterator()` を追加 |
| **バージョン 3.0** | 2026-10-23 | IntArray / LongArray に既定の getOrDefault を追加                                                       |
| **バージョン 4.0** | 2026-10-24 | IntArray / LongArray に既定の getLast / getLastOrDefault を追加                                         |
| **バージョン 5.0** | 2026-10-25 | getFirst / getFirstOrDefault と indexOf / lastIndexOf を両インターフェースへ追加                        |
