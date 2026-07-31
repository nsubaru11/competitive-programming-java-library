# FactorialTable

[`FactorialTable`](../../../src/lib/math/FactorialTable.java) は、素数mod上の階乗、逆元、階乗逆元を必要に応じて拡張するテーブルです。

- 既定の法は`998244353`
- 構築済み範囲の`fact`、`inv`、`invFact`を$\mathcal{O}(1)$で取得
- 構築済み範囲の`nCr`、`nPr`、`nHr`、Catalan数、Lah数、Narayana数、Ballot数を$\mathcal{O}(1)$で計算
- Lucasの定理は提供せず、逆元を使うメソッドは参照添字が`mod`未満であることを前提とする

詳細は[利用ガイド](./FactorialTableGuide.md)を参照してください。
