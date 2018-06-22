package tech.summerly.quiet.local.ui.binder

import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.local_item_music.view.*
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.getPictureUrl
import tech.summerly.quiet.commonlib.utils.popupMenu
import tech.summerly.quiet.commonlib.utils.support.TypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.ui.MusicListFragment
import tech.summerly.quiet.local.utils.showMusicDeleteDialog

internal class MusicItemBinder : TypedBinder<IMusic>() {

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

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder.from(R.layout.local_item_music, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: IMusic) = with(holder.itemView) {
        now_playing_indicator.isVisible = item.isPlaying()
        setOnClickListener {
            MusicPlayerManager.play(MusicListFragment.TOKEN,
                    adapter.list.filterIsInstance(IMusic::class.java),
                    item)
        }
        popup_menu.setOnClickListener {
            popupMenu(it, R.menu.local_popup_music_item) {
                when (it.itemId) {
                    R.id.local_popup_music_add_to_next -> {
                        MusicPlayerManager.player.playlist.insertToNext(item)
                    }
                    R.id.local_popup_music_to_album -> {

                    }
                    R.id.local_popup_music_delete -> {
                        showMusicDeleteDialog(item)
                    }
                }
                true
            }
        }
        GlideApp.with(this).load(item.getPictureUrl()).into(image)
        text_item_title.text = item.title
        text_item_subtitle.text = item.artist.joinToString("/") { it.name() }
        text_item_subtitle_2.text = item.album.name()
    }

    private fun IMusic.isPlaying(): Boolean {
        val playlist = MusicPlayerManager.player.playlist
        return playlist.token == MusicListFragment.TOKEN && playlist.current == this
    }

}