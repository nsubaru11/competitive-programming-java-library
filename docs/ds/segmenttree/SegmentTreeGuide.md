# SegmentTree 利用ガイド

> 汎用・int・long の値に対し、点更新・区間集計・二分探索を行うセグメント木の仕様をまとめる。

[← README: SegmentTree](./README.md) · ソース: [SegmentTree.java](../../../src/lib/ds/segmenttree/SegmentTree.java) · [IntSegmentTree.java](../../../src/lib/ds/segmenttree/IntSegmentTree.java) · [LongSegmentTree.java](../../../src/lib/ds/segmenttree/LongSegmentTree.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

結合則を満たす演算 `operator` と単位元 `identity` を指定し、点更新と区間集計を行う。要素数を n とし、葉の数 size は n 以上の最小の2冪（n <= 1 では1）とする。
添字は0始まりとし、`query(l, r)` は半開区間 `[l, r)` の集計結果を返す。`l >= r` は空区間として単位元を返す。

## 特徴

- 長さ 2 * size の配列に木を保持する非再帰のボトムアップ実装である。
- 構築・`fill`・`setAll` は葉に値を配置した後、共通の `build` で内部ノードを O(size) で構築する。
- 単一点の更新は共通の `update` で葉を書き換え、根までの祖先を再集計する。
- `maxRight` / `minLeft` で、集計結果が条件を満たす区間の端を O(log n) で求める。
- 長さ・配列・コレクション（数値版）・コピーから構築できる。

### 型別クラス

| クラス                                                                    | 対象型 | 選ぶ目安                                             |
|---------------------------------------------------------------------------|--------|------------------------------------------------------|
| [`SegmentTree<T>`](../../../src/lib/ds/segmenttree/SegmentTree.java)      | `T`    | 区間長や複数の値を持つノードなど、任意の型を扱う場合 |
| [`IntSegmentTree`](../../../src/lib/ds/segmenttree/IntSegmentTree.java)   | `int`  | 値と集計結果が int に収まる場合                      |
| [`LongSegmentTree`](../../../src/lib/ds/segmenttree/LongSegmentTree.java) | `long` | 値と集計結果が long に収まる場合                     |

## 依存関係

- Java標準ライブラリ: `java.util.*`、`java.util.function.*`
- 数値版: [IntCollection](../../../src/lib/ds/IntCollection.java) / [LongCollection](../../../src/lib/ds/LongCollection.java)

## API

### `SegmentTree<T>`

[ソース](../../../src/lib/ds/segmenttree/SegmentTree.java)

公開フィールドは `final int size` と `final T identity` である。

| 種別        | シグネチャ                                                                       | 戻り値           | 説明                                                         |
|-------------|----------------------------------------------------------------------------------|------------------|--------------------------------------------------------------|
| Constructor | `SegmentTree(int n, BinaryOperator<T> operator, T identity)`                     | —                | 全要素を単位元で初期化する。                                 |
| Constructor | `SegmentTree(T[] a, BinaryOperator<T> operator, T identity)`                     | —                | 配列の要素を添字順に配置して構築する。                       |
| Constructor | `SegmentTree(Collection<? extends T> a, BinaryOperator<T> operator, T identity)` | —                | コレクションの iterator 順に要素を配置して構築する。         |
| Constructor | `SegmentTree(SegmentTree<T> other)`                                              | —                | 内部配列を複製し、演算・単位元・要素オブジェクトを共有する。 |
| Method      | `size()`                                                                         | `int`            | 要素数 n を返す。                                            |
| Method      | `isEmpty()`                                                                      | `boolean`        | 要素数が0かを返す。                                          |
| Method      | `get(int i)`                                                                     | `T`              | 位置 i の値を返す。                                          |
| Method      | `set(int i, T v)`                                                                | `T`              | 位置 i を v へ更新し、更新前の値を返す。                     |
| Method      | `apply(int i, T x, BinaryOperator<T> f)`                                         | `T`              | 位置 i を `f(現在値, x)` へ更新し、更新後の値を返す。        |
| Method      | `fill(T v)`                                                                      | `SegmentTree<T>` | 全要素を v にして再構築する。                                |
| Method      | `setAll(IntFunction<T> init)`                                                    | `SegmentTree<T>` | 添字順に初期値関数を評価して再構築する。                     |
| Method      | `query(int l, int r)`                                                            | `T`              | 半開区間 `[l, r)` の集計結果を返す。                         |
| Method      | `queryAll()`                                                                     | `T`              | 全要素の集計結果を返す。                                     |
| Method      | `maxRight(int l, Predicate<T> tester)`                                           | `int`            | `tester(query(l, r))` が真となる最大の r を返す。            |
| Method      | `minLeft(int r, Predicate<T> tester)`                                            | `int`            | `tester(query(l, r))` が真となる最小の l を返す。            |
| Method      | `iterator()`                                                                     | `Iterator<T>`    | 添字順に要素を走査する。                                     |
| Method      | `toString()`                                                                     | `String`         | 要素を空白区切りで連結する。空の場合は空文字列を返す。       |

`Iterable<T>` の `forEach(Consumer<? super T> action)` と `spliterator()` も継承する。

### `IntSegmentTree`

[ソース](../../../src/lib/ds/segmenttree/IntSegmentTree.java)

公開フィールドは `final int size` と `final int identity` である。

`IntCollection` を実装する。

| 種別        | シグネチャ                                                                  | 戻り値                    | 説明                                                    |
|-------------|-----------------------------------------------------------------------------|---------------------------|---------------------------------------------------------|
| Constructor | `IntSegmentTree(int n, IntBinaryOperator operator, int identity)`           | —                         | 全要素を単位元で初期化する。                            |
| Constructor | `IntSegmentTree(int[] a, IntBinaryOperator operator, int identity)`         | —                         | 配列の要素を添字順に配置して構築する。                  |
| Constructor | `IntSegmentTree(IntCollection a, IntBinaryOperator operator, int identity)` | —                         | コレクションの走査順に要素を配置して構築する。          |
| Constructor | `IntSegmentTree(IntSegmentTree other)`                                      | —                         | 内部配列を複製し、演算・単位元を共有する。              |
| Method      | `size()`                                                                    | `int`                     | 要素数 n を返す。                                       |
| Method      | `get(int i)`                                                                | `int`                     | 位置 i の値を返す。                                     |
| Method      | `set(int i, int v)`                                                         | `int`                     | 位置 i を v へ更新し、更新前の値を返す。                |
| Method      | `add(int i, int d)`                                                         | `int`                     | 位置 i に d を加え、更新後の値を返す。                  |
| Method      | `multiply(int i, int m)`                                                    | `int`                     | 位置 i を m 倍し、更新後の値を返す。                    |
| Method      | `affine(int i, int m, int d)`                                               | `int`                     | 位置 i を `現在値 * m + d` へ更新し、更新後の値を返す。 |
| Method      | `apply(int i, int x, IntBinaryOperator f)`                                  | `int`                     | 位置 i を `f(現在値, x)` へ更新し、更新後の値を返す。   |
| Method      | `fill(int v)`                                                               | `IntSegmentTree`          | 全要素を v にして再構築する。                           |
| Method      | `setAll(IntUnaryOperator init)`                                             | `IntSegmentTree`          | 添字順に初期値関数を評価して再構築する。                |
| Method      | `query(int l, int r)`                                                       | `int`                     | 半開区間 `[l, r)` の集計結果を返す。                    |
| Method      | `queryAll()`                                                                | `int`                     | 全要素の集計結果を返す。                                |
| Method      | `maxRight(int l, IntPredicate tester)`                                      | `int`                     | `tester(query(l, r))` が真となる最大の r を返す。       |
| Method      | `minLeft(int r, IntPredicate tester)`                                       | `int`                     | `tester(query(l, r))` が真となる最小の l を返す。       |
| Method      | `toArray()`                                                                 | `int[]`                   | 葉の区間を複製した配列を返す。                          |
| Method      | `iterator()`                                                                | `PrimitiveIterator.OfInt` | 添字順に要素を走査する。                                |
| Method      | `toString()`                                                                | `String`                  | 要素を空白区切りで連結する。空の場合は空文字列を返す。  |

### `LongSegmentTree`

[ソース](../../../src/lib/ds/segmenttree/LongSegmentTree.java)

公開フィールドは `final int size` と `final long identity` である。

`LongCollection` を実装する。値・単位元・更新引数が long になる以外は `IntSegmentTree` と同じである。

| 種別        | シグネチャ                                                                      | 戻り値                     | 説明                                                    |
|-------------|---------------------------------------------------------------------------------|----------------------------|---------------------------------------------------------|
| Constructor | `LongSegmentTree(int n, LongBinaryOperator operator, long identity)`            | —                          | 全要素を単位元で初期化する。                            |
| Constructor | `LongSegmentTree(long[] a, LongBinaryOperator operator, long identity)`         | —                          | 配列の要素を添字順に配置して構築する。                  |
| Constructor | `LongSegmentTree(LongCollection a, LongBinaryOperator operator, long identity)` | —                          | コレクションの走査順に要素を配置して構築する。          |
| Constructor | `LongSegmentTree(LongSegmentTree other)`                                        | —                          | 内部配列を複製し、演算・単位元を共有する。              |
| Method      | `size()`                                                                        | `int`                      | 要素数 n を返す。                                       |
| Method      | `get(int i)`                                                                    | `long`                     | 位置 i の値を返す。                                     |
| Method      | `set(int i, long v)`                                                            | `long`                     | 位置 i を v へ更新し、更新前の値を返す。                |
| Method      | `add(int i, long d)`                                                            | `long`                     | 位置 i に d を加え、更新後の値を返す。                  |
| Method      | `multiply(int i, long m)`                                                       | `long`                     | 位置 i を m 倍し、更新後の値を返す。                    |
| Method      | `affine(int i, long m, long d)`                                                 | `long`                     | 位置 i を `現在値 * m + d` へ更新し、更新後の値を返す。 |
| Method      | `apply(int i, long x, LongBinaryOperator f)`                                    | `long`                     | 位置 i を `f(現在値, x)` へ更新し、更新後の値を返す。   |
| Method      | `fill(long v)`                                                                  | `LongSegmentTree`          | 全要素を v にして再構築する。                           |
| Method      | `setAll(IntToLongFunction init)`                                                | `LongSegmentTree`          | 添字順に初期値関数を評価して再構築する。                |
| Method      | `query(int l, int r)`                                                           | `long`                     | 半開区間 `[l, r)` の集計結果を返す。                    |
| Method      | `queryAll()`                                                                    | `long`                     | 全要素の集計結果を返す。                                |
| Method      | `maxRight(int l, LongPredicate tester)`                                         | `int`                      | `tester(query(l, r))` が真となる最大の r を返す。       |
| Method      | `minLeft(int r, LongPredicate tester)`                                          | `int`                      | `tester(query(l, r))` が真となる最小の l を返す。       |
| Method      | `toArray()`                                                                     | `long[]`                   | 葉の区間を複製した配列を返す。                          |
| Method      | `iterator()`                                                                    | `PrimitiveIterator.OfLong` | 添字順に要素を走査する。                                |
| Method      | `toString()`                                                                    | `String`                   | 要素を空白区切りで連結する。空の場合は空文字列を返す。  |

### 数値版の継承 API

`IntSegmentTree` / `LongSegmentTree` は `IntCollection` / `LongCollection` の既定メソッド `isEmpty`、`contains`、`forEachInt` / `forEachLong`、`spliterator`、`intStream` / `longStream`、`toList`、`forEach` を継承する。全シグネチャと既定実装は [配列・コレクションの継承API](../arrays/ArrayGuide.md#継承元のコレクション-api) を参照する。

## 利用例

準備中

## 制約・注意事項

- 添字は `[0, n)`、区間は `0 <= l`、`r <= n` とする。葉は配列の `[size, size + n)` に置くため、範囲外の添字は内部ノードや余白の葉を指し、配列アクセスでは検出できない。そのため単一点の操作・`query`・`maxRight`・`minLeft` は状態を変更する前に検査し、範囲外では `IndexOutOfBoundsException` を投げる。`query` は `l >= r` を空区間として扱い、範囲を検査しない。
- `operator` は結合則を満たし、`identity` はその単位元とする。可換性は不要であり、`query` は左から順に集計する。
- `SegmentTree<T>` の `identity` が null の場合、`operator` は null を受け付けること。配列とコピーからの構築は内部配列を共有せず、要素オブジェクトは共有する。`set` は旧値と新値が等しくても指定された参照を代入する。
- `maxRight(l, tester)` は `0 <= l <= n`、`minLeft(r, tester)` は `0 <= r <= n` とする。`tester(identity)` は真とし、区間を広げると真から偽へ一度だけ変わる条件とする。
- 係数を指定するアフィン変換は `affine`、関数を指定する更新は `apply` とする。数値版の `add`・`multiply`・`affine`・`apply` はオーバーフローを検査しない。
- 初期値関数・更新関数・判定関数の中で同じインスタンスを変更しない。iterator の終端後の取得は `NoSuchElementException` を投げる。

## 性能特性

関数の評価時間は別途加算する。

| 操作                                        | 時間計算量 | 補足                                      |
|---------------------------------------------|------------|-------------------------------------------|
| 構築・`fill`・`setAll`                      | O(n)       | 内部ノードを葉から一度だけ集計する        |
| コピー                                      | O(n)       | 内部配列を複製する                        |
| `get`・`queryAll`                           | O(1)       | 葉または根を直接読む                      |
| `set`・`add`・`multiply`・`affine`・`apply` | O(log n)   | 葉から根までを再集計する                  |
| `query`                                     | O(log n)   | 左右から最大 2 log n 個のノードを集計する |
| `maxRight`・`minLeft`                       | O(log n)   | 上昇と下降をそれぞれ一度行う              |
| `toArray`・走査・文字列化                   | O(n)       | 葉を直接読む                              |

- **空間計算量**: O(n)。木の配列長は 2 * size であり、size < 2n である。

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
|--------------------|------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-11-25 | 3クラス: 初期実装（`set`・`query`・`queryAll`・`fill`）                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| **バージョン 2.0** | 2025-11-27 | 3クラス: 境界探索 `maxRight`・`minLeft` を追加                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| **バージョン 2.1** | 2026-04-30 | IntSegmentTree / LongSegmentTree: `add`・`multiply`・`apply(i, m, d)`・`apply(i, x, f)` を追加                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| **バージョン 2.2** | 2026-10-07 | LongSegmentTree: `setAll` の初期化関数を `IntToLongFunction` に変更                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| **バージョン 2.3** | 2026-10-27 | 3クラス: `set` を更新前の値を返す仕様に統一。SegmentTree: 同値判定による代入省略を削除                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| **バージョン 3.0** | 2026-10-28 | 3クラス: `fill`・`setAll` が自身を返すように変更。`minLeft` が処理済みノードではなく次のノードで終了判定していた誤りを修正。コピーからのコンストラクタを追加し、構築を `build`、点更新を `update` へ集約。空の `toString` を空文字列に変更。SegmentTree: `isEmpty` を追加。配列アクセスで検出できない範囲外の添字・区間の検査を3クラスで統一。IntSegmentTree / LongSegmentTree: 係数指定の `apply(i, m, d)` を `affine` へ改名し、IntCollection / LongCollection からのコンストラクタと `toArray` を追加。`add`・`multiply`・`affine`・`apply` を葉の直接更新へ変更 |
| **バージョン 3.1** | 2026-10-29 | 3クラス: `set` の更新前の値を保持するローカル変数名を old へ統一                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| **バージョン 4.0** | 2026-10-29 | 3クラス: 要素数 `size` と単位元 `identity` を公開フィールドにし、内部の2のべき乗の葉数を `leafSize` へ改名。SegmentTree: Collection からのコンストラクタを追加                                                                                                                                                                                                                                                                                                                                                                                                      |
