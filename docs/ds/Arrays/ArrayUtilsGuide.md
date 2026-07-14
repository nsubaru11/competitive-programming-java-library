# ArrayUtils 利用ガイド

## 概要

[`ArrayUtils`](../../../src/lib/util/ArrayUtils.java) は、`int[]`, `long[]`, `IntArray`, `LongArray` に対する代表的な配列アルゴリズムを提供する static utility クラスです。局所値、連長、固定幅窓、最長単調部分列、部分集合の個数を扱います。

## 特徴

- `int[]` / `long[]` と配列インターフェースの両方に対応
- プリミティブ配列版は先頭 `len` 要素だけを対象にするオーバーロードを提供
- 固定幅窓の和を追加メモリ O(1) で計算
- monotonic deque による窓最大・最小担当位置の O(n) 解析
- 二分探索による LIS 系4種類を O(n log n) で計算
- 半分全列挙、HashMap DP、個数指定再帰の3種類の部分集合数え上げを提供
- 入力配列を変更しない

## 依存関係

- `java.util.HashMap`
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)

## 主な機能（メソッド一覧）

### 1. 局所値・連長

| メソッド                                                       | 戻り値の型 | 説明               |
|------------------------------------------------------------|-------|------------------|
| `localMaxCnt(int[] a)` / `localMaxCnt(int[] a, int len)`   | `int` | 両隣より真に大きい内部要素の個数 |
| `localMaxCnt(long[] a)` / `localMaxCnt(long[] a, int len)` | `int` | long 配列版         |
| `localMaxCnt(IntArray a)` / `localMaxCnt(LongArray a)`     | `int` | 配列インターフェース版      |
| `localMinCnt(int[] a)` / `localMinCnt(int[] a, int len)`   | `int` | 両隣より真に小さい内部要素の個数 |
| `localMinCnt(long[] a)` / `localMinCnt(long[] a, int len)` | `int` | long 配列版         |
| `localMinCnt(IntArray a)` / `localMinCnt(LongArray a)`     | `int` | 配列インターフェース版      |
| `runLen(int[] a)` / `runLen(int[] a, int len)`             | `int` | 同じ値が連続する最長区間の長さ  |
| `runLen(long[] a)` / `runLen(long[] a, int len)`           | `int` | long 配列版         |
| `runLen(IntArray a)` / `runLen(LongArray a)`               | `int` | 配列インターフェース版      |

端点は局所最大・局所最小に数えません。

### 2. 固定幅窓の和

| メソッド                                                           | 戻り値の型  | 説明           |
|----------------------------------------------------------------|--------|--------------|
| `maxWin(int[] a, int k)` / `maxWin(int[] a, int len, int k)`   | `long` | 長さ k の窓和の最大値 |
| `maxWin(long[] a, int k)` / `maxWin(long[] a, int len, int k)` | `long` | long 配列版     |
| `maxWin(IntArray a, int k)` / `maxWin(LongArray a, int k)`     | `long` | 配列インターフェース版  |
| `minWin(int[] a, int k)` / `minWin(int[] a, int len, int k)`   | `long` | 長さ k の窓和の最小値 |
| `minWin(long[] a, int k)` / `minWin(long[] a, int len, int k)` | `long` | long 配列版     |
| `minWin(IntArray a, int k)` / `minWin(LongArray a, int k)`     | `long` | 配列インターフェース版  |

### 3. 窓最大・最小の担当継続長

| メソッド                                                                 | 戻り値の型   | 説明                                      |
|----------------------------------------------------------------------|---------|-----------------------------------------|
| `winMaxLen(int[] a, int k)` / `winMaxLen(int[] a, int len, int k)`   | `int[]` | 窓最大を担う同一位置が最長で続く `{index, windowCount}` |
| `winMaxLen(long[] a, int k)` / `winMaxLen(long[] a, int len, int k)` | `int[]` | long 配列版                                |
| `winMaxLen(IntArray a, int k)` / `winMaxLen(LongArray a, int k)`     | `int[]` | 配列インターフェース版                             |
| `winMinLen(int[] a, int k)` / `winMinLen(int[] a, int len, int k)`   | `int[]` | 窓最小を担う同一位置が最長で続く `{index, windowCount}` |
| `winMinLen(long[] a, int k)` / `winMinLen(long[] a, int len, int k)` | `int[]` | long 配列版                                |
| `winMinLen(IntArray a, int k)` / `winMinLen(LongArray a, int k)`     | `int[]` | 配列インターフェース版                             |

`len < k` の場合は `{-1, -1}` を返します。同値では古い添字を deque に残すため、同じ値ではなく同じ要素位置が担当し続けた窓数です。

### 4. 最長単調部分列

| メソッド                                         | 戻り値の型 | 説明           |
|----------------------------------------------|-------|--------------|
| `lis(int[] a)` / `lis(int[] a, int len)`     | `int` | 最長狭義増加部分列の長さ |
| `lis(long[] a)` / `lis(long[] a, int len)`   | `int` | long 配列版     |
| `lis(IntArray a)` / `lis(LongArray a)`       | `int` | 配列インターフェース版  |
| `lnds(int[] a)` / `lnds(int[] a, int len)`   | `int` | 最長広義増加部分列の長さ |
| `lnds(long[] a)` / `lnds(long[] a, int len)` | `int` | long 配列版     |
| `lnds(IntArray a)` / `lnds(LongArray a)`     | `int` | 配列インターフェース版  |
| `lds(int[] a)` / `lds(int[] a, int len)`     | `int` | 最長狭義減少部分列の長さ |
| `lds(long[] a)` / `lds(long[] a, int len)`   | `int` | long 配列版     |
| `lds(IntArray a)` / `lds(LongArray a)`       | `int` | 配列インターフェース版  |
| `lnis(int[] a)` / `lnis(int[] a, int len)`   | `int` | 最長広義減少部分列の長さ |
| `lnis(long[] a)` / `lnis(long[] a, int len)` | `int` | long 配列版     |
| `lnis(IntArray a)` / `lnis(LongArray a)`     | `int` | 配列インターフェース版  |

### 5. 部分集合の個数

| メソッド                                                                                             | 戻り値の型 | 説明                         |
|--------------------------------------------------------------------------------------------------|-------|----------------------------|
| `subsetMitm(int[] a, long t)` / `subsetMitm(int[] a, int len, long t)`                           | `int` | 半分全列挙で和が t の部分集合数を計算       |
| `subsetMitm(long[] a, long t)` / `subsetMitm(long[] a, int len, long t)`                         | `int` | long 配列版                   |
| `subsetMitm(IntArray a, long t)` / `subsetMitm(LongArray a, long t)`                             | `int` | 配列インターフェース版                |
| `subsetDp(int[] a, long t)` / `subsetDp(int[] a, int len, long t)`                               | `int` | HashMap DP で和が t の部分集合数を計算 |
| `subsetDp(long[] a, long t)` / `subsetDp(long[] a, int len, long t)`                             | `int` | long 配列版                   |
| `subsetDp(IntArray a, long t)` / `subsetDp(LongArray a, long t)`                                 | `int` | 配列インターフェース版                |
| `subsetRecursion(int[] a, long t, int k)` / `subsetRecursion(int[] a, int len, long t, int k)`   | `int` | ちょうど k 個、和が t の部分集合数を再帰で計算 |
| `subsetRecursion(long[] a, long t, int k)` / `subsetRecursion(long[] a, int len, long t, int k)` | `int` | long 配列版                   |
| `subsetRecursion(IntArray a, long t, int k)` / `subsetRecursion(LongArray a, long t, int k)`     | `int` | 配列インターフェース版                |

## 利用例

```java
int[] a = {3, 1, 4, 1, 5, 9};

System.out.println(ArrayUtils.lis(a));       // 4
System.out.println(ArrayUtils.runLen(a));    // 1
System.out.println(ArrayUtils.maxWin(a, 3)); // 15
```

```java
long[] values = {1, 3, 5, 7};

int count = ArrayUtils.subsetMitm(values, 8);          // {1,7}, {3,5} の2通り
int pairs = ArrayUtils.subsetRecursion(values, 8, 2);  // 2
```

## 注意事項

- `(a, len, ...)` 形式は配列の先頭 `len` 要素を対象とし、`0 <= len <= a.length` を前提とします。
- 固定幅窓は問題の定義に合う `k` で使用します。
- `winMaxLen` / `winMinLen` の戻り値は値ではなく、元配列の添字と継続窓数です。
- LIS 系は部分列そのものではなく長さだけを返します。
- subset 系は存在判定ではなく部分集合の個数を `int` で返し、空集合も数えます。
- subset 系の個数、途中の和、ビットマスクが型の範囲に収まり、実行時間・メモリが現実的な問題制約で使用します。
- 全メソッドは入力配列を変更しません。

## パフォーマンス特性

- `localMaxCnt`, `localMinCnt`, `runLen`, `maxWin`, `minWin`: 時間 O(n)、追加メモリ O(1)
- `winMaxLen`, `winMinLen`: 時間 O(n)、追加メモリ O(n)
- `lis`, `lnds`, `lds`, `lnis`: 時間 O(n log n)、追加メモリ O(n)
- `subsetMitm`: 時間 O(n 2^(n/2))、追加メモリ O(2^(n/2))
- `subsetDp`: 到達可能な異なる和の個数を S として時間 O(nS)、追加メモリ O(S)
- `subsetRecursion`: 最悪時間 O(2^n)、再帰深さ O(n)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                                          |
|:--------------|:-----------|:------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-12 | 旧一次元配列クラス内のアルゴリズムを static utility へ分離し、プリミティブ配列と `IntArray` / `LongArray` のオーバーロードを備える `ArrayUtils` として初回実装 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
