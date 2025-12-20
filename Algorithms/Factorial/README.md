# Factorial

## 概要

階乗計算とそれに関連する組み合わせ計算（組み合わせ数、順列数）を効率的に行うためのライブラリです。
競技プログラミングでよく使用される、モジュロ演算を用いた実装を提供しています。

## 実装クラス

### [FactorialTable](src/FactorialTable.java)

- 用途: 階乗、逆階乗、組み合わせ数、順列数、重複組み合わせ数を効率的に計算するためのテーブルクラス
- 特徴:
	- 指定された最大値 n までの階乗と逆階乗を事前計算
	- モジュロ演算に対応（デフォルト: 998244353）
	- フェルマーの小定理による逆元計算（mod は素数を想定）
	- 提供メソッド: fact(n), invFact(n), nCr(n, r), nPr(n, r), nHr(n, r)
	- コンストラクタ: new FactorialTable(), new FactorialTable(n), new FactorialTable(n, mod)

### [FactorialNaive](src/FactorialNaive.java)

- 用途: 小規模検証や単純な計算用のナイーブ実装（事前計算なし）
- 提供メソッド: fact(n), modFact(n, mod), invFact(n, mod)

### [Example](src/Example.java)

- 用途: FactorialTable と FactorialNaive の整合性チェック用の簡易サンプル

## 注意事項

- 事前計算のため、指定した最大値 n を超える引数には対応しません（範囲外アクセスに注意）
- モジュロ演算を使用しているため、結果は常に指定した mod の余りになります
- 逆元計算はフェルマーの小定理を用いるため、mod は素数である必要があります

## 使い方（最小例）

```
// 例: n=10^6 まで、mod=998244353 で事前計算
FactorialTable ft = new FactorialTable(1_000_000, 998244353);

int n = 5, r = 2;
int nCr = ft.nCr(n, r); // 5C2
int nPr = ft.nPr(n, r); // 5P2
int nHr = ft.nHr(n, r); // 重複組み合わせ
```

コンパイル/実行（PowerShell の例）:

```
javac -encoding UTF-8 -d out "Algorithms\Factorial\src\FactorialTable.java" "Algorithms\Factorial\src\FactorialNaive.java"
// 任意の Main を用意した場合
javac -encoding UTF-8 -cp out -d out Main.java
java -cp out Main
```