package com.zll.compose.compose.model

data class UserModel(
    val coinInfo:CoinInfoModel?,
    val userInfo:UserInfoModel?,
)

data class CoinInfoModel(
    val coinCount:Int,
    val level:Int,
    val rank:String?,
)
data class UserInfoModel(
    val username:String?,
    val id:Int,
)