# lib.math.polynomial

多項式演算と各種畳み込みを扱うパッケージです。

## 実装クラス

### [PolynomialUtils](../../../src/lib/math/polynomial/PolynomialUtils.java)

- `double[]`を係数列とする加算・減算・乗算・微分・積分・評価・次数・GCD
- 素朴な乗算は時間計算量$\mathcal{O}(NM)$
- 浮動小数点演算の丸め誤差を含みます。

### [Convolution](../../../src/lib/math/polynomial/Convolution.java)

- NTT、FFT、XOR・AND・OR、GCD・LCM畳み込みの公開API
- 内部変換とGarner復元は未実装であり、現在は正しい結果を返しません。
- 設計は[Convolution設計書](./ConvolutionArchitecture.md)を参照してください。

## 注意事項

- `Convolution`は実装完了まで利用できません。
- `PolynomialUtils`の係数は次数の昇順に格納します。
