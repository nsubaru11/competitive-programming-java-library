# ArrayUtils 利用ガイド

> [`ArrayUtils`](../../../src/lib/util/ArrayUtils.java) は、`char[]`, `int[]`, `long[]`, `IntArray`, `LongArray` に対する代表的な配列処理とアルゴリズムを提供する static utility クラスである。

[← README: Primitive Array Utilities](./README.md) · ソース: [ArrayUtils.java](../../../src/lib/util/ArrayUtils.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

集計、探索、配列操作、局所値、連長、固定幅窓、最長単調部分列、部分集合の個数を扱う。

## 特徴

- 基本処理とLIS系は`char[]` / `int[]` / `long[]`に対応
- `int[]` / `long[]`では配列インターフェース版も提供
- 総和、最小・最大、線形探索、異なる値の個数などの基本処理を提供
- プリミティブ配列の反転・交換に対応
- 局所値・連長・固定幅窓・LIS・部分集合のプリミティブ配列版は、先頭 `len` 要素を対象にするオーバーロードを提供
- 固定幅窓の和を追加メモリ $\mathcal{O}(1)$ で計算
- monotonic deque による窓最大・最小担当位置の $\mathcal{O}(n)$ 解析
- 二分探索による LIS 系4種類を $\mathcal{O}(n \log n)$ で計算
- 半分全列挙、HashMap DP、個数指定再帰の3種類の部分集合数え上げを提供
- `reverse` / `swap` / `swapRow` / `descendingSort` は入力配列を直接変更

## 依存関係

- `java.util.HashMap`
- `java.util.Arrays`
- `java.util.function.IntPredicate` / `LongPredicate`
- [`lib.search.ArrayBinarySearch`](../../search/binarysearch/ArrayBinarySearchGuide.md)
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)

## API

### 1. 基本処理

| 種別   | シグネチャ                                                                                                                        | 戻り値の型     | 説明                                        |
|--------|-----------------------------------------------------------------------------------------------------------------------------------|----------------|---------------------------------------------|
| Method | `sum(int[] a)` / `sum(long[] a)`                                                                                                  | `long`         | 配列の総和                                  |
| Method | `sum(IntArray a)` / `sum(LongArray a)`                                                                                            | `long`         | 配列インターフェース版                      |
| Method | `min(char[] a)` / `min(int[] a)` / `min(long[] a)`                                                                                | 各要素型       | 最小値                                      |
| Method | `min(IntArray a)` / `min(LongArray a)`                                                                                            | `int` / `long` | 配列インターフェース版                      |
| Method | `max(char[] a)` / `max(int[] a)` / `max(long[] a)`                                                                                | 各要素型       | 最大値                                      |
| Method | `max(IntArray a)` / `max(LongArray a)`                                                                                            | `int` / `long` | 配列インターフェース版                      |
| Method | `argMin(char[] a)` / `argMin(int[] a)` / `argMin(long[] a)`                                                                       | `int`          | 最小値が最初に現れる添字                    |
| Method | `argMin(IntArray a)` / `argMin(LongArray a)`                                                                                      | `int`          | 配列インターフェース版                      |
| Method | `argMax(char[] a)` / `argMax(int[] a)` / `argMax(long[] a)`                                                                       | `int`          | 最大値が最初に現れる添字                    |
| Method | `argMax(IntArray a)` / `argMax(LongArray a)`                                                                                      | `int`          | 配列インターフェース版                      |
| Method | `count(char[] a, char t)` / `count(int[] a, int t)` / `count(long[] a, long t)`                                                   | `int`          | 未ソート配列における `t` の出現回数         |
| Method | `count(IntArray a, int t)` / `count(LongArray a, long t)`                                                                         | `int`          | 配列インターフェース版                      |
| Method | `indexOf(char[] a, char t)` / `indexOf(int[] a, int t)` / `indexOf(long[] a, long t)`                                             | `int`          | `t` が最初に現れる添字。存在しなければ `-1` |
| Method | `indexOf(IntArray a, int t)` / `indexOf(LongArray a, long t)`                                                                     | `int`          | 配列インターフェース版                      |
| Method | `lastIndexOf(char[] a, char t)` / `lastIndexOf(int[] a, int t)` / `lastIndexOf(long[] a, long t)`                                 | `int`          | `t` が最後に現れる添字。存在しなければ `-1` |
| Method | `lastIndexOf(IntArray a, int t)` / `lastIndexOf(LongArray a, long t)`                                                             | `int`          | 配列インターフェース版                      |
| Method | `reverse(int[] a)` / `reverse(int[] a, int fromIdx, int toIdx)`                                                                   | `void`         | `int` 配列全体・指定範囲を反転              |
| Method | `reverse(long[] a)` / `reverse(long[] a, int fromIdx, int toIdx)`                                                                 | `void`         | `long` 配列版                               |
| Method | `reverse(char[] a)` / `reverse(char[] a, int fromIdx, int toIdx)`                                                                 | `void`         | `char` 配列版                               |
| Method | `reverse(int[][] a)` / `reverse(int[][] a, int fromIdx, int toIdx)`                                                               | `void`         | 行全体・指定範囲の行順を反転                |
| Method | `reverse(long[][] a)` / `reverse(long[][] a, int fromIdx, int toIdx)`                                                             | `void`         | long配列版                                  |
| Method | `reverse(char[][] a)` / `reverse(char[][] a, int fromIdx, int toIdx)`                                                             | `void`         | char配列版                                  |
| Method | `swap(char[] a, int i, int j)` / `swap(int[] a, int i, int j)` / `swap(long[] a, int i, int j)`                                   | `void`         | `i`, `j` の要素を交換                       |
| Method | `swapRow(char[][] a, int i, int j)` / `swapRow(int[][] a, int i, int j)` / `swapRow(long[][] a, int i, int j)`                    | `void`         | `i`, `j` の行を交換                         |
| Method | `descendingSort(char[] arr)` / `descendingSort(int[] arr)` / `descendingSort(long[] arr)`                                         | `void`         | 配列を降順にソート                          |
| Method | `uniqueSize(char[] a)` / `uniqueSize(int[] a)` / `uniqueSize(long[] a)`                                                           | `int`          | 異なる値の個数                              |
| Method | `uniqueSize(IntArray a)` / `uniqueSize(LongArray a)`                                                                              | `int`          | 配列インターフェース版                      |
| Method | `freq(int[] a, int size)` / `freq(long[] a, int size)`                                                                            | `int[]`        | `[0, size)` の各値の出現回数                |
| Method | `freq(IntArray a, int size)` / `freq(LongArray a, int size)`                                                                      | `int[]`        | 配列インターフェース版                      |
| Method | `freqLower(char[] a)` / `freqUpper(char[] a)`                                                                                     | `int[]`        | 英小文字／英大文字26種の出現回数            |
| Method | `mexLower(char[] a)` / `mexUpper(char[] a)`                                                                                       | `char`         | 含まれない最小の英小文字／英大文字          |
| Method | `mex(int[] a)` / `mex(long[] a)`                                                                                                  | `int`          | 含まれない最小の非負整数                    |
| Method | `mex(IntArray a)` / `mex(LongArray a)`                                                                                            | `int`          | 配列インターフェース版                      |
| Method | `isSorted(char[] a)` / `isSorted(int[] a)` / `isSorted(long[] a)`                                                                 | `boolean`      | 広義昇順かを判定                            |
| Method | `isSorted(IntArray a)` / `isSorted(LongArray a)`                                                                                  | `boolean`      | 配列インターフェース版                      |
| Method | `allEqual(char[] a)` / `allEqual(int[] a)` / `allEqual(long[] a)`                                                                 | `boolean`      | 全要素が等しいかを判定                      |
| Method | `allEqual(IntArray a)` / `allEqual(LongArray a)`                                                                                  | `boolean`      | 配列インターフェース版                      |
| Method | `allMatch(char[] a, IntPredicate tester)` / `allMatch(int[] a, IntPredicate tester)` / `allMatch(long[] a, LongPredicate tester)` | `boolean`      | 全要素が条件を満たすかを判定                |
| Method | `allMatch(IntArray a, IntPredicate tester)` / `allMatch(LongArray a, LongPredicate tester)`                                       | `boolean`      | 配列インターフェース版                      |
| Method | `anyMatch(char[] a, IntPredicate tester)` / `anyMatch(int[] a, IntPredicate tester)` / `anyMatch(long[] a, LongPredicate tester)` | `boolean`      | いずれかの要素が条件を満たすかを判定        |
| Method | `anyMatch(IntArray a, IntPredicate tester)` / `anyMatch(LongArray a, LongPredicate tester)`                                       | `boolean`      | 配列インターフェース版                      |

`argMin` / `argMax`、`min` / `max` は空でない配列を前提とする。`uniqueSize` はコピーをソートするため元配列を変更しない。

### 2. 局所値・連長

| 種別   | シグネチャ                                                 | 戻り値の型 | 説明                             |
|--------|------------------------------------------------------------|------------|----------------------------------|
| Method | `localMaxCnt(int[] a)` / `localMaxCnt(int[] a, int len)`   | `int`      | 両隣より真に大きい内部要素の個数 |
| Method | `localMaxCnt(long[] a)` / `localMaxCnt(long[] a, int len)` | `int`      | long 配列版                      |
| Method | `localMaxCnt(IntArray a)` / `localMaxCnt(LongArray a)`     | `int`      | 配列インターフェース版           |
| Method | `localMaxCnt(char[] a)` / `localMaxCnt(char[] a, int len)` | `int`      | char配列版                       |
| Method | `localMinCnt(int[] a)` / `localMinCnt(int[] a, int len)`   | `int`      | 両隣より真に小さい内部要素の個数 |
| Method | `localMinCnt(long[] a)` / `localMinCnt(long[] a, int len)` | `int`      | long 配列版                      |
| Method | `localMinCnt(IntArray a)` / `localMinCnt(LongArray a)`     | `int`      | 配列インターフェース版           |
| Method | `localMinCnt(char[] a)` / `localMinCnt(char[] a, int len)` | `int`      | char配列版                       |
| Method | `maxRunLen(int[] a)` / `maxRunLen(int[] a, int len)`       | `int`      | 同じ値が連続する最長区間の長さ   |
| Method | `maxRunLen(long[] a)` / `maxRunLen(long[] a, int len)`     | `int`      | long 配列版                      |
| Method | `maxRunLen(IntArray a)` / `maxRunLen(LongArray a)`         | `int`      | 配列インターフェース版           |
| Method | `maxRunLen(char[] a)` / `maxRunLen(char[] a, int len)`     | `int`      | char配列版                       |

端点は局所最大・局所最小に数えない。

### 3. 固定幅窓の和

| 種別   | シグネチャ                                                     | 戻り値の型 | 説明                   |
|--------|----------------------------------------------------------------|------------|------------------------|
| Method | `maxWin(int[] a, int k)` / `maxWin(int[] a, int len, int k)`   | `long`     | 長さ k の窓和の最大値  |
| Method | `maxWin(long[] a, int k)` / `maxWin(long[] a, int len, int k)` | `long`     | long 配列版            |
| Method | `maxWin(IntArray a, int k)` / `maxWin(LongArray a, int k)`     | `long`     | 配列インターフェース版 |
| Method | `minWin(int[] a, int k)` / `minWin(int[] a, int len, int k)`   | `long`     | 長さ k の窓和の最小値  |
| Method | `minWin(long[] a, int k)` / `minWin(long[] a, int len, int k)` | `long`     | long 配列版            |
| Method | `minWin(IntArray a, int k)` / `minWin(LongArray a, int k)`     | `long`     | 配列インターフェース版 |

### 4. 窓最大・最小の担当継続長

| 種別   | シグネチャ                                                           | 戻り値の型 | 説明                                                    |
|--------|----------------------------------------------------------------------|------------|---------------------------------------------------------|
| Method | `winMaxLen(int[] a, int k)` / `winMaxLen(int[] a, int len, int k)`   | `int[]`    | 窓最大を担う同一位置が最長で続く `{index, windowCount}` |
| Method | `winMaxLen(long[] a, int k)` / `winMaxLen(long[] a, int len, int k)` | `int[]`    | long 配列版                                             |
| Method | `winMaxLen(IntArray a, int k)` / `winMaxLen(LongArray a, int k)`     | `int[]`    | 配列インターフェース版                                  |
| Method | `winMinLen(int[] a, int k)` / `winMinLen(int[] a, int len, int k)`   | `int[]`    | 窓最小を担う同一位置が最長で続く `{index, windowCount}` |
| Method | `winMinLen(long[] a, int k)` / `winMinLen(long[] a, int len, int k)` | `int[]`    | long 配列版                                             |
| Method | `winMinLen(IntArray a, int k)` / `winMinLen(LongArray a, int k)`     | `int[]`    | 配列インターフェース版                                  |

`len < k` の場合は `{-1, -1}` を返す。同値では古い添字を deque に残すため、同じ値ではなく同じ要素位置が担当し続けた窓数である。

### 5. 最長単調部分列

| 種別   | シグネチャ                                   | 戻り値の型 | 説明                               |
|--------|----------------------------------------------|------------|------------------------------------|
| Method | `lis(int[] a)` / `lis(int[] a, int len)`     | `int`      | 最長狭義増加部分列の長さ           |
| Method | `lis(long[] a)` / `lis(long[] a, int len)`   | `int`      | long 配列版                        |
| Method | `lis(IntArray a)` / `lis(LongArray a)`       | `int`      | 配列インターフェース版             |
| Method | `lnds(int[] a)` / `lnds(int[] a, int len)`   | `int`      | 最長広義増加部分列の長さ           |
| Method | `lnds(long[] a)` / `lnds(long[] a, int len)` | `int`      | long 配列版                        |
| Method | `lnds(IntArray a)` / `lnds(LongArray a)`     | `int`      | 配列インターフェース版             |
| Method | `lds(int[] a)` / `lds(int[] a, int len)`     | `int`      | 最長狭義減少部分列の長さ           |
| Method | `lds(long[] a)` / `lds(long[] a, int len)`   | `int`      | long 配列版                        |
| Method | `lds(IntArray a)` / `lds(LongArray a)`       | `int`      | 配列インターフェース版             |
| Method | `lnis(int[] a)` / `lnis(int[] a, int len)`   | `int`      | 最長広義減少部分列の長さ           |
| Method | `lnis(long[] a)` / `lnis(long[] a, int len)` | `int`      | long 配列版                        |
| Method | `lnis(IntArray a)` / `lnis(LongArray a)`     | `int`      | 配列インターフェース版             |
| Method | `lis(char[] a)` / `lis(char[] a, int len)`   | `int`      | char配列の最長狭義増加部分列の長さ |
| Method | `lnds(char[] a)` / `lnds(char[] a, int len)` | `int`      | char配列の最長広義増加部分列の長さ |
| Method | `lds(char[] a)` / `lds(char[] a, int len)`   | `int`      | char配列の最長狭義減少部分列の長さ |
| Method | `lnis(char[] a)` / `lnis(char[] a, int len)` | `int`      | char配列の最長広義減少部分列の長さ |

### 6. 部分集合の個数

| 種別   | シグネチャ                                                                                       | 戻り値の型 | 説明                                           |
|--------|--------------------------------------------------------------------------------------------------|------------|------------------------------------------------|
| Method | `subsetMitm(int[] a, long t)` / `subsetMitm(int[] a, int len, long t)`                           | `int`      | 半分全列挙で和が t の部分集合数を計算          |
| Method | `subsetMitm(long[] a, long t)` / `subsetMitm(long[] a, int len, long t)`                         | `int`      | long 配列版                                    |
| Method | `subsetMitm(IntArray a, long t)` / `subsetMitm(LongArray a, long t)`                             | `int`      | 配列インターフェース版                         |
| Method | `subsetDp(int[] a, long t)` / `subsetDp(int[] a, int len, long t)`                               | `int`      | HashMap DP で和が t の部分集合数を計算         |
| Method | `subsetDp(long[] a, long t)` / `subsetDp(long[] a, int len, long t)`                             | `int`      | long 配列版                                    |
| Method | `subsetDp(IntArray a, long t)` / `subsetDp(LongArray a, long t)`                                 | `int`      | 配列インターフェース版                         |
| Method | `subsetRecursion(int[] a, long t, int k)` / `subsetRecursion(int[] a, int len, long t, int k)`   | `int`      | ちょうど k 個、和が t の部分集合数を再帰で計算 |
| Method | `subsetRecursion(long[] a, long t, int k)` / `subsetRecursion(long[] a, int len, long t, int k)` | `int`      | long 配列版                                    |
| Method | `subsetRecursion(IntArray a, long t, int k)` / `subsetRecursion(LongArray a, long t, int k)`     | `int`      | 配列インターフェース版                         |

## 利用例

準備中

## 制約・注意事項

- `(a, len, ...)` 形式は配列の先頭 `len` 要素を対象とし、`0 <= len <= a.length` を前提とする。
- `min` / `max` / `argMin` / `argMax` は空でない配列を前提とする。
- 数値版の `freq` は全要素が `[0, size)` に含まれることを前提とする。`freqLower` / `mexLower`は英小文字、`freqUpper` / `mexUpper`は英大文字だけを受け取る。
- `mexLower` / `mexUpper`は26文字を全て含む場合にNUL文字を返す。
- `reverse` / `swap` / `swapRow` / `descendingSort` は入力配列を直接変更する。`descendingSort` は昇順ソート後に反転する。
- 範囲指定 `reverse` は `[fromIdx, toIdx)` を対象とする。2次元版は各行の内容ではなく、行の並びを反転する。
- 固定幅窓は問題の定義に合う `k` で使用する。
- `winMaxLen` / `winMinLen` の戻り値は値ではなく、元配列の添字と継続窓数である。
- LIS 系は部分列そのものではなく長さだけを返す。
- subset 系は存在判定ではなく部分集合の個数を `int` で返し、空集合も数える。
- subset 系の個数、途中の和、ビットマスクが型の範囲に収まり、実行時間・メモリが現実的な問題制約で使用する。
- 上記以外のメソッドは入力配列を変更しない。

## 性能特性

`n` は処理対象の要素数とする。`len` 指定時は `n = len`、2次元配列の行操作では対象の行数である。

- `sum`, `min`, `max`, `argMin`, `argMax`, `count`, `indexOf`, `lastIndexOf`, `reverse`, `isSorted`, `allEqual`, `allMatch`, `anyMatch`: 時間 $\mathcal{O}(n)$、追加メモリ $\mathcal{O}(1)$
- `swap`: 時間 $\mathcal{O}(1)$、追加メモリ $\mathcal{O}(1)$
- `uniqueSize`: 時間 $\mathcal{O}(n \log n)$、追加メモリ $\mathcal{O}(n)$
- `freq`: 時間 $\mathcal{O}(n + size)$、追加メモリ $\mathcal{O}(size)$
- `mex`: 時間 $\mathcal{O}(n)$、追加メモリ $\mathcal{O}(n)$
- `localMaxCnt`, `localMinCnt`, `maxRunLen`, `maxWin`, `minWin`: 時間 $\mathcal{O}(n)$、追加メモリ $\mathcal{O}(1)$
- `winMaxLen`, `winMinLen`: 時間 $\mathcal{O}(n)$、追加メモリ $\mathcal{O}(n)$
- `lis`, `lnds`, `lds`, `lnis`: 時間 $\mathcal{O}(n \log n)$、追加メモリ $\mathcal{O}(n)$
- `subsetMitm`: 時間 $\mathcal{O}(n 2^{n/2})$、追加メモリ $\mathcal{O}(2^{n/2})$
- `subsetDp`: 到達可能な異なる和の個数を S として時間 $\mathcal{O}(nS)$、追加メモリ $\mathcal{O}(S)$
- `subsetRecursion`: 最悪時間 $\mathcal{O}(2^n)$、再帰深さ $\mathcal{O}(n)$

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                  |
|--------------------|------------|-----------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-12 | プリミティブ配列と `IntArray` / `LongArray` のオーバーロードを備える `ArrayUtils` を実装                              |
| **バージョン 2.0** | 2026-07-27 | 集計、線形探索、範囲・行反転、要素・行交換、異なる値の個数、頻度表、MEX、整列・一致・条件判定を追加し、配列操作を集約 |
| **バージョン 3.0** | 2026-07-31 | 最長連長とLIS系を追加し、内部二分探索を標準APIおよび `ArrayBinarySearch` へ委譲                                       |
| **バージョン 4.0** | 2026-08-01 | 基本処理・局所値・連長・LIS系へ`char[]`版を追加し、英字頻度・MEXと降順ソートを追加                                    |
| **バージョン 4.1** | 2026-10-27 | `IntMutableArray` / `LongMutableArray` 向けの `reverse`・`swap` を削除し、各インターフェースの既定メソッドへ移行      |
