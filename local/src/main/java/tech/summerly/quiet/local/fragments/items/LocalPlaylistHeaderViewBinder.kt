package tech.summerly.quiet.local.fragments.items

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.local_item_playlist_header.view.*
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.local.R

/**
 * Created by summer on 17-12-27
 */
internal class LocalPlaylistHeaderViewBinder : ItemViewBinder<LocalPlaylistHeaderViewBinder.PlaylistHeader>() {
    override fun onBindViewHolder(holder: ViewHolder, item: PlaylistHeader) {
        holder.itemView.actionMore.setOnClickListener {
            log { "todo : more action for playlist header" }
        }
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.local_item_playlist_header, parent, inflater)
    }


    class PlaylistHeader
}