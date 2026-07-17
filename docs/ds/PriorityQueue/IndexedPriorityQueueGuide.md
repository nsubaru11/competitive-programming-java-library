# IntIndexedPriorityQueue / LongIndexedPriorityQueue 利用ガイド

## 概要

[`IntIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/IntIndexedPriorityQueue.java) と
[`LongIndexedPriorityQueue`](../../../src/lib/ds/priorityqueue/LongIndexedPriorityQueue.java) は、固定範囲`0 <= i < n`のindexへcostを対応付ける優先度キューです。同じindexを高々1個だけactiveにし、costの更新、改善、削除、削除後の最終cost取得を行えます。

## 特徴

- int / longのcostをプリミティブのまま保持
- indexからヒープ位置をO(1)で取得
- `set`はcostを無条件更新し、未追加・削除済みindexも登録
- `relax`はより優先されるcostだけ反映し、削除済みindexは再登録しない
- `push` / `pushAll`後のヒープ構築を遅延
- `set` / `relax`は未整列領域を展開せず、整列済み領域だけヒープ性を維持
- `setAll`で全indexを一括登録してheapify
- `position`配列と負のstampだけで未追加・active・削除済みを管理
- `clear()`は配列を初期化せずO(1)
- 削除済みindexのcostを`getLast`で取得可能
- activeなcostのCollectionとして走査可能

## 依存関係

- `java.util.NoSuchElementException`
- `java.util.PrimitiveIterator`
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntToLongFunction`
- [`lib.ds.IntCollection`](../../../src/lib/ds/IntCollection.java)
- [`lib.ds.LongCollection`](../../../src/lib/ds/LongCollection.java)

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                                                  | 説明                              |
|-------------------------------------------------------|---------------------------------|
| `IntIndexedPriorityQueue(int n)`                      | index数`n`、int cost、最小値優先で構築     |
| `IntIndexedPriorityQueue(int n, boolean descending)`  | index数`n`、int cost、優先方向を指定して構築  |
| `LongIndexedPriorityQueue(int n)`                     | index数`n`、long cost、最小値優先で構築    |
| `LongIndexedPriorityQueue(int n, boolean descending)` | index数`n`、long cost、優先方向を指定して構築 |

### 2. 一括初期化・追加・更新

| メソッド                                                      | 戻り値の型     | 説明                            |
|-----------------------------------------------------------|-----------|-------------------------------|
| `IntIndexedPriorityQueue.setAll(IntUnaryOperator init)`   | `void`    | 全indexを`init(i)`のint costで登録  |
| `LongIndexedPriorityQueue.setAll(IntToLongFunction init)` | `void`    | 全indexを`init(i)`のlong costで登録 |
| `IntIndexedPriorityQueue.push(int i, int c)`              | `void`    | 現在世代で未追加のindexを登録             |
| `LongIndexedPriorityQueue.push(int i, long c)`            | `void`    | 現在世代で未追加のindexを登録             |
| `IntIndexedPriorityQueue.pushAll(int[] is, int[] cs)`     | `void`    | 未追加のindexとint costをまとめて登録     |
| `LongIndexedPriorityQueue.pushAll(int[] is, long[] cs)`   | `void`    | 未追加のindexとlong costをまとめて登録    |
| `IntIndexedPriorityQueue.set(int i, int c)`               | `void`    | activeなら更新し、未追加・削除済みなら登録      |
| `LongIndexedPriorityQueue.set(int i, long c)`             | `void`    | activeなら更新し、未追加・削除済みなら登録      |
| `IntIndexedPriorityQueue.relax(int i, int c)`             | `boolean` | 登録またはより優先されるcostへの更新を行ったか返す   |
| `LongIndexedPriorityQueue.relax(int i, long c)`           | `boolean` | 登録またはより優先されるcostへの更新を行ったか返す   |

### 3. 優先要素・削除

| メソッド                                    | 戻り値の型  | 説明                     |
|-----------------------------------------|--------|------------------------|
| `IntIndexedPriorityQueue.peek()`        | `int`  | 最優先costを削除せず返す         |
| `LongIndexedPriorityQueue.peek()`       | `long` | 最優先costを削除せず返す         |
| `peekIndex()`                           | `int`  | 最優先indexを削除せず返す        |
| `IntIndexedPriorityQueue.peekSecond()`  | `int`  | 2番目に優先されるint costを返す   |
| `LongIndexedPriorityQueue.peekSecond()` | `long` | 2番目に優先されるlong costを返す  |
| `IntIndexedPriorityQueue.poll()`        | `int`  | 最優先要素を削除してint costを返す  |
| `LongIndexedPriorityQueue.poll()`       | `long` | 最優先要素を削除してlong costを返す |
| `pollIndex()`                           | `int`  | 最優先要素を削除してindexを返す     |
| `remove(int i)`                         | `void` | 指定したactiveなindexを削除    |

### 4. indexごとのcost取得

| メソッド                                    | 未追加     | active | 削除済み    |
|-----------------------------------------|---------|--------|---------|
| `get(int i)`                            | 例外      | cost   | 例外      |
| `getOrDefault(int i, defaultValue)`     | default | cost   | default |
| `getLast(int i)`                        | 例外      | cost   | 最後のcost |
| `getLastOrDefault(int i, defaultValue)` | default | cost   | 最後のcost |

Int版の戻り値と`defaultValue`は`int`、Long版では`long`です。

### 5. 状態・Collection

| メソッド                                         | 戻り値の型                                | 説明                           |
|----------------------------------------------|--------------------------------------|------------------------------|
| `containsIndex(int i)`                       | `boolean`                            | indexがactiveか判定              |
| `hasCost(int i)`                             | `boolean`                            | 現在世代でcostが記録されたか判定。削除済みもtrue |
| `size()`                                     | `int`                                | activeなindex数を返す             |
| `isEmpty()`                                  | `boolean`                            | activeなindexがないか判定           |
| `clear()`                                    | `void`                               | active要素と現在世代のcost記録を論理的に消去  |
| `iterator()`                                 | `PrimitiveIterator.OfInt` / `OfLong` | activeなcostを内部ヒープ順に走査        |
| `contains(cost)`                             | `boolean`                            | activeなcostが含まれるか線形探索        |
| `forEachInt(action)` / `forEachLong(action)` | `void`                               | activeな全costへ処理を適用           |
| `spliterator()`                              | `Spliterator.OfInt` / `OfLong`       | プリミティブspliteratorを返す         |
| `intStream()` / `longStream()`               | `IntStream` / `LongStream`           | activeなcostのStreamを返す        |
| `toList()`                                   | `List<Integer>` / `List<Long>`       | activeなcostをボックス化した不変Listで返す |
| `toArray()`                                  | `int[]` / `long[]`                   | activeなcostをプリミティブ配列へコピー     |

## 状態管理

各indexは現在世代で次のいずれかです。

| 状態     | `containsIndex(i)` | `hasCost(i)` | `set(i, c)` | `relax(i, c)` |
|--------|-------------------:|-------------:|-------------|---------------|
| 未追加    |              false |        false | 登録          | 登録            |
| active |               true |         true | 無条件更新       | 改善時だけ更新       |
| 削除済み   |              false |         true | 再登録         | 何もしない         |

active判定は`position[i]`が指すヒープ位置と`heap[position[i]] == i`の一致で行います。削除時は`position[i]`へ現在の負のstampを保存し、`clear()`ではstampを1つ減らすだけで全indexを次世代の未追加状態として扱います。

## 利用例

```java
LongIndexedPriorityQueue q = new LongIndexedPriorityQueue(5);
q.push(0, 10);
q.push(1, 4);
q.set(0, 3);

int i = q.pollIndex();                    // 0
long active = q.getOrDefault(i, -1);      // -1
long last = q.getLastOrDefault(i, -1);    // 3
```

```java
IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(4, true);
q.setAll(i -> i * 10);

System.out.println(q.peekIndex());  // 3
System.out.println(q.peek());       // 30
```

## 注意事項

- 構築時の`n`は使用するindex範囲全体を表し、各操作は`0 <= i < n`で呼び出します。
- `push(i, c)`は現在世代で未追加のindexにだけ使用します。activeまたは削除済みなら`IllegalArgumentException`を投げます。
- `pushAll`の2配列は同じ長さとし、含まれるindexはすべて未追加かつ重複なしとします。
- `setAll`は以前の状態を消去し、`0`から`n - 1`までの全indexをactiveにします。
- `set`は削除済みindexを再登録しますが、`relax`は再登録しません。
- `get`系はactive限定、`getLast`系は削除済みを含む現在世代の記録を対象にします。
- `peek`、`peekIndex`、`poll`、`pollIndex`は空でない状態で呼び出します。
- `peekSecond`は要素数が2以上の状態で呼び出します。
- iteratorとCollection由来の変換結果は優先順ではありません。

## パフォーマンス特性

- `push`: O(1)
- `pushAll`: O(k)
- `setAll`: O(n)
- `set`, `relax`: O(log n)。対象が未整列領域ならO(1)
- `remove`: ヒープ構築済みならO(log n)。未整列要素があれば先にO(n)またはO(k log n)の構築を伴う
- `peek`, `peekIndex`, `peekSecond`: ヒープ構築済みならO(1)。未整列要素があれば構築コストを伴う
- `poll`, `pollIndex`: ヒープ構築済みならO(log n)。未整列要素があれば構築コストを伴う
- `get`, `getOrDefault`, `getLast`, `getLastOrDefault`, `containsIndex`, `hasCost`, `size`, `clear`: O(1)
- iteratorとCollection由来の走査・変換: O(n)。iterator生成時に未整列要素があればヒープ構築を行う
- 使用メモリ: O(n)。cost、heap、positionの3配列を保持

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                 |
|:--------------|:-----------|:-------------------------------------------------------------------|
| **バージョン 1.0** | 2025-10-07 | long costの`IndexedPriorityQueue`として初回実装                            |
| **バージョン 2.0** | 2026-07-18 | Int/Long版へ分割し、stampによるO(1) clear、`set`、active限定getter、`getLast`を追加 |
| **バージョン 3.0** | 2026-07-18 | `setAll`を追加し、`set` / `relax`が未整列領域を展開しないよう改善                       |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
