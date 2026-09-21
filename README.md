# Gradle-Beispiel: zentrale Modulkonfiguration

Dieses Repository ist die dritte Gradle-Stufe. Er besitzt weiterhin die Module `app` und `Shared`, zieht deren gemeinsame Plugins, Android-Einstellungen und Abhängigkeiten aber in das Root-`build.gradle.kts`. Die beiden Modul-Builddateien bleiben dadurch minimal.

Die Änderungen werden in [docs/Gradle.md](docs/Gradle.md) beschrieben.

## Die drei Stufen

| Projekt | Inhalt | Schwerpunkt |
|---|---|---|
| [`gradle_01_wizard`](https://github.com/berndRog/gradle_01_wizard) | ein `app`-Modul | Gradle-Dateien eines Wizard-Projekts |
| [`gradle_02_shared`](https://github.com/berndRog/gradle_02_shared) | `app` und `Shared` | Application- und Library-Modul |
| [`gradle_03_modules`](https://github.com/berndRog/gradle_03_modules) | zentrale Konfiguration für `app` und `Shared` | gemeinsame Einstellungen aus Moduldateien herausziehen |

Der Projektname `gradle_03_modules` bedeutet hier **Konfiguration der Module**. Dieser Stand fügt keine weiteren A-Module hinzu.

Die Änderungen gegenüber `gradle_02_shared` werden in der Gradle-Beschreibung dateibezogen gegenübergestellt.
