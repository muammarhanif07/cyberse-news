package cyberse.news.ui.article

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cyberse.news.R
import cyberse.news.bases.BaseSheet
import cyberse.news.databinding.SheetArticleDetailBinding
import cyberse.news.extensions.observeData
import cyberse.news.extensions.openWebView
import cyberse.news.ui.main.MainActionClick
import cyberse.news.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailSheet : BaseSheet<SheetArticleDetailBinding>(SheetArticleDetailBinding::inflate) {

    private val args by navArgs<ArticleDetailSheetArgs>()
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_CyberseNews_SheetTheme)
    }

    override fun setUpViews() {
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.viewModel = mainViewModel
        mainViewModel.updateArticleDetail(args.article)
    }

    override fun onObserveData() {
        observeData(mainViewModel.actionClick, ::handleActionClick)
    }

    private fun handleActionClick(action: MainActionClick) {
        if (action == MainActionClick.GoBack) {
            dismiss()
        } else {
            openWebView(args.article.url ?: "")
        }
    }

}