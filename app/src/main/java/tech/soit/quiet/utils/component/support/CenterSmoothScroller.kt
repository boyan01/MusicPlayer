package tech.soit.quiet.utils.component.support

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 *
 * a smooth scroller make RecyclerView could scroll to center
 *
 * @author : summer
 * @date : 18-9-1
 */
class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {

    override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int): Int {
        return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
    }
}