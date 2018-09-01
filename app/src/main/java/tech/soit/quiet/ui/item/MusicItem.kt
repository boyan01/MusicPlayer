package tech.soit.quiet.ui.item

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.item_music.view.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder
import tech.soit.typed.adapter.annotation.TypeLayoutResource


/**
 * item binder for Music Playlist
 *
 * @param token playlist Token
 * @param onClick on item clicked
 * @param onPlayingItemShowHide call back when playing item displayed or not displayed
 *
 */
@TypeLayoutResource(R.layout.item_music)
class MusicItemBinder(
        private val token: String,
        private val onClick: (view: View, music: Music) -> Unit,
        private val onPlayingItemShowHide: ((show: Boolean) -> Unit)? = null
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

    /**
     * save current playing music index
     */
    private var currentPlayingPosition = -1


    override fun onBindViewHolder(holder: ViewHolder, item: Music) = with(holder.itemView) {
        val isPlaying = isPlaying(item)
        if (isPlaying) {
            if (currentPlayingPosition != -1) {
                val position = currentPlayingPosition
                post {
                    adapter.notifyItemChanged(position)
                }
            }
            currentPlayingPosition = holder.adapterPosition
            indicatorPlaying.isVisible = true
        } else {
            indicatorPlaying.isGone = true
        }
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

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.adapterPosition == currentPlayingPosition) {
            onPlayingItemShowHide?.invoke(false)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.adapterPosition == currentPlayingPosition) {
            onPlayingItemShowHide?.invoke(true)
        }
    }

    private fun isPlaying(music: Music): Boolean {
        return MusicPlayerManager.musicPlayer.playlist.token == token
                && MusicPlayerManager.musicPlayer.playlist.current == music
    }

}