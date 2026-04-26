# Base Map 利用ガイド

## 概要

このガイドは以下3クラスをまとめて説明します。

- `BaseIntIntMap`（`int -> int`）
- `BaseLongIntMap`（`long -> int`）
- `BaseLongLongMap`（`long -> long`）

いずれもオープンアドレス法（線形探索）を採用した高速マップで、競技プログラミングでの利用を想定しています。

## 特徴

- 墓石（tombstone）方式の削除対応
- `occupied` を使ったリサイズ判定で、墓石蓄積時の探索悪化を抑制
- `put` / `addOrDefault` / `merge` / `putIfAbsent` など更新 API が揃っている
- `forEach` / `keys` / `entries` による一括取得が可能

## 依存関係

- `java.util.NoSuchElementException`
- `java.util.function.*`
	- `IntBinaryOperator`（`BaseIntIntMap`, `BaseLongIntMap`）
	- `LongBinaryOperator`（`BaseLongLongMap`）
	- `IntConsumer`, `LongConsumer`

## 主な機能（メソッド一覧）

### 1. 参照・判定系メソッド

| メソッド                              | 戻り値の型          | 説明                          |
|-----------------------------------|----------------|-----------------------------|
| `get(key)`                        | `int` / `long` | 値取得。キー未存在時は例外。              |
| `getOrDefault(key, defaultValue)` | `int` / `long` | キー未存在時は `defaultValue` を返す。 |
| `containsKey(key)`                | `boolean`      | キー存在判定。                     |
| `size()`                          | `int`          | 要素数。                        |
| `isEmpty()`                       | `boolean`      | 空判定。                        |

### 2. 更新系メソッド

| メソッド                                     | 戻り値の型          | 説明                                      |
|------------------------------------------|----------------|-----------------------------------------|
| `put(key, value)`                        | `int` / `long` | 値を設定して設定後の値を返す。                         |
| `putIfAbsent(key, value)`                | `int` / `long` | 未存在時のみ挿入。                               |
| `add(key, delta)`                        | `int` / `long` | 既存値に加算。未存在時は `delta` で作成。               |
| `increment(key)` / `decrement(key)`      | `int` / `long` | `+1` / `-1` の加算更新。                      |
| `addOrDefault(key, delta, defaultValue)` | `int` / `long` | 未存在時は `defaultValue` で作成。               |
| `merge(key, value, op)`                  | `int` / `long` | 既存時 `op(old, value)`、未存在時は `value` で作成。 |
| `remove(key)`                            | `boolean`      | キー削除。                                   |
| `clear()`                                | `void`         | 全削除。                                    |

### 3. 走査・抽出系メソッド

| メソッド                   | 戻り値の型                  | 説明                      |
|------------------------|------------------------|-------------------------|
| `forEach(action)`      | `void`                 | 全エントリ走査。                |
| `forEachKey(action)`   | `void`                 | 全キー走査。                  |
| `forEachValue(action)` | `void`                 | 全値走査。                   |
| `keys()`               | `int[]` / `long[]`     | キー配列を返す。                |
| `values()`             | `int[]` / `long[]`     | 値配列を返す。                 |
| `entries()`            | `int[][]` / `long[][]` | `[2][size]` 形式でキーと値を返す。 |

### 4. クラス別差分

| クラス               | キー型    | 値型     | `merge` の演算子型        |
|-------------------|--------|--------|----------------------|
| `BaseIntIntMap`   | `int`  | `int`  | `IntBinaryOperator`  |
| `BaseLongIntMap`  | `long` | `int`  | `IntBinaryOperator`  |
| `BaseLongLongMap` | `long` | `long` | `LongBinaryOperator` |

## 利用例

```java
public class Example {
	public static void main(String[] args) {
		BaseLongIntMap freq = new BaseLongIntMap(1 << 10);
		freq.add(10000000000L, 1);
		freq.add(10000000000L, 3);
		freq.putIfAbsent(7L, 42);
		freq.merge(7L, 8, (oldV, addV) -> oldV + addV); // 50
	}
}
```

## 注意事項

- `get` は未存在キーで例外を投げるため、安全側に倒す場合は `getOrDefault` を使ってください。
- 反復順序はハッシュ配置順であり、挿入順ではありません。
- 線形探索なので、最悪ケースでは参照・更新ともに $O(N)$ になります。

## パフォーマンス特性

- 平均時間計算量:
	- 参照・更新: $O(1)$
- 最悪時間計算量:
	- 参照・更新: $O(N)$
- 走査:
	- `forEach` / `keys` / `entries`: $O(capacity)$
- 空間計算量:
	- $O(capacity)$

## バージョン情報

| バージョン番号       | 年月日        | 詳細              |
|:--------------|:-----------|:----------------|
| **バージョン 1.0** | 2026-04-27 | Base 系3クラス初期実装。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
