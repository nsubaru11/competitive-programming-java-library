# Permutation 利用ガイド

## 概要

`Permutation` は、辞書順に基づいて配列の現在位置を求め、**次の順列**または**前の順列**を効率的に生成するための静的ユーティリティクラスです。
全探索アルゴリズムなど、要素の全ての並び順を試す必要がある場合に利用できます。

## 特徴

- **多彩なデータ型対応**: `int`, `long`, `char` の1次元配列および2次元配列をサポートします。
- **範囲指定**: 配列の特定の範囲 `[fromIdx, toIdx)` 内での順列生成が可能です。
- **辞書順位置**: 重複する並びを除き、現在の順列が何番目かを0始まりで求めます。
- **インプレース操作**: 追加のメモリをほとんど使用せず、与えられた配列を直接変更します（$O(1)$ の空間計算量）。
- **効率的なアルゴリズム**: 各操作は、操作対象の要素数を $n$ として線形時間 $O(n)$ で完了します。

## 依存関係

- [`lib.util.ArrayUtils`](../../ds/Arrays/ArrayUtilsGuide.md)

## 主な機能（メソッド一覧）

### 1. 辞書順位置 (index) 計算メソッド

| メソッド                                                                                | 戻り値の型 | 説明                                   |
|-----------------------------------------------------------------------------------------|-----------:|----------------------------------------|
| `index(int[] arr)` / `index(int[] arr, int fromIdx, int toIdx)`                         |     `long` | `int` 配列全体・指定範囲の辞書順位置   |
| `index(long[] arr)` / `index(long[] arr, int fromIdx, int toIdx)`                       |     `long` | `long` 配列版                          |
| `index(char[] arr)` / `index(char[] arr, int fromIdx, int toIdx)`                       |     `long` | `char` 配列版                          |
| `index(int[][] arr, int idx)` / `index(int[][] arr, int idx, int fromIdx, int toIdx)`   |     `long` | `idx` 列を基準とする2次元 `int` 配列版 |
| `index(long[][] arr, int idx)` / `index(long[][] arr, int idx, int fromIdx, int toIdx)` |     `long` | 2次元 `long` 配列版                    |
| `index(char[][] arr, int idx)` / `index(char[][] arr, int idx, int fromIdx, int toIdx)` |     `long` | 2次元 `char` 配列版                    |

`index` は同じ値を区別しません。例えば `{1, 1, 2}`, `{1, 2, 1}`, `{2, 1, 1}` の位置はそれぞれ `0`, `1`, `2` です。

### 2. 次の順列 (next) 生成メソッド

| メソッド                                              | 戻り値の型 | 説明                                                                                      |
|-------------------------------------------------------|------------|-------------------------------------------------------------------------------------------|
| `next(int[] arr)`                                     | `boolean`  | `int` 型配列 `arr` を辞書順で次の順列に並び替えます。                                     |
| `next(int[] arr, int fromIdx, int toIdx)`             | `boolean`  | `int` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を次の順列に並び替えます。               |
| `next(long[] arr)`                                    | `boolean`  | `long` 型配列 `arr` を辞書順で次の順列に並び替えます。                                    |
| `next(long[] arr, int fromIdx, int toIdx)`            | `boolean`  | `long` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を次の順列に並び替えます。              |
| `next(char[] arr)`                                    | `boolean`  | `char` 型配列 `arr` を辞書順で次の順列に並び替えます。                                    |
| `next(char[] arr, int fromIdx, int toIdx)`            | `boolean`  | `char` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を次の順列に並び替えます。              |
| `next(int[][] arr, int idx)`                          | `boolean`  | 2次元配列 `arr` を `idx` 列を基準に次の順列に並び替えます。                               |
| `next(int[][] arr, int idx, int fromIdx, int toIdx)`  | `boolean`  | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に次の順列に並び替えます。 |
| `next(long[][] arr, int idx)`                         | `boolean`  | 2次元配列 `arr` を `idx` 列を基準に次の順列に並び替えます。                               |
| `next(long[][] arr, int idx, int fromIdx, int toIdx)` | `boolean`  | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に次の順列に並び替えます。 |
| `next(char[][] arr, int idx)`                         | `boolean`  | 2次元配列 `arr` を `idx` 列を基準に次の順列に並び替えます。                               |
| `next(char[][] arr, int idx, int fromIdx, int toIdx)` | `boolean`  | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に次の順列に並び替えます。 |

### 3. 前の順列 (prev) 生成メソッド

| メソッド                                              | 戻り値の型 | 説明                                                                                      |
|-------------------------------------------------------|------------|-------------------------------------------------------------------------------------------|
| `prev(int[] arr)`                                     | `boolean`  | `int` 型配列 `arr` を辞書順で前の順列に並び替えます。                                     |
| `prev(int[] arr, int fromIdx, int toIdx)`             | `boolean`  | `int` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を前の順列に並び替えます。               |
| `prev(long[] arr)`                                    | `boolean`  | `long` 型配列 `arr` を辞書順で前の順列に並び替えます。                                    |
| `prev(long[] arr, int fromIdx, int toIdx)`            | `boolean`  | `long` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を前の順列に並び替えます。              |
| `prev(char[] arr)`                                    | `boolean`  | `char` 型配列 `arr` を辞書順で前の順列に並び替えます。                                    |
| `prev(char[] arr, int fromIdx, int toIdx)`            | `boolean`  | `char` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を前の順列に並び替えます。              |
| `prev(int[][] arr, int idx)`                          | `boolean`  | 2次元配列 `arr` を `idx` 列を基準に前の順列に並び替えます。                               |
| `prev(int[][] arr, int idx, int fromIdx, int toIdx)`  | `boolean`  | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に前の順列に並び替えます。 |
| `prev(long[][] arr, int idx)`                         | `boolean`  | 2次元配列 `arr` を `idx` 列を基準に前の順列に並び替えます。                               |
| `prev(long[][] arr, int idx, int fromIdx, int toIdx)` | `boolean`  | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に前の順列に並び替えます。 |
| `prev(char[][] arr, int idx)`                         | `boolean`  | 2次元配列 `arr` を `idx` 列を基準に前の順列に並び替えます。                               |
| `prev(char[][] arr, int idx, int fromIdx, int toIdx)` | `boolean`  | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に前の順列に並び替えます。 |

## 利用例

`do-while` ループと組み合わせることで、全ての順列を簡単に列挙できます。

```java
import java.util.Arrays;

public class Example {
    public static void main(String[] args) {
        int[] array = {1, 2, 3};

        System.out.println(Permutation.index(new int[]{2, 1, 3})); // 2

        // 全ての順列を列挙するため、最初に昇順ソートする
        Arrays.sort(array);

        System.out.println("All permutations of " + Arrays.toString(array) + ":");
        do {
            // 現在の順列を出力
            System.out.println(Arrays.toString(array));
        } while (Permutation.next(array)); // 次の順列を生成
    }
}
```

## 注意事項

- **破壊的操作**: `next` / `prev` は引数として渡された配列を直接変更します。元の配列を保持したい場合は、事前にコピーを作成してください。
- `index` は入力配列を変更しません。戻り値と計算途中の異なる順列数が `long` に収まることを前提とします。
- **初期状態**: 全ての順列を辞書順で列挙するには、操作を開始する前に配列を昇順にソートしておく必要があります。
- **計算量**: $n$ 個の要素の順列の総数は $n!$ です。$n$ が大きくなると（例: $n > 15$）、計算時間が非常に長くなるため、使用する際は要素数に注意してください。

## パフォーマンス特性

- `next` / `prev`: 時間 $O(n)$、追加メモリ $O(1)$
- 1次元配列の `index`: 時間 $O(n^2)$、追加メモリ $O(1)$
- 2次元配列の `index`: 時間 $O(n^2)$、追加メモリ $O(n)$

## バージョン情報

| バージョン番号     | 年月日     | 詳細                                                                                     |
|:-------------------|:-----------|:-----------------------------------------------------------------------------------------|
| **バージョン 1.0** | 2025-10-13 | 初版リリース                                                                             |
| **バージョン 2.0** | 2026-07-27 | 重複を除いた辞書順の0始まり位置を求める `index` を、既存の型・範囲指定構成に合わせて追加 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
