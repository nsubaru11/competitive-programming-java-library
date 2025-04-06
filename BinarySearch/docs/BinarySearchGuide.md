# BinarySearch 利用ガイド

## 概要

`BinarySearch`クラスは柔軟な二分探索アルゴリズムを提供するユーティリティクラスです。  
整数(int)および長整数(long)の範囲に対して、カスタム比較ロジックを使用した二分探索を実行できます。  
このクラスは以下の3種類の探索方式をサポートしています：

- 通常の二分探索（Normal Search）：条件にちょうど一致する値を探索
- 上限探索（Upper Bound）：条件を満たす最大の値を探索
- 下限探索（Lower Bound）：条件を満たす最小の値を探索

## 特徴

- `CompareFunction`インターフェースを利用した柔軟な比較ロジックのカスタマイズ
- 整数型(int)と長整数型(long)の両方の範囲に対応
- 探索に失敗した場合は挿入位置の情報を含んだ負の値を返す（-(挿入位置 + 1)）
- シンプルで効率的なアルゴリズム実装
- 競技プログラミングでの使用に最適化されており、高速な実行が可能

## 依存関係

- `CompareFunction` - 二分探索の比較ロジックを定義する関数型インターフェース
- `BSException` - 不正な引数や条件が与えられた際に投げられる例外クラス

## 主な機能

### 通常の二分探索

- `normalSearch(int l, int r, CompareFunction comparator)` - 整数範囲での通常探索
- `normalSearch(long l, long r, CompareFunction comparator)` - 長整数範囲での通常探索

### 上限探索（Upper Bound）

- `upperBoundSearch(int l, int r, CompareFunction comparator)` - 整数範囲での上限探索
- `upperBoundSearch(long l, long r, CompareFunction comparator)` - 長整数範囲での上限探索

### 下限探索（Lower Bound）

- `lowerBoundSearch(int l, int r, CompareFunction comparator)` - 整数範囲での下限探索
- `lowerBoundSearch(long l, long r, CompareFunction comparator)` - 長整数範囲での下限探索

### メソッド詳細

#### 通常の二分探索

``` java
public static int normalSearch(int l, int r, CompareFunction comparator)
```

- **引数**：
    - `l` - 探索範囲の下限値（この値を含む）
    - `r` - 探索範囲の上限値（この値を含まない）
    - `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件にちょうど一致する整数。見つからない場合は-(挿入位置 + 1)
- **例外**：`BSException` - comparatorがnullの場合、または範囲が不正な場合
- **使用例**：特定の条件を満たす値を探索する場合に使用します。

``` java
public static long normalSearch(long l, long r, CompareFunction comparator)
```

- **引数**：
    - `l` - 探索範囲の下限値（この値を含む）
    - `r` - 探索範囲の上限値（この値を含まない）
    - `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件にちょうど一致する長整数。見つからない場合は-(挿入位置 + 1)
- **例外**：`BSException` - comparatorがnullの場合、または範囲が不正な場合
- **使用例**：大きな数値範囲で特定の条件を満たす値を探索する場合に使用します。

#### 上限探索（Upper Bound）

``` java
public static int upperBoundSearch(int l, int r, CompareFunction comparator)
```

- **引数**：
    - `l` - 探索範囲の下限値（この値を含む）
    - `r` - 探索範囲の上限値（この値を含まない）
    - `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件を満たす最大の整数。見つからない場合は-(挿入位置 + 1)
- **例外**：`BSException` - comparatorがnullの場合、または範囲が不正な場合
- **使用例**：条件を満たす最大の値を探索する場合に使用します。

``` java
public static long upperBoundSearch(long l, long r, CompareFunction comparator)
```

- **引数**：
    - `l` - 探索範囲の下限値（この値を含む）
    - `r` - 探索範囲の上限値（この値を含まない）
    - `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件を満たす最大の長整数。見つからない場合は-(挿入位置 + 1)
- **例外**：`BSException` - comparatorがnullの場合、または範囲が不正な場合
- **使用例**：大きな数値範囲で条件を満たす最大の値を探索する場合に使用します。

#### 下限探索（Lower Bound）

``` java
public static int lowerBoundSearch(int l, int r, CompareFunction comparator)
```

- **引数**：
    - `l` - 探索範囲の下限値（この値を含む）
    - `r` - 探索範囲の上限値（この値を含まない）
    - `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件を満たす最小の整数。見つからない場合は-(挿入位置 + 1)
- **例外**：`BSException` - comparatorがnullの場合、または範囲が不正な場合
- **使用例**：条件を満たす最小の値を探索する場合に使用します。

``` java
public static long lowerBoundSearch(long l, long r, CompareFunction comparator)
```

- **引数**：
    - `l` - 探索範囲の下限値（この値を含む）
    - `r` - 探索範囲の上限値（この値を含まない）
    - `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件を満たす最小の長整数。見つからない場合は-(挿入位置 + 1)
- **例外**：`BSException` - comparatorがnullの場合、または範囲が不正な場合
- **使用例**：大きな数値範囲で条件を満たす最小の値を探索する場合に使用します。

## 利用例

```java
// カスタム比較関数での通常の二分探索 (12345678 < x⁵ < 1234567890 を満たす x を探索)
int target2 = 1234567890;
int result = BinarySearch.normalSearch(0, 100, i -> {
    long k = i * i * i * i * i;
    return target2 / 100 < k && k < target2 ? 0 : k >= target2 ? 1 : -1;
});

// カスタム比較関数での下限探索 (12345678 < x⁵ < 1234567890 を満たす最小の x を探索)
result = BinarySearch.lowerBoundSearch(0, 100, i -> {
    long k = i * i * i * i * i;
    return target2 / 100 < k && k < target2 ? 0 : k >= target2 ? 1 : -1;
});

// カスタム比較関数での上限探索 (12345678 < x⁵ < 1234567890 を満たす最大の x を探索)
result = BinarySearch.upperBoundSearch(0, 100, i -> {
    long k = i * i * i * i * i;
    return target2 / 100 < k && k < target2 ? 0 : k >= target2 ? 1 : -1;
});

// 平方根の近似値を求める例
double target = 2.0;
int precision = 1000000; // 精度を上げるために大きな数を使用
int sqrtResult = BinarySearch.normalSearch(0, precision, i -> {
    double x = (double) i / precision;
    double diff = x * x - target;
    return Math.abs(diff) < 0.000001 ? 0 : diff > 0 ? 1 : -1;
});
double sqrtApprox = (double) sqrtResult / precision;
```

## 注意事項

- `CompareFunction`が適切に実装されていないと、正しい結果が得られない場合があります
- 比較関数は以下のルールに従ってください：
    - 正の値: 対象の値が条件を超過する場合
    - 0: 対象の値が条件にちょうど一致する場合
    - 負の値: 対象の値が条件より小さい場合

- 範囲の指定は左閉右開区間（`[l, r)`）で行われます
- 探索に失敗した場合の戻り値は`-(挿入位置 + 1)`となります。挿入位置を取得するには`-(戻り値) - 1`の計算が必要です。
- 比較関数内で例外が発生した場合、その例外はそのまま伝播されます。

## パフォーマンス特性

- 時間計算量: O(log N)（Nは探索範囲のサイズ）
- 空間計算量: O(1)（追加のメモリ使用は定数時間）
- 最適化: 競技プログラミングでの使用を考慮して最適化されており、高速な実行が可能です。

## バージョン情報

- 初期バージョン: すべての基本機能を実装（通常探索、上限探索、下限探索）
- 現在のバージョン: 整数型と長整数型の範囲に対応し、カスタム比較ロジックをサポート
