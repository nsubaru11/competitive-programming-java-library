@echo off

cd "C:\Users\20051\Projects\GitHubRepositories\competitive-programming-java-library\Data Structures\FastIO\Benchmark"

REM ==========================================
REM コンパイル
REM ==========================================
echo Compiling Java files...
javac TestOutputLongNumbers.java
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
REM TestOutputLongNumbers のテスト（output3.txtに追記）
REM ==========================================
echo Initializing output3.txt...
echo. > Output\output3.txt
echo Initializing temp3.txt...
echo. > Output\temp3.txt

REM 100 回実行
echo "正負混合データ\n" >> Output\output3.txt
for %%i in (0 1 2 3 4 5 6 7) do (
    echo Running TestOutputLongNumbers with argument %%i...
    for /L %%j in (1,1,100) do (
        java %JVM_OPTS% TestOutputLongNumbers %%i < TestCases\TestCase_LongNumbers_123456789.txt > Output\temp3.txt
    )
)

REM 100 回実行
echo "\n正の整数データ\n" >> Output\output3.txt
for %%i in (0 1 2 3 4 5 6 7) do (
    echo Running TestOutputLongNumbers with argument %%i...
    for /L %%j in (1,1,100) do (
        java %JVM_OPTS% TestOutputLongNumbers %%i < TestCases\TestCase_PositiveLongNumbers_123456789.txt > Output\temp3.txt
    )
)

echo TestOutputLongNumbers completed.
