# プロジェクトガイドライン（Junie用）

このリポジトリは、AtCoderなどで利用するJava 24向け競技プログラミングライブラリです。ローカルでは通常のパッケージ付きライブラリとして編集し、提出時にAtCoder側のバンドラで単一の`Main.java`へ展開します。Maven/Gradleには依存しません。

## 1. ディレクトリ構成

- `src/lib/`: 安定したimport対象API
	- `lib.ds`, `lib.graph`, `lib.io`, `lib.math`, `lib.search`, `lib.sort`, `lib.string`, `lib.util`
	- `lib.io.compat17`のみJava 17互換を維持
- `src/patterns/`: DPやカレンダーなど、読んで写経・改変する実装パターン
- `test/verify/`: Example、Check、オンラインジャッジ用コード、ベンチマークドライバ
- `docs/`: モジュールREADME、詳細ガイド、ベンチマークランナー
- `out/`: ローカルコンパイル出力（gitignored）

IntelliJでは`src`をSources Root、`test`をTest Sources Rootとして扱います。公開APIは必ず、ファイルパス・`package`宣言・public型名・ファイル名を一致させてください。

## 2. ビルドと検証

PowerShellでの全体スモークコンパイル例：

```powershell
$src = @(rg --files src -g '*.java')
javac --release 24 -encoding UTF-8 -d out $src

$verify = @(rg --files test -g '*.java')
javac --release 24 -encoding UTF-8 -cp out -d out/test $verify
```

`src/lib/io/compat17`は別途`javac --release 17`で確認します。実行可能な検証コードは、例として`java -cp "out;out/test" verify.graph.mst.Example`のように完全修飾名で起動します。

## 3. 設計方針

- 競技用途の速度を優先し、境界チェックと割り当てを必要十分に留める。
- 外部依存は持たず、標準ライブラリとリポジトリ内の`lib.*`だけを利用する。
- 共通プリミティブを高階アルゴリズム内へ複製しない。通常のimportで再利用する。
- 依存グラフは小さく保ち、循環依存を避ける。
- インデントはタブ、文字コードはUTF-8、定数は`UPPER_SNAKE_CASE`。
- 非圧縮の公開クラス・メソッドにはJavaDocを書く。
- 高速化コードには、非自明な前提や実行環境の制約をコメントで残す。

## 4. バンドラ互換性

解答側は`import lib.ds.UnionFind;`のように記述します。`run`、`localtest`、`test`、`tomain`、`submit`はいずれもバンドル後のソースを使う前提です。

次の形式は避けてください。

- `import static lib...`
- メソッド本文中の`lib.ds.UnionFind`のような完全修飾参照
- バンドル後に単純名が衝突するトップレベル型

バンドラ障害時は`src/lib/`から手動展開できる導線を維持します。ライブラリが見つからない場合は、AtCoderリポジトリ直下の`library/` submoduleか、`ATCODER_LIB_SRC=<このrepo>/src`を確認します。

## 5. 変更手順

1. 対象APIと依存元を特定する。
2. 既存API互換を可能な限り維持して最小変更する。
3. 本体と`test/verify`をコンパイルし、関連Example/Checkを実行する。
4. `lib.*`依存を追加した場合は、推移的バンドル結果もコンパイルする。
5. 関連する`docs/`、ルートREADME、テンプレートを更新する。

Java 24 FastIOのベンチマークは`docs/io/Java24/Benchmark/run_tests_24.sh`または`.bat`を利用し、生成物は同ディレクトリの`work/`以下に置きます。
