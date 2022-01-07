package com.zll.compose.ext


fun String.number(size: Int, default: String = "...."):String {
    return "${this.slice(0..size)}$default"
}