package cyberse.news.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categories(
    @Json(name = "status")
    val status: String,

    @Json(name = "totalResults")
    val totalResults: Int,

    @Json(name = "articles")
    val articles: List<Article>
) : Parcelable