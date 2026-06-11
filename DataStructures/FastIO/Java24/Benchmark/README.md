# FastIO24 Benchmark

`FastScanner` / `FastPrinter`（src 本体）と、比較用実装 `FastScanner2` / `FastPrinter2`
（ストリーミング型・本フォルダ内）の性能を比較計測するためのベンチマーク一式です。

## 構成

| ファイル                                      | 役割                                              |
|-------------------------------------------|-------------------------------------------------|
| `FastScanner2.java` / `FastPrinter2.java` | 比較用実装（ライブラリ本体ではない）                              |
| `Test1.java`                              | 本体実装（FastScanner / FastPrinter）の計測ドライバ          |
| `Test2.java`                              | 比較実装（FastScanner2 / FastPrinter2）の計測ドライバ        |
| `GenerateTestInput.java`                  | ベンチマーク入力（int/long/文字列 各1000万件）の生成。第1引数で出力先を指定可能 |
| `run_tests_24.sh`                         | Linux/WSL 用ランナー（コンパイル→入力生成→ドライラン→計測→集計）         |
| `run_tests_24.bat`                        | Windows ネイティブ用ランナー（同上）                          |
| `work/`                                   | 生成物の出力先（クラス・入力・CSV・各種ログ）。gitignore 対象           |

## 使い方

```bash
# Linux / WSL（リポジトリのどこから実行してもよい）
"./DataStructures/FastIO/Java24/Benchmark/run_tests_24.sh" [RUN_COUNT] [TOKEN_COUNT] [STRING_PRINT_COUNT]
```

```bat
:: Windows
"DataStructures\FastIO\Java24\Benchmark\run_tests_24.bat" [RUN_COUNT] [TOKEN_COUNT] [STRING_PRINT_COUNT]
```

- 既定値: RUN_COUNT=10, TOKEN_COUNT=10000000, STRING_PRINT_COUNT=256
- 環境変数 `ENABLE_JFR=1` で JFR 記録を有効化（`JFR_SETTINGS` で設定変更可）
- AtCoder 想定の JVM オプション（`-XX:TieredStopAtLevel=1 -Xms512M -Xss8M -XX:+UseSerialGC`）で計測
- `ac_library.jar` / `bifurcan.jar` がリポジトリ直下にあればクラスパスに含める

## 出力

- `work/benchmark_results.csv` — 全計測の生データ
- 実行末尾に test ごとの mean/stddev/fastest/slowest（ms）を表示（要 python3）
- `work/jit_profile.log`, `work/logs/gc/`, `work/logs/jfr/`, `work/logs/startup/` — 各種プロファイル

## 別実装を比較したいとき

1. 比較したい実装（例: `FastScanner3.java`）をこのフォルダに置く
2. `Test2.java` をコピーして参照クラスを差し替える
3. ランナーのコンパイル対象と Test 実行部にファイルを追加する
