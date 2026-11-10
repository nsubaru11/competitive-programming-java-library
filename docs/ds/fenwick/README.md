# Binary Indexed Tree

## 概要

バイナリインデックスツリー（BIT）、別名フェニック木（Fenwick Tree）は、配列の要素に対する累積和の計算と要素の更新を効率的に行うためのデータ構造である。
特に、要素の更新と区間和の計算が頻繁に必要とされる問題に適している。

## クラス一覧

| クラス         | 用途                                                           | Guide                     | ソース                                                        | 利用例                                                               | テスト                                                                                                                |
|----------------|----------------------------------------------------------------|---------------------------|---------------------------------------------------------------|----------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `BIT`          | 二項演算・逆演算・単位元を指定する汎用BIT。                    | [BITGuide](./BITGuide.md) | [BIT](../../../src/lib/ds/fenwick/BIT.java)                   | [Example](../../../test/example/ds/fenwick/BITExample.java)          | [Unit](../../../test/unit/ds/fenwick/BITTest.java)                                                                    |
| `LongBIT`      | `long`値向けの1次元BIT。点更新・区間和・累積和境界探索に対応。 | [BITGuide](./BITGuide.md) | [LongBIT](../../../src/lib/ds/fenwick/LongBIT.java)           | [Example](../../../test/example/ds/fenwick/LongBITExample.java)      | [Unit](../../../test/unit/ds/fenwick/LongBITTest.java) · [Judge1](../../../test/judge/ds/fenwick/longbit/Judge1.java) |
| `IntBIT`       | `int`値向けの1次元BIT。点更新・区間和・累積和境界探索に対応。  | [BITGuide](./BITGuide.md) | [IntBIT](../../../src/lib/ds/fenwick/IntBIT.java)             | [Example](../../../test/example/ds/fenwick/IntBITExample.java)       | [Unit](../../../test/unit/ds/fenwick/IntBITTest.java)                                                                 |
| `LongBIT2D`    | `long`値向けの2次元BIT。点更新と矩形領域和に対応。             | [BITGuide](./BITGuide.md) | [LongBIT2D](../../../src/lib/ds/fenwick/LongBIT2D.java)       | [Example](../../../test/example/ds/fenwick/LongBIT2DExample.java)    | [Unit](../../../test/unit/ds/fenwick/LongBIT2DTest.java)                                                              |
| `IntBIT2D`     | `int`値向けの2次元BIT。点更新と矩形領域和に対応。              | [BITGuide](./BITGuide.md) | [IntBIT2D](../../../src/lib/ds/fenwick/IntBIT2D.java)         | [Example](../../../test/example/ds/fenwick/IntBIT2DExample.java)     | [Unit](../../../test/unit/ds/fenwick/IntBIT2DTest.java)                                                               |
| `LongRangeBIT` | `long`値向けの区間加算・区間和BIT。                            | [BITGuide](./BITGuide.md) | [LongRangeBIT](../../../src/lib/ds/fenwick/LongRangeBIT.java) | [Example](../../../test/example/ds/fenwick/LongRangeBITExample.java) | [Unit](../../../test/unit/ds/fenwick/LongRangeBITTest.java)                                                           |
| `IntRangeBIT`  | `int`値向けの区間加算・区間和BIT。                             | [BITGuide](./BITGuide.md) | [IntRangeBIT](../../../src/lib/ds/fenwick/IntRangeBIT.java)   | [Example](../../../test/example/ds/fenwick/IntRangeBITExample.java)  | [Unit](../../../test/unit/ds/fenwick/IntRangeBITTest.java)                                                            |

## API・計算量・利用条件

全クラスのシグネチャ、添字検査と空区間の扱い、係数指定の `affine` と関数指定の `apply`、更新前の値を返す `set`、int版の要素とlong型の集計値の区別は [BIT 利用ガイド](./BITGuide.md) を参照する。
LongBIT2D の初期化関数は `IntBinaryToLongFunction` を使う。
