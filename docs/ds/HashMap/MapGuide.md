# 整数型マップ利用ガイド

## 概要

このガイドは以下3クラスをまとめて説明します。

- `IntIntMap`（`int -> int`）
- `LongIntMap`（`long -> int`）
- `LongLongMap`（`long -> long`）

いずれもオープンアドレス法（線形探索）を採用した高速マップで、競技プログラミングでの利用を想定しています。クラス名は「キー型 + 値型」の順で表します。

## 特徴

- backward-shift deletion による削除対応
- 世代番号を使った `clear()` の O(1) 初期化
- `defaultValue` と `getOrDefault` による2種類の未存在時の既定値
- `put` / `addOrDefault` / `merge` / `putIfAbsent` など更新 API が揃っている
- `forEach` / `keys` / `entries` による一括取得が可能

## 依存関係

- `java.util.function.*`
	- `IntBinaryOperator`（`IntIntMap`, `LongIntMap`）
	- `LongBinaryOperator`（`LongLongMap`）
	- `IntConsumer`, `LongConsumer`

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| クラス        | コンストラクタ                               | 説明                                |
|---------------|----------------------------------------------|-------------------------------------|
| `IntIntMap`   | `IntIntMap()`                                | 初期容量1024、`defaultValue`は0。   |
| `IntIntMap`   | `IntIntMap(initialCapacity)`                 | 初期容量を指定。`defaultValue`は0。 |
| `IntIntMap`   | `IntIntMap(initialCapacity, defaultValue)`   | 初期容量と未存在時の既定値を指定。  |
| `LongIntMap`  | `LongIntMap()`                               | 初期容量1024、`defaultValue`は0。   |
| `LongIntMap`  | `LongIntMap(initialCapacity)`                | 初期容量を指定。`defaultValue`は0。 |
| `LongIntMap`  | `LongIntMap(initialCapacity, defaultValue)`  | 初期容量と未存在時の既定値を指定。  |
| `LongLongMap` | `LongLongMap()`                              | 初期容量1024、`defaultValue`は0。   |
| `LongLongMap` | `LongLongMap(initialCapacity)`               | 初期容量を指定。`defaultValue`は0。 |
| `LongLongMap` | `LongLongMap(initialCapacity, defaultValue)` | 初期容量と未存在時の既定値を指定。  |

### 2. 参照・判定系メソッド

| メソッド                          | 戻り値の型     | 説明                                   |
|-----------------------------------|----------------|----------------------------------------|
| `getDefaultValue()`               | `int` / `long` | 現在の未存在時の既定値を返す。         |
| `setDefaultValue(defaultValue)`   | `void`         | 未存在時の既定値を変更する。           |
| `get(key)`                        | `int` / `long` | 値取得。未存在時は設定済み既定値。     |
| `getOrDefault(key, defaultValue)` | `int` / `long` | キー未存在時は `defaultValue` を返す。 |
| `containsKey(key)`                | `boolean`      | キー存在判定。                         |
| `size()`                          | `int`          | 要素数。                               |
| `isEmpty()`                       | `boolean`      | 空判定。                               |

### 3. 更新系メソッド

| メソッド                                 | 戻り値の型     | 説明                                                           |
|------------------------------------------|----------------|----------------------------------------------------------------|
| `put(key, value)`                        | `int` / `long` | 値を設定して設定後の値を返す。                                 |
| `putIfAbsent(key, value)`                | `int` / `long` | 未存在時のみ挿入。                                             |
| `add(key, delta)`                        | `int` / `long` | 既存値に加算。未存在時は `defaultValue + delta` で作成。       |
| `increment(key)` / `decrement(key)`      | `int` / `long` | `defaultValue + 1` / `defaultValue - 1` を未存在時の値とする。 |
| `addOrDefault(key, delta, defaultValue)` | `int` / `long` | 未存在時は引数の `defaultValue` をそのまま格納。               |
| `merge(key, value, op)`                  | `int` / `long` | 既存時 `op(old, value)`、未存在時は `value` で作成。           |
| `remove(key)`                            | `boolean`      | キー削除。                                                     |
| `clear()`                                | `void`         | 全削除。                                                       |

### 4. 走査・抽出系メソッド

| メソッド                              | 戻り値の型             | 説明                               |
|---------------------------------------|------------------------|------------------------------------|
| `forEach(action)`                     | `void`                 | 全エントリ走査。                   |
| `forEachKey(action)`                  | `void`                 | 全キー走査。                       |
| `forEachValue(action)`                | `void`                 | 全値走査。                         |
| `reduce(identity, accumulator)`       | `long`                 | `(key, value)` を使って集約。      |
| `reduceKeys(identity, accumulator)`   | `long`                 | キーのみを集約。                   |
| `reduceValues(identity, accumulator)` | `long`                 | 値のみを集約。                     |
| `keys()`                              | `int[]` / `long[]`     | キー配列を返す。                   |
| `values()`                            | `int[]` / `long[]`     | 値配列を返す。                     |
| `entries()`                           | `int[][]` / `long[][]` | `[2][size]` 形式でキーと値を返す。 |

### 5. クラス別差分

| クラス        | キー型 | 値型   | `merge` の演算子型   |
|---------------|--------|--------|----------------------|
| `IntIntMap`   | `int`  | `int`  | `IntBinaryOperator`  |
| `LongIntMap`  | `long` | `int`  | `IntBinaryOperator`  |
| `LongLongMap` | `long` | `long` | `LongBinaryOperator` |

## 利用例

```java
public class Example {
	public static void main(String[] args) {
		LongIntMap freq = new LongIntMap(1 << 10, 0);
		freq.add(10000000000L, 1);
		freq.add(10000000000L, 3);
		freq.putIfAbsent(7L, 42);
		freq.merge(7L, 8, (oldV, addV) -> oldV + addV); // 50
		int missing = freq.get(9L); // 0
	}
}
```

## 注意事項

- `get` は未存在キーに対して設定済みの `defaultValue` を返しますが、キーを挿入しません。
- `getOrDefault` は呼び出し単位の既定値であり、設定済みの `defaultValue` は変更しません。
- `add` は未存在キーを `defaultValue + delta` で作成します。明示的な初期格納値を指定する場合は `addOrDefault` を使います。
- `containsKey` を使うと、格納値が `defaultValue` と同じ場合でも存在性を判定できます。
- 反復順序はハッシュ配置順であり、挿入順ではありません。
- backward-shift deletion は削除時に同一クラスタの要素を移動する場合があります。

## パフォーマンス特性

- 平均時間計算量:
	- 参照・更新・削除: $\mathcal{O}(1)$（通常の負荷率の場合）
- 最悪時間計算量:
	- 参照・更新・削除: $\mathcal{O}(N)$
	- リサイズ: $\mathcal{O}(capacity)$
- 走査:
	- `forEach` / `keys` / `entries`: $\mathcal{O}(capacity)$
- 空間計算量:
	- $\mathcal{O}(capacity)$

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                                     |
|:-------------------|:-----------|:---------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2026-04-27 | 整数型マップ3クラス初期実装。                                                                            |
| **バージョン 2.0** | 2026-05-10 | `forEach`/`forEachKey`/`forEachValue` の引数修飾子や配列生成まわりの軽微な実装調整を実施。               |
| **バージョン 3.0** | 2026-08-02 | 整数型マップのクラス名を変更し、`defaultValue`、`get()` の未存在時返却、backward-shift deletion を追加。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
