# Fast Input/Output (FastIO)

## 概要

競技プログラミングにおいて、入出力処理の速度は重要な要素です。
このライブラリでは、Javaの標準入出力よりも高速な入出力処理を提供するクラスを実装しています。
基本的な高速化はもちろん、競技プログラミングコンテスト向けの拡張機能も備えた高機能な入出力クラスを提供します。
**2025年9月のメジャーアップデート（v3.0）**
では、最新のベンチマーク結果に基づき、パフォーマンスの向上とAPIの全面的な刷新が行われました。

## 実装クラス

### [FastScanner](./src/FastScanner.java)

- **用途**: 標準入力からの高速な読み込みを提供するクラス。
- **特徴**:
	- バッファリングによる高速な入力処理。
	- 配列、行列、コレクション、マルチセットなど、競技プログラミングでよく使われるデータ構造の読み込みをサポート。
	- 入力の終端（EOF）を安全に扱う `hasNext()` / `peek()` を提供。
	- `AutoCloseable`対応による確実なリソース管理。
- **主な操作**:
	- `nextInt()`, `nextLong()`, `nextDouble()`: 各種数値型を読み込む。
	- `nextInt(n)`, `nextLong(n)`: n個の要素を配列として読み込む。
	- `nextIntMat(h, w)`: h×wの整数行列を読み込む。
	- `nextCollection(n, element, collection)`: n個の要素をコレクションとして読み込む。
	- `next()`: 文字列トークンを読み込む。
	- `hasNext()`: 次に読み込むべきトークンが存在するか判定する。
	- `peek()`: 次の非空白文字を消費せずに確認する。
- **時間計算量**：
	- 単一の読み込み操作: 平均 O(1)（バッファリングによる）
	- 配列等の読み込み操作: O(n)、ここでnは読み込む要素数
- **空間計算量**：O(B + n)、ここでBはバッファサイズ、nは読み込む要素数

### [FastPrinter](./src/FastPrinter.java)

- **用途**: 標準出力への高速な書き込みを提供するクラス。
- **特徴**:
	- **メソッドチェーン**による流れるような記述 (`fp.print(a).println(b)`) に対応。
	- バッファリングによる高速な出力処理。
	- 様々なデータ型（int, long, double, char, String, Object, BigInteger, BigDecimal）に加え、配列や行列、イテラブルの出力をサポート。
	- 区切り文字指定や変換関数適用など、柔軟な出力オプションを提供。
	- `AutoCloseable`インターフェースの実装によるリソース管理。
- **主な操作**:
	- `print()/println()`: 各種データ型、配列、行列、イテラブルの出力。
	- `print(arr, delimiter)`: 区切り文字付きの配列出力。
	- `print(iter, function)`: 変換関数を適用したイテラブルの出力。
	- `printf()`: フォーマット付き出力。
	- `flush()`: バッファの強制書き込み。
- **時間計算量**：
	- 単一の出力操作: 平均 O(1)（バッファリングによる）
	- 配列等の出力操作: O(n)、ここでnは出力する要素数
- **空間計算量**：O(B + n)、ここでBはバッファサイズ、nは出力する要素数

## 使い方

```java
try (FastScanner fs = new FastScanner(System.in); FastPrinter fp = new FastPrinter(System.out)) {
    int n = fs.nextInt();
    int[] a = fs.nextInt(n);
    fp.println(a);
}
```

## 注意事項

- すべてのクラスは`AutoCloseable`を実装しているため、`try-with-resources`文での使用を強く推奨します。
- `autoFlush=false`（デフォルト）の場合、`try-with-resources`を抜けるか、明示的に`close()`/`flush()`
	を呼び出さないとバッファの内容が出力されない点に注意してください。
- 以前のバージョンに存在した `ContestScanner/Printer` および `CompressedFastScanner/Printer` は、機能が `FastScanner/Printer` に統合されたため削除されました。