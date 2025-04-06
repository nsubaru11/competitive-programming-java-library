# Java 競技プログラミング用ライブラリ

author: <https://atcoder.jp/users/nsubaru>

このリポジトリーは、Java で書かれた競技プログラミング向けの各種ライブラリを掲載しています。  
バグを含む可能性が高く、完全な動作は保証しません。あくまでライブラリ作成のヒントとしてご利用ください。

## クラス一覧

### [FastScanner](./FastIO/src/FastScanner.java)

- **用途**  
  - 標準入力（`InputStream`）を高速に処理するクラス
- **特徴**  
  - 内部バッファを使用して効率的に入力を読み込む  
  - ASCII 範囲外の文字には対応していない  
  - `AutoCloseable`を実装しているため、`try-with-resources`の利用が推奨される

---

### [FastPrinter](./FastIO/src/FastPrinter.java)

- **用途**  
  - 標準出力（`OutputStream`）を高速に処理するクラス
- **特徴**  
  - 内部バッファに出力内容を蓄積し、バッファが満杯になったタイミングで指定の`OutputStream`に書き出す  
  - ASCII 範囲外の文字は取り扱わない  
  - `AutoCloseable`を実装しているため、`try-with-resources`の利用が推奨される

---

### [ContestScanner](./FastIO/src/ContestScanner.java)

- **用途**  
  - `FastScanner`を拡張し、競技プログラミングでよく使用される入力形式の処理をサポートするユーティリティクラス
- **特徴**  
  - 以下の入力形式を高速に読み込む  
    - 一次元配列（整数、長整数、浮動小数点数、文字、文字列）
    - 二次元・三次元配列
    - ソート済み配列
    - 累積和配列
    - 逆写像配列（入力値に対応する元のインデックスを保持）
    - 各種コレクション（`ArrayList`、`HashSet`、`TreeSet`）
    - MultiSet（入力値の出現回数を保持）（`HashMap`, `TreeMap`, `int[]`, `char[]`）
  - 内部バッファを利用し、FastScanner 同様に効率的な入力処理が可能  
  - `AutoCloseable`を実装しているため、`try-with-resources`の利用が推奨される

---

### [ContestPrinter](./FastIO/src/ContestPrinter.java)

- **用途**  
  - FastPrinter を拡張し、競技プログラミングで頻繁に使用される出力形式の処理をサポートするユーティリティクラス
- **特徴**  
  - 配列や 2 次元配列の出力を簡便に行える（各要素間にスペース、各行は改行で区切る）  
  - 任意のオブジェクトは `toString()` を利用して出力可能  
  - 変換処理を適用した出力（関数適用後の配列出力など）にも対応  
  - null チェックを導入しているため、NullPointerException の発生リスクを低減  
  - `AutoCloseable` を実装しているため、try-with-resources の利用が推奨される

---

### [BinarySearch](./BinarySearch/src/BinarySearch.java)
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

### [ArrayBinarySearch](./BinarySearch/src/ArrayBinarySearch.java)
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
