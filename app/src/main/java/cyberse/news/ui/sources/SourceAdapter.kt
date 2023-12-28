package cyberse.news.ui.sources

import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import cyberse.news.R
import cyberse.news.bases.BaseListAdapter
import cyberse.news.databinding.ItemSourceBinding
import cyberse.news.extensions.visibleOrGone
import cyberse.news.models.Article

class SourceAdapter : BaseListAdapter<Article, ItemSourceBinding>(DiffCallback()), Filterable {

    var mListRef: List<Article> = emptyList()
        private set

    override fun getLayout() = R.layout.item_source

    override fun onBindViewHolder(holder: Companion.ViewHolder<ItemSourceBinding>, position: Int) {
        with(holder.binding) {
            item = getItem(position)
            emptyView.visibleOrGone(position == currentList.indices.first)
            source.setOnClickListener { listener?.invoke(getItem(position)) }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(char: CharSequence?): FilterResults {
                val filtered = if (char.isNullOrEmpty()) {
                    mListRef
                } else {
                    mListRef.filter {
                        it.source?.name?.contains(char, true) == true
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filtered
                filterResults.count = filtered.size

                return filterResults
            }

            @Suppress("UNCHECKED_CAST", "NotifyDataSetChanged")
            override fun publishResults(char: CharSequence, filterResults: FilterResults) {
                submitList(filterResults.values as List<Article>)
                notifyDataSetChanged()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem.source?.id == newItem.source?.id
    }

    fun addAll(articles: List<Article>) {
        if (!containLastValue(articles)) {
            val list = currentList.toMutableList()
            list.addAll(articles)

            submitList(list)
            mListRef = list
        }
    }

    fun clearAll() {
        submitList(null)
        mListRef = emptyList()
    }

    private fun containLastValue(articles: List<Article>): Boolean {
        return if (currentList.isNotEmpty()) {
            articles.find { it == currentList[currentList.lastIndex] } != null
        } else {
            false
        }
    }

}