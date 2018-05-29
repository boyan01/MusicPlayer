package tech.summerly.quiet.netease

import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import org.jetbrains.anko.toast
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.player.playlist.Playlist
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi
import java.io.IOException

class NeteaseFmPlaylist : Playlist(
        TOKEN, ArrayList()
) {

    companion object {

        const val TOKEN = "netease_fm"

    }

    override val type: PlayerType = PlayerType.FM

    private val neteaseApi get() = NeteaseCloudMusicApi()

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
            asyncUI { NeteaseModule.toast("io exception : ${e.message}") }
            throw CancellationException()
        }
        onPlaylistChanged()
    }

    override suspend fun getPreviousMusic(music: Music?): Music? = null

    override fun insertToNext(music: IMusic) {
        //do nothing
    }
}