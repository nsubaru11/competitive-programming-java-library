# Conversions

[`Conversions`](../../../src/lib/util/Conversions.java) は、数値・文字配列・数字配列の相互変換を提供します。

エンディアンを指定しない変換はビッグエンディアンです。
例えば `00121` はビッグエンディアンでは `121`、リトルエンディアンでは `12100` として解釈されます。

| 分類       | メソッド                                                                               |
|------------|----------------------------------------------------------------------------------------|
| 整数化     | `toInt(char[])`, `toInt(char[], boolean)`, `toLong(char[])`, `toLong(char[], boolean)` |
| 数字配列化 | `toInt(int[])`, `toInt(int[], boolean)`, `toLong(int[])`, `toLong(int[], boolean)`     |
| 文字配列化 | `toCharArray(int/long)`, 桁数指定版、エンディアン指定版、`toCharArray(int[])`          |
| 数字配列化 | `toIntArray(char[])`, `toIntArray(String)`                                             |

数値を受け取るメソッドは非負整数、数字配列の各要素は `0` 以上 `9` 以下を前提とします。桁数指定版は不足分を `0` で埋め、指定桁数を超える上位桁を切り捨てます。
