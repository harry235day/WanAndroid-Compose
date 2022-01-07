package com.zll.compose.vm
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*

class CountModel : ViewModel(){

    private val _count = mutableStateOf(0)
    val count = _count

    var c = 0

    fun add(){
        _count.value ++
        println("count${count.value}")
        c++
        liveDataTrigger.value = c
    }
    val liveDataTrigger = MutableLiveData<Int>()
    val liveData:LiveData<Int>

    init {
        liveData = liveDataTrigger.switchMap {
            liveData {
                emit(it)
            }
        }
    }



}



