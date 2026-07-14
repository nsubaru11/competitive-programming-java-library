# IntArray / LongArray 利用ガイド

## 概要

[`IntArray`](../../../src/lib/ds/arrays/IntArray.java) と [`LongArray`](../../../src/lib/ds/arrays/LongArray.java) は、添字アクセス可能なプリミティブ配列の読み取り用インターフェースです。アルゴリズム側が具体的な格納形式に依存せず、複数の配列実装を同じ API で扱うために使用します。

## 特徴

- `int` / `long` をプリミティブのまま取得できる
- `PrimitiveIterator` とプリミティブ Stream に対応
- `ORDERED`, `SIZED`, `SUBSIZED`, `NONNULL` の特性を持つ `Spliterator` を提供
- `PrefixSum`, `CircularArray`, `ArrayDeque`, `CompressedArray` の共通引数として利用可能
- 読み取り専用 API のため、アルゴリズムが不要な更新操作へ依存しない

## 依存関係

- `java.util.PrimitiveIterator`
- `java.util.Spliterator`
- `java.util.Spliterators`
- [`lib.ds.IntCollection`](../../../src/lib/ds/IntCollection.java)
- [`lib.ds.LongCollection`](../../../src/lib/ds/LongCollection.java)

## 主な機能（メソッド一覧）

### 1. 配列アクセス

| メソッド                   | 戻り値の型                                | 説明                    |
|------------------------|--------------------------------------|-----------------------|
| `IntArray.get(int i)`  | `int`                                | 論理添字 `i` の値を返す        |
| `LongArray.get(int i)` | `long`                               | 論理添字 `i` の値を返す        |
| `size()`               | `int`                                | 要素数を返す                |
| `iterator()`           | `PrimitiveIterator.OfInt` / `OfLong` | 論理順に走査する iterator を返す |
| `spliterator()`        | `Spliterator.OfInt` / `OfLong`       | 順序付き spliterator を返す  |

### 2. Collection 由来の既定メソッド

| メソッド                                         | 戻り値の型                          | 説明                 |
|----------------------------------------------|--------------------------------|--------------------|
| `isEmpty()`                                  | `boolean`                      | `size() == 0` か判定  |
| `contains(value)`                            | `boolean`                      | 値が含まれるか線形探索        |
| `forEachInt(action)` / `forEachLong(action)` | `void`                         | 全要素へ処理を適用          |
| `intStream()` / `longStream()`               | `IntStream` / `LongStream`     | プリミティブ Stream を返す  |
| `toList()`                                   | `List<Integer>` / `List<Long>` | ボックス化した不変 List を返す |
| `toArray()`                                  | `int[]` / `long[]`             | 論理順のプリミティブ配列を返す    |

## 利用例

```java
static long sum(final IntArray a) {
	long ans = 0;
	for (final PrimitiveIterator.OfInt it = a.iterator(); it.hasNext(); ) {
		ans += it.nextInt();
	}
	return ans;
}

IntArray a = new IntCircularArray(4, i -> i + 1);
System.out.println(sum(a)); // 10
```

## 注意事項

- `get(i)` は `0 <= i < size()` の有効な論理添字で呼び出します。
- `IntArray` / `LongArray` 自体は格納形式や可変性を規定しません。
- 更新が必要な共通引数には `IntMutableArray` / `LongMutableArray` を使用します。
- `toList()` はボックス化を伴うため、性能重視のループでは `iterator()` またはプリミティブ Stream を使用します。

## パフォーマンス特性

- 現在の `lib.ds.arrays` 実装では `get` と `size` は O(1)
- `contains`, `forEachInt` / `forEachLong`, `toList`, `toArray` は O(n)
- `toList` と `toArray` は O(n) の追加メモリを使用

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                               |
|:--------------|:-----------|:---------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-07-12 | 配列データ構造の共通インターフェースとして初回実装                                                        |
| **バージョン 2.0** | 2026-07-15 | `IntCollection` / `LongCollection` を継承する読み取り専用 API へ分離し、順序付き `spliterator()` を追加 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
