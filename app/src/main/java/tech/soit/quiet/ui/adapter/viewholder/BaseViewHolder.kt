package tech.soit.quiet.ui.adapter.viewholder

import android.view.View
import androidx.annotation.ColorInt
import tech.soit.quiet.utils.KViewHolder

open class BaseViewHolder(itemView: View) : KViewHolder(itemView) {


    open fun applyPrimaryColor(@ColorInt colorPrimary: Int) {

    }

}