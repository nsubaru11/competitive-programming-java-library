# Conversions

[`Conversions`](../../../src/lib/util/Conversions.java) は、数字文字列・配列・整数の相互変換と、十進桁の反転・並べ替えを提供します。

| 分類       | メソッド                                                             |
|------------|----------------------------------------------------------------------|
| 整数化     | `toInt(char[])`, `toInt(String)`, `toLong(char[])`, `toLong(String)` |
| 文字配列化 | `toCharArray(int/long)`, 桁数指定版, `toCharArray(int[])`            |
| 文字列化   | `toString(int[])`, `toString(long[])`, 2次元配列版                   |
| 数字配列化 | `toIntArray(char[])`, `toIntArray(String)`                           |
| 十進桁操作 | `reverse(int/long)`, `sort(int/long/String)`, `descendingSort(...)`  |

符号や区切りを含まない数字列など、各メソッドが想定する競技入力を前提とします。桁数指定の`toCharArray`は不足分を`0`で埋め、指定桁数を超える上位桁を切り捨てます。配列の一般操作は`ArrayUtils`が提供します。
