# マスターユーティリティ (Master Utilities)

## 概要

競技プログラミングで頻繁に使用される様々なユーティリティ関数を提供するライブラリです。データ型変換、配列操作、数学的計算など、幅広い機能を含んでいます。

## 実装クラス

### Master

- **用途**: 基本的なデータ型変換と配列操作のためのユーティリティクラス
- **特徴**:
	- 様々なデータ型間の変換（int, long, char[], String, int[]など）
	- 数値や配列の反転操作
	- 数値や配列のソート（昇順・降順）

### Advance

- **用途**: より高度な数学的操作のためのユーティリティクラス
- **特徴**:
	- べき乗計算とモジュラーべき乗計算
	- 最大公約数（GCD）と最小公倍数（LCM）の計算
	- 配列の集計操作（合計、最小値、最大値）
	- 約数の計算

## 主なメソッド (Master)

| メソッド                                                               | 説明                 |
|--------------------------------------------------------------------|--------------------|
| `toInt(char[] arr)`, `toInt(String s)`                             | 文字配列または文字列を整数に変換   |
| `toLong(char[] arr)`, `toLong(String s)`                           | 文字配列または文字列を長整数に変換  |
| `toCharArray(int n)`, `toCharArray(long n)`                        | 数値を文字配列に変換         |
| `toString(int n)`, `toString(long n)`                              | 数値を文字列に変換          |
| `toIntArray(char[] arr)`, `toIntArray(String s)`                   | 文字配列または文字列を整数配列に変換 |
| `reverse(int n)`, `reverse(long n)`                                | 数値の桁を反転            |
| `reverse(int[] arr)`, `reverse(char[] arr)`, `reverse(long[] arr)` | 配列を反転              |
| `sort(int n)`, `sort(long n)`                                      | 数値の桁を昇順にソート        |
| `sort(char[] arr)`, `sort(int[] arr)`, `sort(long[] arr)`          | 配列を昇順にソート          |
| `descendingSort(int n)`, `descendingSort(long n)`                  | 数値の桁を降順にソート        |
| `descendingSort(char[] arr)`, `descendingSort(int[] arr)`          | 配列を降順にソート          |

## 主なメソッド (Advance)

| メソッド                                       | 説明                        |
|--------------------------------------------|---------------------------|
| `pow(long n, int k)`                       | nのk乗を計算                   |
| `modPow(long n, long k, long mod)`         | nのk乗をmodで割った余りを計算         |
| `gcd(int a, int b)`, `gcd(long a, long b)` | 2つの数の最大公約数を計算             |
| `gcd(int[] arr)`, `gcd(long[] arr)`        | 配列内の全ての数の最大公約数を計算         |
| `lcm(int a, int b)`, `lcm(long a, long b)` | 2つの数の最小公倍数を計算             |
| `lcm(int[] arr)`, `lcm(long[] arr)`        | 配列内の全ての数の最小公倍数を計算         |
| `sum(int[] arr)`, `sum(long[] arr)`        | 配列の合計を計算                  |
| `min(int[] arr)`, `min(long[] arr)`        | 配列の最小値を取得                 |
| `max(int[] arr)`, `max(long[] arr)`        | 配列の最大値を取得                 |
| `factorsHashSet(long n)`                   | 数値nの約数をHashSetとして取得       |
| `factorsTreeSet(long n)`                   | 数値nの約数をソートされたTreeSetとして取得 |

## 使用例

    // 文字列から整数への変換
    int num = Master.toInt("1234567890");

    // 整数から文字配列への変換（固定長）
    char[] digits = Master.toCharArray(1234567890, 8);

    // 数値の桁をソート
    int sorted = Master.sort(417253219);  // 123456789
    int descSorted = Master.descendingSort(417253219);  // 987654321

    // 文字配列から整数配列への変換
    char[] chars = {'1','2','3','4'};
    int[] intArray = Master.toIntArray(chars);  // [1, 2, 3, 4]

    // 配列の反転
    int[] reversed = intArray.clone();
    Master.reverse(reversed);  // [4, 3, 2, 1]

    // 最大公約数の計算
    int gcd = Advance.gcd(24, 36);  // 12

    // 最小公倍数の計算
    long lcm = Advance.lcm(4, 6);  // 12

    // べき乗計算
    long pow = Advance.pow(2, 10);  // 1024

    // 約数の取得
    HashSet<Integer> factors = Advance.factorsHashSet(12);  // [1, 2, 3, 4, 6, 12]

## 計算量

- 基本的な変換操作: O(n)、ここでnは入力の長さ
- ソート操作: O(n log n)、ここでnは入力の長さ
- GCD/LCM計算: O(log(min(a, b)))
- 約数計算: O(√n)