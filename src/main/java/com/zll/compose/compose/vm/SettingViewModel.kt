package com.zll.compose.compose.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zll.compose.App
import com.zll.compose.compose.model.BannerModel
import com.zll.compose.compose.model.HotKeyModel
import com.zll.compose.compose.model.ProjectListData
import com.zll.compose.compose.model.ScoreListModel
import com.zll.compose.compose.paging.CommonPagingSource
import com.zll.compose.compose.room.SearchResultModel
import com.zll.compose.compose.util.DBManager
import com.zll.compose.ext.isDataOk
import com.zll.compose.model.ArticleListModel
import com.zll.compose.repository.Repository
import com.zll.compose.util.EmptyException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel : BaseViewModel() {

    //积分列表状态
    val scoreLazyListState: LazyListState = LazyListState()

    //搜索页面
    val searchLazyListState: LazyListState = LazyListState()
    //搜索结果
    val searchResultLazyListState: LazyListState = LazyListState()
    //个人积分列表
    val userScoreLazyListState: LazyListState = LazyListState()

    private val repository = Repository()

    var searchKey = ""

    //积分列表
    val scoreListData: Flow<PagingData<ScoreListModel>>
        get() = _scoreListData

    private val _scoreListData = Pager(PagingConfig(pageSize = 20)) {
        CommonPagingSource { nextPage: Int ->
            repository.loadScoreList(nextPage)
        }
    }.flow.cachedIn(viewModelScope)


    //我的积分列表
    val userScoreListData: Flow<PagingData<ScoreListModel>>
        get() = _userScoreListData

    private val _userScoreListData = Pager(PagingConfig(pageSize = 20)) {
        CommonPagingSource { nextPage: Int ->
            repository.loadScoreDetail(nextPage)
        }
    }.flow.cachedIn(viewModelScope)

    //搜索结果
    val searchResultListData: Flow<PagingData<ProjectListData>>
        get() = _searchResultListData

    private val _searchResultListData = Pager(PagingConfig(pageSize = 20)) {
        CommonPagingSource { nextPage: Int ->
            repository.loadSearchResult(nextPage, searchKey)
        }
    }.flow.cachedIn(viewModelScope)


    val hotListData: LiveData<List<HotKeyModel>?>
    private val _hotListData = MutableLiveData<Unit>()

    val loadSearchList: LiveData<List<SearchResultModel>?>
    private val _loadSearchList = MutableLiveData<Unit>()

    init {
        hotListData = _hotListData.switchMap {
            liveData {
                repository.loadHotKey()
                    .catch {
                        emit(null)
                    }
                    .collect {
                        emit(it.data)
                    }
            }
        }

        loadSearchList = _loadSearchList.switchMap {
            DBManager.searchDao().loadSearchList().asLiveData()
        }
    }

    fun loadHotKey() {
        _hotListData.value = Unit
    }

    fun loadSearchList() {
        _loadSearchList.value = Unit
    }

    fun delete(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            DBManager.searchDao().delete(title)
            withContext(Dispatchers.Main) {
                loadSearchList()
            }
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            DBManager.searchDao().deleteAll()
            withContext(Dispatchers.Main) {
                loadSearchList()
            }
        }
    }

    fun addSearch(result: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultModel = DBManager.searchDao().loadSearchByTitle(result)
            if (resultModel == null) {
                val model = SearchResultModel(
                    null, result, System.currentTimeMillis()
                )
                DBManager.searchDao().insert(model)
            } else {
                DBManager.searchDao().update(resultModel.copy(time = System.currentTimeMillis()))
            }
        }
    }

}