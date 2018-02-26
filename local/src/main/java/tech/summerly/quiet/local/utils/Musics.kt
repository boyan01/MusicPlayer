package tech.summerly.quiet.local.utils

import kotlinx.coroutines.experimental.async
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.service.local.LocalMusicApi

/**
 * delete local music
 */
internal fun Music.delete() = async {
    if (type == MusicType.LOCAL) {
        LocalMusicApi
                .getLocalMusicApi(LocalModule)
                .deleteMusic(this@delete, true)
    }
}