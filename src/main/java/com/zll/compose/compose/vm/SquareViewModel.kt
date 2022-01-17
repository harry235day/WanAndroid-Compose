package com.zll.compose.compose.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zll.compose.compose.model.BannerModel
import com.zll.compose.compose.model.SquareArticleModel
import com.zll.compose.compose.paging.CommonPagingSource
import com.zll.compose.ext.isDataOk
import com.zll.compose.model.ArticleListModel
import com.zll.compose.repository.Repository
import com.zll.compose.util.EmptyException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SquareViewModel : BaseViewModel() {

    //首页列表状态
    val squareLazyListState: LazyListState = LazyListState()

    private val repository = Repository()

    //首页列表
    val squareListData: Flow<PagingData<SquareArticleModel>>
        get() = _squareListData

    private val _squareListData = Pager(PagingConfig(pageSize = 20)) {
        CommonPagingSource { nextPage: Int ->
            repository.loadSquareArticleList(nextPage)
        }
    }.flow.cachedIn(viewModelScope)

}