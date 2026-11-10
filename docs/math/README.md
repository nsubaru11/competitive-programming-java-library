# lib.math

`src/lib/math` の公開APIに対応するドキュメントである。ルートパッケージのクラスはクラス名ごと、サブパッケージはパッケージ名ごとに整理している。

| ドキュメント                                     | 対象                        | 用途                                       | ソース                                                       |
|--------------------------------------------------|-----------------------------|--------------------------------------------|--------------------------------------------------------------|
| [MathUtils](./mathutils/README.md)               | `lib.math.MathUtils`        | 整数演算・組み合わせ・数論の主要な静的入口 | [MathUtils](../../src/lib/math/MathUtils.java)               |
| [FactorialTable](./factorialtable/README.md)     | `lib.math.FactorialTable`   | 素数mod上の動的な階乗・逆元テーブル        | [FactorialTable](../../src/lib/math/FactorialTable.java)     |
| [FactorUtils](./factorutils/README.md)           | `lib.math.FactorUtils`      | 素因数分解、素因数個数、約数列挙           | [FactorUtils](../../src/lib/math/FactorUtils.java)           |
| [PrimeUtils](./primeutils/README.md)             | `lib.math.PrimeUtils`       | 一回限りの素数判定・篩                     | [PrimeUtils](../../src/lib/math/PrimeUtils.java)             |
| [PrimeTable](./primetable/README.md)             | `lib.math.PrimeTable`       | 再利用可能な素数テーブル                   | [PrimeTable](../../src/lib/math/PrimeTable.java)             |
| [NumberPredicates](./numberpredicates/README.md) | `lib.math.NumberPredicates` | 完全数・回文数などの判定                   | [NumberPredicates](../../src/lib/math/NumberPredicates.java) |
| [GeometryUtils](./geometryutils/README.md)       | `lib.math.GeometryUtils`    | 交差・包含・距離計算                       | [GeometryUtils](../../src/lib/math/GeometryUtils.java)       |
| [number](./number/README.md)                     | `lib.math.number`           | `Fraction`、`Int128`、`ModInt`、`ModLong`  | —                                                            |
| [LinearAlgebra](./linearalgebra/README.md)       | `lib.math.linearalgebra`    | 行列演算と未実装の線形代数API              | —                                                            |
| [Polynomial](./polynomial/README.md)             | `lib.math.polynomial`       | 多項式と畳み込み                           | —                                                            |

## 個別クラス

専用ページがない実装も含め、ルートパッケージのクラスと幾何サブパッケージを一覧する。未実装の雛形はクラス名に状態を付記している。

| クラス                              | ソース                                                                     | 用途                        |
|-------------------------------------|----------------------------------------------------------------------------|-----------------------------|
| `BerlekampMassey`（未実装）         | [BerlekampMassey](../../src/lib/math/BerlekampMassey.java)                 | 数列から線形漸化式を復元    |
| `ChineseRemainderTheorem`（未実装） | [ChineseRemainderTheorem](../../src/lib/math/ChineseRemainderTheorem.java) | 中国剰余定理                |
| `DiscreteLogarithm`（未実装）       | [DiscreteLogarithm](../../src/lib/math/DiscreteLogarithm.java)             | 離散対数                    |
| `FloorSum`（未実装）                | [FloorSum](../../src/lib/math/FloorSum.java)                               | floor関数を含む整数和       |
| `Kitamasa`（未実装）                | [Kitamasa](../../src/lib/math/Kitamasa.java)                               | 線形漸化式の高速計算        |
| `ModularSquareRoot`（未実装）       | [ModularSquareRoot](../../src/lib/math/ModularSquareRoot.java)             | 法を指定した平方根          |
| `PollardRho`（未実装）              | [PollardRho](../../src/lib/math/PollardRho.java)                           | Pollard rho法による因数分解 |
| `ConvexHull`（未実装）              | [ConvexHull](../../src/lib/math/geometry/ConvexHull.java)                  | 凸包の構築                  |

一回だけの整数計算は`MathUtils`、同じ法で階乗・組み合わせを繰り返す場合は`FactorialTable`、素数表を再利用する場合は`PrimeTable`を選択する。

## 内部パッケージ

| パッケージ               | ドキュメント                                |
|--------------------------|---------------------------------------------|
| `lib.math.geometry`      | [GeometryUtils](./geometryutils/README.md)  |
| `lib.math.linearalgebra` | [Linear Algebra](./linearalgebra/README.md) |
| `lib.math.number`        | [Number types](./number/README.md)          |
| `lib.math.polynomial`    | [Polynomial](./polynomial/README.md)        |
