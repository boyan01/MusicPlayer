package tech.summerly.quiet.netease.ui.items

import kotlinx.android.synthetic.main.netease_item_record.view.*
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.Record
import tech.summerly.quiet.commonlib.utils.ItemViewBinder2
import tech.summerly.quiet.commonlib.utils.image.setImageUrl
import tech.summerly.quiet.netease.R


/**
 * Created by summer on 18-2-27
 */

internal class RecordItemViewBinder(
        private val onClick: (Music) -> Unit
) : ItemViewBinder2<Record>() {

    override fun onBindViewHolder(holder: ViewHolder, item: Record) = with(holder.itemView) {
        musicView.setMusic(item.music)
        textCount.text = context.getString(R.string.netease_play_count, item.playCount)
        val picUri = item.music.picUri
        if (picUri == null) {
            imageArtwork.setImageResource(R.drawable.common_image_music_disk)
        } else {
            imageArtwork.setImageUrl(picUri)
        }
        setOnClickListener { onClick(item.music) }
    }

    override val layoutId: Int
        get() = R.layout.netease_item_record
}