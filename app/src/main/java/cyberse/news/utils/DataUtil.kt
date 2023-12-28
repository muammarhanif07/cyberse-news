package cyberse.news.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object DataUtil {

    inline fun <reified T> toList(data: String?): List<T> {
        try {
            if (data.isNullOrBlank()) return emptyList()

            val type = Types.newParameterizedType(List::class.java, T::class.java)
            val builder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<List<T>> = builder.adapter(type)

            return adapter.nullSafe().fromJson(data) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

}