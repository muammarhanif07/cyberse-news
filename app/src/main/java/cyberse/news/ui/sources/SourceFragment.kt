package cyberse.news.ui.sources

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cyberse.news.R
import cyberse.news.bases.BaseFragment
import cyberse.news.constants.ApiCode
import cyberse.news.databinding.FragmentSourceBinding
import cyberse.news.extensions.getJsonResponse
import cyberse.news.extensions.hideKeyboard
import cyberse.news.extensions.observeData
import cyberse.news.extensions.showArticleDetail
import cyberse.news.extensions.toLinear
import cyberse.news.extensions.visibleOrGone
import cyberse.news.models.Article
import cyberse.news.models.Category
import cyberse.news.networks.ApiResult
import cyberse.news.ui.main.MainActionClick
import cyberse.news.ui.main.MainViewModel
import cyberse.news.utils.DataUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SourceFragment : BaseFragment<FragmentSourceBinding>(FragmentSourceBinding::inflate),
    SearchView.OnQueryTextListener {

    private val args by navArgs<SourceFragmentArgs>()
    private val mainViewModel by viewModels<MainViewModel>()
    private val sourceAdapter by lazy { SourceAdapter() }

    private var activeQuery = ""

    override fun setUpViews() {
        binding?.apply {
            loadData()
            configureSearchBar(ivSearch)
            lifecycleOwner = viewLifecycleOwner
            viewModel = mainViewModel
            ivSearch.setOnQueryTextFocusChangeListener { _, isFocused ->
                binding?.hideTitle = isFocused
            }

            swipeRefresh.setColorSchemeResources(R.color.red)
            swipeRefresh.setOnRefreshListener {
                sourceAdapter.clearAll()
                loadData()
            }

            sources.toLinear(sourceAdapter)
            sourceAdapter.setOnClickItemListener { item -> showArticleDetail(item) }

            val resId = when (args.selectedCategory) {
                Category.APPLE.name -> R.drawable.ic_logo_apple
                Category.TESLA.name -> R.drawable.ic_logo_tesla
                Category.BUSINESS.name -> R.drawable.ic_logo_us
                Category.TECH_CRUNCH.name -> R.drawable.ic_logo_techcrunch
                else -> R.drawable.ic_logo_wsj
            }

            ivEmptyState.setImageDrawable(ContextCompat.getDrawable(requireContext(), resId))
        }
    }

    override fun onObserveData() {
        with(mainViewModel) {
            observeData(errorApi, ::handleErrorApi)
            observeData(selectedCategory, ::handleResponse)
            observeData(actionClick, ::handleActionClick)
            observeData(queryFilter, ::handleQueryFilter)
        }
    }

    override fun onRetry(apiCode: String?) {
        if (apiCode == ApiCode.LOAD_SELECTED_CATEGORY) loadData()
    }

    private fun loadData() {
        mainViewModel.loadSelectedCategory(args.selectedCategory)
    }

    private fun configureSearchBar(view: SearchView) {
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        view.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        view.setOnQueryTextListener(this)
        view.queryHint = getString(R.string.search_source)
    }

    override fun onQueryTextChange(newText: String): Boolean {
        mainViewModel.setQuery(newText)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        hideKeyboard()
        return false
    }

    private fun handleResponse(result: ApiResult<String>) {
        when (result) {
            is ApiResult.Loading -> binding?.sources?.veil().also {
                binding?.swipeRefresh?.isRefreshing = false
                binding?.ivEmptyState?.visibleOrGone(false)
                binding?.tvEmptyState?.visibleOrGone(false)
            }
            is ApiResult.Error -> binding?.sources?.unVeil().also {
                binding?.swipeRefresh?.isRefreshing = false
                binding?.ivEmptyState?.visibleOrGone(true)
                binding?.tvEmptyState?.visibleOrGone(true)
                mainViewModel.setError(result)
            }
            is ApiResult.Success -> binding?.sources?.unVeil().also {
                binding?.swipeRefresh?.isRefreshing = false
                val data = result.data.getJsonResponse("articles")
                val sourceList = DataUtil.toList<Article>(data)

                sourceAdapter.addAll(sourceList).also { doFilterAdapter() }
                checkTotalItemInAdapter()
            }
        }
    }

    private fun checkTotalItemInAdapter() {
        binding?.ivEmptyState?.visibleOrGone(sourceAdapter.itemCount == 0)
        binding?.tvEmptyState?.visibleOrGone(sourceAdapter.itemCount == 0)
        binding?.sources?.visibleOrGone(sourceAdapter.itemCount > 0)
    }

    private fun handleActionClick(action: MainActionClick) {
        if (action == MainActionClick.GoBack) dismiss()
    }

    private fun handleQueryFilter(query: String) {
        activeQuery = query

        if (sourceAdapter.mListRef.isNotEmpty()) doFilterAdapter()
    }

    private fun doFilterAdapter() {
        sourceAdapter.filter.filter(activeQuery)
    }

}