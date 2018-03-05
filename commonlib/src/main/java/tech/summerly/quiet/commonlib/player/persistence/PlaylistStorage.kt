package tech.summerly.quiet.commonlib.player.persistence

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.playlist.FmPlaylist
import tech.summerly.quiet.commonlib.player.playlist.NormalPlaylist
import tech.summerly.quiet.commonlib.player.playlist.Playlist
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.utils.edit
import tech.summerly.quiet.commonlib.utils.fromJson

/**
 * Created by summer on 18-3-4
 *
 * 用于保存当前播放信息到配置文件中，便于恢复。
 */
internal object PlaylistStorage {

    private const val KEY_PLAY_LIST = "music_list"
    private const val KEY_CURRENT_MUSIC = "music_current"
    private const val KEY_PLAY_MODE = "play_mode"
    private const val KEY_TYPE = "type"

    private val preference: SharedPreferences = LibModule.getSharedPreferences("common_music_player_info",
            Context.MODE_PRIVATE)

    private val gson = Gson()

    fun savePlaylist(musics: Collection<Music>) {
        preference.edit {
            val listJson = gson.toJson(musics)
            putString(KEY_PLAY_LIST, listJson)
        }
    }

    fun saveCurrent(playing: Music?) {
        preference.edit {
            putString(KEY_CURRENT_MUSIC, gson.toJson(playing))
        }
    }

    fun savePlayMode(playMode: PlayMode?) {
        playMode ?: return
        preference.edit {
            putString(KEY_PLAY_MODE, playMode.name)
        }
    }

    fun saveType(type: Int) {
        preference.edit {
            putInt(KEY_TYPE, type)
        }
    }

    /**
     * 从配置文件中读取上一次保存的[Playlist]信息
     */
    fun resotrePlaylist(): Playlist {
        val type = preference.getInt(KEY_TYPE, Playlist.TYPE_DEFAULT)
        return when (type) {
            Playlist.TYPE_FM -> FmPlaylist(getCurrentMusic(), getPlaylist())
            Playlist.TYPE_NORMAL -> NormalPlaylist(getCurrentMusic(), getPlayMode(), getPlaylist())
            else -> Playlist.newInstance()
        }
    }

    private fun getPlayMode() = PlayMode(preference.getString(KEY_PLAY_MODE, PlayMode.Sequence.name))

    private fun getCurrentMusic() = gson.fromJson<Music>(preference.getString(KEY_CURRENT_MUSIC, ""))

    private fun getPlaylist() = gson.fromJson<ArrayList<Music>>(
            preference.getString(KEY_PLAY_LIST,
                    "")) ?: ArrayList()
}
