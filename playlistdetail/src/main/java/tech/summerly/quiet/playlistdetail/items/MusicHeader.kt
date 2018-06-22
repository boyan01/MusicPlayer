package tech.summerly.quiet.playlistdetail.items

import android.view.ViewGroup
import kotlinx.android.synthetic.main.pd_header_music.view.*
import tech.summerly.quiet.commonlib.utils.support.TypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.playlistdetail.R


/**
 * author : yangbin10
 * date   : 2018/1/15
 */
internal class MusicHeaderViewBinder : TypedBinder<MusicHeader>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder.from(R.layout.pd_header_music, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: MusicHeader) = with(holder.itemView) {
        setOnClickListener {

        }
        imageMultiSelect.setOnClickListener {

        }
    }

}

internal class MusicHeader(val count: Int)