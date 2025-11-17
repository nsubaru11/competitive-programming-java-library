# SuffixTrie 利用ガイド

## 概要

`SuffixTrie` は「語尾一致（サフィックス）」の照合を前方一致に還元して高速に扱うため、文字列を逆向きに格納する Trie です。
`countPrefix(suffix)` により、与えたサフィックスで終わる登録単語数を O(L) 時間（L はサフィックス長）で取得できます。

## 特徴

- 逆方向に走査・格納する Trie（`insert` は末尾から先頭へ、`findLastNode` も末尾から先頭へ）
- `countPrefix(suffix)` が「suffix に一致する登録単語の件数」を返す
- ハッシュマップベースの素直な実装（`HashMap<Character, TrieNode>`）
- メソッドチェーン可能な `insert`（自身を返却）
- スレッドセーフではありません（単一スレッド用途を想定）

## 依存関係

- 標準ライブラリのみ：`java.util.HashMap`, `java.util.Map`
- 他クラスへの依存なし（デフォルトパッケージ）

## 主な機能（メソッド一覧）

### 基本操作

| メソッド                         | 戻り値の型        | 説明                                                      |
 |------------------------------|--------------|---------------------------------------------------------|
| `insert(String word)`        | `SuffixTrie` | 単語を末尾→先頭の順に挿入し、各ノードの `frequency` をインクリメント。自身を返すのでチェーン可。 |
| `search(String word)`        | `boolean`    | 逆向きに完全一致検索。登録語がそのまま一致する場合に `true`。                      |
| `countPrefix(String suffix)` | `int`        | 与えた `suffix` で終わる登録単語の件数を返します。                          |

[内部] `findLastNode(String key)` は末尾から先頭へ走査するヘルパー（非公開）。

## 利用例

 ```java
 SuffixTrie strie = new SuffixTrie();
 strie.insert("banana").insert("cabana").insert("band");
 
 // "ana" で終わる語は "banana", "cabana" の 2 つ
 int cntAna = strie.countPrefix("ana");  // 2
 
 boolean hasExact = strie.search("banana"); // true（完全一致）
 boolean notExact = strie.search("ana");    // false（部分一致は不可）
 ```

## 注意事項

- 全メソッドの引数に `null` は渡さないでください（未チェック、未定義動作）。
- 空文字列は未対応です：
	- `insert("")` は内部実装上 `NullPointerException` を引き起こします。
	- `search("")` は常に `false`、`countPrefix("")` は `0` を返します。
- 文字単位は Java の `char`（UTF-16 コードユニット）。サロゲートペア等の取り扱いに注意。
- 大文字小文字の区別や正規化は行いません。必要なら呼び出し側で前処理してください。

## パフォーマンス特性

- 時間計算量（平均）：
	- `insert`, `search`, `countPrefix` いずれも O(L)（L は文字列長）。
	- 子探索は `HashMap` による平均 O(1) を仮定。
- 空間計算量：O(N)（N は全ノード数、挿入した全単語長の総和に比例）。

## バージョン情報

| バージョン番号       | 年月日        | 詳細          |
 |:--------------|:-----------|:------------|
| **バージョン 1.0** | 2025-11-18 | 初版作成（ガイド追加） |

### バージョン管理について

- 1桁目（メジャー）: 機能追加・互換性に影響する変更時に更新
- 2桁目（マイナー）: 誤字修正や軽微な更新時に更新