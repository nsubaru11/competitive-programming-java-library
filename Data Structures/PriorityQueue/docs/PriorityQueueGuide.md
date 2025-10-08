# PriorityQueue 利用ガイド

## 概要

`PriorityQueue<T>`は、競技プログラミング向けに最適化された軽量・高速なジェネリック優先度キュークラスです。
Java標準ライブラリの`java.util.PriorityQueue`から不要な機能を排除し、コア機能に特化することで、効率的な実装を行います。

## 特徴

- **OpenJDK準拠の最適化**: `siftUp`/`siftDown`操作で要素交換ではなく代入による「ずらし方式」を採用し、Java標準ライブラリと同等の性能を出す
- **柔軟な順序指定**: 昇順（最小ヒープ）・降順（最大ヒープ）の両方に対応する
- **カスタムComparator対応**: 任意の比較ロジックを指定可能
- **動的拡張**: 容量不足時に自動的に配列を2倍に拡張する
- **軽量設計**: 競技プログラミングで必要な機能のみに絞り込み、シンプルで高速

## 依存関係

- Java標準ライブラリのみ（`java.util.Arrays`, `java.util.Comparator`, `java.util.Iterator`,
	`java.util.NoSuchElementException`）
- 外部依存なし

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                                                                               | 説明                    |
|------------------------------------------------------------------------------------|-----------------------|
| `PriorityQueue()`                                                                  | デフォルトコンストラクタ（容量16、昇順） |
| `PriorityQueue(int capacity)`                                                      | 初期容量を指定（昇順）           |
| `PriorityQueue(Comparator<T> comparator)`                                          | カスタム比較器を指定（容量16）      |
| `PriorityQueue(boolean isDescendingOrder)`                                         | 降順/昇順を指定（容量16）        |
| `PriorityQueue(int capacity, Comparator<T> comparator)`                            | 容量と比較器を指定             |
| `PriorityQueue(Comparator<T> comparator, boolean isDescendingOrder)`               | 比較器と順序を指定（容量16）       |
| `PriorityQueue(int capacity, boolean isDescendingOrder)`                           | 容量と順序を指定              |
| `PriorityQueue(int capacity, Comparator<T> comparator, boolean isDescendingOrder)` | 全パラメータを指定             |

### 2. 基本操作メソッド

| メソッド              | 戻り値の型  | 説明                      |
|-------------------|--------|-------------------------|
| `push(T v)`       | `void` | 要素を追加（遅延評価）             |
| `poll()`          | `T`    | 最優先要素を取り出して削除（O(log N)） |
| `peek()`          | `T`    | 最優先要素を参照（削除しない、O(1)）    |
| `replaceTop(T v)` | `T`    | 先頭要素を置き換える（O(log N)）    |

### 3. 状態取得メソッド

| メソッド        | 戻り値の型     | 説明           |
|-------------|-----------|--------------|
| `size()`    | `int`     | 現在の要素数を取得    |
| `isEmpty()` | `boolean` | キューが空かどうかを判定 |
| `clear()`   | `void`    | 全要素を削除       |

### 4. イテレータ

| メソッド         | 戻り値の型         | 説明                        |
|--------------|---------------|---------------------------|
| `iterator()` | `Iterator<T>` | ヒープ配列順のイテレータを取得（優先度順ではない） |

## 遅延評価戦略

`PriorityQueue<T>`の最大の特徴の一つは、**遅延評価による自動最適化**です。

### アルゴリズムの選択基準

`push`操作では即座にヒープ化せず、未整列要素として蓄積します。
`poll`や`peek`などのヒープ性質を必要とする操作時に、**厳密なステップ数計算**に基づいて最適なアルゴリズムを選択します。

#### 1. heapifyのコスト

- **Floyd's algorithm**: `2N - 2 * log₂N` ステップ（N=総要素数）
- これは理論値として正確

#### 2. インクリメンタル構築のコスト

各要素を挿入する時点でのヒープサイズから個別に計算：

- **少数の場合（k ≤ 100）**: 厳密に計算
	```
	incrementalCost = Σ(i=1 to k) log₂(sortedSize + i)
	```
	各挿入時点の実際のヒープサイズの対数を合計

- **多数の場合（k > 100）**: 効率化のため近似値を使用
	```
	avgHeapSize = sortedSize + k / 2
	incrementalCost = k × log₂(avgHeapSize)
	```
	対数の平均値を利用した高速計算

#### 3. アルゴリズム選択

```
if (heapifyCost < incrementalCost) {
    heapify を実行
} else {
    逐次 siftUp を実行
}
```

固定閾値を使わず、現在のヒープ状態に応じて常に最適なアルゴリズムを選択します。

### Comparatorベースの順序制御

`PriorityQueue<T>`では、降順指定時にComparatorを反転させることで順序を制御します。

- **統一されたヒープ操作**: 内部的には常に最小ヒープとして動作
- **柔軟な比較ロジック**: カスタムComparatorと降順指定を組み合わせ可能
- **コードの簡潔性**: プリミティブ型特化版の符号反転とは異なるアプローチ

例：

```java
// 昇順（デフォルト）
Comparator<T> comp = Comparator.naturalOrder();

// 降順指定時
Comparator<T> comp = Comparator.naturalOrder().reversed();
```

この設計により、任意の比較ロジックに対して昇順・降順を簡単に切り替えられます。

## 利用例

```java
// 昇順（最小値優先）
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.push(5);
pq.push(2);
pq.push(8);
System.out.println(pq.peek());      // 2（最小値を参照）
System.out.println(pq.poll());      // 2（最小値を取り出して削除）
System.out.println(pq.size());      // 2
pq.replaceTop(10);                  // 先頭を10に置き換え
pq.clear();                         // 全削除

// 降順（最大値優先）
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(true);
maxHeap.push(5);
maxHeap.push(2);
maxHeap.push(8);
System.out.println(maxHeap.poll()); // 8（最大値を取り出し）

// カスタムComparatorの使用（文字列長で比較）
PriorityQueue<String> strPq = new PriorityQueue<>(Comparator.comparingInt(String::length));
strPq.push("apple");
strPq.push("pie");
strPq.push("banana");
System.out.println(strPq.poll());   // "pie"（最短）
```

## 注意事項

- **null要素は非対応**: null値を追加すると予期しない動作が発生します
- **空キューでのpoll/peek**: 空のキューに対して`poll()`または`peek()`を呼び出すと`NoSuchElementException`がスローされます
- **イテレータの順序**: `iterator()`で取得されるイテレータは内部配列の順序で要素を返すため、優先度順ではありません
- **型制約**: ジェネリック型`T`は`Comparable<T>`を実装している必要があります（Comparatorを明示的に指定する場合を除く）
- **オートボクシング**: プリミティブ型（int, long）を扱う場合、オートボクシングのオーバーヘッドが発生します。大量のデータを扱う場合は
	`IntPriorityQueue`または`LongPriorityQueue`の使用を推奨

## パフォーマンス特性

### 時間計算量

- **push**: O(log N)
- **poll**: O(log N)
- **peek**: O(1)
- **size/isEmpty**: O(1)
- **clear**: O(1)

### 空間計算量

- O(N): N個の要素を格納
- 初期容量は1024、容量不足時に2倍に拡張

### 最適化のポイント

- 要素の移動は`swap`ではなく代入の連鎖により実行されるため、定数倍が小さい
- 配列アクセスのみで構成され、分岐予測に優しい実装
- ジェネリクス版のため、オートボクシングが発生する点に注意（プリミティブ型特化版の使用を推奨）

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                             |
|:--------------|:-----------|:---------------------------------------------------------------|
| **バージョン 1.0** | 2025-10-04 | 初回リリース：OpenJDK準拠の最適化された実装                                      |
| **バージョン 2.0** | 2025-10-08 | 遅延評価による高速化、replaceTopメソッドの追加、Comparatorベースの降順対応、javadocコメントの統一 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新