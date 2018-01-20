package tech.summerly.quiet.local.utils

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicUrlGetter
import java.io.File
import java.net.URI

/**
 * author : yangbin10
 * date   : 2018/1/18
 */
internal object LocalMusicUrlGetter : MusicUrlGetter {

    override suspend fun getPlayableUrl(music: Music): String? = with(music) {
        if (playUri.isEmpty()) {
            return@with null
        }
        val uri = playUri[0].uri
        if (uri.startsWith("file:", true)) {
            val file = File(URI(uri))
            return if (file.exists()) {
                file.path
            } else {
                null
            }
        }
        return@with null
    }

}