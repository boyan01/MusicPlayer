package tech.summerly.quiet.netease.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import tech.summerly.quiet.netease.NeteaseModule

fun getRealScreenSize(context: Context = NeteaseModule): Point {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val point = Point()
    display.getRealSize(point)
    return point
}

fun getAppUsableScreenSize(context: Context = NeteaseModule): Point {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    return Point().also {
        display.getSize(it)
    }
}

fun getNavigationBarHeight(context: Context = NeteaseModule): Int {
    val usable = getAppUsableScreenSize(context)
    val real = getRealScreenSize(context)

    //navigation bar on the right
    if (usable.x < real.x) {
        return usable.x - real.x
    }
    if (usable.y < real.y) {
        return real.y - usable.y
    }
    return 0
}