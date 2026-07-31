# PrimeUtils

[`PrimeUtils`](../../../src/lib/math/PrimeUtils.java) は、一回限りの素数判定、素数個数、範囲内の素数列挙を提供します。

- `isPrime`: 6k±1の試し割り
- `isProbablePrime`: `BigInteger.isProbablePrime`による確率的判定
- `primeCount`: `BitSet`を使った篩による個数計算
- `eratosthenes`: 昇順の配列または指定Collectionへ列挙

同じ上限に対する問い合わせを繰り返す場合は[`PrimeTable`](../PrimeTable)を使用します。詳細は[利用ガイド](./PrimeUtilsGuide.md)を参照してください。
