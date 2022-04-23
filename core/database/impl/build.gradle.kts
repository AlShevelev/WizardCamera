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
    implementation(project(":core:common_entities"))
    implementation(project(":core:utils"))
    implementation(project(":core:database:api"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.coreKtx)

    implementation(Dependencies.timber)

    implementation(Dependencies.threeTen)

    implementation(Dependencies.dagger)
    implementation(Dependencies.koin)

    implementation(Dependencies.room)
    kapt(Dependencies.roomCompiler)

    implementation(Dependencies.moshi)
    kapt(Dependencies.moshiCodegen)
}
