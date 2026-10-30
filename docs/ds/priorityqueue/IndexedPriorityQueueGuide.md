# IndexedPriorityQueue 利用ガイド

> 固定範囲の添字にコストを対応付け、コストの更新・改善・削除と削除後の最終コストの取得を行う `IndexedPriorityQueue<T>`・`IntIndexedPriorityQueue`・`LongIndexedPriorityQueue` の仕様をまとめる。

[← README: Priority Queue](./README.md) · ソース: [IndexedPriorityQueue.java](../../../src/lib/ds/priorityqueue/IndexedPriorityQueue.java) · [IntIndexedPriorityQueue.java](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java) · [LongIndexedPriorityQueue.java](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

添字 `0 <= i < n` ごとに高々1個のコストを持つ二分ヒープである。n は構築時に固定する。
添字からヒープ上の位置を引く配列を持ち、任意の添字のコスト更新と削除を O(log n) で行う。Dijkstra 法や Prim 法のように、頂点ごとの暫定コストを改善しながら最小のものを確定させる用途を想定する。
active な添字の数を s、未整列領域の添字の数を k とする。

## 特徴

- 各添字は「未追加」「active（キュー内）」「削除済み（`poll` 系・`remove` で取り出し済み）」の3状態を持つ。
- `add` は active でない添字を登録し、`set` は無条件に更新・登録し、`relax` はより優先されるコストだけを反映する。`relax` は削除済みの添字を再登録しない。
- `get` 系は active な添字のコスト、`getCost` 系は削除済みを含めて最後に持ったコストを返す。Dijkstra 法の確定距離を `getCostOrDefault` で取得できる。
- コストと添字の両方について `peek`・`poll` と `OrDefault` 系を提供する。
- `clear` は世代番号 stamp を1減らすだけで全添字を未追加に戻し、O(1) で動作する。
- 登録・配列からの構築・`setAll` はヒープ化を遅延する。ヒープ領域内の `set`・`relax` は即座に `siftUp`・`siftDown` し、未整列領域内では値の更新だけを行う。未整列領域の添字の `remove` は末尾の添字と入れ替えて O(1) で外す。
- 比較順は昇順（汎用版は自然順序）・降順（汎用版は逆順）・Comparator から選ぶ。
- index 数・配列・コピーから構築できる。関数による初期化は構築後に `setAll` を呼ぶ。

### 型別クラス

| クラス                                                                                        | 対象型 | 選ぶ目安                                       |
|-----------------------------------------------------------------------------------------------|--------|------------------------------------------------|
| [`IndexedPriorityQueue<T>`](../../../src/lib/ds/priorityqueue/IndexedPriorityQueue.java)      | `T`    | Comparable なコスト、Comparator で並べるコスト |
| [`IntIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java)   | `int`  | int のコストをボクシングせずに扱う場合         |
| [`LongIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java) | `long` | 距離など long のコストを扱う場合               |

## 依存関係

- Java標準ライブラリ: `java.util.*`、`java.util.function.*`
- int 版: [IntCollection](../../../src/lib/ds/IntCollection.java)、[IntComparator](../../../src/lib/util/function/IntComparator.java)
- long 版: [LongCollection](../../../src/lib/ds/LongCollection.java)、[LongComparator](../../../src/lib/util/function/LongComparator.java)
- 利用側: [Dijkstra](../../../src/lib/graph/Dijkstra.java)・[Prim](../../../src/lib/graph/Prim.java) が `LongIndexedPriorityQueue` を使う。

## API

### `IndexedPriorityQueue<T>`

[ソース](../../../src/lib/ds/priorityqueue/IndexedPriorityQueue.java)

`Iterable<T>` を実装する。

公開フィールドは `final int indexCount` であり、`indexCount()` と同じ値を持つ。

| 種別        | シグネチャ                                                                              | 戻り値                    | 説明                                                                                             |
|-------------|-----------------------------------------------------------------------------------------|---------------------------|--------------------------------------------------------------------------------------------------|
| Constructor | `IndexedPriorityQueue(int n)`                                                           | —                         | 添字数 n、自然順序で構築する。全添字は未追加。                                                   |
| Constructor | `IndexedPriorityQueue(int n, boolean isDescendingOrder)`                                | —                         | 添字数 n、自然順序または逆順で構築する。                                                         |
| Constructor | `IndexedPriorityQueue(int n, Comparator<? super T> comparator)`                         | —                         | 添字数 n、Comparator の順で構築する。                                                            |
| Constructor | `IndexedPriorityQueue(T[] costs)`                                                       | —                         | n = costs.length とし、costs[i] を添字 i のコストとして全添字を自然順序で登録する。              |
| Constructor | `IndexedPriorityQueue(T[] costs, boolean isDescendingOrder)`                            | —                         | 全添字を自然順序または逆順で登録する。                                                           |
| Constructor | `IndexedPriorityQueue(T[] costs, Comparator<? super T> comparator)`                     | —                         | 全添字を Comparator の順で登録する。                                                             |
| Constructor | `IndexedPriorityQueue(Collection<? extends T> costs)`                                   | —                         | n = costs.size() とし、iterator の j 番目の値を添字 j のコストとして全添字を自然順序で登録する。 |
| Constructor | `IndexedPriorityQueue(Collection<? extends T> costs, boolean isDescendingOrder)`        | —                         | 全添字を自然順序または逆順で登録する。                                                           |
| Constructor | `IndexedPriorityQueue(Collection<? extends T> costs, Comparator<? super T> comparator)` | —                         | 全添字を Comparator の順で登録する。                                                             |
| Constructor | `IndexedPriorityQueue(IndexedPriorityQueue<T> other)`                                   | —                         | 比較順・状態・コストを引き継ぐ独立したコピー。コスト自体は共有する。                             |
| Method      | `size()`                                                                                | `int`                     | active な添字の数を返す。                                                                        |
| Method      | `indexCount()`                                                                          | `int`                     | 使用できる添字の数 n を返す。                                                                    |
| Method      | `isEmpty()`                                                                             | `boolean`                 | active な添字がないかを返す。                                                                    |
| Method      | `containsIndex(int i)`                                                                  | `boolean`                 | 添字が active かを返す。                                                                         |
| Method      | `hasCost(int i)`                                                                        | `boolean`                 | 添字が active または削除済みかを返す。                                                           |
| Method      | `get(int i)`                                                                            | `T`                       | active な添字のコストを返す。                                                                    |
| Method      | `getOrDefault(int i, T defaultValue)`                                                   | `T`                       | active でなければ defaultValue を返す。                                                          |
| Method      | `getCost(int i)`                                                                        | `T`                       | active または削除済みの添字が最後に持ったコストを返す。                                          |
| Method      | `getCostOrDefault(int i, T defaultValue)`                                               | `T`                       | 未追加なら defaultValue を返す。                                                                 |
| Method      | `peek()`                                                                                | `T`                       | 最優先のコストを返す。                                                                           |
| Method      | `peekOrDefault(T defaultValue)`                                                         | `T`                       | 空なら defaultValue、そうでなければ最優先のコストを返す。                                        |
| Method      | `peekIndex()`                                                                           | `int`                     | 最優先の添字を返す。                                                                             |
| Method      | `peekIndexOrDefault(int defaultIndex)`                                                  | `int`                     | 空なら defaultIndex、そうでなければ最優先の添字を返す。                                          |
| Method      | `peekSecond()`                                                                          | `T`                       | 2番目に優先されるコストを返す。要素数は2以上とする。                                             |
| Method      | `set(int i, T c)`                                                                       | `boolean`                 | active なら c へ更新して false、そうでなければ c で登録して true。                               |
| Method      | `add(int i, T c)`                                                                       | `boolean`                 | active でなければ登録して true、active なら false。削除済みの添字も再登録する。                  |
| Method      | `relax(int i, T c)`                                                                     | `boolean`                 | 未追加なら登録、active で c がより優先されるなら更新して true。削除済みは false。                |
| Method      | `poll()`                                                                                | `T`                       | 最優先の添字を削除済みにしてコストを返す。                                                       |
| Method      | `pollOrDefault(T defaultValue)`                                                         | `T`                       | 空なら defaultValue、そうでなければ `poll` する。                                                |
| Method      | `pollIndex()`                                                                           | `int`                     | 最優先の添字を削除済みにして添字を返す。                                                         |
| Method      | `pollIndexOrDefault(int defaultIndex)`                                                  | `int`                     | 空なら defaultIndex、そうでなければ `pollIndex` する。                                           |
| Method      | `remove(int i)`                                                                         | `boolean`                 | active なら削除済みにして true、そうでなければ false。未整列領域の添字はヒープを復元せずに外す。 |
| Method      | `setAll(IntFunction<? extends T> init)`                                                 | `IndexedPriorityQueue<T>` | 全記録を消去し、init(i) を添字 i のコストとして全添字を登録して自身を返す。                      |
| Method      | `clear()`                                                                               | `void`                    | 全添字を未追加に戻す。                                                                           |
| Method      | `iterator()`                                                                            | `Iterator<T>`             | active な添字のコストを内部配列の順に走査する。                                                  |
| Method      | `toString()`                                                                            | `String`                  | active な添字のコストを内部配列の順に空白区切りで連結する。空の場合は空文字列を返す。            |

継承するメソッド：

- `Iterable`：`forEach`、`spliterator`

### `IntIndexedPriorityQueue`

[ソース](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java)

`IntCollection` を実装する。コストを `int` とし、比較順は昇順・降順・`IntComparator` から選ぶ。

公開フィールドは `final int indexCount` であり、`indexCount()` と同じ値を持つ。

| 種別        | シグネチャ                                                                | 戻り値                    | 説明                                                                                             |
|-------------|---------------------------------------------------------------------------|---------------------------|--------------------------------------------------------------------------------------------------|
| Constructor | `IntIndexedPriorityQueue(int n)`                                          | —                         | 添字数 n、昇順で構築する。全添字は未追加。                                                       |
| Constructor | `IntIndexedPriorityQueue(int n, boolean isDescendingOrder)`               | —                         | 添字数 n、昇順または降順で構築する。                                                             |
| Constructor | `IntIndexedPriorityQueue(int n, IntComparator comparator)`                | —                         | 添字数 n、Comparator の順で構築する。                                                            |
| Constructor | `IntIndexedPriorityQueue(int[] costs)`                                    | —                         | n = costs.length とし、costs[i] を添字 i のコストとして全添字を昇順で登録する。                  |
| Constructor | `IntIndexedPriorityQueue(int[] costs, boolean isDescendingOrder)`         | —                         | 全添字を昇順または降順で登録する。                                                               |
| Constructor | `IntIndexedPriorityQueue(int[] costs, IntComparator comparator)`          | —                         | 全添字を Comparator の順で登録する。                                                             |
| Constructor | `IntIndexedPriorityQueue(IntCollection costs)`                            | —                         | n = costs.size() とし、iterator の j 番目の値を添字 j のコストとして全添字を昇順で登録する。     |
| Constructor | `IntIndexedPriorityQueue(IntCollection costs, boolean isDescendingOrder)` | —                         | 全添字を昇順または降順で登録する。                                                               |
| Constructor | `IntIndexedPriorityQueue(IntCollection costs, IntComparator comparator)`  | —                         | 全添字を Comparator の順で登録する。                                                             |
| Constructor | `IntIndexedPriorityQueue(IntIndexedPriorityQueue other)`                  | —                         | 比較順・状態・コストを引き継ぐ独立したコピー。                                                   |
| Method      | `size()`                                                                  | `int`                     | active な添字の数を返す。                                                                        |
| Method      | `indexCount()`                                                            | `int`                     | 使用できる添字の数 n を返す。                                                                    |
| Method      | `containsIndex(int i)`                                                    | `boolean`                 | 添字が active かを返す。コストを探索する `contains` とは異なる。                                 |
| Method      | `hasCost(int i)`                                                          | `boolean`                 | 添字が active または削除済みかを返す。                                                           |
| Method      | `get(int i)`                                                              | `int`                     | active な添字のコストを返す。                                                                    |
| Method      | `getOrDefault(int i, int defaultValue)`                                   | `int`                     | active でなければ defaultValue を返す。                                                          |
| Method      | `getCost(int i)`                                                          | `int`                     | active または削除済みの添字が最後に持ったコストを返す。                                          |
| Method      | `getCostOrDefault(int i, int defaultValue)`                               | `int`                     | 未追加なら defaultValue を返す。                                                                 |
| Method      | `peek()`                                                                  | `int`                     | 最優先のコストを返す。                                                                           |
| Method      | `peekOrDefault(int defaultValue)`                                         | `int`                     | 空なら defaultValue、そうでなければ最優先のコストを返す。                                        |
| Method      | `peekIndex()`                                                             | `int`                     | 最優先の添字を返す。                                                                             |
| Method      | `peekIndexOrDefault(int defaultIndex)`                                    | `int`                     | 空なら defaultIndex、そうでなければ最優先の添字を返す。                                          |
| Method      | `peekSecond()`                                                            | `int`                     | 2番目に優先されるコストを返す。要素数は2以上とする。                                             |
| Method      | `set(int i, int c)`                                                       | `boolean`                 | active なら c へ更新して false、そうでなければ c で登録して true。                               |
| Method      | `add(int i, int c)`                                                       | `boolean`                 | active でなければ登録して true、active なら false。削除済みの添字も再登録する。                  |
| Method      | `relax(int i, int c)`                                                     | `boolean`                 | 未追加なら登録、active で c がより優先されるなら更新して true。削除済みは false。                |
| Method      | `poll()`                                                                  | `int`                     | 最優先の添字を削除済みにしてコストを返す。                                                       |
| Method      | `pollOrDefault(int defaultValue)`                                         | `int`                     | 空なら defaultValue、そうでなければ `poll` する。                                                |
| Method      | `pollIndex()`                                                             | `int`                     | 最優先の添字を削除済みにして添字を返す。                                                         |
| Method      | `pollIndexOrDefault(int defaultIndex)`                                    | `int`                     | 空なら defaultIndex、そうでなければ `pollIndex` する。                                           |
| Method      | `remove(int i)`                                                           | `boolean`                 | active なら削除済みにして true、そうでなければ false。未整列領域の添字はヒープを復元せずに外す。 |
| Method      | `setAll(IntUnaryOperator init)`                                           | `IntIndexedPriorityQueue` | 全記録を消去し、init(i) を添字 i のコストとして全添字を登録して自身を返す。                      |
| Method      | `clear()`                                                                 | `void`                    | 全添字を未追加に戻す。                                                                           |
| Method      | `iterator()`                                                              | `PrimitiveIterator.OfInt` | active な添字のコストを内部配列の順に走査する。                                                  |
| Method      | `toString()`                                                              | `String`                  | active な添字のコストを内部配列の順に空白区切りで連結する。空の場合は空文字列を返す。            |

継承するメソッド：

- [`IntCollection`](../arrays/ArrayGuide.md#intcollection)：`contains`、`forEachInt`、`intStream`、`isEmpty`、`spliterator`、`toArray`、`toList`
- `Iterable`：`forEach`

### `LongIndexedPriorityQueue`

[ソース](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java)

`LongCollection` を実装する。`IntIndexedPriorityQueue` のコストを `long` に、`IntComparator`・`IntUnaryOperator` を `LongComparator`・`IntToLongFunction` に置き換えた API を持つ。添字の引数・戻り値は `int` のままである。

公開フィールドは `final int indexCount` であり、`indexCount()` と同じ値を持つ。

| 種別        | シグネチャ                                                                  | 戻り値                     | 説明                                                                                             |
|-------------|-----------------------------------------------------------------------------|----------------------------|--------------------------------------------------------------------------------------------------|
| Constructor | `LongIndexedPriorityQueue(int n)`                                           | —                          | 添字数 n、昇順で構築する。全添字は未追加。                                                       |
| Constructor | `LongIndexedPriorityQueue(int n, boolean isDescendingOrder)`                | —                          | 添字数 n、昇順または降順で構築する。                                                             |
| Constructor | `LongIndexedPriorityQueue(int n, LongComparator comparator)`                | —                          | 添字数 n、Comparator の順で構築する。                                                            |
| Constructor | `LongIndexedPriorityQueue(long[] costs)`                                    | —                          | n = costs.length とし、costs[i] を添字 i のコストとして全添字を昇順で登録する。                  |
| Constructor | `LongIndexedPriorityQueue(long[] costs, boolean isDescendingOrder)`         | —                          | 全添字を昇順または降順で登録する。                                                               |
| Constructor | `LongIndexedPriorityQueue(long[] costs, LongComparator comparator)`         | —                          | 全添字を Comparator の順で登録する。                                                             |
| Constructor | `LongIndexedPriorityQueue(LongCollection costs)`                            | —                          | n = costs.size() とし、iterator の j 番目の値を添字 j のコストとして全添字を昇順で登録する。     |
| Constructor | `LongIndexedPriorityQueue(LongCollection costs, boolean isDescendingOrder)` | —                          | 全添字を昇順または降順で登録する。                                                               |
| Constructor | `LongIndexedPriorityQueue(LongCollection costs, LongComparator comparator)` | —                          | 全添字を Comparator の順で登録する。                                                             |
| Constructor | `LongIndexedPriorityQueue(LongIndexedPriorityQueue other)`                  | —                          | 比較順・状態・コストを引き継ぐ独立したコピー。                                                   |
| Method      | `size()`                                                                    | `int`                      | active な添字の数を返す。                                                                        |
| Method      | `indexCount()`                                                              | `int`                      | 使用できる添字の数 n を返す。                                                                    |
| Method      | `containsIndex(int i)`                                                      | `boolean`                  | 添字が active かを返す。コストを探索する `contains` とは異なる。                                 |
| Method      | `hasCost(int i)`                                                            | `boolean`                  | 添字が active または削除済みかを返す。                                                           |
| Method      | `get(int i)`                                                                | `long`                     | active な添字のコストを返す。                                                                    |
| Method      | `getOrDefault(int i, long defaultValue)`                                    | `long`                     | active でなければ defaultValue を返す。                                                          |
| Method      | `getCost(int i)`                                                            | `long`                     | active または削除済みの添字が最後に持ったコストを返す。                                          |
| Method      | `getCostOrDefault(int i, long defaultValue)`                                | `long`                     | 未追加なら defaultValue を返す。                                                                 |
| Method      | `peek()`                                                                    | `long`                     | 最優先のコストを返す。                                                                           |
| Method      | `peekOrDefault(long defaultValue)`                                          | `long`                     | 空なら defaultValue、そうでなければ最優先のコストを返す。                                        |
| Method      | `peekIndex()`                                                               | `int`                      | 最優先の添字を返す。                                                                             |
| Method      | `peekIndexOrDefault(int defaultIndex)`                                      | `int`                      | 空なら defaultIndex、そうでなければ最優先の添字を返す。                                          |
| Method      | `peekSecond()`                                                              | `long`                     | 2番目に優先されるコストを返す。要素数は2以上とする。                                             |
| Method      | `set(int i, long c)`                                                        | `boolean`                  | active なら c へ更新して false、そうでなければ c で登録して true。                               |
| Method      | `add(int i, long c)`                                                        | `boolean`                  | active でなければ登録して true、active なら false。削除済みの添字も再登録する。                  |
| Method      | `relax(int i, long c)`                                                      | `boolean`                  | 未追加なら登録、active で c がより優先されるなら更新して true。削除済みは false。                |
| Method      | `poll()`                                                                    | `long`                     | 最優先の添字を削除済みにしてコストを返す。                                                       |
| Method      | `pollOrDefault(long defaultValue)`                                          | `long`                     | 空なら defaultValue、そうでなければ `poll` する。                                                |
| Method      | `pollIndex()`                                                               | `int`                      | 最優先の添字を削除済みにして添字を返す。                                                         |
| Method      | `pollIndexOrDefault(int defaultIndex)`                                      | `int`                      | 空なら defaultIndex、そうでなければ `pollIndex` する。                                           |
| Method      | `remove(int i)`                                                             | `boolean`                  | active なら削除済みにして true、そうでなければ false。未整列領域の添字はヒープを復元せずに外す。 |
| Method      | `setAll(IntToLongFunction init)`                                            | `LongIndexedPriorityQueue` | 全記録を消去し、init(i) を添字 i のコストとして全添字を登録して自身を返す。                      |
| Method      | `clear()`                                                                   | `void`                     | 全添字を未追加に戻す。                                                                           |
| Method      | `iterator()`                                                                | `PrimitiveIterator.OfLong` | active な添字のコストを内部配列の順に走査する。                                                  |
| Method      | `toString()`                                                                | `String`                   | active な添字のコストを内部配列の順に空白区切りで連結する。空の場合は空文字列を返す。            |

継承するメソッド：

- [`LongCollection`](../arrays/ArrayGuide.md#longcollection)：`contains`、`forEachLong`、`isEmpty`、`longStream`、`spliterator`、`toArray`、`toList`
- `Iterable`：`forEach`

### 状態と操作の対応

| 状態     | `add(i, c)`     | `set(i, c)`     | `relax(i, c)`   | `remove(i)`   | `get(i)` | `getCost(i)` |
|----------|-----------------|-----------------|-----------------|---------------|----------|--------------|
| 未追加   | 登録して true   | 登録して true   | 登録して true   | false         | 例外     | 例外         |
| active   | false           | 更新して false  | 改善時だけ true | 削除して true | コスト   | コスト       |
| 削除済み | 再登録して true | 再登録して true | false           | false         | 例外     | 最後のコスト |

`OrDefault` 系は「例外」の欄で defaultValue を返す。`containsIndex` は active、`hasCost` は active または削除済みで true を返す。

active の判定は `position[i]` が `[0, size)` にあり、かつ `heap[position[i]] == i` であることで行う。削除時は `position[i]` に現在の stamp を書き、削除済みの判定は `position[i] == stamp` で行う。`clear` は stamp を1減らすため、それ以前の削除済み・active の添字はすべて未追加として扱われる。

## 利用例

準備中

## 制約・注意事項

- 添字は `0 <= i < indexCount()` とする。範囲は検査せず、範囲外では配列アクセスの `ArrayIndexOutOfBoundsException` を投げる。
- `compare(a, b) < 0` となるコスト a を持つ添字が先に取り出される。同順位のコストを持つ添字の取り出し順は規定しない。
- 空のキューの `peek`・`peekIndex`・`poll`・`pollIndex` と、要素数2未満の `peekSecond` は `NoSuchElementException` を投げる。
- `get` は active でない添字、`getCost` は未追加の添字で `NoSuchElementException` を投げる。
- `relax` は削除済みの添字を再登録しない。確定済みの頂点を再び開く場合は `add` または `set` を使う。
- `setAll` と配列からの構築は全添字を active にする。`setAll` は直前までの削除済みの記録も消去する。
- Comparator を省略した汎用版は全コストが相互に Comparable であることを前提とする。そうでない型もコンパイルできるが、最初に比較した時点で `ClassCastException` を投げる。汎用版のコストに null を使わない。
- Comparator を受け取るコンストラクタは null を `NullPointerException` で拒否する。
- 走査・`contains`・配列化・Stream・`toString` はヒープを復元せず内部配列の順に行い、優先順ではない。走査中にキューを変更しない。
- stamp は `clear`・`setAll` のたびに1減る。int の範囲を一巡するほどの回数は想定しない。

## 性能特性

| 操作                                                                                      | 時間計算量         | 補足                                                                                      |
|-------------------------------------------------------------------------------------------|--------------------|-------------------------------------------------------------------------------------------|
| 構築（添字数指定）                                                                        | O(n)               | 3本の配列を確保する。                                                                     |
| 配列からの構築・`setAll`・コピー                                                          | O(n)               | ヒープ化は遅延する。                                                                      |
| `add`                                                                                     | O(1)               | 未整列領域へ置く。                                                                        |
| `set`・`relax`                                                                            | O(log s)           | 対象が未整列領域にあれば O(1)。                                                           |
| ヒープ性の復元                                                                            | O(min(s, k log s)) | 未整列要素がある場合に `peek`・`poll` や、ヒープ領域内の添字の `remove` の直前で1回行う。 |
| `peek`・`peekIndex`・`peekSecond`                                                         | O(1)               | 未整列要素があれば復元コストを加える。                                                    |
| `poll`・`pollIndex`                                                                       | O(log s)           | 未整列要素があれば復元コストを加える。                                                    |
| `remove`                                                                                  | O(log s)           | 未整列領域の添字は O(1)。ヒープ領域の添字で未整列要素があれば復元コストを加える。         |
| `get`・`getCost`・`containsIndex`・`hasCost`・`size`・`indexCount`・`clear`・iterator生成 | O(1)               | —                                                                                         |
| 走査・`contains`・配列化・文字列化                                                        | O(s)               | —                                                                                         |

ヒープ性の復元方式の選択は [PriorityQueue 利用ガイド](./PriorityQueueGuide.md#性能特性) と同じである。ヒープ領域内の `set` でコストが劣化した場合は、ヒープ領域の範囲内だけで `siftDown` する。

- **空間計算量**: O(n)。コスト・ヒープ・位置の3本の配列を保持する。

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                                                                                                                                                                                               |
|--------------------|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-10-07 | long costのIndexedPriorityQueueとして初回実装                                                                                                                                                                                                                                                                                                      |
| **バージョン 2.0** | 2026-07-18 | Int/Long版へ分割し、stampによる$\mathcal{O}(1)$ clear、`set`、active限定getter、`getLast`を追加                                                                                                                                                                                                                                                    |
| **バージョン 3.0** | 2026-07-18 | `setAll`を追加し、`set` / `relax`が未整列領域を展開しないよう改善                                                                                                                                                                                                                                                                                  |
| **バージョン 4.0** | 2026-07-20 | generic版の自然順序/Comparator構築、Primitive Comparator、`add` / `remove`のboolean化、default系、配列構築、`generate`、`indexCount`を追加し、bulk操作とiteratorのheapifyを遅延                                                                                                                                                                    |
| **バージョン 5.0** | 2026-10-28 | 全クラス: コピーコンストラクタを追加し、`setAll` が自身を返すよう、`set` が新規登録したかを boolean で返すよう変更。`remove` が未整列領域の添字をヒープを復元せず O(1) で外すよう改善。登録処理を `append`、配列からの構築を `build` へ集約し、`siftDown` の2引数版を廃止。LongIndexedPriorityQueue: private コンストラクタの位置を Int 版へ揃えた |
| **バージョン 6.0** | 2026-10-29 | 全クラス: 使われていない `addAll(int[], T[])` 系を削除。`getLast`・`getLastOrDefault` を、判定の `hasCost` と対になる `getCost`・`getCostOrDefault` へ改名                                                                                                                                                                                         |
| **バージョン 7.0** | 2026-10-29 | 全クラス: 添字数を公開フィールド `indexCount` として追加し、コレクションからのコンストラクタと内部配列の順に連結する `toString` を追加                                                                                                                                                                                                             |
