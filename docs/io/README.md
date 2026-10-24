# `lib.io`

`src/lib/io`にある高速入出力クラスの実装一覧である。実行環境に合わせてJava 24版またはJava 17互換版を参照すること。

| 対象        | README                       | 主なクラス                                         |
|-------------|------------------------------|----------------------------------------------------|
| Java 24     | [README](./java24/README.md) | `FastScanner`、`FastPrinter`、`InteractiveScanner` |
| Java 17互換 | [README](./java17/README.md) | `FastScanner`、`FastPrinter`、`InteractiveScanner` |

Java 24版とJava 17互換版は別パッケージに実装されている。詳細な利用条件と実装差は各READMEを参照すること。

## 内部パッケージ

| パッケージ        | ソース                           | ドキュメント                        |
|-------------------|----------------------------------|-------------------------------------|
| `lib.io.compat17` | [src](../../src/lib/io/compat17) | [Java 17互換版](./java17/README.md) |
