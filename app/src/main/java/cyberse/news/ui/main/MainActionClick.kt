package cyberse.news.ui.main

sealed class MainActionClick {
    data object GoBack : MainActionClick()
    data object OpenWebView : MainActionClick()
}