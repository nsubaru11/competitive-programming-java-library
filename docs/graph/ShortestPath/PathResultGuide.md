# PathResult 利用ガイド

## 概要

`PathResult`は、`BFS`、`Dijkstra`、`BellmanFord`、`ZeroOneBFS`、`TopologicalShortestPath`が返す経路計算結果の共通型です。
始点、距離、親、負閉路の有無を保持し、距離の参照と経路復元を提供します。
`TopologicalShortestPath.solveLongest`の結果では、距離と親が最長経路を表します。

## 特徴

- 単一始点と複数始点の結果を同じ型で扱える
- DAGの最短経路・最長経路の結果も同じ型で扱える
- 距離配列と親配列を直接参照できる
- `pathTo`で始点から指定頂点までの経路を復元できる
- `toString`で計算結果を複数行の文字列として確認できる
- 到達不能と負閉路の影響を距離の特別値で表現

## 依存関係

- Java標準ライブラリのみ
- 結果を生成するアルゴリズム: [`BFS`](./BFSGuide.md)、[`Dijkstra`](./DijkstraGuide.md)、[`BellmanFord`](./BellmanFordGuide.md)、[`ZeroOneBFS`](./ZeroOneBFSGuide.md)、[`TopologicalShortestPath`](./TopologicalShortestPathGuide.md)

## 公開フィールド

| フィールド    | 型        | 説明                                                                                      |
|---------------|-----------|-------------------------------------------------------------------------------------------|
| `s`           | `int[]`   | 距離0として探索を開始した始点の配列                                                       |
| `hasNegCycle` | `boolean` | 到達可能な負閉路がある場合は`true`。BFS・Dijkstra・0-1 BFS・トポロジカル法では常に`false` |
| `dist`        | `long[]`  | 各頂点への計算距離。到達不能は`Long.MAX_VALUE`、負閉路の影響下は`Long.MIN_VALUE`          |
| `parent`      | `int[]`   | 経路復元用の親頂点。始点自身は自身、復元不能な頂点は`-1`                                  |

## 主な機能（メソッド一覧）

| メソッド           | 戻り値の型 | 説明                                               |
|--------------------|------------|----------------------------------------------------|
| `distTo(int v)`    | `long`     | 頂点`v`への距離を返す                              |
| `reachable(int v)` | `boolean`  | 頂点`v`へ到達可能か返す。負閉路の影響下でも`true`  |
| `parent(int v)`    | `int`      | 頂点`v`の経路復元用の親を返す                      |
| `pathTo(int v)`    | `int[]`    | 始点から`v`までの頂点列を返す。復元不能なら`null`  |
| `toString()`       | `String`   | 始点、負閉路の有無、距離配列、親配列を複数行で返す |

コンストラクタはpackage-privateです。通常は各経路アルゴリズムの`solve`または`solveLongest`の戻り値として取得します。

## 利用例

```java
import lib.graph.Dijkstra;
import lib.graph.DirectedGraph;
import lib.graph.PathResult;

DirectedGraph graph = new DirectedGraph(4, 4);
graph.add(0, 1, 2);
graph.add(1, 2, 3);
graph.add(0, 3, 10);
graph.add(2, 3, 1);

PathResult result = Dijkstra.solve(graph, 0);
System.out.println(result.distTo(3)); // 6
System.out.println(result.pathTo(3));
System.out.println(result);            // s / hasNegCycle / dist / parent
```

## 注意事項

- 公開配列は複製されません。呼び出し側で書き換えると結果の内容も変化します。
- `s`は単一始点の場合も長さ1の配列です。
- 到達不能な頂点では`reachable`が`false`、`parent`が`-1`、`pathTo`が`null`です。
- 負閉路の影響下では`reachable`が`true`でも、`parent`は`-1`、`pathTo`は`null`です。
- `dist`の有限値として`Long.MAX_VALUE`と`Long.MIN_VALUE`を使用する入力は前提としません。

## パフォーマンス特性

- `distTo`、`reachable`、`parent`: $\mathcal{O}(1)$
- `pathTo`: 経路長に比例する配列を作成し、最悪$\mathcal{O}(V)$
- `toString`: 保持している配列全体を走査するため$\mathcal{O}(V)$
- 保持領域: 距離配列と親配列で$\mathcal{O}(V)$

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                    |
|:-------------------|:-----------|:------------------------------------------------------------------------|
| **バージョン 2.1** | 2026-08-23 | `toString()`を追加し、始点・負閉路状態・距離・親の確認を可能にした      |
| **バージョン 2.0** | 2026-08-23 | `ShortestPathResult`から`PathResult`へ改名し、DAG最長経路の結果にも対応 |
| **バージョン 1.0** | 2026-08-23 | 単一始点・複数始点の最短路アルゴリズムで共有する結果型として追加        |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
