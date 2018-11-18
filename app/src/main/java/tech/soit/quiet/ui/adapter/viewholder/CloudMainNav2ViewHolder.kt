package tech.soit.quiet.ui.adapter.viewholder

import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.item_cloud_nav_2.view.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.view.RoundRectOutlineProvider
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.support.px

@LayoutId(R.layout.item_cloud_nav_2)
class CloudMainNav2ViewHolder(itemView: View) : BaseViewHolder(itemView) {

    companion object {

        private val imageOutline = RoundRectOutlineProvider(3.px.toFloat())

    }

    init {
        itemView.run {
            imageCover.outlineProvider = imageOutline
            imageCover.clipToOutline = true
        }
    }

    fun set(title: String, image: Any) = itemView.run {
        textTitle.text = title
        ImageLoader.with(this).load(image).into(imageCover)
    }

    fun setPlayCount(playCount: Long) = itemView.run {
        textRightTop.text = playCount.toString()
    }


    fun setIsRightTopVisiable(show: Boolean) {
        itemView.layoutRightTop.isVisible = show
    }


}