package tech.soit.quiet.utils.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.module.AppGlideModule
import jp.wasabeef.glide.transformations.BitmapTransformation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import tech.soit.quiet.R
import tech.soit.quiet.utils.component.support.color
import java.security.MessageDigest


@GlideModule(glideName = "ImageLoader")
class MyAppGlideModule : AppGlideModule()


/**
 * generate palette for bitmap
 */
fun Bitmap.generatePalette() = GlobalScope.async {
    Palette.from(this@generatePalette).generate()
}

fun Palette.getMuteSwatch(night: Boolean = false): Palette.Swatch {
    return if (night) {
        darkMutedSwatch ?: Palette.Swatch(color(R.color.color_primary_dark), 100)
    } else {
        mutedSwatch ?: Palette.Swatch(color(R.color.color_primary), 100)
    }
}


class ColorMaskTransformation(
        @ColorInt private val color: Int
) : BitmapTransformation() {

    companion object {
        private const val ID = "ColorMaskTransformation"
    }

    override fun hashCode(): Int {
        return ID.hashCode() + color * 10
    }

    override fun equals(other: Any?): Boolean {
        return other is ColorMaskTransformation && other.color == this.color
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + color).toByteArray())
    }

    override fun transform(context: Context, pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        Canvas(toTransform).drawColor(color, PorterDuff.Mode.SRC_OVER)
        return toTransform
    }

}