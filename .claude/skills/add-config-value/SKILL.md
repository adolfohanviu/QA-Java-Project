---
name: add-config-value
description: "Trigger: new config value, environment variable, ConfigManager, add setting, new env var. Add configuration only through ConfigManager's layered precedence chain, never a side-channel."
license: MIT
metadata:
  author: "adolfohanviu"
  version: "1.0"
---

## Activation Contract

Trigger when a new runtime-configurable value (URL, timeout, flag,
credential-like value) needs to be read anywhere by the framework.

## Hard Rules

- Every new config value is a new typed getter on `ConfigManager`, built on
  its existing `getStringConfig`/`getIntConfig`/`getBooleanConfig` private
  helpers, following the same precedence: system property (only if a CLI
  override is genuinely needed, mirroring `isHeadless()`) → environment
  variable via HOCON `${?VAR}` substitution → HOCON default in
  `application.conf` → hardcoded Java default in the getter.
- Never call `System.getenv`/`System.getProperty` directly from a page
  object, step definition, hook, or utility class.
- Never add a `.env` file loader or code that reads `.vscode/settings.json`
  — no such loader exists in this codebase and none should be added.
- Every new getter must log and return the caller's default on a
  missing/malformed key — no silent `null`, no swallowed exception.
- Do not edit `pom.xml` as part of this work — dependency versions are owned
  by a separate task.

## Decision Gates

| Situation | Action |
|---|---|
| Value only consumed by tests | Still add via `ConfigManager`, not a test-local env read |
| Value is credential-like | Add a `ConfigManager` getter; do not extend `TestConstants.TestUsers`' direct `System.getenv` pattern |
| Need a CLI override | Add a system-property check first, same shape as `isHeadless()` |

## Execution Steps

1. Add the key to `application.conf` (and env-specific `.conf` files if
   needed) with a sensible default and `${?VAR}` substitution.
2. Add a public static getter on `ConfigManager` using the existing typed
   helpers.
3. Document the new environment variable in `.env.example` and README's
   "Common runtime variables" list.
4. Reference the new getter from calling code — never inline
   `System.getenv`.

## Output Contract

Report the edited files (`application*.conf`, `ConfigManager.java`,
`.env.example`, README variable list) and confirm `pom.xml` was not touched.

## References

- `assets/config-getter-template.java`
- `AGENTS.md` Rule 3
