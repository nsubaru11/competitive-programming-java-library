# Gradle 導入計画

単体テスト・網羅率計測・性能測定・CI を整備するため、Gradle を導入する計画である。本書は作業用の計画文書であり、導入完了後は確定した手順を [test/README.md](../test/README.md) などの正規の場所へ移す。

## 1. 背景と目的

### 現状（2026-11-08 確認）

- ビルドツールはなく、`javac` で `src/` と `test/` を直接コンパイルする。
- `test/unit/` の検証は15ファイルで、いずれも `main` から `AssertionError` / `assert` で照合する形式である。自動実行ランナーはない。
- IntelliJ IDEA の設定（`.idea/`、`*.iml`）は `.gitignore` により Git 管理外である。ソースルートの設定はリポジトリに残っていない。
- CI は Qodana のみで、テストは実行していない。
- ローカル JDK は 25 である。`javac --release 24` で `src/` と `test/` の全ソース、`--release 17` で `src/lib/io/compat17/*.java` がコンパイルできることを確認した（警告のみ）。

### 目的

| 目的             | 手段                                           |
|------------------|------------------------------------------------|
| 正当性の検証     | JUnit Jupiter による境界値・愚直解照合         |
| 網羅率の把握     | JaCoCo レポート、IntelliJ のカバレッジ         |
| TLE の検出       | JUnit の `@Timeout` と `@Tag("slow")`          |
| 実装間の性能比較 | JMH                                            |
| 自動実行         | GitHub Actions で `./gradlew check`            |
| 環境の再現性     | Gradle Wrapper とビルドファイルを Git 管理する |

### 変えないもの

- `src/` のレイアウト、パッケージ、バンドラー互換性。親リポジトリのバンドラーは `src/` をソースとして読むだけであり、Gradle の影響を受けない。
- `src/` は外部ライブラリに依存しない。外部依存はテスト・ベンチマークの sourceSet に限る。
- `javac --release 24 -encoding UTF-8 -d out (rg --files src -g '*.java')` によるライブラリ単体のスモークコンパイルは引き続き通る状態を保つ。

## 2. 採用ツール

| ツール                                         | 用途                     | 備考                                                                |
|------------------------------------------------|--------------------------|---------------------------------------------------------------------|
| Gradle Wrapper（`gradlew`）                    | ビルド・テスト実行       | Gradle 本体のインストールは不要。JDK 25 で動かすため 9.1 以降を使う |
| Kotlin DSL（`build.gradle.kts`）               | ビルド定義               | IntelliJ の補完が効く                                               |
| Version Catalog（`gradle/libs.versions.toml`） | 依存バージョンの一元管理 | Dependabot の更新対象にする                                         |
| JUnit Jupiter（JUnit BOM）                     | 単体テスト               | 書き方は JUnit 5 系と同じ                                           |
| JaCoCo（Gradle 標準プラグイン）                | 網羅率レポート           | 実行 JVM（25）のクラスファイルに対応する版を指定する                |
| JMH（`me.champeau.jmh` プラグイン）            | マイクロベンチマーク     | CI では実行しない                                                   |
| `gradle/actions/setup-gradle`                  | CI                       | Action は SHA で固定する（Dependabot の既存方針）                   |

AssertJ と jqwik は当面導入しない。JUnit の `Assertions` とシード固定の乱数照合で不足した時点で検討する。

各バージョンは導入時に最新安定版を確認し、Version Catalog に記録する。

## 3. 構成

### ファイル配置

```text
library/java/
├── settings.gradle.kts
├── build.gradle.kts
├── gradlew / gradlew.bat
├── .gitattributes             # gradlew は LF、gradlew.bat は CRLF
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/                       # main sourceSet
├── test/                      # test sourceSet（example / unit / judge / benchmark）
└── jmh/                       # jmh sourceSet（配置は「9. 要決定事項」を参照）
```

### sourceSet

| sourceSet | ディレクトリ | 内容                                                                                                                                                             |
|-----------|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `main`    | `src/`       | `lib.*` と `patterns.*`                                                                                                                                          |
| `test`    | `test/`      | 4用途すべてをコンパイルする。JUnit が検出するのは `@Test` などの注釈を持つクラスだけであり、`main` 形式の example / judge / benchmark はテストとして実行されない |
| `jmh`     | `jmh/`       | JMH ベンチマーク。`main` と `test` を参照できる                                                                                                                  |

### JDK

- Gradle とテストは `JAVA_HOME` の JDK（手元では 24.0.2）で実行し、コンパイルは `options.release = 24` で行う。toolchain は使わない。
- `src/lib/io/compat17/` は別タスク `compileCompat17` で `--release 17` のコンパイルだけを確認する。
- ジャッジと同じ JDK 24 で測定したくなった場合は、toolchain と foojay-resolver プラグインで JDK 24 を指定する（「9. 要決定事項」を参照）。

### build.gradle.kts の雛形

導入時に動作を確認して調整する。

```kotlin
plugins {
  java
  jacoco
  alias(libs.plugins.jmh)
}

repositories {
  mavenCentral()
}

sourceSets {
  main { java.setSrcDirs(listOf("src")) }
  test { java.setSrcDirs(listOf("test")) }
  named("jmh") { java.setSrcDirs(listOf("jmh")) }
}

dependencies {
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
  testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
  if (name != "compileCompat17") options.release = 24
}

tasks.test {
  useJUnitPlatform { excludeTags("slow") }
  jvmArgs("-ea")
  finalizedBy(tasks.jacocoTestReport)
}

val slowTest by tasks.registering(Test::class) {
  description = "最大制約の入力で実行時間を検査する"
  group = "verification"
  testClassesDirs = sourceSets.test.get().output.classesDirs
  classpath = sourceSets.test.get().runtimeClasspath
  useJUnitPlatform { includeTags("slow") }
  // 時間計測を歪めないよう -ea は付けない
}

val compileCompat17 by tasks.registering(JavaCompile::class) {
  description = "lib.io.compat17 の Java 17 互換性を検査する"
  group = "verification"
  source = fileTree("src/lib/io/compat17")
  classpath = files()
  destinationDirectory = layout.buildDirectory.dir("compat17")
  options.release = 17
}

tasks.check {
  dependsOn(compileCompat17)
}

jacoco {
  toolVersion = libs.versions.jacoco.get()
}

tasks.jacocoTestReport {
  reports {
    html.required = true
    xml.required = true
  }
}

jmh {
  jmhVersion = libs.versions.jmh.get()
}
```

### .gitignore

- `**/*.jar` が Wrapper の jar も除外するため、`!gradle/wrapper/gradle-wrapper.jar` を追加する。
- `**/build/` と `**/.gradle/` は除外済みである。
- `.idea/` と `*.iml` は Git 管理外のままとする。IntelliJ の構成は Gradle から生成する。ローカルの `library.iml` は Gradle 取り込み後に削除してよい。

### IntelliJ IDEA

- 親リポジトリの `.ide/idea` から、Gradle ツールウィンドウの Link Gradle Project で `library/java` をリンクした（2026-11-09）。コンテストのモジュールは、Gradle から生成された `library.main` に依存する。
- 生成されたモジュールとライブラリは、IDE が `.idea` の外（IDE のキャッシュ）に保存する。`gradle.xml` を手で書いてリンクしたときは JUnit のライブラリ定義が保存されず、IDE のビルドが失敗したため、画面からリンクし直した。
- Gradle JVM は Project SDK（jdk24.0.2）とし、ビルドとテストの実行は IntelliJ IDEA で行う。
- example / judge の `main` は従来通りガターから個別に実行できる。

## 4. テストの規約

### 配置と命名

- `test/unit/<分野>/...` に置き、package は `test/` からの相対ディレクトリに一致させる。既存の規則と同じである。
- クラス名は `<対象クラス名>Test` とする。例: `test/unit/ds/fenwick/IntBITTest.java`。
- クラス名 `Test` は使わない。`import org.junit.jupiter.api.*;` と併用すると、`@Test` が自クラスを指してコンパイルエラーになる。
- import は wildcard を使う。テストはバンドル対象外のため、`import static org.junit.jupiter.api.Assertions.*;` を使ってよい。`lib.*` の static import 禁止はバンドル対象の `src/` に対する規則である。

### テストの種類

| 種類         | JUnit の機能                                           | 方針                                                                                         |
|--------------|--------------------------------------------------------|----------------------------------------------------------------------------------------------|
| 境界値       | `@ParameterizedTest` と `@CsvSource` / `@MethodSource` | 空区間、先頭・末尾、0 を含む値など、Guide に記載した前提条件の範囲内の境界を表にする         |
| 愚直解照合   | `@RepeatedTest` とシード固定の `Random`                | 配列などの単純な実装と同じ操作列を適用して比較する。失敗メッセージにシードと操作番号を含める |
| 型バリアント | 抽象クラスの契約テストと継承                           | generic / `int` / `long` 版に共通する振る舞いを1回だけ書く                                   |
| 動的生成     | `@TestFactory`                                         | 生成したケースを個別のテストとして表示したい場合に使う                                       |

AGENTS.md の競プロ前提に従い、ライブラリが検査しない不正入力のテストは書かない。テストするのは Guide と JavaDoc に記載された振る舞いだけである。

テストクラスの中を API のグループごとの `@Nested` に分けること、契約テスト、愚直解との照合の共通の仕組み（`RandomOperations`）の詳細は [unit の README](../test/unit/README.md) に置く。`ds/fenwick` で試作して決めた（2026-11-09）。

### 既存の main 形式の unit

JUnit へ移行したファイルから削除する。移行完了までは `main` 形式と JUnit 形式が混在してよい。

## 5. 性能測定

| 種類                     | 手段                              | 目的                                                                   | 実行                            |
|--------------------------|-----------------------------------|------------------------------------------------------------------------|---------------------------------|
| TLE 検出テスト           | `@Tag("slow")` と `@Timeout`      | 最大制約の入力が制限時間内に終わることを確認し、計算量の退行を検出する | `./gradlew slowTest`            |
| マイクロベンチマーク     | JMH                               | 実装間の速度比較（自作実装と `java.util`、SWAR と愚直実装など）        | `./gradlew jmh` または `jmhJar` |
| プロセス単位ベンチマーク | 既存の `run_tests_24.sh` / `.bat` | 標準入出力を含む FastIO の測定                                         | 従来通り                        |

### TLE 検出テスト

- 入力は最大制約で生成し、シードを固定する。
- `@Timeout` の閾値は実測値に余裕を持たせる。JIT のウォームアップと環境差があるため、精密な比較には使わない。
- CI では実行しない。必要になった時点で、別ジョブとして追加するか判断する。

### JMH

- クラス名は `<対象クラス名>Benchmark` とする。
- 入力は `@Setup` でシード固定で生成し、計測対象から生成処理を外す。
- 結果は戻り値または `Blackhole` で消費し、デッドコード除去を防ぐ。
- 比較対象（`java.util.PriorityQueue` など）は同じクラスに並べ、同じ入力で計測する。
- 特定のベンチマークだけを実行する場合は `./gradlew jmhJar` で jar を作り、`java -jar build/libs/<名前>-jmh.jar <正規表現>` で実行する。
- 共有ランナーは値のばらつきが大きいため、CI では実行しない。
- 測定結果を Guide の性能特性に記載する場合は、JDK のバージョンと実行環境を併記する。

## 6. CI

- `.github/workflows/test.yml` を追加する。`push`（main）と `pull_request` で起動し、`actions/checkout`、`actions/setup-java`（JDK 25）、`gradle/actions/setup-gradle` を使って `./gradlew check` を実行する。`check` には `test`、`jacocoTestReport`、`compileCompat17` が含まれる。
- Action は既存方針に従って SHA で固定する。
- `.github/dependabot.yml` に `package-ecosystem: gradle` を追加する。
- Gradle 導入後、Qodana が Gradle プロジェクトとして解析できることを確認する。

## 7. 更新が必要な既存ファイル

| ファイル                                                              | 更新内容                                                                                                                 |
|-----------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| [AGENTS.md](../AGENTS.md)                                             | 「There is no Maven or Gradle build」の記述、コンパイル・実行コマンド、`unit.*` の `-ea` 実行の記述、`test/unit/` の説明 |
| [.github/copilot-instructions.md](../.github/copilot-instructions.md) | `javac` による検証手順と `unit.*` の実行方法                                                                             |
| [README.md](../README.md)                                             | 「外部ライブラリやビルドツールには依存していません」を、ライブラリ本体は依存なし・テストは Gradle と書き分ける           |
| [test/README.md](../test/README.md)                                   | IntelliJ の設定、コマンドライン手順、「JUnit を導入していない」の記述                                                    |
| [test/unit/README.md](../test/unit/README.md)                         | 「準備中」を、配置・命名・テストの種類の説明に置き換える                                                                 |
| [docs/STATUS.md](./STATUS.md)                                         | 「ビルドツールと自動テストランナーはなく」の記述                                                                         |
| `.gitignore`                                                          | Wrapper の jar の除外解除                                                                                                |
| `.github/dependabot.yml`                                              | Gradle の依存更新                                                                                                        |

コミットはビルド設定を `chore`、テストの追加・移行を `test`、ドキュメントを `docs` とし、[COMMIT_MESSAGE_TEMPLATE.md](../.github/COMMIT_MESSAGE_TEMPLATE.md) に従う。

## 8. 作業手順

### ステップ1: Gradle の基盤と最初のテスト

1. Gradle Wrapper を生成する。Gradle 本体がないため、IntelliJ の New Project（Gradle）で生成するか、Gradle 配布物を OS の一時ディレクトリに展開して `gradle wrapper --gradle-version <版> --gradle-distribution-sha256-sum <ハッシュ>` を実行し、展開物を削除する。
2. `settings.gradle.kts`、`build.gradle.kts`、`gradle/libs.versions.toml`、`.gitattributes` を作成し、`.gitignore` を更新する。
3. `test/unit/ds/fenwick/Test.java` を `IntBITTest.java` と `LongBITTest.java` に移行する。
4. AGENTS.md と `test/README.md` のビルド・実行手順を更新する。

完了条件: `./gradlew build` が成功し、`./gradlew test` で fenwick のテストが実行される。Gradle 9 はテストソースがあるのにテストが1件も検出されない場合に失敗するため、このステップで最低1件のテストを移行する。

### ステップ2: JaCoCo と既存 unit の移行

1. JaCoCo レポートを有効にする。
2. 残り14件の `main` 形式の unit を JUnit に移行し、元のファイルを削除する。

| 移行元                                             | 対象                   |
|----------------------------------------------------|------------------------|
| `test/unit/ds/arrays/Test.java`                    | `lib.ds.arrays`        |
| `test/unit/ds/map/intintmap/Test.java`             | `lib.ds.map`           |
| `test/unit/ds/map/longintmap/Test.java`            | `lib.ds.map`           |
| `test/unit/ds/map/longlongmap/Test.java`           | `lib.ds.map`           |
| `test/unit/ds/priorityqueue/Test.java`             | `lib.ds.priorityqueue` |
| `test/unit/ds/segmenttree/Test.java`               | `lib.ds.segmenttree`   |
| `test/unit/graph/connectivity/Test1.java`          | `lib.graph`            |
| `test/unit/graph/tree/lca/TestLCA.java`            | `lib.graph.tree`       |
| `test/unit/math/factorialtable/Test.java`          | `lib.math`             |
| `test/unit/math/factorutils/Test.java`             | `lib.math`             |
| `test/unit/math/primetable/FactorizationTest.java` | `lib.math`             |
| `test/unit/util/conversions/Test.java`             | `lib.util`             |
| `test/unit/util/format/Test.java`                  | `lib.util`             |
| `test/unit/util/mo/Test.java`                      | `lib.util`             |

`test/unit/util/conversions/Test.java` の出力デモ部分は、同じ値を照合するテストに置き換えて削除した。

完了条件: `test/unit/` に `main` 形式のファイルが残らず、`build/reports/jacoco/test/html/index.html` が生成される。

### ステップ3: テストの拡充

JaCoCo レポートで未網羅の公開 API を確認し、モジュール単位でテストを追加する。優先順は `lib.ds`（fenwick、segmenttree、priorityqueue、map、arrays、unionfind）、`lib.math`、`lib.search` / `lib.sort`、`lib.string`、`lib.graph` とする。TODO のみのクラスは対象外である（[STATUS.md](./STATUS.md) を参照）。

完了条件: モジュールごとに区切って進め、モジュール単位でコミットする。

### ステップ4: TLE 検出テスト

`slowTest` タスクを追加し、データ構造ごとに最大制約の操作列で `@Timeout` を検査するテストを書く。

完了条件: `./gradlew test` が `slow` タグのテストを実行せず、`./gradlew slowTest` だけが実行する。

### ステップ5: JMH

JMH プラグインを有効にし、比較したい実装からベンチマークを書く。最初の対象は `lib.ds.priorityqueue` の各実装と `java.util.PriorityQueue` の比較とする。

完了条件: `./gradlew jmh` または `jmhJar` で結果が得られる。

### ステップ6: CI とドキュメントの仕上げ

1. `.github/workflows/test.yml` と Dependabot の Gradle 設定を追加する。
2. 「7. 更新が必要な既存ファイル」の残りを更新する。
3. 本書の確定内容を正規の場所へ移し、本書を削除する。

完了条件: PR で `./gradlew check` が成功し、Qodana も従来通り動作する。

## 9. 要決定事項

| 項目                      | 選択肢                                                                                                          | 推奨                                                                                                                                                                        |
|---------------------------|-----------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| JMH ソースの配置          | (a) トップレベルの `jmh/`。(b) `test/benchmark/jmh/` に置き、test sourceSet から除外して jmh sourceSet に含める | (a)。(b) は `test/` を両 sourceSet が共有するため、IntelliJ でソースルートの重複が起きやすい。(a) を採る場合は AGENTS.md の `test/benchmark/` の説明に JMH の場所を追記する |
| 実行 JDK                  | (a) JDK 25 で実行し `--release 24` でコンパイル。(b) toolchain で JDK 24 を指定                                 | (a)。ジャッジと同じ JDK での性能測定が必要になった時点で (b) を検討する                                                                                                     |
| IntelliJ のテスト実行方式 | Gradle / IntelliJ IDEA                                                                                          | 好みで選ぶ。IntelliJ の方が起動は速い                                                                                                                                       |
| 既存 FastIO ベンチマーク  | Gradle タスク化する / 現状維持                                                                                  | 当面は現状維持                                                                                                                                                              |
