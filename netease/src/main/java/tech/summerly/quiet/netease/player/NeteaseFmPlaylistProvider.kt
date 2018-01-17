package tech.summerly.quiet.netease.player

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicPlaylistProvider
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.netease.NeteaseModule
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi

class NeteaseFmPlaylistProvider : MusicPlaylistProvider {
    override var playMode: PlayMode = PlayMode.Sequence
        set(value) {
            field = PlayMode.Sequence
        }

    override fun insertToNext(music: Music) {
        //do nothing
    }

    private val neteaseApi = NeteaseCloudMusicApi(NeteaseModule.instance)

    override var current: Music? = null

    private val musicList = ArrayList<Music>()

    override fun setPlaylist(musics: List<Music>) {

    }

    override fun getPlaylist(): List<Music> = musicList

    suspend override fun getNextMusic(music: Music?): Music? = suspendCancellableCoroutine {
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

        musicList.clear()
        musicList.addAll(musics)
    }

    suspend override fun getPreviousMusic(music: Music?): Music? = null

    override fun clear() {

    }

    override fun isTypeAccept(type: MusicType): Boolean {
        return type == MusicType.NETEASE_FM
    }

}