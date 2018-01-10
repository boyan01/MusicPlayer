package tech.summerly.quiet.commonlib.items

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.common_item_a.view.*
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.commonlib.utils.gone
import tech.summerly.quiet.commonlib.utils.visible

/**
 * author : yangbin10
 * date   : 2018/1/10
 *
 * common item a : with an icon , a title , a subtitle
 * and this items (icon,title,subtitle) are horizontal
 * sub title is optional , if set subtitle is null , the the subtitle view will gone
 */
class CommonItemAViewBinder(
        private val onItemClick: (CommonItemA) -> Unit,
        @ColorRes private val colorFilter: Int = R.color.common_color_primary
) : ItemViewBinder<CommonItemA>() {
    override fun onBindViewHolder(holder: ViewHolder, item: CommonItemA) = with(holder.itemView) {
        textTitle.setText(item.title)
        @Suppress("DEPRECATION")
        image.setColorFilter(resources.getColor(colorFilter))
        image.setImageResource(item.icon)
        if (item.subtitle == null) {
            textSubtitle.gone()
        } else {
            textSubtitle.visible()
            textSubtitle.text = item.subtitle
        }
        setOnClickListener {
            onItemClick(item)
        }
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.common_item_a, parent, inflater)
    }

}

class CommonItemA(
        @StringRes val title: Int,
        @DrawableRes val icon: Int,
        val subtitle: String? = null
)