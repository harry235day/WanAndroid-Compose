package com.zll.compose.compose.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zll.compose.compose.model.BannerModel
import com.zll.compose.compose.paging.CommonPagingSource
import com.zll.compose.ext.isDataOk
import com.zll.compose.model.ArticleListModel
import com.zll.compose.repository.Repository
import com.zll.compose.util.EmptyException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    //首页列表状态
    val homeLazyListState: LazyListState = LazyListState()

    private val repository = Repository()

    //首页列表
    val homeListData: Flow<PagingData<ArticleListModel>>
        get() = _homeListData

    private val _homeListData = Pager(PagingConfig(pageSize = 20)) {
        CommonPagingSource { nextPage: Int ->
            repository.loadArticleList(nextPage)
        }
    }.flow.cachedIn(viewModelScope)


    //首页banner
    val bannerList: LiveData<Result<List<BannerModel>?>>
    private val _banner = MutableLiveData<Unit>()

    val topArticleList: LiveData<Result<List<ArticleListModel>?>>
    private val _topArticle = MutableLiveData<Unit>()

    init {
        bannerList = _banner.switchMap {
            liveData {
                repository.loadBanner()
                    .catch {
                        emit(Result.failure(it))
                    }.collect {
                        if (it.isDataOk()) {
                            emit(Result.success(it.data))
                        } else {
                            emit(Result.failure(EmptyException(it.errorMsg)))
                        }
                    }
            }
        }
        topArticleList = _topArticle.switchMap {
            liveData {
                repository.loadTopArticleList()
                    .catch {
                        emit(Result.failure(it))
                    }.collect {
                        if (it.isDataOk()) {
                            emit(Result.success(it.data))
                        } else {
                            emit(Result.failure(EmptyException(it.errorMsg)))
                        }
                    }
            }
        }
    }

    fun loadBanner(){
        _banner.value = Unit
    }
    fun loadTopArticleList(){
        _topArticle.value = Unit
    }

}