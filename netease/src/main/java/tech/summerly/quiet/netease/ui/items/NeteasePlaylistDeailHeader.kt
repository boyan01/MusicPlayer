package tech.summerly.quiet.netease.ui.items

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.request.target.ImageViewTarget
import kotlinx.android.synthetic.main.netease_header_playlist_detail.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.api.result.PlaylistDetailResultBean

/**
 * author: summerly
 * email: yangbinyhbn@gmail.com
 */

class NeteasePlaylistDetailHeaderViewBinder(
        private val onBackgroundColor: (color: Int) -> Unit
) : ItemViewBinder<PlaylistDetailResultBean.Playlist>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.netease_header_playlist_detail, parent, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: PlaylistDetailResultBean.Playlist): Unit = with(holder.itemView) {
        GlideApp.with(this).asBitmap().load(item.coverImgUrl).into(object : ImageViewTarget<Bitmap>(image) {
            override fun setResource(resource: Bitmap?) {
                view.setImageBitmap(resource)
                resource ?: return
                launch(UI) {
                    val background = resource.generatePalette().await()
                            .getDarkMutedColor(color(R.color.netease_color_primary))
                    setBackgroundColor(background)
                    onBackgroundColor(background)
                }
            }
        })
        title.text = item.name
        if (item.subscribed) {
            buttonCollection.setImageResource(R.drawable.netease_ic_folder_special_black_24dp)
        } else {
            buttonCollection.setImageResource(R.drawable.netease_ic_folder_open_black_24dp)
        }
        buttonComment.setOnClickListener {
            log { "comment : ${item.name}" }
        }
    }

}