# Priority Queue

> `lib.ds.priorityqueue` の可変容量優先度キュー、添字ごとにコストを管理する indexed priority queue、両端優先度キューをまとめる。

各キューは追加時のヒープ化を遅延し、優先順が必要な操作の直前にまとめて復元する。走査は内部配列の順であり、優先順ではない。

## クラス一覧

| クラス                        | 用途                                                                           | Guide                                                       | ソース                                                                     | Example / 検証                                                                                                                            |
|-------------------------------|--------------------------------------------------------------------------------|-------------------------------------------------------------|----------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| `PriorityQueue`               | 自然順序・逆順・Comparator で並べる汎用の可変容量優先度キュー                  | [PriorityQueueGuide](./PriorityQueueGuide.md)               | [Java](../../../src/lib/ds/priorityqueue/PriorityQueue.java)               | [Test](../../../test/unit/ds/priorityqueue/Test.java)                                                                                     |
| `IntPriorityQueue`            | int 値をボクシングせずに扱う可変容量優先度キュー                               | [PriorityQueueGuide](./PriorityQueueGuide.md)               | [Java](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java)            | [Example](../../../test/example/ds/priorityqueue/Example.java) · [Test](../../../test/unit/ds/priorityqueue/Test.java)                    |
| `LongPriorityQueue`           | long 値をボクシングせずに扱う可変容量優先度キュー                              | [PriorityQueueGuide](./PriorityQueueGuide.md)               | [Java](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java)           | [Test](../../../test/unit/ds/priorityqueue/Test.java)                                                                                     |
| `IndexedPriorityQueue`        | 添字ごとに汎用型のコストを持ち、更新・改善・削除・最終コスト取得に対応する     | [IndexedPriorityQueueGuide](./IndexedPriorityQueueGuide.md) | [Java](../../../src/lib/ds/priorityqueue/IndexedPriorityQueue.java)        | [Test](../../../test/unit/ds/priorityqueue/Test.java)                                                                                     |
| `IntIndexedPriorityQueue`     | 添字ごとに int のコストを持つ indexed priority queue                           | [IndexedPriorityQueueGuide](./IndexedPriorityQueueGuide.md) | [Java](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java)     | [Test](../../../test/unit/ds/priorityqueue/Test.java) · [Judge](../../../test/judge/ds/priorityqueue/intindexedpriorityqueue/Judge1.java) |
| `LongIndexedPriorityQueue`    | 添字ごとに long のコストを持つ indexed priority queue。Dijkstra・Prim が使う   | [IndexedPriorityQueueGuide](./IndexedPriorityQueueGuide.md) | [Java](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java)    | [Test](../../../test/unit/ds/priorityqueue/Test.java)                                                                                     |
| `IntDoubleEndedPriorityQueue` | int 値の最小値・最大値の両方を取り出す両端優先度キュー。開発中のため使用しない | —                                                           | [Java](../../../src/lib/ds/priorityqueue/IntDoubleEndedPriorityQueue.java) | —                                                                                                                                         |

`lib.ds.priorityqueue.PriorityQueue` は `java.util.PriorityQueue` と単純名が衝突する。`java.util.*` と併用する場合は `import lib.ds.priorityqueue.PriorityQueue;` を明示する。
