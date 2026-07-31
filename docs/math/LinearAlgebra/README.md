# lib.math.linearalgebra

行列演算と線形代数を扱うパッケージです。

## 実装クラス

### [IntMatrixUtils](../../../src/lib/math/linearalgebra/IntMatrixUtils.java)

- `int[][]`の行列加減算・乗算・累乗・剰余演算
- 戻り値を新しい配列にする版と、引数を書き換える`Raw`版を提供

### [LongMatrixUtils](../../../src/lib/math/linearalgebra/LongMatrixUtils.java)

- `long[][]`向けの同等API

### [LinearAlgebra](../../../src/lib/math/linearalgebra/LinearAlgebra.java)

- 掃き出し法、行列式、ランク計算を配置するクラス
- 現在は未実装

### [Matrix](../../../src/lib/math/linearalgebra/Matrix.java)

- `Fraction`を内部表現に使う行列型
- 現在はパッケージ外から構築できないため、公開APIとしては利用不可

## 注意事項

- `Raw`版は入力配列を直接変更します。
- 行列の形状と演算可能性は呼び出し側で保証します。
- 整数演算のオーバーフローは呼び出し側の制約で避けます。
