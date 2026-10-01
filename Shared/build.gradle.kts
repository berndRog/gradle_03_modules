plugins {
   // Builds an Android Archive (AAR), not an installable APK.
   alias(libs.plugins.android.library)

   // Shared may also contain reusable Compose UI components.
   alias(libs.plugins.kotlin.compose)

   // Makes kotlinx.serialization available to shared network models.
   alias(libs.plugins.kotlin.serialization)

   // Generates Room database implementations for shared persistence code.
   alias(libs.plugins.google.devtools.ksp)
}

android {
   // Every Android module requires its own unique namespace.
   namespace = "de.rogallab.mobile.shared"

   // The library is compiled against the same SDK as the application.
   compileSdk {
      version = release(37) { minorApiLevel = 1 }
   }

   defaultConfig {
      // The library must support every device supported by the app.
      minSdk = 26

      // A library has no applicationId, versionCode, or versionName because it
      // is packaged into an app instead of being installed independently.
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

      // Consumer rules are applied later to every app using this library.
      consumerProguardFiles("consumer-rules.pro")
   }

   testOptions {
      // Disabling animations makes instrumented UI tests more deterministic.
      animationsDisabled = true

      // Robolectric tests need access to Android resources on the local JVM.
      unitTests.isIncludeAndroidResources = true
   }

   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_21
      targetCompatibility = JavaVersion.VERSION_21
   }

   buildFeatures {
      // Required when reusable Composables are placed in Shared.
      compose = true
   }
}

dependencies {
   // Dependencies are module-local in Gradle. Shared therefore declares the
   // libraries needed by code that may gradually be moved out of app.

   // -------------------------------------------------------------------------
   // Kotlin and Android core
   // -------------------------------------------------------------------------
   implementation(libs.androidx.core.ktx)
   implementation(libs.kotlinx.coroutines.core)
   implementation(libs.kotlinx.coroutines.android)
   implementation(libs.kotlinx.datetime)
   implementation(libs.kotlinx.serialization.json)

   // -------------------------------------------------------------------------
   // Activity and Jetpack Compose
   // -------------------------------------------------------------------------
   implementation(platform(libs.androidx.compose.bom))
   testImplementation(platform(libs.androidx.compose.bom))
   androidTestImplementation(platform(libs.androidx.compose.bom))

   implementation(libs.androidx.activity.compose)
   implementation(libs.androidx.compose.foundation.layout)
   implementation(libs.androidx.ui)
   implementation(libs.androidx.ui.graphics)
   implementation(libs.androidx.ui.tooling.preview)
   implementation(libs.androidx.animation)
   implementation(libs.androidx.material3)
   implementation(libs.androidx.material.icons.extended)

   // -------------------------------------------------------------------------
   // Lifecycle, ViewModel, and Navigation 3
   // -------------------------------------------------------------------------
   implementation(libs.androidx.lifecycle.runtime.ktx)
   implementation(libs.androidx.lifecycle.runtime.compose)
   implementation(libs.androidx.lifecycle.viewmodel.compose)
   implementation(libs.androidx.lifecycle.viewmodel.navigation3)
   implementation(libs.androidx.navigation3.runtime)
   implementation(libs.androidx.navigation3.ui)

   // -------------------------------------------------------------------------
   // Room 3 and SQLite
   // -------------------------------------------------------------------------
   implementation(libs.androidx.room3.runtime)
   implementation(libs.androidx.sqlite.bundled)
   ksp(libs.androidx.room3.compiler)

   // -------------------------------------------------------------------------
   // Images and media
   // -------------------------------------------------------------------------
   implementation(libs.coil.compose)
   implementation(libs.coil.network.okhttp)
   implementation(libs.androidx.media3.exoplayer)

   // -------------------------------------------------------------------------
   // Dependency injection with Koin
   // -------------------------------------------------------------------------
   implementation(libs.koin.core)
   implementation(libs.koin.android)
   implementation(libs.koin.androidx.compose)

   // -------------------------------------------------------------------------
   // Networking with Retrofit
   // -------------------------------------------------------------------------
   implementation(libs.gson.json)
   implementation(libs.retrofit2.core)
   implementation(libs.retrofit2.gson)
   implementation(libs.retrofit2.kotlinx.serialization)
   implementation(libs.retrofit2.logging)

   // -------------------------------------------------------------------------
   // Google Play Services
   // -------------------------------------------------------------------------
   implementation(libs.gplay.location)

   // -------------------------------------------------------------------------
   // Local JVM tests
   // -------------------------------------------------------------------------
   testImplementation(libs.junit)
   testImplementation(libs.androidx.test.core)
   testImplementation(libs.androidx.test.core.ktx)
   testImplementation(libs.koin.test)
   testImplementation(libs.koin.test.junit4)
   testImplementation(libs.kotlinx.coroutines.test)
   testImplementation(libs.turbine.test)
   testImplementation(libs.robolectric.test)

   // -------------------------------------------------------------------------
   // Instrumented Android and Compose tests
   // -------------------------------------------------------------------------
   androidTestImplementation(libs.kotlinx.coroutines.test)
   androidTestImplementation(libs.androidx.test.core)
   androidTestImplementation(libs.androidx.test.core.ktx)
   androidTestImplementation(libs.androidx.test.ext.junit)
   androidTestImplementation(libs.androidx.test.ext.junit.ktx)
   androidTestImplementation(libs.androidx.test.ext.truth)
   androidTestImplementation(libs.androidx.test.runner)
   androidTestImplementation(libs.androidx.ui.test.junit4)
   androidTestImplementation(libs.androidx.test.espresso.core)
   androidTestImplementation(libs.koin.test)
   androidTestImplementation(libs.koin.test.junit4)
   androidTestImplementation(libs.koin.androidx.compose)
   androidTestImplementation(libs.mockito.core)
   androidTestImplementation(libs.mockito.android)
   androidTestImplementation(libs.mockito.kotlin)

   // -------------------------------------------------------------------------
   // Debug-only tooling
   // -------------------------------------------------------------------------
   debugImplementation(libs.androidx.ui.tooling)
   debugImplementation(libs.androidx.ui.test.manifest)
}
