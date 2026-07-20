# Priority Queue

`lib.ds.priorityqueue`の通常・プリミティブ特化・index付き優先度キューをまとめます。

## Guide 一覧

| Guide                                                       | 対象                                                                             | 用途                                               |
|-------------------------------------------------------------|----------------------------------------------------------------------------------|----------------------------------------------------|
| [PriorityQueueGuide](./PriorityQueueGuide.md)               | `PriorityQueue<T>`, `IntPriorityQueue`, `LongPriorityQueue`                      | 可変容量の通常優先度キュー                         |
| [IndexedPriorityQueueGuide](./IndexedPriorityQueueGuide.md) | `IndexedPriorityQueue<T>`, `IntIndexedPriorityQueue`, `LongIndexedPriorityQueue` | denseなindexごとのcost更新に対応する固定範囲キュー |

## 実装クラス

| クラス                                                                                        | 保持する値         | 特徴                                       |
|-----------------------------------------------------------------------------------------------|--------------------|--------------------------------------------|
| [`PriorityQueue<T>`](../../../src/lib/ds/priorityqueue/PriorityQueue.java)                    | `T`                | 自然順序またはComparator、動的容量         |
| [`IntPriorityQueue`](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java)                 | `int`              | 昇順/降順、`IntComparator`、動的容量       |
| [`LongPriorityQueue`](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java)               | `long`             | 昇順/降順、`LongComparator`、動的容量      |
| [`IndexedPriorityQueue<T>`](../../../src/lib/ds/priorityqueue/IndexedPriorityQueue.java)      | index付きT cost    | 自然順序またはComparator、固定index範囲    |
| [`IntIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java)   | index付きint cost  | 昇順/降順、`IntComparator`、固定index範囲  |
| [`LongIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java) | index付きlong cost | 昇順/降順、`LongComparator`、固定index範囲 |

## 選択の目安

- Comparableオブジェクトの通常キューには`new PriorityQueue<>()` / `new PriorityQueue<>(true)`
- 比較不能なオブジェクトにはComparatorを渡した`PriorityQueue<T>`
- int / long値にはboxingしない`IntPriorityQueue` / `LongPriorityQueue`
- indexごとのcost更新・削除・履歴取得にはIndexed版
- Comparable costをindex管理する場合は`new IndexedPriorityQueue<>(n)`、ComparableでないcostはComparatorを指定
- Dijkstraなどで削除後の確定costも取得する場合は`getLast` / `getLastOrDefault`

## 共通方針

- `add` / `addAll`と一括初期化はヒープ構築を遅延します。
- `peek`、`poll`など優先順を必要とする操作の直前にヒープ性を復元します。
- 空キューで例外を避ける場合は`peekOrDefault` / `pollOrDefault`を使用します。
- iteratorは内部順であり、優先順ではありません。
- primitive CollectionとIndexed版のIterable要素はcostです。indexは付属情報です。
- Indexed版の重複`add`と存在しない`remove`はfalseを返します。
- 問題制約を満たす入力を前提とし、全不正引数を検査する汎用Collectionではありません。

## import

```java
import lib.ds.priorityqueue.*;
import lib.util.function.IntComparator;
import lib.util.function.LongComparator;
```

提出用ソースでは通常の`lib.*` importを使用し、AtCoder側のバンドラで展開します。
