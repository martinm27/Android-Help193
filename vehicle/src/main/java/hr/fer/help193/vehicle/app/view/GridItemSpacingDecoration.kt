package hr.fer.help193.vehicle.app.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION

/**
 * Adds specified offset (margin) between items (doesn't include edges) and at the bottom.
 *
 * Basic logic pulled from https://stackoverflow.com/a/30701422/2174252 and adjusted to work properly with different column spans.
 *
 * Due to bug related to grid items exiting view and losing padding, view margins are used as an offset instead.
 */
class GridItemSpacingDecoration(context: Context, @DimenRes private val itemOffsetRes: Int) : RecyclerView.ItemDecoration() {

    private val itemOffset = context.resources.getDimensionPixelSize(itemOffsetRes)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view)
        if (position == NO_POSITION) {
            return
        }

        with(parent.layoutManager as GridLayoutManager) {
            val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
            val spanSize = spanSizeLookup.getSpanSize(position)

            val marginLeft = spanIndex * itemOffset / spanCount
            val marginRight = itemOffset - (spanIndex + spanSize) * itemOffset / spanCount
            view.layoutParams = (view.layoutParams as GridLayoutManager.LayoutParams).apply {
                setMargins(marginLeft, 0, marginRight, itemOffset)
            }
        }
    }
}
