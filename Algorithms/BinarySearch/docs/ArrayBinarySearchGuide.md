# ArrayBinarySearch 利用ガイド

## 概要

`ArrayBinarySearch`クラスは多様な型の配列（整数、長整数、倍精度浮動小数点数、および比較可能な型）に対する二分探索を効率的に実行するためのユーティリティクラスです。このクラスでは以下の3種類の探索アルゴリズムを提供します：

1. **通常の二分探索（Normal Search）**：配列内で指定した目標値に一致する要素を検索。
2. **下限探索（Lower Bound Search）**：配列内で目標値以下の最大の値を検索。
3. **上限探索（Upper Bound Search）**：配列内で目標値以上の最小の値を検索。

探索はソート済み配列で動作し、探索操作は全体または指定範囲内で行えます。

## 特徴

- 静的メソッドのみを持つユーティリティクラスであり、インスタンス化を防ぐためにコンストラクタは`private`に設定されています。
- 探索に失敗した場合、`-(挿入位置 + 1)`という形式で挿入位置を表す負の値を返します。
- 各探索メソッドは配列が`null`である場合や探索範囲が論理的でない場合に例外をスローします。
- 整数型 (`int`)、長整数型 (`long`)、倍精度浮動小数点型 (`double`)、および`Comparable`を実装した型の配列をサポートします。
- 競技プログラミングでの使用に最適化されており、高速な実行が可能です。

## 依存関係

`ArrayBinarySearch`クラスは、以下の依存関係を持ちます：

- `BSException`：不正な操作（null配列の指定、不正範囲など）が発生した場合にスローされます。

## 主な機能

### 整数型配列の探索

```java
public static int normalSearch(int[] arr, int target)

public static int normalSearch(int[] arr, int l, int r, int target)

public static int lowerBoundSearch(int[] arr, int target)

public static int lowerBoundSearch(int[] arr, int l, int r, int target)

public static int upperBoundSearch(int[] arr, int target)

public static int upperBoundSearch(int[] arr, int l, int r, int target)
```

### 長整数型配列の探索

```java
public static int normalSearch(long[] arr, long target)

public static int normalSearch(long[] arr, int l, int r, long target)

public static int lowerBoundSearch(long[] arr, long target)

public static int lowerBoundSearch(long[] arr, int l, int r, long target)

public static int upperBoundSearch(long[] arr, long target)

public static int upperBoundSearch(long[] arr, int l, int r, long target)
```

### 倍精度浮動小数点数型配列の探索

```java
public static int normalSearch(double[] arr, double target)

public static int normalSearch(double[] arr, int l, int r, double target)

public static int lowerBoundSearch(double[] arr, double target)

public static int lowerBoundSearch(double[] arr, int l, int r, double target)

public static int upperBoundSearch(double[] arr, double target)

public static int upperBoundSearch(double[] arr, int l, int r, double target)
```

### 比較可能な型（`Comparable<T>`）の配列の探索

```java
public static <T extends Comparable<T>> int normalSearch(T[] arr, T target)

public static <T extends Comparable<T>> int normalSearch(T[] arr, int l, int r, T target)

public static <T extends Comparable<T>> int lowerBoundSearch(T[] arr, T target)

public static <T extends Comparable<T>> int lowerBoundSearch(T[] arr, int l, int r, T target)

public static <T extends Comparable<T>> int upperBoundSearch(T[] arr, T target)

public static <T extends Comparable<T>> int upperBoundSearch(T[] arr, int l, int r, T target)
```

## メソッド詳細

以下は各メソッドの一般的な機能です。

### 通常の二分探索（Normal Search）

- **概要**：配列内の目標値に一致する値のインデックスを返します。
- **戻り値**：目標値が存在する場合、そのインデックスを返します。見つからない場合、`-(挿入位置 + 1)`を返します。
- **使用例**：特定の値が配列内に存在するかどうかを確認する場合に使用します。

### 下限探索（Lower Bound Search）

- **概要**：目標値以下の最大要素のインデックスを返します。
- **戻り値**：条件を満たす値が存在する場合、そのインデックスを返します。見つからない場合、`-(挿入位置 + 1)`を返します。
- **使用例**：目標値以下の最大値を探す場合や、配列内で目標値以下の要素の数を数える場合に使用します。

### 上限探索（Upper Bound Search）

- **概要**：目標値以上の最小要素のインデックスを返します。
- **戻り値**：条件を満たす値が存在する場合、そのインデックスを返します。見つからない場合、`-(挿入位置 + 1)`を返します。
- **使用例**：目標値以上の最小値を探す場合や、配列内で目標値以上の要素の数を数える場合に使用します。

## 利用例

```java
// 整数の配列で通常の二分探索
int[] array = {1, 5, 5, 5, 9};
int target = 5;

// 通常の二分探索（結果: インデックス2）
int normalResult = ArrayBinarySearch.normalSearch(array, target);

// 下限探索（結果: インデックス1）
int lowerResult = ArrayBinarySearch.lowerBoundSearch(array, target);

// 上限探索（結果: インデックス3）
int upperResult = ArrayBinarySearch.upperBoundSearch(array, target);

// 存在しない値の探索
int notFoundResult = ArrayBinarySearch.normalSearch(array, 4);
// 挿入位置の計算: -(notFoundResult) - 1
// 結果: 1
int insertPosition = -(notFoundResult) - 1;

// 範囲指定での探索
int[] largeArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
// インデックス2から5の範囲で探索（結果: インデックス3）
int rangeResult = ArrayBinarySearch.normalSearch(largeArray, 2, 5, 4);
```

## 注意事項

- **ソート済み配列が必須**：探索対象の配列はあらかじめ昇順でソートされている必要があります。
- 探索実行前にはnullチェックを行ってください。
- 範囲指定の際、`[l, r)`形式（左側は含むが右側は含まない）を使用してください。`l == r` の空区間は有効で、`l > r` の場合のみ `INVALID_BOUNDS` となります。
- 探索に失敗した場合の戻り値は`-(挿入位置 + 1)`となります。挿入位置を取得するには`-(戻り値) - 1`の計算が必要です。
- 配列内に重複要素がある場合、通常の二分探索は任意の一致する要素のインデックスを返す可能性があります。

## パフォーマンス特性

- **時間計算量**：すべての探索メソッドで`O(log N)`（`N`は配列の要素数）
- **空間計算量**：すべての探索メソッドは定数（`O(1)`）の追加領域を必要とします。
- **最適化**：競技プログラミングでの使用を考慮して最適化されており、高速な実行が可能です。

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                                                                                                       |
|:--------------|:-----------|:-----------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-03-29 | 通常の二分探索、下限探索、上限探索の実装に対応                                                                                                                  |
| **バージョン 1.1** | 2025-04-06 | 整数、長整数、倍精度浮動小数点数、および比較可能な型の配列に対応                                                                                                         |
| **バージョン 1.2** | 2025-04-17 | 例外処理を強化し、nullチェックを追加。比較関数内の例外を適切にハンドル                                                                                                    |
| **バージョン 2.0** | 2025-06-30 | 内部実装を最適化し、各探索タイプ（通常、上限、下限）に特化したメソッドを提供することでパフォーマンスを向上。SearchTypeの列挙型を削除し、コードをよりシンプルかつ効率的に改善。整数型と長整数型の配列では直接比較を行うことで比較メソッド呼び出しのオーバーヘッドを削減 |
| **バージョン 2.1** | 2025-10-13 | 全メソッド引数、ローカル変数に`final`を追加し、不変性を向上                                                                                                        |
| **バージョン 2.2** | 2026-01-06 | `validateRange` の境界判定を `l > r` のみ不正とし、空区間を有効化。ドキュメントを更新                                                                                  |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
