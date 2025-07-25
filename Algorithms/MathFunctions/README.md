# Mathematical Functions

## 概要

競技プログラミングで頻繁に使用される様々な数学的関数とアルゴリズムを提供するライブラリです。
組み合わせ論、数論、幾何学、多項式計算など、幅広い数学的操作をサポートしています。

## 実装クラス

### CombinatoricsUtils

- **用途**: 組み合わせ論関連の計算を行うユーティリティクラス
- **特徴**:
	- 組み合わせ数 (nCr) の計算（通常版とモジュロ版）
	- 順列数 (nPr) の計算（通常版とモジュロ版）
	- 重複組み合わせ (nHr) の計算
	- スターリング数（第2種）の計算
	- ベル数の計算

### NumberTheoryUtils

- **用途**: 数論関連の計算を行うユーティリティクラス
- **特徴**:
	- 最大公約数 (GCD) と最小公倍数 (LCM) の計算
	- 拡張ユークリッドアルゴリズム (exGCD)
	- オイラーのトーシェント関数
	- 素数判定
	- 素因数分解
	- モジュロ逆数の計算

### GeometryUtils

- **用途**: 幾何学的計算を行うユーティリティクラス
- **特徴**:
	- 線分の交差判定（2次元と3次元）
	- 点が線分上にあるかの判定
	- 長方形の交差判定
	- 点が図形内にあるかの判定
	- 各種距離計算（ユークリッド距離、マンハッタン距離、チェビシェフ距離）

### PowerUtils

- **用途**: べき乗計算を行うユーティリティクラス
- **特徴**:
	- 高速べき乗計算
	- モジュロべき乗計算

### PolynomialUtils

- **用途**: 多項式計算を行うユーティリティクラス
- **特徴**:
	- 多項式の加算、減算、乗算
	- 多項式の評価

### DivisionUtils

- **用途**: 除算関連の計算を行うユーティリティクラス

### NumberFormatUtils

- **用途**: 数値のフォーマット変換を行うユーティリティクラス

### NumberPredicates

- **用途**: 数値の性質を判定するユーティリティクラス
