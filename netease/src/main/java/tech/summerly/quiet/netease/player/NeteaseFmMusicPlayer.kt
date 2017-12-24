package tech.summerly.quiet.netease.player

import android.content.Context
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.BaseMusicPlayer
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi

/**
 * author : yangbin10
 * date   : 2017/12/22
 */
class NeteaseFmMusicPlayer(context: Context) : BaseMusicPlayer(context) {

    private val neteaseApi = NeteaseCloudMusicApi(context)

    override val musicList = ArrayList<Music>()


    override suspend fun getNextMusic(current: Music?): Music? {
        val nextIndex = musicList.indexOf(current) + 1
        return if (nextIndex >= musicList.size) {
            fetchMusicList()
            musicList[0]
        } else {
            musicList[nextIndex]
        }
    }

    private suspend fun fetchMusicList() {
        val musics = neteaseApi.getFmMusics()
        musicList.clear()
        musicList.addAll(musics)
    }

    //do not support back to previous music for FM player
    override suspend fun getPreviousMusic(current: Music?): Music? = null

}