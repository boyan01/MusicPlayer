package tech.summerly.quiet.netease.utils

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicUri
import tech.summerly.quiet.commonlib.player.MusicUrlGetter
import tech.summerly.quiet.commonlib.utils.retryNull
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi


/**
 * to fetch the url for netease music
 */
internal object NeteaseMusicUrlGetter : MusicUrlGetter {

    override suspend fun getPlayableUrl(music: Music): String? = with(music) {
        val uris = playUri
                .filter {
                    it.isValid()
                }
                .sortedByDescending { it.bitrate }
        if (uris.isNotEmpty()) {
            return uris[0].uri
        }
        val datum = retryNull {
            NeteaseCloudMusicApi().getMusicUrl(id)
        } ?: return null
        val url = datum.url ?: return null
        playUri.clear()
        playUri.add(MusicUri(datum.bitrate, url,
                System.currentTimeMillis() + 12000, datum.md5))
        return url
    }
}