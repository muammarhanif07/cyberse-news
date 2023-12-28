package cyberse.news.modules

import androidx.multidex.BuildConfig
import cyberse.news.constants.Url
import cyberse.news.networks.ApiInterface
import cyberse.news.networks.ApiRepository
import cyberse.news.networks.NetworkChecker
import cyberse.news.networks.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(okHttp)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    fun provideRequestInterceptor() : RequestInterceptor {
        return RequestInterceptor()
    }

    @Singleton
    @Provides
    fun provideOkHttp(interceptor: RequestInterceptor) : OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) { level = HttpLoggingInterceptor.Level.BODY }
        }
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(logger)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRepository(api: ApiInterface, network: NetworkChecker): ApiRepository {
        return ApiRepository(api, network)
    }

}