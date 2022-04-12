plugins {
    id("com.android.library")
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