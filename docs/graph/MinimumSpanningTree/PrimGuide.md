# Prim 利用ガイド

## 概要

[`Prim`](../../../src/lib/graph/Prim.java)は、無向重み付きグラフの最小全域木または最大全域木を求めるソルバーです。辺を内部の隣接リストへ保持し、`LongIndexedPriorityQueue`で各頂点を木へ接続する最良costを管理します。

## 特徴

- 最小全域木と最大全域木に対応
- 無向辺を辺番号付きで保持
- 全域木の総costと、採用した辺番号列を取得可能
- 頂点ごとの候補costを`LongIndexedPriorityQueue.relax`で更新
- グラフが連結でない場合は`solve()`が`-1`を返す
- 一度計算した結果をインスタンス内へ保持

## 依存関係

- `java.util.Arrays.fill`
- [`LongIndexedPriorityQueue`](../../ds/PriorityQueue/IndexedPriorityQueueGuide.md)

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド | 説明 |
|---|---|
| `Prim(int n, int m)` | 頂点数`n`、無向辺数`m`の最小全域木ソルバーを構築 |
| `Prim(int n, int m, boolean isMinimum)` | 最小全域木または最大全域木を指定して構築 |

### 2. 辺追加・計算

| メソッド | 戻り値の型 | 説明 |
|---|---|---|
| `addEdge(int u, int v, long c)` | `void` | 無向辺を追加。辺番号は追加順の0-indexed |
| `solve()` | `long` | 全域木の総costを返す。非連結なら`-1` |
| `solvePath()` | `int[]` | 採用した辺番号を木へ追加した順に返す |

## 利用例

```java
Prim prim = new Prim(4, 5);
prim.addEdge(0, 1, 1); // edge 0
prim.addEdge(0, 2, 4); // edge 1
prim.addEdge(1, 2, 2); // edge 2
prim.addEdge(1, 3, 5); // edge 3
prim.addEdge(2, 3, 3); // edge 4

long cost = prim.solve(); // 6
int[] edges = prim.solvePath(); // {0, 2, 4}
```

最大全域木では第3引数へ`false`を指定します。

```java
Prim maximum = new Prim(n, m, false);
```

## 注意事項

- 頂点番号は`0 <= u, v < n`で指定します。
- コンストラクタの`m`は追加する無向辺数です。各辺は内部で2方向分の領域を使用します。
- `addEdge`が返さない辺番号は、呼び出し順に0から割り当てられます。
- `solve()`または`solvePath()`を初めて呼んだ時点で結果を確定します。それ以降に辺を追加しても再計算しません。
- `solvePath()`は内部配列をそのまま返します。
- 非連結グラフでは`solve()`は`-1`を返し、`solvePath()`の全要素が有効な全域木を表すとは限りません。

## パフォーマンス特性

- `addEdge`: O(1)
- `solve`: O((V + E) log V)
- `solvePath`: 未計算なら`solve`を実行し、計算済みならO(1)
- 使用メモリ: O(V + E)

## バージョン情報

| バージョン番号 | 年月日 | 詳細 |
|:---|:---|:---|
| **バージョン 1.0** | 2025-10-07 | 最小・最大全域木と採用辺取得を初回実装 |
| **バージョン 2.0** | 2026-07-18 | private優先度キューを削除し、`LongIndexedPriorityQueue`を再利用 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
