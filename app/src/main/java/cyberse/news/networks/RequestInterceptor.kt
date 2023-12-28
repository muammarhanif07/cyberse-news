package cyberse.news.networks

import cyberse.news.constants.Url
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val CONTENT_TYPE = "Content-Type"
        private const val CONTENT_TYPE_VALUE = "application/json"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKeyBearer = "Bearer ${Url.API_KEY}"
        val newRequest = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, apiKeyBearer)
            .addHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
            .build()

        return chain.proceed(newRequest)
    }

}