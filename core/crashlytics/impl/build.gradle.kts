plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = AndroidConfigData.compileSdk

    defaultConfig {
        minSdk = AndroidConfigData.minSdk
        targetSdk = AndroidConfigData.targetSdk
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":core:crashlytics:api"))
    implementation(project(":core:utils"))
    implementation(project(":core:build_info:api"))

    implementation(Dependencies.dagger)
    implementation(Dependencies.firebaseCrashlytics)
    implementation(Dependencies.timber)
}
