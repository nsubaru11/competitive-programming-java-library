# ローカル検証

`lib` の公開 API を JUnit Jupiter で検査する。実行方法は [テスト・利用例の構成](../README.md#コマンドライン) を参照する。雛形は親リポジトリの `templates/java/Test.java` である。

## 配置と命名

- `lib` の package に対応するフォルダへ置く。例えば `lib.ds.map` のテストは `test/unit/ds/map/` に置き、package は `unit.ds.map` とする。
- クラス名は `<対象>Test` とする。対象はクラス名、または int・long などの型別クラスをまとめた Guide の単位とする。例: `IntBITTest`、`PriorityQueueTest`。
- クラス名を `Test` にしない。`@Test` が自分自身のクラスを指してコンパイルエラーになる。
- テストはバンドル対象外のため、`import static org.junit.jupiter.api.Assertions.*;` を使ってよい。

## テストの種類

| 種類           | JUnit の機能                                  | 方針                                                                                             |
|----------------|-----------------------------------------------|--------------------------------------------------------------------------------------------------|
| 境界値・具体例 | `@Test`、`@ParameterizedTest` と `@CsvSource` | 空区間、先頭・末尾、0 を含む値など、Guide に記載した前提条件の範囲内の境界を検査する             |
| 愚直解との照合 | `@RepeatedTest` とシード固定の `Random`       | 配列や `java.util` などの単純な実装と同じ操作列を適用して比べる。失敗メッセージにシードを含める  |
| TLE 検出       | `@Tag("slow")` と `@Timeout`                  | 最大制約の入力で実行時間を検査する。`./gradlew slowTest` だけが実行する                          |

ライブラリが検査しない不正入力のテストは書かない。テストするのは Guide と Javadoc に記載された振る舞いだけである。
