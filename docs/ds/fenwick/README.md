# Binary Indexed Tree

## 概要

バイナリインデックスツリー（BIT）、別名フェニック木（Fenwick Tree）は、配列の要素に対する累積和の計算と要素の更新を効率的に行うためのデータ構造である。
特に、要素の更新と区間和の計算が頻繁に必要とされる問題に適している。

## クラス一覧

| クラス         | 用途                                                           | Guide                     | ソース                                                | Example / 検証                                                                                                                         |
|----------------|----------------------------------------------------------------|---------------------------|-------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| `BIT`          | 二項演算・逆演算・単位元を指定する汎用BIT。                    | [BITGuide](./BITGuide.md) | [Java](../../../src/lib/ds/fenwick/BIT.java)          | [BITTest.java](../../../test/unit/ds/fenwick/BITTest.java)                                                                             |
| `LongBIT`      | `long`値向けの1次元BIT。点更新・区間和・累積和境界探索に対応。 | [BITGuide](./BITGuide.md) | [Java](../../../src/lib/ds/fenwick/LongBIT.java)      | [Judge1.java](../../../test/judge/ds/fenwick/longbit/Judge1.java) · [LongBITTest.java](../../../test/unit/ds/fenwick/LongBITTest.java) |
| `IntBIT`       | `int`値向けの1次元BIT。点更新・区間和・累積和境界探索に対応。  | [BITGuide](./BITGuide.md) | [Java](../../../src/lib/ds/fenwick/IntBIT.java)       | [IntBITTest.java](../../../test/unit/ds/fenwick/IntBITTest.java)                                                                       |
| `LongBIT2D`    | `long`値向けの2次元BIT。点更新と矩形領域和に対応。             | [BITGuide](./BITGuide.md) | [Java](../../../src/lib/ds/fenwick/LongBIT2D.java)    | [LongBIT2DTest.java](../../../test/unit/ds/fenwick/LongBIT2DTest.java)                                                                 |
| `IntBIT2D`     | `int`値向けの2次元BIT。点更新と矩形領域和に対応。              | [BITGuide](./BITGuide.md) | [Java](../../../src/lib/ds/fenwick/IntBIT2D.java)     | [IntBIT2DTest.java](../../../test/unit/ds/fenwick/IntBIT2DTest.java)                                                                   |
| `LongRangeBIT` | `long`値向けの区間加算・区間和BIT。                            | [BITGuide](./BITGuide.md) | [Java](../../../src/lib/ds/fenwick/LongRangeBIT.java) | [LongRangeBITTest.java](../../../test/unit/ds/fenwick/LongRangeBITTest.java)                                                           |
| `IntRangeBIT`  | `int`値向けの区間加算・区間和BIT。                             | [BITGuide](./BITGuide.md) | [Java](../../../src/lib/ds/fenwick/IntRangeBIT.java)  | [IntRangeBITTest.java](../../../test/unit/ds/fenwick/IntRangeBITTest.java)                                                             |

## API・計算量・利用条件

全クラスのシグネチャ、添字検査と空区間の扱い、係数指定の `affine` と関数指定の `apply`、更新前の値を返す `set`、int版の要素とlong型の集計値の区別は [BIT 利用ガイド](./BITGuide.md) を参照する。
LongBIT2D の初期化関数は `IntBinaryToLongFunction` を使う。
