# UBAA (智慧北航 Remake)

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-blue.svg?style=flat&logo=kotlin)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.10.3-blueviolet.svg?style=flat&logo=jetpack-compose)
![Ktor](https://img.shields.io/badge/Ktor-3.4.1-orange.svg?style=flat&logo=ktor)
![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS%20%7C%20Desktop%20%7C%20Web-lightgrey.svg?style=flat)

UBAA 是一个 Kotlin Multiplatform 单仓库，包含 Compose Multiplatform 客户端、Ktor 服务端网关、共享契约层，以及用于承载 Compose Framework 的 iOS Xcode 壳工程。

[立刻下载](https://github.com/BUAASubnet/UBAA/releases)  
[网页版](https://app.buaa.team)

服务端负责聚合和标准化北航多个系统的数据，并向 Android、iOS、Desktop、Web JS、Web Wasm 客户端暴露统一的 `/api/v1/*` 接口。当前已接入的主要功能包括课表、考试、BYKC、课堂签到、研讨室预约、空教室、自动评教和 SPOC 作业。

## 当前技术栈

| 组件 | 当前版本 / 形态 |
| :--- | :--- |
| Kotlin | `2.3.20` |
| JDK | `21` |
| Compose Multiplatform | `1.10.3` |
| Ktor | `3.4.1` |
| 客户端平台 | Android、iOS、Desktop、Web JS、Web Wasm |
| 服务端运行时 | Ktor + Netty + Redis 会话持久化 |

## 仓库结构

| 路径 | 角色 |
| :--- | :--- |
| `androidApp` | Android 应用模块，负责打包、签名和 `MainActivity` |
| `composeApp` | 主跨平台 UI 模块，包含页面、导航、ViewModel 和平台入口 |
| `shared` | 客户端与服务端共享 DTO、API 层、存储助手与平台抽象 |
| `server` | Ktor 网关，负责 JWT、会话恢复、上游系统适配与统一 API |
| `iosApp` | Xcode 壳工程，用于嵌入 `ComposeApp` Framework |
| `docs` | 面向维护者的架构、契约、开发和部署文档 |

更完整的文档导航见 [docs/index.md](./docs/index.md)。

## 快速开始

### 前置条件

- JDK 21
- IntelliJ IDEA 或 Android Studio
- Xcode 16+（仅 iOS 开发需要）

### 1. 克隆与配置

```bash
git clone <your-repo-url>
cd UBAA
cp .env.sample .env
```

如需本地后端地址，请在 `.env` 中保留或修改 `API_ENDPOINT=http://localhost:5432`。

### 2. 启动后端

```bash
./gradlew :server:run
```

服务端默认监听 `http://0.0.0.0:5432`。

### 3. 启动客户端

| 平台 | 命令 / 入口 | 说明 |
| :--- | :--- | :--- |
| Android | `./gradlew :androidApp:installDebug` | 安装到真机或模拟器 |
| Desktop | `./gradlew :composeApp:run` | 本地桌面调试的主入口 |
| Web | `./gradlew :composeApp:wasmJsBrowserDevelopmentRun` | 启动 Wasm 开发服务器 |
| iOS | 打开 `iosApp/iosApp.xcodeproj` | 由 Xcode 构建并运行壳工程 |

在 Windows 上请把 `./gradlew` 替换为 `gradlew.bat`。

## 配置说明

项目主要通过根目录 `.env` 管理运行时和构建时配置。

| 变量 | 用途 | 默认值 / 说明 |
| :--- | :--- | :--- |
| `API_ENDPOINT` | 客户端构建时注入的后端地址 | `.env.sample` 默认 `http://localhost:5432` |
| `SERVER_PORT` | 服务端监听端口 | `5432` |
| `SERVER_BIND_HOST` | 服务端绑定地址 | `0.0.0.0` |
| `JWT_SECRET` | JWT 签名密钥 | 生产环境必须显式设置 |
| `USE_VPN` | 是否启用 WebVPN URL 转写 | `false` |
| `ACCESS_TOKEN_TTL_MINUTES` | access token 有效期 | `30` |
| `REFRESH_TOKEN_TTL_DAYS` | refresh token 有效期 | `7` |
| `SESSION_TTL_DAYS` | Redis 会话与 Cookie 有效期 | `7` |
| `REDIS_URI` | Redis 连接地址 | `redis://localhost:6379` |

`API_ENDPOINT` 是构建时配置，不是运行时热切换开关。`shared/build.gradle.kts` 会优先读取根目录 `.env`，其次读取进程环境变量 `API_ENDPOINT`，都没有时回退到 `https://ubaa.mofrp.top`。

## 常用开发命令

| 任务 | 命令 |
| :--- | :--- |
| 启动服务端 | `./gradlew :server:run` |
| 启动桌面端 | `./gradlew :composeApp:run` |
| 构建 Android Debug APK | `./gradlew :androidApp:assembleDebug` |
| 构建服务端 JAR | `./gradlew :server:buildFatJar` |
| 后端测试 | `./gradlew :server:test` |
| shared JVM 测试 | `./gradlew :shared:jvmTest` |
| Compose JVM 测试 | `./gradlew :composeApp:jvmTest` |
| 聚合覆盖率报告 | `./gradlew koverHtmlReport` |
| Android Lint | `./gradlew lint` |

推荐的本地较完整验证命令：

```bash
./gradlew :server:test :shared:jvmTest :composeApp:jvmTest
```

补充说明：
- `./gradlew test` 当前会覆盖 `:androidApp:test` 与 `:server:test`，不会自动代替 `:shared:jvmTest` 和 `:composeApp:jvmTest`
- `./gradlew lint` 当前实际对应 `:androidApp:lint`

## 运行与发布

### 服务端观测

- Prometheus 指标端点：`GET /metrics`
- 默认日志文件：`logs/server.log`
- 当前 `/metrics` 会暴露活跃会话数、预登录会话数、签到客户端缓存数、BYKC 客户端缓存数、研讨室客户端缓存数、SPOC 客户端缓存数等指标

### GitHub Release 当前产物

- Android APK
- Linux Deb
- Server fat JAR
- Web Wasm zip
- Web JS zip
- iOS Framework zip
- Windows exe

如果后端地址变化，需要重新构建客户端产物，因为各端都会在构建时注入 `API_ENDPOINT`。

## 架构摘要

- `composeApp` 依赖 `shared`，承载绝大多数 UI、导航、页面和 ViewModel 逻辑
- `server` 依赖 `shared`，通过 JWT + Redis-backed 会话模型访问并聚合上游系统
- `androidApp` 仅负责 Android 应用壳层，`iosApp` 仅负责 Xcode 壳工程
- DTO 和客户端 API 契约统一位于 `shared/src/commonMain`

当前路由域以 `server/src/main/kotlin/cn/edu/ubaa/Application.kt` 注册结果为准，包括 `auth`、`user`、`schedule`、`exam`、`bykc`、`signin`、`classroom`、`cgyy`、`evaluation`、`spoc`。
