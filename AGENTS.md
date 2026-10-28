# AGENTS.md

## Project Big Picture

- This repository is a competitive-programming Java library collection, not a single app or service.
- `src/lib/` is the importable API source tree. Packages are grouped as `lib.ds`, `lib.graph`, `lib.io`, `lib.math`, `lib.search`, `lib.sort`, `lib.string`, and `lib.util`.
- `src/patterns/` contains code meant to be read, copied, or adapted rather than treated as a stable reusable API.
- `test/` is the test source root. `test/example/` contains usage examples, `test/unit/` contains local correctness checks, `test/judge/` contains online-judge programs, and `test/benchmark/` contains benchmark drivers and their supporting implementations.
- `docs/` contains module READMEs, detailed guides, and benchmark runners.
- Fast I/O is split by runtime target: `lib.io` is Java 24 optimized; `lib.io.compat17` must remain Java 17 compatible.

## Coding Conventions

- Follow `.github/copilot-instructions.md` as project-local policy.
- Use wildcard imports such as `import java.util.*;` and `import lib.math.*;` throughout the repository to keep source code short. Do not replace them with explicit imports merely for import-list cleanliness.
- Write JavaDoc for non-compressed public classes and methods; keep constants in `UPPER_SNAKE_CASE`.
- Use tabs for Java indentation and keep a trailing newline at end-of-file.
- Prefer bit operations and low-level optimizations where they measurably help performance.
- For reusable APIs, add overloads and primitive-specialized variants when appropriate.
- Public importable classes must live at `src/lib/<package>/<ClassName>.java`; the path, package declaration, public type, and filename must agree.
- Declare class members in this order: static constants, fields (`public final`, then `private final`, then mutable), constructors, static methods (public static factories, then private static helpers), public instance methods, package-private instance methods, private instance methods, nested types. This matches IntelliJ's default "Rearrange entries" order, which moves static methods directly below the constructors.
- Order constructors by source kind: empty or size, size with an initial value, array, collection, copy, then private constructors. Within each kind, place fewer parameters first.
- Group public methods in this order:
  1. State: `size`, `isEmpty`, `capacity`, `indexCount`, dimension accessors such as `h` / `w`, `rotation`, `getDefaultValue` / `setDefaultValue`.
  2. Access: membership checks first (`inRange`, `containsKey`, `containsIndex`, `hasCost`), then `get`, `getOrDefault`, `getFirst`, `getCost`, `peek`, and similar reads.
  3. Element updates: `set`, `add`, `multiply`, `affine`, `apply`, `addFirst` / `addLast`, `removeFirst` / `removeLast`, `put`, `putIfAbsent`, `computeIfAbsent`, `remove`, `increment`, `merge`, `poll`, `replaceTop`, `relax`.
  4. Bulk updates: `fill`, `setAll`, `clear`.
  5. Aggregation: `sum`, `sumAll`, `query`, `queryAll`.
  6. Search: `lowerBound`, `upperBound`, `maxRight`, `minLeft`, `indexOf`, `rankOfValue`, `count`.
  7. Transformation: `swap`, `reverse`, `rotate*`, `transpose`, `flip*`.
  8. Conversion and traversal: `restore`, `toArray`, `toArray2D`, `keys` / `values` / `entries`, `forEach*`, `reduce*`, `iterator`, `spliterator`.
  9. `Object` methods: `toString`.
- Within a group, keep overloads of the same name adjacent with fewer parameters first, place an `OrDefault` variant immediately after its base method, and order related operations from simple to general (`add`, `multiply`, `affine`, `apply`; `put`, `putIfAbsent`, `computeIfAbsent`; `increment`, `decrement`, `add`, `addOrDefault`; `mergeMin`, `mergeMax`, `merge`).
- Among private instance methods, place `build` first and order the remaining helpers from callers to callees.

## Competitive-Programming Assumptions

- Evaluate APIs under valid contest constraints and inputs. Do not require defensive handling for states that cannot be produced by the problem input, such as empty construction when the problem guarantees a positive size or arithmetic edge cases excluded by the constraints.
- Treat extreme values such as `Integer.MIN_VALUE` and `Long.MIN_VALUE`, including overflow caused by negating them, as out of scope for routine design and review. The same applies to stamp or generation-counter overflow when valid usage cannot reach it. Address these cases only when the user explicitly requests it or the valid constraints of a target problem include them; do not repeatedly raise excluded arithmetic edge cases as general concerns.
- Prefer speed, short hot paths, and readable contest code over exhaustive validation, fail-fast checks, or general-purpose collection contracts.
- Add validation or special-case handling only when it is required for valid contest inputs, prevents a realistic wrong answer, or has negligible cost and clearly improves the API.
- Documentation should state the expected preconditions instead of implying that every invalid argument is checked.
- Do not add utility methods that merely wrap functionality already provided by the Java standard library, such as `Integer.parseInt`, `Long.parseLong`, `Integer.toString`, or `Long.toString`. Add a custom API only when it provides a different representation, avoids a relevant allocation, or combines contest-specific operations.

## Architecture and Dependency Rules

- Static utilities normally use a `final` class, private constructor, and static methods, for example `src/lib/search/BinarySearch.java`.
- Binary-search APIs return insertion points using bitwise-complement (`~index`) semantics.
- Performance-critical code avoids avoidable allocation and may use manual buffering, SWAR, `VarHandle`, or `Unsafe` where the runtime-specific package permits it.
- Generic data structures accept operation functions where useful, for example `src/lib/ds/SegmentTree.java`.
- Library classes may depend on other `lib.*` classes through normal imports. Keep the dependency graph small and acyclic where practical.
- Do not restore private copies of shared primitives inside higher-level algorithms. For example, `lib.graph.Kruskal` uses `lib.ds.UnionFind`.
- Imports from `lib.*` are expanded transitively by the AtCoder-side bundler. Static imports from `lib.*` and fully qualified `lib.*` references in method bodies are not bundler-compatible.
- For APIs that expose digit or byte order, document the order with a concrete example. Big-endian places the most significant unit first: `00121` represents `121`; little-endian places the least significant unit first: `00121` represents `12100` when interpreted as decimal digits.

## Developer Workflows

- After completing work, remove temporary build outputs and editing scripts created for the task. Do not leave them in `out/` or include them in deliverables. Prefer an OS temporary directory for one-off editing scripts, and remove those scripts after use. Preserve pre-existing files unless cleanup is requested.
- There is no Maven or Gradle build. Compile the source roots directly with `javac`.
- Before creating a commit, read `.github/COMMIT_MESSAGE_TEMPLATE.md` and follow it. Commit messages must be written in Japanese using one of the documented prefixes (`feat`, `refactor`, `docs`, `style`, `test`, `perf`, `fix`) in the form `prefix: 概要`; add the `詳細:` section with bullet points when useful.
- Full Java 24 smoke compile in PowerShell:

  `javac --release 24 -encoding UTF-8 -d out (rg --files src -g '*.java')`

- Compile verification sources after the library:

  `javac --release 24 -encoding UTF-8 -cp out -d out/test (rg --files test -g '*.java')`

- Check Java 17 compatibility separately for `src/lib/io/compat17/*.java` using `--release 17`.
- Run executable examples by fully qualified name, for example `java -cp "out;out/test" example.graph.Example` on Windows. Existing local checks use `main` methods; enable Java assertions with `-ea` when running `unit.*` classes. See `test/README.md` for the layout and naming rules.
- The Java 24 benchmark entrypoints are `docs/io/java24/benchmark/run_tests_24.sh` and `.bat`; generated files stay under that directory's `work/` folder.

## Submission Integration

- Contest solutions should use ordinary imports such as `import lib.ds.UnionFind;` while editing locally.
- Raw import-based solutions cannot be submitted to AtCoder. The runner's `run`, `localtest`, `test`, `tomain`, and `submit` paths must all operate on the bundled source.
- Keep a manual fallback available: every bundled FQCN maps back to its source under `src/`, and bundler output reports the inlined class list.
- The AtCoder repository may mount this repository as `library/` or set `ATCODER_LIB_SRC` to this repository's `src` directory.

## Documentation Conventions

- Write Japanese documentation in concise plain style (`常体`), using forms such as `〜する` and `〜である` instead of `です・ます`. Noun phrases are suitable for headings, labels, and short table cells; keep explicit predicates when a fragment could obscure behavior or conditions.
- Treat the documentation as a reference for understanding and implementing algorithms. Use descriptive headings, exact class and method names, and concise statements. Avoid conversational filler, repeated explanations, and unexplained pronouns. Do not sacrifice precision merely to reduce token count.
- A module README gives a short module summary and a class list with each class's purpose and links to its Guide and source. Include supporting and tree-related classes that are useful for understanding the module, not only top-level algorithms. List generic, `int`, and `long` variants separately. Link Examples or verification files when they exist; use `—` for unavailable links. Adjust relative paths to the README's location.
- A class Guide documents the class's role, features, dependencies, complete public API including every overload, preconditions and cautions, time and space complexity, performance characteristics, and implementation history. In `利用例`, link to `Example.java` when it exists and do not embed code blocks or per-class usage examples; the Example file is the canonical location. When it has not been implemented, write only `準備中` and omit the link and code blocks. Omit inapplicable sections and class variants. For Guides covering multiple classes, identify each class's API and differences. Label the README backlink with the destination README title.
- A reference-materials document records sources actually consulted, algorithm definitions, external URLs, and how each source relates to the implementation. Distinguish theoretical definitions from reference implementations and verification targets. Remove unused source categories. For external implementations, check and record applicable licenses and modifications. Keep source citations out of module READMEs and class Guides; link to the reference-materials document when needed.
- Keep each detailed explanation at its canonical location and link to it from other pages instead of duplicating it. Use relative links for files inside the repository.
- Verify documentation claims against `src/`. Use exact API signatures. State actual preconditions and behavior; do not imply that invalid arguments are checked when the implementation does not check them.
- Define symbols such as `n` and `m` before using them in complexity statements. State index bases and interval conventions explicitly where they affect an API. Mark amortized complexity as amortized.
- Record source implementation changes in Guide history; omit documentation-only edits. In history version numbers, use the first digit for feature additions and the second digit for bug fixes or small improvements.
- Use `docs/README_TEMPLATE.md`, `docs/GuideTemplate.md`, and `docs/ReferenceTemplate.md` as structural templates. Keep general writing rules here rather than duplicating them in template instructions.

## Integration and Maintenance

- Static analysis is configured in `qodana.yaml` with JDK 24.
- When creating or updating a detailed class guide, follow the root `docs/GuideTemplate.md`, including features, dependencies, complete overload-aware method tables listed in source declaration order, examples, cautions, performance characteristics, and implementation version history.
- When API behavior or location changes, update the relevant module README under `docs/`, detailed guides, root `README.md`, and verification code.
- Prefer minimal API breakage; contest solutions and the bundler consume these classes as source.
- Some documentation may lag implementation. Verify claims against `src/` before editing.
