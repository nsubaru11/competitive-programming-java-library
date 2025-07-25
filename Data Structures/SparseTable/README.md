# Sparse Table

## 概要

スパーステーブルは、静的な配列に対する区間クエリを効率的に処理するためのデータ構造です。
特に、結合法則を満たす演算（最小値、最大値、GCDなど）に対する区間クエリを高速に実行できます。
前処理に時間がかかる代わりに、クエリ処理が非常に高速であるという特徴があります。
このライブラリでは、基本的なスパーステーブルの実装を提供する予定です。

## 実装クラス

### [SparseTable](./src/SparseTable.java) (計画中)

- **用途**：静的な配列に対する区間クエリを効率的に処理するためのデータ構造
- **特徴**：
	- 結合法則を満たす演算（最小値、最大値、GCDなど）に対応予定
	- 前処理後は区間クエリをO(1)またはO(log n)で実行可能
	- 配列の値が変更されない場合に最適
- **主な操作**:
	- `query(int left, int right)`: 指定区間に対するクエリを実行
	- `get(int index)`: 指定位置の値を取得
- **時間計算量**：
	- 構築: O(n log n)
	- クエリ:
		- 冪等演算（最小値、最大値など）: O(1)
		- 非冪等演算（和、積など）: O(log n)
- **空間計算量**：O(n log n)

## アルゴリズム（データ構造）選択ガイド

スパーステーブルは以下の場合に特に適しています：

- 配列の値が変更されない（静的な配列）場合
- 区間クエリが多数実行される場合
- 結合法則を満たす演算（最小値、最大値、GCDなど）を使用する場合
- クエリの応答時間が重要な場合

一方、以下の場合は他のデータ構造を検討すべきです：

- 配列の値が頻繁に更新される場合 → セグメント木を使用
- 結合法則を満たさない演算を使用する場合 → セグメント木を使用
- メモリ使用量が制限される場合 → 平方分割法を検討

## 注意事項

- 現在、このクラスは計画段階であり、実装はまだ完了していません。
- スパーステーブルは配列の値が変更されない場合にのみ効率的です。値が変更される場合は、セグメント木などの他のデータ構造を検討してください。
- 冪等演算（a⊕a = a となる演算、例えば最小値や最大値）の場合、クエリ処理がO(1)で実行可能です。
- 非冪等演算（例えば和や積）の場合、クエリ処理はO(log n)になりますが、これは区間を2のべき乗の区間に分割する必要があるためです。