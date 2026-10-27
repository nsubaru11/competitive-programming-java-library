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
javac --release 17 -encoding UTF-8 -d "%WORK_PATH%\out" -sourcepath "%PROJECT_ROOT%\src" "%PROJECT_ROOT%\test\benchmark\io\bench17\TestInputDoubleNumbers.java"
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
        java %JVM_OPTS% -cp "%WORK_PATH%\out" benchmark.io.bench17.TestInputDoubleNumbers %%i < TestCases\TestCase_DoubleNumbers_123456789.txt >> Output\output5.txt
    )
)

echo TestInputDoubleNumbers completed.
