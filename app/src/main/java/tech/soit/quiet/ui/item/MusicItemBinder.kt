package tech.soit.quiet.ui.item

import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.item_music.view.*
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.ui.item.MusicItemViewBinder.PlayingIndicatorObserver
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.KViewHolder
import tech.soit.quiet.utils.TypeLayoutRes
import tech.soit.quiet.utils.component.ImageLoader


/**
 * item music
 *
 * NOTE: to perceive the playing music change , need use [PlayingIndicatorObserver]
 *
 * @param token the token of playlist, to check music if playing
 * @param onClick callback of music been clicked
 * @param onPlayingItemShowHide callback of playing music item show/hide
 */
@TypeLayoutRes(R.layout.item_music)
class MusicItemViewBinder(
        private val lifecycleObserver: LifecycleOwner,
        private val token: String,
        private val onClick: (view: View, music: Music) -> Unit,
        private val onPlayingItemShowHide: ((show: Boolean) -> Unit)? = null
) : KItemViewBinder<Music>() {

    /**
     * save current playing music index
     */
    private var playingViewHolder: KViewHolder? = null

    private val playingIndicatorObserver = PlayingIndicatorObserver()
    private var observerAdded = false

    @ColorInt
    var colorIndicator: Int = 0

    override fun onBindViewHolder(holder: KViewHolder, item: Music) = with(holder.itemView) {
        val isPlaying = isPlaying(item)
        if (isPlaying) {
            if (playingViewHolder != null && playingViewHolder != holder) {
                playingViewHolder!!.itemView.indicatorPlaying.isGone = true
            }
            playingViewHolder = holder
            indicatorPlaying.isVisible = true
            if (colorIndicator != 0) {
                indicatorPlaying.setBackgroundColor(colorIndicator)
            }
            onPlayingItemShowHide?.invoke(true)
        } else {
            indicatorPlaying.isGone = true
        }
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

    override fun onViewAttachedToWindow(holder: KViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (!observerAdded) {
            MusicPlayerManager.playingMusic.observe(lifecycleObserver, playingIndicatorObserver)
            observerAdded = true
        }
    }

    override fun onViewDetachedFromWindow(holder: KViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder == playingViewHolder) {
            onPlayingItemShowHide?.invoke(false)
            playingViewHolder = null
        }
    }

    private fun isPlaying(music: Music): Boolean {
        return MusicPlayerManager.musicPlayer.playlist.token == token
                && MusicPlayerManager.musicPlayer.playlist.current == music
    }

    private inner class PlayingIndicatorObserver : Observer<Music?> {

        override fun onChanged(playing: Music?) {
            playing ?: return
            val new = adapter.items.indexOf(playing)
            GlobalScope.launch(Dispatchers.Main) { adapter.notifyItemChanged(new) }
        }

    }

}