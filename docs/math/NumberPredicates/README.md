# NumberPredicates

[`NumberPredicates`](../../../src/lib/math/NumberPredicates.java) は、整数の性質を判定する静的ユーティリティです。

| メソッド                  | 説明                     |
|---------------------------|--------------------------|
| `isPerfectNumber(long n)` | 完全数か判定             |
| `isPalindrome(long n)`    | 十進表記が回文か判定     |
| `isFibonacci(long n)`     | フィボナッチ数か判定     |
| `isArmstrong(long n)`     | アームストロング数か判定 |
| `isHappyNumber(long n)`   | ハッピー数か判定         |

`isFibonacci`は`5 * n * n + 4`が`long`に収まる`0 <= n <= 1_358_187_913`を前提とします。その他のメソッドも途中計算が`long`に収まる範囲で使用します。

平方数・立方数の判定は`MathUtils.isSquare`、`MathUtils.isCube`へ集約されています。
