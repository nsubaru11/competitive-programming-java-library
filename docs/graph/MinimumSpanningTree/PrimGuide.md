# Prim法 利用ガイド

## 概要

Prim法を用いた最小/最大全域木（MST）問題のソルバーです。  
グラフの全頂点を最小コストで連結する木を構築します。

## 特徴

- 最小全域木または最大全域木を求めることが可能
- 頂点ベースのアルゴリズム
- 密なグラフで効率的に動作
- AIZUのオンラインジャッジでは、Kruskal法と比べてわずかに遅い結果

## 依存関係

- Java標準ライブラリ
	- java.util.List
	- java.util.ArrayList
	- java.util.PriorityQueue
	- java.util.Comparator
	- java.util.BitSet

## 主な機能

### コンストラクタ

- `Prim(int v)` - 最小全域木を求めるソルバーを初期化
- `Prim(int v, boolean isMinimum)` - 最小/最大全域木を求めるソルバーを初期化

### メソッド

- `void addEdge(int u, int v, long cost)` - グラフに無向辺を追加
- `long solve()` - Prim法を実行し、全域木の総コストを計算

### メソッド詳細

#### コンストラクタ

- `Prim(int v)`
	- 引数: v - 頂点数（0からv-1までの頂点番号が使用される）
	- 説明: 最小全域木を求めるソルバーを初期化します

- `Prim(int v, boolean isMinimum)`
	- 引数:
		- v - 頂点数（0からv-1までの頂点番号が使用される）
		- isMinimum - trueの場合は最小全域木、falseの場合は最大全域木を求めます
	- 説明: 最小/最大全域木を求めるソルバーを初期化します

#### addEdge

- `void addEdge(int u, int v, long cost)`
	- 引数:
		- u - 辺の始点（0からv-1までの値）
		- v - 辺の終点（0からv-1までの値）
		- cost - 辺の重み
	- 説明: グラフに無向辺を追加します

#### solve

- `long solve()`
	- 戻り値: 全域木の総コスト、または連結グラフでない場合は-1
	- 説明: Prim法を実行し、全域木の総コストを計算します

## 利用例

```java
// 最小全域木を求める例
Prim p = new Prim(5);
p.addEdge(0, 1, 3);
p.addEdge(0, 4, 1);
p.addEdge(1, 4, 4);
p.addEdge(2, 3, 2);
p.addEdge(3, 4, 7);
p.addEdge(1, 2, 5);
p.addEdge(2, 4, 6);
p.addEdge(3, 1, 2);
long result = p.solve();
System.out.println(result); // 最小全域木の総コストを出力
```

## 注意事項

- 頂点番号は0からv-1までの範囲で指定する必要があります
- グラフが連結でない場合は-1が返されます
- 負のコストを持つ辺も処理可能ですが、負の閉路がある場合は正しい結果が得られない可能性があります

## パフォーマンス特性

- 時間計算量: O(|E|log|V|)
- 空間計算量: O(|V| + |E|)
- 密なグラフ（|E| ≈ |V|²）で効率的に動作します
- [AIZUOnlineJudge](https://onlinejudge.u-aizu.ac.jp/courses/library/5/GRL/2/GRL_2_A)では、Kruskal法と比べてわずかに遅い結果が出ています

## バージョン情報

- 初期バージョン: 最小/最大全域木の計算機能を実装