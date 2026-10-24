# Primitive Array Utilities

`lib.ds.arrays` のプリミティブ配列ラッパーと、`lib.util.ArrayUtils` の配列アルゴリズムをまとめる。
現在の `src/` の実装を正とし、`int` 版と `long` 版を同じ Guide で説明する。

座標圧縮の `get` と累積和の `get`・引数付き `sum` は添字・区間を検査する。詳細は各Guideを参照する。

## クラス一覧

| クラス                  | 用途                                                                | Guide                                             | ソース                                                        | Example / 検証                                                                                                                 |
|-------------------------|---------------------------------------------------------------------|---------------------------------------------------|---------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| `IntArray2D`            | int配列を保持し、論理回転・転置・反転・一括更新に対応する2次元配列  | [Array2DGuide](./Array2DGuide.md)                 | [Java](../../../src/lib/ds/arrays/IntArray2D.java)            | [Example.java](../../../test/verify/ds/arrays/intarray2d/Example.java) · [Test.java](../../../test/verify/ds/arrays/Test.java) |
| `LongArray2D`           | long配列を保持し、論理回転・転置・反転・一括更新に対応する2次元配列 | [Array2DGuide](./Array2DGuide.md)                 | [Java](../../../src/lib/ds/arrays/LongArray2D.java)           | [Test.java](../../../test/verify/ds/arrays/Test.java)                                                                          |
| `CharArray2D`           | char配列の論理回転・転置・反転・一括更新                            | [Array2DGuide](./Array2DGuide.md)                 | [Java](../../../src/lib/ds/arrays/CharArray2D.java)           | —                                                                                                                              |
| `IntArrayDeque`         | int特化の可変長deque。指定値付き取得に対応                          | [ArrayDequeGuide](./ArrayDequeGuide.md)           | [Java](../../../src/lib/ds/arrays/IntArrayDeque.java)         | [Check2.java](../../../test/verify/ds/arrays/intarraydeque/Check2.java)                                                        |
| `LongArrayDeque`        | long特化の可変長deque。指定値付き取得に対応                         | [ArrayDequeGuide](./ArrayDequeGuide.md)           | [Java](../../../src/lib/ds/arrays/LongArrayDeque.java)        | —                                                                                                                              |
| `IntCollection`         | int値の走査・配列化の共通インターフェース                           | [ArrayGuide](./ArrayGuide.md)                     | [Java](../../../src/lib/ds/IntCollection.java)                | —                                                                                                                              |
| `LongCollection`        | long値の走査・配列化の共通インターフェース                          | [ArrayGuide](./ArrayGuide.md)                     | [Java](../../../src/lib/ds/LongCollection.java)               | —                                                                                                                              |
| `IntArray`              | int配列の読み取り専用インターフェース                               | [ArrayGuide](./ArrayGuide.md)                     | [Java](../../../src/lib/ds/arrays/IntArray.java)              | —                                                                                                                              |
| `LongArray`             | long配列の読み取り専用インターフェース                              | [ArrayGuide](./ArrayGuide.md)                     | [Java](../../../src/lib/ds/arrays/LongArray.java)             | —                                                                                                                              |
| `ArrayUtils`            | プリミティブ配列の集計・探索・変換などのアルゴリズム                | [ArrayUtilsGuide](./ArrayUtilsGuide.md)           | [Java](../../../src/lib/util/ArrayUtils.java)                 | [Example.java](../../../test/verify/util/mo/Example.java) · [Test.java](../../../test/verify/util/conversions/Test.java)       |
| `IntCircularArray`      | int固定長配列の論理回転・末尾取得                                   | [CircularArrayGuide](./CircularArrayGuide.md)     | [Java](../../../src/lib/ds/arrays/IntCircularArray.java)      | [Test.java](../../../test/verify/ds/arrays/Test.java)                                                                          |
| `LongCircularArray`     | long固定長配列の論理回転・末尾取得                                  | [CircularArrayGuide](./CircularArrayGuide.md)     | [Java](../../../src/lib/ds/arrays/LongCircularArray.java)     | [Test.java](../../../test/verify/ds/arrays/Test.java)                                                                          |
| `IntCompressedArray`    | int値の座標圧縮                                                     | [CompressedArrayGuide](./CompressedArrayGuide.md) | [Java](../../../src/lib/ds/arrays/IntCompressedArray.java)    | [Check3.java](../../../test/verify/ds/arrays/intcompressedarray/Check3.java)                                                   |
| `IntCompressedArray2D`  | int値の2次元座標圧縮                                                | [CompressedArrayGuide](./CompressedArrayGuide.md) | [Java](../../../src/lib/ds/arrays/IntCompressedArray2D.java)  | —                                                                                                                              |
| `LongCompressedArray`   | long値の座標圧縮                                                    | [CompressedArrayGuide](./CompressedArrayGuide.md) | [Java](../../../src/lib/ds/arrays/LongCompressedArray.java)   | —                                                                                                                              |
| `LongCompressedArray2D` | long値の2次元座標圧縮                                               | [CompressedArrayGuide](./CompressedArrayGuide.md) | [Java](../../../src/lib/ds/arrays/LongCompressedArray2D.java) | —                                                                                                                              |
| `RankType`              | 座標圧縮時の順位割り当て方式                                        | [CompressedArrayGuide](./CompressedArrayGuide.md) | [Java](../../../src/lib/ds/arrays/RankType.java)              | —                                                                                                                              |
| `IntMutableArray`       | int配列の更新インターフェース                                       | [MutableArrayGuide](./MutableArrayGuide.md)       | [Java](../../../src/lib/ds/arrays/IntMutableArray.java)       | —                                                                                                                              |
| `LongMutableArray`      | long配列の更新インターフェース                                      | [MutableArrayGuide](./MutableArrayGuide.md)       | [Java](../../../src/lib/ds/arrays/LongMutableArray.java)      | —                                                                                                                              |
| `IntPrefixSum`          | int累積和                                                           | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/IntPrefixSum.java)          | —                                                                                                                              |
| `IntPrefixSum2D`        | int配列の2次元累積和                                                | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/IntPrefixSum2D.java)        | —                                                                                                                              |
| `IntPrefixSum3D`        | int配列の3次元累積和                                                | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/IntPrefixSum3D.java)        | —                                                                                                                              |
| `LongPrefixSum`         | long累積和                                                          | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/LongPrefixSum.java)         | [Check1.java](../../../test/verify/ds/arrays/longprefixsum/Check1.java)                                                        |
| `LongPrefixSum2D`       | long配列の2次元累積和                                               | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/LongPrefixSum2D.java)       | —                                                                                                                              |
| `LongPrefixSum3D`       | long配列の3次元累積和                                               | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/LongPrefixSum3D.java)       | —                                                                                                                              |
| `IntPrefixModSum`       | int剰余累積和                                                       | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/IntPrefixModSum.java)       | —                                                                                                                              |
| `IntPrefixModSum2D`     | int配列の2次元剰余累積和                                            | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/IntPrefixModSum2D.java)     | —                                                                                                                              |
| `IntPrefixModSum3D`     | int配列の3次元剰余累積和                                            | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/IntPrefixModSum3D.java)     | —                                                                                                                              |
| `LongPrefixModSum`      | long剰余累積和                                                      | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/LongPrefixModSum.java)      | —                                                                                                                              |
| `LongPrefixModSum2D`    | long配列の2次元剰余累積和                                           | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/LongPrefixModSum2D.java)    | —                                                                                                                              |
| `LongPrefixModSum3D`    | long配列の3次元剰余累積和                                           | [PrefixSumGuide](./PrefixSumGuide.md)             | [Java](../../../src/lib/ds/arrays/LongPrefixModSum3D.java)    | —                                                                                                                              |

## 選択の目安

- 値を読むだけの共通引数には `IntArray` / `LongArray`
- 固定長で要素を回転させるなら `CircularArray`
- 先頭・末尾の追加削除が必要なら `ArrayDeque`
- 更新せず区間和を繰り返すなら `PrefixSum`、各和を法で保持するなら `PrefixModSum`
- 値を順位へ写すなら `CompressedArray`
- 集計、線形探索、反転、LIS、固定幅窓、部分集合の個数には `ArrayUtils`

## 共通方針

- `int` / `long` をプリミティブのまま扱い、ボクシングと不要な割り当てを避ける。
- `IntArray` / `LongArray` を受け取る API により、具体実装を差し替えられる。
- 添字・座標が範囲外の場合に指定値を返す getOrDefault を利用できる。1次元版は配列インターフェースの既定実装を提供する。
- `IntArray` / `LongArray` は末尾取得の `getLast` と、空の場合に指定値を返す `getLastOrDefault` を提供する。詳細は [ArrayGuide](./ArrayGuide.md) を参照する。
- 更新可能配列は `fill` / `setAll` により実装を問わず一括更新できる。
- コンテストの問題制約を満たす入力を前提とし、全ての不正引数を検査する汎用コレクションではない。
- 配列長、添字、窓幅などの事前条件は各 Guide に記載する。

## import

```java
import lib.ds.arrays.*;
import lib.util.ArrayUtils;
```

提出用ソースでは通常の `lib.*` import を使用し、AtCoder 側のバンドラで展開する。
