package com.zll.compose.model

import androidx.annotation.Keep

data class ArticleListData(
    val curPage: Int,
    val datas: List<ArticleListModel>?,
)

/**
 * 首页列表数据
 */
@Keep
data class ArticleListModel(
    val apkLink: String?,
    val audit: Int,
//作者
    val author: String?,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String?,
//是否收藏
    val collect: Boolean,
    val courseId: Int,
    val desc: String?,
    val descMd: String?,
    val envelopePic: String?,
//是否是最新的
    val fresh: Boolean,
    val host: String?,
    val id: Int,
//链接
    val link: String?,
    val niceDate: String?,
    val niceShareDate: String?,
    val origin: String?,
    val prefix: String?,
    val projectLink: String?,
    //发布时间
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    //分享用户
    val shareUser: String?,
    val superChapterId: Int,
    //来源渠道名
    val superChapterName: String?,
    val tags: List<Tag?>?,
    //标题
    val title: String?,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
) {
    @Keep
    data class Tag(
        val name: String?,
        val url: String?
    )
}