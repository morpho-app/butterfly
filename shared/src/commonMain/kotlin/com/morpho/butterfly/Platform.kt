package com.morpho.butterfly

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform