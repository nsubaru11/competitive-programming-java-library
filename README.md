# Java 競技プログラミング用ライブラリ

author: <https://atcoder.jp/users/nsubaru>

このリポジトリーは、Java で書かれた競技プログラミング向けの各種ライブラリを掲載しています。  
バグを含む可能性が高く、完全な動作は保証しません。あくまでライブラリ作成のヒントとしてご利用ください。

## クラス一覧

### [FastScanner](./FastIO/FastScanner.java)

- **用途**  
  - 標準入力（`InputStream`）を高速に処理するクラス
- **特徴**  
  - 内部バッファを使用して効率的に入力を読み込む  
  - ASCII 範囲外の文字には対応していない  
  - `AutoCloseable` を実装しているため、try-with-resources の利用が推奨される

#### 使用例
```java
try (FastScanner sc = new FastScanner()) {
    // 整数入力
    int intValue = sc.nextInt();
    // 長整数入力
    long longValue = sc.nextLong();
    // 小数入力
    double doubleValue = sc.nextDouble();
    // 文字入力
    char charValue = sc.nextChar();
    // 文字列入力
    String stringValue = sc.next();
} catch (Exception e) {
    throw new RuntimeException(e);
}
```

#### 実際の入出力例

【入力例】
```
42 9876543210
3.1415 Z
HelloWorld
```

【出力例】  
（各メソッドで読み込んだ値をそのまま出力した場合）
```
42
9876543210
3.1415
Z
HelloWorld
```

---

### [FastPrinter](/FastIO/FastPrinter.java)

- **用途**  
  - 標準出力（`OutputStream`）を高速に処理するクラス
- **特徴**  
  - 内部バッファに出力内容を蓄積し、バッファが満杯になったタイミングで指定の `OutputStream` に書き出す  
  - ASCII 範囲外の文字は取り扱わない  
  - `AutoCloseable` を実装しているため、try-with-resources の利用が推奨される

#### 使用例
```java
try (FastPrinter out = new FastPrinter()) {
    // 整数出力
    out.println(123);
    // 長整数出力
    out.println(12345678910L);
    // 小数出力
    out.println(78.9);
    // 文字出力
    out.println('A');
    // boolean 出力
    out.println(true);
    // 文字列出力
    out.println("Hello, World!");
} catch (Exception e) {
    throw new RuntimeException(e);
}
```

#### 実際の入出力例

【出力例】
```
123
12345678910
78.9
A
Yes
Hello, World!
```

---

### [ContestScanner](/FastIO/ContestScanner.java)

- **用途**  
  - FastScanner を拡張し、競技プログラミングでよく使用される入力形式の処理をサポートするユーティリティクラス
- **特徴**  
  - 以下の入力形式を高速に読み込む  
    - 一次元配列（整数、長整数、浮動小数点数、文字、文字列）
    - 二次元・三次元配列
    - ソート済み配列
    - 累積和配列
    - 逆写像配列（入力値に対応する元のインデックスを保持）
    - 各種コレクション（ArrayList、HashSet、TreeSet など）
  - 内部バッファを利用し、FastScanner 同様に効率的な入力処理が可能  
  - `AutoCloseable` を実装しているため、try-with-resources の利用が推奨される

#### 使用例
```java
try (ContestScanner cs = new ContestScanner(System.in)) {
    // 整数配列入力（例：要素数 5 の整数配列）
    int[] intArray = cs.nextInt(5);
    
    // ソート済み整数配列入力（例：要素数 5 の配列を読み込んでソート）
    int[] sortedIntArray = cs.nextSortedInt(5);
    
    // 2次元整数配列入力（例：行数 3, 列数 3 の行列）
    int[][] intMatrix = cs.nextIntMat(3, 3);
    
    // 逆写像配列入力（例：1-indexed の入力に対する逆写像）
    int[] inverseMapping = cs.nextIntInverseMapping(5);
    
    // 任意のコレクション入力（例：文字列の ArrayList）
    ArrayList<String> stringList = cs.nextStringAL(3);
} catch (Exception e) {
    throw new RuntimeException(e);
}
```

#### 実際の入出力例

【入力例】  
（各行はスペースまたは改行で区切られたデータ）
```
3 1 2 5 4
10 7 3 1 9
1 2 3
4 5 6
7 8 9
2 3 1 4 5
apple orange banana
```

【出力例】  
各メソッドで読み込んだ結果を確認すると、例えば：
- `nextInt(5)` の結果: `[3, 1, 2, 5, 4]`
- `nextSortedInt(5)` の結果: `[1, 3, 7, 9, 10]`
- `nextIntMat(3, 3)` の結果:
  ```
  [
    [1, 2, 3],
    [4, 5, 6],  // ※入力例はあくまでイメージです。実際は入力数と形状に注意
    [7, 8, 9]
  ]
  ```
- `nextIntInverseMapping(5)`（1-indexedの場合）の結果:  
  入力が `[2, 3, 1, 4, 5]` なら、各入力値から 1 を引いた位置に元の入力順序を記録して `[2, 0, 1, 3, 4]` のような配列になる

※ 入出力例はあくまで概念を示すものであり、実際の利用時は問題の仕様に合わせた入力形式となります。

---

### [ContestPrinter](/FastIO/ContestPrinter.java)

- **用途**  
  - FastPrinter を拡張し、競技プログラミングで頻繁に使用される出力形式の処理をサポートするユーティリティクラス
- **特徴**  
  - 配列や 2 次元配列の出力を簡便に行える（各要素間にスペース、各行は改行で区切る）  
  - 任意のオブジェクトは `toString()` を利用して出力可能  
  - 変換処理を適用した出力（関数適用後の配列出力など）にも対応  
  - null チェックを導入しているため、NullPointerException の発生リスクを低減  
  - `AutoCloseable` を実装しているため、try-with-resources の利用が推奨される

#### 使用例
```java
try (ContestPrinter cp = new ContestPrinter(System.out)) {
    // 整数配列出力（各要素間にスペース、最後に改行）
    int[] intArray = {1, 2, 3, 4, 5};
    cp.print(intArray);
    
    // 2次元整数配列出力（各行を改行で区切る）
    int[][] intMatrix = {
        {1, 2, 3},
        {4, 5, 6}
    };
    cp.println(intMatrix);
    
    // 任意のオブジェクト出力（toString() を利用）
    String message = "ContestPrinter output test.";
    cp.println(message);
    
    // ラムダ式を利用した変換出力（例：配列各要素を 2 倍して出力）
    cp.println(intArray, x -> x * 2);
} catch (Exception e) {
    throw new RuntimeException(e);
}
```

#### 実際の入出力例

【入力例】  
（ContestPrinter は出力用ユーティリティなので、入力例はなく、以下は出力内容の例）

【出力例】
```
1 2 3 4 5
1 2 3
4 5 6
ContestPrinter output test.
2
4
6
8
10
```

### [AbstractBinarySearch](/AbstractBinarySearch.java)

- **用途**  
  - 整数および長整数に対して、通常の二分探索、上限探索（Upper Bound）、下限探索（Lower Bound）を行うための抽象クラス
- **特徴**  
  - ユーザーは抽象メソッド `comparator(long n)` を実装することで、任意の探索条件を定義可能  
  - 通常探索（`normalSearch`）では、条件に一致する任意の値が見つかった時点で探索を終了する  
  - 上限探索（`upperBoundSearch`）では、条件に一致する範囲のうち最大のインデックスを返す（条件に合致する最後の値の位置）  
  - 下限探索（`lowerBoundSearch`）では、条件に一致する範囲のうち最小のインデックスを返す（条件に合致する最初の値の位置）  
  - 探索に失敗した場合、戻り値は「~挿入位置（ビット反転、すなわち -(挿入位置) - 1)」となる

#### 使用例

以下は、ソート済みの整数配列から特定の値（target）を探索する例です。  
※実際の使用には、対象となるデータ構造や条件に合わせて `comparator(long n)` を実装してください。
```java
int target = 7;
int[] intArray = {1, 2, 4, 7, 7, 8, 20};
AbstractBinarySearch binarySearch = new AbstractBinarySearch() {
    @Override
    protected int comparator(long l) {
        return Integer.compare(intArray[(int) l], target);
    }
};
int normal = binarySearch.normalSearch(0, intArray.length);
int upper = binarySearch.upperBoundSearch(0, intArray.length);
int lower = binarySearch.lowerBoundSearch(0, intArray.length);
System.out.println(normal);
System.out.println(lower);
System.out.println(upper);
```
【出力例】
```
3
3
4
```
