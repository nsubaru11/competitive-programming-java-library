# 回文 (Palindrome) アルゴリズム

## 概要

回文（前から読んでも後ろから読んでも同じ文字列）を効率的に検出・操作するためのアルゴリズムとユーティリティを提供します。Manacherのアルゴリズムを用いた線形時間での回文検出や、様々な回文関連の操作をサポートしています。

## 実装クラス

### Manacher

- **用途**: 線形時間で文字列内の全ての回文を検出するアルゴリズム
- **特徴**:
	- O(n)の時間計算量で全ての回文を検出
	- 奇数長と偶数長の両方の回文に対応
	- 特定の位置を中心とする回文の取得
	- 最長回文の検出
	- 範囲が回文かどうかの判定
- **時間計算量**: O(n)、ここでnは文字列の長さ
- **空間計算量**: O(n)

### PalindromeUtils

- **用途**: 回文に関する様々なユーティリティ関数を提供
- **特徴**:
	- 文字列や文字配列が回文かどうかの判定
	- 文字列や文字配列の特定範囲が回文かどうかの判定
	- 最長回文部分文字列の検出（単純なO(n²)アルゴリズム）
	- 文字列を回文に変換する機能（最小の文字を追加）

## 主なメソッド

### Manacher

| メソッド                              | 説明                    |
|-----------------------------------|-----------------------|
| `getPalindromeLengths()`          | 各位置を中心とする回文の長さの配列を取得  |
| `isPalindromeRange(int l, int r)` | 指定された範囲が回文かどうかを判定     |
| `getLongestLen()`                 | 最長回文の長さを取得            |
| `getOddLongestLen()`              | 最長奇数長回文の長さを取得         |
| `getEvenLongestLen()`             | 最長偶数長回文の長さを取得         |
| `getLongestPalindrome()`          | 最長回文を取得               |
| `getLongestOddPalindrome()`       | 最長奇数長回文を取得            |
| `getLongestEvenPalindrome()`      | 最長偶数長回文を取得            |
| `getOddPalindromeAt(int center)`  | 指定位置を中心とする奇数長回文を取得    |
| `getEvenPalindromeAt(int center)` | 指定位置の直後を中心とする偶数長回文を取得 |

### PalindromeUtils

| メソッド                                              | 説明                  |
|---------------------------------------------------|---------------------|
| `isPalindrome(String s)`                          | 文字列が回文かどうかを判定       |
| `isPalindrome(char[] c)`                          | 文字配列が回文かどうかを判定      |
| `isPalindromeRange(String s, int start, int end)` | 文字列の指定範囲が回文かどうかを判定  |
| `isPalindromeRange(char[] c, int start, int end)` | 文字配列の指定範囲が回文かどうかを判定 |
| `findLongestPalindrome(String s)`                 | 文字列内の最長回文部分文字列を検出   |
| `makePalindrome(String s)`                        | 文字列に最小の文字を追加して回文に変換 |

## 使用例

    // Manacherアルゴリズムの使用例
    String text = "levelracecarrefer";
    Manacher mc = new Manacher(text);
    
    // 基本情報の取得
    int[] palindromeLengths = mc.getPalindromeLengths();
    int longestPalindromeLength = mc.getLongestLen();
    
    // 最長回文の取得
    String longestPalindrome = mc.getLongestPalindrome();
    String longestOddPalindrome = mc.getLongestOddPalindrome();
    String longestEvenPalindrome = mc.getLongestEvenPalindrome();
    
    // 特定位置の回文
    String oddPalindromeAt2 = mc.getOddPalindromeAt(2);
    String evenPalindromeAt4 = mc.getEvenPalindromeAt(4);
    
    // 範囲判定
    boolean isRangePalindrome = mc.isPalindromeRange(1, 6);
    
    // PalindromeUtilsの使用例
    boolean isPalindrome = PalindromeUtils.isPalindrome("racecar");
    boolean isRangePalindrome2 = PalindromeUtils.isPalindromeRange("levelupracecar", 0, 5);
    String longestPalindrome2 = PalindromeUtils.findLongestPalindrome("babad");
    String convertedToPalindrome = PalindromeUtils.makePalindrome("abc");  // "abcba"

## 注意事項

- Manacherアルゴリズムは線形時間で動作するため、大きな文字列でも効率的に処理できます
- PalindromeUtilsの`findLongestPalindrome`メソッドは単純なO(n²)アルゴリズムを使用しています。大きな文字列では、Manacherクラスを使用することをお勧めします
- 文字列のインデックスは0から始まります