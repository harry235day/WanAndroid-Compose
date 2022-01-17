package com.zll.compose

import android.app.Application
import android.util.Log
import com.tencent.mmkv.MMKV
import com.zll.compose.compose.util.DataStoreUtils

class App : Application() {
    companion object{
        lateinit var CONTEXT: Application
    }
    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
        DataStoreUtils.init(this)
        val rootDir: String = MMKV.initialize(this)
        Log.d("初始化", "MMKV根目录：$rootDir")
    }
}
