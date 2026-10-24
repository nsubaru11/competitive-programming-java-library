# PalindromeNaive 利用ガイド

> PalindromeNaiveは回文に関する様々なユーティリティメソッドを提供するクラスである。

[← README: Palindrome](./README.md) · ソース: [PalindromeNaive.java](../../../src/lib/string/palindrome/PalindromeNaive.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

文字列や文字配列が回文かどうかの判定、最長回文部分文字列の検索、回文への変換など、回文に関する基本的な操作を簡単に行うことができる。

## 特徴

- 単純で使いやすい API を提供
- 文字列と文字配列の両方に対応
- 範囲指定による回文判定が可能
- 最長回文部分文字列の検索機能
- 文字列を回文に変換する機能
- すべてのメソッドは静的（static）で、インスタンス化不要

## 依存関係

- 標準Javaライブラリのみを使用（外部依存なし）

## API

### 1. 回文判定系メソッド

| メソッド                                        | 戻り値の型 | 説明                                                     |
|-------------------------------------------------|------------|----------------------------------------------------------|
| isPalindrome(String s)                          | boolean    | 文字列が回文かどうかを判定する                         |
| isPalindrome(char[] c)                          | boolean    | 文字配列が回文かどうかを判定する                       |
| isPalindromeRange(String s, int start, int end) | boolean    | 文字列の指定範囲[start, end)が回文かどうかを判定する   |
| isPalindromeRange(char[] c, int start, int end) | boolean    | 文字配列の指定範囲[start, end)が回文かどうかを判定する |

### 2. 回文検索・変換系メソッド

| メソッド                        | 戻り値の型 | 説明                                                                         |
|---------------------------------|------------|------------------------------------------------------------------------------|
| findLongestPalindrome(String s) | String     | 文字列内の最長回文部分文字列を見つける（$\mathcal{O}(n^2)$のアルゴリズム） |
| makePalindrome(String s)        | String     | 文字列が回文になるように最小の文字を追加する                               |

## 利用例

準備中

## 制約・注意事項

- isPalindromeRangeメソッドの引数endは含まれない（半開区間[start, end)）
- findLongestPalindromeメソッドは$\mathcal{O}(n^2)$の時間計算量を持つ。より効率的な実装にはManacherクラスを使用すること
- 空文字列や長さ1の文字列は常に回文として扱われる

## 性能特性

- isPalindrome: $\mathcal{O}(n)$の時間計算量、$\mathcal{O}(1)$の空間計算量
- isPalindromeRange: $\mathcal{O}(length)$の時間計算量、$\mathcal{O}(1)$の空間計算量（lengthは範囲の長さ）
- findLongestPalindrome: $\mathcal{O}(n^2)$の時間計算量、$\mathcal{O}(1)$の空間計算量
- makePalindrome: 最悪の場合$\mathcal{O}(n^2)$の時間計算量、$\mathcal{O}(n)$の空間計算量

## 実装履歴

| バージョン番号 | 年月日     | 詳細                                                                     |
|:---------------|:-----------|:-------------------------------------------------------------------------|
| バージョン 1.0 | 2025-03-29 | 初期実装：基本的な回文判定機能                                           |
| バージョン 2.0 | 2025-07-06 | バグ修正と機能追加：範囲指定の回文判定、最長回文検索、回文変換機能を追加 |
| バージョン 2.1 | 2026-01-06 | クラス名を `PalindromUtils` から `PalindromNative` に変更                |
