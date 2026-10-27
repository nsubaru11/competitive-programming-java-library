# SquareSumSegmentTree 利用ガイド

> `IntSquareSumSegmentTree` と `LongSquareSumSegmentTree` は、区間の一次和・二乗和を同時管理する専用セグメント木である。

[← README: SegmentTree](./README.md) · ソース: [IntSquareSumSegmentTree.java](../../../src/lib/ds/segmenttree/IntSquareSumSegmentTree.java) · [LongSquareSumSegmentTree.java](../../../src/lib/ds/segmenttree/LongSquareSumSegmentTree.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

更新はアフィン変換 `x -> a*x + b` を遅延伝播で処理し、すべて法 `mod` 付きで計算される。以下、`N` は要素数を表す。

## 特徴

- **一次和と二乗和を同時取得**:
	- `query` で一次和、`query2` で二乗和を取得できる。
- **区間アフィン更新を標準装備**:
	- `add` / `multiply` / `set` / `apply(a, b)` を点・区間の両方で提供する。
- **型別に2実装**:
	- `IntSquareSumSegmentTree`: `int` ベース。
	- `LongSquareSumSegmentTree`: `long` ベース。

## 依存関係

- `java.util`
- `java.util.function`

## API

### 1. コンストラクタ系メソッド

| メソッド                                          | 戻り値の型 | 説明                                        |
|---------------------------------------------------|------------|---------------------------------------------|
| `IntSquareSumSegmentTree(int n)`                  | `-`        | 要素数 `n`、既定法 `998244353` で構築する。 |
| `IntSquareSumSegmentTree(int n, int mod)`         | `-`        | 要素数 `n`、法 `mod` を指定して構築する。   |
| `IntSquareSumSegmentTree(int[] data)`             | `-`        | 配列初期値 + 既定法で構築する。             |
| `IntSquareSumSegmentTree(int[] data, int mod)`    | `-`        | 配列初期値 + 指定法で構築する。             |
| `LongSquareSumSegmentTree(int n)`                 | `-`        | 要素数 `n`、既定法 `998244353` で構築する。 |
| `LongSquareSumSegmentTree(int n, long mod)`       | `-`        | 要素数 `n`、法 `mod` を指定して構築する。   |
| `LongSquareSumSegmentTree(long[] data)`           | `-`        | 配列初期値 + 既定法で構築する。             |
| `LongSquareSumSegmentTree(long[] data, long mod)` | `-`        | 配列初期値 + 指定法で構築する。             |

### 2. 更新系メソッド

| メソッド                                          | 戻り値の型 | 説明                                       |
|---------------------------------------------------|------------|--------------------------------------------|
| `add(int i, int/long v)`                          | `void`     | 単一点に加算する。                         |
| `add(int l, int r, int/long v)`                   | `void`     | 区間加算を行う。                           |
| `multiply(int i, int/long v)`                     | `void`     | 単一点を乗算更新する。                     |
| `multiply(int l, int r, int/long v)`              | `void`     | 区間乗算更新を行う。                       |
| `set(int i, int v)`                              | `int`      | `IntSquareSumSegmentTree` の単一点を更新し、更新前の値を返す。 |
| `set(int i, long v)`                             | `long`     | `LongSquareSumSegmentTree` の単一点を更新し、更新前の値を返す。 |
| `set(int l, int r, int/long v)`                   | `void`     | 区間代入更新を行う。                       |
| `apply(int i, int/long a, int/long b)`            | `void`     | 単一点へ `x -> a*x + b` を適用する。       |
| `apply(int l, int r, int/long a, int/long b)`     | `void`     | 区間へ `x -> a*x + b` を適用する。         |
| `fill(int/long val)`                              | `void`     | 全要素を同一値で埋める。                   |
| `setAll(IntUnaryOperator/IntToLongFunction func)` | `void`     | インデックスごとの関数で全要素を設定する。 |

### 3. 取得系メソッド

| メソッド               | 戻り値の型   | 説明                           |
|------------------------|--------------|--------------------------------|
| `get(int i)`           | `int / long` | 位置 `i` の値を取得する。      |
| `query(int l, int r)`  | `int / long` | 区間 `[l, r)` の一次和を返す。 |
| `query2(int l, int r)` | `int / long` | 区間 `[l, r)` の二乗和を返す。 |
| `queryAll()`           | `int / long` | 全体の一次和を返す。           |
| `query2All()`          | `int / long` | 全体の二乗和を返す。           |

### 4. その他

| メソッド     | 戻り値の型          | 説明                       |
|--------------|---------------------|----------------------------|
| `size()`     | `int`               | 要素数を返す。             |
| `iterator()` | `PrimitiveIterator` | 要素を順に走査する。       |
| `toString()` | `String`            | 葉要素を空白区切りで返す。 |

## 利用例

準備中

## 制約・注意事項

- すべての演算は法 `mod` で処理される。
- 区間は半開区間 `[l, r)` である。
- `query2` は値そのものの和ではなく「二乗値の和」を返す。
- 単一点の `set` は、保留中の遅延操作を反映した旧値を `get` と同じく `[0, mod)` に正規化して返す。区間の `set` は `void` である。

## 性能特性

単一点の `set` は対象の葉への遅延伝播を1回行い、旧値の取得、一次和・二乗和の代入、祖先の再集約を行う。

- **時間計算量**:
	- 構築: $\mathcal{O}(N)$
	- 各更新（`add` / `multiply` / `set` / `apply`）: $\mathcal{O}(\log N)$
	- 各取得（`query` / `query2` / `get`）: $\mathcal{O}(\log N)$（`get` は遅延伝播込み）
- **空間計算量**:
	- $\mathcal{O}(N)$

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                       |
|--------------------|------------|------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-30 | `IntSquareSumSegmentTree` / `LongSquareSumSegmentTree` を初期実装（`query`/`query2` + 区間アフィン更新）。 |
| **バージョン 1.1** | 2026-10-07 | `LongSquareSumSegmentTree.setAll` の初期化関数を `IntToLongFunction` に変更。                              |
| **バージョン 2.0** | 2026-10-27 | 両クラスの単一点 `set` に更新前の値を返す機能を追加。葉の直接更新により、旧値取得のための重複した経路走査を回避。 |
