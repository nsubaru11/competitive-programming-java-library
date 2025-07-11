# 最短経路 (Shortest Path) アルゴリズム

## 概要

グラフ上の頂点間の最短経路を求めるための様々なアルゴリズムを提供します。単一始点からの最短経路や全点対間の最短経路を効率的に計算するための実装が含まれています。

## 実装クラス

### [Dijkstra](./src/Dijkstra.java)

- **用途**: 非負の辺重みを持つグラフにおける単一始点最短経路問題を解くアルゴリズム
- **特徴**:
	- 優先度付きキューを使用した効率的な実装
	- 非負の辺重みを持つグラフに対してのみ正しく動作
	- 同一始点からの複数回のクエリに対応するキャッシュ機構
	- 辺の追加後はキャッシュが自動的に無効化される
- **時間計算量**: O((V + E) log V)、ここでVは頂点数、Eは辺数
- **空間計算量**: O(V + E)

### [BellmanFord](./src/BellmanFord.java)

- **用途**: 負の辺重みを持つグラフにおける単一始点最短経路問題を解くアルゴリズム
- **特徴**:
	- 負の辺重みを持つグラフでも正しく動作
	- 負閉路（負の総重みを持つサイクル）の検出が可能
	- 負閉路が存在する場合は-INFを返す
	- 同一始点からの複数回のクエリに対応するキャッシュ機構
- **時間計算量**: O(V * E)、ここでVは頂点数、Eは辺数
- **空間計算量**: O(V + E)

### [Warshallfroyd](./src/Warshallfroyd.java)

- **用途**: 全点対間の最短経路問題を解くアルゴリズム（Floyd-Warshallアルゴリズム）
- **特徴**:
	- 全ての頂点ペア間の最短経路を一度に計算
	- 負の辺重みを持つグラフでも正しく動作
	- 負閉路の検出が可能
	- 隣接行列を使用するため、大規模グラフには不向き
- **時間計算量**: O(V³)、ここでVは頂点数
- **空間計算量**: O(V²)

## 主なメソッド

### Dijkstra

| メソッド                                  | 説明                   |
|---------------------------------------|----------------------|
| `addEdge(int i, int j, long cost)`    | グラフに有向辺を追加           |
| `getShortestPathWeight(int i, int j)` | 始点iから終点jへの最短経路の重みを計算 |

### BellmanFord

| メソッド                                  | 説明                                  |
|---------------------------------------|-------------------------------------|
| `addEdge(int i, int j, long cost)`    | グラフに有向辺を追加                          |
| `getShortestPathWeight(int i, int j)` | 始点iから終点jへの最短経路の重みを計算（負閉路がある場合は-INF） |

### Warshallfroyd

| メソッド                                      | 説明                                      |
|-------------------------------------------|-----------------------------------------|
| `addEdge(int from, int to, long cost)`    | グラフに有向辺を追加                              |
| `getShortestPathWeight(int from, int to)` | 始点fromから終点toへの最短経路の重みを計算（負閉路がある場合は-INF） |
| `getAllShortestPathWeight()`              | 全点対間の最短経路の重みを含む隣接行列を返す（負閉路がある場合はnull）   |

## 使用例

```java
// グラフの構築
int v = 5; // 頂点数
int e = 10; // 辺数
int[][] edges = {
		{0, 2, 2}, // 頂点0から頂点2への重み2の辺
		{3, 1, 1}, // 頂点3から頂点1への重み1の辺
		{2, 1, 1}, // ...
		{3, 2, 2},
		{4, 1, 10},
		{2, 1, 2},
		{2, 2, 8},
		{1, 4, 9},
		{3, 0, 7},
		{0, 1, 7}
};

// Dijkstraアルゴリズムの使用例
Dijkstra dijkstra = new Dijkstra(v);
for(
int[] edge :edges){
		dijkstra.

addEdge(edge[0], edge[1], edge[2]);
}
long distDijkstra = dijkstra.getShortestPathWeight(0, 4); // 頂点0から頂点4への最短距離

// Bellman-Fordアルゴリズムの使用例
BellmanFord bellmanFord = new BellmanFord(v, e);
for(
int[] edge :edges){
		bellmanFord.

addEdge(edge[0], edge[1], edge[2]);
}
long distBellmanFord = bellmanFord.getShortestPathWeight(0, 4); // 頂点0から頂点4への最短距離

// Warshall-Floydアルゴリズムの使用例
Warshallfroyd warshallfroyd = new Warshallfroyd(v);
for(
int[] edge :edges){
		warshallfroyd.

addEdge(edge[0], edge[1], edge[2]);
}
long distWarshallfroyd = warshallfroyd.getShortestPathWeight(0, 4); // 頂点0から頂点4への最短距離
long[][] allDistances = warshallfroyd.getAllShortestPathWeight(); // 全点対間の最短距離
```

## アルゴリズムの選択ガイド

| アルゴリズム         | 負の辺重み | 負閉路検出 | 時間計算量            | 適した用途                  |
|----------------|-------|-------|------------------|------------------------|
| Dijkstra       | ×     | ×     | O((V + E) log V) | 非負の辺重みを持つグラフでの単一始点最短経路 |
| Bellman-Ford   | ○     | ○     | O(V * E)         | 負の辺重みを持つグラフでの単一始点最短経路  |
| Warshall-Floyd | ○     | ○     | O(V³)            | 全点対間の最短経路、小〜中規模グラフ     |

## 注意事項

- Dijkstraアルゴリズムは負の辺重みを持つグラフでは正しく動作しません
- Bellman-Fordアルゴリズムは負閉路が存在する場合、到達可能な頂点への最短経路として-INFを返します
- Warshall-Floydアルゴリズムは頂点数の3乗の計算量を要するため、大規模グラフには適していません
- 全てのアルゴリズムは0-indexedの頂点番号を使用します