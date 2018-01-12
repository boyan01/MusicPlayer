package tech.summerly.quiet.netease.ui.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.netease_header_playlist.view.*
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.commonlib.utils.PopupMenu
import tech.summerly.quiet.commonlib.utils.invisible
import tech.summerly.quiet.commonlib.utils.visible
import tech.summerly.quiet.netease.R

/**
 * author : yangbin10
 * date   : 2018/1/12
 */
internal class NeteasePlaylistHeaderViewBinder(
        private val onHeaderClick: (header: NeteasePlaylistHeader, position: Int) -> Unit
) : ItemViewBinder<NeteasePlaylistHeader>() {
    override fun onBindViewHolder(holder: ViewHolder, item: NeteasePlaylistHeader) {
        holder.itemView.setOnClickListener {
            onHeaderClick(item, holder.adapterPosition)
        }
        holder.itemView.headerTitle.text = item.title
        holder.itemView.headerMore.setOnClickListener {
            createHeaderMenu(it)
        }
        if (item.isExpanded) {
            holder.itemView.iconExpandIndicator.rotation = 90f
        } else {
            holder.itemView.iconExpandIndicator.rotation = 0f
        }
        if (item.isLoading) {
            holder.itemView.iconExpandIndicator.invisible()
            holder.itemView.progressBar.visible()
        } else {
            holder.itemView.iconExpandIndicator.visible()
            holder.itemView.progressBar.invisible()
        }
    }

    private fun createHeaderMenu(anchor: View) {
        PopupMenu(anchor, R.menu.netease_popup_playlist_header) {
            true
        }
    }


    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.netease_header_playlist, parent, inflater)
    }

}

internal class NeteasePlaylistHeader(val title: String,
                                     var isExpanded: Boolean,
                                     var isLoading: Boolean = true)