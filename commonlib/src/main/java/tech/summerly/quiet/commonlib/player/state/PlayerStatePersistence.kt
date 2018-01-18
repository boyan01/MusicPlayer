package tech.summerly.quiet.commonlib.player.state

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.utils.fromJson

internal class PlayerStateKeeper {

    companion object {
        private val KEY_PLAY_LIST = "music_list"
        private val KEY_CURRENT_MUSIC = "music_current"
        private val KEY_PLAY_MODE = "play_mode"
    }

    private val preference: SharedPreferences =
            LibModule.instance.getSharedPreferences("common_music_player_info",
                    Context.MODE_PRIVATE)

    private val gson = Gson()

    fun savePlaylist(
            musics: Collection<Music>,
            playing: Music? = null,
            editor: SharedPreferences.Editor = preference.edit()) {
        if (musics.isEmpty()) {
            return
        }
        saveCurrent(playing ?: musics.elementAtOrNull(0), editor)
        val listJson = gson.toJson(musics)
        editor.putString(KEY_PLAY_LIST, listJson)
        editor.apply()
    }

    fun saveCurrent(playing: Music?,
                    editor: SharedPreferences.Editor = preference.edit()) {
        playing ?: return
        editor.putString(KEY_CURRENT_MUSIC, gson.toJson(playing))
        editor.apply()
    }

    fun savePlayMode(playMode: PlayMode?,
                     editor: SharedPreferences.Editor = preference.edit()) {
        playMode ?: return
        editor.putString(KEY_PLAY_MODE, playMode.name)
        editor.apply()
    }

    fun getPlayMode() = PlayMode(preference.getString(KEY_PLAY_MODE, PlayMode.Sequence.name))

    fun getCurrentMusic() = gson.fromJson<Music>(preference.getString(KEY_CURRENT_MUSIC, ""))

    fun getPlaylist() = gson.fromJson<ArrayList<Music>>(
            preference.getString(KEY_PLAY_LIST,
                    "")) ?: ArrayList()
}


interface BasePlayerDataListener {

    /**
     * call when player's playlist has been changed.
     */
    fun onPlaylistUpdated(playlist: List<Music>)

    /**
     * call when player's current playing music has been changed
     */
    fun onCurrentMusicUpdated(old: Music?, new: Music?)

    /**
     * call when player's play mode has been changed
     */
    fun onPlayModeUpdated(playMode: PlayMode)
}


object BasePlayerDataSaver : BasePlayerDataListener {
    private val sateKeeper
        get() = PlayerStateKeeper()

    override fun onPlaylistUpdated(playlist: List<Music>) {
        sateKeeper.savePlaylist(playlist)
    }

    override fun onCurrentMusicUpdated(old: Music?, new: Music?) {
        sateKeeper.saveCurrent(new)
    }

    override fun onPlayModeUpdated(playMode: PlayMode) {
        sateKeeper.savePlayMode(playMode)
    }
}

internal operator fun BasePlayerDataListener.plus(listener: BasePlayerDataListener): BasePlayerDataListenerHolder {
    return BasePlayerDataListenerHolder(this, listener)
}

internal class BasePlayerDataListenerHolder constructor(
        val list: MutableList<BasePlayerDataListener>
) : MutableList<BasePlayerDataListener> by list, BasePlayerDataListener {

    constructor(vararg listeners: BasePlayerDataListener) : this(listeners.toMutableList())

    override fun onPlaylistUpdated(playlist: List<Music>) {
        list.forEach { it.onPlaylistUpdated(playlist) }
    }

    override fun onCurrentMusicUpdated(old: Music?, new: Music?) {
        list.forEach { it.onCurrentMusicUpdated(old, new) }
    }

    override fun onPlayModeUpdated(playMode: PlayMode) {
        list.forEach { it.onPlayModeUpdated(playMode) }
    }
}