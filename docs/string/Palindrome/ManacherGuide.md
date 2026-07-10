# Manacher 利用ガイド

## 概要

Manacherクラスは、Manacher'sアルゴリズムを実装して文字列内の回文を効率的に検出するためのクラスです。
このアルゴリズムは線形時間（O(n)）で文字列内のすべての回文を見つけることができ、最長回文部分文字列の検索や任意の部分文字列が回文かどうかの判定に特に有用です。

## 特徴

- 線形時間（O(n)）で全ての回文を検出する効率的なアルゴリズム
- 奇数長と偶数長の両方の回文に対応
- 最長回文部分文字列の取得
- 任意の部分文字列が回文かどうかの高速判定
- 特定の位置を中心とする回文の取得

## 依存関係

- import java.nio.CharBuffer
- java.lang.Math.max（静的インポート）

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                 | 説明                                    |
|----------------------|---------------------------------------|
| Manacher(String str) | 文字列を受け取り、Manacher'sアルゴリズムを実行して初期化します  |
| Manacher(char[] chr) | 文字配列を受け取り、Manacher'sアルゴリズムを実行して初期化します |

### 2. 回文長さ取得系メソッド

| メソッド                                  | 戻り値の型 | 説明                               |
|---------------------------------------|-------|----------------------------------|
| getLongestLen()                       | int   | 最長回文の長さを返します                     |
| getOddLongestLen()                    | int   | 最長の奇数長の回文の長さを返します                |
| getEvenLongestLen()                   | int   | 最長の偶数長の回文の長さを返します                |
| getOddPalindromeLengthAt(int center)  | int   | 指定した文字位置を中心とする奇数長の回文列の長さを返します    |
| getEvenPalindromeLengthAt(int center) | int   | 指定した文字位置の直後を中心とする偶数長の回文列の長さを返します |

### 3. 回文文字列取得系メソッド

| メソッド                            | 戻り値の型  | 説明                                |
|---------------------------------|--------|-----------------------------------|
| getLongestPalindrome()          | String | 最長回文部分文字列を返します                    |
| getLongestOddPalindrome()       | String | 最長の奇数長の回文部分文字列を返します               |
| getLongestEvenPalindrome()      | String | 最長の偶数長の回文部分文字列を返します               |
| getOddPalindromeAt(int center)  | String | 指定した文字位置を中心とする奇数長の回文部分文字列を返します    |
| getEvenPalindromeAt(int center) | String | 指定した文字位置の直後を中心とする偶数長の回文部分文字列を返します |

### 4. 判定系メソッド

| メソッド                            | 戻り値の型   | 説明                          |
|---------------------------------|---------|-----------------------------|
| isPalindromeRange(int l, int r) | boolean | 指定した区間 [l, r) が回文かどうかを判定します |

### 5. その他

| メソッド                   | 戻り値の型 | 説明                   |
|------------------------|-------|----------------------|
| getPalindromeLengths() | int[] | 内部で計算された回文長さの配列を返します |

## 利用例

```java
public class Example {
	public static void main(String[] args) {
		String text = "abacaba";
		Manacher manacher = new Manacher(text);

		// 最長回文の長さと文字列を取得
		System.out.println("最長回文の長さ: " + manacher.getLongestLen());
		System.out.println("最長回文: " + manacher.getLongestPalindrome());

		// 特定の位置を中心とする回文を取得
		System.out.println("位置3を中心とする奇数長回文: " + manacher.getOddPalindromeAt(3));
		System.out.println("位置2の直後を中心とする偶数長回文: " + manacher.getEvenPalindromeAt(2));

		// 範囲が回文かどうかを判定
		System.out.println("範囲[1, 6)は回文か: " + manacher.isPalindromeRange(1, 6));
	}
}
```

## 注意事項

- isPalindromeRangeメソッドの引数rは含まれません（半開区間[l, r)）
- 文字列の長さが0の場合、結果は未定義です
- 中心位置を指定するメソッドでは、範囲外の位置を指定するとIndexOutOfBoundsExceptionが発生する可能性があります

## パフォーマンス特性

- 初期化（コンストラクタ）: O(n)の時間計算量、O(n)の空間計算量
- 長さ取得系メソッド: O(1)の時間計算量
- 文字列取得系メソッド: O(1)の時間計算量
- isPalindromeRange: O(1)の時間計算量

## バージョン情報

| バージョン番号   | 年月日        | 詳細                                  |
|:----------|:-----------|:------------------------------------|
| バージョン 1.0 | 2025-03-29 | 初期実装：基本的なManacher'sアルゴリズムの実装        |
| バージョン 2.0 | 2025-07-06 | 機能追加：各種回文文字列取得メソッドの追加               |
| バージョン 2.1 | 2025-07-06 | 修正：内部実装をradiiからpalindromeLengthsに変更 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
