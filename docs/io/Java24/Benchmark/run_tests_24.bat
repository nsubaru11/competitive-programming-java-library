@echo off
setlocal enabledelayedexpansion

:: ==========================================
:: FastIO24 Scanner Benchmark (Windows native)
:: ==========================================

set RUN_COUNT=%~1
if "%RUN_COUNT%"=="" set RUN_COUNT=10
set TOKEN_COUNT=%~2
if "%TOKEN_COUNT%"=="" set TOKEN_COUNT=10000000
set STRING_PRINT_COUNT=%~3
if "%STRING_PRINT_COUNT%"=="" set STRING_PRINT_COUNT=256

if "%DRY_RUN_TOKEN_COUNT%"=="" set DRY_RUN_TOKEN_COUNT=10000
if "%DRY_RUN_STRING_PRINT_COUNT%"=="" set DRY_RUN_STRING_PRINT_COUNT=100
if "%ENABLE_JFR%"=="" set ENABLE_JFR=0
if "%JFR_SETTINGS%"=="" set JFR_SETTINGS=profile

:: Directories and files
:: This script lives in "docs\io\Java24\Benchmark\"
set "BENCH_PATH=%~dp0"
set "PROJECT_ROOT=%BENCH_PATH%..\..\..\..\"
set "SRC_PATH=%PROJECT_ROOT%src\lib\io"
set "TEST_PATH=%PROJECT_ROOT%test\verify\io\bench24"
:: All generated artifacts (classes, input, CSV, logs) go under work\ (gitignored)
set "WORK_PATH=%BENCH_PATH%work"
set "OUT_PATH=%WORK_PATH%\out"
set "INPUT_FILE=%WORK_PATH%\large_input.txt"
set "RESULTS_CSV=%WORK_PATH%\benchmark_results.csv"
set "TMP1=%WORK_PATH%\_bench_test1.tmp"
set "TMP2=%WORK_PATH%\_bench_test2.tmp"

:: Log directories
set "JIT_LOG=%WORK_PATH%\jit_profile.log"
set "LOG_ROOT=%WORK_PATH%\logs"
set "GC_LOG_DIR=%LOG_ROOT%\gc"
set "JFR_LOG_DIR=%LOG_ROOT%\jfr"
set "STARTUP_LOG_DIR=%LOG_ROOT%\startup"
set "DRY_RUN_GC_LOG=%GC_LOG_DIR%\dry_run_test1_gc.log"
set "DRY_RUN_STARTUP_LOG=%STARTUP_LOG_DIR%\dry_run_test1_startup.log"

:: Commands
set "JAVA_EXE=java"
set "JAVAC_EXE=javac"

:: Classpaths
set "CP_LIBS=%PROJECT_ROOT%ac_library.jar;%PROJECT_ROOT%bifurcan.jar"
set "BENCH_CLASSPATH=%OUT_PATH%;%CP_LIBS%"

:: JVM Options
set "CP_FAST_START_OPTS=-XX:TieredStopAtLevel=1 -Xms512M -Xss8M -XX:+UseSerialGC"

echo ==========================================
echo FastIO24 Scanner Benchmark (Windows Native)
echo run_count=%RUN_COUNT% token_count=%TOKEN_COUNT% string_print_count=%STRING_PRINT_COUNT%
echo dry_run_token_count=%DRY_RUN_TOKEN_COUNT%
echo jfr_enabled=%ENABLE_JFR%
echo classpath=%BENCH_CLASSPATH%
echo ==========================================
echo.

if not exist "%OUT_PATH%" mkdir "%OUT_PATH%"
if not exist "%GC_LOG_DIR%" mkdir "%GC_LOG_DIR%"
if not exist "%JFR_LOG_DIR%" mkdir "%JFR_LOG_DIR%"
if not exist "%STARTUP_LOG_DIR%" mkdir "%STARTUP_LOG_DIR%"

echo [1/4] Compiling benchmark classes...
%JAVAC_EXE% --release 24 -encoding UTF-8 -cp "%CP_LIBS%" -d "%OUT_PATH%" "%SRC_PATH%\FastScanner.java" "%SRC_PATH%\FastPrinter.java" "%TEST_PATH%\FastScanner2.java" "%TEST_PATH%\FastPrinter2.java" "%TEST_PATH%\Test1.java" "%TEST_PATH%\Test2.java" "%TEST_PATH%\GenerateTestInput.java"
if errorlevel 1 (
    echo [ERROR] Compilation failed.
    exit /b 1
)

if not exist "%INPUT_FILE%" (
    echo [2/4] Input file not found. Generating large_input.txt...
    %JAVA_EXE% -cp "%OUT_PATH%" verify.io.bench24.GenerateTestInput "%INPUT_FILE%"
) else (
    echo [2/4] Using existing input file: %INPUT_FILE%
)
echo.

echo [3/4] Profiling JIT and Startup Time (Dry Run)...
%JAVA_EXE% %CP_FAST_START_OPTS% -Xlog:compilation*=debug,inlining*=debug:file="%JIT_LOG%":time,uptime,level,tags:filecount=0 -Xlog:gc*=debug,safepoint*=info:file="%DRY_RUN_GC_LOG%":time,uptime,level,tags:filecount=0 -cp "%BENCH_CLASSPATH%" verify.io.bench24.Test1 %DRY_RUN_TOKEN_COUNT% %DRY_RUN_TOKEN_COUNT% %DRY_RUN_STRING_PRINT_COUNT% < "%INPUT_FILE%" > nul 2> "%DRY_RUN_STARTUP_LOG%"
if errorlevel 1 (
    echo [ERROR] Dry run profiling failed.
    exit /b 1
)
echo   -^> Dry-run JIT log saved to: %JIT_LOG%
echo   -^> Dry-run GC log saved to: %DRY_RUN_GC_LOG%
findstr "JVM Startup to Main" "%DRY_RUN_STARTUP_LOG%" > nul
if errorlevel 1 echo   -^> JVM Startup to Main log not found.
echo.

echo [4/4] Measuring...
echo run,test,n,stringLen,stringPrintCount,nextIntNs,nextLongNs,nextNs,scannerInitNs,nextTotalNs,scannerWithNextTotalNs,sumInt,sumLong,strHash,printlnIntNs,printlnLongNs,printlnStringNs,printlnBoolNs,mainTotalNs,appTotalNs > "%RESULTS_CSV%"

for /l %%i in (1, 1, %RUN_COUNT%) do (
    echo ---------------- Run %%i / %RUN_COUNT% ----------------
    set "RUN_ID=0%%i"
    set "RUN_ID=!RUN_ID:~-2!"

    :: Test1
    set "TEST1_GC_LOG=%GC_LOG_DIR%\test1_run_!RUN_ID!.log"
    set "TEST1_STARTUP_LOG=%STARTUP_LOG_DIR%\test1_run_!RUN_ID!.log"
    set "T1_OPTS=%CP_FAST_START_OPTS% -Xlog:gc*=debug,safepoint*=info:file="!TEST1_GC_LOG!":time,uptime,level,tags:filecount=0"

    %JAVA_EXE% !T1_OPTS! -cp "%BENCH_CLASSPATH%" verify.io.bench24.Test1 %TOKEN_COUNT% %TOKEN_COUNT% %STRING_PRINT_COUNT% < "%INPUT_FILE%" > "%TMP1%" 2> "!TEST1_STARTUP_LOG!"

    for /f "tokens=* delims=" %%A in ('findstr "^RESULT," "%TMP1%"') do (
        set "line=%%A"
        set "line=!line:RESULT,=%%i,!"
        echo !line!>> "%RESULTS_CSV%"
    )

    :: Test2
    set "TEST2_GC_LOG=%GC_LOG_DIR%\test2_run_!RUN_ID!.log"
    set "TEST2_STARTUP_LOG=%STARTUP_LOG_DIR%\test2_run_!RUN_ID!.log"
    set "T2_OPTS=%CP_FAST_START_OPTS% -Xlog:gc*=debug,safepoint*=info:file="!TEST2_GC_LOG!":time,uptime,level,tags:filecount=0"

    %JAVA_EXE% !T2_OPTS! -cp "%BENCH_CLASSPATH%" verify.io.bench24.Test2 %TOKEN_COUNT% %TOKEN_COUNT% %STRING_PRINT_COUNT% < "%INPUT_FILE%" > "%TMP2%" 2> "!TEST2_STARTUP_LOG!"

    for /f "tokens=* delims=" %%A in ('findstr "^RESULT," "%TMP2%"') do (
        set "line=%%A"
        set "line=!line:RESULT,=%%i,!"
        echo !line!>> "%RESULTS_CSV%"
    )
)

echo.
echo ==========================================
echo Result Summary
echo ==========================================

:: Python script generation
set "PY_SCRIPT=%PROJECT_ROOT%calc_summary.py"
(
echo import csv, sys, math
echo from collections import defaultdict
echo rows = []
echo try:
echo     with open^(r'%RESULTS_CSV%', 'r'^) as f:
echo         reader = csv.DictReader^(f^)
echo         for row in reader: rows.append^(row^)
echo except Exception as e:
echo     print^('Failed to read CSV:', e^)
echo     sys.exit^(1^)
echo if not rows:
echo     print^('No benchmark rows found.'^)
echo     sys.exit^(1^)
echo metrics = ['nextIntNs','nextLongNs','nextNs','scannerInitNs','nextTotalNs','scannerWithNextTotalNs','printlnIntNs','printlnLongNs','printlnStringNs','printlnBoolNs','mainTotalNs','appTotalNs']
echo groups = defaultdict^(list^)
echo for r in rows: groups[r['test']].append^(r^)
echo for test_name, group in groups.items^(^):
echo     print^(f'Test: {test_name}'^)
echo     for m in metrics:
echo         vals = []
echo         for r in group:
echo             try: vals.append^(float^(r[m]^)^)
echo             except: pass
echo         if not vals: continue
echo         avg = sum^(vals^)/len^(vals^)
echo         min_val = min^(vals^)
echo         max_val = max^(vals^)
echo         std = math.sqrt^(sum^(^(v - avg^)**2 for v in vals^)/len^(vals^)^)
echo         m_ms = m.replace^('Ns','Ms'^)
echo         print^(f'{m_ms:^<15} avg={avg/1e6:10.3f} std={std/1e6:10.3f} fastest={min_val/1e6:10.3f} slowest={max_val/1e6:10.3f}'^)
echo     print^(''^)
) > "%PY_SCRIPT%"

python "%PY_SCRIPT%"
del "%PY_SCRIPT%"

echo Raw CSV: %RESULTS_CSV%
if exist "%TMP1%" del "%TMP1%"
if exist "%TMP2%" del "%TMP2%"

exit /b 0
