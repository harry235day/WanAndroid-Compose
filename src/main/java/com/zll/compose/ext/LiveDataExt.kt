package com.zll.compose.ext

import androidx.lifecycle.*
import kotlin.reflect.KProperty1

//监听一个属性
fun <T, A> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    action: (A) -> Unit
) {
    this.map {
        StateTuple1(prop1.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a) ->
        action.invoke(a)
    }
}

//监听两个属性
fun <T, A, B> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    action: (A, B) -> Unit
) {
    this.map {
        StateTuple2(prop1.get(it), prop2.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a, b) ->
        action.invoke(a, b)
    }
}

internal data class StateTuple1<A>(val a: A)
internal data class StateTuple2<A, B>(val a: A, val b: B)

//更新State
fun <T> MutableLiveData<T>.setState(reducer: T.() -> T) {
    this.value = this.value?.reducer()
}
