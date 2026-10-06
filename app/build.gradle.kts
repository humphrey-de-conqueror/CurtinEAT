plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    //Google Services Gradle Plugin
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.curtineat"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.curtineat"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.icons.extended)

    // Lifecycle / MVVM
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    // Retrofit / Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)

    // Room / SQLite
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.19.0"))

    // Firebase
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    // to use .await() with Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")

    // OkHttp - needed for MultipartBody.Part
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //Authentication
    implementation("com.google.firebase:firebase-auth")

    // Testing
    testImplementation(libs.junit)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}