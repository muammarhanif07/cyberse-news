package cyberse.news.networks

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {

    @GET
    suspend fun get(@Url url: String): Response<String>

}