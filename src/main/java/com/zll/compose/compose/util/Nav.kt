package com.zll.compose.compose.util

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.zll.compose.compose.model.ProjectTreeData

object Nav {

    //项目
    val projectIndex = mutableStateOf(0)
    val projectTabData = MutableLiveData<List<ProjectTreeData>>()

    //公众号
    val publicIndex = mutableStateOf(0)
    val publicTabData = MutableLiveData<List<ProjectTreeData>>()

    //体系
    val systemIndex = mutableStateOf(0)

    //是否登录
    val loginState = mutableStateOf(SpUtilsMMKV.getBoolean(Constant.IS_LOGIN) == true)
}