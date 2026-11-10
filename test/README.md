# テスト・利用例の構成

`test/` をソースルートとし、用途ごとに配置する。配下の階層はライブラリの分野に対応させる。

| フォルダ                           | 用途                                         | 実行方式                                            |
|------------------------------------|----------------------------------------------|-----------------------------------------------------|
| [example](./example/README.md)     | 利用例・出力デモ                             | main を個別に実行する。標準入力を必要とする例もある |
| [unit](./unit/README.md)           | 期待値や参照実装と照合するローカル検証       | JUnit Jupiter。Gradle または IntelliJ で実行する    |
| [judge](./judge/README.md)         | オンラインジャッジの問題を解く検証プログラム | 対応する問題の入力を標準入力から与える              |
| [benchmark](./benchmark/README.md) | 性能測定ドライバ・比較用実装・入力生成       | 専用の引数・入力・ランナーで実行する                |

## 命名・移動方針

- package は `test/` からの相対ディレクトリに一致させる。例えば `test/judge/ds/arrays/intarraydeque/Judge2.java` の package は `judge.ds.arrays.intarraydeque` である。
- オンラインジャッジ用クラスは `Judge*.java` とする。旧 Check 系の番号は維持する。テンプレートは親リポジトリの `templates/java/Judge.java`（IDE のファイルテンプレート）を使う。
- 利用例は1クラスにつき1ファイルとし、`test/example/<lib のパッケージ>/{ClassName}Example.java` に置く。
- benchmark の既存の Test は、ファイル名と処理本体を維持して移動した。ファイル名だけで用途を判定しない。
- unit の配置と命名は [unit の README](./unit/README.md) に従う。
- Judge の配置は受理済みであることを意味しない。比較用実装を内包するファイルもあり、現在の `src/lib` を直接検証するかは各ファイルで確認する。

## IntelliJ IDEA

親リポジトリの `.ide/idea` を開く。`.ide/idea` は `library/java` を Gradle プロジェクトとしてリンクしており（Gradle ツールウィンドウから設定する）、IDE が `build.gradle.kts` から `library`・`library.main`（`src/`）・`library.test`（`test/`）の3つのモジュールを作る。JUnit などの依存も Gradle の設定から入る。4つの用途別フォルダを追加のソースルートにはしない。
`build.gradle.kts` や `gradle/libs.versions.toml` を変えたら、Gradle ツールウィンドウの Reload All Gradle Projects で読み込み直す。設定の詳細は親リポジトリの `docs/IDE_SETUP.md` を参照する。

JUnit のテストはガターから1件ずつ実行できる。example・judge・benchmark の main も従来どおり個別に実行・デバッグできる。

## コマンドライン

Gradle Wrapper を使うため、Gradle 本体のインストールは不要である。`JAVA_HOME` の JDK で実行し（手元では 24.0.2）、`--release 24` でコンパイルする。

| コマンド             | 内容                                                                              |
|----------------------|-----------------------------------------------------------------------------------|
| `./gradlew test`     | JUnit のテストを実行する。`slow` タグのテストは除く                               |
| `./gradlew check`    | `test` に加え、`compileCompat17` で `lib.io.compat17` の Java 17 互換性を検査する |
| `./gradlew build`    | コンパイル・テスト・検査をまとめて行う                                            |
| `./gradlew slowTest` | `slow` タグのテストだけを、時間計測を歪めないよう `-ea` なしで実行する            |

JUnit が検出するのは `@Test` などの注釈を持つクラスだけであり、example・judge・benchmark の main はテストとして実行されない。

Gradle を使わずに example などの main を実行する場合は、ライブラリを先にコンパイルし、JUnit を使わない test のソースを同じ出力先へコンパイルする。unit は JUnit の jar がないとコンパイルできないため除く。

```powershell
javac --release 24 -encoding UTF-8 -d out (rg --files src -g '*.java')
javac --release 24 -encoding UTF-8 -cp out -d out/test (rg --files test -g '*.java' -g '!test/unit/**')

java -cp "out;out/test" example.ds.arrays.IntArray2DExample
```

Judge は入力を用意して対応する main を起動する。ベンチマークの手順は [Java 17 FastIO Benchmark](../docs/io/java17/benchmark/README.md) / [Java 24 FastIO Benchmark](../docs/io/java24/benchmark/README.md) を参照する。

ビルド成果物をコミットしない。一時的な検証には専用の一時ディレクトリを使い、作業終了時に削除する。
