package tech.soit.quiet.player

import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import tech.soit.quiet.AppContext
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager.musicPlayer
import tech.soit.quiet.player.MusicPlayerManager.play
import tech.soit.quiet.player.MusicPlayerManager.playerState
import tech.soit.quiet.player.MusicPlayerManager.playingMusic
import tech.soit.quiet.player.MusicPlayerManager.playlist
import tech.soit.quiet.player.MusicPlayerManager.position
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.player.playlist.Playlist
import tech.soit.quiet.ui.service.QuietPlayerService
import tech.soit.quiet.utils.component.persistence.KeyValue
import tech.soit.quiet.utils.component.persistence.get
import tech.soit.quiet.utils.component.support.liveDataWith


/**
 *
 * Global MusicPlayerManager
 *
 * provider [musicPlayer] to access [IMediaPlayer] , [Playlist]
 * and play action such as :
 * [QuietMusicPlayer.playNext],
 * [QuietMusicPlayer.playPrevious],
 * [QuietMusicPlayer.playPause]
 *
 * provider LiveData such as [playingMusic] [position] [playerState] [playlist]
 * to listen MusicPlayer' state
 *
 * provider [play] method for convenience to play music
 *
 */
object MusicPlayerManager {

    /**
     * keys use to save PlaylistData to Db
     *
     * [KEY_PLAYLIST_CURRENT] : current playing music
     * [KEY_PLAYLIST_MUSIC_LIST] : current playing music list
     * [KEY_PLAYLIST_TOKEN] : token to identify this music list
     * [KEY_PLAYLIST_PLAY_MODE] : [PlayMode]
     *
     */
    private const val KEY_PLAYLIST_MUSIC_LIST = "player_playlist_key_music_list"
    private const val KEY_PLAYLIST_TOKEN = "player_playlist_key_token"
    private const val KEY_PLAYLIST_CURRENT = "player_playlist_key_current"
    private const val KEY_PLAYLIST_PLAY_MODE = "play_playlist_key_play_mode"

    /**
     * music player, manage the playlist and [IMediaPlayer]
     *
     * ATTENTION: setter is only for TEST!!
     *
     */
    var musicPlayer = QuietMusicPlayer()

    /**
     * current playing music live data
     */
    val playingMusic = liveDataWith(musicPlayer.playlist.current)

    val position = MutableLiveData<Position>()

    /**
     * @see IMediaPlayer.PlayerState
     */
    val playerState = liveDataWith(IMediaPlayer.IDLE)

    init {
        musicPlayer.mediaPlayer.setOnStateChangeCallback {
            playerState.postValue(it)
        }
    }


    val playlist = MutableLiveData<Playlist>()

    /**
     * @param token [Playlist.token]
     * @param music the music which will be play
     * @param list the music from
     */
    fun play(token: String, music: Music, list: List<Music>) {
        val newPlaylist = Playlist(token, list)
        newPlaylist.current = music
        musicPlayer.playlist = newPlaylist
        musicPlayer.play(music)
    }

    init {
        //restore Playlist for MusicPlayer
        val token = KeyValue.get<String>(KEY_PLAYLIST_TOKEN)
        val musics: List<Music>? = KeyValue.get(KEY_PLAYLIST_MUSIC_LIST, object : TypeToken<List<Music>>() {}.type)
        if (token != null && musics != null) {
            val playMode = KeyValue.get<String>(KEY_PLAYLIST_PLAY_MODE)
            val current = KeyValue.get<Music>(KEY_PLAYLIST_CURRENT)
            val restore = Playlist(token, musics)
            restore.current = current
            restore.playMode = PlayMode.from(playMode)
            musicPlayer.playlist = restore
            playingMusic.postValue(current)
        }

        //persistence playlist
        playlist.observeForever { pl ->
            pl ?: return@observeForever
            GlobalScope.launch {
                KeyValue.put(KEY_PLAYLIST_TOKEN, pl.token)
                KeyValue.put(KEY_PLAYLIST_CURRENT, pl.current)
                KeyValue.put(KEY_PLAYLIST_PLAY_MODE, pl.playMode)
                KeyValue.put(KEY_PLAYLIST_MUSIC_LIST, pl.list)
            }
        }

        //persistence playing music
        playingMusic.observeForever { m ->
            m ?: return@observeForever
            KeyValue.put(KEY_PLAYLIST_CURRENT, m)
        }
        QuietPlayerService.init(AppContext)
    }


    /**
     * unit is Millisecond
     *
     * @param current the current playing postiion
     * @param total music total length
     */
    data class Position(val current: Long, val total: Long)

}