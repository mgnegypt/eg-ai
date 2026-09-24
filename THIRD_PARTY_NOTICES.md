# Third-Party Notices — MGN AI

MGN AI is licensed under the **GNU Affero General Public License v3.0**
(see `LICENSE`). It is based on RikkaHub and integrates implementations from
the following projects, all of which are also AGPL-3.0. Original copyright
notices are preserved; no merged code is claimed as solely written by MGN AI.

## Integrated sources

### rikkahub/rikkahub (upstream)
- https://github.com/rikkahub/rikkahub — AGPL-3.0
- Base of this project. Merged since the fork point: `STEP_5` and
  `XIAOMI_MIMO_V2_6` model definitions (`ai/.../registry/ModelRegistry.kt`).

### sybdz/rikkahub-lune
- https://github.com/sybdz/rikkahub-lune — AGPL-3.0
- Chat runtime inspector: `TextRequestPreview` (+ per-provider preview
  builders), `GenerationLoop.previewPreparedMessages` /
  `buildTextGenerationParams`, `ChatService.inspectConversationRuntime`,
  `ChatRuntimeInspection`, `ChatRuntimeInspectorSheet` and chat entry point.
- Per-workspace agent columns + `Migration_25_26` (defensive variant).
- Note: secret header values are redacted in MGN AI's payload preview,
  unlike the original.

### xiaoyuili/Yuihub
- https://github.com/xiaoyuili/Yuihub — AGPL-3.0
- Self-management tools: `manage_skill` (`SkillManageTools.kt`) and
  `manage_mcp_server` (`McpManageTools.kt`), wired into `ChatToolFactory`.
- `SettingPermissionsPage` + `SystemPermissions` utility + `keepAwakeEnabled`
  setting.

### roccla231023/ROCL
- https://github.com/roccla231023/ROCL — AGPL-3.0
- Opt-in sub-agent: `data.ai.subagent` engine/models/registry/tools,
  `dispatch_subagent` gating in `ChatToolFactory`, `SubAgentToolUI` card,
  sub-agent model picker in Settings.
- Storage manager UI + `StorageManagerRepository`/`StorageScanUtils`/
  `TimedSuspendCache` + `ConversationDeletionCoordinator` + DAO range queries.
- Group chat: `GroupChatTemplate` model, `GroupChatEngine`,
  `GroupChatSeatPromptTransformer`, generation branches, template UI, routes.
- `UIMessage.speakerSeatId` / `speakerAssistantId` fields.

### Inonvation/rikkahub
- https://github.com/Inonvation/rikkahub — AGPL-3.0
- Todo task plans: `TodoStorage`, `todo_write` tool, `TodoReminderTransformer`
  (+ `conversationId` plumbing through the transforms chain),
  `TodolistBanner` + `SectionExpandStore`, `Settings.enableTodoList`.
- Prompt optimizer: prompts/scenes/tones/depths, per-scene settings,
  `ChatService.optimizePrompt`, `PromptOptimizeVM`/sheet, input-bar entry,
  model-page settings group.
- RAG knowledge: `:knowledge` module, knowledge tables (`Migration_26_27`),
  `KnowledgeSearchTool` (keyword mode), reminder transformer, picker +
  assistant binding. Deferred: embedding/rerank config, FTS index, CRUD UI,
  study tutors, TrustedFolders, incremental sync, Shizuku management.

### topabomb/rikkahub_mcp
- https://github.com/topabomb/rikkahub_mcp — AGPL-3.0
- Analyzed for the feature matrix; MCP runtime hardening not yet merged
  (base MCP stack is newer; queued).

### ExTV/rikkahub-agent
- https://github.com/ExTV/rikkahub-agent — AGPL-3.0
- Merged: permission-free device tools (`get_battery_status`,
  `get_storage_info`, `get_audio_info`, sensor list/read), keyless DuckDuckGo
  search engine with circuit breaker + egress guard + extractor.
- Merged: automation workflows (engine, triggers incl. geofence/boot/cron,
  conditions, tools, screens, `agent_runs` ledger, boot receiver, location
  permission) and scheduled cron jobs (scheduler, worker, job tools, screens,
  boot reschedule, `RECEIVE_BOOT_COMPLETED` permission).
- Deferred with reasons: cost guards (need Assistant budget-cap fields +
  settings UI), WifiInfo/location tools (need runtime permission UX),
  workflows/cron/Telegram/local-LLM/subsystems needing new permissions,
  services or native modules.

## Other third-party software

Gradle dependencies retain their own licenses (see `gradle/libs.versions.toml`;
e.g. Apache-2.0, MIT). The `material3/material-color-utilities` sources are
vendored in-tree under their original permissive licenses.
