package tech.summerly.quiet.local.utils

import android.content.Context
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.utils.alert
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.R
import tech.summerly.quiet.service.local.LocalMusicApi


fun Context.showMusicDeleteDialog(music: Music) {
    launch(UI + CoroutineExceptionHandler(handler = { _, throwable ->
        toast(getString(R.string.local_toast_delete_failed_template, throwable.message))
        throwable.printStackTrace()
    })) {
        val ok = alert(message = getString(R.string.local_message_delete_template, music.title),
                positive = getString(R.string.local_action_delete))
        if (ok) {
            LocalMusicApi
                    .getLocalMusicApi(LocalModule)
                    .deleteMusic(music, true)
            toast(getString(R.string.local_toast_delete_succeed_template, music.title))
        }
    }
}