# PrimeUtils 利用ガイド

## 概要

`PrimeUtils`は、テーブルを保持せず、一回限りの素数判定・個数計算・列挙を行う静的ユーティリティです。

## 特徴

- 6k±1の試し割りによる決定的判定
- `BigInteger.isProbablePrime`による確率的判定
- `BitSet`を使った範囲篩
- 配列版とSupplier指定Collection版

## 依存関係

- `java.math.BigInteger`
- `java.util.ArrayList`、`BitSet`、`Collection`
- `java.util.function.Supplier`

## 主な機能（メソッド一覧）

| メソッド                                      |                          戻り値 | 説明                          |
|-----------------------------------------------|--------------------------------:|-------------------------------|
| `isPrime(long n)`                             |                       `boolean` | 試し割りによる素数判定        |
| `isProbablePrime(long n, int k)`              |                       `boolean` | certaintyを指定した確率的判定 |
| `primeCount(int n)`                           |                           `int` | `[2, n]`の素数個数            |
| `primeCount(int min, int max)`                |                           `int` | `[min, max]`の素数個数        |
| `eratosthenes(int min, int max)`              |                         `int[]` | 範囲内の素数を昇順配列で返す  |
| `eratosthenes(int min, int max, Supplier<T>)` | `T extends Collection<Integer>` | 指定Collectionへ追加          |

## 利用例

```java
import lib.math.PrimeUtils;

boolean prime = PrimeUtils.isPrime(1_000_000_007L);
int count = PrimeUtils.primeCount(1_000_000);
int[] primes = PrimeUtils.eratosthenes(100, 200);
```

## 注意事項

- 範囲メソッドは`max`が非負かつ`min <= max`であることを前提とします。
- `isPrime`は大きな素数ほど時間がかかります。
- 同じ上限で判定・検索を繰り返す場合は`PrimeTable`を使用します。
- Collection版の反復順序はCollection実装に依存します。

## パフォーマンス特性

- `isPrime`: $\mathcal{O}(\sqrt{n})$時間、$\mathcal{O}(1)$空間
- `primeCount`、`eratosthenes`: $\mathcal{O}(N \log \log N)$時間、$\mathcal{O}(N)$ビット空間

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                               |
|:-------------------|:-----------|:---------------------------------------------------|
| **バージョン 1.0** | 2026-08-01 | 素数判定・個数計算・篩の配列版とCollection版を実装 |
