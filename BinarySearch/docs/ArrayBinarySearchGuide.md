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

### 下限探索（Lower Bound Search）

- **概要**：目標値以下の最大要素のインデックスを返します。
- **戻り値**：条件を満たす値が存在する場合、そのインデックスを返します。見つからない場合、`-(挿入位置 + 1)`を返します。

### 上限探索（Upper Bound Search）

- **概要**：目標値以上の最小要素のインデックスを返します。
- **戻り値**：条件を満たす値が存在する場合、そのインデックスを返します。見つからない場合、`-(挿入位置 + 1)`を返します。

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
```

## 注意事項

- **ソート済み配列が必須**：探索対象の配列はあらかじめ昇順でソートされている必要があります。
- 探索実行前にはnullチェックを行ってください。
- 範囲指定の際、`[l, r)`形式（左側は含むが右側は含まない）を使用してください。

## パフォーマンス特性

- **時間計算量**：すべての探索メソッドで`O(log N)`（`N`は配列の要素数）
- **空間計算量**：すべての探索メソッドは定数（`O(1)`）の追加領域を必要とします。

## バージョン情報

- 初期バージョン：通常の二分探索、下限探索、上限探索の実装に対応。