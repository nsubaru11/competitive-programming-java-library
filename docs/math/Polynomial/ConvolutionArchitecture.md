# Convolution API設計書

## 基本設計

`Convolution`は入力を変更せず、必要なゼロ埋めと結果長の調整を担当します。
`Transform`のコピー版は、必要な長さの配列を作成してから対応する`*InPlace`メソッドを呼び出します。`*InPlace`メソッドは渡された配列を直接変更し、配列長が変換条件を満たさない場合は`false`を返します。

## 公開API

| メソッド群                    | 現在のオーバーロード                              | 用途と返却長                                                      | 状態     |
|-------------------------------|---------------------------------------------------|-------------------------------------------------------------------|----------|
| `convolveNtt`                 | `long[]` / `int[]`、各`mod`付き                   | 多項式畳み込み。長さ`a.length + b.length - 1`                     | 未実装   |
| `convolveArbitraryMod`        | `long[]` / `int[]`、各`mod`付き                   | 3素数NTTとGarner復元。長さ`a.length + b.length - 1`               | 未実装   |
| `convolveFft`                 | `double[]`、`long[]`、`int[]`                     | 実数または整数係数の多項式畳み込み。長さ`a.length + b.length - 1` | 未実装   |
| `convolveXor`                 | `long[]`（modあり/なし）、`int[]`（modあり/なし） | XOR畳み込み。2冪へ拡張した定義域長を返す                          | 利用可能 |
| `convolveAnd`                 | `long[]`（modあり/なし）、`int[]`（modあり/なし） | AND畳み込み。2冪へ拡張した定義域長を返す                          | 利用可能 |
| `convolveOr`                  | `long[]`（modあり/なし）、`int[]`（modあり/なし） | OR畳み込み。2冪へ拡張した定義域長を返す                           | 利用可能 |
| `convolveGcd` / `convolveLcm` | `long[]`（modあり/なし）、`int[]`（modあり/なし） | GCD / LCM畳み込み。最大入力長を返す                               | 未実装   |

NTT・任意mod・FFTによる通常の多項式畳み込みは、内部の変換配列を2の冪へ拡張しても、末尾の不要な0を除いた論理長`a.length + b.length - 1`で返します。
ビット演算畳み込みは任意の正の入力長を受け取り、最大入力長以上の最小の2の冪を定義域長として返します。短い方の配列と拡張部分は0埋めされます。GCD / LCM畳み込みは最大入力長を返します。
剰余版では係数を`[0, mod)`に正規化して渡してください。FWHTによる逆変換には変換長と`mod`が互いに素であることが必要です。`long`版は通常の競技プログラミングで使う範囲のmodと係数を対象とし、積・和が`long`に収まることを前提とします。

## 変換API

| 変換群       | コピー版                             | in-place版                                      | 状態     |
|--------------|--------------------------------------|-------------------------------------------------|----------|
| NTT          | `ntt`                                | `nttInPlace`                                    | 未実装   |
| FFT          | `fft`                                | `fftInPlace`                                    | 未実装   |
| FWHT         | `fwht`                               | `fwhtInPlace`                                   | 利用可能 |
| 部分集合変換 | `subsetZeta` / `subsetMobius`        | `subsetZetaInPlace` / `subsetMobiusInPlace`     | 利用可能 |
| 上位集合変換 | `supersetZeta` / `supersetMobius`    | `supersetZetaInPlace` / `supersetMobiusInPlace` | 利用可能 |
| 倍数変換     | `multipleZeta` / `multipleMobius`    | `multipleZetaInPlace` / `multipleMobiusInPlace` | 未実装   |
| 約数変換     | `divisorZeta` / `divisorMobius`      | `divisorZetaInPlace` / `divisorMobiusInPlace`   | 未実装   |
| CRT復元      | `garnerProcess`（`Convolution`内部） | -                                               | 未実装   |

NTT、FFT、FWHT、部分集合・上位集合変換のコピー版にある`len`付きオーバーロードは、`len`以上の最小の2の冪へ0埋めした配列を返します。元の配列は変更しません。`len`は保持したい入力要素数以上を指定します。
対応する`*InPlace`版は配列を拡張できないため、NTT、FFT、FWHT、部分集合・上位集合変換では長さが正の2の冪である配列を渡します。FFTでは実部と虚部の配列長も一致させます。
`multiple*` / `divisor*`のコピー版は指定した`len`をそのまま返却長とし、in-place版は渡された配列長を定義域として扱います。

## FWHTとXOR畳み込み

`fwht`は各段階で、対象bitだけが異なる二要素を`(x + y, x - y)`へ置き換えます。正変換と同じ加減算をもう一度行うと各要素は変換長倍になるため、逆変換では最後に変換長の逆元（非剰余版では通常の除算）を適用します。
`convolveXor`は、両入力を同一の2冪長へ0埋めして正変換し、同じ添字どうしを乗算してから逆変換します。返却値の添字`k`には、`i xor j = k`を満たす`a[i] * b[j]`の総和が入ります。
