plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    compileOnly("com.android.tools.lint:lint-api:31.5.2") // 31.5.2 → matches AGP 8.5.2
    testImplementation("com.android.tools.lint:lint-tests:31.5.2")
}