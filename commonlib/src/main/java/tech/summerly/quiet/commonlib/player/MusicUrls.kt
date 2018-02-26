@file:Suppress("REDUNDANT_ELSE_IN_WHEN")

package tech.summerly.quiet.commonlib.player

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.bean.MusicUri
import tech.summerly.quiet.commonlib.utils.retryNull
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URI

/**
 * @throws IOException
 */
internal suspend fun Music.getPlayableUri(): String? {
    return when (type) {
        MusicType.LOCAL -> getLocalMusicUri()
        MusicType.NETEASE, MusicType.NETEASE_FM -> getNeteaseMusicUri()
        else -> throw IllegalAccessError()
    }
}


private suspend fun Music.getNeteaseMusicUri(): String? {
    val uris = playUri.filter { it.isValid() }.sortedByDescending { it.bitrate }
    if (uris.isNotEmpty()) {
        return uris[0].uri
    }
    val cloudMusicApi = NeteaseCloudMusicApi()
    val datum = retryNull {
        cloudMusicApi.getMusicUrl(id)
    } ?: return null
    val url = datum.url ?: return null
    playUri.clear()
    playUri.add(MusicUri(datum.bitrate, url, System.currentTimeMillis() + 12_000, datum.md5))
    return url
}


private fun Music.getLocalMusicUri(): String? {
    if (playUri.isEmpty()) {
        return null
    }
    val uri = playUri[0].uri
    if (uri.startsWith("file", true)) {
        val file = File(URI(uri))
        if (!file.exists()) {
            throw FileNotFoundException("can not find file : ${file.path}")
        }
        return uri
    }
    throw IOException("error uri")
}


