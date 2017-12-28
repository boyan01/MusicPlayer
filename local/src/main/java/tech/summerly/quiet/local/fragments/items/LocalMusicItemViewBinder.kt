package tech.summerly.quiet.local.fragments.items

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.local_item_music.view.*
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.local.R

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

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.local_item_music, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Music): Unit = with(holder.itemView) {
        if (checkMusicIsPlaying(item)) {
            now_playing_indicator.visible()
        } else {
            now_playing_indicator.invisible()
        }
        setOnClickListener {
            MusicPlayerManager.INSTANCE.getOrCreateSimplePlayer().play(item)
        }
        popup_menu.setOnClickListener {
            log { "popup menu" }
        }
        item.picUri?.let { GlideApp.with(this).load(it).into(image) }
        popup_menu.contentDescription = context.getString(R.string.local_description_music_more, item.title)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            popup_menu.tooltipText = context.getString(R.string.local_tips_more_options)
        }
        text_item_title.text = item.title
        text_item_subtitle.text = item.artist.joinToString("/") { it.name }
        text_item_subtitle_2.text = item.album.name
    }
}