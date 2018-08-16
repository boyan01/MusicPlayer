package tech.soit.quiet.ui.item

import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.item_music.view.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder
import tech.soit.typed.adapter.annotation.TypeLayoutResource

@TypeLayoutResource(R.layout.item_music)
class MusicItemBinder : TypedBinder<Music>() {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Music>() {
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem == newItem
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, item: Music) = with(holder.itemView) {
        now_playing_indicator.isVisible = item.isPlaying()
        setOnClickListener {
            //
        }
        popup_menu.setOnClickListener {
            //
        }
        text_item_title.text = item.title
        text_item_subtitle.text = item.artists.joinToString("/") { it.name }
        text_item_subtitle_2.text = item.album.title
    }

    private fun Music.isPlaying(): Boolean {
        return false
    }

}