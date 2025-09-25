package com.company.newsapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform