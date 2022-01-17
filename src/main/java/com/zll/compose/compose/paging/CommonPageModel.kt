package com.zll.compose.compose.paging

import androidx.annotation.Keep

@Keep
data class CommonPageModel<T>(
    val `data`: Data<T>,
) {
    @Keep
    data class Data<T>(
        val curPage: Int,
        val datas: List<T>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
    )

}