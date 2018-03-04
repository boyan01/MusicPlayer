package tech.summerly.quiet.commonlib.player.playlist

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi

/**
 * Created by summer on 18-3-4
 */
internal class FmPlaylist(current: Music?,
                          musicList: ArrayList<Music>
) : Playlist(current, PlayMode.Sequence, musicList) {

    constructor() : this(null, ArrayList())

    override val type: Int = Playlist.TYPE_FM

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

        musicList.clear()
        musicList.addAll(musics)
        stateChangeListener.onMusicListChange(musicList)
    }


    override suspend fun getPreviousMusic(music: Music?): Music? = null

    override fun clear() {}

    override fun insertToNext(music: Music) {}

    private val neteaseApi = NeteaseCloudMusicApi()

}