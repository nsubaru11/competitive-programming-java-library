# SegmentTree

## 概要

Segment Tree（セグメント木）データ構造に関連するクラス群を提供します。
このフォルダには、ジェネリック型に対応した汎用的な `SegmentTree` クラス、およびプリミティブ型に特化してパフォーマンスを向上させた
`IntSegmentTree` と `LongSegmentTree` クラスが含まれています。

## 実装クラス

### [SegmentTree](src/SegmentTree.java)

- **用途**：任意のオブジェクト型 `T` を扱う汎用的なセグメント木。
- **特徴**：
	- 集約ルールを定義する二項演算 `operator` とその単位元 `identity`
	  をコンストラクタで指定でき、範囲合計、範囲最小値、範囲最大値、累積判定 (`maxRight`/`minLeft`) まで幅広く適用できます。
	- 点更新 (`set`/`update`) は内部キューに遅延反映され、`query`/`queryAll`/境界探索呼び出し時にまとめて処理されるため、連続更新を高速化します。
	- `fill` や `setAll` を用意し、全要素の一括変更にも対応しています。
- **時間計算量**：
	- 初期化: $O(N)$
	- 点更新 (`set`/`update`): $O(\log N)$
	- 範囲クエリ (`query`/`queryAll`/`maxRight`/`minLeft`): $O(\log N)$
- **空間計算量**：$O(N)$

### [IntSegmentTree](src/IntSegmentTree.java) / [LongSegmentTree](src/LongSegmentTree.java)

- **用途**：`int` 型または `long` 型のデータに特化したセグメント木。
- **特徴**：
	- プリミティブ型を直接扱うことで、ジェネリック版の `SegmentTree` で生じるボクシング・アンボクシングのオーバーヘッドを解消し、より高いパフォーマンスを発揮します。
	- 汎用版と同等の API（`set`/`update`/`fill`/`setAll`/`query`/`queryAll`/`maxRight`/`minLeft`）を提供し、境界探索も同じ要領で利用できます。
- **時間計算量**：`SegmentTree` と同様
- **空間計算量**：`SegmentTree` と同様

## アルゴリズム（データ構造）選択ガイド

- **`SegmentTree<T>`**:
	- `Integer` や `Long` 以外のオブジェクト（例: `String`
	  、カスタムオブジェクト）をセグメント木で扱いたい場合に選択します。柔軟性が高いですが、プリミティブ型を扱う場合は特化版に比べて若干のパフォーマンスオーバーヘッドがあります。

- **`IntSegmentTree` / `LongSegmentTree`**:
	- `int` 型または `long` 型の配列に対して、計算速度が要求される問題に取り組む場合に最適です。競技プログラミングなど、パフォーマンスが重要な場面での使用を推奨します。

## 注意事項

- このライブラリの実装は、再帰を用いないボトムアップ形式のセグメント木です。
- `query(l, r)` の範囲指定は、`l` と `r` を両端に含む閉区間 `[l, r]` です。
