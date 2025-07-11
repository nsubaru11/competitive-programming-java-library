# CombSort

## 概要

`CombSort`クラスは、整数の配列を昇順にソートするためのコムソート（CombSort）アルゴリズムの実装を提供します。  
コムソートはバブルソートを改良したものであり、要素比較の間隔（gap）を徐々に小さくすることにより、大規模な配列のソート性能を向上させます。

## 特徴

- バブルソートの単純さをベースとしつつ、効率性を大幅に改善
- 配列サイズに応じ徐々にgapを縮小していくことで、高速なソート処理を実現
- 主に大規模配列や、ある程度順序が崩れた配列に有効
- 標準アルゴリズムに加え、特定の状況での高速化を意識した調整済みのアルゴリズムを提供

## メソッド詳細

### standardCombSort(int[] arr)

- 基本的なコムソートアルゴリズム。
- gapを約1.3の比率で徐々に縮小し、gapが最終的に1となるとバブルソートと同様の挙動になります。

### optimizedCombSort(int[] arr)

- コムソートの基本アルゴリズムを改善したバージョン。
- 特定のgap値（9または10）の場合にパフォーマンスが低下することがあるため、これらを11に置き換え高速化を図ります。

## CombSortアルゴリズム詳細

コムソートは、バブルソートの要素同士の交換機構を利用しつつ、ギャップ（gap）という間隔を用いて要素を比較・交換するアルゴリズムです。

基本手順:

1. 最初のgapは配列サイズであり、徐々に約1.3倍でgapを縮小していきます（この縮小比率をシュリンクファクターと呼びます）。
2. gapに基づき、各要素とgap分離れた要素を比較・交換処理します。
3. gapが1になってからはバブルソートと同じ挙動になり、完全にソートされるまで処理を継続します。

optimizedバージョンでは特定gap値（9や10）に到達した際、この範囲でのソート性能の低下を防ぐためにgap値を調整しています。

## 注意事項

- CombSortは高速で比較的単純ですが、常に最速なソートアルゴリズムであるとは限りません。
- 実際にはクイックソートやマージソートが平均的または特に巨大な配列のソートで優れた性能を持っています。
- ソートの安定性（同じ値の要素の元の順序を保持する性質）は保証されません。
- 安定性が重視される場合には、安定ソートアルゴリズム（例えばマージソート）を検討してください。

## パフォーマンス特性

- 時間計算量:
	- 平均ケース: O(n²/2^p), 一般的にはO(n log n)に近づきます
	- 最悪ケース: O(n²), 状況により効率は低下します
	- 最良ケース: O(n log n)
- 空間計算量:
	- 元の配列を除く追加空間はほぼ不要（O(1)）