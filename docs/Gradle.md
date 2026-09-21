# Shared Gradle Configuration for Modules

[Deutsche Version](Gradle_ger.md)

## Stage 3: Centralizing repeated configuration

The `gradle_02_shared` project deliberately begins with two largely complete module build files. In `gradle_03_modules`, the common parts are moved to the root `build.gradle.kts`. This makes it clear which configuration is the same throughout the project and which settings depend on the module type.

The previous stages remain available separately:

- [`gradle_01_wizard`: basic Gradle project](https://github.com/berndRog/gradle_01_wizard/blob/master/docs/Gradle.md)
- [`gradle_02_shared`: adding an Android library](https://github.com/berndRog/gradle_02_shared/blob/master/docs/Gradle.md)

The project still contains exactly two modules:

```text
gradle_03_modules/
├── app/
│   └── build.gradle.kts
├── Shared/
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

In this example, the name `gradle_03_modules` refers to the **centralized configuration of the modules**. No additional course modules are registered at this stage.

## Minimal module build files

The files `app/build.gradle.kts` and `Shared/build.gradle.kts` now contain only a note:

```kotlin
// Android configuration, plugins, and dependencies are managed centrally in
// the build.gradle.kts file of the root project.
```

This is possible because the root script applies its configuration to all direct and indirect subprojects using `subprojects { ... }`.

## Applying plugins centrally

As before, the plugin versions remain available in the root `build.gradle.kts` through the version catalog. Inside `subprojects`, the module type is determined first:

```kotlin
val isSharedLibrary = project.name.startsWith("Shared")

if (isSharedLibrary) {
   pluginManager.apply("com.android.library")
}
else {
   pluginManager.apply("com.android.application")
}
```

This treats `Shared` as an Android library and all other existing modules as Android applications. The commonly required plugins are then applied:

```kotlin
pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
pluginManager.apply("com.google.devtools.ksp")
```

The plugin versions are still declared with `apply false` in the `plugins` block. `pluginManager.apply(...)` then applies these already known plugins to each subproject.

Detecting `Shared` by name is compact and easy to see in this small teaching example. In a larger production project, custom convention plugins would be more robust because the module type would not depend on a naming rule.

## Different Android extensions

The Android Gradle Plugin provides a different extension for each module type. The root script therefore imports:

```kotlin
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
```

`LibraryExtension` is configured for `Shared`:

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

`ApplicationExtension` is used for `app`:

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

Common values such as `compileSdk`, `minSdk`, the Java version, test options, and Compose support appear in both branches, although their Android DSLs differ in some respects. Application-specific values such as `applicationId` and `targetSdk` do not belong in the library configuration.

## Declaring dependencies centrally

Inside a regular module script, one can write, for example:

```kotlin
implementation(libs.androidx.core.ktx)
```

The root script configures other projects dynamically. It therefore uses the more general form:

```kotlin
add("implementation", sharedLibs.androidx.core.ktx)
add("testImplementation", sharedLibs.junit)
add("ksp", sharedLibs.androidx.room3.compiler)
```

`sharedLibs` holds a reference to the root project's version catalog:

```kotlin
val sharedLibs = libs
```

The local project dependency is added only to application modules:

```kotlin
if (!isSharedLibrary) {
   add("implementation", project(":Shared"))
}
```

The dependency direction therefore remains `app` → `Shared`. `Shared` receives neither a dependency on itself nor a dependency on an application module.

The Compose BOM must also be registered for every relevant configuration:

```kotlin
val composeBom = platform(sharedLibs.androidx.compose.bom)
add("implementation", composeBom)
add("testImplementation", composeBom)
add("androidTestImplementation", composeBom)
```

## What changes compared with `shared`?

| File | Change |
|---|---|
| Root `build.gradle.kts` | applies plugins and configures Android and dependencies for all subprojects. |
| `app/build.gradle.kts` | no longer contains repeated configuration. |
| `Shared/build.gradle.kts` | no longer contains repeated configuration. |
| `settings.gradle.kts` | remains unchanged and still registers `app` and `Shared`. |
| `libs.versions.toml` | remains the central source for versions and coordinates. |

## Assessment of the approach

For this teaching example, `subprojects` demonstrates very directly that Gradle can configure modules programmatically. New modules of the same type can therefore require very little configuration in their own build files.

Centralization also has a cost: when opening a module build file, it is no longer immediately apparent which plugins and libraries the module receives. In addition, all subprojects currently receive nearly the complete course stack, even if an individual module only needs part of it. For a large production project, typed convention plugins in a separate build are often the more scalable solution. For the course, however, this intermediate stage is compact and demonstrates the basic principle without additional plugin infrastructure.

## Comparison of the three stages

| Stage | Modules | Location of the main configuration |
|---|---|---|
| `gradle_01_wizard` | `app` | `app/build.gradle.kts` |
| `gradle_02_shared` | `app`, `Shared` | separate build file for each module |
| `gradle_03_modules` | `app`, `Shared` | shared root `build.gradle.kts` |

Useful commands for checking the build are:

```bash
./gradlew projects
./gradlew test
./gradlew :app:assembleDebug
```

The three projects therefore document three traceable development stages of the same project, not three unrelated projects.
