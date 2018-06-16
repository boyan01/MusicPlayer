package tech.summerly.quiet.local.utils

import android.content.Context
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.utils.alert
import tech.summerly.quiet.local.R


fun Context.showMusicDeleteDialog(music: IMusic) {
    launch(UI + CoroutineExceptionHandler(handler = { _, throwable ->
        toast(getString(R.string.local_toast_delete_failed_template, throwable.message))
        throwable.printStackTrace()
    })) {
        val ok = alert(message = getString(R.string.local_message_delete_template, music.title),
                positive = getString(R.string.local_action_delete))
        if (ok) {
            music.delete()
            toast(getString(R.string.local_toast_delete_succeed_template, music.title))
        }
    }
}