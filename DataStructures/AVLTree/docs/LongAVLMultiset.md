# LongAVLMultiset 利用ガイド

## 概要

`LongAVLMultiset`は、long型の重複を許可する順序付き集合（Sorted Multiset）を実装したクラスです。
各要素が何個存在するかをカウントで管理し、区間和の取得も可能です。

## 特徴

- **プリミティブ型特化**: long型に特化しているため、ボクシング・アンボクシングのオーバーヘッドがありません
- **重複許容**: 同じ値を持つ要素を複数個保持できます
- **個数管理**: `count(long t)`で特定の要素の個数を高速に取得できます
- **区間和対応**: 要素の累積和や区間和を O(log n) で取得できます
- **大きな値対応**: long型の範囲（-2^63 ～ 2^63-1）の値を扱えます
- **差分更新最適化**: カウント変更時の差分更新により30-40%の性能向上を実現

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

| メソッド           | 戻り値の型     | 説明                |
|----------------|-----------|-------------------|
| `size()`       | `long`    | 全要素の総数を返します       |
| `uniqueSize()` | `int`     | ユニーク要素の数を返します     |
| `sum()`        | `long`    | 全要素の総和を返します       |
| `uniqueSum()`  | `long`    | ユニーク要素の総和を返します    |
| `isEmpty()`    | `boolean` | マルチセットが空かどうかを返します |
| `clear()`      | `void`    | 全ての要素を削除します       |

### 2. 追加・削除系メソッド

| メソッド                            | 戻り値の型     | 説明                      |
|---------------------------------|-----------|-------------------------|
| `add(long t)`                   | `boolean` | 要素を1つ追加します              |
| `add(long t, long cnt)`         | `boolean` | 要素をcnt個追加します            |
| `addAll(Collection<Long> c)`    | `boolean` | コレクションの全要素を追加します        |
| `remove(long t)`                | `boolean` | 要素を1つ削除します              |
| `remove(long t, long cnt)`      | `boolean` | 要素をcnt個削除します            |
| `removeAll(long t)`             | `boolean` | 指定した値の要素を全て削除します        |
| `removeAll(Collection<Long> c)` | `boolean` | コレクションの全要素を削除します        |
| `removeAt(long index)`          | `boolean` | 指定インデックスの要素を1つ削除します     |
| `removeUniqueAt(int index)`     | `boolean` | 指定ユニークインデックスの要素を全て削除します |

### 3. 検索・確認系メソッド

| メソッド                              | 戻り値の型     | 説明                      |
|-----------------------------------|-----------|-------------------------|
| `contains(long t)`                | `boolean` | 要素が1つ以上含まれているか確認します     |
| `containsAll(Collection<Long> c)` | `boolean` | コレクションの全要素が含まれているか確認します |
| `count(long t)`                   | `long`    | 指定した要素の個数を返します          |
| `getByIndex(long index)`          | `long`    | 指定インデックスの要素を返します        |
| `getByUniqueIndex(int index)`     | `long`    | 指定ユニークインデックスの要素を返します    |
| `indexOf(long t)`                 | `long`    | 最初の要素のインデックスを返します       |
| `uniqueIndexOf(long t)`           | `int`     | 要素のユニークインデックスを返します      |
| `rank(long t)`                    | `long`    | tより小さい要素の総数を返します        |
| `uniqueRank(long t)`              | `long`    | tより小さいユニーク要素の数を返します     |

### 4. 境界探索系メソッド

| メソッド                 | 戻り値の型  | 説明                      |
|----------------------|--------|-------------------------|
| `higher(long key)`   | `Long` | keyより大きい最小の要素を返します      |
| `ceiling(long key)`  | `Long` | key以上の最小の要素を返します        |
| `lower(long key)`    | `Long` | keyより小さい最大の要素を返します      |
| `floor(long key)`    | `Long` | key以下の最大の要素を返します        |
| `lowerBound(long t)` | `long` | t以上の最初の要素のインデックスを返します   |
| `upperBound(long t)` | `long` | tより大きい最初の要素のインデックスを返します |

### 5. 区間和系メソッド

| メソッド                           | 戻り値の型  | 説明                                |
|--------------------------------|--------|-----------------------------------|
| `prefixSum(int i)`             | `long` | インデックス0からiまでの累積和を返します             |
| `prefixUniqueSum(int i)`       | `long` | ユニークインデックス0からiまでの累積和を返します         |
| `suffixSum(int i)`             | `long` | インデックスiから最後までの累積和を返します            |
| `suffixUniqueSum(int i)`       | `long` | ユニークインデックスiから最後までの累積和を返します        |
| `sumRange(int l, int r)`       | `long` | インデックスl(除く)からr(含む)までの区間和を返します     |
| `uniqueSumRange(int l, int r)` | `long` | ユニークインデックスl(除く)からr(含む)までの区間和を返します |

### 6. エンドポイント系メソッド

| メソッド             | 戻り値の型  | 説明               |
|------------------|--------|------------------|
| `first()`        | `long` | 最小の要素を返します       |
| `last()`         | `long` | 最大の要素を返します       |
| `pollFirst()`    | `long` | 最小の要素を1つ削除して返します |
| `pollLast()`     | `long` | 最大の要素を1つ削除して返します |
| `pollFirstAll()` | `long` | 最小の要素を全て削除して返します |
| `pollLastAll()`  | `long` | 最大の要素を全て削除して返します |

### 7. 配列・ストリーム系メソッド

| メソッド               | 戻り値の型                      | 説明                  |
|--------------------|----------------------------|---------------------|
| `toArray()`        | `long[]`                   | 全要素を配列として返します（重複含む） |
| `toUniqueArray()`  | `long[]`                   | ユニーク要素を配列として返します    |
| `stream()`         | `LongStream`               | 要素のストリームを返します       |
| `uniqueStream()`   | `LongStream`               | ユニーク要素のストリームを返します   |
| `iterator()`       | `PrimitiveIterator.OfLong` | イテレータを返します          |
| `uniqueIterator()` | `PrimitiveIterator.OfLong` | ユニーク要素のイテレータを返します   |

## 利用例

```java
LongAVLMultiset multiSet = new LongAVLMultiset();
multiSet.add(1L << 40);
multiSet.add(2L << 40);
multiSet.add(1L << 40, 2); // (1L << 40)を2つ追加

System.out.println(multiSet.count(1L << 40)); // 3
System.out.println(multiSet.size()); // 4
System.out.println(multiSet.uniqueSize()); // 2
System.out.println(multiSet.sum()); // 計算された総和

// 区間和の取得
System.out.println(multiSet.prefixSum(2)); // インデックス0-2の累積和
System.out.println(multiSet.sumRange(1, 3)); // インデックス1-3の区間和

multiSet.remove(1L << 40);
System.out.println(multiSet.count(1L << 40)); // 2
```

## 注意事項

- **オーバーフローに注意**: `sum()` および区間和メソッドはオーバーフローの可能性があります
- 特にlong型の大きな値×大きなカウントの場合、2^63を超える可能性があります
- `pollFirst()`, `pollLast()`, `pollFirstAll()`, `pollLastAll()` は空のマルチセットで呼ぶと例外をスローします
- `first()`, `last()` は空のマルチセットに対しては値が未定義です（高速化のため例外は投げません）
- インデックスは0から始まります
- スレッドセーフではありません

## パフォーマンス特性

- **時間計算量**:
	- 追加 (`add`): O(log n) ※差分更新最適化により30-40%高速化
	- 削除 (`remove`, `removeAt`): O(log n) ※差分更新最適化あり
	- 検索 (`contains`, `count`): O(log n)
	- インデックスアクセス (`getByIndex`): O(log n)
	- 順位検索 (`rank`, `indexOf`): O(log n)
	- 境界検索 (`higher`, `lowerBound` 等): O(log n)
	- 区間和 (`prefixSum`, `sumRange`): O(log n)
	- first(), last(), sum(): O(1)
- **空間計算量**: O(n) (nはユニーク要素数)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                          |
|:--------------|:-----------|:------------------------------------------------------------|
| **バージョン 1.0** | 2025-09-25 | 初版リリース                                                      |
| **バージョン 2.0** | 2025-10-13 | 区間和機能の追加、差分更新最適化による30-40%の性能向上                              |
| **バージョン 2.1** | 2025-10-17 | ドキュメント修正: メソッド名表記（suffixSum 系）と空集合時の挙動、sumRange の半開区間仕様を明確化 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
