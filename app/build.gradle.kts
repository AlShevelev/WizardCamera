plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
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

            setProguardFiles(
                mutableListOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    File("proguard-rules.pro")
                )
            )
        }
    }

    dataBinding {
        isEnabled = true
    }

    buildFeatures {
        viewBinding = true
    }

    flavorDimensions("wizardCamera-config-type")

    productFlavors {
        create("prod") {
            dimension = "wizardCamera-config-type"
            buildConfigField("Boolean", "CRASH_REPORTS_ENABLED", "true")
        }

        create("dev") {
            dimension = "wizardCamera-config-type"
            buildConfigField("Boolean", "CRASH_REPORTS_ENABLED", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":core:camera_gl"))
    implementation(project(":core:utils"))
    implementation(project(":core:common_entities"))

    implementation(project(":core:bitmaps:api"))
    implementation(project(":core:bitmaps:impl"))

    implementation(project(":core:database:impl"))
    implementation(project(":core:database:api"))

    implementation(project(":core:catalano:impl"))
    implementation(project(":core:catalano:api"))

    implementation(project(":core:crashlytics:impl"))
    implementation(project(":core:crashlytics:api"))

    implementation(project(":core:build_info:api"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Dependencies.appcompat)

    implementation(Dependencies.coreKtx)

    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.lifecycleExtensions)
    implementation(Dependencies.lifecycleViewmodelKtx)
    kapt(Dependencies.lifecycleCompiler)

    implementation(Dependencies.navigationFragmentKtx)
    implementation(Dependencies.navigationUiKtx)

    implementation(Dependencies.cameraCore)
    implementation(Dependencies.camera2)
    implementation(Dependencies.cameraLifecycle)

    implementation(Dependencies.viewPager)
    implementation(Dependencies.constraintLayout)

    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.kotlinReflect)
    implementation(Dependencies.kotlinxCoroutinesCore)
    implementation(Dependencies.kotlinxCoroutinesAndroid)

    implementation(Dependencies.timber)
    implementation(Dependencies.threeTen)

    implementation(Dependencies.permissionsDispatcher)
    kapt(Dependencies.permissionsDispatcherProcessor)

    implementation(Dependencies.exif)

    implementation(Dependencies.dagger)
    kapt(Dependencies.daggerCompiler)
    kapt(Dependencies.daggerAndroid)

    implementation(Dependencies.glide)
    kapt(Dependencies.glideCompiler)

    implementation(Dependencies.moshi)
    kapt(Dependencies.moshiCodegen)
}

repositories {
    google()
    mavenCentral()
}
