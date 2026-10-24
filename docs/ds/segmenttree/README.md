# SegmentTree

## 概要

Segment Tree（セグメント木）に関連するクラス群を提供する。

このフォルダでは、通常版セグメント木・遅延セグメント木・一次和/二乗和特化セグメント木を、ジェネリクス版とプリミティブ版の両方で利用できる。

## クラス一覧

| クラス | 用途 | Guide | ソース | Example / 検証 |
|--------|------|-------|--------|----------------|
| `LazySegmentTree` | 区間更新 + 範囲集約クエリ向けの遅延セグメント木。 | [LazySegmentTreeGuide](./LazySegmentTreeGuide.md) | [Java](../../../src/lib/ds/segmenttree/LazySegmentTree.java) | — |
| `IntLazySegmentTree` | `int`値向けの遅延セグメント木。 | [LazySegmentTreeGuide](./LazySegmentTreeGuide.md) | [Java](../../../src/lib/ds/segmenttree/IntLazySegmentTree.java) | — |
| `LongLazySegmentTree` | `long`値向けの遅延セグメント木。 | [LazySegmentTreeGuide](./LazySegmentTreeGuide.md) | [Java](../../../src/lib/ds/segmenttree/LongLazySegmentTree.java) | — |
| `SegmentTree` | 点更新 + 範囲集約クエリ向けの標準セグメント木。 | [SegmentTreeGuide](./SegmentTreeGuide.md) | [Java](../../../src/lib/ds/segmenttree/SegmentTree.java) | [Test.java](../../../test/verify/ds/segmenttree/Test.java) |
| `IntSegmentTree` | `int`値向けの点更新・範囲集約セグメント木。 | [SegmentTreeGuide](./SegmentTreeGuide.md) | [Java](../../../src/lib/ds/segmenttree/IntSegmentTree.java) | [Check2.java](../../../test/verify/ds/segmenttree/intsegmenttree/Check2.java) |
| `LongSegmentTree` | `long`値向けの点更新・範囲集約セグメント木。 | [SegmentTreeGuide](./SegmentTreeGuide.md) | [Java](../../../src/lib/ds/segmenttree/LongSegmentTree.java) | [Check1.java](../../../test/verify/ds/segmenttree/longsegmenttree/Check1.java) |
| `IntSquareSumSegmentTree` | 区間の一次和・二乗和を同時に扱う専用セグメント木。 | [SquareSumSegmentTreeGuide](./SquareSumSegmentTreeGuide.md) | [Java](../../../src/lib/ds/segmenttree/IntSquareSumSegmentTree.java) | — |
| `LongSquareSumSegmentTree` | `long`値の区間和・二乗和を同時に管理する専用セグメント木。 | [SquareSumSegmentTreeGuide](./SquareSumSegmentTreeGuide.md) | [Java](../../../src/lib/ds/segmenttree/LongSquareSumSegmentTree.java) | — |

## 実装クラス

### [SegmentTree](../../../src/lib/ds/segmenttree/SegmentTree.java) / [IntSegmentTree](../../../src/lib/ds/segmenttree/IntSegmentTree.java) / [LongSegmentTree](../../../src/lib/ds/segmenttree/LongSegmentTree.java)

- **用途**：点更新 + 範囲集約クエリ向けの標準セグメント木。
- **特徴**：
	- `query` / `queryAll` / `maxRight` / `minLeft` に対応。
	- `SegmentTree` は任意型、`IntSegmentTree` / `LongSegmentTree` は高速なプリミティブ特化。
	- `add` / `multiply` / `apply(a, b)` など（プリミティブ版）を備え、点更新表現が豊富。
- **時間計算量**：
	- 構築: $\mathcal{O}(N)$
	- 更新 / クエリ / 境界探索: $\mathcal{O}(\log N)$
- **空間計算量**：$\mathcal{O}(N)$

### [LazySegmentTree](../../../src/lib/ds/segmenttree/LazySegmentTree.java) / [IntLazySegmentTree](../../../src/lib/ds/segmenttree/IntLazySegmentTree.java) / [LongLazySegmentTree](../../../src/lib/ds/segmenttree/LongLazySegmentTree.java)

- **用途**：区間更新 + 範囲集約クエリ向けの遅延セグメント木。
- **特徴**：
	- `apply(l, r, v)` で遅延更新を適用可能。
	- `mapping` / `composition` により更新則を差し替え可能。
	- `maxRight` / `minLeft` など探索 API も利用可能。
- **時間計算量**：
	- 構築: $\mathcal{O}(N)$
	- 区間更新 / 区間クエリ / 境界探索: $\mathcal{O}(\log N)$
- **空間計算量**：$\mathcal{O}(N)$

### [IntSquareSumSegmentTree](../../../src/lib/ds/segmenttree/IntSquareSumSegmentTree.java) / [LongSquareSumSegmentTree](../../../src/lib/ds/segmenttree/LongSquareSumSegmentTree.java)

- **用途**：区間の一次和・二乗和を同時に扱う専用セグメント木。
- **特徴**：
	- `query`（一次和）と `query2`（二乗和）を提供。
	- `add` / `multiply` / `set` / `apply(a, b)` を点・区間の両方に適用可能。
	- すべて法 `mod`（既定値 `998244353`）で計算。
- **時間計算量**：
	- 構築: $\mathcal{O}(N)$
	- 各更新 / 各クエリ: $\mathcal{O}(\log N)$
- **空間計算量**：$\mathcal{O}(N)$

## アルゴリズム（データ構造）選択ガイド

- **標準集約（点更新中心）**:
	- `SegmentTree` / `IntSegmentTree` / `LongSegmentTree`
- **区間更新が必要**:
	- `LazySegmentTree` / `IntLazySegmentTree` / `LongLazySegmentTree`
- **一次和 + 二乗和を同時管理**:
	- `IntSquareSumSegmentTree` / `LongSquareSumSegmentTree`

## 注意事項

- すべて非再帰ボトムアップ実装である。
- 区間指定は半開区間 `[l, r)` を採用している。
- `src/Check*.java` は検証用コードで、提出用ライブラリ本体とは用途が異なる。
