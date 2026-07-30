# CombinatoricsUtils 利用ガイド（開発中）

## 概要

`CombinatoricsUtils` は組み合わせ論に関する計算を補助するユーティリティクラスです。二項係数（nCr）、順列（nPr）、重複組み合わせ（nHr）、スターリング数（第2種）、ベル数を効率的に計算できます。

## 特徴

- 基本的な演算のみを用い、外部ライブラリに依存せず、高速に計算を行います。
- モジュロ演算を含むメソッドを提供し、整数のオーバーフローを防ぎます。

## 依存関係

このクラスは標準ライブラリ以外に追加の依存関係はありません。

## 主な機能

### 組み合わせ（二項係数）

- n 種類のものから r 個選ぶ場合の数。
- `nCr(long n, long r)`: nCr を計算します。
- `nCr(long n, long r, long mod)`: nCr を指定した法（mod）で割った余りを計算します。

### 順列

- n 種類のものから r 個選んで並べる場合の数。
- `nPr(long n, long r)`: nPr を計算します。
- `nPr(long n, long r, long mod)`: nPr を指定した法で割った余りを計算します。

### 重複組み合わせ

- n 種類のものから重複を許して r 個選ぶ場合の数。
- `nHr(long n, long r)`: nHr を計算します。
- `nHr(long n, long r, long mod)`: nHr を指定した法で割った余りを計算します（注意:現在の実装はモジュロが適用されていません。改善が必要です）。

### 特殊な数列計算

- `stirlingNumber2(int n, int k)`: スターリング数（第2種）を計算します。
- `bellNumber(int n)`: ベル数を計算します。

### メソッド詳細

#### nCr(long n, long r)

| 引数 | 説明                 |
|------|----------------------|
| n, r | 二項係数のパラメータ |

**戻り値**  
nCr の結果 (`long` 型)

---

#### nCr(long n, long r, long mod)

| 引数 | 説明                 |
|------|----------------------|
| n, r | 二項係数のパラメータ |
| mod  | モジュロ演算の法     |

**戻り値**  
`nCr % mod` の結果 (`long` 型)

---

#### nPr(long n, long r)

| 引数 | 説明             |
|------|------------------|
| n, r | 順列のパラメータ |

**戻り値**  
nPr の結果 (`long` 型)

---

#### nPr(long n, long r, long mod)

| 引数 | 説明             |
|------|------------------|
| n, r | 順列のパラメータ |
| mod  | モジュロ演算の法 |

**戻り値**  
`nPr % mod` の結果 (`long` 型)

---

#### nHr(long n, long r)

| 引数 | 説明                       |
|------|----------------------------|
| n, r | 重複組み合わせのパラメータ |

**戻り値**  
nHr の結果 (`long` 型)

---

#### nHr(long n, long r, long mod)

| 引数 | 説明                       |
|------|----------------------------|
| n, r | 重複組み合わせのパラメータ |
| mod  | モジュロ演算の法           |

**戻り値**  
`nHr % mod` の結果。ただし現時点の実装はモジュロを考慮しておらず、修正が必要です。  
（注意事項を参照してください）

---

#### stirlingNumber2 (int n, int k)

| 引数 | 説明                                |
|------|-------------------------------------|
| n, k | スターリング数（第2種）のパラメータ |

**戻り値**  
S (n, k) の結果 (`long` 型)

---

#### bellNumber (int n)

| 引数 | 説明               |
|------|--------------------|
| n    | ベル数のパラメータ |

**戻り値**  
B (n) の結果 (`long` 型)

---

## 利用例

```java
// 単純な 10C3 を求める場合
long combination = CombinatoricsUtils.comb(10, 3);

// 10C3 mod 17 を求める場合
long modCombination = CombinatoricsUtils.modComb(10, 3, 17);

// ベル数を求める場合 (n=5)
long bellNum = CombinatoricsUtils.bellNumber(5);
```

## 注意事項

- ここでは愚直な方法で計算を行うため、オーバーフローする可能性があります。

## パフォーマンス特性

- 各関数の時間計算量:
	- comb / perm / multiComb：O (r)
	- stirlingNumber2：O (nk)
	- bellNumber：O (n^2)
- 各関数の空間計算量:
	- stirlingNumber2：O (n^2)
	- bellNumber：O (n^2)（二次元配列使用のため）

## バージョン情報
