# Gemeinsame Gradle-Konfiguration für Module

## Stufe 3: Wiederholungen zentralisieren

Das Projekt `gradle_02_shared` zeigt zunächst bewusst zwei weitgehend vollständige Modul-Builddateien. Im Projekt `gradle_03_modules` werden die gemeinsamen Teile in das Root-`build.gradle.kts` verschoben. Dadurch wird sichtbar, welche Konfiguration projektweit gleich ist und welche Einstellungen von der Modulart abhängen.

Die vorherigen Stufen bleiben separat lesbar:

- [`gradle_01_wizard`: Gradle-Basisprojekt](https://github.com/berndRog/gradle_01_wizard/blob/master/docs/Gradle.md)
- [`gradle_02_shared`: Android Library ergänzen](https://github.com/berndRog/gradle_02_shared/blob/master/docs/Gradle.md)

Das Projekt enthält weiterhin genau zwei Module:

```text
gradle_03_modules/
├── app/
│   └── build.gradle.kts
├── Shared/
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

Der Name `gradle_03_modules` steht in diesem Beispiel für die **zentrale Konfiguration der Module**. Zusätzliche Vorlesungsmodule werden in diesem Repository-Stand noch nicht registriert.

## Minimale Modul-Builddateien

Die Dateien `app/build.gradle.kts` und `Shared/build.gradle.kts` enthalten nur noch einen Hinweis:

```kotlin
// Android configuration, plugins, and dependencies are managed centrally in
// the build.gradle.kts file of the root project.
```

Das ist möglich, weil das Root-Skript seine Konfiguration mit `subprojects { ... }` auf alle direkten und indirekten Subprojekte anwendet.

## Plugins zentral anwenden

Im Root-`build.gradle.kts` bleiben die Plugin-Versionen wie zuvor über den Version Catalog verfügbar. Innerhalb von `subprojects` wird zunächst die Modulart bestimmt:

```kotlin
val isSharedLibrary = project.name.startsWith("Shared")

if (isSharedLibrary) {
   pluginManager.apply("com.android.library")
}
else {
   pluginManager.apply("com.android.application")
}
```

`Shared` wird damit als Android Library behandelt; alle anderen vorhandenen Module werden als Android Applications behandelt. Anschließend werden die gemeinsam benötigten Plugins angewendet:

```kotlin
pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
pluginManager.apply("com.google.devtools.ksp")
```

Im `plugins`-Block sind die Plugin-Versionen weiterhin mit `apply false` deklariert. `pluginManager.apply(...)` wendet die bereits bekannten Plugins dann auf das jeweilige Subprojekt an.

Die Erkennung über den Namen `Shared` ist für dieses kleine Lehrbeispiel kompakt und gut sichtbar. In einem größeren Produktivprojekt wären eigene Convention Plugins robuster, weil die Modulart dann nicht von einer Namensregel abhängt.

## Unterschiedliche Android-Erweiterungen

Das Android-Gradle-Plugin stellt je nach Modulart eine andere Erweiterung bereit. Deshalb importiert das Root-Skript:

```kotlin
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
```

Für `Shared` wird `LibraryExtension` konfiguriert:

```kotlin
extensions.configure<LibraryExtension> {
   namespace = "de.rogallab.mobile.shared"
   compileSdk = 37

   defaultConfig {
      minSdk = 26
      consumerProguardFiles("consumer-rules.pro")
   }
}
```

Für `app` wird `ApplicationExtension` verwendet:

```kotlin
extensions.configure<ApplicationExtension> {
   namespace = "de.rogallab.mobile"
   compileSdk = 37

   defaultConfig {
      applicationId = "de.rogallab.mobile"
      minSdk = 26
      targetSdk = 37
      versionCode = 1
      versionName = "1.0"
   }
}
```

Gemeinsame Werte wie `compileSdk`, `minSdk`, Java-Version, Testoptionen und Compose-Unterstützung stehen zwar in beiden Zweigen, unterscheiden sich aber teilweise in ihrer Android-DSL. Application-spezifische Werte wie `applicationId` und `targetSdk` gehören nicht in die Library-Konfiguration.

## Abhängigkeiten zentral deklarieren

Innerhalb eines normalen Modulskripts kann beispielsweise geschrieben werden:

```kotlin
implementation(libs.androidx.core.ktx)
```

Das Root-Skript konfiguriert andere Projekte dynamisch. Deshalb verwendet es die allgemeinere Form:

```kotlin
add("implementation", sharedLibs.androidx.core.ktx)
add("testImplementation", sharedLibs.junit)
add("ksp", sharedLibs.androidx.room3.compiler)
```

`sharedLibs` hält eine Referenz auf den Version Catalog des Root-Projekts:

```kotlin
val sharedLibs = libs
```

Die lokale Projektabhängigkeit wird nur für Application-Module ergänzt:

```kotlin
if (!isSharedLibrary) {
   add("implementation", project(":Shared"))
}
```

Damit gilt weiterhin die Abhängigkeitsrichtung `app` → `Shared`. `Shared` erhält keine Abhängigkeit auf sich selbst und keine Abhängigkeit auf ein Application-Modul.

Auch die Compose-BOM muss für jede relevante Konfiguration registriert werden:

```kotlin
val composeBom = platform(sharedLibs.androidx.compose.bom)
add("implementation", composeBom)
add("testImplementation", composeBom)
add("androidTestImplementation", composeBom)
```

## Was ändert sich gegenüber `shared`?

| Datei | Änderung |
|---|---|
| Root-`build.gradle.kts` | wendet Plugins an und konfiguriert Android sowie Dependencies für alle Subprojekte. |
| `app/build.gradle.kts` | enthält keine wiederholte Konfiguration mehr. |
| `Shared/build.gradle.kts` | enthält keine wiederholte Konfiguration mehr. |
| `settings.gradle.kts` | bleibt unverändert; registriert weiterhin `app` und `Shared`. |
| `libs.versions.toml` | bleibt die zentrale Quelle für Versionen und Koordinaten. |

## Bewertung des Ansatzes

Für das Lehrbeispiel zeigt `subprojects` sehr direkt, dass Gradle Module programmatisch konfigurieren kann. Neue gleichartige Module können dadurch mit wenig eigener Buildkonfiguration auskommen.

Die Zentralisierung hat aber auch einen Preis: Beim Öffnen einer Modul-Builddatei ist nicht mehr unmittelbar sichtbar, welche Plugins und Bibliotheken das Modul erhält. Außerdem bekommen derzeit alle Subprojekte nahezu den vollständigen Kurs-Stack, auch wenn ein einzelnes Modul nur einen Teil davon benötigt. Für ein großes Produktivprojekt wären typisierte Convention Plugins in einem separaten Build häufig die besser skalierende Lösung. Für die Vorlesung ist der gezeigte Zwischenschritt jedoch kompakt und macht das Grundprinzip ohne zusätzliche Plugin-Infrastruktur sichtbar.

## Vergleich der drei Stufen

| Stufe | Module | Ort der Hauptkonfiguration |
|---|---|---|
| `gradle_01_wizard` | `app` | `app/build.gradle.kts` |
| `gradle_02_shared` | `app`, `Shared` | jeweils eigene Modul-Builddatei |
| `gradle_03_modules` | `app`, `Shared` | gemeinsames Root-`build.gradle.kts` |

Zum Prüfen des Builds eignen sich:

```bash
./gradlew projects
./gradlew test
./gradlew :app:assembleDebug
```

Die drei Projekte dokumentieren damit nicht drei unabhängige Projekte, sondern drei nachvollziehbare Entwicklungsstände desselben Projekts.
