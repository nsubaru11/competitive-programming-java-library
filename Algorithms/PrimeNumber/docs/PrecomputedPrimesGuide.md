# PrecomputedPrimes 利用ガイド

## 概要

エラトステネスの篩を用いて指定された上限までの素数を事前計算し、高速な素数判定・検索・列挙機能を提供するクラスです。
ビット操作による省メモリ実装により、大規模な素数テーブルを効率的に管理します。

## 特徴

- エラトステネスの篩による高速な素数生成（O(N log log N)）
- ビット配列による省メモリ実装（奇数のみを管理）
- 二分探索ベースの高速な素数検索（O(log N)）
- 素数の範囲検索機能（ceiling, floor, higher, lower）
- イテレータとストリームによる素数の列挙
- 素因数分解機能
- 最大Integer.MAX_VALUEまでの素数を扱える

## 依存関係

- `java.util.Arrays;`
- `java.util.HashMap;`
- `java.util.Map;`
- `java.util.NoSuchElementException;`
- `java.util.PrimitiveIterator;`
- `java.util.stream.LongStream;`

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                      | 戻り値の型 | 説明          |
|---------------------------|-------|-------------|
| PrecomputedPrimes(long n) | -     | n以下の素数を事前計算 |

### 2. 素数判定系メソッド

| メソッド            | 戻り値の型   | 説明                |
|-----------------|---------|-------------------|
| isPrime(long n) | boolean | nが素数かどうかを判定（O(1)） |

### 3. カウント系メソッド

| メソッド                    | 戻り値の型 | 説明                     |
|-------------------------|-------|------------------------|
| countPrimesUpTo(long n) | int   | n以下の素数の個数を返す（O(log N)） |

### 4. 範囲検索系メソッド

| メソッド                 | 戻り値の型 | 説明                        |
|----------------------|-------|---------------------------|
| ceilingPrime(long n) | long  | n以上の最小の素数を返す。存在しない場合は-1   |
| higherPrime(long n)  | long  | nより大きい最小の素数を返す。存在しない場合は-1 |
| floorPrime(long n)   | long  | n以下の最大の素数を返す。存在しない場合は-1   |
| lowerPrime(long n)   | long  | nより小さい最大の素数を返す。存在しない場合は-1 |

### 5. インデックスアクセス系メソッド

| メソッド            | 戻り値の型 | 説明                   |
|-----------------|-------|----------------------|
| kthPrime(int i) | long  | i番目の素数を返す（0-indexed） |

### 6. 素因数分解系メソッド

| メソッド                   | 戻り値の型              | 説明                       |
|------------------------|--------------------|--------------------------|
| primeFactorize(long n) | Map<Long, Integer> | nを素因数分解し、素因数とその指数のマップを返す |

### 7. イテレーション系メソッド

| メソッド       | 戻り値の型                    | 説明              |
|------------|--------------------------|-----------------|
| iterator() | PrimitiveIterator.OfLong | 素数を列挙するイテレータを返す |
| stream()   | LongStream               | 素数のストリームを返す     |

## 利用例

```java
// 100以下の素数を事前計算
PrecomputedPrimes primes = new PrecomputedPrimes(100);

// 素数判定
System.out.println(primes.isPrime(97));  // true

// 50以下の素数の個数
System.out.println(primes.countPrimesUpTo(50));  // 15

// 30以上の最小の素数
System.out.println(primes.ceilingPrime(30));  // 31

// 10番目の素数（0-indexed）
System.out.println(primes.kthPrime(10));  // 31

// 素因数分解
Map<Long, Integer> factors = primes.primeFactorize(60);
// {2=2, 3=1, 5=1} → 60 = 2^2 * 3 * 5

// イテレータで列挙
for (var it = primes.iterator(); it.hasNext(); ) {
    long p = it.nextLong();
    if (p > 50) break;
    System.out.print(p + " ");
}
// 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47

// ストリームで処理
long sum = primes.stream().filter(p -> p <= 100).sum();
```

## 注意事項

- 初期化時にn以下の全素数を生成するため、nが大きい場合はメモリ消費に注意
- 素数判定・検索は事前計算範囲内のみ有効。範囲外の値を渡すとIllegalArgumentExceptionが発生
- 素因数分解は事前計算された素数の範囲内でのみ動作。範囲外の因数が含まれる場合はIllegalArgumentExceptionが発生
- n = 10^8程度までは実用的に利用可能（メモリ約100MB、初期化約1秒）

## パフォーマンス特性

- **初期化**: O(N log log N) 時間、O(N) 空間（実際はビット配列により約N/128バイト）
- **isPrime**: O(1) 時間
- **countPrimesUpTo**: O(log π(N)) 時間（π(N)は素数の個数）
- **ceilingPrime, floorPrime, higherPrime, lowerPrime**: O(log π(N)) 時間
- **kthPrime**: O(1) 時間
- **primeFactorize**: O(√N / ln(N)) 時間（最悪ケース）
- **iterator, stream**: 素数1個あたりO(1)

## バージョン情報

| バージョン番号       | 年月日        | 詳細   |
|:--------------|:-----------|:-----|
| **バージョン 1.0** | 2025-10-13 | 初回作成 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新