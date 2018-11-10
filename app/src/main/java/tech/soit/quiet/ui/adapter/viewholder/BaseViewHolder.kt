package tech.soit.quiet.ui.adapter.viewholder

import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    open fun applyPrimaryColor(@ColorInt colorPrimary: Int) {

    }

}