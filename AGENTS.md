# AGENTS.md

## Project Big Picture

- This repository is a competitive-programming Java library collection, not a single app or service.
- `src/lib/` is the importable API source tree. Packages are grouped as `lib.ds`, `lib.graph`, `lib.io`, `lib.math`, `lib.search`, `lib.sort`, `lib.string`, and `lib.util`.
- `src/patterns/` contains code meant to be read, copied, or adapted rather than treated as a stable reusable API.
- `test/verify/` contains executable examples, online-judge checks, and benchmark drivers. It is a test source root.
- `docs/` contains module READMEs, detailed guides, and benchmark runners.
- Fast I/O is split by runtime target: `lib.io` is Java 24 optimized; `lib.io.compat17` must remain Java 17 compatible.

## Coding Conventions

- Follow `.github/copilot-instructions.md` as project-local policy.
- Write JavaDoc for non-compressed public classes and methods; keep constants in `UPPER_SNAKE_CASE`.
- Use tabs for Java indentation and keep a trailing newline at end-of-file.
- Prefer bit operations and low-level optimizations where they measurably help performance.
- For reusable APIs, add overloads and primitive-specialized variants when appropriate.
- Public importable classes must live at `src/lib/<package>/<ClassName>.java`; the path, package declaration, public type, and filename must agree.

## Competitive-Programming Assumptions

- Evaluate APIs under valid contest constraints and inputs. Do not require defensive handling for states that cannot be produced by the problem input, such as empty construction when the problem guarantees a positive size or arithmetic edge cases excluded by the constraints.
- Treat extreme values such as `Integer.MIN_VALUE` and `Long.MIN_VALUE`, including overflow caused by negating them, as out of scope for routine design and review. The same applies to stamp or generation-counter overflow when valid usage cannot reach it. Address these cases only when the user explicitly requests it or the valid constraints of a target problem include them; do not repeatedly raise excluded arithmetic edge cases as general concerns.
- Prefer speed, short hot paths, and readable contest code over exhaustive validation, fail-fast checks, or general-purpose collection contracts.
- Add validation or special-case handling only when it is required for valid contest inputs, prevents a realistic wrong answer, or has negligible cost and clearly improves the API.
- Documentation should state the expected preconditions instead of implying that every invalid argument is checked.

## Architecture and Dependency Rules

- Static utilities normally use a `final` class, private constructor, and static methods, for example `src/lib/search/BinarySearch.java`.
- Binary-search APIs return insertion points using bitwise-complement (`~index`) semantics.
- Performance-critical code avoids avoidable allocation and may use manual buffering, SWAR, `VarHandle`, or `Unsafe` where the runtime-specific package permits it.
- Generic data structures accept operation functions where useful, for example `src/lib/ds/SegmentTree.java`.
- Library classes may depend on other `lib.*` classes through normal imports. Keep the dependency graph small and acyclic where practical.
- Do not restore private copies of shared primitives inside higher-level algorithms. For example, `lib.graph.Kruskal` uses `lib.ds.UnionFind`.
- Imports from `lib.*` are expanded transitively by the AtCoder-side bundler. Static imports from `lib.*` and fully qualified `lib.*` references in method bodies are not bundler-compatible.

## Developer Workflows

- There is no Maven or Gradle build. Compile the source roots directly with `javac`.
- Full Java 24 smoke compile in PowerShell:

  `javac --release 24 -encoding UTF-8 -d out (rg --files src -g '*.java')`

- Compile verification sources after the library:

  `javac --release 24 -encoding UTF-8 -cp out -d out/test (rg --files test -g '*.java')`

- Check Java 17 compatibility separately for `src/lib/io/compat17/*.java` using `--release 17`.
- Run executable checks by fully qualified name, for example `java -cp "out;out/test" verify.graph.mst.Example` on Windows.
- The Java 24 benchmark entrypoints are `docs/io/Java24/Benchmark/run_tests_24.sh` and `.bat`; generated files stay under that directory's `work/` folder.

## Submission Integration

- Contest solutions should use ordinary imports such as `import lib.ds.UnionFind;` while editing locally.
- Raw import-based solutions cannot be submitted to AtCoder. The runner's `run`, `localtest`, `test`, `tomain`, and `submit` paths must all operate on the bundled source.
- Keep a manual fallback available: every bundled FQCN maps back to its source under `src/`, and bundler output reports the inlined class list.
- The AtCoder repository may mount this repository as `library/` or set `ATCODER_LIB_SRC` to this repository's `src` directory.

## Integration and Maintenance

- Static analysis is configured in `qodana.yaml` with JDK 24.
- When creating or updating a detailed class guide, follow the root `GuideTemplate.md`, including features, dependencies, complete overload-aware method tables, examples, cautions, performance characteristics, and implementation version history.
- When API behavior or location changes, update the relevant module README under `docs/`, detailed guides, root `README.md`, and verification code.
- Prefer minimal API breakage; contest solutions and the bundler consume these classes as source.
- Some documentation may lag implementation. Verify claims against `src/` before editing.
