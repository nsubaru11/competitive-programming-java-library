# Binary Search

## 概要

二分探索は、ソートされた配列や特定の条件を満たす値の範囲を効率的に検索するアルゴリズムです。
このライブラリでは、汎用的な二分探索の実装を提供しています。

## 実装クラス

### [BinarySearch](src/BinarySearch.java)

- **用途**: 整数範囲内で特定の条件を満たす値を効率的に検索するための汎用的な二分探索クラス
- **特徴**:
	- ユーザー定義の比較関数を使用して柔軟な探索が可能
	- 整数型 (int) と長整数型 (long) の両方をサポート
	- 3種類の探索方法を提供
	- 比較関数 `CompareFunction` は `int compare(long index)` を採用
	- 高速化のため、引数検証や専用例外クラスを省略した軽量実装

### [ArrayBinarySearch](src/ArrayBinarySearch.java)

- **用途**: ソート済み配列に対する効率的な二分探索を提供
- **特徴**:
	- 複数のデータ型 (int[], long[], double[], Comparable<T>[]) に対応
	- 探索範囲の指定が可能
	- 重複値がある場合、`lowerBoundSearch` は一致する左端インデックス、`upperBoundSearch` は一致する右端インデックスを返却

## 注意事項

- 見つからなかった場合は、`-(挿入位置 + 1)` の形式で挿入位置を返します
- 比較関数は次の値を返す必要があります:
	- 負の値: 現在のインデックスが目標より小さい
	- 0: 現在のインデックスが目標と一致
	- 正の値: 現在のインデックスが目標より大きい
- ここでいう上限探索[UpperBound]（下限探索[LowerBound]）というのは、特定の条件を満たしている区間の最大値（最小値）を、取得するというものです。
- `BinarySearch` / `ArrayBinarySearch` ともに、明示的な入力検証 (`validateRange`) と専用 `BSException` は提供していません。

## 更新履歴

- **v3.0 (2026-04-17)**: `origin/main` との差分を反映。`BinarySearch` の `CompareFunction` を `int` 戻り値へ統一し、
  `validateRange`/`BSException` 依存を削除。`ArrayBinarySearch` も同様に軽量化された現在実装に合わせて説明を更新し、重複値時の
  lower/upper の意味を明記。
