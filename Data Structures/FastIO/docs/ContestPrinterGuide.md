# ContestPrinter 利用ガイド

## 概要

`ContestPrinter` は、`FastPrinter` を拡張した高速出力ユーティリティです。  
配列データの出力、変換関数の適用出力など、競技プログラミングにおける多様な出力用途に対応します。

## 特徴

- `FastPrinter` で提供される高速出力機能を継承
- 配列、ペアデータの簡単な出力メソッドを追加
- nullチェックを導入し、意図しない`NullPointerException`を防止
- `AutoCloseable`を実装し、try-with-resourcesでの使用が可能

## 依存関係

- `FastPrinter` クラスを継承
- Java標準ライブラリの関数型インターフェース: `Function<T, R>`, `IntFunction<R>`, `LongFunction<R>`

## 主な機能

### 1. コンストラクタ

| コンストラクタ                                                               | 説明                                                      |
|-----------------------------------------------------------------------|---------------------------------------------------------|
| `ContestPrinter()`                                                    | デフォルト設定（バッファ容量:65536バイト、出力先:System.out、autoFlush:false） |
| `ContestPrinter(OutputStream out)`                                    | 出力先を指定（バッファ容量:65536バイト、autoFlush:false）                 |
| `ContestPrinter(int bufferSize)`                                      | バッファ容量を指定（出力先:System.out、autoFlush:false）               |
| `ContestPrinter(boolean autoFlush)`                                   | autoFlushを指定（バッファ容量:65536バイト、出力先:System.out）            |
| `ContestPrinter(OutputStream out, int bufferSize)`                    | 出力先とバッファ容量を指定（autoFlush:false）                          |
| `ContestPrinter(OutputStream out, boolean autoFlush)`                 | 出力先とautoFlushを指定（バッファ容量:65536バイト）                       |
| `ContestPrinter(int bufferSize, boolean autoFlush)`                   | バッファ容量とautoFlushを指定（出力先:System.out）                     |
| `ContestPrinter(OutputStream out, int bufferSize, boolean autoFlush)` | すべての設定を指定                                               |

### 2. ペア出力メソッド

#### 改行付き

| メソッド                                      | 説明                        |
|-------------------------------------------|---------------------------|
| `println(int a, int b)`                   | 2つの整数値を改行区切りで出力（改行付き）     |
| `println(int a, long b)`                  | int値とlong値を改行区切りで出力（改行付き） |
| `println(long a, int b)`                  | long値とint値を改行区切りで出力（改行付き） |
| `println(long a, long b)`                 | 2つのlong値を改行区切りで出力（改行付き）   |
| `println(long a, long b, char delimiter)` | 2つの整数値を指定した区切り文字で出力（改行付き） |

#### 改行無し

| メソッド                                    | 説明                            |
|-----------------------------------------|-------------------------------|
| `print(int a, int b)`                   | 2つの整数値を半角スペース区切りで出力（改行無し）     |
| `print(int a, long b)`                  | int値とlong値を半角スペース区切りで出力（改行無し） |
| `print(long a, int b)`                  | long値とint値を半角スペース区切りで出力（改行無し） |
| `print(long a, long b)`                 | 2つのlong値を半角スペース区切りで出力（改行無し）   |
| `print(long a, long b, char delimiter)` | 2つの整数値を指定した区切り文字で出力（改行無し）     |

### 3. 小数系メソッド

| メソッド                       | 説明                                 |
|----------------------------|------------------------------------|
| `println(double d, int n)` | double値を指定された小数点以下桁数で出力（四捨五入、改行付き） |
| `print(double d, int n)`   | double値を指定された小数点以下桁数で出力（四捨五入、改行無し） |

### 4. 1次元配列出力メソッド

#### 改行付き

| メソッド                               | 説明                              |
|------------------------------------|---------------------------------|
| `println(int[] arr)`               | int配列の各要素を改行区切りで出力（改行付き）        |
| `println(long[] arr)`              | long配列の各要素を改行区切りで出力（改行付き）       |
| `println(char[] arr)`              | char配列の各要素を改行区切りで出力（改行付き）       |
| `println(boolean[] arr)`           | boolean配列の各要素を改行区切りで出力（改行付き）    |
| `println(String[] arr)`            | String配列の各要素を改行区切りで出力（改行付き）     |
| `println(Object... arr)`           | 可変長のObject配列の各要素を改行区切りで出力（改行付き） |
| `println(U[] arr, char delimiter)` | 配列の各要素を指定した区切り文字で出力（改行付き）       |

#### 改行無し

| メソッド                             | 説明                                  |
|----------------------------------|-------------------------------------|
| `print(int[] arr)`               | int配列の各要素を半角スペース区切りで出力（改行無し）        |
| `print(long[] arr)`              | long配列の各要素を半角スペース区切りで出力（改行無し）       |
| `print(char[] arr)`              | char配列の各要素を半角スペース区切りで出力（改行無し）       |
| `print(boolean[] arr)`           | boolean配列の各要素を半角スペース区切りで出力（改行無し）    |
| `print(String[] arr)`            | String配列の各要素を半角スペース区切りで出力（改行無し）     |
| `print(Object... arr)`           | 可変長のObject配列の各要素を半角スペース区切りで出力（改行無し） |
| `print(U[] arr, char delimiter)` | 配列の各要素を指定した区切り文字で出力（改行無し）           |

### 5. 1次元配列の関数変換系メソッド

#### 改行付き

| メソッド                                                    | 説明                                 |
|---------------------------------------------------------|------------------------------------|
| `println(int[] arr, IntFunction<T> function)`           | int配列の各要素を指定された関数で変換し、改行区切りで出力     |
| `println(long[] arr, LongFunction<T> function)`         | long配列の各要素を指定された関数で変換し、改行区切りで出力    |
| `println(char[] arr, Function<Character, T> function)`  | char配列の各要素を指定された関数で変換し、改行区切りで出力    |
| `println(boolean[] arr, Function<Boolean, T> function)` | boolean配列の各要素を指定された関数で変換し、改行区切りで出力 |
| `println(String[] arr, Function<String, T> function)`   | String配列の各要素を指定された関数で変換し、改行区切りで出力  |

#### 改行無し

| メソッド                                                  | 説明                                     |
|-------------------------------------------------------|----------------------------------------|
| `print(int[] arr, IntFunction<T> function)`           | int配列の各要素を指定された関数で変換し、半角スペース区切りで出力     |
| `print(long[] arr, LongFunction<T> function)`         | long配列の各要素を指定された関数で変換し、半角スペース区切りで出力    |
| `print(char[] arr, Function<Character, T> function)`  | char配列の各要素を指定された関数で変換し、半角スペース区切りで出力    |
| `print(boolean[] arr, Function<Boolean, T> function)` | boolean配列の各要素を指定された関数で変換し、半角スペース区切りで出力 |
| `print(String[] arr, Function<String, T> function)`   | String配列の各要素を指定された関数で変換し、半角スペース区切りで出力  |

### 6. 2次元配列出力メソッド

| メソッド                                   | 説明                                     |
|----------------------------------------|----------------------------------------|
| `println(int[][] arr2d)`               | 二次元のint配列を、各行を半角スペース区切りで出力（各行末に改行）     |
| `println(long[][] arr2d)`              | 二次元のlong配列を、各行を半角スペース区切りで出力（各行末に改行）    |
| `println(char[][] arr2d)`              | 二次元のchar配列を、各行を半角スペース区切りで出力（各行末に改行）    |
| `println(boolean[][] arr2d)`           | 二次元のboolean配列を、各行を半角スペース区切りで出力（各行末に改行） |
| `println(String[][] arr2d)`            | 二次元のString配列を、各行を半角スペース区切りで出力（各行末に改行）  |
| `println(Object[][] arr2d)`            | 二次元のObject配列を、各行を半角スペース区切りで出力（各行末に改行）  |
| `println(U[][] arr2d, char delimiter)` | 二次元配列を、各行を指定した区切り文字で出力（各行末に改行）         |

### 7. 2次元配列の関数変換系メソッド

| メソッド                                                        | 説明                                            |
|-------------------------------------------------------------|-----------------------------------------------|
| `println(int[][] arr2d, IntFunction<T> function)`           | 二次元のint配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力     |
| `println(long[][] arr2d, LongFunction<T> function)`         | 二次元のlong配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力    |
| `println(char[][] arr2d, LongFunction<T> function)`         | 二次元のchar配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力    |
| `println(boolean[][] arr2d, Function<Boolean, T> function)` | 二次元のboolean配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力 |
| `println(String[][] arr2d, Function<String, T> function)`   | 二次元のString配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力  |

### 8. char配列系メソッド

| メソッド                                                                  | 説明                                       |
|-----------------------------------------------------------------------|------------------------------------------|
| `printChars(char[] arr)`                                              | char配列の各要素を区切り文字無しで出力（改行無し）              |
| `printChars(char[] arr, Function<Character, Character> function)`     | char配列の各要素を指定された関数で変換し、区切り文字無しで出力        |
| `printChars(char[][] arr2d)`                                          | 二次元のchar配列を、各行を区切り文字無しで出力（各行末に改行）        |
| `printChars(char[][] arr2d, Function<Character, Character> function)` | 二次元のchar配列の各要素を指定された関数で変換し、各行を区切り文字無しで出力 |

## 利用例

```java
int[] numbers = {1, 2, 3, 4, 5};
int a = 10, b = 20;

try (ContestPrinter cp = new ContestPrinter()) {
	// 配列の出力
	cp.println(numbers);  // 1, 2, 3, 4, 5を各行区切りで出力

	// ペアの出力
	cp.println(a, b);     // 10と20を改行区切りで出力

	// 区切り文字を指定した出力
	cp.print(numbers);  // 1 2 3 4 5と半角スペース区切りで出力

	// 変換関数を用いた出力
	Integer[] nums = {1, 2, 3};
	cp.println(nums, n -> n * n);  // 1, 4, 9を改行区切りで出力
}
```

## 注意事項

1. 変換関数に`null`を指定すると`NullPointerException`が発生します
2. `autoFlush`を`false`に設定している場合、明示的に`flush()`を呼び出す必要があります
3. バッファ容量は64バイト未満の場合、内部的に64バイトに調整されます

## パフォーマンス特性

- 内部バッファを利用した高速な出力処理
- 配列出力: 要素数に比例した線形時間（O(n)）

## バージョン情報

- 初期バージョン: `FastPrinter`を拡張し、配列やペアの出力機能を追加
