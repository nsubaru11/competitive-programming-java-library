# 📚 Java 畳み込み（Convolution）ライブラリ API仕様書

## 1. 基本設計思想（アーキテクチャ）

本ライブラリは、「外部公開API層（Convolution層）」**と**「内部変換器層（Transform層）」の厳密な2層アーキテクチャを採用します。

* **関心事の分離:** 「配列のパディング（メモリ確保）」と「代数的変換（バタフライ演算）」を完全に分離し、パディングサイズの計算ミスによる論理欠陥を構造的に防ぎます。
* **パフォーマンス至上主義（GC抑制）:** 最も重いループが回る内部変換器層では、オブジェクトのインスタンス化（`new`
  ）を一切禁止し、すべて引数で渡された配列のインプレース（直接書き換え）処理とします。
* **オーバーヘッドの排除:** 関数型インターフェースやポリモーフィズムによるメソッド呼び出しのオーバーヘッドを避けるため、各変換アルゴリズムは独立した
  `static` メソッドとして実装し、JITコンパイラによるインライン展開を最大限に引き出します。

---

## 2. クラス構造

すべてのメソッドを保持する単一のユーティリティクラス `Convolution` として定義します。インスタンス化は不要（不可）とします。

```java
public final class Convolution {
    private Convolution() {} // インスタンス化の禁止
    
    // ... メソッド群 ...
}

```

---

## 3. レイヤー1：外部公開API（Convolution層）

ユーザーがコンテスト中に直接呼び出すメソッド群です。
ここでは「演算の種類に応じた適切なパディング（2の累乗サイズの計算）」を行い、内部変換器を呼び出した後、要素ごとの積を取り、逆変換して結果を返します。

### 3.1 加法的畳み込み（多項式乗算）

* **インデックス演算:** $i + j = k$
* **要求パディングサイズ:** $|A| + |B| - 1$ 以上の最小の2の累乗。

```java
/**
 * NTT (Number Theoretic Transform) を用いた多項式乗算
 * @param a 多項式Aの係数配列
 * @param b 多項式Bの係数配列
 * @param mod NTT素数 (例: 998244353)
 * @return 乗算結果の係数配列 (長さは |A| + |B| - 1)
 */
public static long[] multiplyNtt(long[] a, long[] b, int mod)

/**
 * 任意Modにおける多項式乗算 (Garnerのアルゴリズム併用)
 * @param mod 任意の素数 (例: 1000000007)
 */
public static long[] multiplyArbitraryMod(long[] a, long[] b, int mod)

/**
 * FFT (Fast Fourier Transform) を用いた多項式乗算 (実数版)
 */
public static double[] multiplyFft(double[] a, double[] b)

```

### 3.2 ビットごとの畳み込み

* **インデックス演算:** $i \oplus j = k$, $i \ \& \ j = k$, $i \mid j = k$
* **要求パディングサイズ:** $\max(|A|, |B|)$ 以上の最小の2の累乗。（桁上がりが発生しないため和は不要）

```java
/** XOR 畳み込み (NimのGrundy数計算などに使用) */
public static long[] convoluteXor(long[] a, long[] b, int mod)

/** AND 畳み込み (上位集合の共通部分) */
public static long[] convoluteAnd(long[] a, long[] b, int mod)

/** OR 畳み込み (部分集合の和集合) */
public static long[] convoluteOr(long[] a, long[] b, int mod)

```

### 3.3 数論的畳み込み

* **インデックス演算:** $\gcd(i, j) = k$, $\text{lcm}(i, j) = k$
* **要求パディングサイズ:** $\max(|A|, |B|)$。（2の累乗への切り上げは不要）

```java
/** GCD 畳み込み (最大公約数が k になる組み合わせ) */
public static long[] convoluteGcd(long[] a, long[] b, int mod)

/** LCM 畳み込み (最小公倍数が k になる組み合わせ) */
public static long[] convoluteLcm(long[] a, long[] b, int mod)

```

---

## 4. レイヤー2：内部変換器（Transform層）

外部APIからのみ呼び出される、アクセス修飾子 `private` のメソッド群です。
与えられた配列を直接書き換えます。

### 4.1 加法系（バタフライ演算ベース）

```java
// NTT順変換・逆変換 (isInverse で回転因子の正負を反転)
private static void transformNtt(long[] a, boolean isInverse, int mod)

// FFT順変換・逆変換 (複素数を実部と虚部の2配列で管理してオブジェクト生成を回避)
private static void transformFft(double[] real, double[] imag, boolean isInverse)

```

### 4.2 ビット系（FWHT / 高速ゼータ・メビウス変換）

```java
// XOR変換 (順変換・逆変換は対称なため isInverse で最後の除算のみ分岐)
private static void transformFwht(long[] a, boolean isInverse, int mod)

// OR変換 (部分集合・非対称なため順逆を分割)
private static void transformSubsetZeta(long[] a, int mod)     // 順変換
private static void transformSubsetMobius(long[] a, int mod)   // 逆変換

// AND変換 (上位集合・非対称なため順逆を分割)
private static void transformSupersetZeta(long[] a, int mod)   // 順変換
private static void transformSupersetMobius(long[] a, int mod) // 逆変換

```

### 4.3 数論系（エラトステネスの篩ベース）

```java
// GCD変換 (倍数系)
private static void transformMultipleZeta(long[] a, int mod)
private static void transformMultipleMobius(long[] a, int mod)

// LCM変換 (約数系)
private static void transformDivisorZeta(long[] a, int mod)
private static void transformDivisorMobius(long[] a, int mod)

```
