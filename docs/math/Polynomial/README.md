# lib.math.polynomial

多項式演算と各種畳み込みを扱うパッケージです。

## 実装クラス

### [PolynomialUtils](../../../src/lib/math/polynomial/PolynomialUtils.java)

- `double[]`を係数列とする加算・減算・乗算・微分・積分・評価・次数・GCD
- 素朴な乗算は時間計算量$\mathcal{O}(NM)$
- 浮動小数点演算の丸め誤差を含みます。

### [Convolution](../../../src/lib/math/polynomial/Convolution.java)

- NTT、FFT、XOR・AND・OR、GCD・LCM畳み込みの公開API
- `convolveAnd` / `convolveOr`は、部分集合・上位集合ゼータ変換を用いて利用できます。`convolveXor`はFWHTを、`convolveGcd` / `convolveLcm`は倍数・約数ゼータ変換を用いて利用できます。
- 通常の多項式畳み込みは、内部の変換長にかかわらず`a.length + b.length - 1`要素を返します。
- NTTおよび3素数NTTとGarner復元による任意mod畳み込みを利用できます。FFTは未実装です。
- 設計は[Convolution設計書](./ConvolutionArchitecture.md)を参照してください。

### [Transform](../../../src/lib/math/polynomial/Transform.java)

- 畳み込みで使う各種変換の公開API。コピー版は入力を変更せず、必要なら0埋めした新しい配列を作成して対応する`*InPlace`版へ処理を委譲します。
- `*InPlace`版は渡された配列を直接変更します。2冪長が必要な変換では、条件を満たさない配列に対して`false`を返します。
- FWHT、部分集合・上位集合・約数・倍数ゼータ変換とメビウス変換は利用可能です。
- NTTを利用できます。FFTは未実装です。
- 正確なオーバーロードと配列長の規約は[Convolution設計書](./ConvolutionArchitecture.md)を参照してください。

## 注意事項

- `convolveAnd` / `convolveOr` / `convolveXor`は任意の正の入力長を受け取り、最大入力長以上の最小の2の冪へ0埋めした定義域長の結果を返します。係数は非負かつ剰余版では`[0, mod)`に正規化して渡してください。
- `fwht`の剰余版で逆変換を行うには、変換長と`mod`が互いに素である必要があります。通常の奇素数modでは満たされます。
- `ntt`では`mod`が素数であり、変換長が`mod - 1`を割り切ることを前提とします。係数は`[0, mod)`に正規化して渡してください。
- `convolveGcd` / `convolveLcm`では配列添字`t`が数学上の整数`t + 1`を表します。返却長は最大入力長であり、LCM畳み込みではその定義域外となる添字の結果を返しません。
- `Convolution`の通常多項式畳み込みは、NTT向けの法では`convolveNtt`、それ以外の法では`convolveArbitraryMod`を利用できます。FFTは未実装です。
- `PolynomialUtils`の係数は次数の昇順に格納します。
