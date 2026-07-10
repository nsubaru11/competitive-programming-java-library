# Trie

## 概要

このフォルダには、前方一致（Trie）および語尾一致（SuffixTrie）に特化したシンプルな木構造の実装と、各種派生Trie（RadixTrie / PatriciaTrie / TernarySearchTree / DoubleArrayTrie / SuffixArray）の実装が含まれます。競技プログラミング環境でのコピペ利用を前提とした最小構成です。

## 実装クラス

### [Trie](../../../src/lib/ds/Trie.java)

- **用途**：
	- 文字列集合に対する前方一致（プレフィックス）検索、完全一致検索、挿入
- **特徴**：
	- `HashMap<Character, TrieNode>` による素直な子ノード管理
	- ノードに `frequency`（そのノード以下の通過回数）を保持 → プレフィックス件数取得に利用
	- `insert` は自身を返すためメソッドチェーン可能
- **時間計算量**：
	- `insert`, `search`, `countPrefix` いずれも平均 O(L)（L は文字列長）
- **空間計算量**：
	- O(N)（N は挿入した全単語長の総和に比例）

### [SuffixTrie](../../../src/lib/ds/SuffixTrie.java)

- **用途**：
	- 語尾一致（サフィックス）条件での件数取得や完全一致検索（逆向き格納）
- **特徴**：
	- 文字列を末尾→先頭の順に格納し、`countPrefix(suffix)` で「suffix に一致する語の件数」を返す
	- 内部は `HashMap<Character, TrieNode>` を用いた素直な実装
	- `insert` は自身を返し、チェーン可能
- **時間計算量**：
	- `insert`, `search`, `countPrefix` いずれも平均 O(L)（L は文字列長）
- **空間計算量**：
	- O(N)（N は挿入した全単語長の総和に比例）

### その他の実装クラス

- [RadixTrie](../../../src/lib/ds/RadixTrie.java)：パス圧縮Trie。共通プレフィックスを1ノードにまとめてメモリと走査を削減
- [PatriciaTrie](../../../src/lib/ds/PatriciaTrie.java)：ビット単位の分岐による二分Trie（葉でキー照合）
- [TernarySearchTree](../../../src/lib/ds/TernarySearchTree.java)：三分探索木。文字比較の三方向分岐で省メモリにプレフィックス検索を提供
- [DoubleArrayTrie](../../../src/lib/ds/DoubleArrayTrie.java)：BASE/CHECK 2配列による静的構築Trie。検索が高速
- [SuffixArray](../../../src/lib/ds/SuffixArray.java)：接尾辞配列+LCP配列（簡易構築版、部分文字列検索 `contains` 付き）
- [CompactTrie](../../../src/lib/ds/CompactTrie.java)（未実装）：ビットマップ圧縮Trie（TODO）

## アルゴリズム（データ構造）選択ガイド

- 前方一致や辞書のプレフィックス件数を取りたい → `Trie`
- ある接尾辞で終わる語の件数を取りたい → `SuffixTrie`

いずれも完全一致の存在判定は `search` で可能です。部分一致の存在判定（例：`SuffixTrie#search("ana")`
で末尾一致のみ判断）はできない点に注意してください。

## 注意事項

- いずれのクラスも引数に `null` を渡すことは想定していません（未定義動作）。
- 空文字列は未対応です：`insert("")` は内部実装上例外を引き起こす可能性があります。`search("")` は常に `false`、
  `countPrefix("")` は `0` を返します。
- 文字単位は Java の `char`（UTF-16 コードユニット）です。大文字小文字の正規化やサロゲートペア処理は呼び出し側で行ってください。