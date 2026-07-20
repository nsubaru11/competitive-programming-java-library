# PriorityQueue / IntPriorityQueue / LongPriorityQueue 利用ガイド

## 概要

[`PriorityQueue<T>`](../../../src/lib/ds/priorityqueue/PriorityQueue.java)、
[`IntPriorityQueue`](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java)、
[`LongPriorityQueue`](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java)は、追加を未整列領域へ蓄積し、優先順が必要になった時点でヒープを構築する可変容量優先度キューです。

## 特徴

- `add` / `addAll`のヒープ構築を遅延
- 未整列要素数に応じてFloydのheapifyと逐次`siftUp`を選択
- `peekOrDefault` / `pollOrDefault`による空キュー向けのプリミティブ対応API
- `peekSecond`と`replaceTop`を提供
- Comparable型はComparatorを渡さず自然順序または逆順で構築可能
- 比較不能な型と任意順序にはComparator指定コンストラクタを提供
- 型制約付き`naturalOrder` / `reverseOrder`ファクトリも提供
- int / long版はボクシングせず、昇順、降順、Primitive Comparatorに対応
- 明示した初期容量を尊重し、容量不足時は2倍へ拡張
- iteratorはヒープを構築せず内部配列順で走査

## 依存関係

- `java.util.Arrays`
- `java.util.Comparator`
- `java.util.Iterator`
- `java.util.PrimitiveIterator`
- `java.util.NoSuchElementException`
- [`IntCollection`](../../../src/lib/ds/IntCollection.java)
- [`LongCollection`](../../../src/lib/ds/LongCollection.java)
- [`IntComparator`](../../../src/lib/util/function/IntComparator.java)
- [`LongComparator`](../../../src/lib/util/function/LongComparator.java)

## 主な機能（メソッド一覧）

### 1. generic版コンストラクタ・ファクトリ

| メソッド                                                               | 説明                                       |
|------------------------------------------------------------------------|--------------------------------------------|
| `PriorityQueue()`                                                      | 容量1024、自然順序で構築                   |
| `PriorityQueue(int initialCapacity)`                                   | 指定した初期容量で自然順序に構築           |
| `PriorityQueue(boolean descending)`                                    | 自然順序または逆順で構築                   |
| `PriorityQueue(int initialCapacity, boolean descending)`               | 指定した初期容量で自然順序または逆順に構築 |
| `PriorityQueue(Comparator<? super T> comparator)`                      | 容量1024、Comparator順で構築               |
| `PriorityQueue(int initialCapacity, Comparator<? super T> comparator)` | 初期容量とComparatorを指定                 |
| `PriorityQueue(T[] values)`                                            | 配列から自然順序で構築                     |
| `PriorityQueue(T[] values, boolean descending)`                        | 配列から自然順序または逆順で構築           |
| `PriorityQueue(T[] values, Comparator<? super T> comparator)`          | 配列の全要素をComparator順で登録           |
| `PriorityQueue.naturalOrder()`                                         | Comparable要素を自然順序で扱うキューを生成 |
| `PriorityQueue.naturalOrder(int initialCapacity)`                      | 指定した初期容量で自然順序のキューを生成   |
| `PriorityQueue.naturalOrder(T[] values)`                               | 配列から自然順序のキューを生成             |
| `PriorityQueue.reverseOrder()`                                         | Comparable要素を逆順で扱うキューを生成     |
| `PriorityQueue.reverseOrder(int initialCapacity)`                      | 指定した初期容量で逆順のキューを生成       |
| `PriorityQueue.reverseOrder(T[] values)`                               | 配列から逆順のキューを生成                 |

### 2. IntPriorityQueueコンストラクタ

| メソッド                                                          | 説明                                   |
|-------------------------------------------------------------------|----------------------------------------|
| `IntPriorityQueue()`                                              | 容量1024、昇順で構築                   |
| `IntPriorityQueue(int initialCapacity)`                           | 指定した初期容量で昇順に構築           |
| `IntPriorityQueue(boolean descending)`                            | 昇順または降順で構築                   |
| `IntPriorityQueue(int initialCapacity, boolean descending)`       | 指定した初期容量で昇順または降順に構築 |
| `IntPriorityQueue(IntComparator comparator)`                      | Primitive Comparator順で構築           |
| `IntPriorityQueue(int initialCapacity, IntComparator comparator)` | 容量とComparatorを指定                 |
| `IntPriorityQueue(int[] values)`                                  | 配列から昇順で構築                     |
| `IntPriorityQueue(int[] values, boolean descending)`              | 配列から昇順または降順で構築           |
| `IntPriorityQueue(int[] values, IntComparator comparator)`        | 配列とComparatorを指定                 |

### 3. LongPriorityQueueコンストラクタ

| メソッド                                                            | 説明                                   |
|---------------------------------------------------------------------|----------------------------------------|
| `LongPriorityQueue()`                                               | 容量1024、昇順で構築                   |
| `LongPriorityQueue(int initialCapacity)`                            | 指定した初期容量で昇順に構築           |
| `LongPriorityQueue(boolean descending)`                             | 昇順または降順で構築                   |
| `LongPriorityQueue(int initialCapacity, boolean descending)`        | 指定した初期容量で昇順または降順に構築 |
| `LongPriorityQueue(LongComparator comparator)`                      | Primitive Comparator順で構築           |
| `LongPriorityQueue(int initialCapacity, LongComparator comparator)` | 容量とComparatorを指定                 |
| `LongPriorityQueue(long[] values)`                                  | 配列から昇順で構築                     |
| `LongPriorityQueue(long[] values, boolean descending)`              | 配列から昇順または降順で構築           |
| `LongPriorityQueue(long[] values, LongComparator comparator)`       | 配列とComparatorを指定                 |

### 4. 追加

| メソッド                                             | 戻り値の型 | 説明                            |
|------------------------------------------------------|-----------:|---------------------------------|
| `PriorityQueue.add(T v)`                             |  `boolean` | 要素を追加。常にtrue            |
| `PriorityQueue.addAll(T[] values)`                   |  `boolean` | 配列を追加。1件以上ならtrue     |
| `PriorityQueue.addAll(Iterable<? extends T> values)` |  `boolean` | Iterableを追加。1件以上ならtrue |
| `IntPriorityQueue.add(int v)`                        |  `boolean` | int値を追加。常にtrue           |
| `IntPriorityQueue.addAll(int[] values)`              |  `boolean` | int配列を追加                   |
| `IntPriorityQueue.addAll(Iterable<Integer> values)`  |  `boolean` | Iterableのint値を追加           |
| `LongPriorityQueue.add(long v)`                      |  `boolean` | long値を追加。常にtrue          |
| `LongPriorityQueue.addAll(long[] values)`            |  `boolean` | long配列を追加                  |
| `LongPriorityQueue.addAll(Iterable<Long> values)`    |  `boolean` | Iterableのlong値を追加          |

### 5. 優先要素

| メソッド                                             | 戻り値の型 | 説明                          |
|------------------------------------------------------|-----------:|-------------------------------|
| `PriorityQueue.peek()`                               |        `T` | 最優先要素を返す              |
| `PriorityQueue.peekOrDefault(T defaultValue)`        |        `T` | 空ならdefaultValueを返す      |
| `PriorityQueue.peekSecond()`                         |        `T` | 2番目に優先される要素を返す   |
| `PriorityQueue.poll()`                               |        `T` | 最優先要素を削除して返す      |
| `PriorityQueue.pollOrDefault(T defaultValue)`        |        `T` | 空ならdefaultValueを返す      |
| `PriorityQueue.replaceTop(T v)`                      |        `T` | 先頭を置換して旧要素を返す    |
| `IntPriorityQueue.peek()`                            |      `int` | 最優先int値を返す             |
| `IntPriorityQueue.peekOrDefault(int defaultValue)`   |      `int` | 空ならdefaultValueを返す      |
| `IntPriorityQueue.peekSecond()`                      |      `int` | 2番目に優先されるint値を返す  |
| `IntPriorityQueue.poll()`                            |      `int` | 最優先int値を削除して返す     |
| `IntPriorityQueue.pollOrDefault(int defaultValue)`   |      `int` | 空ならdefaultValueを返す      |
| `IntPriorityQueue.replaceTop(int v)`                 |      `int` | 先頭を置換して旧値を返す      |
| `LongPriorityQueue.peek()`                           |     `long` | 最優先long値を返す            |
| `LongPriorityQueue.peekOrDefault(long defaultValue)` |     `long` | 空ならdefaultValueを返す      |
| `LongPriorityQueue.peekSecond()`                     |     `long` | 2番目に優先されるlong値を返す |
| `LongPriorityQueue.poll()`                           |     `long` | 最優先long値を削除して返す    |
| `LongPriorityQueue.pollOrDefault(long defaultValue)` |     `long` | 空ならdefaultValueを返す      |
| `LongPriorityQueue.replaceTop(long v)`               |     `long` | 先頭を置換して旧値を返す      |

### 6. 状態・反復

| メソッド                       |                 戻り値の型 | 説明                                  |
|--------------------------------|---------------------------:|---------------------------------------|
| `size()`                       |                      `int` | 現在の要素数                          |
| `capacity()`                   |                      `int` | 現在の内部配列容量                    |
| `isEmpty()`                    |                  `boolean` | 空か判定。primitive版はCollection由来 |
| `clear()`                      |                     `void` | O(1)で論理的に空にする                |
| `PriorityQueue.iterator()`     |              `Iterator<T>` | 内部順で走査                          |
| `IntPriorityQueue.iterator()`  |  `PrimitiveIterator.OfInt` | int値を内部順で走査                   |
| `LongPriorityQueue.iterator()` | `PrimitiveIterator.OfLong` | long値を内部順で走査                  |

### 7. primitive Collection由来

| メソッド                                     |                     戻り値の型 | 説明                   |
|----------------------------------------------|-------------------------------:|------------------------|
| `contains(value)`                            |                      `boolean` | 値を線形探索           |
| `forEachInt(action)` / `forEachLong(action)` |                         `void` | 全要素へ処理を適用     |
| `spliterator()`                              | `Spliterator.OfInt` / `OfLong` | primitive spliterator  |
| `intStream()` / `longStream()`               |     `IntStream` / `LongStream` | primitive Stream       |
| `toList()`                                   | `List<Integer>` / `List<Long>` | ボックス化した不変List |
| `toArray()`                                  |             `int[]` / `long[]` | primitive配列へコピー  |

## 利用例

```java
IntPriorityQueue q = new IntPriorityQueue(new int[]{5, 2, 8});
q.add(1);

System.out.println(q.peek());       // 1
System.out.println(q.peekSecond()); // 2
while (!q.isEmpty()) System.out.println(q.poll());
```

```java
record Job(int priority, String name) {}

PriorityQueue<Job> jobs =
	new PriorityQueue<>(Comparator.comparingInt(Job::priority));
jobs.add(new Job(2, "build"));
jobs.add(new Job(1, "test"));

PriorityQueue<Integer> natural = new PriorityQueue<>();
```

## 注意事項

- Comparatorなしのgenericコンストラクタは全要素が相互にComparableであることを前提とします。
- 比較不能な型をComparatorなしで構築すること自体はコンパイルできますが、実際に比較した時点で`ClassCastException`になります。
- コンパイル時にComparable制約を確認したい場合は`naturalOrder` / `reverseOrder`ファクトリを使用します。
- Comparableでない型はComparatorを渡します。
- Comparatorで`compare(a, b) < 0`となる`a`が先に取り出されます。
- `peek`、`poll`、`replaceTop`は空の場合に`NoSuchElementException`を投げます。
- `peekSecond`は要素数2以上で呼び出します。
- iteratorと変換結果は優先順ではありません。
- 配列コンストラクタと`addAll`はヒープ構築を遅延します。
- generic版へ`null`を追加しません。
- `java.util.PriorityQueue`との単純名衝突に注意します。

## パフォーマンス特性

- `add`: 償却O (1)、拡張時O (n)
- `addAll`: O (k)、拡張時は既存要素コピーも実行
- `peek`, `peekSecond`: cleanならO (1)、dirtyならO (n)またはO (k log n)の構築を伴う
- `poll`, `replaceTop`: cleanならO (log n)、dirtyなら構築コストを伴う
- `size`, `capacity`, `isEmpty`, `clear`, iterator生成: O (1)
- iteratorとprimitive Collectionの走査・変換: O (n)
- 使用メモリ: O(capacity)

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                                                     |
|:-------------------|:-----------|:-------------------------------------------------------------------------------------------------------------------------|
| **バージョン 4.0** | 2026-07-20 | `add`へ統一し、Primitive Comparator、generic自然順序/Comparator構築、配列構築、default系、`peekSecond`、`capacity`を追加 |
| **バージョン 3.0** | 2026-07-18 | `lib.ds.priorityqueue`へ移動し、primitive版をCollection化、`addAll(Iterable)`を追加                                      |
| **バージョン 2.0** | 2025-10-08 | 遅延ヒープ構築、降順、`replaceTop`を追加                                                                                 |
| **バージョン 1.0** | 2025-10-04 | generic版、int版、long版を初回実装                                                                                       |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
