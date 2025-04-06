# FastScanner 利用ガイド

## 概要

`FastScanner`は、競技プログラミング向けに設計された高速入力クラスです。  
内部バッファを利用してInputStream（標準では`System.in`）からの入力を効率的に処理し、通常の`Scanner`クラスよりも高速な処理が可能です。

## 特徴

- 内部バッファを利用した効率的な入力処理
- 余分なオブジェクト生成を避けた高速な実装
- ASCII範囲内の文字を処理（全角文字など ASCII 範囲外の文字は正しく処理できません）
- 半角スペースや改行で区切られた入力を前提とする設計
- `AutoCloseable`インターフェースを実装し、`try-with-resources`構文に対応

## 依存関係

- Java標準のI/Oライブラリ（`InputStream`）
- `BigInteger`および`BigDecimal`クラス（大きな数値の処理用）

## 主な機能

### 基本的な入力メソッド

| メソッド                  | 戻り値の型           | 説明                            |
|-----------------------|-----------------|-------------------------------|
| `nextInt()`           | `int`           | 次の整数値を読み込む                    |
| `nextLong()`          | `long`          | 次の長整数値を読み込む                   |
| `nextDouble()`        | `double`        | 次の浮動小数点値を読み込む                 |
| `nextChar()`          | `char`          | 次の文字（非空白）を読み込む                |
| `next()`              | `String`        | 次のトークン（空白区切りの文字列）を読み込む        |
| `nextStringBuilder()` | `StringBuilder` | 次のトークンを`StringBuilder`として読み込む |
| `nextLine()`          | `String`        | 次の1行を読み込む（改行は含まない）            |

### 拡張数値型の入力メソッド

| メソッド               | 戻り値の型        | 説明                         |
|--------------------|--------------|----------------------------|
| `nextBigInteger()` | `BigInteger` | 次のトークンを`BigInteger`として読み込む |
| `nextBigDecimal()` | `BigDecimal` | 次のトークンを`BigDecimal`として読み込む |

### コンストラクタ

| コンストラクタ                                       | 説明                                   |
|-----------------------------------------------|--------------------------------------|
| `FastScanner()`                               | デフォルト設定（`System.in`、バッファサイズ65536バイト） |
| `FastScanner(InputStream in)`                 | 指定された入力ストリームを使用                      |
| `FastScanner(int bufferSize)`                 | 指定されたバッファサイズを使用（`System.in`）         |
| `FastScanner(InputStream in, int bufferSize)` | 指定された入力ストリームとバッファサイズを使用              |

### メソッド詳細

#### nextInt()

- **説明**: 次の整数値を読み込みます
- **戻り値**: 読み込んだ整数値（`int`型）
- **例外**: 入力がない場合や入力が整数でない場合は`RuntimeException`

#### nextLong()

- **説明**: 次の長整数値を読み込みます
- **戻り値**: 読み込んだ長整数値（`long`型）
- **例外**: 入力がない場合や入力が長整数でない場合は`RuntimeException`

#### nextDouble()

- **説明**: 次の浮動小数点値を読み込みます
- **戻り値**: 読み込んだ浮動小数点値（`double`型）
- **例外**: 入力がない場合や入力が数値でない場合は`RuntimeException`

#### nextChar()

- **説明**: 次の非空白文字を読み込みます
- **戻り値**: 読み込んだ文字（`char`型）
- **例外**: 入力がない場合は`RuntimeException`

#### next()

- **説明**: 次の空白で区切られた文字列（トークン）を読み込みます
- **戻り値**: 読み込んだ文字列（`String`型）
- **例外**: 入力がない場合は`RuntimeException`

#### nextStringBuilder()

- **説明**: 次の文字列をStringBuilderとして読み込みます（String生成のコストを抑える）
- **戻り値**: 読み込んだ文字列（`StringBuilder`型）
- **例外**: 入力がない場合は`RuntimeException`

#### nextLine()

- **説明**: 次の1行を読み込みます（改行文字は含まれません）
- **戻り値**: 読み込んだ行（`String`型）
- **例外**: 入力がない場合は`RuntimeException`

#### close()

- **説明**: 入力ストリームを閉じます（`System.in`の場合は閉じません）
- **例外**: `IOException`が発生した場合

## 利用例

```java
// 基本的な使用例
try(FastScanner sc = new FastScanner()){
int n = sc.nextInt();
long m = sc.nextLong();
double d = sc.nextDouble();
String s = sc.next();
String line = sc.nextLine();
// 処理
} 

// カスタムストリームとバッファサイズの指定
try(FastScanner sc = new FastScanner(new FileInputStream("input.txt"), 8192)) {
	// ファイルからの入力処理
}
```

## 注意事項

1. ASCII範囲外の文字（全角文字など）は正しく処理できません
2. 入力は半角スペースまたは改行で区切られていることを前提としています
3. バッファサイズは処理するデータ量に合わせて適切に設定してください
4. `try-with-resources`で使用すると、リソースの自動クローズが保証されます

## パフォーマンス特性

- 時間計算量: トークン数に対して線形（O(n)）
- 空間計算量: 指定したバッファサイズ（デフォルト: 65536バイト）

## バージョン情報

- 新機能: 大きな数値型（`BigInteger`、`BigDecimal`）のサポート
- メモリ効率化のために`nextStringBuilder()`メソッドの追加
