# PriorityQueue 利用ガイド

> 汎用・int・long の可変容量優先度キュー `PriorityQueue<T>`・`IntPriorityQueue`・`LongPriorityQueue` の仕様をまとめる。

[← README: Priority Queue](./README.md) · ソース: [PriorityQueue.java](../../../src/lib/ds/priorityqueue/PriorityQueue.java) · [IntPriorityQueue.java](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java) · [LongPriorityQueue.java](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

二分ヒープによる優先度キューである。比較順で先頭となる要素を最優先とし、`peek` で参照、`poll` で取り出す。
追加した要素は内部配列の末尾の未整列領域へ置き、優先順が必要な操作の直前にヒープ性を復元する。
要素数を n、未整列領域の要素数を k、一度に追加する要素数を m とする。

## 特徴

- `add`・`addAll`・配列やコレクションからの構築はヒープ化を遅延する。
- ヒープ性の復元では、k に応じて Floyd の heapify と逐次 `siftUp` のうち推定コストが小さい方を選ぶ。
- `peekSecond` は2番目に優先される要素を、`replaceTop` は先頭の置換を1回の `siftDown` で行う。
- 空のキューで例外を避ける `peekOrDefault`・`pollOrDefault` を提供する。
- 比較順は昇順（汎用版は自然順序）・降順（汎用版は逆順）・Comparator から選ぶ。汎用版の `naturalOrder`・`reverseOrder` ファクトリは要素型が Comparable であることを型で保証する。
- 初期容量・配列・コレクション・コピーから構築できる。コピーは比較順・要素・容量を引き継ぐ。
- 初期容量は1未満なら1とし、容量不足時は2倍へ拡張する。`clear` は内部配列を再利用する。
- int 版・long 版はボクシングせず、`IntCollection`・`LongCollection` を実装する。

### 型別クラス

| クラス                                                                          | 対象型 | 選ぶ目安                                         |
|---------------------------------------------------------------------------------|--------|--------------------------------------------------|
| [`PriorityQueue<T>`](../../../src/lib/ds/priorityqueue/PriorityQueue.java)      | `T`    | Comparable なオブジェクト、Comparator で並べる型 |
| [`IntPriorityQueue`](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java)   | `int`  | int 値をボクシングせずに扱う場合                 |
| [`LongPriorityQueue`](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java) | `long` | long 値をボクシングせずに扱う場合                |

## 依存関係

- Java標準ライブラリ: `java.util.*`
- int 版: [IntCollection](../../../src/lib/ds/IntCollection.java)、[IntComparator](../../../src/lib/util/function/IntComparator.java)
- long 版: [LongCollection](../../../src/lib/ds/LongCollection.java)、[LongComparator](../../../src/lib/util/function/LongComparator.java)

## API

### `PriorityQueue<T>`

[ソース](../../../src/lib/ds/priorityqueue/PriorityQueue.java)

`Iterable<T>` を実装する。

| 種別        | シグネチャ                                                                        | 戻り値             | 説明                                                                  |
|-------------|-----------------------------------------------------------------------------------|--------------------|-----------------------------------------------------------------------|
| Constructor | `PriorityQueue()`                                                                 | —                  | 初期容量1024、自然順序で構築する。                                    |
| Constructor | `PriorityQueue(int initialCapacity)`                                              | —                  | 指定した初期容量、自然順序で構築する。                                |
| Constructor | `PriorityQueue(boolean isDescendingOrder)`                                        | —                  | 初期容量1024、自然順序または逆順で構築する。                          |
| Constructor | `PriorityQueue(int initialCapacity, boolean isDescendingOrder)`                   | —                  | 指定した初期容量、自然順序または逆順で構築する。                      |
| Constructor | `PriorityQueue(Comparator<? super T> comparator)`                                 | —                  | 初期容量1024、Comparator の順で構築する。                             |
| Constructor | `PriorityQueue(int initialCapacity, Comparator<? super T> comparator)`            | —                  | 指定した初期容量、Comparator の順で構築する。                         |
| Constructor | `PriorityQueue(T[] values)`                                                       | —                  | 配列の全要素を自然順序で持つ。                                        |
| Constructor | `PriorityQueue(T[] values, boolean isDescendingOrder)`                            | —                  | 配列の全要素を自然順序または逆順で持つ。                              |
| Constructor | `PriorityQueue(T[] values, Comparator<? super T> comparator)`                     | —                  | 配列の全要素を Comparator の順で持つ。                                |
| Constructor | `PriorityQueue(Collection<? extends T> values)`                                   | —                  | コレクションの全要素を自然順序で持つ。values の比較順は引き継がない。 |
| Constructor | `PriorityQueue(Collection<? extends T> values, boolean isDescendingOrder)`        | —                  | コレクションの全要素を自然順序または逆順で持つ。                      |
| Constructor | `PriorityQueue(Collection<? extends T> values, Comparator<? super T> comparator)` | —                  | コレクションの全要素を Comparator の順で持つ。                        |
| Constructor | `PriorityQueue(PriorityQueue<T> other)`                                           | —                  | 比較順・要素・容量を引き継ぐ独立したコピー。要素自体は共有する。      |
| Static      | `naturalOrder()`                                                                  | `PriorityQueue<T>` | Comparable 要素を自然順序で扱う空のキューを返す。                     |
| Static      | `naturalOrder(int initialCapacity)`                                               | `PriorityQueue<T>` | 指定した初期容量で自然順序の空のキューを返す。                        |
| Static      | `naturalOrder(T[] values)`                                                        | `PriorityQueue<T>` | 配列の全要素を自然順序で持つキューを返す。                            |
| Static      | `reverseOrder()`                                                                  | `PriorityQueue<T>` | Comparable 要素を逆順で扱う空のキューを返す。                         |
| Static      | `reverseOrder(int initialCapacity)`                                               | `PriorityQueue<T>` | 指定した初期容量で逆順の空のキューを返す。                            |
| Static      | `reverseOrder(T[] values)`                                                        | `PriorityQueue<T>` | 配列の全要素を逆順で持つキューを返す。                                |
| Method      | `size()`                                                                          | `int`              | 要素数を返す。                                                        |
| Method      | `capacity()`                                                                      | `int`              | 内部配列の長さを返す。                                                |
| Method      | `isEmpty()`                                                                       | `boolean`          | 要素数が0かを返す。                                                   |
| Method      | `peek()`                                                                          | `T`                | 最優先の要素を返す。                                                  |
| Method      | `peekOrDefault(T defaultValue)`                                                   | `T`                | 空なら defaultValue、そうでなければ最優先の要素を返す。               |
| Method      | `peekSecond()`                                                                    | `T`                | 2番目に優先される要素を返す。要素数は2以上とする。                    |
| Method      | `add(T v)`                                                                        | `boolean`          | 要素を追加する。常に true。                                           |
| Method      | `addAll(T[] values)`                                                              | `boolean`          | 配列の全要素を追加する。1件以上追加した場合に true。                  |
| Method      | `addAll(Iterable<? extends T> values)`                                            | `boolean`          | 全要素を追加する。1件以上追加した場合に true。                        |
| Method      | `poll()`                                                                          | `T`                | 最優先の要素を削除して返す。                                          |
| Method      | `pollOrDefault(T defaultValue)`                                                   | `T`                | 空なら defaultValue、そうでなければ最優先の要素を削除して返す。       |
| Method      | `replaceTop(T v)`                                                                 | `T`                | 最優先の要素を v に置き換え、置き換え前の要素を返す。                 |
| Method      | `clear()`                                                                         | `void`             | 内部配列を再利用して空にする。                                        |
| Method      | `iterator()`                                                                      | `Iterator<T>`      | 内部配列の順に走査する。                                              |

`Iterable<T>` の `forEach(Consumer<? super T> action)` と `spliterator()` も継承する。

### `IntPriorityQueue`

[ソース](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java)

`IntCollection` を実装する。

| 種別        | シグネチャ                                                          | 戻り値                    | 説明                                                                        |
|-------------|---------------------------------------------------------------------|---------------------------|-----------------------------------------------------------------------------|
| Constructor | `IntPriorityQueue()`                                                | —                         | 初期容量1024、昇順で構築する。                                              |
| Constructor | `IntPriorityQueue(int initialCapacity)`                             | —                         | 指定した初期容量、昇順で構築する。                                          |
| Constructor | `IntPriorityQueue(boolean isDescendingOrder)`                       | —                         | 初期容量1024、昇順または降順で構築する。                                    |
| Constructor | `IntPriorityQueue(int initialCapacity, boolean isDescendingOrder)`  | —                         | 指定した初期容量、昇順または降順で構築する。                                |
| Constructor | `IntPriorityQueue(IntComparator comparator)`                        | —                         | 初期容量1024、Comparator の順で構築する。                                   |
| Constructor | `IntPriorityQueue(int initialCapacity, IntComparator comparator)`   | —                         | 指定した初期容量、Comparator の順で構築する。                               |
| Constructor | `IntPriorityQueue(int[] values)`                                    | —                         | 配列の全要素を昇順で持つ。                                                  |
| Constructor | `IntPriorityQueue(int[] values, boolean isDescendingOrder)`         | —                         | 配列の全要素を昇順または降順で持つ。                                        |
| Constructor | `IntPriorityQueue(int[] values, IntComparator comparator)`          | —                         | 配列の全要素を Comparator の順で持つ。                                      |
| Constructor | `IntPriorityQueue(IntCollection values)`                            | —                         | コレクションの全要素を昇順で持つ。                                          |
| Constructor | `IntPriorityQueue(IntCollection values, boolean isDescendingOrder)` | —                         | コレクションの全要素を昇順または降順で持つ。                                |
| Constructor | `IntPriorityQueue(IntCollection values, IntComparator comparator)`  | —                         | コレクションの全要素を Comparator の順で持つ。                              |
| Constructor | `IntPriorityQueue(IntPriorityQueue other)`                          | —                         | 比較順・要素・容量を引き継ぐ独立したコピー。                                |
| Method      | `size()`                                                            | `int`                     | 要素数を返す。                                                              |
| Method      | `capacity()`                                                        | `int`                     | 内部配列の長さを返す。                                                      |
| Method      | `peek()`                                                            | `int`                     | 最優先の値を返す。                                                          |
| Method      | `peekOrDefault(int defaultValue)`                                   | `int`                     | 空なら defaultValue、そうでなければ最優先の値を返す。                       |
| Method      | `peekSecond()`                                                      | `int`                     | 2番目に優先される値を返す。要素数は2以上とする。                            |
| Method      | `add(int v)`                                                        | `boolean`                 | 値を追加する。常に true。                                                   |
| Method      | `addAll(int[] values)`                                              | `boolean`                 | 配列の全要素を追加する。1件以上追加した場合に true。                        |
| Method      | `addAll(IntCollection values)`                                      | `boolean`                 | 全要素をボクシングせずに追加する。1件以上追加した場合に true。              |
| Method      | `addAll(Iterable<Integer> values)`                                  | `boolean`                 | 全要素を追加する。`IntCollection` なら `addAll(IntCollection)` へ委譲する。 |
| Method      | `poll()`                                                            | `int`                     | 最優先の値を削除して返す。                                                  |
| Method      | `pollOrDefault(int defaultValue)`                                   | `int`                     | 空なら defaultValue、そうでなければ最優先の値を削除して返す。               |
| Method      | `replaceTop(int v)`                                                 | `int`                     | 最優先の値を v に置き換え、置き換え前の値を返す。                           |
| Method      | `clear()`                                                           | `void`                    | 内部配列を再利用して空にする。                                              |
| Method      | `iterator()`                                                        | `PrimitiveIterator.OfInt` | 内部配列の順に走査する。                                                    |

### `LongPriorityQueue`

[ソース](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java)

`LongCollection` を実装する。`IntPriorityQueue` の `int` 値を `long` 値に、`IntCollection`・`IntComparator`・`Iterable<Integer>` を `LongCollection`・`LongComparator`・`Iterable<Long>` に置き換えた API を持つ。

| 種別        | シグネチャ                                                            | 戻り値                     | 説明                                                                          |
|-------------|-----------------------------------------------------------------------|----------------------------|-------------------------------------------------------------------------------|
| Constructor | `LongPriorityQueue()`                                                 | —                          | 初期容量1024、昇順で構築する。                                                |
| Constructor | `LongPriorityQueue(int initialCapacity)`                              | —                          | 指定した初期容量、昇順で構築する。                                            |
| Constructor | `LongPriorityQueue(boolean isDescendingOrder)`                        | —                          | 初期容量1024、昇順または降順で構築する。                                      |
| Constructor | `LongPriorityQueue(int initialCapacity, boolean isDescendingOrder)`   | —                          | 指定した初期容量、昇順または降順で構築する。                                  |
| Constructor | `LongPriorityQueue(LongComparator comparator)`                        | —                          | 初期容量1024、Comparator の順で構築する。                                     |
| Constructor | `LongPriorityQueue(int initialCapacity, LongComparator comparator)`   | —                          | 指定した初期容量、Comparator の順で構築する。                                 |
| Constructor | `LongPriorityQueue(long[] values)`                                    | —                          | 配列の全要素を昇順で持つ。                                                    |
| Constructor | `LongPriorityQueue(long[] values, boolean isDescendingOrder)`         | —                          | 配列の全要素を昇順または降順で持つ。                                          |
| Constructor | `LongPriorityQueue(long[] values, LongComparator comparator)`         | —                          | 配列の全要素を Comparator の順で持つ。                                        |
| Constructor | `LongPriorityQueue(LongCollection values)`                            | —                          | コレクションの全要素を昇順で持つ。                                            |
| Constructor | `LongPriorityQueue(LongCollection values, boolean isDescendingOrder)` | —                          | コレクションの全要素を昇順または降順で持つ。                                  |
| Constructor | `LongPriorityQueue(LongCollection values, LongComparator comparator)` | —                          | コレクションの全要素を Comparator の順で持つ。                                |
| Constructor | `LongPriorityQueue(LongPriorityQueue other)`                          | —                          | 比較順・要素・容量を引き継ぐ独立したコピー。                                  |
| Method      | `size()`                                                              | `int`                      | 要素数を返す。                                                                |
| Method      | `capacity()`                                                          | `int`                      | 内部配列の長さを返す。                                                        |
| Method      | `peek()`                                                              | `long`                     | 最優先の値を返す。                                                            |
| Method      | `peekOrDefault(long defaultValue)`                                    | `long`                     | 空なら defaultValue、そうでなければ最優先の値を返す。                         |
| Method      | `peekSecond()`                                                        | `long`                     | 2番目に優先される値を返す。要素数は2以上とする。                              |
| Method      | `add(long v)`                                                         | `boolean`                  | 値を追加する。常に true。                                                     |
| Method      | `addAll(long[] values)`                                               | `boolean`                  | 配列の全要素を追加する。1件以上追加した場合に true。                          |
| Method      | `addAll(LongCollection values)`                                       | `boolean`                  | 全要素をボクシングせずに追加する。1件以上追加した場合に true。                |
| Method      | `addAll(Iterable<Long> values)`                                       | `boolean`                  | 全要素を追加する。`LongCollection` なら `addAll(LongCollection)` へ委譲する。 |
| Method      | `poll()`                                                              | `long`                     | 最優先の値を削除して返す。                                                    |
| Method      | `pollOrDefault(long defaultValue)`                                    | `long`                     | 空なら defaultValue、そうでなければ最優先の値を削除して返す。                 |
| Method      | `replaceTop(long v)`                                                  | `long`                     | 最優先の値を v に置き換え、置き換え前の値を返す。                             |
| Method      | `clear()`                                                             | `void`                     | 内部配列を再利用して空にする。                                                |
| Method      | `iterator()`                                                          | `PrimitiveIterator.OfLong` | 内部配列の順に走査する。                                                      |

### 数値版の継承 API

`IntPriorityQueue`・`LongPriorityQueue` は `IntCollection`・`LongCollection` の `isEmpty`、`contains`、`forEachInt` / `forEachLong`、`spliterator`、`intStream` / `longStream`、`toList`、`toArray`、`forEach` を既定実装のまま継承する。全シグネチャは [配列・コレクションの継承API](../arrays/ArrayGuide.md#継承元のコレクション-api) を参照する。走査順は内部配列の順であり、優先順ではない。

## 利用例

`IntPriorityQueue` の追加と取り出し: [Example.java](../../../test/example/ds/priorityqueue/Example.java)

## 制約・注意事項

- `compare(a, b) < 0` となる a が b より先に取り出される。同順位の要素の取り出し順は規定しない。
- 空のキューの `peek`・`poll`・`replaceTop` と、要素数2未満の `peekSecond` は `NoSuchElementException` を投げる。`peekOrDefault`・`pollOrDefault` は空のとき状態を変えずに defaultValue を返す。
- Comparator を省略した汎用版は全要素が相互に Comparable であることを前提とする。そうでない型もコンパイルできるが、最初に比較した時点で `ClassCastException` を投げる。コンパイル時に確認する場合は `naturalOrder`・`reverseOrder` ファクトリを使う。
- 汎用版に null を追加しない。
- Comparator を受け取るコンストラクタは null を `NullPointerException` で拒否する。
- コピーコンストラクタは比較順を引き継ぐ。数値版で比較順を変えて複製する場合は `IntPriorityQueue(IntCollection values, boolean isDescendingOrder)` などを使う。コピーコンストラクタとコレクションからのコンストラクタは、静的な引数型が `IntPriorityQueue` か `IntCollection` かで選ばれる。
- 汎用版のコレクションからのコンストラクタは、`java.util.PriorityQueue` と異なり values の比較順を引き継がない。
- 走査・`contains`・配列化・Stream はヒープを復元せず内部配列の順に行い、優先順ではない。走査中にキューを変更しない。
- `java.util.PriorityQueue` と単純名が衝突するため、`java.util.*` と併用する場合は `import lib.ds.priorityqueue.PriorityQueue;` を明示する。

## 性能特性

| 操作                                                 | 時間計算量         | 補足                                                        |
|------------------------------------------------------|--------------------|-------------------------------------------------------------|
| 構築（容量指定）                                     | O(容量)            | 内部配列を確保する。                                        |
| 配列・コレクションからの構築、コピー                 | O(n)               | ヒープ化は遅延する。                                        |
| `add`                                                | 償却 O(1)          | 拡張時は O(n) のコピーを伴う。                              |
| `addAll`                                             | O(m)               | 拡張時は O(n) のコピーを伴う。                              |
| ヒープ性の復元                                       | O(min(n, k log n)) | 未整列要素がある場合に `peek`・`poll` などの直前で1回行う。 |
| `peek`・`peekSecond`                                 | O(1)               | 未整列要素があれば復元コストを加える。                      |
| `poll`・`replaceTop`                                 | O(log n)           | 未整列要素があれば復元コストを加える。                      |
| `size`・`capacity`・`isEmpty`・`clear`・iterator生成 | O(1)               | —                                                           |
| 走査・`contains`・配列化                             | O(n)               | —                                                           |

ヒープ性の復元は、Floyd の heapify の推定比較回数 `2n - 2 log n` と逐次 `siftUp` の推定比較回数を比べて小さい方を選ぶ。逐次 `siftUp` の推定は k が100以下なら各挿入位置の深さの和、それより大きければ平均の深さと k の積で求める。
数値版は Comparator を指定しない場合、昇順・降順を `Integer.compare`・`Long.compare` で直接比較する。

- **空間計算量**: O(容量)。容量は max(1, 初期容量) 以上の値から2倍ずつ拡張する。

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                                                                                                                                                                                                                                                     |
|--------------------|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-10-04 | generic版、int版、long版を初回実装                                                                                                                                                                                                                                                                                                                                                                       |
| **バージョン 2.0** | 2025-10-08 | 遅延ヒープ構築、降順、`replaceTop`を追加                                                                                                                                                                                                                                                                                                                                                                 |
| **バージョン 3.0** | 2026-07-18 | `lib.ds.priorityqueue`へ移動し、primitive版をCollection化、`addAll(Iterable)`を追加                                                                                                                                                                                                                                                                                                                      |
| **バージョン 4.0** | 2026-07-20 | `add`へ統一し、Primitive Comparator、generic自然順序/Comparator構築、配列構築、default系、`peekSecond`、`capacity`を追加                                                                                                                                                                                                                                                                                 |
| **バージョン 5.0** | 2026-10-28 | 全クラス: コレクション（`Collection` / `IntCollection` / `LongCollection`）とコピーからのコンストラクタを追加し、容量フィールドを廃止して `capacity` が内部配列の長さを返す形へ変更。数値版: `addAll(IntCollection)` / `addAll(LongCollection)` を追加し、`addAll(Iterable)` が数値コレクションを受け取った場合は委譲するよう変更（自身を渡した場合の無限ループを解消）。`replaceTop` の冗長な代入を除去 |
