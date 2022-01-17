package com.zll.compose.repository

import com.zll.compose.compose.api.Api
import com.zll.compose.compose.api.ApiData
import com.zll.compose.compose.model.*
import com.zll.compose.compose.paging.CommonPageModel
import com.zll.compose.model.ArticleListModel
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
     * @param page 0开始
     */
    suspend fun loadArticleList(page: Int): CommonPageModel<ArticleListModel> {
        return api.loadArticleList(page)
    }

    /**
     * 获取广场
     * @param page 0开始
     */
    suspend fun loadSquareArticleList(page: Int): CommonPageModel<SquareArticleModel> {
        return api.loadSquareArticleList(page)
    }

    /**
     * @param page 0开始
     */
    suspend fun loadPublicArticleList(page: Int, id: Int): CommonPageModel<ProjectListData> {
        return api.loadPublicArticleList(page, id)
    }

    suspend fun loadBanner(): Flow<BaseModel<List<BannerModel>?>> {
        return flow {
            emit(api.loadBanner())
        }.flowOn(Dispatchers.IO)
    }


    suspend fun loadTopArticleList(): Flow<BaseModel<List<ArticleListModel>?>> {
        return flow {
            emit(api.loadTopArticleList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loadUserInfo(): Flow<BaseModel<UserModel?>> {
        return flow {
            emit(api.loadUserInfo())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loadPublicTabList(): Flow<BaseModel<List<ProjectTreeData>>> {
        return flow {
            emit(api.loadPublicTabList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loadSystemList(): Flow<BaseModel<List<ProjectTreeData>>> {
        return flow {
            emit(api.loadSystemList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loadNavList(): Flow<BaseModel<List<NavModel>?>> {
        return flow {
            emit(api.loadNavList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loadSystemArticleById(page: Int, id: Int): CommonPageModel<ProjectListData> {
        return api.loadSystemArticleById(page, id)
    }

    suspend fun loadProjectTabList(): Flow<BaseModel<List<ProjectTreeData>>> {
        return flow {
            emit(api.loadProjectTabList())
        }.flowOn(Dispatchers.IO)
    }

    /**
     * @param page 0开始
     */
    suspend fun loadProjectArticleList(page: Int, id: Int): CommonPageModel<ProjectListData> {
        return api.loadProjectArticleList(page, id)
    }

    /**
     * 登录
     */
    suspend fun login(name: String, pwd: String): Flow<BaseModel<Any>> {
        return flow {
            kotlinx.coroutines.delay(1000)
            emit(api.login(name, pwd))
        }.flowOn(Dispatchers.IO)
    }
}