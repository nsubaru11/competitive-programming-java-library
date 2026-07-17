# PriorityQueue / IntPriorityQueue / LongPriorityQueue 利用ガイド

## 概要

[`PriorityQueue<T>`](../../../src/lib/ds/priorityqueue/PriorityQueue.java)、
[`IntPriorityQueue`](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java)、
[`LongPriorityQueue`](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java) は、可変長配列を内部に持つ競技プログラミング向け優先度キューです。追加を未整列領域へ蓄積し、優先順が必要になった時点でヒープを構築します。

## 特徴

- 最小値優先と最大値優先に対応
- `push` / `addAll` は要素を末尾へ追加し、ヒープ構築を遅延
- 未整列要素数に応じてFloydのheapifyと逐次`siftUp`を選択
- 容量不足時は内部配列を2倍へ拡張
- ジェネリック版は`Comparator`による順序指定に対応
- int / long版はボクシングせず、`IntCollection` / `LongCollection`として走査可能
- iteratorは優先順ではなく内部配列順

## 依存関係

- `java.util.Arrays`
- `java.util.Comparator`
- `java.util.Iterator`
- `java.util.PrimitiveIterator`
- `java.util.NoSuchElementException`
- [`lib.ds.IntCollection`](../../../src/lib/ds/IntCollection.java)
- [`lib.ds.LongCollection`](../../../src/lib/ds/LongCollection.java)

## 主な機能（メソッド一覧）

### 1. ジェネリック版コンストラクタ

| メソッド                                                                        | 説明                    |
|-----------------------------------------------------------------------------|-----------------------|
| `PriorityQueue()`                                                           | 容量1024、自然順、最小値優先で構築   |
| `PriorityQueue(int capacity)`                                               | 初期容量を指定し、自然順、最小値優先で構築 |
| `PriorityQueue(Comparator<T> comparator)`                                   | 比較器を指定し、最小値優先で構築      |
| `PriorityQueue(boolean descending)`                                         | 自然順で優先方向を指定して構築       |
| `PriorityQueue(int capacity, Comparator<T> comparator)`                     | 初期容量と比較器を指定して構築       |
| `PriorityQueue(Comparator<T> comparator, boolean descending)`               | 比較器と優先方向を指定して構築       |
| `PriorityQueue(int capacity, boolean descending)`                           | 初期容量と優先方向を指定して構築      |
| `PriorityQueue(int capacity, Comparator<T> comparator, boolean descending)` | 全設定を指定して構築            |

### 2. プリミティブ版コンストラクタ

| メソッド                                                  | 説明                |
|-------------------------------------------------------|-------------------|
| `IntPriorityQueue()`                                  | 容量1024、最小値優先で構築   |
| `IntPriorityQueue(int capacity)`                      | 初期容量を指定し、最小値優先で構築 |
| `IntPriorityQueue(boolean descending)`                | 優先方向を指定して構築       |
| `IntPriorityQueue(int capacity, boolean descending)`  | 初期容量と優先方向を指定して構築  |
| `LongPriorityQueue()`                                 | 容量1024、最小値優先で構築   |
| `LongPriorityQueue(int capacity)`                     | 初期容量を指定し、最小値優先で構築 |
| `LongPriorityQueue(boolean descending)`               | 優先方向を指定して構築       |
| `LongPriorityQueue(int capacity, boolean descending)` | 初期容量と優先方向を指定して構築  |

### 3. 追加・一括追加

| メソッド                                                  | 戻り値の型  | 説明                       |
|-------------------------------------------------------|--------|--------------------------|
| `PriorityQueue.push(T v)`                             | `void` | 要素を未整列領域へ追加              |
| `PriorityQueue.addAll(T[] elements)`                  | `void` | 配列の全要素を未整列領域へ追加          |
| `PriorityQueue.addAll(Iterable<T> elements)`          | `void` | Iterableの全要素を未整列領域へ追加    |
| `IntPriorityQueue.push(int v)`                        | `void` | int値を未整列領域へ追加            |
| `IntPriorityQueue.addAll(int[] elements)`             | `void` | int配列の全要素を未整列領域へ追加       |
| `IntPriorityQueue.addAll(Iterable<Integer> elements)` | `void` | Iterableの全int値を未整列領域へ追加  |
| `LongPriorityQueue.push(long v)`                      | `void` | long値を未整列領域へ追加           |
| `LongPriorityQueue.addAll(long[] elements)`           | `void` | long配列の全要素を未整列領域へ追加      |
| `LongPriorityQueue.addAll(Iterable<Long> elements)`   | `void` | Iterableの全long値を未整列領域へ追加 |

### 4. 優先要素の操作

| メソッド                                   | 戻り値の型  | 説明                   |
|----------------------------------------|--------|----------------------|
| `PriorityQueue.peek()`                 | `T`    | 最優先要素を削除せず返す         |
| `IntPriorityQueue.peek()`              | `int`  | 最優先int値を削除せず返す       |
| `LongPriorityQueue.peek()`             | `long` | 最優先long値を削除せず返す      |
| `PriorityQueue.poll()`                 | `T`    | 最優先要素を返して削除          |
| `IntPriorityQueue.poll()`              | `int`  | 最優先int値を返して削除        |
| `LongPriorityQueue.poll()`             | `long` | 最優先long値を返して削除       |
| `PriorityQueue.replaceTop(T v)`        | `T`    | 最優先要素を`v`へ置換し、旧要素を返す |
| `IntPriorityQueue.replaceTop(int v)`   | `int`  | 最優先int値を置換し、旧値を返す    |
| `LongPriorityQueue.replaceTop(long v)` | `long` | 最優先long値を置換し、旧値を返す   |

### 5. 状態・反復

| メソッド                           | 戻り値の型                      | 説明              |
|--------------------------------|----------------------------|-----------------|
| `size()`                       | `int`                      | 現在の要素数を返す       |
| `isEmpty()`                    | `boolean`                  | 要素がないか判定        |
| `clear()`                      | `void`                     | 要素数と未整列要素数を0にする |
| `PriorityQueue.iterator()`     | `Iterator<T>`              | 内部配列順に走査        |
| `IntPriorityQueue.iterator()`  | `PrimitiveIterator.OfInt`  | 内部配列順にint値を走査   |
| `LongPriorityQueue.iterator()` | `PrimitiveIterator.OfLong` | 内部配列順にlong値を走査  |

### 6. プリミティブCollection由来のメソッド

| メソッド                                         | 戻り値の型                          | 説明                   |
|----------------------------------------------|--------------------------------|----------------------|
| `contains(value)`                            | `boolean`                      | 値が含まれるか線形探索          |
| `forEachInt(action)` / `forEachLong(action)` | `void`                         | 全要素へ処理を適用            |
| `spliterator()`                              | `Spliterator.OfInt` / `OfLong` | プリミティブspliteratorを返す |
| `intStream()` / `longStream()`               | `IntStream` / `LongStream`     | プリミティブStreamを返す      |
| `toList()`                                   | `List<Integer>` / `List<Long>` | ボックス化した不変Listを返す     |
| `toArray()`                                  | `int[]` / `long[]`             | 全要素をプリミティブ配列へコピー     |

## 遅延ヒープ構築

内部配列の先頭`size - unsortedCount`要素はヒープで、末尾`unsortedCount`要素は追加順の未整列領域です。`peek`、`poll`、`replaceTop`の直前に、全体のheapifyと未整列要素の逐次`siftUp`の推定比較回数を比べ、少ない方を実行します。

iteratorとCollection由来の線形操作は優先順を必要としないため、通常版ではヒープ構築を行いません。

## 利用例

```java
IntPriorityQueue q = new IntPriorityQueue();
q.addAll(new int[]{5, 2, 8});
q.push(1);

System.out.println(q.peek()); // 1
while (!q.isEmpty()) {
	System.out.println(q.poll());
}
```

```java
PriorityQueue<String> q =
	new PriorityQueue<>(Comparator.comparingInt(String::length));
q.addAll(new String[]{"apple", "pie", "banana"});

System.out.println(q.poll()); // pie
```

## 注意事項

- `peek`、`poll`、`replaceTop`は空でない状態で呼び出します。空の場合は`NoSuchElementException`を投げます。
- iterator、`toArray`、`toList`の順序は優先順ではありません。優先順が必要なら`poll`を繰り返します。
- `clear()`は確保済み内部配列を縮小しません。
- 現在の実装では、指定した初期容量が1024未満でも内部容量は1024になります。
- `PriorityQueue<T>`の`T`は、比較器を指定する場合も`Comparable<T>`を実装する必要があります。
- ジェネリック版は`java.util.PriorityQueue`と単純名が同じため、同時利用時はimportの衝突に注意します。
- ジェネリック版へ`null`を追加しません。

## パフォーマンス特性

- `push`: 償却O(1)、拡張時O(n)
- `addAll`: O(k)、拡張時は既存要素のコピーにO(n)
- `peek`: ヒープ構築済みならO(1)。未整列要素があればO(n)またはO(k log n)の構築を伴う
- `poll`, `replaceTop`: ヒープ構築済みならO(log n)。未整列要素があれば構築コストを伴う
- `size`, `isEmpty`, `clear`: O(1)
- iteratorとプリミティブCollection由来の走査・変換: O(n)
- 使用メモリ: O(capacity)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                   |
|:--------------|:-----------|:---------------------------------------------------------------------|
| **バージョン 1.0** | 2025-10-04 | ジェネリック版、int版、long版を初回実装                                              |
| **バージョン 2.0** | 2025-10-08 | 遅延ヒープ構築、降順、`replaceTop`を追加                                           |
| **バージョン 3.0** | 2026-07-18 | `lib.ds.priorityqueue`へ移動し、プリミティブ版をCollection化、`addAll(Iterable)`を追加 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
