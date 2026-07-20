# IndexedPriorityQueue / IntIndexedPriorityQueue / LongIndexedPriorityQueue 利用ガイド

## 概要

[`IndexedPriorityQueue<T>`](../../../src/lib/ds/priorityqueue/IndexedPriorityQueue.java)、
[`IntIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java)、
[`LongIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java)は、固定範囲`0 <= i < n`のindexへcostを対応付ける優先度キューです。同じindexを高々1個だけactiveにし、costの更新、改善、削除、削除後の最終cost取得を行えます。

## 特徴

- generic / int / longの3種類
- generic版はComparable costの自然順序構築と、比較不能なcost型向けのComparator構築に対応
- primitive版は昇順、降順、`IntComparator` / `LongComparator`に対応
- indexからヒープ位置をO(1)で取得
- `add`はinactiveなindexを登録し、重複時はfalse
- `set`は無条件更新または登録
- `relax`はより優先されるcostだけ反映し、削除済みindexを再登録しない
- `remove`は削除成否をbooleanで返す
- `get`系と`getLast`系でactive限定値と削除後を含む値を分離
- `peekOrDefault` / `pollOrDefault`とindex版を提供
- `position`配列とstampによるO (1) `clear`
- 一括追加、配列構築、`setAll`、`generate`のヒープ構築を遅延
- iteratorはcostを内部順で走査し、indexは付属情報として扱う

## 依存関係

- `java.util.Arrays`
- `java.util.Comparator`
- `java.util.Iterator`
- `java.util.PrimitiveIterator`
- `java.util.NoSuchElementException`
- `java.util.function.IntFunction`
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntToLongFunction`
- [`IntCollection`](../../../src/lib/ds/IntCollection.java)
- [`LongCollection`](../../../src/lib/ds/LongCollection.java)
- [`IntComparator`](../../../src/lib/util/function/IntComparator.java)
- [`LongComparator`](../../../src/lib/util/function/LongComparator.java)

## 主な機能（メソッド一覧）

### 1. generic版コンストラクタ・ファクトリ

| メソッド                                                                        | 説明                                          |
|---------------------------------------------------------------------------------|-----------------------------------------------|
| `IndexedPriorityQueue(int n)`                                                   | index数を指定して自然順序で構築               |
| `IndexedPriorityQueue(int n, boolean descending)`                               | index数を指定して自然順序または逆順で構築     |
| `IndexedPriorityQueue(int n, Comparator<? super T> comparator)`                 | index数とComparatorを指定                     |
| `IndexedPriorityQueue(T[] costs)`                                               | `costs[i]`をindex iへ自然順序で登録           |
| `IndexedPriorityQueue(T[] costs, boolean descending)`                           | 配列から自然順序または逆順で構築              |
| `IndexedPriorityQueue(T[] costs, Comparator<? super T> comparator)`             | `costs[i]`をindex iへ登録                     |
| `IndexedPriorityQueue.naturalOrder(int n)`                                      | Comparable costの自然順序キューを生成         |
| `IndexedPriorityQueue.reverseOrder(int n)`                                      | Comparable costの逆順キューを生成             |
| `IndexedPriorityQueue.naturalOrder(T[] costs)`                                  | 配列から自然順序キューを生成                  |
| `IndexedPriorityQueue.reverseOrder(T[] costs)`                                  | 配列から逆順キューを生成                      |
| `IndexedPriorityQueue.generate(int n, IntFunction init)`                        | `init(i)`を自然順序costとして全登録           |
| `IndexedPriorityQueue.generate(int n, IntFunction init, boolean descending)`    | `init(i)`を自然順序または逆順costとして全登録 |
| `IndexedPriorityQueue.generate(int n, IntFunction init, Comparator comparator)` | `init(i)`とComparatorを指定                   |

### 2. IntIndexedPriorityQueueコンストラクタ・ファクトリ

| メソッド                                                                                   | 説明                                  |
|--------------------------------------------------------------------------------------------|---------------------------------------|
| `IntIndexedPriorityQueue(int n)`                                                           | index数を指定して昇順で構築           |
| `IntIndexedPriorityQueue(int n, boolean descending)`                                       | index数を指定して昇順または降順で構築 |
| `IntIndexedPriorityQueue(int n, IntComparator comparator)`                                 | index数とComparatorを指定             |
| `IntIndexedPriorityQueue(int[] costs)`                                                     | 全indexを配列から昇順で登録           |
| `IntIndexedPriorityQueue(int[] costs, boolean descending)`                                 | 全indexを配列から昇順または降順で登録 |
| `IntIndexedPriorityQueue(int[] costs, IntComparator comparator)`                           | 配列とComparatorを指定                |
| `IntIndexedPriorityQueue.generate(int n, IntUnaryOperator init)`                           | 全indexを昇順で生成                   |
| `IntIndexedPriorityQueue.generate(int n, IntUnaryOperator init, boolean descending)`       | 全indexを昇順または降順で生成         |
| `IntIndexedPriorityQueue.generate(int n, IntUnaryOperator init, IntComparator comparator)` | 生成関数とComparatorを指定            |

### 3. LongIndexedPriorityQueueコンストラクタ・ファクトリ

| メソッド                                                                                      | 説明                                  |
|-----------------------------------------------------------------------------------------------|---------------------------------------|
| `LongIndexedPriorityQueue(int n)`                                                             | index数を指定して昇順で構築           |
| `LongIndexedPriorityQueue(int n, boolean descending)`                                         | index数を指定して昇順または降順で構築 |
| `LongIndexedPriorityQueue(int n, LongComparator comparator)`                                  | index数とComparatorを指定             |
| `LongIndexedPriorityQueue(long[] costs)`                                                      | 全indexを配列から昇順で登録           |
| `LongIndexedPriorityQueue(long[] costs, boolean descending)`                                  | 全indexを配列から昇順または降順で登録 |
| `LongIndexedPriorityQueue(long[] costs, LongComparator comparator)`                           | 配列とComparatorを指定                |
| `LongIndexedPriorityQueue.generate(int n, IntToLongFunction init)`                            | 全indexを昇順で生成                   |
| `LongIndexedPriorityQueue.generate(int n, IntToLongFunction init, boolean descending)`        | 全indexを昇順または降順で生成         |
| `LongIndexedPriorityQueue.generate(int n, IntToLongFunction init, LongComparator comparator)` | 生成関数とComparatorを指定            |

### 4. 追加・更新

| メソッド                                                  | 戻り値の型 | 説明                               |
|-----------------------------------------------------------|-----------:|------------------------------------|
| `IndexedPriorityQueue.add(int i, T c)`                    |  `boolean` | inactiveなら登録                   |
| `IntIndexedPriorityQueue.add(int i, int c)`               |  `boolean` | inactiveなら登録                   |
| `LongIndexedPriorityQueue.add(int i, long c)`             |  `boolean` | inactiveなら登録                   |
| `IndexedPriorityQueue.addAll(int[] is, T[] cs)`           |  `boolean` | inactiveなindexを一括登録          |
| `IntIndexedPriorityQueue.addAll(int[] is, int[] cs)`      |  `boolean` | int costを一括登録                 |
| `LongIndexedPriorityQueue.addAll(int[] is, long[] cs)`    |  `boolean` | long costを一括登録                |
| `IndexedPriorityQueue.set(int i, T c)`                    |     `void` | activeなら更新、そうでなければ登録 |
| `IntIndexedPriorityQueue.set(int i, int c)`               |     `void` | int costを設定                     |
| `LongIndexedPriorityQueue.set(int i, long c)`             |     `void` | long costを設定                    |
| `IndexedPriorityQueue.setAll(IntFunction init)`           |     `void` | 全indexをgeneric costで登録        |
| `IntIndexedPriorityQueue.setAll(IntUnaryOperator init)`   |     `void` | 全indexをint costで登録            |
| `LongIndexedPriorityQueue.setAll(IntToLongFunction init)` |     `void` | 全indexをlong costで登録           |
| `IndexedPriorityQueue.relax(int i, T c)`                  |  `boolean` | より優先される場合だけ登録・更新   |
| `IntIndexedPriorityQueue.relax(int i, int c)`             |  `boolean` | int版relax                         |
| `LongIndexedPriorityQueue.relax(int i, long c)`           |  `boolean` | long版relax                        |

### 5. 優先要素

| メソッド                               |           戻り値の型 | 説明                            |
|----------------------------------------|---------------------:|---------------------------------|
| `peek()`                               | `T` / `int` / `long` | 最優先costを返す                |
| `peekOrDefault(defaultValue)`          | `T` / `int` / `long` | 空ならdefaultValue              |
| `peekIndex()`                          |                `int` | 最優先indexを返す               |
| `peekIndexOrDefault(int defaultIndex)` |                `int` | 空ならdefaultIndex              |
| `peekSecond()`                         | `T` / `int` / `long` | 2番目に優先されるcostを返す     |
| `poll()`                               | `T` / `int` / `long` | 最優先要素を削除してcostを返す  |
| `pollOrDefault(defaultValue)`          | `T` / `int` / `long` | 空ならdefaultValue              |
| `pollIndex()`                          |                `int` | 最優先要素を削除してindexを返す |
| `pollIndexOrDefault(int defaultIndex)` |                `int` | 空ならdefaultIndex              |
| `remove(int i)`                        |            `boolean` | activeなら削除してtrue          |

### 6. indexごとのcost取得

| メソッド                                | 未追加  | active | 削除済み   |
|-----------------------------------------|---------|--------|------------|
| `get(int i)`                            | 例外    | cost   | 例外       |
| `getOrDefault(int i, defaultValue)`     | default | cost   | default    |
| `getLast(int i)`                        | 例外    | cost   | 最後のcost |
| `getLastOrDefault(int i, defaultValue)` | default | cost   | 最後のcost |

### 7. 状態・反復

| メソッド                              |                 戻り値の型 | 説明                                   |
|---------------------------------------|---------------------------:|----------------------------------------|
| `containsIndex(int i)`                |                  `boolean` | indexがactiveか判定                    |
| `hasCost(int i)`                      |                  `boolean` | 現在世代のcost記録があるか判定         |
| `size()`                              |                      `int` | activeなindex数                        |
| `indexCount()`                        |                      `int` | 使用可能なindex数                      |
| `isEmpty()`                           |                  `boolean` | active要素がないか判定                 |
| `clear()`                             |                     `void` | active要素と現在世代の記録をO(1)で消去 |
| `IndexedPriorityQueue.iterator()`     |              `Iterator<T>` | generic costを内部順で走査             |
| `IntIndexedPriorityQueue.iterator()`  |  `PrimitiveIterator.OfInt` | int costを内部順で走査                 |
| `LongIndexedPriorityQueue.iterator()` | `PrimitiveIterator.OfLong` | long costを内部順で走査                |

primitive版では`contains(cost)`、`toArray()`、`intStream()` / `longStream()`などのCollection由来メソッドも利用できます。`contains`はindexではなくcostを検索します。

## 状態管理

| 状態     | `add(i,c)`     | `set(i,c)` | `relax(i,c)`   | `remove(i)`  | `getLast(i)` |
|----------|----------------|------------|----------------|--------------|--------------|
| 未追加   | 登録してtrue   | 登録       | 登録してtrue   | false        | 例外         |
| active   | false          | 無条件更新 | 改善時だけtrue | 削除してtrue | cost         |
| 削除済み | 再登録してtrue | 再登録     | false          | false        | 最後のcost   |

active判定は`position[i]`が指すヒープ位置と`heap[position[i]] == i`の一致で行います。削除時は`position[i]`へ現在のstampを保存し、`clear()`ではstampを1つ減らすだけで全indexを次世代の未追加状態として扱います。

## 利用例

```java
LongIndexedPriorityQueue q = new LongIndexedPriorityQueue(5);
q.add(0, 10);
q.add(1, 4);
q.set(0, 3);

int i = q.pollIndex();                 // 0
long active = q.getOrDefault(i, -1);   // -1
long last = q.getLastOrDefault(i, -1); // 3
```

```java
record Job(int priority, String name) {}

IndexedPriorityQueue<Job> q =
	new IndexedPriorityQueue<>(10, Comparator.comparingInt(Job::priority));
q.add(3, new Job(2, "build"));
q.add(7, new Job(1, "test"));
int first = q.pollIndex(); // 7
```

## 注意事項

- Comparatorなしのgenericコンストラクタは全costが相互にComparableであることを前提とします。
- 比較不能なcost型でComparatorを省略すると、実際に比較した時点で`ClassCastException`になります。
- コンパイル時にComparable制約を確認したい場合は`naturalOrder` / `reverseOrder`ファクトリを使用します。
- Comparableでないcost型はComparatorを渡します。
- 構築時のnと配列長は使用可能なindex範囲全体を表します。
- 各index引数は`0 <= i < indexCount()`を満たす必要があります。
- `add`はactiveなindexではfalse、削除済みindexは明示的に再登録します。
- `relax`は削除済みindexを再登録しません。
- `addAll`内の重複indexは最初の1件だけ登録され、activeなindexは無視されます。
- `addAll`のindex配列とcost配列は同じ長さにします。
- `setAll`は以前の状態を消去して全indexをactiveにし、ヒープ構築は遅延します。
- `peek`、`peekIndex`、`poll`、`pollIndex`は空の場合に`NoSuchElementException`を投げます。
- `peekSecond`は要素数2以上で呼び出します。
- iteratorとCollection由来の変換結果は優先順ではなく、iterator生成時もヒープ化しません。

## パフォーマンス特性

- `add`: O (1)
- `addAll`: O (k)
- 配列構築、`generate`、`setAll`: O (n)で登録し、ヒープ構築を遅延
- `set`, `relax`: clean領域ならO (log n)、未整列領域ならO (1)
- `remove`: cleanならO (log n)、dirtyなら先にヒープ構築を伴う
- `peek`, `peekIndex`, `peekSecond`: cleanならO (1)、dirtyならO (n)またはO (k log n)の構築を伴う
- `poll`, `pollIndex`: cleanならO (log n)、dirtyなら構築コストを伴う
- getter、状態判定、`size`、`indexCount`、`clear`、iterator生成: O (1)
- iteratorとCollection由来の走査・変換: O (n)
- 使用メモリ: O(n)。cost、heap、positionの3配列を保持

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                                                                                                            |
|:-------------------|:-----------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 4.0** | 2026-07-20 | generic版の自然順序/Comparator構築、Primitive Comparator、`add` / `remove`のboolean化、default系、配列構築、`generate`、`indexCount`を追加し、bulk操作とiteratorのheapifyを遅延 |
| **バージョン 3.0** | 2026-07-18 | `setAll`を追加し、`set` / `relax`が未整列領域を展開しないよう改善                                                                                                               |
| **バージョン 2.0** | 2026-07-18 | Int/Long版へ分割し、stampによるO(1) clear、`set`、active限定getter、`getLast`を追加                                                                                             |
| **バージョン 1.0** | 2025-10-07 | long costのIndexedPriorityQueueとして初回実装                                                                                                                                   |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
