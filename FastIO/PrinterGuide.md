# FastScanner / ContestScanner 利用ガイド

## 概要

`FastScanner` は、競技プログラミング向けの高速入力ユーティリティです。  
内部バッファと低レベルなバイト操作により、余分なオブジェクト生成を避け、効率的な入力処理を実現します。  
また、`ContestScanner` は `FastScanner` を拡張して、1次元・2次元・3次元配列、累積和、ソート済み配列、逆写像、各種コレクションなど、多様な入力パターンをサポートします。

**注意:**
- このクラスは ASCII 文字のみを正しく処理します。
- 入力は半角スペース、改行、復帰文字で区切られていることを前提としています。
- 一部のメソッドでは、入力の終了時や予期しないフォーマットの場合に `RuntimeException` が発生する可能性があります。

## 主な機能

### 1. 基本的な入力メソッド
- `nextInt()`, `nextLong()`, `nextDouble()`, `nextChar()`, `next()`, `nextLine()`  
  基本的なデータ型の入力をサポートします。

### 2. 拡張された配列入力
- `nextInt(int n)`, `nextLong(int n)`, `nextDouble(int n)`, `nextChars(int n)`, `nextStrings(int n)`  
  指定された要素数の配列を一括で入力可能です。

### 3. 多次元配列の入力
- `nextIntMat(int h, int w)`, `nextLongMat(int h, int w)`, `nextDoubleMat(int h, int w)`  
  2次元配列を、行数と列数を指定して入力できます。
- `nextInt3D(int x, int y, int z)`, `nextLong3D(int x, int y, int z)`  
  3次元配列を入力できます。

### 4. ソート済み配列の入力
- `nextSortedInt(int n)`, `nextSortedLong(int n)`, `nextSortedDouble(int n)`  
  入力後に自動的にソートされた配列を返します。
- `nextSortedChars()`, `nextSortedChars(int n)`, `nextSortedStrings(int n)`  
  文字・文字列配列も同様にソートして返します。

### 5. 累積和配列の入力
- `nextIntPrefixSum(int n)`, `nextLongPrefixSum(int n)`  
  一次元の累積和配列を入力します。
- `nextIntPrefixSum(int h, int w)`, `nextLongPrefixSum(int h, int w)`  
  2次元の累積和配列（サイズ: (h+1) x (w+1)）を入力します。
- `nextIntPrefixSum(int x, int y, int z)`, `nextLongPrefixSum(int x, int y, int z)`  
  3次元の累積和配列（サイズ: (x+1) x (y+1) x (z+1)）を入力します。

### 6. 逆写像配列の入力
- `nextIntInverseMapping(int n)`  
  入力が 1-indexed の整数に対して、逆写像（0-indexed）を生成します。

### 7. コレクションの入力
- **整数のコレクション:**  
  `nextIntAL(int n)`, `nextIntHS(int n)`, `nextIntTS(int n)`  
  ArrayList、HashSet、TreeSet への入力をサポートします。
- **長整数のコレクション:**  
  `nextLongAL(int n)`, `nextLongHS(int n)`, `nextLongTS(int n)`
- **文字および文字列のコレクション:**  
  `nextCharacterAL(int n)`, `nextCharacterHS(int n)`, `nextCharacterTS(int n)`  
  `nextStringAL(int n)`, `nextStringHS(int n)`, `nextStringTS(int n)`
- **マルチセット（Map）:**  
  各データ型（整数、長整数、文字、文字列）の出現回数をカウントしたマップを入力するメソッドも用意されています。

## 利用例

### 基本的な入力
```java
FastScanner scanner = new FastScanner();
int n = scanner.nextInt();
String token = scanner.next();
```

### 配列の入力
```java
ContestScanner cs = new ContestScanner();
int[] array = cs.nextInt(n);
int[][] matrix = cs.nextIntMat(h, w);
```

### 累積和配列の入力
```java
int[] prefixSum = cs.nextIntPrefixSum(n);
```

### コレクションへの入力
```java
ArrayList<Integer> list = cs.nextIntAL(n);
HashSet<String> set = cs.nextStringHS(n);
```

## 注意点
- 入力が ASCII 文字に限定されるため、非 ASCII 文字を扱う場合は注意してください。
- 入力が想定外の形式の場合、`RuntimeException` が発生する可能性があります。
- `FastScanner` は競技プログラミング向けに最適化されており、入力の高速性を重視しているため、デバッグ用途には向かない場合があります。

---

# FastPrinter / ContestPrinter 利用ガイド

## 概要

`FastPrinter` は、競技プログラミング向けの高速出力ユーティリティです。  
低レベルなバイト操作と内部バッファリングにより、余分なオブジェクト生成を避け、非常に高速な出力を実現します。  
また、`ContestPrinter` は `FastPrinter` を拡張して、配列出力や関数変換付き出力など、様々な出力パターンをサポートします。

**注意:**
- このクラスはASCII文字のみを扱います。
- 内部バッファサイズは最低64バイトが保証され、必要に応じて自動的に flush() が呼ばれます。
- 高速化を重視しているため、標準の文字列変換や例外処理よりもパフォーマンスを優先しています。

## 主な機能

### 1. 基本的な出力メソッド
- `print(int)`, `print(long)`, `print(double)`, `print(char)`, `print(boolean)`, `print(String)`, `print(Object)`  
  改行なしで出力します。
- `println(...)` 系は、出力後に改行を追加します。

### 2. ペア出力メソッド
- `print(a, b)` および `println(a, b)`  
  2 つの整数値を半角スペースや指定の区切り文字で出力します。

### 3. 小数点指定出力
- `print(double d, int n)` および `println(double d, int n)`  
  指定された小数点以下桁数に四捨五入して出力します。

### 4. 1次元・2次元配列出力
- 各種配列（`int[]`, `long[]`, `char[]`, `boolean[]`, `String[]`）を改行区切りまたは半角スペース区切りで出力するメソッドを多数用意しています。
- また、関数を用いて各要素を変換しながら出力するメソッドも用意されています。

### 5. printf 系メソッド
- `printf(String format, Object... args)` および `printf(Locale locale, String format, Object... args)`  
  指定したフォーマットに従って文字列を生成し、出力します。

## 利用例

### 基本的な出力
```java
FastPrinter printer = new FastPrinter();
printer.println("Hello, world!");
printer.print(123);
printer.println(456);
printer.printf("Value: %.2f", 3.14159);
