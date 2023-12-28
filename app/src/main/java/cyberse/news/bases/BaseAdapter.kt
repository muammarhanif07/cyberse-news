package cyberse.news.bases

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any, VB : ViewDataBinding>
    : RecyclerView.Adapter<BaseAdapter.Companion.BaseViewHolder<VB>>() {

    val items = mutableListOf<T>()
    var listener: ((view: View, position: Int, item: T) -> Unit)? = null

    private var onChangedListener: ((List<T>) -> Unit)? = null
    private var positionListener: ((Int) -> Unit)? = null

    fun setOnClickItemListener(listener: (view: View, position: Int, item: T) -> Unit) {
        this.listener = listener
    }

    fun setOnClickPositionListener(listener: (Int) -> Unit) {
        positionListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<T>?) {
        if (list != null) {
            items.clear()
            items.addAll(list)
            notifyDataSetChanged()
            onChangedListener?.invoke(list)
        }
    }

    fun delete(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
        onChangedListener?.invoke(items)
    }

    abstract fun getLayout(): Int

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder<VB>(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayout(),
            parent,
            false
        )
    )

    companion object {
        class BaseViewHolder<VB : ViewDataBinding>(val binding: VB) :
            RecyclerView.ViewHolder(binding.root)
    }

}