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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":common_entities"))
    implementation(project(":utils"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.coreKtx)

    implementation(Dependencies.timber)

    implementation(Dependencies.threeTen)

    implementation(Dependencies.dagger)

    implementation(Dependencies.room)
    kapt(Dependencies.roomCompiler)

    implementation(Dependencies.moshi)
    kapt(Dependencies.moshiCodegen)
}
