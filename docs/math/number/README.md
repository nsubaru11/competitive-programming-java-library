# lib.math.number

数値型として`Fraction`、`Int128`、`ModInt`、`ModLong`を提供します。

| クラス                                                   | 説明                                             |
|----------------------------------------------------------|--------------------------------------------------|
| [`Fraction`](../../../src/lib/math/number/Fraction.java) | `long`分子・分母による分数                       |
| [`Int128`](../../../src/lib/math/number/Int128.java)     | 128bit符号付き整数。`Number`と`Comparable`を実装 |
| [`ModInt`](./ModInt.md)                                  | `int`値と法を保持する`Number`                    |
| [`ModLong`](./ModLong.md)                                | `long`値と法を保持する`Number`                   |

`ModInt`と`ModLong`は`Number`を継承します。二項演算では、両辺の法が等しいことを前提とします。
