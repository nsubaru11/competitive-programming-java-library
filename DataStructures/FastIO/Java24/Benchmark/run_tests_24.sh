#!/bin/bash

# ==========================================
# FastIO24 Scanner Benchmark & Profiler (Linux/WSL)
# Target Environment: AtCoder (Java 24 / 1024 MiB / CP Libraries)
# ==========================================

RUN_COUNT=${1:-10}
TOKEN_COUNT=${2:-10000000}
STRING_PRINT_COUNT=${3:-256}
DRY_RUN_TOKEN_COUNT=${DRY_RUN_TOKEN_COUNT:-10000}
DRY_RUN_STRING_PRINT_COUNT=${DRY_RUN_STRING_PRINT_COUNT:-100}

# プロファイリング機能はデフォルト無効（純粋な実行時間計測のため）
ENABLE_JFR=${ENABLE_JFR:-0}
JFR_SETTINGS=${JFR_SETTINGS:-profile}

# ディレクトリおよびファイル設定
# このスクリプトは Data Structures/FastIO24/Benchmark/ に配置されている前提
BENCH_PATH="$(cd "$(dirname "$0")" && pwd)"
IO_PATH="$(cd "${BENCH_PATH}/.." && pwd)"
PROJECT_ROOT="$(cd "${IO_PATH}/../.." && pwd)/"
SRC_PATH="${IO_PATH}/src"
# 生成物（クラス・入力・CSV・ログ）はすべて work/ 配下に出力（gitignore対象）
WORK_PATH="${BENCH_PATH}/work"
OUT_PATH="${WORK_PATH}/out"
INPUT_FILE="${WORK_PATH}/large_input.txt"
RESULTS_CSV="${WORK_PATH}/benchmark_results.csv"
TMP1="${WORK_PATH}/_bench_test1.tmp"
TMP2="${WORK_PATH}/_bench_test2.tmp"

# ログディレクトリ設定
JIT_LOG="${WORK_PATH}/jit_profile.log"
LOG_ROOT="${WORK_PATH}/logs"
GC_LOG_DIR="${LOG_ROOT}/gc"
JFR_LOG_DIR="${LOG_ROOT}/jfr"
STARTUP_LOG_DIR="${LOG_ROOT}/startup"
DRY_RUN_GC_LOG="${GC_LOG_DIR}/dry_run_test1_gc.log"
DRY_RUN_STARTUP_LOG="${STARTUP_LOG_DIR}/dry_run_test1_startup.log"

JAVA_EXE="java"
JAVAC_EXE="javac"

# Javaコマンドの確認
if ! command -v $JAVA_EXE &> /dev/null; then
    echo "[ERROR] java command not found."
    exit 1
fi

# ==========================================
# AtCoder環境シミュレーション設定
# ==========================================

# 1. クラスパスの構築 (ac_library.jar, bifurcan.jar)
CP_LIBS="${PROJECT_ROOT}ac_library.jar:${PROJECT_ROOT}bifurcan.jar"
BENCH_CLASSPATH="$OUT_PATH:${CP_LIBS}"

# 2. 競技プログラミング向け最適化オプション
CP_FAST_START_OPTS=(
    "-XX:TieredStopAtLevel=1"
    "-Xms512M"
    "-Xss8M"
    "-XX:+UseSerialGC"
)

# ロギングオプション生成関数
java_jit_log_opts() {
    local log_file="$1"
    printf '%s' "-Xlog:compilation*=debug,inlining*=debug:file=${log_file}:time,uptime,level,tags:filecount=0"
}

java_gc_log_opts() {
    local log_file="$1"
    printf '%s' "-Xlog:gc*=debug,safepoint*=info:file=${log_file}:time,uptime,level,tags:filecount=0"
}

java_jfr_opts() {
    local jfr_file="$1"
    printf '%s' "-XX:StartFlightRecording=filename=${jfr_file},settings=${JFR_SETTINGS},dumponexit=true"
}

echo "=========================================="
echo "FastIO24 Scanner Benchmark (AtCoder Mode)"
echo "run_count=$RUN_COUNT token_count=$TOKEN_COUNT string_print_count=$STRING_PRINT_COUNT"
echo "dry_run_token_count=$DRY_RUN_TOKEN_COUNT"
echo "jfr_enabled=$ENABLE_JFR"
echo "classpath=$BENCH_CLASSPATH"
echo "=========================================="
echo ""

mkdir -p "$OUT_PATH" "$GC_LOG_DIR" "$JFR_LOG_DIR" "$STARTUP_LOG_DIR"

echo "[1/4] Compiling benchmark classes..."
# コンパイル時にも外部ライブラリをクラスパスに含める
$JAVAC_EXE -cp "$CP_LIBS" -d "$OUT_PATH" "$SRC_PATH/FastScanner.java" "$SRC_PATH/FastPrinter.java" "$BENCH_PATH/FastScanner2.java" "$BENCH_PATH/FastPrinter2.java" "$BENCH_PATH/Test1.java" "$BENCH_PATH/Test2.java" "$BENCH_PATH/GenerateTestInput.java"
if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed."
    exit 1
fi

if [ ! -f "$INPUT_FILE" ]; then
    echo "[2/4] Input file not found. Generating large_input.txt..."
    $JAVA_EXE -cp "$OUT_PATH" GenerateTestInput "$INPUT_FILE"
else
    echo "[2/4] Using existing input file: $INPUT_FILE"
fi
echo ""

echo "[3/4] Profiling JIT and Startup Time (Dry Run)..."
$JAVA_EXE "${CP_FAST_START_OPTS[@]}" "$(java_jit_log_opts "$JIT_LOG")" "$(java_gc_log_opts "$DRY_RUN_GC_LOG")" -cp "$BENCH_CLASSPATH" Test1 "$DRY_RUN_TOKEN_COUNT" "$DRY_RUN_TOKEN_COUNT" "$DRY_RUN_STRING_PRINT_COUNT" < "$INPUT_FILE" > /dev/null 2> "$DRY_RUN_STARTUP_LOG"
if [ $? -ne 0 ]; then
    echo "[ERROR] Dry run profiling failed."
    exit 1
fi
echo "  -> Dry-run JIT log saved to: $JIT_LOG"
echo "  -> Dry-run GC log saved to: $DRY_RUN_GC_LOG"
echo -n "  -> "
if ! grep "JVM Startup to Main" "$DRY_RUN_STARTUP_LOG"; then
    echo "JVM Startup to Main log not found."
fi
echo ""

echo "[4/4] Measuring..."
echo "run,test,n,stringLen,stringPrintCount,nextIntNs,nextLongNs,nextNs,scannerInitNs,nextTotalNs,scannerWithNextTotalNs,sumInt,sumLong,strHash,printlnIntNs,printlnLongNs,printlnStringNs,printlnBoolNs,mainTotalNs,appTotalNs" > "$RESULTS_CSV"

for i in $(seq 1 $RUN_COUNT); do
    echo "---------------- Run $i / $RUN_COUNT ----------------"
    RUN_ID=$(printf "%02d" "$i")

    # Test1
    TEST1_GC_LOG="${GC_LOG_DIR}/test1_run_${RUN_ID}.log"
    TEST1_STARTUP_LOG="${STARTUP_LOG_DIR}/test1_run_${RUN_ID}.log"
    TEST1_JAVA_OPTS=("${CP_FAST_START_OPTS[@]}" "$(java_gc_log_opts "$TEST1_GC_LOG")")
    if [ "$ENABLE_JFR" = "1" ]; then
        TEST1_JFR_LOG="${JFR_LOG_DIR}/test1_run_${RUN_ID}.jfr"
        TEST1_JAVA_OPTS+=("$(java_jfr_opts "$TEST1_JFR_LOG")")
    fi

    $JAVA_EXE "${TEST1_JAVA_OPTS[@]}" -cp "$BENCH_CLASSPATH" Test1 $TOKEN_COUNT $TOKEN_COUNT $STRING_PRINT_COUNT < "$INPUT_FILE" > "$TMP1" 2> "$TEST1_STARTUP_LOG"
    if [ $? -ne 0 ]; then
        echo "[ERROR] Runtime failed in Test1 at run $i."
        exit 1
    fi
    if ! grep "^RESULT," "$TMP1" | sed "s/^RESULT,/$i,/" >> "$RESULTS_CSV"; then
        echo "[ERROR] RESULT row not found in Test1 at run $i."
        exit 1
    fi

    # Test2
    TEST2_GC_LOG="${GC_LOG_DIR}/test2_run_${RUN_ID}.log"
    TEST2_STARTUP_LOG="${STARTUP_LOG_DIR}/test2_run_${RUN_ID}.log"
    TEST2_JAVA_OPTS=("${CP_FAST_START_OPTS[@]}" "$(java_gc_log_opts "$TEST2_GC_LOG")")
    if [ "$ENABLE_JFR" = "1" ]; then
        TEST2_JFR_LOG="${JFR_LOG_DIR}/test2_run_${RUN_ID}.jfr"
        TEST2_JAVA_OPTS+=("$(java_jfr_opts "$TEST2_JFR_LOG")")
    fi

    $JAVA_EXE "${TEST2_JAVA_OPTS[@]}" -cp "$BENCH_CLASSPATH" Test2 $TOKEN_COUNT $TOKEN_COUNT $STRING_PRINT_COUNT < "$INPUT_FILE" > "$TMP2" 2> "$TEST2_STARTUP_LOG"
    if [ $? -ne 0 ]; then
        echo "[ERROR] Runtime failed in Test2 at run $i. See: $TEST2_STARTUP_LOG"
        exit 1
    fi
    if ! grep "^RESULT," "$TMP2" | sed "s/^RESULT,/$i,/" >> "$RESULTS_CSV"; then
        echo "[ERROR] RESULT row not found in Test2 at run $i."
        exit 1
    fi
done

echo ""
echo "=========================================="
echo "Result Summary (mean/stddev + fastest/slowest in ms)"
echo "=========================================="

python3 -c "
import csv, sys, math
from collections import defaultdict

rows = []
try:
    with open('$RESULTS_CSV', 'r') as f:
        reader = csv.DictReader(f)
        for row in reader: rows.append(row)
except Exception as e:
    print('Failed to read CSV:', e)
    sys.exit(1)

if not rows:
    print('No benchmark rows found.')
    sys.exit(1)

metrics = ['nextIntNs','nextLongNs','nextNs','scannerInitNs','nextTotalNs','scannerWithNextTotalNs','printlnIntNs','printlnLongNs','printlnStringNs','printlnBoolNs','mainTotalNs','appTotalNs']
groups = defaultdict(list)
for r in rows: groups[r['test']].append(r)

for test_name, group in groups.items():
    print(f'Test: {test_name}')
    for m in metrics:
        vals = []
        for r in group:
            try: vals.append(float(r[m]))
            except: pass
        if not vals: continue

        avg = sum(vals)/len(vals)
        min_val = min(vals)
        max_val = max(vals)
        std = math.sqrt(sum((v - avg)**2 for v in vals)/len(vals))

        m_ms = m.replace('Ns','Ms')
        print(f'{m_ms:<15} avg={avg/1e6:10.3f} std={std/1e6:10.3f} fastest={min_val/1e6:10.3f} slowest={max_val/1e6:10.3f}')
    print('')
"

echo "Raw CSV: $RESULTS_CSV"
echo "Dry-run JIT log: $JIT_LOG"
echo "Dry-run GC log: $DRY_RUN_GC_LOG"
echo "Per-run GC logs: $GC_LOG_DIR"
if [ "$ENABLE_JFR" = "1" ]; then
    echo "Per-run JFR recordings: $JFR_LOG_DIR"
fi

rm -f "$TMP1" "$TMP2"
