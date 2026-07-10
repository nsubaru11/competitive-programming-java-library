# UnionFind 利用ガイド

## 概要

UnionFind（素集合データ構造）は、グラフの連結成分管理や動的なグループ分割に適した、高速な集合管理用クラスです。

## 特徴

- パス圧縮による高速な`find`操作
- ランク付けによる効率的な`union`操作
- 各グループのサイズ（要素数）・総辺数の取得
- 全グループの列挙機能
- 多重辺・自己ループにも対応
- Java標準ライブラリのみ依存
- **バージョン1.1でfinalの明示化（不変なフィールドやメソッドへのfinal指定）が追加**

## 依存関係

- Java 標準ライブラリ（`java.util.*`）

## 主な機能（メソッド一覧）

### 1. グループ操作系メソッド

| メソッド                      | 戻り値の型   | 説明                       |
|---------------------------|---------|--------------------------|
| find(int i)               | int     | 要素iの属するグループの代表元を取得       |
| isConnected(int x, int y) | boolean | 要素xとyが同じグループに属するか判定      |
| union(int x, int y)       | boolean | 要素xとyのグループを結合（新結合ならtrue） |
| groupCount()              | int     | 現在のグループ数                 |

### 2. グループ情報系メソッド

| メソッド             | 戻り値の型                       | 説明                     |
|------------------|-----------------------------|------------------------|
| size(int i)      | int                         | 要素iが属するグループのサイズ（頂点数）   |
| pathCount(int i) | int                         | 要素iが属するグループの総辺数（多重辺含む） |
| groups()         | Map<Integer, List<Integer>> | 全グループと各グループの要素リスト      |

## 利用例

```java
UnionFind uf = new UnionFind(5);
uf.union(0, 1);
uf.union(1, 2);
System.out.println(uf.isConnected(0, 2)); // true
System.out.println(uf.groupCount()); // 3
System.out.println(uf.size(0)); // 3
System.out.println(uf.pathCount(1)); // 2
System.out.println(uf.groups()); // グループごとの頂点リスト
```

## 注意事項

- 削除操作（グループ分割）はサポートされていません
- 要素は0～n-1の整数で管理。非整数型は変換が必要
- `groups()`はO(n)で全要素走査します

## パフォーマンス特性

- 初期化：O(n)
- find/union/isConnected：償却O(α(n))（α(n)はアッカーマン関数の逆関数、実用上ほぼO(1)）
- groupCount/size/pathCount：O(α(n))
- groups：O(n)
- 空間計算量：O(n)

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                       |
|:--------------|:-----------|:---------------------------------------------------------|
| **バージョン 1.0** | 2025-03-29 | 初期実装（find, union, isConnected, groupCount, size, groups） |
| **バージョン 1.1** | 2025-10-01 | finalの明示化、JavaDoc・README整理、GuideTemplate準拠ガイド追加          |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッド追加や機能拡張時に更新
- 2桁目（マイナーバージョン）: 誤字修正・バグ修正・軽微な高速化等で更新
