package tech.summerly.quiet.commonlib.utils

import android.content.res.Resources
import android.view.View
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by summer on 17-12-17
 */

val View.isVisible: Boolean
    get() = visibility == View.VISIBLE

val View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE

val View.isGone: Boolean
    get() = visibility == View.GONE

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}


/**
 * 为 View 设置一个安全的点击事件,防止重复点击
 */
fun View.setOnClickListenerSafely(listener: suspend ((View) -> Unit)) {
    setOnClickListener {
        launch(UI) {
            isClickable = false
            listener(it)
            isClickable = true
        }
    }
}

