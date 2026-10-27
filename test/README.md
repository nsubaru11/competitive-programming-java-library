# テスト・利用例の構成

`test/` をソースルートとし、用途ごとに配置する。配下の階層はライブラリの分野に対応させる。

| フォルダ                           | 用途                                         | 実行方式                                            |
|------------------------------------|----------------------------------------------|-----------------------------------------------------|
| [example](./example/README.md)     | 利用例・出力デモ                             | main を個別に実行する。標準入力を必要とする例もある |
| [unit](./unit/README.md)           | 期待値や参照実装と照合するローカル検証       | 現在は main と AssertionError / assert による検証   |
| [judge](./judge/README.md)         | オンラインジャッジの問題を解く検証プログラム | 対応する問題の入力を標準入力から与える              |
| [benchmark](./benchmark/README.md) | 性能測定ドライバ・比較用実装・入力生成       | 専用の引数・入力・ランナーで実行する                |

## 命名・移動方針

- package は `test/` からの相対ディレクトリに一致させる。例えば `test/judge/ds/arrays/intarraydeque/Judge2.java` の package は `judge.ds.arrays.intarraydeque` である。
- オンラインジャッジ用クラスは `Judge*.java` とする。旧 Check 系の番号は維持する。テンプレートは [Judge.java](../template/Judge.java) に置く。
- 既存の Test・Example は、ファイル名と処理本体を維持して移動した。ファイル名だけで用途を判定しない。
- [Conversions の Test](./unit/util/conversions/Test.java) は出力デモと期待値照合を併せ持つため、unit に置く。
- Judge の配置は受理済みであることを意味しない。比較用実装を内包するファイルもあり、現在の `src/lib` を直接検証するかは各ファイルで確認する。

## IntelliJ IDEA

`src/` を Sources Root、`test/` を Test Sources Root とする。4つの用途別フォルダを追加のソースルートにはしない。
既存の main はエディタから個別に実行・デバッグできる。unit の実行構成では VM options に `-ea` を指定し、Java の assert を有効にする。

現在は JUnit の依存・アノテーション・自動実行ランナーを導入していない。unit への配置だけで JUnit テストとして検出されるわけではない。

## コマンドライン

リポジトリのルートからライブラリをコンパイルした後、test の各ソースを同じ出力先へコンパイルする。JDK は原則24とする。

以下は基本コマンドである。現状で全ソースの一括コンパイルが通ることは確認していない。配列関連の実装状況は [arrays の検証状況](../docs/ds/arrays/README.md#example検証コード) を参照する。

```powershell
javac --release 24 -encoding UTF-8 -d out (rg --files src -g '*.java')
javac --release 24 -encoding UTF-8 -cp out -d out/test (rg --files test -g '*.java')

java -cp "out;out/test" example.ds.arrays.intarray2d.Example
java -ea -cp "out;out/test" unit.util.format.Test
```

Judge は入力を用意して対応する main を起動する。ベンチマークの手順は [Java 17 FastIO Benchmark](../docs/io/java17/benchmark/README.md) / [Java 24 FastIO Benchmark](../docs/io/java24/benchmark/README.md) を参照する。

ビルド成果物をコミットしない。一時的な検証には専用の一時ディレクトリを使い、作業終了時に削除する。
