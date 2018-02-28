package tech.summerly.quiet.netease.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.Record
import tech.summerly.quiet.commonlib.items.MusicItemViewBinder
import tech.summerly.quiet.commonlib.utils.ItemViewBinder

/**
 * Created by summer on 18-2-27
 */

internal class NeteaseRecordItemViewBinder(
        onMusicItemClick: (Music) -> Unit
) : ItemViewBinder<Record>() {

    private val musicItemViewBinder = MusicItemViewBinder(onMusicItemClick)

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return musicItemViewBinder.onCreateViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Record) {
        musicItemViewBinder.onBindViewHolder(holder, item.music)
    }

}