package com.zll.compose.vm

import androidx.lifecycle.*
import com.zll.compose.model.ArticleListModel
import com.zll.compose.repository.Repository
import com.zll.compose.util.EmptyException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

@HiltViewModel
class ArticleViewModel : ViewModel() {
    private val repository = Repository()
    private var page = 1

    private val articleHomeTrigger = MutableLiveData<Int>()

    init {

    }

    fun loadArticleList(isFresh: Boolean) {
        if (isFresh) page = 1 else page++
        articleHomeTrigger.value = page
    }

}