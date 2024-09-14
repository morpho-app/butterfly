
plugins {
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinxSerialization).apply(false)
    alias(libs.plugins.kotlinParcelize).apply(false)
    alias(libs.plugins.kspPlugin).apply(false)
    alias(libs.plugins.kotlinxAbiPlugin).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)

    alias(libs.plugins.jetbrainsCompose).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)

}
