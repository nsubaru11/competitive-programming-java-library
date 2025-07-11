# 素数 (Prime Number) アルゴリズム

## 概要

素数（1と自身以外に約数を持たない自然数）を効率的に生成、判定、操作するためのアルゴリズムとユーティリティを提供します。エラトステネスの篩を用いた高速な素数生成や、様々な素数関連の操作をサポートしています。

## 実装クラス

### PrecomputedPrimes

- **用途**: 指定された上限までの素数を事前計算し、様々な素数関連の操作を提供するクラス
- **特徴**:
	- エラトステネスの篩を用いた効率的な素数生成
	- ビット操作を用いたメモリ効率の良い実装
	- 素数判定、素数の個数カウント、k番目の素数取得などの機能
	- 素数の範囲検索（ceiling, floor, higher, lower）
	- 素数の反復処理とストリーム処理をサポート
	- 素因数分解機能
- **時間計算量**:
	- 初期化: O(N log log N)、ここでNは上限値
	- クエリ: O(1)〜O(log N)（操作による）

### FastPrecomputedPrimes

- **用途**: PrecomputedPrimesと同様の機能を提供する最適化バージョン
- **特徴**:
	- PrecomputedPrimesと同じインターフェースを提供
	- 内部実装の最適化により、より高速な処理を実現

### PrimeUtils

- **用途**: 素数に関する様々なユーティリティメソッドを提供するクラス
- **特徴**:
	- 単一の素数判定（試し割り法）
	- 確率的素数判定（Miller-Rabinアルゴリズム）
	- 指定範囲内の素数の個数カウント
	- 指定範囲内の素数をセットまたはリストとして取得

## 主なメソッド

### PrecomputedPrimes / FastPrecomputedPrimes

| メソッド                      | 説明                           |
|---------------------------|------------------------------|
| `isPrime(long n)`         | 数値nが素数かどうかを判定                |
| `countPrimesUpTo(long n)` | n以下の素数の個数（π(n)）を計算           |
| `ceilingPrime(long n)`    | n以上の最小の素数を取得                 |
| `higherPrime(long n)`     | nより大きい最小の素数を取得               |
| `floorPrime(long n)`      | n以下の最大の素数を取得                 |
| `lowerPrime(long n)`      | nより小さい最大の素数を取得               |
| `kthPrime(int i)`         | i番目の素数を取得（0-indexed）         |
| `primeFactorize(long n)`  | 数値nの素因数分解を行い、素因数とその指数のマップを返す |
| `iterator()`              | 素数を反復処理するためのイテレータを取得         |
| `stream()`                | 素数をストリーム処理するためのLongStreamを取得 |

### PrimeUtils

| メソッド                             | 説明                           |
|----------------------------------|------------------------------|
| `isPrime(long n)`                | 試し割り法による素数判定                 |
| `isProbablePrime(long n, int k)` | Miller-Rabinアルゴリズムによる確率的素数判定 |
| `elements(int n)`                | 2以上n以下の素数の個数を計算              |
| `elements(int min, int max)`     | min以上max以下の素数の個数を計算          |
| `getPrimeSet(int n)`             | 2以上n以下の素数をHashSetとして取得       |
| `getPrimeSet(int min, int max)`  | min以上max以下の素数をHashSetとして取得   |
| `getPrimeList(int n)`            | 2以上n以下の素数をArrayListとして取得     |
| `getPrimeList(int min, int max)` | min以上max以下の素数をArrayListとして取得 |

## 使用例

```java
// PrecomputedPrimesの使用例
PrecomputedPrimes primes = new PrecomputedPrimes(1000000);

// 素数判定
boolean is97Prime = primes.isPrime(97);  // true

// 素数の個数
int count = primes.countPrimesUpTo(100);  // 25（100以下の素数の個数）

// 素数の検索
long nextPrime = primes.ceilingPrime(100);  // 101
long prevPrime = primes.floorPrime(100);  // 97

// k番目の素数
long tenthPrime = primes.kthPrime(9);  // 29（10番目の素数、0-indexed）

// 素因数分解
Map<Long, Integer> factors = primes.primeFactorize(60);  // {2=2, 3=1, 5=1}（60 = 2^2 * 3^1 * 5^1）

// 素数の反復処理
for(
long prime :primes){
		if(prime >100)break;
		System.out.

print(prime +" ");  // 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97
}

// PrimeUtilsの使用例
boolean isPrime = PrimeUtils.isPrime(101);  // true

// 確率的素数判定
boolean isProbablePrime = PrimeUtils.isProbablePrime(1000000007, 10);  // true

// 素数のリスト取得
List<Integer> primeList = PrimeUtils.getPrimeList(10, 50);  // [11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47]
```

## 計算量

- PrecomputedPrimes/FastPrecomputedPrimes初期化: O(N log log N)
- isPrime: O(1)
- countPrimesUpTo: O(log N)
- ceilingPrime/floorPrime: O(log N)
- kthPrime: O(1)
- primeFactorize: O(√N)
- PrimeUtils.isPrime: O(√N)
- PrimeUtils.elements/getPrimeSet/getPrimeList: O(N log log N)

## 注意事項

- PrecomputedPrimesは指定された上限までの素数を事前計算するため、メモリ使用量に注意が必要です
- 非常に大きな数値の素数判定には、PrimeUtils.isProbablePrimeを使用することを検討してください
- 素因数分解は、事前計算された素数の範囲内でのみ動作します