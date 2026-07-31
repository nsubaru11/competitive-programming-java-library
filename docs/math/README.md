# lib.math

`src/lib/math` の公開APIに対応するドキュメントです。ルートパッケージのクラスはクラス名ごと、サブパッケージはパッケージ名ごとに整理しています。

| ドキュメント                           | 対象                        | 用途                                       |
|----------------------------------------|-----------------------------|--------------------------------------------|
| [MathUtils](./MathUtils)               | `lib.math.MathUtils`        | 整数演算・組み合わせ・数論の主要な静的入口 |
| [FactorialTable](./FactorialTable)     | `lib.math.FactorialTable`   | 素数mod上の動的な階乗・逆元テーブル        |
| [FactorUtils](./FactorUtils)           | `lib.math.FactorUtils`      | 素因数分解、素因数個数、約数列挙           |
| [PrimeUtils](./PrimeUtils)             | `lib.math.PrimeUtils`       | 一回限りの素数判定・篩                     |
| [PrimeTable](./PrimeTable)             | `lib.math.PrimeTable`       | 再利用可能な素数テーブル                   |
| [NumberPredicates](./NumberPredicates) | `lib.math.NumberPredicates` | 完全数・回文数などの判定                   |
| [GeometryUtils](./GeometryUtils)       | `lib.math.GeometryUtils`    | 交差・包含・距離計算                       |
| [number](./number)                     | `lib.math.number`           | `Fraction`、`Int128`、`ModInt`、`ModLong`  |
| [LinearAlgebra](./LinearAlgebra)       | `lib.math.linearalgebra`    | 行列演算と未実装の線形代数API              |
| [Polynomial](./Polynomial)             | `lib.math.polynomial`       | 多項式と畳み込み                           |

一回だけの整数計算は`MathUtils`、同じ法で階乗・組み合わせを繰り返す場合は`FactorialTable`、素数表を再利用する場合は`PrimeTable`を選択します。
