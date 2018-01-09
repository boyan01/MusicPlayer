@file:Suppress("NOTHING_TO_INLINE")

package tech.summerly.quiet.commonlib.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import org.jetbrains.anko.alert
import org.jetbrains.anko.dip

/**
 * 创建一个有输入框的dialog
 *
 */
inline fun Context.inputDialog(
        title: String? = null,
        hint: String? = null,
        init: String? = null,
        yes: String = getString(android.R.string.yes),
        no: String = getString(android.R.string.no),
        noinline onYesClickListener: ((DialogInterface, TextInputLayout) -> Unit)? = null)
        : AlertDialog {
    val editText = TextInputEditText(this)
    val editLayout = TextInputLayout(this).apply {
        addView(editText)
    }
    editText.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            editLayout.isErrorEnabled = false
        }
    })
    editLayout.setPadding(dip(16), 0, dip(16), 0)
    editLayout.hint = hint
    editText.setText(init)
    val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setView(editLayout)
            .setPositiveButton(yes, null)
            .setNegativeButton(no, null)
            .create()
    dialog.setOnShowListener {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            onYesClickListener?.invoke(dialog, editLayout)
        }
    }
    return dialog
}


/**
 * create an alert dialog , then waiting for the result.
 *
 * @return true: if ok button is clicked
 *         false: if cancel button is clicked
 */
suspend fun Context.alert(
        message: String,
        title: String? = null,
        negative: String = getString(android.R.string.no),
        positive: String = getString(android.R.string.yes)
): Boolean = suspendCancellableCoroutine { continuation ->
    val dialog = alert(message, title) {
        negativeButton(negative) {
            continuation.resume(false)
        }
        positiveButton(positive) {
            continuation.resume(true)
        }
        onCancelled {
            if (!continuation.isCompleted) {
                continuation.resume(false)
            }
        }
    }.build()
    continuation.invokeOnCompletion {
        if (continuation.isCancelled) {
            dialog.dismiss()
        }
    }
    dialog.show()
}