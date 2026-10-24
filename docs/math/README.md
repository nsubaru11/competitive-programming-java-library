# lib.math

`src/lib/math` の公開APIに対応するドキュメントである。ルートパッケージのクラスはクラス名ごと、サブパッケージはパッケージ名ごとに整理している。

| ドキュメント                           | 対象                        | 用途                                       | ソース |
|----------------------------------------|-----------------------------|--------------------------------------------|--------|
| [MathUtils](./mathutils)               | `lib.math.MathUtils`        | 整数演算・組み合わせ・数論の主要な静的入口 | [Java](../../src/lib/math/MathUtils.java) |
| [FactorialTable](./factorialtable)     | `lib.math.FactorialTable`   | 素数mod上の動的な階乗・逆元テーブル        | [Java](../../src/lib/math/FactorialTable.java) |
| [FactorUtils](./factorutils)           | `lib.math.FactorUtils`      | 素因数分解、素因数個数、約数列挙           | [Java](../../src/lib/math/FactorUtils.java) |
| [PrimeUtils](./primeutils)             | `lib.math.PrimeUtils`       | 一回限りの素数判定・篩                     | [Java](../../src/lib/math/PrimeUtils.java) |
| [PrimeTable](./primetable)             | `lib.math.PrimeTable`       | 再利用可能な素数テーブル                   | [Java](../../src/lib/math/PrimeTable.java) |
| [NumberPredicates](./numberpredicates) | `lib.math.NumberPredicates` | 完全数・回文数などの判定                   | [Java](../../src/lib/math/NumberPredicates.java) |
| [GeometryUtils](./geometryutils)       | `lib.math.GeometryUtils`    | 交差・包含・距離計算                       | [Java](../../src/lib/math/GeometryUtils.java) |
| [number](./number)                     | `lib.math.number`           | `Fraction`、`Int128`、`ModInt`、`ModLong`  | [src](../../src/lib/math/number) |
| [LinearAlgebra](./linearalgebra)       | `lib.math.linearalgebra`    | 行列演算と未実装の線形代数API              | [src](../../src/lib/math/linearalgebra) |
| [Polynomial](./polynomial)             | `lib.math.polynomial`       | 多項式と畳み込み                           | [src](../../src/lib/math/polynomial) |

## 個別クラス

専用ページがない実装も含め、ルートパッケージのクラスと幾何サブパッケージを一覧する。未実装の雛形はクラス名に状態を付記している。

| クラス | ソース | 用途 |
|--------|--------|------|
| `BerlekampMassey`（未実装） | [Java](../../src/lib/math/BerlekampMassey.java) | 数列から線形漸化式を復元 |
| `ChineseRemainderTheorem`（未実装） | [Java](../../src/lib/math/ChineseRemainderTheorem.java) | 中国剰余定理 |
| `DiscreteLogarithm`（未実装） | [Java](../../src/lib/math/DiscreteLogarithm.java) | 離散対数 |
| `FloorSum`（未実装） | [Java](../../src/lib/math/FloorSum.java) | floor関数を含む整数和 |
| `Kitamasa`（未実装） | [Java](../../src/lib/math/Kitamasa.java) | 線形漸化式の高速計算 |
| `ModularSquareRoot`（未実装） | [Java](../../src/lib/math/ModularSquareRoot.java) | 法を指定した平方根 |
| `PollardRho`（未実装） | [Java](../../src/lib/math/PollardRho.java) | Pollard rho法による因数分解 |
| `ConvexHull`（未実装） | [Java](../../src/lib/math/geometry/ConvexHull.java) | 凸包の構築 |

一回だけの整数計算は`MathUtils`、同じ法で階乗・組み合わせを繰り返す場合は`FactorialTable`、素数表を再利用する場合は`PrimeTable`を選択する。

## 内部パッケージ

| パッケージ               | ソース                                  | ドキュメント                                |
|--------------------------|-----------------------------------------|---------------------------------------------|
| `lib.math.geometry`      | [src](../../src/lib/math/geometry)      | [GeometryUtils](./geometryutils/README.md)  |
| `lib.math.linearalgebra` | [src](../../src/lib/math/linearalgebra) | [Linear Algebra](./linearalgebra/README.md) |
| `lib.math.number`        | [src](../../src/lib/math/number)        | [Number types](./number/README.md)          |
| `lib.math.polynomial`    | [src](../../src/lib/math/polynomial)    | [Polynomial](./polynomial/README.md)        |
