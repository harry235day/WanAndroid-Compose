package com.zll.compose.compose.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zll.compose.compose.model.BannerModel
import com.zll.compose.compose.model.ProjectListData
import com.zll.compose.compose.model.ProjectTreeData
import com.zll.compose.compose.model.SquareArticleModel
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

class PublicViewModel : BaseViewModel() {

    val listStates = hashMapOf<Int, LazyListState>()
    private val repository = Repository()

    var saveChangeProjectIndex = 0

    //首页列表状态
    val publicLazyListState =  LazyListState()

    val tabListData: LiveData<List<ProjectTreeData>>
    private val _tabListData = MutableLiveData<Unit>()


    val id: Int
        get() = tabListData.value?.getOrNull(Nav.publicIndex.value)?.id ?: 0

    val idState = mutableStateOf(0)

    init {
        tabListData = _tabListData.switchMap {
            liveData {
                repository.loadPublicTabList()
                    .catch {
                    }
                    .collect {
                        if (it.isDataOk()) {
                            Nav.publicTabData.value = it.data
                            idState.value = id
                            emit(it.data)
                        }
                    }
            }
        }
    }

    //首页列表
    val publicListData: Flow<PagingData<ProjectListData>>
        get() = _publicListData

    private val _publicListData = Pager(PagingConfig(pageSize = 20)) {
        CommonPagingSource { nextPage: Int ->
            repository.loadPublicArticleList(nextPage, id)
        }
    }.flow.cachedIn(viewModelScope)


    fun loadPublicTabList() {
        _tabListData.value = Unit
    }
}