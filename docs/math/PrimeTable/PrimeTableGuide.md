# PrimeTable 利用ガイド

## 概要

`PrimeTable`は、指定上限までの素数を構築し、同じテーブルを判定・近傍検索・列挙・素因数分解へ再利用します。

## 特徴

- 奇数のみを1bitずつ保持する篩
- 素数配列上の二分探索
- primitive iteratorとLongStream
- 構築済み素数を使う反復的な素因数分解

## 依存関係

- `java.util.Arrays`、`HashMap`、`Map`、`PrimitiveIterator`
- `java.util.stream.LongStream`

## 主な機能（メソッド一覧）

| メソッド                  |                     戻り値 | 説明                     |
|---------------------------|---------------------------:|--------------------------|
| `PrimeTable(long n)`      |                          - | `n`以下の素数を構築      |
| `isPrime(long n)`         |                  `boolean` | テーブル範囲内の素数判定 |
| `countPrimesUpTo(long n)` |                      `int` | `n`以下の素数個数        |
| `ceilingPrime(long n)`    |                     `long` | `n`以上の最小素数        |
| `higherPrime(long n)`     |                     `long` | `n`より大きい最小素数    |
| `floorPrime(long n)`      |                     `long` | `n`以下の最大素数        |
| `lowerPrime(long n)`      |                     `long` | `n`より小さい最大素数    |
| `kthPrime(int i)`         |                     `long` | 0始まりのi番目の素数     |
| `primeFactorize(long n)`  |       `Map<Long, Integer>` | 素因数と指数のMap        |
| `iterator()`              | `PrimitiveIterator.OfLong` | 昇順iterator             |
| `stream()`                |               `LongStream` | 昇順stream               |

## 利用例

```java
import lib.math.PrimeTable;

PrimeTable table = new PrimeTable(1_000_000);
boolean prime = table.isPrime(999_983);
long next = table.ceilingPrime(100_000);
var factors = table.primeFactorize(600_851_475_143L);
```

## 注意事項

- `isPrime`は`0`以上、判定・近傍検索は構築上限以下の値を前提とします。
- `primeFactorize(n)`は、残る因数を判定できるよう`√n`までの素数がテーブルに含まれることを前提とします。
- 素因数Mapの反復順序は`HashMap`に従い、昇順を保証しません。
- コンストラクタ引数は`long`ですが、内部配列長は`int`なので、実際の上限は利用可能メモリに強く制約されます。

## パフォーマンス特性

- 構築: $\mathcal{O}(N \log \log N)$時間
- 合成数bit配列: およそN/16 byte。別途素数配列が必要
- `isPrime`: $\mathcal{O}(1)$
- 個数・近傍検索: $\mathcal{O}(\log \pi(N))$
- `kthPrime`: $\mathcal{O}(1)$
- 素因数分解: 使用する素数個数に比例

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                  |
|:-------------------|:-----------|:------------------------------------------------------|
| **バージョン 1.0** | 2025-10-13 | 初回実装                                              |
| **バージョン 1.2** | 2026-05-02 | `isPrime(1)`の誤判定を修正                            |
| **バージョン 2.0** | 2026-08-01 | 近傍検索、素因数分解、iterator、streamを含むAPIを整備 |
