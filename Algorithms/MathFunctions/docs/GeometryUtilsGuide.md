# GeometryUtils 利用ガイド

## 概要

`GeometryUtils`
は幾何学的な計算を行うためのユーティリティクラスです。2次元、3次元、およびN次元空間での直線の交差判定、長方形（直方体）の交差判定、点の包含判定、および様々な距離計算（ユークリッド距離、マンハッタン距離、チェビシェフ距離）を提供します。

## 特徴

- 2次元、3次元、およびN次元空間での幾何学的計算をサポート
- 数値誤差を考慮した実装（浮動小数点の比較には適切な許容誤差を使用）
- 座標系に依存しない汎用的な実装
- 高速な計算のための最適化されたアルゴリズム

## 依存関係

このクラスは標準ライブラリの `java.lang.Math` のみに依存しています。

## 主な機能（メソッド一覧）

全てのメソッドを種類別に分類し、オーバーロードも正しく記述しています。

### 1. 交差判定系メソッド

| メソッド                                                                                                                                                        | 戻り値の型 | 説明                      |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|-------------------------|
| crossLine(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)                                                           | int   | 2次元空間での2つの直線の交差判定を行います  |
| crossLine3D(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4)             | int   | 3次元空間での2つの直線の交差判定を行います  |
| crossLineND(int n, double[] p1, double[] p2, double[] p3, double[] p4)                                                                                      | int   | N次元空間での2つの直線の交差判定を行います  |
| crossRect(double x11, double y11, double x12, double y12, double x21, double y21, double x22, double y22)                                                   | int   | 2次元空間での2つの長方形の交差判定を行います |
| crossRect3D(double x11, double y11, double z11, double x12, double y12, double z12, double x21, double y21, double z21, double x22, double y22, double z22) | int   | 3次元空間での2つの直方体の交差判定を行います |

### 2. 包含判定系メソッド

| メソッド                                                                                                             | 戻り値の型 | 説明                         |
|------------------------------------------------------------------------------------------------------------------|-------|----------------------------|
| containsPoint(int px, int py, int pz, int x1, int y1, int z1, int x2, int y2, int z2)                            | int   | 点が直方体の内部に含まれるかを判定します       |
| containsPointTri(int px, int py, int pz, int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3) | int   | 点が3次元空間の三角形の内部に含まれるかを判定します |

### 3. 距離計算系メソッド

| メソッド                                                                         | 戻り値の型  | 説明                        |
|------------------------------------------------------------------------------|--------|---------------------------|
| eucDist(double x1, double y1, double x2, double y2)                          | double | 2次元空間での2点間のユークリッド距離を計算します |
| eucDist3D(double x1, double y1, double z1, double x2, double y2, double z2)  | double | 3次元空間での2点間のユークリッド距離を計算します |
| eucDistN(int n, double[] o1, double[] o2)                                    | double | N次元空間での2点間のユークリッド距離を計算します |
| manDist(double x1, double y1, double x2, double y2)                          | double | 2次元空間での2点間のマンハッタン距離を計算します |
| manDist3D(double x1, double y1, double z1, double x2, double y2, double z2)  | double | 3次元空間での2点間のマンハッタン距離を計算します |
| manDistN(int n, double[] o1, double[] o2)                                    | double | N次元空間での2点間のマンハッタン距離を計算します |
| chebDist(double x1, double y1, double x2, double y2)                         | double | 2次元空間での2点間のチェビシェフ距離を計算します |
| chebDist3D(double x1, double y1, double z1, double x2, double y2, double z2) | double | 3次元空間での2点間のチェビシェフ距離を計算します |
| chebDistN(int n, double[] o1, double[] o2)                                   | double | N次元空間での2点間のチェビシェフ距離を計算します |

## 利用例

```java
// 2つの直線が交差するかを判定
int crossResult = GeometryUtils.crossLine(0, 0, 10, 10, 0, 10, 10, 0);
if(crossResult >0){
		System.out.

println("直線は交差しています");
}else if(crossResult ==0){
		System.out.

println("直線は完全に一致しています");
}else{
		System.out.

println("直線は交差していません");
}

// 2つの長方形が交差するかを判定
int rectIntersect = GeometryUtils.crossRect(0, 0, 5, 5, 3, 3, 8, 8);
if(rectIntersect >0){
		System.out.

println("長方形は交差しています");
}else if(rectIntersect ==0){
		System.out.

println("一方の長方形がもう一方を含んでいます");
}else{
		System.out.

println("長方形は交差していません");
}

// 点が三角形の内部にあるかを判定
int pointInTriangle = GeometryUtils.containsPointTri(2, 2, 2, 0, 0, 0, 5, 0, 0, 0, 5, 5);
if(pointInTriangle >0){
		System.out.

println("点は三角形の内部にあります");
}else if(pointInTriangle ==0){
		System.out.

println("点は三角形の境界上にあります");
}else{
		System.out.

println("点は三角形の外部にあります");
}

// 2点間のユークリッド距離を計算
double distance = GeometryUtils.eucDist(0, 0, 3, 4);
System.out.

println("2点間のユークリッド距離: "+distance);
```

## 注意事項

- 浮動小数点演算を使用しているため、数値誤差が発生する可能性があります。許容誤差（1e-10）を使用して比較を行っています。
- 交差判定メソッドの戻り値は整数値で、正の値は交差を、0以下の値は非交差を示します。
	- 2: 垂直に交差している場合
	- 1: 交差している場合
	- 0: 直線が完全に等しい場合（平行）
	- -1: 一方の直線がもう一方の直線を含む場合（平行）
	- -2: 片方の直線の端点との一方の直線の端点が接する場合（平行）
	- -3: 一方の直線の端点がもう一方の直線に含まれる場合（垂直）
	- -4: 一方の直線の端点がもう一方の直線に含まれる場合（ねじれ）
	- -5: 交差していない場合（平行）
	- -6: 交差していない場合（垂直）
	- -7: 交差していない場合（ねじれ）
- 座標が逆転している場合（例：最小点の座標が最大点の座標より大きい場合）は、メソッド内部で自動的に修正されます。
- N次元空間のメソッドでは、配列の長さが次元数以上であることを確認してください。

## パフォーマンス特性

- 交差判定メソッドの時間計算量：O(1)（固定次元の場合）
- N次元空間のメソッドの時間計算量：O(n)（nは次元数）
- 空間計算量：すべてのメソッドでO(1)または O(n)（nは次元数）

## バージョン情報

| バージョン番号       | 年月日        | 詳細                                                             |
|:--------------|:-----------|:---------------------------------------------------------------|
| **バージョン 1.3** | 2023-12-25 | Javadocコメントを改善し、各メソッドの詳細な説明と使用例を追加。                            |
| **バージョン 1.2** | 2023-12-20 | メソッド名を短縮化し、パフォーマンスを最適化。変数名を簡略化。                                |
| **バージョン 1.1** | 2023-12-15 | 交差判定メソッドの戻り値を詳細化。manhattanDistanceNとchebyshevDistanceNメソッドを追加。 |
| **バージョン 1.0** | 2023-12-01 | 初期実装。2次元および3次元空間での基本的な幾何学的計算機能を提供。                             |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
