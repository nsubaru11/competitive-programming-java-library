# Binary Indexed Tree

## 概要

バイナリインデックスツリー（BIT）、別名フェニック木（Fenwick Tree）は、配列の要素に対する累積和の計算と要素の更新を効率的に行うためのデータ構造です。
特に、要素の更新と区間和の計算が頻繁に必要とされる問題に適しています。

## 実装クラス（開発中）

### [BIT](./src/BIT.java)

- **用途**: 配列の累積和を効率的に管理するデータ構造
- **特徴**:
	- 要素の更新と区間和のクエリを対数時間で処理
	- メモリ効率が良い（元の配列と同じサイズのメモリしか使用しない）
	- 実装がシンプルで理解しやすい
- **主な操作**:
	- `update(int i, int val)`: インデックスiの要素にvalを加算
	- `sum(int i)`: インデックス0からiまでの累積和を計算
	- `rangeSum(int l, int r)`: インデックスlからrまでの区間和を計算
- **時間計算量**:
	- 更新操作: O(log n)
	- 累積和計算: O(log n)
- **空間計算量**: O(n)、ここでnは配列のサイズ
- **注**: 現在の実装は不完全です

## 基本的な考え方

バイナリインデックスツリーは、各ノードが特定の範囲の要素の和を保持する木構造です。この構造は、インデックスのビット表現を利用して効率的に実装されます。

1. **更新操作（update）**:
	- インデックスiの要素を更新する場合、iに関連するすべてのノードを更新
	- 次のノードは `i + (i & -i)` で計算（最下位ビットを加算）

2. **累積和計算（sum）**:
	- インデックス0からiまでの累積和を計算
	- iから始めて、`i - (i & -i)` で前のノードに移動（最下位ビットを減算）

3. **区間和計算（rangeSum）**:
	- `sum(r) - sum(l-1)` で計算

## 応用例

バイナリインデックスツリーは以下のような問題に適用できます：

1. **区間和クエリ**:
	- 配列の特定の区間の和を効率的に計算

2. **点更新・区間和**:
	- 配列の特定の要素を更新し、区間の和を計算する操作が混在する問題

3. **転置数（Inversion Count）**:
	- 配列内の転置数（順序が逆転している要素のペアの数）を効率的に計算

4. **2次元累積和**:
	- 2次元BITを使用して、2次元グリッド上の矩形領域の和を効率的に計算

## セグメント木との比較

| 特性        | バイナリインデックスツリー | セグメント木   |
|-----------|---------------|----------|
| 実装の複雑さ    | シンプル          | やや複雑     |
| メモリ使用量    | O(n)          | O(n)     |
| 更新操作      | O(log n)      | O(log n) |
| 区間和計算     | O(log n)      | O(log n) |
| 区間最小値/最大値 | 非対応           | 対応       |
| 区間の遅延更新   | 非対応           | 対応       |

## 注意事項

- バイナリインデックスツリーは1-indexedで実装されることが多いため、0-indexedの配列を扱う場合は注意が必要です
- 区間の最小値や最大値を求める操作には対応していません（そのような操作にはセグメント木が適しています）
- 現在の実装は不完全または開発中です
- 将来的なバージョンでは、より効率的で完全な実装が提供される予定です