plugins {
    id("com.android.library")
    id("kotlin-android")
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
    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.coreKtx)

    implementation(Dependencies.dagger)

    implementation(Dependencies.timber)

    implementation(Dependencies.exif)

    testImplementation(DependenciesTest.junit)
    androidTestImplementation(DependenciesTest.junitExt)
    androidTestImplementation(DependenciesTest.espresso)
}