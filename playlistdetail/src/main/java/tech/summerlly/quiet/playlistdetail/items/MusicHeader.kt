package tech.summerlly.quiet.playlistdetail.items

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pd_header_music.view.*
import tech.summerlly.quiet.playlistdetail.R
import tech.summerly.quiet.commonlib.utils.ItemViewBinder


/**
 * author : yangbin10
 * date   : 2018/1/15
 */
internal class MusicHeaderViewBinder : ItemViewBinder<MusicHeader>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.pd_header_music, parent, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: MusicHeader) = with(holder.itemView) {
        setOnClickListener {

        }
        imageMultiSelect.setOnClickListener {

        }
    }

}

internal class MusicHeader(val count: Int)