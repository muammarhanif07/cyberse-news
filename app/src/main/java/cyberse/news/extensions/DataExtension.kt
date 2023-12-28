package cyberse.news.extensions

import cyberse.news.utils.DateUtil
import cyberse.news.utils.Tools
import org.json.JSONException
import org.json.JSONObject
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun String.toUrlWithQuery(params: List<Pair<String, Any?>>?): String {
    if (params == null) return this

    var query = ""

    for (paramPair in params) {
        if (paramPair.second == null) continue

        query += if (query.isEmpty()) {
            "?" + paramPair.first + "=" + URLEncoder.encode(paramPair.second.toString(), "UTF-8")
        } else {
            "&" + paramPair.first + "=" + URLEncoder.encode(paramPair.second.toString(), "UTF-8")
        }
    }

    return this + query
}

fun String.isHasHtmlTags() = Tools.getHtmlPattern().matcher(this).find()

fun String?.getJsonResponse(key: String): String? {
    if (this.isNullOrBlank()) return null

    return try {
        JSONObject(this).getString(key)
    } catch (e: JSONException) {
        null
    }
}

fun String?.toDateWithTimezone(
    pattern: String = DateUtil.FORMAT_DATETIME_FULL_LENGTH,
    newPattern: String = DateUtil.FORMAT_DATE_TIME_TO_SHOW
): String? {
    if (this == null) return null

    try {
        val read = SimpleDateFormat(pattern, Locale.US)
        val date = read.parse(this) ?: return null
        val write = SimpleDateFormat(newPattern, Locale.getDefault())

        return write.format(date)
    } catch (e: ParseException) {
        return null
    }
}