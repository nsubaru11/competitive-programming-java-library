# MathUtils

[`MathUtils`](../../../src/lib/math/MathUtils.java) は、整数の最小・最大、べき乗、GCD、階乗、組み合わせ、Eulerのトーシェント関数などを集約した`lib.math`の主要な静的入口です。

- 一回限りの計算を対象とし、前計算を必要としません。
- 素数判定は内部で`PrimeUtils`へ委譲します。
- 同じmodで階乗や組み合わせを繰り返す場合は[`FactorialTable`](../FactorialTable)を使用します。
- 素因数分解と約数列挙は[`FactorUtils`](../FactorUtils)を使用します。

全メソッド、前提条件、計算量は[利用ガイド](./MathUtilsGuide.md)を参照してください。
