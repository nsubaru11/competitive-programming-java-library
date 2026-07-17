# UndirectedGraph 利用ガイド

## 概要

`UndirectedGraph`は、固定長の前方スター形式で無向グラフを保持するクラスです。

## 特徴

- 1本の無向辺に0始まりの論理辺IDを割り当て
- 各無向辺を向きの異なる2本の内部辺として保持
- 頂点から見た反対側の端点を`to(u, e)`で取得
- 重みなしの辺を重み1として追加可能

## 依存関係

- [`Graph`](./GraphGuide.md)

## 主な機能（メソッド一覧）

### 1. 構築・更新

| メソッド                                                              | 戻り値の型  | 説明                    |
|-------------------------------------------------------------------|--------|-----------------------|
| `UndirectedGraph(int n, int m)`                                   | -      | `n`頂点、`m`無向辺分の領域を確保   |
| `add(int u, int v)`                                               | `void` | 重み1の無向辺を追加            |
| `add(int u, int v, long c)`                                       | `void` | 重み付き無向辺を追加            |
| `setAll(IntSupplier u, IntSupplier v)`                            | `void` | 現在の辺を消去し、重み1の辺を`m`本入力 |
| `setAll(IntSupplier u, IntSupplier v, LongSupplier c)`            | `void` | 現在の辺を消去し、重み付き辺を`m`本入力 |
| `addAll(int count, IntSupplier u, IntSupplier v)`                 | `void` | 重み1の辺を`count`本追加      |
| `addAll(int count, IntSupplier u, IntSupplier v, LongSupplier c)` | `void` | 重み付き辺を`count`本追加      |
| `clear()`                                                         | `void` | すべての辺と次数を消去           |

### 2. 参照

| メソッド                | 戻り値の型   | 説明                    |
|---------------------|---------|-----------------------|
| `to(int u, int e)`  | `int`   | 辺`e`について、端点`u`と反対側の頂点 |
| `cost(int e)`       | `long`  | 無向辺`e`の重み             |
| `edgeCount()`       | `int`   | 現在の無向辺数               |
| `degree(int v)`     | `int`   | 頂点`v`の次数              |
| `adj(int u)`        | `int[]` | `u`に隣接する頂点            |
| `adjEdgeIds(int u)` | `int[]` | `u`に接続する無向辺ID         |

## 利用例

```java
import lib.graph.UndirectedGraph;

UndirectedGraph graph = new UndirectedGraph(4, 3);
graph.add(0, 1, 3);
graph.add(1, 2, 5);
graph.add(2, 3, 1);

for (int e : graph.adjEdgeIds(1)) {
	int to = graph.to(1, e);
	long cost = graph.cost(e);
}
```

## 注意事項

- 自己ループを含まない無向グラフを前提とします。
- コンストラクタの`m`は一括入力辺数であり、同時に確保する無向辺容量です。
- 容量を超える辺追加に対する明示的な検査は行いません。
- `to(u, e)`の`u`には辺`e`の端点を渡します。
- 隣接頂点と隣接辺IDは基本的に辺の追加と逆の順序で返ります。

## パフォーマンス特性

- 辺追加、辺情報、次数の取得: O(1)
- `adj`、`adjEdgeIds`: O(次数)
- 保持領域: O(V + 2E)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                             |
|:--------------|:-----------|:-------------------------------|
| **バージョン 2.0** | 2026-07-17 | 共通基底`Graph`を継承する固定長無向グラフとして再実装 |
| **バージョン 1.0** | 2026-01-08 | 無向グラフクラスとして初回実装                |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
