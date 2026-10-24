# lib.math.polynomial

多項式演算と各種畳み込みを扱うパッケージである。

## クラス一覧

| クラス | 用途 | Guide | ソース | Example / 検証 |
|--------|------|-------|--------|----------------|
| `PolynomialUtils` | `double[]`係数の加減算・乗算・微積分・評価・次数・GCD。 | — | [Java](../../../src/lib/math/polynomial/PolynomialUtils.java) | [Example.java](../../../test/verify/math/polynomial/polynomialutils/Example.java) |
| `Convolution` | NTTによる多項式畳み込みと、各種演算の畳み込み。 | — | [Java](../../../src/lib/math/polynomial/Convolution.java) | [Check1.java](../../../test/verify/math/polynomial/convolution/Check1.java) |
| `Transform` | NTT・FWHT・ゼータ変換・メビウス変換。 | — | [Java](../../../src/lib/math/polynomial/Transform.java) | [Check1.java](../../../test/verify/math/polynomial/transform/Check1.java) |
| `FormalPowerSeries` | 形式的冪級数の操作をまとめる未実装クラス。 | — | [Java](../../../src/lib/math/polynomial/FormalPowerSeries.java) | — |

## 実装クラス

### [PolynomialUtils](../../../src/lib/math/polynomial/PolynomialUtils.java)

- `double[]`を係数列とする加算・減算・乗算・微分・積分・評価・次数・GCD
- 素朴な乗算は時間計算量$\mathcal{O}(NM)$
- 浮動小数点演算の丸め誤差を含む。

### [Convolution](../../../src/lib/math/polynomial/Convolution.java)

- NTT、FFT、XOR・AND・OR、GCD・LCM畳み込みの公開API
- `convolveAnd` / `convolveOr`は、部分集合・上位集合ゼータ変換を用いて利用できる。`convolveXor`はFWHTを、`convolveGcd` / `convolveLcm`は倍数・約数ゼータ変換を用いて利用できる。
- 通常の多項式畳み込みは、内部の変換長にかかわらず`a.length + b.length - 1`要素を返す。
- NTTおよび3素数NTTとGarner復元による任意mod畳み込みを利用できる。FFTは未実装である。
- 設計は[Convolution設計書](./ConvolutionArchitecture.md)を参照すること。

### [Transform](../../../src/lib/math/polynomial/Transform.java)

- 畳み込みで使う各種変換の公開API。コピー版は入力を変更せず、必要なら0埋めした新しい配列を作成して対応する`*InPlace`版へ処理を委譲する。
- `*InPlace`版は渡された配列を直接変更する。2冪長が必要な変換では、条件を満たさない配列に対して`false`を返す。
- FWHT、部分集合・上位集合・約数・倍数ゼータ変換とメビウス変換は利用可能である。
- NTTを利用できる。FFTは未実装である。
- 正確なオーバーロードと配列長の規約は[Convolution設計書](./ConvolutionArchitecture.md)を参照すること。

### [FormalPowerSeries](../../../src/lib/math/polynomial/FormalPowerSeries.java)（未実装）

- 形式的冪級数の操作をまとめるクラスである。公開APIと実装状況はソースを参照すること。

## 注意事項

- `convolveAnd` / `convolveOr` / `convolveXor`は任意の正の入力長を受け取り、最大入力長以上の最小の2の冪へ0埋めした定義域長の結果を返す。係数は非負かつ剰余版では`[0, mod)`に正規化して渡すこと。
- `fwht`の剰余版で逆変換を行うには、変換長と`mod`が互いに素である必要がある。通常の奇素数modでは満たされる。
- `ntt`では`mod`が素数であり、変換長が`mod - 1`を割り切ることを前提とする。係数は`[0, mod)`に正規化して渡すこと。
- `convolveGcd` / `convolveLcm`では配列添字`t`が数学上の整数`t + 1`を表す。返却長は最大入力長であり、LCM畳み込みではその定義域外となる添字の結果を返さない。
- `Convolution`の通常多項式畳み込みは、NTT向けの法では`convolveNtt`、それ以外の法では`convolveArbitraryMod`を利用できる。FFTは未実装である。
- `PolynomialUtils`の係数は次数の昇順に格納する。

## Example / 検証

- [Example.java](../../../test/verify/math/polynomial/polynomialutils/Example.java)
