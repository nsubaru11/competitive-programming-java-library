# Convolution API設計書

## 基本設計

公開APIが配列のパディングと結果長の調整を担当し、内部変換メソッドが引数配列をインプレースで処理します。
内部の変換処理はオブジェクト生成や関数オブジェクトを避け、静的メソッドとして実装します。

## 公開API

| メソッド                                    | 用途                               | 必要な配列長                         |
|---------------------------------------------|------------------------------------|--------------------------------------|
| `multiplyNtt(long[], long[], int)`          | NTTによる多項式乗算                | `a.length + b.length - 1`以上の2の冪 |
| `multiplyArbitraryMod(long[], long[], int)` | 3素数とGarner復元による任意mod乗算 | 同上                                 |
| `multiplyFft(double[], double[])`           | FFTによる実数係数の乗算            | 同上                                 |
| `convoluteXor(long[], long[], int)`         | XOR畳み込み                        | 最大入力長以上の2の冪                |
| `convoluteAnd(long[], long[], int)`         | AND畳み込み                        | 最大入力長以上の2の冪                |
| `convoluteOr(long[], long[], int)`          | OR畳み込み                         | 最大入力長以上の2の冪                |
| `convoluteGcd(long[], long[], int)`         | GCD畳み込み                        | 最大入力長                           |
| `convoluteLcm(long[], long[], int)`         | LCM畳み込み                        | 最大入力長                           |

## 内部変換

- NTT / FFT
- FWHT
- 部分集合・上位集合ゼータ変換とメビウス変換
- 約数・倍数ゼータ変換とメビウス変換
- 3素数の結果からのGarner復元

いずれも現在は未実装です。公開メソッドを利用可能とするには、対応する変換と逆変換を実装する必要があります。
