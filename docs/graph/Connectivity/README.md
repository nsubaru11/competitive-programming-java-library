# Connectivity（🚧 一部実装）

## 概要

無向グラフのlow-link値を利用した橋・関節点検出を提供します。二重辺連結成分分解と二重頂点連結成分分解は未実装です。

## 実装クラス

### [Connectivity](../../../src/lib/graph/Connectivity.java)

- **用途**：
	- 橋・関節点の検出
- **特徴**：
	- 再帰を使わない反復DFS
	- `UndirectedGraph`の論理辺IDで橋を参照
- **時間計算量**：
	- `lowLink`: $\mathcal{O}(V + E)$
- **空間計算量**：
	- `lowLink`: $\mathcal{O}(V + E)$
- **詳細**: [ConnectivityGuide.md](./ConnectivityGuide.md)

## アルゴリズム（データ構造）選択ガイド

橋・関節点の検出には`Connectivity.lowLink`を使用します。二重辺連結成分分解・二重頂点連結成分分解は今後追加予定です。

## 注意事項

- `lowLink`の入力は`UndirectedGraph`です。辺IDは辺の追加順に割り当てられる論理辺IDです。
- `UndirectedGraph`は自己ループを含まない前提のクラスです。
- 二重辺連結成分分解と二重頂点連結成分分解は未実装です。
