package tech.soit.quiet.ui.adapter.viewholder

import android.view.View
import kotlinx.android.synthetic.main.item_placeholder.view.*
import tech.soit.quiet.R
import tech.soit.quiet.utils.annotation.LayoutId

/**
 * RecyclerView 空白占位
 */
@LayoutId(R.layout.item_placeholder)
class PlaceholderViewHolder(itemView: View) : BaseViewHolder(itemView) {


    override fun applyPrimaryColor(colorPrimary: Int) {
        itemView.progressBar.indeterminateDrawable.setTint(colorPrimary)
    }

}