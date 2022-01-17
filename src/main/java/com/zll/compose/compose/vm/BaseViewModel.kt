package com.zll.compose.compose.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zll.compose.compose.util.Constant
import com.zll.compose.compose.util.DataStoreUtils
import com.zll.compose.compose.util.SpUtilsMMKV
import com.zll.compose.ext.addTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    //job列表
    private val jobs: MutableList<Job> = mutableListOf<Job>()

    //标记网络loading状态
    val isLoading = MutableLiveData<Boolean>()

    protected fun await(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        //协程启动之前
        isLoading.value = true
        block.invoke(this)
        //协程启动之后
        isLoading.value = false
    }.addTo(jobs)

    //取消全部协程
    override fun onCleared() {
        jobs.forEach { it.cancel() }
        super.onCleared()
    }

    //是否登录
    fun isLogin(): Boolean = SpUtilsMMKV.getBoolean(Constant.IS_LOGIN) == true

    fun loginSuccess() {
        SpUtilsMMKV.put(Constant.IS_LOGIN, true)
    }

   suspend fun clearLogin(){
        SpUtilsMMKV.removeKey(Constant.IS_LOGIN)
        DataStoreUtils.clear()
    }

}