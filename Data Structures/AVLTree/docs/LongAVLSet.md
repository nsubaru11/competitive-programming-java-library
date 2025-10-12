# LongAVLSet 利用ガイド

## 概要

`LongAVLSet`は、long型の重複を許さない順序付き集合（Sorted Set）を実装したクラスです。
AVL木に基づいているため、要素の追加、削除、検索が対数時間`O(log n)` で行えます。

## 特徴

- **プリミティブ型特化**: long型に特化しているため、ボクシング・アンボクシングのオーバーヘッドがありません
- **自動平衡**: 要素の追加・削除時に自動で木を平衡させ、性能を維持します
- **順序保証**: 要素は常にソートされた状態で保持されます
- **大きな値対応**: long型の範囲（-2^63 ～ 2^63-1）の値を扱えます

## 依存関係

- `java.util.Collection`
- `java.util.HashSet`
- `java.util.NoSuchElementException`
- `java.util.PrimitiveIterator`
- `java.util.Set`
- `java.util.Spliterator`
- `java.util.stream.LongStream`
- `java.lang.Math`

## 主な機能(メソッド一覧)

### 1. 状態管理系メソッド

| メソッド        | 戻り値の型     | 説明             |
|-------------|-----------|----------------|
| `size()`    | `int`     | 要素数を返します       |
| `isEmpty()` | `boolean` | セットが空かどうかを返します |
| `clear()`   | `void`    | 全ての要素を削除します    |

### 2. 追加・削除系メソッド

| メソッド                            | 戻り値の型     | 説明                |
|---------------------------------|-----------|-------------------|
| `add(long t)`                   | `boolean` | 要素を追加します（重複は無視）   |
| `addAll(Collection<Long> c)`    | `boolean` | コレクションの全要素を追加します  |
| `remove(long t)`                | `boolean` | 要素を削除します          |
| `removeAll(Collection<Long> c)` | `boolean` | コレクションの全要素を削除します  |
| `removeAt(int index)`           | `boolean` | 指定インデックスの要素を削除します |
| `pollFirst()`                   | `long`    | 最小の要素を削除して返します    |
| `pollLast()`                    | `long`    | 最大の要素を削除して返します    |

### 3. 検索・確認系メソッド

| メソッド                              | 戻り値の型     | 説明                         |
|-----------------------------------|-----------|----------------------------|
| `contains(long t)`                | `boolean` | 要素が含まれているか確認します            |
| `containsAll(Collection<Long> c)` | `boolean` | コレクションの全要素が含まれているか確認します    |
| `getByIndex(int index)`           | `long`    | 指定インデックスの要素を返します           |
| `indexOf(long t)`                 | `int`     | 要素のインデックスを返します（存在しない場合は-1） |
| `rank(long t)`                    | `int`     | 指定した値より小さい要素数を返します         |
| `first()`                         | `long`    | 最小の要素を返します                 |
| `last()`                          | `long`    | 最大の要素を返します                 |

### 4. 境界探索系メソッド

| メソッド                | 戻り値の型  | 説明                 |
|---------------------|--------|--------------------|
| `higher(long key)`  | `Long` | keyより大きい最小の要素を返します |
| `ceiling(long key)` | `Long` | key以上の最小の要素を返します   |
| `lower(long key)`   | `Long` | keyより小さい最大の要素を返します |
| `floor(long key)`   | `Long` | key以下の最大の要素を返します   |

### 5. 配列・ストリーム系メソッド

| メソッド         | 戻り値の型                      | 説明            |
|--------------|----------------------------|---------------|
| `toArray()`  | `long[]`                   | 全要素を配列として返します |
| `stream()`   | `LongStream`               | 要素のストリームを返します |
| `iterator()` | `PrimitiveIterator.OfLong` | イテレータを返します    |

## 利用例

```java
LongAVLSet set = new LongAVLSet();
set.add(1L << 40);
set.add(2L << 40);
set.add(5L << 35);

System.out.println(set.contains(1L << 40)); // true
System.out.println(set.last()); // 2199023255552
System.out.println(set.rank(1L << 40)); // 1

set.remove(1L << 40);
System.out.println(set.size()); // 2
```

## 注意事項

- 要素の重複は許可されません（追加しても無視されます）
- `first()`, `last()`, `pollFirst()`, `pollLast()` は空のセットで呼ぶと例外をスローします
- インデックスは0から始まります
- `higher`, `ceiling`, `lower`, `floor` は要素が見つからない場合nullを返します

## パフォーマンス特性

- **時間計算量**:
	- 追加 (`add`): O(log n)
	- 削除 (`remove`, `removeAt`): O(log n)
	- 検索 (`contains`): O(log n)
	- インデックスアクセス (`getByIndex`): O(log n)
	- 順位検索 (`rank`, `indexOf`): O(log n)
	- 境界検索 (`higher`, `ceiling`, `lower`, `floor`): O(log n)
	- first(), last(): O(1)
- **空間計算量**: O(n)

## バージョン情報

| バージョン番号       | 年月日        | 詳細     |
|:--------------|:-----------|:-------|
| **バージョン 1.0** | 2025-09-25 | 初版リリース |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
