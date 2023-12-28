package cyberse.news.modules

import android.content.Context
import cyberse.news.networks.NetworkChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNetworkChecker(@ApplicationContext context: Context) : NetworkChecker {
        return NetworkChecker(context)
    }

}