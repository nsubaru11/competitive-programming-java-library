# FastScanner 利用ガイド

## 概要

`FastScanner` は、競技プログラミング向けに設計された高速入力クラスです。  
内部バッファを利用して `InputStream`（既定では `FileDescriptor.in` 経由の標準入力）から効率的に読み込みを行い、通常の
`Scanner` より低オーバーヘッドで動作します。
`FastIO24` 版では Java 24 の `VarHandle` を活用した数値パース最適化を採用しつつ、`ContestScanner` /
`CompressedFastScanner` 系の機能も統合しています。

## 特徴

- **効率的な入力**: バッファリングと `VarHandle` ベースの低レベル最適化により高速に入力を処理します。
- **多機能**: 1 次元〜3 次元配列、ソート済み配列、累積和、逆写像、各種コレクション、マルチセットを一括生成できます。
- **0-index 補助**: `nextInt0()` / `nextLong0()` とその配列版で、1-indexed 入力を即座に 0-indexed 化できます。
- **EOF/状態確認**: `hasNext()` と `peek()` により、終端判定や先読みが可能です。
- **行入力対応**: `nextLine()` は `LF` / `CRLF` の両改行形式を処理します。
- **文字コード**: `ASCII` 範囲内の文字入力を前提としています。
- **リソース管理**: `AutoCloseable` を実装しており、`try-with-resources` に対応します。

## 依存関係

- Java 標準の入出力ライブラリ（`InputStream`, `FileDescriptor`）
- `java.lang.invoke.VarHandle`
- `java.math.BigInteger`, `java.math.BigDecimal`
- `java.util.*`, `java.util.function.*`

## 主な機能

### 基本的な入力メソッド

| メソッド                  | 戻り値の型           | 説明                                 |
|-----------------------|-----------------|------------------------------------|
| `nextInt()`           | `int`           | 次の整数値を読み込みます。                      |
| `nextLong()`          | `long`          | 次の長整数値を読み込みます。                     |
| `nextDouble()`        | `double`        | 次の浮動小数点値を読み込みます。                   |
| `nextChar()`          | `char`          | 次の非空白 1 文字を読み込みます。                 |
| `next()`              | `String`        | 次のトークンを文字列として読み込みます。               |
| `nextStringBuilder()` | `StringBuilder` | 次のトークンを `StringBuilder` として読み込みます。 |
| `nextLine()`          | `String`        | 次の 1 行を読み込みます（改行文字は含みません）。         |
| `nextBigInteger()`    | `BigInteger`    | 次の整数トークンを `BigInteger` として読み込みます。  |
| `nextBigDecimal()`    | `BigDecimal`    | 次の数値トークンを `BigDecimal` として読み込みます。  |

### 0-index 補助メソッド

| メソッド               | 戻り値の型    | 説明                                  |
|--------------------|----------|-------------------------------------|
| `nextInt0()`       | `int`    | `nextInt() - 1` を返します。              |
| `nextLong0()`      | `long`   | `nextLong() - 1` を返します。             |
| `nextInt0(int n)`  | `int[]`  | 各要素を 1 減らした長さ `n` の `int[]` を返します。  |
| `nextLong0(int n)` | `long[]` | 各要素を 1 減らした長さ `n` の `long[]` を返します。 |

### 配列入力メソッド

| メソッド                             | 戻り値の型        | 説明                             |
|----------------------------------|--------------|--------------------------------|
| `nextInt(int n)`                 | `int[]`      | 長さ `n` の整数配列を読み込みます。           |
| `nextLong(int n)`                | `long[]`     | 長さ `n` の長整数配列を読み込みます。          |
| `nextDouble(int n)`              | `double[]`   | 長さ `n` の浮動小数点配列を読み込みます。        |
| `nextChars()`                    | `char[]`     | 次のトークンを `char[]` として読み込みます。    |
| `nextChars(int n)`               | `char[]`     | 非空白文字を `n` 個読み込みます。            |
| `nextStrings(int n)`             | `String[]`   | 長さ `n` の文字列配列を読み込みます。          |
| `nextIntMat(int h, int w)`       | `int[][]`    | `h × w` の整数行列を読み込みます。          |
| `nextLongMat(int h, int w)`      | `long[][]`   | `h × w` の長整数行列を読み込みます。         |
| `nextDoubleMat(int h, int w)`    | `double[][]` | `h × w` の浮動小数点行列を読み込みます。       |
| `nextCharMat(int h, int w)`      | `char[][]`   | `h × w` の文字行列を読み込みます。          |
| `nextInt3D(int x, int y, int z)` | `int[][][]`  | `x × y × z` の 3 次元整数配列を読み込みます。 |

### ソート済み・累積和・逆写像

| メソッド                             | 戻り値の型     | 説明                         |
|----------------------------------|-----------|----------------------------|
| `nextSortedInt(int n)`           | `int[]`   | 整数配列を読み込み、ソートして返します。       |
| `nextIntPrefixSum(int n)`        | `int[]`   | 整数の累積和配列を返します。             |
| `nextIntPrefixSum(int h, int w)` | `int[][]` | 整数の 2 次元累積和配列を返します。        |
| `nextIntInverseMapping(int n)`   | `int[]`   | 1-indexed の値に対する逆写像を生成します。 |

### コレクション・マルチセット入力

| メソッド                            | 戻り値の型                       | 説明                           |
|---------------------------------|-----------------------------|------------------------------|
| `nextIntAL(int n)`              | `ArrayList<Integer>`        | 整数 `ArrayList` を読み込みます。      |
| `nextIntHS(int n)`              | `HashSet<Integer>`          | 整数 `HashSet` を読み込みます。        |
| `nextIntMultisetHM(int n)`      | `HashMap<Integer, Integer>` | 整数マルチセットを `HashMap` で読み込みます。 |
| `nextIntMultiset(int n, int m)` | `int[]`                     | 整数マルチセットを `int[]` で読み込みます。   |

### ユーティリティ/状態確認メソッド

| メソッド        | 戻り値の型     | 説明                                  |
|-------------|-----------|-------------------------------------|
| `hasNext()` | `boolean` | 次に読み込めるトークンが残っているか判定します。            |
| `peek()`    | `char`    | 次の非空白文字を消費せずに返します。EOF では `0` を返します。 |

## 利用例

```java
try (FastScanner sc = new FastScanner()) {
  int n = sc.nextInt();
  int m = sc.nextInt();
  int[] a = sc.nextInt(n);
  int s = sc.nextInt0();
  int t = sc.nextInt0();
  String line = sc.nextLine();

  if (sc.hasNext()) {
    char head = sc.peek();
    String token = sc.next();
  }
}
```

## 注意事項

- `ASCII` 範囲外の文字は正しく処理できません。
- `hasNext()` や `peek()` は次トークン探索のために空白文字を読み飛ばすことがあります。
- `nextChars(int n)` は「次のトークンを長さ `n` に切る」メソッドではなく、非空白文字を `n` 回読みます。
- `nextLine()` を入力終端で呼び出した場合は `NoSuchElementException` になります。
- 統合に伴い、以前の `ContestScanner` は削除されました。

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                                                            |
|:--------------|:-----------|:------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-04-07 | 初期バージョンとしてファイルを新規作成しました。                                                                                                      |
| **バージョン 1.1** | 2025-06-09 | `nextStringBuilder` や `nextLine` 内の変数宣言を `final` に統一するなどのリファクタリングを行いました。                                                      |
| **バージョン 1.2** | 2025-06-09 | `nextLine` メソッドの改行文字処理を改善しました。                                                                                                |
| **バージョン 1.3** | 2025-06-25 | `nextDouble` の小数構築処理を、ループ内の浮動小数点演算を削減する方式に変更しました。                                                                             |
| **バージョン 2.0** | 2025-09-22 | `hasNext` / `peek` を追加し、EOF ハンドリングと数値解析ロジックを改善しました。                                                                           |
| **バージョン 3.0** | 2025-11-11 | `FastIO24` として Java 24 向けの `VarHandle` 最適化を導入しました。                                                                            |
| **バージョン 4.0** | 2026-02-15 | `ContestScanner` と `CompressedFastScanner` の機能を統合し、拡張入力メソッドを `FastScanner` に集約しました。                                           |
| **バージョン 5.0** | 2026-02-18 | `nextInt0` / `nextLong0` とその配列版を追加。`next()` / `nextLine()` / `nextDouble()` の内部処理を見直し、Java 24 版の高速パスと API 説明を現状実装に合わせて整理しました。 |
| **バージョン 5.1** | 2026/03/12 | README との記述を同期し、最新版として現状実装に合うよう説明と更新日を整理しました。                                                                                 |

### バージョン管理について

バージョン番号は 2 桁で管理します：

- 1 桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2 桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
