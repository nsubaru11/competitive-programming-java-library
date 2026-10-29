# SquareSumSegmentTree 利用ガイド

> `IntSquareSumSegmentTree` の仕様をまとめる。int値の区間アフィン変換と区間和・区間二乗和を法 mod で処理する。

[← README: SegmentTree](./README.md) · ソース: [IntSquareSumSegmentTree.java](../../../src/lib/ds/segmenttree/IntSquareSumSegmentTree.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

各要素を `現在値 * m + d` へ更新する区間アフィン変換を遅延伝播し、区間の一次和と二乗和を法 mod で返す。要素数を n とし、葉の数 size は n 以上の最小の2冪（n <= 1 では1）、log = log2(size) とする。
添字は0始まりとし、区間を受け取る操作は半開区間 `[l, r)` を扱う。`l >= r` は空区間とし、`query`・`query2` は0を返し、区間更新は何もしない。

## 特徴

- 各ノードに区間の一次和と二乗和を保持する。作用 `x -> a * x + b` を長さ len の区間へ反映すると、一次和 S と二乗和 Q は `S' = a * S + b * len`、`Q' = a^2 * Q + 2ab * S + b^2 * len` となる。
- 区間更新は `a^2`・`b^2`・`2ab` を1回だけ計算し、作用させる各ノードで共有する。
- 要素・一次和・二乗和は `(-mod, mod)` の剰余として int 配列に保持し、ノードへの作用と区間の集計は long で計算する。作用素の係数 a, b は1つの long に詰めて保持する。取得値と戻り値は `[0, mod)` に正規化する。
- 構築・`fill`・`setAll` は葉に値を配置した後、共通の `build` で内部ノードを構築し、保留中の作用素を破棄する。
- 単一点の `get`・`set`・`add`・`multiply`・`affine` は根から葉までの作用素を伝播した後、葉を直接読み書きする。
- 走査・配列化・文字列化は全ノードの作用素を伝播した後、葉を直接読む。
- 長さ・配列・コレクション・コピーから構築できる。

## 依存関係

- Java標準ライブラリ: `java.util.*`、`java.util.function.*`
- [IntCollection](../../../src/lib/ds/IntCollection.java)

## API

### `IntSquareSumSegmentTree`

[ソース](../../../src/lib/ds/segmenttree/IntSquareSumSegmentTree.java)

公開フィールドは `final int size` と `final int mod` である。

`IntCollection` を実装する。

| 種別        | シグネチャ                                               | 戻り値                    | 説明                                                             |
|-------------|----------------------------------------------------------|---------------------------|------------------------------------------------------------------|
| Constructor | `IntSquareSumSegmentTree(int n)`                         | —                         | 全要素を0、法を998244353として初期化する。                       |
| Constructor | `IntSquareSumSegmentTree(int n, int mod)`                | —                         | 全要素を0、法を mod として初期化する。                           |
| Constructor | `IntSquareSumSegmentTree(int[] a)`                       | —                         | 配列の要素を添字順に配置し、法を998244353とする。                |
| Constructor | `IntSquareSumSegmentTree(int[] a, int mod)`              | —                         | 配列の要素を添字順に配置し、法を mod とする。                    |
| Constructor | `IntSquareSumSegmentTree(IntCollection a)`               | —                         | コレクションの走査順に要素を配置し、法を998244353とする。        |
| Constructor | `IntSquareSumSegmentTree(IntCollection a, int mod)`      | —                         | コレクションの走査順に要素を配置し、法を mod とする。            |
| Constructor | `IntSquareSumSegmentTree(IntSquareSumSegmentTree other)` | —                         | 内部配列を複製し、法を共有する。                                 |
| Method      | `size()`                                                 | `int`                     | 要素数 n を返す。                                                |
| Method      | `get(int i)`                                             | `int`                     | 位置 i の値を返す。                                              |
| Method      | `set(int i, int v)`                                      | `int`                     | 位置 i を v へ更新し、作用を反映した更新前の値を返す。           |
| Method      | `set(int l, int r, int v)`                               | `void`                    | 半開区間 `[l, r)` の各要素を v へ更新する。                      |
| Method      | `add(int i, int d)`                                      | `int`                     | 位置 i に d を加え、更新後の値を返す。                           |
| Method      | `add(int l, int r, int d)`                               | `void`                    | 半開区間 `[l, r)` の各要素に d を加える。                        |
| Method      | `multiply(int i, int m)`                                 | `int`                     | 位置 i を m 倍し、更新後の値を返す。                             |
| Method      | `multiply(int l, int r, int m)`                          | `void`                    | 半開区間 `[l, r)` の各要素を m 倍する。                          |
| Method      | `affine(int i, int m, int d)`                            | `int`                     | 位置 i を `現在値 * m + d` へ更新し、更新後の値を返す。          |
| Method      | `affine(int l, int r, int m, int d)`                     | `void`                    | 半開区間 `[l, r)` の各要素を `現在値 * m + d` へ更新する。       |
| Method      | `fill(int v)`                                            | `IntSquareSumSegmentTree` | 全要素を v にして再構築する。                                    |
| Method      | `setAll(IntUnaryOperator init)`                          | `IntSquareSumSegmentTree` | 添字順に初期値関数を評価して再構築する。                         |
| Method      | `query(int l, int r)`                                    | `int`                     | 半開区間 `[l, r)` の和を返す。                                   |
| Method      | `query2(int l, int r)`                                   | `int`                     | 半開区間 `[l, r)` の二乗和を返す。                               |
| Method      | `queryAll()`                                             | `int`                     | 全要素の和を返す。                                               |
| Method      | `query2All()`                                            | `int`                     | 全要素の二乗和を返す。                                           |
| Method      | `toArray()`                                              | `int[]`                   | 全作用素を伝播した後、正規化した要素の配列を返す。               |
| Method      | `iterator()`                                             | `PrimitiveIterator.OfInt` | 全作用素を伝播した後、添字順に正規化した要素を走査する。         |
| Method      | `toString()`                                             | `String`                  | 正規化した要素を空白区切りで連結する。空の場合は空文字列を返す。 |

### 継承 API

`IntCollection` の既定メソッド `isEmpty`、`contains`、`forEachInt`、`spliterator`、`intStream`、`toList`、`forEach` を継承する。いずれも `iterator()` を使うため、走査開始時に全作用素を伝播する。全シグネチャと既定実装は [配列・コレクションの継承API](../arrays/ArrayGuide.md#継承元のコレクション-api) を参照する。

## 利用例

準備中

## 制約・注意事項

- 添字は `[0, n)`、区間は `0 <= l`、`r <= n` とする。葉は配列の `[size, size + n)` に置くため、範囲外の添字は配列アクセスでは検出できない。そのため全ての単一点・区間の操作は状態を変更する前に検査し、範囲外では `IndexOutOfBoundsException` を投げる。区間の操作は `l >= r` を空区間として扱い、範囲を検査しない。
- `add`・`multiply`・区間の `set` は `affine` の係数を固定した操作である。
- 法は `1 <= mod <= 2 * 10^9` とする。この範囲で `a^2 * Q + 2ab * S + b^2 * len` を剰余を取る前に long で計算してもオーバーフローしない。
- 入力値・更新引数は負でもよい。いずれも `% mod` で `(-mod, mod)` へ縮めてから使い、取得値と戻り値は `[0, mod)` に正規化する。int に収まらない値は呼び出し側で `% mod` を取ってから渡す。
- `query2` は値の和の二乗ではなく、各要素の二乗の和を返す。
- `get`・`query`・`query2`・走査は保留中の作用素を伝播するため、内部状態を変更する。結果の値は変わらない。
- 初期値関数の中で同じインスタンスを変更しない。iterator の終端後の取得は `NoSuchElementException` を投げる。

## 性能特性

| 操作                                               | 時間計算量 | 補足                                              |
|----------------------------------------------------|------------|---------------------------------------------------|
| 構築・`fill`・`setAll`                             | O(n)       | 一次和・二乗和の内部ノードを葉から集計する        |
| コピー                                             | O(n)       | 和・二乗和・作用素の配列を複製する                |
| `queryAll`・`query2All`                            | O(1)       | 根を直接読む                                      |
| 単一点の `get`・`set`・`add`・`multiply`・`affine` | O(log n)   | 根から葉まで伝播し、更新は祖先を再集計する        |
| 区間の `set`・`add`・`multiply`・`affine`          | O(log n)   | 境界のノードだけを伝播・再集計する                |
| `query`・`query2`                                  | O(log n)   | 剰余を取らずに long で加算し、最後に1回剰余を取る |
| `toArray`・走査・文字列化                          | O(n)       | 全ノードを一度だけ伝播し、葉を直接読む            |

- **空間計算量**: O(n)。一次和・二乗和の配列長は 2 * size、作用素の配列長は size であり、size < 2n である。

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|--------------------|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-30 | IntSquareSumSegmentTree / LongSquareSumSegmentTree: 初期実装（`query`・`query2` と区間アフィン変換）                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| **バージョン 1.1** | 2026-10-07 | LongSquareSumSegmentTree: `setAll` の初期化関数を `IntToLongFunction` に変更                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| **バージョン 2.0** | 2026-10-27 | IntSquareSumSegmentTree / LongSquareSumSegmentTree: 単一点の `set` に更新前の値を返す機能を追加                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| **バージョン 3.0** | 2026-10-28 | LongSquareSumSegmentTree を廃止。保持する剰余は int に収まるため IntSquareSumSegmentTree に一本化。IntSquareSumSegmentTree: `fill`・`setAll` が自身を返すように変更。IntCollection を実装し、コレクションとコピーからのコンストラクタ、`toArray` を追加。単一点の `add`・`multiply`・`affine` を経路の伝播と葉の直接更新へ変更し、更新後の値を返す仕様に変更。走査・文字列化を全ノードの伝播後に葉を読む O(n) の処理へ変更し、空の `toString` を空文字列に変更。作用素の配列長を size へ縮小。アフィン変換の `apply` を `affine` へ改名し、引数名を m / d に統一。配列アクセスで検出できない範囲外の添字・区間の検査を追加 |
| **バージョン 3.1** | 2026-10-29 | IntSquareSumSegmentTree: 既定の法の定数名を MOD から DEFAULT_MOD へ変更し、更新前の値を保持するローカル変数名を old へ統一                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| **バージョン 4.0** | 2026-10-29 | IntSquareSumSegmentTree: 要素数 `size` と法 `mod` を公開フィールドにし、内部の2のべき乗の葉数を `leafSize` へ改名                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
