package tech.summerly.quiet.netease.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.netease_item_playlist.view.*
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.api.result.PlaylistResultBean

/**
 * Created by summer on 18-1-12
 */
internal class NeteasePlaylistItemViewBinder : ItemViewBinder<PlaylistResultBean.PlaylistBean>() {
    override fun onBindViewHolder(holder: ViewHolder, item: PlaylistResultBean.PlaylistBean): Unit = with(holder.itemView) {
        textTitle.text = item.name
        textSubTitle.text = context.getString(R.string.netease_playlist_subtitle_template, item.trackCount)
        GlideApp.with(this).load(item.coverImgUrl).into(imageCover)
        setOnClickListener {
            ARouter.getInstance().build("/netease/playlist_detail")
                    .withLong("playlist_id", item.id)
                    .withObject("playlist_detail", item)
                    .navigation(context)
        }
        imageAction.setOnClickListener {

        }
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.netease_item_playlist, parent, inflater)
    }

}