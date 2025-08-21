plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.kin.easynotes"
    compileSdk = 35
    flavorDimensions += "default"

    productFlavors {
        create("default") {
            dimension = "default"
            applicationId = "com.kin.easynotes"
            versionNameSuffix = "-default"
        }

        create("accrescent") {
            dimension = "default"
            applicationId = "me.easyapps.easynotes"
            versionNameSuffix = "-easyapps"
        }
    }

    defaultConfig {
        applicationId = "com.kin.easynotes"
        minSdk = 26
        targetSdk = 35
        versionCode = 11
        versionName = "1.4"
        vectorDrawables {
            useSupportLibrary = true
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // https://developer.android.com/guide/topics/resources/app-languages#gradle-config
        resourceConfigurations.plus(
            listOf("en", "ar", "de", "es", "fa", "fil", "fr", "hi", "it", "ja", "ru", "sk", "tr", "da", "nl", "pl", "tr", "uk", "vi", "ota", "pt-rBR", "sr", "zh-rCN")
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    buildToolsVersion = "34.0.0"

// ✅ Explicitly tell Gradle where your source code and test code live.
// This prevents mistakes like mixing test code with main code.
// Also ensures correct Gradle tasks run with the right folders.
//
// Gradle has defaults (src/main/java, src/test/java, src/androidTest/java),
// but if you ever override or customize source sets, you must tell Gradle explicitly.
//
// Using forward slashes ("/") works on Windows, macOS, and Linux — no need for double backslashes.
    sourceSets {
        // Main source set → Your production app code
        // Compiled into APK / App Bundle
        getByName("main") {
            java.srcDirs("src/main/java")
        }

        // Unit test source set → Local JVM tests
        // Runs on your computer without an Android device
        // Uses dependencies from testImplementation
        // Not packaged into the APK
        getByName("test") {
            java.srcDirs("src/test/java")
        }

        // Instrumentation test source set → Runs on Android device or emulator
        // Uses dependencies from androidTestImplementation
        // Not packaged into the APK (only for testing)
        getByName("androidTest") {
            java.srcDirs("src/androidTest/java")
        }
    }

}

dependencies {
    implementation(libs.androidx.biometric.ktx)
    implementation(libs.androidx.glance)
    implementation(libs.coil.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.glance.appwidget)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compile)
    implementation(libs.hilt.android)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // Optional -- Mockito framework
    testImplementation(libs.mockito.core)
    // Optional -- mockito-kotlin
    testImplementation(libs.mockito.kotlin)
    // Optional -- Mockk framework
    testImplementation(libs.mockk)

    //Robolectric for simulate android classes in test source set
    testImplementation(libs.robolectric)

    //For Instrumented Test (Ui Test)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)

    //Custom lint
    lintChecks(project(":lint-rules"))
    debugImplementation(project(":lint-rules"))
}