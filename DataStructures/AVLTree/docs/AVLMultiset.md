# AVLMultiset 利用ガイド

## 概要

`AVLMultiset<T extends Comparable<T>>`は、ジェネリック型の重複を許可する順序付き集合（SortedMultiset）を実装したクラスです。
各要素が何個存在するかをカウントで管理します。

## 特徴

- **ジェネリクス対応**: `Comparable`を実装した任意のオブジェクトを格納できます
- **カスタムコンパレータ**: コンストラクタでカスタム比較関数を指定可能です
- **重複許容**: 同じ値を持つ要素を複数個保持できます
- **個数管理**: `count(T t)`で特定の要素の個数を高速に取得できます
- **ユニーク要素操作**: 重複を除いた要素（ユニーク要素）に対する操作もサポートします

## 依存関係

- `java.lang.reflect.Array`
- `java.util.Collection`
- `java.util.Comparator`
- `java.util.HashSet`
- `java.util.Iterator`
- `java.util.NoSuchElementException`
- `java.util.Set`
- `java.util.Spliterator`
- `java.util.stream.Stream`
- `java.lang.Math`

## 主な機能(メソッド一覧)

### 1. 状態管理系メソッド

| メソッド           | 戻り値の型     | 説明                |
|----------------|-----------|-------------------|
| `size()`       | `long`    | 全要素の総数を返します       |
| `uniqueSize()` | `int`     | ユニーク要素の数を返します     |
| `isEmpty()`    | `boolean` | マルチセットが空かどうかを返します |
| `clear()`      | `void`    | 全ての要素を削除します       |

### 2. 追加・削除系メソッド

| メソッド                         | 戻り値の型     | 説明                      |
|------------------------------|-----------|-------------------------|
| `add(T t)`                   | `boolean` | 要素を1つ追加します              |
| `add(T t, long cnt)`         | `boolean` | 要素をcnt個追加します            |
| `addAll(Collection<T> c)`    | `boolean` | コレクションの全要素を追加します        |
| `remove(T t)`                | `boolean` | 要素を1つ削除します              |
| `remove(T t, long cnt)`      | `boolean` | 要素をcnt個削除します            |
| `removeAll(T t)`             | `boolean` | 指定した値の要素を全て削除します        |
| `removeAll(Collection<T> c)` | `boolean` | コレクションの全要素を削除します        |
| `removeAt(long index)`       | `boolean` | 指定インデックスの要素を1つ削除します     |
| `removeUniqueAt(int index)`  | `boolean` | 指定ユニークインデックスの要素を全て削除します |

### 3. 検索・確認系メソッド

| メソッド                           | 戻り値の型     | 説明                      |
|--------------------------------|-----------|-------------------------|
| `contains(T t)`                | `boolean` | 要素が1つ以上含まれているか確認します     |
| `containsAll(Collection<T> c)` | `boolean` | コレクションの全要素が含まれているか確認します |
| `count(T t)`                   | `long`    | 指定した要素の個数を返します          |
| `getByIndex(long index)`       | `T`       | 指定インデックスの要素を返します        |
| `getByUniqueIndex(int index)`  | `T`       | 指定ユニークインデックスの要素を返します    |
| `indexOf(T t)`                 | `long`    | 最初の要素のインデックスを返します       |
| `uniqueIndexOf(T t)`           | `int`     | 要素のユニークインデックスを返します      |
| `rank(T t)`                    | `long`    | tより小さい要素の総数を返します        |
| `uniqueRank(T t)`              | `long`    | tより小さいユニーク要素の数を返します     |

### 4. 境界探索系メソッド

| メソッド              | 戻り値の型  | 説明                      |
|-------------------|--------|-------------------------|
| `higher(T key)`   | `T`    | keyより大きい最小の要素を返します      |
| `ceiling(T key)`  | `T`    | key以上の最小の要素を返します        |
| `lower(T key)`    | `T`    | keyより小さい最大の要素を返します      |
| `floor(T key)`    | `T`    | key以下の最大の要素を返します        |
| `lowerBound(T t)` | `long` | t以上の最初の要素のインデックスを返します   |
| `upperBound(T t)` | `long` | tより大きい最初の要素のインデックスを返します |

### 5. エンドポイント系メソッド

| メソッド             | 戻り値の型 | 説明               |
|------------------|-------|------------------|
| `first()`        | `T`   | 最小の要素を返します       |
| `last()`         | `T`   | 最大の要素を返します       |
| `pollFirst()`    | `T`   | 最小の要素を1つ削除して返します |
| `pollLast()`     | `T`   | 最大の要素を1つ削除して返します |
| `pollFirstAll()` | `T`   | 最小の要素を全て削除して返します |
| `pollLastAll()`  | `T`   | 最大の要素を全て削除して返します |

### 6. 配列・ストリーム系メソッド

| メソッド               | 戻り値の型         | 説明                  |
|--------------------|---------------|---------------------|
| `toArray()`        | `T[]`         | 全要素を配列として返します（重複含む） |
| `toUniqueArray()`  | `T[]`         | ユニーク要素を配列として返します    |
| `stream()`         | `Stream<T>`   | 要素のストリームを返します       |
| `uniqueStream()`   | `Stream<T>`   | ユニーク要素のストリームを返します   |
| `iterator()`       | `Iterator<T>` | イテレータを返します          |
| `uniqueIterator()` | `Iterator<T>` | ユニーク要素のイテレータを返します   |

## 利用例

```java
// 自然順序での使用
AVLMultiset<String> multiSet = new AVLMultiset<>();
multiSet.add("apple");
multiSet.add("orange");
multiSet.add("apple", 2); // appleを2つ追加

System.out.println(multiSet); // [apple, apple, apple, orange]
System.out.println(multiSet.count("apple")); // 3
System.out.println(multiSet.size()); // 4
System.out.println(multiSet.uniqueSize()); // 2

// カスタムコンパレータでの使用
AVLMultiset<String> reverseSet = new AVLMultiset<>(Comparator.reverseOrder());
reverseSet.add("a");
reverseSet.add("c");
reverseSet.add("b");
System.out.println(reverseSet); // [c, b, a]

multiSet.remove("apple");
System.out.println(multiSet.count("apple")); // 2
```

## 注意事項

- 要素の型 `T` は `Comparable` インターフェースを実装している必要があります
- `first()`, `last()`, `pollFirst()` 等は空のマルチセットで呼ぶと null を返します
- インデックスは0から始まります
- `higher`, `ceiling`, `lower`, `floor` は要素が見つからない場合nullを返します
- スレッドセーフではありません

## パフォーマンス特性

- **時間計算量**:
	- 追加 (`add`): O(log n)
	- 削除 (`remove`, `removeAt`): O(log n)
	- 検索 (`contains`, `count`): O(log n)
	- インデックスアクセス (`getByIndex`): O(log n)
	- 順位検索 (`rank`, `indexOf`): O(log n)
	- 境界検索 (`higher`, `lowerBound` 等): O(log n)
	- first(), last(): O(1)
- **空間計算量**: O(n) (nはユニーク要素数)

## バージョン情報

| バージョン番号       | 年月日        | 詳細     |
|:--------------|:-----------|:-------|
| **バージョン 1.0** | 2025-09-25 | 初版リリース |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
