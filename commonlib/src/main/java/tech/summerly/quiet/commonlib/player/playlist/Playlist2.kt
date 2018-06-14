package tech.summerly.quiet.commonlib.player.playlist

import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayerPersistenceHelper
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.utils.log
import java.io.Serializable

abstract class Playlist2<T>(
        val token: String,
        list: List<T>
) : Serializable {

    companion object {

        fun empty(): Playlist2<IMusic> = NormalPlaylist2("EMPTY", ArrayList())

        /**
         * @param token     to identify a playlist
         * @param musicList the music collection to play
         */
        fun normalPlaylist(musicList: List<IMusic>, token: String): Playlist2<IMusic> {
            val playlist = NormalPlaylist2(token, ArrayList(musicList))
            return playlist
        }
    }

    protected val mList = ArrayList(list)

    /**
     * playlist content
     */
    val list: List<T> get() = mList

    /**
     * the type of this playlist
     */
    abstract val type: PlayerType


    var current: T? = null
        set(value) {
            val old = field
            field = value
            onCurrentChanged(old, value)
        }


    @Transient
    private var isActive: Boolean = false

    //attention please do not throw exception
    abstract suspend fun getNext(anchor: T? = current): T?

    //attention please do not throw exception
    abstract suspend fun getPrevious(anchor: T? = current): T?


    protected fun onCurrentChanged(from: T?, to: T?) {
        log { "$token is active : $isActive ,change $from to $to" }
        if (!isActive) {
            return
        }
        if (to is Music? && from is Music?) {
            MusicPlayerManager.internalPlayingMusic.postValue(to)
            MusicPlayerManager.internalMusicChange.postValue(from to to)
        }
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
        launch {
            PlayerPersistenceHelper.savePlaylist(this@Playlist2)
        }
    }


    /* operation for playlist */

    /**
     * remove a music item from [mList]
     */
    open fun remove(music: T?): Boolean {
        return mList.remove(music)
    }

    internal fun reset(list: List<T>) {
        mList.clear()
        mList.addAll(list)
    }

    /**
     * 添加[music]到当前播放的下一首
     */
    abstract fun insertToNext(next: T)


    override fun toString(): String {
        return "token:$token, current :$current , list :$mList"
    }

    open fun active() {
        isActive = true
        this.current = this.current
    }

    open fun inActive() {
        isActive = false
        current = null
    }

}