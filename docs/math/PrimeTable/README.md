# PrimeTable

[`PrimeTable`](../../../src/lib/math/PrimeTable.java) は、指定上限までの素数を篩で構築し、判定・検索・列挙・素因数分解へ再利用するテーブルです。

- 6k±1の候補を走査し、奇数のみをビットで管理する篩
- 素数判定$\mathcal{O}(1)$、近傍・個数検索$\mathcal{O}(\log \pi(N))$
- `PrimitiveIterator.OfLong`と`LongStream`に対応
- 列挙済み素数を使った反復的な素因数分解に対応
- FactorUtilsと同等の配列・Collection・Map APIを提供
- FactorUtils と同等の配列・Collection・Map APIを提供

詳細は[利用ガイド](./PrimeTableGuide.md)を参照してください。
