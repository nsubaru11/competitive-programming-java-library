# FastIO17 Benchmark

ベンチマーク本体は [test/benchmark/io/bench17](../../../../test/benchmark/io/bench17/) に置く。既存の比較処理は変更せず、ランナーの参照先を新しい配置へ合わせている。

## 実行

JDK 17 を利用し、`batchfiles/test1.bat` から `test6.bat` の対象を実行する。各バッチは対応するソースと依存ライブラリをコンパイルし、`benchmark.io.bench17.*` を起動する。

- 入力データ: このディレクトリの `work/TestCases/` に、各バッチが参照する名前で配置する。
- クラスファイル: `work/out/`
- 測定結果: `work/Output/`

`GenerateTestCase.java` は今回の移動では処理本体を変更しておらず、旧配置を基準とする出力パスを保持している。入力生成先の指定は未対応のため、生成済みデータを上記の入力ディレクトリへ配置する。

`work/` はGit管理対象外である。今回の配置整理ではコンパイル・ベンチマークの実行は行っていない。
