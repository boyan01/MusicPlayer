package tech.summerly.quiet.netease.player

import android.content.Context
import kotlinx.coroutines.experimental.launch
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.BaseMusicPlayer
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * author : yangbin10
 * date   : 2017/12/22
 */
class NeteaseFmMusicPlayer(context: Context) : BaseMusicPlayer(context) {

    private val neteaseApi = NeteaseCloudMusicApi(context)

    override val musicList = ArrayList<Music>()


    override suspend fun getNextMusic(current: Music?): Music? = suspendCoroutine {
        val nextIndex = musicList.indexOf(current) + 1
        if (nextIndex >= musicList.size) {
            launch {
                fetchMusicList()
                it.resume(musicList[0])
            }
        } else {
            it.resume(musicList[nextIndex])
        }
    }

    private suspend fun fetchMusicList() {
        val musics = neteaseApi.getFmMusics()
        log { "fmMusics : ${musics.joinToString { it.toShortString() }}" }
        val keeper = PlayerStateKeeper(baseContext)
        keeper.savePlaylist(musics)
        musicList.clear()
        musicList.addAll(musics)
    }

    //do not support back to previous music for FM player
    override suspend fun getPreviousMusic(current: Music?): Music? = null

}