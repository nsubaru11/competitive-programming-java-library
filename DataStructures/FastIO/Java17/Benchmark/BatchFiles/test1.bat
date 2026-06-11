@echo off

cd "C:\Users\20051\Projects\GitHubRepositories\competitive-programming-java-library\Data Structures\FastIO\Benchmark"

REM ==========================================
REM コンパイル
REM ==========================================
echo Compiling Java files...
javac TestInputLongNumbers.java
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
REM TestInputLongNumbers のテスト（output1_1.txt, output1_2.txt に追記）
REM ==========================================
echo Initializing output1_1.txt...
echo. > Output\output1_1.txt
echo Initializing output1_2.txt...
echo. > Output\output1_2.txt

REM 10 回実行
for %%i in (0 1 2 3 4 5 6 7) do (
    echo Running TestInputLongNumbers with argument %%i...
    for /L %%j in (1,1,10) do (
        java %JVM_OPTS% TestInputLongNumbers %%i < TestCases\TestCase_LongNumbers_123456789.txt >> Output\output1_1.txt
    )
)

REM 10 回実行
for %%i in (0 1 2 3 4 5 6 7) do (
    echo Running TestInputLongNumbers with argument %%i...
    for /L %%j in (1,1,10) do (
        java %JVM_OPTS% TestInputLongNumbers %%i < TestCases\TestCase_PositiveLongNumbers_123456789.txt >> Output\output1_2.txt
    )
)

echo TestInputLongNumbers completed.
