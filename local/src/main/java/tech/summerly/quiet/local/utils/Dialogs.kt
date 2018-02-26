package tech.summerly.quiet.local.utils

import android.content.Context
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.utils.alert
import tech.summerly.quiet.commonlib.utils.inputDialog
import tech.summerly.quiet.local.R
import tech.summerly.quiet.service.local.LocalMusicApi


fun Context.showPlaylistCreatorDialog() {
    inputDialog(
            title = getString(R.string.local_title_playlist_creator)
    ) { dialogInterface, textInputLayout ->
        val text = textInputLayout.editText?.text.toString().trim()
        if (text.isEmpty()) {
            textInputLayout.error = getString(R.string.local_error_text_empty)
            return@inputDialog
        }
        launch(UI) {
            val result = LocalMusicApi.getLocalMusicApi(this@showPlaylistCreatorDialog).createPlaylist(text).await()
            when (result) {
                -1 -> textInputLayout.error = getString(R.string.local_message_create_playlist_failed)
                -2 -> textInputLayout.error = getString(R.string.local_error_repeated)
                0 -> dialogInterface.dismiss()
            }
        }
    }.show()
}

fun Context.showMusicDeleteDialog(music: Music) {
    launch(UI + CoroutineExceptionHandler({ _, throwable ->
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