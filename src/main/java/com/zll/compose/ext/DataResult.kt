package com.zll.compose.ext
/**
 * 数据请求返回格式
 */
sealed class DataResult <out R> {

    //成功
    data class Success<out T>(val data: T): DataResult<T>()

    //请求失败
    data class Error(val exception: Exception): DataResult<Nothing>()

    //加载数据中
    object Loading: DataResult<Nothing>()

    override fun toString(): String {
        return when(this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
            else -> "Error"
        }
    }

}

/**
 * 返回结果如果是Success类，并且data非null才认为是成功的
 */
val DataResult<*>.succeeded: Boolean
    get() = this is DataResult.Success && data != null

/**
 * 获取资源的状态
 */
enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}