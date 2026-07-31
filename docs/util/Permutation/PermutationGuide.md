# Permutation 利用ガイド

## 概要

`Permutation`は、配列の重複を除いた辞書順位置と、次・前の順列を扱う静的ユーティリティです。

## 特徴

- `int[]`、`long[]`、`char[]`に対応
- 2次元配列では指定列を比較キーとして行を並べ替える
- 配列全体版と`[fromIdx, toIdx)`範囲版
- `next` / `prev`はインプレース$\mathcal{O}(n)$

## 依存関係

- `lib.math.MathUtils`

## 主な機能（メソッド一覧）

### 辞書順位置

| メソッド                                        | 戻り値 | 説明                      |
|-------------------------------------------------|-------:|---------------------------|
| `index(int[]/long[]/char[] arr)`                | `long` | 配列全体の0始まり位置     |
| `index(arr, int fromIdx, int toIdx)`            | `long` | 指定範囲版                |
| `index(int[][]/long[][]/char[][] arr, int idx)` | `long` | `idx`列をキーとする行順列 |
| `index(arr, int idx, int fromIdx, int toIdx)`   | `long` | 2次元範囲版               |

### 次・前の順列

| メソッド                             |    戻り値 | 説明                                  |
|--------------------------------------|----------:|---------------------------------------|
| `next(int[]/long[]/char[] arr)`      | `boolean` | 次の順列へ変更。存在しなければ`false` |
| `next(arr, int fromIdx, int toIdx)`  | `boolean` | 指定範囲版                            |
| `prev(int[]/long[]/char[] arr)`      | `boolean` | 前の順列へ変更。存在しなければ`false` |
| `prev(arr, int fromIdx, int toIdx)`  | `boolean` | 指定範囲版                            |
| `next/prev(2次元配列, int idx, ...)` | `boolean` | `idx`列をキーとする行順列             |

## 利用例

```java
import java.util.Arrays;
import lib.util.Permutation;

int[] a = {1, 2, 3};
do {
	System.out.println(Arrays.toString(a));
} while (Permutation.next(a));
```

## 注意事項

- 全順列を辞書順で列挙する場合、最初に対象範囲を昇順にしておきます。
- `next` / `prev`は入力配列を直接変更します。
- `index`の途中値と戻り値が`long`に収まる要素数を前提とします。
- 2次元版は行自体を交換します。

## パフォーマンス特性

- `next` / `prev`: $\mathcal{O}(n)$時間、$\mathcal{O}(1)$追加空間
- 1次元`index`: $\mathcal{O}(n^2)$時間、$\mathcal{O}(1)$追加空間
- 2次元`index`: $\mathcal{O}(n^2)$時間、$\mathcal{O}(n)$追加空間

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                       |
|:-------------------|:-----------|:-------------------------------------------|
| **バージョン 1.0** | 2025-10-13 | 初回実装                                   |
| **バージョン 2.0** | 2026-07-27 | 重複を除いた辞書順indexを追加              |
| **バージョン 3.0** | 2026-08-01 | プリミティブ2次元配列と範囲指定のAPIを整備 |
