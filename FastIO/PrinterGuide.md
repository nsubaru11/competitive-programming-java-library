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
