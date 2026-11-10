# lib.math.number

数値型として`Fraction`、`Int128`、`ModInt`、`ModLong`を提供する。

## クラス一覧

| クラス     | 用途                       | Guide | ソース                                                 | 利用例                                                            | テスト                                                                                                                |
|------------|----------------------------|-------|--------------------------------------------------------|-------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `Fraction` | `long`分子・分母による分数 | —     | [Fraction](../../../src/lib/math/number/Fraction.java) | [Example](../../../test/example/math/number/FractionExample.java) | [Unit](../../../test/unit/math/number/FractionTest.java)                                                              |
| `Int128`   | 128bit符号付き整数         | —     | [Int128](../../../src/lib/math/number/Int128.java)     | [Example](../../../test/example/math/number/Int128Example.java)   | [Unit](../../../test/unit/math/number/Int128Test.java) · [Judge1](../../../test/judge/math/number/int128/Judge1.java) |
| `ModInt`   | 法付き`int`値              | —     | [ModInt](../../../src/lib/math/number/ModInt.java)     | [Example](../../../test/example/math/number/ModIntExample.java)   | [Unit](../../../test/unit/math/number/ModIntTest.java)                                                                |
| `ModLong`  | 法付き`long`値             | —     | [ModLong](../../../src/lib/math/number/ModLong.java)   | [Example](../../../test/example/math/number/ModLongExample.java)  | [Unit](../../../test/unit/math/number/ModLongTest.java)                                                               |

| クラス                                                   | 説明                                             |
|----------------------------------------------------------|--------------------------------------------------|
| [`Fraction`](../../../src/lib/math/number/Fraction.java) | `long`分子・分母による分数                       |
| [`Int128`](../../../src/lib/math/number/Int128.java)     | 128bit符号付き整数。`Number`と`Comparable`を実装 |
| [`ModInt`](../../../src/lib/math/number/ModInt.java)     | `int`値と法を保持する`Number`                    |
| [`ModLong`](../../../src/lib/math/number/ModLong.java)   | `long`値と法を保持する`Number`                   |

`ModInt`と`ModLong`は`Number`を継承する。二項演算では、両辺の法が等しいことを前提とする。

詳細資料: [ModInt](./ModInt.md) · [ModLong](./ModLong.md)
