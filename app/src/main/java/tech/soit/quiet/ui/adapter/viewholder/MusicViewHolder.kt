package tech.soit.quiet.ui.adapter.viewholder

import android.view.View
import kotlinx.android.synthetic.main.item_music_1.view.*
import tech.soit.quiet.model.vo.Music

class MusicViewHolder(itemView: View) : BaseViewHolder(itemView) {


    fun bind(data: Music, position: Int): Unit = with(itemView) {
        //音乐序号
        textPosition.text = position.toString()

        text_item_title.text = data.getTitle()
        text_item_subtitle.text = data.getArtists().joinToString("/") { it.getName() }
        text_item_subtitle_2.text = data.getAlbum().getName()
    }


    /**
     * @param play 播放当前歌曲
     * @param showOptions 展示更多选项
     */
    fun setListener(
            play: () -> Unit,
            showOptions: () -> Unit
    ) = with(itemView) {
        setOnClickListener {
            play()
        }
        iconMore.setOnClickListener {
            showOptions()
        }
    }


    override fun applyPrimaryColor(colorPrimary: Int) {
        itemView.divider_subtitle.setBackgroundColor(colorPrimary)
    }

}