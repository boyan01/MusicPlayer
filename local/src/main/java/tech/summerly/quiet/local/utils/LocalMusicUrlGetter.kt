package tech.summerly.quiet.local.utils

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicUrlGetter
import java.io.File
import java.net.URI

/**
 * author : yangbin10
 * date   : 2018/1/18
 */
object LocalMusicUrlGetter : MusicUrlGetter {

    suspend override fun getPlayableUrl(music: Music): String? = with(music) {
        if (playUri.isEmpty()) {
            error("no playable url!")
        }
        val uri = playUri[0].uri
        if (uri.startsWith("file:", true)) {
            val file = File(URI(uri))
            if (file.exists()) {
                return file.path
            } else {
                error("no playable url!")
            }
        }
        error("no playable url!")
    }

}