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

class ProjectViewModel : BaseViewModel() {

    private val repository = Repository()

    var saveChangeProjectIndex = 0

    //首页列表状态
    val projectLazyListState =  LazyListState()

    val tabListData: LiveData<List<ProjectTreeData>>
    private val _tabListData = MutableLiveData<Unit>()


    val id: Int
        get() = tabListData.value?.getOrNull(Nav.projectIndex.value)?.id ?: 0

    val idState = mutableStateOf(0)

    init {
        tabListData = _tabListData.switchMap {
            liveData {
                repository.loadProjectTabList()
                    .catch {
                    }
                    .collect {
                        if (it.isDataOk()) {
                            Nav.projectTabData.value = it.data
                            idState.value = id
                            emit(it.data)
                        }
                    }
            }
        }
    }

    //
    val projectListData: Flow<PagingData<ProjectListData>>
        get() = _projectListData

    private val _projectListData = Pager(PagingConfig(pageSize = 20)) {
        CommonPagingSource { nextPage: Int ->
            repository.loadProjectArticleList(nextPage, id)
        }
    }.flow.cachedIn(viewModelScope)


    fun loadProjectTabList() {
        _tabListData.value = Unit
    }
}