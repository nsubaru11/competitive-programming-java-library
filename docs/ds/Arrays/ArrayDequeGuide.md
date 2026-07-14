# IntArrayDeque / LongArrayDeque 利用ガイド

## 概要

[`IntArrayDeque`](../../../src/lib/ds/arrays/IntArrayDeque.java) と [`LongArrayDeque`](../../../src/lib/ds/arrays/LongArrayDeque.java) は、先頭と末尾の追加・削除に対応するプリミティブ特化 deque です。内部容量を2のべき乗に正規化し、循環位置の計算に剰余ではなくビット演算を使用します。

## 特徴

- `int` / `long` 特化によりボクシングを回避
- 先頭・末尾の追加を償却 O(1) で実行
- 2のべき乗容量とビットマスクによる循環バッファ
- 正負の論理添字による O(1) のランダムアクセス・更新
- 配列、`IntArray` / `LongArray`、supplierからの初期化に対応
- `fill` と `setAll` による一括更新
- 満杯時は容量を2倍にして論理順へ再配置
- clone、要素順に基づく equals / hashCode、配列化に対応

## 依存関係

- `java.util.Arrays`
- `java.util.NoSuchElementException`
- `java.util.PrimitiveIterator`
- `java.util.function.IntSupplier`
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntToLongFunction`
- `java.util.function.LongSupplier`
- [`lib.ds.arrays.IntMutableArray`](../../../src/lib/ds/arrays/IntMutableArray.java)
- [`lib.ds.arrays.LongMutableArray`](../../../src/lib/ds/arrays/LongMutableArray.java)

## 主な機能（メソッド一覧）

### 1. コンストラクタ・容量

| メソッド                                   | 戻り値の型            | 説明                      |
|----------------------------------------|------------------|-------------------------|
| `IntArrayDeque(int capacity)`          | -                | 指定容量以上の2のべき乗容量で構築       |
| `LongArrayDeque(int capacity)`         | -                | 指定容量以上の2のべき乗容量で構築       |
| `IntArrayDeque(int[] a)`               | -                | 配列の要素を同じ順序で保持して構築       |
| `LongArrayDeque(long[] a)`             | -                | 配列の要素を同じ順序で保持して構築       |
| `IntArrayDeque(IntArray a)`            | -                | `IntArray` の論理順を保持して構築  |
| `LongArrayDeque(LongArray a)`          | -                | `LongArray` の論理順を保持して構築 |
| `IntArrayDeque.generate(int n, init)`  | `IntArrayDeque`  | supplierが生成する `n` 要素で構築 |
| `LongArrayDeque.generate(int n, init)` | `LongArrayDeque` | supplierが生成する `n` 要素で構築 |
| `size()`                               | `int`            | 現在の要素数                  |
| `capacity()`                           | `int`            | 現在の内部配列容量               |
| `remainingCapacity()`                  | `int`            | `capacity() - size()`   |
| `isEmpty()`                            | `boolean`        | 空か判定                    |
| `isFull()`                             | `boolean`        | 現在の内部容量を使い切っているか判定      |

### 2. 先頭・末尾操作

| メソッド              | 戻り値の型          | 説明                |
|-------------------|----------------|-------------------|
| `addFirst(value)` | `boolean`      | 先頭へ追加し `true` を返す |
| `addLast(value)`  | `boolean`      | 末尾へ追加し `true` を返す |
| `peekFirst()`     | `int` / `long` | 先頭を削除せず返す         |
| `peekLast()`      | `int` / `long` | 末尾を削除せず返す         |
| `pollFirst()`     | `int` / `long` | 先頭を返して削除          |
| `pollLast()`      | `int` / `long` | 末尾を返して削除          |

### 3. 添字アクセス・検索

| メソッド                    | 戻り値の型          | 説明                   |
|-------------------------|----------------|----------------------|
| `get(int index)`        | `int` / `long` | 論理添字の値を返す。負数は末尾から数える |
| `set(int index, value)` | `int` / `long` | 論理添字を更新し、旧値を返す       |
| `fill(value)`           | `void`         | 全要素を指定値で更新           |
| `setAll(init)`          | `void`         | 論理添字から生成した値で全要素を更新   |
| `contains(value)`       | `boolean`      | 値が含まれるか線形探索          |

### 4. クリア・複製・比較

| メソッド               | 戻り値の型                              | 説明               |
|--------------------|------------------------------------|------------------|
| `clear()`          | `IntArrayDeque` / `LongArrayDeque` | 要素数を0にして自身を返す    |
| `clone()`          | `IntArrayDeque` / `LongArrayDeque` | 内部配列を含む独立した複製を返す |
| `equals(Object o)` | `boolean`                          | 同じ型・要素数・論理順か比較   |
| `hashCode()`       | `int`                              | 論理順の要素に基づくハッシュ値  |

### 5. 変換・反復

| メソッド            | 戻り値の型                                | 説明                               |
|-----------------|--------------------------------------|----------------------------------|
| `toArray()`     | `int[]` / `long[]`                   | 論理順の新しい配列を返す                     |
| `toArray(data)` | `int[]` / `long[]`                   | 十分な長さなら `data` へコピーし、不足時は新規配列を返す |
| `iterator()`    | `PrimitiveIterator.OfInt` / `OfLong` | 先頭から末尾へ走査                        |
| `toString()`    | `String`                             | 論理順を空白区切りで返す。空なら空文字列             |

## 利用例

```java
IntArrayDeque deque = new IntArrayDeque(5);
System.out.println(deque.capacity()); // 8

deque.addLast(2);
deque.addFirst(1);
deque.addLast(3);                     // 1 2 3

System.out.println(deque.get(-1));    // 3
int first = deque.pollFirst();        // 1、残りは 2 3
deque.set(-1, 10);                    // 2 10
```

## 注意事項

- 初期容量と最大要素数が Java 配列として確保可能な問題制約で使用します。
- `peek` / `poll` は空でない状態で呼び出します。空の場合は `NoSuchElementException` を投げます。
- `get` / `set` の有効範囲は `-size() <= index < size()` です。
- `clear()` は内部配列を縮小・初期化せず、確保済み容量を再利用します。
- `toArray(data)` で `data.length > size()` の場合、`size()` 以降の領域は変更しません。
- 自動縮小には対応しません。

## パフォーマンス特性

- `addFirst`, `addLast`: 償却 O(1)、拡張時 O(n)
- `peekFirst`, `peekLast`, `pollFirst`, `pollLast`, `get`, `set`, `size`, `clear`: O(1)
- 配列・`IntArray` / `LongArray`・supplierからの構築: O(n)
- `fill`, `setAll`, `contains`, `equals`, `hashCode`, `toArray`, `iterator`, `toString`: O(n)
- `clone`: O(capacity)
- 使用メモリ: O(capacity)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                 |
|:--------------|:-----------|:-------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-12 | プリミティブ特化の可変容量循環 deque として初回実装                                      |
| **バージョン 2.0** | 2026-07-15 | `IntMutableArray` / `LongMutableArray` の実装へ変更                      |
| **バージョン 2.1** | 2026-07-15 | `toString()` を論理順の空白区切り形式へ改善                                       |
| **バージョン 3.0** | 2026-07-15 | 配列・配列インターフェース・supplierからの構築と、`head` を0へ正規化する `fill` / `setAll` を追加 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
