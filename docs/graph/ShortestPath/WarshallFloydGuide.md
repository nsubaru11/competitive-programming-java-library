# WarshallFloyd 利用ガイド

## 概要

`WarshallFloyd`は、`Graph`の全頂点対間の最短距離を計算する静的ユーティリティクラスです。

## 特徴

- `DirectedGraph`と`UndirectedGraph`の両方に対応
- 負辺と多重辺に対応
- グラフ内の負閉路を検出
- 負閉路を経由できる頂点対だけを`Long.MIN_VALUE`として保持
- 負閉路と無関係な有限距離と到達不能状態を維持

## 依存関係

- [`Graph`](../Core/GraphGuide.md)

## 主な機能（メソッド一覧）

### 1. 計算

| メソッド                 | 戻り値の型                  | 説明                 |
|----------------------|------------------------|--------------------|
| `solve(Graph graph)` | `WarshallFloyd.Result` | 全頂点対間の距離と負閉路の影響を計算 |

### 2. Result

| メソッド                      | 戻り値の型     | 説明                 |
|---------------------------|-----------|--------------------|
| `dist(int u, int v)`      | `long`    | 頂点`u`から`v`への距離     |
| `reachable(int u, int v)` | `boolean` | 頂点`u`から`v`へ到達可能か判定 |

`Result`からは`hasNegCycle`と距離行列`dist`も直接参照できます。
`hasNegCycle`は、グラフ内のいずれかに負閉路がある場合に`true`です。

## 利用例

```java
import lib.graph.DirectedGraph;
import lib.graph.WarshallFloyd;

DirectedGraph graph = new DirectedGraph(5, 5);
graph.add(0, 1, 2);
graph.add(1, 2, -3);
graph.add(2, 1, 1); // 負閉路
graph.add(2, 3, 4);
graph.add(4, 3, 7);

WarshallFloyd.Result result = WarshallFloyd.solve(graph);
long affected = result.dist(0, 3);  // Long.MIN_VALUE
long unreachable = result.dist(4, 0); // Long.MAX_VALUE
long finite = result.dist(4, 3);    // 7
```

## 注意事項

- 到達不能な頂点対の距離は`Long.MAX_VALUE`です。
- `u`から負閉路へ到達でき、さらにその負閉路から`v`へ到達できる場合、`dist(u, v)`は`Long.MIN_VALUE`です。
- `reachable(u, v)`は距離が`Long.MIN_VALUE`でも`true`を返します。
- `hasNegCycle`が`true`でも、負閉路の影響を受けない距離はそのまま利用できます。
- 経路復元情報は保持しません。

## パフォーマンス特性

- 時間計算量: O(V³)
- 距離行列と負閉路の影響判定を含む追加領域: O(V²)
- 入力辺の距離行列への反映: O(E)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                     |
|:--------------|:-----------|:-------------------------------------------------------|
| **バージョン 2.0** | 2026-07-17 | `Graph`を受け取る静的ユーティリティへ変更し、負閉路の影響を受ける頂点対だけを識別する結果APIを追加 |
| **バージョン 1.0** | 2025-03-22 | グラフを内部保持するクラスとして初回実装                                   |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
