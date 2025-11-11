# Fast Input/Output (FastIO) for Java 24

## 概要

このライブラリでは、Javaの標準入出力よりも高速な入出力処理を提供する複数のクラスを実装しています。
基本的な高速入出力から、競技プログラミングコンテスト向けの拡張機能を提供します。
この`FastIO24`では、`FastIO17`をJava 24向けに最適化し、最新のJava機能を活用してパフォーマンスとAPIの改善を図ったものです。

## 実装クラス

### [FastScanner](./src/FastScanner.java)

- **用途**: 標準入力からの高速な読み込みを提供する基本クラス。
- **特徴**:
	- バッファリングによる高速な入力処理。
	- 入力の終端（EOF）を安全に扱う `hasNext()` / `peek()` を提供。
	- `AutoCloseable`対応による確実なリソース管理。
- **主な操作**:
	- `nextInt()`, `nextLong()`, `nextDouble()`: 各種数値型を読み込む。
	- `next()`: 文字列トークンを読み込む。
	- `hasNext()`: 次に読み込むべきトークンが存在するか判定する。
	- `peek()`: 次の非空白文字を消費せずに確認する。
- **時間計算量**：
	- 各読み込み操作: 平均 O(1)（バッファリングによる）
- **空間計算量**：O(B)、ここでBはバッファサイズ

### [FastPrinter](./src/FastPrinter.java)

- **用途**: 標準出力への高速な書き込みを提供する基本クラス。
- **特徴**:
	- **メソッドチェーン**による流れるような記述 (`fp.print(a).println(b)`) に対応。
	- バッファリングによる高速な出力処理。
	- 様々なデータ型（int, long, double, char, String, Object, BigInteger, BigDecimal）の出力をサポート
	- AutoCloseableインターフェースの実装によるリソース管理
- **主な操作**:
	- `print()/println()`: 各種データ型の出力
	- `printf()`: フォーマット付き出力
	- `flush()`: バッファの強制書き込み
- **時間計算量**：
	- 各出力操作: 平均 O(1)（バッファリングによる）
- **空間計算量**：O(B)、ここでBはバッファサイズ

### [ContestScanner](./src/ContestScanner.java)

- **用途**：競技プログラミングコンテスト向けに拡張された入力クラス
- **特徴**：
	- FastScannerの全機能を継承
	- 配列、行列、コレクション、マルチセットなど、競技プログラミングでよく使われるデータ構造の読み込みをサポート
	- ソート済み配列や累積和の直接生成など、便利な機能を多数搭載
- **主な操作**:
	- `nextInt(n)`: n個の整数を配列として読み込む
	- `nextIntMat(h, w)`: h×wの整数行列を読み込む
	- `nextCollection(n, element, coolection)`: n個の要素をコレクションとして読み込む
	- `nextMultiset(n, element, map)`: n個の要素をマルチセットとして読み込む
- **時間計算量**：
	- 各読み込み操作: O(n)、ここでnは読み込む要素数
- **空間計算量**：O(n + B)、ここでnは読み込む要素数、Bはバッファサイズ

### [ContestPrinter](./src/ContestPrinter.java)

- **用途**：競技プログラミングコンテスト向けに拡張された出力クラス
- **特徴**：
	- FastPrinterの全機能を継承
	- 配列、行列、イテラブル、変換関数など、競技プログラミングでよく使われるデータ構造の出力をサポート
	- 区切り文字指定や変換関数適用など、柔軟な出力オプションを提供
- **主な操作**:
	- `print()/println()`: 各種データ型、配列、行列、イテラブルの出力
	- `print(arr, delimiter)`: 区切り文字付きの配列出力
	- `print(iter, function)`: 変換関数を適用したイテラブルの出力
- **時間計算量**：
	- 各出力操作: O(n)、ここでnは出力する要素数
- **空間計算量**：O(n + B)、ここでnは出力する要素数、Bはバッファサイズ

### [CompressedFastScanner](./src/CompressedFastScanner.java) / [CompressedFastPrinter](./src/CompressedFastPrinter.java)

- **用途**：コードサイズを最小化した圧縮版の入出力クラス
- **特徴**：
	- ContestScanner/ContestPrinterと同等の機能を提供
	- コードが圧縮されており、コードサイズ制限のある競技プログラミングプラットフォームに適している
	- 可読性よりもコードサイズを優先
- **時間計算量**：ContestScanner/ContestPrinterと同等
- **空間計算量**：ContestScanner/ContestPrinterと同等

## 選択ガイド

- **FastScanner/FastPrinter**: 基本的な高速入出力が必要な場合に使用。シンプルで理解しやすい。
- **ContestScanner/ContestPrinter**: 競技プログラミングコンテストで、配列や行列、コレクションなどの複雑なデータ構造を扱う場合に使用。多機能で便利。
- **CompressedFastScanner/CompressedFastPrinter**:
	コードサイズ制限が厳しいプラットフォームでの競技プログラミングに使用。機能はContestScanner/ContestPrinterと同等だが、コードサイズが小さい。

## 注意事項

- すべてのクラスは`AutoCloseable`を実装しているため、`try-with-resources`文での使用を強く推奨します。
- `autoFlush=false`（デフォルト）の場合、`try-with-resources`を抜けるか、明示的に`close()`/`flush()`
	を呼び出さないとバッファの内容が出力されない点に注意してください。
- CompressedFastScanner/CompressedFastPrinterは可読性よりもコードサイズを優先しているため、デバッグや保守が難しい場合があります。