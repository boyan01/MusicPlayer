package tech.summerly.quiet.netease.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.netease_item_playlist.view.*
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.commonlib.utils.toPictureUrl
import tech.summerly.quiet.constraints.PlaylistDetail
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.utils.NeteasePlaylistDetailProvider
import tech.summerly.quiet.service.netease.result.PlaylistResultBean

/**
 * Created by summer on 18-1-12
 */
internal class NeteasePlaylistItemViewBinder : ItemViewBinder<PlaylistResultBean.PlaylistBean>() {
    override fun onBindViewHolder(holder: ViewHolder, item: PlaylistResultBean.PlaylistBean): Unit = with(holder.itemView) {
        textTitle.text = item.name
        textSubTitle.text = context.getString(R.string.netease_playlist_subtitle_template, item.trackCount)
        GlideApp.with(this)
                .load(item.coverImgUrl.toPictureUrl())
                .placeholder(R.drawable.common_image_placeholder_loading).into(imageCover)
        setOnClickListener {
            ARouter.getInstance().build(PlaylistDetail.ACTIVITY_PLAYLIST_DEAILT)
                    .withParcelable(PlaylistDetail.PARAM_PLAYLIST_PROVIDER, NeteasePlaylistDetailProvider(item))
                    .navigation()
        }
        imageAction.setOnClickListener {

        }
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.netease_item_playlist, parent, inflater)
    }

}