# MathUtils 利用ガイド

## 概要

`MathUtils`は、一回限りの整数演算、数論、階乗、組み合わせ計算を集約した静的ユーティリティです。繰り返し問い合わせ用の状態は保持しません。

## 特徴

- `int` / `long`の整数結果を返すべき乗、平方根、立方根
- 2値・3値・可変長の最小値／最大値
- binary GCD、LCM、拡張Euclid、Eulerのトーシェント関数
- 前計算を作らない階乗・組み合わせ計算
- `PrimeUtils`へ委譲する素数判定

## 依存関係

- `java.lang.Math`
- `lib.math.PrimeUtils`

## 主な機能（メソッド一覧）

### 基本整数演算

| メソッド                                |         戻り値 | 説明       |
|-----------------------------------------|---------------:|------------|
| `min(int/long a, b)`、3引数版、可変長版 | `int` / `long` | 最小値     |
| `max(int/long a, b)`、3引数版、可変長版 | `int` / `long` | 最大値     |
| `diff(int/long a, b)`                   | `int` / `long` | 差の絶対値 |
| `pow(long a, int b)`                    |         `long` | 整数乗     |
| `sqrt(int/long n)`                      | `int` / `long` | 床平方根   |
| `cbrt(int/long n)`                      |          `int` | 床立方根   |
| `isSquare(int/long n)`                  |      `boolean` | 平方数判定 |
| `isCube(int/long n)`                    |      `boolean` | 立方数判定 |

### 剰余演算

| メソッド                           |         戻り値 | 説明                     |
|------------------------------------|---------------:|--------------------------|
| `modPow(int a, int b, int mod)`    |          `int` | 繰り返し二乗法           |
| `modPow(long a, long b, long mod)` |         `long` | long版                   |
| `modInv(int/long a, mod)`          | `int` / `long` | Fermatの小定理による逆元 |

### 数論

| メソッド                         |         戻り値 | 説明                    |
|----------------------------------|---------------:|-------------------------|
| `gcd(int/long a, b)`             | `int` / `long` | binary GCD              |
| `lcm(int/long a, b)`             | `int` / `long` | 最小公倍数              |
| `exgcd(int/long a, b, x, y)`     | `int` / `long` | Bézout係数を配列へ格納  |
| `eulerTotient(int/long n)`       | `int` / `long` | Eulerのトーシェント関数 |
| `isPrime(long n)`                |      `boolean` | 決定的な試し割り判定    |
| `isProbablePrime(long n, int k)` |      `boolean` | 確率的素数判定          |

### 階乗・組み合わせ

| メソッド                           |         戻り値 | 説明                                    |
|------------------------------------|---------------:|-----------------------------------------|
| `fact(int n)`                      |         `long` | `0 <= n <= 20`の階乗                    |
| `modFact(int n, int/long mod)`     | `int` / `long` | `n! mod mod`                            |
| `invFact(int n, int/long mod)`     | `int` / `long` | 階乗のmod逆元                           |
| `primeExponent(int/long n, prime)` | `int` / `long` | `n!`に含まれる素数の指数                |
| `nCr(long n, int r)`、mod版        |         `long` | 二項係数                                |
| `nPr(long n, int r)`、mod版        |         `long` | 順列数                                  |
| `nHr(long n, int r)`、mod版        |         `long` | 重複組み合わせ                          |
| `stirlingNumber2(int n, int k)`    |         `long` | Stirling数（第2種）                     |
| `bellNumber(int n)`                |         `long` | Bell数                                  |

## 利用例

```java
import lib.math.MathUtils;

long power = MathUtils.pow(3, 10);
long combination = MathUtils.nCr(20, 5);
int gcd = MathUtils.gcd(84, 30);
```

## 注意事項

- 整数演算のオーバーフローは呼び出し側の制約で避けます。
- `modInv`、`invFact`、mod付き`nCr`は素数modを前提とします。
- `modPow`、`modFact`、mod付き`nPr`自体は素数modを必要としません。
- 同じmodで多数の組み合わせを求める場合は`FactorialTable`を使用します。
- `stirlingNumber2`は`S(0, 0) = 1`、`S(n, 0) = 0 (n > 0)`として扱います。

## パフォーマンス特性

- `min`、`max`、`diff`、`fact`: $\mathcal{O}(1)$
- `pow`、`modPow`、`gcd`、`exgcd`: $\mathcal{O}(\log N)$
- `modFact`: $\mathcal{O}(n)$
- `nCr`、`nPr`、`nHr`: $\mathcal{O}(r)$
- `eulerTotient`、`isPrime`: $\mathcal{O}(\sqrt{n})$
- `stirlingNumber2`: $\mathcal{O}(nk)$時間、$\mathcal{O}(n^2)$空間
- `bellNumber`: $\mathcal{O}(n^2)$時間・空間

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                      |
|:-------------------|:-----------|:------------------------------------------|
| **バージョン 1.0** | 2026-08-01 | 整数演算・組み合わせ・数論の静的APIを実装 |
