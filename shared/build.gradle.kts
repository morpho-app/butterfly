import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)

    alias(libs.plugins.jetbrainsCompose) // testing
    alias(libs.plugins.compose.compiler) // testing

    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kspPlugin)
    alias(libs.plugins.kotlinxAbiPlugin)
    id("maven-publish")
    id("kotlin-parcelize")
}

group = "com.morpho"
version = "0.1"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11

        }
    }

    jvm("desktop")

    // iOS targets stubbed out for now
    // Can't build them on my desktop anyway
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }//*/


    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(libs.ktor.logging)
            implementation(libs.slf4j.api)
            implementation(kotlin("reflect"))
            implementation(libs.okio)
            implementation(libs.ktor.cio)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.immutable)
            implementation(libs.kotlinx.serialization.cbor)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.contentnegotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.websockets)
            implementation(libs.ktor.client.resources)
            implementation(libs.ktor.client.auth)
            implementation(libs.kotlin.stdlib)
            implementation(libs.kstore)
            implementation(libs.kstore.file)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.core.coroutines)
            implementation(libs.koin.annotations)

            implementation(compose.materialIconsExtended)

            implementation("com.russhwolf:multiplatform-settings:1.2.0")
            implementation("com.russhwolf:multiplatform-settings-serialization:1.2.0")
            implementation("com.russhwolf:multiplatform-settings-coroutines:1.2.0")
            implementation("com.russhwolf:multiplatform-settings-datastore:1.2.0")
            implementation("com.russhwolf:multiplatform-settings-no-arg:1.2.0")
            implementation("androidx.datastore:datastore-preferences-core:1.1.1")
            implementation("androidx.datastore:datastore-core:1.1.1")

            api(libs.logging)
            api("dev.icerock.moko:parcelize:0.9.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

        }
        desktopMain.dependencies {
            implementation(libs.appdirs)
            implementation(libs.apache.commons)
            implementation(libs.logback.core)
            implementation(libs.logback.classic)
            implementation(libs.jwt)
        }

        nativeMain.dependencies {

        }
        androidMain.dependencies {
            // Koin dependency injection
            implementation(libs.koin.android)
            // Java Compatibility
            implementation(libs.koin.android.compat)

            implementation(libs.jwt)
        }
    }

    task("testClasses")
}

publishing {
    repositories {
        maven {
            name = "Butterfly"
            description = "Multiplatform Kotlin library for the AT Protocol and Bluesky"
            
            url = uri("https://maven.pkg.github.com/morpho-app/butterfly")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }

    publications {
        register<MavenPublication>("gpr") {
            from(components["kotlin"])
        }
    }
}

android {
    namespace = "com.morpho.butterfly"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler) // Run KSP on [commonMain] code
    //add("kspAndroid", libs.koin.ksp.compiler)
    //add("kspIosX64", libs.koin.ksp.compiler)
    //add("kspIosArm64", libs.koin.ksp.compiler)
    //add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
}