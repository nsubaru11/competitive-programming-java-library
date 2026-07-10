@echo off

cd "C:\Users\20051\Projects\GitHubRepositories\competitive-programming-java-library\Data Structures\FastIO\Benchmark"

REM ==========================================
REM コンパイル
REM ==========================================
echo Compiling Java files...
javac TestOutputStrings.java
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
REM TestOutputStrings のテスト（output4.txtに追記）
REM ==========================================
echo Initializing output4.txt...
echo. > Output\output4.txt
echo. > Output\temp4.txt

REM 100 回実行
for %%i in (0 1) do (
    echo Running TestOutputStrings with argument %%i...
    for /L %%j in (1,1,100) do (
        java %JVM_OPTS% TestOutputStrings %%i < TestCases\TestCase_Strings_123456789.txt > Output\temp4.txt
    )
)

echo TestOutputStrings completed.
