# Trie 利用ガイド

> `Trie` は文字列集合に対する前方一致（プレフィックス）操作を高速に行うための木構造である。

[← README: Trie](./README.md) · ソース: [Trie.java](../../../src/lib/ds/trie/Trie.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

本実装は競技プログラミング向けの最小機能版で、単語の挿入・完全一致検索・プレフィックスに一致する単語数の取得を $\mathcal{O}(L)$ 時間（L は文字列長）で行える。

## 特徴

- ハッシュマップベースの素直な実装（子ノードは `HashMap<Character, TrieNode>`）
- ルート直下に子ノードを持つシンプル設計（`lib.ds.trie`パッケージ、外部依存なし）
- パス上の各ノードに出現回数 `frequency` を保持 → `countPrefix` でプレフィックス件数を取得可能
- メソッドチェーン可能な `insert`（自身を返却）
- スレッドセーフではない（単一スレッド用途を想定）

## 依存関係

- 標準ライブラリのみ：`java.util.HashMap`, `java.util.Map`
- 他の`lib.*`クラスへの依存なし

## API

### 基本操作

| メソッド                     | 戻り値の型 | 説明                                                                                    |
 |------------------------------|------------|-----------------------------------------------------------------------------------------|
| `insert(String word)`        | `Trie`     | 単語を挿入し、各ノードの `frequency` をインクリメントする。自身を返すのでチェーン可。 |
| `search(String word)`        | `boolean`  | 単語が完全一致で登録済みかを返す。                                                  |
| `countPrefix(String prefix)` | `int`      | 指定したプレフィックスで始まる登録単語の件数を返す。                                |

[内部] `findLastNode(String key)` は走査ヘルパー（非公開）。

## 利用例

```java
 import lib.ds.trie.Trie;

 Trie trie = new Trie();
 trie.insert("apple").insert("app").insert("apricot");
 
 boolean hasApp = trie.search("app");           // true
 boolean hasApply = trie.search("apply");       // false
 int cntAp = trie.countPrefix("ap");            // 3 ("app", "apple", "apricot")
 int cntApp = trie.countPrefix("app");          // 2 ("app", "apple")
 ```


- [Example.java](../../../test/verify/ds/trie/Example.java)



## 制約・注意事項

- 全メソッドの引数に `null` は渡さないこと（未チェック、未定義動作）。
- 空文字列は未対応である：
	- `insert("")` は内部実装上 `NullPointerException` を引き起こす。
	- `search("")` は常に `false`、`countPrefix("")` は `0` を返す。
- 大文字小文字の区別や正規化は行わない。必要なら呼び出し側で前処理すること。
- 文字単位は Java の `char`（UTF-16 コードユニット）である。サロゲートペア等の取り扱いに注意すること。
- 競技向け最小実装につき、入力検証やエラー処理は限定的である。

## 性能特性

- 時間計算量（平均）：
	- `insert`, `search`, `countPrefix` いずれも $\mathcal{O}(L)$（L は文字列長）。
	- 子探索は `HashMap` による平均 $\mathcal{O}(1)$ を仮定。
- 空間計算量：$\mathcal{O}(N)$（N は全ノード数、挿入した全単語長の総和に比例）。
- 定数倍はアルファベットサイズや `HashMap` のオーバーヘッドに依存する。

## 実装履歴

| バージョン番号     | 年月日     | 詳細                   |
 |:-------------------|:-----------|:-----------------------|
| **バージョン 1.0** | 2025-11-18 | 初版作成（ガイド追加） |
