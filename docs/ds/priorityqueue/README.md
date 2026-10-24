# Priority Queue

`lib.ds.priorityqueue`の通常・プリミティブ特化・index付き優先度キューをまとめる。

## クラス一覧

| クラス | 用途 | Guide | ソース | Example / 検証 |
|--------|------|-------|--------|----------------|
| `IndexedPriorityQueue` | indexごとのcost更新に対応する汎用優先度キュー | [IndexedPriorityQueueGuide](./IndexedPriorityQueueGuide.md) | [Java](../../../src/lib/ds/priorityqueue/IndexedPriorityQueue.java) | [Test.java](../../../test/verify/ds/priorityqueue/Test.java) |
| `IntIndexedPriorityQueue` | indexごとのcost更新に対応するint優先度キュー | [IndexedPriorityQueueGuide](./IndexedPriorityQueueGuide.md) | [Java](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java) | [Check1.java](../../../test/verify/ds/priorityqueue/intindexedpriorityqueue/Check1.java) |
| `LongIndexedPriorityQueue` | indexごとのcost更新に対応するlong優先度キュー | [IndexedPriorityQueueGuide](./IndexedPriorityQueueGuide.md) | [Java](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java) | [Test.java](../../../test/verify/ds/priorityqueue/Test.java) |
| `PriorityQueue` | 自然順序またはComparatorを使う汎用優先度キュー | [PriorityQueueGuide](./PriorityQueueGuide.md) | [Java](../../../src/lib/ds/priorityqueue/PriorityQueue.java) | [Test.java](../../../test/verify/ds/priorityqueue/Test.java) |
| `IntPriorityQueue` | int値専用の可変容量優先度キュー | [PriorityQueueGuide](./PriorityQueueGuide.md) | [Java](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java) | [Example.java](../../../test/verify/ds/priorityqueue/Example.java) · [Test.java](../../../test/verify/ds/priorityqueue/Test.java) |
| `LongPriorityQueue` | long値専用の可変容量優先度キュー | [PriorityQueueGuide](./PriorityQueueGuide.md) | [Java](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java) | [Test.java](../../../test/verify/ds/priorityqueue/Test.java) |
| `IntDoubleEndedPriorityQueue` | 最小値・最大値の両端取得、動的容量 | — | [Java](../../../src/lib/ds/priorityqueue/IntDoubleEndedPriorityQueue.java) | — |

## 実装クラス

| クラス                                                                                              | 保持する値         | 特徴                                       |
|-----------------------------------------------------------------------------------------------------|--------------------|--------------------------------------------|
| [`PriorityQueue<T>`](../../../src/lib/ds/priorityqueue/PriorityQueue.java)                          | `T`                | 自然順序またはComparator、動的容量         |
| [`IntPriorityQueue`](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java)                       | `int`              | 昇順/降順、`IntComparator`、動的容量       |
| [`LongPriorityQueue`](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java)                     | `long`             | 昇順/降順、`LongComparator`、動的容量      |
| [`IntDoubleEndedPriorityQueue`](../../../src/lib/ds/priorityqueue/IntDoubleEndedPriorityQueue.java) | `int`              | 最小値・最大値の両端取得、動的容量         |
| [`IndexedPriorityQueue<T>`](../../../src/lib/ds/priorityqueue/IndexedPriorityQueue.java)            | index付きT cost    | 自然順序またはComparator、固定index範囲    |
| [`IntIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java)         | index付きint cost  | 昇順/降順、`IntComparator`、固定index範囲  |
| [`LongIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java)       | index付きlong cost | 昇順/降順、`LongComparator`、固定index範囲 |

## 選択の目安

- Comparableオブジェクトの通常キューには`new PriorityQueue<>()` / `new PriorityQueue<>(true)`
- 比較不能なオブジェクトにはComparatorを渡した`PriorityQueue<T>`
- int / long値にはboxingしない`IntPriorityQueue` / `LongPriorityQueue`
- 同じint集合から最小値と最大値の両方を取り出す場合は`IntDoubleEndedPriorityQueue`
- indexごとのcost更新・削除・履歴取得にはIndexed版
- Comparable costをindex管理する場合は`new IndexedPriorityQueue<>(n)`、ComparableでないcostはComparatorを指定
- Dijkstraなどで削除後の確定costも取得する場合は`getLast` / `getLastOrDefault`

## 共通方針

- `add` / `addAll`と一括初期化はヒープ構築を遅延する。
- `peek`、`poll`など優先順を必要とする操作の直前にヒープ性を復元する。
- 空キューで例外を避ける場合は`peekOrDefault` / `pollOrDefault`を使用する。
- iteratorは内部順であり、優先順ではない。
- primitive CollectionとIndexed版のIterable要素はcostである。indexは付属情報である。
- Indexed版の重複`add`と存在しない`remove`はfalseを返す。
- 問題制約を満たす入力を前提とし、全不正引数を検査する汎用Collectionではない。

## import

```java
import lib.ds.priorityqueue.*;
import lib.util.function.IntComparator;
import lib.util.function.LongComparator;
```

提出用ソースでは通常の`lib.*` importを使用し、AtCoder側のバンドラで展開する。

## Example / 検証

- [Example.java](../../../test/verify/ds/priorityqueue/Example.java)
