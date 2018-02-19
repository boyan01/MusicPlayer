@file:Suppress("NOTHING_TO_INLINE")

package tech.summerly.quiet.netease.ui.items

import android.support.v7.widget.PopupMenu
import android.view.MenuItem
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.items.MusicItemViewBinder

/**
 * author : yangbin10
 * date   : 2018/1/15
 */
internal open class NeteaseMusicItemViewBinder(
        onMusicClick: (Music) -> Unit
) : MusicItemViewBinder(onMusicClick) {

    override fun onMorePopupMenuShow(menu: PopupMenu) {

    }

    override fun onMorePopupMenuClick(item: MenuItem, music: Music) {

    }
}