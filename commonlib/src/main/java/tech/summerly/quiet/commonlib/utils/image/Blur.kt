package tech.summerly.quiet.commonlib.utils.image

import android.graphics.Bitmap
import android.support.annotation.IntRange
import tech.summerly.quiet.commonlib.utils.FastBlur

suspend fun Bitmap.blur(@IntRange(from = 0, to = 24) radius: Int = 24): Bitmap {
    return FastBlur.doBlur(this, radius, false)
}