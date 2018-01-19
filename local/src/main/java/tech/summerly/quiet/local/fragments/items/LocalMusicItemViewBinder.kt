package tech.summerly.quiet.local.fragments.items

import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.local_item_music.view.*
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.musicPlayer
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.fragments.dialog.LocalPlaylistSelectorFragment
import tech.summerly.quiet.local.utils.showMusicDeleteDialog

/**
 * Created by summer on 17-12-23
 * recycler view item binder for local music
 */
internal class LocalMusicItemViewBinder(
        private val onMusicItemClick: (music: Music) -> Unit
) : ItemViewBinder<Music>() {


    //to check current music is playing , if true ,will set a playing indicator for this music
    private fun checkMusicIsPlaying(music: Music): Boolean {
        return musicPlayer.current == music
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.local_item_music, parent, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Music): Unit = with(holder.itemView) {
        if (checkMusicIsPlaying(item)) {
            now_playing_indicator.visible()
        } else {
            now_playing_indicator.invisible()
        }
        setOnClickListener {
            onMusicItemClick(item)
        }
        popup_menu.setOnClickListener {
            popupMenu(it, R.menu.local_popup_music_item) {
                when (it.itemId) {
                    R.id.local_popup_music_add_to_next -> {
                        musicPlayer.playlistProvider.insertToNext(item)
                    }
                    R.id.local_popup_music_add_to_playlist -> {
                        LocalPlaylistSelectorFragment(arrayOf(item))
                                .show((context as AppCompatActivity).supportFragmentManager,
                                        "local_playlist_selector_fragment")
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
        popup_menu.contentDescription = context.getString(R.string.local_description_music_more, item.title)
        text_item_title.text = item.title
        text_item_subtitle.text = item.artist.joinToString("/") { it.name }
        text_item_subtitle_2.text = item.album.name
    }
}
