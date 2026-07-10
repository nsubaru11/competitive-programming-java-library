# Kruskal法 利用ガイド

## 概要

Kruskal法を用いた最小/最大全域木（MST）問題のソルバーです。  
辺の重みに基づいて最小コストの木を構築します。

## 特徴

- 最小全域木または最大全域木を求めることが可能
- 辺ベースのアルゴリズム
- 疎なグラフで効率的に動作
- AIZUのオンラインジャッジでは、Prim法と比べてわずかに速い結果

## 依存関係

- `lib.ds.UnionFind`
- 辺のソートはクラス内のプリミティブ配列向け実装を使用し、追加の標準ライブラリ依存はありません

## 主な機能

### コンストラクタ

- `Kruskal(int n, int m)` - 頂点数・辺数を指定して最小全域木ソルバーを初期化
- `Kruskal(int n, int m, boolean isMinimum)` - 最小/最大全域木ソルバーを初期化

### メソッド

- `void addEdge(int u, int v, long cost)` - グラフに辺を追加
- `long solve()` - Kruskal法を実行し、全域木の総コストを計算
- `int[] solvePath()` - 全域木を構成する、追加順の辺インデックスを返す

### メソッド詳細

#### コンストラクタ

- `Kruskal(int n, int m)`
	- 引数: n - 頂点数、m - 追加する辺数
	- 説明: 最小全域木を求めるソルバーを初期化します

- `Kruskal(int n, int m, boolean isMinimum)`
	- 引数:
		- n - 頂点数（0からn-1までの頂点番号が使用される）
		- m - 追加する辺数
		- isMinimum - trueの場合は最小全域木、falseの場合は最大全域木を求めます
	- 説明: 最小/最大全域木を求めるソルバーを初期化します

#### addEdge

- `void addEdge(int u, int v, long cost)`
	- 引数:
		- u - 辺の始点（0からv-1までの値）
		- v - 辺の終点（0からv-1までの値）
		- cost - 辺の重み
	- 説明: グラフに辺を追加します

#### solve

- `long solve()`
	- 戻り値: 全域木の総コスト、または連結グラフでない場合は-1
	- 説明: Kruskal法を実行し、全域木の総コストを計算します

## 利用例

```java
// 最小全域木を求める例
Kruskal k = new Kruskal(5);
k.addEdge(0, 1, 3);
k.addEdge(0, 4, 1);
k.addEdge(1, 4, 4);
k.addEdge(2, 3, 2);
k.addEdge(3, 4, 7);
k.addEdge(1, 2, 5);
k.addEdge(2, 4, 6);
k.addEdge(3, 1, 2);
long result = k.solve();
System.out.println(result); // 最小全域木の総コストを出力
```

## 注意事項

- 頂点番号は0からv-1までの範囲で指定する必要があります
- グラフが連結でない場合は-1が返されます
- 負のコストを持つ辺も処理可能ですが、負の閉路がある場合は正しい結果が得られない可能性があります
- `lib.ds.UnionFind`を再利用するため、提出用バンドラはこの依存も推移的に展開します

## パフォーマンス特性

- 時間計算量: O(|E|log|E|)
- 空間計算量: O(|V| + |E|)
- 疎なグラフ（|E| ≈ |V|）で効率的に動作します
- [AIZUOnlineJudge](https://onlinejudge.u-aizu.ac.jp/courses/library/5/GRL/2/GRL_2_A)では、Prim法と比べてわずかに速い結果が出ています

## バージョン情報

- 初期バージョン: 最小/最大全域木の計算機能を実装
