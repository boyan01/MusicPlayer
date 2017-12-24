package tech.summerly.quiet.commonlib.player

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.experimental.launch
import kotlinx.serialization.json.JSON
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.core.CoreMediaPlayer
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.player.state.PlayerState
import tech.summerly.quiet.commonlib.utils.WithDefaultLiveData
import tech.summerly.quiet.commonlib.utils.edit
import tech.summerly.quiet.commonlib.utils.log

@Suppress("MemberVisibilityCanPrivate")
abstract class BaseMusicPlayer(context: Context) {

    companion object {

        private val KEY_PLAY_LIST = "music_list"
        private val KEY_CURRENT_MUSIC = "music_current"
        private val KEY_PLAY_MODE = "play_mode"
//        private val KEY_POSITION = "position"
    }

    private val baseContext = context.applicationContext

    /**
     * this collection of play list
     */
    protected abstract val musicList: Collection<Music>

    /**
     * to control the player's play order
     */
    val playMode = WithDefaultLiveData(PlayMode.Sequence)

    /**
     * get the position of current playing music has been play.
     * unit: MS
     */
    val position: LiveData<Long>
        get() = corePlayer.getPosition()

    /**
     * get the state of current playing music: playing? pause? or loading.
     */
    val playerState: LiveData<PlayerState>
        get() = corePlayer.getPlayerState()

    protected val playingMusic = MutableLiveData<Music>()

    /**
     * get player current playing music
     */
    fun getPlayingMusic(): LiveData<Music> = playingMusic

    protected val corePlayer: CoreMediaPlayer = CoreMediaPlayer()

    /**
     * everything get ready , just to start play music
     */
    protected fun performPlay(music: Music) {
        if (playingMusic.value != music) {
            playingMusic.postValue(music)
        }
        corePlayer.play(music)
    }

    fun playNext() = launch {
        val next = getNext() ?: return@launch
        performPlay(next)
    }

    fun playPrevious() = launch {
        val previous = getPrevious() ?: return@launch
        performPlay(previous)
    }

    suspend fun getNext() = getNextMusic(playingMusic.value)

    suspend fun getPrevious() = getPreviousMusic(playingMusic.value)

    protected abstract suspend fun getNextMusic(current: Music?): Music?

    protected abstract suspend fun getPreviousMusic(current: Music?): Music?


    fun playPause() = launch {
        when (corePlayer.getPlayerState().value) {
            PlayerState.Pausing -> corePlayer.start()
            PlayerState.Playing -> corePlayer.pause()
            PlayerState.Loading -> Unit
            else -> {
                val shouldBePlay = playingMusic.value
                        ?: getNextMusic(null)
                shouldBePlay?.let(this@BaseMusicPlayer::performPlay)
                Unit
            }
        }
    }

    fun stop() {
        corePlayer.stop()
    }

    fun play(music: Music) {
        log { music.toShortString() }
        performPlay(music)
    }

    fun setPlaylist(musics: List<Music>) {
        if (musics.isEmpty()) {
            playingMusic.value = null
        }
    }

    fun seekToPosition(position: Long) {
        corePlayer.seekTo(position)
    }

    fun destroy() {
        val keeper = PlayerStateKeeper(baseContext)
        keeper.preference.edit {
            keeper.savePlayMode(playMode.value, editor = this)
            keeper.savePlaylist(musicList, playing = playingMusic.value, editor = this)
        }
    }


    protected inner class PlayerStateKeeper(context: Context) {


        val preference: SharedPreferences = context.getSharedPreferences("music_player_info", Context.MODE_PRIVATE)

        fun savePlaylist(
                musics: Collection<Music>,
                playing: Music? = null,
                editor: SharedPreferences.Editor = preference.edit()) {
            if (musics.isEmpty()) {
                return
            }
            saveCurrent(playing ?: musics.elementAtOrNull(0), editor)
            val listJson = JSON.stringify(musics)
            editor.putString(KEY_PLAY_LIST, listJson)
            editor.apply()
        }

        fun saveCurrent(playing: Music?,
                        editor: SharedPreferences.Editor = preference.edit()) {
            playing ?: return
            editor.putString(KEY_CURRENT_MUSIC, JSON.stringify(playing))
            editor.apply()
        }

        fun savePlayMode(playMode: PlayMode?,
                         editor: SharedPreferences.Editor = preference.edit()) {
            playMode ?: return
            editor.putString(KEY_PLAY_MODE, playMode.name)
            editor.apply()
        }


        fun restore() {
            playMode.postValue(PlayMode.fromName(preference.getString(KEY_PLAY_MODE, PlayMode.Sequence.name)))
            val saved = JSON.parse<Music>(preference.getString(KEY_CURRENT_MUSIC, ""))
            if (corePlayer.getPlayerState().value == PlayerState.Playing) {
                if (playingMusic.value != saved) {
                    corePlayer.stop()
                }
            }
            playingMusic.value = saved
            val list = JSON.parse<List<Music>>(preference.getString(KEY_PLAY_LIST, ""))
            setPlaylist(list)
        }

    }

}