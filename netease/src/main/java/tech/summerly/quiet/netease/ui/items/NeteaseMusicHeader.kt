package tech.summerly.quiet.netease.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.netease_item_playlist.view.*
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.netease.R


/**
 * author : yangbin10
 * date   : 2018/1/15
 */
internal class NeteaseMusicHeaderViewBinder : ItemViewBinder<NeteaseMusicHeader>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.netease_header_music, parent, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: NeteaseMusicHeader) = with(holder.itemView) {
        setOnClickListener {

        }
        imageAction.setOnClickListener {

        }
    }

}


internal class NeteaseMusicHeader(val count: Int)