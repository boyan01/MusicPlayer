package tech.summerly.quiet.netease.bean

import tech.summerly.quiet.commonlib.bean.*
import tech.summerly.quiet.netease.NeteaseModule
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi

/**
 * author : yangbin10
 * date   : 2017/12/29
 */
class NeteaseMusic(id: Long, title: String, artist: List<Artist>, album: Album, picUri: String?, type: MusicType, mvId: Long, duration: Long, playUri: MutableList<MusicUri>)
    : Music(id, title, artist, album, picUri, type, mvId, duration, playUri) {

    override suspend fun getPlayableUrl(): String {
        val uris = playUri
                .filter {
                    it.isValid()
                }
                .sortedByDescending { it.bitrate }
        if (uris.isNotEmpty()) {
            return uris[0].uri
        }
        val datum = NeteaseCloudMusicApi(NeteaseModule).getMusicUrl(id) ?: error("can not get url")
        val url = datum.url ?: error("can not get url")
        playUri.clear()
        playUri.add(MusicUri(datum.bitrate, url,
                System.currentTimeMillis() + 12000, datum.md5))
        return url
    }
}