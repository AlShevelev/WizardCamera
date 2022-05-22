buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(DependenciesGradle.gradle)
        classpath(DependenciesGradle.kotlinGradlePlugin)

        classpath(DependenciesGradle.googleServices)
        classpath(DependenciesGradle.firebaseCrashlytics)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}