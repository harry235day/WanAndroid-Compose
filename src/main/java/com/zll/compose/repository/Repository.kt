package com.zll.compose.repository

import com.zll.compose.api.Api
import com.zll.compose.api.ApiData
import com.zll.compose.model.ArticleListData
import com.zll.compose.model.BaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository {

    private val api by lazy {
        ApiData.createApi(Api::class.java)
    }

    /**
     * 获取文章列表
     * @param page 1开始
     */
    fun loadArticleList(page: Int): Flow<BaseModel<ArticleListData?>> {
        return flow {
            emit(api.loadArticleList(page))
        }.flowOn(Dispatchers.IO)
    }
}