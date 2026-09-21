// Central plugin repository configuration for the complete Gradle build.
pluginManagement {
   repositories {
      google {
         content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("com\\.google.*")
            includeGroupByRegex("androidx.*")
         }
      }

      mavenCentral()
      gradlePluginPortal()
   }
}

// Automatically provisions a matching Java toolchain when necessary.
plugins {
   id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// All modules resolve their external libraries from the same repositories.
dependencyResolutionManagement {

   // Module-specific repository declarations are rejected deliberately.
   repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

   repositories {
      google()
      mavenCentral()
   }
}

// Name of the complete Gradle project.
rootProject.name = "gradle_03_modules"

// The app remains the installable entry point of the project.
include(":app")

// Shared is an Android Library and cannot be started on its own.
include(":Shared")
