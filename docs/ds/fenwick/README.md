# Binary Indexed Tree

## 概要

バイナリインデックスツリー（BIT）、別名フェニック木（Fenwick Tree）は、配列の要素に対する累積和の計算と要素の更新を効率的に行うためのデータ構造です。
特に、要素の更新と区間和の計算が頻繁に必要とされる問題に適しています。

## 実装クラス

### 1. [LongBIT](../../../src/lib/ds/fenwick/LongBIT.java) / [IntBIT](../../../src/lib/ds/fenwick/IntBIT.java)

- **用途**: `long`型 / `int`型に特化した高速な1次元BIT（点更新・区間和）
- **主な操作**:
    - `add(int i, v)`: インデックス `i` に `v` を加算 $O (\log N)$
    - `set(int i, v)`: インデックス `i` を `v` に更新 $O (\log N)$
    - `multiply(int i, a)`: インデックス `i` を `a` 倍 $O (\log N)$
    - `apply(int i, a, b)`: `x -> a*x + b` を適用 $O (\log N)$
    - `apply(int i, v, op)`: 任意の二項演算 `op(x, v)` を適用 $O (\log N)$
    - `get(int i)`: インデックス `i` の値を取得 $O (1)$
    - `sum(int r)`: 閉区間 `[0, r]` の和を計算 $O (\log N)$
    - `sum(int l, int r)`: 閉区間 `[l, r]` の和を計算 $O (\log N)$
    - `sumAll()`: 全要素の和 $O (\log N)$
    - `lowerBound(w)`: 累積和が `w` 以上となる最小のインデックスを検索 $O (\log N)$（全要素が非負であることが前提）
    - `upperBound(w)`: 累積和が `w` より大きくなる最小のインデックスを検索 $O (\log N)$（全要素が非負であることが前提）
- **一括操作**: `fill(v)` / `setAll(init)` で全要素を $O (N)$ で再構築
- **構築**: `new LongBIT(n, init)` / `new IntBIT(n, init)` で初期値関数から構築可能
- **列挙**: `IntCollection` / `LongCollection` を実装し、`iterator()` / `intStream()` / `toArray()` / `toList()` に対応

### 2. [BIT&lt;T&gt;](../../../src/lib/ds/fenwick/BIT.java)

- **用途**: 汎用的な型 `T` と任意の二項演算を扱うBIT
- **特徴**: 演算 `op`・逆演算 `inv`・単位元 `identity` を与えて構築。逆演算を与えると区間クエリ `query(l, r)` や `set`
  にも対応可能
- **主な操作**:
    - `apply(int i, T v)`: インデックス `i` に `v` を作用 $O (\log N)$
    - `set(int i, T v)`: インデックス `i` を `v` に更新（`inv` が必要）$O (\log N)$
    - `get(int i)`: インデックス `i` の値を取得 $O (1)$
    - `query(int r)`: 閉区間 `[0, r]` の演算結果 $O (\log N)$
    - `query(int l, int r)`: 閉区間 `[l, r]` の演算結果（`inv` が必要）$O (\log N)$
- **列挙**: `Iterable<T>` を実装

### 3. [LongBIT2D](../../../src/lib/ds/fenwick/LongBIT2D.java) / [IntBIT2D](../../../src/lib/ds/fenwick/IntBIT2D.java)

- **用途**: 2次元平面上の点加算と矩形領域和を管理
- **主な操作**:
    - `add(i, j, v)`: $(i, j)$ に `v` を加算 $O (\log H \log W)$
    - `set(i, j, v)` / `multiply(i, j, a)` / `apply(i, j, ...)`: 点更新・点演算 $O (\log H \log W)$
    - `get(i, j)`: $(i, j)$ の値を取得 $O (1)$
    - `sum(i, j)`: 矩形 `[0, i] x [0, j]` の和 $O (\log H \log W)$
    - `sum(i1, j1, i2, j2)`: 矩形 `[i1, i2] x [j1, j2]` の和 $O (\log H \log W)$
- **一括操作**: `fill(v)` / `setAll(init)` で全要素を $O (HW)$ で再構築
- **構築**: `IntBIT2D` は初期値関数に `IntBinaryOperator`、`LongBIT2D` は `(int, int) -> long` を返す
  `LongBIT2D.Initializer` を使用（JDK 標準に `(int, int) -> long` が無いための専用インターフェース）

### 4. [LongRangeBIT](../../../src/lib/ds/fenwick/LongRangeBIT.java) / [IntRangeBIT](../../../src/lib/ds/fenwick/IntRangeBIT.java)

- **用途**: 区間加算と区間和の取得を共に $O (\log N)$ で行う（2本のBITを内部で管理）
- **主な操作**:
    - `add(l, r, v)`: 区間 `[l, r]` に `v` を一括加算 $O (\log N)$
    - `add(i, v)` / `set(i, v)` / `multiply(i, a)` / `apply(i, ...)`: 点更新・点演算 $O (\log N)$
    - `get(i)`: インデックス `i` の値を取得 $O (\log N)$（※点BITと異なり `O(1)` ではない）
    - `sum(r)` / `sum(l, r)` / `sumAll()`: 区間和の取得 $O (\log N)$

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

| 特性              | バイナリインデックスツリー | セグメント木 |
|-------------------|----------------------------|--------------|
| 実装の複雑さ      | シンプル                   | やや複雑     |
| メモリ使用量      | O(n)                       | O(n)         |
| 更新操作          | O(log n)                   | O(log n)     |
| 区間和計算        | O(log n)                   | O(log n)     |
| 区間最小値/最大値 | 非対応                     | 対応         |
| 区間の遅延更新    | 非対応                     | 対応         |

## 注意事項

- 本ライブラリの実装は 0-indexed での操作を基本としています。
- 区間の最小値や最大値を求める操作には対応していません（そのような操作にはセグメント木が適しています）。
- `lowerBound(w)` は「prefixSum (i) >= w」、`upperBound(w)` は「prefixSum (i) > w」を満たす最小の `i` を返し、条件を満たす位置がない場合は
  `n` を返します。
