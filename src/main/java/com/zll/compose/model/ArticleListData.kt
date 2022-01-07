package com.zll.compose.model

data class ArticleListData(
    val curPage:Int,
    val datas:List<ArticleListModel>?,
)

data class ArticleListModel(
    val author:String?,
    val title:String?,
    val zan:String?,
    val link:String?,
    val niceDate:String?,
)
