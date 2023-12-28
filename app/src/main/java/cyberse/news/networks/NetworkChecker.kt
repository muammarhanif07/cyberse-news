package cyberse.news.networks

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.InetSocketAddress
import java.net.Socket
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkChecker @Inject constructor(@ApplicationContext private val context: Context) {

    val isConnected: Boolean
        get() {
            var isConnect = false

            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
                getNetworkCapabilities(activeNetwork)?.run {
                    isConnect = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }

            if (isConnect) {
                val socket = Socket()

                try {
                    socket.connect(InetSocketAddress("www.google.com", 443), 6000)
                    isConnect = true
                } catch(ex: UnknownHostException) {
                    isConnect = false
                    ex.printStackTrace()
                } catch(ex: Exception) {
                    isConnect = false
                    ex.printStackTrace()
                } finally {
                    try {
                        socket.close()
                    } catch(ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }

            return isConnect
        }

}