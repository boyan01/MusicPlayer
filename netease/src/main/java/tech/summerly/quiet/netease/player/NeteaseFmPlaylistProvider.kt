package tech.summerly.quiet.netease.player

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicPlaylistProvider
import tech.summerly.quiet.commonlib.player.state.BasePlayerDataListener
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi

class NeteaseFmPlaylistProvider(
        current: Music?,
        musicList: ArrayList<Music>,
        playerStateListener: BasePlayerDataListener
) : MusicPlaylistProvider(current, PlayMode.Sequence, musicList, playerStateListener) {

    override fun insertToNext(music: Music) {
        //do nothing
    }

    private val neteaseApi = NeteaseCloudMusicApi()

    override fun setPlaylist(musics: List<Music>) {
        //do nothing , because do not need it
    }

    override fun getPlaylist(): List<Music> = musicList

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
        val musics = neteaseApi.getFmMusics()
        log { "fmMusics : ${musics.joinToString { it.toShortString() }}" }

        musicList.clear()
        musicList.addAll(musics)
        playerStateListener.onPlaylistUpdated(musics)
    }

    override suspend fun getPreviousMusic(music: Music?): Music? = null

    override fun clear() {

    }

    override fun isTypeAccept(type: MusicType): Boolean {
        return type == MusicType.NETEASE_FM
    }

}