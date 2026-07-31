# FactorUtils 利用ガイド

## 概要

`FactorUtils`は、一回限りの試し割りによる素因数分解、素因数個数、正の約数個数・列挙を提供します。

## 特徴

- `int` / `long`の素因数配列を重複込みの昇順で返す
- `Supplier`で任意のCollection・Map実装を選択可能
- 約数をソートせず昇順へ結合
- `int <= 10^9`、`long <= 10^16`を想定

## 依存関係

- `java.util.Collection`、`List`、`Map`、`ArrayList`
- `java.util.function.Supplier`

## 主な機能（メソッド一覧）

### 素因数

| メソッド                                   |                 戻り値 | 説明                       |
|--------------------------------------------|-----------------------:|----------------------------|
| `uniquePrimeFactorCount(long n)`           |                  `int` | 異なる素因数の個数         |
| `primeFactorCount(long n)`                 |                  `int` | 重複込みの素因数の個数     |
| `primeFactors(int n)`                      |                `int[]` | 重複込みの昇順配列         |
| `primeFactors(long n)`                     |               `long[]` | long版                     |
| `primeFactors(int/long n, Supplier<T>)`    | `T extends Collection` | 指定Collectionへ昇順に追加 |
| `primeFactorsMap(int/long n, Supplier<T>)` |        `T extends Map` | 素因数と指数の対応         |

### 約数

| メソッド                            |                 戻り値 | 説明                       |
|-------------------------------------|-----------------------:|----------------------------|
| `divisorCount(long n)`              |                  `int` | 正の約数の個数             |
| `divisors(int n)`                   |                `int[]` | 正の約数の昇順配列         |
| `divisors(long n)`                  |               `long[]` | long版                     |
| `divisors(int/long n, Supplier<T>)` | `T extends Collection` | 指定Collectionへ昇順に追加 |

## 利用例

```java
import java.util.ArrayList;
import java.util.LinkedHashMap;
import lib.math.FactorUtils;

long[] factors = FactorUtils.primeFactors(360L); // 2,2,2,3,3,5
long[] divisors = FactorUtils.divisors(360L);
var map = FactorUtils.primeFactorsMap(360L, LinkedHashMap::new);
var list = FactorUtils.divisors(360L, ArrayList::new);

ArrayList<Integer> intDivisors =
	FactorUtils.<ArrayList<Integer>>divisors(360, ArrayList::new);
```

## 注意事項

- 正の整数を前提とし、`n <= 1`の素因数結果は空です。
- `primeFactors`のCollection版で`Set`を渡すと重複度が失われます。
- Collectionへの追加順は昇順ですが、`HashSet`などの反復順序は保証されません。
- Mapの反復順序は指定したMap実装に従います。
- `int`版と`long`版に対してコンストラクタ参照を渡す呼び出しが曖昧になる場合は、上の例のように型引数を明示します。
- 想定範囲内ではループ条件`p * p`は各プリミティブ型でオーバーフローしません。

## パフォーマンス特性

- 素因数分解・個数計算・約数個数: 最悪$\mathcal{O}(\sqrt{n})$
- 約数列挙: $\mathcal{O}(\sqrt{n} + d)$、dは約数個数
- 約数列挙の追加メモリ: $\mathcal{O}(d)$

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                 |
|:-------------------|:-----------|:-------------------------------------|
| **バージョン 1.0** | 2026-08-01 | 素因数分解・個数計算・約数列挙を実装 |
