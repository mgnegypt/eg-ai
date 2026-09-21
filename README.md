<div align="center">
  <img src="docs/icon.png" alt="MGN AI Icon" width="100" />
  <h1>MGN AI</h1>

A native Android LLM chat client with full Arabic support, per-app language switching,
in-app updates from its own releases, and Firebase-powered services 🤖💬🌙

[العربية](README_AR.md) | English
</div>

## 🚀 Download

🔗 [Download from GitHub Releases](https://github.com/mgnegypt/eg-ai/releases) (Recommended)

Available APKs per release (see [APK naming](#-apk-naming)):

- `mgn-ai-v<VERSION>-arm64-v8a-release.apk` — most phones
- `mgn-ai-v<VERSION>-x86_64-release.apk` — x86_64 devices/emulators
- `mgn-ai-v<VERSION>-universal-release.apk` — all ABIs

Package: `com.mgn.ai` (installs alongside other clients, no conflicts).

## ✨ Features

- 🌙 Full Arabic localization with RTL, plus English; instant in-app language switcher (System / English / العربية)
- 🎨 MGN identity theme — calm green/white light mode, neon-lime dark mode — plus optional presets
- 🔄 Multiple AI Provider Support: custom API / URL / models (all OpenAI, Google, Anthropic compatible api)
- 🖼️ Multimodal input support (Image, Text Documentation, PDF, Docx)
- 🖥️ Web access for multi-platform use
- 🛠️ MCP support
- 📝 Markdown Rendering (with code highlighting, Latex formulas, tables, Mermaid)
- 🪾 Message Branching
- 🔍 Search capabilities (Exa, Tavily, Zhipu, LinkUp, Brave, Perplexity, etc.)
- 🧩 Prompt variables (model name, time, etc.)
- 🤳 QR code export and import for providers
- 🤖 Agent customization
- 🧠 ChatGPT-like memory feature
- 📝 AI Translation
- 🌐 Custom HTTP request headers and request bodies
- 💌 Silly Tavern character card import
- 🔔 In-app update checker wired to [MGN AI releases](https://github.com/mgnegypt/eg-ai/releases)
- 💝 Binance donations configured remotely via Firebase Remote Config
- 📇 Contact Developer (Facebook, WhatsApp, Telegram) from Settings → About

## 🛠️ Setup & Build

Requirements: JDK 17, Android SDK (API 37 + build-tools), Node 22 + pnpm 11.

```bash
git clone https://github.com/mgnegypt/eg-ai.git
cd eg-ai
# Optional: a real google-services.json for com.mgn.ai (Firebase project mgn-ai)
# cp /path/to/google-services.json app/google-services.json
./gradlew assembleDebug   # Debug APK (package com.mgn.ai.debug)
./gradlew test            # JVM unit tests
./gradlew lint            # Android Lint
./gradlew assembleRelease # Signed release APKs (needs signing, see below)
```

CI (`.github/workflows/ci.yml`) runs `assembleDebug`, an APK identity/locales check
(`aapt dump badging`), unit tests and lint on every push/PR.


## 🏗️ Architecture

- **app**: UI (Jetpack Compose, Navigation 3), ViewModels, update checker, locale system
- **ai**: provider SDK abstraction (OpenAI, Google, Anthropic, …)
- **common / document / highlight / material3 / search / speech / videogen / workspace / oauth / web**: feature libraries
- Internal Kotlin namespaces live under `com.mgn.ai.*`; Android application ID is `com.mgn.ai`

## ✨ Contributing

This project is developed using [Android Studio](https://developer.android.com/studio). Before
submitting a pull request, please read the [contribution guidelines](CONTRIBUTING.md).

Technology stack:

- [Kotlin](https://kotlinlang.org/) (Development language)
- [Koin](https://insert-koin.io/) (Dependency Injection)
- [Jetpack Compose](https://developer.android.com/jetpack/compose) (UI framework)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (Preference data storage)
- [Room](https://developer.android.com/training/data-storage/room) (Database)
- [Coil](https://coil-kt.github.io/coil/) (Image loading)
- [Material You](https://m3.material.io/) (UI design)
- [Navigation 3](https://developer.android.com/guide/navigation/navigation-3) (Navigation)
- [Okhttp](https://square.github.io/okhttp/) (HTTP client)
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) (JSON serialization)
- [Firebase](https://firebase.google.com/) (Crashlytics, Analytics, Remote Config)

## ⭐ Star History

If you like this project, please give it a star ⭐

## 📄 License

This project is licensed under the [GNU Affero General Public License v3.0](LICENSE) (AGPL-3.0).

MGN AI's copyright is held by its developers and contributors only, and it is distributed under the same license. The original copyright notices are preserved.
