# GeometryUtils 利用ガイド

> `GeometryUtils`は、浮動小数点座標による2次元・3次元の交差、包含、距離を計算する静的ユーティリティである。

[← README: GeometryUtils](./README.md) · ソース: [GeometryUtils.java](../../../src/lib/math/GeometryUtils.java)

**目次**　[概要](#概要) · [特徴](#特徴) · [依存関係](#依存関係) · [API](#api) · [利用例](#利用例) · [制約・注意事項](#制約注意事項) · [性能特性](#性能特性) · [実装履歴](#実装履歴)

---

## 概要

`GeometryUtils`は、浮動小数点座標による2次元・3次元の交差、包含、距離を計算する静的ユーティリティである。

## 特徴

- 線分、矩形、直方体を対象とする交差判定
- 直方体と三角形への点の包含判定
- 2次元・3次元・N次元の距離
- 許容誤差`1e-10`を使用

## 依存関係

- `java.lang.Math`

## API

### 交差・包含

| メソッド                | 戻り値 | 説明                      |
|-------------------------|--------|---------------------------|
| `crossLine(...)`        | `int`  | 2次元の線分交差状態       |
| `crossLine3D(...)`      | `int`  | 3次元の線分交差状態       |
| `crossRect(...)`        | `int`  | 2次元の矩形交差状態       |
| `crossRect3D(...)`      | `int`  | 3次元の直方体交差状態     |
| `containsPoint(...)`    | `int`  | 点と直方体の位置関係      |
| `containsPointTri(...)` | `int`  | 点と3次元三角形の位置関係 |

### 距離

| メソッド                                                            | 戻り値   | 説明          |
|---------------------------------------------------------------------|----------|---------------|
| `euclidDist(...)`、`euclidDist3D(...)`、`euclidDistN(...)`          | `double` | Euclid距離    |
| `manhattanDist(...)`、`manhattanDist3D(...)`、`manhattanDistN(...)` | `double` | Manhattan距離 |
| `chebyshevDist(...)`、`chebyshevDist3D(...)`、`chebyshevDistN(...)` | `double` | Chebyshev距離 |

## 利用例

```java
import lib.math.GeometryUtils;

double distance = GeometryUtils.euclidDist(0, 0, 3, 4); // 5.0
int intersection = GeometryUtils.crossLine(0, 0, 4, 4, 0, 4, 4, 0);
```


- [Example.java](../../../test/example/math/geometryutils/Example.java)



## 制約・注意事項

- 交差・包含メソッドは単純なbooleanではなく、位置関係を表す整数コードを返す。詳細は各メソッドのJavaDocを参照すること。
- N次元距離では配列長が`n`以上であることを前提とする。
- 浮動小数点誤差と座標規模に応じ、必要なら問題側で整数幾何を選択すること。

## 性能特性

- 固定次元メソッド: $\mathcal{O}(1)$時間・空間
- N次元距離: $\mathcal{O}(n)$時間、$\mathcal{O}(1)$追加空間

## 実装履歴

| バージョン番号     | 年月日     | 詳細                                    |
|--------------------|------------|-----------------------------------------|
| **バージョン 2.0** | 2026-08-01 | 2次元・3次元の交差、包含、距離APIを整備 |
