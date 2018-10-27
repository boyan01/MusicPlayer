package tech.soit.quiet.utils.component.support

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import tech.soit.quiet.AppContext

/**
 * @see Context.getString
 */
fun string(@StringRes stringId: Int) = AppContext.getString(stringId)!!

/**
 * @see Context.getString
 */
fun string(@StringRes stringId: Int, vararg formatArgs: Any) = string(stringId).format(*formatArgs)

/**
 * @see Context.getColor
 */
fun color(@ColorRes colorId: Int) = ContextCompat.getColor(AppContext, colorId)


/**
 * @see Context.getDrawable
 */
fun drawable(@DrawableRes id: Int, @ColorInt tint: Int = 0) = AppContext.getDrawable(id)!!.also {
    if (tint != 0) {
        it.setTint(tint)
    }
}

/**
 * @see android.content.res.Resources.getDimension
 */
fun dimen(@DimenRes id: Int) = AppContext.resources.getDimension(id)

/**
 * get theme data by Attr id
 */
fun Context.attrValue(@AttrRes id: Int): Int {
    val value = TypedValue()
    if (theme.resolveAttribute(id, value, true)) {
        return value.data
    } else {
        error("can not attribute for : $id")
    }
}


fun View.attrValue(@AttrRes id: Int): Int {
    return context.attrValue(id)
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()