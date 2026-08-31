# Add a config value through ConfigManager

**Purpose:** get an agent to add a new configurable value using this
codebase's real, layered precedence chain instead of a shortcut side-channel.

## Prompt template

```
I need a new configurable value: {name, e.g. "checkout page timeout"},
environment variable {ENV_VAR_NAME}, default {default value}.

Add it the way every existing value in ConfigManager.java is added:
1. Add the key to `application.conf` (and any env-specific `.conf` file that
   needs a different default) as `{section}.{key} = {default}` followed by
   `{section}.{key} = ${?{ENV_VAR_NAME}}`.
2. Add a public static getter on `ConfigManager` that calls the existing
   `getStringConfig`/`getIntConfig`/`getBooleanConfig` private helper with
   that key and a hardcoded Java-side default — matching the pattern of
   `getBaseURL()` / `getTimeout()` / `isAllureEnabled()`.
3. If (and only if) this needs a command-line override, add a system-property
   check ahead of the environment variable, the same way `isHeadless()` does
   for `-Dbrowser.headless`.
4. Document {ENV_VAR_NAME} in `.env.example` and in README's "Common runtime
   variables" list.

Do not read `System.getenv`/`System.getProperty` from anywhere except that
new ConfigManager getter. Do not add a `.env` file loader or read
`.vscode/settings.json` — neither is ever read by this framework. Do not
touch `pom.xml`.
```

## Why this works

`ConfigManager` resolves config through a real precedence chain — system
property → environment variable (via HOCON `${?VAR}` substitution) → HOCON
file default → hardcoded Java default — and every typed getter logs and
returns a safe default instead of throwing or silently returning `null`. The
sibling TypeScript repo had a swallowed exception in its config layer that
silently disabled API mocking with no error anywhere; spelling out the exact
existing pattern in the prompt is what stops a new getter from reintroducing
that failure mode or bypassing `ConfigManager` altogether.
