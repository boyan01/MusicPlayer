@file:Suppress("FunctionName")

package tech.summerly.quiet.commonlib.utils

import android.content.Context
import android.support.annotation.MenuRes
import android.support.v7.widget.PopupMenu
import android.view.MenuItem
import android.view.View

fun PopupMenu(anchor: View,
              @MenuRes menuRes: Int,
              context: Context = anchor.context,
              itemClickListener: Context.(MenuItem) -> Boolean): PopupMenu {
    val popupMenu = PopupMenu(context, anchor)
    popupMenu.inflate(menuRes)
    popupMenu.setOnMenuItemClickListener {
        context.itemClickListener(it)
    }
    popupMenu.show()
    return popupMenu
}