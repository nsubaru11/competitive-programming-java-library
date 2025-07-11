# 数学関数 (Mathematical Functions)

## 概要

競技プログラミングで頻繁に使用される様々な数学的関数とアルゴリズムを提供するライブラリです。組み合わせ論、数論、幾何学、多項式計算など、幅広い数学的操作をサポートしています。

## 実装クラス

### CombinatoricsUtils

- **用途**: 組み合わせ論関連の計算を行うユーティリティクラス
- **特徴**:
	- 組み合わせ数 (nCr) の計算（通常版とモジュラー版）
	- 順列数 (nPr) の計算（通常版とモジュラー版）
	- 重複組み合わせ (nHr) の計算
	- スターリング数（第2種）の計算
	- ベル数の計算

### NumberTheoryUtils

- **用途**: 数論関連の計算を行うユーティリティクラス
- **特徴**:
	- 最大公約数 (GCD) と最小公倍数 (LCM) の計算
	- 拡張ユークリッドアルゴリズム (exGCD)
	- オイラーのトーシェント関数
	- 素数判定
	- 素因数分解
	- モジュラー逆数の計算

### GeometryUtils

- **用途**: 幾何学的計算を行うユーティリティクラス
- **特徴**:
	- 線分の交差判定（2次元と3次元）
	- 点が線分上にあるかの判定
	- 長方形の交差判定
	- 点が図形内にあるかの判定
	- 各種距離計算（ユークリッド距離、マンハッタン距離、チェビシェフ距離）

### PowerUtils

- **用途**: べき乗計算を行うユーティリティクラス
- **特徴**:
	- 高速べき乗計算
	- モジュラーべき乗計算

### PolynomialUtils

- **用途**: 多項式計算を行うユーティリティクラス
- **特徴**:
	- 多項式の加算、減算、乗算
	- 多項式の評価

### DivisionUtils

- **用途**: 除算関連の計算を行うユーティリティクラス

### NumberFormatUtils

- **用途**: 数値のフォーマット変換を行うユーティリティクラス

### NumberPredicates

- **用途**: 数値の性質を判定するユーティリティクラス

## 主なメソッド

### CombinatoricsUtils

| メソッド                                | 説明                      |
|-------------------------------------|-------------------------|
| `comb(int n, int r, long mod)`      | nCrをmodで割った余りを計算        |
| `perm(long n, long r)`              | nPrを計算                  |
| `multiComb(int n, int r, long mod)` | 重複組み合わせnHrをmodで割った余りを計算 |
| `stirlingNumber2(int n, int k)`     | スターリング数（第2種）S(n,k)を計算   |
| `bellNumber(int n)`                 | ベル数B(n)を計算              |

### NumberTheoryUtils

| メソッド                                        | 説明                |
|---------------------------------------------|-------------------|
| `GCD(long x, long y)`                       | 最大公約数を計算          |
| `LCM(long x, long y)`                       | 最小公倍数を計算          |
| `exGCD(long a, long b, long[] x, long[] y)` | 拡張ユークリッドアルゴリズムを実行 |
| `EulerTotientFunction(long n)`              | オイラーのトーシェント関数を計算  |
| `isPrime(long n)`                           | 素数判定を行う           |
| `primeFactors(long n)`                      | 素因数分解を行う          |
| `modInverse(long a, long m)`                | モジュラー逆数を計算        |

### GeometryUtils

| メソッド                 | 説明          |
|----------------------|-------------|
| `crossLine(...)`     | 2次元の線分の交差判定 |
| `crossLine3D(...)`   | 3次元の線分の交差判定 |
| `euclidDist(...)`    | ユークリッド距離を計算 |
| `manhattanDist(...)` | マンハッタン距離を計算 |
| `chebyshevDist(...)` | チェビシェフ距離を計算 |

## 使用例

    // 組み合わせ数の計算
    long comb = CombinatoricsUtils.comb(10, 2, 1000000007);  // 10C2 mod 10^9+7
    
    // 最大公約数の計算
    long gcd = NumberTheoryUtils.GCD(24, 36);  // 12
    
    // 素数判定
    boolean isPrime = NumberTheoryUtils.isPrime(17);  // true
    
    // 2点間のユークリッド距離
    double dist = GeometryUtils.euclidDist(0, 0, 3, 4);  // 5.0

## 計算量

- 組み合わせ計算: O(r)、ここでrは選ぶ要素数
- GCD/LCM計算: O(log(min(a, b)))
- 素数判定: O(√n)
- 素因数分解: O(√n)
- 幾何学的計算: 操作によって異なる（多くはO(1)〜O(n)）