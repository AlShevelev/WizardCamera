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