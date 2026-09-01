# Convolution API設計書

## 基本設計

`Convolution`は入力を変更せず、必要なゼロ埋めと結果長の調整を担当します。
公開されている`Transform`も入力をコピーして新しい配列を返します。将来の高速化では、公開APIのコピー型契約を保ったまま、パッケージ内のin-placeコアを利用します。

## 公開API

| メソッド群                    | 現在のオーバーロード                              | 用途と返却長                                                      | 状態     |
|-------------------------------|---------------------------------------------------|-------------------------------------------------------------------|----------|
| `convolveNtt`                 | `long[]` / `int[]`、各`mod`付き                   | 多項式畳み込み。長さ`a.length + b.length - 1`                     | 未実装   |
| `convolveArbitraryMod`        | `long[]` / `int[]`、各`mod`付き                   | 3素数NTTとGarner復元による多項式畳み込み                          | 未実装   |
| `convolveFft`                 | `double[]`、`long[]`、`int[]`                     | 実数または整数係数の多項式畳み込み。長さ`a.length + b.length - 1` | 未実装   |
| `convolveXor`                 | `long[]`（modあり/なし）、`int[]`（modあり/なし） | XOR畳み込み。定義域長を返す                                       | 未実装   |
| `convolveAnd`                 | `long[]`（modあり/なし）、`int[]`（modあり/なし） | AND畳み込み。定義域長を返す                                       | 利用可能 |
| `convolveOr`                  | `long[]`（modあり/なし）、`int[]`（modあり/なし） | OR畳み込み。定義域長を返す                                        | 利用可能 |
| `convolveGcd` / `convolveLcm` | `long[]`（modあり/なし）、`int[]`（modあり/なし） | GCD / LCM畳み込み。最大入力長を返す                               | 未実装   |

ビット演算畳み込みは、最大入力長が2の冪である添字集合を対象にします。短い方の配列は0埋めされます。
剰余版では係数を`[0, mod)`に正規化して渡してください。`long`版は通常の競技プログラミングで使う範囲のmodと係数を対象とし、積・和が`long`に収まることを前提とします。

## 変換API

| 変換群       | 公開メソッド                      | 状態     |
|--------------|-----------------------------------|----------|
| NTT          | `ntt`                             | 未実装   |
| FFT          | `fft`                             | 未実装   |
| FWHT         | `fwht`                            | 未実装   |
| 部分集合変換 | `subsetZeta` / `subsetMobius`     | 利用可能 |
| 上位集合変換 | `supersetZeta` / `supersetMobius` | 利用可能 |
| 倍数変換     | `multipleZeta` / `multipleMobius` | 未実装   |
| 約数変換     | `divisorZeta` / `divisorMobius`   | 未実装   |
| CRT復元      | `garnerProcess`（内部）           | 未実装   |

NTT、FFT、FWHT、部分集合・上位集合変換の`len`付きオーバーロードは、`len`以上の最小の2の冪へ0埋めして返します。`multiple*` / `divisor*`は添字の最大値を`len`として扱います。
