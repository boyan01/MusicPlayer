package tech.summerlly.quiet.playlistdetail.items

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.request.target.ImageViewTarget
import kotlinx.android.synthetic.main.pd_header_playlist_netease.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import tech.summerlly.quiet.playlistdetail.R
import tech.summerly.quiet.commonlib.model.PlaylistProvider
import tech.summerly.quiet.commonlib.utils.*


internal class NeteaseHeaderViewBinder(
        private val onHeaderColorAvailable: (color: Int) -> Unit
) : ItemViewBinder<PlaylistProvider.Description>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.pd_header_playlist_netease, parent, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: PlaylistProvider.Description) = with(holder.itemView) {
        GlideApp.with(this).load(item.coverImgUrl.toPictureUrl())
                .into(object : ImageViewTarget<Drawable>(image) {
                    override fun setResource(resource: Drawable?) {
                        view.setImageDrawable(resource)
                        resource ?: return
                        launch(UI) {
                            if (resource is BitmapDrawable) {
                                val palette = resource.bitmap.generatePalette().await()
                                val background = palette.getDarkMutedColor(color(R.color.common_color_primary_dark))
                                setBackgroundColor(background)
                                onHeaderColorAvailable(background)
                            }
                        }
                    }
                })
        title.text = item.name
        if (item.subscribed) {
            buttonCollection.setImageResource(R.drawable.pd_ic_folder_special_black_24dp)
        } else {
            buttonCollection.setImageResource(R.drawable.pd_ic_folder_open_black_24dp)
        }
        buttonComment.setOnClickListener {
            log { "comment : ${item.name}" }
        }
    }

}