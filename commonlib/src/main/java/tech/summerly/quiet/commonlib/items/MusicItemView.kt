@file:Suppress("NOTHING_TO_INLINE")

package tech.summerly.quiet.commonlib.items

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

    protected open val isShowPlayingIndicator = true
    protected open val isShowMoreActionButton = true
    protected open val isShowQualityIndicator = true
    protected open val isShowMvIndicator = true
    protected open val isShowArtworkImage = true
    protected open val isLongClickReaction = false

    private val currentPlaying
        get() = musicPlayer.current

    public override fun onBindViewHolder(holder: ViewHolder, item: Music): Unit = with(holder.itemView) {
        val canPlay = item.playUri.isNotEmpty()
        setOnClickListener {
            onMusicClick(item)
        }

        showPlayingIndicator(item)
        setPlayable(canPlay)
        textTitle.text = item.title
        textSubTitle.text = item.artistAlbumString()
        showMvIndictor(item)
        showQualityIndicator(item)
        showMoreActionButton(item)
        showArtworkImage(item.picUri)

        if (isLongClickReaction) {
            setOnLongClickListener { showPopupMenu(it, item);true }
        } else {
            setOnLongClickListener(null)
        }
        //textTitle'width need be recalculate
        textTitle.requestLayout()
    }

    private fun View.showPlayingIndicator(music: Music) {
        if (isShowPlayingIndicator && music == currentPlaying) {
            indicatorPlaying.visible()
        } else {
            indicatorPlaying.invisible()
        }
    }

    private fun View.showMoreActionButton(music: Music) {
        if (!isShowMoreActionButton) {
            imageMore.gone()
            imageMore.setOnClickListener(null)
        } else {
            imageMore.visible()
            imageMore.setOnClickListener { showPopupMenu(it, music) }
        }
    }

    //弹出一个菜单选项
    private fun showPopupMenu(anchor: View, music: Music) {
        val menu = popupMenu(anchor, R.menu.popup_music_item) {
            onMorePopupMenuClick(it, music)
            true
        }
        onMorePopupMenuShow(menu)
    }


    private fun View.showArtworkImage(uri: String?) {
        if (!isShowArtworkImage) {
            image.gone()
            return
        }
        //todo 省流量模式
        image.visible()
        val isOnlyLoadFromCache = true && !isWifi()
        GlideApp.with(this).asBitmap()
                .onlyRetrieveFromCache(isOnlyLoadFromCache)
                .load(uri)
                .placeholder(R.drawable.common_image_placeholder_loading)
                .into(image)
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

    private inline fun View.showQualityIndicator(music: Music) {
        val quality = music.getHighestQuality()
        if (!isShowQualityIndicator || quality.isNullOrEmpty()) {
            indicatorQuality.gone()
        } else {
            indicatorQuality.visible()
            indicatorQuality.text = quality
        }
    }

    /**
     * check mv availability
     */
    private inline fun View.showMvIndictor(music: Music) {
        if (!isShowMvIndicator || music.mvId == 0L) {
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