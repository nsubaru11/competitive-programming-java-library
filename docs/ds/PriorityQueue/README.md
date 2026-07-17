# Priority Queue

`lib.ds.priorityqueue`の汎用・プリミティブ特化・index付き優先度キューをまとめます。
このドキュメントは2026-07-18時点の`src/`の実装を正とし、通常版3クラスとIndexed版2クラスをそれぞれ同じGuideで説明します。

## Guide 一覧

| Guide                                                       | 対象                                                          | 用途                               |
|-------------------------------------------------------------|-------------------------------------------------------------|----------------------------------|
| [PriorityQueueGuide](./PriorityQueueGuide.md)               | `PriorityQueue<T>`, `IntPriorityQueue`, `LongPriorityQueue` | 可変容量の通常優先度キュー                    |
| [IndexedPriorityQueueGuide](./IndexedPriorityQueueGuide.md) | `IntIndexedPriorityQueue`, `LongIndexedPriorityQueue`       | denseなindexごとのcost更新に対応する固定容量キュー |

## 実装クラス

| クラス                                                                                           | 保持する値            | 特徴                      |
|-----------------------------------------------------------------------------------------------|------------------|-------------------------|
| [`PriorityQueue<T>`](../../../src/lib/ds/priorityqueue/PriorityQueue.java)                    | `T`              | Comparator対応、動的容量       |
| [`IntPriorityQueue`](../../../src/lib/ds/priorityqueue/IntPriorityQueue.java)                 | `int`            | `IntCollection`実装、動的容量  |
| [`LongPriorityQueue`](../../../src/lib/ds/priorityqueue/LongPriorityQueue.java)               | `long`           | `LongCollection`実装、動的容量 |
| [`IntIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java)   | index付きint cost  | `IntCollection`実装、固定容量  |
| [`LongIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java) | index付きlong cost | `LongCollection`実装、固定容量 |

## 選択の目安

- 任意のComparable値やComparatorが必要なら`PriorityQueue<T>`
- int / long値だけを扱うなら`IntPriorityQueue` / `LongPriorityQueue`
- 固定範囲のindexごとにcostを更新・削除するならIndexed版
- 全indexの初期costを関数で生成するならIndexed版の`setAll`
- Dijkstraなどで削除後の確定costも取得するなら`getLast` / `getLastOrDefault`

## 共通方針

- 最小値優先を既定とし、コンストラクタのboolean指定で最大値優先へ切り替えます。
- 通常版の`push` / `addAll`、Indexed版の`push` / `pushAll`はヒープ構築を遅延します。
- `peek`、`poll`など優先順を必要とする操作の直前にヒープ性を復元します。
- iteratorは内部配列順であり、優先順ではありません。
- プリミティブCollectionの要素はcostです。Indexed版のindexは付属情報として扱います。
- 問題制約を満たす入力を前提とし、全不正引数を検査する汎用Collectionではありません。

## import

```java
import lib.ds.priorityqueue.*;
```

提出用ソースでは通常の`lib.*` importを使用し、AtCoder側のバンドラで展開します。
