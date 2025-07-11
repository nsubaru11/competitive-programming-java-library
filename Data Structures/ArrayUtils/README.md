# 配列ユーティリティ (Array Utilities)

## 概要

配列操作を効率的に行うためのユーティリティクラスを提供します。物理的なデータ移動を最小限に抑えながら、循環配列や回転可能な2次元配列などの機能を実現します。

## 実装クラス

### [Array1D](./src/Array1D.java)

- **用途**: 1次元配列に対する拡張機能を提供するラッパークラス
- **特徴**:
	- 循環インデックス（負のインデックスや範囲外のインデックスを自動的に調整）
	- 効率的な左右シフト操作（物理的なデータ移動なし）
	- Iterableインターフェースの実装によるfor-each構文のサポート
- **主な操作**:
	- `get(int index)`: 循環インデックスを考慮して要素を取得
	- `set(int index, int value)`: 要素を設定
	- `lShift()`: 配列を左にシフト（実際には内部カウンタを調整）
	- `rShift()`: 配列を右にシフト（実際には内部カウンタを調整）
- **時間計算量**:
	- アクセス/設定: O(1)
	- シフト操作: O(1)
- **空間計算量**: O(n)、ここでnは配列のサイズ

### [Array2D](./src/Array2D.java)

- **用途**: 2次元配列に対する拡張機能を提供するラッパークラス
- **特徴**:
	- 効率的な回転操作（物理的なデータ移動なし）
	- 内部的には1次元配列として格納し、アクセス時に座標変換
	- Iterableインターフェースの実装によるfor-each構文のサポート
- **主な操作**:
	- `get(int i, int j)`: 回転を考慮して要素を取得
	- `set(int i, int j, int value)`: 要素を設定
	- `lRotate()`: 配列を左（反時計回り）に90度回転
	- `rRotate()`: 配列を右（時計回り）に90度回転
- **時間計算量**:
	- アクセス/設定: O(1)
	- 回転操作: O(1)
- **空間計算量**: O(n*m)、ここでnとmは配列の行数と列数

## 使用例

### Array1D の使用例

```java
// 循環配列の作成
Array1D circularArray = new Array1D(5);
for (int i = 0; i < 5; i++) {
    circularArray.set(i, i + 1);  // [1, 2, 3, 4, 5]
}

// 循環インデックスの使用
System.out.println(circularArray.get(7));  // 3 (インデックス7は循環して2になる)
System.out.println(circularArray.get(-1)); // 5 (インデックス-1は循環して4になる)

// 左シフト操作
circularArray.lShift();  // 論理的には [2, 3, 4, 5, 1] になる
System.out.println(circularArray.get(0));  // 2

// 右シフト操作
circularArray.rShift();  // 元に戻る: [1, 2, 3, 4, 5]
System.out.println(circularArray.get(0));  // 1
```

### Array2D の使用例

```java
// 2次元配列の作成と初期化
Array2D rotatingArray = new Array2D(2, 3);
int value = 0;
for (int i = 0; i < 2; i++) {
    for (int j = 0; j < 3; j++) {
        rotatingArray.set(i, j, value++);
    }
}
// 配列の内容: [0, 1, 2]
//            [3, 4, 5]

// 配列の表示
for (int i = 0; i < 2; i++) {
    for (int j = 0; j < 3; j++) {
        System.out.print(rotatingArray.get(i, j) + " ");
    }
    System.out.println();
}
// 出力:
// 0 1 2
// 3 4 5

// 右（時計回り）に90度回転
rotatingArray.rRotate();
// 論理的な配列の内容: [3, 0]
//                    [4, 1]
//                    [5, 2]

// 回転後の配列の表示（注：次元が入れ替わる）
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 2; j++) {
        System.out.print(rotatingArray.get(i, j) + " ");
    }
    System.out.println();
}
// 出力:
// 3 0
// 4 1
// 5 2
```

## 特徴と利点

1. **効率的なメモリ使用**:
	- 物理的なデータ移動を行わないため、大きな配列でも効率的に操作可能
	- シフトや回転操作がO(1)の時間計算量で実行可能

2. **使いやすいインターフェース**:
	- 循環インデックスにより、境界チェックを気にせずに配列操作が可能
	- Iterableインターフェースの実装により、for-each構文で要素を走査可能

3. **競技プログラミングでの活用**:
	- グリッドベースの問題で配列の回転が必要な場合に有用
	- 循環バッファが必要な問題で、Array1Dクラスが活用可能

## 注意事項

- Array1Dクラスは現在、整数型(int)の配列のみをサポートしています
- Array2Dクラスも同様に整数型の2次元配列のみをサポートしています
- 回転操作後は配列の論理的な次元が変わることに注意してください（例：2×3の配列が3×2になる）
- `set`メソッドは回転を考慮していないため、回転後に`set`を使用する場合は注意が必要です