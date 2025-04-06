# FastPrinter利用ガイド

## 概要

`FastPrinter` は、競技プログラミング向けに設計された高速出力ユーティリティクラスです。  
低レベルなバイト操作と内部バッファリングを用いてパフォーマンスを最大化し、標準的な出力方式よりも高速な処理が可能です。

## 特徴

- 内部バッファを利用した効率的な出力。
- 最小限のオブジェクト生成で、高速性を重視。
- `ASCII` 範囲内の文字出力をサポート。
- `AutoCloseable` インターフェースを実装しているため、`try-with-resources`構文に対応。

## 依存関係

- Java 標準の入出力ライブラリ（`OutputStream`）
- 静的インポート: `java.lang.Math`（`max`, `min`等の操作）

## 主な機能

### 1. 基本的な出力

- `print(...)` 系: 改行なしでデータ出力
- `println(...)` 系: 改行付きのデータ出力

### 2. バッファ制御

- 明示的なバッファのフラッシュ処理: `flush()`
- 自動フラッシュ設定: コンストラクタで `autoFlush` を指定可能。

### 3. コンストラクタ

| コンストラクタ                                                               | 説明                                                  |
|-----------------------------------------------------------------------|-----------------------------------------------------|
| `FastPrinter()`                                                       | デフォルト設定（バッファ64K、出力先 `System.out`、`autoFlush=false`） |
| `FastPrinter(OutputStream output)`                                    | 指定した出力ストリームを利用                                      |
| `FastPrinter(int bufferSize)`                                         | 指定サイズのバッファを使用                                       |
| `FastPrinter(OutputStream output, boolean autoFlush)`                 | 出力ストリームと `autoFlush` を明示的に指定                        |
| `FastPrinter(int bufferSize, boolean autoFlush)`                      | バッファサイズと `autoFlush` を指定                            |
| `FastPrinter(OutputStream output, int bufferSize, boolean autoFlush)` | 全てを指定                                               |

## メソッド詳細

| メソッド                | 説明                               |
|---------------------|----------------------------------|
| `void flush()`      | バッファを強制的にフラッシュ                   |
| `void close()`      | クラスを閉じてリソースを解放 (`AutoCloseable`) |
| `void print(...)`   | 各データ型に対応した改行なしの出力                |
| `void println(...)` | 改行付きの出力                          |

## 利用例

``` java
try (FastPrinter fp = new FastPrinter()) {
	fp.println("競技プログラミング用出力");
	fp.print(12345);
	fp.flush();
}
```

## 注意事項

1. `ASCII` 範囲外の文字はサポートされていません。
2. 出力先 `OutputStream` を未指定の場合、`System.out` が使用されます。

## パフォーマンス特性

- 計算量: 出力文字数に対して線形（`O(文字数)`）。
- 小型バッファサイズを使用する場合、効率がやや低下します。
