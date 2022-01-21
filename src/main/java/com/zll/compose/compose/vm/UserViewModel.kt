package com.zll.compose.compose.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.zll.compose.compose.model.UserModel
import com.zll.compose.compose.util.Key
import com.zll.compose.compose.util.SpUtilsMMKV
import com.zll.compose.ext.isCodeOk
import com.zll.compose.ext.isDataOk
import com.zll.compose.model.BaseModel
import com.zll.compose.repository.Repository
import com.zll.compose.util.EmptyException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform

class UserViewModel : BaseViewModel() {
    private val repository = Repository()

    //首页banner
    val userInfo: LiveData<UserModel?>
    private val _userInfo = MutableLiveData<Unit>()

    val loading = MutableLiveData<NetWorkState>()

    val loginData: LiveData<Result<BaseModel<Any>>>
    private val _loginData = MutableLiveData<Pair<String, String>>()

    val logoutData: LiveData<Result<BaseModel<Any>>>
    private val _logoutData = MutableLiveData<Unit>()


    init {
        userInfo = _userInfo.switchMap {
            liveData {
                repository.loadUserInfo()
                    .catch {
                        emit(null)
                    }.collect {
                        if (it.isDataOk()) {
                            emit(it.data)
                        } else {
                            emit(null)
                        }
                    }
            }
        }

        loginData = _loginData.switchMap { pair ->
            liveData {
                repository.login(pair.first, pair.second)
                    .onStart {
                        loading.value = NetWorkState.loading()
                    }
                    .catch {
                        loading.value = NetWorkState.error("请求失败", it)
                        emit(Result.failure(it))
                    }
                    .collect {
                        if (it.isCodeOk()) {
                            loginSuccess()
                            SpUtilsMMKV.put(Key.KEY_USER_NAME, pair.first)
                            loading.value = NetWorkState.success(if (it.errorMsg.isNullOrBlank()) "登录成功" else it.errorMsg)
                            emit(Result.success(it))
                        } else {
                            loading.value = NetWorkState.error(it.errorMsg ?: "登录失败", null)
                            emit(Result.failure(EmptyException(it.errorMsg)))
                        }
                    }
            }
        }

        logoutData = _logoutData.switchMap {
            liveData {
                repository.logout()
                    .onStart {
                        loading.value = NetWorkState.loading()
                    }
                    .transform {
                        if(it.isCodeOk()){
                            clearLogin()
                        }
                        emit(it)
                    }
                    .catch {
                        loading.value = NetWorkState.error("请求失败", it)
                        emit(Result.failure(it))
                    }
                    .collect {
                        if (it.isCodeOk()) {

                            loading.value = NetWorkState.success(if (it.errorMsg.isNullOrBlank()) "退出成功" else it.errorMsg)
                            emit(Result.success(it))
                        } else {
                            loading.value = NetWorkState.error(it.errorMsg ?: "退出失败", null)
                            emit(Result.failure(EmptyException(it.errorMsg)))
                        }
                    }
            }
        }

    }

    fun loadUserInfo() {
        _userInfo.value = Unit
    }

    fun login(name: String, pwd: String) {
        _loginData.value = Pair(name, pwd)
    }
    fun logout() {
        _logoutData.value = Unit
    }


}

data class NetWorkState(val state: State, val msg: String?, val t: Throwable?) {
    sealed class State {
        object Loading : State()
        object Error : State()
        object Success : State()
    }

    companion object {

        fun loading(msg: String = ""): NetWorkState {
            return NetWorkState(State.Loading, msg, null)
        }

        fun error(msg: String, t: Throwable?): NetWorkState {
            return NetWorkState(State.Error, msg, null)
        }

        fun success(msg: String): NetWorkState {
            return NetWorkState(State.Success, msg, null)
        }
    }
}