# PrefixSum / PrefixModSum 利用ガイド

## 概要

`IntPrefixSum` / `LongPrefixSum` とその2D・3D版は、構築後に更新しない配列の累積和を保持します。
`IntPrefixModSum` / `LongPrefixModSum` とその2D・3D版は、同じ累積和を法 `mod` で保持します。 1Dの閉区間、2Dの閉矩形、3Dの閉直方体の和をいずれも $\mathcal{O}(1)$ で取得できます。

## 特徴

- 内部に先頭ゼロの境界を設け、1Dは `n + 1`、2Dは `(h + 1)(w + 1)`、3Dは `(d + 1)(h + 1)(w + 1)` 要素で保持
- 多次元版も一次元プリミティブ配列へ圧縮
- 1Dの `IntPrefixSum` は累積値を `int[]` で保持
- 2D・3Dの `IntPrefixSum` は累積値を `long[]` で保持し、int列の合計をlongで返す
- `IntPrefixModSum` 系は剰余値がintに収まるため `int[]` で保持
- 多次元MOD版は包含排除式を1回の走査で計算し、剰余を `[0, mod)` に正規化
- 配列、添字付き初期化関数、行優先または `i`・`j`・`k` 順のsupplierから構築可能
- 全体和、原点からの累積和、任意区間の和、元の1要素を取得可能
- 全ての区間指定を両端を含む閉区間で統一

## 対象クラス

| 次元 | 通常版                              | MOD版                                     |
|------|-------------------------------------|-------------------------------------------|
| 1D   | `IntPrefixSum`, `LongPrefixSum`     | `IntPrefixModSum`, `LongPrefixModSum`     |
| 2D   | `IntPrefixSum2D`, `LongPrefixSum2D` | `IntPrefixModSum2D`, `LongPrefixModSum2D` |
| 3D   | `IntPrefixSum3D`, `LongPrefixSum3D` | `IntPrefixModSum3D`, `LongPrefixModSum3D` |

## 依存関係

- `java.util.PrimitiveIterator`
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntBinaryOperator`
- `java.util.function.IntToLongFunction`
- `java.util.function.LongBinaryOperator`
- `java.util.function.IntSupplier`
- `java.util.function.LongSupplier`
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)

## 主な機能（メソッド一覧）

### 1. 1Dコンストラクタ・生成メソッド

| メソッド                                                        | 戻り値の型         | 説明                         |
|-----------------------------------------------------------------|--------------------|------------------------------|
| `IntPrefixSum(int n, IntUnaryOperator init)`                    | -                  | `init(i)` で構築             |
| `IntPrefixSum(int[] a)`                                         | -                  | int配列から構築              |
| `IntPrefixSum(IntArray a)`                                      | -                  | `IntArray` の論理順から構築  |
| `IntPrefixSum.generate(int n, IntSupplier init)`                | `IntPrefixSum`     | supplierをn回呼んで構築      |
| `LongPrefixSum(int n, IntToLongFunction init)`                  | -                  | `init(i)` で構築             |
| `LongPrefixSum(long[] a)`                                       | -                  | long配列から構築             |
| `LongPrefixSum(LongArray a)`                                    | -                  | `LongArray` の論理順から構築 |
| `LongPrefixSum.generate(int n, LongSupplier init)`              | `LongPrefixSum`    | supplierをn回呼んで構築      |
| `IntPrefixModSum(int n, int mod, IntUnaryOperator init)`        | -                  | `init(i)` でMOD版を構築      |
| `IntPrefixModSum(int[] a, int mod)`                             | -                  | int配列からMOD版を構築       |
| `IntPrefixModSum(IntArray a, int mod)`                          | -                  | `IntArray` からMOD版を構築   |
| `IntPrefixModSum.generate(int n, int mod, IntSupplier init)`    | `IntPrefixModSum`  | supplierからMOD版を構築      |
| `LongPrefixModSum(int n, long mod, IntToLongFunction init)`     | -                  | `init(i)` でMOD版を構築      |
| `LongPrefixModSum(long[] a, long mod)`                          | -                  | long配列からMOD版を構築      |
| `LongPrefixModSum(LongArray a, long mod)`                       | -                  | `LongArray` からMOD版を構築  |
| `LongPrefixModSum.generate(int n, long mod, LongSupplier init)` | `LongPrefixModSum` | supplierからMOD版を構築      |

1D版は元の要素数を `public final int length` で公開し、MOD版は `mod` も公開します。

### 2. 1Dアクセス・区間和

| メソッド            | 戻り値の型                           | 説明                             |
|---------------------|--------------------------------------|----------------------------------|
| `get(int i)`        | `int` / `long`                       | 元配列のi番目を返す              |
| `sum()`             | `int` / `long`                       | 全要素の和を返す                 |
| `sum(int i)`        | `int` / `long`                       | 閉区間 `[0, i]` の和を返す       |
| `sum(int i, int j)` | `int` / `long`                       | 閉区間 `[i, j]` の和を返す       |
| `size()`            | `int`                                | 元配列の要素数を返す             |
| `iterator()`        | `PrimitiveIterator.OfInt` / `OfLong` | 元配列順に走査する               |
| `toString()`        | `String`                             | 元配列を半角スペース区切りで返す |

MOD版の `get` と各 `sum` は `[0, mod)` の値を返します。 1Dのint版は `int`、long版は `long` を返します。

### 3. 2Dコンストラクタ・生成メソッド

| メソッド                                                                 | 戻り値の型           | 説明                           |
|--------------------------------------------------------------------------|----------------------|--------------------------------|
| `IntPrefixSum2D(int h, int w, IntBinaryOperator init)`                   | -                    | `init(i, j)` で構築            |
| `IntPrefixSum2D(int[][] a)`                                              | -                    | int配列から構築                |
| `IntPrefixSum2D.generate(int h, int w, IntSupplier init)`                | `IntPrefixSum2D`     | supplierを行優先順に呼んで構築 |
| `LongPrefixSum2D(int h, int w, LongBinaryOperator init)`                 | -                    | `init(i, j)` で構築            |
| `LongPrefixSum2D(long[][] a)`                                            | -                    | long配列から構築               |
| `LongPrefixSum2D.generate(int h, int w, LongSupplier init)`              | `LongPrefixSum2D`    | long supplierから構築          |
| `IntPrefixModSum2D(int h, int w, int mod, IntBinaryOperator init)`       | -                    | `init(i, j)` でMOD版を構築     |
| `IntPrefixModSum2D(int[][] a, int mod)`                                  | -                    | int配列からMOD版を構築         |
| `IntPrefixModSum2D.generate(int h, int w, int mod, IntSupplier init)`    | `IntPrefixModSum2D`  | supplierからMOD版を構築        |
| `LongPrefixModSum2D(int h, int w, long mod, LongBinaryOperator init)`    | -                    | `init(i, j)` でMOD版を構築     |
| `LongPrefixModSum2D(long[][] a, long mod)`                               | -                    | long配列からMOD版を構築        |
| `LongPrefixModSum2D.generate(int h, int w, long mod, LongSupplier init)` | `LongPrefixModSum2D` | supplierからMOD版を構築        |

2D版は `public final int h, w, length` を公開し、MOD版は `mod` も公開します。

### 4. 2Dアクセス・矩形和

| メソッド                              | 戻り値の型     | 説明                                                     |
|---------------------------------------|----------------|----------------------------------------------------------|
| `get(int i, int j)`                   | `int` / `long` | 元配列の `(i, j)` を返す                                 |
| `sum()`                               | `long` / `int` | 全要素の和を返す。intのMOD版はint                        |
| `sum(int i, int j)`                   | `long` / `int` | 閉矩形 `[0, i] x [0, j]` の和を返す。intのMOD版はint     |
| `sum(int i1, int j1, int i2, int j2)` | `long` / `int` | 閉矩形 `[i1, i2] x [j1, j2]` の和を返す。intのMOD版はint |
| `toString()`                          | `String`       | 各行を半角スペース区切り、行間を改行して返す             |

### 5. 3Dコンストラクタ・生成メソッド

| メソッド                                                                        | 戻り値の型           | 説明                                      |
|---------------------------------------------------------------------------------|----------------------|-------------------------------------------|
| `IntPrefixSum3D(int d, int h, int w, IntSupplier init)`                         | -                    | supplierを `i`, `j`, `k` の順に呼んで構築 |
| `IntPrefixSum3D(int[][][] a)`                                                   | -                    | int配列から構築                           |
| `IntPrefixSum3D.generate(int d, int h, int w, IntSupplier init)`                | `IntPrefixSum3D`     | supplierから構築                          |
| `LongPrefixSum3D(int d, int h, int w, LongSupplier init)`                       | -                    | supplierを `i`, `j`, `k` の順に呼んで構築 |
| `LongPrefixSum3D(long[][][] a)`                                                 | -                    | long配列から構築                          |
| `LongPrefixSum3D.generate(int d, int h, int w, LongSupplier init)`              | `LongPrefixSum3D`    | supplierから構築                          |
| `IntPrefixModSum3D(int d, int h, int w, int mod, IntSupplier init)`             | -                    | supplierからMOD版を構築                   |
| `IntPrefixModSum3D(int[][][] a, int mod)`                                       | -                    | int配列からMOD版を構築                    |
| `IntPrefixModSum3D.generate(int d, int h, int w, int mod, IntSupplier init)`    | `IntPrefixModSum3D`  | supplierからMOD版を構築                   |
| `LongPrefixModSum3D(int d, int h, int w, long mod, LongSupplier init)`          | -                    | supplierからMOD版を構築                   |
| `LongPrefixModSum3D(long[][][] a, long mod)`                                    | -                    | long配列からMOD版を構築                   |
| `LongPrefixModSum3D.generate(int d, int h, int w, long mod, LongSupplier init)` | `LongPrefixModSum3D` | supplierからMOD版を構築                   |

3D版は `public final int d, h, w, length` を公開し、MOD版は `mod` も公開します。

### 6. 3Dアクセス・直方体和

| メソッド                                              | 戻り値の型     | 説明                                                            |
|-------------------------------------------------------|----------------|-----------------------------------------------------------------|
| `get(int i, int j, int k)`                            | `int` / `long` | 元配列の `(i, j, k)` を返す                                     |
| `sum()`                                               | `long` / `int` | 全要素の和を返す。intのMOD版はint                               |
| `sum(int i, int j, int k)`                            | `long` / `int` | 閉直方体 `[0, i] x [0, j] x [0, k]` の和を返す。intのMOD版はint |
| `sum(int i1, int j1, int k1, int i2, int j2, int k2)` | `long` / `int` | 指定した閉直方体の和を返す。intのMOD版はint                     |
| `toString()`                                          | `String`       | 最終軸を半角スペース区切り、各行を改行して返す                  |

## 利用例

```java
IntPrefixSum ps = new IntPrefixSum(new int[]{2, 1, 4, 3});

System.out.println(ps.sum());      // 10
System.out.println(ps.sum(2));     // 7
System.out.println(ps.sum(1, 3));  // 8
```

```java
IntPrefixSum2D ps = new IntPrefixSum2D(new int[][]{
	{1, 2, 3},
	{4, 5, 6}
});

System.out.println(ps.sum(0, 1, 1, 2)); // 2 + 3 + 5 + 6 = 16
```

```java
IntPrefixModSum3D ps = IntPrefixModSum3D.generate(d, h, w, 998244353, sc::nextInt);
int ans = ps.sum(i1, j1, k1, i2, j2, k2);
System.out.println(ps);
```

## 注意事項

- 各次元の長さが1以上で、配列が矩形または直方体である問題制約を前提とします。
- 1Dは `0 <= i <= j < length`、2D・3Dも各軸で同様の有効な閉区間を指定します。
- 半開区間 `[l, r)` は閉区間 `[l, r - 1]` へ変換して使用します。
- 1Dの `IntPrefixSum` は全ての累積値がint、その他の通常版はlongの範囲に収まる問題制約で使用します。
- MOD版は `mod > 0`、かつ全ての入力値が `0 <= value < mod` を満たす問題制約で使用します。
- MOD版は `mod` と途中の加減算がlongの範囲に十分収まる、通常の競技プログラミング用法を前提とします。
- 構築後の更新には対応しません。

## パフォーマンス特性

- 構築: 全次元で $\mathcal{O}(\text{要素数})$
- `get`, `sum`: $\mathcal{O}(1)$
- `toString`: $\mathcal{O}(\text{要素数})$
- 1D追加メモリ: $\mathcal{O}(n)$
- 2D追加メモリ: $\mathcal{O}((h + 1)(w + 1))$
- 3D追加メモリ: $\mathcal{O}((d + 1)(h + 1)(w + 1))$
- 全ての多次元版は累積値を一次元配列へ圧縮して保持

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                         |
|:-------------------|:-----------|:---------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-15 | 1Dの `IntPrefixSum` / `LongPrefixSum` を追加                                                 |
| **バージョン 2.0** | 2026-07-15 | 初期化関数コンストラクタとsupplierによる生成を追加                                           |
| **バージョン 3.0** | 2026-07-27 | 先頭ゼロ付きの内部表現と公開 `length`、1DのMOD版、2D・3D版、次元に応じた `toString()` を追加 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
