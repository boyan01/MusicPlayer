package tech.summerly.quiet.local.fragments.items

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.local_item_music.view.*
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.simpleMusicPlayer
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.fragments.dialog.LocalPlaylistSelectorFragment
import tech.summerly.quiet.local.utils.delete

/**
 * Created by summer on 17-12-23
 * recycler view item binder for local music
 */
internal class LocalMusicItemViewBinder : ItemViewBinder<Music>() {

    private val musicPlayer = MusicPlayerManager.INSTANCE.getMusicPlayer()

    //to check current music is playing , if true ,will set a playing indicator for this music
    private fun checkMusicIsPlaying(music: Music): Boolean {
        return musicPlayer.getPlayingMusic().value == music
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
            simpleMusicPlayer.play(item)
        }
        popup_menu.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.inflate(R.menu.local_popup_music_item)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.local_popup_music_add_to_next -> {
                        simpleMusicPlayer.insertToNext(item)
                    }
                    R.id.local_popup_music_add_to_playlist -> {
                        LocalPlaylistSelectorFragment(arrayOf(item))
                                .show((context as AppCompatActivity).supportFragmentManager,
                                        "local_playlist_selector_fragment")
                    }
                    R.id.local_popup_music_to_album -> {

                    }
                    R.id.local_popup_music_delete -> {
                        launch(UI + CoroutineExceptionHandler({ _, throwable ->
                            context.toast(context.getString(R.string.local_toast_delete_failed_template, throwable.message))
                            throwable.printStackTrace()
                        })) {
                            val ok = getContext()
                                    .alert(message = getContext().getString(R.string.local_message_delete_template, item.title),
                                            positive = getContext().getString(R.string.local_action_delete))
                            if (ok) {
                                item.delete()
                                getContext().toast(getContext().getString(R.string.local_toast_delete_succeed_template, item.title))
                            }
                        }
                    }
                }
                true
            }
            popupMenu.show()
        }
        (item.picUri ?: R.drawable.local_ic_album_black_24dp)
                .let { GlideApp.with(this).load(it).into(image) }
        popup_menu.contentDescription = context.getString(R.string.local_description_music_more, item.title)
        text_item_title.text = item.title
        text_item_subtitle.text = item.artist.joinToString("/") { it.name }
        text_item_subtitle_2.text = item.album.name
    }
}
