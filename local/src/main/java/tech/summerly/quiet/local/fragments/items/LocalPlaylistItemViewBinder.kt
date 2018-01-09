package tech.summerly.quiet.local.fragments.items

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.local_item_playlist.view.*
import tech.summerly.quiet.commonlib.bean.Playlist
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.local.R

class LocalPlaylistItemViewBinder(
        private val onItemClick: (Playlist) -> Unit
) : ItemViewBinder<Playlist>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.local_item_playlist, parent, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Playlist) = with(holder.itemView) {
        setOnClickListener {
            onItemClick(item)
        }
        item.coverImageUri ?: R.drawable.local_ic_album_black_24dp.let {
            GlideApp.with(this).load(it).into(image)
        }
        textTitle.text = item.name
        textSubTitle.text = context.getString(R.string.local_playlist_item_subtitle_template, item.musicCount)
    }
}