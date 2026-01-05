# PalindromeNative 利用ガイド

## 概要

PalindromeNativeは回文に関する様々なユーティリティメソッドを提供するクラスです。
文字列や文字配列が回文かどうかの判定、最長回文部分文字列の検索、回文への変換など、回文に関する基本的な操作を簡単に行うことができます。

## 特徴

- 単純で使いやすい API を提供
- 文字列と文字配列の両方に対応
- 範囲指定による回文判定が可能
- 最長回文部分文字列の検索機能
- 文字列を回文に変換する機能
- すべてのメソッドは静的（static）で、インスタンス化不要

## 依存関係

- 標準Javaライブラリのみを使用（外部依存なし）

## 主な機能（メソッド一覧）

### 1. 回文判定系メソッド

| メソッド                                            | 戻り値の型   | 説明                                 |
|-------------------------------------------------|---------|------------------------------------|
| isPalindrome(String s)                          | boolean | 文字列が回文かどうかを判定します                   |
| isPalindrome(char[] c)                          | boolean | 文字配列が回文かどうかを判定します                  |
| isPalindromeRange(String s, int start, int end) | boolean | 文字列の指定範囲[start, end)が回文かどうかを判定します  |
| isPalindromeRange(char[] c, int start, int end) | boolean | 文字配列の指定範囲[start, end)が回文かどうかを判定します |

### 2. 回文検索・変換系メソッド

| メソッド                            | 戻り値の型  | 説明                                 |
|---------------------------------|--------|------------------------------------|
| findLongestPalindrome(String s) | String | 文字列内の最長回文部分文字列を見つけます（O(n²)のアルゴリズム） |
| makePalindrome(String s)        | String | 文字列が回文になるように最小の文字を追加します            |

## 利用例

```java
import static PalindromeNative.*;

public class Example {
	public static void main(String[] args) {
		// 回文判定
		System.out.println(isPalindrome("level"));  // true
		System.out.println(isPalindrome("hello"));  // false

		// 範囲指定の回文判定
		System.out.println(isPalindromeRange("levelup", 0, 5));  // true (level)

		// 最長回文部分文字列の検索
		System.out.println(findLongestPalindrome("babad"));  // "bab" または "aba"

		// 回文への変換
		System.out.println(makePalindrome("abc"));  // "abcba"
	}
}
```

## 注意事項

- isPalindromeRangeメソッドの引数endは含まれません（半開区間[start, end)）
- findLongestPalindromeメソッドはO(n²)の時間計算量を持ちます。より効率的な実装にはManacherクラスを使用してください
- 空文字列や長さ1の文字列は常に回文として扱われます

## パフォーマンス特性

- isPalindrome: O(n)の時間計算量、O(1)の空間計算量
- isPalindromeRange: O(length)の時間計算量、O(1)の空間計算量（lengthは範囲の長さ）
- findLongestPalindrome: O(n²)の時間計算量、O(1)の空間計算量
- makePalindrome: 最悪の場合O(n²)の時間計算量、O(n)の空間計算量

## バージョン情報

| バージョン番号   | 年月日        | 詳細                                              |
|:----------|:-----------|:------------------------------------------------|
| バージョン 1.0 | 2025-03-29 | 初期実装：基本的な回文判定機能                                 |
| バージョン 2.0 | 2025-07-06 | バグ修正と機能追加：範囲指定の回文判定、最長回文検索、回文変換機能を追加            |
| バージョン 2.1 | 2026-01-06 | クラス名を `PalindromUtils` から `PalindromNative` に変更 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
