plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = AndroidConfigData.compileSdk

    defaultConfig {
        applicationId = AndroidConfigData.applicationId

        minSdk = AndroidConfigData.minSdk
        targetSdk = AndroidConfigData.targetSdk

        versionCode = AndroidConfigData.versionCode
        versionName = AndroidConfigData.versionName
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core:ui_kit:lib"))

    implementation(Dependencies.appcompat)

    implementation(Dependencies.coreKtx)

    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.lifecycleExtensions)
    implementation(Dependencies.lifecycleViewmodelKtx)
    kapt(Dependencies.lifecycleCompiler)

    implementation(Dependencies.viewPager)
    implementation(Dependencies.constraintLayout)

    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.kotlinReflect)
    implementation(Dependencies.kotlinxCoroutinesCore)
    implementation(Dependencies.kotlinxCoroutinesAndroid)

    implementation(Dependencies.timber)
    implementation(Dependencies.threeTen)

    implementation(Dependencies.glide)
    kapt(Dependencies.glideCompiler)
}