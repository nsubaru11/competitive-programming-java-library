# PrimeTable

[`PrimeTable`](../../../src/lib/math/PrimeTable.java) は、指定上限までの素数を篩で構築し、判定・検索・列挙・素因数分解へ再利用するテーブルです。

- 奇数のみをビットで管理
- 素数判定$\mathcal{O}(1)$、近傍・個数検索$\mathcal{O}(\log \pi(N))$
- `PrimitiveIterator.OfLong`と`LongStream`に対応
- 列挙済み素数を使った反復的な素因数分解に対応

詳細は[利用ガイド](./PrimeTableGuide.md)を参照してください。
