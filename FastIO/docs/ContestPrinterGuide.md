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

- `void println(int a, int b)` - 2つの整数値を改行区切りで出力（改行付き）
- `void println(int a, long b)` - int値とlong値を改行区切りで出力（改行付き）
- `void println(long a, int b)` - long値とint値を改行区切りで出力（改行付き）
- `void println(long a, long b)` - 2つのlong値を改行区切りで出力（改行付き）
- `void println(long a, long b, char delimiter)` - 2つの整数値を指定した区切り文字で出力（改行付き）

#### 改行無し

- `void print(int a, int b)` - 2つの int 値を半角スペース区切りで出力
- `void print(int a, long b)` - int 値と long 値を半角スペース区切りで出力
- `void print(long a, int b)` - long 値と int 値を半角スペース区切りで出力
- `void print(long a, long b)` - 2つの long 値を半角スペース区切りで出力
- `void print(long a, long b, char delimiter)` - 2つの整数値を指定した区切り文字で出力

### 3. 配列出力メソッド

様々な型 {U: int, long, double, char, String, Object} の配列の出力を行います。

- `void println(U[] arr)` - 配列の要素を改行区切りで出力（行末改行）
- `void print(U[] arr)` - 配列の要素を半角スペース区切りで出力
- `void println(U[] arr, char delimiter)` - 配列の要素を指定した区切り文字で出力（行末改行）
- `void print(U[] arr, char delimiter)` - 配列の要素を指定した区切り文字で出力

### 4. 変換関数を用いた出力

- `<T, U> void println(T[] arr, Function<T, U> mapper)` - 配列の各要素に変換関数を適用して出力（改行区切り）
- `<T, U> void print(T[] arr, Function<T, U> mapper)` - 配列の各要素に変換関数を適用して出力（半角スペース区切り）

### 5. char配列を文字列として出力

- `void printChars(char[] arr)` - 配列の各要素区切り文字無しで出力
- `void printChars(char[][] arr)` - 配列の各要素区切り文字無しで出力（各行改行付き）

## 利用例

``` java
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
