![Java 24](https://img.shields.io/badge/Java-24-ED8B00?style=flat-square)
![Java 17 compatible](https://img.shields.io/badge/Java-17%20compatible-5382A1?style=flat-square)
![Dependencies: none](https://img.shields.io/badge/dependencies-none-4C9A2A?style=flat-square)
[![License: 0BSD](https://img.shields.io/badge/license-0BSD-blue?style=flat-square)](./LICENSE)

# Competitive Programming Java Library

競技プログラミング向けのアルゴリズム・データ構造をJavaで実装したリポジトリです。各モジュールのREADMEから、クラスの用途、ソース、Guideへ移動できます。

- [Author: nsubaru (AtCoder)](https://atcoder.jp/users/nsubaru)

## モジュール一覧

| パッケージ   | 主な内容                     | ドキュメント                              |
|--------------|------------------------------|-------------------------------------------|
| `lib.ds`     | データ構造                   | [モジュール一覧](./docs/ds/README.md)     |
| `lib.graph`  | グラフ・木アルゴリズム       | [モジュール一覧](./docs/graph/README.md)  |
| `lib.io`     | 高速入出力                   | [モジュール一覧](./docs/io/README.md)     |
| `lib.math`   | 数論・代数・幾何・畳み込み   | [モジュール一覧](./docs/math/README.md)   |
| `lib.search` | 二分探索・探索アルゴリズム   | [モジュール一覧](./docs/search/README.md) |
| `lib.sort`   | ソートアルゴリズム           | [モジュール一覧](./docs/sort/README.md)   |
| `lib.string` | 文字列アルゴリズム           | [モジュール一覧](./docs/string/README.md) |
| `lib.util`   | 変換・順列・その他の汎用処理 | [モジュール一覧](./docs/util/README.md)   |

配列とFenwick Treeの更新API・添字規約は、[Primitive Array Utilities](./docs/ds/arrays/README.md) と [Binary Indexed Tree](./docs/ds/fenwick/README.md) を参照する。

## 実装環境

利用例・検証コードは `test/example`、`test/unit`、`test/judge`、`test/benchmark` に分ける。配置と実行方法は [テスト・利用例の構成](./test/README.md) を参照する。

基本ターゲットはJava 24です。`lib.io.compat17`はJava 17互換です。外部ライブラリやビルドツールには依存していません。

## ライセンス

本リポジトリの内容は、個別に別条件が明記された第三者の素材を除き、[0BSD（Zero-Clause BSD）](./LICENSE)で提供します。利用・改変・再配布に、作者表示やライセンス文の同梱を求めません。

## 免責事項

本ライブラリは現状のまま提供され、バグがないこと、正確性、完全性、特定用途への適合性などを保証しません。各クラスの仕様、制約、前提条件は、対応するソースとGuideを確認してください。サポートや保守も保証しません。ライセンスの免責・責任制限条項は適用される法令の許す範囲で適用されます。詳細は[LICENSE](./LICENSE)を参照してください。
