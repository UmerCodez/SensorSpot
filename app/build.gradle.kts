import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.umercodez.sensorspot"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.github.umercodez.sensorspot"
        minSdk = 21
        targetSdk = 36
        versionCode = 12
        versionName = "1.6.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    hilt {
        enableAggregatingTask = false
    }

    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false

    }

    packaging {
        resources {
            excludes += "bundle.properties"
        }
    }

}

dependencies {

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.activity.compose)
    implementation(project(":ui"))
    implementation(project(":data"))
    implementation(libs.acra.mail)
    debugImplementation(libs.leakcanary.android)

}