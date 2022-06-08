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
    implementation(project(":core:common_entities"))
    implementation(project(":core:utils"))
    implementation(project(":core:database:api"))
    implementation(project(":core:ui_kit:lib"))

    implementation(project(":feature:filters_facade:api"))

    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.coreKtx)

    implementation(Dependencies.timber)

    implementation(Dependencies.threeTen)

    implementation(Dependencies.koin)
}
