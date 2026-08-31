---
name: add-page-object
description: "Trigger: new page object, POM, scaffold page, add page, Cucumber step definitions. Scaffold a Page Object + step-def pair gated on live-selector verification first."
license: MIT
metadata:
  author: "adolfohanviu"
  version: "1.0"
---

## Activation Contract

Trigger when adding a new SauceDemo page object and its Cucumber step
definitions, or extending an existing one with new interactions/selectors.

## Hard Rules

- Never write a selector without opening the real, live page (saucedemo.com)
  and confirming the attribute/class exists — no selector by memory or by
  analogy to another site.
- New page objects extend `BasePage`; never call `BrowserContextManager`
  lifecycle methods (`initBrowser`/`createContext`/`createPage`) directly —
  only `Hooks` owns the lifecycle.
- All selectors live in `TestConstants.Selectors` (or a nested class);
  never an inline string literal in a page object or step definition.
- Step definitions call only page-object methods; they never touch browser
  lifecycle.
- Annotate public page-object methods with `@Step` (Allure), matching
  existing style.

## Decision Gates

| Situation | Action |
|---|---|
| Selector unverified | Stop, inspect the live DOM, confirm the exact attribute, then proceed |
| Existing selector covers the need | Reuse from `TestConstants`, don't duplicate |
| New page/flow (e.g. checkout) | New class extends `BasePage` under `src/main/java/com/qa/pages` |

## Execution Steps

1. Identify the target SauceDemo page/flow.
2. Verify every needed selector against the live DOM; record the exact attribute used.
3. Add verified selectors to `TestConstants.Selectors`.
4. Create/extend the page object extending `BasePage`, using its
   click/typeText/getText/waitForElement helpers.
5. Add step definitions in the matching `*StepDefinitions` class, calling only
   the page object.
6. Add/extend the `.feature` scenario.

## Output Contract

Report the page object file, `TestConstants.Selectors` entries added, step
definitions touched, and feature file changed — plus which selectors were
verified and against what live element.

## References

- `assets/page-object-template.java`
- `assets/step-definitions-template.java`
- `AGENTS.md` Rule 1 and Rule 2
