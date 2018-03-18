package tech.summerly.quiet.commonlib.utils

import android.content.Context
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDelegate
import android.util.TypedValue
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

fun dimen(@DimenRes id: Int) = LibModule.resources.getDimension(id)

fun Context.getAttrColor(@AttrRes id: Int): Int {
    val value = TypedValue()
    if (theme.resolveAttribute(id, value, true)) {
        return value.data
    } else {
        error("can not found color for : $id")
    }
}

fun isNightMode(): Boolean {
    return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
}