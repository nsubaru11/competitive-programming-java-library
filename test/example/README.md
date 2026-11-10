# 利用例

`src/lib` の各クラスの主要メソッドを、固定データで実行して出力する。標準入力は使わない。

- 配置は `test/example/<lib のパッケージ>/{ClassName}Example.java` とし、1クラスにつき1ファイルとする。例えば `lib.ds.arrays.IntArray2D` の利用例は `example.ds.arrays.IntArray2DExample` である。
- 各ファイルは50行以下とし、操作のまとまりごとに短いコメントと空行で区切る。
- 開発中・未実装のクラス、interface・enum、結果型には利用例を置かない。
- 各クラスの利用例へのリンクは、`docs` の各 README の「利用例」列と、各 Guide の「利用例」にある。
- 実行方法は [テスト・利用例の構成](../README.md#コマンドライン) を参照する。
