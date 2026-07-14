# IntCompressedArray / LongCompressedArray 利用ガイド

## 概要

[`IntCompressedArray`](../../../src/lib/ds/arrays/IntCompressedArray.java) と [`LongCompressedArray`](../../../src/lib/ds/arrays/LongCompressedArray.java) は、元配列の値をソート順の順位へ変換して保持する座標圧縮クラスです。dense ranking に加え、competition ranking と modified competition ranking に対応します。

## 特徴

- 元配列順の圧縮結果を `int[]` で保持
- 0-based / 1-based の順位を選択可能
- 3種類の同順位処理を `RankType` で選択可能
- 値から順位、順位から値への O(log n) 変換
- 元配列の復元、出現判定、出現回数の取得に対応
- 圧縮結果・ソート済み値・順位配列をコピーとして取得可能
- `LongCompressedArray` も圧縮後の順位は `int` のため `IntArray` を実装

## 依存関係

- `java.util.Arrays`
- `java.util.PrimitiveIterator`
- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)
- [`lib.search.ArrayBinarySearch`](../../../src/lib/search/ArrayBinarySearch.java)

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                                                                | 戻り値の型 | 説明                              |
|---------------------------------------------------------------------|-------|---------------------------------|
| `IntCompressedArray(int[] a)`                                       | -     | DENSE・0-based で int 配列を圧縮       |
| `IntCompressedArray(int[] a, RankType type)`                        | -     | 指定順位方式・0-based で圧縮              |
| `IntCompressedArray(int[] a, RankType type, boolean oneBased)`      | -     | 順位方式と開始位置を指定                    |
| `IntCompressedArray(IntArray a)`                                    | -     | DENSE・0-based で `IntArray` を圧縮  |
| `IntCompressedArray(IntArray a, RankType type)`                     | -     | 指定順位方式・0-based で圧縮              |
| `IntCompressedArray(IntArray a, RankType type, boolean oneBased)`   | -     | 順位方式と開始位置を指定                    |
| `LongCompressedArray(long[] a)`                                     | -     | DENSE・0-based で long 配列を圧縮      |
| `LongCompressedArray(long[] a, RankType type)`                      | -     | 指定順位方式・0-based で圧縮              |
| `LongCompressedArray(long[] a, RankType type, boolean oneBased)`    | -     | 順位方式と開始位置を指定                    |
| `LongCompressedArray(LongArray a)`                                  | -     | DENSE・0-based で `LongArray` を圧縮 |
| `LongCompressedArray(LongArray a, RankType type)`                   | -     | 指定順位方式・0-based で圧縮              |
| `LongCompressedArray(LongArray a, RankType type, boolean oneBased)` | -     | 順位方式と開始位置を指定                    |

`RankType` は `IntCompressedArray` と `LongCompressedArray` の各クラス内に同名で定義されています。

### 2. RankType

ソート済み値 `[10, 20, 20, 30]` に対する順位は次の通りです。

| RankType               | 0-based        | 1-based        | 説明                 |
|------------------------|----------------|----------------|--------------------|
| `DENSE`                | `[0, 1, 1, 2]` | `[1, 2, 2, 3]` | 異なる値ごとに連番          |
| `COMPETITION`          | `[0, 1, 1, 3]` | `[1, 2, 2, 4]` | 同順位の個数だけ次順位を飛ばす    |
| `MODIFIED_COMPETITION` | `[0, 2, 2, 3]` | `[1, 3, 3, 4]` | 同順位グループの末尾位置を順位にする |

### 3. 圧縮結果・設定情報

| メソッド             | 戻り値の型      | 説明                 |
|------------------|------------|--------------------|
| `get(int i)`     | `int`      | 元配列の `i` 番目に対応する順位 |
| `size()`         | `int`      | 元配列の長さ             |
| `distinctSize()` | `int`      | 異なる値の個数            |
| `rankType()`     | `RankType` | 使用中の順位方式           |
| `isOneBased()`   | `boolean`  | 1-based か判定        |
| `toArray()`      | `int[]`    | 元配列順の圧縮結果のコピーを返す   |
| `compressed()`   | `int[]`    | 元配列順の圧縮結果のコピーを返す   |

`length` と `distinctSize` は public final フィールドとしても公開されています。

### 4. 値・順位の変換

| メソッド                    | 戻り値の型              | 説明           |
|-------------------------|--------------------|--------------|
| `rankOfValue(value)`    | `int`              | 元の値に対応する順位   |
| `valueOfRank(int rank)` | `int` / `long`     | 指定順位に対応する元の値 |
| `containsValue(value)`  | `boolean`          | 元の値が存在するか判定  |
| `count(value)`          | `int`              | 元の値の出現回数     |
| `restore()`             | `int[]` / `long[]` | 元の順序と値を復元    |

### 5. 内部順序の取得・反復

| メソッド         | 戻り値の型                     | 説明                 |
|--------------|---------------------------|--------------------|
| `sorted()`   | `int[]` / `long[]`        | 重複を含むソート済み値のコピー    |
| `ranks()`    | `int[]`                   | ソート済み各要素の順位のコピー    |
| `iterator()` | `PrimitiveIterator.OfInt` | 元配列順の圧縮結果を走査       |
| `toString()` | `String`                  | 元配列順の圧縮結果を空白区切りで返す |

## 利用例

```java
IntCompressedArray a = new IntCompressedArray(new int[]{50, 10, 50, 20});

System.out.println(a);              // 2 0 2 1
System.out.println(a.distinctSize()); // 3
System.out.println(a.rankOfValue(20)); // 1
System.out.println(Arrays.toString(a.restore())); // [50, 10, 50, 20]
```

```java
LongCompressedArray c = new LongCompressedArray(
	new long[]{100L, 20L, 20L, 50L},
	LongCompressedArray.RankType.COMPETITION,
	true
);
// 4 1 1 3
```

## 注意事項

- 現在の実装は長さ1以上の配列を前提とします。
- `rankOfValue(value)` は `containsValue(value)` が true の値に対して呼び出します。
- `valueOfRank(rank)` は選択した `RankType` に実在する順位に対して呼び出します。
- `sorted()` と `ranks()` は重複を除かず、どちらも元配列と同じ長さです。
- コンストラクタは入力配列を変更せず、内部へコピーしてソートします。
- 圧縮後の順位は元の数値型に関係なく `int` です。

## パフォーマンス特性

- 構築: O(n log n)
- `get`, `size`, `distinctSize`, `rankType`, `isOneBased`: O(1)
- `rankOfValue`, `valueOfRank`, `containsValue`, `count`: O(log n)
- `restore`: O(n log n)
- `toArray`, `compressed`, `sorted`, `ranks`, iterator, `toString`: O(n)
- 使用メモリ: O(n)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                        |
|:--------------|:-----------|:--------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-15 | DENSE・COMPETITION・MODIFIED_COMPETITION、0/1-based、値と順位の相互変換を備える座標圧縮として初回実装 |
| **バージョン 1.1** | 2026-07-15 | `toString()` を元配列順の空白区切り形式へ改善                                             |
| **バージョン 2.0** | 2026-07-15 | 元配列順の圧縮結果を直接コピーする `toArray()` と `compressed()` を追加                        |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
