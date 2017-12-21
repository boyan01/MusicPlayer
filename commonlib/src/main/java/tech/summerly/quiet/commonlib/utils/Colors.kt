package tech.summerly.quiet.commonlib.utils

import android.support.annotation.ColorInt

/**
 * 获取颜色的 alpha 值, 值域为:[0,1]
 */
val Int.alpha: Float
    get() = (this ushr 24) / 0xff.toFloat()


/**
 * 计算以 [background] 为底色的叠加颜色
 */
fun Int.withColor(@ColorInt background: Int): Int {
    val red = (red * (1 - alpha) + background.red * alpha).toInt()
    val blue = (blue * (1 - alpha) + background.blue * alpha).toInt()
    val green = (green * (1 - alpha) + background.green * alpha).toInt()
    return (0xff shl 24) or (red shl 16) or (green shl 8) or blue
}

private val Int.blue
    get() = this and 0xff

private val Int.red
    get() = this ushr 16 and 0xff

private val Int.green
    get() = this ushr 8 and 0xff