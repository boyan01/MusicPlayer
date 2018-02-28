@file:Suppress("NOTHING_TO_INLINE")

package tech.summerly.quiet.commonlib.items

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_music.view.*
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.musicPlayer
import tech.summerly.quiet.commonlib.utils.*

/**
 * Created by summer on 18-2-19
 */
open class MusicItemViewBinder(
        private val onMusicClick: (Music) -> Unit
) : ItemViewBinder<Music>() {

    private val currentPlaying
        get() = musicPlayer.current

    public override fun onBindViewHolder(holder: ViewHolder, item: Music): Unit = with(holder.itemView) {
        val canPlay = item.playUri.isNotEmpty()
        setOnClickListener {
            onMusicClick(item)
        }
        if (item == currentPlaying) {
            indicatorPlaying.visible()
        } else {
            indicatorPlaying.invisible()
        }
        setPlayable(canPlay)
        checkMv(item)
        checkQuality(item)
        textTitle.text = item.title
        textSubTitle.text = item.artistAlbumString()
        imageMore.setOnClickListener {
            val menu = popupMenu(it, R.menu.popup_music_item) {
                onMorePopupMenuClick(it, item)
                true
            }
            onMorePopupMenuShow(menu)
        }
        displayMusicImage(image, item.picUri)
        //textTitle'width need be recalculate
        textTitle.requestLayout()
    }


    private fun View.displayMusicImage(imageView: AppCompatImageView, uri: String?) {
        //todo 省流量模式
        val isOnlyLoadFromCache = true && !isWifi()
        GlideApp.with(this).asBitmap()
                .onlyRetrieveFromCache(isOnlyLoadFromCache)
                .load(uri)
                .placeholder(R.drawable.common_image_placeholder_loading)
                .into(imageView)
    }

    private fun View.setPlayable(canPlay: Boolean) {
        if (!canPlay) {
            textTitle.setTextColor(color(R.color.common_textDisable))
            textSubTitle.setTextColor(color(R.color.common_textDisable))
        } else {
            textTitle.setTextColor(color(R.color.common_text_primary))
            textSubTitle.setTextColor(color(R.color.color_text_secondary))
        }
    }

    protected open fun onMorePopupMenuShow(menu: PopupMenu) {

    }

    protected open fun onMorePopupMenuClick(item: MenuItem, music: Music) {

    }

    private inline fun View.checkQuality(music: Music) {
        val quality = music.getHighestQuality()
        if (quality.isNullOrEmpty()) {
            indicatorQuality.gone()
        } else {
            indicatorQuality.visible()
            indicatorQuality.text = quality
        }
    }

    /**
     * check mv availability
     */
    private inline fun View.checkMv(music: Music) {
        if (music.mvId == 0L) {
            indicatorMV.gone()
            indicatorMV.setOnClickListener(null)
        } else {
            indicatorMV.visible()
            indicatorMV.setOnClickListener {
                //todo
            }
        }
    }

    public override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.item_music, parent, inflater)
    }

}