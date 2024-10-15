package id.overlogic.devent.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // Set spacing between all items
        outRect.left = space
        outRect.right = space
        outRect.bottom = space

        // Add top spacing only for the first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space
        }
    }
}
