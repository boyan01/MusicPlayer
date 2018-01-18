package tech.summerly.quiet.commonlib.player

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType

/**
 * use to get the playable url for music bean
 * for each type of music , the method to get url is different
 */
interface MusicUrlGetter {
    suspend fun getPlayableUrl(music: Music): String?
}

object MusicUrlFetcher : MusicUrlGetter {

    private val map = HashMap<MusicType, MusicUrlGetter>()

    fun addMusicUrlGetter(type: MusicType, urlGetter: MusicUrlGetter) {
        map.put(type, urlGetter)
    }

    override suspend fun getPlayableUrl(music: Music): String? {
        val urlGetter = map[music.type] ?: error("must [addMusicUrlGetter] for type: ${music.type}")
        return urlGetter.getPlayableUrl(music)
    }

}


