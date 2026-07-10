# SegmentTree

## 概要

Segment Tree（セグメント木）に関連するクラス群を提供します。  
このフォルダでは、通常版セグメント木・遅延セグメント木・一次和/二乗和特化セグメント木を、ジェネリクス版とプリミティブ版の両方で利用できます。

## 実装クラス

### [SegmentTree](../../../src/lib/ds/SegmentTree.java) / [IntSegmentTree](../../../src/lib/ds/IntSegmentTree.java) / [LongSegmentTree](../../../src/lib/ds/LongSegmentTree.java)

- **用途**：点更新 + 範囲集約クエリ向けの標準セグメント木。
- **特徴**：
	- `query` / `queryAll` / `maxRight` / `minLeft` に対応。
	- `SegmentTree` は任意型、`IntSegmentTree` / `LongSegmentTree` は高速なプリミティブ特化。
	- `add` / `multiply` / `apply(a, b)` など（プリミティブ版）を備え、点更新表現が豊富。
- **時間計算量**：
	- 構築: $O(N)$
	- 更新 / クエリ / 境界探索: $O(\log N)$
- **空間計算量**：$O(N)$

### [LazySegmentTree](../../../src/lib/ds/LazySegmentTree.java) / [IntLazySegmentTree](../../../src/lib/ds/IntLazySegmentTree.java) / [LongLazySegmentTree](../../../src/lib/ds/LongLazySegmentTree.java)

- **用途**：区間更新 + 範囲集約クエリ向けの遅延セグメント木。
- **特徴**：
	- `apply(l, r, v)` で遅延更新を適用可能。
	- `mapping` / `composition` により更新則を差し替え可能。
	- `maxRight` / `minLeft` など探索 API も利用可能。
- **時間計算量**：
	- 構築: $O(N)$
	- 区間更新 / 区間クエリ / 境界探索: $O(\log N)$
- **空間計算量**：$O(N)$

### [IntSquareSumSegmentTree](../../../src/lib/ds/IntSquareSumSegmentTree.java) / [LongSquareSumSegmentTree](../../../src/lib/ds/LongSquareSumSegmentTree.java)

- **用途**：区間の一次和・二乗和を同時に扱う専用セグメント木。
- **特徴**：
	- `query`（一次和）と `query2`（二乗和）を提供。
	- `add` / `multiply` / `set` / `apply(a, b)` を点・区間の両方に適用可能。
	- すべて法 `mod`（既定値 `998244353`）で計算。
- **時間計算量**：
	- 構築: $O(N)$
	- 各更新 / 各クエリ: $O(\log N)$
- **空間計算量**：$O(N)$

## アルゴリズム（データ構造）選択ガイド

- **標準集約（点更新中心）**:
	- `SegmentTree` / `IntSegmentTree` / `LongSegmentTree`
- **区間更新が必要**:
	- `LazySegmentTree` / `IntLazySegmentTree` / `LongLazySegmentTree`
- **一次和 + 二乗和を同時管理**:
	- `IntSquareSumSegmentTree` / `LongSquareSumSegmentTree`

## Guide

- [SegmentTree Guide](./SegmentTreeGuide.md)
- [LazySegmentTree Guide](./LazySegmentTreeGuide.md)
- [SquareSumSegmentTree Guide](./SquareSumSegmentTreeGuide.md)

## 注意事項

- すべて非再帰ボトムアップ実装です。
- 区間指定は半開区間 `[l, r)` を採用しています。
- `src/Check*.java` は検証用コードで、提出用ライブラリ本体とは用途が異なります。
