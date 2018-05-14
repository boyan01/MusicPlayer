package tech.summerly.quiet.commonlib.player.playlist

import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import org.jetbrains.anko.toast
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi
import java.io.IOException

/**
 * Created by summer on 18-3-4
 */
internal class FmPlaylist(musicList: ArrayList<Music>
) : Playlist("netease_fm", musicList) {

    constructor() : this(ArrayList())

    override val type: PlayerType = PlayerType.FM

    override suspend fun getNextMusic(music: Music?): Music? = suspendCancellableCoroutine {
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
        try {
            val musics = neteaseApi.getFmMusics()
            musicList.clear()
            musicList.addAll(musics)
        } catch (e: IOException) {
            log { e.printStackTrace();"获取下一首歌曲失败" }
            asyncUI { LibModule.toast("io exception : ${e.message}") }
            throw CancellationException()
        }
        onPlaylistChanged()
    }


    override suspend fun getPreviousMusic(music: Music?): Music? = null


    override fun insertToNext(music: IMusic) {}

    private val neteaseApi = NeteaseCloudMusicApi()

}