@file:Suppress("NOTHING_TO_INLINE")

package tech.summerly.quiet.netease.ui.items

import android.annotation.SuppressLint
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.netease_item_music.view.*
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.api.result.PlaylistDetailResultBean

/**
 * author : yangbin10
 * date   : 2018/1/15
 */
internal open class NeteaseMusicItemViewBinder : ItemViewBinder<Music>() {
    override fun onBindViewHolder(holder: ViewHolder, item: Music): Unit = with(holder.itemView) {
        val canPlay = item.playUri.isNotEmpty()
        setOnClickListener {

        }
        checkPlayable(canPlay)
        checkMv(item)
        checkQuality(item)
        textTitle.text = item.title
        textSubTitle.text = item.artistAlbumString()
        imageMore.setOnClickListener {
            val menu = popupMenu(it, R.menu.netease_popup_music_item) {
                onMorePopupMenuClick(it, item)
                true
            }
            onMorePopupMenuShow(menu)
        }
        //textTitle'width need be recalculate
        textTitle.requestLayout()
    }

    private fun View.checkPlayable(canPlay: Boolean) {
        if (!canPlay) {
            textTitle.setTextColor(color(R.color.common_textDisable))
            textSubTitle.setTextColor(color(R.color.common_textDisable))
        } else {
            textTitle.setTextColor(color(R.color.common_text_primary))
            textSubTitle.setTextColor(color(R.color.common_text_secondary))
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

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.netease_item_music, parent, inflater)
    }

}

internal class NeteasePlaylistMusicItemViewBinder : ItemViewBinder<PlaylistDetailResultBean.Track>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.netease_item_music, parent, inflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, item: PlaylistDetailResultBean.Track): Unit = with(holder.itemView) {
        textTitle.text = item.name
        textSubTitle.text =
                (item.al?.name ?: "未知专辑") +
                " - " +
                (item.ar?.joinToString(",") ?: "未知歌手")
        if (item.mv == 0L) {
            indicatorMV.gone()
            indicatorMV.setOnClickListener(null)
        } else {
            indicatorMV.visible()
            indicatorMV.setOnClickListener {
                //todo
            }
        }
        val picUrl = item.al?.picUrl
        if (picUrl == null) {
            image.gone()
        } else {
            image.visible()
            GlideApp.with(this).load(picUrl).into(image)
        }
        textTitle.requestLayout()
    }
}