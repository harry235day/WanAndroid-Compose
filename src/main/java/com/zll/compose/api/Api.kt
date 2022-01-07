package com.zll.compose.api

import com.zll.compose.model.ArticleListData
import com.zll.compose.model.BaseModel
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("article/list/{page}/json")
    suspend fun loadArticleList(@Path("page") page: Int): BaseModel<ArticleListData?>
}