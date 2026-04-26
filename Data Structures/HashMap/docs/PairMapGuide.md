# Pair Map 利用ガイド

## 概要

このガイドは以下2クラスをまとめて説明します。

- `IntPairIntMap`（`(int, int) -> int`）
- `IntPairLongMap`（`(int, int) -> long`）

2つの `int` キーを `long` にパックして、`BaseLongIntMap` / `BaseLongLongMap` に委譲する構成です。

## 特徴

- `(a, b)` をそのまま受け取る API で可読性が高い
- 内部実装はベースマップへ委譲するため挙動が一貫
- `forEach` / `forEachKey` でキーを自動復元
- `merge` / `putIfAbsent` をサポート

## 依存関係

- `java.util.function.*`
	- `IntBinaryOperator`（`IntPairIntMap#merge`）
	- `LongBinaryOperator`（`IntPairLongMap#merge`）
	- `IntConsumer`, `LongConsumer`

## 主な機能（メソッド一覧）

### 1. 参照・判定系メソッド

| メソッド                               | 戻り値の型          | 説明             |
|------------------------------------|----------------|----------------|
| `get(a, b)`                        | `int` / `long` | 値取得。キー未存在時は例外。 |
| `getOrDefault(a, b, defaultValue)` | `int` / `long` | 未存在時に既定値を返す。   |
| `containsKey(a, b)`                | `boolean`      | キー存在判定。        |
| `size()`                           | `int`          | 要素数。           |
| `isEmpty()`                        | `boolean`      | 空判定。           |

### 2. 更新系メソッド

| メソッド                                      | 戻り値の型          | 説明                        |
|-------------------------------------------|----------------|---------------------------|
| `put(a, b, value)`                        | `int` / `long` | 値を設定。                     |
| `putIfAbsent(a, b, value)`                | `int` / `long` | 未存在時のみ挿入。                 |
| `add(a, b, delta)`                        | `int` / `long` | 既存値へ加算。                   |
| `increment(a, b)` / `decrement(a, b)`     | `int` / `long` | `+1` / `-1` 更新。           |
| `addOrDefault(a, b, delta, defaultValue)` | `int` / `long` | 未存在時は `defaultValue` で作成。 |
| `merge(a, b, value, op)`                  | `int` / `long` | 既存時 `op(old, value)` を適用。 |
| `remove(a, b)`                            | `boolean`      | キー削除。                     |
| `clear()`                                 | `void`         | 全削除。                      |

### 3. 走査・抽出系メソッド

| メソッド                   | 戻り値の型                  | 説明                                 |
|------------------------|------------------------|------------------------------------|
| `forEach(action)`      | `void`                 | `(a, b, value)` で全要素を走査。           |
| `forEachKey(action)`   | `void`                 | `(a, b)` で全キー走査。                   |
| `forEachValue(action)` | `void`                 | 値のみ走査。                             |
| `keys()`               | `int[][]` / `long[][]` | `[2][size]` 形式でキー集合を返す。            |
| `values()`             | `int[]` / `long[]`     | 値配列を返す。                            |
| `entries()`            | `int[][]` / `long[][]` | `[3][size]` 形式で `(a,b,value)` を返す。 |

### 4. クラス別差分

| クラス              | 値型     | `merge` の演算子型        |
|------------------|--------|----------------------|
| `IntPairIntMap`  | `int`  | `IntBinaryOperator`  |
| `IntPairLongMap` | `long` | `LongBinaryOperator` |

## 利用例

```java
public class Example {
	public static void main(String[] args) {
		IntPairIntMap dist = new IntPairIntMap(1 << 10);
		dist.put(1, 2, 5);
		dist.add(1, 2, 3); // 8
		dist.merge(1, 2, 10, Math::max); // 10
		int v = dist.getOrDefault(3, 4, -1); // -1
	}
}
```

## 注意事項

- `get(a, b)` は未存在時に例外を送出します。
- キーの順序は `(a,b)` と `(b,a)` で別物です。
- 反復順序は挿入順ではありません。

## パフォーマンス特性

- 平均時間計算量:
	- 参照・更新: $O(1)$
- 最悪時間計算量:
	- 参照・更新: $O(N)$
- 空間計算量:
	- $O(capacity)$

## バージョン情報

| バージョン番号       | 年月日        | 詳細              |
|:--------------|:-----------|:----------------|
| **バージョン 1.0** | 2026-04-27 | Pair 系2クラス初期実装。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
