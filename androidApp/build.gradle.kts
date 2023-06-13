plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1"
}

kotlin {
    android()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))

                implementation("org.jetbrains.skiko:skiko-awt-runtime-linux-arm64:0.7.9")
            }
        }
    }
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "org.jetbrains.Imageviewer"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    packagingOptions {
        // pickFirst("META-INF/*")
        exclude("META-INF/DEPENDENCIES")
    }
}

secrets {
    defaultPropertiesFileName = "default.local.properties"
    propertiesFileName = "local.properties"
}
