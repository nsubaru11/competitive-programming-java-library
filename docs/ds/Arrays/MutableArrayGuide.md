# IntMutableArray / LongMutableArray 利用ガイド

## 概要

[`IntMutableArray`](../../../src/lib/ds/arrays/IntMutableArray.java) と [`LongMutableArray`](../../../src/lib/ds/arrays/LongMutableArray.java) は、`IntArray` / `LongArray` に添字更新を追加するインターフェースです。

## 特徴

- 読み取り API と更新 API を型レベルで分離
- `set` は更新前の値を返すため、別途 `get` せず差分処理が可能
- `CircularArray` と `ArrayDeque` を同じ更新可能配列として扱える
- `int` / `long` のプリミティブ特化によりボクシングを回避

## 依存関係

- [`lib.ds.arrays.IntArray`](../../../src/lib/ds/arrays/IntArray.java)
- [`lib.ds.arrays.LongArray`](../../../src/lib/ds/arrays/LongArray.java)

## 主な機能（メソッド一覧）

### 1. 更新メソッド

| メソッド                                  | 戻り値の型  | 説明                     |
|---------------------------------------|--------|------------------------|
| `IntMutableArray.set(int i, int v)`   | `int`  | 論理添字 `i` を更新し、更新前の値を返す |
| `LongMutableArray.set(int i, long v)` | `long` | 論理添字 `i` を更新し、更新前の値を返す |

### 2. 継承メソッド

`get`, `size`, `iterator`, `spliterator` と、`IntCollection` / `LongCollection` 由来の既定メソッドを利用できます。詳細は [ArrayGuide](./ArrayGuide.md) を参照してください。

## 利用例

```java
IntMutableArray a = new IntCircularArray(4, i -> i + 1);
int old = a.set(2, 10);

System.out.println(old);      // 3
System.out.println(a.get(2)); // 10
```

## 注意事項

- `set` は具体実装が定める有効な論理添字で呼び出します。
- `CircularArray` の添字は現在の回転状態を反映します。
- `ArrayDeque` は `-1` を末尾とする負の添字にも対応します。
- 配列長の変更操作はこのインターフェースには含まれません。

## パフォーマンス特性

- 現在の実装クラスでは `set` は O(1)
- `set` 自体は追加メモリを使用しない

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                  |
|:--------------|:-----------|:----------------------------------------------------|
| **バージョン 1.0** | 2026-07-15 | `IntArray` / `LongArray` から更新操作を分離するインターフェースとして初回実装 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
