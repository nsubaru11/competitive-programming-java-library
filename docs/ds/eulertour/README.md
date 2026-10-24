# EulerTour

## 概要

`lib.graph.tree.EulerTour` は、根付き木の各頂点を入場時と退場時に記録する長さ `2N` のイベント列を提供する。木上 Mo など、DFS中の頂点の出入りを扱うアルゴリズムに利用できる。

頂点を各1回だけ並べるpreorderとは別形式である。部分木の区間クエリには、通常は `RootedTree` または `HLD` のpreorder区間を使う。また、LCA用の深さ付きDFS往復列とも異なる。

## クラス一覧

| クラス | 用途 | Guide | ソース | Example / 検証 |
|--------|------|-------|--------|----------------|
| `EulerTour` | `RootedTree`のpreorder情報から長さ`2N`の入退場イベント列を構築する。 | — | [Java](../../../src/lib/graph/tree/EulerTour.java) | — |

## 実装クラス

### [EulerTour](../../../src/lib/graph/tree/EulerTour.java)

- **入力**：DFS前処理済みの `RootedTree`
- **列**：長さ `2N`。各頂点は入場・退場の2回記録される
- **問い合わせ**：`eventAt(i)` でイベント位置の頂点、`enter(u)` / `exit(u)` で頂点の入退場位置を取得
- **構築計算量**：`O(N)`
- **追加領域**：`O(N)`

イベント列は `RootedTree` が持つpreorder、部分木区間、深さから位置を計算して構築するため、隣接リストの再DFSは行わない。

## 配列位置の意味

`RootedTree`の`in/out`は頂点を1回ずつ並べたpreorder上の半開区間である。EulerTourでは入場・退場イベントの位置を次の式で求める。

```text
enter(u) = 2 * in(u) - depth(u)
exit(u)  = 2 * out(u) - depth(u) - 1
```

EulerTourのイベント区間 `[enter(u), exit(u) + 1)` には、`u`の部分木に属する全頂点の入退場イベントが含まれる。

## 用途と制約

- 木上 Mo ではイベント列上の区間を動かし、通過した頂点の有効状態を反転する。
- 入退場位置の包含関係から祖先関係を判定できるが、`RootedTree`のpreorder区間でも同じ判定ができる。
- 部分木の値を集約する場合、イベント列では入場イベントに値を置き、退場イベントには単位値を置くなどの扱いが必要である。頂点1回のpreorder列の方が簡潔なことが多い。
- このイベント列単独ではLCAをRMQで求めるためのDFS往復列を提供しない。

木アルゴリズム全体は[Graph/Tree](../../graph/tree/README.md)を参照すること。
