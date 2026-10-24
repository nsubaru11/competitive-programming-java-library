# `lib.string`

`src/lib/string`にある文字列アルゴリズムの実装一覧である。

| 分野                                   | 主な実装                      |
|----------------------------------------|-------------------------------|
| [編集距離](./levenshtein/README.md)    | Levenshtein距離               |
| [回文](./palindrome/README.md)         | Manacher法、回文判定・生成    |
| [文字列探索](./stringsearch/README.md) | Z-Algorithmなどのパターン照合 |

## 内部パッケージ

| パッケージ               | ソース                                  | ドキュメント                           |
|--------------------------|-----------------------------------------|----------------------------------------|
| `lib.string.approximate` | [src](../../src/lib/string/approximate) | [編集距離](./levenshtein/README.md)    |
| `lib.string.matching`    | [src](../../src/lib/string/matching)    | [文字列探索](./stringsearch/README.md) |
| `lib.string.palindrome`  | [src](../../src/lib/string/palindrome)  | [回文](./palindrome/README.md)         |
| `lib.string.sequence`    | [src](../../src/lib/string/sequence)    | —                                      |
| `lib.string.suffix`      | [src](../../src/lib/string/suffix)      | —                                      |

## sequence / suffix の実装

| クラス | ソース | 用途 |
|--------|--------|------|
| `LongestCommonSubsequence` | [Java](../../src/lib/string/sequence/LongestCommonSubsequence.java) | 最長共通部分列 |
| `LyndonFactorization` | [Java](../../src/lib/string/sequence/LyndonFactorization.java) | Lyndon分解 |
| `MinimumRotation` | [Java](../../src/lib/string/sequence/MinimumRotation.java) | 最小巡回シフト |
| `SuffixArray` | [Java](../../src/lib/string/suffix/SuffixArray.java) | 接尾辞配列 |
| `SuffixAutomaton`（未実装） | [Java](../../src/lib/string/suffix/SuffixAutomaton.java) | 部分文字列の状態表現 |
