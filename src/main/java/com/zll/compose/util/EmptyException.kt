package com.zll.compose.util

data class EmptyException(val msg:String?,val e:Throwable?=null):Exception(msg,e)