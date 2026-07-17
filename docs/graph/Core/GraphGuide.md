# Graph 利用ガイド

## 概要

`Graph`は、前方スター形式の隣接リストをプリミティブ配列で保持する抽象基底クラスです。
通常は`DirectedGraph`または`UndirectedGraph`を生成して使用します。

## 特徴

- 辺の終点、重み、次の内部辺IDを配列で保持
- 重みなしの辺を重み1として扱い、重み付きアルゴリズムと表現を共通化
- 容量固定により、辺追加時の再確保を回避
- サプライヤからの一括初期化と追加入力に対応

## 依存関係

- Java標準ライブラリの`IntSupplier`、`LongSupplier`
- 具象クラスとして`DirectedGraph`または`UndirectedGraph`

## 主な機能（メソッド一覧）

### 1. 初期化

| メソッド                                                   | 戻り値の型  | 説明                    |
|--------------------------------------------------------|--------|-----------------------|
| `Graph(int n, int m, int edgeCapacity)`                | -      | 頂点数、一括入力辺数、内部辺容量を設定   |
| `setAll(IntSupplier u, IntSupplier v)`                 | `void` | 現在の辺を消去し、重み1の辺を`m`本入力 |
| `setAll(IntSupplier u, IntSupplier v, LongSupplier c)` | `void` | 現在の辺を消去し、重み付き辺を`m`本入力 |
| `clear()`                                              | `void` | すべての辺を削除              |

### 2. 辺追加

| メソッド                                                              | 戻り値の型  | 説明                           |
|-------------------------------------------------------------------|--------|------------------------------|
| `add(int u, int v)`                                               | `void` | 重み1の論理辺を追加                   |
| `add(int u, int v, long c)`                                       | `void` | 重み付き論理辺を追加                   |
| `addAll(int count, IntSupplier u, IntSupplier v)`                 | `void` | 現在の辺に続けて重み1の辺を`count`本追加     |
| `addAll(int count, IntSupplier u, IntSupplier v, LongSupplier c)` | `void` | 現在の辺に続けて重み付き辺を`count`本追加     |
| `addEdge(int u, int v, long c)`                                   | `void` | 具象クラスが使う内部辺追加処理（`protected`） |

### 3. 参照

| メソッド                | 戻り値の型   | 説明                      |
|---------------------|---------|-------------------------|
| `edgeCount()`       | `int`   | 現在の論理辺数                 |
| `cost(int e)`       | `long`  | 論理辺IDに対応する重み            |
| `degree(int v)`     | `int`   | 頂点の次数。有向グラフでは入次数と出次数の合計 |
| `adj(int u)`        | `int[]` | 頂点`u`から進める隣接頂点          |
| `adjEdgeIds(int u)` | `int[]` | 頂点`u`から進める論理辺ID         |

## 利用例

```java
import lib.graph.DirectedGraph;
import lib.graph.Graph;

Graph graph = new DirectedGraph(4, 4);
graph.add(0, 1);
graph.add(1, 2, 5);

int edgeCount = graph.edgeCount();
int[] adjacent = graph.adj(1);
```

## 注意事項

- 頂点番号と辺IDは0-indexedです。
- `m`は現在の辺数ではなく、辺数引数を持たない`setAll`が読み込む辺数です。現在の辺数は`edgeCount()`で取得します。
- `setAll`は既存の辺を削除してから入力し、`addAll`は既存の辺に追加します。
- 論理辺数が構築時の容量を超えないように使用します。
- 前方スター形式では新しい辺をリストの先頭へ挿入するため、`adj`と`adjEdgeIds`は基本的に辺の追加と逆の順序で返ります。

## パフォーマンス特性

- `add`、`edgeCount`、`cost`、`degree`: O(1)
- `setAll`、`addAll`: O(追加する辺数)
- `adj`、`adjEdgeIds`: O(出次数)と同じ大きさの配列を確保
- 保持領域: O(V + E)。無向グラフの内部辺領域はO(V + 2E)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                   |
|:--------------|:-----------|:---------------------|
| **バージョン 1.0** | 2026-07-17 | 前方スター形式の共通基底クラスとして実装 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新
