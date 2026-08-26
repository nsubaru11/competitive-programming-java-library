# Connectivity 利用ガイド

## 概要

[`Connectivity`](../../../src/lib/graph/Connectivity.java)は、無向グラフのlow-link値を計算し、橋と関節点を検出するユーティリティクラスです。
DFSを明示的なスタックで実行するため、深いグラフでもJavaの再帰スタックを消費しません。

## 特徴

- `UndirectedGraph`の全連結成分を一度に解析
- 橋を入力時の論理辺IDで判定
- 関節点を頂点番号で判定
- DFSの訪問順`ord`とlow-link値`low`を結果から参照可能
- 並列辺に対応
- 二重辺連結成分分解と二重頂点連結成分分解は未実装

## 依存関係

- [`UndirectedGraph`](../Core/UndirectedGraphGuide.md)

## 主な機能（メソッド一覧）

### 1. low-link計算

| メソッド                         | 戻り値の型 | 説明                         |
|----------------------------------|------------|------------------------------|
| `lowLink(UndirectedGraph graph)` | `Result`   | 橋・関節点とlow-link値を計算 |

### 2. `Result`の参照

| フィールド／メソッド    | 型          | 説明                         |
|-------------------------|-------------|------------------------------|
| `ord`                   | `int[]`     | 各頂点のDFS訪問順（1始まり） |
| `low`                   | `int[]`     | 各頂点のlow-link値           |
| `bridge`                | `boolean[]` | 論理辺IDごとの橋の判定       |
| `articulation`          | `boolean[]` | 頂点番号ごとの関節点の判定   |
| `isBridge(int e)`       | `boolean`   | 論理辺`e`が橋か判定          |
| `isArticulation(int u)` | `boolean`   | 頂点`u`が関節点か判定        |

## 利用例

```java
import lib.graph.Connectivity;
import lib.graph.UndirectedGraph;

UndirectedGraph graph = new UndirectedGraph(5, 5);
graph.add(0, 1); // 辺ID 0
graph.add(1, 2); // 辺ID 1
graph.add(2, 0); // 辺ID 2: 三角形の一辺
graph.add(1, 3); // 辺ID 3: 橋
graph.add(3, 4); // 辺ID 4: 橋

Connectivity.Result result = Connectivity.lowLink(graph);
System.out.println(result.isBridge(3));       // true
System.out.println(result.isArticulation(1)); // true
```

`bridge[e]`は`UndirectedGraph`へ辺を追加した順の論理辺ID`e`に対応します。`ord`と`low`を使う場合は、次のように値を直接参照できます。

```java
for (int u = 0; u < graph.n; u++) {
	System.out.println(u + ": ord=" + result.ord[u] + ", low=" + result.low[u]);
}
```

## 注意事項

- 入力は自己ループを含まない`UndirectedGraph`を想定します。
- 頂点番号と辺IDは0-indexedです。`bridge.length`は論理辺数、`articulation.length`は頂点数です。
- `ord`は各連結成分の探索開始時点からではなく、グラフ全体で増加する1始まりの訪問順です。
- `Result`の配列はコピーされずに公開されます。呼び出し側で書き換えると結果も変化します。
- 橋・関節点の判定結果は、`Result`を生成した時点のグラフに対するものです。

## パフォーマンス特性

- 時間計算量: $\mathcal{O}(V + E)$
- 追加領域: $\mathcal{O}(V + E)$
- 再帰スタックは使用せず、頂点用の明示的スタックと辺走査用配列を使用

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                |
|:-------------------|:-----------|:------------------------------------|
| **バージョン 1.0** | 2026-08-27 | `lowLink`、橋判定、関節点判定を追加 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
