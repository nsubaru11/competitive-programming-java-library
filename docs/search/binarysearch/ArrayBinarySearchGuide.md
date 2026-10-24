# ArrayBinarySearch 利用ガイド

> `ArrayBinarySearch` は、昇順ソート済みの配列に対して下限探索、上限探索、包含判定、出現数の取得を行うユーティリティクラスである。

[← README: Binary Search](./README.md) · ソース: [ArrayBinarySearch.java](../../../src/lib/search/ArrayBinarySearch.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

プリミティブ配列、`Comparable` 配列、`IntArray`、`LongArray` に対応し、配列全体または半開区間 `[l, r)` を探索できる。

## 特徴

- `lowerBoundSearch` は目標値と一致する左端のインデックスを返却
- `upperBoundSearch` は目標値と一致する右端のインデックスを返却
- 一致する値がない場合は `-(挿入位置 + 1)` を返却
- `contains` は目標値の有無、`count` は目標値の出現数を返却
- `int[]`, `long[]`, `double[]`, `Comparable[]`, `IntArray`, `LongArray` に対応
- 範囲指定オーバーロードは半開区間 `[l, r)` を探索

## 依存関係

- `java.util.Arrays`
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)

## API

各メソッドには配列全体を対象とする形式と、`l`, `r` を受け取って `[l, r)` を対象とする形式がある。

### 1. `int[]`

| メソッド                                                | 戻り値    | 説明                                 |
|:--------------------------------------------------------|:----------|:-------------------------------------|
| `lowerBoundSearch(int[] arr, int target)`               | `int`     | 一致する左端、なければ挿入位置の補数 |
| `lowerBoundSearch(int[] arr, int l, int r, int target)` | `int`     | 範囲指定版                           |
| `upperBoundSearch(int[] arr, int target)`               | `int`     | 一致する右端、なければ挿入位置の補数 |
| `upperBoundSearch(int[] arr, int l, int r, int target)` | `int`     | 範囲指定版                           |
| `contains(int[] arr, int target)`                       | `boolean` | 目標値が含まれるか                   |
| `contains(int[] arr, int l, int r, int target)`         | `boolean` | 範囲指定版                           |
| `count(int[] arr, int target)`                          | `int`     | 目標値の出現数                       |
| `count(int[] arr, int l, int r, int target)`            | `int`     | 範囲指定版                           |

### 2. `long[]`

| メソッド                                                  | 戻り値    | 説明                                 |
|:----------------------------------------------------------|:----------|:-------------------------------------|
| `lowerBoundSearch(long[] arr, long target)`               | `int`     | 一致する左端、なければ挿入位置の補数 |
| `lowerBoundSearch(long[] arr, int l, int r, long target)` | `int`     | 範囲指定版                           |
| `upperBoundSearch(long[] arr, long target)`               | `int`     | 一致する右端、なければ挿入位置の補数 |
| `upperBoundSearch(long[] arr, int l, int r, long target)` | `int`     | 範囲指定版                           |
| `contains(long[] arr, long target)`                       | `boolean` | 目標値が含まれるか                   |
| `contains(long[] arr, int l, int r, long target)`         | `boolean` | 範囲指定版                           |
| `count(long[] arr, long target)`                          | `int`     | 目標値の出現数                       |
| `count(long[] arr, int l, int r, long target)`            | `int`     | 範囲指定版                           |

### 3. `double[]`

| メソッド                                                      | 戻り値    | 説明                                 |
|:--------------------------------------------------------------|:----------|:-------------------------------------|
| `lowerBoundSearch(double[] arr, double target)`               | `int`     | 一致する左端、なければ挿入位置の補数 |
| `lowerBoundSearch(double[] arr, int l, int r, double target)` | `int`     | 範囲指定版                           |
| `upperBoundSearch(double[] arr, double target)`               | `int`     | 一致する右端、なければ挿入位置の補数 |
| `upperBoundSearch(double[] arr, int l, int r, double target)` | `int`     | 範囲指定版                           |
| `contains(double[] arr, double target)`                       | `boolean` | 目標値が含まれるか                   |
| `contains(double[] arr, int l, int r, double target)`         | `boolean` | 範囲指定版                           |
| `count(double[] arr, double target)`                          | `int`     | 目標値の出現数                       |
| `count(double[] arr, int l, int r, double target)`            | `int`     | 範囲指定版                           |

### 4. `Comparable[]`

| メソッド                                            | 戻り値    | 説明                                 |
|:----------------------------------------------------|:----------|:-------------------------------------|
| `lowerBoundSearch(T[] arr, T target)`               | `int`     | 一致する左端、なければ挿入位置の補数 |
| `lowerBoundSearch(T[] arr, int l, int r, T target)` | `int`     | 範囲指定版                           |
| `upperBoundSearch(T[] arr, T target)`               | `int`     | 一致する右端、なければ挿入位置の補数 |
| `upperBoundSearch(T[] arr, int l, int r, T target)` | `int`     | 範囲指定版                           |
| `contains(T[] arr, T target)`                       | `boolean` | 目標値が含まれるか                   |
| `contains(T[] arr, int l, int r, T target)`         | `boolean` | 範囲指定版                           |
| `count(T[] arr, T target)`                          | `int`     | 目標値の出現数                       |
| `count(T[] arr, int l, int r, T target)`            | `int`     | 範囲指定版                           |

各 `T` は `Comparable<? super T>` を実装している必要がある。

### 5. `IntArray`

| メソッド                                                   | 戻り値    | 説明                                 |
|:-----------------------------------------------------------|:----------|:-------------------------------------|
| `lowerBoundSearch(IntArray arr, int target)`               | `int`     | 一致する左端、なければ挿入位置の補数 |
| `lowerBoundSearch(IntArray arr, int l, int r, int target)` | `int`     | 範囲指定版                           |
| `upperBoundSearch(IntArray arr, int target)`               | `int`     | 一致する右端、なければ挿入位置の補数 |
| `upperBoundSearch(IntArray arr, int l, int r, int target)` | `int`     | 範囲指定版                           |
| `contains(IntArray arr, int target)`                       | `boolean` | 目標値が含まれるか                   |
| `contains(IntArray arr, int l, int r, int target)`         | `boolean` | 範囲指定版                           |
| `count(IntArray arr, int target)`                          | `int`     | 目標値の出現数                       |
| `count(IntArray arr, int l, int r, int target)`            | `int`     | 範囲指定版                           |

### 6. `LongArray`

| メソッド                                                     | 戻り値    | 説明                                 |
|:-------------------------------------------------------------|:----------|:-------------------------------------|
| `lowerBoundSearch(LongArray arr, long target)`               | `int`     | 一致する左端、なければ挿入位置の補数 |
| `lowerBoundSearch(LongArray arr, int l, int r, long target)` | `int`     | 範囲指定版                           |
| `upperBoundSearch(LongArray arr, long target)`               | `int`     | 一致する右端、なければ挿入位置の補数 |
| `upperBoundSearch(LongArray arr, int l, int r, long target)` | `int`     | 範囲指定版                           |
| `contains(LongArray arr, long target)`                       | `boolean` | 目標値が含まれるか                   |
| `contains(LongArray arr, int l, int r, long target)`         | `boolean` | 範囲指定版                           |
| `count(LongArray arr, long target)`                          | `int`     | 目標値の出現数                       |
| `count(LongArray arr, int l, int r, long target)`            | `int`     | 範囲指定版                           |

## 利用例

準備中

## 制約・注意事項

- 探索対象は昇順にソート済みであることを前提とする。
- 範囲指定版は半開区間 `[l, r)` を対象とする。
- `lowerBoundSearch` / `upperBoundSearch` で値が見つからない場合、戻り値の補数 `~result` が挿入位置である。
- `contains` / `count` は値が見つからない場合、それぞれ `false` / `0` を返す。
- 引数の妥当性を網羅的に検証する汎用コレクションではない。

## 性能特性

- `lowerBoundSearch`, `upperBoundSearch`, `contains`: 時間 $\mathcal{O}(\log n)$、追加メモリ $\mathcal{O}(1)$
- `count`: 時間 $\mathcal{O}(\log n)$、追加メモリ $\mathcal{O}(1)$

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                                    |
|:-------------------|:-----------|:------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-03-29 | 通常の二分探索、下限探索、上限探索を初回実装                                                                            |
| **バージョン 1.1** | 2025-04-06 | `int[]`, `long[]`, `double[]`, `Comparable[]` に対応                                                                    |
| **バージョン 1.2** | 2025-04-17 | 例外処理とnullチェックを追加                                                                                            |
| **バージョン 2.0** | 2025-06-30 | 各探索方式に特化した実装へ再構成し、`SearchType` を削除                                                                 |
| **バージョン 2.1** | 2025-10-13 | 引数とローカル変数へ `final` を追加                                                                                     |
| **バージョン 2.2** | 2026-01-06 | 空区間を有効化                                                                                                          |
| **バージョン 3.0** | 2026-04-17 | `BSException` と明示的な範囲検証を削除し、下限・上限探索の意味を明確化                                                  |
| **バージョン 4.0** | 2026-07-31 | `normalSearch` を廃止し、`contains` の配列版を `Arrays.binarySearch` へ委譲。`IntArray` / `LongArray` の全探索APIを追加 |
