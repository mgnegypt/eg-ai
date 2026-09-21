<div align="center">
  <img src="docs/icon.png" alt="MGN AI Icon" width="100" />
  <h1>MGN AI</h1>

  <p>
    A native Android LLM chat client with full Arabic support, per-app language switching,
    in-app updates from its own releases, and Firebase-powered services.
  </p>

  <p>
    <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Platform" />
    <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Language" />
    <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="UI" />
    <img src="https://img.shields.io/badge/License-AGPL--3.0-blue?style=flat-square" alt="License" />
  </p>

  <p>
    <a href="README_AR.md">العربية</a> · English
  </p>
</div>

---

## Table of Contents

- [Download](#download)
- [Features](#features)
- [Setup & Build](#setup--build)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [Star History](#star-history)
- [License](#license)

## Download

**[Download from GitHub Releases →](https://github.com/mgnegypt/eg-ai/releases)** (Recommended)

Available APKs per release (see [APK naming](#-apk-naming)):

| APK | Target |
|---|---|
| `mgn-ai-v<VERSION>-arm64-v8a-release.apk` | Most phones |
| `mgn-ai-v<VERSION>-x86_64-release.apk` | x86_64 devices / emulators |
| `mgn-ai-v<VERSION>-universal-release.apk` | All ABIs |

Package: `com.mgn.ai` (installs alongside other clients, no conflicts).

## Features

**Localization & Theming**
- Full Arabic localization with RTL support, plus English; instant in-app language switcher (System / English / العربية)
- MGN identity theme — calm green/white light mode, neon-lime dark mode — plus optional presets

**AI & Providers**
- Multiple AI provider support: custom API / URL / models (OpenAI, Google, Anthropic compatible APIs)
- Multimodal input support (image, text documents, PDF, DOCX)
- Web access for multi-platform use
- MCP (Model Context Protocol) support
- Agent customization
- ChatGPT-like memory feature
- AI translation

**Chat Experience**
- Markdown rendering with code highlighting, LaTeX formulas, tables, and Mermaid diagrams
- Message branching
- Search capabilities (Exa, Tavily, Zhipu, LinkUp, Brave, Perplexity, etc.)
- Prompt variables (model name, time, etc.)
- SillyTavern character card import

**Utilities & Integrations**
- QR code export/import for providers
- Custom HTTP request headers and request bodies
- In-app update checker wired to [MGN AI releases](https://github.com/mgnegypt/eg-ai/releases)
- Binance donations, configured remotely via Firebase Remote Config
- Contact developer (Facebook, WhatsApp, Telegram) from Settings → About

## Setup & Build

**Requirements:** JDK 17, Android SDK (API 37 + build-tools), Node 22 + pnpm 11

```bash
git clone https://github.com/mgnegypt/eg-ai.git
cd eg-ai

# Optional: a real google-services.json for com.mgn.ai (Firebase project mgn-ai)
# cp /path/to/google-services.json app/google-services.json

./gradlew assembleDebug     # Debug APK (package com.mgn.ai.debug)
./gradlew test              # JVM unit tests
./gradlew lint              # Android Lint
./gradlew assembleRelease   # Signed release APKs (needs signing, see below)
```

CI (`.github/workflows/ci.yml`) runs `assembleDebug`, an APK identity/locales check (`aapt dump badging`), unit tests, and lint on every push/PR.

## Architecture

| Module | Responsibility |
|---|---|
| `app` | UI (Jetpack Compose, Navigation 3), ViewModels, update checker, locale system |
| `ai` | Provider SDK abstraction (OpenAI, Google, Anthropic, …) |
| `common` / `document` / `highlight` / `material3` / `search` / `speech` / `videogen` / `workspace` / `oauth` / `web` | Feature libraries |

Internal Kotlin namespaces live under `com.mgn.ai.*`; the Android application ID is `com.mgn.ai`.

## Contributing

This project is developed using [Android Studio](https://developer.android.com/studio). Before submitting a pull request, please read the [contribution guidelines](CONTRIBUTING.md).

**Technology stack:**

| Technology | Role |
|---|---|
| [Kotlin](https://kotlinlang.org/) | Development language |
| [Koin](https://insert-koin.io/) | Dependency injection |
| [Jetpack Compose](https://developer.android.com/jetpack/compose) | UI framework |
| [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) | Preference data storage |
| [Room](https://developer.android.com/training/data-storage/room) | Database |
| [Coil](https://coil-kt.github.io/coil/) | Image loading |
| [Material You](https://m3.material.io/) | UI design |
| [Navigation 3](https://developer.android.com/guide/navigation/navigation-3) | Navigation |
| [OkHttp](https://square.github.io/okhttp/) | HTTP client |
| [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) | JSON serialization |
| [Firebase](https://firebase.google.com/) | Crashlytics, Analytics, Remote Config |

## 🌟 Star History

<div align="center">

[![Star History Chart](https://api.star-history.com/svg?repos=mgnegypt/eg-ai&type=Date)](https://star-history.com/#mgnegypt/eg-ai&Date)

</div>


<div align="center">

### License 📜

**This project is licensed under the [GNU Affero General Public License v3.0](LICENSE)** • **(AGPL-3.0)**


<sub>MGN AI's copyright is held by its developers and contributors only, and it is distributed under the same license. The original copyright notices are preserved</sub>

<sub>Built with ❤️ by MGN EG</sub>

</div>
