@file:Suppress("NOTHING_TO_INLINE")

package tech.summerly.quiet.commonlib.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout

/**
 * Created by summer on 17-12-17
 */
/**
 * 创建一个有输入框的dialog
 *
 * TODO: 改进输入框的margin和添加一个预设值防止需要用户重复输入
 */
inline fun Context.inputDialog(
        title: String,
        hint: String,
        yes: String,
        no: String,
        noinline onYesClickListener: (DialogInterface, TextInputLayout) -> Unit)
        : AlertDialog {
    val edit = TextInputLayout(this).apply {
        addView(TextInputEditText(context))
    }
    edit.hint = hint
    val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setView(edit)
            .setPositiveButton(yes, null)
            .setNegativeButton(no, null)
            .create()
    dialog.setOnShowListener {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            onYesClickListener(dialog, edit)
        }
    }
    return dialog
}