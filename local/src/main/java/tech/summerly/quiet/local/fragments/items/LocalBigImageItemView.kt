package tech.summerly.quiet.local.fragments.items

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.local_item_big_image.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.bean.Album
import tech.summerly.quiet.commonlib.bean.Artist
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.commonlib.utils.support.TypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.constraints.PlaylistDetail
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.utils.AlbumDetailProvider
import tech.summerly.quiet.local.utils.ArtistDetailProvider


internal class LocalBigImageItem(
        val title: String,
        val imageUri: String?,
        val data: Any
)


internal class LocalBigImageItemViewBinder : TypedBinder<LocalBigImageItem>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder.from(R.layout.local_item_big_image, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: LocalBigImageItem) = with(holder.itemView) {
        GlideApp.with(this)
                .asBitmap()
                .load(item.imageUri?.toPictureUrl() ?: R.drawable.common_image_audience)
                .fitCenter()
                .override(getScreenWidth() / 3)
                .into(object : BitmapImageViewTarget(image) {
                    override fun setResource(resource: Bitmap?) {
                        super.setResource(resource)
                        resource?.let {
                            setPaletteColor(it)
                        }
                    }
                })
        title.text = item.title
        setOnClickListener {
            when (item.data) {
                is Artist -> {
                    ARouter.getInstance()
                            .build(PlaylistDetail.ACTIVITY_PLAYLIST_DETAIL)
                            .withParcelable(PlaylistDetail.PARAM_PLAYLIST_PROVIDER, ArtistDetailProvider(item.data))
                            .navigation()
                }
                is Album -> {
                    ARouter.getInstance()
                            .build(PlaylistDetail.ACTIVITY_PLAYLIST_DETAIL)
                            .withParcelable(PlaylistDetail.PARAM_PLAYLIST_PROVIDER, AlbumDetailProvider(item.data))
                            .navigation()
                }
            }
        }
    }

    private fun View.setPaletteColor(bitmap: Bitmap) = launch(UI) {
        val swatch = bitmap.generatePalette().await().getMuteSwatch()
        val background = swatch.rgb
        val foreground = swatch.titleTextColor
        setBackgroundColor(background)
        title.setTextColor(foreground)
        actionMore.drawable.setTint(foreground)
        (actionMore.background as RippleDrawable).setColor(ColorStateList.valueOf(foreground))
    }

}