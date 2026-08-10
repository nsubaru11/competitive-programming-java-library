# FactorUtils

[`FactorUtils`](../../../src/lib/math/FactorUtils.java) は、一回限りの素因数分解と正の約数列挙を提供します。

- 素因数配列は重複込みの昇順
- 素因数と指数を`[2][k]`のint / long配列で取得可能
- 素因数と指数のMap、任意のCollectionへの格納に対応
- 重複込み／異なる素因数の個数を直接計算
- 約数配列は昇順で、ソートを行わず大小2群から構築

詳細は[利用ガイド](./FactorUtilsGuide.md)を参照してください。
