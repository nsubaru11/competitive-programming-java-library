# lib.math.polynomial

多項式演算と各種畳み込みを扱うパッケージです。

## 実装クラス

### [PolynomialUtils](../../../src/lib/math/polynomial/PolynomialUtils.java)

- `double[]`を係数列とする加算・減算・乗算・微分・積分・評価・次数・GCD
- 素朴な乗算は時間計算量$\mathcal{O}(NM)$
- 浮動小数点演算の丸め誤差を含みます。

### [Convolution](../../../src/lib/math/polynomial/Convolution.java)

- NTT、FFT、XOR・AND・OR、GCD・LCM畳み込みの公開API
- `convolveAnd` / `convolveOr`は、部分集合・上位集合ゼータ変換を用いて利用できます。
- NTT、FFT、XOR、GCD・LCM畳み込み、およびGarner復元は未実装です。
- 設計は[Convolution設計書](./ConvolutionArchitecture.md)を参照してください。

### [Transform](../../../src/lib/math/polynomial/Transform.java)

- 畳み込みで使う各種変換の公開API。すべてのメソッドは入力を変更せず、必要なら0埋めした新しい配列を返します。
- 部分集合・上位集合ゼータ変換とメビウス変換は利用可能です。
- NTT、FFT、FWHT、約数・倍数ゼータ変換とメビウス変換は未実装です。
- 正確なオーバーロードと配列長の規約は[Convolution設計書](./ConvolutionArchitecture.md)を参照してください。

## 注意事項

- `convolveAnd` / `convolveOr`は、配列長の最大値が2の冪であるビット集合を対象に利用できます。係数は非負かつ剰余版では`[0, mod)`に正規化して渡してください。
- `Convolution`のその他の畳み込みは実装完了まで利用できません。
- `PolynomialUtils`の係数は次数の昇順に格納します。
