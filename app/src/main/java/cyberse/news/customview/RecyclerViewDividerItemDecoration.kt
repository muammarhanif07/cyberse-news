package cyberse.news.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cyberse.news.R
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
internal class RecyclerViewDividerItemDecoration(val context: Context) : RecyclerView.ItemDecoration() {

    private val mDivider = ContextCompat.getDrawable(context, R.drawable.divider_item)
    private val mBounds = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount

        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + child.translationY.roundToInt()
            val top = bottom - (mDivider?.intrinsicHeight ?: 1)

            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
        }

        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            outRect.setEmpty()
        } else {
            outRect[0, 0, 0] = mDivider?.intrinsicHeight ?: 1
        }
    }

}