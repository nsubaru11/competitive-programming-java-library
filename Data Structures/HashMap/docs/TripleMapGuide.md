# Triple Map 利用ガイド

## 概要

このガイドは以下2クラスをまとめて説明します。

- `IntTripleIntMap`（`(int, int, int) -> int`）
- `IntTripleLongMap`（`(int, int, int) -> long`）

3つの `int` キーを 21bit ずつ `long` にパックして、ベースマップに委譲する構成です。

## 特徴

- 3要素キーを直接指定できる API
- 内部でキーをパックして高速に探索
- `forEach` / `forEachKey` でキー成分を復元して受け取れる
- `merge` / `putIfAbsent` / `addOrDefault` をサポート

## 依存関係

- `java.util.function.*`
	- `IntBinaryOperator`（`IntTripleIntMap#merge`）
	- `LongBinaryOperator`（`IntTripleLongMap#merge`）
	- `IntConsumer`, `LongConsumer`

## 主な機能（メソッド一覧）

### 1. 参照・判定系メソッド

| メソッド                                  | 戻り値の型          | 説明           |
|---------------------------------------|----------------|--------------|
| `get(a, b, c)`                        | `int` / `long` | 値取得。未存在時は例外。 |
| `getOrDefault(a, b, c, defaultValue)` | `int` / `long` | 未存在時に既定値を返す。 |
| `containsKey(a, b, c)`                | `boolean`      | キー存在判定。      |
| `size()`                              | `int`          | 要素数。         |
| `isEmpty()`                           | `boolean`      | 空判定。         |

### 2. 更新系メソッド

| メソッド                                         | 戻り値の型          | 説明                        |
|----------------------------------------------|----------------|---------------------------|
| `put(a, b, c, value)`                        | `int` / `long` | 値を設定。                     |
| `putIfAbsent(a, b, c, value)`                | `int` / `long` | 未存在時のみ挿入。                 |
| `add(a, b, c, delta)`                        | `int` / `long` | 既存値へ加算。                   |
| `increment(a, b, c)` / `decrement(a, b, c)`  | `int` / `long` | `+1` / `-1` 更新。           |
| `addOrDefault(a, b, c, delta, defaultValue)` | `int` / `long` | 未存在時は `defaultValue` で作成。 |
| `merge(a, b, c, value, op)`                  | `int` / `long` | 既存時 `op(old, value)` を適用。 |
| `remove(a, b, c)`                            | `boolean`      | キー削除。                     |
| `clear()`                                    | `void`         | 全削除。                      |

### 3. 走査・抽出系メソッド

| メソッド                   | 戻り値の型                  | 説明                                   |
|------------------------|------------------------|--------------------------------------|
| `forEach(action)`      | `void`                 | `(a, b, c, value)` で全要素走査。           |
| `forEachKey(action)`   | `void`                 | `(a, b, c)` で全キー走査。                  |
| `forEachValue(action)` | `void`                 | 値のみ走査。                               |
| `keys()`               | `int[][]`              | `[3][size]` 形式でキー集合を返す。              |
| `values()`             | `int[]` / `long[]`     | 値配列を返す。                              |
| `entries()`            | `int[][]` / `long[][]` | `[4][size]` 形式で `(a,b,c,value)` を返す。 |

### 4. クラス別差分

| クラス                | 値型     | `merge` の演算子型        |
|--------------------|--------|----------------------|
| `IntTripleIntMap`  | `int`  | `IntBinaryOperator`  |
| `IntTripleLongMap` | `long` | `LongBinaryOperator` |

## 利用例

```java
public class Example {
	public static void main(String[] args) {
		IntTripleLongMap dp = new IntTripleLongMap(1 << 10);
		dp.put(1, 2, 3, 10L);
		dp.add(1, 2, 3, 7L); // 17
		dp.merge(1, 2, 3, 100L, Math::min); // 17
		long v = dp.getOrDefault(4, 5, 6, -1L); // -1
	}
}
```

## 注意事項

- キーは 21bit ごとにパックされます（`MASK = 0x1FFFFF`）。
- `get(a, b, c)` は未存在時に例外を送出します。
- 反復順序は挿入順ではありません。

## パフォーマンス特性

- 平均時間計算量:
	- 参照・更新: $O(1)$
- 最悪時間計算量:
	- 参照・更新: $O(N)$
- 空間計算量:
	- $O(capacity)$

## バージョン情報

| バージョン番号       | 年月日        | 詳細                |
|:--------------|:-----------|:------------------|
| **バージョン 1.0** | 2026-04-27 | Triple 系2クラス初期実装。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
