package com.zll.compose.compose.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zll.compose.compose.model.*
import com.zll.compose.compose.paging.CommonPagingSource
import com.zll.compose.compose.util.Nav
import com.zll.compose.ext.isDataOk
import com.zll.compose.model.ArticleListModel
import com.zll.compose.repository.Repository
import com.zll.compose.util.EmptyException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SystemViewModel : BaseViewModel() {

    private val repository = Repository()

    //体系
    val systemIndexState: LazyListState = LazyListState()

    //导航
    val systemArticleState: LazyListState = LazyListState()

    val tabList = arrayListOf(
        TabBaseModel(0,"体系"),
        TabBaseModel(0,"导航"),
    )

    val systemListData: LiveData<List<ProjectTreeData>>
    private val _systemListData = MutableLiveData<Unit>()

    val navListData: LiveData<List<NavModel>?>
    private val _navListData = MutableLiveData<Unit>()

    var id = 0
    //体系下文章列表
    val articleListData: Flow<PagingData<ProjectListData>>
        get() = _articleListData

    private val _articleListData = Pager(PagingConfig(pageSize = 20)) {
        CommonPagingSource { nextPage: Int ->
            repository.loadSystemArticleById(nextPage,id)
        }
    }.flow.cachedIn(viewModelScope)

    init {
        systemListData = _systemListData.switchMap {
            liveData {
                repository.loadSystemList()
                    .catch {
                    }
                    .collect {
                        if (it.isDataOk()) {
                            emit(it.data)
                        }
                    }
            }
        }

        navListData = _navListData.switchMap {
            liveData {
                repository.loadNavList()
                    .catch {
                    }
                    .collect {
                        if (it.isDataOk()) {
                            emit(it.data)
                        }
                    }
            }
        }
    }




    fun loadSystemList() {
        _systemListData.value = Unit
    }
    fun loadNavList() {
        _navListData.value = Unit
    }

}