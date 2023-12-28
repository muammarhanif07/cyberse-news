package cyberse.news.utils

import androidx.annotation.StringRes
import cyberse.news.R
import cyberse.news.constants.ErrorCode
import java.util.regex.Pattern

object Tools {

    @StringRes
    fun getErrorResId(code: Int): Int {
        val map = mapOf(
            Pair(ErrorCode.NO_INTERNET_CONNECTION, R.string.error_no_internet)
        ).withDefault {
            R.string.error_internal_server
        }

        return map.getValue(code)
    }

    fun getHtmlPattern(): Pattern = Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^'\">])*>")

}