# AVLTree

## 概要

AVL木は、平衡二分探索木の一種である。
このフォルダには、ジェネリック型およびプリミティブ型（`int`, `long`）に対応したAVL木の実装が含まれている。
Set（重複なし）とMultiset（重複あり）の両方を提供する。

## クラス一覧

| クラス            | 用途                                            | Guide | ソース                                                          | 利用例                                                              | テスト                                                                                                            |
|-------------------|-------------------------------------------------|-------|-----------------------------------------------------------------|---------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| `AVLSet`          | 重複を許さない要素の集合を管理する。            | —     | [AVLSet](../../../src/lib/ds/set/AVLSet.java)                   | [Example](../../../test/example/ds/set/AVLSetExample.java)          | [Unit](../../../test/unit/ds/set/AVLSetTest.java)                                                                 |
| `AVLMultiset`     | 重複を許す要素の集合を管理する。                | —     | [AVLMultiset](../../../src/lib/ds/set/AVLMultiset.java)         | [Example](../../../test/example/ds/set/AVLMultisetExample.java)     | [Unit](../../../test/unit/ds/set/AVLMultisetTest.java)                                                            |
| `IntAVLSet`       | `int` 型の重複を許さない要素の集合を管理する。  | —     | [IntAVLSet](../../../src/lib/ds/set/IntAVLSet.java)             | [Example](../../../test/example/ds/set/IntAVLSetExample.java)       | [Unit](../../../test/unit/ds/set/IntAVLSetTest.java) · [Judge1](../../../test/judge/ds/set/intavlset/Judge1.java) |
| `IntAVLMultiset`  | `int` 型の重複を許す要素の集合を管理する。      | —     | [IntAVLMultiset](../../../src/lib/ds/set/IntAVLMultiset.java)   | [Example](../../../test/example/ds/set/IntAVLMultisetExample.java)  | [Unit](../../../test/unit/ds/set/IntAVLMultisetTest.java)                                                         |
| `LongAVLSet`      | `long` 型の重複を許さない要素の集合を管理する。 | —     | [LongAVLSet](../../../src/lib/ds/set/LongAVLSet.java)           | [Example](../../../test/example/ds/set/LongAVLSetExample.java)      | [Unit](../../../test/unit/ds/set/LongAVLSetTest.java)                                                             |
| `LongAVLMultiset` | `long` 型の重複を許す要素の集合を管理する。     | —     | [LongAVLMultiset](../../../src/lib/ds/set/LongAVLMultiset.java) | [Example](../../../test/example/ds/set/LongAVLMultisetExample.java) | [Unit](../../../test/unit/ds/set/LongAVLMultisetTest.java)                                                        |

## 実装クラス

### [AVLSet](../../../src/lib/ds/set/AVLSet.java)

- **用途**: 重複を許さない要素の集合を管理する。
- **特徴**:
    - ジェネリック型 `T` に対応している。
    - 木が自動で平衡するため、各操作が安定して高速である。
- **時間計算量**:
    - 追加、削除、検索: $\mathcal{O}(\log n)$
- **空間計算量**: $\mathcal{O}(n)$

### [AVLMultiset](../../../src/lib/ds/set/AVLMultiset.java)

- **用途**: 重複を許す要素の集合を管理する。
- **特徴**:
    - ジェネリック型 `T` に対応している。
    - 同じ値の要素を複数個保持できる。
- **時間計算量**:
    - 追加、削除、検索: $\mathcal{O}(\log n)$
- **空間計算量**: $\mathcal{O}(n)$

### [IntAVLSet](../../../src/lib/ds/set/IntAVLSet.java)

- **用途**: `int` 型の重複を許さない要素の集合を管理する。
- **特徴**:
    - `int` プリミティブ型に特化しているため、ジェネリック版より高速でメモリ効率が良い。
- **時間計算量**:
    - 追加、削除、検索: $\mathcal{O}(\log n)$
- **空間計算量**: $\mathcal{O}(n)$

### [IntAVLMultiset](../../../src/lib/ds/set/IntAVLMultiset.java)

- **用途**: `int` 型の重複を許す要素の集合を管理する。
- **特徴**:
    - `int` プリミティブ型に特化しており、重複した値を保持できる。
- **時間計算量**:
    - 追加、削除、検索: $\mathcal{O}(\log n)$
- **空間計算量**: $\mathcal{O}(n)$

### [LongAVLSet](../../../src/lib/ds/set/LongAVLSet.java)

- **用途**: `long` 型の重複を許さない要素の集合を管理する。
- **特徴**:
    - `long` プリミティブ型に特化しているため、ジェネリック版より高速でメモリ効率が良い。
- **時間計算量**:
    - 追加、削除、検索: $\mathcal{O}(\log n)$
- **空間計算量**: $\mathcal{O}(n)$

### [LongAVLMultiset](../../../src/lib/ds/set/LongAVLMultiset.java)

- **用途**: `long` 型の重複を許す要素の集合を管理する。
- **特徴**:
    - `long` プリミティブ型に特化しており、重複した値を保持できる。
- **時間計算量**:
    - 追加、削除、検索: $\mathcal{O}(\log n)$
- **空間計算量**: $\mathcal{O}(n)$

## アルゴリズム（データ構造）選択ガイド

- **ジェネリック型 vs プリミティブ型**:
    - `Integer` や `Long` などのオブジェクトを扱う場合は `AVLSet` や `AVLMultiset` を使用する。
    - `int` や `long` などのプリミティブ型を扱う場合は、パフォーマンスとメモリ効率の観点から `IntAVLSet`,
      `IntAVLMultiset`,
      `LongAVLSet`, `LongAVLMultiset` の使用を推奨する。
- **Set vs Multiset**:
    - コレクション内の要素が一意である必要がある場合は `Set` を選択する。
    - コレクションに同じ要素が複数含まれる可能性がある場合は `Multiset` を選択する。

## 注意事項

- ジェネリック版のクラス (`AVLSet`, `AVLMultiset`) を使用する場合、要素の型 `T` は `Comparable` インターフェースを実装している必要がある。

## 利用例・テスト

- [AVLSetExample.java](../../../test/example/ds/set/AVLSetExample.java) · [AVLMultisetExample.java](../../../test/example/ds/set/AVLMultisetExample.java)
