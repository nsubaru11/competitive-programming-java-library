# Pair Map 利用ガイド

## 概要

このガイドは以下2クラスをまとめて説明します。

- `IntPairIntMap`（`(int, int) -> int`）
- `IntPairLongMap`（`(int, int) -> long`）

2つの `int` キーを `long` にパックして、`LongIntMap` / `LongLongMap` に委譲する構成です。

## 特徴

- `(a, b)` をそのまま受け取る API で可読性が高い
- 内部実装は整数型マップへ委譲するため挙動が一貫
- 未存在キー用の `defaultValue` を設定・変更可能
- `forEach` / `forEachKey` でキーを自動復元
- `merge` / `putIfAbsent` をサポート

## 依存関係

- `java.util.function.*`
	- `IntBinaryOperator`（`IntPairIntMap#merge`）
	- `LongBinaryOperator`（`IntPairLongMap#merge`）
	- `IntConsumer`, `LongConsumer`
- `lib.util.function.*`
	- `IntBinaryConsumer`, `IntTernaryConsumer`, `IntBinaryLongConsumer`
	- `IntBinaryToLongFunction`

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| クラス                             | コンストラクタ                                  | 説明                                           |
|------------------------------------|-------------------------------------------------|------------------------------------------------|
| `IntPairIntMap` / `IntPairLongMap` | `IntPair*Map()`                                 | 初期想定要素数1024、`defaultValue`は0。        |
| `IntPairIntMap` / `IntPairLongMap` | `IntPair*Map(expectedSize)`                     | 初期想定要素数を指定、`defaultValue`は0。      |
| `IntPairIntMap`                    | `IntPairIntMap(expectedSize, defaultValue)`     | 初期想定要素数と未存在時の`int`既定値を指定。  |
| `IntPairLongMap`                   | `IntPairLongMap(expectedSize, defaultValue)`    | 初期想定要素数と未存在時の`long`既定値を指定。 |

### 2. 参照・判定系メソッド

| メソッド                           | 戻り値の型     | 説明                                                   |
|------------------------------------|----------------|--------------------------------------------------------|
| `getDefaultValue()`                | `int` / `long` | 現在の未存在時の既定値を返す。                         |
| `setDefaultValue(defaultValue)`    | `void`         | 未存在時の既定値を変更する。                           |
| `get(a, b)`                        | `int` / `long` | 値取得。キー未存在時は設定済みの`defaultValue`を返す。 |
| `getOrDefault(a, b, defaultValue)` | `int` / `long` | 未存在時に既定値を返す。                               |
| `containsKey(a, b)`                | `boolean`      | キー存在判定。                                         |
| `size()`                           | `int`          | 要素数。                                               |
| `isEmpty()`                        | `boolean`      | 空判定。                                               |

### 3. 更新系メソッド

| メソッド                                 | 戻り値の型     | 説明                                                                           |
|------------------------------------------|----------------|--------------------------------------------------------------------------------|
| `put(a, b, value)`                       | `int` / `long` | 値を設定。                                                                     |
| `putIfAbsent(a, b, value)`               | `int` / `long` | 未存在時のみ挿入。                                                             |
| `computeIfAbsent(a, b, op)`              | `int` / `long` | 未存在時だけ2成分のキーへ `op` を適用して挿入。                                |
| `mergeMin(a, b, value)`                  | `int` / `long` | 既存値との最小値を格納。未存在時は `value` を格納。                            |
| `mergeMax(a, b, value)`                  | `int` / `long` | 既存値との最大値を格納。未存在時は `value` を格納。                            |
| `add(a, b, delta)`                       | `int` / `long` | 既存値へ加算。未存在時は `defaultValue + delta` で作成。                       |
| `increment(a, b)` / `decrement(a, b)`    | `int` / `long` | 既存値へ`+1` / `-1`。未存在時は`defaultValue + 1` / `defaultValue - 1`で作成。 |
| `addOrDefault(a, b, delta, absentValue)` | `int` / `long` | 未存在時は `absentValue` で作成。                                              |
| `merge(a, b, value, op)`                 | `int` / `long` | 既存時 `op(old, value)` を適用。                                               |
| `remove(a, b)`                           | `boolean`      | キー削除。                                                                     |
| `clear()`                                | `void`         | 全削除。                                                                       |

### 4. 走査・抽出系メソッド

| メソッド                              | 戻り値の型             | 説明                                      |
|---------------------------------------|------------------------|-------------------------------------------|
| `forEach(action)`                     | `void`                 | `(a, b, value)` で全要素を走査。          |
| `forEachKey(action)`                  | `void`                 | `(a, b)` で全キー走査。                   |
| `forEachValue(action)`                | `void`                 | 値のみ走査。                              |
| `reduce(identity, accumulator)`       | `long`                 | `(a, b, value)` を使って集約。            |
| `reduceKeys(identity, accumulator)`   | `long`                 | `(a, b)` のキーのみを集約。               |
| `reduceValues(identity, accumulator)` | `long`                 | 値のみを集約。                            |
| `keys()`                              | `int[][]`              | `[2][size]` 形式でキー集合を返す。        |
| `values()`                            | `int[]` / `long[]`     | 値配列を返す。                            |
| `entries()`                           | `int[][]` / `long[][]` | `[3][size]` 形式で `(a,b,value)` を返す。 |

### 5. クラス別差分

| クラス           | 値型   | `computeIfAbsent` の関数型 | `forEach` の型          | `merge` の演算子型   |
|------------------|--------|----------------------------|-------------------------|----------------------|
| `IntPairIntMap`  | `int`  | `IntBinaryOperator`        | `IntTernaryConsumer`    | `IntBinaryOperator`  |
| `IntPairLongMap` | `long` | `IntBinaryToLongFunction`  | `IntBinaryLongConsumer` | `LongBinaryOperator` |

`reduce` / `reduceKeys` は各クラス内の `EntryToLongAccumulator` / `KeysToLongAccumulator` を使います。いずれも第1引数は現在の累積値です。

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

- `get(a, b)` は未存在時に設定済みの `defaultValue` を返します。`defaultValue` は `setDefaultValue` で構築後も変更できます。
- `setDefaultValue` は既存エントリの値を変更せず、未存在キーの取得と今後の `add` / `increment` / `decrement` に適用されます。
- `getOrDefault(a, b, defaultValue)` は呼び出し単位の既定値を返します。
- `add(a, b, delta)` は未存在時に `defaultValue` から加算します。明示的な初期格納値には `addOrDefault` を使います。
- `expectedSize` は内部配列長ではなく、リサイズせずに保持したい初期想定要素数です。
- キーの順序は `(a,b)` と `(b,a)` で別物です。
- 反復順序は挿入順ではありません。

## パフォーマンス特性

- 平均時間計算量:
	- 参照・更新: $\mathcal{O}(1)$
- 最悪時間計算量:
	- 参照・更新: $\mathcal{O}(N)$
- 走査・抽出:
	- `forEach` / `keys` / `entries`: $\mathcal{O}(capacity)$
- `keys()` / `entries()` は委譲先の中間配列を生成せず、最終的な戻り値だけを割り当てます。
- 空間計算量:
	- $\mathcal{O}(capacity)$

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                                                         |
|:-------------------|:-----------|:-----------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-27 | Pair 系2クラス初期実装。                                                                                                     |
| **バージョン 2.0** | 2026-05-10 | `reduce` / `reduceKeys` / `reduceValues` と対応 Accumulator API を追加。その他軽微な実装調整。                               |
| **バージョン 3.0** | 2026-08-02 | 委譲先を `LongIntMap` / `LongLongMap` に変更し、未存在キーの `get` が0を返す仕様に対応。                                     |
| **バージョン 4.0** | 2026-08-03 | コンストラクタと変更可能な `defaultValue` を追加し、抽出時の中間配列生成を削除。                                             |
| **バージョン 5.0** | 2026-08-08 | `computeIfAbsent`、`mergeMin`、`mergeMax` を追加。走査・通常変換は汎用 function 型、reduce は用途固有 Accumulator 型へ整理。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
