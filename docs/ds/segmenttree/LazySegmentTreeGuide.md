# LazySegmentTree 利用ガイド

> 汎用・int・long の値に対し、作用素を遅延伝播して区間作用・区間集計・二分探索を行うセグメント木の仕様をまとめる。

[← README: SegmentTree](./README.md) · ソース: [LazySegmentTree.java](../../../src/lib/ds/segmenttree/LazySegmentTree.java) · [IntLazySegmentTree.java](../../../src/lib/ds/segmenttree/IntLazySegmentTree.java) · [LongLazySegmentTree.java](../../../src/lib/ds/segmenttree/LongLazySegmentTree.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

結合則を満たす演算 `operator`、単位元 `identity`、作用 `mapping`、作用素の合成 `composition` を指定し、区間作用と区間集計を行う。要素数を n とし、葉の数 size は n 以上の最小の2冪（n <= 1 では1）、log = log2(size) とする。
添字は0始まりとし、`apply(l, r, f)` と `query(l, r)` は半開区間 `[l, r)` を扱う。`l >= r` は空区間とし、`query` は単位元を返し、`apply` は何もしない。

## 特徴

- 長さ 2 * size の配列に値、長さ size の配列に内部ノードの保留中の作用素を保持する非再帰のボトムアップ実装である。
- `mapping(f, x)` は値 x に作用素 f を作用させた値、`composition(f, g)` は g の後に f を作用させる作用素を返す。省略した場合は区間代入（`mapping(f, x) = f`、`composition(f, g) = f`）となる。
- 汎用版と数値版は、作用素が未設定かを boolean 配列で管理する。作用素の単位元は指定しない。
- 構築・`fill`・`setAll` は葉に値を配置した後、共通の `build` で内部ノードを構築し、保留中の作用素を破棄する。
- 単一点の `get`・`set`・`apply(i, f)` は根から葉までの作用素を伝播した後、葉を直接読み書きする。
- 走査・配列化・文字列化は全ノードの作用素を伝播した後、葉を直接読む。
- 長さ・配列・コレクション（数値版）・コピーから構築できる。

### 型別クラス

| クラス                                                                            | 対象型 | 選ぶ目安                                                   |
|-----------------------------------------------------------------------------------|--------|------------------------------------------------------------|
| [`LazySegmentTree<T>`](../../../src/lib/ds/segmenttree/LazySegmentTree.java)      | `T`    | 区間長を持つノードなど、値と作用素を同じ任意の型で扱う場合 |
| [`IntLazySegmentTree`](../../../src/lib/ds/segmenttree/IntLazySegmentTree.java)   | `int`  | 値と作用素が int に収まる場合                              |
| [`LongLazySegmentTree`](../../../src/lib/ds/segmenttree/LongLazySegmentTree.java) | `long` | 値と作用素が long に収まる場合                             |

## 依存関係

- Java標準ライブラリ: `java.util.*`、`java.util.function.*`
- 数値版: [IntCollection](../../../src/lib/ds/IntCollection.java) / [LongCollection](../../../src/lib/ds/LongCollection.java)

## API

### `LazySegmentTree<T>`

[ソース](../../../src/lib/ds/segmenttree/LazySegmentTree.java)

| 種別        | シグネチャ                                                                                                                   | 戻り値               | 説明                                                         |
|-------------|------------------------------------------------------------------------------------------------------------------------------|----------------------|--------------------------------------------------------------|
| Constructor | `LazySegmentTree(int n, BinaryOperator<T> operator, T identity)`                                                             | —                    | 全要素を単位元で初期化し、作用を区間代入とする。             |
| Constructor | `LazySegmentTree(int n, BinaryOperator<T> operator, T identity, BiFunction<T, T, T> mapping, BinaryOperator<T> composition)` | —                    | 全要素を単位元で初期化し、作用と合成を指定する。             |
| Constructor | `LazySegmentTree(T[] a, BinaryOperator<T> operator, T identity)`                                                             | —                    | 配列の要素を添字順に配置し、作用を区間代入とする。           |
| Constructor | `LazySegmentTree(T[] a, BinaryOperator<T> operator, T identity, BiFunction<T, T, T> mapping, BinaryOperator<T> composition)` | —                    | 配列の要素を添字順に配置し、作用と合成を指定する。           |
| Constructor | `LazySegmentTree(LazySegmentTree<T> other)`                                                                                  | —                    | 内部配列を複製し、関数・単位元・要素オブジェクトを共有する。 |
| Method      | `size()`                                                                                                                     | `int`                | 要素数 n を返す。                                            |
| Method      | `isEmpty()`                                                                                                                  | `boolean`            | 要素数が0かを返す。                                          |
| Method      | `get(int i)`                                                                                                                 | `T`                  | 位置 i の値を返す。                                          |
| Method      | `set(int i, T v)`                                                                                                            | `T`                  | 位置 i を v へ更新し、作用を反映した更新前の値を返す。       |
| Method      | `apply(int i, T f)`                                                                                                          | `T`                  | 位置 i を `mapping(f, 現在値)` へ更新し、更新後の値を返す。  |
| Method      | `apply(int l, int r, T f)`                                                                                                   | `void`               | 半開区間 `[l, r)` の各要素に f を作用させる。                |
| Method      | `fill(T v)`                                                                                                                  | `LazySegmentTree<T>` | 全要素を v にして再構築する。                                |
| Method      | `setAll(IntFunction<T> init)`                                                                                                | `LazySegmentTree<T>` | 添字順に初期値関数を評価して再構築する。                     |
| Method      | `query(int l, int r)`                                                                                                        | `T`                  | 半開区間 `[l, r)` の集計結果を返す。                         |
| Method      | `queryAll()`                                                                                                                 | `T`                  | 全要素の集計結果を返す。                                     |
| Method      | `maxRight(int l, Predicate<T> tester)`                                                                                       | `int`                | `tester(query(l, r))` が真となる最大の r を返す。            |
| Method      | `minLeft(int r, Predicate<T> tester)`                                                                                        | `int`                | `tester(query(l, r))` が真となる最小の l を返す。            |
| Method      | `iterator()`                                                                                                                 | `Iterator<T>`        | 全作用素を伝播した後、添字順に要素を走査する。               |
| Method      | `toString()`                                                                                                                 | `String`             | 要素を空白区切りで連結する。空の場合は空文字列を返す。       |

`Iterable<T>` の `forEach(Consumer<? super T> action)` と `spliterator()` も継承する。

### `IntLazySegmentTree`

[ソース](../../../src/lib/ds/segmenttree/IntLazySegmentTree.java)

`IntCollection` を実装する。

| 種別        | シグネチャ                                                                                                                                | 戻り値                    | 説明                                                        |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------------|---------------------------|-------------------------------------------------------------|
| Constructor | `IntLazySegmentTree(int n, IntBinaryOperator operator, int identity)`                                                                     | —                         | 全要素を単位元で初期化し、作用を区間代入とする。            |
| Constructor | `IntLazySegmentTree(int n, IntBinaryOperator operator, int identity, IntBinaryOperator mapping, IntBinaryOperator composition)`           | —                         | 全要素を単位元で初期化し、作用と合成を指定する。            |
| Constructor | `IntLazySegmentTree(int[] a, IntBinaryOperator operator, int identity)`                                                                   | —                         | 配列の要素を添字順に配置し、作用を区間代入とする。          |
| Constructor | `IntLazySegmentTree(int[] a, IntBinaryOperator operator, int identity, IntBinaryOperator mapping, IntBinaryOperator composition)`         | —                         | 配列の要素を添字順に配置し、作用と合成を指定する。          |
| Constructor | `IntLazySegmentTree(IntCollection a, IntBinaryOperator operator, int identity)`                                                           | —                         | コレクションの走査順に要素を配置し、作用を区間代入とする。  |
| Constructor | `IntLazySegmentTree(IntCollection a, IntBinaryOperator operator, int identity, IntBinaryOperator mapping, IntBinaryOperator composition)` | —                         | コレクションの走査順に要素を配置し、作用と合成を指定する。  |
| Constructor | `IntLazySegmentTree(IntLazySegmentTree other)`                                                                                            | —                         | 内部配列を複製し、関数・単位元を共有する。                  |
| Method      | `size()`                                                                                                                                  | `int`                     | 要素数 n を返す。                                           |
| Method      | `get(int i)`                                                                                                                              | `int`                     | 位置 i の値を返す。                                         |
| Method      | `set(int i, int v)`                                                                                                                       | `int`                     | 位置 i を v へ更新し、作用を反映した更新前の値を返す。      |
| Method      | `apply(int i, int f)`                                                                                                                     | `int`                     | 位置 i を `mapping(f, 現在値)` へ更新し、更新後の値を返す。 |
| Method      | `apply(int l, int r, int f)`                                                                                                              | `void`                    | 半開区間 `[l, r)` の各要素に f を作用させる。               |
| Method      | `fill(int v)`                                                                                                                             | `IntLazySegmentTree`      | 全要素を v にして再構築する。                               |
| Method      | `setAll(IntUnaryOperator init)`                                                                                                           | `IntLazySegmentTree`      | 添字順に初期値関数を評価して再構築する。                    |
| Method      | `query(int l, int r)`                                                                                                                     | `int`                     | 半開区間 `[l, r)` の集計結果を返す。                        |
| Method      | `queryAll()`                                                                                                                              | `int`                     | 全要素の集計結果を返す。                                    |
| Method      | `maxRight(int l, IntPredicate tester)`                                                                                                    | `int`                     | `tester(query(l, r))` が真となる最大の r を返す。           |
| Method      | `minLeft(int r, IntPredicate tester)`                                                                                                     | `int`                     | `tester(query(l, r))` が真となる最小の l を返す。           |
| Method      | `toArray()`                                                                                                                               | `int[]`                   | 全作用素を伝播した後、葉の区間を複製した配列を返す。        |
| Method      | `iterator()`                                                                                                                              | `PrimitiveIterator.OfInt` | 全作用素を伝播した後、添字順に要素を走査する。              |
| Method      | `toString()`                                                                                                                              | `String`                  | 要素を空白区切りで連結する。空の場合は空文字列を返す。      |

### `LongLazySegmentTree`

[ソース](../../../src/lib/ds/segmenttree/LongLazySegmentTree.java)

`LongCollection` を実装する。値・単位元・作用素が long になる以外は `IntLazySegmentTree` と同じである。

| 種別        | シグネチャ                                                                                                                                      | 戻り値                     | 説明                                                        |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------|-------------------------------------------------------------|
| Constructor | `LongLazySegmentTree(int n, LongBinaryOperator operator, long identity)`                                                                        | —                          | 全要素を単位元で初期化し、作用を区間代入とする。            |
| Constructor | `LongLazySegmentTree(int n, LongBinaryOperator operator, long identity, LongBinaryOperator mapping, LongBinaryOperator composition)`            | —                          | 全要素を単位元で初期化し、作用と合成を指定する。            |
| Constructor | `LongLazySegmentTree(long[] a, LongBinaryOperator operator, long identity)`                                                                     | —                          | 配列の要素を添字順に配置し、作用を区間代入とする。          |
| Constructor | `LongLazySegmentTree(long[] a, LongBinaryOperator operator, long identity, LongBinaryOperator mapping, LongBinaryOperator composition)`         | —                          | 配列の要素を添字順に配置し、作用と合成を指定する。          |
| Constructor | `LongLazySegmentTree(LongCollection a, LongBinaryOperator operator, long identity)`                                                             | —                          | コレクションの走査順に要素を配置し、作用を区間代入とする。  |
| Constructor | `LongLazySegmentTree(LongCollection a, LongBinaryOperator operator, long identity, LongBinaryOperator mapping, LongBinaryOperator composition)` | —                          | コレクションの走査順に要素を配置し、作用と合成を指定する。  |
| Constructor | `LongLazySegmentTree(LongLazySegmentTree other)`                                                                                                | —                          | 内部配列を複製し、関数・単位元を共有する。                  |
| Method      | `size()`                                                                                                                                        | `int`                      | 要素数 n を返す。                                           |
| Method      | `get(int i)`                                                                                                                                    | `long`                     | 位置 i の値を返す。                                         |
| Method      | `set(int i, long v)`                                                                                                                            | `long`                     | 位置 i を v へ更新し、作用を反映した更新前の値を返す。      |
| Method      | `apply(int i, long f)`                                                                                                                          | `long`                     | 位置 i を `mapping(f, 現在値)` へ更新し、更新後の値を返す。 |
| Method      | `apply(int l, int r, long f)`                                                                                                                   | `void`                     | 半開区間 `[l, r)` の各要素に f を作用させる。               |
| Method      | `fill(long v)`                                                                                                                                  | `LongLazySegmentTree`      | 全要素を v にして再構築する。                               |
| Method      | `setAll(IntToLongFunction init)`                                                                                                                | `LongLazySegmentTree`      | 添字順に初期値関数を評価して再構築する。                    |
| Method      | `query(int l, int r)`                                                                                                                           | `long`                     | 半開区間 `[l, r)` の集計結果を返す。                        |
| Method      | `queryAll()`                                                                                                                                    | `long`                     | 全要素の集計結果を返す。                                    |
| Method      | `maxRight(int l, LongPredicate tester)`                                                                                                         | `int`                      | `tester(query(l, r))` が真となる最大の r を返す。           |
| Method      | `minLeft(int r, LongPredicate tester)`                                                                                                          | `int`                      | `tester(query(l, r))` が真となる最小の l を返す。           |
| Method      | `toArray()`                                                                                                                                     | `long[]`                   | 全作用素を伝播した後、葉の区間を複製した配列を返す。        |
| Method      | `iterator()`                                                                                                                                    | `PrimitiveIterator.OfLong` | 全作用素を伝播した後、添字順に要素を走査する。              |
| Method      | `toString()`                                                                                                                                    | `String`                   | 要素を空白区切りで連結する。空の場合は空文字列を返す。      |

### 数値版の継承 API

`IntLazySegmentTree` / `LongLazySegmentTree` は `IntCollection` / `LongCollection` の既定メソッド `isEmpty`、`contains`、`forEachInt` / `forEachLong`、`spliterator`、`intStream` / `longStream`、`toList`、`forEach` を継承する。いずれも `iterator()` を使うため、走査開始時に全作用素を伝播する。全シグネチャと既定実装は [配列・コレクションの継承API](../arrays/ArrayGuide.md#継承元のコレクション-api) を参照する。

## 利用例

準備中

## 制約・注意事項

- 添字は `[0, n)`、区間は `0 <= l`、`r <= n` とする。葉は配列の `[size, size + n)` に置くため、範囲外の添字は配列アクセスでは検出できない。そのため単一点の操作・区間の `apply`・`query`・`maxRight`・`minLeft` は状態を変更する前に検査し、範囲外では `IndexOutOfBoundsException` を投げる。区間の `apply`・`query` は `l >= r` を空区間として扱い、範囲を検査しない。
- `operator` は結合則を満たし、`identity` はその単位元とする。`mapping` は `operator` に対して分配的（`mapping(f, operator(x, y)) = operator(mapping(f, x), mapping(f, y))`）であり、`mapping(composition(f, g), x) = mapping(f, mapping(g, x))` を満たすこと。
- 作用素は区間長を受け取らない。区間和への一様加算など区間長に依存する作用は、`LazySegmentTree<T>` のノードに区間長を持たせて表す。
- `LazySegmentTree<T>` の `identity` が null の場合、`operator` は null を受け付けること。配列とコピーからの構築は内部配列を共有せず、要素オブジェクトは共有する。
- `maxRight(l, tester)` は `0 <= l <= n`、`minLeft(r, tester)` は `0 <= r <= n` とする。`tester(identity)` は真とし、区間を広げると真から偽へ一度だけ変わる条件とする。
- `get`・`query`・二分探索・走査は保留中の作用素を伝播するため、内部状態を変更する。結果の値は変わらない。
- 初期値関数・演算・判定関数の中で同じインスタンスを変更しない。iterator の終端後の取得は `NoSuchElementException` を投げる。

## 性能特性

関数の評価時間は別途加算する。

| 操作                        | 時間計算量 | 補足                                            |
|-----------------------------|------------|-------------------------------------------------|
| 構築・`fill`・`setAll`      | O(n)       | 内部ノードを葉から一度だけ集計する              |
| コピー                      | O(n)       | 値・作用素・フラグの配列を複製する              |
| `queryAll`                  | O(1)       | 根を直接読む                                    |
| `get`・`set`・`apply(i, f)` | O(log n)   | 根から葉まで伝播し、`set`・`apply` は再集計する |
| `apply(l, r, f)`・`query`   | O(log n)   | 境界のノードだけを伝播・再集計する              |
| `maxRight`・`minLeft`       | O(log n)   | 始点の経路を伝播した後、上昇と下降を行う        |
| `toArray`・走査・文字列化   | O(n)       | 全ノードを一度だけ伝播し、葉を直接読む          |

- **空間計算量**: O(n)。値の配列長は 2 * size、作用素とフラグの配列長は size であり、size < 2n である。

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
|--------------------|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-30 | 3クラス: 初期実装（`apply(l, r, f)` による区間作用と `query`）                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| **バージョン 1.1** | 2026-10-07 | LongLazySegmentTree: `setAll` の初期化関数を `IntToLongFunction` に変更                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| **バージョン 2.0** | 2026-10-27 | 3クラス: 単一点の `set` に更新前の値を返す機能を追加                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **バージョン 3.0** | 2026-10-28 | 3クラス: `fill`・`setAll` が自身を返すように変更。`minLeft` が処理済みノードではなく次のノードで終了判定していた誤りを修正。コピーからのコンストラクタを追加し、構築を `build` へ集約。`apply(i, f)` を経路の伝播と葉の直接更新へ変更し、更新後の値を返す仕様に変更。走査・文字列化を全ノードの伝播後に葉を読む O(n) の処理へ変更し、空の `toString` を空文字列に変更。作用素の配列長を size へ縮小。配列アクセスで検出できない範囲外の添字・区間の検査を追加。LazySegmentTree: `isEmpty` を追加。IntLazySegmentTree / LongLazySegmentTree: IntCollection / LongCollection を実装し、コレクションからのコンストラクタと `toArray` を追加 |
| **バージョン 3.1** | 2026-10-29 | 3クラス: 省略時の mapping・composition を定数 DEFAULT_MAPPING・DEFAULT_COMPOSITION へ統一し、引数名を `(f, x)`・`(f, g)` に揃えた。`set` の更新前の値を保持するローカル変数名を old へ統一                                                                                                                                                                                                                                                                                                                                                                                                                                               |
