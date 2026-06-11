# Fast Input/Output (FastIO17)

## 概要

`FastIO17` は、競技プログラミング向けに最適化した Java 17 用の高速入出力ライブラリです。
`FastScanner` と `FastPrinter` の 2 クラスを中心に、標準入出力へ直接つないだ既定コンストラクタ、配列・行列・コレクションの一括入出力、
`ContestScanner/Printer` 系から統合された拡張 API を提供します。

最新更新日: `2026/03/12`

`2026/03/12` の最新更新では、入力側に `0-index` 補助メソッドが追加され、`next()` / `nextLine()` / `nextDouble()`
の内部処理が見直されました。出力側では既定バッファサイズが 1 MiB に統一され、単発の大きな書き込み時はフラッシュ後に必要に応じてバッファを再確保する動作になっています。

## 実装クラス

### [FastScanner](./src/FastScanner.java)

- **用途**: 標準入力からの高速な読み込みを提供するクラス。
- **特徴**:
	- 既定コンストラクタ `new FastScanner()` で `FileDescriptor.in` を通じて標準入力へ接続。
	- 配列、行列、3 次元配列、各種コレクション、マルチセットなどをまとめて読み込み可能。
	- `hasNext()` / `peek()` により EOF と次トークンを安全に確認可能。
	- `nextInt0()` / `nextLong0()` とその配列版により、1-indexed 入力を 0-indexed に即変換可能。
	- `nextLine()` は `LF` / `CRLF` の両方を処理。
- **主な操作**:
	- `nextInt()`, `nextLong()`, `nextDouble()`, `nextChar()`, `next()`
	- `nextInt0()`, `nextLong0()`, `nextInt0(n)`, `nextLong0(n)`
	- `nextInt(n)`, `nextLong(n)`, `nextDouble(n)`
	- `nextIntMat(h, w)`, `nextLongMat(h, w)`, `nextCharMat(h, w)`
	- `nextCollection(n, element, collection)`
- **時間計算量**：
	- 単一の読み込み操作: 平均 O(1)
	- 配列・行列・コレクション読み込み: O(n)
- **空間計算量**：O(B + n)

### [FastPrinter](./src/FastPrinter.java)

- **用途**: 標準出力への高速な書き込みを提供するクラス。
- **特徴**:
	- 既定コンストラクタ `new FastPrinter()` で `FileDescriptor.out` を通じて標準出力へ接続。
	- **メソッドチェーン** に対応し、`fp.print(a).println(b)` のように記述可能。
	- `boolean` は `true/false` ではなく `Yes/No` として出力。
	- 配列、行列、`Iterable`、変換関数付き出力、反転出力、繰り返し出力をサポート。
	- 既定バッファは 1 MiB。容量不足時は一度フラッシュし、1 回の書き込みが現バッファより大きい場合のみ拡張。
	- Java 17 版では `sun.misc.Unsafe` を用いた低レベル最適化を採用。
- **主な操作**:
	- `print()/println()`: 各種データ型、配列、行列、イテラブルの出力。
	- `print(arr, delimiter)`: 区切り文字付き出力。
	- `print(iter, function)`: 変換関数を適用した出力。
	- `printRepeat(...)`, `printlnRepeat(...)`, `printReverse(...)`, `printlnReverse(...)`
	- `printf()`, `flush()`
- **時間計算量**：
	- 単一の出力操作: 平均 O(1)
	- 配列・行列・イテラブル出力: O(n)
- **空間計算量**：O(B + n)

## 使い方

```java
try (FastScanner fs = new FastScanner(); FastPrinter fp = new FastPrinter()) {
	int n = fs.nextInt();
	int[] a = fs.nextInt(n);
	int u = fs.nextInt0();
	int v = fs.nextInt0();
	fp.println(a).println(u, v);
}
```

## 注意事項

- すべてのクラスは `AutoCloseable` を実装しているため、`try-with-resources` の利用を推奨します。
- `autoFlush=false`（デフォルト）の場合、`flush()` または `close()` を呼ぶまでバッファ内容は出力されません。
- `close()` は内部で保持している入力・出力ストリームを閉じます。標準入出力を再利用するコードでは呼び出しタイミングに注意してください。
- 文字列入出力は `ASCII` 前提です。
- 以前の `ContestScanner/Printer` および `CompressedFastScanner/Printer` は、機能が `FastScanner/Printer`
  に統合されたため削除されました。
