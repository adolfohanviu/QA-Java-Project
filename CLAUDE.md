# CLAUDE.md

Read `AGENTS.md` first — it is the canonical, tool-agnostic instruction file
for this repository. This file only adds Claude-Code-specific notes.

- The project skills referenced in `AGENTS.md` (`add-page-object`,
  `add-config-value`, `repo-hygiene-check`) live under `.claude/skills/` and
  are invokable directly via the Skill tool.
- Reusable prompt templates live under `prompts/`.

Nothing else here duplicates `AGENTS.md` — if this file and `AGENTS.md` ever
disagree, `AGENTS.md` wins.
