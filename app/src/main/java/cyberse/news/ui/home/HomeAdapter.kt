package cyberse.news.ui.home

import cyberse.news.R
import cyberse.news.bases.BaseAdapter
import cyberse.news.databinding.ItemCategoryBinding
import cyberse.news.models.Category

class HomeAdapter : BaseAdapter<Category, ItemCategoryBinding>() {

    override fun getLayout() = R.layout.item_category

    override fun onBindViewHolder(holder: Companion.BaseViewHolder<ItemCategoryBinding>, position: Int) {
        holder.binding.item = items[position]
        holder.binding.category.setOnClickListener { listener?.invoke(it, position, items[position]) }
    }

}