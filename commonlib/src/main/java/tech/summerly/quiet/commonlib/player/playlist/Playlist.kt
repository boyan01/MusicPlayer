package tech.summerly.quiet.commonlib.player.playlist

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.persistence.PlaylistStorage
import tech.summerly.quiet.commonlib.player.state.PlayMode

/**
 * Created by summer on 18-3-4
 */
abstract class Playlist(
        current: Music?,
        playMode: PlayMode,
        musicList: MutableList<Music>
) {

    companion object {

        const val TYPE_NORMAL = 1

        const val TYPE_FM = 2

        const val TYPE_DEFAULT = TYPE_NORMAL

        fun newInstance(type: Int = TYPE_DEFAULT): Playlist {
            return when (type) {
                TYPE_NORMAL -> NormalPlaylist()
                TYPE_FM -> FmPlaylist()
                else -> throw IllegalAccessError("type error : $type")
            }
        }

        internal val stateChangeListener = object : StateChangeListener {
            override fun onMusicChange(old: Music?, new: Music?) {
                MusicPlayerManager.onMusicChange(old, new)
                PlaylistStorage.saveCurrent(new)
            }

            override fun onPlayModeChange(playMode: PlayMode) {
                MusicPlayerManager.onPlayModeChange(playMode)
                PlaylistStorage.savePlayMode(playMode)
            }

            override fun onMusicListChange(musicList: List<Music>) {
                MusicPlayerManager.onMusicListChange(musicList)
                PlaylistStorage.savePlaylist(musicList)
            }
        }
    }

    /**
     * the type of this playlist
     *
     * 取值有：[TYPE_FM] [TYPE_NORMAL]
     */
    abstract val type: Int

    var musicList: MutableList<Music> = musicList
        set(value) {
            field.clear()
            field.addAll(value)
            stateChangeListener.onMusicListChange(field)
        }

    var current: Music? = current
        set(value) {
            stateChangeListener.onMusicChange(field, value)
            field = value
        }

    var playMode: PlayMode = playMode
        set(value) {
            field = value
            stateChangeListener.onPlayModeChange(value)
        }

    abstract suspend fun getNextMusic(music: Music? = current): Music?

    abstract suspend fun getPreviousMusic(music: Music? = current): Music?

    /**
     * 清空当前音乐列表
     */
    abstract fun clear()

    /**
     * 添加[music]到当前播放的下一首
     */
    abstract fun insertToNext(music: Music)

    /**
     * 从当前音乐列表中移除[music]
     */
    fun remove(music: Music?) {
        musicList.remove(music)
        stateChangeListener.onMusicListChange(musicList)
    }

    fun setMusicLists(musicList: List<Music>) {
        this.musicList.clear()
        this.musicList.addAll(musicList)
        stateChangeListener.onMusicListChange(musicList)
    }

    init {
        this.current = current
        this.playMode = playMode
    }

    internal interface StateChangeListener {

        fun onMusicChange(old: Music?, new: Music?)

        fun onPlayModeChange(playMode: PlayMode)

        fun onMusicListChange(musicList: List<Music>)

    }
}