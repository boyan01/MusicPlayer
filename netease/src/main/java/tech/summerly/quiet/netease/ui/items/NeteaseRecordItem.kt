package tech.summerly.quiet.netease.ui.items

import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.Record
import tech.summerly.quiet.commonlib.items.MusicItemViewBinder
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.commonlib.utils.getAttrColor
import tech.summerly.quiet.commonlib.utils.getScreenWidth
import tech.summerly.quiet.netease.R

/**
 * Created by summer on 18-2-27
 */

internal class NeteaseRecordItemViewBinder(
        onMusicItemClick: (Music) -> Unit
) : ItemViewBinder<Record>() {

    private val musicItemViewBinder = object : MusicItemViewBinder(onMusicItemClick) {
        override val isShowArtworkImage: Boolean = false
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val context = inflater.context
        val viewHolder = musicItemViewBinder.onCreateViewHolder(inflater, parent)
        val itemBackground = View(context)
        itemBackground.setBackgroundColor(context.getAttrColor(R.attr.colorPrimary))
        itemBackground.alpha = 0.2f
        (viewHolder.itemView as ConstraintLayout).addView(itemBackground, 0)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Record) {
        musicItemViewBinder.onBindViewHolder(holder, item.music)

        val itemview = holder.itemView as ConstraintLayout
        val translation = getScreenWidth() * ((100 - item.score) / 100f)
        itemview.getChildAt(0).translationX = -translation
    }

}