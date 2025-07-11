# ContestPrinter 利用ガイド

## 概要

`ContestPrinter` は、`FastPrinter` を拡張した高速出力ユーティリティです。  
配列データの出力、変換関数の適用出力など、競技プログラミングにおける多様な出力用途に対応します。

## 特徴

- **機能拡張**: `FastPrinter` の全機能に加え、配列やコレクションを簡単に出力するメソッドを提供します。
- **安全性**: `null` チェックを導入し、`NullPointerException`を未然に防ぎます。
- **多様な出力**: プリミティブ型の配列、オブジェクト配列、`Iterable`を実装したコレクションなど、多彩なデータ構造の出力に対応します。

## 依存関係

- `FastPrinter` クラス
- `java.util.function` パッケージの各種関数型インターフェース

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
| `println(double[] arr)`            | double配列の各要素を改行区切りで出力（改行付き）     |
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
| `print(double[] arr)`            | double配列の各要素を半角スペース区切りで出力（改行無し）     |
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
| `println(double[] arr, DoubleFunction<T> function)`     | double配列の各要素を指定された関数で変換し、改行区切りで出力  |
| `println(char[] arr, Function<Character, T> function)`  | char配列の各要素を指定された関数で変換し、改行区切りで出力    |
| `println(boolean[] arr, Function<Boolean, T> function)` | boolean配列の各要素を指定された関数で変換し、改行区切りで出力 |
| `println(String[] arr, Function<String, T> function)`   | String配列の各要素を指定された関数で変換し、改行区切りで出力  |

#### 改行無し

| メソッド                                                  | 説明                                     |
|-------------------------------------------------------|----------------------------------------|
| `print(int[] arr, IntFunction<T> function)`           | int配列の各要素を指定された関数で変換し、半角スペース区切りで出力     |
| `print(long[] arr, LongFunction<T> function)`         | long配列の各要素を指定された関数で変換し、半角スペース区切りで出力    |
| `print(double[] arr, DoubleFunction<T> function)`     | double配列の各要素を指定された関数で変換し、半角スペース区切りで出力  |
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
| `println(double[][] arr2d, DoubleFunction<T> function)`     | 二次元のdouble配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力  |
| `println(char[][] arr2d, Function<Character, T> function)`  | 二次元のchar配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力    |
| `println(boolean[][] arr2d, Function<Boolean, T> function)` | 二次元のboolean配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力 |
| `println(String[][] arr2d, Function<String, T> function)`   | 二次元のString配列の各要素を指定された関数で変換し、各行を半角スペース区切りで出力  |

### 8. char配列系メソッド

| メソッド                                                                  | 説明                                       |
|-----------------------------------------------------------------------|------------------------------------------|
| `printChars(char[] arr)`                                              | char配列の各要素を区切り文字無しで出力（改行無し）              |
| `printChars(char[] arr, Function<Character, Character> function)`     | char配列の各要素を指定された関数で変換し、区切り文字無しで出力        |
| `printChars(char[][] arr2d)`                                          | 二次元のchar配列を、各行を区切り文字無しで出力（各行末に改行）        |
| `printChars(char[][] arr2d, Function<Character, Character> function)` | 二次元のchar配列の各要素を指定された関数で変換し、各行を区切り文字無しで出力 |

### 9. Iterableオブジェクト出力メソッド

#### 改行付き

| メソッド                                                 | 説明                                         |
|------------------------------------------------------|--------------------------------------------|
| `println(Iterable<T> iter)`                          | イテラブルオブジェクトの各要素を改行区切りで出力（改行付き）             |
| `println(Iterable<T> iter, char delimiter)`          | イテラブルオブジェクトの各要素を指定した区切り文字で出力（改行付き）         |
| `println(Iterable<T> iter, Function<T, U> function)` | イテラブルオブジェクトの各要素を指定された関数で変換し、改行区切りで出力（改行付き） |

#### 改行無し

| メソッド                                                               | 説明                                             |
|--------------------------------------------------------------------|------------------------------------------------|
| `print(Iterable<T> iter)`                                          | イテラブルオブジェクトの各要素を半角スペース区切りで出力（改行無し）             |
| `print(Iterable<T> iter, char delimiter)`                          | イテラブルオブジェクトの各要素を指定した区切り文字で出力（改行無し）             |
| `print(Iterable<T> iter, Function<T, U> function)`                 | イテラブルオブジェクトの各要素を指定された関数で変換し、半角スペース区切りで出力（改行無し） |
| `print(Iterable<T> iter, Function<T, U> function, char delimiter)` | イテラブルオブジェクトの各要素を指定された関数で変換し、指定した区切り文字で出力（改行無し） |

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

- `FastPrinter` の注意事項がすべて適用されます。
- 関数変換系メソッドで、変換後のオブジェクトが`print`/`println`で想定されていない型の場合、そのオブジェクトの `toString()`
	メソッドが利用されます。

## パフォーマンス特性

- 基本的なパフォーマンスは `FastPrinter` に準じますが、関数変換やオブジェクトの型判定による若干のオーバーヘッドが存在します。

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                             |
|:--------------|:-----------|:---------------------------------------------------------------|
| **バージョン 1.0** | 2025-04-07 | 初期バージョンとしてファイルを新規作成しました。                                       |
| **バージョン 2.0** | 2025-05-23 | `double`型配列および`Iterable`オブジェクトの出力サポートを追加し、Object型の出力判定を拡張しました。 |
| **バージョン 2.1** | 2025-06-09 | 関数変換系の2次元配列出力メソッドにおける型シグネチャのバグを修正しました。                         |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
