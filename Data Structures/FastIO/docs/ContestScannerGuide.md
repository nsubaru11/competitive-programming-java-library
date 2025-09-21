# ContestScanner 利用ガイド

## 概要

`ContestScanner`は、`FastScanner`を拡張し、競技プログラミングで頻出する多様なデータ構造（配列、コレクション、累積和など）を一行で読み込むためのメソッドを提供する高速入力クラスです。

## 特徴

- **コードの簡略化**: `for`ループを使わずに、配列やコレクションを一行で読み込めます。
- **多機能**: 1次元から3次元までの配列、ソート済み配列、累積和配列、各種コレクション、マルチセットなど、多彩な入力パターンに対応します。
- **高速性**: `FastScanner`の高速な入力処理を継承しています。
- **高い汎用性**: `nextCollection`や`nextMultiset`メソッドにより、任意のコレクションやマップを柔軟に生成できます。

## 依存関係

- `FastScanner`クラス
- `java.util.Arrays`, `java.util.function.Supplier`, `java.util.function.Function`, および各種コレクションクラス

## 主な機能

### コンストラクタ

| コンストラクタ                                          | 説明                                   |
|--------------------------------------------------|--------------------------------------|
| `ContestScanner()`                               | デフォルト設定（`System.in`、バッファサイズ65536バイト） |
| `ContestScanner(InputStream in)`                 | 指定された入力ストリームを使用                      |
| `ContestScanner(int bufferSize)`                 | 指定されたバッファサイズを使用（`System.in`）         |
| `ContestScanner(InputStream in, int bufferSize)` | 指定された入力ストリームとバッファサイズを使用              |

### 1次元配列入力メソッド

| メソッド                 | 戻り値の型      | 説明                   |
|----------------------|------------|----------------------|
| `nextInt(int n)`     | `int[]`    | 指定された長さの整数配列を読み込む    |
| `nextLong(int n)`    | `long[]`   | 指定された長さの長整数配列を読み込む   |
| `nextDouble(int n)`  | `double[]` | 指定された長さの浮動小数点配列を読み込む |
| `nextChars()`        | `char[]`   | 次の文字列を文字配列として読み込む    |
| `nextChars(int n)`   | `char[]`   | 指定された長さの文字配列を読み込む    |
| `nextStrings(int n)` | `String[]` | 指定された長さの文字列配列を読み込む   |

### 2次元配列入力メソッド

| メソッド                          | 戻り値の型        | 説明                         |
|-------------------------------|--------------|----------------------------|
| `nextIntMat(int h, int w)`    | `int[][]`    | 指定された行数・列数の整数2次元配列を読み込む    |
| `nextLongMat(int h, int w)`   | `long[][]`   | 指定された行数・列数の長整数2次元配列を読み込む   |
| `nextDoubleMat(int h, int w)` | `double[][]` | 指定された行数・列数の浮動小数点2次元配列を読み込む |
| `nextCharMat(int n)`          | `char[][]`   | 複数の文字列を2次元の文字配列として読み込む     |
| `nextCharMat(int h, int w)`   | `char[][]`   | 指定された行数・列数の文字2次元配列を読み込む    |
| `nextStringMat(int h, int w)` | `String[][]` | 指定された行数・列数の文字列2次元配列を読み込む   |

### 3次元配列入力メソッド

| メソッド                              | 戻り値の型        | 説明                     |
|-----------------------------------|--------------|------------------------|
| `nextInt3D(int x, int y, int z)`  | `int[][][]`  | 指定されたサイズの整数3次元配列を読み込む  |
| `nextLong3D(int x, int y, int z)` | `long[][][]` | 指定されたサイズの長整数3次元配列を読み込む |

### ソート済み配列入力メソッド

| メソッド                       | 戻り値の型      | 説明                           |
|----------------------------|------------|------------------------------|
| `nextSortedInt(int n)`     | `int[]`    | 指定された長さの整数配列を読み込み、ソートして返す    |
| `nextSortedLong(int n)`    | `long[]`   | 指定された長さの長整数配列を読み込み、ソートして返す   |
| `nextSortedDouble(int n)`  | `double[]` | 指定された長さの浮動小数点配列を読み込み、ソートして返す |
| `nextSortedChars()`        | `char[]`   | 次の文字列を文字配列として読み込み、ソートして返す    |
| `nextSortedChars(int n)`   | `char[]`   | 指定された長さの文字配列を読み込み、ソートして返す    |
| `nextSortedStrings(int n)` | `String[]` | 指定された長さの文字列配列を読み込み、ソートして返す   |

### 累積和配列入力メソッド

| メソッド                                     | 戻り値の型        | 説明                |
|------------------------------------------|--------------|-------------------|
| `nextIntPrefixSum(int n)`                | `int[]`      | 整数の累積和配列を読み込む     |
| `nextLongPrefixSum(int n)`               | `long[]`     | 長整数の累積和配列を読み込む    |
| `nextIntPrefixSum(int h, int w)`         | `int[][]`    | 整数の2次元累積和配列を読み込む  |
| `nextLongPrefixSum(int h, int w)`        | `long[][]`   | 長整数の2次元累積和配列を読み込む |
| `nextIntPrefixSum(int x, int y, int z)`  | `int[][][]`  | 整数の3次元累積和配列を読み込む  |
| `nextLongPrefixSum(int x, int y, int z)` | `long[][][]` | 長整数の3次元累積和配列を読み込む |

### 逆写像配列入力メソッド

| メソッド                           | 戻り値の型   | 説明                           |
|--------------------------------|---------|------------------------------|
| `nextIntInverseMapping(int n)` | `int[]` | 入力値が1-indexedの整数に対する逆写像を生成する |

### コレクション入力メソッド

| メソッド                     | 戻り値の型                  | 説明                        |
|--------------------------|------------------------|---------------------------|
| `nextIntAL(int n)`       | `ArrayList<Integer>`   | 指定された長さの整数ArrayListを読み込む  |
| `nextIntHS(int n)`       | `HashSet<Integer>`     | 指定された長さの整数HashSetを読み込む    |
| `nextIntTS(int n)`       | `TreeSet<Integer>`     | 指定された長さの整数TreeSetを読み込む    |
| `nextLongAL(int n)`      | `ArrayList<Long>`      | 指定された長さの長整数ArrayListを読み込む |
| `nextLongHS(int n)`      | `HashSet<Long>`        | 指定された長さの長整数HashSetを読み込む   |
| `nextLongTS(int n)`      | `TreeSet<Long>`        | 指定された長さの長整数TreeSetを読み込む   |
| `nextCharacterAL(int n)` | `ArrayList<Character>` | 指定された長さの文字ArrayListを読み込む  |
| `nextCharacterHS(int n)` | `HashSet<Character>`   | 指定された長さの文字HashSetを読み込む    |
| `nextCharacterTS(int n)` | `TreeSet<Character>`   | 指定された長さの文字TreeSetを読み込む    |
| `nextStringAL(int n)`    | `ArrayList<String>`    | 指定された長さの文字列ArrayListを読み込む |
| `nextStringHS(int n)`    | `HashSet<String>`      | 指定された長さの文字列HashSetを読み込む   |
| `nextStringTS(int n)`    | `TreeSet<String>`      | 指定された長さの文字列TreeSetを読み込む   |

### マルチセット入力メソッド（Map）

| メソッド                          | 戻り値の型                         | 説明                      |
|-------------------------------|-------------------------------|-------------------------|
| `nextIntMultisetHM(int n)`    | `HashMap<Integer, Integer>`   | 整数のマルチセットをHashMapで読み込む  |
| `nextIntMultisetTM(int n)`    | `TreeMap<Integer, Integer>`   | 整数のマルチセットをTreeMapで読み込む  |
| `nextLongMultisetHM(int n)`   | `HashMap<Long, Integer>`      | 長整数のマルチセットをHashMapで読み込む |
| `nextLongMultisetTM(int n)`   | `TreeMap<Long, Integer>`      | 長整数のマルチセットをTreeMapで読み込む |
| `nextCharMultisetHM(int n)`   | `HashMap<Character, Integer>` | 文字のマルチセットをHashMapで読み込む  |
| `nextCharMultisetTM(int n)`   | `TreeMap<Character, Integer>` | 文字のマルチセットをTreeMapで読み込む  |
| `nextStringMultisetHM(int n)` | `HashMap<String, Integer>`    | 文字列のマルチセットをHashMapで読み込む |
| `nextStringMultisetTM(int n)` | `TreeMap<String, Integer>`    | 文字列のマルチセットをTreeMapで読み込む |

### マルチセット入力メソッド（配列）

| メソッド                                      | 戻り値の型   | 説明                        |
|-------------------------------------------|---------|---------------------------|
| `nextIntMultiset(int n, int m)`           | `int[]` | 整数のマルチセットをint[]で読み込む      |
| `nextUpperCharMultiset(int n)`            | `int[]` | 大文字のマルチセットをchar[]で読み込む    |
| `nextLowerCharMultiset(int n)`            | `int[]` | 小文字のマルチセットをchar[]で読み込む    |
| `nextCharMultiset(int n, char l, char r)` | `int[]` | 連続する文字のマルチセットをchar[]で読み込む |

## 利用例

```java
// 基本的な使用例
try (ContestScanner sc = new ContestScanner()) {
    // 1次元配列の読み込み
    int n = sc.nextInt();
    int[] a = sc.nextInt(n);

    // 2次元配列の読み込み
    int h = sc.nextInt();
    int w = sc.nextInt();
    int[][] mat = sc.nextIntMat(h, w);

    // ソート済み配列の読み込み
    int[] sorted = sc.nextSortedInt(n);

    // 累積和配列の読み込み
    int[] prefixSum = sc.nextIntPrefixSum(n);

    // 逆写像配列の読み込み
    int[] inv = sc.nextIntInverseMapping(n);

    // コレクションの読み込み
    ArrayList<Integer> list = sc.nextIntAL(n);
    HashSet<Integer> set = sc.nextIntHS(n);

    // マルチセットの読み込み
    HashMap<Integer, Integer> multiset = sc.nextIntMultisetHM(n);

    // 処理
} 
```

## 注意事項

- `FastScanner`の注意事項がすべて適用されます。
- 累積和配列の2次元・3次元版は、戻り値の配列サイズが入力サイズより1大きくなります。
- 逆写像配列 (`nextIntInverseMapping`) は入力値が1-indexedであることを前提としています。

## パフォーマンス特性

- `FastScanner`に準じますが、配列やコレクションの生成に伴うオーバーヘッドがあります。

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                                    |
|:--------------|:-----------|:------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-04-07 | 初期バージョンとしてファイルを新規作成しました。                                                                              |
| **バージョン 1.1** | 2025-06-09 | nextStringMultiset メソッドのバグを修正し、nextIntMultisetのインクリメント処理をリファクタリングしました。                                |
| **バージョン 2.0** | 2025-09-22 | 汎用的な`nextCollection`, `nextMultiset`メソッドを追加し、コードの再利用性を向上。数値解析ロジックの最適化、setAllからforループへの変更、コメント統一などを実施。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
