package tech.summerly.quiet.commonlib.player

import tech.summerly.quiet.commonlib.player.playlist.Playlist
import tech.summerly.quiet.commonlib.utils.persistence.PropertiesDatabase

/**
 * Created by summer on 18-3-4
 *
 * 用于保存当前播放信息到配置文件中，便于恢复。
 */
internal object PlayerPersistenceHelper {

    private const val KEY_PLAY_LIST = "music_list"
    private const val KEY_PLAY_MODE = "play_mode"


    private val dao = PropertiesDatabase.INSTANCE.objectsDao()


    fun savePlaylist(playlist: Playlist?) {
        dao[KEY_PLAY_LIST] = playlist
    }

    /**
     * 从配置文件中读取上一次保存的[Playlist]信息
     */
    fun restorePlaylist(): Playlist? {
        return dao[KEY_PLAY_LIST] as Playlist?
    }

    fun restorePlayMode(): PlayMode {
        val name = dao[KEY_PLAY_MODE] as String?
        name ?: return PlayMode.Sequence
        return PlayMode.valueOf(name)
    }

    fun savePlayMode(mode: PlayMode) {
        dao[KEY_PLAY_MODE] = mode.name
    }

}
