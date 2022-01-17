package com.zll.compose.ext

import com.zll.compose.model.BaseModel

fun <T> BaseModel<T>.isCodeOk(code: Int = 0): Boolean {
    return errorCode == code
}

fun <T> BaseModel<T>.isDataOk(code: Int = 0): Boolean {
    return errorCode == code && if (data is Collection<*>) {
        return !data.isNullOrEmpty()
    } else {
        return data != null
    }
}