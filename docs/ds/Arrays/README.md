# Primitive Array Utilities

`lib.ds.arrays` のプリミティブ配列ラッパーと、`lib.util.ArrayUtils` の配列アルゴリズムをまとめます。
このドキュメントは 2026-07-15 時点の `src/` の実装を正とし、`int` 版と `long` 版を同じ Guide で説明します。

## Guide 一覧

| Guide                                             | 対象                                          | 用途                    |
|---------------------------------------------------|---------------------------------------------|-----------------------|
| [ArrayGuide](./ArrayGuide.md)                     | `IntArray`, `LongArray`                     | 読み取り専用配列インターフェース      |
| [MutableArrayGuide](./MutableArrayGuide.md)       | `IntMutableArray`, `LongMutableArray`       | 更新可能配列インターフェース        |
| [PrefixSumGuide](./PrefixSumGuide.md)             | `IntPrefixSum`, `LongPrefixSum`             | 静的配列の区間和              |
| [CircularArrayGuide](./CircularArrayGuide.md)     | `IntCircularArray`, `LongCircularArray`     | O(1) の論理回転と固定長更新      |
| [ArrayDequeGuide](./ArrayDequeGuide.md)           | `IntArrayDeque`, `LongArrayDeque`           | プリミティブ特化の可変長 deque    |
| [Array2DGuide](./Array2DGuide.md)                 | `IntArray2D`, `LongArray2D`                 | 一次元圧縮された2次元配列と論理回転    |
| [CompressedArrayGuide](./CompressedArrayGuide.md) | `IntCompressedArray`, `LongCompressedArray` | 座標圧縮と順位変換             |
| [ArrayUtilsGuide](./ArrayUtilsGuide.md)           | `ArrayUtils`                                | 窓、部分列、部分集合などの配列アルゴリズム |

## 選択の目安

- 値を読むだけの共通引数には `IntArray` / `LongArray`
- 固定長で要素を回転させるなら `CircularArray`
- 先頭・末尾の追加削除が必要なら `ArrayDeque`
- 更新せず区間和を繰り返すなら `PrefixSum`
- 値を順位へ写すなら `CompressedArray`
- LIS、固定幅窓、部分集合の個数には `ArrayUtils`

## 共通方針

- `int` / `long` をプリミティブのまま扱い、ボクシングと不要な割り当てを避けます。
- `IntArray` / `LongArray` を受け取る API により、具体実装を差し替えられます。
- コンテストの問題制約を満たす入力を前提とし、全ての不正引数を検査する汎用コレクションではありません。
- 配列長、添字、窓幅などの事前条件は各 Guide に記載します。

## import

```java
import lib.ds.arrays.*;
import lib.util.ArrayUtils;
```

提出用ソースでは通常の `lib.*` import を使用し、AtCoder 側のバンドラで展開します。
