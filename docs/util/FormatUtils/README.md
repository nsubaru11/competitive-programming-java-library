# FormatUtils

[`FormatUtils`](../../../src/lib/util/FormatUtils.java) は、数値や配列を出力用の文字列へ変換します。

| 分類       | メソッド                                                               |
|------------|------------------------------------------------------------------------|
| 配列連結   | `join(int[])`, `join(long[])`, 区切り文字列指定版                      |
| 行列連結   | `joinLines(int[][]/long[][])`, 区切り文字指定版                        |
| 小数表示   | `formatDouble(double, int)`, `formatDouble(double, int, RoundingMode)` |
| パディング | `toPaddedString(long, int)`, 埋め文字・基数指定版                      |

`join` のデフォルト区切り文字は半角スペースです。`formatDouble` のデフォルト丸めは `RoundingMode.HALF_UP` で、`DOWN`、`UP`、`CEILING`、`FLOOR`、`HALF_DOWN`、`HALF_EVEN`、`UNNECESSARY` も指定できます。`digits` は `0` 以上 `18` 以下を前提とします。

`toPaddedString` は指定幅を最小幅として扱い、値が長い場合は切り詰めません。負数では符号を先頭に残してから埋めます。基数は `2` 以上 `36` 以下を指定でき、デフォルトは10進数です。
