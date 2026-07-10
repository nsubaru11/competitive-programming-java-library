# NumberUtils

## 概要

べき乗計算・数値フォーマット変換・数値の性質判定など、数値全般に関する小規模なユーティリティ群を提供します
（旧 MathFunctions
のうち、[Combinatorics](../Combinatorics)・[Geometry](../Geometry)・[NumberTheory](../NumberTheory)・[Polynomial](../Polynomial)
へ分割した残りをまとめたモジュールです）。

## 実装クラス

### [PowerUtils](../../../src/lib/math/PowerUtils.java)

- **用途**：
	- べき乗計算（通常版・モジュロ版）、階乗計算
- **特徴**：
	- 高速べき乗（繰り返し二乗法）
	- モジュロべき乗・モジュロ階乗

### [NumberFormatUtils](../../../src/lib/math/NumberFormatUtils.java)

- **用途**：
	- 数値の文字列フォーマット変換
- **特徴**：
	- 小数の書式付き文字列化 (`formatDouble`)
	- ゼロ埋め文字列化 (`toPaddedString`)

### [NumberPredicates](../../../src/lib/math/NumberPredicates.java)

- **用途**：
	- 数値の性質判定
- **特徴**：
	- 平方数・立方数・完全数・回文数・フィボナッチ数の判定

## アルゴリズム（データ構造）選択ガイド

- 上記3クラスは独立したユーティリティであり、必要なものだけを個別にコピーして利用できます。

## 注意事項

- 組み合わせ・数論・幾何・多項式関連の計算は、それぞれ分割先のモジュールを参照してください。
