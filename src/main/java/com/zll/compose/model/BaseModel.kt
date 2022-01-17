package com.zll.compose.model

data class BaseModel<T>(
    val errorCode: Int,
    val errorMsg: String? = null,
    val data: T
) {

}
