# BinarySearch 利用ガイド

> `BinarySearch`クラスは柔軟な二分探索アルゴリズムを提供するユーティリティクラスである。

[← README: Binary Search](./README.md) · ソース: [BinarySearch.java](../../../src/lib/search/BinarySearch.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

整数 (int)および長整数 (long)の範囲に対して、カスタム比較ロジックを使用した二分探索を実行できる。
このクラスは以下の3種類の探索方式をサポートしている：

- 通常の二分探索（Normal Search）：条件にちょうど一致する値を探索
- 上限探索（Upper Bound）：条件を満たす最大の値を探索
- 下限探索（Lower Bound）：条件を満たす最小の値を探索

## 特徴

- 静的メソッドのみを持つユーティリティクラスであり、インスタンス化を防ぐためにコンストラクタは`private`に設定されている。
- `CompareFunction`インターフェースを利用した柔軟な比較ロジックのカスタマイズ
- `CompareFunction` は `int compare(long index)` のシグネチャを採用
- 整数型 (int)と長整数型 (long)の両方の範囲に対応
- 探索に失敗した場合は挿入位置の情報を含んだ負の値を返す（- (挿入位置 + 1)）
- シンプルで効率的なアルゴリズム実装
- 競技プログラミングでの使用に最適化されており、高速な実行が可能

## 依存関係

- `CompareFunction` - 二分探索の比較ロジックを定義する関数型インターフェース

## API

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

- **戻り値**：条件にちょうど一致する整数。見つからない場合は- (挿入位置 + 1)
- **例外**：明示的な専用例外はない（`comparator == null` の場合は実行時に `NullPointerException` が発生する）
- **使用例**：特定の条件を満たす値を探索する場合に使用する。

``` java
public static long normalSearch(long l, long r, CompareFunction comparator)
```

- **引数**：
	- `l` - 探索範囲の下限値（この値を含む）
	- `r` - 探索範囲の上限値（この値を含まない）
	- `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件にちょうど一致する長整数。見つからない場合は- (挿入位置 + 1)
- **例外**：明示的な専用例外はない（`comparator == null` の場合は実行時に `NullPointerException` が発生する）
- **使用例**：大きな数値範囲で特定の条件を満たす値を探索する場合に使用する。

#### 上限探索（Upper Bound）

``` java
public static int upperBoundSearch(int l, int r, CompareFunction comparator)
```

- **引数**：
	- `l` - 探索範囲の下限値（この値を含む）
	- `r` - 探索範囲の上限値（この値を含まない）
	- `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件を満たす最大の整数。見つからない場合は- (挿入位置 + 1)
- **例外**：明示的な専用例外はない（`comparator == null` の場合は実行時に `NullPointerException` が発生する）
- **使用例**：条件を満たす最大の値を探索する場合に使用する。

``` java
public static long upperBoundSearch(long l, long r, CompareFunction comparator)
```

- **引数**：
	- `l` - 探索範囲の下限値（この値を含む）
	- `r` - 探索範囲の上限値（この値を含まない）
	- `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件を満たす最大の長整数。見つからない場合は- (挿入位置 + 1)
- **例外**：明示的な専用例外はない（`comparator == null` の場合は実行時に `NullPointerException` が発生する）
- **使用例**：大きな数値範囲で条件を満たす最大の値を探索する場合に使用する。

#### 下限探索（Lower Bound）

``` java
public static int lowerBoundSearch(int l, int r, CompareFunction comparator)
```

- **引数**：
	- `l` - 探索範囲の下限値（この値を含む）
	- `r` - 探索範囲の上限値（この値を含まない）
	- `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件を満たす最小の整数。見つからない場合は- (挿入位置 + 1)
- **例外**：明示的な専用例外はない（`comparator == null` の場合は実行時に `NullPointerException` が発生する）
- **使用例**：条件を満たす最小の値を探索する場合に使用する。

``` java
public static long lowerBoundSearch(long l, long r, CompareFunction comparator)
```

- **引数**：
	- `l` - 探索範囲の下限値（この値を含む）
	- `r` - 探索範囲の上限値（この値を含まない）
	- `comparator` - 比較ロジックを提供する関数インターフェース

- **戻り値**：条件を満たす最小の長整数。見つからない場合は- (挿入位置 + 1)
- **例外**：明示的な専用例外はない（`comparator == null` の場合は実行時に `NullPointerException` が発生する）
- **使用例**：大きな数値範囲で条件を満たす最小の値を探索する場合に使用する。

## 利用例

準備中

## 制約・注意事項

- `CompareFunction`が適切に実装されていないと、正しい結果が得られない場合がある
- 比較関数は以下のルールに従うこと：
	- 正の値: 対象の値が条件を超過する場合
	- 0: 対象の値が条件にちょうど一致する場合
	- 負の値: 対象の値が条件より小さい場合

- 範囲の指定は左閉右開区間（`[l, r)`）で行われる
- `l == r` の空区間は有効である（結果は探索失敗として `-(挿入位置 + 1)` になる）
- 現在実装では `validateRange` を持たないため、`l > r` でも専用例外は送出されない
- 探索に失敗した場合の戻り値は`-(挿入位置 + 1)`となる。挿入位置を取得するには`-(戻り値) - 1`の計算が必要である。
- 比較関数内で例外が発生した場合、その例外はそのまま伝播される。

## 性能特性

- 時間計算量: $\mathcal{O}(\log N)$（Nは探索範囲のサイズ）
- 空間計算量: $\mathcal{O}(1)$（追加のメモリ使用は定数時間）
- 最適化: 競技プログラミングでの使用を考慮して最適化されており、高速な実行が可能である。

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                                                                                                                                                         |
|:-------------------|:-----------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-04-06 | すべての基本機能を実装（通常探索、上限探索、下限探索）                                                                                                                       |
| **バージョン 1.1** | 2025-04-17 | 例外処理を強化し、nullチェックを追加。比較関数内の例外を適切にハンドル                                                                                                       |
| **バージョン 2.0** | 2025-06-30 | 内部実装を最適化し、各探索タイプ（通常、上限、下限）に特化したメソッドを提供することでパフォーマンスを向上。SearchTypeの列挙型を削除し、コードをよりシンプルかつ効率的に改善 |
| **バージョン 2.1** | 2025-10-13 | 全メソッド引数、ローカル変数に`final`を追加し、不変性を向上                                                                                                                  |
| **バージョン 2.2** | 2026-01-06 | `validateRange` の境界判定を `l > r` のみ不正とし、空区間を有効化。ドキュメントを更新                                                                                        |
| **バージョン 3.0** | 2026-04-17 | `CompareFunction` の戻り値型を `int` に統一し、`validateRange` と `BSException` の記載を削除して現行実装へ整合                                                               |
| **バージョン 3.1** | 2026-05-10 | `lowerBound`, `upperBound` の内部メソッドにおいて、バグを修正                                                                                                                |
