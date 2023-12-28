package cyberse.news.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cyberse.news.R

enum class Category(@StringRes val menu: Int, @DrawableRes val icon: Int) {
    APPLE(R.string.apple_caption, R.drawable.ic_logo_apple),
    TESLA(R.string.tesla_caption, R.drawable.ic_logo_tesla),
    BUSINESS(R.string.business_caption, R.drawable.ic_logo_us),
    TECH_CRUNCH(R.string.tech_crunch_caption, R.drawable.ic_logo_techcrunch),
    WALL_STREET_JOURNAL(R.string.wall_street_journal_caption, R.drawable.ic_logo_wsj)
}