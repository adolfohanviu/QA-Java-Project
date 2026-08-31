# Verify selector before scaffolding

**Purpose:** get an agent to confirm a selector against the real, live app
before it writes a page object or step definition that depends on it.

## Prompt template

```
I need a Page Object method (or step definition) that interacts with
{element description, e.g. "the cart badge count"} on {page URL, e.g.
"https://www.saucedemo.com/inventory.html"}.

Before writing any code:
1. Fetch/inspect the real, current DOM at that URL.
2. Find the actual selector for {element description} — prefer a
   `data-test` attribute if one exists, otherwise the most stable class.
3. State the exact selector you found and where in the DOM you found it.
4. Only after that, add it to `TestConstants.Selectors` (or the matching
   nested class) and write the {page object method / step definition} using
   it via `BasePage`'s helpers (`click`, `typeText`, `getText`,
   `waitForElement`, etc.) — never as an inline string literal.

Do not invent a selector from a similar site or from memory of a past
SauceDemo version. If you cannot reach the live page, say so instead of
guessing.
```

## Why this works

This codebase's `TestConstants.Selectors` (`[data-test='username']`,
`.inventory_container`, `.shopping_cart_badge`, …) has always matched the
real, live `saucedemo.com` DOM — that discipline is the one thing this repo
never regressed on, unlike the sibling TypeScript/Playwright repo, which
shipped selectors against a domain that didn't exist. Forcing the
verification step *before* code generation, rather than asking for it after
the fact, is what keeps that streak intact.
