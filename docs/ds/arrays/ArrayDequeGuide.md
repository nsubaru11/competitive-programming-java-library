# IntArrayDeque / LongArrayDeque 利用ガイド

> 先頭・末尾の追加削除と添字アクセスに対応するプリミティブ特化 deque。

[← README: Primitive Array Utilities](./README.md) · ソース: [IntArrayDeque.java](../../../src/lib/ds/arrays/IntArrayDeque.java) · [LongArrayDeque.java](../../../src/lib/ds/arrays/LongArrayDeque.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

内部配列を循環バッファとして利用し、先頭を `head`、要素数を `size` で管理する。内部容量は2のべき乗とし、論理添字から物理添字への変換にビットマスクを用いる。

## 特徴

- int / long 特化で要素を保持する。
- 先頭・末尾の追加は償却定数時間で、満杯時は容量を2倍にする。
- 先頭を0とする添字アクセスと、配列・コレクションからのコピーに対応する。
- 空や範囲外で指定値を返す `OrDefault` 系を備える。
- `fill` は有効要素数だけを書き換え、`toArray` は最大2区間を一括コピーする。

### 型別クラス

| クラス                                                             | 対象型 | 選ぶ目安                           |
|--------------------------------------------------------------------|--------|------------------------------------|
| [`IntArrayDeque`](../../../src/lib/ds/arrays/IntArrayDeque.java)   | `int`  | int値を両端から追加・削除する場合  |
| [`LongArrayDeque`](../../../src/lib/ds/arrays/LongArrayDeque.java) | `long` | long値を両端から追加・削除する場合 |

## 依存関係

- `java.lang.Math`、`java.util` の配列操作・添字検査・例外・プリミティブ iterator。
- `IntMutableArray` / `LongMutableArray`、`IntArray` / `LongArray`、`IntCollection` / `LongCollection`。継承 API の詳細は [MutableArrayGuide](./MutableArrayGuide.md) と [ArrayGuide](./ArrayGuide.md) を参照する。
- 継承 API は `java.util.function` と `java.util.stream` を利用する。

## API

### `IntArrayDeque`

| 種別        | シグネチャ                               | 戻り値                    | 説明                                                                                                                     |
|-------------|------------------------------------------|---------------------------|--------------------------------------------------------------------------------------------------------------------------|
| Constructor | `IntArrayDeque()`                        | —                         | 初期容量16の空の deque を構築する。                                                                                      |
| Constructor | `IntArrayDeque(int capacity)`            | —                         | 空の deque を構築する。初期容量は非負とし、最低1の2のべき乗へ切り上げる。確保可能な容量を指定する。                      |
| Constructor | `IntArrayDeque(int[] a)`                 | —                         | 配列の全要素を同じ順序でコピーして構築する。                                                                             |
| Constructor | `IntArrayDeque(IntCollection a)`         | —                         | コレクションの iterator 順に全要素をコピーして構築する。                                                                 |
| Method      | `size()`                                 | `int`                     | 現在の要素数を返す。                                                                                                     |
| Method      | `get(int i)`                             | `int`                     | 指定した論理位置の値を返す。                                                                                             |
| Method      | `set(int i, int v)`                      | `int`                     | 指定した論理位置を v に更新し、更新前の値を返す。                                                                        |
| Method      | `addFirst(int v)`                        | `void`                    | 先頭に v を追加する。満杯の場合は内部容量を2倍に拡張する。                                                               |
| Method      | `addLast(int v)`                         | `void`                    | 末尾に v を追加する。満杯の場合は内部容量を2倍に拡張する。                                                               |
| Method      | `removeFirst()`                          | `int`                     | 先頭の要素を削除して返す。                                                                                               |
| Method      | `removeFirstOrDefault(int defaultValue)` | `int`                     | 先頭の要素を削除して返す。空の場合は状態を変更せず defaultValue を返す。                                                 |
| Method      | `removeLast()`                           | `int`                     | 末尾の要素を削除して返す。                                                                                               |
| Method      | `removeLastOrDefault(int defaultValue)`  | `int`                     | 末尾の要素を削除して返す。空の場合は状態を変更せず defaultValue を返す。                                                 |
| Method      | `fill(int v)`                            | `IntArrayDeque`           | 全要素を v に更新する。要素数と容量を維持し、内部の先頭位置を0へ移す。                                                   |
| Method      | `setAll(IntUnaryOperator init)`          | `IntArrayDeque`           | 先頭から論理添字の昇順に init を1回ずつ呼んで全要素を更新し、自身を返す。要素数と容量を維持し、内部の先頭位置を0へ移す。 |
| Method      | `clear()`                                | `void`                    | 全要素を削除する。内部配列の縮小や初期化は行わず、容量を再利用する。                                                     |
| Method      | `toArray()`                              | `int[]`                   | 先頭から末尾の論理順で独立した配列へコピーする。                                                                         |
| Method      | `iterator()`                             | `PrimitiveIterator.OfInt` | 先頭から末尾の論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。                         |
| Method      | `toString()`                             | `String`                  | 先頭から末尾の要素を半角スペース区切りで返す。要素がなければ空文字列を返す。                                             |

継承する API：

| 種別   | シグネチャ                                  | 戻り値              | 説明                                                                   |
|--------|---------------------------------------------|---------------------|------------------------------------------------------------------------|
| Method | `isEmpty()`                                 | `boolean`           | 要素数が0か判定                                                        |
| Method | `getOrDefault(int i, int defaultValue)`     | `int`               | 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。       |
| Method | `getFirst()`                                | `int`               | 先頭の要素を削除せず返す。空の場合は NoSuchElementException を投げる。 |
| Method | `getFirstOrDefault(int defaultValue)`       | `int`               | 先頭の要素を削除せず返す。空の場合は defaultValue を返す。             |
| Method | `getLast()`                                 | `int`               | 末尾の要素を削除せず返す。空の場合は NoSuchElementException を投げる。 |
| Method | `getLastOrDefault(int defaultValue)`        | `int`               | 末尾の要素を削除せず返す。空の場合は defaultValue を返す。             |
| Method | `contains(int v)`                           | `boolean`           | iterator による線形探索                                                |
| Method | `forEachInt(IntConsumer action)`            | `void`              | 論理順で各要素に適用                                                   |
| Method | `intStream()`                               | `IntStream`         | 論理順の逐次 Stream を生成                                             |
| Method | `toList()`                                  | `List<Integer>`     | 論理順の不変リストを生成                                               |
| Method | `forEach(Consumer<? super Integer> action)` | `void`              | Iterable の既定実装。ボクシングして論理順に適用                        |
| Method | `spliterator()`                             | `Spliterator.OfInt` | ORDERED・SIZED・SUBSIZED を持つ                                        |

### `LongArrayDeque`

| 種別        | シグネチャ                                | 戻り値                     | 説明                                                                                                                     |
|-------------|-------------------------------------------|----------------------------|--------------------------------------------------------------------------------------------------------------------------|
| Constructor | `LongArrayDeque()`                        | —                          | 初期容量16の空の deque を構築する。                                                                                      |
| Constructor | `LongArrayDeque(int capacity)`            | —                          | 空の deque を構築する。初期容量は非負とし、最低1の2のべき乗へ切り上げる。確保可能な容量を指定する。                      |
| Constructor | `LongArrayDeque(long[] a)`                | —                          | 配列の全要素を同じ順序でコピーして構築する。                                                                             |
| Constructor | `LongArrayDeque(LongCollection a)`        | —                          | コレクションの iterator 順に全要素をコピーして構築する。                                                                 |
| Method      | `size()`                                  | `int`                      | 現在の要素数を返す。                                                                                                     |
| Method      | `get(int i)`                              | `long`                     | 指定した論理位置の値を返す。                                                                                             |
| Method      | `set(int i, long v)`                      | `long`                     | 指定した論理位置を v に更新し、更新前の値を返す。                                                                        |
| Method      | `addFirst(long v)`                        | `void`                     | 先頭に v を追加する。満杯の場合は内部容量を2倍に拡張する。                                                               |
| Method      | `addLast(long v)`                         | `void`                     | 末尾に v を追加する。満杯の場合は内部容量を2倍に拡張する。                                                               |
| Method      | `removeFirst()`                           | `long`                     | 先頭の要素を削除して返す。                                                                                               |
| Method      | `removeFirstOrDefault(long defaultValue)` | `long`                     | 先頭の要素を削除して返す。空の場合は状態を変更せず defaultValue を返す。                                                 |
| Method      | `removeLast()`                            | `long`                     | 末尾の要素を削除して返す。                                                                                               |
| Method      | `removeLastOrDefault(long defaultValue)`  | `long`                     | 末尾の要素を削除して返す。空の場合は状態を変更せず defaultValue を返す。                                                 |
| Method      | `fill(long v)`                            | `LongArrayDeque`           | 全要素を v に更新する。要素数と容量を維持し、内部の先頭位置を0へ移す。                                                   |
| Method      | `setAll(IntToLongFunction init)`          | `LongArrayDeque`           | 先頭から論理添字の昇順に init を1回ずつ呼んで全要素を更新し、自身を返す。要素数と容量を維持し、内部の先頭位置を0へ移す。 |
| Method      | `clear()`                                 | `void`                     | 全要素を削除する。内部配列の縮小や初期化は行わず、容量を再利用する。                                                     |
| Method      | `toArray()`                               | `long[]`                   | 先頭から末尾の論理順で独立した配列へコピーする。                                                                         |
| Method      | `iterator()`                              | `PrimitiveIterator.OfLong` | 先頭から末尾の論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。                         |
| Method      | `toString()`                              | `String`                   | 先頭から末尾の要素を半角スペース区切りで返す。要素がなければ空文字列を返す。                                             |

継承する API：

| 種別   | シグネチャ                               | 戻り値               | 説明                                                                   |
|--------|------------------------------------------|----------------------|------------------------------------------------------------------------|
| Method | `isEmpty()`                              | `boolean`            | 要素数が0か判定                                                        |
| Method | `getOrDefault(int i, long defaultValue)` | `long`               | 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。       |
| Method | `getFirst()`                             | `long`               | 先頭の要素を削除せず返す。空の場合は NoSuchElementException を投げる。 |
| Method | `getFirstOrDefault(long defaultValue)`   | `long`               | 先頭の要素を削除せず返す。空の場合は defaultValue を返す。             |
| Method | `getLast()`                              | `long`               | 末尾の要素を削除せず返す。空の場合は NoSuchElementException を投げる。 |
| Method | `getLastOrDefault(long defaultValue)`    | `long`               | 末尾の要素を削除せず返す。空の場合は defaultValue を返す。             |
| Method | `contains(long v)`                       | `boolean`            | iterator による線形探索                                                |
| Method | `forEachLong(LongConsumer action)`       | `void`               | 論理順で各要素に適用                                                   |
| Method | `longStream()`                           | `LongStream`         | 論理順の逐次 Stream を生成                                             |
| Method | `toList()`                               | `List<Long>`         | 論理順の不変リストを生成                                               |
| Method | `forEach(Consumer<? super Long> action)` | `void`               | Iterable の既定実装。ボクシングして論理順に適用                        |
| Method | `spliterator()`                          | `Spliterator.OfLong` | ORDERED・SIZED・SUBSIZED を持つ                                        |

探索の `indexOf` / `lastIndexOf` は [ArrayGuide](./ArrayGuide.md)、要素演算・交換・反転の継承 API は [MutableArrayGuide](./MutableArrayGuide.md) を参照する。

## 利用例

[Test.java](../../../test/example/ds/arrays/Test.java)

## 制約・注意事項

- 初期容量は非負で、2のべき乗への切り上げ後および拡張後の配列を確保可能な範囲とする。容量0は内部容量1になる。
- 添字は0始まりで、有効範囲は `[0, size())`。`get` / `set` は範囲外で `IndexOutOfBoundsException` を投げる。`getOrDefault` は指定値を返す。
- `removeFirst` / `removeLast` / `getFirst` / `getLast` は空の場合に `NoSuchElementException` を投げる。対応する `OrDefault` 系は指定値を返し、状態を変えない。
- 先頭・末尾取得は配列インターフェースの `getFirst` / `getLast` と対応する `OrDefault` 系を継承する。
- `set` は更新前の値を返す。`fill` は要素数と容量を維持する。更新関数の共通条件は [MutableArrayGuide](./MutableArrayGuide.md#制約注意事項) を参照する。
- コレクションからは iterator 順にコピーする。元と内部配列を共有しない。コピー元が同じ deque 型でも独立した複製になる。
- `equals` / `hashCode` は `Object` の実装を継承する。`equals` は同一インスタンスかを判定し、要素列による比較は行わない。
- `clear` は内部配列を縮小・初期化しない。自動縮小は行わない。
- iterator / spliterator / Stream と、それらを使う一括処理の実行中は、要素追加・削除・`clear`・`fill` を行わない。各要素の取得時に現在の `head` と `size` を参照し、`fill` も `head` を0へ戻すため、要素数が変わらなくても配置は変わる。変更検出は保証しない。
- `clone`、`generate`、`isFull`、`remainingCapacity`、コピー先を受け取る `toArray` は提供しない。

## 性能特性

`n` は現在の要素数、`c` は内部容量とする。構築時の `n` はコピー元の要素数である。

| 操作                                                                                                            | 時間計算量                 | 補足                                |
|-----------------------------------------------------------------------------------------------------------------|----------------------------|-------------------------------------|
| 容量指定の構築                                                                                                  | `O(c)`                     | 追加領域 `O(c)`                     |
| 配列・コレクションからの構築                                                                                    | `O(n)`                     | 追加領域 `O(n)`                     |
| `addFirst`, `addLast`                                                                                           | 償却 `O(1)`、拡張時 `O(n)` | 追加領域 拡張時 `O(n)`、通常 `O(1)` |
| `size`, `isEmpty`, `get`, `getOrDefault`, `getLast`, `getLastOrDefault`, `set`, `remove*`, `getFirst*`, `clear` | `O(1)`                     | 追加領域 `O(1)`                     |
| `fill`, `setAll`, `contains`, `indexOf`, `lastIndexOf`, `reverse`, 全要素の走査                                 | `O(n)`                     | 追加領域 `O(1)`                     |
| `toArray`, `toList`, `toString`                                                                                 | `O(n)`                     | 追加領域 `O(n)`                     |
| iterator / spliterator / Stream の生成                                                                          | `O(1)`                     | 追加領域 `O(1)`                     |

`remove*` / `getFirst*` は `OrDefault` 系を含む。関数の評価時間は別途加算する。iterator の1要素取得は `O(1)`。本体の使用領域は `O(c)` である。

- **空間計算量**: `O(c)`

## 実装履歴

| バージョン         | 日付       | 対象クラス・変更内容                                                                                                                                                                                                                                                           |
|--------------------|------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-12 | プリミティブ特化の可変容量循環 deque として初回実装                                                                                                                                                                                                                            |
| **バージョン 2.0** | 2026-07-15 | `IntMutableArray` / `LongMutableArray` の実装へ変更                                                                                                                                                                                                                            |
| **バージョン 2.1** | 2026-07-15 | `toString()` を論理順の空白区切り形式へ改善                                                                                                                                                                                                                                    |
| **バージョン 3.0** | 2026-07-15 | 配列・配列インターフェース・supplierからの構築と、`head` を0へ正規化する `fill` / `setAll` を追加                                                                                                                                                                              |
| **バージョン 3.1** | 2026-07-27 | `toString()` の初期容量を最大文字数と区切り文字数に合わせて調整                                                                                                                                                                                                                |
| **バージョン 4.0** | 2026-10-23 | Collection からの構築と OrDefault 系を追加。添字を非負に統一。追加・clear を void 化。fill を連続領域の更新、grow・toArray を arraycopy に変更。setAll は既定実装へ移行。clone・generate・isFull・remainingCapacity・コピー先指定 toArray と内容比較の equals・hashCode を削除 |
| **バージョン 4.1** | 2026-10-23 | 取得と更新のメソッドを整理し、OrDefault を対応メソッドに隣接。内部の命名と初期化を統一                                                                                                                                                                                         |
| **バージョン 5.0** | 2026-10-24 | getOrDefault を配列インターフェースの既定実装へ移行し、getLast / getLastOrDefault を継承                                                                                                                                                                                       |
| **バージョン 6.0** | 2026-10-25 | 削除操作を removeFirst / removeLast 系に変更。peek 系を継承する getFirst / getLast 系へ統合し、capacity() を削除                                                                                                                                                               |
| **バージョン 6.1** | 2026-10-27 | IntArrayDeque / LongArrayDeque: 容量フィールドを内部配列長で代替し、例外メッセージを削除。OrDefault 系の削除を対応する削除操作へ委譲                                                                                                                                           |
| **バージョン 7.0** | 2026-10-27 | IntArrayDeque / LongArrayDeque: 初期容量16の引数なしコンストラクタを追加                                                                                                                                                                                                       |
| **バージョン 8.0** | 2026-10-28 | IntArrayDeque / LongArrayDeque: fill が自身を返すように変更し、論理添字順に内部配列へ直接書き込む setAll を追加                                                                                                                                                                |
