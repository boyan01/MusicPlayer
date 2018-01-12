package tech.summerly.quiet.commonlib.items

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.common_item_a.view.*
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.ItemViewBinder

/**
 * author : yangbin10
 * date   : 2018/1/10
 *
 * common item a : with an icon , a title
 * and this items (icon,title) are horizontal
 */
open class CommonItemAViewBinder(
        private val onItemClick: (CommonItemA) -> Unit
) : ItemViewBinder<CommonItemA>() {
    override fun onBindViewHolder(holder: ViewHolder, item: CommonItemA) = with(holder.itemView) {
        textTitle.text = item.title
        GlideApp.with(this).load(item.icon).into(image)
        setOnClickListener {
            onItemClick(item)
        }
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.common_item_a, parent, inflater).also {
            onCreateViewHolder(it.itemView.image, it.itemView.textTitle)
        }
    }

    protected open fun onCreateViewHolder(image: AppCompatImageView, title: AppCompatTextView) {
        @Suppress("DEPRECATION")
        image.setColorFilter(image.resources.getColor(R.color.common_color_primary))
    }

}

class CommonItemA(
        val title: String,
        val icon: Any
)