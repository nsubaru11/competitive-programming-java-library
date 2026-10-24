# FastScanner 利用ガイド

> `FastScanner` は、競技プログラミング向けに設計された高速入力クラスである。

[← README: Fast Input/output (FastIO24)](./README.md) · ソース: [FastScanner.java](../../../src/lib/io/FastScanner.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

内部バッファを利用して `InputStream`（既定では `FileDescriptor.in` 経由の標準入力）から効率的に読み込みを行い、通常の
`Scanner` より低オーバーヘッドで動作する。
`FastIO24` 版では Java 24 の `VarHandle` を活用した数値パース最適化を採用しつつ、`ContestScanner` /
`CompressedFastScanner` 系の機能も統合している。

## 特徴

- **効率的な入力**: バッファリングと `VarHandle` ベースの低レベル最適化により高速に入力を処理する。
- **多機能**: 1 次元〜3 次元配列、ソート済み配列、累積和、逆写像、各種コレクション、マルチセットを一括生成できる。
- **0-index 補助**: `nextInt0()` / `nextLong0()` とその配列版、行列・3 次元配列版で、1-indexed 入力を即座に 0-indexed 化できる。
- **EOF/状態確認**: `hasNext()` と `peek()` により、終端判定や先読みが可能である。
- **行入力対応**: `nextLine()` は `LF` / `CRLF` の両改行形式を処理する。
- **真偽値入力の拡張**: `nextBoolean` 系で、文字/数値の単一値一致・述語判定・配列/行列入力に対応する。
- **文字コード**: `ASCII` 範囲内の文字入力を前提としている。
- **リソース管理**: `AutoCloseable` を実装しており、`try-with-resources` に対応する。

## 依存関係

- Java 標準の入出力ライブラリ（`InputStream`, `FileDescriptor`）
- `java.lang.invoke.VarHandle`
- `java.math.BigInteger`, `java.math.BigDecimal`
- `java.util.*`, `java.util.function.*`

## API

### 基本的な入力メソッド

| メソッド              | 戻り値の型      | 説明                                                 |
|-----------------------|-----------------|------------------------------------------------------|
| `nextInt()`           | `int`           | 次の整数値を読み込む。                           |
| `nextLong()`          | `long`          | 次の長整数値を読み込む。                         |
| `nextDouble()`        | `double`        | 次の浮動小数点値を読み込む。                     |
| `nextChar()`          | `char`          | 次の非空白 1 文字を読み込む。                    |
| `next()`              | `String`        | 次のトークンを文字列として読み込む。             |
| `nextStringBuilder()` | `StringBuilder` | 次のトークンを `StringBuilder` として読み込む。  |
| `nextLine()`          | `String`        | 次の 1 行を読み込む（改行文字は含まない）。    |
| `nextBigInteger()`    | `BigInteger`    | 次の整数トークンを `BigInteger` として読み込む。 |
| `nextBigDecimal()`    | `BigDecimal`    | 次の数値トークンを `BigDecimal` として読み込む。 |

### 0-index 補助メソッド

| メソッド           | 戻り値の型 | 説明                                                 |
|--------------------|------------|------------------------------------------------------|
| `nextInt0()`       | `int`      | `nextInt() - 1` を返す。                         |
| `nextLong0()`      | `long`     | `nextLong() - 1` を返す。                        |
| `nextInt0(int n)`  | `int[]`    | 各要素を 1 減らした長さ `n` の `int[]` を返す。  |
| `nextLong0(int n)` | `long[]`   | 各要素を 1 減らした長さ `n` の `long[]` を返す。 |

### 配列入力メソッド

| メソッド                           | 戻り値の型   | 説明                                                                |
|------------------------------------|--------------|---------------------------------------------------------------------|
| `nextInt(int n)`                   | `int[]`      | 長さ `n` の整数配列を読み込む。                                 |
| `nextLong(int n)`                  | `long[]`     | 長さ `n` の長整数配列を読み込む。                               |
| `nextDouble(int n)`                | `double[]`   | 長さ `n` の浮動小数点配列を読み込む。                           |
| `nextChars()`                      | `char[]`     | 次のトークンを `char[]` として読み込む。                        |
| `nextChars(int n)`                 | `char[]`     | 非空白文字を `n` 個読み込む。                                   |
| `nextStrings(int n)`               | `String[]`   | 長さ `n` の文字列配列を読み込む。                               |
| `nextIntMat(int h, int w)`         | `int[][]`    | `h × w` の整数行列を読み込む。                                  |
| `nextIntMat0(int h, int w)`        | `int[][]`    | 全要素を 1 減らした `h × w` の整数行列を読み込む。              |
| `nextIntMatInv(int h, int w)`      | `int[][]`    | 入力を `h × w` として読み、転置した `w × h` 行列を返す。        |
| `nextIntMatInv0(int h, int w)`     | `int[][]`    | 全要素を 1 減らして、転置した `w × h` 整数行列を返す。          |
| `nextLongMat(int h, int w)`        | `long[][]`   | `h × w` の長整数行列を読み込む。                                |
| `nextLongMat0(int h, int w)`       | `long[][]`   | 全要素を 1 減らした `h × w` の長整数行列を読み込む。            |
| `nextLongMatInv(int h, int w)`     | `long[][]`   | 入力を `h × w` として読み、転置した `w × h` 長整数行列を返す。  |
| `nextLongMatInv0(int h, int w)`    | `long[][]`   | 全要素を 1 減らして、転置した `w × h` 長整数行列を返す。        |
| `nextDoubleMat(int h, int w)`      | `double[][]` | `h × w` の浮動小数点行列を読み込む。                            |
| `nextCharMat(int h, int w)`        | `char[][]`   | `h × w` の文字行列を読み込む。                                  |
| `nextStringMat(int h, int w)`      | `String[][]` | `h × w` の文字列行列を読み込む。                                |
| `nextInt3D(int x, int y, int z)`   | `int[][][]`  | `x × y × z` の 3 次元整数配列を読み込む。                       |
| `nextInt3D0(int x, int y, int z)`  | `int[][][]`  | 全要素を 1 減らした `x × y × z` の 3 次元整数配列を読み込む。   |
| `nextLong3D(int x, int y, int z)`  | `long[][][]` | `x × y × z` の 3 次元長整数配列を読み込む。                     |
| `nextLong3D0(int x, int y, int z)` | `long[][][]` | 全要素を 1 減らした `x × y × z` の 3 次元長整数配列を読み込む。 |

### 真偽値入力メソッド

| メソッド                                        | 戻り値の型    | 説明                                                                     |
|-------------------------------------------------|---------------|--------------------------------------------------------------------------|
| `nextBoolean()`                                 | `boolean`     | 次トークンを `true/false` として読み込む（先頭文字で判定）。         |
| `nextBoolean(char c)`                           | `boolean`     | 次の 1 文字が `c` と一致する場合に `true` を返す。                   |
| `nextBoolean(int i)`                            | `boolean`     | 次の整数が `i` と一致する場合に `true` を返す。                      |
| `nextBoolean(long l)`                           | `boolean`     | 次の長整数が `l` と一致する場合に `true` を返す。                    |
| `nextBoolean(CharPredicate p)`                  | `boolean`     | 次の 1 文字を `CharPredicate` で判定する。                             |
| `nextBoolean(IntPredicate p)`                   | `boolean`     | 次の整数を `IntPredicate` で判定する。                                 |
| `nextBoolean(LongPredicate p)`                  | `boolean`     | 次の長整数を `LongPredicate` で判定する。                              |
| `nextBoolean(int n, char c)`                    | `boolean[]`   | 長さ `n` の真偽値配列を読み込み、各要素を `char` 一致判定で生成する。  |
| `nextBoolean(int n, int i)`                     | `boolean[]`   | 長さ `n` の真偽値配列を読み込み、各要素を `int` 一致判定で生成する。   |
| `nextBoolean(int n, long l)`                    | `boolean[]`   | 長さ `n` の真偽値配列を読み込み、各要素を `long` 一致判定で生成する。  |
| `nextBoolean(int n, CharPredicate p)`           | `boolean[]`   | 長さ `n` の真偽値配列を読み込み、各要素を `CharPredicate` で判定する。 |
| `nextBoolean(int n, IntPredicate p)`            | `boolean[]`   | 長さ `n` の真偽値配列を読み込み、各要素を `IntPredicate` で判定する。  |
| `nextBoolean(int n, LongPredicate p)`           | `boolean[]`   | 長さ `n` の真偽値配列を読み込み、各要素を `LongPredicate` で判定する。 |
| `nextBooleanMat(int h, int w, char c)`          | `boolean[][]` | `h × w` の真偽値行列を読み込み、各要素を `char` 一致判定で生成する。   |
| `nextBooleanMat(int h, int w, int n)`           | `boolean[][]` | `h × w` の真偽値行列を読み込み、各要素を `int` 一致判定で生成する。    |
| `nextBooleanMat(int h, int w, long l)`          | `boolean[][]` | `h × w` の真偽値行列を読み込み、各要素を `long` 一致判定で生成する。   |
| `nextBooleanMat(int h, int w, CharPredicate p)` | `boolean[][]` | `h × w` の真偽値行列を読み込み、各要素を `CharPredicate` で判定する。  |
| `nextBooleanMat(int h, int w, IntPredicate p)`  | `boolean[][]` | `h × w` の真偽値行列を読み込み、各要素を `IntPredicate` で判定する。   |
| `nextBooleanMat(int h, int w, LongPredicate p)` | `boolean[][]` | `h × w` の真偽値行列を読み込み、各要素を `LongPredicate` で判定する。  |

### ソート済み・累積和・逆写像

| メソッド                         | 戻り値の型                           | 説明                                              |
|----------------------------------|--------------------------------------|---------------------------------------------------|
| `nextSortedInt(int n)`           | `int[]`                              | 整数配列を読み込み、ソートして返す。          |
| `nextSortedLong(int n)`          | `long[]`                             | long配列を読み込み、ソートして返す。          |
| `nextSortedDouble(int n)`        | `double[]`                           | double配列を読み込み、ソートして返す。        |
| `nextSortedChars(...)`           | `char[]`                             | 文字列またはn文字を読み込み、ソートして返す。 |
| `nextSortedStrings(int n)`       | `String[]`                           | 文字列配列を読み込み、ソートして返す。        |
| `nextIntPrefixSum(int n)`        | `int[]`                              | 整数の累積和配列を返す。                      |
| `nextIntPrefixSum(int h, int w)` | `int[][]`                            | 整数の 2 次元累積和配列を返す。               |
| `nextLongPrefixSum(...)`         | `long[]` / `long[][]` / `long[][][]` | longの1〜3次元累積和を返す。                  |
| `nextIntInverseMapping(int n)`   | `int[]`                              | 1-indexed の値に対する逆写像を生成する。        |

### コレクション・マルチセット入力

| メソッド                         | 戻り値の型                    | 説明                                                             |
|----------------------------------|-------------------------------|------------------------------------------------------------------|
| `nextIntAL(int n)`               | `ArrayList<Integer>`          | 整数 `ArrayList` を読み込む。                                |
| `nextIntHS(int n)`               | `HashSet<Integer>`            | 整数 `HashSet` を読み込む。                                  |
| `nextIntTS(int n)`               | `TreeSet<Integer>`            | 整数 `TreeSet` を読み込む。                                  |
| `nextLongAL(int n)`              | `ArrayList<Long>`             | long `ArrayList` を読み込む。                                |
| `nextLongHS(int n)`              | `HashSet<Long>`               | long `HashSet` を読み込む。                                  |
| `nextLongTS(int n)`              | `TreeSet<Long>`               | long `TreeSet` を読み込む。                                  |
| `nextCharacterAL(int n)`         | `ArrayList<Character>`        | 文字 `ArrayList` を読み込む。                                |
| `nextCharacterHS(int n)`         | `HashSet<Character>`          | 文字 `HashSet` を読み込む。                                  |
| `nextCharacterTS(int n)`         | `TreeSet<Character>`          | 文字 `TreeSet` を読み込む。                                  |
| `nextStringAL(int n)`            | `ArrayList<String>`           | 文字列 `ArrayList` を読み込む。                              |
| `nextStringHS(int n)`            | `HashSet<String>`             | 文字列 `HashSet` を読み込む。                                |
| `nextStringTS(int n)`            | `TreeSet<String>`             | 文字列 `TreeSet` を読み込む。                                |
| `nextIntMultisetHM(int n)`       | `HashMap<Integer, Integer>`   | 整数マルチセットを `HashMap` で読み込む。                    |
| `nextIntMultisetTM(int n)`       | `TreeMap<Integer, Integer>`   | 整数マルチセットを `TreeMap` で読み込む。                    |
| `nextLongMultisetHM(int n)`      | `HashMap<Long, Integer>`      | longマルチセットを `HashMap` で読み込む。                    |
| `nextLongMultisetTM(int n)`      | `TreeMap<Long, Integer>`      | longマルチセットを `TreeMap` で読み込む。                    |
| `nextCharMultisetHM(int n)`      | `HashMap<Character, Integer>` | 文字マルチセットを `HashMap` で読み込む。                    |
| `nextCharMultisetTM(int n)`      | `TreeMap<Character, Integer>` | 文字マルチセットを `TreeMap` で読み込む。                    |
| `nextStringMultisetHM(int n)`    | `HashMap<String, Integer>`    | 文字列マルチセットを `HashMap` で読み込む。                  |
| `nextStringMultisetTM(int n)`    | `TreeMap<String, Integer>`    | 文字列マルチセットを `TreeMap` で読み込む。                  |
| `nextIntMultiset(int n, int m)`  | `int[]`                       | 値域 `[0,m)` の整数マルチセットを `int[]` で読み込む。       |
| `nextIntMultiset0(int n, int m)` | `int[]`                       | 値域 `[1,m]` を 0-indexed 化して整数マルチセットを読み込む。 |
| `nextUpperCharMultiset(int n)`   | `int[]`                       | `A`〜`Z`の出現回数を読み込む。                               |
| `nextLowerCharMultiset(int n)`   | `int[]`                       | `a`〜`z`の出現回数を読み込む。                               |
| `nextCharMultiset(n, l, r)`      | `int[]`                       | 閉区間`[l,r]`の文字出現回数を読み込む。                      |

### ユーティリティ/状態確認メソッド

| メソッド    | 戻り値の型 | 説明                                                          |
|-------------|------------|---------------------------------------------------------------|
| `hasNext()` | `boolean`  | 次に読み込めるトークンが残っているか判定する。              |
| `peek()`    | `char`     | 次の非空白文字を消費せずに返す。EOF では `0` を返す。 |

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

  // 単一値一致で true 判定
  boolean isYes = sc.nextBoolean('Y');

  // 配列・行列を述語で判定
  boolean[] nonNegative = sc.nextBoolean(n, x -> x >= 0);
  boolean[][] isWall = sc.nextBooleanMat(n, m, '#');
}
```


- [Example.java](../../../test/verify/io/Example.java)



## 制約・注意事項

- `ASCII` 範囲外の文字は正しく処理できない。
- `hasNext()` や `peek()` は次トークン探索のために空白文字を読み飛ばすことがある。
- `nextChars(int n)` は「次のトークンを長さ `n` に切る」メソッドではなく、非空白文字を `n` 回読む。
- `nextLine()` を入力終端で呼び出した場合は `NoSuchElementException` になる。
- 統合に伴い、以前の `ContestScanner` は削除された。
- コンストラクタは`InputStream.available() + 64`バイトを一度だけ`read`する。`available()`が全入力長を返さないストリームやshort readが起こるストリームでは入力が欠落するため、現状は提出環境の標準入力・ファイル入力向けである。

## 性能特性

- 全入力を内部byte配列へ保持し、各トークンを入力長に対して線形に走査する。
- 数値・文字列1個の読み取りはトークン長に比例し、配列入力は全要素の文字数に比例する。
- Java 24版は`VarHandle`によるbyte配列アクセスを利用する。

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                                                                                        |
|:-------------------|:-----------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-04-07 | 初期バージョンとしてファイルを新規作成した。                                                                                                                            |
| **バージョン 1.1** | 2025-06-09 | `nextStringBuilder` や `nextLine` 内の変数宣言を `final` に統一するなどのリファクタリングを行った。                                                                     |
| **バージョン 1.2** | 2025-06-09 | `nextLine` メソッドの改行文字処理を改善した。                                                                                                                           |
| **バージョン 1.3** | 2025-06-25 | `nextDouble` の小数構築処理を、ループ内の浮動小数点演算を削減する方式に変更した。                                                                                       |
| **バージョン 2.0** | 2025-09-22 | `hasNext` / `peek` を追加し、EOF ハンドリングと数値解析ロジックを改善した。                                                                                             |
| **バージョン 3.0** | 2025-11-11 | `FastIO24` として Java 24 向けの `VarHandle` 最適化を導入した。                                                                                                         |
| **バージョン 4.0** | 2026-02-15 | `ContestScanner` と `CompressedFastScanner` の機能を統合し、拡張入力メソッドを `FastScanner` に集約した。                                                               |
| **バージョン 5.0** | 2026-02-18 | `nextInt0` / `nextLong0` とその配列版を追加。`next()` / `nextLine()` / `nextDouble()` の内部処理を見直し、Java 24 版の高速パスと API 説明を現状実装に合わせて整理した。 |
| **バージョン 5.1** | 2026-03-12 | 機能の拡張とリファクタリング                                                                                                                                                |
| **バージョン 6.0** | 2026-04-18 | 一括読み込み用と、インタラクティブ用に分割し、分岐の削減および最適化                                                                                                        |
| **バージョン 7.0** | 2026-05-11 | `nextBoolean` 系 API（単一値一致・述語判定・1次元配列・2次元配列）を追加し、`InteractiveScanner` と同等の真偽値入力機能に揃えた。                                       |
| **バージョン 7.1** | 2026-08-10 | 行列・3 次元配列の `int` / `long` 向け 0-index・転置入力、`nextIntMultiset0` を追加。`nextIntMultiset` は 0-indexed の値域をそのまま読む仕様へ変更。                        |
