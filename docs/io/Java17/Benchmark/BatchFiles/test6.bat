@echo off

cd "C:\Users\20051\Projects\GitHubRepositories\competitive-programming-java-library\Data Structures\FastIO\Benchmark"

REM ==========================================
REM コンパイル
REM ==========================================
echo Compiling Java files...
javac TestOutputDoubleNumbers.java
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
REM TestOutputDoubleNumbers のテスト（output6_1.txt, output6_2.txt に追記）
REM ==========================================
echo Initializing output6_1.txt...
echo. > Output\output6_1.txt
echo Initializing output6_2.txt...
echo. > Output\output6_2.txt
echo. > Output\temp6.txt

REM 100 回実行
for %%i in (0 1 2 3) do (
    echo Running TestOutputDoubleNumbers with argument %%i...
    for /L %%j in (1,1,100) do (
        java %JVM_OPTS% TestOutputDoubleNumbers %%i < TestCases\TestCase_DoubleNumbers_123456789.txt > Output\temp6.txt
    )
)

echo TestOutputDoubleNumbers completed.
