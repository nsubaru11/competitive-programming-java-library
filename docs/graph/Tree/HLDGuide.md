# HLD 利用ガイド

## 概要

`HLD` は根付き木を heavy-first 順に線形化し、頂点パスや部分木を配列上の半開区間へ分解します。LCA、距離、k-th ancestor、パス・部分木の区間処理を提供します。

## 特徴

- 各頂点に HLD 順の位置 `enter(u)` を割り当て、逆写像 `vertexAt(i)` も提供します。
- 部分木は1区間 `[enter(u), exit(u))`、パスは `O(log N)` 個の区間に分解されます。
- 区間コールバックは `IntBinaryConsumer` または `IntBinaryToLongFunction` を使い、区間端点を `int` で受け取ります。
- `queryNode` / `queryEdge` は結合的かつ可換な long 集約を扱います。

## 依存関係

- `lib.graph.tree.RootedTree`
- `lib.util.function.IntBinaryConsumer`
- `lib.util.function.IntBinaryToLongFunction`
- `java.util.function.IntConsumer`
- `java.util.function.LongBinaryOperator`

## 主な機能（メソッド一覧）

### 1. 構築・位置取得

| メソッド               | 戻り値の型 | 説明                                                |
|------------------------|------------|-----------------------------------------------------|
| `HLD(RootedTree tree)` | `-`        | 根付き木の情報から HLD 順を構築します。             |
| `vertexAt(int i)`      | `int`      | HLD 順の位置 `i` にある頂点を返します。             |
| `enter(int u)`         | `int`      | 頂点 `u` の HLD 順の位置を返します。                |
| `exit(int u)`          | `int`      | 頂点 `u` の部分木区間の終端（含まない）を返します。 |
| `top(int u)`           | `int`      | 頂点 `u` が属する heavy chain の先頭を返します。    |

### 2. 木上の位置・距離クエリ

| メソッド | 戻り値の型 | 説明 |
|----------|------------|------|
| `lca(int u, int v)` | `int` | `u`, `v` の最小共通祖先を返します。 |
| `distance(int u, int v)` | `long` | 辺重みを使った `u` から `v` までの距離を返します。 |
| `kthAncestor(int u, int k)` | `int` | `u` から `k` 個上の祖先を返します。存在しなければ `-1`。 |
| `jump(int u, int v, int k)` | `int` | パス `u`→`v` 上で `k` 辺進んだ頂点を返します。パス外なら `-1`。 |

### 3. 区間更新用コールバック

| メソッド | 戻り値の型 | 説明 |
|----------|------------|------|
| `updateSubtree(int u, IntBinaryConsumer action)` | `void` | 部分木の HLD 区間 `[enter(u), exit(u))` を渡します。 |
| `updateEdge(int e, IntConsumer action)` | `void` | 論理辺 `e` を深い側の頂点位置に対応づけ、その位置を渡します。 |
| `updateEdge(int u, int v, IntBinaryConsumer action)` | `void` | パス上の辺を、LCA を除く HLD 区間に分けて渡します。 |
| `updateNode(int u, IntConsumer action)` | `void` | 頂点 `u` の HLD 位置を渡します。 |
| `updateNode(int u, int v, IntBinaryConsumer action)` | `void` | 両端を含むパス上の頂点区間を渡します。 |

### 4. 集約クエリ

| メソッド | 戻り値の型 | 説明 |
|----------|------------|------|
| `querySubtree(int u, IntBinaryToLongFunction query)` | `long` | 部分木区間 `[enter(u), exit(u))` の集約値を返します。 |
| `queryEdge(int u, int v, long identity, IntBinaryToLongFunction query, LongBinaryOperator op)` | `long` | パス上の辺集約を返します。 |
| `queryNode(int u, int v, long identity, IntBinaryToLongFunction query, LongBinaryOperator op)` | `long` | 両端を含むパス上の頂点集約を返します。 |

`IntBinaryToLongFunction` の引数は区間端点 `int l, int r`、戻り値は区間 `[l, r)` の集約値です。`LongBinaryOperator` の `op` は区間集約値を結合します。

## 利用例

```java
RootedTree tree = new RootedTree(n, 0);
tree.setAll(sc::nextInt, sc::nextInt); // 全辺を追加してから HLD を構築
HLD hld = new HLD(tree);

LongSegmentTree seg = new LongSegmentTree(n, Long::sum, 0);
seg.setAll(i -> value[hld.vertexAt(i)]); // 頂点順から HLD 順へ並べ替える
seg.add(hld.enter(p), delta);
long sum = hld.queryNode(u, v, 0, seg::query, Long::sum);
```

## 注意事項

- `RootedTree` の前処理は最初の情報参照時に行われます。全辺を追加してから `HLD` を構築し、前処理後は木を変更しないでください。
- `HLD.enter/exit` は heavy-first 順です。`RootedTree.in/out` の preorder 番号とは別の位置です。
- `queryNode` / `queryEdge` は区間の向きや結合順を保ちません。`op` は結合的かつ可換である必要があります。文字列結合など非可換なパス集約には使用できません。
- パス区間コールバックの呼び出し順は、パスの頂点順ではありません。更新用途では順序に依存しない処理を行ってください。
- 頂点値をセグメント木に格納する場合は、初期値を `value[hld.vertexAt(i)]` で並べ、頂点更新には `hld.enter(u)` を使います。

## パフォーマンス特性

- 構築: `O(N)` 時間、`O(N)` 空間。
- `lca` / `distance` / `kthAncestor` / `jump`: `O(log N)` 時間。
- 部分木操作: 1 区間。パス操作: `O(log N)` 区間。
- パス上でセグメント木の `O(log N)` 区間クエリを行う場合、全体で `O(log² N)` 時間です。

## バージョン情報

| バージョン番号 | 年月日 | 詳細 |
|:---------------|:-------|:-----|
| **バージョン 1.0** | 2026-10-03 | HLD、LCA、距離、k-th ancestor、部分木・パスの区間分解を初期実装。 |
| **バージョン 1.1** | 2026-10-07 | HLD の部分木・パス、および `RootedTree` の部分木クエリ関数を `IntBinaryToLongFunction` に変更し、区間端点を `int` で受け取る API に修正。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
