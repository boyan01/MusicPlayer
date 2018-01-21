package tech.summerly.quiet.commonlib.utils

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import kotlinx.coroutines.experimental.async
import tech.summerly.quiet.commonlib.R

/**
 * generate palette for bitmap
 */
fun Bitmap.generatePalette() = async {
    Palette.from(this@generatePalette).generate()
}

fun Palette.getMuteSwatch(night: Boolean = isNightMode()): Palette.Swatch {
    return if (night) {
        darkMutedSwatch ?: Palette.Swatch(color(R.color.common_background_dark), 100)
    } else {
        mutedSwatch ?: Palette.Swatch(color(R.color.common_background_dark), 100)
    }
}