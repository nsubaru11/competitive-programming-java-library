# Palindrome

## 概要

回文を効率的に検出・操作するためのアルゴリズムとユーティリティを提供する。
Manacherのアルゴリズムを用いた線形時間での回文検出や、様々な回文関連の操作をサポートしている。

## クラス一覧

| クラス | 用途 | Guide | ソース | Example / 検証 |
|--------|------|-------|--------|----------------|
| `Manacher` | 線形時間で文字列内の全ての回文を検出するアルゴリズム | [ManacherGuide](./ManacherGuide.md) | [Java](../../../src/lib/string/palindrome/Manacher.java) | [Check1.java](../../../test/verify/string/palindrome/manacher/Check1.java) |
| `PalindromeNaive` | 回文に関する様々なユーティリティ関数を提供 | [PalindromeNaiveGuide](./PalindromeNaiveGuide.md) | [Java](../../../src/lib/string/palindrome/PalindromeNaive.java) | [Test.java](../../../test/verify/string/palindrome/Test.java) |
| `Eertree` | 相異なる回文部分文字列をオンラインに管理する回文木 | — | [Java](../../../src/lib/string/palindrome/Eertree.java) | — |

## 実装クラス

### [Manacher](../../../src/lib/string/palindrome/Manacher.java)

- **用途**: 線形時間で文字列内の全ての回文を検出するアルゴリズム
- **特徴**:
	- $\mathcal{O}(n)$の時間計算量で全ての回文を検出
	- 奇数長と偶数長の両方の回文に対応
	- 特定の位置を中心とする回文の取得
	- 最長回文の検出
	- 範囲が回文かどうかの判定
- **時間計算量**: $\mathcal{O}(n)$、ここでnは文字列の長さ
- **空間計算量**: $\mathcal{O}(n)$

### [PalindromeNaive](../../../src/lib/string/palindrome/PalindromeNaive.java)

- **用途**: 回文に関する様々なユーティリティ関数を提供
- **特徴**:
	- 文字列や文字配列が回文かどうかの判定
	- 文字列や文字配列の特定範囲が回文かどうかの判定
	- 最長回文部分文字列の検出（単純な$\mathcal{O}(n^2)$アルゴリズム）
	- 文字列を回文に変換する機能（最小の文字を追加）

### [Eertree](../../../src/lib/string/palindrome/Eertree.java)（未実装）

- **用途**: 相異なる回文部分文字列をオンラインに管理する回文木
- **状態**: クラスとTODOコメントのみで、公開APIは未実装である

## 注意事項

- Manacherアルゴリズムは線形時間で動作するため、大きな文字列でも効率的に処理できる
- `PalindromeNaive`の最長回文探索は単純な$\mathcal{O}(n^2)$アルゴリズムを使用する
- 文字列のインデックスは0から始まる
