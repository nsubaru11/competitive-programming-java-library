# CompressedArray / CompressedArray2D 利用ガイド

## 概要

`IntCompressedArray` / `LongCompressedArray` とその2D版は、元配列の全要素をソート順の順位へ変換して保持する座標圧縮クラスです。
dense ranking に加え、competition ranking と modified competition ranking に対応します。

## 特徴

- 元配列順の圧縮結果を `int[]` で保持
- 2D版も内部では一次元配列へ圧縮し、全要素を共通の順位空間へ変換
- 0-based / 1-based の順位を選択可能
- 3種類の同順位処理を `RankType` で選択可能
- 値から順位、順位から値への $\mathcal{O}(\log n)$ 変換
- 元配列の復元、出現判定、出現回数の取得に対応
- 圧縮結果・ソート済み値・順位配列をコピーとして取得可能
- `LongCompressedArray` も圧縮後の順位は `int` のため `IntArray` を実装

## 依存関係

- `java.util.Arrays`
- `java.util.PrimitiveIterator`
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)
- [`lib.ds.arrays.RankType`](../../../src/lib/ds/arrays/RankType.java)
- [`lib.search.ArrayBinarySearch`](../../../src/lib/search/ArrayBinarySearch.java)

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                                                             | 戻り値の型 | 説明                                 |
|----------------------------------------------------------------------|------------|--------------------------------------|
| `IntCompressedArray(int[] a)`                                        | -          | DENSE・0-based で int 配列を圧縮     |
| `IntCompressedArray(int[] a, RankType type)`                         | -          | 指定順位方式・0-based で圧縮         |
| `IntCompressedArray(int[] a, RankType type, boolean oneBased)`       | -          | 順位方式と開始位置を指定             |
| `IntCompressedArray(IntArray a)`                                     | -          | DENSE・0-based で `IntArray` を圧縮  |
| `IntCompressedArray(IntArray a, RankType type)`                      | -          | 指定順位方式・0-based で圧縮         |
| `IntCompressedArray(IntArray a, RankType type, boolean oneBased)`    | -          | 順位方式と開始位置を指定             |
| `LongCompressedArray(long[] a)`                                      | -          | DENSE・0-based で long 配列を圧縮    |
| `LongCompressedArray(long[] a, RankType type)`                       | -          | 指定順位方式・0-based で圧縮         |
| `LongCompressedArray(long[] a, RankType type, boolean oneBased)`     | -          | 順位方式と開始位置を指定             |
| `LongCompressedArray(LongArray a)`                                   | -          | DENSE・0-based で `LongArray` を圧縮 |
| `LongCompressedArray(LongArray a, RankType type)`                    | -          | 指定順位方式・0-based で圧縮         |
| `LongCompressedArray(LongArray a, RankType type, boolean oneBased)`  | -          | 順位方式と開始位置を指定             |
| `IntCompressedArray2D(int[][] a)`                                    | -          | DENSE・0-based で2D int配列を圧縮    |
| `IntCompressedArray2D(int[][] a, RankType type)`                     | -          | 指定順位方式・0-based で圧縮         |
| `IntCompressedArray2D(int[][] a, RankType type, boolean oneBased)`   | -          | 順位方式と開始位置を指定             |
| `LongCompressedArray2D(long[][] a)`                                  | -          | DENSE・0-based で2D long配列を圧縮   |
| `LongCompressedArray2D(long[][] a, RankType type)`                   | -          | 指定順位方式・0-based で圧縮         |
| `LongCompressedArray2D(long[][] a, RankType type, boolean oneBased)` | -          | 順位方式と開始位置を指定             |

4クラスはトップレベルの `lib.ds.arrays.RankType` を共通して使用します。

### 2. RankType

ソート済み値 `[10, 20, 20, 30]` に対する順位は次の通りです。

| RankType               | 0-based        | 1-based        | 説明                                 |
|------------------------|----------------|----------------|--------------------------------------|
| `DENSE`                | `[0, 1, 1, 2]` | `[1, 2, 2, 3]` | 異なる値ごとに連番                   |
| `COMPETITION`          | `[0, 1, 1, 3]` | `[1, 2, 2, 4]` | 同順位の個数だけ次順位を飛ばす       |
| `MODIFIED_COMPETITION` | `[0, 2, 2, 3]` | `[1, 3, 3, 4]` | 同順位グループの末尾位置を順位にする |

### 3. 圧縮結果・設定情報

| メソッド            | 戻り値の型 | 説明                               |
|---------------------|------------|------------------------------------|
| `get(int i)`        | `int`      | 元配列の `i` 番目に対応する順位    |
| `get(int i, int j)` | `int`      | 2D元配列の `(i, j)` に対応する順位 |
| `size()`            | `int`      | 元配列の長さ                       |
| `uniqueSize()`      | `int`      | 異なる値の個数                     |
| `rankType()`        | `RankType` | 使用中の順位方式                   |
| `isOneBased()`      | `boolean`  | 1-based か判定                     |
| `toArray()`         | `int[]`    | 元配列順の圧縮結果のコピーを返す   |
| `compressed()`      | `int[]`    | 元配列順の圧縮結果のコピーを返す   |

2D版の `toArray()` / `compressed()` は `int[][]` を返します。`h`, `w`, `length`, `uniqueSize` は public final フィールドです。 1D版は `length` と `uniqueSize` を公開しています。

### 4. 値・順位の変換

| メソッド                | 戻り値の型                  | 説明                     |
|-------------------------|-----------------------------|--------------------------|
| `rankOfValue(value)`    | `int`                       | 元の値に対応する順位     |
| `valueOfRank(int rank)` | `int` / `long`              | 指定順位に対応する元の値 |
| `containsValue(value)`  | `boolean`                   | 元の値が存在するか判定   |
| `count(value)`          | `int`                       | 元の値の出現回数         |
| `restore()`             | `int[]` / `long[]` / 2D配列 | 元の形状・順序と値を復元 |

### 5. 内部順序の取得・反復

| メソッド     | 戻り値の型                | 説明                                   |
|--------------|---------------------------|----------------------------------------|
| `sorted()`   | `int[]` / `long[]`        | 重複を含むソート済み値のコピー         |
| `ranks()`    | `int[]`                   | ソート済み各要素の順位のコピー         |
| `iterator()` | `PrimitiveIterator.OfInt` | 元配列順または行優先順に圧縮結果を走査 |
| `toString()` | `String`                  | 1Dは空白区切り、2Dは行ごとの改行で返す |

## 利用例

```java
IntCompressedArray a = new IntCompressedArray(new int[]{50, 10, 50, 20});

System.out.println(a);              // 2 0 2 1
System.out.println(a.uniqueSize()); // 3
System.out.println(a.rankOfValue(20)); // 1
System.out.println(Arrays.toString(a.restore())); // [50, 10, 50, 20]
```

```java
LongCompressedArray c = new LongCompressedArray(
	new long[]{100L, 20L, 20L, 50L},
	RankType.COMPETITION,
	true
);
// 4 1 1 3
```

```java
IntCompressedArray2D a = new IntCompressedArray2D(new int[][]{
	{50, 10},
	{50, 20}
});
System.out.println(a);
// 2 0
// 2 1
```

## 注意事項

- 現在の実装は各次元の長さが1以上で、2D配列は矩形であることを前提とします。
- 2D版は行ごとではなく、全要素をまとめて座標圧縮します。
- `rankOfValue(value)` は `containsValue(value)` が true の値に対して呼び出します。
- `valueOfRank(rank)` は選択した `RankType` に実在する順位に対して呼び出します。
- `sorted()` と `ranks()` は重複を除かず、どちらも元配列と同じ長さです。
- コンストラクタは入力配列を変更せず、内部へコピーしてソートします。
- 圧縮後の順位は元の数値型に関係なく `int` です。

## パフォーマンス特性

- 構築: $\mathcal{O}(n \log n)$。2D版のnは全要素数
- `get`, `size`, `uniqueSize`, `rankType`, `isOneBased`: $\mathcal{O}(1)$
- `rankOfValue`, `valueOfRank`, `containsValue`, `count`: $\mathcal{O}(\log n)$
- `restore`: $\mathcal{O}(n \log n)$
- `toArray`, `compressed`, `sorted`, `ranks`, iterator, `toString`: $\mathcal{O}(n)$
- 使用メモリ: $\mathcal{O}(n)$

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                                  |
|:-------------------|:-----------|:------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-15 | DENSE・COMPETITION・MODIFIED_COMPETITION、0/1-based、値と順位の相互変換を備える座標圧縮として初回実装 |
| **バージョン 1.1** | 2026-07-15 | `toString()` を元配列順の空白区切り形式へ改善                                                         |
| **バージョン 2.0** | 2026-07-15 | 元配列順の圧縮結果を直接コピーする `toArray()` と `compressed()` を追加                               |
| **バージョン 3.0** | 2026-07-27 | `distinctSize` を `uniqueSize` へ変更して `length` とともに公開し、`toString()` の容量を調整          |
| **バージョン 4.0** | 2026-07-27 | 2D版を追加し、4クラスで重複していた順位方式をトップレベルの `RankType` へ統合                         |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
