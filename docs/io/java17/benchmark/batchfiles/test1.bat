@echo off
setlocal

for %%I in ("%~dp0..\..\..\..\..") do set "PROJECT_ROOT=%%~fI"
set "WORK_PATH=%~dp0..\work"
if not exist "%WORK_PATH%\out" mkdir "%WORK_PATH%\out"
if not exist "%WORK_PATH%\Output" mkdir "%WORK_PATH%\Output"
if not exist "%WORK_PATH%\TestCases" mkdir "%WORK_PATH%\TestCases"
cd /d "%WORK_PATH%" || exit /b 1

REM ==========================================
REM コンパイル
REM ==========================================
echo Compiling Java files...
javac --release 17 -encoding UTF-8 -d "%WORK_PATH%\out" -sourcepath "%PROJECT_ROOT%\src" "%PROJECT_ROOT%\test\benchmark\io\bench17\TestInputLongNumbers.java"
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
        java %JVM_OPTS% -cp "%WORK_PATH%\out" benchmark.io.bench17.TestInputLongNumbers %%i < TestCases\TestCase_LongNumbers_123456789.txt >> Output\output1_1.txt
    )
)

REM 10 回実行
for %%i in (0 1 2 3 4 5 6 7) do (
    echo Running TestInputLongNumbers with argument %%i...
    for /L %%j in (1,1,10) do (
        java %JVM_OPTS% -cp "%WORK_PATH%\out" benchmark.io.bench17.TestInputLongNumbers %%i < TestCases\TestCase_PositiveLongNumbers_123456789.txt >> Output\output1_2.txt
    )
)

echo TestInputLongNumbers completed.
