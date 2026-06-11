@echo off

cd "C:\Users\20051\Projects\GitHubRepositories\competitive-programming-java-library\Data Structures\FastIO\Benchmark"

REM ==========================================
REM コンパイル
REM ==========================================
echo Compiling Java files...
javac TestInputDoubleNumbers.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b
)

REM ==========================================
REM JVM_OPTIONS
REM ==========================================
set JVM_OPTS=-Xms2g -Xmx2g -XX:+AlwaysPreTouch

REM ==========================================
REM TestInputDoubleNumbers のテスト（output5.txt に追記）
REM ==========================================
echo Initializing output5.txt...
echo. > Output\output5.txt

REM 100 回実行
for %%i in (0 1 2) do (
    echo Running TestInputDoubleNumbers with argument %%i...
    for /L %%j in (1,1,100) do (
        java %JVM_OPTS% TestInputDoubleNumbers %%i < TestCases\TestCase_DoubleNumbers_123456789.txt >> Output\output5.txt
    )
)

echo TestInputDoubleNumbers completed.
