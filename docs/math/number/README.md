# lib.math.number

数値型として`Fraction`、`Int128`、`ModInt`、`ModLong`を提供する。

## クラス一覧

| クラス     | 用途                       | Guide | ソース                                             | Example / 検証                                                    |
|------------|----------------------------|-------|----------------------------------------------------|-------------------------------------------------------------------|
| `Fraction` | `long`分子・分母による分数 | —     | [Java](../../../src/lib/math/number/Fraction.java) | —                                                                 |
| `Int128`   | 128bit符号付き整数         | —     | [Java](../../../src/lib/math/number/Int128.java)   | [Judge1.java](../../../test/judge/math/number/int128/Judge1.java) |
| `ModInt`   | 法付き`int`値              | —     | [Java](../../../src/lib/math/number/ModInt.java)   | —                                                                 |
| `ModLong`  | 法付き`long`値             | —     | [Java](../../../src/lib/math/number/ModLong.java)  | —                                                                 |

| クラス                                                   | 説明                                             |
|----------------------------------------------------------|--------------------------------------------------|
| [`Fraction`](../../../src/lib/math/number/Fraction.java) | `long`分子・分母による分数                       |
| [`Int128`](../../../src/lib/math/number/Int128.java)     | 128bit符号付き整数。`Number`と`Comparable`を実装 |
| [`ModInt`](../../../src/lib/math/number/ModInt.java)     | `int`値と法を保持する`Number`                    |
| [`ModLong`](../../../src/lib/math/number/ModLong.java)   | `long`値と法を保持する`Number`                   |

`ModInt`と`ModLong`は`Number`を継承する。二項演算では、両辺の法が等しいことを前提とする。

詳細資料: [ModInt](./ModInt.md) · [ModLong](./ModLong.md)
