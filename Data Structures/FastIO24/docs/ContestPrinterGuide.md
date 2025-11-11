# ContestPrinter 利用ガイド

## 概要

`ContestPrinter` は、競技プログラミングにおける多様な出力要求に特化した高速出力ユーティリティです。<br>
{@link FastPrinter} の基本機能に加え、配列やコレクションの柔軟な出力、繰り返し出力、逆順出力など、
競技プログラミングで頻出するパターンを効率的に扱うためのメソッドを提供します。<br>
<b>注意:</b> パフォーマンスを優先するため、ほとんどのメソッドで引数のnullチェックを行っていません。
nullが渡された場合、{@code NullPointerException} が発生する可能性があります。

## 特徴

- **機能拡張**: `FastPrinter` の全機能に加え、配列やコレクションを簡単に出力するメソッドを提供します。
- **最新APIの活用**: Java 24の `VarHandle` API を採用し、リフレクションを介さずにバッファへの高速なアクセスを実現しています。
- **動的なバッファ管理**: バッファが不足した場合、自動的に2の冪乗サイズに拡張されるため、大規模な出力でも `flush`
	の頻度を抑え、パフォーマンスを維持します。
- **多様な出力**: プリミティブ型の配列、オブジェクト配列、`Iterable`を実装したコレクションなど、多彩なデータ構造の出力に対応します。
- **柔軟な配列出力**: 配列の全体だけでなく、指定した範囲 `[from, to)` のみを出力するメソッドを提供します。
- **繰り返し出力**: `printRepeat` や `printlnRepeat` を使うことで、同じ文字や文字列を効率的に繰り返し出力できます。
- **逆順出力**: `printReverse` や `printlnReverse` を使えば、配列の要素を逆の順序で簡単に出力できます。
- **関数マッピング**: 出力する要素を、指定した関数でその場で変換しながら出力できます。
- **便利メソッド**: `boolean`値から "Yes"/"No" を出力するなど、競技プログラミングで便利なメソッドを備えています。

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

| メソッド                                     | 説明                               |
|------------------------------------------|----------------------------------|
| `println(boolean[] arr)`                 | boolean配列の各要素を改行区切りで出力（改行付き）     |
| `println(char[] arr)`                    | char配列の各要素を改行区切りで出力（改行付き）        |
| `println(int[] arr)`                     | int配列の各要素を改行区切りで出力（改行付き）         |
| `println(long[] arr)`                    | long配列の各要素を改行区切りで出力（改行付き）        |
| `println(double[] arr)`                  | double配列の各要素を改行区切りで出力（改行付き）      |
| `println(String[] arr)`                  | String配列の各要素を改行区切りで出力（改行付き）      |
| `println(Object... arr)`                 | 可変長のObject配列の各要素を改行区切りで出力（改行付き）  |
| `println(boolean[] arr, char delimiter)` | boolean配列の各要素を指定した区切り文字で出力（改行付き） |
| `println(char[] arr, char delimiter)`    | char配列の各要素を指定した区切り文字で出力（改行付き）    |
| `println(int[] arr, char delimiter)`     | int配列の各要素を指定した区切り文字で出力（改行付き）     |
| `println(long[] arr, char delimiter)`    | long配列の各要素を指定した区切り文字で出力（改行付き）    |
| `println(double[] arr, char delimiter)`  | double配列の各要素を指定した区切り文字で出力（改行付き）  |
| `println(String[] arr, char delimiter)`  | String配列の各要素を指定した区切り文字で出力（改行付き）  |

#### 改行無し

| メソッド                                   | 説明                                  |
|----------------------------------------|-------------------------------------|
| `print(boolean[] arr)`                 | boolean配列の各要素を半角スペース区切りで出力（改行無し）    |
| `print(char[] arr)`                    | char配列の各要素を半角スペース区切りで出力（改行無し）       |
| `print(int[] arr)`                     | int配列の各要素を半角スペース区切りで出力（改行無し）        |
| `print(long[] arr)`                    | long配列の各要素を半角スペース区切りで出力（改行無し）       |
| `print(double[] arr)`                  | double配列の各要素を半角スペース区切りで出力（改行無し）     |
| `print(String[] arr)`                  | String配列の各要素を半角スペース区切りで出力（改行無し）     |
| `print(Object... arr)`                 | 可変長のObject配列の各要素を半角スペース区切りで出力（改行無し） |
| `print(boolean[] arr, char delimiter)` | boolean配列の各要素を指定した区切り文字で出力（改行無し）    |
| `print(char[] arr, char delimiter)`    | char配列の各要素を指定した区切り文字で出力（改行無し）       |
| `print(int[] arr, char delimiter)`     | int配列の各要素を指定した区切り文字で出力（改行無し）        |
| `print(long[] arr, char delimiter)`    | long配列の各要素を指定した区切り文字で出力（改行無し）       |
| `print(double[] arr, char delimiter)`  | double配列の各要素を指定した区切り文字で出力（改行無し）     |
| `print(String[] arr, char delimiter)`  | String配列の各要素を指定した区切り文字で出力（改行無し）     |

### 5. 配列の範囲指定出力メソッド

| メソッド                                       | 説明                                    |
|--------------------------------------------|---------------------------------------|
| `println(boolean[] arr, int from, int to)` | boolean配列の指定範囲 `[from, to)` を改行区切りで出力 |
| `println(char[] arr, int from, int to)`    | char配列の指定範囲 `[from, to)` を改行区切りで出力    |
| `println(int[] arr, int from, int to)`     | int配列の指定範囲 `[from, to)` を改行区切りで出力     |
| `println(long[] arr, int from, int to)`    | long配列の指定範囲 `[from, to)` を改行区切りで出力    |
| `println(double[] arr, int from, int to)`  | double配列の指定範囲 `[from, to)` を改行区切りで出力  |
| `println(String[] arr, int from, int to)`  | String配列の指定範囲 `[from, to)` を改行区切りで出力  |
| `print(boolean[] arr, int from, int to)`   | boolean配列の指定範囲 `[from, to)` を空白区切りで出力 |
| `print(char[] arr, int from, int to)`      | char配列の指定範囲 `[from, to)` を空白区切りで出力    |
| `print(int[] arr, int from, int to)`       | int配列の指定範囲 `[from, to)` を空白区切りで出力     |
| `print(long[] arr, int from, int to)`      | long配列の指定範囲 `[from, to)` を空白区切りで出力    |
| `print(double[] arr, int from, int to)`    | double配列の指定範囲 `[from, to)` を空白区切りで出力  |
| `print(String[] arr, int from, int to)`    | String配列の指定範囲 `[from, to)` を空白区切りで出力  |

### 6. 繰り返し出力メソッド

| メソッド                               | 説明                                  |
|------------------------------------|-------------------------------------|
| `printRepeat(char c, int cnt)`     | 指定された文字 `c` を `cnt` 回繰り返して出力（改行無し）  |
| `printRepeat(String s, int cnt)`   | 指定された文字列 `s` を `cnt` 回繰り返して出力（改行無し） |
| `printlnRepeat(char c, int cnt)`   | 指定された文字 `c` と改行のペアを `cnt` 回繰り返して出力  |
| `printlnRepeat(String s, int cnt)` | 指定された文字列 `s` と改行のペアを `cnt` 回繰り返して出力 |

### 7. 逆順出力メソッド

| メソッド                            | 説明                        |
|---------------------------------|---------------------------|
| `printlnReverse(boolean[] arr)` | boolean配列の要素を逆順で、改行区切りで出力 |
| `printlnReverse(char[] arr)`    | char配列の要素を逆順で、改行区切りで出力    |
| `printlnReverse(int[] arr)`     | int配列の要素を逆順で、改行区切りで出力     |
| `printlnReverse(long[] arr)`    | long配列の要素を逆順で、改行区切りで出力    |
| `printlnReverse(double[] arr)`  | double配列の要素を逆順で、改行区切りで出力  |
| `printlnReverse(String[] arr)`  | String配列の要素を逆順で、改行区切りで出力  |
| `printlnReverse(Object[] arr)`  | Object配列の要素を逆順で、改行区切りで出力  |
| `printReverse(boolean[] arr)`   | boolean配列の要素を逆順で、空白区切りで出力 |
| `printReverse(char[] arr)`      | char配列の要素を逆順で、空白区切りで出力    |
| `printReverse(int[] arr)`       | int配列の要素を逆順で、空白区切りで出力     |
| `printReverse(long[] arr)`      | long配列の要素を逆順で、空白区切りで出力    |
| `printReverse(double[] arr)`    | double配列の要素を逆順で、空白区切りで出力  |
| `printReverse(String[] arr)`    | String配列の要素を逆順で、空白区切りで出力  |
| `printReverse(Object[] arr)`    | Object配列の要素を逆順で、空白区切りで出力  |

## 利用例

```java
import java.util.List;

// try-with-resources での利用を推奨
try (ContestPrinter out = new ContestPrinter()) {
    int[] numbers = {1, 2, 3, 4, 5};

    // 配列の範囲出力
    out.println(numbers, 1, 4, '-'); // "2-3-4"

    // 繰り返し出力
    out.printRepeat('*', 10).println(); // "**********"

    // 逆順出力
    out.printlnReverse(numbers); // 5, 4, 3, 2, 1 をそれぞれ改行

    // 関数マッピングと組み合わせた出力
    List<Integer> list = List.of(1, 2, 3);
    out.print(list, e -> String.format("[%d]", e), ", ").println(); // "[1], [2], [3]"
}
```

## 注意事項

- `FastPrinter` の注意事項がすべて適用されます。
- 関数変換系メソッドで、変換後のオブジェクトが`print`/`println`で想定されていない型の場合、そのオブジェクトの `toString()`
	メソッドが利用されます。

## パフォーマンス特性

- 基本的なパフォーマンスは `FastPrinter` に準じます。
- `VarHandle` の利用により、リフレクションを用いる旧バージョンよりも高速なバッファアクセスが可能です。
- バッファが不足した際に `flush` する代わりにバッファを拡張するため、システムコールの回数が削減され、特に大量の細切れデータを出力する際のパフォーマンスが向上しています。
- `write(String s)` や `printChars` などの内部実装では、ループ展開により一度に複数の文字を処理することで、ループのオーバーヘッドを削減しています。

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                                                 |
|:--------------|:-----------|:-------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-04-07 | 初期バージョンとしてファイルを新規作成しました。                                                                                           |
| **バージョン 2.0** | 2025-05-23 | `double`型配列および`Iterable`オブジェクトの出力サポートを追加し、Object型の出力判定を拡張しました。                                                     |
| **バージョン 2.1** | 2025-06-09 | 関数変換系の2次元配列出力メソッドにおける型シグネチャのバグを修正しました。                                                                             |
| **バージョン 3.0** | 2025-09-22 | `FastPrinter` v3.0に追従し、メソッドチェーン対応と内部ロジックの最適化を実施。                                                                   |
| **バージョン 4.0** | 2025-11-11 | `FastIO24`としてJava 24向けに全面的な刷新を実施。`sun.misc.Unsafe`から`VarHandle`への移行、動的バッファ拡張、配列の範囲指定・繰り返し・逆順出力など多数の機能追加とパフォーマンス改善。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
