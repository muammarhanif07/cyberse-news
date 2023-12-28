package cyberse.news.interfaces

interface OnLostConnection {
    fun onRetry(apiCode: String?)
}