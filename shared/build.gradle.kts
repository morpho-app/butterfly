plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kspPlugin)
    alias(libs.plugins.kotlinxAbiPlugin)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }




    sourceSets {
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
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

        }
    }

    task("testClasses")
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