# AGENTS.md

## Project Big Picture

- This repo is a **competitive programming Java library collection**, not a single app/service.
- Top-level boundaries are `Algorithms/` and `DataStructures/`; each topic is split into a standalone module with
  `src/`, often `README.md`, and sometimes `docs/`.
- Classes are designed for **copy-paste use in contests** and are usually in the **default package** (no `package`
  declaration), e.g. `Algorithms/Search/BinarySearch/src/BinarySearch.java`.
- Fast I/O is split by runtime target: `DataStructures/FastIO/Java17/src/*` (Java 17) and
  `DataStructures/FastIO/Java24/src/*` (
  Java 24 optimized).
- Many modules include `Example.java` as an executable usage check, e.g.
  `Algorithms/Search/BinarySearch/src/Example.java`.

## Coding Conventions Used Here

- Follow `.github/copilot-instructions.md` as project-local policy.
- Write JavaDoc for non-compressed classes/methods; keep constants in `UPPER_SNAKE_CASE`.
- Use **tabs** for indentation and keep a trailing newline at end-of-file.
- Prefer bit operations and low-level optimizations where reasonable (see `FastScanner` and `FastPrinter`
  implementations).
- For reusable APIs, add overloads and provide primitive + generic variants when appropriate.

## Architecture/Implementation Patterns (with examples)

- Static utility style: `final` class + private constructor + static methods (
  `Algorithms/Search/BinarySearch/src/BinarySearch.java`).
- Binary search APIs return insertion points with bitwise complement (`~index`) semantics.
- Performance-critical code avoids extra allocations and uses manual buffering/bit tricks:
	- `DataStructures/FastIO/Java24/src/FastScanner.java` (`(n << 3) + (n << 1)`, `VarHandle` access)
	- `DataStructures/FastIO/Java24/src/FastPrinter.java` (power-of-two buffers, manual digit writes)
- Generic data structures accept function objects to define operations, e.g.
  `DataStructures/SegmentTree/src/SegmentTree.java` with `BinaryOperator<T>` and `Predicate<T>`.
- I/O usage pattern is `try-with-resources` for `AutoCloseable` scanners/printers (
  `DataStructures/FastIO/Java24/src/Example.java`, root `README.md`).

## Developer Workflows (non-obvious but practical)

- There is **no Maven/Gradle** build; compile only needed files with `javac`.
- Typical PowerShell flow (adjust module paths as needed):
	-
  `javac -encoding UTF-8 -d out "DataStructures\FastIO\Java24\src\FastScanner.java" "DataStructures\FastIO\Java24\src\FastPrinter.java"`
	- `javac -encoding UTF-8 -cp out -d out Main.java`
	- `java -cp out Main`
- For module smoke checks, compile module `src/*.java` and run its `Example` class.
- Benchmark/profiling entrypoint is `DataStructures/FastIO/Java24/Benchmark/run_tests_24.sh` (Linux/WSL bash; `.bat` for
  Windows); it compiles FastIO24 benchmark classes, runs repeated measurements, and emits CSV/logs under
  `Benchmark/work/` (gitignored). See `DataStructures/FastIO/Java24/Benchmark/README.md`.

## Integration Points and Maintenance Rules

- Static analysis is configured in `qodana.yaml` (`jetbrains/qodana-jvm:2025.2`, `projectJDK: "24"`).
- Contribution metadata is standardized via `.github/ISSUE_TEMPLATE/` and `.github/PULL_REQUEST_TEMPLATE/`.
- When API behavior changes, update both code and docs:
	- module `README.md` (format guided by `README_TEMPLATE.md`)
	- module guides in `docs/` (template: `GuideTemplate.md`)
- Prefer minimal API breakage; this library is consumed as standalone snippets.
- Some READMEs may lag implementation (example: `Algorithms/Math/Factorial/README.md` class names). Verify against
  `src/`
  before editing.
