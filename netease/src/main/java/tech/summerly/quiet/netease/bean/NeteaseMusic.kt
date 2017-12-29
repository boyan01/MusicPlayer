package tech.summerly.quiet.netease.bean

import tech.summerly.quiet.commonlib.bean.*

/**
 * author : yangbin10
 * date   : 2017/12/29
 */
class NeteaseMusic(id: Long, title: String, artist: List<Artist>, album: Album, picUri: String?, type: MusicType, mvId: Long, duration: Long, playUri: MutableList<MusicUri>)
    : Music(id, title, artist, album, picUri, type, mvId, duration, playUri) {

    override suspend fun getPlayableUrl(): String {
        return super.getPlayableUrl()
    }
}