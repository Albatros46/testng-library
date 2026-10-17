# TestNG Deep Dive · Library System

![Java](https://img.shields.io/badge/Java-21-orange) ![TestNG](https://img.shields.io/badge/TestNG-7.12-red) ![Maven](https://img.shields.io/badge/Maven-3.9-blue) ![Tests](https://img.shields.io/badge/tests-43%20passing-brightgreen)

The TestNG features every Selenium and Appium framework is built on, learned on a small library domain (books, members, borrowing) without a browser.
Part of my 52-week QA Automation roadmap — **Week 5**.

## What is covered

| Test class | TestNG features |
|---|---|
| `LifecycleTest` | Execution order of `@BeforeSuite` → `@BeforeTest` → `@BeforeClass` → `@BeforeMethod` and the `@After…` counterparts |
| `IsbnValidatorTest` | `@DataProvider` in 3 ways: same class, `parallel = true`, and from a CSV file via `dataProviderClass` |
| `BorrowFlowTest` | `priority`, `dependsOnMethods`, `expectedExceptionsMessageRegExp`, `alwaysRun` |
| `LibraryRulesTest` | `expectedExceptions`, `assertThrows`, `SoftAssert`, `timeOut`, `invocationCount`, `enabled = false` |
| `EnvironmentTest` | `@Parameters` + `@Optional`: environment values come from `testng.xml` |

## Suites

The suite is chosen with a Maven property, the same way a CI pipeline does it.

| Suite | Content | Command | Tests |
|---|---|---|---|
| `testng.xml` | Everything, `env = qa` | `mvn test` | 43 |
| `smoke.xml` | `smoke` group only, `env = staging` | `mvn test -Dsuite=smoke` | 10 |
| `regression.xml` | `regression` group, `performance` excluded | `mvn test -Dsuite=regression` | 40 |
| `parallel.xml` | Classes in 3 threads | `mvn test -Dsuite=parallel` | 40 |

Run a single test: `mvn test -Dtest=LibraryRulesTest#newLibraryState`

## JUnit 5 vs TestNG

| Topic | JUnit 5 | TestNG |
|---|---|---|
| Test class instance | New instance per test | **One instance per class** (state can be shared) |
| Dependent tests | Not supported (by design) | `dependsOnMethods`, `dependsOnGroups` |
| Grouping | `@Tag` | `groups` + include/exclude in `testng.xml` |
| Suite definition | `@Suite` (Java) | `testng.xml` (outside the code) |
| Parameterized tests | `@ParameterizedTest` | `@DataProvider` |
| Environment parameters | — | `@Parameters` |
| Assertion argument order | `assertEquals(expected, actual)` | **`assertEquals(actual, expected)`** |

## Three experiments

**1 · `dependsOnMethods` turns a broken flow into one clear failure.**
I broke the first step of the borrow flow (`registerMember`). The result was `Failures: 1, Skipped: 3`. The three dependent steps were skipped rather than failed, so the report points straight at the real cause.

**2 · A `SoftAssert` without `assertAll()` is a false positive.**

| Code | Result |
|---|---|
| `soft.assertEquals(size, 99)` + `soft.assertAll()` | ❌ `expected [99] but found [4]` |
| `soft.assertEquals(size, 99)` without `assertAll()` | ✅ **green** — the wrong value is never reported |

Soft assertions only collect failures. Only `assertAll()` reports them.

**3 · Why `parallel="classes"` and not `methods`.**
`BorrowFlowTest` shares one `Library` instance across its steps. With `parallel="methods"`, borrow and give-back could run at the same time in different threads, which is a classic source of flaky tests. `classes` keeps each class on one thread.

## Project structure

```
src/main/java/com/servet/library/      Book, IsbnValidator, Library
src/test/java/com/servet/library/      5 test classes
src/test/java/com/servet/library/data/ TestData (shared DataProviders)
src/test/resources/isbn-data.csv       test data outside the code
src/test/resources/suites/             testng, smoke, regression, parallel
```

## What I learned
- _TODO: 2–3 sentences in your own words_
