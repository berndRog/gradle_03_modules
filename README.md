# Gradle Example: Centralized Module Configuration

[Deutsche Version](README_ger.md)

This repository is the third Gradle stage. It still contains the modules `app` and `Shared`, but moves their common plugins, Android settings, and dependencies into the root `build.gradle.kts`. The two module build files therefore remain minimal.

The changes are explained in [docs/Gradle.md](docs/Gradle.md). A [German version](docs/Gradle_ger.md) is also available.

## The three stages

| Project | Contents | Focus |
|---|---|---|
| [`gradle_01_wizard`](https://github.com/berndRog/gradle_01_wizard) | one `app` module | Gradle files of a wizard project |
| [`gradle_02_shared`](https://github.com/berndRog/gradle_02_shared) | `app` and `Shared` | application and library modules |
| [`gradle_03_modules`](https://github.com/berndRog/gradle_03_modules) | centralized configuration for `app` and `Shared` | extracting common settings from module build files |

The name `gradle_03_modules` refers to the **configuration of the modules**. This stage does not add any further course modules.

The Gradle guide compares the changes with `gradle_02_shared` file by file.
