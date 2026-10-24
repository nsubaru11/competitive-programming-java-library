@echo off

cd "C:\Users\20051\Projects\GitHubRepositories\competitive-programming-java-library\Data Structures\FastIO\Benchmark"

REM ==========================================
REM コンパイル
REM ==========================================
echo Compiling Java files...
javac TestInputLongArray.java
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
REM TestInputLongArray のテスト（output2_1.txt, output2_2.txt に追記）
REM ==========================================
echo Initializing output2_1.txt...
echo. > Output\output2_1.txt
echo Initializing output2_2.txt...
echo. > Output\output2_2.txt

REM 100 回実行
for %%i in (0 1) do (
    echo Running TestInputLongArray with argument %%i...
    for /L %%j in (1,1,100) do (
        java %JVM_OPTS% TestInputLongArray %%i < TestCases\TestCase_LongNumbers_123456789.txt >> Output\output2_1.txt
    )
)

REM 100 回実行
for %%i in (0 1) do (
    echo Running TestInputLongArray with argument %%i...
    for /L %%j in (1,1,100) do (
        java %JVM_OPTS% TestInputLongArray %%i < TestCases\TestCase_PositiveLongNumbers_123456789.txt >> Output\output2_2.txt
    )
)

echo TestInputLongArray completed.
