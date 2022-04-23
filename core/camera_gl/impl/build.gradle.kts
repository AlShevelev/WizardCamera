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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":core:camera_gl:api"))

    implementation(project(":core:utils"))
    implementation(project(":core:common_entities"))

    implementation(project(":core:catalano:impl"))
    implementation(project(":core:catalano:api"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.coreKtx)

    implementation(Dependencies.cameraCore)
    implementation(Dependencies.camera2)
    implementation(Dependencies.cameraLifecycle)

    implementation(Dependencies.timber)
    implementation(Dependencies.koin)
}
