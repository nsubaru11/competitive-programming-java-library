# DigitUtils

[`DigitUtils`](../../../src/lib/util/DigitUtils.java) は、非負整数の桁数取得と十進表現の操作を提供します。

数値版の `sort` / `descendingSort` は、桁の値が `0` から `9` に限られることを利用したカウント方式です。
`String` 版は一般の文字列を扱うため `Arrays.sort` を使用します。

| 分類       | メソッド                                  |
|------------|-------------------------------------------|
| 桁数       | `digits2(int/long)`, `digits10(int/long)` |
| 反転       | `reverse(int/long)`                       |
| 昇順ソート | `sort(int/long/String)`                   |
| 降順ソート | `descendingSort(int/long/String)`         |

数値を受け取るメソッドは非負整数を前提とします。`sort(String)` は文字コード順に文字を並べ替えます。
