package cyberse.news.networks

import cyberse.news.constants.ErrorCode
import cyberse.news.extensions.getJsonResponse
import cyberse.news.extensions.isHasHtmlTags
import cyberse.news.extensions.toUrlWithQuery
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@ActivityRetainedScoped
class ApiRepository @Inject constructor(private val api: ApiInterface, private val network: NetworkChecker) {

    suspend fun get(
        url: String,
        query: List<Pair<String, Any?>>? = null,
        apiCode: String? = null
    ): Flow<ApiResult<String>> {
        return safeApiCall({ api.get(url.toUrlWithQuery(query)) }, apiCode)
    }

    private suspend fun safeApiCall(
        apiCall: suspend () -> Response<String>,
        apiCode: String?
    ): Flow<ApiResult<String>> {
        return flow {
            if (!network.isConnected) {
                emit(ApiResult.Error(apiCode, ErrorCode.NO_INTERNET_CONNECTION))
                return@flow
            }

            try {
                val response = apiCall.invoke()

                if (response.isSuccessful) {
                    emit(ApiResult.Success(response.body() ?: ""))
                } else {
                    emit(ApiResult.Error(apiCode, getErrorCode(response)))
                }
            } catch (e: IOException) {
                emit(ApiResult.Error(apiCode, getErrorCodeByException(e)))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getErrorCode(response: Response<String>): Int {
        val errorBody = response.errorBody()?.charStream()?.readText() ?: return response.code()

        if (errorBody.isHasHtmlTags()) return response.code()
        return errorBody.getJsonResponse("errorCode")?.toInt() ?: 0
    }

    private fun getErrorCodeByException(e: Exception): Int {
        e.printStackTrace()

        return when (e) {
            is SocketTimeoutException -> ErrorCode.NO_INTERNET_CONNECTION
            else -> ErrorCode.INTERNAL_SERVER_ERROR
        }
    }

}