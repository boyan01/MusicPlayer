package tech.soit.quiet.ui.item

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.item_music.view.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder
import tech.soit.typed.adapter.annotation.TypeLayoutResource

@TypeLayoutResource(R.layout.item_music)
class MusicItemBinder(
        private val token: String,
        private val onClick: (view: View, music: Music) -> Unit
) : TypedBinder<Music>() {

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
        indicatorPlaying.isVisible = isPlaying(item)
        setOnClickListener {
            onClick(holder.itemView, item)
        }
        popup_menu.setOnClickListener {
            //
        }
        text_item_title.text = item.title
        text_item_subtitle.text = item.artists.joinToString("/") { it.name }
        text_item_subtitle_2.text = item.album.title
    }

    private fun isPlaying(music: Music): Boolean {
        return MusicPlayerManager.musicPlayer.playlist.token == token
                && MusicPlayerManager.musicPlayer.playlist.current == music
    }

}