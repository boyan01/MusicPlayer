package tech.summerly.quiet.commonlib.fragments.items

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.common_item_playing_music.view.*
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.musicPlayer
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.commonlib.utils.gone
import tech.summerly.quiet.commonlib.utils.visible

/**
 * author: summerly
 * email: yangbinyhbn@gmail.com
 */
internal class PlayingMusicItemViewBinder : ItemViewBinder<Music>() {

    private val current: Music?
        get() = musicPlayer.current

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.common_item_playing_music, parent, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Music) = with(holder.itemView) {
        title.text = item.title
        @SuppressLint("SetTextI18n")
        subtitle.text = "(${item.artistAlbumString()})"
        setOnClickListener {
            musicPlayer.play(item)
        }
        if (current == item) {
            indicatorPlaying.visible()
            buttonLink.gone()
        } else {
            indicatorPlaying.gone()
            buttonLink.gone()
        }
        buttonClear.setOnClickListener {
            asyncUI {
                if (item == current) {
                    musicPlayer.exit()
                    musicPlayer.playlist.current = musicPlayer.playlist.getNextMusic()
                }
                musicPlayer.playlist.remove(item)
                adapter.notifyItemRemoved(holder.adapterPosition)
            }
        }

        buttonLink.setOnClickListener {
            //todo
        }
        subtitle.requestLayout()
    }
}