package tech.summerly.quiet.local.fragments.items

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.local_item_artist.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.bean.Artist
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.local.R

/**
 * Created by summer on 17-12-24
 */
internal class LocalArtistItemViewBinder(
        private val onArtistClick: (Artist) -> Unit) : ItemViewBinder<Artist>() {

    override fun onBindViewHolder(holder: ViewHolder, item: Artist) = with(holder.itemView) {
        GlideApp.with(this)
                .asBitmap()
                .load(item.getPictureUrl())
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
        title.text = item.name
        setOnClickListener {
            onArtistClick(item)
        }
    }

    /**
     * set
     */
    private fun View.setPaletteColor(bitmap: Bitmap) = launch(UI) {
        val swatch = bitmap.generatePalette().await().getMuteSwatch()
        val background = swatch.rgb
        val foreground = swatch.titleTextColor
        setBackgroundColor(background)
        title.setTextColor(foreground)
        actionMore.drawable.setTint(foreground)
        (actionMore.background as RippleDrawable).setColor(ColorStateList.valueOf(foreground))
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.local_item_artist, parent, inflater)
    }

}