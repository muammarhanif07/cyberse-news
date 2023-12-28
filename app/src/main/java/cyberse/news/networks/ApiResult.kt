package cyberse.news.networks

sealed class ApiResult<T>(
    val data: T? = null,
    val apiCode: String? = null,
    val errorCode: Int? = null,
) {

    class Loading<T> : ApiResult<T>()
    class Error<T>(apiCode: String?, errorCode: Int?) : ApiResult<T>(null, apiCode, errorCode)
    class Success<T>(data: T?) : ApiResult<T>(data)

}