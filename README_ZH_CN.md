<div align="center">
  <img src="docs/icon.png" alt="App 图标" width="100" />
  <h1>MGN AI</h1>

一个原生 Android LLM 聊天客户端，支持切换不同的供应商进行聊天 🤖💬，现已完整支持阿拉伯语 🌙

> MGN AI 是基于 [RikkaHub](https://github.com/rikkahub/rikkahub) 的独立项目，
> 应用 ID（`com.mgn.ai`）、完整阿拉伯语本地化、应用内语言切换、MGN 主题、
> 通过本仓库 [Releases](https://github.com/mgnegypt/eg-ai/releases) 的应用内更新，
> 以及 Firebase 远程捐赠配置。
> 遵循相同的 [AGPL-3.0](LICENSE) 协议发布。

[English](README.md) | [繁體中文](README_ZH_TW.md) | 简体中文

</div>

<div align="center">
  <img src="docs/img/chat.png" alt="Chat Interface" width="150" />
  <img src="docs/img/desktop.png" alt="Models Picker" width="450" />
</div>


## 🚀 下载

🔗 [从 GitHub Releases 下载](https://github.com/mgnegypt/eg-ai/releases)（推荐）

每个版本提供三个 APK：`mgn-ai-v<VERSION>-arm64-v8a-release.apk`（大多数手机）、
`mgn-ai-v<VERSION>-x86_64-release.apk`、`mgn-ai-v<VERSION>-universal-release.apk`。
包名 `com.mgn.ai`，可与其他客户端共存。

## 📇 联系开发者

设置 → 关于 → 联系开发者：Facebook、WhatsApp、Telegram。

## 💝 捐赠

设置 → 捐赠。捐赠地址通过 Firebase Remote Config（键 `donations_config`）远程配置，
正式地址公布前应用内显示“即将推出”。

## ✨ 功能特色

- 🎨 现代化安卓APP设计（Material You / 预测性返回）和 🌙 暗色模式
- 📦 工作区：基于 proot 的 Linux 智能体环境
- 🖥️ Web多端访问支持
- 🛠️ MCP 支持
- 🔄 多种类型的供应商支持，自定义 API / URL / 模型（目前支持 OpenAI、Google、Anthropic）
- 🖼️ 多模态输入支持
- 📝 Markdown 渲染（支持代码高亮、数学公式、表格、Mermaid）
- 🔍 搜索功能（Exa、Tavily、Zhipu、LinkUp、Brave、Perplexity、..）
- 🧩 Prompt 变量（模型名称、时间等）
- 🤳 二维码导出和导入提供商
- 🤖 智能体自定义
- 🧠 类ChatGPT记忆功能
- 📝 AI翻译
- 🌐 自定义HTTP请求头和请求体

## ✨ 贡献

本项目使用[Android Studio](https://developer.android.com/studio)开发，欢迎提交PR

技术栈文档:

- [Kotlin](https://kotlinlang.org/) (开发语言)
- [Koin](https://insert-koin.io/) (依赖注入)
- [Jetpack Compose](https://developer.android.com/jetpack/compose) (UI 框架)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore?hl=zh-cn#preferences-datastore) (
  偏好数据存储)
- [Room](https://developer.android.com/training/data-storage/room) (数据库)
- [Coil](https://coil-kt.github.io/coil/) (图片加载)
- [Material You](https://m3.material.io/) (UI 设计)
- [Navigation 3](https://developer.android.com/guide/navigation/navigation-3) (导航)
- [Okhttp](https://square.github.io/okhttp/) (HTTP 客户端)
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) (Json序列化)

> [!TIP]
> 你需要在 `app` 文件夹下添加 `google-services.json` 文件才能构建应用。

> [!IMPORTANT]  
> 以下PR将被拒绝：
> 1. 添加新语言，因为添加新语言会增加后续本地化的工作量
> 2. 添加新功能，这个项目是有态度的
> 3. AI生成的大规模重构和更改

## ⭐ Star History

如果喜欢这个项目，请给个Star ⭐

<a href="https://www.star-history.com/?type=date&repos=mgnegypt%2Feg-ai">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/chart?repos=mgnegypt/eg-ai&type=date&theme=dark&legend=top-left&sealed_token=qSytWeq7LkzQQViTjK0MYlvvA_qkfuwjOxOqgbRpLUZZwok5rO6LXhpVL7Mq-q3o89BfKpzE7g66BCy18H6eiqTsD8czD0J-HejLqmHy-npcvCTHu11wZw" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/chart?repos=mgnegypt/eg-ai&type=date&legend=top-left&sealed_token=qSytWeq7LkzQQViTjK0MYlvvA_qkfuwjOxOqgbRpLUZZwok5rO6LXhpVL7Mq-q3o89BfKpzE7g66BCy18H6eiqTsD8czD0J-HejLqmHy-npcvCTHu11wZw" />
   <img alt="Star History Chart" src="https://api.star-history.com/chart?repos=mgnegypt/eg-ai&type=date&legend=top-left&sealed_token=qSytWeq7LkzQQViTjK0MYlvvA_qkfuwjOxOqgbRpLUZZwok5rO6LXhpVL7Mq-q3o89BfKpzE7g66BCy18H6eiqTsD8czD0J-HejLqmHy-npcvCTHu11wZw" />
 </picture>
</a>

## 📄 许可证

本项目基于 [GNU Affero General Public License v3.0](LICENSE) (AGPL-3.0) 开源。

MGN AI 是 [RikkaHub](https://github.com/rikkahub/rikkahub) 的 fork 版本，遵循相同协议发布，原始版权声明予以保留。
