package cyberse.news.models

import android.os.Parcelable
import com.squareup.moshi.Json
import cyberse.news.extensions.toDateWithTimezone
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    @Json(name = "source")
    val source: Source? = null,

    @Json(name = "author")
    val author: String? = null,

    @Json(name = "title")
    val title: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "url")
    val url: String? = null,

    @Json(name = "urlToImage")
    val urlToImage: String? = null,

    @Json(name = "publishedAt")
    val publishedAt: String? = null,

    @Json(name = "content")
    val content: String? = null
) : Parcelable {

    val authorName
        get() = if (author.isNullOrBlank()) "-" else author

    val formattedPublishedAt
        get() = publishedAt.toDateWithTimezone()

}