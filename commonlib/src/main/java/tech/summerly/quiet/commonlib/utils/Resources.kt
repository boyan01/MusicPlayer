package tech.summerly.quiet.commonlib.utils

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import tech.summerly.quiet.commonlib.LibModule

/**
 * 对获取 Android Resource 的一层简单的封装，方便调用
 */

fun string(@StringRes stringId: Int) = LibModule.getString(stringId)!!

fun string(@StringRes stringId: Int, vararg formatArgs: Any) = string(stringId).format(*formatArgs)

fun color(@ColorRes colorId: Int) = ContextCompat.getColor(LibModule, colorId)

fun drawable(@DrawableRes id: Int, @ColorInt tint: Int = 0) = LibModule.getDrawable(id)!!.also {
    if (tint != 0) {
        it.setTint(tint)
    }
}