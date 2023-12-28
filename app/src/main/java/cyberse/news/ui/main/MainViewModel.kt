package cyberse.news.ui.main

import androidx.lifecycle.viewModelScope
import cyberse.news.bases.BaseViewModel
import cyberse.news.constants.ApiCode
import cyberse.news.constants.Url
import cyberse.news.models.Article
import cyberse.news.models.Category
import cyberse.news.networks.ApiRepository
import cyberse.news.networks.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val apiRepo: ApiRepository) : BaseViewModel() {

    private val _selectedCategory = Channel<ApiResult<String>>(Channel.BUFFERED)
    val selectedCategory = _selectedCategory.receiveAsFlow()

    private val _actionClick = Channel<MainActionClick>()
    val actionClick = _actionClick.receiveAsFlow()

    private val _article = MutableStateFlow(Article())
    val article = _article.asStateFlow()

    private val _queryFilter = MutableStateFlow("")
    val queryFilter = _queryFilter.asStateFlow()

    fun onClickGoBack() = viewModelScope.launch {
        _actionClick.send(MainActionClick.GoBack)
    }

    fun onClickOpenWebView() = viewModelScope.launch {
        _actionClick.send(MainActionClick.OpenWebView)
    }

    fun loadSelectedCategory(selectedCategory: String) = viewModelScope.launch {
        val url = when (selectedCategory) {
            Category.APPLE.name -> Url.APPLE_ARTICLES
            Category.TESLA.name -> Url.TESLA_ARTICLES
            Category.BUSINESS.name -> Url.TOP_BUSINESS
            Category.TECH_CRUNCH.name -> Url.TECH_CRUNCH
            else -> Url.WSJ_ARTICLES
        }
        val apiCode = ApiCode.LOAD_SELECTED_CATEGORY

        apiRepo.get(url = url, apiCode = apiCode)
            .onStart { _selectedCategory.send(ApiResult.Loading()) }
            .collect { _selectedCategory.send(it) }
    }

    fun setQuery(query: String) = viewModelScope.launch {
        _queryFilter.update { query }
    }

    fun updateArticleDetail(article: Article) = viewModelScope.launch {
        _article.update { article }
    }

}