# SquareSumSegmentTree 利用ガイド

## 概要

`IntSquareSumSegmentTree` と `LongSquareSumSegmentTree` は、区間の一次和・二乗和を同時管理する専用セグメント木です。  
更新はアフィン変換 `x -> a*x + b` を遅延伝播で処理し、すべて法 `mod` 付きで計算されます。

## 特徴

- **一次和と二乗和を同時取得**:
	- `query` で一次和、`query2` で二乗和を取得できます。
- **区間アフィン更新を標準装備**:
	- `add` / `multiply` / `set` / `apply(a, b)` を点・区間の両方で提供します。
- **型別に2実装**:
	- `IntSquareSumSegmentTree`: `int` ベース。
	- `LongSquareSumSegmentTree`: `long` ベース。

## 依存関係

- `java.util`
- `java.util.function`

## 主な機能（メソッド一覧）

### 1. コンストラクタ系メソッド

| メソッド                                              | 戻り値の型 | 説明                              |
|---------------------------------------------------|-------|---------------------------------|
| `IntSquareSumSegmentTree(int n)`                  | `-`   | 要素数 `n`、既定法 `998244353` で構築します。 |
| `IntSquareSumSegmentTree(int n, int mod)`         | `-`   | 要素数 `n`、法 `mod` を指定して構築します。     |
| `IntSquareSumSegmentTree(int[] data)`             | `-`   | 配列初期値 + 既定法で構築します。              |
| `IntSquareSumSegmentTree(int[] data, int mod)`    | `-`   | 配列初期値 + 指定法で構築します。              |
| `LongSquareSumSegmentTree(int n)`                 | `-`   | 要素数 `n`、既定法 `998244353` で構築します。 |
| `LongSquareSumSegmentTree(int n, long mod)`       | `-`   | 要素数 `n`、法 `mod` を指定して構築します。     |
| `LongSquareSumSegmentTree(long[] data)`           | `-`   | 配列初期値 + 既定法で構築します。              |
| `LongSquareSumSegmentTree(long[] data, long mod)` | `-`   | 配列初期値 + 指定法で構築します。              |

### 2. 更新系メソッド

| メソッド                                              | 戻り値の型  | 説明                          |
|---------------------------------------------------|--------|-----------------------------|
| `add(int i, int/long v)`                          | `void` | 単一点に加算します。                  |
| `add(int l, int r, int/long v)`                   | `void` | 区間加算を行います。                  |
| `multiply(int i, int/long v)`                     | `void` | 単一点を乗算更新します。                |
| `multiply(int l, int r, int/long v)`              | `void` | 区間乗算更新を行います。                |
| `set(int i, int/long v)`                          | `void` | 単一点を代入更新します。                |
| `set(int l, int r, int/long v)`                   | `void` | 区間代入更新を行います。                |
| `apply(int i, int/long a, int/long b)`            | `void` | 単一点へ `x -> a*x + b` を適用します。 |
| `apply(int l, int r, int/long a, int/long b)`     | `void` | 区間へ `x -> a*x + b` を適用します。  |
| `fill(int/long val)`                              | `void` | 全要素を同一値で埋めます。               |
| `setAll(IntUnaryOperator/LongUnaryOperator func)` | `void` | インデックスごとの関数で全要素を設定します。      |

### 3. 取得系メソッド

| メソッド                   | 戻り値の型        | 説明                     |
|------------------------|--------------|------------------------|
| `get(int i)`           | `int / long` | 位置 `i` の値を取得します。       |
| `query(int l, int r)`  | `int / long` | 区間 `[l, r)` の一次和を返します。 |
| `query2(int l, int r)` | `int / long` | 区間 `[l, r)` の二乗和を返します。 |
| `queryAll()`           | `int / long` | 全体の一次和を返します。           |
| `query2All()`          | `int / long` | 全体の二乗和を返します。           |

### 4. その他

| メソッド         | 戻り値の型               | 説明              |
|--------------|---------------------|-----------------|
| `size()`     | `int`               | 要素数を返します。       |
| `iterator()` | `PrimitiveIterator` | 要素を順に走査します。     |
| `toString()` | `String`            | 葉要素を空白区切りで返します。 |

## 利用例

```java
IntSquareSumSegmentTree seg = new IntSquareSumSegmentTree(new int[]{1, 2, 3, 4});
seg.add(0, 4, 2);      // 全要素 +2
seg.multiply(1, 3, 3); // [1,3) を3倍
System.out.println(seg.query(0, 4));  // 一次和
System.out.println(seg.query2(0, 4)); // 二乗和
```

```java
LongSquareSumSegmentTree seg = new LongSquareSumSegmentTree(8, 1_000_000_007L);
seg.apply(0, 8, 2, 5); // x -> 2x + 5
System.out.println(seg.queryAll());
```

## 注意事項

- すべての演算は法 `mod` で処理されます。
- 区間は半開区間 `[l, r)` です。
- `query2` は値そのものの和ではなく「二乗値の和」を返します。

## パフォーマンス特性

- **時間計算量**:
	- 構築: $O(N)$
	- 各更新（`add` / `multiply` / `set` / `apply`）: $O(\log N)$
	- 各取得（`query` / `query2` / `get`）: $O(\log N)$（`get` は遅延伝播込み）
- **空間計算量**:
	- $O(N)$

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                         |
|:--------------|:-----------|:-------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-30 | `IntSquareSumSegmentTree` / `LongSquareSumSegmentTree` を初期実装（`query`/`query2` + 区間アフィン更新）。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
