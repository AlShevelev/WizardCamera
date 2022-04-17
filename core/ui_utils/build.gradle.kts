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

    dataBinding {
        isEnabled = true
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(Dependencies.appcompat)

    implementation(Dependencies.glide)
    kapt(Dependencies.glideCompiler)

    implementation(Dependencies.dagger)

    implementation(Dependencies.timber)
}
