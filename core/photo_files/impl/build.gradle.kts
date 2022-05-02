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

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":core:photo_files:api"))
    implementation(project(":core:utils"))
    implementation(project(":core:build_info:api"))
    implementation(project(":core:bitmaps:api"))
    implementation(project(":core:common_entities"))
    implementation(project(":core:database:api"))

    implementation(Dependencies.kotlinxCoroutinesCore)
    implementation(Dependencies.timber)
    implementation(Dependencies.threeTen)

    implementation(Dependencies.koin)
}
