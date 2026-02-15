# Java 競技プログラミング用ライブラリ

author: <https://atcoder.jp/users/nsubaru>

このリポジトリーは、Java で書かれた競技プログラミング向けの各種ライブラリを掲載しています。  
バグを含む可能性が高く、完全な動作は保証しません。あくまでライブラリ作成のヒントとしてご利用ください。

## クラス一覧

### [FastScanner (Java 17)](./Data%20Structures/FastIO17/src/FastScanner.java) / [FastScanner (Java 24)](./Data%20Structures/FastIO24/src/FastScanner.java)

- **用途**
	- 標準入力（`InputStream`）を高速に処理するクラス。
- **特徴**
	- 内部バッファを使用して効率的に入力を読み込む。
	- 競技プログラミングでよく使用される入力形式（配列、行列、コレクション等）の読み込みをサポート。
	- `AutoCloseable`を実装しているため、`try-with-resources`の利用が推奨される。
	- Java 17環境向けとJava 24環境向け（最新機能による最適化版）がある。

---

### [FastPrinter (Java 17)](./Data%20Structures/FastIO17/src/FastPrinter.java) / [FastPrinter (Java 24)](./Data%20Structures/FastIO24/src/FastPrinter.java)

- **用途**
	- 標準出力（`OutputStream`）を高速に処理するクラス。
- **特徴**
	- 内部バッファに出力内容を蓄積し、効率的に書き出す。
	- 配列や行列の出力、メソッドチェーンによる記述をサポート。
	- `AutoCloseable`を実装しているため、`try-with-resources`の利用が推奨される。
	- Java 17環境向けとJava 24環境向け（最新機能による最適化版）がある。

---

### [BinarySearch](./Algorithms/BinarySearch/src/BinarySearch.java)

- **用途**
	- 通常の二分探索や上限/下限探索を実行するための **汎用的な二分探索クラス**

- **特徴**
	- ユーザーが条件を指定するLambda関数またはメソッドを使用して、柔軟な探索が可能
	- **サポートする探索方法**
		- `normalSearch(n)`: 条件を満たす任意の要素のインデックスを取得
		- `lowerBoundSearch(n)`: 条件を満たす最も左側のインデックスを取得
		- `upperBoundSearch(n)`: 条件を満たす最も右側のインデックスを取得
	- **見つからなかった場合の戻り値**
		- 挿入位置を計算し、`-(挿入位置 + 1)` の形式で返す
	- 整数 (`int`) および長整数 (`long`) をサポート

### [ArrayBinarySearch](./Algorithms/BinarySearch/src/ArrayBinarySearch.java)

- **用途**
	- ソート済み配列に対する効率的な二分探索を提供するクラス

- **特徴**
	- **サポートする探索方法**（BinarySearch と同じ）
		- `normalSearch(target)`: 配列内で一致する任意のインデックスを取得
		- `lowerBoundSearch(target)`: 配列内で一致する範囲の最も左側のインデックスを取得
		- `upperBoundSearch(target)`: 配列内で一致する範囲の最も右側のインデックスを取得
	- **対応する配列型**
		- 整数 (`int[]`)
		- 長整数 (`long[]`)
		- 倍精度浮動小数点数 (`double[]`)
		- 比較可能な型 (`Comparable<T>`)
	- ソート済み配列を前提にしており、探索範囲の指定も可能
	- 見つからなかった場合は、`-(挿入位置 + 1)` の形式で挿入位置を返す

---
