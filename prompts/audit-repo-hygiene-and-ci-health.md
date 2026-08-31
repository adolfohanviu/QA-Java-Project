# Audit repo hygiene and CI health

**Purpose:** get an agent to check for stray build output/vendored binaries
before a commit, and for silently disabled GitHub Actions workflows before
assuming CI is healthy.

## Prompt template

```
Before I commit/push, audit this repo for two specific failure modes that
have already happened here once:

1. Stray build output or vendored binaries.
   - Run `git status` and list every new top-level path.
   - For each one, decide: is this generated/downloaded tool output (a
     build directory, a report directory, a vendored CLI/binary
     distribution)? If yes, add it to `.gitignore` and confirm it's
     untracked — don't stage it "to fix later."
   - Flag anything that looks like it came from `mvn`, an installer, or a
     downloaded CLI rather than something I wrote.

2. Silently disabled GitHub Actions workflows.
   - Run `gh workflow list --all`.
   - Report the state of every workflow. If any show `disabled_inactivity`,
     tell me explicitly — don't just say "CI looks fine" because nothing is
     failing.
   - If you find one, re-enable it with `gh workflow enable <name-or-id>` and
     mention it in the commit/PR description so it doesn't get silently
     rediscovered later.

Report both findings even if there's nothing to fix — I want the negative
result stated, not omitted.
```

## Why this works

This repo already committed `dependency-reduced-pom.xml` (stale
`maven-shade-plugin` output) and the entire `.allure/` CLI distribution (68
binary jars) by mistake, and separately had two of its three workflows
(`regression-tests.yml`, `all-tests.yml` — both `schedule:`-triggered) sit
`disabled_inactivity` for 4+ months with nothing in the README surfacing it.
Both were real, paid-for incidents, not hypotheticals — asking for an
explicit report of "nothing found" instead of silence is what prevents the
same class of oversight from repeating unnoticed.
