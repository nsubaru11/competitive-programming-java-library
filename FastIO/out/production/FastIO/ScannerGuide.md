# FastScanner / ContestScanner 利用ガイド

## 1. FastScanner 利用ガイド

### 概要

`FastScanner` は、競技プログラミング向けの高速入力ユーティリティです。  
内部バッファと低レベルなバイト操作により、余分なオブジェクト生成を避け、効率的な入力処理を実現します。  
入力は半角スペース、改行、復帰文字で区切られていることを前提としています。

**注意:**
- ASCII 文字のみが正しく処理されます。
- 入力形式が想定外の場合、`RuntimeException` が発生する可能性があります。

### 主な機能

#### 基本的な入力メソッド
- `nextInt()`, `nextLong()`, `nextDouble()`, `nextChar()`, `next()`, `nextLine()`  
  基本的なデータ型の入力をサポートします。

#### その他の入力メソッド
- `nextStringBuilder()`: 空白区切りの文字列を `StringBuilder` として取得できます。
- `nextBigInteger()`, `nextBigDecimal()`: 大きな整数や小数の入力も可能です。

### 利用例

```java
FastScanner sc = new FastScanner();
int n = sc.nextInt();
String token = sc.next();
String line = sc.nextLine();
```

---

## 2. ContestScanner 利用ガイド

### 概要

`ContestScanner` は、`FastScanner` を拡張したクラスです。  
競技プログラミングで必要となる各種配列（1次元、2次元、3次元）、累積和、ソート済み配列、逆写像、コレクション入力など、多様な入力パターンをサポートしています。

**注意:**
- 入力は半角スペース、改行、復帰文字で区切られていることを前提としています。
- `FastScanner` と同様に ASCII 文字のみが正しく処理されます。

### 主な機能

#### 1. 拡張された配列入力
- **1次元配列:**  
  `nextInt(int n)`, `nextLong(int n)`, `nextDouble(int n)`, `nextChars(int n)`, `nextStrings(int n)`  
  指定された要素数の配列を一括で入力可能です。

#### 2. 多次元配列の入力
- **2次元配列:**  
  `nextIntMat(int h, int w)`, `nextLongMat(int h, int w)`, `nextDoubleMat(int h, int w)`
- **3次元配列:**  
  `nextInt3D(int x, int y, int z)`, `nextLong3D(int x, int y, int z)`

#### 3. ソート済み配列の入力
- `nextSortedInt(int n)`, `nextSortedLong(int n)`, `nextSortedDouble(int n)`  
- `nextSortedChars()`, `nextSortedChars(int n)`, `nextSortedStrings(int n)`  
  入力後に自動的にソートされた配列を返します。

#### 4. 累積和配列の入力
- **1次元:** `nextIntPrefixSum(int n)`, `nextLongPrefixSum(int n)`
- **2次元:** `nextIntPrefixSum(int h, int w)`, `nextLongPrefixSum(int h, int w)`
- **3次元:** `nextIntPrefixSum(int x, int y, int z)`, `nextLongPrefixSum(int x, int y, int z)`

#### 5. 逆写像配列の入力
- `nextIntInverseMapping(int n)`  
  入力値（1-indexed）から、0-indexed の逆写像を生成します。

#### 6. コレクションへの入力
- **整数のコレクション:**  
  `nextIntAL(int n)`, `nextIntHS(int n)`, `nextIntTS(int n)`
- **長整数のコレクション:**  
  `nextLongAL(int n)`, `nextLongHS(int n)`, `nextLongTS(int n)`
- **文字・文字列のコレクション:**  
  `nextCharacterAL(int n)`, `nextCharacterHS(int n)`, `nextCharacterTS(int n)`  
  `nextStringAL(int n)`, `nextStringHS(int n)`, `nextStringTS(int n)`
- **マルチセット（Map）:**  
  各データ型の出現回数をカウントしたマップも入力可能です。

### 利用例

```java
ContestScanner cs = new ContestScanner();

// 1次元配列の入力
int[] array = cs.nextInt(n);

// 2次元配列の入力
int[][] matrix = cs.nextIntMat(h, w);

// 累積和配列の入力
int[] prefixSum = cs.nextIntPrefixSum(n);

// コレクションへの入力
ArrayList<Integer> list = cs.nextIntAL(n);
HashSet<String> set = cs.nextStringHS(n);
```

### 注意点

- 入力が ASCII 文字に限定されるため、非 ASCII 文字の入力には注意してください。
- 入力フォーマットが予期せぬ場合、`RuntimeException` が発生する可能性があります。
- `ContestScanner` は、`FastScanner` の機能に加えて多数の便利な入力メソッドを提供していますが、その分コード量が増えており、理解するためにはソースコード内のコメントを併せて参照してください。

---
