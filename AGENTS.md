# AGENTS.md

Canonical, tool-agnostic instructions for any AI agent (Claude Code, Copilot,
Cursor, etc.) working in this repository. `CLAUDE.md` is a thin pointer to
this file — this is the source of truth.

## Project context

Java 21 / Maven / Playwright Java / Cucumber BDD (JUnit 4 runner) / REST
Assured / Allure. Two real, live test targets, not sandboxes:

- UI: `https://www.saucedemo.com/`
- API: `https://jsonplaceholder.typicode.com`

Core files you must read before touching the areas below:
`src/main/java/com/qa/utils/BrowserContextManager.java`,
`src/main/java/com/qa/utils/ConfigManager.java`,
`src/main/java/com/qa/utils/TestConstants.java`,
`src/main/java/com/qa/pages/BasePage.java`, `src/test/java/com/qa/stepdefs/Hooks.java`.

---

## Rule 1 — Verify every selector against the real, live app before writing it

Every selector in `TestConstants.Selectors` (`src/main/java/com/qa/utils/TestConstants.java:89-106`)
— `[data-test='username']`, `.inventory_container`, `.shopping_cart_badge`,
etc. — matches the actual, current DOM of `saucedemo.com`. This codebase has
never had to walk back an invented selector against a domain or element that
doesn't exist.

- Before adding or changing a selector, open the real page (SauceDemo for UI,
  JSONPlaceholder's documented response shape for API) and confirm the
  attribute/class exists. Do not guess from memory or from a similar site.
- New selectors go in `TestConstants.Selectors` (or a nested class like it),
  never inlined as a string literal in a page object or step definition.
- Prefer `data-test` attributes over class names when the target page exposes
  them — they're the most stable anchor SauceDemo provides.
- **Why:** the sibling TypeScript/Playwright portfolio repo regressed exactly
  here — it shipped tests against selectors that didn't exist on the real
  domain. Keeping this discipline is the entire point of citing it here.

## Rule 2 — Browser lifecycle is `ThreadLocal`-owned; never reintroduce static state

`BrowserContextManager` holds `Playwright`/`Browser`/`BrowserContext`/`Page`
in `ThreadLocal` fields (`src/main/java/com/qa/utils/BrowserContextManager.java:21-24`),
and `closeBrowser()` (`:137-155`) explicitly calls `.remove()` on every one of
them after closing. `Hooks` (`src/test/java/com/qa/stepdefs/Hooks.java`) is
the **sole owner** of that lifecycle: `@Before` initializes browser → context
→ page, `@After` tears it all down in a `finally` block.

- Never add a `static` `Playwright`/`Browser`/`BrowserContext`/`Page` field
  anywhere. Always go through `BrowserContextManager.getPage()` (or the
  matching context/browser accessor).
- Never call `BrowserContextManager.createPage()` / `.createContext()` /
  `.initBrowser()` from a step definition or page object. Page objects call
  `BrowserContextManager.getPage()` in their constructor (see `BasePage.java:28-30`);
  scenario-level lifecycle belongs in `Hooks` only.
- If you add teardown logic anywhere, put it in `Hooks.tearDown()`'s `finally`
  block, or ensure whatever you add can't skip `closeBrowser()` on failure.
- **Why:** commit `84253b0` fixed a real bug where static fields caused
  cross-scenario page bleed under parallel Cucumber execution, and a
  duplicate `createPage()` call in `LoginStepDefinitions` was silently
  creating an orphaned second page per scenario. Both are the direct reason
  `Hooks` is now the single lifecycle owner and `ThreadLocal` exists at all.

## Rule 3 — `ConfigManager` is the only config-reading path

`ConfigManager.loadConfig()` (`src/main/java/com/qa/utils/ConfigManager.java:34-52`)
layers config as: environment-specific HOCON file (`application-<env>.conf`,
selected by `TEST_ENV`) → `application.conf` defaults, with `${?VAR}`
substitutions inside the HOCON files themselves pulling in real environment
variables (see `src/test/resources/application.conf:5-6,10-13,17-18,28-29,33-34`).
`isHeadless()` (`:73-87`) additionally checks a JVM system property
(`-Dbrowser.headless=...`) before the environment variable, for CLI overrides.
Every typed getter (`getStringConfig`/`getIntConfig`/`getBooleanConfig`,
`:164-200`) logs and returns a caller-supplied default on a missing or
malformed key — there is no catch block that swallows an exception and
leaves a feature silently disabled.

- Add new config values as a new typed getter on `ConfigManager`, following
  the same precedence chain and the same "log + typed default" pattern as the
  existing getters. Never read `System.getenv`/`System.getProperty` directly
  from a page object, step definition, or hook.
- **Known, narrow exception:** `TestConstants.TestUsers` (`TestConstants.java:43-65`)
  reads credentials via `System.getenv` directly, bypassing `ConfigManager`.
  This is pre-existing and scoped to test-user credentials only — do not copy
  this pattern for anything new. New credential-like values belong on
  `ConfigManager` as a proper getter, not a second `TestConstants` env read.
- There is no `.env` file loader and no code that reads `.vscode/settings.json`
  anywhere in this codebase. `.env.example` and `.vscode/settings.json` (both
  gitignored) are documentation of variable names for humans to export in
  their shell or set as CI secrets — the framework never reads either file.
  Don't add a loader for either; don't imply in docs/comments that one exists.
- **Why:** the sibling TypeScript repo had a swallowed exception in its config
  layer that silently disabled API mocking with no error surfaced anywhere.
  This codebase's config layer was already built to avoid exactly that failure
  mode (every catch block here logs); keep any new config code as loud on
  failure as the existing getters are.

## Rule 4 — Never commit vendored binaries or build tool output

`.gitignore` (`:1-8`) excludes `target/`, `dependency-reduced-pom.xml`, and
`.allure/` for a reason, not by convention: `dependency-reduced-pom.xml` was
stale `maven-shade-plugin` residue from a build config that no longer exists
in `pom.xml` (proven stale — its pinned dependency versions didn't match the
real `pom.xml`), and `.allure/` was the entire Allure commandline distribution
committed as 68 binary jar files instead of being installed locally. Both
were removed in commit `8129102`.

- If a tool you introduce writes output into a new top-level directory
  (reports, caches, downloaded CLIs, generated poms), add that directory to
  `.gitignore` *before* running the tool for the first time — don't wait to
  notice it in `git status` later.
- Never commit a `target/`, `node_modules`-equivalent, or vendored
  `bin`/binary-distribution directory. Install CLI tools (Allure, etc.)
  locally per-developer/per-CI-runner instead of checking them in.
- **Why:** both artifacts above were committed by mistake and had to be
  untracked after the fact — cheaper to gitignore proactively than to prove a
  file is stale months later.

## Rule 5 — Check for silently disabled workflows before your first commit in a while

GitHub auto-disables any workflow with a `schedule:` trigger
(`disabled_inactivity`) after 60 days without a repository push. Two of this
repo's three workflows are schedule-triggered —
`regression-tests.yml` (`schedule: cron '0 22 * * 5'`) and `all-tests.yml`
(`schedule: cron '0 2 * * *'`) — and both were silently auto-disabled for 4+
months with nothing in the README surfacing it; a viewer checking the Actions
tab would just see 2 of 3 workflows greyed out with no explanation. They were
manually re-enabled; `smoke-tests.yml` (pull-request-triggered only) was
never affected.

- If you're making the first commit in a while (or any time CI looks
  suspiciously idle), run `gh workflow list --all` and check for
  `disabled_inactivity` before assuming a greyed-out workflow in the Actions
  tab is self-explanatory or that CI is simply passing quietly.
- If you find one, re-enable it (`gh workflow enable <name-or-id>`) rather
  than leaving it for the next person to rediscover.
- **Why:** grey-vs-active status in the GitHub UI carries no explanation by
  itself; this already caused two of three workflows to silently stop running
  for months with zero signal.

---

## Skills

Project skills live under `.claude/skills/` and are invocable via the Skill
tool in Claude Code (see `CLAUDE.md`):

- `add-page-object` — scaffolds a new Page Object + step-definition pair,
  gated on selector verification against the real live app (Rule 1) and the
  `BasePage`/`ThreadLocal` conventions (Rule 2).
- `add-config-value` — adds a new config value through `ConfigManager`'s
  precedence chain only (Rule 3).
- `repo-hygiene-check` — pre-commit/pre-push check for stray build output or
  vendored binaries (Rule 4) and silently disabled workflows (Rule 5).

## Prompts

Reusable prompt templates live under `prompts/`. Each file documents one
real methodology used on this codebase (selector verification, config
precedence, commit-history archaeology) with an actual `{placeholder}`
template — not generic prompting advice. Read the file closest to the task
at hand before improvising a prompt from scratch.
