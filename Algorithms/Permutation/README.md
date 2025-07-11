# 順列 (Permutation) アルゴリズム

## 概要

順列（要素の並び替え）を効率的に生成するためのアルゴリズムを提供します。辞書順での次の順列や前の順列を生成するメソッドを含み、様々なデータ型に対応しています。

## 実装クラス

### Permutation

- **用途**: 辞書順での順列生成を行うユーティリティクラス
- **特徴**:
	- 辞書順で次の順列を生成するメソッド
	- 辞書順で前の順列を生成するメソッド
	- 整数配列(int[])、長整数配列(long[])、文字配列(char[])に対応
	- 配列の特定範囲内での順列生成をサポート
- **時間計算量**: O(n)、ここでnは配列の長さ
- **空間計算量**: O(1)（追加のメモリを使用せず、入力配列を直接変更）

## 主なメソッド

| メソッド                                       | 説明                       |
|--------------------------------------------|--------------------------|
| `next(int[] arr)`                          | 整数配列を辞書順で次の順列に並び替え       |
| `next(int[] arr, int fromIdx, int toIdx)`  | 整数配列の指定範囲を辞書順で次の順列に並び替え  |
| `prev(int[] arr)`                          | 整数配列を辞書順で前の順列に並び替え       |
| `prev(int[] arr, int fromIdx, int toIdx)`  | 整数配列の指定範囲を辞書順で前の順列に並び替え  |
| `next(long[] arr)`                         | 長整数配列を辞書順で次の順列に並び替え      |
| `next(long[] arr, int fromIdx, int toIdx)` | 長整数配列の指定範囲を辞書順で次の順列に並び替え |
| `prev(long[] arr)`                         | 長整数配列を辞書順で前の順列に並び替え      |
| `prev(long[] arr, int fromIdx, int toIdx)` | 長整数配列の指定範囲を辞書順で前の順列に並び替え |
| `next(char[] arr)`                         | 文字配列を辞書順で次の順列に並び替え       |
| `next(char[] arr, int fromIdx, int toIdx)` | 文字配列の指定範囲を辞書順で次の順列に並び替え  |
| `prev(char[] arr)`                         | 文字配列を辞書順で前の順列に並び替え       |
| `prev(char[] arr, int fromIdx, int toIdx)` | 文字配列の指定範囲を辞書順で前の順列に並び替え  |

## 使用例

    // 整数配列の次の順列を生成
    int[] arr = {1, 2, 3};
    boolean hasNext = Permutation.next(arr);  // true、arrは{1, 3, 2}に変更される
    System.out.println(Arrays.toString(arr));  // [1, 3, 2]
    
    hasNext = Permutation.next(arr);  // true、arrは{2, 1, 3}に変更される
    System.out.println(Arrays.toString(arr));  // [2, 1, 3]
    
    // 文字配列の次の順列を生成
    char[] chars = {'a', 'b', 'c'};
    boolean hasNextChar = Permutation.next(chars);  // true、charsは{'a', 'c', 'b'}に変更される
    System.out.println(Arrays.toString(chars));  // [a, c, b]
    
    // 配列の特定範囲内での順列生成
    int[] rangeArr = {1, 2, 3, 4, 5};
    boolean hasNextRange = Permutation.next(rangeArr, 1, 4);  // true、rangeArrは{1, 3, 4, 2, 5}に変更される
    System.out.println(Arrays.toString(rangeArr));  // [1, 3, 4, 2, 5]
    
    // 全ての順列を列挙
    int[] permArr = {1, 2, 3};
    do {
        System.out.println(Arrays.toString(permArr));
    } while (Permutation.next(permArr));

## アルゴリズムの説明

辞書順で次の順列を生成するアルゴリズムは以下のステップで動作します：

1. 配列の末尾から見て、arr[i] < arr[i+1]となる最大のインデックスiを見つける
2. そのようなインデックスが存在しない場合、配列は降順に並んでおり、次の順列は存在しない
3. 配列の末尾から見て、arr[i] < arr[j]となる最大のインデックスjを見つける
4. arr[i]とarr[j]を交換する
5. arr[i+1]から配列の末尾までの要素を反転する

前の順列を生成するアルゴリズムも同様ですが、比較の向きが逆になります。

## 注意事項

- 全ての順列を列挙する場合、最初に配列を昇順にソートしておくことで、最初の順列から始めることができます
- 全ての順列の数はn!（nの階乗）であり、nが大きい場合は非常に多くなるため注意が必要です
- メソッドは入力配列を直接変更します（破壊的操作）