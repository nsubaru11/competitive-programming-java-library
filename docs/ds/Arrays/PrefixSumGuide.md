# IntPrefixSum / LongPrefixSum 利用ガイド

## 概要

[`IntPrefixSum`](../../../src/lib/ds/arrays/IntPrefixSum.java) と [`LongPrefixSum`](../../../src/lib/ds/arrays/LongPrefixSum.java) は、構築後に更新しない一次元配列の累積和を保持します。構築後は要素取得、全体和、任意の閉区間和を O(1) で求められます。

## 特徴

- 構築 O(n)、区間和 O(1)
- `IntPrefixSum` も累積値を `long[]` で保持し、`int` 配列の合計を `long` で返す
- プリミティブ配列、配列インターフェース、初期化関数、supplier から構築可能
- 元の要素列を `IntArray` / `LongArray` として再取得・反復可能
- 閉区間 `[i, j]` の和を直接取得

## 依存関係

- `java.util.PrimitiveIterator`
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntToLongFunction`
- `java.util.function.IntSupplier`
- `java.util.function.LongSupplier`
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)

## 主な機能（メソッド一覧）

### 1. コンストラクタ・生成メソッド

| メソッド                                               | 戻り値の型           | 説明                   |
|----------------------------------------------------|-----------------|----------------------|
| `IntPrefixSum(int n, IntUnaryOperator init)`       | -               | `init(i)` で n 要素を初期化 |
| `IntPrefixSum(int[] a)`                            | -               | `int[]` から構築         |
| `IntPrefixSum(IntArray a)`                         | -               | `IntArray` の論理順から構築  |
| `IntPrefixSum.generate(int n, IntSupplier init)`   | `IntPrefixSum`  | supplier を n 回呼んで構築  |
| `LongPrefixSum(int n, IntToLongFunction init)`     | -               | `init(i)` で n 要素を初期化 |
| `LongPrefixSum(long[] a)`                          | -               | `long[]` から構築        |
| `LongPrefixSum(LongArray a)`                       | -               | `LongArray` の論理順から構築 |
| `LongPrefixSum.generate(int n, LongSupplier init)` | `LongPrefixSum` | supplier を n 回呼んで構築  |

### 2. 要素・区間和メソッド

| メソッド                | 戻り値の型                                | 説明                 |
|---------------------|--------------------------------------|--------------------|
| `get(int i)`        | `int` / `long`                       | 元配列の `i` 番目を返す     |
| `sum()`             | `long`                               | 全要素の和を返す           |
| `sum(int i)`        | `long`                               | 閉区間 `[0, i]` の和を返す |
| `sum(int i, int j)` | `long`                               | 閉区間 `[i, j]` の和を返す |
| `size()`            | `int`                                | 元配列の要素数を返す         |
| `iterator()`        | `PrimitiveIterator.OfInt` / `OfLong` | 元配列の順に走査する         |

## 利用例

```java
IntPrefixSum ps = new IntPrefixSum(new int[]{2, 1, 4, 3});

System.out.println(ps.sum());      // 10
System.out.println(ps.sum(2));     // 2 + 1 + 4 = 7
System.out.println(ps.sum(1, 3));  // 1 + 4 + 3 = 8
```

```java
IntPrefixSum ps = IntPrefixSum.generate(n, sc::nextInt);
while (q-- > 0) {
	int l = sc.nextInt();
	int r = sc.nextInt();
	out.println(ps.sum(l, r - 1)); // 半開区間 [l, r)
}
```

## 注意事項

- 現在の実装は `n >= 1` の問題制約を前提とします。
- `get(i)` と `sum(i)` は `0 <= i < n` で呼び出します。
- `sum(i, j)` は `0 <= i <= j < n` で呼び出します。
- 区間指定は両端を含む閉区間です。半開区間 `[l, r)` には `sum(l, r - 1)` を使用します。
- `LongPrefixSum` は累積和が `long` の範囲に収まる問題制約で使用します。
- 構築後の更新には対応しません。

## パフォーマンス特性

- 構築: O(n)
- `get`, `sum`, `size`: O(1)
- 全走査: O(n)
- 使用メモリ: O(n)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                        |
|:--------------|:-----------|:------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-15 | 配列と `IntArray` / `LongArray` から構築し、`get`・閉区間 `sum`・反復を提供する初回実装                            |
| **バージョン 2.0** | 2026-07-15 | 初期化関数コンストラクタと `generate(int n, Supplier)` を追加し、`LongPrefixSum` の配列コンストラクタを `long[]` 対応へ修正 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
