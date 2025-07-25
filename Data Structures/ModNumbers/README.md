# Modular Numbers

## 概要

剰余演算（mod演算）は、競技プログラミングにおいて非常に重要な演算です。
特に大きな数値を扱う問題や、組み合わせ論、数論関連の問題で頻繁に使用されます。
このライブラリでは、mod演算を効率的に行うためのModInt（Modular Integer）クラスを提供しています。

## 実装クラス

### [ModInt](./src/ModInt.java)

- **用途**：mod演算を効率的に行うためのクラス
- **特徴**：
	- イミュータブル（不変）な設計
	- 基本的な算術演算（加算、減算、乗算、除算、べき乗）をサポート
	- カスタムモジュロ値の指定が可能（デフォルトは998244353）
	- 除算は逆元を使用して実装
	- 2による除算の最適化
- **主な操作**:
	- `add(ModInt other)`: モジュロ加算
	- `sub(ModInt other)`: モジュロ減算
	- `mul(ModInt other)`: モジュロ乗算
	- `div(ModInt other)`: モジュロ除算（逆元を使用）
	- `pow(int exp)`: モジュロべき乗
- **時間計算量**：
	- 加算/減算/乗算: O(1)
	- 除算: O(log mod)（フェルマーの小定理を使用）
	- べき乗: O(log exp)（繰り返し二乗法を使用）
- **空間計算量**：O(1)

## 選択ガイド

ModIntクラスは以下のような場合に特に有用です：

- 大きな数値を扱う問題（オーバーフローを防ぐため）
- 組み合わせ論の問題（階乗、二項係数など）
- 数論関連の問題
- 逆元を使った除算が必要な場合
- 繰り返し二乗法によるべき乗計算が必要な場合

## 注意事項

- ModIntオブジェクト同士の演算では、両方が同じモジュロ値を持っていることを前提としています。異なるモジュロ値を持つオブジェクト間の演算は想定されていません。
- 除算操作は、フェルマーの小定理を使用して逆元を計算しています。そのため、モジュロ値は素数である必要があります。
- デフォルトのモジュロ値（998244353）は、高速フーリエ変換（FFT）との互換性のために選ばれた素数です。
- 2による除算は頻繁に使用されるため、コンストラクタで2の逆元を事前計算して最適化しています。