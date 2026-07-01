# OrangeHRM Selenium Test Suite

Automated UI regression tests for the OrangeHRM demo site
(`https://opensource-demo.orangehrmlive.com/`), built with **Selenium WebDriver
4**, **TestNG**, and **Maven**, and wired up for **Jenkins** + **Docker**.

## What changed from the original scripts

The original 10 files were plain `main()` classes with several bugs and bad
practices that would not have compiled or run reliably:

- Two different files both declared `public class logintest` (illegal — a jar
  can't have duplicate classes). Split into a single `LoginTest` with a
  success **and** a failure case.
- `PIMTest`, `testTime`, `LeaveTest`, `RecruitmentTest`, `MYInfo`, and
  `testPerformance` were ~95% duplicated code. Consolidated into a shared
  `BaseTest.navigateToMenuAndVerify()` helper.
- Missing semicolons / unbalanced braces (`PIMTest`, `testTime`) — fixed.
- `Thread.sleep(...)` everywhere — replaced with explicit
  `WebDriverWait`/`ExpectedConditions`, which is faster and far less flaky.
- "Assertions" were just `System.out.println("PASS/FAIL")` — replaced with
  real TestNG `Assert` calls, so failures actually fail the build and show up
  in CI reports.
- No project/build files at all — added `pom.xml`, `testng.xml`, `Dockerfile`,
  `Jenkinsfile`.

## Project layout

```
orangehrm-selenium-tests/
├── pom.xml
├── Dockerfile
├── Jenkinsfile
└── src/test/
    ├── java/
    │   ├── base/BaseTest.java        # shared setup/teardown + helpers
    │   └── tests/
    │       ├── LoginTest.java        # TC01 success + failure
    │       ├── AdminTest.java        # TC02
    │       ├── AddUserTest.java      # TC03
    │       ├── PIMTest.java          # TC04
    │       ├── TimeTest.java         # TC05
    │       ├── LeaveTest.java        # TC06
    │       ├── RecruitmentTest.java  # TC07
    │       ├── MyInfoTest.java       # TC08
    │       └── PerformanceTest.java  # TC09
    └── resources/testng.xml
```

## Running locally

Requires JDK 11+, Maven, and Google Chrome installed. `webdrivermanager`
auto-downloads the matching chromedriver at runtime, so there's no manual
driver setup.

```bash
mvn test
```

Run a single class:

```bash
mvn test -Dtest=LoginTest
```

Run headless (no visible browser window):

```bash
mvn test -Dheadless=true
```

Reports land in `target/surefire-reports/`.

## Running in Docker

```bash
docker build -t orangehrm-tests .
docker run --rm orangehrm-tests
```

To pull the reports out afterward, mount a volume:

```bash
docker run --rm -v "$(pwd)/target:/app/target" orangehrm-tests
```

## Running in Jenkins

The included `Jenkinsfile` defines a declarative pipeline: checkout → build →
run tests headless → publish JUnit/TestNG results via the `junit` step. In
Jenkins, configure:

- A Maven tool named `Maven3` (Manage Jenkins → Tools)
- A JDK tool named `JDK17`
- A pipeline job pointing at this repo, using the `Jenkinsfile`

Chrome must be installed on the Jenkins agent, or run the pipeline stage
inside the same Docker image as an `agent { dockerfile true }` block if you'd
rather not install Chrome directly on the agent.

## Notes / things worth double-checking

- **Page headings**: `navigateToMenuAndVerify` asserts on the `<h6>` page
  title (e.g. "Employee Information" for PIM, "Leave List" for Leave). The
  OrangeHRM demo UI changes occasionally — if a test fails only on the
  heading assertion, inspect the live page and adjust the expected string in
  the relevant test class.
- **AddUserTest** picks the first autocomplete match for the employee name
  and generates a unique username per run (`sanjay_<timestamp>`) so repeat
  runs don't collide on "username already exists" validation. You'll likely
  want to point this at a test employee that actually exists in your
  environment rather than "Sanjay".
- This is a public demo environment — data you add (like the new user) will
  persist and be visible to other people using the same demo site.
