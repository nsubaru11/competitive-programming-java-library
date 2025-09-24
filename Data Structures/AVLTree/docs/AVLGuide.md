# AVL木 (AVLSet / AVLMultiset) 利用ガイド

## 1. AVLSet<T extends Comparable<T>>

### 概要

`AVLSet`は、重複を許可しない順序付き集合（Sorted Set）を実装したクラスです。AVL木に基づいているため、要素の追加、削除、検索が対数時間
`O(log n)` で行えます。

### 特徴

- **自動平衡**: 要素の追加・削除時に自動で木を平衡させ、性能を維持します。
- **順序保証**: 要素は常にソートされた状態で保持されます。
- **ジェネリクス対応**: `Comparable`を実装した任意のオブジェクトを格納できます。

### 主な機能（メソッド一覧）

#### 状態管理系

| メソッド        | 戻り値の型     | 説明              |
|:------------|:----------|:----------------|
| `size()`    | `int`     | 要素数を返します。       |
| `isEmpty()` | `boolean` | セットが空かどうかを返します。 |
| `clear()`   | `void`    | 全ての要素を削除します。    |

#### 追加・削除系

| メソッド                  | 戻り値の型     | 説明                 |
|:----------------------|:----------|:-------------------|
| `add(T t)`            | `boolean` | 要素を追加します。          |
| `remove(T t)`         | `boolean` | 要素を削除します。          |
| `removeAt(int index)` | `boolean` | 指定インデックスの要素を削除します。 |
| `pollFirst()`         | `T`       | 最小の要素を削除して返します。    |
| `pollLast()`          | `T`       | 最大の要素を削除して返します。    |

#### 検索・確認系

| メソッド                    | 戻り値の型     | 説明                  |
|:------------------------|:----------|:--------------------|
| `contains(T t)`         | `boolean` | 要素が含まれているか確認します。    |
| `first()`               | `T`       | 最小の要素を返します。         |
| `last()`                | `T`       | 最大の要素を返します。         |
| `getByIndex(int index)` | `T`       | 指定インデックスの要素を返します。   |
| `indexOf(T t)`          | `int`     | 要素のインデックス（順位）を返します。 |
| `rank(T t)`             | `int`     | 指定した値未満の要素数を返します。   |

#### 境界探索系

| メソッド             | 戻り値の型 | 説明                    |
|:-----------------|:------|:----------------------|
| `higher(T key)`  | `T`   | `key`より大きい最小の要素を返します。 |
| `ceiling(T key)` | `T`   | `key`以上の最小の要素を返します。   |
| `lower(T key)`   | `T`   | `key`より小さい最大の要素を返します。 |
| `floor(T key)`   | `T`   | `key`以下の最大の要素を返します。   |

### 利用例

```java
AVLSet<Integer> set = new AVLSet<>();
set.add(10);
set.add(20);
set.add(5);

System.out.println(set); // [5, 10, 20]
System.out.println(set.contains(10)); // true
System.out.println(set.getByIndex(1)); // 10
set.remove(10);
System.out.println(set); // [5, 20]
```

---

## 2. AVLMultiset<T extends Comparable<T>>

### 概要

`AVLMultiset`は、重複を許可する順序付き集合（Sorted Multiset）です。各要素が何個存在するかをカウントで管理します。

### 特徴

- **重複許容**: 同じ値を持つ要素を複数個保持できます。
- **個数管理**: `count(T t)`で特定の要素の個数を高速に取得できます。
- **ユニーク要素操作**: 重複を除いた要素（ユニーク要素）に対する操作もサポートします。

### 主な機能（メソッド一覧）

#### 状態管理系

| メソッド           | 戻り値の型     | 説明                 |
|:---------------|:----------|:-------------------|
| `size()`       | `long`    | 全要素の総数を返します。       |
| `uniqueSize()` | `int`     | ユニーク要素の数を返します。     |
| `isEmpty()`    | `boolean` | マルチセットが空かどうかを返します。 |
| `clear()`      | `void`    | 全ての要素を削除します。       |

#### 追加・削除系

| メソッド                        | 戻り値の型     | 説明                             |
|:----------------------------|:----------|:-------------------------------|
| `add(T t)`                  | `boolean` | 要素を1つ追加します。                    |
| `add(T t, long cnt)`        | `boolean` | 要素を`cnt`個追加します。                |
| `remove(T t)`               | `boolean` | 要素を1つ削除します。                    |
| `remove(T t, long cnt)`     | `boolean` | 要素を`cnt`個削除します。                |
| `removeAll(T t)`            | `boolean` | 指定した値の要素を全て削除します。              |
| `removeAt(long index)`      | `boolean` | 指定インデックスの要素を1つ削除します。           |
| `removeUniqueAt(int index)` | `boolean` | 指定したユニークインデックスに該当する要素を全て削除します。 |

#### 検索・確認系

| メソッド                          | 戻り値の型     | 説明                     |
|:------------------------------|:----------|:-----------------------|
| `contains(T t)`               | `boolean` | 要素が1つ以上含まれているか確認します。   |
| `count(T t)`                  | `long`    | 指定した要素の個数を返します。        |
| `getByIndex(long index)`      | `T`       | 指定インデックスの要素を返します。      |
| `getByUniqueIndex(int index)` | `T`       | 指定ユニークインデックスの要素を返します。  |
| `indexOf(T t)`                | `long`    | 最初の要素のインデックスを返します。     |
| `uniqueIndexOf(T t)`          | `int`     | 要素のユニークインデックスを返します。    |
| `rank(T t)`                   | `long`    | `t`より小さい要素の総数を返します。    |
| `uniqueRank(T t)`             | `long`    | `t`より小さいユニーク要素の数を返します。 |

### 利用例

```java
AVLMultiset<String> multiSet = new AVLMultiset<>();
multiSet.add("apple");
multiSet.add("orange");
multiSet.add("apple", 2); // appleを2つ追加

System.out.println(multiSet); // [apple, apple, apple, orange]
System.out.println(multiSet.count("apple")); // 3
multiSet.remove("apple");
System.out.println(multiSet.count("apple")); // 2
```

---

## 3. プリミティブ型特化クラス

`AVLSet`および`AVLMultiset`には、`int`型と`long`型に特化したクラスがあり、ボクシング・アンボクシングによるオーバーヘッドを回避して高速に動作します。

### 3.1. IntAVLSet

`int`型専用の`AVLSet`です。APIは`AVLSet`とほぼ同じですが、引数と戻り値が`int`型になります。

#### 利用例

```java
IntAVLSet intSet = new IntAVLSet();
intSet.add(100);
intSet.add(200);
intSet.add(100); // 重複は許可されない
System.out.println(intSet.size()); // 2
System.out.println(intSet.contains(100)); // true
```

### 3.2. LongAVLSet

`long`型専用の`AVLSet`です。APIは`AVLSet`とほぼ同じですが、引数と戻り値が`long`型になります。

#### 利用例

```java
LongAVLSet longSet = new LongAVLSet();
longSet.add(1L << 40);
longSet.add(2L << 40);
System.out.println(longSet.last()); // 2199023255552
```

### 3.3. IntAVLMultiset

`int`型専用の`AVLMultiset`です。APIは`AVLMultiset`とほぼ同じですが、引数と戻り値が`int`型になります。

#### 利用例

```java
IntAVLMultiset intMultiSet = new IntAVLMultiset();
intMultiSet.add(100);
intMultiSet.add(200, 5); // 200を5個追加
System.out.println(intMultiSet.count(200)); // 5
```

### 3.4. LongAVLMultiset

`long`型専用の`AVLMultiset`です。APIは`AVLMultiset`とほぼ同じですが、引数と戻り値が`long`型になります。

#### 利用例

```java
LongAVLMultiset longMultiSet = new LongAVLMultiset();
longMultiSet.add(1L << 40);
longMultiSet.add(2L << 40, 3);
System.out.println(longMultiSet.size()); // 4
```

## パフォーマンス特性

- **時間計算量**:
	- 追加 (`add`), 削除 (`remove`), 検索 (`contains`, `count`): **O(log n)**
	- インデックスによるアクセス (`getByIndex`): **O(log n)**
	- 順位検索 (`rank`, `indexOf`): **O(log n)**
	- first(), last(): **O(1)**
- **空間計算量**: **O(n)** (nはユニーク要素数)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                           |
|:--------------|:-----------|:---------------------------------------------|
| **バージョン 1.0** | 2025-09-25 | `AVLSet`, `AVLMultiset`およびプリミティブ特化クラスのガイド作成。 |
