---
name: repo-hygiene-check
description: "Trigger: pre-commit check, pre-push check, new directory, vendored binaries, disabled workflow, repo hygiene. Flag stray build output before staging and silently disabled GitHub Actions workflows."
license: MIT
metadata:
  author: "adolfohanviu"
  version: "1.0"
---

## Activation Contract

Trigger before committing or pushing when a new top-level directory has
appeared, an unfamiliar tool/CLI was just run locally, or this is the first
commit after a long gap in repo activity.

## Hard Rules

- Never let generated or downloaded tool output reach the index: inspect
  `git status` for any new top-level directory before `git add`. If it looks
  like build output, a report/results directory, or a vendored CLI/binary
  distribution, add it to `.gitignore` first — don't stage it and fix it
  later.
- Treat any directory containing jar/binary files pulled in by `mvn`, an
  installer, or a CLI download as vendored — it belongs in `.gitignore`,
  never in git history.
- Before the first commit in a while, run `gh workflow list --all` and check
  for `disabled_inactivity` — grey-vs-active status in the Actions tab is not
  self-explanatory to a reader.

## Decision Gates

| Finding | Action |
|---|---|
| New top-level dir, generated-looking | Add to `.gitignore`, confirm it's untracked, then commit |
| New top-level dir, legitimately source | Stage normally |
| `gh workflow list --all` shows `disabled_inactivity` | Run `gh workflow enable <name-or-id>`, note it in the commit/PR description |
| All workflows `active` | No action, proceed |

## Execution Steps

1. Run `git status` and inspect any new top-level path.
2. Classify each new path using the Decision Gates table.
3. Run `gh workflow list --all`.
4. Re-enable any `disabled_inactivity` workflow found.
5. Only then stage and commit.

## Output Contract

Report any `.gitignore` additions made, any workflows re-enabled, and confirm
the staged diff contains no generated or vendored paths.

## References

- `AGENTS.md` Rule 4 and Rule 5
