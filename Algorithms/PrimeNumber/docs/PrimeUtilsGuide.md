# PrimeUtils 利用ガイド

## 概要

素数判定、素数の個数カウント、指定範囲の素数取得などの静的ユーティリティメソッドを提供するクラスです。
事前計算が不要で、必要なときに必要な範囲の素数を処理できます。

## 特徴

- 静的メソッドのみで構成されたユーティリティクラス
- 事前計算不要で即座に使用可能
- 試し割り法による素数判定
- Miller-RabinアルゴリズムによるBigIntegerベースの確率的素数判定
- エラトステネスの篩による範囲内素数の列挙
- SetまたはListとして素数を取得可能
- メモリ効率の良いBitSetベースの実装

## 依存関係

- `java.math.BigInteger;`
- `java.util.ArrayList;`
- `java.util.BitSet;`
- `java.util.Collection;`
- `java.util.HashSet;`
- `java.util.List;`
- `java.util.Set;`
- `java.util.function.Supplier;`

## 主な機能（メソッド一覧）

### 1. 素数判定系メソッド

| メソッド                           | 戻り値の型   | 説明                            |
|--------------------------------|---------|-------------------------------|
| isPrime(long n)                | boolean | nが素数かどうかを試し割り法で判定（O(√N)）      |
| isProbablePrime(long n, int k) | boolean | nが素数である確率が高いかをMiller-Rabinで判定 |

### 2. カウント系メソッド

| メソッド                       | 戻り値の型 | 説明                  |
|----------------------------|-------|---------------------|
| elements(int n)            | int   | 2以上n以下の素数の個数を返す     |
| elements(int min, int max) | int   | min以上max以下の素数の個数を返す |

### 3. 素数取得系メソッド（Set）

| メソッド                          | 戻り値の型        | 説明                       |
|-------------------------------|--------------|--------------------------|
| getPrimeSet(int n)            | Set<Integer> | 2以上n以下の素数をHashSetで返す     |
| getPrimeSet(int min, int max) | Set<Integer> | min以上max以下の素数をHashSetで返す |

### 4. 素数取得系メソッド（List）

| メソッド                           | 戻り値の型         | 説明                             |
|--------------------------------|---------------|--------------------------------|
| getPrimeList(int n)            | List<Integer> | 2以上n以下の素数をArrayListで返す（昇順）     |
| getPrimeList(int min, int max) | List<Integer> | min以上max以下の素数をArrayListで返す（昇順） |

## 利用例

```java
// 単一の素数判定（試し割り法）
System.out.println(PrimeUtils.isPrime(97));  // true
System.out.println(PrimeUtils.isPrime(100)); // false

// 大きな数の確率的素数判定（Miller-Rabin）
long bigNum = 1000000007L;
System.out.println(PrimeUtils.isProbablePrime(bigNum, 20));  // true

// 100以下の素数の個数
System.out.println(PrimeUtils.elements(100));  // 25

// 50以上100以下の素数の個数
System.out.println(PrimeUtils.elements(50, 100));  // 10

// 30以下の素数をSetで取得
Set<Integer> primeSet = PrimeUtils.getPrimeSet(30);
// {2, 3, 5, 7, 11, 13, 17, 19, 23, 29}

// 20以上50以下の素数をListで取得
List<Integer> primeList = PrimeUtils.getPrimeList(20, 50);
// [23, 29, 31, 37, 41, 43, 47]
```

## 注意事項

- isPrimeメソッドは試し割り法を使用しているため、大きな数の判定には時間がかかる（O(√N)）
- 大きな数の素数判定にはisProbablePrimeを使用することを推奨（確率的アルゴリズム）
- isProbablePrimeのkパラメータが大きいほど精度が上がる（k=20で誤判定確率は約2^-40）
- 範囲指定メソッドでは、min > maxの場合は空のコレクションを返す
- Int型の範囲内（約2×10^9）の素数のみ扱える

## パフォーマンス特性

- **isPrime**: O(√N) 時間、O(1) 空間
- **isProbablePrime**: O(k log³ N) 時間（kは精度パラメータ）、O(log N) 空間
- **elements**: O(N log log N) 時間、O(N) 空間（Nはmax値）
- **getPrimeSet**: O(N log log N) 時間、O(π(N)) 空間（π(N)は素数の個数）
- **getPrimeList**: O(N log log N) 時間、O(π(N)) 空間

## バージョン情報

| バージョン番号       | 年月日        | 詳細   |
|:--------------|:-----------|:-----|
| **バージョン 1.0** | 2025-10-13 | 初回作成 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新