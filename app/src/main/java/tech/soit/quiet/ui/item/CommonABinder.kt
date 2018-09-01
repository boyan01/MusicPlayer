package tech.soit.quiet.ui.item

import android.graphics.drawable.ColorDrawable
import androidx.annotation.DrawableRes
import kotlinx.android.synthetic.main.item_common_a.view.*
import tech.soit.quiet.R
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder
import tech.soit.typed.adapter.annotation.TypeLayoutResource

/**
 * @author : summer
 * @date : 18-9-1
 */
@TypeLayoutResource(R.layout.item_common_a)
class CommonAItemBinder : TypedBinder<CommonAItem>() {


    override fun onBindViewHolder(holder: ViewHolder, item: CommonAItem) = with(holder.itemView) {
        when {
            item.imageResource != 0 -> image.setImageResource(item.imageResource)
            item.imageUrl != null -> ImageLoader.with(image).load(item.imageUrl).into(image)
            else -> image.setImageDrawable(ColorDrawable(context.attrValue(R.attr.colorPrimary)))
        }
        textTitle.text = item.title
        textCaption.text = item.caption
    }

}


class CommonAItem(
        val title: String,
        val caption: String
) {

    constructor(@DrawableRes image: Int, title: String, caption: String) : this(title, caption) {
        imageResource = image
    }

    constructor(image: String, title: String, caption: String) : this(title, caption) {
        imageUrl = image
    }

    var imageResource: Int = 0
        private set

    var imageUrl: String? = null
        private set

}