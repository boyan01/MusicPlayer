package tech.soit.quiet.ui.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_music.view.*
import tech.soit.quiet.AppContext
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.KViewHolder
import tech.soit.quiet.utils.TypeLayoutRes
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.support.attrValue


/**
 * item music
 *
 * @param token the token of playlist, to check music if playing
 * @param onClick callback of music been clicked
 * @param onPlayingItemShowHide callback of playing music item show/hide
 */
@TypeLayoutRes(R.layout.item_music)
class MusicItemViewBinder(
        private val token: String,
        private val onClick: (view: View, music: Music) -> Unit,
        private val onPlayingItemShowHide: ((show: Boolean) -> Unit)? = null
) : KItemViewBinder<Music>() {

    /**
     * save current playing music index
     */
    private var playingViewHolder: MusicViewHolder? = null

    @ColorInt
    var colorIndicator: Int = AppContext.attrValue(R.attr.colorPrimary)

    fun applyPrimaryColor(@ColorInt color: Int) {
        colorIndicator = color
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): KViewHolder {
        return MusicViewHolder(inflater.inflate(R.layout.item_music, parent, false))
    }

    override fun onBindViewHolder(holder: KViewHolder, item: Music) = with(holder.itemView) {
        holder as MusicViewHolder
        if (colorIndicator != 0) {
            holder.setPrimaryColor(colorIndicator)
        }

        val isPlaying = isPlaying(item)
        if (isPlaying) {
            if (playingViewHolder != holder) {
                playingViewHolder?.setIsPlaying(false)
            }
            playingViewHolder = holder
            onPlayingItemShowHide?.invoke(true)
        }
        holder.setIsPlaying(isPlaying)

        ImageLoader.with(this).load(item.getAlbum().getCoverImageUrl()).centerCrop().into(image)
        setOnClickListener {
            onClick(holder.itemView, item)
        }
        popup_menu.setOnClickListener {
            //
        }
        text_item_title.text = item.getTitle()
        text_item_subtitle.text = item.getArtists().joinToString("/") { it.getName() }
        text_item_subtitle_2.text = item.getAlbum().getName()
    }

    override fun onViewDetachedFromWindow(holder: KViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder == playingViewHolder) {
            onPlayingItemShowHide?.invoke(false)
        }
    }

    private fun isPlaying(music: Music): Boolean {
        return MusicPlayerManager.musicPlayer.playlist.token == token
                && MusicPlayerManager.musicPlayer.playlist.current == music
    }

    /**
     * 设置当前播放的音乐
     */
    fun setCurrentPlaying(music: Music?, recyclerView: RecyclerView) {
        val index = adapter.items.indexOf(music)
        if (index == -1) {
            //如果当前列表不存在此歌曲，那么置空 playingViewHolder
            playingViewHolder?.setIsPlaying(false)
            playingViewHolder = null
            return
        }
        val holder = recyclerView.findViewHolderForAdapterPosition(index) as? MusicViewHolder
        if (holder == playingViewHolder) {
            //不需要再走流程了，因为播放的item没有改变
            return
        }
        playingViewHolder?.setIsPlaying(false)

        playingViewHolder = holder
        holder?.setIsPlaying(true)
    }

    class MusicViewHolder(itemView: View) : KViewHolder(itemView) {

        fun setIsPlaying(isPlaying: Boolean) {
            itemView.indicatorPlaying.isGone = !isPlaying
        }

        /**
         * reset item view primary color
         */
        fun setPrimaryColor(@ColorInt color: Int) {
            itemView.indicatorPlaying.setBackgroundColor(color)
            itemView.divider_subtitle.setBackgroundColor(color)
        }

    }

}