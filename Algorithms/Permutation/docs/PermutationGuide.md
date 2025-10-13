# Permutation 利用ガイド

## 概要

`Permutation` は、辞書順に基づいて配列の**次の順列**または**前の順列**を効率的に生成するための静的ユーティリティクラスです。
全探索アルゴリズムなど、要素の全ての並び順を試す必要がある場合に利用できます。

## 特徴

- **多彩なデータ型対応**: `int`, `long`, `char` の1次元配列および2次元配列をサポートします。
- **範囲指定**: 配列の特定の範囲 `[fromIdx, toIdx)` 内での順列生成が可能です。
- **インプレース操作**: 追加のメモリをほとんど使用せず、与えられた配列を直接変更します（$O(1)$ の空間計算量）。
- **効率的なアルゴリズム**: 各操作は、操作対象の要素数を $n$ として線形時間 $O(n)$ で完了します。

## 依存関係

このクラスは、Javaの標準ライブラリ以外に依存関係はありません。

## 主な機能（メソッド一覧）

### 1. 次の順列 (next) 生成メソッド

| メソッド                                                  | 戻り値の型     | 説明                                                             |
|-------------------------------------------------------|-----------|----------------------------------------------------------------|
| `next(int[] arr)`                                     | `boolean` | `int` 型配列 `arr` を辞書順で次の順列に並び替えます。                              |
| `next(int[] arr, int fromIdx, int toIdx)`             | `boolean` | `int` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を次の順列に並び替えます。         |
| `next(long[] arr)`                                    | `boolean` | `long` 型配列 `arr` を辞書順で次の順列に並び替えます。                             |
| `next(long[] arr, int fromIdx, int toIdx)`            | `boolean` | `long` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を次の順列に並び替えます。        |
| `next(char[] arr)`                                    | `boolean` | `char` 型配列 `arr` を辞書順で次の順列に並び替えます。                             |
| `next(char[] arr, int fromIdx, int toIdx)`            | `boolean` | `char` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を次の順列に並び替えます。        |
| `next(int[][] arr, int idx)`                          | `boolean` | 2次元配列 `arr` を `idx` 列を基準に次の順列に並び替えます。                          |
| `next(int[][] arr, int idx, int fromIdx, int toIdx)`  | `boolean` | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に次の順列に並び替えます。 |
| `next(long[][] arr, int idx)`                         | `boolean` | 2次元配列 `arr` を `idx` 列を基準に次の順列に並び替えます。                          |
| `next(long[][] arr, int idx, int fromIdx, int toIdx)` | `boolean` | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に次の順列に並び替えます。 |
| `next(char[][] arr, int idx)`                         | `boolean` | 2次元配列 `arr` を `idx` 列を基準に次の順列に並び替えます。                          |
| `next(char[][] arr, int idx, int fromIdx, int toIdx)` | `boolean` | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に次の順列に並び替えます。 |

### 2. 前の順列 (prev) 生成メソッド

| メソッド                                                  | 戻り値の型     | 説明                                                             |
|-------------------------------------------------------|-----------|----------------------------------------------------------------|
| `prev(int[] arr)`                                     | `boolean` | `int` 型配列 `arr` を辞書順で前の順列に並び替えます。                              |
| `prev(int[] arr, int fromIdx, int toIdx)`             | `boolean` | `int` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を前の順列に並び替えます。         |
| `prev(long[] arr)`                                    | `boolean` | `long` 型配列 `arr` を辞書順で前の順列に並び替えます。                             |
| `prev(long[] arr, int fromIdx, int toIdx)`            | `boolean` | `long` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を前の順列に並び替えます。        |
| `prev(char[] arr)`                                    | `boolean` | `char` 型配列 `arr` を辞書順で前の順列に並び替えます。                             |
| `prev(char[] arr, int fromIdx, int toIdx)`            | `boolean` | `char` 型配列 `arr` の指定範囲 `[fromIdx, toIdx)` を前の順列に並び替えます。        |
| `prev(int[][] arr, int idx)`                          | `boolean` | 2次元配列 `arr` を `idx` 列を基準に前の順列に並び替えます。                          |
| `prev(int[][] arr, int idx, int fromIdx, int toIdx)`  | `boolean` | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に前の順列に並び替えます。 |
| `prev(long[][] arr, int idx)`                         | `boolean` | 2次元配列 `arr` を `idx` 列を基準に前の順列に並び替えます。                          |
| `prev(long[][] arr, int idx, int fromIdx, int toIdx)` | `boolean` | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に前の順列に並び替えます。 |
| `prev(char[][] arr, int idx)`                         | `boolean` | 2次元配列 `arr` を `idx` 列を基準に前の順列に並び替えます。                          |
| `prev(char[][] arr, int idx, int fromIdx, int toIdx)` | `boolean` | 2次元配列 `arr` の指定範囲 `[fromIdx, toIdx)` を `idx` 列を基準に前の順列に並び替えます。 |

## 利用例

`do-while` ループと組み合わせることで、全ての順列を簡単に列挙できます。

```java
import java.util.Arrays;

public class Example {
    public static void main(String[] args) {
        int[] array = {1, 2, 3};

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

- **破壊的操作**: このクラスのメソッドは、引数として渡された配列を直接変更します。元の配列を保持したい場合は、事前にコピーを作成してください。
- **初期状態**: 全ての順列を辞書順で列挙するには、操作を開始する前に配列を昇順にソートしておく必要があります。
- **計算量**: $n$ 個の要素の順列の総数は $n!$ です。$n$ が大きくなると（例: $n > 15$）、計算時間が非常に長くなるため、使用する際は要素数に注意してください。

## パフォーマンス特性

- **時間計算量**: $O(n)$ （$n$ は操作対象の配列または範囲の長さ）
- **空間計算量**: $O(1)$ （インプレースで操作を行い、追加のメモリは不要）

## バージョン情報

| バージョン番号       | 年月日        | 詳細     |
|:--------------|:-----------|:-------|
| **バージョン 1.0** | 2025-10-13 | 初版リリース |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
