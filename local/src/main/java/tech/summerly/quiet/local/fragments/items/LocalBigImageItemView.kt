package tech.summerly.quiet.local.fragments.items

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.local_item_big_image.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.local.R


internal class LocalBigImageItem(
        val title: String,
        val imageUri: String?,
        val data: Any
)


internal class LocalBigImageItemViewBinder(
        private val onItemClick: (LocalBigImageItem) -> Unit) : ItemViewBinder<LocalBigImageItem>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.local_item_big_image, parent, inflater)
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
            onItemClick(item)
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