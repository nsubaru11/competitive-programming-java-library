# SparseTable 利用ガイド

> Sparse Table は、値を変更しない配列に対して、冪等な区間演算を高速に行うデータ構造である。

[← README: Sparse Table](./README.md) · ソース: [SparseTable.java](../../../src/lib/ds/sparsetable/SparseTable.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

長さが `2^k` の区間演算結果を前計算し、クエリ区間を覆う重なった2区間を使って `O(1)` で答える。

## 特徴

- 構築は `O(N log N)`、クエリは `O(1)`。
- int / long / ジェネリクス版を提供する。
- 入力配列の要素列をコピーするため、構築後に元配列の要素を別の値へ置き換えてもクエリ結果は変わらない。
- 通常の Sparse Table のクエリには、演算の結合則と冪等性が必要である。

## 依存関係

- `lib.math.MathUtils`（前処理で `floorLog2` を使用）
- ジェネリクス版: `java.util.function.BinaryOperator`
- int 版: `java.util.function.IntBinaryOperator`
- long 版: `java.util.function.LongBinaryOperator`

## API

### 構築

| クラス            | コンストラクタ                                              | 説明                 |
|-------------------|-------------------------------------------------------------|----------------------|
| `SparseTable<T>`  | `SparseTable(T[] data, BinaryOperator<T> operator)`         | ジェネリクス版を構築 |
| `IntSparseTable`  | `IntSparseTable(int[] data, IntBinaryOperator operator)`    | int 特化版を構築     |
| `LongSparseTable` | `LongSparseTable(long[] data, LongBinaryOperator operator)` | long 特化版を構築    |

### 区間クエリ

| クラス            | メソッド              | 戻り値 | 説明                          |
|-------------------|-----------------------|--------|-------------------------------|
| `SparseTable<T>`  | `query(int l, int r)` | `T`    | `[l, r)` に演算を適用した結果 |
| `IntSparseTable`  | `query(int l, int r)` | `int`  | `[l, r)` に演算を適用した結果 |
| `LongSparseTable` | `query(int l, int r)` | `long` | `[l, r)` に演算を適用した結果 |

## 利用例

準備中

## 制約・注意事項

- `query(l, r)` は `0 <= l < r <= N` を満たす非空半開区間を指定すること。空区間の単位元は定義していない。
- 演算は結合的かつ冪等である必要がある。和や積など冪等でない演算には使えない。
- ジェネリクス版は要素オブジェクト自体を複製しない。可変オブジェクトを使う場合、演算結果に影響する内部状態を構築後に変更しないこと。
- 配列の値を更新する用途には向かない。更新が必要な場合はセグメント木を検討すること。
- 結合則のみを満たす演算に対する `O(1)` クエリには Disjoint Sparse Table が適している。

## 性能特性

- 構築: `O(N log N)` 時間・空間
- 区間クエリ: `O(1)`
- int / long 版はプリミティブ配列を使い、ボクシングを行わない。

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                  |
|--------------------|------------|-------------------------------------------------------|
| **バージョン 1.0** | 2026-10-05 | ジェネリクス版、int 版、long 版と半開区間クエリを追加 |
| **バージョン 1.1** | 2026-10-06 | `logTable` を前計算し、クエリ時の段数取得に利用       |
