package tech.summerly.quiet.commonlib.player.playlist

import kotlinx.coroutines.experimental.async
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayerPersistenceHelper
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.utils.log
import java.io.Serializable

/**
 * Created by summer on 18-3-4
 */
abstract class Playlist(
        val token: String,
        protected val musicList: MutableList<Music>
) : Serializable {


    companion object {

        private val EMPTY = normalPlaylist(emptyList(), "EMPTY")

        fun empty(): Playlist = EMPTY

        /**
         * @param token     to identify a playlist
         * @param musicList the music collection to play
         */
        fun normalPlaylist(musicList: List<IMusic>, token: String): Playlist {
            val playlist = NormalPlaylist(token, ArrayList(musicList) as ArrayList<Music>)
            return playlist
        }
    }

    val musics get() = musicList.toList()

    /**
     * the type of this playlist
     */
    abstract val type: PlayerType


    var current: Music? = null
        set(value) {
            val old = field
            field = value
            onPlayingMusicChanged(old, value)
        }

    @Transient
    private var isActive: Boolean = false

    //attention please do not throw exception
    abstract suspend fun getNextMusic(music: Music? = current): Music?

    //attention please do not throw exception
    abstract suspend fun getPreviousMusic(music: Music? = current): Music?


    /* call back */
    protected fun onPlayingMusicChanged(from: IMusic?, to: IMusic?) {
        log { "$token is active : $isActive ,change $from to $to" }
        if (!isActive) {
            return
        }
        to as Music?
        from as Music?
        MusicPlayerManager.internalPlayingMusic.postValue(to)
        MusicPlayerManager.internalMusicChange.postValue(from to to)
        onPlaylistChanged()
    }


    /**
     * call back of playlist data changed
     * we need to refresh persistence data
     */
    protected fun onPlaylistChanged() {
        if (!isActive) {
            return
        }
        async {
            PlayerPersistenceHelper.savePlaylist(this@Playlist)
        }
    }

    /* operation for playlist */

    /**
     * remove a music item from [musicList]
     */
    open fun remove(music: IMusic?): Boolean {
        music as Music?
        return musicList.remove(music)
    }

    internal fun resetMusicList(musicList: List<IMusic>) {
        this.musicList.clear()
        this.musicList.addAll(musicList as List<Music>)
    }

    /**
     * 添加[music]到当前播放的下一首
     */
    abstract fun insertToNext(music: IMusic)


    override fun toString(): String {
        return "token:$token, current :${current?.title} , list :${musicList}"
    }

    open fun active() {
        isActive = true
        this.current = this.current
    }

    open fun inActive() {
        isActive = false
        current = null
        musicList.clear()
    }

}