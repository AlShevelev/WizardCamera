plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = AndroidConfigData.compileSdk

    defaultConfig {
        minSdk = AndroidConfigData.minSdk
        targetSdk = AndroidConfigData.targetSdk
    }
}

dependencies {
    implementation(project(":core:common_entities"))
    implementation(project(":core:ui_kit:lib"))

    implementation(Dependencies.koin)
}
