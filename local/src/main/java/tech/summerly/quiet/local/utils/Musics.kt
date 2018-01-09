package tech.summerly.quiet.local.utils

import kotlinx.coroutines.experimental.async
import tech.summerly.quiet.commonlib.AppContext
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.local.LocalMusicApi

/**
 * delete local music
 */
internal fun Music.delete() = async {
    if (type == MusicType.LOCAL) {
        LocalMusicApi
                .getLocalMusicApi(AppContext.instance)
                .deleteMusic(this@delete, true)
    }
}