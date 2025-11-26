# IntSegmentTree 利用ガイド

## 概要

`IntSegmentTree` は、`int` 型の配列に対する範囲クエリと点更新を効率的に処理するための、セグメント木の `int` 特化実装です。
`int` 型の合計、最小値、最大値などを高速に計算する必要がある場合に使用します。

## 特徴

- **`int` 型への特化**:
	- `int` プリミティブ型を直接扱うことで、ジェネリック版の `SegmentTree`
	  で発生するボクシング・アンボクシングのオーバーヘッドをなくし、優れたパフォーマンスを実現します。
- **パフォーマンス**:
	- 配列を利用したボトムアップ方式で実装されており、再帰呼び出しのオーバーヘッドがありません。
	- `set` による更新処理は遅延評価され、`query` 系メソッド実行前にまとめて実行されるため、更新が連続する際の効率が向上します。
- **境界探索**:
	- `maxRight` と `minLeft` を備え、条件を満たす最大/最小の端点を $O(\log N)$ で探索できます。

## 依存関係

- `java.util.function.IntBinaryOperator`
- `java.util.function.IntUnaryOperator`
- `java.util.function.IntPredicate`

## 主な機能（メソッド一覧）

### 1. コンストラクタ

| メソッド                                                               | 説明                                                        |
|--------------------------------------------------------------------|-----------------------------------------------------------|
| `IntSegmentTree(int n, IntBinaryOperator func, int identity)`      | サイズ `n`、`int` 集約ルール `func`、単位元 `identity` を指定して空の木を構築します。 |
| `IntSegmentTree(int[] data, IntBinaryOperator func, int identity)` | 既存配列 `data` から木を構築します。長さは自動でサイズとして採用され、即利用可能です。           |

### 2. 更新系メソッド

| メソッド                                   | 説明                                                      |
|----------------------------------------|---------------------------------------------------------|
| `set(int i, int e)`                    | インデックス `i` の要素を `e` に更新します。更新は遅延キューに記録され、クエリ前に一括反映されます。 |
| `update(int i, IntUnaryOperator func)` | インデックス `i` の値に `func` を適用して更新します。                       |
| `fill(int value)`                      | すべての葉を `value` で埋め、木全体を再構築します。                          |
| `setAll(IntUnaryOperator func)`        | 全葉に `func` を適用し、一括更新します。                                |

### 3. クエリ系メソッド

| メソッド                                   | 説明                                                                   |
|----------------------------------------|----------------------------------------------------------------------|
| `get(int i)`                           | インデックス `i` の現在値を取得します。                                               |
| `query(int l, int r)`                  | 閉区間 `[l, r]` の集約結果を返します。                                             |
| `queryAll()`                           | すべての要素を集約した結果（根ノードの値）を返します。                                          |
| `maxRight(int l, IntPredicate tester)` | 左端 `l` を固定し、条件 `tester` を満たす最大右端を返します。満たさない場合は `elementCount` を返します。 |
| `minLeft(int r, IntPredicate tester)`  | 右端 `r` を固定し、条件を満たす最小左端を返します。満たさない場合は `0` を返します。                      |

### 4. その他

| メソッド         | 説明                                        |
|--------------|-------------------------------------------|
| `size()`     | 管理している要素数（葉の数）を返します。                      |
| `iterator()` | 葉を順に走査する `PrimitiveIterator.OfInt` を返します。 |
| `toString()` | 内部配列全体を空白区切りで列挙した文字列を返します。                |

## 利用例

`IntSegmentTree` を使って、配列の範囲合計（Range Sum Query）を管理する例です。

```java
import java.util.function.IntBinaryOperator;

public class IntSegmentTreeExample {
    public static void main(String[] args) {
        int n = 10;
        IntBinaryOperator sumFunc = (a, b) -> a + b;
        int identity = 0;
        var segTree = new IntSegmentTree(n, sumFunc, identity);

        segTree.set(0, 5);
        segTree.set(1, 3);
        segTree.set(2, 7);
        segTree.set(3, 1);
        segTree.set(4, 8);

        int sum1 = segTree.query(1, 3);
        System.out.println("Sum of range [1, 3]: " + sum1);

        segTree.update(2, v -> v + 3);
        int sum2 = segTree.query(1, 3);
        System.out.println("Sum of range [1, 3] after update: " + sum2);

        int right = segTree.maxRight(0, acc -> acc <= 20);
        System.out.println("Max right with sum <= 20: " + right);
    }
}
```

## 注意事項

- クエリ `query(l, r)` の範囲は閉区間 `[l, r]`（`l` と `r` を両方含む）です。
- `set` 系更新は遅延キューに積まれ、`query`/`queryAll`/`maxRight`/`minLeft` などを呼び出す際に自動で反映されるため、利用者が
  `build` を明示的に呼ぶ必要はありません。
- `identity` が `0` でない場合のみ、空の葉を単位元で初期化します。必要な単位元を必ず渡してください。

## パフォーマンス特性

- **時間計算量**:
	- `set`/`update`: $O(\log N)$
	- `query`/`queryAll`/`maxRight`/`minLeft`: $O(\log N)$
	- `get`: $O(1)$
- **空間計算量**:
	- 全体: $O(N)$

## バージョン情報

| バージョン番号       | 年月日        | 詳細                           |
|:--------------|:-----------|:-----------------------------|
| **バージョン 1.0** | 2025-11-25 | 初回実装。                        |
| **バージョン 2.0** | 2025-11-27 | コンストラクタ・API 説明、境界探索機能の記述を追加。 |

### バージョン管理について

バージョン番号は2桁で管理します：

- 1桁目（メジャーバージョン）: メソッドの追加や機能拡張があった場合に更新
- 2桁目（マイナーバージョン）: 誤字修正、バグ修正、マイクロ高速化などの小さな更新があった場合に更新
