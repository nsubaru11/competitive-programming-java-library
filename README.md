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

### [AbstractBinarySearch](./BinarySearch/src/AbstractBinarySearch.java)
- **用途**  
  - 整数および長整数に対する **汎用的な二分探索のための抽象クラス**  

- **特徴**  
  - ユーザーは `comparator(long n)` を実装することで、**自由に探索条件を定義** できる  
  - **三種類の探索をサポート**  
    - `normalSearch(n)`: 条件を満たす **任意の要素のインデックス** を返す  
    - `lowerBoundSearch(n)`: 条件を満たす範囲の **最も左側のインデックス** を返す  
    - `upperBoundSearch(n)`: 条件を満たす範囲の **最も右側のインデックス** を返す  
  - **探索対象は整数 (`int` or `long`)**
  - **見つからなかった場合の戻り値**  
    - `~挿入位置`（`-(挿入位置) - 1`）を返す  
    - **例**: `5` を `[1, 3, 7, 9]` で探索 → 挿入位置 `2` → 戻り値 `-3`  

---

### [ArrayBinarySearch](./BinarySearch/src/ArrayBinarySearch.java)
- **用途**  
  - **ソート済み配列に対する高速な二分探索を提供**  

- **特徴**  
  - **三種類の探索をサポート**（AbstractBinarySearch と同じ）  
    - `normalSearch(n)`: **任意の要素のインデックス** を返す  
    - `lowerBoundSearch(n)`: **最も左側のインデックス** を返す  
    - `upperBoundSearch(n)`: **最も右側のインデックス** を返す  
  - **探索対象はソート済みの `int[]` 配列**  
  - **探索範囲を指定可能**
  - **見つからなかった場合の戻り値**  
    - `~挿入位置`（`-(挿入位置) - 1`）を返す  

---
