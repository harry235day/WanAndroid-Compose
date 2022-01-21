package com.zll.compose.compose.api

import com.zll.compose.compose.model.*
import com.zll.compose.compose.paging.CommonPageModel
import com.zll.compose.model.ArticleListModel
import com.zll.compose.model.BaseModel
import retrofit2.http.*

interface Api {

    @GET("article/list/{page}/json")
    suspend fun loadArticleList(@Path("page") page: Int): CommonPageModel<ArticleListModel>

    @GET("user_article/list/{page}/json")
    suspend fun loadSquareArticleList(@Path("page") page: Int): CommonPageModel<SquareArticleModel>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun loadPublicArticleList(@Path("page") page: Int, @Path("id") id: Int): CommonPageModel<ProjectListData>

    @GET("banner/json")
    suspend fun loadBanner(): BaseModel<List<BannerModel>?>

    //置顶
    @GET("article/top/json")
    suspend fun loadTopArticleList(): BaseModel<List<ArticleListModel>?>

    /**
     * 个人信息
     */
    @GET("user/lg/userinfo/json")
    suspend fun loadUserInfo(): BaseModel<UserModel?>

    //获取公众号列表
    @GET("wxarticle/chapters/json")
    suspend fun loadPublicTabList(): BaseModel<List<ProjectTreeData>>

    /**
     * 体系列表
     */
    @GET("tree/json")
    suspend fun loadSystemList(): BaseModel<List<ProjectTreeData>>

    /**
     * 体系下的文章
     */
    @GET("article/list/{page}/json")
    suspend fun loadSystemArticleById(@Path("page") page: Int, @Query("cid") id: Int): CommonPageModel<ProjectListData>

    /**
     * 导航
     */
    @GET("navi/json")
    suspend fun loadNavList(): BaseModel<List<NavModel>?>


    /**
     * 获取公众号列表
     */
    @GET("project/tree/json")
    suspend fun loadProjectTabList(): BaseModel<List<ProjectTreeData>>

    /**
     *
     */
    @GET("project/list/{page}/json")
    suspend fun loadProjectArticleList(@Path("page") page: Int, @Query("cid") id: Int): CommonPageModel<ProjectListData>


    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username") username: String, @Field("password") password: String): BaseModel<Any>

    @GET("user/logout/json")
    suspend fun logout(): BaseModel<Any>

    /**
     * 积分排行
     */
    @GET("coin/rank/{page}/json")
    suspend fun loadScoreList(@Path("page") page: Int): CommonPageModel<ScoreListModel>

    /**
     * 热门词汇
     */
    @GET("hotkey/json")
    suspend fun loadHotKey(): BaseModel<List<HotKeyModel>>

    /**
     * 搜索
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    suspend fun loadSearchResult(@Path("page") page: Int, @Field("k") key: String): CommonPageModel<ProjectListData>

    /**
     * 个人积分列表
     */
    @GET("lg/coin/list/{page}/json")
    suspend fun loadScoreDetail(@Path("page")page:Int):CommonPageModel<ScoreListModel>
}