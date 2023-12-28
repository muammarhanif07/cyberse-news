package cyberse.news.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cyberse.news.models.Article
import cyberse.news.ui.article.ArticleDetailSheetDirections
import cyberse.news.ui.home.HomeFragmentDirections
import cyberse.news.ui.sources.SourceFragmentDirections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> Fragment.observeData(data: Flow<T>, action: (t: T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        data.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { action(it) }
    }
}

fun Fragment.hideKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
}

fun Fragment.navigate(navDirections: NavDirections, options: NavOptions? = null) {
    try {
        findNavController().navigate(navDirections, options)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.openSourceFragment(selectedCategory: String) {
    val direction = HomeFragmentDirections.navigateToSourceFragment(selectedCategory)
    navigate(direction)
}

fun Fragment.showArticleDetail(article: Article) {
    val direction = SourceFragmentDirections.navigateToArticleDetailSheet(article)
    navigate(direction)
}

fun Fragment.openWebView(url: String) {
    val direction = ArticleDetailSheetDirections.navigateToWebView(url)
    navigate(direction)
}