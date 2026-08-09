# Triple Map 利用ガイド

## 概要

このガイドは以下2クラスをまとめて説明します。

- `IntTripleIntMap`（`(int, int, int) -> int`）
- `IntTripleLongMap`（`(int, int, int) -> long`）

3つの `int` キーをオフセット方式で符号付き21bitずつ`long`にパックして、整数型マップに委譲する構成です。各キー成分は`-2^20 = -1,048,576`以上`2^20 - 1 = 1,048,575`以下である必要があります。

## 特徴

- 3要素キーを直接指定できる API
- 各キー成分に `2^20 = 1,048,576` を加えて符号なし21bitへ変換し、高速に探索
- 未存在キー用の `defaultValue` を設定・変更可能
- `forEach` / `forEachKey` でキー成分を復元して受け取れる
- `merge` / `putIfAbsent` / `addOrDefault` をサポート

## 依存関係

- `java.util.function.*`
	- `IntBinaryOperator`（`IntTripleIntMap#merge`）
	- `LongBinaryOperator`（`IntTripleLongMap#merge`）
	- `IntConsumer`, `LongConsumer`
- `lib.util.function.*`
	- `IntTernaryConsumer`, `IntQuaternaryConsumer`, `IntTernaryLongConsumer`
	- `IntTernaryOperator`, `IntTernaryToLongFunction`

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| クラス                                 | コンストラクタ                                    | 説明                                           |
|----------------------------------------|---------------------------------------------------|------------------------------------------------|
| `IntTripleIntMap` / `IntTripleLongMap` | `IntTriple*Map()`                                 | 初期想定要素数1024、`defaultValue`は0。        |
| `IntTripleIntMap` / `IntTripleLongMap` | `IntTriple*Map(expectedSize)`                     | 初期想定要素数を指定、`defaultValue`は0。      |
| `IntTripleIntMap`                      | `IntTripleIntMap(expectedSize, defaultValue)`     | 初期想定要素数と未存在時の`int`既定値を指定。  |
| `IntTripleLongMap`                     | `IntTripleLongMap(expectedSize, defaultValue)`    | 初期想定要素数と未存在時の`long`既定値を指定。 |

### 2. 参照・判定系メソッド

| メソッド                              | 戻り値の型     | 説明                                               |
|---------------------------------------|----------------|----------------------------------------------------|
| `getDefaultValue()`                   | `int` / `long` | 現在の未存在時の既定値を返す。                     |
| `setDefaultValue(defaultValue)`       | `void`         | 未存在時の既定値を変更する。                       |
| `get(a, b, c)`                        | `int` / `long` | 値取得。未存在時は設定済みの`defaultValue`を返す。 |
| `getOrDefault(a, b, c, defaultValue)` | `int` / `long` | 未存在時に既定値を返す。                           |
| `containsKey(a, b, c)`                | `boolean`      | キー存在判定。                                     |
| `size()`                              | `int`          | 要素数。                                           |
| `isEmpty()`                           | `boolean`      | 空判定。                                           |

### 3. 更新系メソッド

| メソッド                                    | 戻り値の型     | 説明                                                                           |
|---------------------------------------------|----------------|--------------------------------------------------------------------------------|
| `put(a, b, c, value)`                       | `int` / `long` | 値を設定。                                                                     |
| `putIfAbsent(a, b, c, value)`               | `int` / `long` | 未存在時のみ挿入。                                                             |
| `computeIfAbsent(a, b, c, op)`              | `int` / `long` | 未存在時だけ3成分のキーへ `op` を適用して挿入。                                |
| `mergeMin(a, b, c, value)`                  | `int` / `long` | 既存値との最小値を格納。未存在時は `value` を格納。                            |
| `mergeMax(a, b, c, value)`                  | `int` / `long` | 既存値との最大値を格納。未存在時は `value` を格納。                            |
| `add(a, b, c, delta)`                       | `int` / `long` | 既存値へ加算。未存在時は `defaultValue + delta` で作成。                       |
| `increment(a, b, c)` / `decrement(a, b, c)` | `int` / `long` | 既存値へ`+1` / `-1`。未存在時は`defaultValue + 1` / `defaultValue - 1`で作成。 |
| `addOrDefault(a, b, c, delta, absentValue)` | `int` / `long` | 未存在時は `absentValue` で作成。                                              |
| `merge(a, b, c, value, op)`                 | `int` / `long` | 既存時 `op(old, value)` を適用。                                               |
| `remove(a, b, c)`                           | `boolean`      | キー削除。                                                                     |
| `clear()`                                   | `void`         | 全削除。                                                                       |

### 4. 走査・抽出系メソッド

| メソッド                              | 戻り値の型             | 説明                                        |
|---------------------------------------|------------------------|---------------------------------------------|
| `forEach(action)`                     | `void`                 | `(a, b, c, value)` で全要素走査。           |
| `forEachKey(action)`                  | `void`                 | `(a, b, c)` で全キー走査。                  |
| `forEachValue(action)`                | `void`                 | 値のみ走査。                                |
| `reduce(identity, accumulator)`       | `long`                 | `(a, b, c, value)` を使って集約。           |
| `reduceKeys(identity, accumulator)`   | `long`                 | `(a, b, c)` のキーのみを集約。              |
| `reduceValues(identity, accumulator)` | `long`                 | 値のみを集約。                              |
| `keys()`                              | `int[][]`              | `[3][size]` 形式でキー集合を返す。          |
| `values()`                            | `int[]` / `long[]`     | 値配列を返す。                              |
| `entries()`                           | `int[][]` / `long[][]` | `[4][size]` 形式で `(a,b,c,value)` を返す。 |

### 5. クラス別差分

| クラス             | 値型   | `computeIfAbsent` の関数型 | `forEach` の型           | `merge` の演算子型   |
|--------------------|--------|----------------------------|--------------------------|----------------------|
| `IntTripleIntMap`  | `int`  | `IntTernaryOperator`       | `IntQuaternaryConsumer`  | `IntBinaryOperator`  |
| `IntTripleLongMap` | `long` | `IntTernaryToLongFunction` | `IntTernaryLongConsumer` | `LongBinaryOperator` |

`reduce` / `reduceKeys` は各クラス内の `EntryToLongAccumulator` / `KeysToLongAccumulator` を使います。いずれも第1引数は現在の累積値です。

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

- 各キー成分の有効範囲は`-2^20 = -1,048,576`以上`2^20 - 1 = 1,048,575`以下です。
- パック時は各成分に`2^20 = 1,048,576`を加え、復元時に同じ値を減算します。
- 範囲外の値はオフセット変換後の下位21bitだけが保持されるため、異なるキーと衝突する可能性があります。
- `get(a, b, c)` は未存在時に設定済みの `defaultValue` を返します。`defaultValue` は `setDefaultValue` で構築後も変更できます。
- `setDefaultValue` は既存エントリの値を変更せず、未存在キーの取得と今後の `add` / `increment` / `decrement` に適用されます。
- `getOrDefault(a, b, c, defaultValue)` は呼び出し単位の既定値を返します。
- `add(a, b, c, delta)` は未存在時に `defaultValue` から加算します。明示的な初期格納値には `addOrDefault` を使います。
- `expectedSize` は内部配列長ではなく、リサイズせずに保持したい初期想定要素数です。
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

| バージョン番号     | 年月日     | 詳細                                                                                                                                           |
|:-------------------|:-----------|:-----------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-27 | Triple 系2クラス初期実装。                                                                                                                     |
| **バージョン 2.0** | 2026-05-10 | `reduce` / `reduceKeys` / `reduceValues` と対応 Accumulator API を追加し、`final` 修飾子の付与やキー分解コードの統一など軽微な実装調整を実施。 |
| **バージョン 3.0** | 2026-08-02 | 委譲先を `LongIntMap` / `LongLongMap` に変更し、未存在キーの `get` が0を返す仕様に対応。                                                       |
| **バージョン 4.0** | 2026-08-03 | 符号付き21bitのオフセットパック、変更可能な `defaultValue`、抽出時の中間配列削除に対応。                                                       |
| **バージョン 5.0** | 2026-08-08 | `computeIfAbsent`、`mergeMin`、`mergeMax` を追加。走査・通常変換は汎用 function 型、reduce は用途固有 Accumulator 型へ整理。                   |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
