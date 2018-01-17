package tech.summerly.quiet.commonlib.player

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.core.CoreMediaPlayer
import tech.summerly.quiet.commonlib.player.core.OnPlayerStateChangeListener
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.utils.edit
import tech.summerly.quiet.commonlib.utils.fromJson
import tech.summerly.quiet.commonlib.utils.log

typealias OnPlayingMusicChange = (old: Music?, new: Music?) -> Unit
typealias OnPlayerStateChange = OnPlayerStateChangeListener
typealias OnPositionChange = (position: Long) -> Unit
typealias OnError = (Throwable) -> Unit

class BaseMusicPlayer(
        private val onPlayingMusicChange: OnPlayingMusicChange,
        private val onPlayerStateChange: OnPlayerStateChange,
        private val onPositionChange: OnPositionChange,
        private val onError: OnError
) {

    companion object {
        private val KEY_PLAY_LIST = "music_list"
        private val KEY_CURRENT_MUSIC = "music_current"
        private val KEY_PLAY_MODE = "play_mode"
    }

    private val baseContext get() = LibModule.instance

    private var internalPlaylistProvider: MusicPlaylistProvider = MusicPlaylistProviderFactory[MusicType.LOCAL]


    /**
     *
     */
    var current: Music? = null

    val corePlayer: CoreMediaPlayer = CoreMediaPlayer()
            .also { it.addOnMediaPlayerStateChangeListener(onPlayerStateChange) }

    val playlistProvider get() = internalPlaylistProvider

    /**
     * everything get ready , just to start play music
     */
    private fun performPlay(music: Music) {
        //check type
        if (!playlistProvider.isTypeAccept(music.type)) {
            internalPlaylistProvider = MusicPlaylistProviderFactory[music.type]
        }
        if (corePlayer.playing != music) {
            onPlayingMusicChange(corePlayer.playing, music)
            playlistProvider.current = music
        }
        corePlayer.play(music)
    }

    fun playNext() = launch {
        val next = playlistProvider.getNextMusic(current) ?: return@launch
        performPlay(next)
    }

    fun playPrevious() = launch {
        val previous = playlistProvider.getPreviousMusic(current) ?: return@launch
        performPlay(previous)
    }

    fun playPause() = launch {
        when (corePlayer.getState()) {
            PlayerState.Pausing -> corePlayer.start()
            PlayerState.Playing -> corePlayer.pause()
            PlayerState.Loading -> Unit
            else -> {
                val shouldBePlay = corePlayer.playing ?: playlistProvider.getNextMusic(current)
                shouldBePlay?.let(this@BaseMusicPlayer::performPlay)
                Unit
            }
        }
    }

    /**
     * try to play [music]
     * if this music is playing , do nothing
     * else will force to play this music from start , even it is pausing
     */
    fun play(music: Music) {
        log { music.toShortString() }
        performPlay(music)
    }

    internal fun destroy() {
        val keeper = PlayerStateKeeper(baseContext)
        keeper.preference.edit {
            //            keeper.savePlayMode(playMode.value, editor = this)
            keeper.savePlaylist(internalPlaylistProvider.getPlaylist(), playing = corePlayer.playing, editor = this)
        }
    }

    fun insertToNext(music: Music) {
        TODO()
    }

    private inner class PlayerStateKeeper(context: Context) {


        val preference: SharedPreferences = context.getSharedPreferences("common_music_player_info", Context.MODE_PRIVATE)

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

        fun getPlaylist() = gson.fromJson<List<Music>>(preference.getString(KEY_PLAY_LIST, "")) ?: emptyList()
    }

    fun setType(type: MusicType) {
        if (!internalPlaylistProvider.isTypeAccept(type)) {
            internalPlaylistProvider = MusicPlaylistProviderFactory[type]
        }
    }
}
