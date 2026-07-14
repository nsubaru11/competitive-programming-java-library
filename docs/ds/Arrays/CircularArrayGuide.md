# IntCircularArray / LongCircularArray 利用ガイド

## 概要

[`IntCircularArray`](../../../src/lib/ds/arrays/IntCircularArray.java) と [`LongCircularArray`](../../../src/lib/ds/arrays/LongCircularArray.java) は、固定長配列を物理移動せず O(1) で左右へ論理回転するプリミティブ配列です。要素更新と全体和の保持にも対応します。

## 特徴

- `offset` の更新だけで左右の論理回転を実現
- 現在の論理順に対する O(1) の `get` / `set`
- 更新時に合計値を差分更新し、`sum()` を O(1) で提供
- `fill` と `setAll` による一括更新
- `IntMutableArray` / `LongMutableArray` として汎用アルゴリズムへ渡せる
- iterator、配列化、文字列化はいずれも現在の論理順を反映

## 依存関係

- `java.util.Arrays`
- `java.util.PrimitiveIterator`
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntToLongFunction`
- [`lib.ds.arrays.IntMutableArray`](../../../src/lib/ds/arrays/IntMutableArray.java)
- [`lib.ds.arrays.LongMutableArray`](../../../src/lib/ds/arrays/LongMutableArray.java)

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                                               | 戻り値の型 | 説明                      |
|----------------------------------------------------|-------|-------------------------|
| `IntCircularArray(int n, IntUnaryOperator init)`   | -     | `init(i)` で int 配列を初期化  |
| `LongCircularArray(int n, IntToLongFunction init)` | -     | `init(i)` で long 配列を初期化 |

### 2. 要素アクセス・更新

| メソッド                | 戻り値の型          | 説明                  |
|---------------------|----------------|---------------------|
| `get(int i)`        | `int` / `long` | 現在の論理順で `i` 番目を返す   |
| `set(int i, value)` | `int` / `long` | 現在の論理順で更新し、旧値を返す    |
| `fill(value)`       | `void`         | 全要素を同じ値に更新          |
| `setAll(init)`      | `void`         | 現在の論理添字を引数として全要素を更新 |
| `sum()`             | `long`         | 全要素の和を返す            |
| `size()`            | `int`          | 固定の要素数を返す           |

`size` は public final フィールドとしても公開されています。

### 3. 回転メソッド

| メソッド              | 戻り値の型  | 説明                |
|-------------------|--------|-------------------|
| `lShift()`        | `void` | 1 要素だけ左へ回転        |
| `rShift()`        | `void` | 1 要素だけ右へ回転        |
| `lShift(int n)`   | `void` | n 要素だけ左へ回転。負数は逆方向 |
| `rShift(int n)`   | `void` | n 要素だけ右へ回転。負数は逆方向 |
| `resetRotation()` | `void` | 初期の論理位置へ戻す        |

### 4. 検索・変換・反復

| メソッド              | 戻り値の型                                | 説明              |
|-------------------|--------------------------------------|-----------------|
| `contains(value)` | `boolean`                            | 値が含まれるか線形探索     |
| `toArray()`       | `int[]` / `long[]`                   | 現在の論理順を配列として返す  |
| `iterator()`      | `PrimitiveIterator.OfInt` / `OfLong` | 現在の論理順で走査       |
| `toString()`      | `String`                             | 現在の論理順を空白区切りで返す |

## 利用例

```java
IntCircularArray a = new IntCircularArray(5, i -> i + 1);

a.lShift();    // 2 3 4 5 1
a.rShift(2);   // 5 1 2 3 4
a.set(1, 10);  // 5 10 2 3 4

System.out.println(a.sum()); // 24
System.out.println(a);       // 5 10 2 3 4
```

## 注意事項

- 現在の実装は `n >= 1` の問題制約を前提とします。
- `get` / `set` は `0 <= i < size` の論理添字で呼び出します。
- `setAll(init)` の `init` には物理添字ではなく現在の論理添字が渡されます。
- `resetRotation()` は要素を並べ替えず、初期物理位置を先頭へ戻します。
- `sum()` が `long` の範囲に収まる問題制約で使用します。

## パフォーマンス特性

- `get`, `set`, `sum`, `size`, 各回転操作: O(1)
- `fill`, `setAll`, `contains`, `toArray`, `iterator`, `toString`: O(n)
- 使用メモリ: O(n)
- `toArray` と `toString` は結果用の追加メモリを使用

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                  |
|:--------------|:-----------|:----------------------------------------------------|
| **バージョン 1.0** | 2026-07-12 | 旧一次元配列実装を、固定長の論理回転・更新・合計保持を行う `CircularArray` として再編 |
| **バージョン 2.0** | 2026-07-15 | `IntMutableArray` / `LongMutableArray` の実装へ変更       |
| **バージョン 2.1** | 2026-07-15 | `toString()` を現在の論理順の空白区切り形式へ改善                     |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
