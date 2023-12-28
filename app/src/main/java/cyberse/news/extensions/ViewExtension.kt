package cyberse.news.extensions

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.skydoves.androidveil.VeilRecyclerFrameView
import cyberse.news.R
import cyberse.news.constants.ErrorCode
import cyberse.news.customview.RecyclerViewDividerItemDecoration
import cyberse.news.utils.Tools

fun View.getErrorApiSnackbar(errorCode: Int, onRetry: () -> Unit): Snackbar {
    val msgId = Tools.getErrorResId(errorCode)
    val duration = when (errorCode) {
        ErrorCode.NO_INTERNET_CONNECTION -> Snackbar.LENGTH_INDEFINITE
        else -> Snackbar.LENGTH_LONG
    }
    val snackbar = Snackbar.make(this, msgId, duration)
    snackbar.setBackgroundTint(context.getColor(R.color.red))
    snackbar.setActionTextColor(context.getColor(R.color.white))

    when (errorCode) {
        ErrorCode.NO_INTERNET_CONNECTION, ErrorCode.INTERNAL_SERVER_ERROR -> {
            snackbar.setAction(R.string.retry) { onRetry.invoke() }
        }
    }

    return snackbar
}

fun VeilRecyclerFrameView.toLinear(
    viewAdapter: RecyclerView.Adapter<*>,
    orientation: Int = RecyclerView.VERTICAL,
    isHasFixedSize: Boolean = true,
    withDivider: Boolean = false
) {
    setAdapter(viewAdapter)
    setLayoutManager(LinearLayoutManager(context, orientation, false))
    addVeiledItems(6)
    getRecyclerView().apply {
        setHasFixedSize(isHasFixedSize)
        clipToPadding = false

        if (withDivider) addItemDecoration(RecyclerViewDividerItemDecoration(context))
    }
}

fun View.visibleOrGone(isVisible: Boolean) { visibility = if (isVisible) View.VISIBLE else View.GONE }